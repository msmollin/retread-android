package com.google.android.gms.internal.measurement;

import android.os.Bundle;
import android.text.TextUtils;
import com.google.android.gms.common.internal.Preconditions;
import java.util.Iterator;

/* loaded from: classes.dex */
public final class zzep {
    final String name;
    private final String origin;
    final long timestamp;
    final long zzafp;
    final zzer zzafq;
    final String zzti;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzep(zzgl zzglVar, String str, String str2, String str3, long j, long j2, Bundle bundle) {
        zzer zzerVar;
        Preconditions.checkNotEmpty(str2);
        Preconditions.checkNotEmpty(str3);
        this.zzti = str2;
        this.name = str3;
        this.origin = TextUtils.isEmpty(str) ? null : str;
        this.timestamp = j;
        this.zzafp = j2;
        if (this.zzafp != 0 && this.zzafp > this.timestamp) {
            zzglVar.zzge().zzip().zzg("Event created with reverse previous/current timestamps. appId", zzfg.zzbm(str2));
        }
        if (bundle == null || bundle.isEmpty()) {
            zzerVar = new zzer(new Bundle());
        } else {
            Bundle bundle2 = new Bundle(bundle);
            Iterator<String> it = bundle2.keySet().iterator();
            while (it.hasNext()) {
                String next = it.next();
                if (next == null) {
                    zzglVar.zzge().zzim().log("Param name can't be null");
                } else {
                    Object zzh = zzglVar.zzgb().zzh(next, bundle2.get(next));
                    if (zzh == null) {
                        zzglVar.zzge().zzip().zzg("Param value can't be null", zzglVar.zzga().zzbk(next));
                    } else {
                        zzglVar.zzgb().zza(bundle2, next, zzh);
                    }
                }
                it.remove();
            }
            zzerVar = new zzer(bundle2);
        }
        this.zzafq = zzerVar;
    }

    private zzep(zzgl zzglVar, String str, String str2, String str3, long j, long j2, zzer zzerVar) {
        Preconditions.checkNotEmpty(str2);
        Preconditions.checkNotEmpty(str3);
        Preconditions.checkNotNull(zzerVar);
        this.zzti = str2;
        this.name = str3;
        this.origin = TextUtils.isEmpty(str) ? null : str;
        this.timestamp = j;
        this.zzafp = j2;
        if (this.zzafp != 0 && this.zzafp > this.timestamp) {
            zzglVar.zzge().zzip().zze("Event created with reverse previous/current timestamps. appId, name", zzfg.zzbm(str2), zzfg.zzbm(str3));
        }
        this.zzafq = zzerVar;
    }

    public final String toString() {
        String str = this.zzti;
        String str2 = this.name;
        String valueOf = String.valueOf(this.zzafq);
        StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 33 + String.valueOf(str2).length() + String.valueOf(valueOf).length());
        sb.append("Event{appId='");
        sb.append(str);
        sb.append("', name='");
        sb.append(str2);
        sb.append("', params=");
        sb.append(valueOf);
        sb.append('}');
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final zzep zza(zzgl zzglVar, long j) {
        return new zzep(zzglVar, this.origin, this.zzti, this.name, this.timestamp, j, this.zzafq);
    }
}
