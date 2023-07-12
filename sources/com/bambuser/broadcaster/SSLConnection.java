package com.bambuser.broadcaster;

import android.util.Log;
import com.bambuser.broadcaster.Connection;
import com.bambuser.broadcaster.RawPacket;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLSocket;

/* loaded from: classes.dex */
final class SSLConnection extends Connection {
    private static final String LOGTAG = "SSLConnection";
    private final String mHostname;
    private InputStream mInput;
    private OutputStream mOutput;
    private final int mPort;
    private SSLSocket mSocket;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SSLConnection(String str, int i, Connection.Observer observer) {
        super(observer);
        this.mSocket = null;
        this.mHostname = str;
        this.mPort = i;
        internalSetup();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.Connection
    public synchronized void connect() {
        if (this.mErrorCode == ConnectionError.NONE) {
            new ConnectingThread().start();
        } else if (this.mObserver != null) {
            this.mObserver.onConnected();
        }
    }

    private void internalSetup() {
        try {
            this.mSocket = (SSLSocket) ModernTlsSocketFactory.getInstance().createSocket();
            this.mSocket.setSendBufferSize(65536);
            this.mSocket.setReceiveBufferSize(32768);
        } catch (Exception e) {
            Log.w(LOGTAG, "exception when creating ssl socket: " + e);
            this.mErrorCode = ConnectionError.CONNECT_FAILED;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.Connection
    public synchronized void disconnect() {
        super.disconnect();
        final SSLSocket sSLSocket = this.mSocket;
        new Thread(new Runnable() { // from class: com.bambuser.broadcaster.SSLConnection.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    if (sSLSocket != null) {
                        sSLSocket.close();
                    }
                } catch (Exception e) {
                    Log.d(SSLConnection.LOGTAG, "exception when closing socket: " + e);
                }
            }
        }).start();
        this.mSocket = null;
    }

    /* loaded from: classes.dex */
    private final class ConnectingThread extends Thread {
        private static final String INNER_LOGTAG = "ConnectingThread";

        ConnectingThread() {
            super(INNER_LOGTAG);
        }

        /* JADX WARN: Removed duplicated region for block: B:20:0x00a2  */
        /* JADX WARN: Removed duplicated region for block: B:30:0x00bd  */
        @Override // java.lang.Thread, java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void run() {
            /*
                Method dump skipped, instructions count: 349
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.bambuser.broadcaster.SSLConnection.ConnectingThread.run():void");
        }
    }

    /* loaded from: classes.dex */
    private final class SendingThread extends Thread {
        private static final String INNER_LOGTAG = "SendingThread";

        SendingThread() {
            super(INNER_LOGTAG);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (SSLConnection.this.isOk()) {
                RawPacket rawPacket = null;
                try {
                    rawPacket = SSLConnection.this.mOutQueue.poll(250L, TimeUnit.MILLISECONDS);
                } catch (InterruptedException unused) {
                }
                if (rawPacket != null) {
                    ByteBuffer dataBuffer = rawPacket.getDataBuffer();
                    dataBuffer.flip();
                    try {
                        SSLConnection.this.mOutput.write(dataBuffer.array(), dataBuffer.position(), dataBuffer.remaining());
                        if (rawPacket.useConnectionQueueSize()) {
                            SSLConnection.this.mQueueSize.addAndGet(-dataBuffer.remaining());
                        }
                        RawPacket.Observer observer = rawPacket.getObserver();
                        synchronized (SSLConnection.this) {
                            if (observer != null && SSLConnection.this.mObserver != null) {
                                observer.onBlockSent();
                            }
                        }
                        rawPacket.releaseToPool();
                    } catch (Exception e) {
                        Log.d(INNER_LOGTAG, "exception while sending: " + e);
                        synchronized (SSLConnection.this) {
                            if (SSLConnection.this.mIsOk.compareAndSet(true, false)) {
                                SSLConnection.this.mErrorCode = ConnectionError.SEND_FAILED;
                                if (SSLConnection.this.mObserver != null) {
                                    SSLConnection.this.mObserver.onConnectionClosed();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /* loaded from: classes.dex */
    private final class ReceivingThread extends Thread {
        private static final String INNER_LOGTAG = "ReceivingThread";
        private ByteBuffer mByteBuffer;

        ReceivingThread() {
            super(INNER_LOGTAG);
            this.mByteBuffer = ByteBuffer.allocate(4096).order(ByteOrder.BIG_ENDIAN);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            int read;
            while (SSLConnection.this.isOk()) {
                ensureFreeBytes(1024);
                try {
                    read = SSLConnection.this.mInput.read(this.mByteBuffer.array(), this.mByteBuffer.position(), this.mByteBuffer.remaining());
                } catch (Exception e) {
                    Log.d(INNER_LOGTAG, "exception while receiving: " + e);
                    synchronized (SSLConnection.this) {
                        if (SSLConnection.this.mIsOk.compareAndSet(true, false)) {
                            SSLConnection.this.mErrorCode = ConnectionError.RECEIVE_FAILED;
                            if (SSLConnection.this.mObserver != null) {
                                SSLConnection.this.mObserver.onConnectionClosed();
                            }
                        }
                    }
                }
                if (read < 0) {
                    synchronized (SSLConnection.this) {
                        if (SSLConnection.this.mIsOk.compareAndSet(true, false)) {
                            SSLConnection.this.mErrorCode = ConnectionError.DISCONNECTED;
                            if (SSLConnection.this.mObserver != null) {
                                SSLConnection.this.mObserver.onConnectionClosed();
                            }
                        }
                    }
                    return;
                } else if (read != 0) {
                    this.mByteBuffer.position(this.mByteBuffer.position() + read);
                    this.mByteBuffer.flip();
                    while (this.mByteBuffer.hasRemaining()) {
                        int position = this.mByteBuffer.position();
                        synchronized (SSLConnection.this) {
                            if (SSLConnection.this.mObserver != null) {
                                SSLConnection.this.mObserver.onReceived(this.mByteBuffer);
                            }
                        }
                        if (this.mByteBuffer.position() == position) {
                            break;
                        }
                    }
                    this.mByteBuffer.compact();
                }
            }
        }

        private void ensureFreeBytes(int i) {
            if (this.mByteBuffer.remaining() < i) {
                int capacity = this.mByteBuffer.capacity();
                while (this.mByteBuffer.position() + i >= capacity) {
                    capacity *= 2;
                }
                this.mByteBuffer.flip();
                this.mByteBuffer = ByteBuffer.allocate(capacity).order(ByteOrder.BIG_ENDIAN).put(this.mByteBuffer);
            }
        }
    }
}
