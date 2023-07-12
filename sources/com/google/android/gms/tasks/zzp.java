package com.google.android.gms.tasks;

import java.util.concurrent.CancellationException;

/* loaded from: classes.dex */
final class zzp implements Runnable {
    private final /* synthetic */ Task zzafn;
    private final /* synthetic */ zzo zzafz;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzp(zzo zzoVar, Task task) {
        this.zzafz = zzoVar;
        this.zzafn = task;
    }

    @Override // java.lang.Runnable
    public final void run() {
        SuccessContinuation successContinuation;
        try {
            successContinuation = this.zzafz.zzafy;
            Task then = successContinuation.then(this.zzafn.getResult());
            if (then == null) {
                this.zzafz.onFailure(new NullPointerException("Continuation returned null"));
                return;
            }
            then.addOnSuccessListener(TaskExecutors.zzagd, this.zzafz);
            then.addOnFailureListener(TaskExecutors.zzagd, this.zzafz);
            then.addOnCanceledListener(TaskExecutors.zzagd, this.zzafz);
        } catch (RuntimeExecutionException e) {
            if (e.getCause() instanceof Exception) {
                this.zzafz.onFailure((Exception) e.getCause());
            } else {
                this.zzafz.onFailure(e);
            }
        } catch (CancellationException unused) {
            this.zzafz.onCanceled();
        } catch (Exception e2) {
            this.zzafz.onFailure(e2);
        }
    }
}
