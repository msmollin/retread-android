package com.google.android.gms.internal.measurement;

/* loaded from: classes.dex */
final class zzaap<T> implements zzaav<T> {
    private final zzaal zzbtk;
    private final zzabj<?, ?> zzbtl;
    private final boolean zzbtm;
    private final zzzj<?> zzbtn;

    private zzaap(zzabj<?, ?> zzabjVar, zzzj<?> zzzjVar, zzaal zzaalVar) {
        this.zzbtl = zzabjVar;
        this.zzbtm = zzzjVar.zza(zzaalVar);
        this.zzbtn = zzzjVar;
        this.zzbtk = zzaalVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> zzaap<T> zza(zzabj<?, ?> zzabjVar, zzzj<?> zzzjVar, zzaal zzaalVar) {
        return new zzaap<>(zzabjVar, zzzjVar, zzaalVar);
    }

    @Override // com.google.android.gms.internal.measurement.zzaav
    public final boolean equals(T t, T t2) {
        if (this.zzbtl.zzu(t).equals(this.zzbtl.zzu(t2))) {
            if (this.zzbtm) {
                return this.zzbtn.zzs(t).equals(this.zzbtn.zzs(t2));
            }
            return true;
        }
        return false;
    }

    @Override // com.google.android.gms.internal.measurement.zzaav
    public final int hashCode(T t) {
        int hashCode = this.zzbtl.zzu(t).hashCode();
        return this.zzbtm ? (hashCode * 53) + this.zzbtn.zzs(t).hashCode() : hashCode;
    }
}
