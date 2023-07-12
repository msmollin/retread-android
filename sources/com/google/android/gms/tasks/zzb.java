package com.google.android.gms.tasks;

/* loaded from: classes.dex */
final class zzb implements OnSuccessListener<Void> {
    private final /* synthetic */ OnTokenCanceledListener zzafi;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzb(zza zzaVar, OnTokenCanceledListener onTokenCanceledListener) {
        this.zzafi = onTokenCanceledListener;
    }

    @Override // com.google.android.gms.tasks.OnSuccessListener
    public final /* synthetic */ void onSuccess(Void r1) {
        this.zzafi.onCanceled();
    }
}
