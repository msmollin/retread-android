package com.daasuu.gpuv.egl.filter;

import android.opengl.GLES20;

/* loaded from: classes.dex */
public class GlSphereRefractionFilter extends GlFilter {
    private static final String FRAGMENT_SHADER = "precision mediump float;varying vec2 vTextureCoord;uniform lowp sampler2D sTexture;uniform highp vec2 center;uniform highp float radius;uniform highp float aspectRatio;uniform highp float refractiveIndex;void main() {highp vec2 textureCoordinateToUse = vec2(vTextureCoord.x, (vTextureCoord.y * aspectRatio + 0.5 - 0.5 * aspectRatio));highp float distanceFromCenter = distance(center, textureCoordinateToUse);lowp float checkForPresenceWithinSphere = step(distanceFromCenter, radius);distanceFromCenter = distanceFromCenter / radius;highp float normalizedDepth = radius * sqrt(1.0 - distanceFromCenter * distanceFromCenter);highp vec3 sphereNormal = normalize(vec3(textureCoordinateToUse - center, normalizedDepth));highp vec3 refractedVector = refract(vec3(0.0, 0.0, -1.0), sphereNormal, refractiveIndex);gl_FragColor = texture2D(sTexture, (refractedVector.xy + 1.0) * 0.5) * checkForPresenceWithinSphere;}";
    private float aspectRatio;
    private float centerX;
    private float centerY;
    private float radius;
    private float refractiveIndex;

    public GlSphereRefractionFilter() {
        super("attribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\ngl_Position = aPosition;\nvTextureCoord = aTextureCoord.xy;\n}\n", FRAGMENT_SHADER);
        this.centerX = 0.5f;
        this.centerY = 0.5f;
        this.radius = 0.5f;
        this.aspectRatio = 1.0f;
        this.refractiveIndex = 0.71f;
    }

    public void setCenterX(float f) {
        this.centerX = f;
    }

    public void setCenterY(float f) {
        this.centerY = f;
    }

    public void setRadius(float f) {
        this.radius = f;
    }

    public void setAspectRatio(float f) {
        this.aspectRatio = f;
    }

    public void setRefractiveIndex(float f) {
        this.refractiveIndex = f;
    }

    @Override // com.daasuu.gpuv.egl.filter.GlFilter
    public void onDraw() {
        GLES20.glUniform2f(getHandle("center"), this.centerX, this.centerY);
        GLES20.glUniform1f(getHandle("radius"), this.radius);
        GLES20.glUniform1f(getHandle("aspectRatio"), this.aspectRatio);
        GLES20.glUniform1f(getHandle("refractiveIndex"), this.refractiveIndex);
    }
}
