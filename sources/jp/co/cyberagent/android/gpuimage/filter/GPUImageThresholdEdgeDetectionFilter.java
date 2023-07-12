package jp.co.cyberagent.android.gpuimage.filter;

/* loaded from: classes2.dex */
public class GPUImageThresholdEdgeDetectionFilter extends GPUImageFilterGroup {
    public GPUImageThresholdEdgeDetectionFilter() {
        addFilter(new GPUImageGrayscaleFilter());
        addFilter(new GPUImageSobelThresholdFilter());
    }

    public void setLineSize(float f) {
        ((GPUImage3x3TextureSamplingFilter) getFilters().get(1)).setLineSize(f);
    }

    public void setThreshold(float f) {
        ((GPUImageSobelThresholdFilter) getFilters().get(1)).setThreshold(f);
    }
}
