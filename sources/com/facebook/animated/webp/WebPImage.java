package com.facebook.animated.webp;

import com.facebook.common.internal.DoNotStrip;
import com.facebook.common.internal.Preconditions;
import com.facebook.imagepipeline.animated.base.AnimatedDrawableFrameInfo;
import com.facebook.imagepipeline.animated.base.AnimatedImage;
import com.facebook.imagepipeline.animated.factory.AnimatedImageDecoder;
import com.facebook.imagepipeline.nativecode.StaticWebpNativeLoader;
import java.nio.ByteBuffer;
import javax.annotation.concurrent.ThreadSafe;

@DoNotStrip
@ThreadSafe
/* loaded from: classes.dex */
public class WebPImage implements AnimatedImage, AnimatedImageDecoder {
    @DoNotStrip
    private long mNativeContext;

    private static native WebPImage nativeCreateFromDirectByteBuffer(ByteBuffer byteBuffer);

    private static native WebPImage nativeCreateFromNativeMemory(long j, int i);

    private native void nativeDispose();

    private native void nativeFinalize();

    private native int nativeGetDuration();

    private native WebPFrame nativeGetFrame(int i);

    private native int nativeGetFrameCount();

    private native int[] nativeGetFrameDurations();

    private native int nativeGetHeight();

    private native int nativeGetLoopCount();

    private native int nativeGetSizeInBytes();

    private native int nativeGetWidth();

    @Override // com.facebook.imagepipeline.animated.base.AnimatedImage
    public boolean doesRenderSupportScaling() {
        return true;
    }

    @DoNotStrip
    public WebPImage() {
    }

    @DoNotStrip
    WebPImage(long j) {
        this.mNativeContext = j;
    }

    protected void finalize() {
        nativeFinalize();
    }

    @Override // com.facebook.imagepipeline.animated.base.AnimatedImage
    public void dispose() {
        nativeDispose();
    }

    public static WebPImage create(byte[] bArr) {
        StaticWebpNativeLoader.ensure();
        Preconditions.checkNotNull(bArr);
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(bArr.length);
        allocateDirect.put(bArr);
        allocateDirect.rewind();
        return nativeCreateFromDirectByteBuffer(allocateDirect);
    }

    public static WebPImage create(long j, int i) {
        StaticWebpNativeLoader.ensure();
        Preconditions.checkArgument(j != 0);
        return nativeCreateFromNativeMemory(j, i);
    }

    @Override // com.facebook.imagepipeline.animated.factory.AnimatedImageDecoder
    public AnimatedImage decode(long j, int i) {
        return create(j, i);
    }

    @Override // com.facebook.imagepipeline.animated.base.AnimatedImage
    public int getWidth() {
        return nativeGetWidth();
    }

    @Override // com.facebook.imagepipeline.animated.base.AnimatedImage
    public int getHeight() {
        return nativeGetHeight();
    }

    @Override // com.facebook.imagepipeline.animated.base.AnimatedImage
    public int getFrameCount() {
        return nativeGetFrameCount();
    }

    @Override // com.facebook.imagepipeline.animated.base.AnimatedImage
    public int getDuration() {
        return nativeGetDuration();
    }

    @Override // com.facebook.imagepipeline.animated.base.AnimatedImage
    public int[] getFrameDurations() {
        return nativeGetFrameDurations();
    }

    @Override // com.facebook.imagepipeline.animated.base.AnimatedImage
    public int getLoopCount() {
        return nativeGetLoopCount();
    }

    @Override // com.facebook.imagepipeline.animated.base.AnimatedImage
    public WebPFrame getFrame(int i) {
        return nativeGetFrame(i);
    }

    @Override // com.facebook.imagepipeline.animated.base.AnimatedImage
    public int getSizeInBytes() {
        return nativeGetSizeInBytes();
    }

    @Override // com.facebook.imagepipeline.animated.base.AnimatedImage
    public AnimatedDrawableFrameInfo getFrameInfo(int i) {
        WebPFrame frame = getFrame(i);
        try {
            return new AnimatedDrawableFrameInfo(i, frame.getXOffset(), frame.getYOffset(), frame.getWidth(), frame.getHeight(), frame.isBlendWithPreviousFrame() ? AnimatedDrawableFrameInfo.BlendOperation.BLEND_WITH_PREVIOUS : AnimatedDrawableFrameInfo.BlendOperation.NO_BLEND, frame.shouldDisposeToBackgroundColor() ? AnimatedDrawableFrameInfo.DisposalMethod.DISPOSE_TO_BACKGROUND : AnimatedDrawableFrameInfo.DisposalMethod.DISPOSE_DO_NOT);
        } finally {
            frame.dispose();
        }
    }
}
