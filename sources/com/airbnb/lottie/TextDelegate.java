package com.airbnb.lottie;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class TextDelegate {
    @Nullable
    private final LottieAnimationView animationView;
    private boolean cacheText;
    @Nullable
    private final LottieDrawable drawable;
    private final Map<String, String> stringMap;

    private String getText(String str) {
        return str;
    }

    @VisibleForTesting
    TextDelegate() {
        this.stringMap = new HashMap();
        this.cacheText = true;
        this.animationView = null;
        this.drawable = null;
    }

    public TextDelegate(LottieAnimationView lottieAnimationView) {
        this.stringMap = new HashMap();
        this.cacheText = true;
        this.animationView = lottieAnimationView;
        this.drawable = null;
    }

    public TextDelegate(LottieDrawable lottieDrawable) {
        this.stringMap = new HashMap();
        this.cacheText = true;
        this.drawable = lottieDrawable;
        this.animationView = null;
    }

    public void setText(String str, String str2) {
        this.stringMap.put(str, str2);
        invalidate();
    }

    public void setCacheText(boolean z) {
        this.cacheText = z;
    }

    public void invalidateText(String str) {
        this.stringMap.remove(str);
        invalidate();
    }

    public void invalidateAllText() {
        this.stringMap.clear();
        invalidate();
    }

    public final String getTextInternal(String str) {
        if (this.cacheText && this.stringMap.containsKey(str)) {
            return this.stringMap.get(str);
        }
        String text = getText(str);
        if (this.cacheText) {
            this.stringMap.put(str, text);
        }
        return text;
    }

    private void invalidate() {
        if (this.animationView != null) {
            this.animationView.invalidate();
        }
        if (this.drawable != null) {
            this.drawable.invalidateSelf();
        }
    }
}
