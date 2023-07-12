package com.google.android.gms.dynamic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.google.android.gms.dynamic.DeferredLifecycleHelper;

/* loaded from: classes.dex */
final class zzd implements DeferredLifecycleHelper.zza {
    private final /* synthetic */ ViewGroup val$container;
    private final /* synthetic */ DeferredLifecycleHelper zzabg;
    private final /* synthetic */ Bundle zzabi;
    private final /* synthetic */ FrameLayout zzabj;
    private final /* synthetic */ LayoutInflater zzabk;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzd(DeferredLifecycleHelper deferredLifecycleHelper, FrameLayout frameLayout, LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.zzabg = deferredLifecycleHelper;
        this.zzabj = frameLayout;
        this.zzabk = layoutInflater;
        this.val$container = viewGroup;
        this.zzabi = bundle;
    }

    @Override // com.google.android.gms.dynamic.DeferredLifecycleHelper.zza
    public final int getState() {
        return 2;
    }

    @Override // com.google.android.gms.dynamic.DeferredLifecycleHelper.zza
    public final void zza(LifecycleDelegate lifecycleDelegate) {
        LifecycleDelegate lifecycleDelegate2;
        this.zzabj.removeAllViews();
        FrameLayout frameLayout = this.zzabj;
        lifecycleDelegate2 = this.zzabg.zzabc;
        frameLayout.addView(lifecycleDelegate2.onCreateView(this.zzabk, this.val$container, this.zzabi));
    }
}
