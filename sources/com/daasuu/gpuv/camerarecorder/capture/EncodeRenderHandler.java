package com.daasuu.gpuv.camerarecorder.capture;

import android.graphics.SurfaceTexture;
import android.opengl.EGLContext;
import android.opengl.Matrix;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import com.daasuu.gpuv.egl.GlFramebufferObject;
import com.daasuu.gpuv.egl.GlPreview;
import com.daasuu.gpuv.egl.filter.GlFilter;

/* loaded from: classes.dex */
public class EncodeRenderHandler implements Runnable {
    private static final String TAG = "GPUCameraRecorder";
    private final float XMatrixScale;
    private final float YMatrixScale;
    private EglWrapper egl;
    private final float fileHeight;
    private final float fileWidth;
    private GlFramebufferObject filterFramebufferObject;
    private GlFramebufferObject framebufferObject;
    private GlFilter glFilter;
    private EglSurface inputSurface;
    private boolean isRecordable;
    private GlFilter normalFilter;
    private GlPreview previewShader;
    private final boolean recordNoFilter;
    private int requestDraw;
    private boolean requestRelease;
    private boolean requestSetEglContext;
    private EGLContext sharedContext;
    private Object surface;
    private final Object sync = new Object();
    private int texId = -1;
    private float[] MVPMatrix = new float[16];
    private float[] STMatrix = new float[16];
    private float aspectRatio = 1.0f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static EncodeRenderHandler createHandler(String str, boolean z, boolean z2, float f, float f2, float f3, boolean z3, GlFilter glFilter) {
        Log.v(TAG, "createHandler:");
        StringBuilder sb = new StringBuilder();
        sb.append("fileAspect:");
        float f4 = f3 / f2;
        sb.append(f4);
        sb.append(" viewAcpect: ");
        sb.append(f);
        Log.v(TAG, sb.toString());
        EncodeRenderHandler encodeRenderHandler = new EncodeRenderHandler(z, z2, f4, f, f2, f3, z3, glFilter);
        synchronized (encodeRenderHandler.sync) {
            new Thread(encodeRenderHandler, !TextUtils.isEmpty(str) ? str : TAG).start();
            try {
                encodeRenderHandler.sync.wait();
            } catch (InterruptedException unused) {
            }
        }
        return encodeRenderHandler;
    }

