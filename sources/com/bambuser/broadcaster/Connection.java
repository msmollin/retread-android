package com.bambuser.broadcaster;

import com.bambuser.broadcaster.RawPacket;
import java.nio.ByteBuffer;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class Connection {
    Observer mObserver;
    final LinkedBlockingQueue<RawPacket> mOutQueue = new LinkedBlockingQueue<>();
    final AtomicInteger mQueueSize = new AtomicInteger();
    final AtomicBoolean mIsOk = new AtomicBoolean(false);
    ConnectionError mErrorCode = ConnectionError.NONE;

    /* loaded from: classes.dex */
    interface Observer {
        void onConnected();

        void onConnectionClosed();

        void onReceived(ByteBuffer byteBuffer);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void connect();

    /* JADX INFO: Access modifiers changed from: package-private */
    public Connection(Observer observer) {
        this.mObserver = observer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void send(RawPacket rawPacket) {
        if (!isOk() || rawPacket == null) {
            return;
        }
        if (rawPacket.useConnectionQueueSize()) {
            this.mQueueSize.addAndGet(rawPacket.size());
        }
        RawPacket.Observer observer = rawPacket.getObserver();
        if (observer != null) {
            observer.onBlockEnqueued();
        }
        this.mOutQueue.add(rawPacket);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void disconnect() {
        this.mObserver = null;
        this.mIsOk.set(false);
        this.mOutQueue.clear();
        this.mQueueSize.set(0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isOk() {
        return this.mIsOk.get();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getSendQueueSize() {
        return this.mQueueSize.get();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConnectionError getError() {
        return this.mErrorCode;
    }
}
