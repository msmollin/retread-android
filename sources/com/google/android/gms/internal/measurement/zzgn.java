package com.google.android.gms.internal.measurement;

import android.os.Binder;
import android.text.TextUtils;
import androidx.annotation.BinderThread;
import androidx.annotation.Nullable;
import com.google.android.gms.common.GooglePlayServicesUtilLight;
import com.google.android.gms.common.GoogleSignatureVerifier;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.UidVerifier;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/* loaded from: classes.dex */
public final class zzgn extends zzez {
    private final zzjr zzajp;
    private Boolean zzanc;
    @Nullable
    private String zzand;

    public zzgn(zzjr zzjrVar) {
        this(zzjrVar, null);
    }

    private zzgn(zzjr zzjrVar, @Nullable String str) {
        Preconditions.checkNotNull(zzjrVar);
        this.zzajp = zzjrVar;
        this.zzand = null;
    }

    @BinderThread
    private final void zzb(zzdz zzdzVar, boolean z) {
        Preconditions.checkNotNull(zzdzVar);
        zzc(zzdzVar.packageName, false);
        this.zzajp.zzgb().zzcg(zzdzVar.zzadm);
    }

    @BinderThread
    private final void zzc(String str, boolean z) {
        boolean z2;
        if (TextUtils.isEmpty(str)) {
            this.zzajp.zzge().zzim().log("Measurement Service called without app package");
            throw new SecurityException("Measurement Service called without app package");
        }
        if (z) {
            try {
                if (this.zzanc == null) {
                    if (!"com.google.android.gms".equals(this.zzand) && !UidVerifier.isGooglePlayServicesUid(this.zzajp.getContext(), Binder.getCallingUid()) && !GoogleSignatureVerifier.getInstance(this.zzajp.getContext()).isUidGoogleSigned(Binder.getCallingUid())) {
                        z2 = false;
                        this.zzanc = Boolean.valueOf(z2);
                    }
                    z2 = true;
                    this.zzanc = Boolean.valueOf(z2);
                }
                if (this.zzanc.booleanValue()) {
                    return;
                }
            } catch (SecurityException e) {
                this.zzajp.zzge().zzim().zzg("Measurement Service called with invalid calling package. appId", zzfg.zzbm(str));
                throw e;
            }
        }
        if (this.zzand == null && GooglePlayServicesUtilLight.uidHasPackageName(this.zzajp.getContext(), Binder.getCallingUid(), str)) {
            this.zzand = str;
        }
        if (str.equals(this.zzand)) {
            return;
        }
        throw new SecurityException(String.format("Unknown calling package name '%s'.", str));
    }

