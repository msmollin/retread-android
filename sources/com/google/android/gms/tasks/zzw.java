package com.google.android.gms.tasks;

import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/* JADX INFO: Add missing generic type declarations: [TResult] */
/* loaded from: classes.dex */
final class zzw<TResult> implements Continuation<Void, List<TResult>> {
    private final /* synthetic */ Collection zzagk;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzw(Collection collection) {
        this.zzagk = collection;
    }

    @Override // com.google.android.gms.tasks.Continuation
    public final /* synthetic */ Object then(@NonNull Task<Void> task) throws Exception {
        if (this.zzagk.size() == 0) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        for (Task task2 : this.zzagk) {
            arrayList.add(task2.getResult());
        }
        return arrayList;
    }
}
