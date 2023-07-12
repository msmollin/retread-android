package com.bambuser.broadcaster;

import android.util.Log;
import com.bambuser.broadcaster.Capturer;
import com.bambuser.broadcaster.MediaCodecAudioWrapper;
import com.bambuser.broadcaster.SentryLogger;
import com.google.android.gms.measurement.AppMeasurement;
import java.nio.ByteBuffer;
import org.json.JSONObject;

/* loaded from: classes.dex */
final class AudioDataHandler {
    private static final String LOGTAG = "AudioDataHandler";
    private static final int MAX_AUDIO_QUEUE_LENGTH = 2000;
    private int mAudioFormat;
    private int mChannels;
    private Capturer.EncodeInterface mDataHandlerObserver;
    private ByteBuffer mEncodeBuffer;
    private int mEncoderDelay;
    private final long mInitTime;
    private MediaCodecAudioWrapper mMediaCodec;
    private int mQueueLimit;
    private int mSampleRate;
    private ByteBuffer mCodecConfigData = null;
    private int mMediaCodecEncodingFailures = 0;
    private boolean mEncoderDelayPassed = false;
    private final MediaCodecAudioWrapper.DataHandler mDataHandler = new MediaCodecAudioWrapper.DataHandler() { // from class: com.bambuser.broadcaster.AudioDataHandler.1
        @Override // com.bambuser.broadcaster.MediaCodecAudioWrapper.DataHandler
        void onOutputData(ByteBuffer byteBuffer, long j, boolean z) {
            long j2;
            if (AudioDataHandler.this.mDataHandlerObserver == null) {
                return;
            }
            if (z) {
                j2 = AudioDataHandler.this.mInitTime;
                byteBuffer.mark();
                AudioDataHandler.this.mCodecConfigData = ByteBuffer.allocate(byteBuffer.remaining());
                AudioDataHandler.this.mCodecConfigData.put(byteBuffer);
                AudioDataHandler.this.mCodecConfigData.flip();
                byteBuffer.reset();
            } else {
                j2 = j - AudioDataHandler.this.mEncoderDelay;
                if (j2 < AudioDataHandler.this.mInitTime && !AudioDataHandler.this.mEncoderDelayPassed) {
                    return;
                }
                AudioDataHandler.this.mEncoderDelayPassed = true;
            }
            int headerType = z ? Movino.getHeaderType(AudioDataHandler.this.mAudioFormat) : AudioDataHandler.this.mAudioFormat;
            byteBuffer.mark();
            if (AudioDataHandler.this.mDataHandlerObserver.onCanSendAudio() || z) {
                AudioDataHandler.this.mDataHandlerObserver.onSendData(false, j2, headerType, byteBuffer, true);
            } else if (AudioDataHandler.this.mDataHandlerObserver.onCanWriteComplement()) {
                AudioDataHandler.this.mDataHandlerObserver.onComplementData(false, j2, headerType, byteBuffer, true);
            }
            if (AudioDataHandler.this.mDataHandlerObserver.onCanWriteLocal()) {
                byteBuffer.reset();
                long j3 = j2 - (1024000 / AudioDataHandler.this.mSampleRate);
                Capturer.EncodeInterface encodeInterface = AudioDataHandler.this.mDataHandlerObserver;
                if (!z) {
                    j2 = j3;
                }
                encodeInterface.onLocalData(j2, headerType, byteBuffer);
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getRawBufferSize(Capturer.EncodeInterface.AudioFormat audioFormat) {
        return audioFormat == Capturer.EncodeInterface.AudioFormat.AAC ? 2048 : 2560;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00c1  */
    /* JADX WARN: Removed duplicated region for block: B:41:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public AudioDataHandler(int r18, int r19, com.bambuser.broadcaster.Capturer.EncodeInterface.AudioFormat r20, long r21, com.bambuser.broadcaster.Capturer.EncodeInterface r23) {
        /*
            Method dump skipped, instructions count: 205
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bambuser.broadcaster.AudioDataHandler.<init>(int, int, com.bambuser.broadcaster.Capturer$EncodeInterface$AudioFormat, long, com.bambuser.broadcaster.Capturer$EncodeInterface):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void close() {
        if (this.mMediaCodec != null) {
            this.mMediaCodec.close();
        }
        this.mMediaCodec = null;
        this.mCodecConfigData = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean processAudio(AudioWrapper audioWrapper, Capturer.EncodeInterface encodeInterface) {
        int i = audioWrapper.mIndex;
        boolean z = encodeInterface != null && encodeInterface.onCanSendAudio();
        boolean z2 = encodeInterface != null && encodeInterface.onCanWriteComplement();
        boolean z3 = encodeInterface != null && encodeInterface.onCanWriteLocal();
        if (z || z2 || z3) {
            this.mEncodeBuffer.clear();
            if (this.mAudioFormat == 37 && this.mMediaCodec != null) {
                this.mDataHandlerObserver = encodeInterface;
                try {
                    this.mMediaCodec.encode(audioWrapper.mBuffer, 0, audioWrapper.mIndex, audioWrapper.mTimestamp, this.mDataHandler);
                    this.mMediaCodec.flush(this.mDataHandler);
                    return z;
                } catch (Exception e) {
                    this.mMediaCodecEncodingFailures++;
                    JSONObject jSONObject = new JSONObject();
                    try {
                        jSONObject.put(AppMeasurement.Param.TIMESTAMP, audioWrapper.mTimestamp);
                        jSONObject.put("encoder_failures", this.mMediaCodecEncodingFailures);
                    } catch (Exception unused) {
                    }
                    Log.e(LOGTAG, "Mediacodec encoding failed", e);
                    SentryLogger.asyncMessage("AudioDataHandler MediaCodec encoding failed", SentryLogger.Level.ERROR, jSONObject, e);
                    this.mMediaCodec.close();
                    this.mMediaCodec = null;
                    throw e;
                }
            } else if (this.mAudioFormat == 5) {
                int encodeMuLaw = NativeUtils.encodeMuLaw(audioWrapper.mBuffer, 0, audioWrapper.mIndex, this.mEncodeBuffer.array());
                long j = audioWrapper.mTimestamp - this.mEncoderDelay;
                if (j >= this.mInitTime || this.mEncoderDelayPassed) {
                    this.mEncoderDelayPassed = true;
                    this.mEncodeBuffer.limit(encodeMuLaw);
                    this.mEncodeBuffer.mark();
                    if (z) {
                        encodeInterface.onSendData(false, j, this.mAudioFormat, this.mEncodeBuffer, true);
                    } else if (z2) {
                        encodeInterface.onComplementData(false, j, this.mAudioFormat, this.mEncodeBuffer, true);
                    }
                    if (z3) {
                        this.mEncodeBuffer.reset();
                        encodeInterface.onLocalData(j - (1024000 / this.mSampleRate), this.mAudioFormat, this.mEncodeBuffer);
                    }
                    return z;
                }
                return z;
            } else {
                return false;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resendAudioInitData(Capturer.EncodeInterface encodeInterface, long j) {
        if (encodeInterface == null) {
            return;
        }
        encodeInterface.onAudioInitialized(j, this.mSampleRate, this.mChannels, this.mQueueLimit);
        if (this.mCodecConfigData == null) {
            return;
        }
        this.mCodecConfigData.mark();
        encodeInterface.onSendData(false, j, Movino.getHeaderType(this.mAudioFormat), this.mCodecConfigData, true);
        this.mCodecConfigData.reset();
    }
}
