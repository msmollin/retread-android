package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

/* loaded from: classes2.dex */
public class GPUImageTwoPassTextureSamplingFilter extends GPUImageTwoPassFilter {
    public float getHorizontalTexelOffsetRatio() {
        return 1.0f;
    }

    public float getVerticalTexelOffsetRatio() {
        return 1.0f;
    }

    public GPUImageTwoPassTextureSamplingFilter(String str, String str2, String str3, String str4) {
        super(str, str2, str3, str4);
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilterGroup, jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onInit() {
        super.onInit();
        initTexelOffsets();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void initTexelOffsets() {
        float horizontalTexelOffsetRatio = getHorizontalTexelOffsetRatio();
        GPUImageFilter gPUImageFilter = getFilters().get(0);
        int glGetUniformLocation = GLES20.glGetUniformLocation(gPUImageFilter.getProgram(), "texelWidthOffset");
        int glGetUniformLocation2 = GLES20.glGetUniformLocation(gPUImageFilter.getProgram(), "texelHeightOffset");
        gPUImageFilter.setFloat(glGetUniformLocation, horizontalTexelOffsetRatio / getOutputWidth());
        gPUImageFilter.setFloat(glGetUniformLocation2, 0.0f);
        float verticalTexelOffsetRatio = getVerticalTexelOffsetRatio();
        GPUImageFilter gPUImageFilter2 = getFilters().get(1);
        int glGetUniformLocation3 = GLES20.glGetUniformLocation(gPUImageFilter2.getProgram(), "texelWidthOffset");
        int glGetUniformLocation4 = GLES20.glGetUniformLocation(gPUImageFilter2.getProgram(), "texelHeightOffset");
        gPUImageFilter2.setFloat(glGetUniformLocation3, 0.0f);
        gPUImageFilter2.setFloat(glGetUniformLocation4, verticalTexelOffsetRatio / getOutputHeight());
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilterGroup, jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onOutputSizeChanged(int i, int i2) {
        super.onOutputSizeChanged(i, i2);
        initTexelOffsets();
    }
}
