package com.google.android.gms.internal.measurement;

import java.util.Arrays;

/* loaded from: classes.dex */
final class zzacg {
    final int tag;
    final byte[] zzbrc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzacg(int i, byte[] bArr) {
        this.tag = i;
        this.zzbrc = bArr;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof zzacg) {
            zzacg zzacgVar = (zzacg) obj;
            return this.tag == zzacgVar.tag && Arrays.equals(this.zzbrc, zzacgVar.zzbrc);
        }
        return false;
    }

    public final int hashCode() {
        return ((this.tag + 527) * 31) + Arrays.hashCode(this.zzbrc);
    }
}
