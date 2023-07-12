package com.google.android.gms.internal.measurement;

import android.util.Log;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzwy extends zzws<Boolean> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzwy(zzxc zzxcVar, String str, Boolean bool) {
        super(zzxcVar, str, bool, null);
    }

    @Override // com.google.android.gms.internal.measurement.zzws
    protected final /* synthetic */ Boolean zzey(String str) {
        if (zzwn.zzbmi.matcher(str).matches()) {
            return true;
        }
        if (zzwn.zzbmj.matcher(str).matches()) {
            return false;
        }
        String str2 = this.zzbnh;
        StringBuilder sb = new StringBuilder(String.valueOf(str2).length() + 28 + String.valueOf(str).length());
        sb.append("Invalid boolean value for ");
        sb.append(str2);
        sb.append(": ");
        sb.append(str);
        Log.e("PhenotypeFlag", sb.toString());
        return null;
    }
}
