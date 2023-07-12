package com.daasuu.gpuv.egl.filter;

import android.opengl.GLES20;

/* loaded from: classes.dex */
public class GlToneFilter extends GlThreex3TextureSamplingFilter {
    private static final String FRAGMENT_SHADER = "precision highp float;\nuniform lowp sampler2D sTexture;\nvarying vec2 textureCoordinate;\nvarying vec2 leftTextureCoordinate;\nvarying vec2 rightTextureCoordinate;\nvarying vec2 topTextureCoordinate;\nvarying vec2 topLeftTextureCoordinate;\nvarying vec2 topRightTextureCoordinate;\nvarying vec2 bottomTextureCoordinate;\nvarying vec2 bottomLeftTextureCoordinate;\nvarying vec2 bottomRightTextureCoordinate;\nuniform highp float threshold;uniform highp float quantizationLevels;const highp vec3 W = vec3(0.2125, 0.7154, 0.0721);void main() {\nvec4 textureColor = texture2D(sTexture, textureCoordinate);float bottomLeftIntensity = texture2D(sTexture, bottomLeftTextureCoordinate).r;float topRightIntensity = texture2D(sTexture, topRightTextureCoordinate).r;float topLeftIntensity = texture2D(sTexture, topLeftTextureCoordinate).r;float bottomRightIntensity = texture2D(sTexture, bottomRightTextureCoordinate).r;float leftIntensity = texture2D(sTexture, leftTextureCoordinate).r;float rightIntensity = texture2D(sTexture, rightTextureCoordinate).r;float bottomIntensity = texture2D(sTexture, bottomTextureCoordinate).r;float topIntensity = texture2D(sTexture, topTextureCoordinate).r;float h = -topLeftIntensity - 2.0 * topIntensity - topRightIntensity + bottomLeftIntensity + 2.0 * bottomIntensity + bottomRightIntensity;float v = -bottomLeftIntensity - 2.0 * leftIntensity - topLeftIntensity + bottomRightIntensity + 2.0 * rightIntensity + topRightIntensity;float mag = length(vec2(h, v));vec3 posterizedImageColor = floor((textureColor.rgb * quantizationLevels) + 0.5) / quantizationLevels;float thresholdTest = 1.0 - step(threshold, mag);gl_FragColor = vec4(posterizedImageColor * thresholdTest, textureColor.a);}";
    private float quantizationLevels;
    private float threshold;

    public GlToneFilter() {
        super(FRAGMENT_SHADER);
        this.threshold = 0.2f;
        this.quantizationLevels = 10.0f;
    }

    public float getThreshold() {
        return this.threshold;
    }

    public void setThreshold(float f) {
        this.threshold = f;
    }

    public float getQuantizationLevels() {
        return this.quantizationLevels;
    }

    public void setQuantizationLevels(float f) {
        this.quantizationLevels = f;
    }

    @Override // com.daasuu.gpuv.egl.filter.GlThreex3TextureSamplingFilter, com.daasuu.gpuv.egl.filter.GlFilter
    public void onDraw() {
        GLES20.glUniform1f(getHandle("threshold"), this.threshold);
        GLES20.glUniform1f(getHandle("quantizationLevels"), this.quantizationLevels);
    }
}
