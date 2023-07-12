package com.google.android.gms.common.api.internal;

import androidx.annotation.WorkerThread;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.IAccountAccessor;
import java.util.Set;

@WorkerThread
/* loaded from: classes.dex */
public interface zzcb {
    void zza(IAccountAccessor iAccountAccessor, Set<Scope> set);

    void zzg(ConnectionResult connectionResult);
}
