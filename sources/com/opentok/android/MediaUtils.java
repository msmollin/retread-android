package com.opentok.android;

import android.content.Context;
import com.opentok.android.Session;
import com.opentok.otc.e;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class MediaUtils {
    private static final String VIDEO_CODEC_H264_STRING = "H264";
    private static final String VIDEO_CODEC_VP8_STRING = "VP8";

    /* loaded from: classes.dex */
    public class SupportedCodecs {
        public ArrayList<VideoCodecType> videoEncoderCodecs = new ArrayList<>();
        public ArrayList<VideoCodecType> videoDecoderCodecs = new ArrayList<>();

        public SupportedCodecs() {
        }
    }

    /* loaded from: classes.dex */
    public enum VideoCodecType {
        VIDEO_CODEC_VP8(1),
        VIDEO_CODEC_H264(2);
        
        private final int value;

        VideoCodecType(int i) {
            this.value = i;
        }

        public static VideoCodecType getValue(int i) {
            VideoCodecType[] values;
            for (VideoCodecType videoCodecType : values()) {
                if (videoCodecType.getValue() == i) {
                    return videoCodecType;
                }
            }
            return null;
        }

        public int getValue() {
            return this.value;
        }
    }

    static {
        Loader.load();
    }

    public static SupportedCodecs getSupportedCodecs(Context context) {
        ArrayList<VideoCodecType> arrayList;
        VideoCodecType videoCodecType;
        MediaUtils mediaUtils = new MediaUtils();
        mediaUtils.getClass();
        SupportedCodecs supportedCodecs = new SupportedCodecs();
        if (new Session.Builder(context, "fakeApiKey", "fakeSssionId").build() == null) {
            return supportedCodecs;
        }
        int[] iArr = new int[1];
        String[] a = e.a(iArr);
        for (int i = 0; i < iArr[0]; i++) {
            if (a[i].equals(VIDEO_CODEC_VP8_STRING)) {
                supportedCodecs.videoEncoderCodecs.add(VideoCodecType.VIDEO_CODEC_VP8);
                arrayList = supportedCodecs.videoDecoderCodecs;
                videoCodecType = VideoCodecType.VIDEO_CODEC_VP8;
            } else if (a[i].equals(VIDEO_CODEC_H264_STRING)) {
                supportedCodecs.videoEncoderCodecs.add(VideoCodecType.VIDEO_CODEC_H264);
                arrayList = supportedCodecs.videoDecoderCodecs;
                videoCodecType = VideoCodecType.VIDEO_CODEC_H264;
            }
            arrayList.add(videoCodecType);
        }
        return supportedCodecs;
    }
}
