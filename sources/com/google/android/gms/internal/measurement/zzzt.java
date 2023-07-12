package com.google.android.gms.internal.measurement;

import java.io.IOException;

/* loaded from: classes.dex */
public final class zzzt extends IOException {
    private zzaal zzbst;

    public zzzt(String str) {
        super(str);
        this.zzbst = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzzt zztm() {
        return new zzzt("While parsing a protocol message, the input ended unexpectedly in the middle of a field.  This could mean either that the input has been truncated or that an embedded message misreported its own length.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzzt zztn() {
        return new zzzt("CodedInputStream encountered an embedded string or message which claimed to have negative size.");
    }
}
