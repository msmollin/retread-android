package com.google.android.gms.tasks;

import java.util.concurrent.Callable;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzv implements Runnable {
    private final /* synthetic */ Callable val$callable;
    private final /* synthetic */ zzu zzagj;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzv(zzu zzuVar, Callable callable) {
        this.zzagj = zzuVar;
        this.val$callable = callable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            this.zzagj.setResult(this.val$callable.call());
        } catch (Exception e) {
            this.zzagj.setException(e);
        }
    }
}
