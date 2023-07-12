package com.google.android.gms.common.api.internal;

import androidx.annotation.WorkerThread;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.IAccountAccessor;
import java.util.ArrayList;
import java.util.Set;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzap extends zzat {
    private final /* synthetic */ zzaj zzhv;
    private final ArrayList<Api.Client> zzib;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzap(zzaj zzajVar, ArrayList<Api.Client> arrayList) {
        super(zzajVar, null);
        this.zzhv = zzajVar;
        this.zzib = arrayList;
    }

    @Override // com.google.android.gms.common.api.internal.zzat
    @WorkerThread
    public final void zzaq() {
        zzbd zzbdVar;
        Set<Scope> zzaw;
        IAccountAccessor iAccountAccessor;
        zzbd zzbdVar2;
        zzbdVar = this.zzhv.zzhf;
        zzav zzavVar = zzbdVar.zzfq;
        zzaw = this.zzhv.zzaw();
        zzavVar.zzim = zzaw;
        ArrayList<Api.Client> arrayList = this.zzib;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Api.Client client = arrayList.get(i);
            i++;
            iAccountAccessor = this.zzhv.zzhr;
            zzbdVar2 = this.zzhv.zzhf;
            client.getRemoteService(iAccountAccessor, zzbdVar2.zzfq.zzim);
        }
    }
}
