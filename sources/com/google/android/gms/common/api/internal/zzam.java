package com.google.android.gms.common.api.internal;

import android.content.Context;
import androidx.annotation.WorkerThread;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.internal.GoogleApiAvailabilityCache;
import com.google.android.gms.signin.SignInClient;
import java.util.ArrayList;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzam extends zzat {
    final /* synthetic */ zzaj zzhv;
    private final Map<Api.Client, zzal> zzhx;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzam(zzaj zzajVar, Map<Api.Client, zzal> map) {
        super(zzajVar, null);
        this.zzhv = zzajVar;
        this.zzhx = map;
    }

    @Override // com.google.android.gms.common.api.internal.zzat
    @WorkerThread
    public final void zzaq() {
        GoogleApiAvailabilityLight googleApiAvailabilityLight;
        Context context;
        boolean z;
        Context context2;
        zzbd zzbdVar;
        SignInClient signInClient;
        zzbd zzbdVar2;
        Context context3;
        boolean z2;
        googleApiAvailabilityLight = this.zzhv.zzgk;
        GoogleApiAvailabilityCache googleApiAvailabilityCache = new GoogleApiAvailabilityCache(googleApiAvailabilityLight);
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (Api.Client client : this.zzhx.keySet()) {
            if (client.requiresGooglePlayServices()) {
                z2 = this.zzhx.get(client).zzfo;
                if (!z2) {
                    arrayList.add(client);
                }
            }
            arrayList2.add(client);
        }
        int i = -1;
        int i2 = 0;
        if (arrayList.isEmpty()) {
            ArrayList arrayList3 = arrayList2;
            int size = arrayList3.size();
            while (i2 < size) {
                Object obj = arrayList3.get(i2);
                i2++;
                context3 = this.zzhv.mContext;
                i = googleApiAvailabilityCache.getClientAvailability(context3, (Api.Client) obj);
                if (i == 0) {
                    break;
                }
            }
        } else {
            ArrayList arrayList4 = arrayList;
            int size2 = arrayList4.size();
            while (i2 < size2) {
                Object obj2 = arrayList4.get(i2);
                i2++;
                context = this.zzhv.mContext;
                i = googleApiAvailabilityCache.getClientAvailability(context, (Api.Client) obj2);
                if (i != 0) {
                    break;
                }
            }
        }
        if (i != 0) {
            ConnectionResult connectionResult = new ConnectionResult(i, null);
            zzbdVar2 = this.zzhv.zzhf;
            zzbdVar2.zza(new zzan(this, this.zzhv, connectionResult));
            return;
        }
        z = this.zzhv.zzhp;
        if (z) {
            signInClient = this.zzhv.zzhn;
            signInClient.connect();
        }
        for (Api.Client client2 : this.zzhx.keySet()) {
            zzal zzalVar = this.zzhx.get(client2);
            if (client2.requiresGooglePlayServices()) {
                context2 = this.zzhv.mContext;
                if (googleApiAvailabilityCache.getClientAvailability(context2, client2) != 0) {
                    zzbdVar = this.zzhv.zzhf;
                    zzbdVar.zza(new zzao(this, this.zzhv, zzalVar));
                }
            }
            client2.connect(zzalVar);
        }
    }
}
