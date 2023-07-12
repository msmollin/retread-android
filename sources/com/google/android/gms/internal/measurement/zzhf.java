package com.google.android.gms.internal.measurement;

/* loaded from: classes.dex */
final class zzhf implements Runnable {
    private final /* synthetic */ zzgn zzanf;
    private final /* synthetic */ String zzanj;
    private final /* synthetic */ String zzanm;
    private final /* synthetic */ String zzann;
    private final /* synthetic */ long zzano;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzhf(zzgn zzgnVar, String str, String str2, String str3, long j) {
        this.zzanf = zzgnVar;
        this.zzanm = str;
        this.zzanj = str2;
        this.zzann = str3;
        this.zzano = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzjr zzjrVar;
        zzjr zzjrVar2;
        if (this.zzanm == null) {
            zzjrVar2 = this.zzanf.zzajp;
            zzjrVar2.zzla().zzfy().zza(this.zzanj, (zzie) null);
            return;
        }
        zzie zzieVar = new zzie(this.zzann, this.zzanm, this.zzano);
        zzjrVar = this.zzanf.zzajp;
        zzjrVar.zzla().zzfy().zza(this.zzanj, zzieVar);
    }
}
