package com.google.android.gms.dynamic;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

/* loaded from: classes.dex */
final class zze implements View.OnClickListener {
    private final /* synthetic */ Context val$context;
    private final /* synthetic */ Intent zzabl;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zze(Context context, Intent intent) {
        this.val$context = context;
        this.zzabl = intent;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        try {
            this.val$context.startActivity(this.zzabl);
        } catch (ActivityNotFoundException e) {
            Log.e("DeferredLifecycleHelper", "Failed to start resolution intent", e);
        }
    }
}
