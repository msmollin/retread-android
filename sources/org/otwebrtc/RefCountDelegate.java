package org.otwebrtc;

import androidx.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes2.dex */
class RefCountDelegate implements RefCounted {
    private final AtomicInteger refCount = new AtomicInteger(1);
    @Nullable
    private final Runnable releaseCallback;

    public RefCountDelegate(@Nullable Runnable runnable) {
        this.releaseCallback = runnable;
    }

    @Override // org.otwebrtc.RefCounted
    public void release() {
        Runnable runnable;
        int decrementAndGet = this.refCount.decrementAndGet();
        if (decrementAndGet < 0) {
            throw new IllegalStateException("release() called on an object with refcount < 1");
        }
        if (decrementAndGet != 0 || (runnable = this.releaseCallback) == null) {
            return;
        }
        runnable.run();
    }

    @Override // org.otwebrtc.RefCounted
    public void retain() {
        if (this.refCount.incrementAndGet() < 2) {
            throw new IllegalStateException("retain() called on an object with refcount < 1");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean safeRetain() {
        int i;
        do {
            i = this.refCount.get();
            if (i == 0) {
                return false;
            }
        } while (!this.refCount.weakCompareAndSet(i, i + 1));
        return true;
    }
}
