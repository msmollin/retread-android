package com.daasuu.gpuv.egl.filter;

import android.opengl.GLES20;

/* loaded from: classes.dex */
public class GlOpacityFilter extends GlFilter {
    private static final String OPACITY_FRAGMENT_SHADER = "precision mediump float; varying highp vec2 vTextureCoord;\n  \n uniform lowp sampler2D sTexture;\n uniform lowp float opacity;\n  \n  void main()\n  {\n      lowp vec4 textureColor = texture2D(sTexture, vTextureCoord);\n      \n      gl_FragColor = vec4(textureColor.rgb, textureColor.a * opacity);\n  }\n";
    private float opacity;

    public GlOpacityFilter() {
        super("attribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\ngl_Position = aPosition;\nvTextureCoord = aTextureCoord.xy;\n}\n", OPACITY_FRAGMENT_SHADER);
        this.opacity = 1.0f;
    }

    public void setOpacity(float f) {
        this.opacity = f;
    }

    @Override // com.daasuu.gpuv.egl.filter.GlFilter
    public void onDraw() {
        GLES20.glUniform1f(getHandle("opacity"), this.opacity);
    }
}
