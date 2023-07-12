package com.google.android.gms.common;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.common.GoogleCertificates;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

@CheckReturnValue
/* loaded from: classes.dex */
class zzg {
    private static final zzg zzbk = new zzg(true, null, null);
    private final Throwable cause;
    final boolean zzbl;
    private final String zzbm;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzg(boolean z, @Nullable String str, @Nullable Throwable th) {
        this.zzbl = z;
        this.zzbm = str;
        this.cause = th;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzg zza(String str, GoogleCertificates.CertData certData, boolean z, boolean z2) {
        return new zzi(str, certData, z, z2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzg zza(@NonNull String str, @NonNull Throwable th) {
        return new zzg(false, str, th);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzg zze(@NonNull String str) {
        return new zzg(false, str, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzg zzg() {
        return zzbk;
    }

    @Nullable
    String getErrorMessage() {
        return this.zzbm;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zzh() throws SecurityException {
        if (this.zzbl) {
            return;
        }
        String valueOf = String.valueOf("GoogleCertificatesRslt: ");
        String valueOf2 = String.valueOf(getErrorMessage());
        String concat = valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf);
        if (this.cause == null) {
            throw new SecurityException(concat);
        }
        throw new SecurityException(concat, this.cause);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zzi() {
        if (this.zzbl) {
            return;
        }
        if (this.cause != null) {
            Log.d("GoogleCertificatesRslt", getErrorMessage(), this.cause);
        } else {
            Log.d("GoogleCertificatesRslt", getErrorMessage());
        }
    }
}
