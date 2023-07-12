package com.daasuu.gpuv.egl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import com.daasuu.gpuv.egl.filter.GlFilter;
import java.util.LinkedList;
import java.util.Queue;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public abstract class GlFrameBufferObjectRenderer implements GLSurfaceView.Renderer {
    private GlFramebufferObject framebufferObject;
    private GlFilter normalShader;
    private final Queue<Runnable> runOnDraw = new LinkedList();

    protected void finalize() throws Throwable {
    }

    public abstract void onDrawFrame(GlFramebufferObject glFramebufferObject);

    public abstract void onSurfaceChanged(int i, int i2);

    public abstract void onSurfaceCreated(EGLConfig eGLConfig);

    @Override // android.opengl.GLSurfaceView.Renderer
    public final void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        this.framebufferObject = new GlFramebufferObject();
        this.normalShader = new GlFilter();
        this.normalShader.setup();
        onSurfaceCreated(eGLConfig);
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public final void onSurfaceChanged(GL10 gl10, int i, int i2) {
        this.framebufferObject.setup(i, i2);
        this.normalShader.setFrameSize(i, i2);
        onSurfaceChanged(i, i2);
        GLES20.glViewport(0, 0, this.framebufferObject.getWidth(), this.framebufferObject.getHeight());
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public final void onDrawFrame(GL10 gl10) {
        synchronized (this.runOnDraw) {
            while (!this.runOnDraw.isEmpty()) {
                this.runOnDraw.poll().run();
            }
        }
        this.framebufferObject.enable();
        onDrawFrame(this.framebufferObject);
        GLES20.glBindFramebuffer(36160, 0);
        GLES20.glClear(16640);
        this.normalShader.draw(this.framebufferObject.getTexName(), null);
    }
}
