package com.google.android.gms.internal.measurement;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzacb implements Cloneable {
    private Object value;
    private zzabz<?, ?> zzbxe;
    private List<zzacg> zzbxf = new ArrayList();

    private final byte[] toByteArray() throws IOException {
        byte[] bArr = new byte[zza()];
        zza(zzabw.zzj(bArr));
        return bArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: zzvg */
    public final zzacb clone() {
        Object clone;
        zzacb zzacbVar = new zzacb();
        try {
            zzacbVar.zzbxe = this.zzbxe;
            if (this.zzbxf == null) {
                zzacbVar.zzbxf = null;
            } else {
                zzacbVar.zzbxf.addAll(this.zzbxf);
            }
            if (this.value != null) {
                if (this.value instanceof zzace) {
                    clone = (zzace) ((zzace) this.value).clone();
                } else if (this.value instanceof byte[]) {
                    clone = ((byte[]) this.value).clone();
                } else {
                    int i = 0;
                    if (this.value instanceof byte[][]) {
                        byte[][] bArr = (byte[][]) this.value;
                        byte[][] bArr2 = new byte[bArr.length];
                        zzacbVar.value = bArr2;
                        while (i < bArr.length) {
                            bArr2[i] = (byte[]) bArr[i].clone();
                            i++;
                        }
                    } else if (this.value instanceof boolean[]) {
                        clone = ((boolean[]) this.value).clone();
                    } else if (this.value instanceof int[]) {
                        clone = ((int[]) this.value).clone();
                    } else if (this.value instanceof long[]) {
                        clone = ((long[]) this.value).clone();
                    } else if (this.value instanceof float[]) {
                        clone = ((float[]) this.value).clone();
                    } else if (this.value instanceof double[]) {
                        clone = ((double[]) this.value).clone();
                    } else if (this.value instanceof zzace[]) {
                        zzace[] zzaceVarArr = (zzace[]) this.value;
                        zzace[] zzaceVarArr2 = new zzace[zzaceVarArr.length];
                        zzacbVar.value = zzaceVarArr2;
                        while (i < zzaceVarArr.length) {
                            zzaceVarArr2[i] = (zzace) zzaceVarArr[i].clone();
                            i++;
                        }
                    }
                }
                zzacbVar.value = clone;
            }
            return zzacbVar;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof zzacb) {
            zzacb zzacbVar = (zzacb) obj;
            if (this.value != null && zzacbVar.value != null) {
                if (this.zzbxe != zzacbVar.zzbxe) {
                    return false;
                }
                return !this.zzbxe.zzbwx.isArray() ? this.value.equals(zzacbVar.value) : this.value instanceof byte[] ? Arrays.equals((byte[]) this.value, (byte[]) zzacbVar.value) : this.value instanceof int[] ? Arrays.equals((int[]) this.value, (int[]) zzacbVar.value) : this.value instanceof long[] ? Arrays.equals((long[]) this.value, (long[]) zzacbVar.value) : this.value instanceof float[] ? Arrays.equals((float[]) this.value, (float[]) zzacbVar.value) : this.value instanceof double[] ? Arrays.equals((double[]) this.value, (double[]) zzacbVar.value) : this.value instanceof boolean[] ? Arrays.equals((boolean[]) this.value, (boolean[]) zzacbVar.value) : Arrays.deepEquals((Object[]) this.value, (Object[]) zzacbVar.value);
            } else if (this.zzbxf == null || zzacbVar.zzbxf == null) {
                try {
                    return Arrays.equals(toByteArray(), zzacbVar.toByteArray());
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            } else {
                return this.zzbxf.equals(zzacbVar.zzbxf);
            }
        }
        return false;
    }

    public final int hashCode() {
        try {
            return Arrays.hashCode(toByteArray()) + 527;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int zza() {
        if (this.value == null) {
            int i = 0;
            for (zzacg zzacgVar : this.zzbxf) {
                i += zzabw.zzas(zzacgVar.tag) + 0 + zzacgVar.zzbrc.length;
            }
            return i;
        }
        zzabz<?, ?> zzabzVar = this.zzbxe;
        Object obj = this.value;
        if (zzabzVar.zzbwy) {
            int length = Array.getLength(obj);
            int i2 = 0;
            for (int i3 = 0; i3 < length; i3++) {
                if (Array.get(obj, i3) != null) {
                    i2 += zzabzVar.zzv(Array.get(obj, i3));
                }
            }
            return i2;
        }
        return zzabzVar.zzv(obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zza(zzabw zzabwVar) throws IOException {
        if (this.value == null) {
            for (zzacg zzacgVar : this.zzbxf) {
                zzabwVar.zzar(zzacgVar.tag);
                zzabwVar.zzk(zzacgVar.zzbrc);
            }
            return;
        }
        zzabz<?, ?> zzabzVar = this.zzbxe;
        Object obj = this.value;
        if (!zzabzVar.zzbwy) {
            zzabzVar.zza(obj, zzabwVar);
            return;
        }
        int length = Array.getLength(obj);
        for (int i = 0; i < length; i++) {
            Object obj2 = Array.get(obj, i);
            if (obj2 != null) {
                zzabzVar.zza(obj2, zzabwVar);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zza(zzacg zzacgVar) throws IOException {
        Object zzi;
        if (this.zzbxf != null) {
            this.zzbxf.add(zzacgVar);
            return;
        }
        if (this.value instanceof zzace) {
            byte[] bArr = zzacgVar.zzbrc;
            zzabv zza = zzabv.zza(bArr, 0, bArr.length);
            int zzuy = zza.zzuy();
            if (zzuy != bArr.length - zzabw.zzao(zzuy)) {
                throw zzacd.zzvh();
            }
            zzi = ((zzace) this.value).zzb(zza);
        } else if (this.value instanceof zzace[]) {
            zzace[] zzaceVarArr = (zzace[]) this.zzbxe.zzi(Collections.singletonList(zzacgVar));
            zzace[] zzaceVarArr2 = (zzace[]) this.value;
            zzace[] zzaceVarArr3 = (zzace[]) Arrays.copyOf(zzaceVarArr2, zzaceVarArr2.length + zzaceVarArr.length);
            System.arraycopy(zzaceVarArr, 0, zzaceVarArr3, zzaceVarArr2.length, zzaceVarArr.length);
            zzi = zzaceVarArr3;
        } else {
            zzi = this.zzbxe.zzi(Collections.singletonList(zzacgVar));
        }
        this.zzbxe = this.zzbxe;
        this.value = zzi;
        this.zzbxf = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public final <T> T zzb(zzabz<?, T> zzabzVar) {
        if (this.value == null) {
            this.zzbxe = zzabzVar;
            this.value = zzabzVar.zzi(this.zzbxf);
            this.zzbxf = null;
        } else if (!this.zzbxe.equals(zzabzVar)) {
            throw new IllegalStateException("Tried to getExtension with a different Extension.");
        }
        return (T) this.value;
    }
}
