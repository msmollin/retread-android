package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.facebook.internal.logging.monitor.MonitorLogServerProtocol;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.measurement.AppMeasurement;
import java.util.concurrent.atomic.AtomicReference;

/* loaded from: classes.dex */
public final class zzfe extends zzhh {
    private static final AtomicReference<String[]> zzaij = new AtomicReference<>();
    private static final AtomicReference<String[]> zzaik = new AtomicReference<>();
    private static final AtomicReference<String[]> zzail = new AtomicReference<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzfe(zzgl zzglVar) {
        super(zzglVar);
    }

    @Nullable
    private static String zza(String str, String[] strArr, String[] strArr2, AtomicReference<String[]> atomicReference) {
        String str2;
        Preconditions.checkNotNull(strArr);
        Preconditions.checkNotNull(strArr2);
        Preconditions.checkNotNull(atomicReference);
        Preconditions.checkArgument(strArr.length == strArr2.length);
        for (int i = 0; i < strArr.length; i++) {
            if (zzka.zzs(str, strArr[i])) {
                synchronized (atomicReference) {
                    String[] strArr3 = atomicReference.get();
                    if (strArr3 == null) {
                        strArr3 = new String[strArr2.length];
                        atomicReference.set(strArr3);
                    }
                    if (strArr3[i] == null) {
                        strArr3[i] = strArr2[i] + "(" + strArr[i] + ")";
                    }
                    str2 = strArr3[i];
                }
                return str2;
            }
        }
        return str;
    }

