package com.bambuser.broadcaster;

import android.util.Log;
import com.bambuser.broadcaster.Connection;
import com.bambuser.broadcaster.RawPacket;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class FileConnection extends Connection {
    private final MovinoData mData;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FileConnection(MovinoData movinoData, Connection.Observer observer) {
        super(observer);
        this.mData = movinoData;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.Connection
    public synchronized void connect() {
        this.mIsOk.set(true);
        new WritingThread().start();
        if (this.mObserver != null) {
            this.mObserver.onConnected();
        }
        new ReceivingThread().start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.Connection
    public synchronized void disconnect() {
        super.disconnect();
        this.mData.lock();
        this.mData.setLength((System.currentTimeMillis() - this.mData.getTimestamp()) / 1000);
        this.mData.store();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MovinoData getData() {
        return this.mData;
    }

    /* loaded from: classes.dex */
    private final class WritingThread extends Thread {
        private static final String LOGTAG = "WritingThread";

        WritingThread() {
            super(LOGTAG);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (FileConnection.this.isOk()) {
                RawPacket rawPacket = null;
                try {
                    rawPacket = FileConnection.this.mOutQueue.poll(250L, TimeUnit.MILLISECONDS);
                } catch (InterruptedException unused) {
                }
                if (rawPacket != null) {
                    if (FileConnection.this.mData.write(rawPacket)) {
                        if (rawPacket.useConnectionQueueSize()) {
                            FileConnection.this.mQueueSize.addAndGet(-rawPacket.size());
                        }
                        RawPacket.Observer observer = rawPacket.getObserver();
                        synchronized (FileConnection.this) {
                            if (observer != null) {
                                try {
                                    if (FileConnection.this.mObserver != null) {
                                        observer.onBlockSent();
                                    }
                                } finally {
                                }
                            }
                        }
                        rawPacket.releaseToPool();
                    } else {
                        Log.w(LOGTAG, "could not write packet");
                        synchronized (FileConnection.this) {
                            if (FileConnection.this.mIsOk.compareAndSet(true, false)) {
                                FileConnection.this.mErrorCode = ConnectionError.WRITE_FAILED;
                                if (FileConnection.this.mObserver != null) {
                                    FileConnection.this.mObserver.onConnectionClosed();
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
        ReceivingThread() {
            super("ReceivingThread");
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            synchronized (FileConnection.this) {
                if (FileConnection.this.mObserver != null) {
                    FileConnection.this.mObserver.onReceived((ByteBuffer) new MovinoPacket(7).getDataBuffer().flip());
                }
            }
        }
    }
}
