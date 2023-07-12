package com.bambuser.broadcaster;

import android.content.Context;
import android.location.Location;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.bambuser.broadcaster.Capturer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class VideoCapturerBase implements SurfaceHolder.Callback {
    static final long CAMERA_WAIT_MILLIS = 2000;
    private static final ArrayList<ResolutionScanListener> LISTENERS = new ArrayList<>();
    static final int MAX_PICTURE_HEIGHT = 1080;
    static final int MAX_PICTURE_WIDTH = 1920;
    final Capturer mCapturer;
    final Context mContext;
    final SurfaceHolder mPreviewSurfaceHolder;
    final SurfaceView mPreviewSurfaceView;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface ResolutionScanListener {
        void onResolutionsScanned();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface TakePictureCallback {
        Location onGetLocation();

        Resolution onGetResolution();

        void onJpegData(ByteBuffer byteBuffer);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void focus();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract int getZoom();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract List<Integer> getZoomRatios();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean hasFocus();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean hasTorch();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean hasZoom();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void initVideoCapture();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void refreshCameraState(boolean z);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void refreshCaptureRotation();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void refreshCropResolution();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void refreshPreviewResolution();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void refreshPreviewRotation();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void reuseFrame(Frame frame);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void setCameraObserver(Capturer.CameraInterface cameraInterface);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void setZoom(int i);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void startVideoCapture(InfoFrame infoFrame);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void stepZoom(boolean z);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void stopVideoCapture();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void takePicture(TakePictureCallback takePictureCallback);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void toggleTorch();

    /* JADX INFO: Access modifiers changed from: package-private */
    public VideoCapturerBase(Capturer capturer, Context context, SurfaceView surfaceView) {
        this.mCapturer = capturer;
        this.mContext = context;
        this.mPreviewSurfaceView = surfaceView;
        this.mPreviewSurfaceHolder = surfaceView.getHolder();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void prepareSurface() {
        this.mPreviewSurfaceHolder.addCallback(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void close() {
        this.mPreviewSurfaceHolder.removeCallback(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized void registerResolutionScanListener(ResolutionScanListener resolutionScanListener) {
        synchronized (VideoCapturerBase.class) {
            LISTENERS.add(resolutionScanListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized void unregisterResolutionScanListener(ResolutionScanListener resolutionScanListener) {
        synchronized (VideoCapturerBase.class) {
            LISTENERS.remove(resolutionScanListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void internalBroadcastResolutions() {
        ArrayList arrayList;
        synchronized (VideoCapturerBase.class) {
            arrayList = new ArrayList(LISTENERS);
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ((ResolutionScanListener) it.next()).onResolutionsScanned();
        }
    }
}
