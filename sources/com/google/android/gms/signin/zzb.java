package com.google.android.gms.signin;

import android.content.Context;
import android.os.Looper;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.signin.SignIn;
import com.google.android.gms.signin.internal.SignInClientImpl;

/* loaded from: classes.dex */
final class zzb extends Api.AbstractClientBuilder<SignInClientImpl, SignIn.SignInOptionsInternal> {
    @Override // com.google.android.gms.common.api.Api.AbstractClientBuilder
    public final /* synthetic */ SignInClientImpl buildClient(Context context, Looper looper, ClientSettings clientSettings, SignIn.SignInOptionsInternal signInOptionsInternal, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        return new SignInClientImpl(context, looper, false, clientSettings, signInOptionsInternal.getSignInOptionsBundle(), connectionCallbacks, onConnectionFailedListener);
    }
}
