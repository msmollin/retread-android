package com.daasuu.gpuv.camerarecorder.capture;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.opengl.EGLContext;
import android.util.Log;
import android.view.Surface;
import com.daasuu.gpuv.camerarecorder.capture.MediaEncoder;
import com.daasuu.gpuv.egl.filter.GlFilter;
import java.io.IOException;

/* loaded from: classes.dex */
public class MediaVideoEncoder extends MediaEncoder {
    private static final float BPP = 0.25f;
    private static final int FRAME_RATE = 30;
    private static final String MIME_TYPE = "video/avc";
    private static final String TAG = "MediaVideoEncoder";
    private EncodeRenderHandler encodeRenderHandler;
    private final int fileHeight;
    private final int fileWidth;
    private Surface surface;

    public MediaVideoEncoder(MediaMuxerCaptureWrapper mediaMuxerCaptureWrapper, MediaEncoder.MediaEncoderListener mediaEncoderListener, int i, int i2, boolean z, boolean z2, float f, float f2, boolean z3, GlFilter glFilter) {
        super(mediaMuxerCaptureWrapper, mediaEncoderListener);
        this.fileWidth = i;
        this.fileHeight = i2;
        this.encodeRenderHandler = EncodeRenderHandler.createHandler(TAG, z2, z, f > f2 ? f / f2 : f2 / f, i, i2, z3, glFilter);
    }

    public void frameAvailableSoon(int i, float[] fArr, float[] fArr2, float f) {
        if (super.frameAvailableSoon()) {
            this.encodeRenderHandler.draw(i, fArr, fArr2, f);
        }
    }

    @Override // com.daasuu.gpuv.camerarecorder.capture.MediaEncoder
    public boolean frameAvailableSoon() {
        boolean frameAvailableSoon = super.frameAvailableSoon();
        if (frameAvailableSoon) {
            this.encodeRenderHandler.prepareDraw();
        }
        return frameAvailableSoon;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.daasuu.gpuv.camerarecorder.capture.MediaEncoder
    public void prepare() throws IOException {
        Log.i(TAG, "prepare: ");
        this.trackIndex = -1;
        this.isEOS = false;
        this.muxerStarted = false;
        MediaCodecInfo selectVideoCodec = selectVideoCodec(MIME_TYPE);
        if (selectVideoCodec == null) {
            Log.e(TAG, "Unable to find an appropriate codec for video/avc");
            return;
        }
        Log.i(TAG, "selected codec: " + selectVideoCodec.getName());
        MediaFormat createVideoFormat = MediaFormat.createVideoFormat(MIME_TYPE, this.fileWidth, this.fileHeight);
        createVideoFormat.setInteger("color-format", 2130708361);
        createVideoFormat.setInteger("bitrate", calcBitRate(this.fileWidth, this.fileHeight));
        createVideoFormat.setInteger("frame-rate", 30);
        createVideoFormat.setInteger("i-frame-interval", 3);
        Log.i(TAG, "format: " + createVideoFormat);
        this.mediaCodec = MediaCodec.createEncoderByType(MIME_TYPE);
        this.mediaCodec.configure(createVideoFormat, (Surface) null, (MediaCrypto) null, 1);
        this.surface = this.mediaCodec.createInputSurface();
        this.mediaCodec.start();
        Log.i(TAG, "prepare finishing");
        if (this.listener != null) {
            try {
                this.listener.onPrepared(this);
            } catch (Exception e) {
                Log.e(TAG, "prepare:", e);
            }
        }
    }

    public void setEglContext(EGLContext eGLContext, int i) {
        this.encodeRenderHandler.setEglContext(eGLContext, i, this.surface);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.daasuu.gpuv.camerarecorder.capture.MediaEncoder
    public void release() {
        Log.i(TAG, "release:");
        if (this.surface != null) {
            this.surface.release();
            this.surface = null;
        }
        if (this.encodeRenderHandler != null) {
            this.encodeRenderHandler.release();
            this.encodeRenderHandler = null;
        }
        super.release();
    }

    private static int calcBitRate(int i, int i2) {
        int i3 = (int) (i * 7.5f * i2);
        Log.i(TAG, "bitrate=" + i3);
        return i3;
    }

    private static MediaCodecInfo selectVideoCodec(String str) {
        MediaCodecInfo[] codecInfos;
        Log.v(TAG, "selectVideoCodec:");
        for (MediaCodecInfo mediaCodecInfo : new MediaCodecList(1).getCodecInfos()) {
            if (mediaCodecInfo.isEncoder()) {
                String[] supportedTypes = mediaCodecInfo.getSupportedTypes();
                for (int i = 0; i < supportedTypes.length; i++) {
                    if (supportedTypes[i].equalsIgnoreCase(str)) {
                        Log.i(TAG, "codec:" + mediaCodecInfo.getName() + ",MIME=" + supportedTypes[i]);
                        if (selectColorFormat(mediaCodecInfo, str) > 0) {
                            return mediaCodecInfo;
                        }
                    }
                }
                continue;
            }
        }
        return null;
    }

    private static int selectColorFormat(MediaCodecInfo mediaCodecInfo, String str) {
        Log.i(TAG, "selectColorFormat: ");
        try {
            Thread.currentThread().setPriority(10);
            MediaCodecInfo.CodecCapabilities capabilitiesForType = mediaCodecInfo.getCapabilitiesForType(str);
            Thread.currentThread().setPriority(5);
            int i = 0;
            int i2 = 0;
            while (true) {
                if (i2 >= capabilitiesForType.colorFormats.length) {
                    break;
                }
                int i3 = capabilitiesForType.colorFormats[i2];
                if (isRecognizedViewoFormat(i3)) {
                    i = i3;
                    break;
                }
                i2++;
            }
            if (i == 0) {
                Log.e(TAG, "couldn't find a good color format for " + mediaCodecInfo.getName() + " / " + str);
            }
            return i;
        } catch (Throwable th) {
            Thread.currentThread().setPriority(5);
            throw th;
        }
    }

    private static boolean isRecognizedViewoFormat(int i) {
        Log.i(TAG, "isRecognizedViewoFormat:colorFormat=" + i);
        return i == 2130708361;
    }

    @Override // com.daasuu.gpuv.camerarecorder.capture.MediaEncoder
    protected void signalEndOfInputStream() {
        Log.d(TAG, "sending EOS to encoder");
        this.mediaCodec.signalEndOfInputStream();
        this.isEOS = true;
    }
}
