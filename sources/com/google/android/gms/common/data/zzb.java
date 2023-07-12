package com.google.android.gms.common.data;

import java.util.Comparator;

/* loaded from: classes.dex */
final class zzb implements Comparator<Integer> {
    private final /* synthetic */ Comparator zzom;
    private final /* synthetic */ SortedDataBuffer zzon;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzb(SortedDataBuffer sortedDataBuffer, Comparator comparator) {
        this.zzon = sortedDataBuffer;
        this.zzom = comparator;
    }

    @Override // java.util.Comparator
    public final /* synthetic */ int compare(Integer num, Integer num2) {
        DataBuffer dataBuffer;
        DataBuffer dataBuffer2;
        Comparator comparator = this.zzom;
        dataBuffer = this.zzon.zzok;
        Object obj = dataBuffer.get(num.intValue());
        dataBuffer2 = this.zzon.zzok;
        return comparator.compare(obj, dataBuffer2.get(num2.intValue()));
    }
}
