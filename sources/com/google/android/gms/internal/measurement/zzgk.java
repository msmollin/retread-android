package com.google.android.gms.internal.measurement;

import android.os.Process;
import com.google.android.gms.common.internal.Preconditions;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzgk extends Thread {
    private final /* synthetic */ zzgg zzalz;
    private final Object zzamc;
    private final BlockingQueue<zzgj<?>> zzamd;

    public zzgk(zzgg zzggVar, String str, BlockingQueue<zzgj<?>> blockingQueue) {
        this.zzalz = zzggVar;
        Preconditions.checkNotNull(str);
        Preconditions.checkNotNull(blockingQueue);
        this.zzamc = new Object();
        this.zzamd = blockingQueue;
        setName(str);
    }

    private final void zza(InterruptedException interruptedException) {
        this.zzalz.zzge().zzip().zzg(String.valueOf(getName()).concat(" was interrupted"), interruptedException);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public final void run() {
        Object obj;
        Semaphore semaphore;
        Object obj2;
        zzgk zzgkVar;
        zzgk zzgkVar2;
        Object obj3;
        Object obj4;
        Semaphore semaphore2;
        Object obj5;
        zzgk zzgkVar3;
        zzgk zzgkVar4;
        boolean z;
        Semaphore semaphore3;
        boolean z2 = false;
        while (!z2) {
            try {
                semaphore3 = this.zzalz.zzalv;
                semaphore3.acquire();
                z2 = true;
            } catch (InterruptedException e) {
                zza(e);
            }
        }
        try {
            int threadPriority = Process.getThreadPriority(Process.myTid());
            while (true) {
                zzgj<?> poll = this.zzamd.poll();
                if (poll == null) {
                    synchronized (this.zzamc) {
                        if (this.zzamd.peek() == null) {
                            z = this.zzalz.zzalw;
                            if (!z) {
                                try {
                                    this.zzamc.wait(30000L);
                                } catch (InterruptedException e2) {
                                    zza(e2);
                                }
                            }
                        }
                    }
                    obj3 = this.zzalz.zzalu;
                    synchronized (obj3) {
                        if (this.zzamd.peek() == null) {
                            break;
                        }
                    }
                } else {
                    Process.setThreadPriority(poll.zzamb ? threadPriority : 10);
                    poll.run();
                }
            }
            obj4 = this.zzalz.zzalu;
            synchronized (obj4) {
                semaphore2 = this.zzalz.zzalv;
                semaphore2.release();
                obj5 = this.zzalz.zzalu;
                obj5.notifyAll();
                zzgkVar3 = this.zzalz.zzalo;
                if (this == zzgkVar3) {
                    zzgg.zza(this.zzalz, null);
                } else {
                    zzgkVar4 = this.zzalz.zzalp;
                    if (this == zzgkVar4) {
                        zzgg.zzb(this.zzalz, null);
                    } else {
                        this.zzalz.zzge().zzim().log("Current scheduler thread is neither worker nor network");
                    }
                }
            }
        } catch (Throwable th) {
            obj = this.zzalz.zzalu;
            synchronized (obj) {
                semaphore = this.zzalz.zzalv;
                semaphore.release();
                obj2 = this.zzalz.zzalu;
                obj2.notifyAll();
                zzgkVar = this.zzalz.zzalo;
                if (this != zzgkVar) {
                    zzgkVar2 = this.zzalz.zzalp;
                    if (this == zzgkVar2) {
                        zzgg.zzb(this.zzalz, null);
                    } else {
                        this.zzalz.zzge().zzim().log("Current scheduler thread is neither worker nor network");
                    }
                } else {
                    zzgg.zza(this.zzalz, null);
                }
                throw th;
            }
        }
    }

    public final void zzjn() {
        synchronized (this.zzamc) {
            this.zzamc.notifyAll();
        }
    }
}
