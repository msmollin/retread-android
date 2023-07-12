package com.polidea.rxandroidble2.internal;

import bleshadow.dagger.Subcomponent;
import com.polidea.rxandroidble2.RxBleDevice;

@DeviceScope
@Subcomponent(modules = {DeviceModule.class, DeviceModuleBinder.class})
/* loaded from: classes.dex */
public interface DeviceComponent {

    @Subcomponent.Builder
    /* loaded from: classes.dex */
    public interface Builder {
        DeviceComponent build();

        Builder deviceModule(DeviceModule deviceModule);
    }

    @DeviceScope
    RxBleDevice provideDevice();
}
