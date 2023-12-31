package jp.co.cyberagent.android.gpuimage.filter;

import android.graphics.PointF;
import android.opengl.GLES20;

/* loaded from: classes2.dex */
public class GPUImageSphereRefractionFilter extends GPUImageFilter {
    public static final String SPHERE_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\n\nuniform highp vec2 center;\nuniform highp float radius;\nuniform highp float aspectRatio;\nuniform highp float refractiveIndex;\n\nvoid main()\n{\nhighp vec2 textureCoordinateToUse = vec2(textureCoordinate.x, (textureCoordinate.y * aspectRatio + 0.5 - 0.5 * aspectRatio));\nhighp float distanceFromCenter = distance(center, textureCoordinateToUse);\nlowp float checkForPresenceWithinSphere = step(distanceFromCenter, radius);\n\ndistanceFromCenter = distanceFromCenter / radius;\n\nhighp float normalizedDepth = radius * sqrt(1.0 - distanceFromCenter * distanceFromCenter);\nhighp vec3 sphereNormal = normalize(vec3(textureCoordinateToUse - center, normalizedDepth));\n\nhighp vec3 refractedVector = refract(vec3(0.0, 0.0, -1.0), sphereNormal, refractiveIndex);\n\ngl_FragColor = texture2D(inputImageTexture, (refractedVector.xy + 1.0) * 0.5) * checkForPresenceWithinSphere;     \n}\n";
    private float aspectRatio;
    private int aspectRatioLocation;
    private PointF center;
    private int centerLocation;
    private float radius;
    private int radiusLocation;
    private float refractiveIndex;
    private int refractiveIndexLocation;

    public GPUImageSphereRefractionFilter() {
        this(new PointF(0.5f, 0.5f), 0.25f, 0.71f);
    }

    public GPUImageSphereRefractionFilter(PointF pointF, float f, float f2) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, SPHERE_FRAGMENT_SHADER);
        this.center = pointF;
        this.radius = f;
        this.refractiveIndex = f2;
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onInit() {
        super.onInit();
        this.centerLocation = GLES20.glGetUniformLocation(getProgram(), "center");
        this.radiusLocation = GLES20.glGetUniformLocation(getProgram(), "radius");
        this.aspectRatioLocation = GLES20.glGetUniformLocation(getProgram(), "aspectRatio");
        this.refractiveIndexLocation = GLES20.glGetUniformLocation(getProgram(), "refractiveIndex");
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onInitialized() {
        super.onInitialized();
        setAspectRatio(this.aspectRatio);
        setRadius(this.radius);
        setCenter(this.center);
        setRefractiveIndex(this.refractiveIndex);
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onOutputSizeChanged(int i, int i2) {
        this.aspectRatio = i2 / i;
        setAspectRatio(this.aspectRatio);
        super.onOutputSizeChanged(i, i2);
    }

    private void setAspectRatio(float f) {
        this.aspectRatio = f;
        setFloat(this.aspectRatioLocation, f);
    }

    public void setRefractiveIndex(float f) {
        this.refractiveIndex = f;
        setFloat(this.refractiveIndexLocation, f);
    }

    public void setCenter(PointF pointF) {
        this.center = pointF;
        setPoint(this.centerLocation, pointF);
    }

    public void setRadius(float f) {
        this.radius = f;
        setFloat(this.radiusLocation, f);
    }
}
