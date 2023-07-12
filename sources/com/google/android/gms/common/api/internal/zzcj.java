package com.google.android.gms.common.api.internal;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzcj extends Handler {
    private final /* synthetic */ zzch zzml;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzcj(zzch zzchVar, Looper looper) {
        super(looper);
        this.zzml = zzchVar;
    }

    @Override // android.os.Handler
    public final void handleMessage(Message message) {
        Object obj;
        zzch zzchVar;
        zzch zzchVar2;
        Status status;
        switch (message.what) {
            case 0:
                PendingResult<?> pendingResult = (PendingResult) message.obj;
                obj = this.zzml.zzfa;
                synchronized (obj) {
                    try {
                        if (pendingResult == null) {
                            zzchVar2 = this.zzml.zzme;
                            status = new Status(13, "Transform returned null");
                        } else if (pendingResult instanceof zzbx) {
                            zzchVar2 = this.zzml.zzme;
                            status = ((zzbx) pendingResult).getStatus();
                        } else {
                            zzchVar = this.zzml.zzme;
                            zzchVar.zza(pendingResult);
                        }
                        zzchVar2.zzd(status);
                    } catch (Throwable th) {
                        throw th;
                    }
                }
                return;
            case 1:
                RuntimeException runtimeException = (RuntimeException) message.obj;
                String valueOf = String.valueOf(runtimeException.getMessage());
                Log.e("TransformedResultImpl", valueOf.length() != 0 ? "Runtime exception on the transformation worker thread: ".concat(valueOf) : new String("Runtime exception on the transformation worker thread: "));
                throw runtimeException;
            default:
                int i = message.what;
                StringBuilder sb = new StringBuilder(70);
                sb.append("TransformationResultHandler received unknown message type: ");
                sb.append(i);
                Log.e("TransformedResultImpl", sb.toString());
                return;
        }
    }
}
