package org.eclipse.paho.client.mqttv3.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.eclipse.paho.client.mqttv3.MqttException;

/* loaded from: classes2.dex */
public class LocalNetworkModule implements NetworkModule {
    static /* synthetic */ Class class$0;
    private String brokerName;
    private Object localAdapter;
    private Class localListener;

    public LocalNetworkModule(String str) {
        this.brokerName = str;
    }

    @Override // org.eclipse.paho.client.mqttv3.internal.NetworkModule
    public void start() throws IOException, MqttException {
        if (!ExceptionHelper.isClassAvailable("com.ibm.mqttdirect.modules.local.bindings.localListener")) {
            throw ExceptionHelper.createMqttException(32103);
        }
        try {
            this.localListener = Class.forName("com.ibm.mqttdirect.modules.local.bindings.localListener");
            Class cls = this.localListener;
            Class<?>[] clsArr = new Class[1];
            Class<?> cls2 = class$0;
            if (cls2 == null) {
                try {
                    cls2 = Class.forName("java.lang.String");
                    class$0 = cls2;
                } catch (ClassNotFoundException e) {
                    throw new NoClassDefFoundError(e.getMessage());
                }
            }
            clsArr[0] = cls2;
            this.localAdapter = cls.getMethod("connect", clsArr).invoke(null, this.brokerName);
        } catch (Exception unused) {
        }
        if (this.localAdapter == null) {
            throw ExceptionHelper.createMqttException(32103);
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.internal.NetworkModule
    public InputStream getInputStream() throws IOException {
        try {
            return (InputStream) this.localListener.getMethod("getClientInputStream", new Class[0]).invoke(this.localAdapter, new Object[0]);
        } catch (Exception unused) {
            return null;
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.internal.NetworkModule
    public OutputStream getOutputStream() throws IOException {
        try {
            return (OutputStream) this.localListener.getMethod("getClientOutputStream", new Class[0]).invoke(this.localAdapter, new Object[0]);
        } catch (Exception unused) {
            return null;
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.internal.NetworkModule
    public void stop() throws IOException {
        if (this.localAdapter != null) {
            try {
                this.localListener.getMethod("close", new Class[0]).invoke(this.localAdapter, new Object[0]);
            } catch (Exception unused) {
            }
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.internal.NetworkModule
    public String getServerURI() {
        StringBuffer stringBuffer = new StringBuffer("local://");
        stringBuffer.append(this.brokerName);
        return stringBuffer.toString();
    }
}
