package jp.co.cyberagent.android.gpuimage;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLDebugHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes2.dex */
public class GLTextureView extends TextureView implements TextureView.SurfaceTextureListener, View.OnLayoutChangeListener {
    public static final int DEBUG_CHECK_GL_ERROR = 1;
    public static final int DEBUG_LOG_GL_CALLS = 2;
    private static final boolean LOG_ATTACH_DETACH = false;
    private static final boolean LOG_EGL = false;
    private static final boolean LOG_PAUSE_RESUME = false;
    private static final boolean LOG_RENDERER = false;
    private static final boolean LOG_RENDERER_DRAW_FRAME = false;
    private static final boolean LOG_SURFACE = false;
    private static final boolean LOG_THREADS = false;
    public static final int RENDERMODE_CONTINUOUSLY = 1;
    public static final int RENDERMODE_WHEN_DIRTY = 0;
    private static final String TAG = "GLTextureView";
    private static final GLThreadManager glThreadManager = new GLThreadManager();
    private int debugFlags;
    private boolean detached;
    private EGLConfigChooser eglConfigChooser;
    private int eglContextClientVersion;
    private EGLContextFactory eglContextFactory;
    private EGLWindowSurfaceFactory eglWindowSurfaceFactory;
    private GLThread glThread;
    private GLWrapper glWrapper;
    private final WeakReference<GLTextureView> mThisWeakRef;
    private boolean preserveEGLContextOnPause;
    private Renderer renderer;
    private List<TextureView.SurfaceTextureListener> surfaceTextureListeners;

