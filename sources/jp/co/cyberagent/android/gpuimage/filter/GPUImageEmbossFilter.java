package jp.co.cyberagent.android.gpuimage.filter;

/* loaded from: classes2.dex */
public class GPUImageEmbossFilter extends GPUImage3x3ConvolutionFilter {
    private float intensity;

    public GPUImageEmbossFilter() {
        this(1.0f);
    }

    public GPUImageEmbossFilter(float f) {
        this.intensity = f;
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImage3x3ConvolutionFilter, jp.co.cyberagent.android.gpuimage.filter.GPUImage3x3TextureSamplingFilter, jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onInit() {
        super.onInit();
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImage3x3ConvolutionFilter, jp.co.cyberagent.android.gpuimage.filter.GPUImage3x3TextureSamplingFilter, jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onInitialized() {
        super.onInitialized();
        setIntensity(this.intensity);
    }

    public void setIntensity(float f) {
        this.intensity = f;
        float f2 = -f;
        setConvolutionKernel(new float[]{(-2.0f) * f, f2, 0.0f, f2, 1.0f, f, 0.0f, f, f * 2.0f});
    }

    public float getIntensity() {
        return this.intensity;
    }
}
