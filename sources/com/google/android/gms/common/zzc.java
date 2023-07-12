package com.google.android.gms.common;

import com.google.android.gms.common.GoogleCertificates;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
abstract class zzc extends GoogleCertificates.CertData {
    private static final WeakReference<byte[]> zzbf = new WeakReference<>(null);
    private WeakReference<byte[]> zzbe;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzc(byte[] bArr) {
        super(bArr);
        this.zzbe = zzbf;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.common.GoogleCertificates.CertData
    public final byte[] getBytes() {
        byte[] bArr;
        synchronized (this) {
            bArr = this.zzbe.get();
            if (bArr == null) {
                bArr = zzf();
                this.zzbe = new WeakReference<>(bArr);
            }
        }
        return bArr;
    }

    protected abstract byte[] zzf();
}
