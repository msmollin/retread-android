package com.google.firebase.internal;

import androidx.annotation.Nullable;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.GetTokenResult;

@KeepForSdk
/* loaded from: classes.dex */
public interface InternalTokenProvider {
    @KeepForSdk
    Task<GetTokenResult> getAccessToken(boolean z);

    @Nullable
    @KeepForSdk
    String getUid();
}
