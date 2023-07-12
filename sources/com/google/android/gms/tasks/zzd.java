package com.google.android.gms.tasks;

/* loaded from: classes.dex */
final class zzd implements Runnable {
    private final /* synthetic */ Task zzafn;
    private final /* synthetic */ zzc zzafo;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzd(zzc zzcVar, Task task) {
        this.zzafo = zzcVar;
        this.zzafn = task;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzu zzuVar;
        zzu zzuVar2;
        zzu zzuVar3;
        Continuation continuation;
        zzu zzuVar4;
        zzu zzuVar5;
        if (this.zzafn.isCanceled()) {
            zzuVar5 = this.zzafo.zzafm;
            zzuVar5.zzdp();
            return;
        }
        try {
            continuation = this.zzafo.zzafl;
            Object then = continuation.then(this.zzafn);
            zzuVar4 = this.zzafo.zzafm;
            zzuVar4.setResult(then);
        } catch (RuntimeExecutionException e) {
            if (e.getCause() instanceof Exception) {
                zzuVar3 = this.zzafo.zzafm;
                zzuVar3.setException((Exception) e.getCause());
                return;
            }
            zzuVar2 = this.zzafo.zzafm;
            zzuVar2.setException(e);
        } catch (Exception e2) {
            zzuVar = this.zzafo.zzafm;
            zzuVar.setException(e2);
        }
    }
}
