package com.google.android.gms.common.api.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.internal.BaseImplementation;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.signin.SignInClient;
import com.google.android.gms.signin.SignInOptions;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import javax.annotation.concurrent.GuardedBy;

/* loaded from: classes.dex */
public final class zzbd implements zzbp, zzq {
    private final Context mContext;
    private final Api.AbstractClientBuilder<? extends SignInClient, SignInOptions> zzdh;
    final zzav zzfq;
    private final Lock zzga;
    private final ClientSettings zzgf;
    private final Map<Api<?>, Boolean> zzgi;
    private final GoogleApiAvailabilityLight zzgk;
    final Map<Api.AnyClientKey<?>, Api.Client> zzil;
    private final Condition zziz;
    private final zzbf zzja;
    private volatile zzbc zzjc;
    int zzje;
    final zzbq zzjf;
    final Map<Api.AnyClientKey<?>, ConnectionResult> zzjb = new HashMap();
    private ConnectionResult zzjd = null;

    public zzbd(Context context, zzav zzavVar, Lock lock, Looper looper, GoogleApiAvailabilityLight googleApiAvailabilityLight, Map<Api.AnyClientKey<?>, Api.Client> map, ClientSettings clientSettings, Map<Api<?>, Boolean> map2, Api.AbstractClientBuilder<? extends SignInClient, SignInOptions> abstractClientBuilder, ArrayList<zzp> arrayList, zzbq zzbqVar) {
        this.mContext = context;
        this.zzga = lock;
        this.zzgk = googleApiAvailabilityLight;
        this.zzil = map;
        this.zzgf = clientSettings;
        this.zzgi = map2;
        this.zzdh = abstractClientBuilder;
        this.zzfq = zzavVar;
        this.zzjf = zzbqVar;
        ArrayList<zzp> arrayList2 = arrayList;
        int size = arrayList2.size();
        int i = 0;
        while (i < size) {
            zzp zzpVar = arrayList2.get(i);
            i++;
            zzpVar.zza(this);
        }
        this.zzja = new zzbf(this, looper);
        this.zziz = lock.newCondition();
        this.zzjc = new zzau(this);
    }

    @Override // com.google.android.gms.common.api.internal.zzbp
    @GuardedBy("mLock")
    public final ConnectionResult blockingConnect() {
        connect();
        while (isConnecting()) {
            try {
                this.zziz.await();
            } catch (InterruptedException unused) {
                Thread.currentThread().interrupt();
                return new ConnectionResult(15, null);
            }
        }
        return isConnected() ? ConnectionResult.RESULT_SUCCESS : this.zzjd != null ? this.zzjd : new ConnectionResult(13, null);
    }

    @Override // com.google.android.gms.common.api.internal.zzbp
    @GuardedBy("mLock")
    public final ConnectionResult blockingConnect(long j, TimeUnit timeUnit) {
        connect();
        long nanos = timeUnit.toNanos(j);
        while (isConnecting()) {
            if (nanos <= 0) {
                disconnect();
                return new ConnectionResult(14, null);
            }
            try {
                nanos = this.zziz.awaitNanos(nanos);
            } catch (InterruptedException unused) {
                Thread.currentThread().interrupt();
                return new ConnectionResult(15, null);
            }
            Thread.currentThread().interrupt();
            return new ConnectionResult(15, null);
        }
        return isConnected() ? ConnectionResult.RESULT_SUCCESS : this.zzjd != null ? this.zzjd : new ConnectionResult(13, null);
    }

    @Override // com.google.android.gms.common.api.internal.zzbp
    @GuardedBy("mLock")
    public final void connect() {
        this.zzjc.connect();
    }

    @Override // com.google.android.gms.common.api.internal.zzbp
    @GuardedBy("mLock")
    public final void disconnect() {
        if (this.zzjc.disconnect()) {
            this.zzjb.clear();
        }
    }

