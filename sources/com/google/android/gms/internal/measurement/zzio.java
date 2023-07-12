package com.google.android.gms.internal.measurement;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzio extends zzem {
    private final /* synthetic */ zzii zzape;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzio(zzii zziiVar, zzhi zzhiVar) {
        super(zzhiVar);
        this.zzape = zziiVar;
    }

    @Override // com.google.android.gms.internal.measurement.zzem
    public final void run() {
        this.zzape.zzge().zzip().log("Tasks have been queued for a long time");
    }
}
