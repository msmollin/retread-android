package com.google.android.gms.internal.measurement;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class zzjq extends zzjp {
    private boolean zzvo;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzjq(zzjr zzjrVar) {
        super(zzjrVar);
        this.zzajp.zzb(this);
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

    public final void zzm() {
        if (this.zzvo) {
            throw new IllegalStateException("Can't initialize twice");
        }
        zzhf();
        this.zzajp.zzkz();
        this.zzvo = true;
    }
}
