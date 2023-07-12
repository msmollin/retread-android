package com.google.android.gms.common;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import java.util.Map;

/* loaded from: classes.dex */
final class zza implements Continuation<Map<com.google.android.gms.common.api.internal.zzh<?>, String>, Void> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zza(GoogleApiAvailability googleApiAvailability) {
    }

    @Override // com.google.android.gms.tasks.Continuation
    public final /* synthetic */ Void then(@NonNull Task<Map<com.google.android.gms.common.api.internal.zzh<?>, String>> task) throws Exception {
        task.getResult();
        return null;
    }
}
