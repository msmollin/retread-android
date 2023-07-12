package com.google.android.gms.common.api.internal;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.internal.GoogleApiManager;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.signin.SignInClient;
import com.google.android.gms.signin.SignInOptions;

/* loaded from: classes.dex */
public final class zzv<O extends Api.ApiOptions> extends GoogleApi<O> {
    private final Api.AbstractClientBuilder<? extends SignInClient, SignInOptions> zzdh;
    private final Api.Client zzgd;
    private final zzp zzge;
    private final ClientSettings zzgf;

    public zzv(@NonNull Context context, Api<O> api, Looper looper, @NonNull Api.Client client, @NonNull zzp zzpVar, ClientSettings clientSettings, Api.AbstractClientBuilder<? extends SignInClient, SignInOptions> abstractClientBuilder) {
        super(context, api, looper);
        this.zzgd = client;
        this.zzge = zzpVar;
        this.zzgf = clientSettings;
        this.zzdh = abstractClientBuilder;
        this.zzcq.zza(this);
    }

    @Override // com.google.android.gms.common.api.GoogleApi
    public final Api.Client zza(Looper looper, GoogleApiManager.zza<O> zzaVar) {
        this.zzge.zza(zzaVar);
        return this.zzgd;
    }

    @Override // com.google.android.gms.common.api.GoogleApi
    public final zzby zza(Context context, Handler handler) {
        return new zzby(context, handler, this.zzgf, this.zzdh);
    }

    public final Api.Client zzae() {
        return this.zzgd;
    }
}
