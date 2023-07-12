package com.google.android.gms.common.server.response;

import android.content.ContentValues;
import com.google.android.gms.common.util.VisibleForTesting;

/* loaded from: classes.dex */
public abstract class FastContentValuesJsonResponse extends FastJsonResponse {
    private final ContentValues zzwj;

    public FastContentValuesJsonResponse() {
        this.zzwj = new ContentValues();
    }

    @VisibleForTesting
    public FastContentValuesJsonResponse(ContentValues contentValues) {
        this.zzwj = contentValues;
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    protected Object getValueObject(String str) {
        return this.zzwj.get(str);
    }

    public ContentValues getValues() {
        return this.zzwj;
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    protected boolean isPrimitiveFieldSet(String str) {
        return this.zzwj.containsKey(str);
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    protected void setBoolean(String str, boolean z) {
        this.zzwj.put(str, Boolean.valueOf(z));
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    protected void setDecodedBytes(String str, byte[] bArr) {
        this.zzwj.put(str, bArr);
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    protected void setDouble(String str, double d) {
        this.zzwj.put(str, Double.valueOf(d));
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    protected void setFloat(String str, float f) {
        this.zzwj.put(str, Float.valueOf(f));
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    protected void setInteger(String str, int i) {
        this.zzwj.put(str, Integer.valueOf(i));
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    protected void setLong(String str, long j) {
        this.zzwj.put(str, Long.valueOf(j));
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    protected void setString(String str, String str2) {
        this.zzwj.put(str, str2);
    }
}
