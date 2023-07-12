package com.google.android.gms.internal.measurement;

import com.google.android.gms.common.internal.Preconditions;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzjv implements zzek {
    private final /* synthetic */ zzjr zzaqu;
    zzkq zzaqv;
    List<Long> zzaqw;
    List<zzkn> zzaqx;
    private long zzaqy;

    private zzjv(zzjr zzjrVar) {
        this.zzaqu = zzjrVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzjv(zzjr zzjrVar, zzjs zzjsVar) {
        this(zzjrVar);
    }

    private static long zza(zzkn zzknVar) {
        return ((zzknVar.zzatb.longValue() / 1000) / 60) / 60;
    }

    @Override // com.google.android.gms.internal.measurement.zzek
    public final boolean zza(long j, zzkn zzknVar) {
        Preconditions.checkNotNull(zzknVar);
        if (this.zzaqx == null) {
            this.zzaqx = new ArrayList();
        }
        if (this.zzaqw == null) {
            this.zzaqw = new ArrayList();
        }
        if (this.zzaqx.size() <= 0 || zza(this.zzaqx.get(0)) == zza(zzknVar)) {
            long zzvm = this.zzaqy + zzknVar.zzvm();
            if (zzvm >= Math.max(0, zzew.zzagq.get().intValue())) {
                return false;
            }
            this.zzaqy = zzvm;
            this.zzaqx.add(zzknVar);
            this.zzaqw.add(Long.valueOf(j));
            return this.zzaqx.size() < Math.max(1, zzew.zzagr.get().intValue());
        }
        return false;
    }

    @Override // com.google.android.gms.internal.measurement.zzek
    public final void zzb(zzkq zzkqVar) {
        Preconditions.checkNotNull(zzkqVar);
        this.zzaqv = zzkqVar;
    }
}
