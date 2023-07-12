package com.google.android.gms.common.config;

import android.content.Context;
import com.google.android.gms.common.config.GservicesValue;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzf extends GservicesValue<String> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzf(String str, String str2) {
        super(str, str2);
    }

    @Override // com.google.android.gms.common.config.GservicesValue
    protected final /* synthetic */ String retrieve(String str) {
        GservicesValue.zza zzaVar;
        zzaVar = GservicesValue.zzmu;
        return zzaVar.getString(this.mKey, (String) this.mDefaultValue);
    }

    @Override // com.google.android.gms.common.config.GservicesValue
    protected final /* synthetic */ String retrieveFromDirectBootCache(Context context, String str, String str2) {
        return context.getSharedPreferences("gservices-direboot-cache", 0).getString(str, str2);
    }
}
