package com.google.android.gms.common.api.internal;

import androidx.annotation.MainThread;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiActivity;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzm implements Runnable {
    private final zzl zzev;
    final /* synthetic */ zzk zzew;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzm(zzk zzkVar, zzl zzlVar) {
        this.zzew = zzkVar;
        this.zzev = zzlVar;
    }

    @Override // java.lang.Runnable
    @MainThread
    public final void run() {
        if (this.zzew.mStarted) {
            ConnectionResult connectionResult = this.zzev.getConnectionResult();
            if (connectionResult.hasResolution()) {
                this.zzew.mLifecycleFragment.startActivityForResult(GoogleApiActivity.zza(this.zzew.getActivity(), connectionResult.getResolution(), this.zzev.zzu(), false), 1);
            } else if (this.zzew.zzdg.isUserResolvableError(connectionResult.getErrorCode())) {
                this.zzew.zzdg.showErrorDialogFragment(this.zzew.getActivity(), this.zzew.mLifecycleFragment, connectionResult.getErrorCode(), 2, this.zzew);
            } else if (connectionResult.getErrorCode() != 18) {
                this.zzew.zza(connectionResult, this.zzev.zzu());
            } else {
                this.zzew.zzdg.registerCallbackOnUpdate(this.zzew.getActivity().getApplicationContext(), new zzn(this, this.zzew.zzdg.showUpdatingDialog(this.zzew.getActivity(), this.zzew)));
            }
        }
    }
}
