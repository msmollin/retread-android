package com.google.android.gms.common;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.internal.ICertData;
import com.google.android.gms.common.internal.IGoogleCertificatesApi;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.dynamite.DynamiteModule;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.CheckReturnValue;
import javax.annotation.concurrent.GuardedBy;

/* JADX INFO: Access modifiers changed from: package-private */
@CheckReturnValue
/* loaded from: classes.dex */
public final class GoogleCertificates {
    private static volatile IGoogleCertificatesApi zzax;
    private static final Object zzay = new Object();
    private static Context zzaz;
    @GuardedBy("GoogleCertificates.class")
    private static Set<ICertData> zzba;
    @GuardedBy("GoogleCertificates.class")
    private static Set<ICertData> zzbb;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static abstract class CertData extends ICertData.Stub {
        private int zzbc;

        /* JADX INFO: Access modifiers changed from: protected */
        public CertData(byte[] bArr) {
            Preconditions.checkArgument(bArr.length == 25);
            this.zzbc = Arrays.hashCode(bArr);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public static byte[] zzd(String str) {
            try {
                return str.getBytes("ISO-8859-1");
            } catch (UnsupportedEncodingException e) {
                throw new AssertionError(e);
            }
        }

        public boolean equals(Object obj) {
            IObjectWrapper bytesWrapped;
            if (obj != null && (obj instanceof ICertData)) {
                try {
                    ICertData iCertData = (ICertData) obj;
                    if (iCertData.getHashCode() == hashCode() && (bytesWrapped = iCertData.getBytesWrapped()) != null) {
                        return Arrays.equals(getBytes(), (byte[]) ObjectWrapper.unwrap(bytesWrapped));
                    }
                    return false;
                } catch (RemoteException e) {
                    Log.e("GoogleCertificates", "Failed to get Google certificates from remote", e);
                }
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract byte[] getBytes();

        @Override // com.google.android.gms.common.internal.ICertData
        public IObjectWrapper getBytesWrapped() {
            return ObjectWrapper.wrap(getBytes());
        }

        @Override // com.google.android.gms.common.internal.ICertData
        public int getHashCode() {
            return hashCode();
        }

        public int hashCode() {
            return this.zzbc;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized void init(Context context) {
        synchronized (GoogleCertificates.class) {
            if (zzaz != null) {
                Log.w("GoogleCertificates", "GoogleCertificates has been initialized already");
            } else if (context != null) {
                zzaz = context.getApplicationContext();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzg zza(String str, CertData certData, boolean z) {
        String str2;
        try {
            zzc();
            Preconditions.checkNotNull(zzaz);
            try {
                if (zzax.isGoogleOrPlatformSigned(new GoogleCertificatesQuery(str, certData, z), ObjectWrapper.wrap(zzaz.getPackageManager()))) {
                    return zzg.zzg();
                }
                boolean z2 = true;
                return zzg.zza(str, certData, z, (z || !zza(str, certData, true).zzbl) ? false : false);
            } catch (RemoteException e) {
                e = e;
                Log.e("GoogleCertificates", "Failed to get Google certificates from remote", e);
                str2 = "module call";
                return zzg.zza(str2, e);
            }
        } catch (DynamiteModule.LoadingException e2) {
            e = e2;
            str2 = "module init";
        }
    }

    private static Set<ICertData> zza(IBinder[] iBinderArr) throws RemoteException {
        HashSet hashSet = new HashSet(iBinderArr.length);
        for (IBinder iBinder : iBinderArr) {
            ICertData asInterface = ICertData.Stub.asInterface(iBinder);
            if (asInterface != null) {
                hashSet.add(asInterface);
            }
        }
        return hashSet;
    }

    private static void zzc() throws DynamiteModule.LoadingException {
        if (zzax != null) {
            return;
        }
        Preconditions.checkNotNull(zzaz);
        synchronized (zzay) {
            if (zzax == null) {
                zzax = IGoogleCertificatesApi.Stub.asInterface(DynamiteModule.load(zzaz, DynamiteModule.PREFER_HIGHEST_OR_LOCAL_VERSION_NO_FORCE_STAGING, "com.google.android.gms.googlecertificates").instantiate("com.google.android.gms.common.GoogleCertificatesImpl"));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized Set<ICertData> zzd() {
        synchronized (GoogleCertificates.class) {
            if (zzba != null) {
                return zzba;
            }
            try {
                zzc();
                try {
                    IObjectWrapper googleCertificates = zzax.getGoogleCertificates();
                    if (googleCertificates == null) {
                        Log.e("GoogleCertificates", "Failed to get Google certificates from remote");
                        return Collections.emptySet();
                    }
                    zzba = zza((IBinder[]) ObjectWrapper.unwrap(googleCertificates));
                    return zzba;
                } catch (RemoteException e) {
                    Log.e("GoogleCertificates", "Failed to get Google certificates from remote", e);
                    return Collections.emptySet();
                }
            } catch (DynamiteModule.LoadingException e2) {
                Log.e("GoogleCertificates", "Failed to load com.google.android.gms.googlecertificates", e2);
                return Collections.emptySet();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized Set<ICertData> zze() {
        synchronized (GoogleCertificates.class) {
            if (zzbb != null) {
                return zzbb;
            }
            try {
                zzc();
                try {
                    IObjectWrapper googleReleaseCertificates = zzax.getGoogleReleaseCertificates();
                    if (googleReleaseCertificates == null) {
                        Log.e("GoogleCertificates", "Failed to get Google certificates from remote");
                        return Collections.emptySet();
                    }
                    zzbb = zza((IBinder[]) ObjectWrapper.unwrap(googleReleaseCertificates));
                    return zzbb;
                } catch (RemoteException e) {
                    Log.e("GoogleCertificates", "Failed to get Google certificates from remote", e);
                    return Collections.emptySet();
                }
            } catch (DynamiteModule.LoadingException e2) {
                Log.e("GoogleCertificates", "Failed to load com.google.android.gms.googlecertificates", e2);
                return Collections.emptySet();
            }
        }
    }
}
