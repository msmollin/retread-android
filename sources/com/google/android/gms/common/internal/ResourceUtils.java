package com.google.android.gms.common.internal;

import android.net.Uri;
import com.facebook.common.util.UriUtil;

/* loaded from: classes.dex */
public final class ResourceUtils {
    private static final Uri zzuw = new Uri.Builder().scheme(UriUtil.QUALIFIED_RESOURCE_SCHEME).authority("com.google.android.gms").appendPath("drawable").build();

    @Deprecated
    /* loaded from: classes.dex */
    public interface SignInResources {
        public static final String BUTTON_DARK_TEXT_DEFAULT = "common_google_signin_btn_text_dark_normal";
    }

    private ResourceUtils() {
    }

    public static Uri getDrawableUri(String str) {
        Preconditions.checkNotNull(str, "Resource name must not be null.");
        return zzuw.buildUpon().appendPath(str).build();
    }
}
