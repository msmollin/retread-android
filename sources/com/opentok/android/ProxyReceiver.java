package com.opentok.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.opentok.android.OtLog;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;

/* loaded from: classes.dex */
class ProxyReceiver extends BroadcastReceiver {
    private static final OtLog.LogToken log = new OtLog.LogToken();
    private Context applicationsCtn;

    static {
        registerNatives();
    }

    protected ProxyReceiver(Context context) {
        updateProxyInfo();
        context.getApplicationContext().registerReceiver(this, new IntentFilter("android.intent.action.PROXY_CHANGE"));
        this.applicationsCtn = context.getApplicationContext();
    }

    public static void forceSetProxy(String str, int i) {
        OtLog.LogToken logToken = log;
        logToken.d("forceSetProxy(" + str + ", " + i + ")", new Object[0]);
        setProxyNative(str, i);
    }

    private static native void registerNatives();

    private static native void setProxyNative(String str, int i);

    private void updateProxyInfo() {
        for (Proxy proxy : ProxySelector.getDefault().select(URI.create(BuildConfig.API_URL))) {
            try {
                InetSocketAddress inetSocketAddress = (InetSocketAddress) proxy.address();
                if (inetSocketAddress != null && inetSocketAddress.getHostName() != null) {
                    OtLog.LogToken logToken = log;
                    logToken.d("updateProxyInfo() Set proxy " + inetSocketAddress.toString(), new Object[0]);
                    setProxyNative(inetSocketAddress.getHostName(), inetSocketAddress.getPort());
                    return;
                }
            } catch (ClassCastException unused) {
            }
        }
    }

    protected void close() {
        this.applicationsCtn.unregisterReceiver(this);
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == null || !intent.getAction().equals("android.intent.action.PROXY_CHANGE")) {
            return;
        }
        updateProxyInfo();
    }
}
