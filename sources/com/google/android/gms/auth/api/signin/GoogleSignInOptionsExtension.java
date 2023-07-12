package com.google.android.gms.auth.api.signin;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.gms.common.api.Scope;
import java.util.List;

/* loaded from: classes.dex */
public interface GoogleSignInOptionsExtension {
    public static final int FALLBACK_SIGN_IN = 2;
    public static final int FITNESS = 3;
    public static final int GAMES = 1;

    /* loaded from: classes.dex */
    public @interface TypeId {
    }

    @TypeId
    int getExtensionType();

    @Nullable
    List<Scope> getImpliedScopes();

    Bundle toBundle();
}
