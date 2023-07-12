package com.bambuser.broadcaster;

import android.os.SystemClock;
import android.util.Log;
import com.bambuser.broadcaster.Connection;
import com.bambuser.broadcaster.RawPacket;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
final class TCPConnection extends Connection {
    private static final String LOGTAG = "TCPConnection";
    private SocketChannel mChannel;
    private Selector mConnectSelector;
    private final String mHostname;
    private final int mPort;
    private Selector mReadSelector;
    private Selector mWriteSelector;

    /* JADX INFO: Access modifiers changed from: package-private */
    public TCPConnection(String str, int i, Connection.Observer observer) {
        super(observer);
        this.mChannel = null;
        this.mReadSelector = null;
        this.mWriteSelector = null;
        this.mConnectSelector = null;
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
            this.mChannel = SocketChannel.open();
            this.mChannel.configureBlocking(false);
            Socket socket = this.mChannel.socket();
            socket.setSendBufferSize(65536);
            socket.setReceiveBufferSize(32768);
            try {
                this.mConnectSelector = Selector.open();
                this.mReadSelector = Selector.open();
                this.mWriteSelector = Selector.open();
                this.mChannel.register(this.mConnectSelector, 8);
                this.mChannel.register(this.mReadSelector, 1);
                this.mChannel.register(this.mWriteSelector, 4);
            } catch (IOException e) {
                Log.d(LOGTAG, "exception when setting up selectors: " + e);
                this.mErrorCode = ConnectionError.CONNECT_FAILED;
            }
        } catch (Exception e2) {
            Log.d(LOGTAG, "exception when configuring channel: " + e2);
            this.mErrorCode = ConnectionError.CONNECT_FAILED;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.Connection
    public synchronized void disconnect() {
        super.disconnect();
        final Selector selector = this.mConnectSelector;
        final Selector selector2 = this.mReadSelector;
        final Selector selector3 = this.mWriteSelector;
        final SocketChannel socketChannel = this.mChannel;
        new Thread(new Runnable() { // from class: com.bambuser.broadcaster.TCPConnection.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    if (selector != null) {
                        selector.close();
                    }
                } catch (Exception e) {
                    Log.d(TCPConnection.LOGTAG, "Exception when closing Selector: " + e);
                }
                try {
                    if (selector2 != null) {
                        selector2.close();
                    }
                } catch (Exception e2) {
                    Log.d(TCPConnection.LOGTAG, "Exception when closing Selector: " + e2);
                }
                try {
                    if (selector3 != null) {
                        selector3.close();
                    }
                } catch (Exception e3) {
                    Log.d(TCPConnection.LOGTAG, "Exception when closing Selector: " + e3);
                }
                try {
                    if (socketChannel != null) {
                        socketChannel.close();
                    }
                } catch (IOException e4) {
                    Log.d(TCPConnection.LOGTAG, "exception when closing SocketChannel: " + e4);
                }
            }
        }).start();
        this.mConnectSelector = null;
        this.mReadSelector = null;
        this.mWriteSelector = null;
        this.mChannel = null;
    }

    /* loaded from: classes.dex */
    private final class ConnectingThread extends Thread {
        private static final String INNER_LOGTAG = "ConnectingThread";

        ConnectingThread() {
            super(INNER_LOGTAG);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Selector selector;
            try {
                TCPConnection.this.mChannel.connect(new InetSocketAddress(TCPConnection.this.mHostname, TCPConnection.this.mPort));
                synchronized (TCPConnection.this) {
                    selector = TCPConnection.this.mConnectSelector;
                }
                boolean z = false;
                if (selector == null) {
                    TCPConnection.this.mErrorCode = ConnectionError.CONNECT_FAILED;
                    Log.d(INNER_LOGTAG, "connectSelector was null when connecting");
                } else {
                    while (!z) {
                        try {
                            if (selector.select() > 0) {
                                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                                it.remove();
                                try {
                                    z = ((SocketChannel) it.next().channel()).finishConnect();
                                } catch (Exception e) {
                                    Log.d(INNER_LOGTAG, "exception in finishConnect: " + e);
                                    TCPConnection.this.mErrorCode = ConnectionError.CONNECT_FAILED;
                                }
                            }
                        } catch (Exception e2) {
                            Log.d(INNER_LOGTAG, "exception during selection while connecting: " + e2);
                            TCPConnection.this.mErrorCode = ConnectionError.CONNECT_FAILED;
                        }
                    }
                }
                synchronized (TCPConnection.this) {
                    if (TCPConnection.this.mChannel != null && z) {
                        TCPConnection.this.mIsOk.set(true);
                        new SendingThread().start();
                        new ReceivingThread().start();
                    }
                    if (TCPConnection.this.mObserver != null) {
                        TCPConnection.this.mObserver.onConnected();
                    }
                }
            } catch (Exception e3) {
                TCPConnection.this.mErrorCode = ConnectionError.CONNECT_FAILED;
                Log.d(INNER_LOGTAG, "exception: " + e3);
                synchronized (TCPConnection.this) {
                    if (TCPConnection.this.mObserver != null) {
                        TCPConnection.this.mObserver.onConnected();
                    }
                }
            }
        }
    }

    /* loaded from: classes.dex */
    private final class SendingThread extends Thread {
        private static final String INNER_LOGTAG = "SendingThread";
        private static final long SEND_TIMEOUT_MS = 90000;

        SendingThread() {
            super(INNER_LOGTAG);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            loop0: while (true) {
                RawPacket rawPacket = null;
                ByteBuffer byteBuffer = null;
                while (TCPConnection.this.isOk()) {
                    if (rawPacket == null) {
                        try {
                            rawPacket = TCPConnection.this.mOutQueue.poll(250L, TimeUnit.MILLISECONDS);
                        } catch (InterruptedException unused) {
                        }
                        if (rawPacket == null) {
                            continue;
                        } else {
                            byteBuffer = rawPacket.getDataBuffer();
                            byteBuffer.flip();
                        }
                    }
                    Selector selector = TCPConnection.this.mWriteSelector;
                    if (selector == null) {
                        return;
                    }
                    try {
                        if (selector.select(1000L) <= 0) {
                            if (SystemClock.elapsedRealtime() - elapsedRealtime > SEND_TIMEOUT_MS) {
                                throw new IOException("Could not send any data for 90000 ms");
                                break loop0;
                            }
                        } else {
                            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                            it.remove();
                            int write = ((SocketChannel) it.next().channel()).write(byteBuffer);
                            if (write > 0) {
                                elapsedRealtime = SystemClock.elapsedRealtime();
                            }
                            if (rawPacket.useConnectionQueueSize()) {
                                TCPConnection.this.mQueueSize.addAndGet(-write);
                            }
                            if (!byteBuffer.hasRemaining()) {
                                RawPacket.Observer observer = rawPacket.getObserver();
                                synchronized (TCPConnection.this) {
                                    if (observer != null && TCPConnection.this.mObserver != null) {
                                        observer.onBlockSent();
                                    }
                                }
                                rawPacket.releaseToPool();
                            }
                        }
                    } catch (Exception e) {
                        Log.d(INNER_LOGTAG, "exception while sending: " + e);
                        synchronized (TCPConnection.this) {
                            if (TCPConnection.this.mIsOk.compareAndSet(true, false)) {
                                TCPConnection.this.mErrorCode = ConnectionError.SEND_FAILED;
                                if (TCPConnection.this.mObserver != null) {
                                    TCPConnection.this.mObserver.onConnectionClosed();
                                }
                            }
                        }
                    }
                }
                return;
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
            while (TCPConnection.this.isOk()) {
                ensureFreeBytes(1024);
                Selector selector = TCPConnection.this.mReadSelector;
                if (selector == null) {
                    return;
                }
                try {
                    if (selector.select() > 0) {
                        Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                        it.remove();
                        int read = ((SocketChannel) it.next().channel()).read(this.mByteBuffer);
                        if (read < 0) {
                            Log.d(INNER_LOGTAG, "read returned " + read);
                            synchronized (TCPConnection.this) {
                                if (TCPConnection.this.mIsOk.compareAndSet(true, false)) {
                                    TCPConnection.this.mErrorCode = ConnectionError.DISCONNECTED;
                                    if (TCPConnection.this.mObserver != null) {
                                        TCPConnection.this.mObserver.onConnectionClosed();
                                    }
                                }
                            }
                            return;
                        } else if (read != 0) {
                            this.mByteBuffer.flip();
                            while (this.mByteBuffer.hasRemaining()) {
                                int position = this.mByteBuffer.position();
                                synchronized (TCPConnection.this) {
                                    if (TCPConnection.this.mObserver != null) {
                                        TCPConnection.this.mObserver.onReceived(this.mByteBuffer);
                                    }
                                }
                                if (this.mByteBuffer.position() == position) {
                                    break;
                                }
                            }
                            this.mByteBuffer.compact();
                        }
                    }
                } catch (Exception e) {
                    Log.d(INNER_LOGTAG, "exception while receiving: " + e);
                    synchronized (TCPConnection.this) {
                        if (TCPConnection.this.mIsOk.compareAndSet(true, false)) {
                            TCPConnection.this.mErrorCode = ConnectionError.RECEIVE_FAILED;
                            if (TCPConnection.this.mObserver != null) {
                                TCPConnection.this.mObserver.onConnectionClosed();
                            }
                        }
                    }
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
