package com.daasuu.gpuv.camerarecorder.capture;

import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Process;
import android.util.Log;
import android.view.Surface;
import com.daasuu.gpuv.camerarecorder.capture.MediaEncoder;
import java.io.IOException;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public class MediaAudioEncoder extends MediaEncoder {
    private static final int[] AUDIO_SOURCES = {1, 0, 5, 7, 6};
    private static final int BIT_RATE = 64000;
    private static final int FRAMES_PER_BUFFER = 25;
    private static final String MIME_TYPE = "audio/mp4a-latm";
    private static final int SAMPLES_PER_FRAME = 1024;
    private static final int SAMPLE_RATE = 44100;
    private static final String TAG = "MediaAudioEncoder";
    private AudioThread audioThread;

    public MediaAudioEncoder(MediaMuxerCaptureWrapper mediaMuxerCaptureWrapper, MediaEncoder.MediaEncoderListener mediaEncoderListener) {
        super(mediaMuxerCaptureWrapper, mediaEncoderListener);
        this.audioThread = null;
    }

    @Override // com.daasuu.gpuv.camerarecorder.capture.MediaEncoder
    protected void prepare() throws IOException {
        Log.v(TAG, "prepare:");
        this.trackIndex = -1;
        this.isEOS = false;
        this.muxerStarted = false;
        MediaCodecInfo selectAudioCodec = selectAudioCodec(MIME_TYPE);
        if (selectAudioCodec == null) {
            Log.e(TAG, "Unable to find an appropriate codec for audio/mp4a-latm");
            return;
        }
        Log.i(TAG, "selected codec: " + selectAudioCodec.getName());
        MediaFormat createAudioFormat = MediaFormat.createAudioFormat(MIME_TYPE, SAMPLE_RATE, 1);
        createAudioFormat.setInteger("aac-profile", 2);
        createAudioFormat.setInteger("channel-mask", 16);
        createAudioFormat.setInteger("bitrate", BIT_RATE);
        createAudioFormat.setInteger("channel-count", 1);
        Log.i(TAG, "format: " + createAudioFormat);
        this.mediaCodec = MediaCodec.createEncoderByType(MIME_TYPE);
        this.mediaCodec.configure(createAudioFormat, (Surface) null, (MediaCrypto) null, 1);
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

    @Override // com.daasuu.gpuv.camerarecorder.capture.MediaEncoder
    protected void startRecording() {
        super.startRecording();
        if (this.audioThread == null) {
            this.audioThread = new AudioThread();
            this.audioThread.start();
        }
    }

    @Override // com.daasuu.gpuv.camerarecorder.capture.MediaEncoder
    protected void release() {
        this.audioThread = null;
        super.release();
    }

    /* loaded from: classes.dex */
    private class AudioThread extends Thread {
        private AudioThread() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Process.setThreadPriority(-19);
            try {
                int minBufferSize = AudioRecord.getMinBufferSize(MediaAudioEncoder.SAMPLE_RATE, 16, 2);
                int i = 25600 < minBufferSize ? ((minBufferSize / 1024) + 1) * 1024 * 2 : 25600;
                AudioRecord audioRecord = null;
                for (int i2 : MediaAudioEncoder.AUDIO_SOURCES) {
                    try {
                        AudioRecord audioRecord2 = new AudioRecord(i2, MediaAudioEncoder.SAMPLE_RATE, 16, 2, i);
                        if (audioRecord2.getState() != 1) {
                            audioRecord2 = null;
                        }
                        audioRecord = audioRecord2;
                    } catch (Exception unused) {
                        audioRecord = null;
                    }
                    if (audioRecord != null) {
                        break;
                    }
                }
                if (audioRecord != null) {
                    if (MediaAudioEncoder.this.isCapturing) {
                        Log.v(MediaAudioEncoder.TAG, "AudioThread:start audio recording");
                        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(1024);
                        audioRecord.startRecording();
                        while (MediaAudioEncoder.this.isCapturing && !MediaAudioEncoder.this.requestStop && !MediaAudioEncoder.this.isEOS) {
                            try {
                                allocateDirect.clear();
                                int read = audioRecord.read(allocateDirect, 1024);
                                if (read > 0) {
                                    allocateDirect.position(read);
                                    allocateDirect.flip();
                                    MediaAudioEncoder.this.encode(allocateDirect, read, MediaAudioEncoder.this.getPTSUs());
                                    MediaAudioEncoder.this.frameAvailableSoon();
                                }
                            } catch (Throwable th) {
                                audioRecord.stop();
                                throw th;
                            }
                        }
                        MediaAudioEncoder.this.frameAvailableSoon();
                        audioRecord.stop();
                    }
                    audioRecord.release();
                } else {
                    Log.e(MediaAudioEncoder.TAG, "failed to initialize AudioRecord");
                }
            } catch (Exception e) {
                Log.e(MediaAudioEncoder.TAG, "AudioThread#run", e);
            }
            Log.v(MediaAudioEncoder.TAG, "AudioThread:finished");
        }
    }

    private static MediaCodecInfo selectAudioCodec(String str) {
        MediaCodecInfo[] codecInfos;
        Log.v(TAG, "selectAudioCodec:");
        for (MediaCodecInfo mediaCodecInfo : new MediaCodecList(1).getCodecInfos()) {
            if (mediaCodecInfo.isEncoder()) {
                String[] supportedTypes = mediaCodecInfo.getSupportedTypes();
                for (int i = 0; i < supportedTypes.length; i++) {
                    if (supportedTypes[i].equalsIgnoreCase(str)) {
                        Log.i(TAG, "codec:" + mediaCodecInfo.getName() + ",MIME=" + supportedTypes[i]);
                        return mediaCodecInfo;
                    }
                }
                continue;
            }
        }
        return null;
    }
}
