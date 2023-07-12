package com.daasuu.gpuv.camerarecorder;

/* loaded from: classes.dex */
public interface CameraRecordListener {
    void onCameraThreadFinish();

    void onError(Exception exc);

    void onGetFlashSupport(boolean z);

    void onRecordComplete();

    void onRecordStart();

    void onVideoFileReady();
}
