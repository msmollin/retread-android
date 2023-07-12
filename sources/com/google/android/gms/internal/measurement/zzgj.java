package com.google.android.gms.internal.measurement;

import androidx.annotation.NonNull;
import com.facebook.common.time.Clock;
import com.google.android.gms.common.internal.Preconditions;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicLong;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzgj<V> extends FutureTask<V> implements Comparable<zzgj> {
    private final String zzaly;
    private final /* synthetic */ zzgg zzalz;
    private final long zzama;
    final boolean zzamb;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzgj(zzgg zzggVar, Runnable runnable, boolean z, String str) {
        super(runnable, null);
        AtomicLong atomicLong;
        this.zzalz = zzggVar;
        Preconditions.checkNotNull(str);
        atomicLong = zzgg.zzalx;
        this.zzama = atomicLong.getAndIncrement();
        this.zzaly = str;
        this.zzamb = false;
        if (this.zzama == Clock.MAX_TIME) {
            zzggVar.zzge().zzim().log("Tasks index overflow");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzgj(zzgg zzggVar, Callable<V> callable, boolean z, String str) {
        super(callable);
        AtomicLong atomicLong;
        this.zzalz = zzggVar;
        Preconditions.checkNotNull(str);
        atomicLong = zzgg.zzalx;
        this.zzama = atomicLong.getAndIncrement();
        this.zzaly = str;
        this.zzamb = z;
        if (this.zzama == Clock.MAX_TIME) {
            zzggVar.zzge().zzim().log("Tasks index overflow");
        }
    }

    @Override // java.lang.Comparable
    public final /* synthetic */ int compareTo(@NonNull zzgj zzgjVar) {
        zzgj zzgjVar2 = zzgjVar;
        if (this.zzamb != zzgjVar2.zzamb) {
            return this.zzamb ? -1 : 1;
        } else if (this.zzama < zzgjVar2.zzama) {
            return -1;
        } else {
            if (this.zzama > zzgjVar2.zzama) {
                return 1;
            }
            this.zzalz.zzge().zzin().zzg("Two tasks share the same index. index", Long.valueOf(this.zzama));
            return 0;
        }
    }

    @Override // java.util.concurrent.FutureTask
    protected final void setException(Throwable th) {
        this.zzalz.zzge().zzim().zzg(this.zzaly, th);
        if (th instanceof zzgh) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
        super.setException(th);
    }
}
