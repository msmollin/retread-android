package com.polidea.rxandroidble2.internal.util;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import bleshadow.javax.inject.Inject;
import com.polidea.rxandroidble2.internal.RxBleLog;

@TargetApi(19)
/* loaded from: classes.dex */
public class CheckerLocationProvider {
    private final ContentResolver contentResolver;
    private final LocationManager locationManager;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Inject
    public CheckerLocationProvider(ContentResolver contentResolver, LocationManager locationManager) {
        this.contentResolver = contentResolver;
        this.locationManager = locationManager;
    }

    public boolean isLocationProviderEnabled() {
        if (Build.VERSION.SDK_INT >= 19) {
            try {
                return Settings.Secure.getInt(this.contentResolver, "location_mode") != 0;
            } catch (Settings.SettingNotFoundException e) {
                RxBleLog.w(e, "Could not use LOCATION_MODE check. Falling back to legacy method.", new Object[0]);
            }
        }
        return this.locationManager.isProviderEnabled("network") || this.locationManager.isProviderEnabled("gps");
    }
}
