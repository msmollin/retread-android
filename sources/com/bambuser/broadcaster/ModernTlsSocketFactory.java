package com.bambuser.broadcaster;

import android.os.Build;
import android.util.Log;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/* loaded from: classes.dex */
public final class ModernTlsSocketFactory extends SSLSocketFactory {
    private static final String LOGTAG = "ModernTlsSocketFactory";
    private final SSLSocketFactory internalFactory;

    private ModernTlsSocketFactory() throws KeyManagementException, NoSuchAlgorithmException {
        if (Build.VERSION.SDK_INT < 21) {
            SSLContext sSLContext = SSLContext.getInstance("TLSv1.2");
            sSLContext.init(null, null, null);
            this.internalFactory = sSLContext.getSocketFactory();
            return;
        }
        this.internalFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
    }

    public static ModernTlsSocketFactory getInstance() {
        try {
            return new ModernTlsSocketFactory();
        } catch (Exception e) {
            Log.w(LOGTAG, "Exception when enforcing TLSv1.2: " + e);
            return null;
        }
    }

    public static X509TrustManager getTrustManager() {
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            return (X509TrustManager) trustManagers[0];
        } catch (Exception e) {
            Log.w(LOGTAG, "Exception when getting trust manager: " + e);
            return null;
        }
    }

    @Override // javax.net.ssl.SSLSocketFactory
    public String[] getDefaultCipherSuites() {
        return this.internalFactory.getDefaultCipherSuites();
    }

    @Override // javax.net.ssl.SSLSocketFactory
    public String[] getSupportedCipherSuites() {
        return this.internalFactory.getSupportedCipherSuites();
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket() throws IOException {
        return configureSocket(this.internalFactory.createSocket());
    }

    @Override // javax.net.ssl.SSLSocketFactory
    public Socket createSocket(Socket socket, String str, int i, boolean z) throws IOException {
        return configureSocket(this.internalFactory.createSocket(socket, str, i, z));
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(String str, int i) throws IOException {
        return configureSocket(this.internalFactory.createSocket(str, i));
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(String str, int i, InetAddress inetAddress, int i2) throws IOException, UnknownHostException {
        return configureSocket(this.internalFactory.createSocket(str, i, inetAddress, i2));
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
        return configureSocket(this.internalFactory.createSocket(inetAddress, i));
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2) throws IOException {
        return configureSocket(this.internalFactory.createSocket(inetAddress, i, inetAddress2, i2));
    }

    private Socket configureSocket(Socket socket) {
        if (socket instanceof SSLSocket) {
            SSLSocket sSLSocket = (SSLSocket) socket;
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(Arrays.asList(sSLSocket.getEnabledCipherSuites()));
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                if (str.startsWith("SSL") || str.contains("RC4") || str.contains("MD5") || str.contains("EXPORT")) {
                    it.remove();
                }
            }
            sSLSocket.setEnabledCipherSuites((String[]) arrayList.toArray(new String[arrayList.size()]));
            ArrayList arrayList2 = new ArrayList();
            arrayList2.addAll(Arrays.asList(sSLSocket.getSupportedProtocols()));
            Iterator it2 = arrayList2.iterator();
            while (it2.hasNext()) {
                String str2 = (String) it2.next();
                if (str2.startsWith("SSL") || str2.equals("TLSv1") || str2.equals("TLSv1.1")) {
                    it2.remove();
                }
            }
            sSLSocket.setEnabledProtocols((String[]) arrayList2.toArray(new String[arrayList2.size()]));
            return sSLSocket;
        }
        return socket;
    }
}
