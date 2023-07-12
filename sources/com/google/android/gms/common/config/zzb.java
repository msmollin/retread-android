package com.google.android.gms.common.config;

import android.content.Context;
import com.google.android.gms.common.config.GservicesValue;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzb extends GservicesValue<Long> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzb(String str, Long l) {
        super(str, l);
    }

    private static Long zza(Context context, String str, Long l) {
        String string = context.getSharedPreferences("gservices-direboot-cache", 0).getString(str, null);
        if (string != null) {
            try {
                return Long.valueOf(Long.parseLong(string));
            } catch (NumberFormatException unused) {
            }
        }
        return l;
    }

    @Override // com.google.android.gms.common.config.GservicesValue
    protected final /* synthetic */ Long retrieve(String str) {
        GservicesValue.zza zzaVar;
        zzaVar = GservicesValue.zzmu;
        return zzaVar.getLong(this.mKey, (Long) this.mDefaultValue);
    }

    @Override // com.google.android.gms.common.config.GservicesValue
    protected final /* synthetic */ Long retrieveFromDirectBootCache(Context context, String str, Long l) {
        return zza(context, str, l);
    }
}
