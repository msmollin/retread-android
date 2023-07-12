package com.google.firebase.iid;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.VisibleForTesting;
import androidx.collection.SimpleArrayMap;
import com.google.android.gms.common.util.CrashUtils;
import com.yalantis.ucrop.view.CropImageView;
import java.util.ArrayDeque;
import java.util.Queue;
import javax.annotation.concurrent.GuardedBy;

/* loaded from: classes.dex */
public final class zzan {
    private static zzan zzcl;
    @GuardedBy("serviceClassNames")
    private final SimpleArrayMap<String, String> zzcm = new SimpleArrayMap<>();
    private Boolean zzcn = null;
    @VisibleForTesting
    final Queue<Intent> zzco = new ArrayDeque();
    @VisibleForTesting
    private final Queue<Intent> zzcp = new ArrayDeque();

    private zzan() {
    }

    public static PendingIntent zza(Context context, int i, Intent intent, int i2) {
        Intent intent2 = new Intent(context, FirebaseInstanceIdReceiver.class);
        intent2.setAction("com.google.firebase.MESSAGING_EVENT");
        intent2.putExtra("wrapped_intent", intent);
        return PendingIntent.getBroadcast(context, i, intent2, CrashUtils.ErrorDialogData.SUPPRESSED);
    }

    public static synchronized zzan zzad() {
        zzan zzanVar;
        synchronized (zzan.class) {
            if (zzcl == null) {
                zzcl = new zzan();
            }
            zzanVar = zzcl;
        }
        return zzanVar;
    }

    /* JADX WARN: Removed duplicated region for block: B:44:0x00db A[Catch: IllegalStateException -> 0x0110, SecurityException -> 0x0138, TryCatch #4 {IllegalStateException -> 0x0110, SecurityException -> 0x0138, blocks: (B:42:0x00d7, B:44:0x00db, B:47:0x00e4, B:48:0x00ea, B:50:0x00f2, B:53:0x0104, B:51:0x00f7), top: B:71:0x00d7 }] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00f2 A[Catch: IllegalStateException -> 0x0110, SecurityException -> 0x0138, TryCatch #4 {IllegalStateException -> 0x0110, SecurityException -> 0x0138, blocks: (B:42:0x00d7, B:44:0x00db, B:47:0x00e4, B:48:0x00ea, B:50:0x00f2, B:53:0x0104, B:51:0x00f7), top: B:71:0x00d7 }] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00f7 A[Catch: IllegalStateException -> 0x0110, SecurityException -> 0x0138, TryCatch #4 {IllegalStateException -> 0x0110, SecurityException -> 0x0138, blocks: (B:42:0x00d7, B:44:0x00db, B:47:0x00e4, B:48:0x00ea, B:50:0x00f2, B:53:0x0104, B:51:0x00f7), top: B:71:0x00d7 }] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0104 A[Catch: IllegalStateException -> 0x0110, SecurityException -> 0x0138, TRY_LEAVE, TryCatch #4 {IllegalStateException -> 0x0110, SecurityException -> 0x0138, blocks: (B:42:0x00d7, B:44:0x00db, B:47:0x00e4, B:48:0x00ea, B:50:0x00f2, B:53:0x0104, B:51:0x00f7), top: B:71:0x00d7 }] */
    /* JADX WARN: Removed duplicated region for block: B:56:0x010e A[RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private final int zzb(android.content.Context r7, android.content.Intent r8) {
        /*
            Method dump skipped, instructions count: 326
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.iid.zzan.zzb(android.content.Context, android.content.Intent):int");
    }

    public final int zza(Context context, String str, Intent intent) {
        char c;
        Queue<Intent> queue;
        int hashCode = str.hashCode();
        if (hashCode != -842411455) {
            if (hashCode == 41532704 && str.equals("com.google.firebase.MESSAGING_EVENT")) {
                c = 1;
            }
            c = 65535;
        } else {
            if (str.equals("com.google.firebase.INSTANCE_ID_EVENT")) {
                c = 0;
            }
            c = 65535;
        }
        switch (c) {
            case 0:
                queue = this.zzco;
                break;
            case 1:
                queue = this.zzcp;
                break;
            default:
                String valueOf = String.valueOf(str);
                Log.w("FirebaseInstanceId", valueOf.length() != 0 ? "Unknown service action: ".concat(valueOf) : new String("Unknown service action: "));
                return CropImageView.DEFAULT_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION;
        }
        queue.offer(intent);
        Intent intent2 = new Intent(str);
        intent2.setPackage(context.getPackageName());
        return zzb(context, intent2);
    }

    public final Intent zzae() {
        return this.zzcp.poll();
    }
}
