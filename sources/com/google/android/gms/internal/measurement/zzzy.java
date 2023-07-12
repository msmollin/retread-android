package com.google.android.gms.internal.measurement;

/* loaded from: classes.dex */
public class zzzy {
    private static final zzzi zzbsw = zzzi.zzte();
    private zzyw zzbsx;
    private volatile zzaal zzbsy;
    private volatile zzyw zzbsz;

    private final zzaal zzb(zzaal zzaalVar) {
        if (this.zzbsy == null) {
            synchronized (this) {
                if (this.zzbsy == null) {
                    try {
                        this.zzbsy = zzaalVar;
                        this.zzbsz = zzyw.zzbqx;
                    } catch (zzzt unused) {
                        this.zzbsy = zzaalVar;
                        this.zzbsz = zzyw.zzbqx;
                    }
                }
            }
        }
        return this.zzbsy;
    }

    private final zzyw zztp() {
        if (this.zzbsz != null) {
            return this.zzbsz;
        }
        synchronized (this) {
            if (this.zzbsz != null) {
                return this.zzbsz;
            }
            this.zzbsz = this.zzbsy == null ? zzyw.zzbqx : this.zzbsy.zztp();
            return this.zzbsz;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof zzzy) {
            zzzy zzzyVar = (zzzy) obj;
            zzaal zzaalVar = this.zzbsy;
            zzaal zzaalVar2 = zzzyVar.zzbsy;
            return (zzaalVar == null && zzaalVar2 == null) ? zztp().equals(zzzyVar.zztp()) : (zzaalVar == null || zzaalVar2 == null) ? zzaalVar != null ? zzaalVar.equals(zzzyVar.zzb(zzaalVar.zztz())) : zzb(zzaalVar2.zztz()).equals(zzaalVar2) : zzaalVar.equals(zzaalVar2);
        }
        return false;
    }

    public int hashCode() {
        return 1;
    }

    public final zzaal zzc(zzaal zzaalVar) {
        zzaal zzaalVar2 = this.zzbsy;
        this.zzbsx = null;
        this.zzbsz = null;
        this.zzbsy = zzaalVar;
        return zzaalVar2;
    }
}
