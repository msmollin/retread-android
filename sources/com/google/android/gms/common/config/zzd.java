package com.google.android.gms.common.config;

import android.content.Context;
import com.google.android.gms.common.config.GservicesValue;

/* loaded from: classes.dex */
final class zzd extends GservicesValue<Double> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzd(String str, Double d) {
        super(str, d);
    }

    private static Double zza(Context context, String str, Double d) {
        String string = context.getSharedPreferences("gservices-direboot-cache", 0).getString(str, null);
        if (string != null) {
            try {
                return Double.valueOf(Double.parseDouble(string));
            } catch (NumberFormatException unused) {
            }
        }
        return d;
    }

    @Override // com.google.android.gms.common.config.GservicesValue
    protected final /* synthetic */ Double retrieve(String str) {
        GservicesValue.zza zzaVar;
        zzaVar = GservicesValue.zzmu;
        return zzaVar.zza(this.mKey, (Double) this.mDefaultValue);
    }

    @Override // com.google.android.gms.common.config.GservicesValue
    protected final /* synthetic */ Double retrieveFromDirectBootCache(Context context, String str, Double d) {
        return zza(context, str, d);
    }
}
