package com.daasuu.gpuv.egl.filter;

import android.opengl.GLES20;

/* loaded from: classes.dex */
public class GlMonochromeFilter extends GlFilter {
    private static final String FRAGMENT_SHADER = "precision lowp float;varying highp vec2 vTextureCoord;uniform lowp sampler2D sTexture;uniform float intensity;const mediump vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);void main() {lowp vec4 textureColor = texture2D(sTexture, vTextureCoord);float luminance = dot(textureColor.rgb, luminanceWeighting);lowp vec4 desat = vec4(vec3(luminance), 1.0);lowp vec4 outputColor = vec4((desat.r < 0.5 ? (2.0 * desat.r * 0.6) : (1.0 - 2.0 * (1.0 - desat.r) * (1.0 - 0.6))),(desat.g < 0.5 ? (2.0 * desat.g * 0.45) : (1.0 - 2.0 * (1.0 - desat.g) * (1.0 - 0.45))),(desat.b < 0.5 ? (2.0 * desat.b * 0.3) : (1.0 - 2.0 * (1.0 - desat.b) * (1.0 - 0.3))),1.0);gl_FragColor = vec4(mix(textureColor.rgb, outputColor.rgb, intensity), textureColor.a);}";
    private float intensity;

    public GlMonochromeFilter() {
        super("attribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\ngl_Position = aPosition;\nvTextureCoord = aTextureCoord.xy;\n}\n", FRAGMENT_SHADER);
        this.intensity = 1.0f;
    }

    public float getIntensity() {
        return this.intensity;
    }

    public void setIntensity(float f) {
        this.intensity = f;
    }

    @Override // com.daasuu.gpuv.egl.filter.GlFilter
    public void onDraw() {
        GLES20.glUniform1f(getHandle("intensity"), this.intensity);
    }
}
