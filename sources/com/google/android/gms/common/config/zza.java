package com.google.android.gms.common.config;

import android.content.Context;
import com.google.android.gms.common.config.GservicesValue;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zza extends GservicesValue<Boolean> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zza(String str, Boolean bool) {
        super(str, bool);
    }

    private static Boolean zza(Context context, String str, Boolean bool) {
        String string = context.getSharedPreferences("gservices-direboot-cache", 0).getString(str, null);
        if (string != null) {
            try {
                return Boolean.valueOf(Boolean.parseBoolean(string));
            } catch (NumberFormatException unused) {
            }
        }
        return bool;
    }

    @Override // com.google.android.gms.common.config.GservicesValue
    protected final /* synthetic */ Boolean retrieve(String str) {
        GservicesValue.zza zzaVar;
        zzaVar = GservicesValue.zzmu;
        return zzaVar.zza(this.mKey, (Boolean) this.mDefaultValue);
    }

    @Override // com.google.android.gms.common.config.GservicesValue
    protected final /* synthetic */ Boolean retrieveFromDirectBootCache(Context context, String str, Boolean bool) {
        return zza(context, str, bool);
    }
}
