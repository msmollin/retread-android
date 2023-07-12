package com.google.android.gms.dynamic;

import android.os.Bundle;
import com.google.android.gms.dynamic.DeferredLifecycleHelper;
import java.util.Iterator;
import java.util.LinkedList;

/* JADX INFO: Add missing generic type declarations: [T] */
/* loaded from: classes.dex */
final class zza<T> implements OnDelegateCreatedListener<T> {
    private final /* synthetic */ DeferredLifecycleHelper zzabg;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zza(DeferredLifecycleHelper deferredLifecycleHelper) {
        this.zzabg = deferredLifecycleHelper;
    }

    /* JADX WARN: Incorrect types in method signature: (TT;)V */
    @Override // com.google.android.gms.dynamic.OnDelegateCreatedListener
    public final void onDelegateCreated(LifecycleDelegate lifecycleDelegate) {
        LinkedList linkedList;
        LinkedList linkedList2;
        LifecycleDelegate lifecycleDelegate2;
        this.zzabg.zzabc = lifecycleDelegate;
        linkedList = this.zzabg.zzabe;
        Iterator it = linkedList.iterator();
        while (it.hasNext()) {
            lifecycleDelegate2 = this.zzabg.zzabc;
            ((DeferredLifecycleHelper.zza) it.next()).zza(lifecycleDelegate2);
        }
        linkedList2 = this.zzabg.zzabe;
        linkedList2.clear();
        DeferredLifecycleHelper.zza(this.zzabg, (Bundle) null);
    }
}
