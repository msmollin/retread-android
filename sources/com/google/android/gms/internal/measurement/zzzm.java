package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzzo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
final class zzzm<FieldDescriptorType extends zzzo<FieldDescriptorType>> {
    private static final zzzm zzbru = new zzzm(true);
    private boolean zzbls;
    private boolean zzbrt = false;
    private final zzaay<FieldDescriptorType, Object> zzbrs = zzaay.zzag(16);

    private zzzm() {
    }

    private zzzm(boolean z) {
        if (this.zzbls) {
            return;
        }
        this.zzbrs.zzrg();
        this.zzbls = true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0024, code lost:
        if ((r3 instanceof com.google.android.gms.internal.measurement.zzzs) == false) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x002e, code lost:
        if ((r3 instanceof byte[]) == false) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x001b, code lost:
        if ((r3 instanceof com.google.android.gms.internal.measurement.zzzu) == false) goto L22;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void zza(com.google.android.gms.internal.measurement.zzabp r2, java.lang.Object r3) {
        /*
            com.google.android.gms.internal.measurement.zzzr.checkNotNull(r3)
            int[] r0 = com.google.android.gms.internal.measurement.zzzn.zzbrv
            com.google.android.gms.internal.measurement.zzabu r2 = r2.zzuv()
            int r2 = r2.ordinal()
            r2 = r0[r2]
            r0 = 1
            r1 = 0
            switch(r2) {
                case 1: goto L40;
                case 2: goto L3d;
                case 3: goto L3a;
                case 4: goto L37;
                case 5: goto L34;
                case 6: goto L31;
                case 7: goto L28;
                case 8: goto L1e;
                case 9: goto L15;
                default: goto L14;
            }
        L14:
            goto L43
        L15:
            boolean r2 = r3 instanceof com.google.android.gms.internal.measurement.zzaal
            if (r2 != 0) goto L26
            boolean r2 = r3 instanceof com.google.android.gms.internal.measurement.zzzu
            if (r2 == 0) goto L43
            goto L26
        L1e:
            boolean r2 = r3 instanceof java.lang.Integer
            if (r2 != 0) goto L26
            boolean r2 = r3 instanceof com.google.android.gms.internal.measurement.zzzs
            if (r2 == 0) goto L43
        L26:
            r1 = r0
            goto L43
        L28:
            boolean r2 = r3 instanceof com.google.android.gms.internal.measurement.zzyw
            if (r2 != 0) goto L26
            boolean r2 = r3 instanceof byte[]
            if (r2 == 0) goto L43
            goto L26
        L31:
            boolean r0 = r3 instanceof java.lang.String
            goto L26
        L34:
            boolean r0 = r3 instanceof java.lang.Boolean
            goto L26
        L37:
            boolean r0 = r3 instanceof java.lang.Double
            goto L26
        L3a:
            boolean r0 = r3 instanceof java.lang.Float
            goto L26
        L3d:
            boolean r0 = r3 instanceof java.lang.Long
            goto L26
        L40:
            boolean r0 = r3 instanceof java.lang.Integer
            goto L26
        L43:
            if (r1 == 0) goto L46
            return
        L46:
            java.lang.IllegalArgumentException r2 = new java.lang.IllegalArgumentException
            java.lang.String r3 = "Wrong object type used with protocol message reflection."
            r2.<init>(r3)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzzm.zza(com.google.android.gms.internal.measurement.zzabp, java.lang.Object):void");
    }

    private final void zza(FieldDescriptorType fielddescriptortype, Object obj) {
        if (!fielddescriptortype.zztk()) {
            zza(fielddescriptortype.zztj(), obj);
        } else if (!(obj instanceof List)) {
            throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
        } else {
            ArrayList arrayList = new ArrayList();
            arrayList.addAll((List) obj);
            ArrayList arrayList2 = arrayList;
            int size = arrayList2.size();
            int i = 0;
            while (i < size) {
                Object obj2 = arrayList2.get(i);
                i++;
                zza(fielddescriptortype.zztj(), obj2);
            }
            obj = arrayList;
        }
        if (obj instanceof zzzu) {
            this.zzbrt = true;
        }
        this.zzbrs.zza((zzaay<FieldDescriptorType, Object>) fielddescriptortype, (FieldDescriptorType) obj);
    }

    public static <T extends zzzo<T>> zzzm<T> zzti() {
        return zzbru;
    }

    public final /* synthetic */ Object clone() throws CloneNotSupportedException {
        zzzm zzzmVar = new zzzm();
        for (int i = 0; i < this.zzbrs.zzuj(); i++) {
            Map.Entry<FieldDescriptorType, Object> zzah = this.zzbrs.zzah(i);
            zzzmVar.zza((zzzm) zzah.getKey(), zzah.getValue());
        }
        for (Map.Entry<FieldDescriptorType, Object> entry : this.zzbrs.zzuk()) {
            zzzmVar.zza((zzzm) entry.getKey(), entry.getValue());
        }
        zzzmVar.zzbrt = this.zzbrt;
        return zzzmVar;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof zzzm) {
            return this.zzbrs.equals(((zzzm) obj).zzbrs);
        }
        return false;
    }

    public final int hashCode() {
        return this.zzbrs.hashCode();
    }

    public final Iterator<Map.Entry<FieldDescriptorType, Object>> iterator() {
        return this.zzbrt ? new zzzx(this.zzbrs.entrySet().iterator()) : this.zzbrs.entrySet().iterator();
    }
}
