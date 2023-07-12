package com.daasuu.gpuv.egl;

import android.opengl.GLES20;

/* loaded from: classes.dex */
public class GlFramebufferObject {
    private int framebufferName;
    private int height;
    private int renderBufferName;
    private int texName;
    private int width;

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getTexName() {
        return this.texName;
    }

    public void setup(int i, int i2) {
        int[] iArr = new int[1];
        GLES20.glGetIntegerv(3379, iArr, 0);
        if (i > iArr[0] || i2 > iArr[0]) {
            throw new IllegalArgumentException("GL_MAX_TEXTURE_SIZE " + iArr[0]);
        }
        GLES20.glGetIntegerv(34024, iArr, 0);
        if (i > iArr[0] || i2 > iArr[0]) {
            throw new IllegalArgumentException("GL_MAX_RENDERBUFFER_SIZE " + iArr[0]);
        }
        GLES20.glGetIntegerv(36006, iArr, 0);
        int i3 = iArr[0];
        GLES20.glGetIntegerv(36007, iArr, 0);
        int i4 = iArr[0];
        GLES20.glGetIntegerv(32873, iArr, 0);
        int i5 = iArr[0];
        release();
        try {
            this.width = i;
            this.height = i2;
            GLES20.glGenFramebuffers(iArr.length, iArr, 0);
            this.framebufferName = iArr[0];
            GLES20.glBindFramebuffer(36160, this.framebufferName);
            GLES20.glGenRenderbuffers(iArr.length, iArr, 0);
            this.renderBufferName = iArr[0];
            GLES20.glBindRenderbuffer(36161, this.renderBufferName);
            GLES20.glRenderbufferStorage(36161, 33189, i, i2);
            GLES20.glFramebufferRenderbuffer(36160, 36096, 36161, this.renderBufferName);
            GLES20.glGenTextures(iArr.length, iArr, 0);
            this.texName = iArr[0];
            GLES20.glBindTexture(3553, this.texName);
            EglUtil.setupSampler(3553, 9729, 9728);
            GLES20.glTexImage2D(3553, 0, 6408, i, i2, 0, 6408, 5121, null);
            GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.texName, 0);
            int glCheckFramebufferStatus = GLES20.glCheckFramebufferStatus(36160);
            if (glCheckFramebufferStatus != 36053) {
                throw new RuntimeException("Failed to initialize framebuffer object " + glCheckFramebufferStatus);
            }
            GLES20.glBindFramebuffer(36160, i3);
            GLES20.glBindRenderbuffer(36161, i4);
            GLES20.glBindTexture(3553, i5);
        } catch (RuntimeException e) {
            release();
            throw e;
        }
    }

    public void release() {
        int[] iArr = {this.texName};
        GLES20.glDeleteTextures(iArr.length, iArr, 0);
        this.texName = 0;
        iArr[0] = this.renderBufferName;
        GLES20.glDeleteRenderbuffers(iArr.length, iArr, 0);
        this.renderBufferName = 0;
        iArr[0] = this.framebufferName;
        GLES20.glDeleteFramebuffers(iArr.length, iArr, 0);
        this.framebufferName = 0;
    }

    public void enable() {
        GLES20.glBindFramebuffer(36160, this.framebufferName);
    }
}
