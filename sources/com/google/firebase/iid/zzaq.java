package com.google.firebase.iid;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PowerManager;
import android.util.Log;
import androidx.exifinterface.media.ExifInterface;
import com.google.android.gms.common.util.VisibleForTesting;
import java.io.IOException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzaq implements Runnable {
    private final zzae zzal;
    private final FirebaseInstanceId zzaw;
    private final long zzcw;
    private final PowerManager.WakeLock zzcx = ((PowerManager) getContext().getSystemService("power")).newWakeLock(1, "fiid-sync");

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public zzaq(FirebaseInstanceId firebaseInstanceId, zzae zzaeVar, long j) {
        this.zzaw = firebaseInstanceId;
        this.zzal = zzaeVar;
        this.zzcw = j;
        this.zzcx.setReferenceCounted(false);
    }

    @VisibleForTesting
    private final boolean zzah() {
        zzap zzg = this.zzaw.zzg();
        if (zzg == null || zzg.zzj(this.zzal.zzy())) {
            try {
                String zzh = this.zzaw.zzh();
                if (zzh == null) {
                    Log.e("FirebaseInstanceId", "Token retrieval failed: null");
                    return false;
                }
                if (Log.isLoggable("FirebaseInstanceId", 3)) {
                    Log.d("FirebaseInstanceId", "Token successfully retrieved");
                }
                if (zzg == null || (zzg != null && !zzh.equals(zzg.zzcu))) {
                    Context context = getContext();
                    Intent intent = new Intent("com.google.firebase.iid.TOKEN_REFRESH");
                    Intent intent2 = new Intent("com.google.firebase.INSTANCE_ID_EVENT");
                    intent2.setClass(context, FirebaseInstanceIdReceiver.class);
                    intent2.putExtra("wrapped_intent", intent);
                    context.sendBroadcast(intent2);
                }
                return true;
            } catch (IOException | SecurityException e) {
                String valueOf = String.valueOf(e.getMessage());
                Log.e("FirebaseInstanceId", valueOf.length() != 0 ? "Token retrieval failed: ".concat(valueOf) : new String("Token retrieval failed: "));
                return false;
            }
        }
        return true;
    }

    @VisibleForTesting
    private final boolean zzai() {
        while (true) {
            synchronized (this.zzaw) {
                String zzaf = FirebaseInstanceId.zzi().zzaf();
                if (zzaf == null) {
                    Log.d("FirebaseInstanceId", "topic sync succeeded");
                    return true;
                } else if (!zzk(zzaf)) {
                    return false;
                } else {
                    FirebaseInstanceId.zzi().zzf(zzaf);
                }
            }
        }
    }

    private final boolean zzk(String str) {
        String str2;
        String str3;
        String[] split = str.split("!");
        if (split.length == 2) {
            String str4 = split[0];
            String str5 = split[1];
            char c = 65535;
            try {
                int hashCode = str4.hashCode();
                if (hashCode != 83) {
                    if (hashCode == 85 && str4.equals("U")) {
                        c = 1;
                    }
                } else if (str4.equals(ExifInterface.LATITUDE_SOUTH)) {
                    c = 0;
                }
                switch (c) {
                    case 0:
                        this.zzaw.zzb(str5);
                        if (FirebaseInstanceId.zzj()) {
                            str2 = "FirebaseInstanceId";
                            str3 = "subscribe operation succeeded";
                            Log.d(str2, str3);
                            break;
                        }
                        break;
                    case 1:
                        this.zzaw.zzc(str5);
                        if (FirebaseInstanceId.zzj()) {
                            str2 = "FirebaseInstanceId";
                            str3 = "unsubscribe operation succeeded";
                            Log.d(str2, str3);
                            break;
                        }
                        break;
                }
            } catch (IOException e) {
                String valueOf = String.valueOf(e.getMessage());
                Log.e("FirebaseInstanceId", valueOf.length() != 0 ? "Topic sync failed: ".concat(valueOf) : new String("Topic sync failed: "));
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Context getContext() {
        return this.zzaw.zze().getApplicationContext();
    }

    @Override // java.lang.Runnable
    public final void run() {
        FirebaseInstanceId firebaseInstanceId;
        this.zzcx.acquire();
        try {
            boolean z = true;
            this.zzaw.zza(true);
            if (this.zzal.zzx() == 0) {
                z = false;
            }
            if (z) {
                if (!zzaj()) {
                    new zzar(this).zzak();
                } else if (zzah() && zzai()) {
                    firebaseInstanceId = this.zzaw;
                } else {
                    this.zzaw.zza(this.zzcw);
                }
            }
            firebaseInstanceId = this.zzaw;
            firebaseInstanceId.zza(false);
        } finally {
            this.zzcx.release();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean zzaj() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService("connectivity");
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
