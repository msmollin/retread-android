package com.google.android.gms.common;

import com.google.android.gms.common.GoogleCertificates;
import java.util.Arrays;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzb extends GoogleCertificates.CertData {
    private final byte[] zzbd;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzb(byte[] bArr) {
        super(Arrays.copyOfRange(bArr, 0, 25));
        this.zzbd = bArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.common.GoogleCertificates.CertData
    public final byte[] getBytes() {
        return this.zzbd;
    }
}
