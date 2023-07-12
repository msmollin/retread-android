package jp.co.cyberagent.android.gpuimage;

import android.app.ActivityManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.view.WindowManager;
import com.facebook.common.util.UriUtil;
import com.facebook.imagepipeline.common.RotationOptions;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.util.Rotation;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* loaded from: classes2.dex */
public class GPUImage {
    static final int SURFACE_TYPE_SURFACE_VIEW = 0;
    static final int SURFACE_TYPE_TEXTURE_VIEW = 1;
    private final Context context;
    private Bitmap currentBitmap;
    private GPUImageFilter filter;
    private GLSurfaceView glSurfaceView;
    private GLTextureView glTextureView;
    private final GPUImageRenderer renderer;
    private int scaleHeight;
    private int scaleWidth;
    private int surfaceType = 0;
    private ScaleType scaleType = ScaleType.CENTER_CROP;

    /* loaded from: classes2.dex */
    public interface OnPictureSavedListener {
        void onPictureSaved(Uri uri);
    }

    /* loaded from: classes2.dex */
    public interface ResponseListener<T> {
        void response(T t);
    }

    /* loaded from: classes2.dex */
    public enum ScaleType {
        CENTER_INSIDE,
        CENTER_CROP
    }

    public GPUImage(Context context) {
        if (!supportsOpenGLES2(context)) {
            throw new IllegalStateException("OpenGL ES 2.0 is not supported on this phone.");
        }
        this.context = context;
        this.filter = new GPUImageFilter();
        this.renderer = new GPUImageRenderer(this.filter);
    }

    private boolean supportsOpenGLES2(Context context) {
        return ((ActivityManager) context.getSystemService("activity")).getDeviceConfigurationInfo().reqGlEsVersion >= 131072;
    }

    public void setGLSurfaceView(GLSurfaceView gLSurfaceView) {
        this.surfaceType = 0;
        this.glSurfaceView = gLSurfaceView;
        this.glSurfaceView.setEGLContextClientVersion(2);
        this.glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        this.glSurfaceView.getHolder().setFormat(1);
        this.glSurfaceView.setRenderer(this.renderer);
        this.glSurfaceView.setRenderMode(0);
        this.glSurfaceView.requestRender();
    }

    public void setGLTextureView(GLTextureView gLTextureView) {
        this.surfaceType = 1;
        this.glTextureView = gLTextureView;
        this.glTextureView.setEGLContextClientVersion(2);
        this.glTextureView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        this.glTextureView.setOpaque(false);
        this.glTextureView.setRenderer(this.renderer);
        this.glTextureView.setRenderMode(0);
        this.glTextureView.requestRender();
    }

    public void setBackgroundColor(float f, float f2, float f3) {
        this.renderer.setBackgroundColor(f, f2, f3);
    }

    public void requestRender() {
        if (this.surfaceType == 0) {
            if (this.glSurfaceView != null) {
                this.glSurfaceView.requestRender();
            }
        } else if (this.surfaceType != 1 || this.glTextureView == null) {
        } else {
            this.glTextureView.requestRender();
        }
    }

    @Deprecated
    public void setUpCamera(Camera camera) {
        setUpCamera(camera, 0, false, false);
    }

    @Deprecated
    public void setUpCamera(Camera camera, int i, boolean z, boolean z2) {
        if (this.surfaceType == 0) {
            this.glSurfaceView.setRenderMode(1);
        } else if (this.surfaceType == 1) {
            this.glTextureView.setRenderMode(1);
        }
        this.renderer.setUpSurfaceTexture(camera);
        Rotation rotation = Rotation.NORMAL;
        if (i == 90) {
            rotation = Rotation.ROTATION_90;
        } else if (i == 180) {
            rotation = Rotation.ROTATION_180;
        } else if (i == 270) {
            rotation = Rotation.ROTATION_270;
        }
        this.renderer.setRotationCamera(rotation, z, z2);
    }

    public void setFilter(GPUImageFilter gPUImageFilter) {
        this.filter = gPUImageFilter;
        this.renderer.setFilter(this.filter);
        requestRender();
    }

    public void setImage(Bitmap bitmap) {
        this.currentBitmap = bitmap;
        this.renderer.setImageBitmap(bitmap, false);
        requestRender();
    }

    public void updatePreviewFrame(byte[] bArr, int i, int i2) {
        this.renderer.onPreviewFrame(bArr, i, i2);
    }

