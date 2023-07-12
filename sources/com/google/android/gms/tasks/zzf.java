package com.google.android.gms.tasks;

/* loaded from: classes.dex */
final class zzf implements Runnable {
    private final /* synthetic */ Task zzafn;
    private final /* synthetic */ zze zzafp;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzf(zze zzeVar, Task task) {
        this.zzafp = zzeVar;
        this.zzafn = task;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzu zzuVar;
        zzu zzuVar2;
        zzu zzuVar3;
        Continuation continuation;
        try {
            continuation = this.zzafp.zzafl;
            Task task = (Task) continuation.then(this.zzafn);
            if (task == null) {
                this.zzafp.onFailure(new NullPointerException("Continuation returned null"));
                return;
            }
            task.addOnSuccessListener(TaskExecutors.zzagd, this.zzafp);
            task.addOnFailureListener(TaskExecutors.zzagd, this.zzafp);
            task.addOnCanceledListener(TaskExecutors.zzagd, this.zzafp);
        } catch (RuntimeExecutionException e) {
            if (e.getCause() instanceof Exception) {
                zzuVar3 = this.zzafp.zzafm;
                zzuVar3.setException((Exception) e.getCause());
                return;
            }
            zzuVar2 = this.zzafp.zzafm;
            zzuVar2.setException(e);
        } catch (Exception e2) {
            zzuVar = this.zzafp.zzafm;
            zzuVar.setException(e2);
        }
    }
}
