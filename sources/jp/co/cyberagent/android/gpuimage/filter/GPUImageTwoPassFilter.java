package jp.co.cyberagent.android.gpuimage.filter;

/* loaded from: classes2.dex */
public class GPUImageTwoPassFilter extends GPUImageFilterGroup {
    public GPUImageTwoPassFilter(String str, String str2, String str3, String str4) {
        super(null);
        addFilter(new GPUImageFilter(str, str2));
        addFilter(new GPUImageFilter(str3, str4));
    }
}
