package com.bambuser.broadcaster;

import android.util.Log;
import com.bambuser.broadcaster.Connection;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
abstract class HttpUpgradeConnection extends Connection {
    private static final String LOGTAG = "HttpUpgradeConnection";
    private final Connection mDelegate;
    private final Connection.Observer mDelegateObserver;
    private volatile int mHttpHeaderLinePos;
    private volatile int mHttpHeaderLines;
    private final Map<String, String> mHttpHeaders;
    private volatile boolean mUseHttp;

    abstract void sendInitialRequest();

    abstract boolean verifyHttpResponseHeaders(Map<String, String> map);

    static /* synthetic */ int access$208(HttpUpgradeConnection httpUpgradeConnection) {
        int i = httpUpgradeConnection.mHttpHeaderLinePos;
        httpUpgradeConnection.mHttpHeaderLinePos = i + 1;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HttpUpgradeConnection(String str, int i, boolean z, Connection.Observer observer) {
        super(observer);
        this.mDelegateObserver = new Connection.Observer() { // from class: com.bambuser.broadcaster.HttpUpgradeConnection.1
            @Override // com.bambuser.broadcaster.Connection.Observer
            public void onConnected() {
                if (HttpUpgradeConnection.this.mDelegate.isOk()) {
                    HttpUpgradeConnection.this.sendInitialRequest();
                }
                synchronized (HttpUpgradeConnection.this) {
                    if (HttpUpgradeConnection.this.mObserver != null) {
                        HttpUpgradeConnection.this.mObserver.onConnected();
                    }
                }
            }

            @Override // com.bambuser.broadcaster.Connection.Observer
            public void onReceived(ByteBuffer byteBuffer) {
                if (HttpUpgradeConnection.this.mUseHttp) {
                    if (HttpUpgradeConnection.this.mErrorCode == ConnectionError.NONE) {
                        while (HttpUpgradeConnection.this.mHttpHeaderLinePos < byteBuffer.remaining() && HttpUpgradeConnection.this.mUseHttp) {
                            if (byteBuffer.get(byteBuffer.position() + HttpUpgradeConnection.this.mHttpHeaderLinePos) == 10) {
                                int i2 = HttpUpgradeConnection.this.mHttpHeaderLinePos;
                                HttpUpgradeConnection.access$208(HttpUpgradeConnection.this);
                                if (i2 > 0 && byteBuffer.get((byteBuffer.position() + i2) - 1) == 13) {
                                    i2--;
                                }
                                HttpUpgradeConnection.this.handleHttpHeader(MovinoUtils.getStringFromUTF8((ByteBuffer) byteBuffer.slice().limit(i2)));
                                byteBuffer.position(byteBuffer.position() + HttpUpgradeConnection.this.mHttpHeaderLinePos);
                                HttpUpgradeConnection.this.mHttpHeaderLinePos = 0;
                            } else {
                                HttpUpgradeConnection.access$208(HttpUpgradeConnection.this);
                            }
                        }
                        if (HttpUpgradeConnection.this.mUseHttp) {
                            return;
                        }
                    } else {
                        byteBuffer.position(byteBuffer.limit());
                        return;
                    }
                }
                synchronized (HttpUpgradeConnection.this) {
                    if (HttpUpgradeConnection.this.mObserver != null) {
                        HttpUpgradeConnection.this.mObserver.onReceived(byteBuffer);
                    }
                }
            }

            @Override // com.bambuser.broadcaster.Connection.Observer
            public void onConnectionClosed() {
                synchronized (HttpUpgradeConnection.this) {
                    if (HttpUpgradeConnection.this.mObserver != null) {
                        HttpUpgradeConnection.this.mObserver.onConnectionClosed();
                    }
                }
            }
        };
        this.mUseHttp = true;
        this.mHttpHeaderLinePos = 0;
        this.mHttpHeaderLines = 0;
        this.mHttpHeaders = new HashMap();
        this.mDelegate = z ? new SSLConnection(str, i, this.mDelegateObserver) : new TCPConnection(str, i, this.mDelegateObserver);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.Connection
    public final void send(RawPacket rawPacket) {
        if (isOk()) {
            this.mDelegate.send(rawPacket);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.Connection
    public final void connect() {
        this.mIsOk.set(true);
        this.mDelegate.connect();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.Connection
    public final void disconnect() {
        this.mDelegate.disconnect();
        synchronized (this) {
            super.disconnect();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.Connection
    public final boolean isOk() {
        return this.mIsOk.get() && this.mDelegate.isOk();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.Connection
    public final int getSendQueueSize() {
        return this.mDelegate.getSendQueueSize();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.Connection
    public final ConnectionError getError() {
        if (this.mErrorCode != ConnectionError.NONE) {
            return this.mErrorCode;
        }
        return this.mDelegate.mErrorCode;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleHttpHeader(String str) {
        if (this.mHttpHeaderLines == 0) {
            String[] split = str.split(" ");
            if (split.length < 3 || !split[1].equals("101")) {
                synchronized (this) {
                    if (this.mIsOk.compareAndSet(true, false)) {
                        Log.w(LOGTAG, "Bad http status from server");
                        this.mErrorCode = ConnectionError.BAD_SERVER;
                        if (this.mObserver != null) {
                            this.mObserver.onConnectionClosed();
                        }
                    }
                }
                return;
            }
        } else if (str.length() == 0) {
            if (!verifyHttpResponseHeaders(this.mHttpHeaders)) {
                synchronized (this) {
                    if (this.mIsOk.compareAndSet(true, false)) {
                        Log.w(LOGTAG, "Incorrect http response");
                        this.mErrorCode = ConnectionError.BAD_SERVER;
                        if (this.mObserver != null) {
                            this.mObserver.onConnectionClosed();
                        }
                    }
                }
                return;
            }
            this.mUseHttp = false;
            return;
        } else {
            int indexOf = str.indexOf(58);
            if (indexOf < 0) {
                synchronized (this) {
                    if (this.mIsOk.compareAndSet(true, false)) {
                        Log.w(LOGTAG, "Bad http header");
                        this.mErrorCode = ConnectionError.BAD_SERVER;
                        if (this.mObserver != null) {
                            this.mObserver.onConnectionClosed();
                        }
                    }
                }
                return;
            }
            String substring = str.substring(0, indexOf);
            int i = indexOf + 1;
            while (i < str.length() && str.charAt(i) == ' ') {
                i++;
            }
            this.mHttpHeaders.put(substring, str.substring(i));
        }
        this.mHttpHeaderLines++;
    }
}
