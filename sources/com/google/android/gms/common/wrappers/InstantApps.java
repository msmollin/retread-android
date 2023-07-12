package com.google.android.gms.common.wrappers;

import android.content.Context;
import com.google.android.gms.common.util.PlatformVersion;

/* loaded from: classes.dex */
public class InstantApps {
    private static Context zzaay;
    private static Boolean zzaaz;

    public static synchronized boolean isInstantApp(Context context) {
        boolean z;
        synchronized (InstantApps.class) {
            Context applicationContext = context.getApplicationContext();
            if (zzaay != null && zzaaz != null && zzaay == applicationContext) {
                return zzaaz.booleanValue();
            }
            zzaaz = null;
            if (!PlatformVersion.isAtLeastO()) {
                try {
                    context.getClassLoader().loadClass("com.google.android.instantapps.supervisor.InstantAppsRuntime");
                    zzaaz = true;
                } catch (ClassNotFoundException unused) {
                    z = false;
                }
                zzaay = applicationContext;
                return zzaaz.booleanValue();
            }
            z = Boolean.valueOf(applicationContext.getPackageManager().isInstantApp());
            zzaaz = z;
            zzaay = applicationContext;
            return zzaaz.booleanValue();
        }
    }
}