    @VisibleForTesting
    private final void zze(Runnable runnable) {
        Preconditions.checkNotNull(runnable);
        if (zzew.zzaia.get().booleanValue() && this.zzajp.zzgd().zzjk()) {
            runnable.run();
        } else {
            this.zzajp.zzgd().zzc(runnable);
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzey
    @BinderThread
    public final List<zzjx> zza(zzdz zzdzVar, boolean z) {
        zzb(zzdzVar, false);
        try {
            List<zzjz> list = (List) this.zzajp.zzgd().zzb(new zzhd(this, zzdzVar)).get();
            ArrayList arrayList = new ArrayList(list.size());
            for (zzjz zzjzVar : list) {
                if (z || !zzka.zzci(zzjzVar.name)) {
                    arrayList.add(new zzjx(zzjzVar));
                }
            }
            return arrayList;
        } catch (InterruptedException | ExecutionException e) {
            this.zzajp.zzge().zzim().zze("Failed to get user attributes. appId", zzfg.zzbm(zzdzVar.packageName), e);
            return null;
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzey
    @BinderThread
    public final List<zzed> zza(String str, String str2, zzdz zzdzVar) {
        zzb(zzdzVar, false);
        try {
            return (List) this.zzajp.zzgd().zzb(new zzgv(this, zzdzVar, str, str2)).get();
        } catch (InterruptedException | ExecutionException e) {
            this.zzajp.zzge().zzim().zzg("Failed to get conditional user properties", e);
            return Collections.emptyList();
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzey
    @BinderThread
    public final List<zzjx> zza(String str, String str2, String str3, boolean z) {
        zzc(str, true);
        try {
            List<zzjz> list = (List) this.zzajp.zzgd().zzb(new zzgu(this, str, str2, str3)).get();
            ArrayList arrayList = new ArrayList(list.size());
            for (zzjz zzjzVar : list) {
                if (z || !zzka.zzci(zzjzVar.name)) {
                    arrayList.add(new zzjx(zzjzVar));
                }
            }
            return arrayList;
        } catch (InterruptedException | ExecutionException e) {
            this.zzajp.zzge().zzim().zze("Failed to get user attributes. appId", zzfg.zzbm(str), e);
            return Collections.emptyList();
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzey
    @BinderThread
    public final List<zzjx> zza(String str, String str2, boolean z, zzdz zzdzVar) {
        zzb(zzdzVar, false);
        try {
            List<zzjz> list = (List) this.zzajp.zzgd().zzb(new zzgt(this, zzdzVar, str, str2)).get();
            ArrayList arrayList = new ArrayList(list.size());
            for (zzjz zzjzVar : list) {
                if (z || !zzka.zzci(zzjzVar.name)) {
                    arrayList.add(new zzjx(zzjzVar));
                }
            }
            return arrayList;
        } catch (InterruptedException | ExecutionException e) {
            this.zzajp.zzge().zzim().zze("Failed to get user attributes. appId", zzfg.zzbm(zzdzVar.packageName), e);
            return Collections.emptyList();
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzey
    @BinderThread
    public final void zza(long j, String str, String str2, String str3) {
        zze(new zzhf(this, str2, str3, str, j));
    }

    @Override // com.google.android.gms.internal.measurement.zzey
    @BinderThread
    public final void zza(zzdz zzdzVar) {
        zzb(zzdzVar, false);
        zze(new zzhe(this, zzdzVar));
    }

    @Override // com.google.android.gms.internal.measurement.zzey
    @BinderThread
    public final void zza(zzed zzedVar, zzdz zzdzVar) {
        Preconditions.checkNotNull(zzedVar);
        Preconditions.checkNotNull(zzedVar.zzaep);
        zzb(zzdzVar, false);
        zzed zzedVar2 = new zzed(zzedVar);
        zzedVar2.packageName = zzdzVar.packageName;
        zze(zzedVar.zzaep.getValue() == null ? new zzgp(this, zzedVar2, zzdzVar) : new zzgq(this, zzedVar2, zzdzVar));
    }

    @Override // com.google.android.gms.internal.measurement.zzey
    @BinderThread
    public final void zza(zzeu zzeuVar, zzdz zzdzVar) {
        Preconditions.checkNotNull(zzeuVar);
        zzb(zzdzVar, false);
        zze(new zzgy(this, zzeuVar, zzdzVar));
    }

    @Override // com.google.android.gms.internal.measurement.zzey
    @BinderThread
    public final void zza(zzeu zzeuVar, String str, String str2) {
        Preconditions.checkNotNull(zzeuVar);
        Preconditions.checkNotEmpty(str);
        zzc(str, true);
        zze(new zzgz(this, zzeuVar, str));
    }

    @Override // com.google.android.gms.internal.measurement.zzey
    @BinderThread
    public final void zza(zzjx zzjxVar, zzdz zzdzVar) {
        Preconditions.checkNotNull(zzjxVar);
        zzb(zzdzVar, false);
        zze(zzjxVar.getValue() == null ? new zzhb(this, zzjxVar, zzdzVar) : new zzhc(this, zzjxVar, zzdzVar));
    }

    @Override // com.google.android.gms.internal.measurement.zzey
    @BinderThread
    public final byte[] zza(zzeu zzeuVar, String str) {
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(zzeuVar);
        zzc(str, true);
        this.zzajp.zzge().zzis().zzg("Log and bundle. event", this.zzajp.zzga().zzbj(zzeuVar.name));
        long nanoTime = this.zzajp.zzbt().nanoTime() / 1000000;
        try {
            byte[] bArr = (byte[]) this.zzajp.zzgd().zzc(new zzha(this, zzeuVar, str)).get();
            if (bArr == null) {
                this.zzajp.zzge().zzim().zzg("Log and bundle returned null. appId", zzfg.zzbm(str));
                bArr = new byte[0];
            }
            this.zzajp.zzge().zzis().zzd("Log and bundle processed. event, size, time_ms", this.zzajp.zzga().zzbj(zzeuVar.name), Integer.valueOf(bArr.length), Long.valueOf((this.zzajp.zzbt().nanoTime() / 1000000) - nanoTime));
            return bArr;
        } catch (InterruptedException | ExecutionException e) {
            this.zzajp.zzge().zzim().zzd("Failed to log and bundle. appId, event, error", zzfg.zzbm(str), this.zzajp.zzga().zzbj(zzeuVar.name), e);
            return null;
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzey
    @BinderThread
    public final void zzb(zzdz zzdzVar) {
        zzb(zzdzVar, false);
        zze(new zzgo(this, zzdzVar));
    }

    @Override // com.google.android.gms.internal.measurement.zzey
    @BinderThread
    public final void zzb(zzed zzedVar) {
        Preconditions.checkNotNull(zzedVar);
        Preconditions.checkNotNull(zzedVar.zzaep);
        zzc(zzedVar.packageName, true);
        zzed zzedVar2 = new zzed(zzedVar);
        zze(zzedVar.zzaep.getValue() == null ? new zzgr(this, zzedVar2) : new zzgs(this, zzedVar2));
    }

    @Override // com.google.android.gms.internal.measurement.zzey
    @BinderThread
    public final String zzc(zzdz zzdzVar) {
        zzb(zzdzVar, false);
        return this.zzajp.zzh(zzdzVar);
    }

    @Override // com.google.android.gms.internal.measurement.zzey
    @BinderThread
    public final void zzd(zzdz zzdzVar) {
        zzc(zzdzVar.packageName, false);
        zze(new zzgx(this, zzdzVar));
    }

    @Override // com.google.android.gms.internal.measurement.zzey
    @BinderThread
    public final List<zzed> zze(String str, String str2, String str3) {
        zzc(str, true);
        try {
            return (List) this.zzajp.zzgd().zzb(new zzgw(this, str, str2, str3)).get();
        } catch (InterruptedException | ExecutionException e) {
            this.zzajp.zzge().zzim().zzg("Failed to get conditional user properties", e);
            return Collections.emptyList();
        }
    }
}
