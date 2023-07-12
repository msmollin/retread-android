package com.google.android.gms.common.api.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.util.concurrent.HandlerExecutor;
import com.google.android.gms.signin.SignInClient;
import com.google.android.gms.signin.SignInOptions;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import javax.annotation.concurrent.GuardedBy;

/* loaded from: classes.dex */
public final class zzw implements zzbp {
    private final Looper zzcn;
    private final GoogleApiManager zzcq;
    private final Lock zzga;
    private final ClientSettings zzgf;
    private final Map<Api<?>, Boolean> zzgi;
    private final zzav zzgj;
    private final GoogleApiAvailabilityLight zzgk;
    private final Condition zzgl;
    private final boolean zzgm;
    private final boolean zzgn;
    @GuardedBy("mLock")
    private boolean zzgp;
    @GuardedBy("mLock")
    private Map<zzh<?>, ConnectionResult> zzgq;
    @GuardedBy("mLock")
    private Map<zzh<?>, ConnectionResult> zzgr;
    @GuardedBy("mLock")
    private zzz zzgs;
    @GuardedBy("mLock")
    private ConnectionResult zzgt;
    private final Map<Api.AnyClientKey<?>, zzv<?>> zzgg = new HashMap();
    private final Map<Api.AnyClientKey<?>, zzv<?>> zzgh = new HashMap();
    private final Queue<BaseImplementation.ApiMethodImpl<?, ?>> zzgo = new LinkedList();

    public zzw(Context context, Lock lock, Looper looper, GoogleApiAvailabilityLight googleApiAvailabilityLight, Map<Api.AnyClientKey<?>, Api.Client> map, ClientSettings clientSettings, Map<Api<?>, Boolean> map2, Api.AbstractClientBuilder<? extends SignInClient, SignInOptions> abstractClientBuilder, ArrayList<zzp> arrayList, zzav zzavVar, boolean z) {
        boolean z2;
        boolean z3;
        boolean z4;
        this.zzga = lock;
        this.zzcn = looper;
        this.zzgl = lock.newCondition();
        this.zzgk = googleApiAvailabilityLight;
        this.zzgj = zzavVar;
        this.zzgi = map2;
        this.zzgf = clientSettings;
        this.zzgm = z;
        HashMap hashMap = new HashMap();
        for (Api<?> api : map2.keySet()) {
            hashMap.put(api.getClientKey(), api);
        }
        HashMap hashMap2 = new HashMap();
        ArrayList<zzp> arrayList2 = arrayList;
        int size = arrayList2.size();
        int i = 0;
        while (i < size) {
            zzp zzpVar = arrayList2.get(i);
            i++;
            zzp zzpVar2 = zzpVar;
            hashMap2.put(zzpVar2.mApi, zzpVar2);
        }
        boolean z5 = true;
        boolean z6 = true;
        boolean z7 = false;
        boolean z8 = false;
        for (Map.Entry<Api.AnyClientKey<?>, Api.Client> entry : map.entrySet()) {
            Api api2 = (Api) hashMap.get(entry.getKey());
            Api.Client value = entry.getValue();
            if (!value.requiresGooglePlayServices()) {
                z2 = z7;
                z3 = z8;
                z4 = false;
            } else if (this.zzgi.get(api2).booleanValue()) {
                z4 = z6;
                z3 = z8;
                z2 = true;
            } else {
                z4 = z6;
                z3 = true;
                z2 = true;
            }
            zzv<?> zzvVar = new zzv<>(context, api2, looper, value, (zzp) hashMap2.get(api2), clientSettings, abstractClientBuilder);
            this.zzgg.put(entry.getKey(), zzvVar);
            if (value.requiresSignIn()) {
                this.zzgh.put(entry.getKey(), zzvVar);
            }
            z8 = z3;
            z6 = z4;
            z7 = z2;
        }
        this.zzgn = (!z7 || z6 || z8) ? false : false;
        this.zzcq = GoogleApiManager.zzbf();
    }

