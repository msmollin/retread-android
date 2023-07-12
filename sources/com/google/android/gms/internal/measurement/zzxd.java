package com.google.android.gms.internal.measurement;

import java.io.PrintStream;

/* loaded from: classes.dex */
public final class zzxd {
    private static final zzxe zzbnx;
    private static final int zzbny;

    /* loaded from: classes.dex */
    static final class zza extends zzxe {
        zza() {
        }

        @Override // com.google.android.gms.internal.measurement.zzxe
        public final void zza(Throwable th, PrintStream printStream) {
            th.printStackTrace(printStream);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0069  */
    static {
        /*
            r0 = 1
            java.lang.Integer r1 = zzsd()     // Catch: java.lang.Throwable -> L2c
            if (r1 == 0) goto L17
            int r2 = r1.intValue()     // Catch: java.lang.Throwable -> L15
            r3 = 19
            if (r2 < r3) goto L17
            com.google.android.gms.internal.measurement.zzxi r2 = new com.google.android.gms.internal.measurement.zzxi     // Catch: java.lang.Throwable -> L15
            r2.<init>()     // Catch: java.lang.Throwable -> L15
            goto L64
        L15:
            r2 = move-exception
            goto L2e
        L17:
            java.lang.String r2 = "com.google.devtools.build.android.desugar.runtime.twr_disable_mimic"
            boolean r2 = java.lang.Boolean.getBoolean(r2)     // Catch: java.lang.Throwable -> L15
            r2 = r2 ^ r0
            if (r2 == 0) goto L26
            com.google.android.gms.internal.measurement.zzxh r2 = new com.google.android.gms.internal.measurement.zzxh     // Catch: java.lang.Throwable -> L15
            r2.<init>()     // Catch: java.lang.Throwable -> L15
            goto L64
        L26:
            com.google.android.gms.internal.measurement.zzxd$zza r2 = new com.google.android.gms.internal.measurement.zzxd$zza     // Catch: java.lang.Throwable -> L15
            r2.<init>()     // Catch: java.lang.Throwable -> L15
            goto L64
        L2c:
            r2 = move-exception
            r1 = 0
        L2e:
            java.io.PrintStream r3 = java.lang.System.err
            java.lang.Class<com.google.android.gms.internal.measurement.zzxd$zza> r4 = com.google.android.gms.internal.measurement.zzxd.zza.class
            java.lang.String r4 = r4.getName()
            java.lang.String r5 = java.lang.String.valueOf(r4)
            int r5 = r5.length()
            int r5 = r5 + 132
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>(r5)
            java.lang.String r5 = "An error has occured when initializing the try-with-resources desuguring strategy. The default strategy "
            r6.append(r5)
            r6.append(r4)
            java.lang.String r4 = "will be used. The error is: "
            r6.append(r4)
            java.lang.String r4 = r6.toString()
            r3.println(r4)
            java.io.PrintStream r3 = java.lang.System.err
            r2.printStackTrace(r3)
            com.google.android.gms.internal.measurement.zzxd$zza r2 = new com.google.android.gms.internal.measurement.zzxd$zza
            r2.<init>()
        L64:
            com.google.android.gms.internal.measurement.zzxd.zzbnx = r2
            if (r1 != 0) goto L69
            goto L6d
        L69:
            int r0 = r1.intValue()
        L6d:
            com.google.android.gms.internal.measurement.zzxd.zzbny = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzxd.<clinit>():void");
    }

    public static void zza(Throwable th, PrintStream printStream) {
        zzbnx.zza(th, printStream);
    }

    private static Integer zzsd() {
        try {
            return (Integer) Class.forName("android.os.Build$VERSION").getField("SDK_INT").get(null);
        } catch (Exception e) {
            System.err.println("Failed to retrieve value from android.os.Build$VERSION.SDK_INT due to the following exception.");
            e.printStackTrace(System.err);
            return null;
        }
    }
}
