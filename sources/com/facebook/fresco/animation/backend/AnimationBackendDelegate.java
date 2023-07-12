package com.facebook.fresco.animation.backend;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.annotation.IntRange;
import com.facebook.fresco.animation.backend.AnimationBackend;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class AnimationBackendDelegate<T extends AnimationBackend> implements AnimationBackend {
    private static final int ALPHA_UNSET = -1;
    @IntRange(from = -1, to = 255)
    private int mAlpha = -1;
    @Nullable
    private T mAnimationBackend;
    @Nullable
    private Rect mBounds;
    @Nullable
    private ColorFilter mColorFilter;

    public AnimationBackendDelegate(@Nullable T t) {
        this.mAnimationBackend = t;
    }

    @Override // com.facebook.fresco.animation.backend.AnimationInformation
    public int getFrameCount() {
        if (this.mAnimationBackend == null) {
            return 0;
        }
        return this.mAnimationBackend.getFrameCount();
    }

    @Override // com.facebook.fresco.animation.backend.AnimationInformation
    public int getFrameDurationMs(int i) {
        if (this.mAnimationBackend == null) {
            return 0;
        }
        return this.mAnimationBackend.getFrameDurationMs(i);
    }

    @Override // com.facebook.fresco.animation.backend.AnimationInformation
    public int getLoopCount() {
        if (this.mAnimationBackend == null) {
            return 0;
        }
        return this.mAnimationBackend.getLoopCount();
    }

    @Override // com.facebook.fresco.animation.backend.AnimationBackend
    public boolean drawFrame(Drawable drawable, Canvas canvas, int i) {
        return this.mAnimationBackend != null && this.mAnimationBackend.drawFrame(drawable, canvas, i);
    }

    @Override // com.facebook.fresco.animation.backend.AnimationBackend
    public void setAlpha(@IntRange(from = 0, to = 255) int i) {
        if (this.mAnimationBackend != null) {
            this.mAnimationBackend.setAlpha(i);
        }
        this.mAlpha = i;
    }

    @Override // com.facebook.fresco.animation.backend.AnimationBackend
    public void setColorFilter(ColorFilter colorFilter) {
        if (this.mAnimationBackend != null) {
            this.mAnimationBackend.setColorFilter(colorFilter);
        }
        this.mColorFilter = colorFilter;
    }

    @Override // com.facebook.fresco.animation.backend.AnimationBackend
    public void setBounds(@Nullable Rect rect) {
        if (this.mAnimationBackend != null) {
            this.mAnimationBackend.setBounds(rect);
        }
        this.mBounds = rect;
    }

    @Override // com.facebook.fresco.animation.backend.AnimationBackend
    public int getSizeInBytes() {
        if (this.mAnimationBackend == null) {
            return 0;
        }
        return this.mAnimationBackend.getSizeInBytes();
    }

    @Override // com.facebook.fresco.animation.backend.AnimationBackend
    public void clear() {
        if (this.mAnimationBackend != null) {
            this.mAnimationBackend.clear();
        }
    }

    @Override // com.facebook.fresco.animation.backend.AnimationBackend
    public int getIntrinsicWidth() {
        if (this.mAnimationBackend == null) {
            return -1;
        }
        return this.mAnimationBackend.getIntrinsicWidth();
    }

    @Override // com.facebook.fresco.animation.backend.AnimationBackend
    public int getIntrinsicHeight() {
        if (this.mAnimationBackend == null) {
            return -1;
        }
        return this.mAnimationBackend.getIntrinsicHeight();
    }

    public void setAnimationBackend(@Nullable T t) {
        this.mAnimationBackend = t;
        if (this.mAnimationBackend != null) {
            applyBackendProperties(this.mAnimationBackend);
        }
    }

    @Nullable
    public T getAnimationBackend() {
        return this.mAnimationBackend;
    }

    @SuppressLint({"Range"})
    private void applyBackendProperties(AnimationBackend animationBackend) {
        if (this.mBounds != null) {
            animationBackend.setBounds(this.mBounds);
        }
        if (this.mAlpha >= 0 && this.mAlpha <= 255) {
            animationBackend.setAlpha(this.mAlpha);
        }
        if (this.mColorFilter != null) {
            animationBackend.setColorFilter(this.mColorFilter);
        }
    }
}
