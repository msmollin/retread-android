package com.google.android.gms.internal.measurement;

import java.util.Iterator;
import java.util.NoSuchElementException;

/* loaded from: classes.dex */
final class zzyx implements Iterator {
    private final int limit;
    private int position = 0;
    private final /* synthetic */ zzyw zzbqz;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzyx(zzyw zzywVar) {
        this.zzbqz = zzywVar;
        this.limit = this.zzbqz.size();
    }

    private final byte nextByte() {
        try {
            zzyw zzywVar = this.zzbqz;
            int i = this.position;
            this.position = i + 1;
            return zzywVar.zzae(i);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException(e.getMessage());
        }
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.position < this.limit;
    }

    @Override // java.util.Iterator
    public final /* synthetic */ Object next() {
        return Byte.valueOf(nextByte());
    }

    @Override // java.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException();
    }
}
