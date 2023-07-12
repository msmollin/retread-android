package com.daasuu.gpuv.camerarecorder;

import android.app.Activity;
import android.content.res.Resources;
import android.hardware.camera2.CameraManager;
import android.opengl.GLSurfaceView;
import android.util.Log;
import com.daasuu.gpuv.egl.filter.GlFilter;

/* loaded from: classes.dex */
public class GPUCameraRecorderBuilder {
    private Activity activity;
    private CameraRecordListener cameraRecordListener;
    private GlFilter glFilter;
    private GLSurfaceView glSurfaceView;
    private Resources resources;
    private LensFacing lensFacing = LensFacing.FRONT;
    private int fileWidth = 720;
    private int fileHeight = 1280;
    private boolean flipVertical = false;
    private boolean flipHorizontal = false;
    private boolean mute = false;
    private boolean recordNoFilter = false;
    private int cameraWidth = 1280;
    private int cameraHeight = 720;

    public GPUCameraRecorderBuilder(Activity activity, GLSurfaceView gLSurfaceView) {
        this.activity = activity;
        this.glSurfaceView = gLSurfaceView;
        this.resources = activity.getResources();
    }

    public GPUCameraRecorderBuilder cameraRecordListener(CameraRecordListener cameraRecordListener) {
        this.cameraRecordListener = cameraRecordListener;
        return this;
    }

    public GPUCameraRecorderBuilder filter(GlFilter glFilter) {
        this.glFilter = glFilter;
        return this;
    }

    public GPUCameraRecorderBuilder videoSize(int i, int i2) {
        this.fileWidth = i;
        this.fileHeight = i2;
        return this;
    }

    public GPUCameraRecorderBuilder cameraSize(int i, int i2) {
        this.cameraWidth = i;
        this.cameraHeight = i2;
        return this;
    }

    public GPUCameraRecorderBuilder lensFacing(LensFacing lensFacing) {
        this.lensFacing = lensFacing;
        return this;
    }

    public GPUCameraRecorderBuilder flipHorizontal(boolean z) {
        this.flipHorizontal = z;
        return this;
    }

    public GPUCameraRecorderBuilder flipVertical(boolean z) {
        this.flipVertical = z;
        return this;
    }

    public GPUCameraRecorderBuilder mute(boolean z) {
        this.mute = z;
        return this;
    }

    public GPUCameraRecorderBuilder recordNoFilter(boolean z) {
        this.recordNoFilter = z;
        return this;
    }

    public GPUCameraRecorder build() {
        int i;
        if (this.glSurfaceView == null) {
            throw new IllegalArgumentException("glSurfaceView and windowManager, multiVideoEffects is NonNull !!");
        }
        CameraManager cameraManager = (CameraManager) this.activity.getSystemService("camera");
        boolean z = this.resources.getConfiguration().orientation == 2;
        if (z) {
            int rotation = this.activity.getWindowManager().getDefaultDisplay().getRotation();
            Log.d("GPUCameraRecorder", "Surface.ROTATION_90 = 1 rotation = " + rotation);
            i = (rotation - 2) * 90;
        } else {
            i = 0;
        }
        GPUCameraRecorder gPUCameraRecorder = new GPUCameraRecorder(this.cameraRecordListener, this.glSurfaceView, this.fileWidth, this.fileHeight, this.cameraWidth, this.cameraHeight, this.lensFacing, this.flipHorizontal, this.flipVertical, this.mute, cameraManager, z, i, this.recordNoFilter);
        gPUCameraRecorder.setFilter(this.glFilter);
        this.activity = null;
        this.resources = null;
        return gPUCameraRecorder;
    }
}
