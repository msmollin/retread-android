package com.google.android.gms.common.data;

import com.google.android.gms.common.data.TextFilterable;

/* loaded from: classes.dex */
final class zzc implements TextFilterable.StringFilter {
    @Override // com.google.android.gms.common.data.TextFilterable.StringFilter
    public final boolean matches(String str, String str2) {
        return str.contains(str2);
    }
}
