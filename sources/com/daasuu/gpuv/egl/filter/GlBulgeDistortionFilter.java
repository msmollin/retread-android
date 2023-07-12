package com.daasuu.gpuv.egl.filter;

import android.opengl.GLES20;

/* loaded from: classes.dex */
public class GlBulgeDistortionFilter extends GlFilter {
    private static final String FRAGMENT_SHADER = "precision mediump float;varying highp vec2 vTextureCoord;uniform lowp sampler2D sTexture;uniform highp vec2 center;uniform highp float radius;uniform highp float scale;void main() {highp vec2 textureCoordinateToUse = vTextureCoord;highp float dist = distance(center, vTextureCoord);textureCoordinateToUse -= center;if (dist < radius) {highp float percent = 1.0 - ((radius - dist) / radius) * scale;percent = percent * percent;textureCoordinateToUse = textureCoordinateToUse * percent;}textureCoordinateToUse += center;gl_FragColor = texture2D(sTexture, textureCoordinateToUse);}";
    private float centerX;
    private float centerY;
    private float radius;
    private float scale;

    public GlBulgeDistortionFilter() {
        super("attribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\ngl_Position = aPosition;\nvTextureCoord = aTextureCoord.xy;\n}\n", FRAGMENT_SHADER);
        this.centerX = 0.5f;
        this.centerY = 0.5f;
        this.radius = 0.25f;
        this.scale = 0.5f;
    }

    public float getCenterX() {
        return this.centerX;
    }

    public void setCenterX(float f) {
        this.centerX = f;
    }

    public float getCenterY() {
        return this.centerY;
    }

    public void setCenterY(float f) {
        this.centerY = f;
    }

    public float getRadius() {
        return this.radius;
    }

    public void setRadius(float f) {
        this.radius = f;
    }

    public float getScale() {
        return this.scale;
    }

    public void setScale(float f) {
        this.scale = f;
    }

    @Override // com.daasuu.gpuv.egl.filter.GlFilter
    public void onDraw() {
        GLES20.glUniform2f(getHandle("center"), this.centerX, this.centerY);
        GLES20.glUniform1f(getHandle("radius"), this.radius);
        GLES20.glUniform1f(getHandle("scale"), this.scale);
    }
}
