package com.facebook.drawee.backends.pipeline.info;

import com.facebook.common.time.MonotonicClock;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.backends.pipeline.info.internal.ImagePerfControllerListener;
import com.facebook.drawee.backends.pipeline.info.internal.ImagePerfImageOriginListener;
import com.facebook.drawee.backends.pipeline.info.internal.ImagePerfRequestListener;
import com.facebook.imagepipeline.listener.BaseRequestListener;
import com.facebook.imagepipeline.listener.ForwardingRequestListener;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ImagePerfMonitor extends BaseRequestListener {
    private boolean mEnabled;
    @Nullable
    private ForwardingRequestListener mForwardingRequestListener;
    @Nullable
    private ImageOriginListener mImageOriginListener;
    @Nullable
    private ImageOriginRequestListener mImageOriginRequestListener;
    @Nullable
    private ImagePerfControllerListener mImagePerfControllerListener;
    @Nullable
    private List<ImagePerfDataListener> mImagePerfDataListeners;
    @Nullable
    private ImagePerfRequestListener mImagePerfRequestListener;
    private final ImagePerfState mImagePerfState = new ImagePerfState();
    private final MonotonicClock mMonotonicClock;
    private final PipelineDraweeController mPipelineDraweeController;

    public ImagePerfMonitor(MonotonicClock monotonicClock, PipelineDraweeController pipelineDraweeController) {
        this.mMonotonicClock = monotonicClock;
        this.mPipelineDraweeController = pipelineDraweeController;
    }

    public void setEnabled(boolean z) {
        this.mEnabled = z;
        if (z) {
            setupListeners();
            if (this.mImageOriginListener != null) {
                this.mPipelineDraweeController.addImageOriginListener(this.mImageOriginListener);
            }
            if (this.mImagePerfControllerListener != null) {
                this.mPipelineDraweeController.addControllerListener(this.mImagePerfControllerListener);
            }
            if (this.mForwardingRequestListener != null) {
                this.mPipelineDraweeController.addRequestListener(this.mForwardingRequestListener);
                return;
            }
            return;
        }
        if (this.mImageOriginListener != null) {
            this.mPipelineDraweeController.removeImageOriginListener(this.mImageOriginListener);
        }
        if (this.mImagePerfControllerListener != null) {
            this.mPipelineDraweeController.removeControllerListener(this.mImagePerfControllerListener);
        }
        if (this.mForwardingRequestListener != null) {
            this.mPipelineDraweeController.removeRequestListener(this.mForwardingRequestListener);
        }
    }

    public void addImagePerfDataListener(@Nullable ImagePerfDataListener imagePerfDataListener) {
        if (imagePerfDataListener == null) {
            return;
        }
        if (this.mImagePerfDataListeners == null) {
            this.mImagePerfDataListeners = new LinkedList();
        }
        this.mImagePerfDataListeners.add(imagePerfDataListener);
    }

    public void removeImagePerfDataListener(ImagePerfDataListener imagePerfDataListener) {
        if (this.mImagePerfDataListeners == null) {
            return;
        }
        this.mImagePerfDataListeners.remove(imagePerfDataListener);
    }

    public void clearImagePerfDataListeners() {
        if (this.mImagePerfDataListeners != null) {
            this.mImagePerfDataListeners.clear();
        }
    }

    public void notifyListeners(ImagePerfState imagePerfState, int i) {
        imagePerfState.setImageLoadStatus(i);
        if (!this.mEnabled || this.mImagePerfDataListeners == null || this.mImagePerfDataListeners.isEmpty()) {
            return;
        }
        ImagePerfData snapshot = imagePerfState.snapshot();
        for (ImagePerfDataListener imagePerfDataListener : this.mImagePerfDataListeners) {
            imagePerfDataListener.onImagePerfDataUpdated(snapshot, i);
        }
    }

    private void setupListeners() {
        if (this.mImagePerfControllerListener == null) {
            this.mImagePerfControllerListener = new ImagePerfControllerListener(this.mMonotonicClock, this.mImagePerfState, this);
        }
        if (this.mImagePerfRequestListener == null) {
            this.mImagePerfRequestListener = new ImagePerfRequestListener(this.mMonotonicClock, this.mImagePerfState);
        }
        if (this.mImageOriginListener == null) {
            this.mImageOriginListener = new ImagePerfImageOriginListener(this.mImagePerfState, this);
        }
        if (this.mImageOriginRequestListener == null) {
            this.mImageOriginRequestListener = new ImageOriginRequestListener(this.mPipelineDraweeController.getId(), this.mImageOriginListener);
        } else {
            this.mImageOriginRequestListener.init(this.mPipelineDraweeController.getId());
        }
        if (this.mForwardingRequestListener == null) {
            this.mForwardingRequestListener = new ForwardingRequestListener(this.mImagePerfRequestListener, this.mImageOriginRequestListener);
        }
    }

    public void reset() {
        clearImagePerfDataListeners();
        setEnabled(false);
        this.mImagePerfState.reset();
    }
}
