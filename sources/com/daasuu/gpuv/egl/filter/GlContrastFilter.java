package com.daasuu.gpuv.egl.filter;

import android.opengl.GLES20;

/* loaded from: classes.dex */
public class GlContrastFilter extends GlFilter {
    private static final String CONTRAST_FRAGMENT_SHADER = "precision mediump float; varying vec2 vTextureCoord;\n \n uniform lowp sampler2D sTexture;\n uniform lowp float contrast;\n \n void main()\n {\n     lowp vec4 textureColor = texture2D(sTexture, vTextureCoord);\n     \n     gl_FragColor = vec4(((textureColor.rgb - vec3(0.5)) * contrast + vec3(0.5)), textureColor.w);\n }";
    private float contrast;

    public GlContrastFilter() {
        super("attribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\ngl_Position = aPosition;\nvTextureCoord = aTextureCoord.xy;\n}\n", CONTRAST_FRAGMENT_SHADER);
        this.contrast = 1.2f;
    }

    public void setContrast(float f) {
        this.contrast = f;
    }

    @Override // com.daasuu.gpuv.egl.filter.GlFilter
    public void onDraw() {
        GLES20.glUniform1f(getHandle("contrast"), this.contrast);
    }
}
