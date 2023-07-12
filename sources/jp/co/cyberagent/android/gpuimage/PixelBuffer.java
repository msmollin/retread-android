package jp.co.cyberagent.android.gpuimage;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.util.Log;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes2.dex */
public class PixelBuffer {
    private static final boolean LIST_CONFIGS = false;
    private static final String TAG = "PixelBuffer";
    private Bitmap bitmap;
    private EGL10 egl10;
    private EGLConfig eglConfig;
    private EGLConfig[] eglConfigs;
    private EGLContext eglContext;
    private EGLDisplay eglDisplay;
    private EGLSurface eglSurface;
    private GL10 gl10;
    private int height;
    private String mThreadOwner;
    private GLSurfaceView.Renderer renderer;
    private int width;

    public PixelBuffer(int i, int i2) {
        this.width = i;
        this.height = i2;
        int[] iArr = {12375, this.width, 12374, this.height, 12344};
        this.egl10 = (EGL10) EGLContext.getEGL();
        this.eglDisplay = this.egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        this.egl10.eglInitialize(this.eglDisplay, new int[2]);
        this.eglConfig = chooseConfig();
        this.eglContext = this.egl10.eglCreateContext(this.eglDisplay, this.eglConfig, EGL10.EGL_NO_CONTEXT, new int[]{12440, 2, 12344});
        this.eglSurface = this.egl10.eglCreatePbufferSurface(this.eglDisplay, this.eglConfig, iArr);
        this.egl10.eglMakeCurrent(this.eglDisplay, this.eglSurface, this.eglSurface, this.eglContext);
        this.gl10 = (GL10) this.eglContext.getGL();
        this.mThreadOwner = Thread.currentThread().getName();
    }

    public void setRenderer(GLSurfaceView.Renderer renderer) {
        this.renderer = renderer;
        if (!Thread.currentThread().getName().equals(this.mThreadOwner)) {
            Log.e(TAG, "setRenderer: This thread does not own the OpenGL context.");
            return;
        }
        this.renderer.onSurfaceCreated(this.gl10, this.eglConfig);
        this.renderer.onSurfaceChanged(this.gl10, this.width, this.height);
    }

    public Bitmap getBitmap() {
        if (this.renderer == null) {
            Log.e(TAG, "getBitmap: Renderer was not set.");
            return null;
        } else if (!Thread.currentThread().getName().equals(this.mThreadOwner)) {
            Log.e(TAG, "getBitmap: This thread does not own the OpenGL context.");
            return null;
        } else {
            this.renderer.onDrawFrame(this.gl10);
            this.renderer.onDrawFrame(this.gl10);
            convertToBitmap();
            return this.bitmap;
        }
    }

    public void destroy() {
        this.renderer.onDrawFrame(this.gl10);
        this.renderer.onDrawFrame(this.gl10);
        this.egl10.eglMakeCurrent(this.eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
        this.egl10.eglDestroySurface(this.eglDisplay, this.eglSurface);
        this.egl10.eglDestroyContext(this.eglDisplay, this.eglContext);
        this.egl10.eglTerminate(this.eglDisplay);
    }

    private EGLConfig chooseConfig() {
        int[] iArr = {12325, 0, 12326, 0, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12352, 4, 12344};
        int[] iArr2 = new int[1];
        this.egl10.eglChooseConfig(this.eglDisplay, iArr, null, 0, iArr2);
        int i = iArr2[0];
        this.eglConfigs = new EGLConfig[i];
        this.egl10.eglChooseConfig(this.eglDisplay, iArr, this.eglConfigs, i, iArr2);
        return this.eglConfigs[0];
    }

    private void listConfig() {
        EGLConfig[] eGLConfigArr;
        Log.i(TAG, "Config List {");
        for (EGLConfig eGLConfig : this.eglConfigs) {
            Log.i(TAG, "    <d,s,r,g,b,a> = <" + getConfigAttrib(eGLConfig, 12325) + "," + getConfigAttrib(eGLConfig, 12326) + "," + getConfigAttrib(eGLConfig, 12324) + "," + getConfigAttrib(eGLConfig, 12323) + "," + getConfigAttrib(eGLConfig, 12322) + "," + getConfigAttrib(eGLConfig, 12321) + ">");
        }
        Log.i(TAG, "}");
    }

    private int getConfigAttrib(EGLConfig eGLConfig, int i) {
        int[] iArr = new int[1];
        if (this.egl10.eglGetConfigAttrib(this.eglDisplay, eGLConfig, i, iArr)) {
            return iArr[0];
        }
        return 0;
    }

    private void convertToBitmap() {
        this.bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888);
        GPUImageNativeLibrary.adjustBitmap(this.bitmap);
    }
}
