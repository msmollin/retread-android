package com.daasuu.gpuv.egl.filter;

import android.opengl.GLES20;

/* loaded from: classes.dex */
public class GlVibranceFilter extends GlFilter {
    private static final String VIBRANCE_FRAGMENT_SHADER = "precision mediump float; varying vec2 vTextureCoord;\n\n uniform lowp sampler2D sTexture;\n uniform lowp float vibrance;\n\nvoid main() {\n    lowp vec4 color = texture2D(sTexture, vTextureCoord);\n    lowp float average = (color.r + color.g + color.b) / 3.0;\n    lowp float mx = max(color.r, max(color.g, color.b));\n    lowp float amt = (mx - average) * (-vibrance * 3.0);\n    color.rgb = mix(color.rgb, vec3(mx), amt);\n    gl_FragColor = color;\n}";
    private float vibrance;

    public GlVibranceFilter() {
        super("attribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\ngl_Position = aPosition;\nvTextureCoord = aTextureCoord.xy;\n}\n", VIBRANCE_FRAGMENT_SHADER);
        this.vibrance = 0.0f;
    }

    public void setVibrance(float f) {
        this.vibrance = f;
    }

    @Override // com.daasuu.gpuv.egl.filter.GlFilter
    public void onDraw() {
        GLES20.glUniform1f(getHandle("vibrance"), this.vibrance);
    }
}
