package org.otwebrtc;

import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.HandlerThread;
import java.nio.ByteBuffer;
import java.util.concurrent.Callable;
import org.otwebrtc.EglBase;

/* loaded from: classes2.dex */
public class SurfaceTextureHelper61 {
    private static final String TAG = "SurfaceTextureHelper61";
    private final EglBase eglBase;
    private final Handler handler;
    private boolean hasPendingTexture;
    private boolean isQuitting;
    private volatile boolean isTextureInUse;
    private OnTextureFrameAvailableListener listener;
    private final int oesTextureId;
    private OnTextureFrameAvailableListener pendingListener;
    final Runnable setListenerRunnable;
    private final SurfaceTexture surfaceTexture;
    private YuvConverter61 yuvConverter;

    /* loaded from: classes2.dex */
    public interface OnTextureFrameAvailableListener {
        void onTextureFrameAvailable(int i, float[] fArr, long j);
    }

    private SurfaceTextureHelper61(EglBase.Context context, Handler handler) {
        this.hasPendingTexture = false;
        this.isTextureInUse = false;
        this.isQuitting = false;
        this.setListenerRunnable = new Runnable() { // from class: org.otwebrtc.SurfaceTextureHelper61.2
            @Override // java.lang.Runnable
            public void run() {
                Logging.d(SurfaceTextureHelper61.TAG, "Setting listener to " + SurfaceTextureHelper61.this.pendingListener);
                SurfaceTextureHelper61 surfaceTextureHelper61 = SurfaceTextureHelper61.this;
                surfaceTextureHelper61.listener = surfaceTextureHelper61.pendingListener;
                SurfaceTextureHelper61.this.pendingListener = null;
                if (SurfaceTextureHelper61.this.hasPendingTexture) {
                    SurfaceTextureHelper61.this.updateTexImage();
                    SurfaceTextureHelper61.this.hasPendingTexture = false;
                }
            }
        };
        if (handler.getLooper().getThread() != Thread.currentThread()) {
            throw new IllegalStateException("SurfaceTextureHelper must be created on the handler thread");
        }
        this.handler = handler;
        EglBase create = EglBase.create(context, EglBase.CONFIG_PIXEL_BUFFER);
        this.eglBase = create;
        try {
            create.createDummyPbufferSurface();
            this.eglBase.makeCurrent();
            this.oesTextureId = GlUtil.generateTexture(36197);
            SurfaceTexture surfaceTexture = new SurfaceTexture(this.oesTextureId);
            this.surfaceTexture = surfaceTexture;
            surfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() { // from class: org.otwebrtc.SurfaceTextureHelper61.3
                @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
                public void onFrameAvailable(SurfaceTexture surfaceTexture2) {
                    SurfaceTextureHelper61.this.hasPendingTexture = true;
                    SurfaceTextureHelper61.this.tryDeliverTextureFrame();
                }
            });
        } catch (RuntimeException e) {
            this.eglBase.release();
            handler.getLooper().quit();
            throw e;
        }
    }

    public static SurfaceTextureHelper61 create(final String str, final EglBase.Context context) {
        HandlerThread handlerThread = new HandlerThread(str);
        handlerThread.start();
        final Handler handler = new Handler(handlerThread.getLooper());
        return (SurfaceTextureHelper61) ThreadUtils.invokeAtFrontUninterruptibly(handler, new Callable<SurfaceTextureHelper61>() { // from class: org.otwebrtc.SurfaceTextureHelper61.1
            @Override // java.util.concurrent.Callable
            public SurfaceTextureHelper61 call() {
                try {
                    return new SurfaceTextureHelper61(EglBase.Context.this, handler);
                } catch (RuntimeException e) {
                    Logging.e(SurfaceTextureHelper61.TAG, str + " create failure", e);
                    return null;
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void release() {
        if (this.handler.getLooper().getThread() != Thread.currentThread()) {
            throw new IllegalStateException("Wrong thread.");
        }
        if (this.isTextureInUse || !this.isQuitting) {
            throw new IllegalStateException("Unexpected release.");
        }
        YuvConverter61 yuvConverter61 = this.yuvConverter;
        if (yuvConverter61 != null) {
            yuvConverter61.release();
        }
        GLES20.glDeleteTextures(1, new int[]{this.oesTextureId}, 0);
        this.surfaceTexture.release();
        this.eglBase.release();
        this.handler.getLooper().quit();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryDeliverTextureFrame() {
        if (this.handler.getLooper().getThread() != Thread.currentThread()) {
            throw new IllegalStateException("Wrong thread.");
        }
        if (this.isQuitting || !this.hasPendingTexture || this.isTextureInUse || this.listener == null) {
            return;
        }
        this.isTextureInUse = true;
        this.hasPendingTexture = false;
        updateTexImage();
        float[] fArr = new float[16];
        this.surfaceTexture.getTransformMatrix(fArr);
        this.listener.onTextureFrameAvailable(this.oesTextureId, fArr, this.surfaceTexture.getTimestamp());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTexImage() {
        synchronized (EglBase.lock) {
            this.surfaceTexture.updateTexImage();
        }
    }

    public void dispose() {
        Logging.d(TAG, "dispose()");
        ThreadUtils.invokeAtFrontUninterruptibly(this.handler, new Runnable() { // from class: org.otwebrtc.SurfaceTextureHelper61.6
            @Override // java.lang.Runnable
            public void run() {
                SurfaceTextureHelper61.this.isQuitting = true;
                if (SurfaceTextureHelper61.this.isTextureInUse) {
                    return;
                }
                SurfaceTextureHelper61.this.release();
            }
        });
    }

    public Handler getHandler() {
        return this.handler;
    }

    public SurfaceTexture getSurfaceTexture() {
        return this.surfaceTexture;
    }

    public boolean isTextureInUse() {
        return this.isTextureInUse;
    }

    public void returnTextureFrame() {
        this.handler.post(new Runnable() { // from class: org.otwebrtc.SurfaceTextureHelper61.5
            @Override // java.lang.Runnable
            public void run() {
                SurfaceTextureHelper61.this.isTextureInUse = false;
                if (SurfaceTextureHelper61.this.isQuitting) {
                    SurfaceTextureHelper61.this.release();
                } else {
                    SurfaceTextureHelper61.this.tryDeliverTextureFrame();
                }
            }
        });
    }

    public void startListening(OnTextureFrameAvailableListener onTextureFrameAvailableListener) {
        if (this.listener != null || this.pendingListener != null) {
            throw new IllegalStateException("SurfaceTextureHelper listener has already been set.");
        }
        this.pendingListener = onTextureFrameAvailableListener;
        this.handler.post(this.setListenerRunnable);
    }

    public void stopListening() {
        Logging.d(TAG, "stopListening()");
        this.handler.removeCallbacks(this.setListenerRunnable);
        ThreadUtils.invokeAtFrontUninterruptibly(this.handler, new Runnable() { // from class: org.otwebrtc.SurfaceTextureHelper61.4
            @Override // java.lang.Runnable
            public void run() {
                SurfaceTextureHelper61.this.listener = null;
                SurfaceTextureHelper61.this.pendingListener = null;
            }
        });
    }

    public void textureToYUV(final ByteBuffer byteBuffer, final int i, final int i2, final int i3, final int i4, final float[] fArr) {
        if (i4 != this.oesTextureId) {
            throw new IllegalStateException("textureToByteBuffer called with unexpected textureId");
        }
        ThreadUtils.invokeAtFrontUninterruptibly(this.handler, new Runnable() { // from class: org.otwebrtc.SurfaceTextureHelper61.7
            @Override // java.lang.Runnable
            public void run() {
                if (SurfaceTextureHelper61.this.yuvConverter == null) {
                    SurfaceTextureHelper61.this.yuvConverter = new YuvConverter61();
                }
                SurfaceTextureHelper61.this.yuvConverter.convert(byteBuffer, i, i2, i3, i4, fArr);
            }
        });
    }
}