    public void setScaleType(ScaleType scaleType) {
        this.scaleType = scaleType;
        this.renderer.setScaleType(scaleType);
        this.renderer.deleteImage();
        this.currentBitmap = null;
        requestRender();
    }

    public int[] getScaleSize() {
        return new int[]{this.scaleWidth, this.scaleHeight};
    }

    public void setRotation(Rotation rotation) {
        this.renderer.setRotation(rotation);
    }

    public void setRotation(Rotation rotation, boolean z, boolean z2) {
        this.renderer.setRotation(rotation, z, z2);
    }

    public void deleteImage() {
        this.renderer.deleteImage();
        this.currentBitmap = null;
        requestRender();
    }

    public void setImage(Uri uri) {
        new LoadImageUriTask(this, uri).execute(new Void[0]);
    }

    public void setImage(File file) {
        new LoadImageFileTask(this, file).execute(new Void[0]);
    }

    private String getPath(Uri uri) {
        Cursor query = this.context.getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
        if (query == null) {
            return null;
        }
        String string = query.moveToFirst() ? query.getString(query.getColumnIndexOrThrow("_data")) : null;
        query.close();
        return string;
    }

    public Bitmap getBitmapWithFilterApplied() {
        return getBitmapWithFilterApplied(this.currentBitmap);
    }

    public Bitmap getBitmapWithFilterApplied(Bitmap bitmap) {
        return getBitmapWithFilterApplied(bitmap, false);
    }

