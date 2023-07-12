package com.daasuu.gpuv.egl.filter;

import android.opengl.GLES20;

/* loaded from: classes.dex */
public class GlHighlightShadowFilter extends GlFilter {
    private static final String HIGHLIGHT_SHADOW_FRAGMENT_SHADER = "precision mediump float; uniform lowp sampler2D sTexture;\n varying vec2 vTextureCoord;\n  \n uniform lowp float shadows;\n uniform lowp float highlights;\n \n const mediump vec3 luminanceWeighting = vec3(0.3, 0.3, 0.3);\n \n void main()\n {\n \tlowp vec4 source = texture2D(sTexture, vTextureCoord);\n \tmediump float luminance = dot(source.rgb, luminanceWeighting);\n \n \tmediump float shadow = clamp((pow(luminance, 1.0/(shadows+1.0)) + (-0.76)*pow(luminance, 2.0/(shadows+1.0))) - luminance, 0.0, 1.0);\n \tmediump float highlight = clamp((1.0 - (pow(1.0-luminance, 1.0/(2.0-highlights)) + (-0.8)*pow(1.0-luminance, 2.0/(2.0-highlights)))) - luminance, -1.0, 0.0);\n \tlowp vec3 result = vec3(0.0, 0.0, 0.0) + ((luminance + shadow + highlight) - 0.0) * ((source.rgb - vec3(0.0, 0.0, 0.0))/(luminance - 0.0));\n \n \tgl_FragColor = vec4(result.rgb, source.a);\n }";
    private float highlights;
    private float shadows;

    public GlHighlightShadowFilter() {
        super("attribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\ngl_Position = aPosition;\nvTextureCoord = aTextureCoord.xy;\n}\n", HIGHLIGHT_SHADOW_FRAGMENT_SHADER);
        this.shadows = 1.0f;
        this.highlights = 0.0f;
    }

    public void setShadows(float f) {
        this.shadows = f;
    }

    public void setHighlights(float f) {
        this.highlights = f;
    }

    @Override // com.daasuu.gpuv.egl.filter.GlFilter
    public void onDraw() {
        GLES20.glUniform1f(getHandle("shadows"), this.shadows);
        GLES20.glUniform1f(getHandle("highlights"), this.highlights);
    }
}