    @Override // com.google.android.gms.common.api.internal.zzbp
    public final void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        String concat = String.valueOf(str).concat("  ");
        printWriter.append((CharSequence) str).append("mState=").println(this.zzjc);
        for (Api<?> api : this.zzgi.keySet()) {
            printWriter.append((CharSequence) str).append((CharSequence) api.getName()).println(":");
            this.zzil.get(api.getClientKey()).dump(concat, fileDescriptor, printWriter, strArr);
        }
    }

    @Override // com.google.android.gms.common.api.internal.zzbp
    @GuardedBy("mLock")
    public final <A extends Api.AnyClient, R extends Result, T extends BaseImplementation.ApiMethodImpl<R, A>> T enqueue(@NonNull T t) {
        t.zzx();
        return (T) this.zzjc.enqueue(t);
    }

    @Override // com.google.android.gms.common.api.internal.zzbp
    @GuardedBy("mLock")
    public final <A extends Api.AnyClient, T extends BaseImplementation.ApiMethodImpl<? extends Result, A>> T execute(@NonNull T t) {
        t.zzx();
        return (T) this.zzjc.execute(t);
    }

    @Override // com.google.android.gms.common.api.internal.zzbp
    @Nullable
    @GuardedBy("mLock")
    public final ConnectionResult getConnectionResult(@NonNull Api<?> api) {
        Api.AnyClientKey<?> clientKey = api.getClientKey();
        if (this.zzil.containsKey(clientKey)) {
            if (this.zzil.get(clientKey).isConnected()) {
                return ConnectionResult.RESULT_SUCCESS;
            }
            if (this.zzjb.containsKey(clientKey)) {
                return this.zzjb.get(clientKey);
            }
            return null;
        }
        return null;
    }

    @Override // com.google.android.gms.common.api.internal.zzbp
    public final boolean isConnected() {
        return this.zzjc instanceof zzag;
    }

    @Override // com.google.android.gms.common.api.internal.zzbp
    public final boolean isConnecting() {
        return this.zzjc instanceof zzaj;
    }

    @Override // com.google.android.gms.common.api.internal.zzbp
    public final boolean maybeSignIn(SignInConnectionListener signInConnectionListener) {
        return false;
    }

    @Override // com.google.android.gms.common.api.internal.zzbp
    public final void maybeSignOut() {
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
    public final void onConnected(@Nullable Bundle bundle) {
        this.zzga.lock();
        try {
            this.zzjc.onConnected(bundle);
        } finally {
            this.zzga.unlock();
        }
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
    public final void onConnectionSuspended(int i) {
        this.zzga.lock();
        try {
            this.zzjc.onConnectionSuspended(i);
        } finally {
            this.zzga.unlock();
        }
    }

    @Override // com.google.android.gms.common.api.internal.zzq
    public final void zza(@NonNull ConnectionResult connectionResult, @NonNull Api<?> api, boolean z) {
        this.zzga.lock();
        try {
            this.zzjc.zza(connectionResult, api, z);
        } finally {
            this.zzga.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zza(zzbe zzbeVar) {
        this.zzja.sendMessage(this.zzja.obtainMessage(1, zzbeVar));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zzb(RuntimeException runtimeException) {
        this.zzja.sendMessage(this.zzja.obtainMessage(2, runtimeException));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zzbc() {
        this.zzga.lock();
        try {
            this.zzjc = new zzaj(this, this.zzgf, this.zzgi, this.zzgk, this.zzdh, this.zzga, this.mContext);
            this.zzjc.begin();
            this.zziz.signalAll();
        } finally {
            this.zzga.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zzbd() {
        this.zzga.lock();
        try {
            this.zzfq.zzaz();
            this.zzjc = new zzag(this);
            this.zzjc.begin();
            this.zziz.signalAll();
        } finally {
            this.zzga.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zzf(ConnectionResult connectionResult) {
        this.zzga.lock();
        try {
            this.zzjd = connectionResult;
            this.zzjc = new zzau(this);
            this.zzjc.begin();
            this.zziz.signalAll();
        } finally {
            this.zzga.unlock();
        }
    }

    @Override // com.google.android.gms.common.api.internal.zzbp
    @GuardedBy("mLock")
    public final void zzz() {
        if (isConnected()) {
            ((zzag) this.zzjc).zzap();
        }
    }
}
