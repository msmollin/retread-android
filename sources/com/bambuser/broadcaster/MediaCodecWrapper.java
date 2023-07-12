package com.bambuser.broadcaster;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;
import com.bambuser.broadcaster.SentryLogger;
import com.bambuser.broadcaster.VideoEncoderBase;
import com.facebook.appevents.internal.ViewHierarchyConstants;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class MediaCodecWrapper extends VideoEncoderBase {
    private static final String LOGTAG = "MediaCodecWrapper";
    private MediaCodec.BufferInfo mBufferInfo;
    private MediaCodec mCodec;
    private int mHeight;
    private int mHeightAlignment;
    private ByteBuffer[] mInputBuffers;
    private long mInputFrameCount;
    private final VideoEncoderBase.TimeMap mMap = new VideoEncoderBase.TimeMap(30);
    private long mMicrosPerFrame;
    private String mMimeType;
    private ByteBuffer[] mOutputBufferArray;
    private ByteBuffer[] mOutputBuffers;
    private int[] mOutputDetailArray;
    private int mPixelFormat;
    private int mSliceHeight;
    private int mStride;
    private ByteBuffer mTempBuffer;
    private int mWidth;
    private int mWidthAlignment;

    private static MediaCodecInfo findCodec(String str, boolean z, int[] iArr) {
        ArrayList arrayList = new ArrayList();
        if (z) {
            arrayList.add(21);
            arrayList.add(39);
            arrayList.add(19);
            arrayList.add(20);
        } else {
            arrayList.add(19);
            arrayList.add(20);
            arrayList.add(21);
            arrayList.add(39);
        }
        arrayList.add(2130706688);
        int codecCount = MediaCodecList.getCodecCount();
        MediaCodecInfo mediaCodecInfo = null;
        int i = -1;
        int i2 = -1;
        int i3 = 0;
        while (true) {
            if (i3 >= codecCount) {
                break;
            }
            MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i3);
            if (codecInfoAt.isEncoder() && !codecInfoAt.getName().startsWith("OMX.google") && (!codecInfoAt.getName().equals("OMX.SEC.AVC.Encoder") || (!DeviceInfoHandler.isSamsGalaxySIII() && !DeviceInfoHandler.isSamsGalaxyNoteII()))) {
                String[] supportedTypes = codecInfoAt.getSupportedTypes();
                boolean z2 = false;
                for (int i4 = 0; i4 < supportedTypes.length && !z2; i4++) {
                    if (supportedTypes[i4].equals(str)) {
                        z2 = true;
                    }
                }
                if (z2) {
                    int[] iArr2 = codecInfoAt.getCapabilitiesForType(str).colorFormats;
                    Log.d(LOGTAG, "Checking codec " + codecInfoAt.getName() + ", " + iArr2.length + " color formats");
                    int i5 = i;
                    int i6 = i2;
                    boolean z3 = false;
                    for (int i7 = 0; i7 < iArr2.length; i7++) {
                        int indexOf = arrayList.indexOf(Integer.valueOf(iArr2[i7]));
                        if (indexOf >= 0) {
                            if (i5 < 0 || indexOf < i5) {
                                i6 = iArr2[i7];
                                i5 = indexOf;
                            }
                            z3 = true;
                        }
                    }
                    if (z3) {
                        mediaCodecInfo = codecInfoAt;
                        i2 = i6;
                        break;
                    }
                    Log.d(LOGTAG, "No supported color formats");
                    i = i5;
                    i2 = i6;
                } else {
                    continue;
                }
            }
            i3++;
        }
        if (iArr != null) {
            iArr[0] = i2;
        }
        return mediaCodecInfo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean supportsCodec(String str) {
        return findCodec(str, false, null) != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void init(String str, int i, int i2, int i3, int i4, boolean z) throws IllegalArgumentException, IOException {
        this.mWidth = i;
        this.mHeight = i2;
        this.mMimeType = str;
        this.mMicrosPerFrame = 1000000 / i4;
        int[] iArr = new int[1];
        MediaCodecInfo findCodec = findCodec(str, z, iArr);
        if (findCodec == null) {
            IllegalArgumentException illegalArgumentException = new IllegalArgumentException("No supported codec found");
            if (!FrameHandler.sMediaCodecFailureLogged) {
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.put("prefersemiplanar", z);
                    jSONObject.put(ViewHierarchyConstants.DIMENSION_WIDTH_KEY, i);
                    jSONObject.put(ViewHierarchyConstants.DIMENSION_HEIGHT_KEY, i2);
                    jSONObject.put("bitrate", i3);
                    jSONObject.put("fps", i4);
                    jSONObject.put("codec_count_all", MediaCodecList.getCodecCount());
                    jSONObject.put("encoders", encodersToJsonArray());
                } catch (Exception unused) {
                }
                SentryLogger.asyncMessage("No supported codec found", SentryLogger.Level.WARNING, jSONObject, illegalArgumentException);
                FrameHandler.sMediaCodecFailureLogged = true;
            }
            throw illegalArgumentException;
        }
        if (str.equals("video/avc")) {
            this.mHeightAlignment = 16;
            this.mWidthAlignment = 16;
        } else if (str.equals("video/hevc")) {
            this.mHeightAlignment = 64;
            this.mWidthAlignment = 64;
        }
        int align = Frame.align(i, this.mWidthAlignment);
        int align2 = Frame.align(i2, this.mHeightAlignment);
        String name = findCodec.getName();
        int i5 = iArr[0];
        Log.d(LOGTAG, "Using codec " + name + " and pixel format " + i5);
        this.mCodec = MediaCodec.createByCodecName(name);
        if (this.mCodec == null) {
            throw new IllegalArgumentException("Encoder not supported");
        }
        MediaFormat createVideoFormat = MediaFormat.createVideoFormat(str, align, align2);
        this.mStride = align;
        this.mSliceHeight = align2;
        createVideoFormat.setInteger("bitrate", i3);
        createVideoFormat.setInteger("frame-rate", i4);
        createVideoFormat.setInteger("color-format", i5);
        createVideoFormat.setInteger("i-frame-interval", 3);
        this.mCodec.configure(createVideoFormat, (Surface) null, (MediaCrypto) null, 1);
        this.mCodec.start();
        this.mInputBuffers = this.mCodec.getInputBuffers();
        this.mOutputBuffers = this.mCodec.getOutputBuffers();
        this.mBufferInfo = new MediaCodec.BufferInfo();
        this.mPixelFormat = i5;
        if (this.mPixelFormat == 39 || this.mPixelFormat == 2130706688) {
            this.mPixelFormat = 21;
        }
        if (this.mPixelFormat == 20) {
            this.mPixelFormat = 19;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.VideoEncoderBase
    public void close() {
        if (this.mCodec != null) {
            try {
                this.mCodec.stop();
            } catch (Exception unused) {
            }
            this.mCodec.release();
            this.mCodec = null;
        }
        this.mOutputDetailArray = null;
        this.mOutputBufferArray = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.VideoEncoderBase
    public void finish(VideoEncoderBase.DataHandler dataHandler) {
        if (this.mInputFrameCount < 1) {
            return;
        }
        while (true) {
            try {
                int dequeueInputBuffer = this.mCodec.dequeueInputBuffer(150000L);
                if (dequeueInputBuffer < 0) {
                    flush(dataHandler);
                } else {
                    this.mInputBuffers[dequeueInputBuffer].clear();
                    this.mCodec.queueInputBuffer(dequeueInputBuffer, 0, 0, 0L, 4);
                    flush(dataHandler, 150000);
                    return;
                }
            } catch (Exception e) {
                Log.w(LOGTAG, "exception when finishing mediacodec: " + e);
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.VideoEncoderBase
    public void encode(Frame frame, VideoEncoderBase.DataHandler dataHandler) {
        while (true) {
            int dequeueInputBuffer = this.mCodec.dequeueInputBuffer(150000L);
            if (dequeueInputBuffer < 0) {
                flush(dataHandler);
            } else {
                ByteBuffer byteBuffer = this.mInputBuffers[dequeueInputBuffer];
                byteBuffer.clear();
                this.mOutputBufferArray = NativeUtils.getByteBufferArray(this.mOutputBufferArray, byteBuffer);
                this.mOutputDetailArray = NativeUtils.fillOmxBufferDetails(this.mOutputDetailArray, this.mStride, this.mSliceHeight, 0, this.mPixelFormat);
                NativeUtils.copyScaleFrame(frame.getBufferArray(), frame.getDetailArray(), frame.mWidth, frame.mHeight, this.mOutputBufferArray, this.mOutputDetailArray, this.mWidth, this.mHeight);
                long j = this.mInputFrameCount + 1;
                this.mInputFrameCount = j;
                long j2 = j * this.mMicrosPerFrame;
                this.mMap.push(j2, frame.mTimestamp, frame.mRotation);
                this.mCodec.queueInputBuffer(dequeueInputBuffer, 0, byteBuffer.position(), j2, 0);
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.VideoEncoderBase
    public void flush(VideoEncoderBase.DataHandler dataHandler) {
        flush(dataHandler, 0);
    }

    void flush(VideoEncoderBase.DataHandler dataHandler, int i) {
        ByteBuffer hEVCCropping;
        while (true) {
            int dequeueOutputBuffer = this.mCodec.dequeueOutputBuffer(this.mBufferInfo, i);
            if (dequeueOutputBuffer == -3) {
                this.mOutputBuffers = this.mCodec.getOutputBuffers();
            } else if (dequeueOutputBuffer == -2) {
                continue;
            } else if (dequeueOutputBuffer < 0) {
                return;
            } else {
                ByteBuffer byteBuffer = this.mOutputBuffers[dequeueOutputBuffer];
                byteBuffer.limit(this.mBufferInfo.offset + this.mBufferInfo.size);
                byteBuffer.position(this.mBufferInfo.offset);
                if ((this.mBufferInfo.flags & 4) != 0) {
                    this.mCodec.releaseOutputBuffer(dequeueOutputBuffer, false);
                    return;
                }
                boolean z = (this.mBufferInfo.flags & 2) != 0;
                if (this.mMimeType.equals("video/avc") || this.mMimeType.equals("video/hevc")) {
                    if (this.mTempBuffer != null) {
                        this.mTempBuffer.clear();
                    }
                    if (this.mMimeType.equals("video/avc")) {
                        hEVCCropping = NativeUtils.setH264Cropping(byteBuffer, this.mWidth, this.mHeight, this.mWidthAlignment, this.mHeightAlignment, this.mTempBuffer);
                    } else {
                        hEVCCropping = NativeUtils.setHEVCCropping(byteBuffer, this.mWidth, this.mHeight, this.mWidthAlignment, this.mHeightAlignment, this.mTempBuffer);
                    }
                    byteBuffer = hEVCCropping;
                    if (byteBuffer != this.mOutputBuffers[dequeueOutputBuffer]) {
                        this.mTempBuffer = byteBuffer;
                        byteBuffer.flip();
                    }
                }
                ByteBuffer byteBuffer2 = byteBuffer;
                int findBestIndex = this.mMap.findBestIndex(this.mBufferInfo.presentationTimeUs);
                dataHandler.onEncodedData(byteBuffer2, this.mMap.getTime(findBestIndex), this.mMap.getRotation(findBestIndex), z);
                this.mCodec.releaseOutputBuffer(dequeueOutputBuffer, false);
            }
        }
    }

    private JSONArray encodersToJsonArray() {
        String[] supportedTypes;
        int codecCount = MediaCodecList.getCodecCount();
        JSONArray jSONArray = new JSONArray();
        for (int i = 0; i < codecCount; i++) {
            MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i);
            if (codecInfoAt.isEncoder()) {
                JSONArray jSONArray2 = new JSONArray();
                for (String str : codecInfoAt.getSupportedTypes()) {
                    JSONObject jSONObject = new JSONObject();
                    JSONArray jSONArray3 = new JSONArray();
                    for (int i2 : codecInfoAt.getCapabilitiesForType(str).colorFormats) {
                        jSONArray3.put(i2);
                    }
                    try {
                        jSONObject.put("type", str);
                        if (jSONArray3.length() > 0) {
                            jSONObject.put("colorFormats", jSONArray3);
                        }
                    } catch (Exception unused) {
                    }
                    jSONArray2.put(jSONObject);
                }
                JSONObject jSONObject2 = new JSONObject();
                try {
                    jSONObject2.put("name", codecInfoAt.getName());
                    jSONObject2.put("types", jSONArray2);
                } catch (Exception unused2) {
                }
                jSONArray.put(jSONObject2);
            }
        }
        return jSONArray;
    }
}
