package com.google.android.gms.internal.measurement;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: Add missing generic type declarations: [V, K] */
/* loaded from: classes.dex */
final class zzabe<K, V> implements Iterator<Map.Entry<K, V>> {
    private int pos;
    private final /* synthetic */ zzaay zzbuf;
    private boolean zzbug;
    private Iterator<Map.Entry<K, V>> zzbuh;

    private zzabe(zzaay zzaayVar) {
        this.zzbuf = zzaayVar;
        this.pos = -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzabe(zzaay zzaayVar, zzaaz zzaazVar) {
        this(zzaayVar);
    }

    private final Iterator<Map.Entry<K, V>> zzup() {
        Map map;
        if (this.zzbuh == null) {
            map = this.zzbuf.zzbtz;
            this.zzbuh = map.entrySet().iterator();
        }
        return this.zzbuh;
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        List list;
        Map map;
        int i = this.pos + 1;
        list = this.zzbuf.zzbty;
        if (i >= list.size()) {
            map = this.zzbuf.zzbtz;
            if (map.isEmpty() || !zzup().hasNext()) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.Iterator
    public final /* synthetic */ Object next() {
        List list;
        Map.Entry<K, V> next;
        List list2;
        this.zzbug = true;
        int i = this.pos + 1;
        this.pos = i;
        list = this.zzbuf.zzbty;
        if (i < list.size()) {
            list2 = this.zzbuf.zzbty;
            next = (Map.Entry<K, V>) list2.get(this.pos);
        } else {
            next = zzup().next();
        }
        return next;
    }

    @Override // java.util.Iterator
    public final void remove() {
        List list;
        if (!this.zzbug) {
            throw new IllegalStateException("remove() was called before next()");
        }
        this.zzbug = false;
        this.zzbuf.zzul();
        int i = this.pos;
        list = this.zzbuf.zzbty;
        if (i >= list.size()) {
            zzup().remove();
            return;
        }
        zzaay zzaayVar = this.zzbuf;
        int i2 = this.pos;
        this.pos = i2 - 1;
        zzaayVar.zzai(i2);
    }
}
