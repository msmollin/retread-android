package com.daasuu.gpuv.egl.filter;

import android.opengl.GLES20;

/* loaded from: classes.dex */
public class GlRGBFilter extends GlFilter {
    private static final String RGB_FRAGMENT_SHADER = "precision mediump float; varying vec2 vTextureCoord;\n  \n uniform lowp sampler2D sTexture;\n  uniform highp float red;\n  uniform highp float green;\n  uniform highp float blue;\n  \n  void main()\n  {\n      highp vec4 textureColor = texture2D(sTexture, vTextureCoord);\n      \n      gl_FragColor = vec4(textureColor.r * red, textureColor.g * green, textureColor.b * blue, 1.0);\n  }\n";
    private float blue;
    private float green;
    private float red;

    public GlRGBFilter() {
        super("attribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\ngl_Position = aPosition;\nvTextureCoord = aTextureCoord.xy;\n}\n", RGB_FRAGMENT_SHADER);
        this.red = 1.0f;
        this.green = 1.0f;
        this.blue = 1.0f;
    }

    public void setRed(float f) {
        this.red = f;
    }

    public void setGreen(float f) {
        this.green = f;
    }

    public void setBlue(float f) {
        this.blue = f;
    }

    @Override // com.daasuu.gpuv.egl.filter.GlFilter
    public void onDraw() {
        GLES20.glUniform1f(getHandle("red"), this.red);
        GLES20.glUniform1f(getHandle("green"), this.green);
        GLES20.glUniform1f(getHandle("blue"), this.blue);
    }
}
