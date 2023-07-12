package com.google.android.gms.common.net;

import android.content.Context;
import android.util.Log;
import com.google.android.gms.common.util.VisibleForTesting;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.security.PrivateKey;
import javax.net.SocketFactory;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/* loaded from: classes.dex */
public class SSLCertificateSocketFactory extends SSLSocketFactory {
    private static final TrustManager[] zzvf = {new zza()};
    @VisibleForTesting
    private final Context mContext;
    @VisibleForTesting
    private SSLSocketFactory zzvg = null;
    @VisibleForTesting
    private SSLSocketFactory zzvh = null;
    @VisibleForTesting
    private TrustManager[] zzvi = null;
    @VisibleForTesting
    private KeyManager[] zzvj = null;
    @VisibleForTesting
    private byte[] zzvk = null;
    @VisibleForTesting
    private byte[] zzvl = null;
    @VisibleForTesting
    private PrivateKey zzvm = null;
    @VisibleForTesting
    private final int zzvn;
    @VisibleForTesting
    private final boolean zzvo;
    @VisibleForTesting
    private final boolean zzvp;
    @VisibleForTesting
    private final String zzvq;

    private SSLCertificateSocketFactory(Context context, int i, boolean z, boolean z2, String str) {
        this.mContext = context.getApplicationContext();
        this.zzvn = i;
        this.zzvo = z;
        this.zzvp = z2;
        this.zzvq = str;
    }

    public static SocketFactory getDefault(Context context, int i) {
        return new SSLCertificateSocketFactory(context, i, false, true, null);
    }

    public static SSLSocketFactory getDefaultWithCacheDir(int i, Context context, String str) {
        return new SSLCertificateSocketFactory(context, i, true, true, str);
    }

    public static SSLSocketFactory getDefaultWithSessionCache(int i, Context context) {
        return new SSLCertificateSocketFactory(context, i, true, true, null);
    }

    public static SSLSocketFactory getInsecure(Context context, int i, boolean z) {
        return new SSLCertificateSocketFactory(context, i, z, false, null);
    }

    public static void verifyHostname(Socket socket, String str) throws IOException {
        if (!(socket instanceof SSLSocket)) {
            throw new IllegalArgumentException("Attempt to verify non-SSL socket");
        }
        SSLSocket sSLSocket = (SSLSocket) socket;
        sSLSocket.startHandshake();
        SSLSession session = sSLSocket.getSession();
        if (session == null) {
            throw new SSLException("Cannot verify SSL socket without session");
        }
        if (HttpsURLConnection.getDefaultHostnameVerifier().verify(str, session)) {
            return;
        }
        String valueOf = String.valueOf(str);
        throw new SSLPeerUnverifiedException(valueOf.length() != 0 ? "Cannot verify hostname: ".concat(valueOf) : new String("Cannot verify hostname: "));
    }

