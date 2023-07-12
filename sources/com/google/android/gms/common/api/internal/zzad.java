package com.google.android.gms.common.api.internal;

import android.app.Activity;
import androidx.collection.ArraySet;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.Preconditions;

/* loaded from: classes.dex */
public class zzad extends zzk {
    private GoogleApiManager zzcq;
    private final ArraySet<zzh<?>> zzhb;

    private zzad(LifecycleFragment lifecycleFragment) {
        super(lifecycleFragment);
        this.zzhb = new ArraySet<>();
        this.mLifecycleFragment.addCallback("ConnectionlessLifecycleHelper", this);
    }

    public static void zza(Activity activity, GoogleApiManager googleApiManager, zzh<?> zzhVar) {
        LifecycleFragment fragment = getFragment(activity);
        zzad zzadVar = (zzad) fragment.getCallbackOrNull("ConnectionlessLifecycleHelper", zzad.class);
        if (zzadVar == null) {
            zzadVar = new zzad(fragment);
        }
        zzadVar.zzcq = googleApiManager;
        Preconditions.checkNotNull(zzhVar, "ApiKey cannot be null");
        zzadVar.zzhb.add(zzhVar);
        googleApiManager.zza(zzadVar);
    }

    private final void zzan() {
        if (this.zzhb.isEmpty()) {
            return;
        }
        this.zzcq.zza(this);
    }

    @Override // com.google.android.gms.common.api.internal.LifecycleCallback
    public final void onResume() {
        super.onResume();
        zzan();
    }

    @Override // com.google.android.gms.common.api.internal.zzk, com.google.android.gms.common.api.internal.LifecycleCallback
    public final void onStart() {
        super.onStart();
        zzan();
    }

    @Override // com.google.android.gms.common.api.internal.zzk, com.google.android.gms.common.api.internal.LifecycleCallback
    public void onStop() {
        super.onStop();
        this.zzcq.zzb(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.common.api.internal.zzk
    public final void zza(ConnectionResult connectionResult, int i) {
        this.zzcq.zza(connectionResult, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final ArraySet<zzh<?>> zzam() {
        return this.zzhb;
    }

    @Override // com.google.android.gms.common.api.internal.zzk
    protected final void zzr() {
        this.zzcq.zzr();
    }
}
