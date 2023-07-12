package com.google.firebase.auth;

import androidx.annotation.NonNull;
import com.google.android.gms.common.internal.Preconditions;
import com.google.firebase.FirebaseException;

/* loaded from: classes.dex */
public class FirebaseAuthException extends FirebaseException {
    private final String zzy;

    public FirebaseAuthException(@NonNull String str, @NonNull String str2) {
        super(str2);
        this.zzy = Preconditions.checkNotEmpty(str);
    }

    @NonNull
    public String getErrorCode() {
        return this.zzy;
    }
}
