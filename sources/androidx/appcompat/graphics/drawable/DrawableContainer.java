package androidx.appcompat.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.SystemClock;
import android.util.SparseArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import androidx.core.graphics.drawable.DrawableCompat;

/* JADX INFO: Access modifiers changed from: package-private */
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP_PREFIX})
/* loaded from: classes.dex */
public class DrawableContainer extends Drawable implements Drawable.Callback {
    private static final boolean DEBUG = false;
    private static final boolean DEFAULT_DITHER = true;
    private static final String TAG = "DrawableContainer";
    private Runnable mAnimationRunnable;
    private BlockInvalidateCallback mBlockInvalidateCallback;
    private Drawable mCurrDrawable;
    private DrawableContainerState mDrawableContainerState;
    private long mEnterAnimationEnd;
    private long mExitAnimationEnd;
    private boolean mHasAlpha;
    private Rect mHotspotBounds;
    private Drawable mLastDrawable;
    private boolean mMutated;
    private int mAlpha = 255;
    private int mCurIndex = -1;
    private int mLastIndex = -1;

    @Override // android.graphics.drawable.Drawable
    public void draw(@NonNull Canvas canvas) {
        if (this.mCurrDrawable != null) {
            this.mCurrDrawable.draw(canvas);
        }
        if (this.mLastDrawable != null) {
            this.mLastDrawable.draw(canvas);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public int getChangingConfigurations() {
        return this.mDrawableContainerState.getChangingConfigurations() | super.getChangingConfigurations();
    }

    private boolean needsMirroring() {
        return isAutoMirrored() && DrawableCompat.getLayoutDirection(this) == 1;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean getPadding(@NonNull Rect rect) {
        boolean padding;
        Rect constantPadding = this.mDrawableContainerState.getConstantPadding();
        if (constantPadding != null) {
            rect.set(constantPadding);
            padding = (constantPadding.right | ((constantPadding.left | constantPadding.top) | constantPadding.bottom)) != 0;
        } else if (this.mCurrDrawable != null) {
            padding = this.mCurrDrawable.getPadding(rect);
        } else {
            padding = super.getPadding(rect);
        }
        if (needsMirroring()) {
            int i = rect.left;
            rect.left = rect.right;
            rect.right = i;
        }
        return padding;
    }

    @Override // android.graphics.drawable.Drawable
    @RequiresApi(21)
    public void getOutline(@NonNull Outline outline) {
        if (this.mCurrDrawable != null) {
            this.mCurrDrawable.getOutline(outline);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        if (this.mHasAlpha && this.mAlpha == i) {
            return;
        }
        this.mHasAlpha = true;
        this.mAlpha = i;
        if (this.mCurrDrawable != null) {
            if (this.mEnterAnimationEnd == 0) {
                this.mCurrDrawable.setAlpha(i);
            } else {
                animate(false);
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return this.mAlpha;
    }

    @Override // android.graphics.drawable.Drawable
    public void setDither(boolean z) {
        if (this.mDrawableContainerState.mDither != z) {
            this.mDrawableContainerState.mDither = z;
            if (this.mCurrDrawable != null) {
                this.mCurrDrawable.setDither(this.mDrawableContainerState.mDither);
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.mDrawableContainerState.mHasColorFilter = true;
        if (this.mDrawableContainerState.mColorFilter != colorFilter) {
            this.mDrawableContainerState.mColorFilter = colorFilter;
            if (this.mCurrDrawable != null) {
                this.mCurrDrawable.setColorFilter(colorFilter);
            }
        }
    }

    @Override // android.graphics.drawable.Drawable, androidx.core.graphics.drawable.TintAwareDrawable
    public void setTintList(ColorStateList colorStateList) {
        this.mDrawableContainerState.mHasTintList = true;
        if (this.mDrawableContainerState.mTintList != colorStateList) {
            this.mDrawableContainerState.mTintList = colorStateList;
            DrawableCompat.setTintList(this.mCurrDrawable, colorStateList);
        }
    }

    @Override // android.graphics.drawable.Drawable, androidx.core.graphics.drawable.TintAwareDrawable
    public void setTintMode(@NonNull PorterDuff.Mode mode) {
        this.mDrawableContainerState.mHasTintMode = true;
        if (this.mDrawableContainerState.mTintMode != mode) {
            this.mDrawableContainerState.mTintMode = mode;
            DrawableCompat.setTintMode(this.mCurrDrawable, mode);
        }
    }

    public void setEnterFadeDuration(int i) {
        this.mDrawableContainerState.mEnterFadeDuration = i;
    }

    public void setExitFadeDuration(int i) {
        this.mDrawableContainerState.mExitFadeDuration = i;
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        if (this.mLastDrawable != null) {
            this.mLastDrawable.setBounds(rect);
        }
        if (this.mCurrDrawable != null) {
            this.mCurrDrawable.setBounds(rect);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        return this.mDrawableContainerState.isStateful();
    }

    @Override // android.graphics.drawable.Drawable
    public void setAutoMirrored(boolean z) {
        if (this.mDrawableContainerState.mAutoMirrored != z) {
            this.mDrawableContainerState.mAutoMirrored = z;
            if (this.mCurrDrawable != null) {
                DrawableCompat.setAutoMirrored(this.mCurrDrawable, this.mDrawableContainerState.mAutoMirrored);
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isAutoMirrored() {
        return this.mDrawableContainerState.mAutoMirrored;
    }

    @Override // android.graphics.drawable.Drawable
    public void jumpToCurrentState() {
        boolean z;
        if (this.mLastDrawable != null) {
            this.mLastDrawable.jumpToCurrentState();
            this.mLastDrawable = null;
            this.mLastIndex = -1;
            z = true;
        } else {
            z = false;
        }
        if (this.mCurrDrawable != null) {
            this.mCurrDrawable.jumpToCurrentState();
            if (this.mHasAlpha) {
                this.mCurrDrawable.setAlpha(this.mAlpha);
            }
        }
        if (this.mExitAnimationEnd != 0) {
            this.mExitAnimationEnd = 0L;
            z = true;
        }
        if (this.mEnterAnimationEnd != 0) {
            this.mEnterAnimationEnd = 0L;
            z = true;
        }
        if (z) {
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setHotspot(float f, float f2) {
        if (this.mCurrDrawable != null) {
            DrawableCompat.setHotspot(this.mCurrDrawable, f, f2);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setHotspotBounds(int i, int i2, int i3, int i4) {
        if (this.mHotspotBounds == null) {
            this.mHotspotBounds = new Rect(i, i2, i3, i4);
        } else {
            this.mHotspotBounds.set(i, i2, i3, i4);
        }
        if (this.mCurrDrawable != null) {
            DrawableCompat.setHotspotBounds(this.mCurrDrawable, i, i2, i3, i4);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void getHotspotBounds(@NonNull Rect rect) {
        if (this.mHotspotBounds != null) {
            rect.set(this.mHotspotBounds);
        } else {
            super.getHotspotBounds(rect);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.graphics.drawable.Drawable
    public boolean onStateChange(int[] iArr) {
        if (this.mLastDrawable != null) {
            return this.mLastDrawable.setState(iArr);
        }
        if (this.mCurrDrawable != null) {
            return this.mCurrDrawable.setState(iArr);
        }
        return false;
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onLevelChange(int i) {
        if (this.mLastDrawable != null) {
            return this.mLastDrawable.setLevel(i);
        }
        if (this.mCurrDrawable != null) {
            return this.mCurrDrawable.setLevel(i);
        }
        return false;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean onLayoutDirectionChanged(int i) {
        return this.mDrawableContainerState.setLayoutDirection(i, getCurrentIndex());
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantWidth();
        }
        if (this.mCurrDrawable != null) {
            return this.mCurrDrawable.getIntrinsicWidth();
        }
        return -1;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantHeight();
        }
        if (this.mCurrDrawable != null) {
            return this.mCurrDrawable.getIntrinsicHeight();
        }
        return -1;
    }

    @Override // android.graphics.drawable.Drawable
    public int getMinimumWidth() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantMinimumWidth();
        }
        if (this.mCurrDrawable != null) {
            return this.mCurrDrawable.getMinimumWidth();
        }
        return 0;
    }

    @Override // android.graphics.drawable.Drawable
    public int getMinimumHeight() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantMinimumHeight();
        }
        if (this.mCurrDrawable != null) {
            return this.mCurrDrawable.getMinimumHeight();
        }
        return 0;
    }

    public void invalidateDrawable(@NonNull Drawable drawable) {
        if (this.mDrawableContainerState != null) {
            this.mDrawableContainerState.invalidateCache();
        }
        if (drawable != this.mCurrDrawable || getCallback() == null) {
            return;
        }
        getCallback().invalidateDrawable(this);
    }

    public void scheduleDrawable(@NonNull Drawable drawable, @NonNull Runnable runnable, long j) {
        if (drawable != this.mCurrDrawable || getCallback() == null) {
            return;
        }
        getCallback().scheduleDrawable(this, runnable, j);
    }

    public void unscheduleDrawable(@NonNull Drawable drawable, @NonNull Runnable runnable) {
        if (drawable != this.mCurrDrawable || getCallback() == null) {
            return;
        }
        getCallback().unscheduleDrawable(this, runnable);
    }

    @Override // android.graphics.drawable.Drawable
    public boolean setVisible(boolean z, boolean z2) {
        boolean visible = super.setVisible(z, z2);
        if (this.mLastDrawable != null) {
            this.mLastDrawable.setVisible(z, z2);
        }
        if (this.mCurrDrawable != null) {
            this.mCurrDrawable.setVisible(z, z2);
        }
        return visible;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        if (this.mCurrDrawable == null || !this.mCurrDrawable.isVisible()) {
            return -2;
        }
        return this.mDrawableContainerState.getOpacity();
    }

    void setCurrentIndex(int i) {
        selectDrawable(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getCurrentIndex() {
        return this.mCurIndex;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean selectDrawable(int i) {
        if (i == this.mCurIndex) {
            return false;
        }
        long uptimeMillis = SystemClock.uptimeMillis();
        if (this.mDrawableContainerState.mExitFadeDuration > 0) {
            if (this.mLastDrawable != null) {
                this.mLastDrawable.setVisible(false, false);
            }
            if (this.mCurrDrawable != null) {
                this.mLastDrawable = this.mCurrDrawable;
                this.mLastIndex = this.mCurIndex;
                this.mExitAnimationEnd = this.mDrawableContainerState.mExitFadeDuration + uptimeMillis;
            } else {
                this.mLastDrawable = null;
                this.mLastIndex = -1;
                this.mExitAnimationEnd = 0L;
            }
        } else if (this.mCurrDrawable != null) {
            this.mCurrDrawable.setVisible(false, false);
        }
        if (i >= 0 && i < this.mDrawableContainerState.mNumChildren) {
            Drawable child = this.mDrawableContainerState.getChild(i);
            this.mCurrDrawable = child;
            this.mCurIndex = i;
            if (child != null) {
                if (this.mDrawableContainerState.mEnterFadeDuration > 0) {
                    this.mEnterAnimationEnd = uptimeMillis + this.mDrawableContainerState.mEnterFadeDuration;
                }
                initializeDrawableForDisplay(child);
            }
        } else {
            this.mCurrDrawable = null;
            this.mCurIndex = -1;
        }
        if (this.mEnterAnimationEnd != 0 || this.mExitAnimationEnd != 0) {
            if (this.mAnimationRunnable == null) {
                this.mAnimationRunnable = new Runnable() { // from class: androidx.appcompat.graphics.drawable.DrawableContainer.1
                    @Override // java.lang.Runnable
                    public void run() {
                        DrawableContainer.this.animate(true);
                        DrawableContainer.this.invalidateSelf();
                    }
                };
            } else {
                unscheduleSelf(this.mAnimationRunnable);
            }
            animate(true);
        }
        invalidateSelf();
        return true;
    }

    private void initializeDrawableForDisplay(Drawable drawable) {
        if (this.mBlockInvalidateCallback == null) {
            this.mBlockInvalidateCallback = new BlockInvalidateCallback();
        }
        drawable.setCallback(this.mBlockInvalidateCallback.wrap(drawable.getCallback()));
        try {
            if (this.mDrawableContainerState.mEnterFadeDuration <= 0 && this.mHasAlpha) {
                drawable.setAlpha(this.mAlpha);
            }
            if (this.mDrawableContainerState.mHasColorFilter) {
                drawable.setColorFilter(this.mDrawableContainerState.mColorFilter);
            } else {
                if (this.mDrawableContainerState.mHasTintList) {
                    DrawableCompat.setTintList(drawable, this.mDrawableContainerState.mTintList);
                }
                if (this.mDrawableContainerState.mHasTintMode) {
                    DrawableCompat.setTintMode(drawable, this.mDrawableContainerState.mTintMode);
                }
            }
            drawable.setVisible(isVisible(), true);
            drawable.setDither(this.mDrawableContainerState.mDither);
            drawable.setState(getState());
            drawable.setLevel(getLevel());
            drawable.setBounds(getBounds());
            if (Build.VERSION.SDK_INT >= 23) {
                drawable.setLayoutDirection(getLayoutDirection());
            }
            if (Build.VERSION.SDK_INT >= 19) {
                drawable.setAutoMirrored(this.mDrawableContainerState.mAutoMirrored);
            }
            Rect rect = this.mHotspotBounds;
            if (Build.VERSION.SDK_INT >= 21 && rect != null) {
                drawable.setHotspotBounds(rect.left, rect.top, rect.right, rect.bottom);
            }
        } finally {
            drawable.setCallback(this.mBlockInvalidateCallback.unwrap());
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0045  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0074  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0079 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:26:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void animate(boolean r12) {
        /*
            r11 = this;
            r0 = 1
            r11.mHasAlpha = r0
            long r1 = android.os.SystemClock.uptimeMillis()
            android.graphics.drawable.Drawable r3 = r11.mCurrDrawable
            r4 = 255(0xff, double:1.26E-321)
            r6 = 0
            r7 = 0
            if (r3 == 0) goto L3e
            long r9 = r11.mEnterAnimationEnd
            int r3 = (r9 > r7 ? 1 : (r9 == r7 ? 0 : -1))
            if (r3 == 0) goto L40
            long r9 = r11.mEnterAnimationEnd
            int r3 = (r9 > r1 ? 1 : (r9 == r1 ? 0 : -1))
            if (r3 > 0) goto L26
            android.graphics.drawable.Drawable r3 = r11.mCurrDrawable
            int r9 = r11.mAlpha
            r3.setAlpha(r9)
            r11.mEnterAnimationEnd = r7
            goto L40
        L26:
            long r9 = r11.mEnterAnimationEnd
            long r9 = r9 - r1
            long r9 = r9 * r4
            int r3 = (int) r9
            androidx.appcompat.graphics.drawable.DrawableContainer$DrawableContainerState r9 = r11.mDrawableContainerState
            int r9 = r9.mEnterFadeDuration
            int r3 = r3 / r9
            android.graphics.drawable.Drawable r9 = r11.mCurrDrawable
            int r3 = 255 - r3
            int r10 = r11.mAlpha
            int r3 = r3 * r10
            int r3 = r3 / 255
            r9.setAlpha(r3)
            r3 = r0
            goto L41
        L3e:
            r11.mEnterAnimationEnd = r7
        L40:
            r3 = r6
        L41:
            android.graphics.drawable.Drawable r9 = r11.mLastDrawable
            if (r9 == 0) goto L74
            long r9 = r11.mExitAnimationEnd
            int r9 = (r9 > r7 ? 1 : (r9 == r7 ? 0 : -1))
            if (r9 == 0) goto L76
            long r9 = r11.mExitAnimationEnd
            int r9 = (r9 > r1 ? 1 : (r9 == r1 ? 0 : -1))
            if (r9 > 0) goto L5f
            android.graphics.drawable.Drawable r0 = r11.mLastDrawable
            r0.setVisible(r6, r6)
            r0 = 0
            r11.mLastDrawable = r0
            r0 = -1
            r11.mLastIndex = r0
            r11.mExitAnimationEnd = r7
            goto L76
        L5f:
            long r6 = r11.mExitAnimationEnd
            long r6 = r6 - r1
            long r6 = r6 * r4
            int r3 = (int) r6
            androidx.appcompat.graphics.drawable.DrawableContainer$DrawableContainerState r4 = r11.mDrawableContainerState
            int r4 = r4.mExitFadeDuration
            int r3 = r3 / r4
            android.graphics.drawable.Drawable r4 = r11.mLastDrawable
            int r5 = r11.mAlpha
            int r3 = r3 * r5
            int r3 = r3 / 255
            r4.setAlpha(r3)
            goto L77
        L74:
            r11.mExitAnimationEnd = r7
        L76:
            r0 = r3
        L77:
            if (r12 == 0) goto L83
            if (r0 == 0) goto L83
            java.lang.Runnable r12 = r11.mAnimationRunnable
            r3 = 16
            long r1 = r1 + r3
            r11.scheduleSelf(r12, r1)
        L83:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.graphics.drawable.DrawableContainer.animate(boolean):void");
    }

    @Override // android.graphics.drawable.Drawable
    @NonNull
    public Drawable getCurrent() {
        return this.mCurrDrawable;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void updateDensity(Resources resources) {
        this.mDrawableContainerState.updateDensity(resources);
    }

    @Override // android.graphics.drawable.Drawable
    @RequiresApi(21)
    public void applyTheme(@NonNull Resources.Theme theme) {
        this.mDrawableContainerState.applyTheme(theme);
    }

    @Override // android.graphics.drawable.Drawable
    @RequiresApi(21)
    public boolean canApplyTheme() {
        return this.mDrawableContainerState.canApplyTheme();
    }

    @Override // android.graphics.drawable.Drawable
    public final Drawable.ConstantState getConstantState() {
        if (this.mDrawableContainerState.canConstantState()) {
            this.mDrawableContainerState.mChangingConfigurations = getChangingConfigurations();
            return this.mDrawableContainerState;
        }
        return null;
    }

    @Override // android.graphics.drawable.Drawable
    @NonNull
    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            DrawableContainerState cloneConstantState = cloneConstantState();
            cloneConstantState.mutate();
            setConstantState(cloneConstantState);
            this.mMutated = true;
        }
        return this;
    }

    DrawableContainerState cloneConstantState() {
        return this.mDrawableContainerState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearMutated() {
        this.mDrawableContainerState.clearMutated();
        this.mMutated = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static abstract class DrawableContainerState extends Drawable.ConstantState {
        boolean mAutoMirrored;
        boolean mCanConstantState;
        int mChangingConfigurations;
        boolean mCheckedConstantSize;
        boolean mCheckedConstantState;
        boolean mCheckedOpacity;
        boolean mCheckedPadding;
        boolean mCheckedStateful;
        int mChildrenChangingConfigurations;
        ColorFilter mColorFilter;
        int mConstantHeight;
        int mConstantMinimumHeight;
        int mConstantMinimumWidth;
        Rect mConstantPadding;
        boolean mConstantSize;
        int mConstantWidth;
        int mDensity;
        boolean mDither;
        SparseArray<Drawable.ConstantState> mDrawableFutures;
        Drawable[] mDrawables;
        int mEnterFadeDuration;
        int mExitFadeDuration;
        boolean mHasColorFilter;
        boolean mHasTintList;
        boolean mHasTintMode;
        int mLayoutDirection;
        boolean mMutated;
        int mNumChildren;
        int mOpacity;
        final DrawableContainer mOwner;
        Resources mSourceRes;
        boolean mStateful;
        ColorStateList mTintList;
        PorterDuff.Mode mTintMode;
        boolean mVariablePadding;

        /* JADX INFO: Access modifiers changed from: package-private */
        public DrawableContainerState(DrawableContainerState drawableContainerState, DrawableContainer drawableContainer, Resources resources) {
            Resources resources2;
            this.mDensity = 160;
            this.mVariablePadding = false;
            this.mConstantSize = false;
            this.mDither = true;
            this.mEnterFadeDuration = 0;
            this.mExitFadeDuration = 0;
            this.mOwner = drawableContainer;
            if (resources != null) {
                resources2 = resources;
            } else {
                resources2 = drawableContainerState != null ? drawableContainerState.mSourceRes : null;
            }
            this.mSourceRes = resources2;
            this.mDensity = DrawableContainer.resolveDensity(resources, drawableContainerState != null ? drawableContainerState.mDensity : 0);
            if (drawableContainerState != null) {
                this.mChangingConfigurations = drawableContainerState.mChangingConfigurations;
                this.mChildrenChangingConfigurations = drawableContainerState.mChildrenChangingConfigurations;
                this.mCheckedConstantState = true;
                this.mCanConstantState = true;
                this.mVariablePadding = drawableContainerState.mVariablePadding;
                this.mConstantSize = drawableContainerState.mConstantSize;
                this.mDither = drawableContainerState.mDither;
                this.mMutated = drawableContainerState.mMutated;
                this.mLayoutDirection = drawableContainerState.mLayoutDirection;
                this.mEnterFadeDuration = drawableContainerState.mEnterFadeDuration;
                this.mExitFadeDuration = drawableContainerState.mExitFadeDuration;
                this.mAutoMirrored = drawableContainerState.mAutoMirrored;
                this.mColorFilter = drawableContainerState.mColorFilter;
                this.mHasColorFilter = drawableContainerState.mHasColorFilter;
                this.mTintList = drawableContainerState.mTintList;
                this.mTintMode = drawableContainerState.mTintMode;
                this.mHasTintList = drawableContainerState.mHasTintList;
                this.mHasTintMode = drawableContainerState.mHasTintMode;
                if (drawableContainerState.mDensity == this.mDensity) {
                    if (drawableContainerState.mCheckedPadding) {
                        this.mConstantPadding = new Rect(drawableContainerState.mConstantPadding);
                        this.mCheckedPadding = true;
                    }
                    if (drawableContainerState.mCheckedConstantSize) {
                        this.mConstantWidth = drawableContainerState.mConstantWidth;
                        this.mConstantHeight = drawableContainerState.mConstantHeight;
                        this.mConstantMinimumWidth = drawableContainerState.mConstantMinimumWidth;
                        this.mConstantMinimumHeight = drawableContainerState.mConstantMinimumHeight;
                        this.mCheckedConstantSize = true;
                    }
                }
                if (drawableContainerState.mCheckedOpacity) {
                    this.mOpacity = drawableContainerState.mOpacity;
                    this.mCheckedOpacity = true;
                }
                if (drawableContainerState.mCheckedStateful) {
                    this.mStateful = drawableContainerState.mStateful;
                    this.mCheckedStateful = true;
                }
                Drawable[] drawableArr = drawableContainerState.mDrawables;
                this.mDrawables = new Drawable[drawableArr.length];
                this.mNumChildren = drawableContainerState.mNumChildren;
                SparseArray<Drawable.ConstantState> sparseArray = drawableContainerState.mDrawableFutures;
                if (sparseArray != null) {
                    this.mDrawableFutures = sparseArray.clone();
                } else {
                    this.mDrawableFutures = new SparseArray<>(this.mNumChildren);
                }
                int i = this.mNumChildren;
                for (int i2 = 0; i2 < i; i2++) {
                    if (drawableArr[i2] != null) {
                        Drawable.ConstantState constantState = drawableArr[i2].getConstantState();
                        if (constantState != null) {
                            this.mDrawableFutures.put(i2, constantState);
                        } else {
                            this.mDrawables[i2] = drawableArr[i2];
                        }
                    }
                }
                return;
            }
            this.mDrawables = new Drawable[10];
            this.mNumChildren = 0;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return this.mChildrenChangingConfigurations | this.mChangingConfigurations;
        }

        public final int addChild(Drawable drawable) {
            int i = this.mNumChildren;
            if (i >= this.mDrawables.length) {
                growArray(i, i + 10);
            }
            drawable.mutate();
            drawable.setVisible(false, true);
            drawable.setCallback(this.mOwner);
            this.mDrawables[i] = drawable;
            this.mNumChildren++;
            this.mChildrenChangingConfigurations = drawable.getChangingConfigurations() | this.mChildrenChangingConfigurations;
            invalidateCache();
            this.mConstantPadding = null;
            this.mCheckedPadding = false;
            this.mCheckedConstantSize = false;
            this.mCheckedConstantState = false;
            return i;
        }

        void invalidateCache() {
            this.mCheckedOpacity = false;
            this.mCheckedStateful = false;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public final int getCapacity() {
            return this.mDrawables.length;
        }

        private void createAllFutures() {
            if (this.mDrawableFutures != null) {
                int size = this.mDrawableFutures.size();
                for (int i = 0; i < size; i++) {
                    this.mDrawables[this.mDrawableFutures.keyAt(i)] = prepareDrawable(this.mDrawableFutures.valueAt(i).newDrawable(this.mSourceRes));
                }
                this.mDrawableFutures = null;
            }
        }

        private Drawable prepareDrawable(Drawable drawable) {
            if (Build.VERSION.SDK_INT >= 23) {
                drawable.setLayoutDirection(this.mLayoutDirection);
            }
            Drawable mutate = drawable.mutate();
            mutate.setCallback(this.mOwner);
            return mutate;
        }

        public final int getChildCount() {
            return this.mNumChildren;
        }

        public final Drawable getChild(int i) {
            int indexOfKey;
            Drawable drawable = this.mDrawables[i];
            if (drawable != null) {
                return drawable;
            }
            if (this.mDrawableFutures == null || (indexOfKey = this.mDrawableFutures.indexOfKey(i)) < 0) {
                return null;
            }
            Drawable prepareDrawable = prepareDrawable(this.mDrawableFutures.valueAt(indexOfKey).newDrawable(this.mSourceRes));
            this.mDrawables[i] = prepareDrawable;
            this.mDrawableFutures.removeAt(indexOfKey);
            if (this.mDrawableFutures.size() == 0) {
                this.mDrawableFutures = null;
            }
            return prepareDrawable;
        }

        final boolean setLayoutDirection(int i, int i2) {
            int i3 = this.mNumChildren;
            Drawable[] drawableArr = this.mDrawables;
            boolean z = false;
            for (int i4 = 0; i4 < i3; i4++) {
                if (drawableArr[i4] != null) {
                    boolean layoutDirection = Build.VERSION.SDK_INT >= 23 ? drawableArr[i4].setLayoutDirection(i) : false;
                    if (i4 == i2) {
                        z = layoutDirection;
                    }
                }
            }
            this.mLayoutDirection = i;
            return z;
        }

        final void updateDensity(Resources resources) {
            if (resources != null) {
                this.mSourceRes = resources;
                int resolveDensity = DrawableContainer.resolveDensity(resources, this.mDensity);
                int i = this.mDensity;
                this.mDensity = resolveDensity;
                if (i != resolveDensity) {
                    this.mCheckedConstantSize = false;
                    this.mCheckedPadding = false;
                }
            }
        }

        @RequiresApi(21)
        final void applyTheme(Resources.Theme theme) {
            if (theme != null) {
                createAllFutures();
                int i = this.mNumChildren;
                Drawable[] drawableArr = this.mDrawables;
                for (int i2 = 0; i2 < i; i2++) {
                    if (drawableArr[i2] != null && drawableArr[i2].canApplyTheme()) {
                        drawableArr[i2].applyTheme(theme);
                        this.mChildrenChangingConfigurations |= drawableArr[i2].getChangingConfigurations();
                    }
                }
                updateDensity(theme.getResources());
            }
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        @RequiresApi(21)
        public boolean canApplyTheme() {
            int i = this.mNumChildren;
            Drawable[] drawableArr = this.mDrawables;
            for (int i2 = 0; i2 < i; i2++) {
                Drawable drawable = drawableArr[i2];
                if (drawable != null) {
                    if (drawable.canApplyTheme()) {
                        return true;
                    }
                } else {
                    Drawable.ConstantState constantState = this.mDrawableFutures.get(i2);
                    if (constantState != null && constantState.canApplyTheme()) {
                        return true;
                    }
                }
            }
            return false;
        }

        void mutate() {
            int i = this.mNumChildren;
            Drawable[] drawableArr = this.mDrawables;
            for (int i2 = 0; i2 < i; i2++) {
                if (drawableArr[i2] != null) {
                    drawableArr[i2].mutate();
                }
            }
            this.mMutated = true;
        }

        final void clearMutated() {
            this.mMutated = false;
        }

        public final void setVariablePadding(boolean z) {
            this.mVariablePadding = z;
        }

        public final Rect getConstantPadding() {
            if (this.mVariablePadding) {
                return null;
            }
            if (this.mConstantPadding != null || this.mCheckedPadding) {
                return this.mConstantPadding;
            }
            createAllFutures();
            Rect rect = new Rect();
            int i = this.mNumChildren;
            Drawable[] drawableArr = this.mDrawables;
            Rect rect2 = null;
            for (int i2 = 0; i2 < i; i2++) {
                if (drawableArr[i2].getPadding(rect)) {
                    if (rect2 == null) {
                        rect2 = new Rect(0, 0, 0, 0);
                    }
                    if (rect.left > rect2.left) {
                        rect2.left = rect.left;
                    }
                    if (rect.top > rect2.top) {
                        rect2.top = rect.top;
                    }
                    if (rect.right > rect2.right) {
                        rect2.right = rect.right;
                    }
                    if (rect.bottom > rect2.bottom) {
                        rect2.bottom = rect.bottom;
                    }
                }
            }
            this.mCheckedPadding = true;
            this.mConstantPadding = rect2;
            return rect2;
        }

        public final void setConstantSize(boolean z) {
            this.mConstantSize = z;
        }

        public final boolean isConstantSize() {
            return this.mConstantSize;
        }

        public final int getConstantWidth() {
            if (!this.mCheckedConstantSize) {
                computeConstantSize();
            }
            return this.mConstantWidth;
        }

        public final int getConstantHeight() {
            if (!this.mCheckedConstantSize) {
                computeConstantSize();
            }
            return this.mConstantHeight;
        }

        public final int getConstantMinimumWidth() {
            if (!this.mCheckedConstantSize) {
                computeConstantSize();
            }
            return this.mConstantMinimumWidth;
        }

        public final int getConstantMinimumHeight() {
            if (!this.mCheckedConstantSize) {
                computeConstantSize();
            }
            return this.mConstantMinimumHeight;
        }

        protected void computeConstantSize() {
            this.mCheckedConstantSize = true;
            createAllFutures();
            int i = this.mNumChildren;
            Drawable[] drawableArr = this.mDrawables;
            this.mConstantHeight = -1;
            this.mConstantWidth = -1;
            this.mConstantMinimumHeight = 0;
            this.mConstantMinimumWidth = 0;
            for (int i2 = 0; i2 < i; i2++) {
                Drawable drawable = drawableArr[i2];
                int intrinsicWidth = drawable.getIntrinsicWidth();
                if (intrinsicWidth > this.mConstantWidth) {
                    this.mConstantWidth = intrinsicWidth;
                }
                int intrinsicHeight = drawable.getIntrinsicHeight();
                if (intrinsicHeight > this.mConstantHeight) {
                    this.mConstantHeight = intrinsicHeight;
                }
                int minimumWidth = drawable.getMinimumWidth();
                if (minimumWidth > this.mConstantMinimumWidth) {
                    this.mConstantMinimumWidth = minimumWidth;
                }
                int minimumHeight = drawable.getMinimumHeight();
                if (minimumHeight > this.mConstantMinimumHeight) {
                    this.mConstantMinimumHeight = minimumHeight;
                }
            }
        }

        public final void setEnterFadeDuration(int i) {
            this.mEnterFadeDuration = i;
        }

        public final int getEnterFadeDuration() {
            return this.mEnterFadeDuration;
        }

        public final void setExitFadeDuration(int i) {
            this.mExitFadeDuration = i;
        }

        public final int getExitFadeDuration() {
            return this.mExitFadeDuration;
        }

        public final int getOpacity() {
            if (this.mCheckedOpacity) {
                return this.mOpacity;
            }
            createAllFutures();
            int i = this.mNumChildren;
            Drawable[] drawableArr = this.mDrawables;
            int opacity = i > 0 ? drawableArr[0].getOpacity() : -2;
            for (int i2 = 1; i2 < i; i2++) {
                opacity = Drawable.resolveOpacity(opacity, drawableArr[i2].getOpacity());
            }
            this.mOpacity = opacity;
            this.mCheckedOpacity = true;
            return opacity;
        }

        public final boolean isStateful() {
            if (this.mCheckedStateful) {
                return this.mStateful;
            }
            createAllFutures();
            int i = this.mNumChildren;
            Drawable[] drawableArr = this.mDrawables;
            boolean z = false;
            int i2 = 0;
            while (true) {
                if (i2 >= i) {
                    break;
                } else if (drawableArr[i2].isStateful()) {
                    z = true;
                    break;
                } else {
                    i2++;
                }
            }
            this.mStateful = z;
            this.mCheckedStateful = true;
            return z;
        }

        public void growArray(int i, int i2) {
            Drawable[] drawableArr = new Drawable[i2];
            System.arraycopy(this.mDrawables, 0, drawableArr, 0, i);
            this.mDrawables = drawableArr;
        }

        public synchronized boolean canConstantState() {
            if (this.mCheckedConstantState) {
                return this.mCanConstantState;
            }
            createAllFutures();
            this.mCheckedConstantState = true;
            int i = this.mNumChildren;
            Drawable[] drawableArr = this.mDrawables;
            for (int i2 = 0; i2 < i; i2++) {
                if (drawableArr[i2].getConstantState() == null) {
                    this.mCanConstantState = false;
                    return false;
                }
            }
            this.mCanConstantState = true;
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setConstantState(DrawableContainerState drawableContainerState) {
        this.mDrawableContainerState = drawableContainerState;
        if (this.mCurIndex >= 0) {
            this.mCurrDrawable = drawableContainerState.getChild(this.mCurIndex);
            if (this.mCurrDrawable != null) {
                initializeDrawableForDisplay(this.mCurrDrawable);
            }
        }
        this.mLastIndex = -1;
        this.mLastDrawable = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class BlockInvalidateCallback implements Drawable.Callback {
        private Drawable.Callback mCallback;

        @Override // android.graphics.drawable.Drawable.Callback
        public void invalidateDrawable(@NonNull Drawable drawable) {
        }

        BlockInvalidateCallback() {
        }

        public BlockInvalidateCallback wrap(Drawable.Callback callback) {
            this.mCallback = callback;
            return this;
        }

        public Drawable.Callback unwrap() {
            Drawable.Callback callback = this.mCallback;
            this.mCallback = null;
            return callback;
        }

        @Override // android.graphics.drawable.Drawable.Callback
        public void scheduleDrawable(@NonNull Drawable drawable, @NonNull Runnable runnable, long j) {
            if (this.mCallback != null) {
                this.mCallback.scheduleDrawable(drawable, runnable, j);
            }
        }

        @Override // android.graphics.drawable.Drawable.Callback
        public void unscheduleDrawable(@NonNull Drawable drawable, @NonNull Runnable runnable) {
            if (this.mCallback != null) {
                this.mCallback.unscheduleDrawable(drawable, runnable);
            }
        }
    }

    static int resolveDensity(@Nullable Resources resources, int i) {
        if (resources != null) {
            i = resources.getDisplayMetrics().densityDpi;
        }
        if (i == 0) {
            return 160;
        }
        return i;
    }
}
