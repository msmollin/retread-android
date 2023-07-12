package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

/* loaded from: classes2.dex */
public class GPUImageMixBlendFilter extends GPUImageTwoInputFilter {
    private float mix;
    private int mixLocation;

    public GPUImageMixBlendFilter(String str) {
        this(str, 0.5f);
    }

    public GPUImageMixBlendFilter(String str, float f) {
        super(str);
        this.mix = f;
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageTwoInputFilter, jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onInit() {
        super.onInit();
        this.mixLocation = GLES20.glGetUniformLocation(getProgram(), "mixturePercent");
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageTwoInputFilter, jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onInitialized() {
        super.onInitialized();
        setMix(this.mix);
    }

    public void setMix(float f) {
        this.mix = f;
        setFloat(this.mixLocation, this.mix);
    }
}
