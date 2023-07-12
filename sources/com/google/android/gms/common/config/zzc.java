package com.google.android.gms.common.config;

import android.content.Context;
import com.google.android.gms.common.config.GservicesValue;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzc extends GservicesValue<Integer> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzc(String str, Integer num) {
        super(str, num);
    }

    private static Integer zza(Context context, String str, Integer num) {
        String string = context.getSharedPreferences("gservices-direboot-cache", 0).getString(str, null);
        if (string != null) {
            try {
                return Integer.valueOf(Integer.parseInt(string));
            } catch (NumberFormatException unused) {
            }
        }
        return num;
    }

    @Override // com.google.android.gms.common.config.GservicesValue
    protected final /* synthetic */ Integer retrieve(String str) {
        GservicesValue.zza zzaVar;
        zzaVar = GservicesValue.zzmu;
        return zzaVar.zza(this.mKey, (Integer) this.mDefaultValue);
    }

    @Override // com.google.android.gms.common.config.GservicesValue
    protected final /* synthetic */ Integer retrieveFromDirectBootCache(Context context, String str, Integer num) {
        return zza(context, str, num);
    }
}
