package com.google.android.gms.internal.measurement;

import com.google.android.gms.common.internal.Preconditions;

/* loaded from: classes.dex */
final class zzjz {
    final String name;
    final String origin;
    final Object value;
    final long zzaqz;
    final String zzti;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzjz(String str, String str2, String str3, long j, Object obj) {
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str3);
        Preconditions.checkNotNull(obj);
        this.zzti = str;
        this.origin = str2;
        this.name = str3;
        this.zzaqz = j;
        this.value = obj;
    }
}
