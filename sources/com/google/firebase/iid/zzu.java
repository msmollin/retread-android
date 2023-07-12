package com.google.firebase.iid;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseArray;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.stats.ConnectionTracker;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.annotation.concurrent.GuardedBy;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzu implements ServiceConnection {
    @GuardedBy("this")
    int state;
    final Messenger zzbj;
    zzz zzbk;
    @GuardedBy("this")
    final Queue<zzab<?>> zzbl;
    @GuardedBy("this")
    final SparseArray<zzab<?>> zzbm;
    final /* synthetic */ zzs zzbn;

    private zzu(zzs zzsVar) {
        this.zzbn = zzsVar;
        this.state = 0;
        this.zzbj = new Messenger(new Handler(Looper.getMainLooper(), new Handler.Callback(this) { // from class: com.google.firebase.iid.zzv
            private final zzu zzbo;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.zzbo = this;
            }

            @Override // android.os.Handler.Callback
            public final boolean handleMessage(Message message) {
                return this.zzbo.zza(message);
            }
        }));
        this.zzbl = new ArrayDeque();
        this.zzbm = new SparseArray<>();
    }

    private final void zzt() {
        ScheduledExecutorService scheduledExecutorService;
        scheduledExecutorService = this.zzbn.zzbg;
        scheduledExecutorService.execute(new Runnable(this) { // from class: com.google.firebase.iid.zzx
            private final zzu zzbo;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.zzbo = this;
            }

            @Override // java.lang.Runnable
            public final void run() {
                final zzab<?> poll;
                ScheduledExecutorService scheduledExecutorService2;
                Context context;
                final zzu zzuVar = this.zzbo;
                while (true) {
                    synchronized (zzuVar) {
                        if (zzuVar.state != 2) {
                            return;
                        }
                        if (zzuVar.zzbl.isEmpty()) {
                            zzuVar.zzu();
                            return;
                        }
                        poll = zzuVar.zzbl.poll();
                        zzuVar.zzbm.put(poll.zzbr, poll);
                        scheduledExecutorService2 = zzuVar.zzbn.zzbg;
                        scheduledExecutorService2.schedule(new Runnable(zzuVar, poll) { // from class: com.google.firebase.iid.zzy
                            private final zzu zzbo;
                            private final zzab zzbp;

                            /* JADX INFO: Access modifiers changed from: package-private */
                            {
                                this.zzbo = zzuVar;
                                this.zzbp = poll;
                            }

                            @Override // java.lang.Runnable
                            public final void run() {
                                this.zzbo.zza(this.zzbp.zzbr);
                            }
                        }, 30L, TimeUnit.SECONDS);
                    }
                    if (Log.isLoggable("MessengerIpcClient", 3)) {
                        String valueOf = String.valueOf(poll);
                        StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 8);
                        sb.append("Sending ");
                        sb.append(valueOf);
                        Log.d("MessengerIpcClient", sb.toString());
                    }
                    context = zzuVar.zzbn.zzz;
                    Messenger messenger = zzuVar.zzbj;
                    Message obtain = Message.obtain();
                    obtain.what = poll.what;
                    obtain.arg1 = poll.zzbr;
                    obtain.replyTo = messenger;
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("oneWay", poll.zzw());
                    bundle.putString("pkg", context.getPackageName());
                    bundle.putBundle("data", poll.zzbt);
                    obtain.setData(bundle);
                    try {
                        zzuVar.zzbk.send(obtain);
                    } catch (RemoteException e) {
                        zzuVar.zza(2, e.getMessage());
                    }
                }
            }
        });
    }

    @Override // android.content.ServiceConnection
    public final synchronized void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (Log.isLoggable("MessengerIpcClient", 2)) {
            Log.v("MessengerIpcClient", "Service connected");
        }
        if (iBinder == null) {
            zza(0, "Null service connection");
            return;
        }
        try {
            this.zzbk = new zzz(iBinder);
            this.state = 2;
            zzt();
        } catch (RemoteException e) {
            zza(0, e.getMessage());
        }
    }

    @Override // android.content.ServiceConnection
    public final synchronized void onServiceDisconnected(ComponentName componentName) {
        if (Log.isLoggable("MessengerIpcClient", 2)) {
            Log.v("MessengerIpcClient", "Service disconnected");
        }
        zza(2, "Service disconnected");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final synchronized void zza(int i) {
        zzab<?> zzabVar = this.zzbm.get(i);
        if (zzabVar != null) {
            StringBuilder sb = new StringBuilder(31);
            sb.append("Timing out request: ");
            sb.append(i);
            Log.w("MessengerIpcClient", sb.toString());
            this.zzbm.remove(i);
            zzabVar.zza(new zzac(3, "Timed out waiting for response"));
            zzu();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final synchronized void zza(int i, String str) {
        Context context;
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            String valueOf = String.valueOf(str);
            Log.d("MessengerIpcClient", valueOf.length() != 0 ? "Disconnected: ".concat(valueOf) : new String("Disconnected: "));
        }
        switch (this.state) {
            case 0:
                throw new IllegalStateException();
            case 1:
            case 2:
                if (Log.isLoggable("MessengerIpcClient", 2)) {
                    Log.v("MessengerIpcClient", "Unbinding service");
                }
                this.state = 4;
                ConnectionTracker connectionTracker = ConnectionTracker.getInstance();
                context = this.zzbn.zzz;
                connectionTracker.unbindService(context, this);
                zzac zzacVar = new zzac(i, str);
                for (zzab<?> zzabVar : this.zzbl) {
                    zzabVar.zza(zzacVar);
                }
                this.zzbl.clear();
                for (int i2 = 0; i2 < this.zzbm.size(); i2++) {
                    this.zzbm.valueAt(i2).zza(zzacVar);
                }
                this.zzbm.clear();
                return;
            case 3:
                this.state = 4;
                return;
            case 4:
                return;
            default:
                int i3 = this.state;
                StringBuilder sb = new StringBuilder(26);
                sb.append("Unknown state: ");
                sb.append(i3);
                throw new IllegalStateException(sb.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean zza(Message message) {
        int i = message.arg1;
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            StringBuilder sb = new StringBuilder(41);
            sb.append("Received response to request: ");
            sb.append(i);
            Log.d("MessengerIpcClient", sb.toString());
        }
        synchronized (this) {
            zzab<?> zzabVar = this.zzbm.get(i);
            if (zzabVar == null) {
                StringBuilder sb2 = new StringBuilder(50);
                sb2.append("Received response for unknown request: ");
                sb2.append(i);
                Log.w("MessengerIpcClient", sb2.toString());
                return true;
            }
            this.zzbm.remove(i);
            zzu();
            Bundle data = message.getData();
            if (data.getBoolean("unsupported", false)) {
                zzabVar.zza(new zzac(4, "Not supported by GmsCore"));
            } else {
                zzabVar.zzb(data);
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final synchronized boolean zzb(zzab zzabVar) {
        Context context;
        ScheduledExecutorService scheduledExecutorService;
        switch (this.state) {
            case 0:
                this.zzbl.add(zzabVar);
                Preconditions.checkState(this.state == 0);
                if (Log.isLoggable("MessengerIpcClient", 2)) {
                    Log.v("MessengerIpcClient", "Starting bind to GmsCore");
                }
                this.state = 1;
                Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
                intent.setPackage("com.google.android.gms");
                ConnectionTracker connectionTracker = ConnectionTracker.getInstance();
                context = this.zzbn.zzz;
                if (connectionTracker.bindService(context, intent, this, 1)) {
                    scheduledExecutorService = this.zzbn.zzbg;
                    scheduledExecutorService.schedule(new Runnable(this) { // from class: com.google.firebase.iid.zzw
                        private final zzu zzbo;

                        /* JADX INFO: Access modifiers changed from: package-private */
                        {
                            this.zzbo = this;
                        }

                        @Override // java.lang.Runnable
                        public final void run() {
                            this.zzbo.zzv();
                        }
                    }, 30L, TimeUnit.SECONDS);
                } else {
                    zza(0, "Unable to bind to service");
                }
                return true;
            case 1:
                this.zzbl.add(zzabVar);
                return true;
            case 2:
                this.zzbl.add(zzabVar);
                zzt();
                return true;
            case 3:
            case 4:
                return false;
            default:
                int i = this.state;
                StringBuilder sb = new StringBuilder(26);
                sb.append("Unknown state: ");
                sb.append(i);
                throw new IllegalStateException(sb.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final synchronized void zzu() {
        Context context;
        if (this.state == 2 && this.zzbl.isEmpty() && this.zzbm.size() == 0) {
            if (Log.isLoggable("MessengerIpcClient", 2)) {
                Log.v("MessengerIpcClient", "Finished handling requests, unbinding");
            }
            this.state = 3;
            ConnectionTracker connectionTracker = ConnectionTracker.getInstance();
            context = this.zzbn.zzz;
            connectionTracker.unbindService(context, this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final synchronized void zzv() {
        if (this.state == 1) {
            zza(1, "Timed out while binding");
        }
    }
}
