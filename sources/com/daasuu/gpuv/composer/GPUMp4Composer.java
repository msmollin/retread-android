package com.daasuu.gpuv.composer;

import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.util.Size;
import com.daasuu.gpuv.composer.GPUMp4ComposerEngine;
import com.daasuu.gpuv.egl.filter.GlFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* loaded from: classes.dex */
public class GPUMp4Composer {
    private static final String TAG = "GPUMp4Composer";
    private final String destPath;
    private ExecutorService executorService;
    private FillModeCustomItem fillModeCustomItem;
    private GlFilter filter;
    private Listener listener;
    private Size outputResolution;
    private final String srcPath;
    private int bitrate = -1;
    private boolean mute = false;
    private Rotation rotation = Rotation.NORMAL;
    private FillMode fillMode = FillMode.PRESERVE_ASPECT_FIT;
    private int timeScale = 1;
    private boolean flipVertical = false;
    private boolean flipHorizontal = false;

    /* loaded from: classes.dex */
    public interface Listener {
        void onCanceled();

        void onCompleted();

        void onFailed(Exception exc);

        void onProgress(double d);
    }

    public GPUMp4Composer(String str, String str2) {
        this.srcPath = str;
        this.destPath = str2;
    }

    public GPUMp4Composer filter(GlFilter glFilter) {
        this.filter = glFilter;
        return this;
    }

    public GPUMp4Composer size(int i, int i2) {
        this.outputResolution = new Size(i, i2);
        return this;
    }

    public GPUMp4Composer videoBitrate(int i) {
        this.bitrate = i;
        return this;
    }

    public GPUMp4Composer mute(boolean z) {
        this.mute = z;
        return this;
    }

    public GPUMp4Composer flipVertical(boolean z) {
        this.flipVertical = z;
        return this;
    }

    public GPUMp4Composer flipHorizontal(boolean z) {
        this.flipHorizontal = z;
        return this;
    }

    public GPUMp4Composer rotation(Rotation rotation) {
        this.rotation = rotation;
        return this;
    }

    public GPUMp4Composer fillMode(FillMode fillMode) {
        this.fillMode = fillMode;
        return this;
    }

    public GPUMp4Composer customFillMode(FillModeCustomItem fillModeCustomItem) {
        this.fillModeCustomItem = fillModeCustomItem;
        this.fillMode = FillMode.CUSTOM;
        return this;
    }

    public GPUMp4Composer listener(Listener listener) {
        this.listener = listener;
        return this;
    }

    public GPUMp4Composer timeScale(int i) {
        this.timeScale = i;
        return this;
    }

    private ExecutorService getExecutorService() {
        if (this.executorService == null) {
            this.executorService = Executors.newSingleThreadExecutor();
        }
        return this.executorService;
    }

    public GPUMp4Composer start() {
        getExecutorService().execute(new Runnable() { // from class: com.daasuu.gpuv.composer.GPUMp4Composer.1
            @Override // java.lang.Runnable
            public void run() {
                GPUMp4ComposerEngine gPUMp4ComposerEngine = new GPUMp4ComposerEngine();
                gPUMp4ComposerEngine.setProgressCallback(new GPUMp4ComposerEngine.ProgressCallback() { // from class: com.daasuu.gpuv.composer.GPUMp4Composer.1.1
                    @Override // com.daasuu.gpuv.composer.GPUMp4ComposerEngine.ProgressCallback
                    public void onProgress(double d) {
                        if (GPUMp4Composer.this.listener != null) {
                            GPUMp4Composer.this.listener.onProgress(d);
                        }
                    }
                });
                try {
                    try {
                        gPUMp4ComposerEngine.setDataSource(new FileInputStream(new File(GPUMp4Composer.this.srcPath)).getFD());
                        int videoRotation = GPUMp4Composer.this.getVideoRotation(GPUMp4Composer.this.srcPath);
                        Size videoResolution = GPUMp4Composer.this.getVideoResolution(GPUMp4Composer.this.srcPath, videoRotation);
                        if (GPUMp4Composer.this.filter == null) {
                            GPUMp4Composer.this.filter = new GlFilter();
                        }
                        if (GPUMp4Composer.this.fillMode == null) {
                            GPUMp4Composer.this.fillMode = FillMode.PRESERVE_ASPECT_FIT;
                        }
                        if (GPUMp4Composer.this.fillModeCustomItem != null) {
                            GPUMp4Composer.this.fillMode = FillMode.CUSTOM;
                        }
                        if (GPUMp4Composer.this.outputResolution == null) {
                            if (GPUMp4Composer.this.fillMode == FillMode.CUSTOM) {
                                GPUMp4Composer.this.outputResolution = videoResolution;
                            } else {
                                Rotation fromInt = Rotation.fromInt(GPUMp4Composer.this.rotation.getRotation() + videoRotation);
                                if (fromInt != Rotation.ROTATION_90 && fromInt != Rotation.ROTATION_270) {
                                    GPUMp4Composer.this.outputResolution = videoResolution;
                                } else {
                                    GPUMp4Composer.this.outputResolution = new Size(videoResolution.getHeight(), videoResolution.getWidth());
                                }
                            }
                        }
                        if (GPUMp4Composer.this.timeScale < 2) {
                            GPUMp4Composer.this.timeScale = 1;
                        }
                        String str = GPUMp4Composer.TAG;
                        Log.d(str, "rotation = " + (GPUMp4Composer.this.rotation.getRotation() + videoRotation));
                        String str2 = GPUMp4Composer.TAG;
                        Log.d(str2, "inputResolution width = " + videoResolution.getWidth() + " height = " + videoResolution.getHeight());
                        String str3 = GPUMp4Composer.TAG;
                        Log.d(str3, "outputResolution width = " + GPUMp4Composer.this.outputResolution.getWidth() + " height = " + GPUMp4Composer.this.outputResolution.getHeight());
                        String str4 = GPUMp4Composer.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("fillMode = ");
                        sb.append(GPUMp4Composer.this.fillMode);
                        Log.d(str4, sb.toString());
                        try {
                            if (GPUMp4Composer.this.bitrate < 0) {
                                GPUMp4Composer.this.bitrate = GPUMp4Composer.this.calcBitRate(GPUMp4Composer.this.outputResolution.getWidth(), GPUMp4Composer.this.outputResolution.getHeight());
                            }
                            gPUMp4ComposerEngine.compose(GPUMp4Composer.this.destPath, GPUMp4Composer.this.outputResolution, GPUMp4Composer.this.filter, GPUMp4Composer.this.bitrate, GPUMp4Composer.this.mute, Rotation.fromInt(GPUMp4Composer.this.rotation.getRotation() + videoRotation), videoResolution, GPUMp4Composer.this.fillMode, GPUMp4Composer.this.fillModeCustomItem, GPUMp4Composer.this.timeScale, GPUMp4Composer.this.flipVertical, GPUMp4Composer.this.flipHorizontal);
                            if (GPUMp4Composer.this.listener != null) {
                                GPUMp4Composer.this.listener.onCompleted();
                            }
                            GPUMp4Composer.this.executorService.shutdown();
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (GPUMp4Composer.this.listener != null) {
                                GPUMp4Composer.this.listener.onFailed(e);
                            }
                            GPUMp4Composer.this.executorService.shutdown();
                        }
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        if (GPUMp4Composer.this.listener != null) {
                            GPUMp4Composer.this.listener.onFailed(e2);
                        }
                    }
                } catch (FileNotFoundException e3) {
                    e3.printStackTrace();
                    if (GPUMp4Composer.this.listener != null) {
                        GPUMp4Composer.this.listener.onFailed(e3);
                    }
                }
            }
        });
        return this;
    }

