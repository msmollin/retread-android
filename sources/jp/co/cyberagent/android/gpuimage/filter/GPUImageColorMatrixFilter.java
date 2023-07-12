package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

/* loaded from: classes2.dex */
public class GPUImageColorMatrixFilter extends GPUImageFilter {
    public static final String COLOR_MATRIX_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\n\nuniform lowp mat4 colorMatrix;\nuniform lowp float intensity;\n\nvoid main()\n{\n    lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n    lowp vec4 outputColor = textureColor * colorMatrix;\n    \n    gl_FragColor = (intensity * outputColor) + ((1.0 - intensity) * textureColor);\n}";
    private float[] colorMatrix;
    private int colorMatrixLocation;
    private float intensity;
    private int intensityLocation;

    public GPUImageColorMatrixFilter() {
        this(1.0f, new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f});
    }

    public GPUImageColorMatrixFilter(float f, float[] fArr) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, COLOR_MATRIX_FRAGMENT_SHADER);
        this.intensity = f;
        this.colorMatrix = fArr;
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onInit() {
        super.onInit();
        this.colorMatrixLocation = GLES20.glGetUniformLocation(getProgram(), "colorMatrix");
        this.intensityLocation = GLES20.glGetUniformLocation(getProgram(), "intensity");
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onInitialized() {
        super.onInitialized();
        setIntensity(this.intensity);
        setColorMatrix(this.colorMatrix);
    }

    public void setIntensity(float f) {
        this.intensity = f;
        setFloat(this.intensityLocation, f);
    }

    public void setColorMatrix(float[] fArr) {
        this.colorMatrix = fArr;
        setUniformMatrix4f(this.colorMatrixLocation, fArr);
    }
}
