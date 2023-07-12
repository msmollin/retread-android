package com.google.android.gms.internal.measurement;

import android.content.Context;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class zzhg implements zzhi {
    protected final zzgl zzacw;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzhg(zzgl zzglVar) {
        Preconditions.checkNotNull(zzglVar);
        this.zzacw = zzglVar;
    }

    @Override // com.google.android.gms.internal.measurement.zzec
    public Context getContext() {
        return this.zzacw.getContext();
    }

    public void zzab() {
        this.zzacw.zzgd().zzab();
    }

    @Override // com.google.android.gms.internal.measurement.zzec
    public Clock zzbt() {
        return this.zzacw.zzbt();
    }

    public void zzfr() {
        zzgl.zzfr();
    }

    public void zzfs() {
        this.zzacw.zzgd().zzfs();
    }

    public zzdu zzft() {
        return this.zzacw.zzft();
    }

    public zzhk zzfu() {
        return this.zzacw.zzfu();
    }

    public zzfb zzfv() {
        return this.zzacw.zzfv();
    }

    public zzeo zzfw() {
        return this.zzacw.zzfw();
    }

    public zzii zzfx() {
        return this.zzacw.zzfx();
    }

    public zzif zzfy() {
        return this.zzacw.zzfy();
    }

    public zzfc zzfz() {
        return this.zzacw.zzfz();
    }

    public zzfe zzga() {
        return this.zzacw.zzga();
    }

    public zzka zzgb() {
        return this.zzacw.zzgb();
    }

    public zzjh zzgc() {
        return this.zzacw.zzgc();
    }

    @Override // com.google.android.gms.internal.measurement.zzec
    public zzgg zzgd() {
        return this.zzacw.zzgd();
    }

    @Override // com.google.android.gms.internal.measurement.zzec
    public zzfg zzge() {
        return this.zzacw.zzge();
    }

    public zzfr zzgf() {
        return this.zzacw.zzgf();
    }

    public zzef zzgg() {
        return this.zzacw.zzgg();
    }
}
