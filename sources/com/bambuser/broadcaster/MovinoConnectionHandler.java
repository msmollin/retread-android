package com.bambuser.broadcaster;

import android.os.SystemClock;
import android.util.Log;
import com.bambuser.broadcaster.Connection;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class MovinoConnectionHandler implements UsernameProvider, Connection.Observer {
    private static final String LOGTAG = "MovinoConnectionHandler";
    private static final int MAX_SANE_PAYLOAD_SIZE = 100000;
    private volatile long mHandshakeMonotonicTime;
    final SettingsReader mSettings;
    private volatile long mSyncedTimeOffsetFromMonotonic;
    final ByteBufferPool mByteBufferPool = new ByteBufferPool();
    final AtomicReference<Connection> mConnection = new AtomicReference<>();
    private volatile int mSyncedTimeUncertainty = -1;
    private final AtomicBoolean mHandshakeDone = new AtomicBoolean(false);
    private final AtomicReference<ChallengeHandler> mChallengeHandler = new AtomicReference<>();
    volatile String mPredefinedUsername = null;
    volatile String mSessionUsername = "";
    volatile boolean mIgnoreConnectionErrors = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public enum Dialog {
        PASSWORD,
        PASSWORD_FOR_USERNAME,
        USERNAME_PASSWORD
    }

    BroadcastElement broadcastInfo(ByteBuffer byteBuffer) {
        return null;
    }

    void currentViewersUpdated(long j) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getEnabledTalkbackType() {
        return 0;
    }

    boolean handleNetworkDisconnected() {
        return false;
    }

    abstract void handshakeDone();

    void onAudioData(int i, int i2, ByteBuffer byteBuffer) {
    }

    void onAudioFormat(int i, int i2) {
    }

    abstract void onConnectionError(ConnectionError connectionError, String str);

    void onDisableReconnect() {
    }

    void onReconnectAccepted() {
    }

    void onReconnectFailed() {
    }

    void onReconnectInfo(String str, String str2, int i, boolean z) {
    }

    void onTalkbackRequest(int i, String str, String str2, String str3) {
    }

    abstract void showConnectionDialog(Dialog dialog);

    void totalViewersUpdated(long j) {
    }

    void uploadFinished() {
    }

    void uploadRejected() {
    }

    void uploadStartPos(long j) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MovinoConnectionHandler(SettingsReader settingsReader) {
        this.mSettings = settingsReader;
    }

    @Override // com.bambuser.broadcaster.Connection.Observer
    public final void onConnected() {
        Connection connection = this.mConnection.get();
        if (connection == null || !connection.isOk()) {
            Log.w(LOGTAG, "onConnected called but connection not successful");
            if (handleNetworkDisconnected()) {
                return;
            }
            abortConnection();
        }
    }

    @Override // com.bambuser.broadcaster.Connection.Observer
    public final void onConnectionClosed() {
        if (Log.isLoggable(LOGTAG, 3)) {
            Log.d(LOGTAG, "connectionClosed callback");
        }
        if (handleNetworkDisconnected()) {
            return;
        }
        abortConnection();
    }

    @Override // com.bambuser.broadcaster.UsernameProvider
    public String getUsername() {
        String str = this.mPredefinedUsername;
        return str != null ? str : this.mSessionUsername;
    }

    private void badServer() {
        this.mIgnoreConnectionErrors = true;
        abortConnection();
        onConnectionError(ConnectionError.BAD_SERVER, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void connectMovino() {
        Connection tCPConnection;
        String string = this.mSettings.getString("hostname");
        int i = this.mSettings.getInt("port");
        boolean z = this.mSettings.getBoolean("movino_http_upgrade");
        boolean z2 = this.mSettings.getBoolean("movino_tls");
        if (z) {
            tCPConnection = new MovinoUpgradeConnection(string, i, z2, this);
        } else if (z2) {
            tCPConnection = new SSLConnection(string, i, this);
        } else {
            tCPConnection = new TCPConnection(string, i, this);
        }
        this.mConnection.set(tCPConnection);
        tCPConnection.connect();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void closeConnection() {
        ConnectionError error;
        Connection andSet = this.mConnection.getAndSet(null);
        if (andSet != null && !andSet.isOk() && !this.mIgnoreConnectionErrors && (error = andSet.getError()) != ConnectionError.NONE) {
            onConnectionError(error, null);
        }
        if (andSet != null) {
            andSet.disconnect();
        }
        XMLUtils.setBroadcastInfo(null);
        this.mHandshakeDone.set(false);
        this.mChallengeHandler.set(null);
        this.mSessionUsername = "";
        this.mPredefinedUsername = null;
        this.mIgnoreConnectionErrors = false;
    }

    @Override // com.bambuser.broadcaster.Connection.Observer
    public final void onReceived(ByteBuffer byteBuffer) {
        if (byteBuffer.remaining() < 5) {
            return;
        }
        byteBuffer.mark();
        int i = byteBuffer.get() & 255;
        int i2 = byteBuffer.getInt();
        if (i2 < 0 || i2 > 100000) {
            badServer();
            byteBuffer.reset();
        } else if (byteBuffer.remaining() < i2) {
            byteBuffer.reset();
        } else {
            handlePacket((ByteBuffer) byteBuffer.slice().limit(i2), i);
            byteBuffer.reset().position(byteBuffer.position() + 5 + i2);
        }
    }

    private void handlePacket(ByteBuffer byteBuffer, int i) {
        long j;
        if (i == 2) {
            if (byteBuffer.remaining() < 2) {
                badServer();
                return;
            }
            if (Log.isLoggable(LOGTAG, 3)) {
                Log.d(LOGTAG, "Got handshake");
            }
            int i2 = byteBuffer.get() & 255;
            int i3 = byteBuffer.get() & 255;
            if (i3 == 2) {
                this.mChallengeHandler.set(new ChallengeHandler(i2, i3, byteBuffer));
                getCredentials(i2);
            } else if (byteBuffer.remaining() > 0) {
                badServer();
            } else {
                Connection connection = this.mConnection.get();
                if (connection == null || !connection.isOk()) {
                    return;
                }
                this.mHandshakeMonotonicTime = SystemClock.elapsedRealtime();
                MovinoPacket movinoPacket = new MovinoPacket(14, 2);
                movinoPacket.writeUint8(1).writeUint8(1);
                connection.send(movinoPacket);
                connection.send(MovinoUtils.createCapabilitiesPacket(100000, getEnabledTalkbackType()));
            }
        } else if (i == 4) {
            if (byteBuffer.remaining() < 4) {
                badServer();
                return;
            }
            int i4 = byteBuffer.getInt();
            String stringFromUTF8 = MovinoUtils.getStringFromUTF8(byteBuffer);
            switch (i4) {
                case 0:
                    handleMessage(stringFromUTF8);
                    return;
                case 1:
                    if (!this.mIgnoreConnectionErrors) {
                        onConnectionError(ConnectionError.BAD_CREDENTIALS, null);
                        this.mSettings.setCredentials("", "");
                    }
                    this.mIgnoreConnectionErrors = true;
                    onDisableReconnect();
                    return;
                case 2:
                    if (!this.mIgnoreConnectionErrors) {
                        onConnectionError(ConnectionError.BAD_CLIENT, stringFromUTF8);
                    }
                    this.mIgnoreConnectionErrors = true;
                    return;
                case 3:
                    if (!this.mIgnoreConnectionErrors) {
                        onConnectionError(ConnectionError.SERVER_FULL, stringFromUTF8);
                    }
                    this.mIgnoreConnectionErrors = true;
                    return;
                case 4:
                case 6:
                    if (!this.mIgnoreConnectionErrors) {
                        onConnectionError(ConnectionError.SERVER_MESSAGE, stringFromUTF8);
                    }
                    this.mIgnoreConnectionErrors = true;
                    onDisableReconnect();
                    return;
                case 5:
                    if (!this.mIgnoreConnectionErrors) {
                        onConnectionError(ConnectionError.RECONNECT_FAILED, stringFromUTF8);
                    }
                    this.mIgnoreConnectionErrors = true;
                    onReconnectFailed();
                    return;
                default:
                    onConnectionError(ConnectionError.SERVER_MESSAGE, stringFromUTF8);
                    return;
            }
        } else if (i == 3) {
            Connection readyConnection = getReadyConnection();
            if (readyConnection != null) {
                readyConnection.send(new MovinoPacket(0));
            }
        } else if (i == 5) {
            if (byteBuffer.remaining() < 8) {
                badServer();
                return;
            }
            long j2 = byteBuffer.getLong();
            if (j2 == -1) {
                uploadRejected();
            } else {
                uploadStartPos(j2);
            }
        } else if (i == 6) {
            uploadFinished();
        } else if (i == 7) {
            this.mHandshakeDone.set(true);
            if (byteBuffer.remaining() >= 12 && this.mHandshakeMonotonicTime > 0) {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                long j3 = elapsedRealtime - this.mHandshakeMonotonicTime;
                long j4 = byteBuffer.getLong();
                int i5 = byteBuffer.getInt();
                long j5 = i5;
                if (j5 > j3) {
                    Log.w(LOGTAG, "delay " + i5 + " larger than measured rtt " + j3 + ", sync rejected!");
                } else {
                    long j6 = j3 - j5;
                    this.mSyncedTimeOffsetFromMonotonic = (j4 + (j6 / 2)) - elapsedRealtime;
                    this.mSyncedTimeUncertainty = ((int) j6) / 2;
                    Log.i(LOGTAG, "system time is off by " + (System.currentTimeMillis() - j) + "ms, uncertainty: " + this.mSyncedTimeUncertainty + "ms");
                }
            }
            handshakeDone();
        } else if (i == 8) {
            if (byteBuffer.remaining() < 9) {
                XMLUtils.setBroadcastInfo(null);
                return;
            }
            int i6 = byteBuffer.getInt();
            BroadcastElement broadcastInfo = broadcastInfo(byteBuffer);
            if (broadcastInfo != null) {
                broadcastInfo.setTag(i6);
            }
            XMLUtils.setBroadcastInfo(broadcastInfo);
        } else if (i == 9) {
            if (byteBuffer.remaining() >= 8) {
                long j7 = byteBuffer.getInt() & Movino.ONES_32;
                long j8 = byteBuffer.getInt() & Movino.ONES_32;
                if (j7 != Movino.ONES_32) {
                    currentViewersUpdated(j7);
                }
                if (j8 != Movino.ONES_32) {
                    totalViewersUpdated(j8);
                }
            }
        } else if (i == 10) {
            if (byteBuffer.remaining() >= 6) {
                int i7 = byteBuffer.getInt();
                String stringFromLengthUTF8 = MovinoUtils.getStringFromLengthUTF8(byteBuffer);
                String stringFromLengthUTF82 = MovinoUtils.getStringFromLengthUTF8(byteBuffer);
                String stringFromLengthUTF83 = MovinoUtils.getStringFromLengthUTF8(byteBuffer);
                if ((getEnabledTalkbackType() & i7) != 0) {
                    onTalkbackRequest(i7, stringFromLengthUTF8, stringFromLengthUTF82, stringFromLengthUTF83);
                    return;
                }
                Log.w(LOGTAG, "Unexpected talkback type: " + i7 + ", was our capabilities packet correct?");
                Connection readyConnection2 = getReadyConnection();
                if (readyConnection2 != null) {
                    readyConnection2.send(MovinoUtils.createTalkbackStatus(1, i7, stringFromLengthUTF8));
                }
            }
        } else if (i == 11) {
            if (byteBuffer.remaining() >= 12) {
                byteBuffer.getInt();
                onAudioFormat(byteBuffer.getInt(), byteBuffer.getInt());
            }
        } else if (i == 13 || i == 12) {
            if (byteBuffer.remaining() >= 4) {
                onAudioData(i, byteBuffer.getInt(), byteBuffer);
            }
        } else if (i != 14) {
            if (i == 15) {
                onReconnectAccepted();
            }
        } else if (byteBuffer.remaining() >= 9) {
            String stringFromLengthUTF84 = MovinoUtils.getStringFromLengthUTF8(byteBuffer);
            String stringFromLengthUTF85 = MovinoUtils.getStringFromLengthUTF8(byteBuffer);
            if (byteBuffer.remaining() >= 5) {
                onReconnectInfo(stringFromLengthUTF84, stringFromLengthUTF85, byteBuffer.getInt(), byteBuffer.get() != 0);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Connection getReadyConnection() {
        Connection connection = this.mConnection.get();
        if (connection != null && connection.isOk() && this.mHandshakeDone.get()) {
            return connection;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean isConnectionReady() {
        Connection connection = this.mConnection.get();
        return connection != null && connection.isOk() && this.mHandshakeDone.get();
    }

    private void getCredentials(int i) {
        String string = this.mSettings.getString("authorization", "remember_both");
        String string2 = this.mSettings.getString("username");
        String string3 = this.mSettings.getString("password");
        String str = this.mPredefinedUsername;
        boolean z = string.equals("remember_both") && string3.length() > 0;
        if (i == 1) {
            if (z) {
                respondToChallenge("", string3);
            } else {
                showConnectionDialog(Dialog.PASSWORD);
            }
        } else if (str != null && str.length() > 0) {
            if (z && string2.equals(str)) {
                respondToChallenge(string2, string3);
            } else {
                showConnectionDialog(Dialog.PASSWORD_FOR_USERNAME);
            }
        } else if (z && string2.length() > 0) {
            respondToChallenge(string2, string3);
        } else {
            showConnectionDialog(Dialog.USERNAME_PASSWORD);
        }
    }

    final void respondToChallenge(String str, String str2) {
        ChallengeHandler andSet = this.mChallengeHandler.getAndSet(null);
        if (andSet == null) {
            Log.e(LOGTAG, "no challengehandler available (is null)");
            abortConnection();
            return;
        }
        MovinoPacket createHandshake = andSet.createHandshake(str, str2);
        if (createHandshake == null) {
            Log.e(LOGTAG, "couldn't create handshake (is null)");
            abortConnection();
            return;
        }
        Connection connection = this.mConnection.get();
        if (connection == null || !connection.isOk()) {
            return;
        }
        this.mHandshakeMonotonicTime = SystemClock.elapsedRealtime();
        this.mSessionUsername = str;
        connection.send(createHandshake);
        connection.send(MovinoUtils.createCapabilitiesPacket(100000, getEnabledTalkbackType()));
    }

    void abortConnection() {
        Log.i(LOGTAG, "aborting connection");
        closeConnection();
    }

    void handleMessage(String str) {
        Log.w(LOGTAG, "handleMessage not overridden, incoming message not handled");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final long getSyncedRealtime() {
        if (this.mSyncedTimeUncertainty >= 0) {
            return SystemClock.elapsedRealtime() + this.mSyncedTimeOffsetFromMonotonic;
        }
        return System.currentTimeMillis();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int getSyncedRealtimeUncertainty() {
        return this.mSyncedTimeUncertainty;
    }
}
