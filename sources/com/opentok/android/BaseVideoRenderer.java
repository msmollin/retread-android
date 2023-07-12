package com.opentok.android;

import android.view.View;
import com.opentok.otc.SWIGTYPE_p_otc_video_frame;
import com.opentok.otc.c;
import com.opentok.otc.d;
import com.opentok.otc.e;
import com.opentok.otc.k;
import com.opentok.otc.opentokJNI;
import com.opentok.otc.otc_video_frame_format;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public abstract class BaseVideoRenderer {
    public static final String STYLE_VIDEO_FILL = "STYLE_VIDEO_FILL";
    public static final String STYLE_VIDEO_FIT = "STYLE_VIDEO_FIT";
    public static final String STYLE_VIDEO_SCALE = "STYLE_VIDEO_SCALE";

    /* loaded from: classes.dex */
    public final class Frame {
        protected ByteBuffer buffer;
        protected otc_video_frame_format format;
        protected int height;
        protected long internalBuffer;
        protected byte[] metadata;
        protected boolean mirrored;
        swig_otc_video_frame otcFrame;
        protected int uvStride;
        protected int width;
        protected int yStride;

        protected Frame() {
        }

        private native long copy_video_frame(long j);

        /* JADX INFO: Access modifiers changed from: package-private */
        public void copyOtcFrame(long j, Boolean bool) {
            swig_otc_video_frame swig_otc_video_frameVar = new swig_otc_video_frame(copy_video_frame(j));
            this.otcFrame = swig_otc_video_frameVar;
            this.buffer = get_frame_byte_buffer(swig_otc_video_frameVar.getCPtr());
            this.format = e.a(this.otcFrame);
            this.height = e.b(this.otcFrame);
            this.width = e.c(this.otcFrame);
            this.yStride = e.a(this.otcFrame, k.c);
            this.uvStride = e.a(this.otcFrame, k.d);
            if (bool != null) {
                this.mirrored = bool.booleanValue();
            }
            c a = e.a();
            d a2 = e.a(this.otcFrame, a);
            int a3 = (int) e.a(a);
            this.metadata = new byte[a3];
            for (int i = 0; i < a3; i++) {
                this.metadata[i] = (byte) e.a(a2, i);
            }
        }

        public void destroy() {
            swig_otc_video_frame swig_otc_video_frameVar = this.otcFrame;
            if (swig_otc_video_frameVar != null && swig_otc_video_frameVar.getCPtr() != 0) {
                this.otcFrame.destroy();
            }
            this.buffer = null;
            this.otcFrame = null;
        }

        protected void finalize() {
            super.finalize();
            destroy();
        }

        public ByteBuffer getBuffer() {
            return this.buffer;
        }

        public int getHeight() {
            return this.height;
        }

        public byte[] getMetadata() {
            return this.metadata;
        }

        public int getUvStride() {
            return this.uvStride;
        }

        public int getWidth() {
            return this.width;
        }

        public int getYstride() {
            return this.yStride;
        }

        native ByteBuffer get_frame_byte_buffer(long j);

        public boolean isMirroredX() {
            return this.mirrored;
        }

        @Deprecated
        public void recycle() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class swig_otc_video_frame extends SWIGTYPE_p_otc_video_frame {
        public swig_otc_video_frame(long j) {
            super(j, true);
        }

        public void destroy() {
            if (getCPtr() != 0) {
                opentokJNI.otc_video_frame_delete(getCPtr());
            }
        }

        public long getCPtr() {
            return SWIGTYPE_p_otc_video_frame.getCPtr(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Frame buildFrameInstance() {
        return new Frame();
    }

    public abstract View getView();

    public abstract void onFrame(Frame frame);

    public abstract void onPause();

    public abstract void onResume();

    public abstract void onVideoPropertiesChanged(boolean z);

    public abstract void setStyle(String str, String str2);
}