    /* loaded from: classes2.dex */
    public interface EGLConfigChooser {
        EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay);
    }

    /* loaded from: classes2.dex */
    public interface EGLContextFactory {
        EGLContext createContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig);

        void destroyContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLContext eGLContext);
    }

    /* loaded from: classes2.dex */
    public interface EGLWindowSurfaceFactory {
        EGLSurface createWindowSurface(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig, Object obj);

        void destroySurface(EGL10 egl10, EGLDisplay eGLDisplay, EGLSurface eGLSurface);
    }

    /* loaded from: classes2.dex */
    public interface GLWrapper {
        GL wrap(GL gl);
    }

    /* loaded from: classes2.dex */
    public interface Renderer {
        void onDrawFrame(GL10 gl10);

        void onSurfaceChanged(GL10 gl10, int i, int i2);

        void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig);
    }

    public GLTextureView(Context context) {
        super(context);
        this.mThisWeakRef = new WeakReference<>(this);
        this.surfaceTextureListeners = new ArrayList();
        init();
    }

    public GLTextureView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mThisWeakRef = new WeakReference<>(this);
        this.surfaceTextureListeners = new ArrayList();
        init();
    }

    protected void finalize() throws Throwable {
        try {
            if (this.glThread != null) {
                this.glThread.requestExitAndWait();
            }
        } finally {
            super.finalize();
        }
    }

    private void init() {
        setSurfaceTextureListener(this);
    }

    public void setGLWrapper(GLWrapper gLWrapper) {
        this.glWrapper = gLWrapper;
    }

    public void setDebugFlags(int i) {
        this.debugFlags = i;
    }

    public int getDebugFlags() {
        return this.debugFlags;
    }

    public void setPreserveEGLContextOnPause(boolean z) {
        this.preserveEGLContextOnPause = z;
    }

    public boolean getPreserveEGLContextOnPause() {
        return this.preserveEGLContextOnPause;
    }

    public void setRenderer(Renderer renderer) {
        checkRenderThreadState();
        if (this.eglConfigChooser == null) {
            this.eglConfigChooser = new SimpleEGLConfigChooser(true);
        }
        if (this.eglContextFactory == null) {
            this.eglContextFactory = new DefaultContextFactory();
        }
        if (this.eglWindowSurfaceFactory == null) {
            this.eglWindowSurfaceFactory = new DefaultWindowSurfaceFactory();
        }
        this.renderer = renderer;
        this.glThread = new GLThread(this.mThisWeakRef);
        this.glThread.start();
    }

    public void setEGLContextFactory(EGLContextFactory eGLContextFactory) {
        checkRenderThreadState();
        this.eglContextFactory = eGLContextFactory;
    }

    public void setEGLWindowSurfaceFactory(EGLWindowSurfaceFactory eGLWindowSurfaceFactory) {
        checkRenderThreadState();
        this.eglWindowSurfaceFactory = eGLWindowSurfaceFactory;
    }

    public void setEGLConfigChooser(EGLConfigChooser eGLConfigChooser) {
        checkRenderThreadState();
        this.eglConfigChooser = eGLConfigChooser;
    }

    public void setEGLConfigChooser(boolean z) {
        setEGLConfigChooser(new SimpleEGLConfigChooser(z));
    }

    public void setEGLConfigChooser(int i, int i2, int i3, int i4, int i5, int i6) {
        setEGLConfigChooser(new ComponentSizeChooser(i, i2, i3, i4, i5, i6));
    }

    public void setEGLContextClientVersion(int i) {
        checkRenderThreadState();
        this.eglContextClientVersion = i;
    }

    public void setRenderMode(int i) {
        this.glThread.setRenderMode(i);
    }

    public int getRenderMode() {
        return this.glThread.getRenderMode();
    }

    public void requestRender() {
        this.glThread.requestRender();
    }

    public void surfaceCreated(SurfaceTexture surfaceTexture) {
        this.glThread.surfaceCreated();
    }

    public void surfaceDestroyed(SurfaceTexture surfaceTexture) {
        this.glThread.surfaceDestroyed();
    }

    public void surfaceChanged(SurfaceTexture surfaceTexture, int i, int i2, int i3) {
        this.glThread.onWindowResize(i2, i3);
    }

    public void onPause() {
        this.glThread.onPause();
    }

    public void onResume() {
        this.glThread.onResume();
    }

    public void queueEvent(Runnable runnable) {
        this.glThread.queueEvent(runnable);
    }

    @Override // android.view.TextureView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.detached && this.renderer != null) {
            int renderMode = this.glThread != null ? this.glThread.getRenderMode() : 1;
            this.glThread = new GLThread(this.mThisWeakRef);
            if (renderMode != 1) {
                this.glThread.setRenderMode(renderMode);
            }
            this.glThread.start();
        }
        this.detached = false;
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        if (this.glThread != null) {
            this.glThread.requestExitAndWait();
        }
        this.detached = true;
        super.onDetachedFromWindow();
    }

    @Override // android.view.View.OnLayoutChangeListener
    public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        surfaceChanged(getSurfaceTexture(), 0, i3 - i, i4 - i2);
    }

    public void addSurfaceTextureListener(TextureView.SurfaceTextureListener surfaceTextureListener) {
        this.surfaceTextureListeners.add(surfaceTextureListener);
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
        surfaceCreated(surfaceTexture);
        surfaceChanged(surfaceTexture, 0, i, i2);
        for (TextureView.SurfaceTextureListener surfaceTextureListener : this.surfaceTextureListeners) {
            surfaceTextureListener.onSurfaceTextureAvailable(surfaceTexture, i, i2);
        }
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
        surfaceChanged(surfaceTexture, 0, i, i2);
        for (TextureView.SurfaceTextureListener surfaceTextureListener : this.surfaceTextureListeners) {
            surfaceTextureListener.onSurfaceTextureSizeChanged(surfaceTexture, i, i2);
        }
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        surfaceDestroyed(surfaceTexture);
        for (TextureView.SurfaceTextureListener surfaceTextureListener : this.surfaceTextureListeners) {
            surfaceTextureListener.onSurfaceTextureDestroyed(surfaceTexture);
        }
        return true;
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        requestRender();
        for (TextureView.SurfaceTextureListener surfaceTextureListener : this.surfaceTextureListeners) {
            surfaceTextureListener.onSurfaceTextureUpdated(surfaceTexture);
        }
    }

    /* loaded from: classes2.dex */
    private class DefaultContextFactory implements EGLContextFactory {
        private int EGL_CONTEXT_CLIENT_VERSION;

        private DefaultContextFactory() {
            this.EGL_CONTEXT_CLIENT_VERSION = 12440;
        }

        @Override // jp.co.cyberagent.android.gpuimage.GLTextureView.EGLContextFactory
        public EGLContext createContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig) {
            int[] iArr = {this.EGL_CONTEXT_CLIENT_VERSION, GLTextureView.this.eglContextClientVersion, 12344};
            EGLContext eGLContext = EGL10.EGL_NO_CONTEXT;
            if (GLTextureView.this.eglContextClientVersion == 0) {
                iArr = null;
            }
            return egl10.eglCreateContext(eGLDisplay, eGLConfig, eGLContext, iArr);
        }

        @Override // jp.co.cyberagent.android.gpuimage.GLTextureView.EGLContextFactory
        public void destroyContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLContext eGLContext) {
            if (egl10.eglDestroyContext(eGLDisplay, eGLContext)) {
                return;
            }
            Log.e("DefaultContextFactory", "display:" + eGLDisplay + " context: " + eGLContext);
            EglHelper.throwEglException("eglDestroyContex", egl10.eglGetError());
        }
    }

    /* loaded from: classes2.dex */
    private static class DefaultWindowSurfaceFactory implements EGLWindowSurfaceFactory {
        private DefaultWindowSurfaceFactory() {
        }

        @Override // jp.co.cyberagent.android.gpuimage.GLTextureView.EGLWindowSurfaceFactory
        public EGLSurface createWindowSurface(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig, Object obj) {
            try {
                return egl10.eglCreateWindowSurface(eGLDisplay, eGLConfig, obj, null);
            } catch (IllegalArgumentException e) {
                Log.e(GLTextureView.TAG, "eglCreateWindowSurface", e);
                return null;
            }
        }

        @Override // jp.co.cyberagent.android.gpuimage.GLTextureView.EGLWindowSurfaceFactory
        public void destroySurface(EGL10 egl10, EGLDisplay eGLDisplay, EGLSurface eGLSurface) {
            egl10.eglDestroySurface(eGLDisplay, eGLSurface);
        }
    }

    /* loaded from: classes2.dex */
    private abstract class BaseConfigChooser implements EGLConfigChooser {
        protected int[] mConfigSpec;

        abstract EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig[] eGLConfigArr);

        public BaseConfigChooser(int[] iArr) {
            this.mConfigSpec = filterConfigSpec(iArr);
        }

        @Override // jp.co.cyberagent.android.gpuimage.GLTextureView.EGLConfigChooser
        public EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay) {
            int[] iArr = new int[1];
            if (!egl10.eglChooseConfig(eGLDisplay, this.mConfigSpec, null, 0, iArr)) {
                throw new IllegalArgumentException("eglChooseConfig failed");
            }
            int i = iArr[0];
            if (i <= 0) {
                throw new IllegalArgumentException("No configs match configSpec");
            }
            EGLConfig[] eGLConfigArr = new EGLConfig[i];
            if (!egl10.eglChooseConfig(eGLDisplay, this.mConfigSpec, eGLConfigArr, i, iArr)) {
                throw new IllegalArgumentException("eglChooseConfig#2 failed");
            }
            EGLConfig chooseConfig = chooseConfig(egl10, eGLDisplay, eGLConfigArr);
            if (chooseConfig != null) {
                return chooseConfig;
            }
            throw new IllegalArgumentException("No config chosen");
        }

        private int[] filterConfigSpec(int[] iArr) {
            if (GLTextureView.this.eglContextClientVersion != 2) {
                return iArr;
            }
            int length = iArr.length;
            int[] iArr2 = new int[length + 2];
            int i = length - 1;
            System.arraycopy(iArr, 0, iArr2, 0, i);
            iArr2[i] = 12352;
            iArr2[length] = 4;
            iArr2[length + 1] = 12344;
            return iArr2;
        }
    }

    /* loaded from: classes2.dex */
    private class ComponentSizeChooser extends BaseConfigChooser {
        protected int alphaSize;
        protected int blueSize;
        protected int depthSize;
        protected int greenSize;
        protected int redSize;
        protected int stencilSize;
        private int[] value;

        public ComponentSizeChooser(int i, int i2, int i3, int i4, int i5, int i6) {
            super(new int[]{12324, i, 12323, i2, 12322, i3, 12321, i4, 12325, i5, 12326, i6, 12344});
            this.value = new int[1];
            this.redSize = i;
            this.greenSize = i2;
            this.blueSize = i3;
            this.alphaSize = i4;
            this.depthSize = i5;
            this.stencilSize = i6;
        }

        @Override // jp.co.cyberagent.android.gpuimage.GLTextureView.BaseConfigChooser
        public EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig[] eGLConfigArr) {
            for (EGLConfig eGLConfig : eGLConfigArr) {
                int findConfigAttrib = findConfigAttrib(egl10, eGLDisplay, eGLConfig, 12325, 0);
                int findConfigAttrib2 = findConfigAttrib(egl10, eGLDisplay, eGLConfig, 12326, 0);
                if (findConfigAttrib >= this.depthSize && findConfigAttrib2 >= this.stencilSize) {
                    int findConfigAttrib3 = findConfigAttrib(egl10, eGLDisplay, eGLConfig, 12324, 0);
                    int findConfigAttrib4 = findConfigAttrib(egl10, eGLDisplay, eGLConfig, 12323, 0);
                    int findConfigAttrib5 = findConfigAttrib(egl10, eGLDisplay, eGLConfig, 12322, 0);
                    int findConfigAttrib6 = findConfigAttrib(egl10, eGLDisplay, eGLConfig, 12321, 0);
                    if (findConfigAttrib3 == this.redSize && findConfigAttrib4 == this.greenSize && findConfigAttrib5 == this.blueSize && findConfigAttrib6 == this.alphaSize) {
                        return eGLConfig;
                    }
                }
            }
            return null;
        }

        private int findConfigAttrib(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig, int i, int i2) {
            return egl10.eglGetConfigAttrib(eGLDisplay, eGLConfig, i, this.value) ? this.value[0] : i2;
        }
    }

    /* loaded from: classes2.dex */
    private class SimpleEGLConfigChooser extends ComponentSizeChooser {
        public SimpleEGLConfigChooser(boolean z) {
            super(8, 8, 8, 0, z ? 16 : 0, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class EglHelper {
        EGL10 egl;
        EGLConfig eglConfig;
        EGLContext eglContext;
        EGLDisplay eglDisplay;
        EGLSurface eglSurface;
        private WeakReference<GLTextureView> glTextureViewWeakRef;

        public EglHelper(WeakReference<GLTextureView> weakReference) {
            this.glTextureViewWeakRef = weakReference;
        }

        public void start() {
            this.egl = (EGL10) EGLContext.getEGL();
            this.eglDisplay = this.egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
            if (this.eglDisplay == EGL10.EGL_NO_DISPLAY) {
                throw new RuntimeException("eglGetDisplay failed");
            }
            if (!this.egl.eglInitialize(this.eglDisplay, new int[2])) {
                throw new RuntimeException("eglInitialize failed");
            }
            GLTextureView gLTextureView = this.glTextureViewWeakRef.get();
            if (gLTextureView != null) {
                this.eglConfig = gLTextureView.eglConfigChooser.chooseConfig(this.egl, this.eglDisplay);
                this.eglContext = gLTextureView.eglContextFactory.createContext(this.egl, this.eglDisplay, this.eglConfig);
            } else {
                this.eglConfig = null;
                this.eglContext = null;
            }
            if (this.eglContext == null || this.eglContext == EGL10.EGL_NO_CONTEXT) {
                this.eglContext = null;
                throwEglException("createContext");
            }
            this.eglSurface = null;
        }

        public boolean createSurface() {
            if (this.egl == null) {
                throw new RuntimeException("egl not initialized");
            }
            if (this.eglDisplay == null) {
                throw new RuntimeException("eglDisplay not initialized");
            }
            if (this.eglConfig == null) {
                throw new RuntimeException("eglConfig not initialized");
            }
            destroySurfaceImp();
            GLTextureView gLTextureView = this.glTextureViewWeakRef.get();
            if (gLTextureView != null) {
                this.eglSurface = gLTextureView.eglWindowSurfaceFactory.createWindowSurface(this.egl, this.eglDisplay, this.eglConfig, gLTextureView.getSurfaceTexture());
            } else {
                this.eglSurface = null;
            }
            if (this.eglSurface == null || this.eglSurface == EGL10.EGL_NO_SURFACE) {
                if (this.egl.eglGetError() == 12299) {
                    Log.e("EglHelper", "createWindowSurface returned EGL_BAD_NATIVE_WINDOW.");
                }
                return false;
            } else if (this.egl.eglMakeCurrent(this.eglDisplay, this.eglSurface, this.eglSurface, this.eglContext)) {
                return true;
            } else {
                logEglErrorAsWarning("EGLHelper", "eglMakeCurrent", this.egl.eglGetError());
                return false;
            }
        }

        GL createGL() {
            GL gl = this.eglContext.getGL();
            GLTextureView gLTextureView = this.glTextureViewWeakRef.get();
            if (gLTextureView != null) {
                if (gLTextureView.glWrapper != null) {
                    gl = gLTextureView.glWrapper.wrap(gl);
                }
                if ((gLTextureView.debugFlags & 3) != 0) {
                    return GLDebugHelper.wrap(gl, (gLTextureView.debugFlags & 1) != 0 ? 1 : 0, (gLTextureView.debugFlags & 2) != 0 ? new LogWriter() : null);
                }
                return gl;
            }
            return gl;
        }

        public int swap() {
            if (this.egl.eglSwapBuffers(this.eglDisplay, this.eglSurface)) {
                return 12288;
            }
            return this.egl.eglGetError();
        }

        public void destroySurface() {
            destroySurfaceImp();
        }

        private void destroySurfaceImp() {
            if (this.eglSurface == null || this.eglSurface == EGL10.EGL_NO_SURFACE) {
                return;
            }
            this.egl.eglMakeCurrent(this.eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
            GLTextureView gLTextureView = this.glTextureViewWeakRef.get();
            if (gLTextureView != null) {
                gLTextureView.eglWindowSurfaceFactory.destroySurface(this.egl, this.eglDisplay, this.eglSurface);
            }
            this.eglSurface = null;
        }

        public void finish() {
            if (this.eglContext != null) {
                GLTextureView gLTextureView = this.glTextureViewWeakRef.get();
                if (gLTextureView != null) {
                    gLTextureView.eglContextFactory.destroyContext(this.egl, this.eglDisplay, this.eglContext);
                }
                this.eglContext = null;
            }
            if (this.eglDisplay != null) {
                this.egl.eglTerminate(this.eglDisplay);
                this.eglDisplay = null;
            }
        }

        private void throwEglException(String str) {
            throwEglException(str, this.egl.eglGetError());
        }

        public static void throwEglException(String str, int i) {
            throw new RuntimeException(formatEglError(str, i));
        }

        public static void logEglErrorAsWarning(String str, String str2, int i) {
            Log.w(str, formatEglError(str2, i));
        }

        public static String formatEglError(String str, int i) {
            return str + " failed: " + i;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class GLThread extends Thread {
        private EglHelper eglHelper;
        private boolean exited;
        private WeakReference<GLTextureView> glTextureViewWeakRef;
        private boolean hasSurface;
        private boolean haveEglContext;
        private boolean haveEglSurface;
        private boolean paused;
        private boolean renderComplete;
        private boolean requestPaused;
        private boolean shouldExit;
        private boolean shouldReleaseEglContext;
        private boolean surfaceIsBad;
        private boolean waitingForSurface;
        private ArrayList<Runnable> eventQueue = new ArrayList<>();
        private boolean sizeChanged = true;
        private int width = 0;
        private int height = 0;
        private boolean requestRender = true;
        private int renderMode = 1;

        GLThread(WeakReference<GLTextureView> weakReference) {
            this.glTextureViewWeakRef = weakReference;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            setName("GLThread " + getId());
            try {
                guardedRun();
            } catch (InterruptedException unused) {
            } catch (Throwable th) {
                GLTextureView.glThreadManager.threadExiting(this);
                throw th;
            }
            GLTextureView.glThreadManager.threadExiting(this);
        }

        private void stopEglSurfaceLocked() {
            if (this.haveEglSurface) {
                this.haveEglSurface = false;
                this.eglHelper.destroySurface();
            }
        }

        private void stopEglContextLocked() {
            if (this.haveEglContext) {
                this.eglHelper.finish();
                this.haveEglContext = false;
                GLTextureView.glThreadManager.releaseEglContextLocked(this);
            }
        }

        private void guardedRun() throws InterruptedException {
            boolean z;
            boolean z2;
            boolean z3;
            boolean z4;
            this.eglHelper = new EglHelper(this.glTextureViewWeakRef);
            this.haveEglContext = false;
            this.haveEglSurface = false;
            boolean z5 = false;
            boolean z6 = false;
            boolean z7 = false;
            int i = 0;
            int i2 = 0;
            boolean z8 = false;
            boolean z9 = false;
            boolean z10 = false;
            boolean z11 = false;
            boolean z12 = false;
            GL10 gl10 = null;
            while (true) {
                Runnable runnable = null;
                while (true) {
                    try {
                        synchronized (GLTextureView.glThreadManager) {
                            while (!this.shouldExit) {
                                if (!this.eventQueue.isEmpty()) {
                                    runnable = this.eventQueue.remove(0);
                                    z = false;
                                } else {
                                    if (this.paused != this.requestPaused) {
                                        z3 = this.requestPaused;
                                        this.paused = this.requestPaused;
                                        GLTextureView.glThreadManager.notifyAll();
                                    } else {
                                        z3 = false;
                                    }
                                    if (this.shouldReleaseEglContext) {
                                        stopEglSurfaceLocked();
                                        stopEglContextLocked();
                                        this.shouldReleaseEglContext = false;
                                        z5 = true;
                                    }
                                    if (z6) {
                                        stopEglSurfaceLocked();
                                        stopEglContextLocked();
                                        z6 = false;
                                    }
                                    if (z3 && this.haveEglSurface) {
                                        stopEglSurfaceLocked();
                                    }
                                    if (z3 && this.haveEglContext) {
                                        GLTextureView gLTextureView = this.glTextureViewWeakRef.get();
                                        if (!(gLTextureView == null ? false : gLTextureView.preserveEGLContextOnPause) || GLTextureView.glThreadManager.shouldReleaseEGLContextWhenPausing()) {
                                            stopEglContextLocked();
                                        }
                                    }
                                    if (z3 && GLTextureView.glThreadManager.shouldTerminateEGLWhenPausing()) {
                                        this.eglHelper.finish();
                                    }
                                    if (!this.hasSurface && !this.waitingForSurface) {
                                        if (this.haveEglSurface) {
                                            stopEglSurfaceLocked();
                                        }
                                        this.waitingForSurface = true;
                                        this.surfaceIsBad = false;
                                        GLTextureView.glThreadManager.notifyAll();
                                    }
                                    if (this.hasSurface && this.waitingForSurface) {
                                        this.waitingForSurface = false;
                                        GLTextureView.glThreadManager.notifyAll();
                                    }
                                    if (z7) {
                                        this.renderComplete = true;
                                        GLTextureView.glThreadManager.notifyAll();
                                        z7 = false;
                                        z12 = false;
                                    }
                                    if (readyToDraw()) {
                                        if (!this.haveEglContext) {
                                            if (z5) {
                                                z5 = false;
                                            } else if (GLTextureView.glThreadManager.tryAcquireEglContextLocked(this)) {
                                                try {
                                                    this.eglHelper.start();
                                                    this.haveEglContext = true;
                                                    GLTextureView.glThreadManager.notifyAll();
                                                    z8 = true;
                                                } catch (RuntimeException e) {
                                                    GLTextureView.glThreadManager.releaseEglContextLocked(this);
                                                    throw e;
                                                }
                                            }
                                        }
                                        if (!this.haveEglContext || this.haveEglSurface) {
                                            z4 = z9;
                                        } else {
                                            this.haveEglSurface = true;
                                            z4 = true;
                                            z10 = true;
                                            z11 = true;
                                        }
                                        if (this.haveEglSurface) {
                                            if (this.sizeChanged) {
                                                i = this.width;
                                                i2 = this.height;
                                                z = false;
                                                this.sizeChanged = false;
                                                z4 = true;
                                                z11 = true;
                                                z12 = true;
                                            } else {
                                                z = false;
                                            }
                                            this.requestRender = z;
                                            GLTextureView.glThreadManager.notifyAll();
                                            z9 = z4;
                                        } else {
                                            z9 = z4;
                                        }
                                    }
                                    GLTextureView.glThreadManager.wait();
                                }
                            }
                            synchronized (GLTextureView.glThreadManager) {
                                stopEglSurfaceLocked();
                                stopEglContextLocked();
                            }
                            return;
                        }
                        if (runnable != null) {
                            break;
                        }
                        if (z9) {
                            if (this.eglHelper.createSurface()) {
                                z9 = z;
                            } else {
                                synchronized (GLTextureView.glThreadManager) {
                                    this.surfaceIsBad = true;
                                    GLTextureView.glThreadManager.notifyAll();
                                }
                            }
                        }
                        if (z10) {
                            GL10 gl102 = (GL10) this.eglHelper.createGL();
                            GLTextureView.glThreadManager.checkGLDriver(gl102);
                            gl10 = gl102;
                            z10 = z;
                        }
                        if (z8) {
                            GLTextureView gLTextureView2 = this.glTextureViewWeakRef.get();
                            if (gLTextureView2 != null) {
                                gLTextureView2.renderer.onSurfaceCreated(gl10, this.eglHelper.eglConfig);
                            }
                            z8 = z;
                        }
                        if (z11) {
                            GLTextureView gLTextureView3 = this.glTextureViewWeakRef.get();
                            if (gLTextureView3 != null) {
                                gLTextureView3.renderer.onSurfaceChanged(gl10, i, i2);
                            }
                            z11 = z;
                        }
                        GLTextureView gLTextureView4 = this.glTextureViewWeakRef.get();
                        if (gLTextureView4 != null) {
                            gLTextureView4.renderer.onDrawFrame(gl10);
                        }
                        int swap = this.eglHelper.swap();
                        if (swap == 12288) {
                            z2 = true;
                        } else if (swap != 12302) {
                            EglHelper.logEglErrorAsWarning("GLThread", "eglSwapBuffers", swap);
                            synchronized (GLTextureView.glThreadManager) {
                                z2 = true;
                                this.surfaceIsBad = true;
                                GLTextureView.glThreadManager.notifyAll();
                            }
                        } else {
                            z2 = true;
                            z6 = true;
                        }
                        if (z12) {
                            z7 = z2;
                        }
                    } catch (Throwable th) {
                        synchronized (GLTextureView.glThreadManager) {
                            stopEglSurfaceLocked();
                            stopEglContextLocked();
                            throw th;
                        }
                    }
                }
                runnable.run();
            }
        }

        public boolean ableToDraw() {
            return this.haveEglContext && this.haveEglSurface && readyToDraw();
        }

        private boolean readyToDraw() {
            return !this.paused && this.hasSurface && !this.surfaceIsBad && this.width > 0 && this.height > 0 && (this.requestRender || this.renderMode == 1);
        }

        public void setRenderMode(int i) {
            if (i >= 0 && i <= 1) {
                synchronized (GLTextureView.glThreadManager) {
                    this.renderMode = i;
                    GLTextureView.glThreadManager.notifyAll();
                }
                return;
            }
            throw new IllegalArgumentException("renderMode");
        }

        public int getRenderMode() {
            int i;
            synchronized (GLTextureView.glThreadManager) {
                i = this.renderMode;
            }
            return i;
        }

        public void requestRender() {
            synchronized (GLTextureView.glThreadManager) {
                this.requestRender = true;
                GLTextureView.glThreadManager.notifyAll();
            }
        }

        public void surfaceCreated() {
            synchronized (GLTextureView.glThreadManager) {
                this.hasSurface = true;
                GLTextureView.glThreadManager.notifyAll();
                while (this.waitingForSurface && !this.exited) {
                    try {
                        GLTextureView.glThreadManager.wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void surfaceDestroyed() {
            synchronized (GLTextureView.glThreadManager) {
                this.hasSurface = false;
                GLTextureView.glThreadManager.notifyAll();
                while (!this.waitingForSurface && !this.exited) {
                    try {
                        GLTextureView.glThreadManager.wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void onPause() {
            synchronized (GLTextureView.glThreadManager) {
                this.requestPaused = true;
                GLTextureView.glThreadManager.notifyAll();
                while (!this.exited && !this.paused) {
                    try {
                        GLTextureView.glThreadManager.wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void onResume() {
            synchronized (GLTextureView.glThreadManager) {
                this.requestPaused = false;
                this.requestRender = true;
                this.renderComplete = false;
                GLTextureView.glThreadManager.notifyAll();
                while (!this.exited && this.paused && !this.renderComplete) {
                    try {
                        GLTextureView.glThreadManager.wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void onWindowResize(int i, int i2) {
            synchronized (GLTextureView.glThreadManager) {
                this.width = i;
                this.height = i2;
                this.sizeChanged = true;
                this.requestRender = true;
                this.renderComplete = false;
                GLTextureView.glThreadManager.notifyAll();
                while (!this.exited && !this.paused && !this.renderComplete && ableToDraw()) {
                    try {
                        GLTextureView.glThreadManager.wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void requestExitAndWait() {
            synchronized (GLTextureView.glThreadManager) {
                this.shouldExit = true;
                GLTextureView.glThreadManager.notifyAll();
                while (!this.exited) {
                    try {
                        GLTextureView.glThreadManager.wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void requestReleaseEglContextLocked() {
            this.shouldReleaseEglContext = true;
            GLTextureView.glThreadManager.notifyAll();
        }

        public void queueEvent(Runnable runnable) {
            if (runnable != null) {
                synchronized (GLTextureView.glThreadManager) {
                    this.eventQueue.add(runnable);
                    GLTextureView.glThreadManager.notifyAll();
                }
                return;
            }
            throw new IllegalArgumentException("r must not be null");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class LogWriter extends Writer {
        private StringBuilder builder = new StringBuilder();

        LogWriter() {
        }

        @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            flushBuilder();
        }

        @Override // java.io.Writer, java.io.Flushable
        public void flush() {
            flushBuilder();
        }

        @Override // java.io.Writer
        public void write(char[] cArr, int i, int i2) {
            for (int i3 = 0; i3 < i2; i3++) {
                char c = cArr[i + i3];
                if (c == '\n') {
                    flushBuilder();
                } else {
                    this.builder.append(c);
                }
            }
        }

        private void flushBuilder() {
            if (this.builder.length() > 0) {
                Log.v(GLTextureView.TAG, this.builder.toString());
                this.builder.delete(0, this.builder.length());
            }
        }
    }

    private void checkRenderThreadState() {
        if (this.glThread != null) {
            throw new IllegalStateException("setRenderer has already been called for this instance.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class GLThreadManager {
        private static String TAG = "GLThreadManager";
        private static final int kGLES_20 = 131072;
        private static final String kMSM7K_RENDERER_PREFIX = "Q3Dimension MSM7500 ";
        private GLThread eglOwner;
        private boolean glesDriverCheckComplete;
        private int glesVersion;
        private boolean glesVersionCheckComplete;
        private boolean limitedGLESContexts;
        private boolean multipleGLESContextsAllowed;

        private GLThreadManager() {
        }

        public synchronized void threadExiting(GLThread gLThread) {
            gLThread.exited = true;
            if (this.eglOwner == gLThread) {
                this.eglOwner = null;
            }
            notifyAll();
        }

        public boolean tryAcquireEglContextLocked(GLThread gLThread) {
            if (this.eglOwner == gLThread || this.eglOwner == null) {
                this.eglOwner = gLThread;
                notifyAll();
                return true;
            }
            checkGLESVersion();
            if (this.multipleGLESContextsAllowed) {
                return true;
            }
            if (this.eglOwner != null) {
                this.eglOwner.requestReleaseEglContextLocked();
                return false;
            }
            return false;
        }

        public void releaseEglContextLocked(GLThread gLThread) {
            if (this.eglOwner == gLThread) {
                this.eglOwner = null;
            }
            notifyAll();
        }

        public synchronized boolean shouldReleaseEGLContextWhenPausing() {
            return this.limitedGLESContexts;
        }

        public synchronized boolean shouldTerminateEGLWhenPausing() {
            checkGLESVersion();
            return !this.multipleGLESContextsAllowed;
        }

        public synchronized void checkGLDriver(GL10 gl10) {
            if (!this.glesDriverCheckComplete) {
                checkGLESVersion();
                String glGetString = gl10.glGetString(7937);
                if (this.glesVersion < 131072) {
                    this.multipleGLESContextsAllowed = !glGetString.startsWith(kMSM7K_RENDERER_PREFIX);
                    notifyAll();
                }
                this.limitedGLESContexts = !this.multipleGLESContextsAllowed;
                this.glesDriverCheckComplete = true;
            }
        }

        private void checkGLESVersion() {
            if (this.glesVersionCheckComplete) {
                return;
            }
            this.glesVersionCheckComplete = true;
        }
    }
}
