package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

/* loaded from: classes2.dex */
public class GPUImagePixelationFilter extends GPUImageFilter {
    public static final String PIXELATION_FRAGMENT_SHADER = "precision highp float;\nvarying vec2 textureCoordinate;\nuniform float imageWidthFactor;\nuniform float imageHeightFactor;\nuniform sampler2D inputImageTexture;\nuniform float pixel;\nvoid main()\n{\n  vec2 uv  = textureCoordinate.xy;\n  float dx = pixel * imageWidthFactor;\n  float dy = pixel * imageHeightFactor;\n  vec2 coord = vec2(dx * floor(uv.x / dx), dy * floor(uv.y / dy));\n  vec3 tc = texture2D(inputImageTexture, coord).xyz;\n  gl_FragColor = vec4(tc, 1.0);\n}";
    private int imageHeightFactorLocation;
    private int imageWidthFactorLocation;
    private float pixel;
    private int pixelLocation;

    public GPUImagePixelationFilter() {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, PIXELATION_FRAGMENT_SHADER);
        this.pixel = 1.0f;
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onInit() {
        super.onInit();
        this.imageWidthFactorLocation = GLES20.glGetUniformLocation(getProgram(), "imageWidthFactor");
        this.imageHeightFactorLocation = GLES20.glGetUniformLocation(getProgram(), "imageHeightFactor");
        this.pixelLocation = GLES20.glGetUniformLocation(getProgram(), "pixel");
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onInitialized() {
        super.onInitialized();
        setPixel(this.pixel);
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onOutputSizeChanged(int i, int i2) {
        super.onOutputSizeChanged(i, i2);
        setFloat(this.imageWidthFactorLocation, 1.0f / i);
        setFloat(this.imageHeightFactorLocation, 1.0f / i2);
    }

    public void setPixel(float f) {
        this.pixel = f;
        setFloat(this.pixelLocation, this.pixel);
    }
}
