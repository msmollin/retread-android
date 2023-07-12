package com.google.android.gms.common.api;

import androidx.annotation.NonNull;
import com.google.android.gms.common.api.Result;

/* loaded from: classes.dex */
public class Response<T extends Result> {
    private T zzdm;

    public Response() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Response(@NonNull T t) {
        this.zzdm = t;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @NonNull
    public T getResult() {
        return this.zzdm;
    }

    public void setResult(@NonNull T t) {
        this.zzdm = t;
    }
}
