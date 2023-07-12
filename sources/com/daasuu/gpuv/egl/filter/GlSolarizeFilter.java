package com.daasuu.gpuv.egl.filter;

import android.opengl.GLES20;

/* loaded from: classes.dex */
public class GlSolarizeFilter extends GlFilter {
    private static final String SOLATIZE_FRAGMENT_SHADER = "precision mediump float; varying vec2 vTextureCoord;\n\n uniform lowp sampler2D sTexture;\n uniform highp float threshold;\n\n const highp vec3 W = vec3(0.2125, 0.7154, 0.0721);\n\nvoid main()\n{\n    highp vec4 textureColor = texture2D(sTexture, vTextureCoord);\n    highp float luminance = dot(textureColor.rgb, W);\n    highp float thresholdResult = step(luminance, threshold);\n    highp vec3 finalColor = abs(thresholdResult - textureColor.rgb);\n    \n    gl_FragColor = vec4(finalColor, textureColor.w);\n}";
    private float threshold;

    public GlSolarizeFilter() {
        super("attribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\ngl_Position = aPosition;\nvTextureCoord = aTextureCoord.xy;\n}\n", SOLATIZE_FRAGMENT_SHADER);
        this.threshold = 0.5f;
    }

    public void setThreshold(float f) {
        this.threshold = f;
    }

    @Override // com.daasuu.gpuv.egl.filter.GlFilter
    public void onDraw() {
        GLES20.glUniform1f(getHandle("threshold"), this.threshold);
    }
}
