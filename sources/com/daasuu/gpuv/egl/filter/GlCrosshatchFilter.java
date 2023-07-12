package com.daasuu.gpuv.egl.filter;

import android.opengl.GLES20;

/* loaded from: classes.dex */
public class GlCrosshatchFilter extends GlFilter {
    private static final String CROSSHATCH_FRAGMENT_SHADER = "precision mediump float; varying vec2 vTextureCoord;\n uniform lowp sampler2D sTexture;\nuniform highp float crossHatchSpacing;\nuniform highp float lineWidth;\nconst highp vec3 W = vec3(0.2125, 0.7154, 0.0721);\nvoid main()\n{\nhighp float luminance = dot(texture2D(sTexture, vTextureCoord).rgb, W);\nlowp vec4 colorToDisplay = vec4(1.0, 1.0, 1.0, 1.0);\nif (luminance < 1.00)\n{\nif (mod(vTextureCoord.x + vTextureCoord.y, crossHatchSpacing) <= lineWidth)\n{\ncolorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);\n}\n}\nif (luminance < 0.75)\n{\nif (mod(vTextureCoord.x - vTextureCoord.y, crossHatchSpacing) <= lineWidth)\n{\ncolorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);\n}\n}\nif (luminance < 0.50)\n{\nif (mod(vTextureCoord.x + vTextureCoord.y - (crossHatchSpacing / 2.0), crossHatchSpacing) <= lineWidth)\n{\ncolorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);\n}\n}\nif (luminance < 0.3)\n{\nif (mod(vTextureCoord.x - vTextureCoord.y - (crossHatchSpacing / 2.0), crossHatchSpacing) <= lineWidth)\n{\ncolorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);\n}\n}\ngl_FragColor = colorToDisplay;\n}\n";
    private float crossHatchSpacing;
    private float lineWidth;

    public GlCrosshatchFilter() {
        super("attribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\ngl_Position = aPosition;\nvTextureCoord = aTextureCoord.xy;\n}\n", CROSSHATCH_FRAGMENT_SHADER);
        this.crossHatchSpacing = 0.03f;
        this.lineWidth = 0.003f;
    }

    @Override // com.daasuu.gpuv.egl.filter.GlFilter
    public void onDraw() {
        GLES20.glUniform1f(getHandle("crossHatchSpacing"), this.crossHatchSpacing);
        GLES20.glUniform1f(getHandle("lineWidth"), this.lineWidth);
    }

    public void setCrossHatchSpacing(float f) {
        this.crossHatchSpacing = f;
    }

    public void setLineWidth(float f) {
        this.lineWidth = f;
    }

    @Override // com.daasuu.gpuv.egl.filter.GlFilter
    public void setFrameSize(int i, int i2) {
        super.setFrameSize(i, i2);
        float f = i != 0 ? 1.0f / i : 4.8828125E-4f;
        if (this.crossHatchSpacing < f) {
            this.crossHatchSpacing = f;
        }
    }
}
