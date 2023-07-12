package jp.co.cyberagent.android.gpuimage.filter;

/* loaded from: classes2.dex */
public class GPUImageSmoothToonFilter extends GPUImageFilterGroup {
    private GPUImageGaussianBlurFilter blurFilter = new GPUImageGaussianBlurFilter();
    private GPUImageToonFilter toonFilter;

    public GPUImageSmoothToonFilter() {
        addFilter(this.blurFilter);
        this.toonFilter = new GPUImageToonFilter();
        addFilter(this.toonFilter);
        getFilters().add(this.blurFilter);
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onInitialized() {
        super.onInitialized();
        setBlurSize(0.5f);
        setThreshold(0.2f);
        setQuantizationLevels(10.0f);
    }

    public void setTexelWidth(float f) {
        this.toonFilter.setTexelWidth(f);
    }

    public void setTexelHeight(float f) {
        this.toonFilter.setTexelHeight(f);
    }

    public void setBlurSize(float f) {
        this.blurFilter.setBlurSize(f);
    }

    public void setThreshold(float f) {
        this.toonFilter.setThreshold(f);
    }

    public void setQuantizationLevels(float f) {
        this.toonFilter.setQuantizationLevels(f);
    }
}
