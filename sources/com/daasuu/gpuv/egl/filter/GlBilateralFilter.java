package com.daasuu.gpuv.egl.filter;

import android.opengl.GLES20;

/* loaded from: classes.dex */
public class GlBilateralFilter extends GlFilter {
    private static final String FRAGMENT_SHADER = "precision mediump float;uniform lowp sampler2D sTexture;const lowp int GAUSSIAN_SAMPLES = 9;varying highp vec2 vTextureCoord;varying highp vec2 blurCoordinates[GAUSSIAN_SAMPLES];const mediump float distanceNormalizationFactor = 1.5;void main() {lowp vec4 centralColor = texture2D(sTexture, blurCoordinates[4]);lowp float gaussianWeightTotal = 0.18;lowp vec4 sum = centralColor * 0.18;lowp vec4 sampleColor = texture2D(sTexture, blurCoordinates[0]);lowp float distanceFromCentralColor;distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);lowp float gaussianWeight = 0.05 * (1.0 - distanceFromCentralColor);gaussianWeightTotal += gaussianWeight;sum += sampleColor * gaussianWeight;sampleColor = texture2D(sTexture, blurCoordinates[1]);distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);gaussianWeightTotal += gaussianWeight;sum += sampleColor * gaussianWeight;sampleColor = texture2D(sTexture, blurCoordinates[2]);distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);gaussianWeight = 0.12 * (1.0 - distanceFromCentralColor);gaussianWeightTotal += gaussianWeight;sum += sampleColor * gaussianWeight;sampleColor = texture2D(sTexture, blurCoordinates[3]);distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);gaussianWeight = 0.15 * (1.0 - distanceFromCentralColor);gaussianWeightTotal += gaussianWeight;sum += sampleColor * gaussianWeight;sampleColor = texture2D(sTexture, blurCoordinates[5]);distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);gaussianWeight = 0.15 * (1.0 - distanceFromCentralColor);gaussianWeightTotal += gaussianWeight;sum += sampleColor * gaussianWeight;sampleColor = texture2D(sTexture, blurCoordinates[6]);distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);gaussianWeight = 0.12 * (1.0 - distanceFromCentralColor);gaussianWeightTotal += gaussianWeight;sum += sampleColor * gaussianWeight;sampleColor = texture2D(sTexture, blurCoordinates[7]);distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);gaussianWeightTotal += gaussianWeight;sum += sampleColor * gaussianWeight;sampleColor = texture2D(sTexture, blurCoordinates[8]);distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);gaussianWeight = 0.05 * (1.0 - distanceFromCentralColor);gaussianWeightTotal += gaussianWeight;sum += sampleColor * gaussianWeight;gl_FragColor = sum / gaussianWeightTotal;}";
    private static final String VERTEX_SHADER = "attribute vec4 aPosition;attribute vec4 aTextureCoord;const lowp int GAUSSIAN_SAMPLES = 9;uniform highp float texelWidthOffset;uniform highp float texelHeightOffset;uniform highp float blurSize;varying highp vec2 vTextureCoord;varying highp vec2 blurCoordinates[GAUSSIAN_SAMPLES];void main() {gl_Position = aPosition;vTextureCoord = aTextureCoord.xy;int multiplier = 0;highp vec2 blurStep;highp vec2 singleStepOffset = vec2(texelHeightOffset, texelWidthOffset) * blurSize;for (lowp int i = 0; i < GAUSSIAN_SAMPLES; i++) {multiplier = (i - ((GAUSSIAN_SAMPLES - 1) / 2));blurStep = float(multiplier) * singleStepOffset;blurCoordinates[i] = vTextureCoord.xy + blurStep;}}";
    private float blurSize;
    private float texelHeightOffset;
    private float texelWidthOffset;

    public GlBilateralFilter() {
        super(VERTEX_SHADER, FRAGMENT_SHADER);
        this.texelWidthOffset = 0.004f;
        this.texelHeightOffset = 0.004f;
        this.blurSize = 1.0f;
    }

    public float getTexelWidthOffset() {
        return this.texelWidthOffset;
    }

    public void setTexelWidthOffset(float f) {
        this.texelWidthOffset = f;
    }

    public float getTexelHeightOffset() {
        return this.texelHeightOffset;
    }

    public void setTexelHeightOffset(float f) {
        this.texelHeightOffset = f;
    }

    public float getBlurSize() {
        return this.blurSize;
    }

    public void setBlurSize(float f) {
        this.blurSize = f;
    }

    @Override // com.daasuu.gpuv.egl.filter.GlFilter
    public void onDraw() {
        GLES20.glUniform1f(getHandle("texelWidthOffset"), this.texelWidthOffset);
        GLES20.glUniform1f(getHandle("texelHeightOffset"), this.texelHeightOffset);
        GLES20.glUniform1f(getHandle("blurSize"), this.blurSize);
    }
}
