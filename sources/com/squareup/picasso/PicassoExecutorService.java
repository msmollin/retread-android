package com.squareup.picasso;

import android.net.NetworkInfo;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Utils;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class PicassoExecutorService extends ThreadPoolExecutor {
    private static final int DEFAULT_THREAD_COUNT = 3;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PicassoExecutorService() {
        super(3, 3, 0L, TimeUnit.MILLISECONDS, new PriorityBlockingQueue(), new Utils.PicassoThreadFactory());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public void adjustThreadCount(NetworkInfo networkInfo) {
        if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
            setThreadCount(3);
            return;
        }
        int type = networkInfo.getType();
        if (type != 6 && type != 9) {
            switch (type) {
                case 0:
                    int subtype = networkInfo.getSubtype();
                    switch (subtype) {
                        case 1:
                        case 2:
                            setThreadCount(1);
                            return;
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                            break;
                        default:
                            switch (subtype) {
                                case 12:
                                    break;
                                case 13:
                                case 14:
                                case 15:
                                    setThreadCount(3);
                                    return;
                                default:
                                    setThreadCount(3);
                                    return;
                            }
                    }
                    setThreadCount(2);
                    return;
                case 1:
                    break;
                default:
                    setThreadCount(3);
                    return;
            }
        }
        setThreadCount(4);
    }

    private void setThreadCount(int i) {
        setCorePoolSize(i);
        setMaximumPoolSize(i);
    }

    @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
    public Future<?> submit(Runnable runnable) {
        PicassoFutureTask picassoFutureTask = new PicassoFutureTask((BitmapHunter) runnable);
        execute(picassoFutureTask);
        return picassoFutureTask;
    }

    /* loaded from: classes.dex */
    private static final class PicassoFutureTask extends FutureTask<BitmapHunter> implements Comparable<PicassoFutureTask> {
        private final BitmapHunter hunter;

        PicassoFutureTask(BitmapHunter bitmapHunter) {
            super(bitmapHunter, null);
            this.hunter = bitmapHunter;
        }

        @Override // java.lang.Comparable
        public int compareTo(PicassoFutureTask picassoFutureTask) {
            int ordinal;
            int ordinal2;
            Picasso.Priority priority = this.hunter.getPriority();
            Picasso.Priority priority2 = picassoFutureTask.hunter.getPriority();
            if (priority == priority2) {
                ordinal = this.hunter.sequence;
                ordinal2 = picassoFutureTask.hunter.sequence;
            } else {
                ordinal = priority2.ordinal();
                ordinal2 = priority.ordinal();
            }
            return ordinal - ordinal2;
        }
    }
}
