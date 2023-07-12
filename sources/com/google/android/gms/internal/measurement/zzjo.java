package com.google.android.gms.internal.measurement;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzjo extends zzem {
    private final /* synthetic */ zzjr zzapt;
    private final /* synthetic */ zzjn zzapz;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzjo(zzjn zzjnVar, zzhi zzhiVar, zzjr zzjrVar) {
        super(zzhiVar);
        this.zzapz = zzjnVar;
        this.zzapt = zzjrVar;
    }

    @Override // com.google.android.gms.internal.measurement.zzem
    public final void run() {
        this.zzapz.cancel();
        this.zzapz.zzge().zzit().log("Starting upload from DelayedRunnable");
        this.zzapt.zzks();
    }
}
