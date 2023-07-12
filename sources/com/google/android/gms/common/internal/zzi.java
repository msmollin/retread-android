package com.google.android.gms.common.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import com.google.android.gms.common.internal.GmsClientSupervisor;
import com.google.android.gms.common.stats.ConnectionTracker;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/* loaded from: classes.dex */
public final class zzi implements ServiceConnection {
    private ComponentName mComponentName;
    private IBinder zzry;
    private boolean zztw;
    private final GmsClientSupervisor.ConnectionStatusConfig zztx;
    private final /* synthetic */ zzh zzty;
    private final Set<ServiceConnection> zztv = new HashSet();
    private int mState = 2;

    public zzi(zzh zzhVar, GmsClientSupervisor.ConnectionStatusConfig connectionStatusConfig) {
        this.zzty = zzhVar;
        this.zztx = connectionStatusConfig;
    }

    public static /* synthetic */ GmsClientSupervisor.ConnectionStatusConfig zza(zzi zziVar) {
        return zziVar.zztx;
    }

    public final IBinder getBinder() {
        return this.zzry;
    }

    public final ComponentName getComponentName() {
        return this.mComponentName;
    }

    public final int getState() {
        return this.mState;
    }

    public final boolean isBound() {
        return this.zztw;
    }

    @Override // android.content.ServiceConnection
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        HashMap hashMap;
        Handler handler;
        hashMap = this.zzty.zztr;
        synchronized (hashMap) {
            handler = this.zzty.mHandler;
            handler.removeMessages(1, this.zztx);
            this.zzry = iBinder;
            this.mComponentName = componentName;
            for (ServiceConnection serviceConnection : this.zztv) {
                serviceConnection.onServiceConnected(componentName, iBinder);
            }
            this.mState = 1;
        }
    }

    @Override // android.content.ServiceConnection
    public final void onServiceDisconnected(ComponentName componentName) {
        HashMap hashMap;
        Handler handler;
        hashMap = this.zzty.zztr;
        synchronized (hashMap) {
            handler = this.zzty.mHandler;
            handler.removeMessages(1, this.zztx);
            this.zzry = null;
            this.mComponentName = componentName;
            for (ServiceConnection serviceConnection : this.zztv) {
                serviceConnection.onServiceDisconnected(componentName);
            }
            this.mState = 2;
        }
    }

    public final void zza(ServiceConnection serviceConnection, String str) {
        ConnectionTracker connectionTracker;
        Context context;
        Context context2;
        connectionTracker = this.zzty.zzts;
        context = this.zzty.zzau;
        GmsClientSupervisor.ConnectionStatusConfig connectionStatusConfig = this.zztx;
        context2 = this.zzty.zzau;
        connectionTracker.logConnectService(context, serviceConnection, str, connectionStatusConfig.getStartServiceIntent(context2));
        this.zztv.add(serviceConnection);
    }

    public final boolean zza(ServiceConnection serviceConnection) {
        return this.zztv.contains(serviceConnection);
    }

    public final void zzb(ServiceConnection serviceConnection, String str) {
        ConnectionTracker connectionTracker;
        Context context;
        connectionTracker = this.zzty.zzts;
        context = this.zzty.zzau;
        connectionTracker.logDisconnectService(context, serviceConnection);
        this.zztv.remove(serviceConnection);
    }

    public final boolean zzcv() {
        return this.zztv.isEmpty();
    }

    public final void zzj(String str) {
        ConnectionTracker connectionTracker;
        Context context;
        Context context2;
        ConnectionTracker connectionTracker2;
        Context context3;
        Handler handler;
        Handler handler2;
        long j;
        this.mState = 3;
        connectionTracker = this.zzty.zzts;
        context = this.zzty.zzau;
        GmsClientSupervisor.ConnectionStatusConfig connectionStatusConfig = this.zztx;
        context2 = this.zzty.zzau;
        this.zztw = connectionTracker.bindService(context, str, connectionStatusConfig.getStartServiceIntent(context2), this, this.zztx.getBindFlags());
        if (this.zztw) {
            handler = this.zzty.mHandler;
            Message obtainMessage = handler.obtainMessage(1, this.zztx);
            handler2 = this.zzty.mHandler;
            j = this.zzty.zztu;
            handler2.sendMessageDelayed(obtainMessage, j);
            return;
        }
        this.mState = 2;
        try {
            connectionTracker2 = this.zzty.zzts;
            context3 = this.zzty.zzau;
            connectionTracker2.unbindService(context3, this);
        } catch (IllegalArgumentException unused) {
        }
    }

    public final void zzk(String str) {
        Handler handler;
        ConnectionTracker connectionTracker;
        Context context;
        handler = this.zzty.mHandler;
        handler.removeMessages(1, this.zztx);
        connectionTracker = this.zzty.zzts;
        context = this.zzty.zzau;
        connectionTracker.unbindService(context, this);
        this.zztw = false;
        this.mState = 2;
    }
}