    public Bitmap getBitmapWithFilterApplied(Bitmap bitmap, boolean z) {
        if (this.glSurfaceView != null || this.glTextureView != null) {
            this.renderer.deleteImage();
            this.renderer.runOnDraw(new Runnable() { // from class: jp.co.cyberagent.android.gpuimage.GPUImage.1
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (GPUImage.this.filter) {
                        GPUImage.this.filter.destroy();
                        GPUImage.this.filter.notify();
                    }
                }
            });
            synchronized (this.filter) {
                requestRender();
                try {
                    this.filter.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        GPUImageRenderer gPUImageRenderer = new GPUImageRenderer(this.filter);
        gPUImageRenderer.setRotation(Rotation.NORMAL, this.renderer.isFlippedHorizontally(), this.renderer.isFlippedVertically());
        gPUImageRenderer.setScaleType(this.scaleType);
        PixelBuffer pixelBuffer = new PixelBuffer(bitmap.getWidth(), bitmap.getHeight());
        pixelBuffer.setRenderer(gPUImageRenderer);
        gPUImageRenderer.setImageBitmap(bitmap, z);
        Bitmap bitmap2 = pixelBuffer.getBitmap();
        this.filter.destroy();
        gPUImageRenderer.deleteImage();
        pixelBuffer.destroy();
        this.renderer.setFilter(this.filter);
        if (this.currentBitmap != null) {
            this.renderer.setImageBitmap(this.currentBitmap, false);
        }
        requestRender();
        return bitmap2;
    }

    public static void getBitmapForMultipleFilters(Bitmap bitmap, List<GPUImageFilter> list, ResponseListener<Bitmap> responseListener) {
        if (list.isEmpty()) {
            return;
        }
        GPUImageRenderer gPUImageRenderer = new GPUImageRenderer(list.get(0));
        gPUImageRenderer.setImageBitmap(bitmap, false);
        PixelBuffer pixelBuffer = new PixelBuffer(bitmap.getWidth(), bitmap.getHeight());
        pixelBuffer.setRenderer(gPUImageRenderer);
        for (GPUImageFilter gPUImageFilter : list) {
            gPUImageRenderer.setFilter(gPUImageFilter);
            responseListener.response(pixelBuffer.getBitmap());
            gPUImageFilter.destroy();
        }
        gPUImageRenderer.deleteImage();
        pixelBuffer.destroy();
    }

    public void saveToPictures(String str, String str2, OnPictureSavedListener onPictureSavedListener) {
        saveToPictures(this.currentBitmap, str, str2, onPictureSavedListener);
    }

    public void saveToPictures(Bitmap bitmap, String str, String str2, OnPictureSavedListener onPictureSavedListener) {
        new SaveTask(bitmap, str, str2, onPictureSavedListener).execute(new Void[0]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void runOnGLThread(Runnable runnable) {
        this.renderer.runOnDrawEnd(runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getOutputWidth() {
        if (this.renderer != null && this.renderer.getFrameWidth() != 0) {
            return this.renderer.getFrameWidth();
        }
        if (this.currentBitmap != null) {
            return this.currentBitmap.getWidth();
        }
        return ((WindowManager) this.context.getSystemService("window")).getDefaultDisplay().getWidth();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getOutputHeight() {
        if (this.renderer != null && this.renderer.getFrameHeight() != 0) {
            return this.renderer.getFrameHeight();
        }
        if (this.currentBitmap != null) {
            return this.currentBitmap.getHeight();
        }
        return ((WindowManager) this.context.getSystemService("window")).getDefaultDisplay().getHeight();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Deprecated
    /* loaded from: classes2.dex */
    public class SaveTask extends AsyncTask<Void, Void, Void> {
        private final Bitmap bitmap;
        private final String fileName;
        private final String folderName;
        private final Handler handler = new Handler();
        private final OnPictureSavedListener listener;

        public SaveTask(Bitmap bitmap, String str, String str2, OnPictureSavedListener onPictureSavedListener) {
            this.bitmap = bitmap;
            this.folderName = str;
            this.fileName = str2;
            this.listener = onPictureSavedListener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Void doInBackground(Void... voidArr) {
            saveImage(this.folderName, this.fileName, GPUImage.this.getBitmapWithFilterApplied(this.bitmap));
            return null;
        }

        private void saveImage(String str, String str2, Bitmap bitmap) {
            File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File file = new File(externalStoragePublicDirectory, str + MqttTopic.TOPIC_LEVEL_SEPARATOR + str2);
            try {
                file.getParentFile().mkdirs();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(file));
                MediaScannerConnection.scanFile(GPUImage.this.context, new String[]{file.toString()}, null, new MediaScannerConnection.OnScanCompletedListener() { // from class: jp.co.cyberagent.android.gpuimage.GPUImage.SaveTask.1
                    @Override // android.media.MediaScannerConnection.OnScanCompletedListener
                    public void onScanCompleted(String str3, final Uri uri) {
                        if (SaveTask.this.listener != null) {
                            SaveTask.this.handler.post(new Runnable() { // from class: jp.co.cyberagent.android.gpuimage.GPUImage.SaveTask.1.1
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

    /* loaded from: classes2.dex */
    private class LoadImageUriTask extends LoadImageTask {
        private final Uri uri;

        public LoadImageUriTask(GPUImage gPUImage, Uri uri) {
            super(gPUImage);
            this.uri = uri;
        }

        @Override // jp.co.cyberagent.android.gpuimage.GPUImage.LoadImageTask
        protected Bitmap decode(BitmapFactory.Options options) {
            InputStream openStream;
            try {
                if (!this.uri.getScheme().startsWith(UriUtil.HTTP_SCHEME) && !this.uri.getScheme().startsWith(UriUtil.HTTPS_SCHEME)) {
                    openStream = this.uri.getPath().startsWith("/android_asset/") ? GPUImage.this.context.getAssets().open(this.uri.getPath().substring("/android_asset/".length())) : GPUImage.this.context.getContentResolver().openInputStream(this.uri);
                    return BitmapFactory.decodeStream(openStream, null, options);
                }
                openStream = new URL(this.uri.toString()).openStream();
                return BitmapFactory.decodeStream(openStream, null, options);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override // jp.co.cyberagent.android.gpuimage.GPUImage.LoadImageTask
        protected int getImageOrientation() throws IOException {
            Cursor query = GPUImage.this.context.getContentResolver().query(this.uri, new String[]{"orientation"}, null, null, null);
            if (query == null || query.getCount() != 1) {
                return 0;
            }
            query.moveToFirst();
            int i = query.getInt(0);
            query.close();
            return i;
        }
    }

    /* loaded from: classes2.dex */
    private class LoadImageFileTask extends LoadImageTask {
        private final File imageFile;

        public LoadImageFileTask(GPUImage gPUImage, File file) {
            super(gPUImage);
            this.imageFile = file;
        }

        @Override // jp.co.cyberagent.android.gpuimage.GPUImage.LoadImageTask
        protected Bitmap decode(BitmapFactory.Options options) {
            return BitmapFactory.decodeFile(this.imageFile.getAbsolutePath(), options);
        }

        @Override // jp.co.cyberagent.android.gpuimage.GPUImage.LoadImageTask
        protected int getImageOrientation() throws IOException {
            int attributeInt = new ExifInterface(this.imageFile.getAbsolutePath()).getAttributeInt(androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION, 1);
            if (attributeInt != 1) {
                if (attributeInt != 3) {
                    if (attributeInt != 6) {
                        if (attributeInt != 8) {
                            return 0;
                        }
                        return RotationOptions.ROTATE_270;
                    }
                    return 90;
                }
                return RotationOptions.ROTATE_180;
            }
            return 0;
        }
    }

    /* loaded from: classes2.dex */
    private abstract class LoadImageTask extends AsyncTask<Void, Void, Bitmap> {
        private final GPUImage gpuImage;
        private int outputHeight;
        private int outputWidth;

        protected abstract Bitmap decode(BitmapFactory.Options options);

        protected abstract int getImageOrientation() throws IOException;

        public LoadImageTask(GPUImage gPUImage) {
            this.gpuImage = gPUImage;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Bitmap doInBackground(Void... voidArr) {
            if (GPUImage.this.renderer != null && GPUImage.this.renderer.getFrameWidth() == 0) {
                try {
                    synchronized (GPUImage.this.renderer.surfaceChangedWaiter) {
                        GPUImage.this.renderer.surfaceChangedWaiter.wait(3000L);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.outputWidth = GPUImage.this.getOutputWidth();
            this.outputHeight = GPUImage.this.getOutputHeight();
            return loadResizedImage();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Bitmap bitmap) {
            super.onPostExecute((LoadImageTask) bitmap);
            this.gpuImage.deleteImage();
            this.gpuImage.setImage(bitmap);
        }

        private Bitmap loadResizedImage() {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            decode(options);
            int i = 1;
            while (true) {
                if (!checkSize(options.outWidth / i > this.outputWidth, options.outHeight / i > this.outputHeight)) {
                    break;
                }
                i++;
            }
            int i2 = i - 1;
            if (i2 < 1) {
                i2 = 1;
            }
            BitmapFactory.Options options2 = new BitmapFactory.Options();
            options2.inSampleSize = i2;
            options2.inPreferredConfig = Bitmap.Config.RGB_565;
            options2.inPurgeable = true;
            options2.inTempStorage = new byte[32768];
            Bitmap decode = decode(options2);
            if (decode == null) {
                return null;
            }
            return scaleBitmap(rotateImage(decode));
        }

        private Bitmap scaleBitmap(Bitmap bitmap) {
            int[] scaleSize = getScaleSize(bitmap.getWidth(), bitmap.getHeight());
            Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, scaleSize[0], scaleSize[1], true);
            if (createScaledBitmap != bitmap) {
                bitmap.recycle();
                System.gc();
                bitmap = createScaledBitmap;
            }
            if (GPUImage.this.scaleType == ScaleType.CENTER_CROP) {
                int i = scaleSize[0] - this.outputWidth;
                int i2 = scaleSize[1] - this.outputHeight;
                Bitmap createBitmap = Bitmap.createBitmap(bitmap, i / 2, i2 / 2, scaleSize[0] - i, scaleSize[1] - i2);
                if (createBitmap != bitmap) {
                    bitmap.recycle();
                    return createBitmap;
                }
            }
            return bitmap;
        }

        private int[] getScaleSize(int i, int i2) {
            float f;
            float f2;
            float f3 = i;
            float f4 = f3 / this.outputWidth;
            float f5 = i2;
            float f6 = f5 / this.outputHeight;
            if (GPUImage.this.scaleType != ScaleType.CENTER_CROP ? f4 < f6 : f4 > f6) {
                float f7 = this.outputHeight;
                f2 = (f7 / f5) * f3;
                f = f7;
            } else {
                float f8 = this.outputWidth;
                f = (f8 / f3) * f5;
                f2 = f8;
            }
            GPUImage.this.scaleWidth = Math.round(f2);
            GPUImage.this.scaleHeight = Math.round(f);
            return new int[]{Math.round(f2), Math.round(f)};
        }

        private boolean checkSize(boolean z, boolean z2) {
            return GPUImage.this.scaleType == ScaleType.CENTER_CROP ? z && z2 : z || z2;
        }

        private Bitmap rotateImage(Bitmap bitmap) {
            if (bitmap == null) {
                return null;
            }
            try {
                int imageOrientation = getImageOrientation();
                if (imageOrientation != 0) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(imageOrientation);
                    Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    try {
                        bitmap.recycle();
                        return createBitmap;
                    } catch (IOException e) {
                        bitmap = createBitmap;
                        e = e;
                        e.printStackTrace();
                        return bitmap;
                    }
                }
            } catch (IOException e2) {
                e = e2;
            }
            return bitmap;
        }
    }
}
