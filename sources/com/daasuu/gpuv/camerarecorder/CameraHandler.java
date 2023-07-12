package com.daasuu.gpuv.camerarecorder;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/* loaded from: classes.dex */
public class CameraHandler extends Handler {
    private static final boolean DEBUG = false;
    private static final int MSG_AUTO_FOCUS = 5;
    private static final int MSG_MANUAL_FOCUS = 3;
    private static final int MSG_PREVIEW_START = 1;
    private static final int MSG_PREVIEW_STOP = 2;
    private static final int MSG_SWITCH_FLASH = 4;
    private static final String TAG = "CameraHandler";
    private CameraThread thread;
    private int viewWidth = 0;
    private int viewHeight = 0;
    private float eventX = 0.0f;
    private float eventY = 0.0f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CameraHandler(CameraThread cameraThread) {
        this.thread = cameraThread;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startPreview(int i, int i2) {
        sendMessage(obtainMessage(1, i, i2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stopPreview(boolean z) {
        synchronized (this) {
            sendEmptyMessage(2);
            if (this.thread == null) {
                return;
            }
            if (z && this.thread.isRunning) {
                try {
                    wait();
                } catch (InterruptedException unused) {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void changeManualFocusPoint(float f, float f2, int i, int i2) {
        this.viewWidth = i;
        this.viewHeight = i2;
        this.eventX = f;
        this.eventY = f2;
        sendMessage(obtainMessage(3));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void changeAutoFocus() {
        sendMessage(obtainMessage(5));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void switchFlashMode() {
        sendMessage(obtainMessage(4));
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        switch (message.what) {
            case 1:
                if (this.thread != null) {
                    this.thread.startPreview(message.arg1, message.arg2);
                    return;
                }
                return;
            case 2:
                if (this.thread != null) {
                    this.thread.stopPreview();
                }
                synchronized (this) {
                    notifyAll();
                }
                try {
                    Looper.myLooper().quit();
                    removeCallbacks(this.thread);
                    removeMessages(1);
                    removeMessages(2);
                    removeMessages(3);
                    removeMessages(4);
                    removeMessages(5);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.thread = null;
                return;
            case 3:
                if (this.thread != null) {
                    this.thread.changeManualFocusPoint(this.eventX, this.eventY, this.viewWidth, this.viewHeight);
                    return;
                }
                return;
            case 4:
                if (this.thread != null) {
                    this.thread.switchFlashMode();
                    return;
                }
                return;
            case 5:
                if (this.thread != null) {
                    this.thread.changeAutoFocus();
                    return;
                }
                return;
            default:
                throw new RuntimeException("unknown message:what=" + message.what);
        }
    }
}
