package com.google.android.gms.tasks;

import androidx.annotation.NonNull;

/* loaded from: classes.dex */
final class zza extends CancellationToken {
    private final zzu<Void> zzafh = new zzu<>();

    public final void cancel() {
        this.zzafh.trySetResult(null);
    }

    @Override // com.google.android.gms.tasks.CancellationToken
    public final boolean isCancellationRequested() {
        return this.zzafh.isComplete();
    }

    @Override // com.google.android.gms.tasks.CancellationToken
    public final CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
        this.zzafh.addOnSuccessListener(new zzb(this, onTokenCanceledListener));
        return this;
    }
}
