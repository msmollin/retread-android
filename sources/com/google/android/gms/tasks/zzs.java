package com.google.android.gms.tasks;

/* loaded from: classes.dex */
final class zzs implements OnTokenCanceledListener {
    private final /* synthetic */ TaskCompletionSource zzagc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzs(TaskCompletionSource taskCompletionSource) {
        this.zzagc = taskCompletionSource;
    }

    @Override // com.google.android.gms.tasks.OnTokenCanceledListener
    public final void onCanceled() {
        zzu zzuVar;
        zzuVar = this.zzagc.zzafh;
        zzuVar.zzdp();
    }
}
