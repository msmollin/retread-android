package com.daasuu.gpuv.egl.filter;

import android.opengl.GLES20;

/* loaded from: classes.dex */
public class GlPixelationFilter extends GlFilter {
    private static final String PIXELATION_FRAGMENT_SHADER = "precision highp float;\nvarying highp vec2 vTextureCoord;\nuniform float imageWidthFactor;\nuniform float imageHeightFactor;\nuniform lowp sampler2D sTexture;\nuniform float pixel;\nvoid main()\n{\n  vec2 uv  = vTextureCoord.xy;\n  float dx = pixel * imageWidthFactor;\n  float dy = pixel * imageHeightFactor;\n  vec2 coord = vec2(dx * floor(uv.x / dx), dy * floor(uv.y / dy));\n  vec3 tc = texture2D(sTexture, coord).xyz;\n  gl_FragColor = vec4(tc, 1.0);\n}";
    private float imageHeightFactor;
    private float imageWidthFactor;
    private float pixel;

    public GlPixelationFilter() {
        super("attribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\ngl_Position = aPosition;\nvTextureCoord = aTextureCoord.xy;\n}\n", PIXELATION_FRAGMENT_SHADER);
        this.pixel = 1.0f;
        this.imageWidthFactor = 0.0013888889f;
        this.imageHeightFactor = 0.0013888889f;
    }

    @Override // com.daasuu.gpuv.egl.filter.GlFilter
    public void setFrameSize(int i, int i2) {
        super.setFrameSize(i, i2);
        this.imageWidthFactor = 1.0f / i;
        this.imageHeightFactor = 1.0f / i2;
    }

    @Override // com.daasuu.gpuv.egl.filter.GlFilter
    public void onDraw() {
        GLES20.glUniform1f(getHandle("pixel"), this.pixel);
        GLES20.glUniform1f(getHandle("imageWidthFactor"), this.imageWidthFactor);
        GLES20.glUniform1f(getHandle("imageHeightFactor"), this.imageHeightFactor);
    }

    public void setPixel(float f) {
        this.pixel = f;
    }
}
