package com.google.firebase.iid;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.GuardedBy;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;
import androidx.core.content.ContextCompat;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzao {
    private final SharedPreferences zzcq;
    private final zzp zzcr;
    @GuardedBy("this")
    private final Map<String, zzq> zzcs;
    private final Context zzz;

    public zzao(Context context) {
        this(context, new zzp());
    }

    private zzao(Context context, zzp zzpVar) {
        this.zzcs = new ArrayMap();
        this.zzz = context;
        this.zzcq = context.getSharedPreferences("com.google.android.gms.appid", 0);
        this.zzcr = zzpVar;
        File file = new File(ContextCompat.getNoBackupFilesDir(this.zzz), "com.google.android.gms.appid-no-backup");
        if (file.exists()) {
            return;
        }
        try {
            if (!file.createNewFile() || isEmpty()) {
                return;
            }
            Log.i("FirebaseInstanceId", "App restored, clearing state");
            zzag();
            FirebaseInstanceId.getInstance().zzk();
        } catch (IOException e) {
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                String valueOf = String.valueOf(e.getMessage());
                Log.d("FirebaseInstanceId", valueOf.length() != 0 ? "Error creating file in no backup dir: ".concat(valueOf) : new String("Error creating file in no backup dir: "));
            }
        }
    }

    private final synchronized boolean isEmpty() {
        return this.zzcq.getAll().isEmpty();
    }

    private static String zza(String str, String str2, String str3) {
        StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 4 + String.valueOf(str2).length() + String.valueOf(str3).length());
        sb.append(str);
        sb.append("|T|");
        sb.append(str2);
        sb.append("|");
        sb.append(str3);
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String zzd(String str, String str2) {
        StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 3 + String.valueOf(str2).length());
        sb.append(str);
        sb.append("|S|");
        sb.append(str2);
        return sb.toString();
    }

    public final synchronized void zza(String str) {
        String string = this.zzcq.getString("topic_operaion_queue", "");
        StringBuilder sb = new StringBuilder(String.valueOf(string).length() + 1 + String.valueOf(str).length());
        sb.append(string);
        sb.append(",");
        sb.append(str);
        this.zzcq.edit().putString("topic_operaion_queue", sb.toString()).apply();
    }

    public final synchronized void zza(String str, String str2, String str3, String str4, String str5) {
        String zza = zzap.zza(str4, str5, System.currentTimeMillis());
        if (zza == null) {
            return;
        }
        SharedPreferences.Editor edit = this.zzcq.edit();
        edit.putString(zza(str, str2, str3), zza);
        edit.commit();
    }

    @Nullable
    public final synchronized String zzaf() {
        String string = this.zzcq.getString("topic_operaion_queue", null);
        if (string != null) {
            String[] split = string.split(",");
            if (split.length > 1 && !TextUtils.isEmpty(split[1])) {
                return split[1];
            }
        }
        return null;
    }

    public final synchronized void zzag() {
        this.zzcs.clear();
        zzp.zza(this.zzz);
        this.zzcq.edit().clear().commit();
    }

    public final synchronized zzap zzb(String str, String str2, String str3) {
        return zzap.zzi(this.zzcq.getString(zza(str, str2, str3), null));
    }

    public final synchronized void zzc(String str, String str2, String str3) {
        String zza = zza(str, str2, str3);
        SharedPreferences.Editor edit = this.zzcq.edit();
        edit.remove(zza);
        edit.commit();
    }

    public final synchronized boolean zzf(String str) {
        boolean z;
        String string = this.zzcq.getString("topic_operaion_queue", "");
        String valueOf = String.valueOf(",");
        String valueOf2 = String.valueOf(str);
        if (string.startsWith(valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf))) {
            String valueOf3 = String.valueOf(",");
            String valueOf4 = String.valueOf(str);
            this.zzcq.edit().putString("topic_operaion_queue", string.substring((valueOf4.length() != 0 ? valueOf3.concat(valueOf4) : new String(valueOf3)).length())).apply();
            z = true;
        } else {
            z = false;
        }
        return z;
    }

    public final synchronized zzq zzg(String str) {
        zzq zzc;
        zzq zzqVar = this.zzcs.get(str);
        if (zzqVar != null) {
            return zzqVar;
        }
        try {
            zzc = this.zzcr.zzb(this.zzz, str);
        } catch (zzr unused) {
            Log.w("FirebaseInstanceId", "Stored data is corrupt, generating new identity");
            FirebaseInstanceId.getInstance().zzk();
            zzc = this.zzcr.zzc(this.zzz, str);
        }
        this.zzcs.put(str, zzc);
        return zzc;
    }

    public final synchronized void zzh(String str) {
        String concat = String.valueOf(str).concat("|T|");
        SharedPreferences.Editor edit = this.zzcq.edit();
        for (String str2 : this.zzcq.getAll().keySet()) {
            if (str2.startsWith(concat)) {
                edit.remove(str2);
            }
        }
        edit.commit();
    }
}
