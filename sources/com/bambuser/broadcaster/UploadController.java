package com.bambuser.broadcaster;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.util.Pair;
import com.bambuser.broadcaster.MovinoConnectionHandler;
import com.bambuser.broadcaster.RawPacket;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class UploadController extends MovinoConnectionHandler implements RawPacket.Observer {
    private static final String LOGTAG = "UploadController";
    private static final int MAX_ENQUEUED_BLOCKS = 5;
    private static final int MSG_UPLOAD_DATA = 1;
    private static final int SEND_BUFFER_SIZE = 65536;
    private static final String WAKELOCKTAG = "bambuser:UploadController";
    private final Context mContext;
    private volatile boolean mDataToSend;
    private final AtomicInteger mEnqueuedBlocks;
    private final Handler mHandler;
    private final AtomicReference<MovinoData> mMovinoData;
    private volatile Observer mObserver;
    private final PowerManager mPowerManager;
    private final byte[] mSendBuffer;
    private final AtomicReference<PowerManager.WakeLock> mUploadingWakeLock;
    private volatile boolean mWaitingForTicketResponse;
    private final AtomicReference<WifiManager.WifiLock> mWifiLock;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface Observer {
        void onBroadcastTicketResponse(JSONObject jSONObject);

        void onConnectionError(ConnectionError connectionError, String str);

        void onConnectionStatusChange(UploadStatus uploadStatus);

        void onDisconnecting();

        String onGetClientVersion();

        void onReadyForUploadStart();

        void onSentBytes(int i);

        void onStartPosDiff(long j);

        void onUploadFailed(MovinoData movinoData, UploadError uploadError);

        void onUploadFinished(MovinoData movinoData);

        void showConnectionDialog(MovinoConnectionHandler.Dialog dialog);
    }

    UploadController(Context context, SettingsReader settingsReader) {
        super(settingsReader);
        this.mUploadingWakeLock = new AtomicReference<>();
        this.mWifiLock = new AtomicReference<>();
        this.mObserver = null;
        this.mMovinoData = new AtomicReference<>();
        this.mDataToSend = false;
        this.mWaitingForTicketResponse = false;
        this.mEnqueuedBlocks = new AtomicInteger();
        this.mContext = context;
        this.mPowerManager = (PowerManager) context.getSystemService("power");
        this.mSendBuffer = new byte[65536];
        HandlerThread handlerThread = new HandlerThread("UploadingThread");
        handlerThread.start();
        this.mHandler = new Handler(handlerThread.getLooper(), new Handler.Callback() { // from class: com.bambuser.broadcaster.UploadController.1
            @Override // android.os.Handler.Callback
            public final boolean handleMessage(Message message) {
                if (message.what == 1) {
                    if (UploadController.this.shouldSend()) {
                        UploadController.this.sendChunk();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    void setObserver(Observer observer) {
        this.mObserver = observer;
    }

    void destroy() {
        this.mIgnoreConnectionErrors = true;
        disconnect();
        this.mHandler.getLooper().quit();
        this.mByteBufferPool.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldSend() {
        return this.mDataToSend && isConnectionReady() && this.mEnqueuedBlocks.get() < 5;
    }

    private void uploadFailed(UploadError uploadError) {
        MovinoData movinoData = this.mMovinoData.get();
        disconnect();
        Observer observer = this.mObserver;
        if (observer != null) {
            observer.onUploadFailed(movinoData, uploadError);
        }
    }

    void startConnecting(String str) {
        if (this.mConnection.get() != null) {
            Log.w(LOGTAG, "already connected, ignoring duplicate startConnecting");
        } else if (this.mWaitingForTicketResponse) {
            Log.i(LOGTAG, "ticket request pending, ignoring duplicate startConnecting");
        } else {
            this.mPredefinedUsername = str;
            Observer observer = this.mObserver;
            if (observer != null) {
                observer.onConnectionStatusChange(UploadStatus.CONNECTING);
            }
            if (DeviceInfoHandler.hasPermission(this.mContext, "android.permission.WAKE_LOCK")) {
                PowerManager.WakeLock newWakeLock = this.mPowerManager.newWakeLock(1, WAKELOCKTAG);
                PowerManager.WakeLock andSet = this.mUploadingWakeLock.getAndSet(newWakeLock);
                if (andSet != null && andSet.isHeld()) {
                    andSet.release();
                }
                newWakeLock.acquire();
                if (DeviceInfoHandler.isNetworkType(this.mContext, 1)) {
                    WifiManager.WifiLock wifiLock = DeviceInfoHandler.getWifiLock(this.mContext);
                    wifiLock.acquire();
                    this.mWifiLock.set(wifiLock);
                }
            }
            if (this.mSettings.getString("application_id").isEmpty()) {
                connectMovino();
            } else {
                fetchBroadcastTicket();
            }
        }
    }

    boolean canConnect() {
        return !this.mWaitingForTicketResponse && this.mConnection.get() == null;
    }

    /* JADX WARN: Type inference failed for: r1v7, types: [com.bambuser.broadcaster.UploadController$2] */
    void fetchBroadcastTicket() {
        if (this.mSettings.getBoolean("skip_broadcast_ticket")) {
            connectMovino();
            return;
        }
        String string = this.mSettings.getString("application_id");
        Observer observer = this.mObserver;
        if (observer == null || this.mWaitingForTicketResponse || string.isEmpty()) {
            return;
        }
        final HashMap hashMap = new HashMap();
        hashMap.put("Accept", "application/vnd.bambuser.cdn.v1+json");
        hashMap.put("X-Bambuser-ClientVersion", observer.onGetClientVersion());
        hashMap.put("X-Bambuser-ClientPlatform", "Android " + Build.VERSION.RELEASE);
        hashMap.put("X-Bambuser-ApplicationId", string);
        final JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("httpUpgradeCapable", true);
            jSONObject.put("tlsCapable", true);
        } catch (Exception unused) {
        }
        this.mWaitingForTicketResponse = true;
        new AsyncTask<Void, Void, Pair<Integer, JSONObject>>() { // from class: com.bambuser.broadcaster.UploadController.2
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Pair<Integer, JSONObject> doInBackground(Void... voidArr) {
                Pair<Integer, String> jsonEncodedRequest = BackendApi.jsonEncodedRequest("https://cdn.bambuser.net/broadcastTickets", jSONObject, "POST", hashMap);
                try {
                    return new Pair<>(jsonEncodedRequest.first, new JSONObject((String) jsonEncodedRequest.second));
                } catch (Exception unused2) {
                    return new Pair<>(jsonEncodedRequest.first, null);
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(Pair<Integer, JSONObject> pair) {
                if (UploadController.this.mWaitingForTicketResponse) {
                    int intValue = ((Integer) pair.first).intValue();
                    JSONObject jSONObject2 = (JSONObject) pair.second;
                    if (intValue == 403) {
                        UploadController.this.mIgnoreConnectionErrors = true;
                        UploadController.this.abortConnection();
                        UploadController.this.onConnectionError(ConnectionError.BAD_CREDENTIALS, "Invalid application id");
                    } else if (intValue == 0) {
                        UploadController.this.mIgnoreConnectionErrors = true;
                        UploadController.this.abortConnection();
                        UploadController.this.onConnectionError(ConnectionError.CONNECT_FAILED, "Could not connect to login server");
                    } else if (jSONObject2 != null && intValue >= 200 && intValue < 400) {
                        Observer observer2 = UploadController.this.mObserver;
                        if (observer2 != null) {
                            observer2.onBroadcastTicketResponse(jSONObject2);
                        }
                        if (UploadController.this.mWaitingForTicketResponse) {
                            UploadController.this.mWaitingForTicketResponse = false;
                            UploadController.this.connectMovino();
                        }
                    } else {
                        UploadController.this.mIgnoreConnectionErrors = true;
                        UploadController.this.abortConnection();
                        UploadController uploadController = UploadController.this;
                        ConnectionError connectionError = ConnectionError.CONNECT_FAILED;
                        uploadController.onConnectionError(connectionError, "Unexpected response from login server, code " + intValue);
                    }
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    void startUpload(MovinoData movinoData) {
        this.mDataToSend = false;
        this.mMovinoData.set(movinoData);
        if (movinoData == null) {
            uploadFailed(UploadError.DATA_NOT_FOUND);
            return;
        }
        String str = this.mSessionUsername;
        if (movinoData.getUploadType() == 1 && movinoData.getUsername().equals("")) {
            movinoData.setUsername(str);
        } else if (str.length() > 0 && !movinoData.getUsername().equals(str)) {
            uploadFailed(UploadError.USERNAME_MISMATCH);
            return;
        }
        Connection readyConnection = getReadyConnection();
        if (readyConnection != null) {
            readyConnection.send(MovinoUtils.createUploadStartPacket(movinoData));
        }
    }

    @Override // com.bambuser.broadcaster.MovinoConnectionHandler
    void abortConnection() {
        Log.w(LOGTAG, "unexpected disconnection");
        if (!this.mIgnoreConnectionErrors) {
            this.mIgnoreConnectionErrors = true;
            Connection connection = this.mConnection.get();
            onConnectionError(connection != null ? connection.getError() : ConnectionError.DISCONNECTED, null);
        }
        uploadFailed(UploadError.CONNECTION_ERROR);
    }

    void disconnect() {
        MovinoData andSet;
        this.mWaitingForTicketResponse = false;
        this.mDataToSend = false;
        synchronized (this) {
            andSet = this.mMovinoData.getAndSet(null);
        }
        boolean z = (this.mConnection.get() == null && andSet == null) ? false : true;
        Observer observer = this.mObserver;
        if (observer != null && z) {
            observer.onConnectionStatusChange(UploadStatus.DISCONNECTING);
        }
        if (andSet != null) {
            andSet.store();
        }
        if (observer != null && z) {
            observer.onDisconnecting();
        }
        closeConnection();
        this.mEnqueuedBlocks.set(0);
        if (observer != null) {
            observer.onConnectionStatusChange(UploadStatus.IDLE);
        }
        WifiManager.WifiLock andSet2 = this.mWifiLock.getAndSet(null);
        if (andSet2 != null && andSet2.isHeld()) {
            andSet2.release();
        }
        PowerManager.WakeLock andSet3 = this.mUploadingWakeLock.getAndSet(null);
        if (andSet3 == null || !andSet3.isHeld()) {
            return;
        }
        andSet3.release();
    }

    @Override // com.bambuser.broadcaster.MovinoConnectionHandler
    void handshakeDone() {
        Observer observer = this.mObserver;
        if (observer != null) {
            observer.onConnectionStatusChange(UploadStatus.UPLOADING);
            observer.onReadyForUploadStart();
        }
    }

    @Override // com.bambuser.broadcaster.MovinoConnectionHandler
    void uploadRejected() {
        uploadFailed(UploadError.UPLOAD_REJECTED);
    }

    @Override // com.bambuser.broadcaster.MovinoConnectionHandler
    void uploadStartPos(long j) {
        MovinoData movinoData = this.mMovinoData.get();
        if (movinoData == null) {
            uploadFailed(UploadError.DATA_NOT_FOUND);
            return;
        }
        long uploadedBytes = movinoData.getUploadedBytes();
        Observer observer = this.mObserver;
        if (observer != null && uploadedBytes != j) {
            observer.onStartPosDiff(j - uploadedBytes);
        }
        movinoData.setUploadedBytes(j);
        movinoData.setUploadStarted();
        movinoData.store();
        try {
            movinoData.setInputPosition(j);
            this.mDataToSend = true;
            for (int i = 0; i < 5; i++) {
                this.mHandler.sendEmptyMessage(1);
            }
        } catch (IOException e) {
            Log.w(LOGTAG, "could not skip" + j + " bytes in movinodata file, exception: " + e);
            uploadFailed(UploadError.DATA_READING_FAILED);
        }
    }

    @Override // com.bambuser.broadcaster.MovinoConnectionHandler
    void uploadFinished() {
        MovinoData andSet = this.mMovinoData.getAndSet(null);
        if (andSet != null) {
            andSet.delete();
        }
        Observer observer = this.mObserver;
        if (observer != null) {
            observer.onUploadFinished(andSet);
        }
    }

    @Override // com.bambuser.broadcaster.MovinoConnectionHandler
    void onConnectionError(ConnectionError connectionError, String str) {
        Observer observer = this.mObserver;
        if (observer != null) {
            observer.onConnectionError(connectionError, str);
        }
    }

    @Override // com.bambuser.broadcaster.MovinoConnectionHandler
    void showConnectionDialog(MovinoConnectionHandler.Dialog dialog) {
        Observer observer = this.mObserver;
        if (observer != null) {
            observer.showConnectionDialog(dialog);
        } else {
            disconnect();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void sendChunk() {
        MovinoData movinoData = this.mMovinoData.get();
        if (movinoData == null) {
            uploadFailed(UploadError.DATA_NOT_FOUND);
            return;
        }
        BufferedInputStream inputStream = movinoData.getInputStream();
        if (inputStream == null) {
            uploadFailed(UploadError.DATA_READING_FAILED);
            return;
        }
        try {
            int read = inputStream.read(this.mSendBuffer);
            if (read < this.mSendBuffer.length) {
                this.mDataToSend = false;
            }
            if (read < 0) {
                read = 0;
            }
            Connection readyConnection = getReadyConnection();
            if (readyConnection != null) {
                readyConnection.send(new MovinoPacket(this.mByteBufferPool, 27, read, this).write(this.mSendBuffer, 0, read));
            }
            movinoData.incrementUploadedBytes(read);
            Observer observer = this.mObserver;
            if (observer != null) {
                observer.onSentBytes(read);
            }
        } catch (IOException e) {
            Log.w(LOGTAG, "could not read data from movinodata file, exception: " + e);
            uploadFailed(UploadError.DATA_READING_FAILED);
        }
    }

    @Override // com.bambuser.broadcaster.RawPacket.Observer
    public void onBlockEnqueued() {
        this.mEnqueuedBlocks.incrementAndGet();
    }

    @Override // com.bambuser.broadcaster.RawPacket.Observer
    public void onBlockSent() {
        this.mEnqueuedBlocks.decrementAndGet();
        if (isConnectionReady()) {
            this.mHandler.sendEmptyMessage(1);
        }
    }

    MovinoData getData() {
        return this.mMovinoData.get();
    }
}
