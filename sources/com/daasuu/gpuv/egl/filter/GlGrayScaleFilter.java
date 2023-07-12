package com.daasuu.gpuv.egl.filter;

/* loaded from: classes.dex */
public class GlGrayScaleFilter extends GlFilter {
    private static final String FRAGMENT_SHADER = "precision mediump float;varying vec2 vTextureCoord;uniform lowp sampler2D sTexture;const highp vec3 weight = vec3(0.2125, 0.7154, 0.0721);void main() {float luminance = dot(texture2D(sTexture, vTextureCoord).rgb, weight);gl_FragColor = vec4(vec3(luminance), 1.0);}";

    public GlGrayScaleFilter() {
        super("attribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\ngl_Position = aPosition;\nvTextureCoord = aTextureCoord.xy;\n}\n", FRAGMENT_SHADER);
    }
}
