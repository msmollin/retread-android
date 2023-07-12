package com.google.android.gms.internal.measurement;

/* loaded from: classes.dex */
public final class zzaca implements Cloneable {
    private static final zzacb zzbxa = new zzacb();
    private int mSize;
    private boolean zzbxb;
    private int[] zzbxc;
    private zzacb[] zzbxd;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzaca() {
        this(10);
    }

    private zzaca(int i) {
        this.zzbxb = false;
        int idealIntArraySize = idealIntArraySize(i);
        this.zzbxc = new int[idealIntArraySize];
        this.zzbxd = new zzacb[idealIntArraySize];
        this.mSize = 0;
    }

    private static int idealIntArraySize(int i) {
        int i2 = i << 2;
        int i3 = 4;
        while (true) {
            if (i3 >= 32) {
                break;
            }
            int i4 = (1 << i3) - 12;
            if (i2 <= i4) {
                i2 = i4;
                break;
            }
            i3++;
        }
        return i2 / 4;
    }

    private final int zzav(int i) {
        int i2 = this.mSize - 1;
        int i3 = 0;
        while (i3 <= i2) {
            int i4 = (i3 + i2) >>> 1;
            int i5 = this.zzbxc[i4];
            if (i5 < i) {
                i3 = i4 + 1;
            } else if (i5 <= i) {
                return i4;
            } else {
                i2 = i4 - 1;
            }
        }
        return ~i3;
    }

    public final /* synthetic */ Object clone() throws CloneNotSupportedException {
        int i = this.mSize;
        zzaca zzacaVar = new zzaca(i);
        System.arraycopy(this.zzbxc, 0, zzacaVar.zzbxc, 0, i);
        for (int i2 = 0; i2 < i; i2++) {
            if (this.zzbxd[i2] != null) {
                zzacaVar.zzbxd[i2] = (zzacb) this.zzbxd[i2].clone();
            }
        }
        zzacaVar.mSize = i;
        return zzacaVar;
    }

    public final boolean equals(Object obj) {
        boolean z;
        boolean z2;
        if (obj == this) {
            return true;
        }
        if (obj instanceof zzaca) {
            zzaca zzacaVar = (zzaca) obj;
            if (this.mSize != zzacaVar.mSize) {
                return false;
            }
            int[] iArr = this.zzbxc;
            int[] iArr2 = zzacaVar.zzbxc;
            int i = this.mSize;
            int i2 = 0;
            while (true) {
                if (i2 >= i) {
                    z = true;
                    break;
                } else if (iArr[i2] != iArr2[i2]) {
                    z = false;
                    break;
                } else {
                    i2++;
                }
            }
            if (z) {
                zzacb[] zzacbVarArr = this.zzbxd;
                zzacb[] zzacbVarArr2 = zzacaVar.zzbxd;
                int i3 = this.mSize;
                int i4 = 0;
                while (true) {
                    if (i4 >= i3) {
                        z2 = true;
                        break;
                    } else if (!zzacbVarArr[i4].equals(zzacbVarArr2[i4])) {
                        z2 = false;
                        break;
                    } else {
                        i4++;
                    }
                }
                if (z2) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public final int hashCode() {
        int i = 17;
        for (int i2 = 0; i2 < this.mSize; i2++) {
            i = (((i * 31) + this.zzbxc[i2]) * 31) + this.zzbxd[i2].hashCode();
        }
        return i;
    }

    public final boolean isEmpty() {
        return this.mSize == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int size() {
        return this.mSize;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zza(int i, zzacb zzacbVar) {
        int zzav = zzav(i);
        if (zzav >= 0) {
            this.zzbxd[zzav] = zzacbVar;
            return;
        }
        int i2 = ~zzav;
        if (i2 < this.mSize && this.zzbxd[i2] == zzbxa) {
            this.zzbxc[i2] = i;
            this.zzbxd[i2] = zzacbVar;
            return;
        }
        if (this.mSize >= this.zzbxc.length) {
            int idealIntArraySize = idealIntArraySize(this.mSize + 1);
            int[] iArr = new int[idealIntArraySize];
            zzacb[] zzacbVarArr = new zzacb[idealIntArraySize];
            System.arraycopy(this.zzbxc, 0, iArr, 0, this.zzbxc.length);
            System.arraycopy(this.zzbxd, 0, zzacbVarArr, 0, this.zzbxd.length);
            this.zzbxc = iArr;
            this.zzbxd = zzacbVarArr;
        }
        if (this.mSize - i2 != 0) {
            int i3 = i2 + 1;
            System.arraycopy(this.zzbxc, i2, this.zzbxc, i3, this.mSize - i2);
            System.arraycopy(this.zzbxd, i2, this.zzbxd, i3, this.mSize - i2);
        }
        this.zzbxc[i2] = i;
        this.zzbxd[i2] = zzacbVar;
        this.mSize++;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final zzacb zzat(int i) {
        int zzav = zzav(i);
        if (zzav < 0 || this.zzbxd[zzav] == zzbxa) {
            return null;
        }
        return this.zzbxd[zzav];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final zzacb zzau(int i) {
        return this.zzbxd[i];
    }
}
