package com.google.android.gms.common.api.internal;

import android.os.Bundle;

/* loaded from: classes.dex */
final class zzcd implements Runnable {
    private final /* synthetic */ LifecycleCallback zzle;
    private final /* synthetic */ String zzlf;
    private final /* synthetic */ zzcc zzly;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzcd(zzcc zzccVar, LifecycleCallback lifecycleCallback, String str) {
        this.zzly = zzccVar;
        this.zzle = lifecycleCallback;
        this.zzlf = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        Bundle bundle;
        Bundle bundle2;
        Bundle bundle3;
        i = this.zzly.zzlc;
        if (i > 0) {
            LifecycleCallback lifecycleCallback = this.zzle;
            bundle = this.zzly.zzld;
            if (bundle != null) {
                bundle3 = this.zzly.zzld;
                bundle2 = bundle3.getBundle(this.zzlf);
            } else {
                bundle2 = null;
            }
            lifecycleCallback.onCreate(bundle2);
        }
        i2 = this.zzly.zzlc;
        if (i2 >= 2) {
            this.zzle.onStart();
        }
        i3 = this.zzly.zzlc;
        if (i3 >= 3) {
            this.zzle.onResume();
        }
        i4 = this.zzly.zzlc;
        if (i4 >= 4) {
            this.zzle.onStop();
        }
        i5 = this.zzly.zzlc;
        if (i5 >= 5) {
            this.zzle.onDestroy();
        }
    }
}
