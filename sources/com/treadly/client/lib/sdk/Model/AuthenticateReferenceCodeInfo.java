package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public class AuthenticateReferenceCodeInfo {
    public String deviceAddress;
    public String message;
    public String referenceCode;
    public String title;

    public AuthenticateReferenceCodeInfo() {
    }

    public AuthenticateReferenceCodeInfo(String str, String str2, String str3, String str4) {
        this.deviceAddress = str;
        this.referenceCode = str2;
        this.title = str3;
        this.message = str4;
    }
}
