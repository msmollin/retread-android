package jp.co.cyberagent.android.gpuimage.filter;

import android.annotation.SuppressLint;
import android.opengl.GLES20;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import jp.co.cyberagent.android.gpuimage.GPUImageRenderer;
import jp.co.cyberagent.android.gpuimage.util.Rotation;
import jp.co.cyberagent.android.gpuimage.util.TextureRotationUtil;

/* loaded from: classes2.dex */
public class GPUImageFilterGroup extends GPUImageFilter {
    private List<GPUImageFilter> filters;
    private int[] frameBufferTextures;
    private int[] frameBuffers;
    private final FloatBuffer glCubeBuffer;
    private final FloatBuffer glTextureBuffer;
    private final FloatBuffer glTextureFlipBuffer;
    private List<GPUImageFilter> mergedFilters;

    public GPUImageFilterGroup() {
        this(null);
    }

    public GPUImageFilterGroup(List<GPUImageFilter> list) {
        this.filters = list;
        if (this.filters == null) {
            this.filters = new ArrayList();
        } else {
            updateMergedFilters();
        }
        this.glCubeBuffer = ByteBuffer.allocateDirect(GPUImageRenderer.CUBE.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.glCubeBuffer.put(GPUImageRenderer.CUBE).position(0);
        this.glTextureBuffer = ByteBuffer.allocateDirect(TextureRotationUtil.TEXTURE_NO_ROTATION.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.glTextureBuffer.put(TextureRotationUtil.TEXTURE_NO_ROTATION).position(0);
        float[] rotation = TextureRotationUtil.getRotation(Rotation.NORMAL, false, true);
        this.glTextureFlipBuffer = ByteBuffer.allocateDirect(rotation.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.glTextureFlipBuffer.put(rotation).position(0);
    }

    public void addFilter(GPUImageFilter gPUImageFilter) {
        if (gPUImageFilter == null) {
            return;
        }
        this.filters.add(gPUImageFilter);
        updateMergedFilters();
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onInit() {
        super.onInit();
        for (GPUImageFilter gPUImageFilter : this.filters) {
            gPUImageFilter.ifNeedInit();
        }
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onDestroy() {
        destroyFramebuffers();
        for (GPUImageFilter gPUImageFilter : this.filters) {
            gPUImageFilter.destroy();
        }
        super.onDestroy();
    }

    private void destroyFramebuffers() {
        if (this.frameBufferTextures != null) {
            GLES20.glDeleteTextures(this.frameBufferTextures.length, this.frameBufferTextures, 0);
            this.frameBufferTextures = null;
        }
        if (this.frameBuffers != null) {
            GLES20.glDeleteFramebuffers(this.frameBuffers.length, this.frameBuffers, 0);
            this.frameBuffers = null;
        }
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onOutputSizeChanged(int i, int i2) {
        super.onOutputSizeChanged(i, i2);
        if (this.frameBuffers != null) {
            destroyFramebuffers();
        }
        int size = this.filters.size();
        for (int i3 = 0; i3 < size; i3++) {
            this.filters.get(i3).onOutputSizeChanged(i, i2);
        }
        if (this.mergedFilters == null || this.mergedFilters.size() <= 0) {
            return;
        }
        int i4 = 1;
        int size2 = this.mergedFilters.size() - 1;
        this.frameBuffers = new int[size2];
        this.frameBufferTextures = new int[size2];
        int i5 = 0;
        while (i5 < size2) {
            GLES20.glGenFramebuffers(i4, this.frameBuffers, i5);
            GLES20.glGenTextures(i4, this.frameBufferTextures, i5);
            GLES20.glBindTexture(3553, this.frameBufferTextures[i5]);
            GLES20.glTexImage2D(3553, 0, 6408, i, i2, 0, 6408, 5121, null);
            GLES20.glTexParameterf(3553, 10240, 9729.0f);
            GLES20.glTexParameterf(3553, 10241, 9729.0f);
            GLES20.glTexParameterf(3553, 10242, 33071.0f);
            GLES20.glTexParameterf(3553, 10243, 33071.0f);
            GLES20.glBindFramebuffer(36160, this.frameBuffers[i5]);
            GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.frameBufferTextures[i5], 0);
            GLES20.glBindTexture(3553, 0);
            GLES20.glBindFramebuffer(36160, 0);
            i5++;
            i4 = 1;
        }
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    @SuppressLint({"WrongCall"})
    public void onDraw(int i, FloatBuffer floatBuffer, FloatBuffer floatBuffer2) {
        runPendingOnDrawTasks();
        if (!isInitialized() || this.frameBuffers == null || this.frameBufferTextures == null || this.mergedFilters == null) {
            return;
        }
        int size = this.mergedFilters.size();
        int i2 = i;
        int i3 = 0;
        while (i3 < size) {
            GPUImageFilter gPUImageFilter = this.mergedFilters.get(i3);
            int i4 = size - 1;
            boolean z = i3 < i4;
            if (z) {
                GLES20.glBindFramebuffer(36160, this.frameBuffers[i3]);
                GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            }
            if (i3 == 0) {
                gPUImageFilter.onDraw(i2, floatBuffer, floatBuffer2);
            } else if (i3 == i4) {
                gPUImageFilter.onDraw(i2, this.glCubeBuffer, size % 2 == 0 ? this.glTextureFlipBuffer : this.glTextureBuffer);
            } else {
                gPUImageFilter.onDraw(i2, this.glCubeBuffer, this.glTextureBuffer);
            }
            if (z) {
                GLES20.glBindFramebuffer(36160, 0);
                i2 = this.frameBufferTextures[i3];
            }
            i3++;
        }
    }

    public List<GPUImageFilter> getFilters() {
        return this.filters;
    }

    public List<GPUImageFilter> getMergedFilters() {
        return this.mergedFilters;
    }

    public void updateMergedFilters() {
        if (this.filters == null) {
            return;
        }
        if (this.mergedFilters == null) {
            this.mergedFilters = new ArrayList();
        } else {
            this.mergedFilters.clear();
        }
        for (GPUImageFilter gPUImageFilter : this.filters) {
            if (gPUImageFilter instanceof GPUImageFilterGroup) {
                GPUImageFilterGroup gPUImageFilterGroup = (GPUImageFilterGroup) gPUImageFilter;
                gPUImageFilterGroup.updateMergedFilters();
                List<GPUImageFilter> mergedFilters = gPUImageFilterGroup.getMergedFilters();
                if (mergedFilters != null && !mergedFilters.isEmpty()) {
                    this.mergedFilters.addAll(mergedFilters);
                }
            } else {
                this.mergedFilters.add(gPUImageFilter);
            }
        }
    }
}
