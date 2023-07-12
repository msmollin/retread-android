package com.opentok.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import com.opentok.android.OtLog;
import com.opentok.android.Publisher;
import com.opentok.android.Session;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class VideoCaptureFactory {
    private static boolean camera2Enabled;
    private static final OtLog.LogToken log = new OtLog.LogToken();

    static {
        boolean z = false;
        camera2Enabled = false;
        Session.SessionOptions sessionOptions = new Session.SessionOptions() { // from class: com.opentok.android.VideoCaptureFactory.1
        };
        if (Build.VERSION.SDK_INT >= 21 && sessionOptions.isCamera2Capable()) {
            z = true;
        }
        camera2Enabled = z;
    }

    VideoCaptureFactory() {
    }

    public static BaseVideoCapturer constructCapturer(Context context) {
        return constructCapturer(context, Publisher.CameraCaptureResolution.defaultResolution(), Publisher.CameraCaptureFrameRate.defaultFrameRate());
    }

    @SuppressLint({"LogNotTimber"})
    public static BaseVideoCapturer constructCapturer(Context context, Publisher.CameraCaptureResolution cameraCaptureResolution, Publisher.CameraCaptureFrameRate cameraCaptureFrameRate) {
        if (isCamera2Capable()) {
            log.i("Using Camera2 Capturer", new Object[0]);
            return new Camera2VideoCapturer(context, cameraCaptureResolution, cameraCaptureFrameRate);
        }
        log.i("Using Camera Capturer", new Object[0]);
        return new DefaultVideoCapturer(context, cameraCaptureResolution, cameraCaptureFrameRate);
    }

    public static void enableCamera2api(boolean z) {
        camera2Enabled = z;
    }

    private static boolean isCamera2Capable() {
        return camera2Enabled;
    }
}
