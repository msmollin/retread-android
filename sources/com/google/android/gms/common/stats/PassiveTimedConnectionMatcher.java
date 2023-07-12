package com.google.android.gms.common.stats;

import android.os.SystemClock;
import android.util.Log;
import androidx.collection.SimpleArrayMap;

/* loaded from: classes.dex */
public class PassiveTimedConnectionMatcher {
    private final long zzym;
    private final int zzyn;
    private final SimpleArrayMap<String, Long> zzyo;

    public PassiveTimedConnectionMatcher() {
        this.zzym = 60000L;
        this.zzyn = 10;
        this.zzyo = new SimpleArrayMap<>(10);
    }

    public PassiveTimedConnectionMatcher(int i, long j) {
        this.zzym = j;
        this.zzyn = i;
        this.zzyo = new SimpleArrayMap<>();
    }

    public Long get(String str) {
        Long l;
        synchronized (this) {
            l = this.zzyo.get(str);
        }
        return l;
    }

    public Long put(String str) {
        Long put;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j = this.zzym;
        synchronized (this) {
            while (this.zzyo.size() >= this.zzyn) {
                for (int size = this.zzyo.size() - 1; size >= 0; size--) {
                    if (elapsedRealtime - this.zzyo.valueAt(size).longValue() > j) {
                        this.zzyo.removeAt(size);
                    }
                }
                j /= 2;
                int i = this.zzyn;
                StringBuilder sb = new StringBuilder(94);
                sb.append("The max capacity ");
                sb.append(i);
                sb.append(" is not enough. Current durationThreshold is: ");
                sb.append(j);
                Log.w("ConnectionTracker", sb.toString());
            }
            put = this.zzyo.put(str, Long.valueOf(elapsedRealtime));
        }
        return put;
    }

    public boolean remove(String str) {
        boolean z;
        synchronized (this) {
            z = this.zzyo.remove(str) != null;
        }
        return z;
    }

    public boolean removeByPrefix(String str) {
        boolean z;
        synchronized (this) {
            z = false;
            for (int i = 0; i < size(); i++) {
                String keyAt = this.zzyo.keyAt(i);
                if (keyAt != null && keyAt.startsWith(str)) {
                    this.zzyo.remove(keyAt);
                    z = true;
                }
            }
        }
        return z;
    }

    public int size() {
        int size;
        synchronized (this) {
            size = this.zzyo.size();
        }
        return size;
    }
}