    @Nullable
    private final ConnectionResult zza(@NonNull Api.AnyClientKey<?> anyClientKey) {
        this.zzga.lock();
        try {
            zzv<?> zzvVar = this.zzgg.get(anyClientKey);
            if (this.zzgq == null || zzvVar == null) {
                this.zzga.unlock();
                return null;
            }
            return this.zzgq.get(zzvVar.zzm());
        } finally {
            this.zzga.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean zza(zzv<?> zzvVar, ConnectionResult connectionResult) {
        return !connectionResult.isSuccess() && !connectionResult.hasResolution() && this.zzgi.get(zzvVar.getApi()).booleanValue() && zzvVar.zzae().requiresGooglePlayServices() && this.zzgk.isUserResolvableError(connectionResult.getErrorCode());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean zza(zzw zzwVar, boolean z) {
        zzwVar.zzgp = false;
        return false;
    }

    private final boolean zzaf() {
        this.zzga.lock();
        try {
            if (this.zzgp && this.zzgm) {
                for (Api.AnyClientKey<?> anyClientKey : this.zzgh.keySet()) {
                    ConnectionResult zza = zza(anyClientKey);
                    if (zza != null) {
                        if (!zza.isSuccess()) {
                        }
                    }
                }
                this.zzga.unlock();
                return true;
            }
            return false;
        } finally {
            this.zzga.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy("mLock")
    public final void zzag() {
        Set<Scope> hashSet;
        zzav zzavVar;
        if (this.zzgf == null) {
            zzavVar = this.zzgj;
            hashSet = Collections.emptySet();
        } else {
            hashSet = new HashSet<>(this.zzgf.getRequiredScopes());
            Map<Api<?>, ClientSettings.OptionalApiSettings> optionalApiSettings = this.zzgf.getOptionalApiSettings();
            for (Api<?> api : optionalApiSettings.keySet()) {
                ConnectionResult connectionResult = getConnectionResult(api);
                if (connectionResult != null && connectionResult.isSuccess()) {
                    hashSet.addAll(optionalApiSettings.get(api).mScopes);
                }
            }
            zzavVar = this.zzgj;
        }
        zzavVar.zzim = hashSet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy("mLock")
    public final void zzah() {
        while (!this.zzgo.isEmpty()) {
            execute(this.zzgo.remove());
        }
        this.zzgj.zzb((Bundle) null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Nullable
    @GuardedBy("mLock")
    public final ConnectionResult zzai() {
        ConnectionResult connectionResult = null;
        int i = 0;
        int i2 = 0;
        ConnectionResult connectionResult2 = null;
        for (zzv<?> zzvVar : this.zzgg.values()) {
            Api<?> api = zzvVar.getApi();
            ConnectionResult connectionResult3 = this.zzgq.get(zzvVar.zzm());
            if (!connectionResult3.isSuccess() && (!this.zzgi.get(api).booleanValue() || connectionResult3.hasResolution() || this.zzgk.isUserResolvableError(connectionResult3.getErrorCode()))) {
                if (connectionResult3.getErrorCode() == 4 && this.zzgm) {
                    int priority = api.zzj().getPriority();
                    if (connectionResult2 == null || i2 > priority) {
                        connectionResult2 = connectionResult3;
                        i2 = priority;
                    }
                } else {
                    int priority2 = api.zzj().getPriority();
                    if (connectionResult == null || i > priority2) {
                        connectionResult = connectionResult3;
                        i = priority2;
                    }
                }
            }
        }
        return (connectionResult == null || connectionResult2 == null || i <= i2) ? connectionResult : connectionResult2;
    }

    private final <T extends BaseImplementation.ApiMethodImpl<? extends Result, ? extends Api.AnyClient>> boolean zzb(@NonNull T t) {
        Api.AnyClientKey<?> clientKey = t.getClientKey();
        ConnectionResult zza = zza(clientKey);
        if (zza == null || zza.getErrorCode() != 4) {
            return false;
        }
        t.setFailedResult(new Status(4, null, this.zzcq.zza(this.zzgg.get(clientKey).zzm(), System.identityHashCode(this.zzgj))));
        return true;
    }

    @Override // com.google.android.gms.common.api.internal.zzbp
    @GuardedBy("mLock")
    public final ConnectionResult blockingConnect() {
        connect();
        while (isConnecting()) {
            try {
                this.zzgl.await();
            } catch (InterruptedException unused) {
                Thread.currentThread().interrupt();
                return new ConnectionResult(15, null);
            }
        }
        return isConnected() ? ConnectionResult.RESULT_SUCCESS : this.zzgt != null ? this.zzgt : new ConnectionResult(13, null);
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
                nanos = this.zzgl.awaitNanos(nanos);
            } catch (InterruptedException unused) {
                Thread.currentThread().interrupt();
                return new ConnectionResult(15, null);
            }
            Thread.currentThread().interrupt();
            return new ConnectionResult(15, null);
        }
        return isConnected() ? ConnectionResult.RESULT_SUCCESS : this.zzgt != null ? this.zzgt : new ConnectionResult(13, null);
    }

    @Override // com.google.android.gms.common.api.internal.zzbp
    public final void connect() {
        this.zzga.lock();
        try {
            if (!this.zzgp) {
                this.zzgp = true;
                this.zzgq = null;
                this.zzgr = null;
                this.zzgs = null;
                this.zzgt = null;
                this.zzcq.zzr();
                this.zzcq.zza(this.zzgg.values()).addOnCompleteListener(new HandlerExecutor(this.zzcn), new zzy(this));
            }
        } finally {
            this.zzga.unlock();
        }
    }

    @Override // com.google.android.gms.common.api.internal.zzbp
    public final void disconnect() {
        this.zzga.lock();
        try {
            this.zzgp = false;
            this.zzgq = null;
            this.zzgr = null;
            if (this.zzgs != null) {
                this.zzgs.cancel();
                this.zzgs = null;
            }
            this.zzgt = null;
            while (!this.zzgo.isEmpty()) {
                BaseImplementation.ApiMethodImpl<?, ?> remove = this.zzgo.remove();
                remove.zza((zzcn) null);
                remove.cancel();
            }
            this.zzgl.signalAll();
        } finally {
            this.zzga.unlock();
        }
    }

    @Override // com.google.android.gms.common.api.internal.zzbp
    public final void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    @Override // com.google.android.gms.common.api.internal.zzbp
    public final <A extends Api.AnyClient, R extends Result, T extends BaseImplementation.ApiMethodImpl<R, A>> T enqueue(@NonNull T t) {
        if (this.zzgm && zzb((zzw) t)) {
            return t;
        }
        if (isConnected()) {
            this.zzgj.zzir.zzb(t);
            return (T) this.zzgg.get(t.getClientKey()).doRead((zzv<?>) t);
        }
        this.zzgo.add(t);
        return t;
    }

    @Override // com.google.android.gms.common.api.internal.zzbp
    public final <A extends Api.AnyClient, T extends BaseImplementation.ApiMethodImpl<? extends Result, A>> T execute(@NonNull T t) {
        Api.AnyClientKey<A> clientKey = t.getClientKey();
        if (this.zzgm && zzb((zzw) t)) {
            return t;
        }
        this.zzgj.zzir.zzb(t);
        return (T) this.zzgg.get(clientKey).doWrite((zzv<?>) t);
    }

    @Override // com.google.android.gms.common.api.internal.zzbp
    @Nullable
    public final ConnectionResult getConnectionResult(@NonNull Api<?> api) {
        return zza(api.getClientKey());
    }

    @Override // com.google.android.gms.common.api.internal.zzbp
    public final boolean isConnected() {
        boolean z;
        this.zzga.lock();
        try {
            if (this.zzgq != null) {
                if (this.zzgt == null) {
                    z = true;
                    return z;
                }
            }
            z = false;
            return z;
        } finally {
            this.zzga.unlock();
        }
    }

    @Override // com.google.android.gms.common.api.internal.zzbp
    public final boolean isConnecting() {
        boolean z;
        this.zzga.lock();
        try {
            if (this.zzgq == null) {
                if (this.zzgp) {
                    z = true;
                    return z;
                }
            }
            z = false;
            return z;
        } finally {
            this.zzga.unlock();
        }
    }

    @Override // com.google.android.gms.common.api.internal.zzbp
    public final boolean maybeSignIn(SignInConnectionListener signInConnectionListener) {
        this.zzga.lock();
        try {
            if (!this.zzgp || zzaf()) {
                this.zzga.unlock();
                return false;
            }
            this.zzcq.zzr();
            this.zzgs = new zzz(this, signInConnectionListener);
            this.zzcq.zza(this.zzgh.values()).addOnCompleteListener(new HandlerExecutor(this.zzcn), this.zzgs);
            this.zzga.unlock();
            return true;
        } catch (Throwable th) {
            this.zzga.unlock();
            throw th;
        }
    }

    @Override // com.google.android.gms.common.api.internal.zzbp
    public final void maybeSignOut() {
        this.zzga.lock();
        try {
            this.zzcq.maybeSignOut();
            if (this.zzgs != null) {
                this.zzgs.cancel();
                this.zzgs = null;
            }
            if (this.zzgr == null) {
                this.zzgr = new ArrayMap(this.zzgh.size());
            }
            ConnectionResult connectionResult = new ConnectionResult(4);
            for (zzv<?> zzvVar : this.zzgh.values()) {
                this.zzgr.put(zzvVar.zzm(), connectionResult);
            }
            if (this.zzgq != null) {
                this.zzgq.putAll(this.zzgr);
            }
        } finally {
            this.zzga.unlock();
        }
    }

    @Override // com.google.android.gms.common.api.internal.zzbp
    public final void zzz() {
    }
}
