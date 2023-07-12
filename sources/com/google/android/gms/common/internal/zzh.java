package com.google.android.gms.common.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.google.android.gms.common.internal.GmsClientSupervisor;
import com.google.android.gms.common.stats.ConnectionTracker;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.HashMap;
import javax.annotation.concurrent.GuardedBy;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzh extends GmsClientSupervisor implements Handler.Callback {
    private final Handler mHandler;
    private final Context zzau;
    @GuardedBy("mConnectionStatus")
    private final HashMap<GmsClientSupervisor.ConnectionStatusConfig, zzi> zztr = new HashMap<>();
    private final ConnectionTracker zzts = ConnectionTracker.getInstance();
    private final long zztt = 5000;
    private final long zztu = 300000;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzh(Context context) {
        this.zzau = context.getApplicationContext();
        this.mHandler = new Handler(context.getMainLooper(), this);
    }

    @Override // com.google.android.gms.common.internal.GmsClientSupervisor
    protected final boolean bindService(GmsClientSupervisor.ConnectionStatusConfig connectionStatusConfig, ServiceConnection serviceConnection, String str) {
        boolean isBound;
        Preconditions.checkNotNull(serviceConnection, "ServiceConnection must not be null");
        synchronized (this.zztr) {
            zzi zziVar = this.zztr.get(connectionStatusConfig);
            if (zziVar == null) {
                zziVar = new zzi(this, connectionStatusConfig);
                zziVar.zza(serviceConnection, str);
                zziVar.zzj(str);
                this.zztr.put(connectionStatusConfig, zziVar);
            } else {
                this.mHandler.removeMessages(0, connectionStatusConfig);
                if (!zziVar.zza(serviceConnection)) {
                    zziVar.zza(serviceConnection, str);
                    switch (zziVar.getState()) {
                        case 1:
                            serviceConnection.onServiceConnected(zziVar.getComponentName(), zziVar.getBinder());
                            break;
                        case 2:
                            zziVar.zzj(str);
                            break;
                    }
                } else {
                    String valueOf = String.valueOf(connectionStatusConfig);
                    StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 81);
                    sb.append("Trying to bind a GmsServiceConnection that was already connected before.  config=");
                    sb.append(valueOf);
                    throw new IllegalStateException(sb.toString());
                }
            }
            isBound = zziVar.isBound();
        }
        return isBound;
    }

    @Override // android.os.Handler.Callback
    public final boolean handleMessage(Message message) {
        switch (message.what) {
            case 0:
                synchronized (this.zztr) {
                    GmsClientSupervisor.ConnectionStatusConfig connectionStatusConfig = (GmsClientSupervisor.ConnectionStatusConfig) message.obj;
                    zzi zziVar = this.zztr.get(connectionStatusConfig);
                    if (zziVar != null && zziVar.zzcv()) {
                        if (zziVar.isBound()) {
                            zziVar.zzk("GmsClientSupervisor");
                        }
                        this.zztr.remove(connectionStatusConfig);
                    }
                }
                return true;
            case 1:
                synchronized (this.zztr) {
                    GmsClientSupervisor.ConnectionStatusConfig connectionStatusConfig2 = (GmsClientSupervisor.ConnectionStatusConfig) message.obj;
                    zzi zziVar2 = this.zztr.get(connectionStatusConfig2);
                    if (zziVar2 != null && zziVar2.getState() == 3) {
                        String valueOf = String.valueOf(connectionStatusConfig2);
                        StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 47);
                        sb.append("Timeout waiting for ServiceConnection callback ");
                        sb.append(valueOf);
                        Log.wtf("GmsClientSupervisor", sb.toString(), new Exception());
                        ComponentName componentName = zziVar2.getComponentName();
                        if (componentName == null) {
                            componentName = connectionStatusConfig2.getComponentName();
                        }
                        if (componentName == null) {
                            componentName = new ComponentName(connectionStatusConfig2.getPackage(), "unknown");
                        }
                        zziVar2.onServiceDisconnected(componentName);
                    }
                }
                return true;
            default:
                return false;
        }
    }

    @Override // com.google.android.gms.common.internal.GmsClientSupervisor
    @VisibleForTesting
    public final void resetForTesting() {
        synchronized (this.zztr) {
            for (zzi zziVar : this.zztr.values()) {
                this.mHandler.removeMessages(0, zzi.zza(zziVar));
                if (zziVar.isBound()) {
                    zziVar.zzk("GmsClientSupervisor");
                }
            }
            this.zztr.clear();
        }
    }

    @Override // com.google.android.gms.common.internal.GmsClientSupervisor
    protected final void unbindService(GmsClientSupervisor.ConnectionStatusConfig connectionStatusConfig, ServiceConnection serviceConnection, String str) {
        Preconditions.checkNotNull(serviceConnection, "ServiceConnection must not be null");
        synchronized (this.zztr) {
            zzi zziVar = this.zztr.get(connectionStatusConfig);
            if (zziVar == null) {
                String valueOf = String.valueOf(connectionStatusConfig);
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 50);
                sb.append("Nonexistent connection status for service config: ");
                sb.append(valueOf);
                throw new IllegalStateException(sb.toString());
            } else if (!zziVar.zza(serviceConnection)) {
                String valueOf2 = String.valueOf(connectionStatusConfig);
                StringBuilder sb2 = new StringBuilder(String.valueOf(valueOf2).length() + 76);
                sb2.append("Trying to unbind a GmsServiceConnection  that was not bound before.  config=");
                sb2.append(valueOf2);
                throw new IllegalStateException(sb2.toString());
            } else {
                zziVar.zzb(serviceConnection, str);
                if (zziVar.zzcv()) {
                    this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(0, connectionStatusConfig), this.zztt);
                }
            }
        }
    }
}
