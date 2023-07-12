package com.google.android.gms.common.internal;

import androidx.annotation.NonNull;

/* loaded from: classes.dex */
public class GmsServiceEndpoint {
    @NonNull
    private final String mPackageName;
    private final int zztq;
    @NonNull
    private final String zzue;
    private final boolean zzuf;

    public GmsServiceEndpoint(@NonNull String str, @NonNull String str2, boolean z, int i) {
        this.mPackageName = str;
        this.zzue = str2;
        this.zzuf = z;
        this.zztq = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int getBindFlags() {
        return this.zztq;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @NonNull
    public final String getPackageName() {
        return this.mPackageName;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @NonNull
    public final String zzcw() {
        return this.zzue;
    }
}