    @VisibleForTesting
    private static void zza(Socket socket, int i) {
        if (socket != null) {
            try {
                socket.getClass().getMethod("setHandshakeTimeout", Integer.TYPE).invoke(socket, Integer.valueOf(i));
            } catch (IllegalAccessException | NoSuchMethodException e) {
                String valueOf = String.valueOf(socket.getClass());
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 45);
                sb.append(valueOf);
                sb.append(" does not implement setSocketHandshakeTimeout");
                throw new IllegalArgumentException(sb.toString(), e);
            } catch (InvocationTargetException e2) {
                Throwable cause = e2.getCause();
                if (cause instanceof RuntimeException) {
                    throw ((RuntimeException) cause);
                }
                String valueOf2 = String.valueOf(socket.getClass());
                StringBuilder sb2 = new StringBuilder(String.valueOf(valueOf2).length() + 46);
                sb2.append("Failed to invoke setSocketHandshakeTimeout on ");
                sb2.append(valueOf2);
                throw new RuntimeException(sb2.toString(), e2);
            }
        }
    }

    @VisibleForTesting
    private static void zza(Socket socket, PrivateKey privateKey) {
        if (socket != null) {
            try {
                socket.getClass().getMethod("setChannelIdPrivateKey", PrivateKey.class).invoke(socket, privateKey);
            } catch (IllegalAccessException | NoSuchMethodException e) {
                String valueOf = String.valueOf(socket.getClass());
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 42);
                sb.append(valueOf);
                sb.append(" does not implement setChannelIdPrivateKey");
                throw new IllegalArgumentException(sb.toString(), e);
            } catch (InvocationTargetException e2) {
                Throwable cause = e2.getCause();
                if (cause instanceof RuntimeException) {
                    throw ((RuntimeException) cause);
                }
                String valueOf2 = String.valueOf(socket.getClass());
                StringBuilder sb2 = new StringBuilder(String.valueOf(valueOf2).length() + 43);
                sb2.append("Failed to invoke setChannelIdPrivateKey on ");
                sb2.append(valueOf2);
                throw new RuntimeException(sb2.toString(), e2);
            }
        }
    }

    @VisibleForTesting
    private static void zza(Socket socket, byte[] bArr) {
        if (socket != null) {
            try {
                socket.getClass().getMethod("setNpnProtocols", byte[].class).invoke(socket, bArr);
            } catch (IllegalAccessException | NoSuchMethodException e) {
                String valueOf = String.valueOf(socket.getClass());
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 35);
                sb.append(valueOf);
                sb.append(" does not implement setNpnProtocols");
                throw new IllegalArgumentException(sb.toString(), e);
            } catch (InvocationTargetException e2) {
                Throwable cause = e2.getCause();
                if (cause instanceof RuntimeException) {
                    throw ((RuntimeException) cause);
                }
                String valueOf2 = String.valueOf(socket.getClass());
                StringBuilder sb2 = new StringBuilder(String.valueOf(valueOf2).length() + 36);
                sb2.append("Failed to invoke setNpnProtocols on ");
                sb2.append(valueOf2);
                throw new RuntimeException(sb2.toString(), e2);
            }
        }
    }

    private static byte[] zza(byte[]... bArr) {
        if (bArr.length != 0) {
            int i = 0;
            for (byte[] bArr2 : bArr) {
                if (bArr2.length == 0 || bArr2.length > 255) {
                    int length = bArr2.length;
                    StringBuilder sb = new StringBuilder(44);
                    sb.append("s.length == 0 || s.length > 255: ");
                    sb.append(length);
                    throw new IllegalArgumentException(sb.toString());
                }
                i += bArr2.length + 1;
            }
            byte[] bArr3 = new byte[i];
            int length2 = bArr.length;
            int i2 = 0;
            int i3 = 0;
            while (i2 < length2) {
                byte[] bArr4 = bArr[i2];
                int i4 = i3 + 1;
                bArr3[i3] = (byte) bArr4.length;
                int length3 = bArr4.length;
                int i5 = i4;
                int i6 = 0;
                while (i6 < length3) {
                    bArr3[i5] = bArr4[i6];
                    i6++;
                    i5++;
                }
                i2++;
                i3 = i5;
            }
            return bArr3;
        }
        throw new IllegalArgumentException("items.length == 0");
    }

    @VisibleForTesting
    private static void zzb(Socket socket, byte[] bArr) {
        if (socket != null) {
            try {
                socket.getClass().getMethod("setAlpnProtocols", byte[].class).invoke(socket, bArr);
            } catch (IllegalAccessException | NoSuchMethodException e) {
                String valueOf = String.valueOf(socket.getClass());
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 36);
                sb.append(valueOf);
                sb.append(" does not implement setAlpnProtocols");
                throw new IllegalArgumentException(sb.toString(), e);
            } catch (InvocationTargetException e2) {
                Throwable cause = e2.getCause();
                if (cause instanceof RuntimeException) {
                    throw ((RuntimeException) cause);
                }
                String valueOf2 = String.valueOf(socket.getClass());
                StringBuilder sb2 = new StringBuilder(String.valueOf(valueOf2).length() + 37);
                sb2.append("Failed to invoke setAlpnProtocols on ");
                sb2.append(valueOf2);
                throw new RuntimeException(sb2.toString(), e2);
            }
        }
    }

    @VisibleForTesting
    private final synchronized SSLSocketFactory zzcx() {
        SSLSocketFactory makeSocketFactory;
        if (!this.zzvp) {
            if (this.zzvg == null) {
                Log.w("SSLCertificateSocketFactory", "Bypassing SSL security checks at caller's request");
                this.zzvg = SocketFactoryCreator.getInstance().makeSocketFactory(this.mContext, this.zzvj, zzvf, this.zzvo);
            }
            return this.zzvg;
        } else if (this.zzvq != null) {
            if (this.zzvh == null) {
                makeSocketFactory = SocketFactoryCreator.getInstance().makeSocketFactoryWithCacheDir(this.mContext, this.zzvj, this.zzvi, this.zzvq);
                this.zzvh = makeSocketFactory;
            }
            return this.zzvh;
        } else {
            if (this.zzvh == null) {
                makeSocketFactory = SocketFactoryCreator.getInstance().makeSocketFactory(this.mContext, this.zzvj, this.zzvi, this.zzvo);
                this.zzvh = makeSocketFactory;
            }
            return this.zzvh;
        }
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket() throws IOException {
        Socket createSocket = zzcx().createSocket();
        zza(createSocket, this.zzvk);
        zzb(createSocket, this.zzvl);
        zza(createSocket, this.zzvn);
        zza(createSocket, this.zzvm);
        return createSocket;
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(String str, int i) throws IOException {
        Socket createSocket = zzcx().createSocket(str, i);
        zza(createSocket, this.zzvk);
        zzb(createSocket, this.zzvl);
        zza(createSocket, this.zzvn);
        zza(createSocket, this.zzvm);
        if (this.zzvp) {
            verifyHostname(createSocket, str);
        }
        return createSocket;
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(String str, int i, InetAddress inetAddress, int i2) throws IOException {
        Socket createSocket = zzcx().createSocket(str, i, inetAddress, i2);
        zza(createSocket, this.zzvk);
        zzb(createSocket, this.zzvl);
        zza(createSocket, this.zzvn);
        zza(createSocket, this.zzvm);
        if (this.zzvp) {
            verifyHostname(createSocket, str);
        }
        return createSocket;
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
        Socket createSocket = zzcx().createSocket(inetAddress, i);
        zza(createSocket, this.zzvk);
        zzb(createSocket, this.zzvl);
        zza(createSocket, this.zzvn);
        zza(createSocket, this.zzvm);
        return createSocket;
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2) throws IOException {
        Socket createSocket = zzcx().createSocket(inetAddress, i, inetAddress2, i2);
        zza(createSocket, this.zzvk);
        zzb(createSocket, this.zzvl);
        zza(createSocket, this.zzvn);
        zza(createSocket, this.zzvm);
        return createSocket;
    }

    @Override // javax.net.ssl.SSLSocketFactory
    public Socket createSocket(Socket socket, String str, int i, boolean z) throws IOException {
        Socket createSocket = zzcx().createSocket(socket, str, i, z);
        zza(createSocket, this.zzvk);
        zzb(createSocket, this.zzvl);
        zza(createSocket, this.zzvn);
        zza(createSocket, this.zzvm);
        if (this.zzvp) {
            verifyHostname(createSocket, str);
        }
        return createSocket;
    }

    public byte[] getAlpnSelectedProtocol(Socket socket) {
        try {
            return (byte[]) socket.getClass().getMethod("getAlpnSelectedProtocol", new Class[0]).invoke(socket, new Object[0]);
        } catch (IllegalAccessException | NoSuchMethodException e) {
            String valueOf = String.valueOf(socket.getClass());
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 43);
            sb.append(valueOf);
            sb.append(" does not implement getAlpnSelectedProtocol");
            throw new IllegalArgumentException(sb.toString(), e);
        } catch (InvocationTargetException e2) {
            Throwable cause = e2.getCause();
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            }
            String valueOf2 = String.valueOf(socket.getClass());
            StringBuilder sb2 = new StringBuilder(String.valueOf(valueOf2).length() + 44);
            sb2.append("Failed to invoke getAlpnSelectedProtocol on ");
            sb2.append(valueOf2);
            throw new RuntimeException(sb2.toString(), e2);
        }
    }

    @Override // javax.net.ssl.SSLSocketFactory
    public String[] getDefaultCipherSuites() {
        return zzcx().getDefaultCipherSuites();
    }

    public byte[] getNpnSelectedProtocol(Socket socket) {
        try {
            return (byte[]) socket.getClass().getMethod("getNpnSelectedProtocol", new Class[0]).invoke(socket, new Object[0]);
        } catch (IllegalAccessException | NoSuchMethodException e) {
            String valueOf = String.valueOf(socket.getClass());
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 42);
            sb.append(valueOf);
            sb.append(" does not implement getNpnSelectedProtocol");
            throw new IllegalArgumentException(sb.toString(), e);
        } catch (InvocationTargetException e2) {
            Throwable cause = e2.getCause();
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            }
            String valueOf2 = String.valueOf(socket.getClass());
            StringBuilder sb2 = new StringBuilder(String.valueOf(valueOf2).length() + 43);
            sb2.append("Failed to invoke getNpnSelectedProtocol on ");
            sb2.append(valueOf2);
            throw new RuntimeException(sb2.toString(), e2);
        }
    }

    @Override // javax.net.ssl.SSLSocketFactory
    public String[] getSupportedCipherSuites() {
        return zzcx().getSupportedCipherSuites();
    }

    public void setAlpnProtocols(byte[][] bArr) {
        this.zzvl = zza(bArr);
    }

    public void setChannelIdPrivateKey(PrivateKey privateKey) {
        this.zzvm = privateKey;
    }

    public void setHostname(Socket socket, String str) {
        try {
            socket.getClass().getMethod("setHostname", String.class).invoke(socket, str);
        } catch (IllegalAccessException | NoSuchMethodException e) {
            String valueOf = String.valueOf(socket.getClass());
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 31);
            sb.append(valueOf);
            sb.append(" does not implement setHostname");
            throw new IllegalArgumentException(sb.toString(), e);
        } catch (InvocationTargetException e2) {
            Throwable cause = e2.getCause();
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            }
            String valueOf2 = String.valueOf(socket.getClass());
            StringBuilder sb2 = new StringBuilder(String.valueOf(valueOf2).length() + 32);
            sb2.append("Failed to invoke setHostname on ");
            sb2.append(valueOf2);
            throw new RuntimeException(sb2.toString(), e2);
        }
    }

    public void setKeyManagers(KeyManager[] keyManagerArr) {
        this.zzvj = keyManagerArr;
        this.zzvh = null;
        this.zzvg = null;
    }

    public void setNpnProtocols(byte[][] bArr) {
        this.zzvk = zza(bArr);
    }

    public void setSoWriteTimeout(Socket socket, int i) throws SocketException {
        try {
            socket.getClass().getMethod("setSoWriteTimeout", Integer.TYPE).invoke(socket, Integer.valueOf(i));
        } catch (IllegalAccessException | NoSuchMethodException e) {
            String valueOf = String.valueOf(socket.getClass());
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 37);
            sb.append(valueOf);
            sb.append(" does not implement setSoWriteTimeout");
            throw new IllegalArgumentException(sb.toString(), e);
        } catch (InvocationTargetException e2) {
            Throwable cause = e2.getCause();
            if (cause instanceof SocketException) {
                throw ((SocketException) cause);
            }
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            }
            String valueOf2 = String.valueOf(socket.getClass());
            StringBuilder sb2 = new StringBuilder(String.valueOf(valueOf2).length() + 38);
            sb2.append("Failed to invoke setSoWriteTimeout on ");
            sb2.append(valueOf2);
            throw new RuntimeException(sb2.toString(), e2);
        }
    }

    public void setTrustManagers(TrustManager[] trustManagerArr) {
        this.zzvi = trustManagerArr;
        this.zzvh = null;
    }

    public void setUseSessionTickets(Socket socket, boolean z) {
        try {
            socket.getClass().getMethod("setUseSessionTickets", Boolean.TYPE).invoke(socket, Boolean.valueOf(z));
        } catch (IllegalAccessException | NoSuchMethodException e) {
            String valueOf = String.valueOf(socket.getClass());
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 40);
            sb.append(valueOf);
            sb.append(" does not implement setUseSessionTickets");
            throw new IllegalArgumentException(sb.toString(), e);
        } catch (InvocationTargetException e2) {
            Throwable cause = e2.getCause();
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            }
            String valueOf2 = String.valueOf(socket.getClass());
            StringBuilder sb2 = new StringBuilder(String.valueOf(valueOf2).length() + 41);
            sb2.append("Failed to invoke setUseSessionTickets on ");
            sb2.append(valueOf2);
            throw new RuntimeException(sb2.toString(), e2);
        }
    }
}
