package com.google.firebase.iid;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.load.Key;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Properties;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzp {
    @Nullable
    private static zzq zza(SharedPreferences sharedPreferences, String str) throws zzr {
        String string = sharedPreferences.getString(zzao.zzd(str, "|P|"), null);
        String string2 = sharedPreferences.getString(zzao.zzd(str, "|K|"), null);
        if (string == null || string2 == null) {
            return null;
        }
        return new zzq(zzc(string, string2), zzb(sharedPreferences, str));
    }

    @Nullable
    private static zzq zza(File file) throws zzr, IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            Properties properties = new Properties();
            properties.load(fileInputStream);
            String property = properties.getProperty("pub");
            String property2 = properties.getProperty("pri");
            if (property != null && property2 != null) {
                try {
                    return new zzq(zzc(property, property2), Long.parseLong(properties.getProperty("cre")));
                } catch (NumberFormatException e) {
                    throw new zzr(e);
                }
            }
            return null;
        } finally {
            zza((Throwable) null, fileInputStream);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void zza(Context context) {
        File[] listFiles;
        for (File file : zzb(context).listFiles()) {
            if (file.getName().startsWith("com.google.InstanceId")) {
                file.delete();
            }
        }
    }

    private static void zza(Context context, String str, zzq zzqVar) {
        String zzq;
        String zzr;
        long j;
        try {
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                Log.d("FirebaseInstanceId", "Writing key to properties file");
            }
            File zzf = zzf(context, str);
            zzf.createNewFile();
            Properties properties = new Properties();
            zzq = zzqVar.zzq();
            properties.setProperty("pub", zzq);
            zzr = zzqVar.zzr();
            properties.setProperty("pri", zzr);
            j = zzqVar.zzbe;
            properties.setProperty("cre", String.valueOf(j));
            FileOutputStream fileOutputStream = new FileOutputStream(zzf);
            properties.store(fileOutputStream, (String) null);
            zza((Throwable) null, fileOutputStream);
        } catch (IOException e) {
            String valueOf = String.valueOf(e);
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 21);
            sb.append("Failed to write key: ");
            sb.append(valueOf);
            Log.w("FirebaseInstanceId", sb.toString());
        }
    }

    private static /* synthetic */ void zza(Throwable th, FileInputStream fileInputStream) {
        if (th == null) {
            fileInputStream.close();
            return;
        }
        try {
            fileInputStream.close();
        } catch (Throwable th2) {
            com.google.android.gms.internal.firebase_messaging.zzh.zza(th, th2);
        }
    }

    private static /* synthetic */ void zza(Throwable th, FileOutputStream fileOutputStream) {
        if (th == null) {
            fileOutputStream.close();
            return;
        }
        try {
            fileOutputStream.close();
        } catch (Throwable th2) {
            com.google.android.gms.internal.firebase_messaging.zzh.zza(th, th2);
        }
    }

    private static long zzb(SharedPreferences sharedPreferences, String str) {
        String string = sharedPreferences.getString(zzao.zzd(str, "cre"), null);
        if (string != null) {
            try {
                return Long.parseLong(string);
            } catch (NumberFormatException unused) {
                return 0L;
            }
        }
        return 0L;
    }

    private static File zzb(Context context) {
        File noBackupFilesDir = ContextCompat.getNoBackupFilesDir(context);
        if (noBackupFilesDir == null || !noBackupFilesDir.isDirectory()) {
            Log.w("FirebaseInstanceId", "noBackupFilesDir doesn't exist, using regular files directory instead");
            return context.getFilesDir();
        }
        return noBackupFilesDir;
    }

    private final void zzb(Context context, String str, zzq zzqVar) {
        String zzq;
        String zzr;
        long j;
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.google.android.gms.appid", 0);
        try {
            if (zzqVar.equals(zza(sharedPreferences, str))) {
                return;
            }
        } catch (zzr unused) {
        }
        if (Log.isLoggable("FirebaseInstanceId", 3)) {
            Log.d("FirebaseInstanceId", "Writing key to shared preferences");
        }
        SharedPreferences.Editor edit = sharedPreferences.edit();
        String zzd = zzao.zzd(str, "|P|");
        zzq = zzqVar.zzq();
        edit.putString(zzd, zzq);
        String zzd2 = zzao.zzd(str, "|K|");
        zzr = zzqVar.zzr();
        edit.putString(zzd2, zzr);
        String zzd3 = zzao.zzd(str, "cre");
        j = zzqVar.zzbe;
        edit.putString(zzd3, String.valueOf(j));
        edit.commit();
    }

    private static KeyPair zzc(String str, String str2) throws zzr {
        try {
            byte[] decode = Base64.decode(str, 8);
            byte[] decode2 = Base64.decode(str2, 8);
            try {
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                return new KeyPair(keyFactory.generatePublic(new X509EncodedKeySpec(decode)), keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decode2)));
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                String valueOf = String.valueOf(e);
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 19);
                sb.append("Invalid key stored ");
                sb.append(valueOf);
                Log.w("FirebaseInstanceId", sb.toString());
                throw new zzr(e);
            }
        } catch (IllegalArgumentException e2) {
            throw new zzr(e2);
        }
    }

    @Nullable
    private final zzq zzd(Context context, String str) throws zzr {
        zzq zze;
        try {
            zze = zze(context, str);
        } catch (zzr e) {
            e = e;
        }
        if (zze != null) {
            zzb(context, str, zze);
            return zze;
        }
        e = null;
        try {
            zzq zza = zza(context.getSharedPreferences("com.google.android.gms.appid", 0), str);
            if (zza != null) {
                zza(context, str, zza);
                return zza;
            }
        } catch (zzr e2) {
            e = e2;
        }
        if (e == null) {
            return null;
        }
        throw e;
    }

    @Nullable
    private final zzq zze(Context context, String str) throws zzr {
        File zzf = zzf(context, str);
        if (zzf.exists()) {
            try {
                return zza(zzf);
            } catch (IOException e) {
                if (Log.isLoggable("FirebaseInstanceId", 3)) {
                    String valueOf = String.valueOf(e);
                    StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 40);
                    sb.append("Failed to read key from file, retrying: ");
                    sb.append(valueOf);
                    Log.d("FirebaseInstanceId", sb.toString());
                }
                try {
                    return zza(zzf);
                } catch (IOException e2) {
                    String valueOf2 = String.valueOf(e2);
                    StringBuilder sb2 = new StringBuilder(String.valueOf(valueOf2).length() + 45);
                    sb2.append("IID file exists, but failed to read from it: ");
                    sb2.append(valueOf2);
                    Log.w("FirebaseInstanceId", sb2.toString());
                    throw new zzr(e2);
                }
            }
        }
        return null;
    }

    private static File zzf(Context context, String str) {
        String sb;
        if (TextUtils.isEmpty(str)) {
            sb = "com.google.InstanceId.properties";
        } else {
            try {
                String encodeToString = Base64.encodeToString(str.getBytes(Key.STRING_CHARSET_NAME), 11);
                StringBuilder sb2 = new StringBuilder(String.valueOf(encodeToString).length() + 33);
                sb2.append("com.google.InstanceId_");
                sb2.append(encodeToString);
                sb2.append(".properties");
                sb = sb2.toString();
            } catch (UnsupportedEncodingException e) {
                throw new AssertionError(e);
            }
        }
        return new File(zzb(context), sb);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final zzq zzb(Context context, String str) throws zzr {
        zzq zzd = zzd(context, str);
        return zzd != null ? zzd : zzc(context, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final zzq zzc(Context context, String str) {
        zzq zzqVar = new zzq(zza.zzb(), System.currentTimeMillis());
        try {
            zzq zzd = zzd(context, str);
            if (zzd != null) {
                if (Log.isLoggable("FirebaseInstanceId", 3)) {
                    Log.d("FirebaseInstanceId", "Loaded key after generating new one, using loaded one");
                }
                return zzd;
            }
        } catch (zzr unused) {
        }
        if (Log.isLoggable("FirebaseInstanceId", 3)) {
            Log.d("FirebaseInstanceId", "Generated new key");
        }
        zza(context, str, zzqVar);
        zzb(context, str, zzqVar);
        return zzqVar;
    }
}
