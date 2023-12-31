package jp.co.cyberagent.android.gpuimage.filter;

import android.graphics.PointF;
import android.opengl.GLES20;

/* loaded from: classes2.dex */
public class GPUImageBulgeDistortionFilter extends GPUImageFilter {
    public static final String BULGE_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\n\nuniform highp float aspectRatio;\nuniform highp vec2 center;\nuniform highp float radius;\nuniform highp float scale;\n\nvoid main()\n{\nhighp vec2 textureCoordinateToUse = vec2(textureCoordinate.x, (textureCoordinate.y * aspectRatio + 0.5 - 0.5 * aspectRatio));\nhighp float dist = distance(center, textureCoordinateToUse);\ntextureCoordinateToUse = textureCoordinate;\n\nif (dist < radius)\n{\ntextureCoordinateToUse -= center;\nhighp float percent = 1.0 - ((radius - dist) / radius) * scale;\npercent = percent * percent;\n\ntextureCoordinateToUse = textureCoordinateToUse * percent;\ntextureCoordinateToUse += center;\n}\n\ngl_FragColor = texture2D(inputImageTexture, textureCoordinateToUse );    \n}\n";
    private float aspectRatio;
    private int aspectRatioLocation;
    private PointF center;
    private int centerLocation;
    private float radius;
    private int radiusLocation;
    private float scale;
    private int scaleLocation;

    public GPUImageBulgeDistortionFilter() {
        this(0.25f, 0.5f, new PointF(0.5f, 0.5f));
    }

    public GPUImageBulgeDistortionFilter(float f, float f2, PointF pointF) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, BULGE_FRAGMENT_SHADER);
        this.radius = f;
        this.scale = f2;
        this.center = pointF;
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onInit() {
        super.onInit();
        this.scaleLocation = GLES20.glGetUniformLocation(getProgram(), "scale");
        this.radiusLocation = GLES20.glGetUniformLocation(getProgram(), "radius");
        this.centerLocation = GLES20.glGetUniformLocation(getProgram(), "center");
        this.aspectRatioLocation = GLES20.glGetUniformLocation(getProgram(), "aspectRatio");
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onInitialized() {
        super.onInitialized();
        setAspectRatio(this.aspectRatio);
        setRadius(this.radius);
        setScale(this.scale);
        setCenter(this.center);
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

    public void setRadius(float f) {
        this.radius = f;
        setFloat(this.radiusLocation, f);
    }

    public void setScale(float f) {
        this.scale = f;
        setFloat(this.scaleLocation, f);
    }

    public void setCenter(PointF pointF) {
        this.center = pointF;
        setPoint(this.centerLocation, pointF);
    }
}
