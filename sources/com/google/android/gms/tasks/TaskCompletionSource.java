package com.google.android.gms.tasks;

import androidx.annotation.NonNull;

/* loaded from: classes.dex */
public class TaskCompletionSource<TResult> {
    private final zzu<TResult> zzafh = new zzu<>();

    public TaskCompletionSource() {
    }

    public TaskCompletionSource(@NonNull CancellationToken cancellationToken) {
        cancellationToken.onCanceledRequested(new zzs(this));
    }

    @NonNull
    public Task<TResult> getTask() {
        return this.zzafh;
    }

    public void setException(@NonNull Exception exc) {
        this.zzafh.setException(exc);
    }

    public void setResult(TResult tresult) {
        this.zzafh.setResult(tresult);
    }

    public boolean trySetException(@NonNull Exception exc) {
        return this.zzafh.trySetException(exc);
    }

    public boolean trySetResult(TResult tresult) {
        return this.zzafh.trySetResult(tresult);
    }
}
