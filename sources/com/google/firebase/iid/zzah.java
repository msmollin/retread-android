package com.google.firebase.iid;

import android.util.Log;
import android.util.Pair;
import androidx.annotation.WorkerThread;
import androidx.collection.ArrayMap;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.annotation.concurrent.GuardedBy;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzah {
    @GuardedBy("this")
    private final Map<Pair<String, String>, TaskCompletionSource<String>> zzca = new ArrayMap();

    /* JADX INFO: Access modifiers changed from: private */
    public static String zza(TaskCompletionSource<String> taskCompletionSource) throws IOException {
        try {
            return (String) Tasks.await(taskCompletionSource.getTask());
        } catch (InterruptedException e) {
            throw new IOException(e);
        } catch (ExecutionException e2) {
            Throwable cause = e2.getCause();
            if (cause instanceof IOException) {
                throw ((IOException) cause);
            }
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            }
            throw new IOException(cause);
        }
    }

    private static String zza(zzak zzakVar, TaskCompletionSource<String> taskCompletionSource) throws IOException {
        try {
            String zzp = zzakVar.zzp();
            taskCompletionSource.setResult(zzp);
            return zzp;
        } catch (IOException | RuntimeException e) {
            taskCompletionSource.setException(e);
            throw e;
        }
    }

    private final synchronized zzak zzb(String str, String str2, final zzak zzakVar) {
        final Pair<String, String> pair = new Pair<>(str, str2);
        final TaskCompletionSource<String> taskCompletionSource = this.zzca.get(pair);
        if (taskCompletionSource != null) {
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                String valueOf = String.valueOf(pair);
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 29);
                sb.append("Joining ongoing request for: ");
                sb.append(valueOf);
                Log.d("FirebaseInstanceId", sb.toString());
            }
            return new zzak(taskCompletionSource) { // from class: com.google.firebase.iid.zzai
                private final TaskCompletionSource zzcb;

                /* JADX INFO: Access modifiers changed from: package-private */
                {
                    this.zzcb = taskCompletionSource;
                }

                @Override // com.google.firebase.iid.zzak
                public final String zzp() {
                    String zza;
                    zza = zzah.zza(this.zzcb);
                    return zza;
                }
            };
        }
        if (Log.isLoggable("FirebaseInstanceId", 3)) {
            String valueOf2 = String.valueOf(pair);
            StringBuilder sb2 = new StringBuilder(String.valueOf(valueOf2).length() + 24);
            sb2.append("Making new request for: ");
            sb2.append(valueOf2);
            Log.d("FirebaseInstanceId", sb2.toString());
        }
        final TaskCompletionSource<String> taskCompletionSource2 = new TaskCompletionSource<>();
        this.zzca.put(pair, taskCompletionSource2);
        return new zzak(this, zzakVar, taskCompletionSource2, pair) { // from class: com.google.firebase.iid.zzaj
            private final TaskCompletionSource zzbb;
            private final zzah zzcc;
            private final zzak zzcd;
            private final Pair zzce;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.zzcc = this;
                this.zzcd = zzakVar;
                this.zzbb = taskCompletionSource2;
                this.zzce = pair;
            }

            @Override // com.google.firebase.iid.zzak
            public final String zzp() {
                return this.zzcc.zza(this.zzcd, this.zzbb, this.zzce);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ String zza(zzak zzakVar, TaskCompletionSource taskCompletionSource, Pair pair) throws IOException {
        try {
            String zza = zza(zzakVar, taskCompletionSource);
            synchronized (this) {
                this.zzca.remove(pair);
            }
            return zza;
        } catch (Throwable th) {
            synchronized (this) {
                this.zzca.remove(pair);
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final String zza(String str, String str2, zzak zzakVar) throws IOException {
        return zzb(str, str2, zzakVar).zzp();
    }
}
