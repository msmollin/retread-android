package com.google.android.gms.internal.measurement;

import java.util.Iterator;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzzx<K> implements Iterator<Map.Entry<K, Object>> {
    private Iterator<Map.Entry<K, Object>> zzbsv;

    public zzzx(Iterator<Map.Entry<K, Object>> it) {
        this.zzbsv = it;
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.zzbsv.hasNext();
    }

    @Override // java.util.Iterator
    public final /* synthetic */ Object next() {
        Map.Entry<K, Object> next = this.zzbsv.next();
        return next.getValue() instanceof zzzu ? new zzzw(next) : next;
    }

    @Override // java.util.Iterator
    public final void remove() {
        this.zzbsv.remove();
    }
}