    private static void zza(StringBuilder sb, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            sb.append("  ");
        }
    }

    private final void zza(StringBuilder sb, int i, zzkf zzkfVar) {
        String[] strArr;
        if (zzkfVar == null) {
            return;
        }
        zza(sb, i);
        sb.append("filter {\n");
        zza(sb, i, "complement", zzkfVar.zzarx);
        zza(sb, i, "param_name", zzbk(zzkfVar.zzary));
        int i2 = i + 1;
        zzki zzkiVar = zzkfVar.zzarv;
        if (zzkiVar != null) {
            zza(sb, i2);
            sb.append("string_filter");
            sb.append(" {\n");
            if (zzkiVar.zzash != null) {
                String str = "UNKNOWN_MATCH_TYPE";
                switch (zzkiVar.zzash.intValue()) {
                    case 1:
                        str = "REGEXP";
                        break;
                    case 2:
                        str = "BEGINS_WITH";
                        break;
                    case 3:
                        str = "ENDS_WITH";
                        break;
                    case 4:
                        str = "PARTIAL";
                        break;
                    case 5:
                        str = "EXACT";
                        break;
                    case 6:
                        str = "IN_LIST";
                        break;
                }
                zza(sb, i2, "match_type", str);
            }
            zza(sb, i2, "expression", zzkiVar.zzasi);
            zza(sb, i2, "case_sensitive", zzkiVar.zzasj);
            if (zzkiVar.zzask.length > 0) {
                zza(sb, i2 + 1);
                sb.append("expression_list {\n");
                for (String str2 : zzkiVar.zzask) {
                    zza(sb, i2 + 2);
                    sb.append(str2);
                    sb.append("\n");
                }
                sb.append("}\n");
            }
            zza(sb, i2);
            sb.append("}\n");
        }
        zza(sb, i2, "number_filter", zzkfVar.zzarw);
        zza(sb, i);
        sb.append("}\n");
    }

    private final void zza(StringBuilder sb, int i, String str, zzkg zzkgVar) {
        if (zzkgVar == null) {
            return;
        }
        zza(sb, i);
        sb.append(str);
        sb.append(" {\n");
        if (zzkgVar.zzarz != null) {
            String str2 = "UNKNOWN_COMPARISON_TYPE";
            switch (zzkgVar.zzarz.intValue()) {
                case 1:
                    str2 = "LESS_THAN";
                    break;
                case 2:
                    str2 = "GREATER_THAN";
                    break;
                case 3:
                    str2 = "EQUAL";
                    break;
                case 4:
                    str2 = "BETWEEN";
                    break;
            }
            zza(sb, i, "comparison_type", str2);
        }
        zza(sb, i, "match_as_float", zzkgVar.zzasa);
        zza(sb, i, "comparison_value", zzkgVar.zzasb);
        zza(sb, i, "min_comparison_value", zzkgVar.zzasc);
        zza(sb, i, "max_comparison_value", zzkgVar.zzasd);
        zza(sb, i);
        sb.append("}\n");
    }

    private static void zza(StringBuilder sb, int i, String str, zzkr zzkrVar) {
        if (zzkrVar == null) {
            return;
        }
        zza(sb, 3);
        sb.append(str);
        sb.append(" {\n");
        int i2 = 0;
        if (zzkrVar.zzaul != null) {
            zza(sb, 4);
            sb.append("results: ");
            long[] jArr = zzkrVar.zzaul;
            int length = jArr.length;
            int i3 = 0;
            int i4 = 0;
            while (i3 < length) {
                Long valueOf = Long.valueOf(jArr[i3]);
                int i5 = i4 + 1;
                if (i4 != 0) {
                    sb.append(", ");
                }
                sb.append(valueOf);
                i3++;
                i4 = i5;
            }
            sb.append('\n');
        }
        if (zzkrVar.zzauk != null) {
            zza(sb, 4);
            sb.append("status: ");
            long[] jArr2 = zzkrVar.zzauk;
            int length2 = jArr2.length;
            int i6 = 0;
            while (i2 < length2) {
                Long valueOf2 = Long.valueOf(jArr2[i2]);
                int i7 = i6 + 1;
                if (i6 != 0) {
                    sb.append(", ");
                }
                sb.append(valueOf2);
                i2++;
                i6 = i7;
            }
            sb.append('\n');
        }
        zza(sb, 3);
        sb.append("}\n");
    }

    private static void zza(StringBuilder sb, int i, String str, Object obj) {
        if (obj == null) {
            return;
        }
        zza(sb, i + 1);
        sb.append(str);
        sb.append(": ");
        sb.append(obj);
        sb.append('\n');
    }

    @Nullable
    private final String zzb(zzer zzerVar) {
        if (zzerVar == null) {
            return null;
        }
        return !zzil() ? zzerVar.toString() : zzb(zzerVar.zzif());
    }

    private final boolean zzil() {
        return this.zzacw.zzge().isLoggable(3);
    }

    @Override // com.google.android.gms.internal.measurement.zzhg, com.google.android.gms.internal.measurement.zzec
    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Nullable
    public final String zza(zzep zzepVar) {
        if (zzepVar == null) {
            return null;
        }
        if (zzil()) {
            return "Event{appId='" + zzepVar.zzti + "', name='" + zzbj(zzepVar.name) + "', params=" + zzb(zzepVar.zzafq) + "}";
        }
        return zzepVar.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final String zza(zzke zzkeVar) {
        if (zzkeVar == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\nevent_filter {\n");
        zza(sb, 0, "filter_id", zzkeVar.zzarp);
        zza(sb, 0, "event_name", zzbj(zzkeVar.zzarq));
        zza(sb, 1, "event_count_filter", zzkeVar.zzart);
        sb.append("  filters {\n");
        for (zzkf zzkfVar : zzkeVar.zzarr) {
            zza(sb, 2, zzkfVar);
        }
        zza(sb, 1);
        sb.append("}\n}\n");
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final String zza(zzkh zzkhVar) {
        if (zzkhVar == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\nproperty_filter {\n");
        zza(sb, 0, "filter_id", zzkhVar.zzarp);
        zza(sb, 0, "property_name", zzbl(zzkhVar.zzasf));
        zza(sb, 1, zzkhVar.zzasg);
        sb.append("}\n");
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final String zza(zzkp zzkpVar) {
        zzkq[] zzkqVarArr;
        StringBuilder sb = new StringBuilder();
        sb.append("\nbatch {\n");
        if (zzkpVar.zzatf != null) {
            for (zzkq zzkqVar : zzkpVar.zzatf) {
                if (zzkqVar != null && zzkqVar != null) {
                    int i = 1;
                    zza(sb, 1);
                    sb.append("bundle {\n");
                    zza(sb, 1, "protocol_version", zzkqVar.zzath);
                    zza(sb, 1, "platform", zzkqVar.zzatp);
                    zza(sb, 1, "gmp_version", zzkqVar.zzatt);
                    zza(sb, 1, "uploading_gmp_version", zzkqVar.zzatu);
                    zza(sb, 1, "config_version", zzkqVar.zzauf);
                    zza(sb, 1, "gmp_app_id", zzkqVar.zzadm);
                    zza(sb, 1, "app_id", zzkqVar.zzti);
                    zza(sb, 1, "app_version", zzkqVar.zzth);
                    zza(sb, 1, "app_version_major", zzkqVar.zzaub);
                    zza(sb, 1, "firebase_instance_id", zzkqVar.zzado);
                    zza(sb, 1, "dev_cert_hash", zzkqVar.zzatx);
                    zza(sb, 1, "app_store", zzkqVar.zzadt);
                    zza(sb, 1, "upload_timestamp_millis", zzkqVar.zzatk);
                    zza(sb, 1, "start_timestamp_millis", zzkqVar.zzatl);
                    zza(sb, 1, "end_timestamp_millis", zzkqVar.zzatm);
                    zza(sb, 1, "previous_bundle_start_timestamp_millis", zzkqVar.zzatn);
                    zza(sb, 1, "previous_bundle_end_timestamp_millis", zzkqVar.zzato);
                    zza(sb, 1, "app_instance_id", zzkqVar.zzadl);
                    zza(sb, 1, "resettable_device_id", zzkqVar.zzatv);
                    zza(sb, 1, "device_id", zzkqVar.zzaue);
                    zza(sb, 1, "limited_ad_tracking", zzkqVar.zzatw);
                    zza(sb, 1, "os_version", zzkqVar.zzatq);
                    zza(sb, 1, MonitorLogServerProtocol.PARAM_DEVICE_MODEL, zzkqVar.zzatr);
                    zza(sb, 1, "user_default_language", zzkqVar.zzafn);
                    zza(sb, 1, "time_zone_offset_minutes", zzkqVar.zzats);
                    zza(sb, 1, "bundle_sequential_index", zzkqVar.zzaty);
                    zza(sb, 1, "service_upload", zzkqVar.zzatz);
                    zza(sb, 1, "health_monitor", zzkqVar.zzaek);
                    if (zzkqVar.zzaug != null && zzkqVar.zzaug.longValue() != 0) {
                        zza(sb, 1, "android_id", zzkqVar.zzaug);
                    }
                    if (zzkqVar.zzauj != null) {
                        zza(sb, 1, "retry_counter", zzkqVar.zzauj);
                    }
                    zzks[] zzksVarArr = zzkqVar.zzatj;
                    if (zzksVarArr != null) {
                        for (zzks zzksVar : zzksVarArr) {
                            if (zzksVar != null) {
                                zza(sb, 2);
                                sb.append("user_property {\n");
                                zza(sb, 2, "set_timestamp_millis", zzksVar.zzaun);
                                zza(sb, 2, "name", zzbl(zzksVar.name));
                                zza(sb, 2, "string_value", zzksVar.zzajf);
                                zza(sb, 2, "int_value", zzksVar.zzate);
                                zza(sb, 2, "double_value", zzksVar.zzarc);
                                zza(sb, 2);
                                sb.append("}\n");
                            }
                        }
                    }
                    zzkm[] zzkmVarArr = zzkqVar.zzaua;
                    if (zzkmVarArr != null) {
                        for (zzkm zzkmVar : zzkmVarArr) {
                            if (zzkmVar != null) {
                                zza(sb, 2);
                                sb.append("audience_membership {\n");
                                zza(sb, 2, "audience_id", zzkmVar.zzarl);
                                zza(sb, 2, "new_audience", zzkmVar.zzasy);
                                zza(sb, 2, "current_data", zzkmVar.zzasw);
                                zza(sb, 2, "previous_data", zzkmVar.zzasx);
                                zza(sb, 2);
                                sb.append("}\n");
                            }
                        }
                    }
                    zzkn[] zzknVarArr = zzkqVar.zzati;
                    if (zzknVarArr != null) {
                        int length = zzknVarArr.length;
                        int i2 = 0;
                        while (i2 < length) {
                            zzkn zzknVar = zzknVarArr[i2];
                            if (zzknVar != null) {
                                zza(sb, 2);
                                sb.append("event {\n");
                                zza(sb, 2, "name", zzbj(zzknVar.name));
                                zza(sb, 2, "timestamp_millis", zzknVar.zzatb);
                                zza(sb, 2, "previous_timestamp_millis", zzknVar.zzatc);
                                zza(sb, 2, "count", zzknVar.count);
                                zzko[] zzkoVarArr = zzknVar.zzata;
                                if (zzkoVarArr != null) {
                                    for (zzko zzkoVar : zzkoVarArr) {
                                        if (zzkoVar != null) {
                                            zza(sb, 3);
                                            sb.append("param {\n");
                                            zza(sb, 3, "name", zzbk(zzkoVar.name));
                                            zza(sb, 3, "string_value", zzkoVar.zzajf);
                                            zza(sb, 3, "int_value", zzkoVar.zzate);
                                            zza(sb, 3, "double_value", zzkoVar.zzarc);
                                            zza(sb, 3);
                                            sb.append("}\n");
                                        }
                                    }
                                }
                                zza(sb, 2);
                                sb.append("}\n");
                            }
                            i2++;
                            i = 1;
                        }
                    }
                    zza(sb, i);
                    sb.append("}\n");
                }
            }
        }
        sb.append("}\n");
        return sb.toString();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ void zzab() {
        super.zzab();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Nullable
    public final String zzb(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        if (zzil()) {
            StringBuilder sb = new StringBuilder();
            for (String str : bundle.keySet()) {
                sb.append(sb.length() != 0 ? ", " : "Bundle[{");
                sb.append(zzbk(str));
                sb.append("=");
                sb.append(bundle.get(str));
            }
            sb.append("}]");
            return sb.toString();
        }
        return bundle.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Nullable
    public final String zzb(zzeu zzeuVar) {
        if (zzeuVar == null) {
            return null;
        }
        if (zzil()) {
            return "origin=" + zzeuVar.origin + ",name=" + zzbj(zzeuVar.name) + ",params=" + zzb(zzeuVar.zzafq);
        }
        return zzeuVar.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Nullable
    public final String zzbj(String str) {
        if (str == null) {
            return null;
        }
        return !zzil() ? str : zza(str, AppMeasurement.Event.zzacy, AppMeasurement.Event.zzacx, zzaij);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Nullable
    public final String zzbk(String str) {
        if (str == null) {
            return null;
        }
        return !zzil() ? str : zza(str, AppMeasurement.Param.zzada, AppMeasurement.Param.zzacz, zzaik);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Nullable
    public final String zzbl(String str) {
        if (str == null) {
            return null;
        }
        if (zzil()) {
            if (str.startsWith("_exp_")) {
                return "experiment_id(" + str + ")";
            }
            return zza(str, AppMeasurement.UserProperty.zzadc, AppMeasurement.UserProperty.zzadb, zzail);
        }
        return str;
    }

    @Override // com.google.android.gms.internal.measurement.zzhg, com.google.android.gms.internal.measurement.zzec
    public final /* bridge */ /* synthetic */ Clock zzbt() {
        return super.zzbt();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ void zzfr() {
        super.zzfr();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ void zzfs() {
        super.zzfs();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ zzdu zzft() {
        return super.zzft();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ zzhk zzfu() {
        return super.zzfu();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ zzfb zzfv() {
        return super.zzfv();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ zzeo zzfw() {
        return super.zzfw();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ zzii zzfx() {
        return super.zzfx();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ zzif zzfy() {
        return super.zzfy();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ zzfc zzfz() {
        return super.zzfz();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ zzfe zzga() {
        return super.zzga();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ zzka zzgb() {
        return super.zzgb();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ zzjh zzgc() {
        return super.zzgc();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg, com.google.android.gms.internal.measurement.zzec
    public final /* bridge */ /* synthetic */ zzgg zzgd() {
        return super.zzgd();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg, com.google.android.gms.internal.measurement.zzec
    public final /* bridge */ /* synthetic */ zzfg zzge() {
        return super.zzge();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ zzfr zzgf() {
        return super.zzgf();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ zzef zzgg() {
        return super.zzgg();
    }

    @Override // com.google.android.gms.internal.measurement.zzhh
    protected final boolean zzhf() {
        return false;
    }
}
