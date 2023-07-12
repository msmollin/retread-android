package com.google.android.gms.internal.measurement;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzhm implements Runnable {
    private final /* synthetic */ String val$name;
    private final /* synthetic */ String zzanh;
    private final /* synthetic */ zzhk zzanw;
    private final /* synthetic */ Object zzanx;
    private final /* synthetic */ long zzany;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzhm(zzhk zzhkVar, String str, String str2, Object obj, long j) {
        this.zzanw = zzhkVar;
        this.zzanh = str;
        this.val$name = str2;
        this.zzanx = obj;
        this.zzany = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzanw.zza(this.zzanh, this.val$name, this.zzanx, this.zzany);
    }
}
