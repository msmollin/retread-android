package com.google.firebase.iid;

import android.util.Base64;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.util.VisibleForTesting;
import java.security.KeyPair;

/* loaded from: classes.dex */
final class zzq {
    private final KeyPair zzbd;
    private final long zzbe;

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public zzq(KeyPair keyPair, long j) {
        this.zzbd = keyPair;
        this.zzbe = j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final String zzq() {
        return Base64.encodeToString(this.zzbd.getPublic().getEncoded(), 11);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final String zzr() {
        return Base64.encodeToString(this.zzbd.getPrivate().getEncoded(), 11);
    }

    public final boolean equals(Object obj) {
        if (obj instanceof zzq) {
            zzq zzqVar = (zzq) obj;
            return this.zzbe == zzqVar.zzbe && this.zzbd.getPublic().equals(zzqVar.zzbd.getPublic()) && this.zzbd.getPrivate().equals(zzqVar.zzbd.getPrivate());
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final long getCreationTime() {
        return this.zzbe;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final KeyPair getKeyPair() {
        return this.zzbd;
    }

    public final int hashCode() {
        return Objects.hashCode(this.zzbd.getPublic(), this.zzbd.getPrivate(), Long.valueOf(this.zzbe));
    }
}
