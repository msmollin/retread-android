package com.daasuu.gpuv.egl.filter;

import android.graphics.PointF;
import android.opengl.GLES20;

/* loaded from: classes.dex */
public class GlSwirlFilter extends GlFilter {
    private static final String SWIRL_FRAGMENT_SHADER = "precision mediump float; varying vec2 vTextureCoord;\n\n uniform lowp sampler2D sTexture;\n\nuniform highp vec2 center;\nuniform highp float radius;\nuniform highp float angle;\n\nvoid main()\n{\nhighp vec2 textureCoordinateToUse = vTextureCoord;\nhighp float dist = distance(center, vTextureCoord);\nif (dist < radius)\n{\ntextureCoordinateToUse -= center;\nhighp float percent = (radius - dist) / radius;\nhighp float theta = percent * percent * angle * 8.0;\nhighp float s = sin(theta);\nhighp float c = cos(theta);\ntextureCoordinateToUse = vec2(dot(textureCoordinateToUse, vec2(c, -s)), dot(textureCoordinateToUse, vec2(s, c)));\ntextureCoordinateToUse += center;\n}\n\ngl_FragColor = texture2D(sTexture, textureCoordinateToUse );\n\n}\n";
    private float angle;
    private PointF center;
    private float radius;

    public GlSwirlFilter() {
        super("attribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\ngl_Position = aPosition;\nvTextureCoord = aTextureCoord.xy;\n}\n", SWIRL_FRAGMENT_SHADER);
        this.angle = 1.0f;
        this.radius = 0.5f;
        this.center = new PointF(0.5f, 0.5f);
    }

    public void setAngle(float f) {
        this.angle = f;
    }

    public void setRadius(float f) {
        this.radius = f;
    }

    public void setCenter(PointF pointF) {
        this.center = pointF;
    }

    @Override // com.daasuu.gpuv.egl.filter.GlFilter
    public void onDraw() {
        GLES20.glUniform2f(getHandle("center"), this.center.x, this.center.y);
        GLES20.glUniform1f(getHandle("radius"), this.radius);
        GLES20.glUniform1f(getHandle("angle"), this.angle);
    }
}
