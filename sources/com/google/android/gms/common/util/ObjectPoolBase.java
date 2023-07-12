package com.google.android.gms.common.util;

import java.util.ArrayList;

/* loaded from: classes.dex */
public abstract class ObjectPoolBase<T> {
    private final ArrayList<T> zzaag;
    private final int zzaah;

    public ObjectPoolBase(int i) {
        this.zzaag = new ArrayList<>(i);
        this.zzaah = i;
    }

    public final T aquire() {
        synchronized (this.zzaag) {
            int size = this.zzaag.size();
            if (size > 0) {
                return this.zzaag.remove(size - 1);
            }
            return newObject();
        }
    }

    protected boolean cleanUpObject(T t) {
        return true;
    }

    protected abstract T newObject();

    public final boolean release(T t) {
        synchronized (this.zzaag) {
            int size = this.zzaag.size();
            for (int i = 0; i < size; i++) {
                if (this.zzaag.get(i) == t) {
                    String valueOf = String.valueOf(t);
                    StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 25);
                    sb.append("Object released already: ");
                    sb.append(valueOf);
                    throw new IllegalArgumentException(sb.toString());
                }
            }
            if (size >= this.zzaah || !cleanUpObject(t)) {
                return false;
            }
            this.zzaag.add(t);
            return true;
        }
    }
}
