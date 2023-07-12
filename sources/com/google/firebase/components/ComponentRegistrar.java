package com.google.firebase.components;

import com.google.android.gms.common.annotation.KeepForSdk;
import java.util.List;

@KeepForSdk
/* loaded from: classes.dex */
public interface ComponentRegistrar {
    @KeepForSdk
    List<Component<?>> getComponents();
}
