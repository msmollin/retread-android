package com.google.firebase.iid;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.FirebaseApp;
import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzl implements zzo {
    private final FirebaseApp zzak;
    private final zzae zzal;
    private final FirebaseInstanceId zzaw;
    private final zzal zzax;
    private final ScheduledThreadPoolExecutor zzay = new ScheduledThreadPoolExecutor(1);

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzl(FirebaseApp firebaseApp, FirebaseInstanceId firebaseInstanceId, zzae zzaeVar) {
        this.zzak = firebaseApp;
        this.zzaw = firebaseInstanceId;
        this.zzal = zzaeVar;
        this.zzax = new zzal(firebaseApp.getApplicationContext(), zzaeVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final String zza(Bundle bundle) throws IOException {
        if (bundle != null) {
            String string = bundle.getString("registration_id");
            if (string != null) {
                return string;
            }
            String string2 = bundle.getString("unregistered");
            if (string2 != null) {
                return string2;
            }
            String string3 = bundle.getString("error");
            if ("RST".equals(string3)) {
                this.zzaw.zzk();
                throw new IOException("INSTANCE_ID_RESET");
            } else if (string3 != null) {
                throw new IOException(string3);
            } else {
                String valueOf = String.valueOf(bundle);
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 21);
                sb.append("Unexpected response: ");
                sb.append(valueOf);
                Log.w("FirebaseInstanceId", sb.toString(), new Throwable());
                throw new IOException("SERVICE_NOT_AVAILABLE");
            }
        }
        throw new IOException("SERVICE_NOT_AVAILABLE");
    }

    private final Bundle zzb(String str, String str2, Bundle bundle) {
        bundle.putString("scope", str2);
        bundle.putString("sender", str);
        bundle.putString("subtype", str);
        bundle.putString("appid", FirebaseInstanceId.zzf());
        bundle.putString("gmp_app_id", this.zzak.getOptions().getApplicationId());
        bundle.putString("gmsv", Integer.toString(this.zzal.zzaa()));
        bundle.putString("osv", Integer.toString(Build.VERSION.SDK_INT));
        bundle.putString("app_ver", this.zzal.zzy());
        bundle.putString("app_ver_name", this.zzal.zzz());
        bundle.putString("cliv", "fiid-12451000");
        return bundle;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String zza(String str, String str2, Bundle bundle) throws IOException {
        zzb(str, str2, bundle);
        return zza(this.zzax.zzc(bundle));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ void zza(Bundle bundle, TaskCompletionSource taskCompletionSource) {
        try {
            taskCompletionSource.setResult(this.zzax.zzc(bundle));
        } catch (IOException e) {
            taskCompletionSource.setException(e);
        }
    }

    @Override // com.google.firebase.iid.zzo
    public final Task<String> zzb(String str, String str2) {
        final Bundle bundle = new Bundle();
        zzb(str, str2, bundle);
        final TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        this.zzay.execute(new Runnable(this, bundle, taskCompletionSource) { // from class: com.google.firebase.iid.zzm
            private final zzl zzaz;
            private final Bundle zzba;
            private final TaskCompletionSource zzbb;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.zzaz = this;
                this.zzba = bundle;
                this.zzbb = taskCompletionSource;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.zzaz.zza(this.zzba, this.zzbb);
            }
        });
        return taskCompletionSource.getTask().continueWith(this.zzay, new zzn(this));
    }
}