    public void cancel() {
        getExecutorService().shutdownNow();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getVideoRotation(String str) {
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            try {
                MediaMetadataRetriever mediaMetadataRetriever2 = new MediaMetadataRetriever();
                try {
                    mediaMetadataRetriever2.setDataSource(str);
                    int intValue = Integer.valueOf(mediaMetadataRetriever2.extractMetadata(24)).intValue();
                    try {
                        mediaMetadataRetriever2.release();
                    } catch (RuntimeException e) {
                        Log.e(TAG, "Failed to release mediaMetadataRetriever.", e);
                    }
                    return intValue;
                } catch (IllegalArgumentException unused) {
                    mediaMetadataRetriever = mediaMetadataRetriever2;
                    Log.e("MediaMetadataRetriever", "getVideoRotation IllegalArgumentException");
                    if (mediaMetadataRetriever != null) {
                        try {
                            mediaMetadataRetriever.release();
                        } catch (RuntimeException e2) {
                            Log.e(TAG, "Failed to release mediaMetadataRetriever.", e2);
                        }
                    }
                    return 0;
                } catch (RuntimeException unused2) {
                    mediaMetadataRetriever = mediaMetadataRetriever2;
                    Log.e("MediaMetadataRetriever", "getVideoRotation RuntimeException");
                    if (mediaMetadataRetriever != null) {
                        try {
                            mediaMetadataRetriever.release();
                        } catch (RuntimeException e3) {
                            Log.e(TAG, "Failed to release mediaMetadataRetriever.", e3);
                        }
                    }
                    return 0;
                } catch (Exception unused3) {
                    mediaMetadataRetriever = mediaMetadataRetriever2;
                    Log.e("MediaMetadataRetriever", "getVideoRotation Exception");
                    if (mediaMetadataRetriever != null) {
                        try {
                            mediaMetadataRetriever.release();
                        } catch (RuntimeException e4) {
                            Log.e(TAG, "Failed to release mediaMetadataRetriever.", e4);
                        }
                    }
                    return 0;
                } catch (Throwable th) {
                    th = th;
                    mediaMetadataRetriever = mediaMetadataRetriever2;
                    if (mediaMetadataRetriever != null) {
                        try {
                            mediaMetadataRetriever.release();
                        } catch (RuntimeException e5) {
                            Log.e(TAG, "Failed to release mediaMetadataRetriever.", e5);
                        }
                    }
                    throw th;
                }
            } catch (IllegalArgumentException unused4) {
            } catch (RuntimeException unused5) {
            } catch (Exception unused6) {
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int calcBitRate(int i, int i2) {
        int i3 = (int) (i * 7.5d * i2);
        String str = TAG;
        Log.i(str, "bitrate=" + i3);
        return i3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Size getVideoResolution(String str, int i) {
        MediaMetadataRetriever mediaMetadataRetriever;
        Throwable th;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
        } catch (Throwable th2) {
            mediaMetadataRetriever = null;
            th = th2;
        }
        try {
            mediaMetadataRetriever.setDataSource(str);
            Size size = new Size(Integer.valueOf(mediaMetadataRetriever.extractMetadata(18)).intValue(), Integer.valueOf(mediaMetadataRetriever.extractMetadata(19)).intValue());
            try {
                mediaMetadataRetriever.release();
            } catch (RuntimeException e) {
                Log.e(TAG, "Failed to release mediaMetadataRetriever.", e);
            }
            return size;
        } catch (Throwable th3) {
            th = th3;
            if (mediaMetadataRetriever != null) {
                try {
                    mediaMetadataRetriever.release();
                } catch (RuntimeException e2) {
                    Log.e(TAG, "Failed to release mediaMetadataRetriever.", e2);
                }
            }
            throw th;
        }
    }
}
