package com.daasuu.gpuv.egl.filter;

import android.opengl.GLES20;

/* loaded from: classes.dex */
public class GlPosterizeFilter extends GlFilter {
    private static final String POSTERIZE_FRAGMENT_SHADER = "precision mediump float; varying vec2 vTextureCoord;\n\nuniform lowp sampler2D sTexture;\nuniform highp float colorLevels;\n\nvoid main()\n{\n   highp vec4 textureColor = texture2D(sTexture, vTextureCoord);\n   \n   gl_FragColor = floor((textureColor * colorLevels) + vec4(0.5)) / colorLevels;\n}";
    private int colorLevels;

    public GlPosterizeFilter() {
        super("attribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\ngl_Position = aPosition;\nvTextureCoord = aTextureCoord.xy;\n}\n", POSTERIZE_FRAGMENT_SHADER);
        this.colorLevels = 10;
    }

    public void setColorLevels(int i) {
        if (i < 0) {
            this.colorLevels = 0;
        } else if (i > 256) {
            this.colorLevels = 256;
        } else {
            this.colorLevels = i;
        }
    }

    @Override // com.daasuu.gpuv.egl.filter.GlFilter
    public void onDraw() {
        GLES20.glUniform1f(getHandle("colorLevels"), this.colorLevels);
    }
}
