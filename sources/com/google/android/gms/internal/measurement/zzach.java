package com.google.android.gms.internal.measurement;

import java.io.IOException;

/* loaded from: classes.dex */
public final class zzach {
    private static final int zzbxi = 11;
    private static final int zzbxj = 12;
    private static final int zzbxk = 16;
    private static final int zzbxl = 26;
    public static final int[] zzbti = new int[0];
    public static final long[] zzbxm = new long[0];
    private static final float[] zzbxn = new float[0];
    private static final double[] zzbxo = new double[0];
    private static final boolean[] zzbxp = new boolean[0];
    public static final String[] zzbxq = new String[0];
    private static final byte[][] zzbxr = new byte[0];
    public static final byte[] zzbxs = new byte[0];

    public static final int zzb(zzabv zzabvVar, int i) throws IOException {
        int position = zzabvVar.getPosition();
        zzabvVar.zzak(i);
        int i2 = 1;
        while (zzabvVar.zzuw() == i) {
            zzabvVar.zzak(i);
            i2++;
        }
        zzabvVar.zzd(position, i);
        return i2;
    }
}
