package com.google.android.gms.internal.measurement;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.util.Log;
import androidx.core.content.PermissionChecker;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class zzws<T> {
    private final zzxc zzbng;
    final String zzbnh;
    private final String zzbni;
    private final T zzbnj;
    private T zzbnk;
    private volatile zzwp zzbnl;
    private volatile SharedPreferences zzbnm;
    private static final Object zzbnc = new Object();
    @SuppressLint({"StaticFieldLeak"})
    private static Context zzqx = null;
    private static boolean zzbnd = false;
    private static volatile Boolean zzbne = null;
    private static volatile Boolean zzbnf = null;

    private zzws(zzxc zzxcVar, String str, T t) {
        Uri uri;
        String str2;
        String str3;
        this.zzbnk = null;
        this.zzbnl = null;
        this.zzbnm = null;
        uri = zzxcVar.zzbns;
        if (uri == null) {
            throw new IllegalArgumentException("Must pass a valid SharedPreferences file name or ContentProvider URI");
        }
        this.zzbng = zzxcVar;
        str2 = zzxcVar.zzbnt;
        String valueOf = String.valueOf(str2);
        String valueOf2 = String.valueOf(str);
        this.zzbni = valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf);
        str3 = zzxcVar.zzbnu;
        String valueOf3 = String.valueOf(str3);
        String valueOf4 = String.valueOf(str);
        this.zzbnh = valueOf4.length() != 0 ? valueOf3.concat(valueOf4) : new String(valueOf3);
        this.zzbnj = t;
    }

    public /* synthetic */ zzws(zzxc zzxcVar, String str, Object obj, zzww zzwwVar) {
        this(zzxcVar, str, obj);
    }

    public static void init(Context context) {
        Context applicationContext;
        synchronized (zzbnc) {
            if ((Build.VERSION.SDK_INT < 24 || !context.isDeviceProtectedStorage()) && (applicationContext = context.getApplicationContext()) != null) {
                context = applicationContext;
            }
            if (zzqx != context) {
                zzbne = null;
            }
            zzqx = context;
        }
        zzbnd = false;
    }

    public static zzws<Double> zza(zzxc zzxcVar, String str, double d) {
        return new zzwz(zzxcVar, str, Double.valueOf(d));
    }

    public static zzws<Integer> zza(zzxc zzxcVar, String str, int i) {
        return new zzwx(zzxcVar, str, Integer.valueOf(i));
    }

    public static zzws<Long> zza(zzxc zzxcVar, String str, long j) {
        return new zzww(zzxcVar, str, Long.valueOf(j));
    }

    public static zzws<String> zza(zzxc zzxcVar, String str, String str2) {
        return new zzxa(zzxcVar, str, str2);
    }

    public static zzws<Boolean> zza(zzxc zzxcVar, String str, boolean z) {
        return new zzwy(zzxcVar, str, Boolean.valueOf(z));
    }

    private static <V> V zza(zzxb<V> zzxbVar) {
        try {
            return zzxbVar.zzsc();
        } catch (SecurityException unused) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return zzxbVar.zzsc();
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    public static boolean zzd(String str, boolean z) {
        try {
            if (zzsa()) {
                return ((Boolean) zza(new zzxb(str, false) { // from class: com.google.android.gms.internal.measurement.zzwv
                    private final String zzbnp;
                    private final boolean zzbnq = false;

                    /* JADX INFO: Access modifiers changed from: package-private */
                    {
                        this.zzbnp = str;
                    }

                    @Override // com.google.android.gms.internal.measurement.zzxb
                    public final Object zzsc() {
                        Boolean valueOf;
                        valueOf = Boolean.valueOf(zzwn.zza(zzws.zzqx.getContentResolver(), this.zzbnp, this.zzbnq));
                        return valueOf;
                    }
                })).booleanValue();
            }
            return false;
        } catch (SecurityException e) {
            Log.e("PhenotypeFlag", "Unable to read GServices, returning default value.", e);
            return false;
        }
    }

    @Nullable
    @TargetApi(24)
    private final T zzry() {
        Uri uri;
        Uri uri2;
        if (zzd("gms:phenotype:phenotype_flag:debug_bypass_phenotype", false)) {
            String valueOf = String.valueOf(this.zzbnh);
            Log.w("PhenotypeFlag", valueOf.length() != 0 ? "Bypass reading Phenotype values for flag: ".concat(valueOf) : new String("Bypass reading Phenotype values for flag: "));
            return null;
        }
        uri = this.zzbng.zzbns;
        if (uri == null) {
            zzxc zzxcVar = this.zzbng;
            return null;
        }
        if (this.zzbnl == null) {
            ContentResolver contentResolver = zzqx.getContentResolver();
            uri2 = this.zzbng.zzbns;
            this.zzbnl = zzwp.zza(contentResolver, uri2);
        }
        String str = (String) zza(new zzxb(this, this.zzbnl) { // from class: com.google.android.gms.internal.measurement.zzwt
            private final zzws zzbnn;
            private final zzwp zzbno;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.zzbnn = this;
                this.zzbno = r2;
            }

            @Override // com.google.android.gms.internal.measurement.zzxb
            public final Object zzsc() {
                return this.zzbno.zzrt().get(this.zzbnn.zzbnh);
            }
        });
        if (str != null) {
            return zzey(str);
        }
        return null;
    }

    @Nullable
    private final T zzrz() {
        zzxc zzxcVar = this.zzbng;
        if (zzsa()) {
            try {
                String str = (String) zza(new zzxb(this) { // from class: com.google.android.gms.internal.measurement.zzwu
                    private final zzws zzbnn;

                    /* JADX INFO: Access modifiers changed from: package-private */
                    {
                        this.zzbnn = this;
                    }

                    @Override // com.google.android.gms.internal.measurement.zzxb
                    public final Object zzsc() {
                        return this.zzbnn.zzsb();
                    }
                });
                if (str != null) {
                    return zzey(str);
                }
                return null;
            } catch (SecurityException e) {
                String valueOf = String.valueOf(this.zzbnh);
                Log.e("PhenotypeFlag", valueOf.length() != 0 ? "Unable to read GServices for flag: ".concat(valueOf) : new String("Unable to read GServices for flag: "), e);
                return null;
            }
        }
        return null;
    }

    private static boolean zzsa() {
        if (zzbne == null) {
            if (zzqx == null) {
                return false;
            }
            zzbne = Boolean.valueOf(PermissionChecker.checkCallingOrSelfPermission(zzqx, "com.google.android.providers.gsf.permission.READ_GSERVICES") == 0);
        }
        return zzbne.booleanValue();
    }

    public final T get() {
        if (zzqx != null) {
            zzxc zzxcVar = this.zzbng;
            T zzry = zzry();
            if (zzry != null) {
                return zzry;
            }
            T zzrz = zzrz();
            return zzrz != null ? zzrz : this.zzbnj;
        }
        throw new IllegalStateException("Must call PhenotypeFlag.init() first");
    }

    protected abstract T zzey(String str);

    public final /* synthetic */ String zzsb() {
        return zzwn.zza(zzqx.getContentResolver(), this.zzbni, (String) null);
    }
}
