package com.google.android.gms.internal.measurement;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class zzhh extends zzhg {
    private boolean zzvo;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzhh(zzgl zzglVar) {
        super(zzglVar);
        this.zzacw.zzb(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean isInitialized() {
        return this.zzvo;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void zzch() {
        if (!isInitialized()) {
            throw new IllegalStateException("Not initialized");
        }
    }

    protected abstract boolean zzhf();

    protected void zzih() {
    }

    public final void zzjw() {
        if (this.zzvo) {
            throw new IllegalStateException("Can't initialize twice");
        }
        zzih();
        this.zzacw.zzju();
        this.zzvo = true;
    }

    public final void zzm() {
        if (this.zzvo) {
            throw new IllegalStateException("Can't initialize twice");
        }
        if (zzhf()) {
            return;
        }
        this.zzacw.zzju();
        this.zzvo = true;
    }
}
