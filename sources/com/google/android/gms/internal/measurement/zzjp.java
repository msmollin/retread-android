package com.google.android.gms.internal.measurement;

import com.google.android.gms.common.internal.Preconditions;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class zzjp extends zzhg implements zzec {
    protected final zzjr zzajp;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzjp(zzjr zzjrVar) {
        super(zzjrVar.zzla());
        Preconditions.checkNotNull(zzjrVar);
        this.zzajp = zzjrVar;
    }

    public zzeb zziw() {
        return this.zzajp.zziw();
    }

    public zzei zzix() {
        return this.zzajp.zzix();
    }
}
