package com.google.firebase.analytics.connector;

import android.util.Log;
import com.google.firebase.analytics.connector.AnalyticsConnector;

/* loaded from: classes.dex */
final class zza implements AnalyticsConnector.AnalyticsConnectorHandle {
    private final /* synthetic */ String zzanh;
    private final /* synthetic */ AnalyticsConnectorImpl zzboh;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zza(AnalyticsConnectorImpl analyticsConnectorImpl, String str) {
        this.zzboh = analyticsConnectorImpl;
        this.zzanh = str;
    }

    @Override // com.google.firebase.analytics.connector.AnalyticsConnector.AnalyticsConnectorHandle
    public final void unregister() {
        boolean zzfc;
        zzfc = this.zzboh.zzfc(this.zzanh);
        if (!zzfc) {
            Log.d("FA-C", "No listener registered");
            return;
        }
        AnalyticsConnector.AnalyticsConnectorListener listener = this.zzboh.zzbog.get(this.zzanh).getListener();
        if (listener != null) {
            listener.onMessageTriggered(0, null);
        }
        this.zzboh.zzbog.remove(this.zzanh);
    }
}
