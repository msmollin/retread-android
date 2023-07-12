package com.google.android.gms.common.api.internal;

import androidx.annotation.BinderThread;
import com.google.android.gms.signin.internal.BaseSignInCallbacks;
import com.google.android.gms.signin.internal.SignInResponse;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
final class zzaq extends BaseSignInCallbacks {
    private final WeakReference<zzaj> zzhw;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzaq(zzaj zzajVar) {
        this.zzhw = new WeakReference<>(zzajVar);
    }

    @Override // com.google.android.gms.signin.internal.BaseSignInCallbacks, com.google.android.gms.signin.internal.ISignInCallbacks
    @BinderThread
    public final void onSignInComplete(SignInResponse signInResponse) {
        zzbd zzbdVar;
        zzaj zzajVar = this.zzhw.get();
        if (zzajVar == null) {
            return;
        }
        zzbdVar = zzajVar.zzhf;
        zzbdVar.zza(new zzar(this, zzajVar, zzajVar, signInResponse));
    }
}
