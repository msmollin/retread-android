package jp.co.cyberagent.android.gpuimage.filter;

/* loaded from: classes2.dex */
public class GPUImageSepiaToneFilter extends GPUImageColorMatrixFilter {
    public GPUImageSepiaToneFilter() {
        this(1.0f);
    }

    public GPUImageSepiaToneFilter(float f) {
        super(f, new float[]{0.3588f, 0.7044f, 0.1368f, 0.0f, 0.299f, 0.587f, 0.114f, 0.0f, 0.2392f, 0.4696f, 0.0912f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f});
    }
}
