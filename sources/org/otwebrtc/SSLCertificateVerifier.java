package org.otwebrtc;

/* loaded from: classes2.dex */
public interface SSLCertificateVerifier {
    @CalledByNative
    boolean verify(byte[] bArr);
}
