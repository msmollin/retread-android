package com.google.android.gms.internal.measurement;

import java.io.Serializable;
import java.util.Iterator;

/* loaded from: classes.dex */
public abstract class zzyw implements Serializable, Iterable<Byte> {
    public static final zzyw zzbqx = new zzzc(zzzr.zzbsq);
    private static final zzza zzbqy;
    private int zzboc = 0;

    /* JADX WARN: Multi-variable type inference failed */
    static {
        zzbqy = zzyv.zzsv() ? new zzzd(null) : new zzyy(null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzb(int i, int i2, int i3) {
        int i4 = i2 - i;
        if ((i | i2 | i4 | (i3 - i2)) < 0) {
            if (i < 0) {
                StringBuilder sb = new StringBuilder(32);
                sb.append("Beginning index: ");
                sb.append(i);
                sb.append(" < 0");
                throw new IndexOutOfBoundsException(sb.toString());
            } else if (i2 < i) {
                StringBuilder sb2 = new StringBuilder(66);
                sb2.append("Beginning index larger than ending index: ");
                sb2.append(i);
                sb2.append(", ");
                sb2.append(i2);
                throw new IndexOutOfBoundsException(sb2.toString());
            } else {
                StringBuilder sb3 = new StringBuilder(37);
                sb3.append("End index: ");
                sb3.append(i2);
                sb3.append(" >= ");
                sb3.append(i3);
                throw new IndexOutOfBoundsException(sb3.toString());
            }
        }
        return i4;
    }

    public static zzyw zzfi(String str) {
        return new zzzc(str.getBytes(zzzr.UTF_8));
    }

    public abstract boolean equals(Object obj);

    public final int hashCode() {
        int i = this.zzboc;
        if (i == 0) {
            int size = size();
            i = zza(size, 0, size);
            if (i == 0) {
                i = 1;
            }
            this.zzboc = i;
        }
        return i;
    }

    @Override // java.lang.Iterable
    public /* synthetic */ Iterator<Byte> iterator() {
        return new zzyx(this);
    }

    public abstract int size();

    public final String toString() {
        return String.format("<ByteString@%s size=%d>", Integer.toHexString(System.identityHashCode(this)), Integer.valueOf(size()));
    }

    protected abstract int zza(int i, int i2, int i3);

    public abstract byte zzae(int i);

    public abstract zzyw zzb(int i, int i2);

    /* JADX INFO: Access modifiers changed from: protected */
    public final int zzsx() {
        return this.zzboc;
    }
}