    private EncodeRenderHandler(boolean z, boolean z2, float f, float f2, float f3, float f4, boolean z3, GlFilter glFilter) {
        this.fileWidth = f3;
        this.fileHeight = f4;
        this.recordNoFilter = z3;
        this.glFilter = glFilter;
        if (f == f2) {
            this.XMatrixScale = z2 ? -1 : 1;
            this.YMatrixScale = z ? -1.0f : 1.0f;
        } else if (f < f2) {
            this.XMatrixScale = z2 ? -1 : 1;
            this.YMatrixScale = (z ? -1 : 1) * (f2 / f);
            Log.v(TAG, "cameraAspect: " + f2 + " YMatrixScale :" + this.YMatrixScale);
        } else {
            this.XMatrixScale = (z2 ? -1 : 1) * (f / f2);
            this.YMatrixScale = z ? -1 : 1;
            Log.v(TAG, "cameraAspect: " + f2 + " YMatrixScale :" + this.YMatrixScale + " XMatrixScale :" + this.XMatrixScale);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void setEglContext(EGLContext eGLContext, int i, Object obj) {
        Log.i(TAG, "setEglContext:");
        if (!(obj instanceof Surface) && !(obj instanceof SurfaceTexture) && !(obj instanceof SurfaceHolder)) {
            throw new RuntimeException("unsupported window type:" + obj);
        }
        synchronized (this.sync) {
            if (this.requestRelease) {
                return;
            }
            this.sharedContext = eGLContext;
            this.texId = i;
            this.surface = obj;
            this.isRecordable = true;
            this.requestSetEglContext = true;
            this.sync.notifyAll();
            try {
                this.sync.wait();
            } catch (InterruptedException unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void prepareDraw() {
        synchronized (this.sync) {
            if (this.requestRelease) {
                return;
            }
            this.requestDraw++;
            this.sync.notifyAll();
        }
    }

    public final void draw(int i, float[] fArr, float[] fArr2, float f) {
        synchronized (this.sync) {
            if (this.requestRelease) {
                return;
            }
            this.texId = i;
            System.arraycopy(fArr, 0, this.STMatrix, 0, 16);
            System.arraycopy(fArr2, 0, this.MVPMatrix, 0, 16);
            Matrix.scaleM(this.MVPMatrix, 0, this.XMatrixScale, this.YMatrixScale, 1.0f);
            this.aspectRatio = f;
            this.requestDraw++;
            this.sync.notifyAll();
        }
    }

    public final void release() {
        Log.i(TAG, "release:");
        synchronized (this.sync) {
            if (this.requestRelease) {
                return;
            }
            this.requestRelease = true;
            this.sync.notifyAll();
            try {
                this.sync.wait();
            } catch (InterruptedException unused) {
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x003a, code lost:
        if (r0 == false) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x003e, code lost:
        if (r6.egl == null) goto L66;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0042, code lost:
        if (r6.texId < 0) goto L65;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0044, code lost:
        r6.inputSurface.makeCurrent();
        android.opengl.GLES20.glClear(16384);
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0052, code lost:
        if (isRecordFilter() == false) goto L60;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0054, code lost:
        r6.framebufferObject.enable();
        r6.filterFramebufferObject.enable();
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x005e, code lost:
        r6.previewShader.draw(r6.texId, r6.MVPMatrix, r6.STMatrix, r6.aspectRatio);
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x006f, code lost:
        if (isRecordFilter() == false) goto L63;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0071, code lost:
        r6.framebufferObject.enable();
        r6.glFilter.draw(r6.filterFramebufferObject.getTexName(), r6.framebufferObject);
        android.opengl.GLES20.glBindFramebuffer(36160, 0);
        android.opengl.GLES20.glViewport(0, 0, r6.framebufferObject.getWidth(), r6.framebufferObject.getHeight());
        android.opengl.GLES20.glClear(16640);
        r6.normalFilter.draw(r6.framebufferObject.getTexName(), null);
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00a9, code lost:
        r6.inputSurface.swap();
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00b0, code lost:
        r0 = r6.sync;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00b2, code lost:
        monitor-enter(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00b3, code lost:
        r6.sync.wait();
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00b8, code lost:
        monitor-exit(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00bb, code lost:
        r6 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00d8, code lost:
        throw r6;
     */
    /* JADX WARN: Removed duplicated region for block: B:60:0x00c1 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void run() {
        /*
            Method dump skipped, instructions count: 223
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.daasuu.gpuv.camerarecorder.capture.EncodeRenderHandler.run():void");
    }

    private void internalPrepare() {
        Log.i(TAG, "internalPrepare:");
        internalRelease();
        this.egl = new EglWrapper(this.sharedContext, false, this.isRecordable);
        this.inputSurface = this.egl.createFromSurface(this.surface);
        this.inputSurface.makeCurrent();
        this.previewShader = new GlPreview(36197);
        this.previewShader.setup();
        if (isRecordFilter()) {
            this.framebufferObject = new GlFramebufferObject();
            this.framebufferObject.setup((int) this.fileWidth, (int) this.fileHeight);
            this.filterFramebufferObject = new GlFramebufferObject();
            this.filterFramebufferObject.setup((int) this.fileWidth, (int) this.fileHeight);
            this.normalFilter = new GlFilter();
            this.normalFilter.setup();
        }
        this.surface = null;
        this.sync.notifyAll();
    }

    private void internalRelease() {
        Log.i(TAG, "internalRelease:");
        if (this.inputSurface != null) {
            this.inputSurface.release();
            this.inputSurface = null;
        }
        if (this.egl != null) {
            this.egl.release();
            this.egl = null;
        }
        if (this.normalFilter != null) {
            this.normalFilter.release();
            this.normalFilter = null;
        }
        if (this.filterFramebufferObject != null) {
            this.filterFramebufferObject.release();
            this.filterFramebufferObject = null;
        }
        if (this.framebufferObject != null) {
            this.framebufferObject.release();
            this.framebufferObject = null;
        }
    }

    private boolean isRecordFilter() {
        return (this.glFilter == null || this.recordNoFilter) ? false : true;
    }
}
