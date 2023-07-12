package jp.co.cyberagent.android.gpuimage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import androidx.core.view.ViewCompat;
import com.google.android.gms.common.util.CrashUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.concurrent.Semaphore;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.util.Rotation;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* loaded from: classes2.dex */
public class GPUImageView extends FrameLayout {
    public static final int RENDERMODE_CONTINUOUSLY = 1;
    public static final int RENDERMODE_WHEN_DIRTY = 0;
    private GPUImageFilter filter;
    public Size forceSize;
    private GPUImage gpuImage;
    private boolean isShowLoading;
    private float ratio;
    private int surfaceType;
    private View surfaceView;

    /* loaded from: classes2.dex */
    public interface OnPictureSavedListener {
        void onPictureSaved(Uri uri);
    }

    public GPUImageView(Context context) {
        super(context);
        this.surfaceType = 0;
        this.isShowLoading = true;
        this.forceSize = null;
        this.ratio = 0.0f;
        init(context, null);
    }

    public GPUImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.surfaceType = 0;
        this.isShowLoading = true;
        this.forceSize = null;
        this.ratio = 0.0f;
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.GPUImageView, 0, 0);
            try {
                this.surfaceType = obtainStyledAttributes.getInt(R.styleable.GPUImageView_gpuimage_surface_type, this.surfaceType);
                this.isShowLoading = obtainStyledAttributes.getBoolean(R.styleable.GPUImageView_gpuimage_show_loading, this.isShowLoading);
            } finally {
                obtainStyledAttributes.recycle();
            }
        }
        this.gpuImage = new GPUImage(context);
        if (this.surfaceType == 1) {
            this.surfaceView = new GPUImageGLTextureView(context, attributeSet);
            this.gpuImage.setGLTextureView((GLTextureView) this.surfaceView);
        } else {
            this.surfaceView = new GPUImageGLSurfaceView(context, attributeSet);
            this.gpuImage.setGLSurfaceView((GLSurfaceView) this.surfaceView);
        }
        addView(this.surfaceView);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        if (this.ratio != 0.0f) {
            int size = View.MeasureSpec.getSize(i);
            int size2 = View.MeasureSpec.getSize(i2);
            float f = size;
            float f2 = size2;
            if (f / this.ratio < f2) {
                size2 = Math.round(f / this.ratio);
            } else {
                size = Math.round(f2 * this.ratio);
            }
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(size, CrashUtils.ErrorDialogData.SUPPRESSED), View.MeasureSpec.makeMeasureSpec(size2, CrashUtils.ErrorDialogData.SUPPRESSED));
            return;
        }
        super.onMeasure(i, i2);
    }

    public GPUImage getGPUImage() {
        return this.gpuImage;
    }

    @Deprecated
    public void setUpCamera(Camera camera) {
        this.gpuImage.setUpCamera(camera);
    }

    @Deprecated
    public void setUpCamera(Camera camera, int i, boolean z, boolean z2) {
        this.gpuImage.setUpCamera(camera, i, z, z2);
    }

    public void updatePreviewFrame(byte[] bArr, int i, int i2) {
        this.gpuImage.updatePreviewFrame(bArr, i, i2);
    }

    public void setBackgroundColor(float f, float f2, float f3) {
        this.gpuImage.setBackgroundColor(f, f2, f3);
    }

    public void setRenderMode(int i) {
        if (this.surfaceView instanceof GLSurfaceView) {
            ((GLSurfaceView) this.surfaceView).setRenderMode(i);
        } else if (this.surfaceView instanceof GLTextureView) {
            ((GLTextureView) this.surfaceView).setRenderMode(i);
        }
    }

    public void setRatio(float f) {
        this.ratio = f;
        this.surfaceView.requestLayout();
        this.gpuImage.deleteImage();
    }

    public void setScaleType(GPUImage.ScaleType scaleType) {
        this.gpuImage.setScaleType(scaleType);
    }

    public void setRotation(Rotation rotation) {
        this.gpuImage.setRotation(rotation);
        requestRender();
    }

    public void setFilter(GPUImageFilter gPUImageFilter) {
        this.filter = gPUImageFilter;
        this.gpuImage.setFilter(gPUImageFilter);
        requestRender();
    }

    public GPUImageFilter getFilter() {
        return this.filter;
    }

    public void setImage(Bitmap bitmap) {
        this.gpuImage.setImage(bitmap);
    }

    public void setImage(Uri uri) {
        this.gpuImage.setImage(uri);
    }

    public void setImage(File file) {
        this.gpuImage.setImage(file);
    }

    public void requestRender() {
        if (this.surfaceView instanceof GLSurfaceView) {
            ((GLSurfaceView) this.surfaceView).requestRender();
        } else if (this.surfaceView instanceof GLTextureView) {
            ((GLTextureView) this.surfaceView).requestRender();
        }
    }

    public void saveToPictures(String str, String str2, OnPictureSavedListener onPictureSavedListener) {
        new SaveTask(this, str, str2, onPictureSavedListener).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public void saveToPictures(String str, String str2, int i, int i2, OnPictureSavedListener onPictureSavedListener) {
        new SaveTask(str, str2, i, i2, onPictureSavedListener).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public Bitmap capture(int i, int i2) throws InterruptedException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException("Do not call this method from the UI thread!");
        }
        this.forceSize = new Size(i, i2);
        final Semaphore semaphore = new Semaphore(0);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: jp.co.cyberagent.android.gpuimage.GPUImageView.1
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    GPUImageView.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    GPUImageView.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                semaphore.release();
            }
        });
        post(new Runnable() { // from class: jp.co.cyberagent.android.gpuimage.GPUImageView.2
            @Override // java.lang.Runnable
            public void run() {
                if (GPUImageView.this.isShowLoading) {
                    GPUImageView.this.addView(new LoadingView(GPUImageView.this.getContext()));
                }
                GPUImageView.this.surfaceView.requestLayout();
            }
        });
        semaphore.acquire();
        this.gpuImage.runOnGLThread(new Runnable() { // from class: jp.co.cyberagent.android.gpuimage.GPUImageView.3
            @Override // java.lang.Runnable
            public void run() {
                semaphore.release();
            }
        });
        requestRender();
        semaphore.acquire();
        Bitmap capture = capture();
        this.forceSize = null;
        post(new Runnable() { // from class: jp.co.cyberagent.android.gpuimage.GPUImageView.4
            @Override // java.lang.Runnable
            public void run() {
                GPUImageView.this.surfaceView.requestLayout();
            }
        });
        requestRender();
        if (this.isShowLoading) {
            postDelayed(new Runnable() { // from class: jp.co.cyberagent.android.gpuimage.GPUImageView.5
                @Override // java.lang.Runnable
                public void run() {
                    GPUImageView.this.removeViewAt(1);
                }
            }, 300L);
        }
        return capture;
    }

    public Bitmap capture() throws InterruptedException {
        final Semaphore semaphore = new Semaphore(0);
        final Bitmap createBitmap = Bitmap.createBitmap(this.surfaceView.getMeasuredWidth(), this.surfaceView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        this.gpuImage.runOnGLThread(new Runnable() { // from class: jp.co.cyberagent.android.gpuimage.GPUImageView.6
            @Override // java.lang.Runnable
            public void run() {
                GPUImageNativeLibrary.adjustBitmap(createBitmap);
                semaphore.release();
            }
        });
        requestRender();
        semaphore.acquire();
        return createBitmap;
    }

    public void onPause() {
        if (this.surfaceView instanceof GLSurfaceView) {
            ((GLSurfaceView) this.surfaceView).onPause();
        } else if (this.surfaceView instanceof GLTextureView) {
            ((GLTextureView) this.surfaceView).onPause();
        }
    }

    public void onResume() {
        if (this.surfaceView instanceof GLSurfaceView) {
            ((GLSurfaceView) this.surfaceView).onResume();
        } else if (this.surfaceView instanceof GLTextureView) {
            ((GLTextureView) this.surfaceView).onResume();
        }
    }

    /* loaded from: classes2.dex */
    public static class Size {
        int height;
        int width;

        public Size(int i, int i2) {
            this.width = i;
            this.height = i2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class GPUImageGLSurfaceView extends GLSurfaceView {
        public GPUImageGLSurfaceView(Context context) {
            super(context);
        }

        public GPUImageGLSurfaceView(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        @Override // android.view.SurfaceView, android.view.View
        protected void onMeasure(int i, int i2) {
            if (GPUImageView.this.forceSize != null) {
                super.onMeasure(View.MeasureSpec.makeMeasureSpec(GPUImageView.this.forceSize.width, CrashUtils.ErrorDialogData.SUPPRESSED), View.MeasureSpec.makeMeasureSpec(GPUImageView.this.forceSize.height, CrashUtils.ErrorDialogData.SUPPRESSED));
            } else {
                super.onMeasure(i, i2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class GPUImageGLTextureView extends GLTextureView {
        public GPUImageGLTextureView(Context context) {
            super(context);
        }

        public GPUImageGLTextureView(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            if (GPUImageView.this.forceSize != null) {
                super.onMeasure(View.MeasureSpec.makeMeasureSpec(GPUImageView.this.forceSize.width, CrashUtils.ErrorDialogData.SUPPRESSED), View.MeasureSpec.makeMeasureSpec(GPUImageView.this.forceSize.height, CrashUtils.ErrorDialogData.SUPPRESSED));
            } else {
                super.onMeasure(i, i2);
            }
        }
    }

    /* loaded from: classes2.dex */
    private class LoadingView extends FrameLayout {
        public LoadingView(Context context) {
            super(context);
            init();
        }

        public LoadingView(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            init();
        }

        public LoadingView(Context context, AttributeSet attributeSet, int i) {
            super(context, attributeSet, i);
            init();
        }

        private void init() {
            ProgressBar progressBar = new ProgressBar(getContext());
            progressBar.setLayoutParams(new FrameLayout.LayoutParams(-2, -2, 17));
            addView(progressBar);
            setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        }
    }

    /* loaded from: classes2.dex */
    private class SaveTask extends AsyncTask<Void, Void, Void> {
        private final String fileName;
        private final String folderName;
        private final Handler handler;
        private final int height;
        private final OnPictureSavedListener listener;
        private final int width;

        public SaveTask(GPUImageView gPUImageView, String str, String str2, OnPictureSavedListener onPictureSavedListener) {
            this(str, str2, 0, 0, onPictureSavedListener);
        }

        public SaveTask(String str, String str2, int i, int i2, OnPictureSavedListener onPictureSavedListener) {
            this.folderName = str;
            this.fileName = str2;
            this.width = i;
            this.height = i2;
            this.listener = onPictureSavedListener;
            this.handler = new Handler();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Void doInBackground(Void... voidArr) {
            try {
                saveImage(this.folderName, this.fileName, this.width != 0 ? GPUImageView.this.capture(this.width, this.height) : GPUImageView.this.capture());
                return null;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }

        private void saveImage(String str, String str2, Bitmap bitmap) {
            File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File file = new File(externalStoragePublicDirectory, str + MqttTopic.TOPIC_LEVEL_SEPARATOR + str2);
            try {
                file.getParentFile().mkdirs();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(file));
                MediaScannerConnection.scanFile(GPUImageView.this.getContext(), new String[]{file.toString()}, null, new MediaScannerConnection.OnScanCompletedListener() { // from class: jp.co.cyberagent.android.gpuimage.GPUImageView.SaveTask.1
                    @Override // android.media.MediaScannerConnection.OnScanCompletedListener
                    public void onScanCompleted(String str3, final Uri uri) {
                        if (SaveTask.this.listener != null) {
                            SaveTask.this.handler.post(new Runnable() { // from class: jp.co.cyberagent.android.gpuimage.GPUImageView.SaveTask.1.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    SaveTask.this.listener.onPictureSaved(uri);
                                }
                            });
                        }
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
