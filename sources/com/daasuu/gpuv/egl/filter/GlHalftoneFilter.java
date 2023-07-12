package com.daasuu.gpuv.egl.filter;

import android.opengl.GLES20;

/* loaded from: classes.dex */
public class GlHalftoneFilter extends GlFilter {
    private static final String HALFTONE_FRAGMENT_SHADER = "precision mediump float; varying vec2 vTextureCoord;\n uniform lowp sampler2D sTexture;\nuniform highp float fractionalWidthOfPixel;\nuniform highp float aspectRatio;\nconst highp vec3 W = vec3(0.2125, 0.7154, 0.0721);\nvoid main()\n{\n  highp vec2 sampleDivisor = vec2(fractionalWidthOfPixel, fractionalWidthOfPixel / aspectRatio);\n  highp vec2 samplePos = vTextureCoord - mod(vTextureCoord, sampleDivisor) + 0.5 * sampleDivisor;\n  highp vec2 textureCoordinateToUse = vec2(vTextureCoord.x, (vTextureCoord.y * aspectRatio + 0.5 - 0.5 * aspectRatio));\n  highp vec2 adjustedSamplePos = vec2(samplePos.x, (samplePos.y * aspectRatio + 0.5 - 0.5 * aspectRatio));\n  highp float distanceFromSamplePoint = distance(adjustedSamplePos, textureCoordinateToUse);\n  lowp vec3 sampledColor = texture2D(sTexture, samplePos).rgb;\n  highp float dotScaling = 1.0 - dot(sampledColor, W);\n  lowp float checkForPresenceWithinDot = 1.0 - step(distanceFromSamplePoint, (fractionalWidthOfPixel * 0.5) * dotScaling);\n  gl_FragColor = vec4(vec3(checkForPresenceWithinDot), 1.0);\n}";
    private float aspectRatio;
    private float fractionalWidthOfPixel;

    public GlHalftoneFilter() {
        super("attribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\ngl_Position = aPosition;\nvTextureCoord = aTextureCoord.xy;\n}\n", HALFTONE_FRAGMENT_SHADER);
        this.fractionalWidthOfPixel = 0.01f;
        this.aspectRatio = 1.0f;
    }

    public void setFractionalWidthOfAPixel(float f) {
        this.fractionalWidthOfPixel = f;
    }

    @Override // com.daasuu.gpuv.egl.filter.GlFilter
    public void setFrameSize(int i, int i2) {
        super.setFrameSize(i, i2);
        this.aspectRatio = i2 / i;
    }

    @Override // com.daasuu.gpuv.egl.filter.GlFilter
    public void onDraw() {
        GLES20.glUniform1f(getHandle("fractionalWidthOfPixel"), this.fractionalWidthOfPixel);
        GLES20.glUniform1f(getHandle("aspectRatio"), this.aspectRatio);
    }
}
