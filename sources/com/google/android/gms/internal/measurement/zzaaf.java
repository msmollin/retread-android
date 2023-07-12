package com.google.android.gms.internal.measurement;

/* loaded from: classes.dex */
final class zzaaf implements zzaak {
    private zzaak[] zzbtf;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzaaf(zzaak... zzaakVarArr) {
        this.zzbtf = zzaakVarArr;
    }

    @Override // com.google.android.gms.internal.measurement.zzaak
    public final boolean zzd(Class<?> cls) {
        for (zzaak zzaakVar : this.zzbtf) {
            if (zzaakVar.zzd(cls)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.google.android.gms.internal.measurement.zzaak
    public final zzaaj zze(Class<?> cls) {
        zzaak[] zzaakVarArr;
        for (zzaak zzaakVar : this.zzbtf) {
            if (zzaakVar.zzd(cls)) {
                return zzaakVar.zze(cls);
            }
        }
        String valueOf = String.valueOf(cls.getName());
        throw new UnsupportedOperationException(valueOf.length() != 0 ? "No factory is available for message type: ".concat(valueOf) : new String("No factory is available for message type: "));
    }
}
