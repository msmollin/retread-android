package com.google.android.gms.common.server.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public abstract class FastMapJsonResponse extends FastJsonResponse {
    private final HashMap<String, Object> zzwp = new HashMap<>();

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    public Object getValueObject(String str) {
        return this.zzwp.get(str);
    }

    public HashMap<String, Object> getValues() {
        return this.zzwp;
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    protected boolean isPrimitiveFieldSet(String str) {
        return this.zzwp.containsKey(str);
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    public void setBigDecimal(String str, BigDecimal bigDecimal) {
        this.zzwp.put(str, bigDecimal);
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    public void setBigDecimals(String str, ArrayList<BigDecimal> arrayList) {
        this.zzwp.put(str, arrayList);
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    public void setBigInteger(String str, BigInteger bigInteger) {
        this.zzwp.put(str, bigInteger);
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    public void setBigIntegers(String str, ArrayList<BigInteger> arrayList) {
        this.zzwp.put(str, arrayList);
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    public void setBoolean(String str, boolean z) {
        this.zzwp.put(str, Boolean.valueOf(z));
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    public void setBooleans(String str, ArrayList<Boolean> arrayList) {
        this.zzwp.put(str, arrayList);
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    public void setDecodedBytes(String str, byte[] bArr) {
        this.zzwp.put(str, bArr);
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    public void setDouble(String str, double d) {
        this.zzwp.put(str, Double.valueOf(d));
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    public void setDoubles(String str, ArrayList<Double> arrayList) {
        this.zzwp.put(str, arrayList);
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    protected void setFloat(String str, float f) {
        this.zzwp.put(str, Float.valueOf(f));
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    protected void setFloats(String str, ArrayList<Float> arrayList) {
        this.zzwp.put(str, arrayList);
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    public void setInteger(String str, int i) {
        this.zzwp.put(str, Integer.valueOf(i));
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    public void setIntegers(String str, ArrayList<Integer> arrayList) {
        this.zzwp.put(str, arrayList);
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    public void setLong(String str, long j) {
        this.zzwp.put(str, Long.valueOf(j));
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    public void setLongs(String str, ArrayList<Long> arrayList) {
        this.zzwp.put(str, arrayList);
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    public void setString(String str, String str2) {
        this.zzwp.put(str, str2);
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    public void setStringMap(String str, Map<String, String> map) {
        this.zzwp.put(str, map);
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    public void setStrings(String str, ArrayList<String> arrayList) {
        this.zzwp.put(str, arrayList);
    }
}
