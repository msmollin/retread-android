package com.google.android.gms.internal.measurement;

import java.io.PrintStream;
import java.util.List;

/* loaded from: classes.dex */
final class zzxh extends zzxe {
    private final zzxf zzbod = new zzxf();

    @Override // com.google.android.gms.internal.measurement.zzxe
    public final void zza(Throwable th, PrintStream printStream) {
        th.printStackTrace(printStream);
        List<Throwable> zza = this.zzbod.zza(th, false);
        if (zza == null) {
            return;
        }
        synchronized (zza) {
            for (Throwable th2 : zza) {
                printStream.print("Suppressed: ");
                th2.printStackTrace(printStream);
            }
        }
    }
}
