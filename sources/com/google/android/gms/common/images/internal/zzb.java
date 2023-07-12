package com.google.android.gms.common.images.internal;

import android.graphics.drawable.Drawable;
import com.google.android.gms.common.images.internal.CrossFadingDrawable;

/* loaded from: classes.dex */
final class zzb extends Drawable.ConstantState {
    private zzb() {
    }

    @Override // android.graphics.drawable.Drawable.ConstantState
    public final int getChangingConfigurations() {
        return 0;
    }

    @Override // android.graphics.drawable.Drawable.ConstantState
    public final Drawable newDrawable() {
        return CrossFadingDrawable.zza.zzqj;
    }
}
