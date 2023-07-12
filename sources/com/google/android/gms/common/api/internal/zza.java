package com.google.android.gms.common.api.internal;

import android.app.Activity;
import androidx.annotation.MainThread;
import androidx.annotation.VisibleForTesting;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class zza extends ActivityLifecycleObserver {
    private final WeakReference<C0015zza> zzds;

    @VisibleForTesting(otherwise = 2)
    /* renamed from: com.google.android.gms.common.api.internal.zza$zza  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    static class C0015zza extends LifecycleCallback {
        private List<Runnable> zzdt;

        private C0015zza(LifecycleFragment lifecycleFragment) {
            super(lifecycleFragment);
            this.zzdt = new ArrayList();
            this.mLifecycleFragment.addCallback("LifecycleObserverOnStop", this);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static C0015zza zza(Activity activity) {
            C0015zza c0015zza;
            synchronized (activity) {
                LifecycleFragment fragment = getFragment(activity);
                c0015zza = (C0015zza) fragment.getCallbackOrNull("LifecycleObserverOnStop", C0015zza.class);
                if (c0015zza == null) {
                    c0015zza = new C0015zza(fragment);
                }
            }
            return c0015zza;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final synchronized void zza(Runnable runnable) {
            this.zzdt.add(runnable);
        }

        @Override // com.google.android.gms.common.api.internal.LifecycleCallback
        @MainThread
        public void onStop() {
            List<Runnable> list;
            synchronized (this) {
                list = this.zzdt;
                this.zzdt = new ArrayList();
            }
            for (Runnable runnable : list) {
                runnable.run();
            }
        }
    }

    public zza(Activity activity) {
        this(C0015zza.zza(activity));
    }

    @VisibleForTesting(otherwise = 2)
    private zza(C0015zza c0015zza) {
        this.zzds = new WeakReference<>(c0015zza);
    }

    @Override // com.google.android.gms.common.api.internal.ActivityLifecycleObserver
    public final ActivityLifecycleObserver onStopCallOnce(Runnable runnable) {
        C0015zza c0015zza = this.zzds.get();
        if (c0015zza != null) {
            c0015zza.zza(runnable);
            return this;
        }
        throw new IllegalStateException("The target activity has already been GC'd");
    }
}
