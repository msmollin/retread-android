package com.polidea.rxandroidble2.internal.connection;

import androidx.annotation.RestrictTo;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* loaded from: classes.dex */
class ConstantPayloadSizeLimit implements PayloadSizeLimitProvider {
    private final int limit;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConstantPayloadSizeLimit(int i) {
        this.limit = i;
    }

    @Override // com.polidea.rxandroidble2.internal.connection.PayloadSizeLimitProvider
    public int getPayloadSizeLimit() {
        return this.limit;
    }
}
