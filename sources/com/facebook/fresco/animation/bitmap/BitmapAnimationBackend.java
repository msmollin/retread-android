package com.facebook.fresco.animation.bitmap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.annotation.IntRange;
import com.facebook.common.logging.FLog;
import com.facebook.common.references.CloseableReference;
import com.facebook.fresco.animation.backend.AnimationBackend;
import com.facebook.fresco.animation.backend.AnimationBackendDelegateWithInactivityCheck;
import com.facebook.fresco.animation.backend.AnimationInformation;
import com.facebook.fresco.animation.bitmap.preparation.BitmapFramePreparationStrategy;
import com.facebook.fresco.animation.bitmap.preparation.BitmapFramePreparer;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class BitmapAnimationBackend implements AnimationBackend, AnimationBackendDelegateWithInactivityCheck.InactivityListener {
    public static final int FRAME_TYPE_CACHED = 0;
    public static final int FRAME_TYPE_CREATED = 2;
    public static final int FRAME_TYPE_FALLBACK = 3;
    public static final int FRAME_TYPE_REUSED = 1;
    public static final int FRAME_TYPE_UNKNOWN = -1;
    private static final Class<?> TAG = BitmapAnimationBackend.class;
    private final AnimationInformation mAnimationInformation;
    private final BitmapFrameCache mBitmapFrameCache;
    @Nullable
    private final BitmapFramePreparationStrategy mBitmapFramePreparationStrategy;
    @Nullable
    private final BitmapFramePreparer mBitmapFramePreparer;
    private final BitmapFrameRenderer mBitmapFrameRenderer;
    private int mBitmapHeight;
    private int mBitmapWidth;
    @Nullable
    private Rect mBounds;
    @Nullable
    private FrameListener mFrameListener;
    private final PlatformBitmapFactory mPlatformBitmapFactory;
    private Bitmap.Config mBitmapConfig = Bitmap.Config.ARGB_8888;
    private final Paint mPaint = new Paint(6);

    /* loaded from: classes.dex */
    public interface FrameListener {
        void onDrawFrameStart(BitmapAnimationBackend bitmapAnimationBackend, int i);

        void onFrameDrawn(BitmapAnimationBackend bitmapAnimationBackend, int i, int i2);

        void onFrameDropped(BitmapAnimationBackend bitmapAnimationBackend, int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface FrameType {
    }

    public BitmapAnimationBackend(PlatformBitmapFactory platformBitmapFactory, BitmapFrameCache bitmapFrameCache, AnimationInformation animationInformation, BitmapFrameRenderer bitmapFrameRenderer, @Nullable BitmapFramePreparationStrategy bitmapFramePreparationStrategy, @Nullable BitmapFramePreparer bitmapFramePreparer) {
        this.mPlatformBitmapFactory = platformBitmapFactory;
        this.mBitmapFrameCache = bitmapFrameCache;
        this.mAnimationInformation = animationInformation;
        this.mBitmapFrameRenderer = bitmapFrameRenderer;
        this.mBitmapFramePreparationStrategy = bitmapFramePreparationStrategy;
        this.mBitmapFramePreparer = bitmapFramePreparer;
        updateBitmapDimensions();
    }

    public void setBitmapConfig(Bitmap.Config config) {
        this.mBitmapConfig = config;
    }

    public void setFrameListener(@Nullable FrameListener frameListener) {
        this.mFrameListener = frameListener;
    }

    @Override // com.facebook.fresco.animation.backend.AnimationInformation
    public int getFrameCount() {
        return this.mAnimationInformation.getFrameCount();
    }

    @Override // com.facebook.fresco.animation.backend.AnimationInformation
    public int getFrameDurationMs(int i) {
        return this.mAnimationInformation.getFrameDurationMs(i);
    }

    @Override // com.facebook.fresco.animation.backend.AnimationInformation
    public int getLoopCount() {
        return this.mAnimationInformation.getLoopCount();
    }

    @Override // com.facebook.fresco.animation.backend.AnimationBackend
    public boolean drawFrame(Drawable drawable, Canvas canvas, int i) {
        if (this.mFrameListener != null) {
            this.mFrameListener.onDrawFrameStart(this, i);
        }
        boolean drawFrameOrFallback = drawFrameOrFallback(canvas, i, 0);
        if (!drawFrameOrFallback && this.mFrameListener != null) {
            this.mFrameListener.onFrameDropped(this, i);
        }
        if (this.mBitmapFramePreparationStrategy != null && this.mBitmapFramePreparer != null) {
            this.mBitmapFramePreparationStrategy.prepareFrames(this.mBitmapFramePreparer, this.mBitmapFrameCache, this, i);
        }
        return drawFrameOrFallback;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean drawFrameOrFallback(Canvas canvas, int i, int i2) {
        CloseableReference<Bitmap> createBitmap;
        boolean z;
        int i3 = 3;
        boolean z2 = true;
        CloseableReference closeableReference = null;
        try {
            try {
                switch (i2) {
                    case 0:
                        createBitmap = this.mBitmapFrameCache.getCachedFrame(i);
                        z = drawBitmapAndCache(i, createBitmap, canvas, 0);
                        i3 = 1;
                        break;
                    case 1:
                        createBitmap = this.mBitmapFrameCache.getBitmapToReuseForFrame(i, this.mBitmapWidth, this.mBitmapHeight);
                        if (!renderFrameInBitmap(i, createBitmap) || !drawBitmapAndCache(i, createBitmap, canvas, 1)) {
                            z2 = false;
                        }
                        i3 = 2;
                        z = z2;
                        break;
                    case 2:
                        try {
                            createBitmap = this.mPlatformBitmapFactory.createBitmap(this.mBitmapWidth, this.mBitmapHeight, this.mBitmapConfig);
                            if (!renderFrameInBitmap(i, createBitmap) || !drawBitmapAndCache(i, createBitmap, canvas, 2)) {
                                z = false;
                                break;
                            }
                            z = z2;
                            break;
                        } catch (RuntimeException e) {
                            FLog.w(TAG, "Failed to create frame bitmap", e);
                            CloseableReference.closeSafely((CloseableReference<?>) null);
                            return false;
                        }
                        break;
                    case 3:
                        createBitmap = this.mBitmapFrameCache.getFallbackFrame(i);
                        z = drawBitmapAndCache(i, createBitmap, canvas, 3);
                        i3 = -1;
                        break;
                    default:
                        CloseableReference.closeSafely((CloseableReference<?>) null);
                        return false;
                }
                CloseableReference.closeSafely(createBitmap);
                return (z || i3 == -1) ? z : drawFrameOrFallback(canvas, i, i3);
            } catch (Throwable th) {
                th = th;
                CloseableReference.closeSafely(closeableReference);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            closeableReference = i2;
            CloseableReference.closeSafely(closeableReference);
            throw th;
        }
    }

    @Override // com.facebook.fresco.animation.backend.AnimationBackend
    public void setAlpha(@IntRange(from = 0, to = 255) int i) {
        this.mPaint.setAlpha(i);
    }

    @Override // com.facebook.fresco.animation.backend.AnimationBackend
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }

    @Override // com.facebook.fresco.animation.backend.AnimationBackend
    public void setBounds(@Nullable Rect rect) {
        this.mBounds = rect;
        this.mBitmapFrameRenderer.setBounds(rect);
        updateBitmapDimensions();
    }

    @Override // com.facebook.fresco.animation.backend.AnimationBackend
    public int getIntrinsicWidth() {
        return this.mBitmapWidth;
    }

    @Override // com.facebook.fresco.animation.backend.AnimationBackend
    public int getIntrinsicHeight() {
        return this.mBitmapHeight;
    }

    @Override // com.facebook.fresco.animation.backend.AnimationBackend
    public int getSizeInBytes() {
        return this.mBitmapFrameCache.getSizeInBytes();
    }

    @Override // com.facebook.fresco.animation.backend.AnimationBackend
    public void clear() {
        this.mBitmapFrameCache.clear();
    }

    @Override // com.facebook.fresco.animation.backend.AnimationBackendDelegateWithInactivityCheck.InactivityListener
    public void onInactive() {
        clear();
    }

    private void updateBitmapDimensions() {
        this.mBitmapWidth = this.mBitmapFrameRenderer.getIntrinsicWidth();
        if (this.mBitmapWidth == -1) {
            this.mBitmapWidth = this.mBounds == null ? -1 : this.mBounds.width();
        }
        this.mBitmapHeight = this.mBitmapFrameRenderer.getIntrinsicHeight();
        if (this.mBitmapHeight == -1) {
            this.mBitmapHeight = this.mBounds != null ? this.mBounds.height() : -1;
        }
    }

    private boolean renderFrameInBitmap(int i, @Nullable CloseableReference<Bitmap> closeableReference) {
        if (CloseableReference.isValid(closeableReference)) {
            boolean renderFrame = this.mBitmapFrameRenderer.renderFrame(i, closeableReference.get());
            if (!renderFrame) {
                CloseableReference.closeSafely(closeableReference);
            }
            return renderFrame;
        }
        return false;
    }

    private boolean drawBitmapAndCache(int i, @Nullable CloseableReference<Bitmap> closeableReference, Canvas canvas, int i2) {
        if (CloseableReference.isValid(closeableReference)) {
            if (this.mBounds == null) {
                canvas.drawBitmap(closeableReference.get(), 0.0f, 0.0f, this.mPaint);
            } else {
                canvas.drawBitmap(closeableReference.get(), (Rect) null, this.mBounds, this.mPaint);
            }
            if (i2 != 3) {
                this.mBitmapFrameCache.onFrameRendered(i, closeableReference, i2);
            }
            if (this.mFrameListener != null) {
                this.mFrameListener.onFrameDrawn(this, i, i2);
                return true;
            }
            return true;
        }
        return false;
    }
}
