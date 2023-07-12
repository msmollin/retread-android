package com.google.android.gms.internal.measurement;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import androidx.annotation.MainThread;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzfz implements ServiceConnection {
    final /* synthetic */ zzfx zzaky;

    private zzfz(zzfx zzfxVar) {
        this.zzaky = zzfxVar;
    }

    @Override // android.content.ServiceConnection
    @MainThread
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (iBinder == null) {
            zzfx.zza(this.zzaky).zzge().zzip().log("Install Referrer connection returned with null binder");
            return;
        }
        try {
            this.zzaky.zzakw = zzs.zza(iBinder);
            if (this.zzaky.zzakw == null) {
                zzfx.zza(this.zzaky).zzge().zzip().log("Install Referrer Service implementation was not found");
                return;
            }
            zzfx.zza(this.zzaky).zzge().zzir().log("Install Referrer Service connected");
            zzfx.zza(this.zzaky).zzgd().zzc(new zzga(this));
        } catch (Exception e) {
            zzfx.zza(this.zzaky).zzge().zzip().zzg("Exception occurred while calling Install Referrer API", e);
        }
    }

    @Override // android.content.ServiceConnection
    @MainThread
    public final void onServiceDisconnected(ComponentName componentName) {
        this.zzaky.zzakw = null;
        zzfx.zza(this.zzaky).zzge().zzir().log("Install Referrer Service disconnected");
    }
}
