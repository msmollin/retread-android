package com.bambuser.broadcaster;

import android.location.Location;
import java.io.File;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public interface PictureCallback {
    File onGetFile();

    Location onGetLocation();

    Resolution onGetResolution();

    void onPictureStored(File file);
}
