package com.daasuu.gpuv.composer;

/* loaded from: classes.dex */
interface IAudioComposer {
    long getWrittenPresentationTimeUs();

    boolean isFinished();

    void release();

    void setup();

    boolean stepPipeline();
}
