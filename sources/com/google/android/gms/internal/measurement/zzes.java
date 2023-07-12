package com.google.android.gms.internal.measurement;

import android.os.Bundle;
import java.util.Iterator;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzes implements Iterator<String> {
    private Iterator<String> zzafz;
    private final /* synthetic */ zzer zzaga;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzes(zzer zzerVar) {
        Bundle bundle;
        this.zzaga = zzerVar;
        bundle = this.zzaga.zzafy;
        this.zzafz = bundle.keySet().iterator();
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.zzafz.hasNext();
    }

    @Override // java.util.Iterator
    public final /* synthetic */ String next() {
        return this.zzafz.next();
    }

    @Override // java.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException("Remove not supported");
    }
}
