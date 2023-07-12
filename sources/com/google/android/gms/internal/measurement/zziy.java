package com.google.android.gms.internal.measurement;

import android.content.ComponentName;

/* loaded from: classes.dex */
final class zziy implements Runnable {
    private final /* synthetic */ ComponentName val$name;
    private final /* synthetic */ zziw zzapn;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zziy(zziw zziwVar, ComponentName componentName) {
        this.zzapn = zziwVar;
        this.val$name = componentName;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzapn.zzape.onServiceDisconnected(this.val$name);
    }
}
