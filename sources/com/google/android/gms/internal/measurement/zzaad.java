package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzzq;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzaad implements zzaaw {
    private static final zzaak zzbte = new zzaae();
    private final zzaak zzbtd;

    public zzaad() {
        this(new zzaaf(zzzp.zztl(), zzts()));
    }

    private zzaad(zzaak zzaakVar) {
        this.zzbtd = (zzaak) zzzr.zza(zzaakVar, "messageInfoFactory");
    }

    private static boolean zza(zzaaj zzaajVar) {
        return zzaajVar.zztw() == zzzq.zzb.zzbsk;
    }

    private static zzaak zzts() {
        try {
            return (zzaak) Class.forName("com.google.protobuf.DescriptorMessageInfoFactory").getDeclaredMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
        } catch (Exception unused) {
            return zzbte;
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzaaw
    public final <T> zzaav<T> zzg(Class<T> cls) {
        zzaax.zzh(cls);
        zzaaj zze = this.zzbtd.zze(cls);
        return zze.zztx() ? zzzq.class.isAssignableFrom(cls) ? zzaap.zza(zzaax.zzug(), zzzl.zztg(), zze.zzty()) : zzaap.zza(zzaax.zzue(), zzzl.zzth(), zze.zzty()) : zzzq.class.isAssignableFrom(cls) ? zza(zze) ? zzaao.zza(cls, zze, zzaas.zzub(), zzzz.zztr(), zzaax.zzug(), zzzl.zztg(), zzaai.zztu()) : zzaao.zza(cls, zze, zzaas.zzub(), zzzz.zztr(), zzaax.zzug(), null, zzaai.zztu()) : zza(zze) ? zzaao.zza(cls, zze, zzaas.zzua(), zzzz.zztq(), zzaax.zzue(), zzzl.zzth(), zzaai.zztt()) : zzaao.zza(cls, zze, zzaas.zzua(), zzzz.zztq(), zzaax.zzuf(), null, zzaai.zztt());
    }
}
