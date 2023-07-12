package com.google.android.gms.common.data;

import android.text.TextUtils;
import com.google.android.gms.common.data.DataBufferObserver;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

@VisibleForTesting
/* loaded from: classes.dex */
public final class ExclusionFilteredDataBuffer<T> extends FilteredDataBuffer<T> implements DataBufferObserver.Observable, ExclusionFilterable {
    private AbstractDataBuffer<T> zzoc;
    private final String zzod;
    private final HashSet<Integer> zzoe;
    private DataBufferObserverSet zzof;

    public ExclusionFilteredDataBuffer(AbstractDataBuffer<T> abstractDataBuffer, String str) {
        super(abstractDataBuffer);
        this.zzoe = new HashSet<>();
        this.zzoc = abstractDataBuffer;
        this.zzod = str;
        this.zzof = new DataBufferObserverSet();
    }

    private final ArrayList<Integer> zza(String str, ArrayList<Integer> arrayList) {
        int i;
        ArrayList<Integer> arrayList2 = new ArrayList<>();
        if (arrayList != null) {
            arrayList.clear();
        }
        DataHolder dataHolder = this.zzoc.mDataHolder;
        String str2 = this.zzod;
        boolean z = this.zzoc instanceof EntityBuffer;
        int count = this.zzoc.getCount();
        int i2 = 0;
        int i3 = 0;
        while (i2 < count) {
            int zzi = z ? ((EntityBuffer) this.zzoc).zzi(i2) : i2;
            String string = dataHolder.getString(str2, zzi, dataHolder.getWindowIndex(zzi));
            if (arrayList == null) {
                i = i3;
            } else if (this.zzoe.contains(Integer.valueOf(i2))) {
                i = i3;
                i3 = (-i3) - 1;
            } else {
                i = i3 + 1;
            }
            if (!TextUtils.isEmpty(string) && string.equals(str)) {
                arrayList2.add(Integer.valueOf(i2));
                if (arrayList != null) {
                    arrayList.add(Integer.valueOf(i3));
                }
            }
            i2++;
            i3 = i;
        }
        return arrayList2;
    }

    @Override // com.google.android.gms.common.data.DataBufferObserver.Observable
    public final void addObserver(DataBufferObserver dataBufferObserver) {
        this.zzof.addObserver(dataBufferObserver);
    }

    @Override // com.google.android.gms.common.data.ExclusionFilterable
    public final void clearFilters() {
        if (!this.zzof.hasObservers()) {
            this.zzoe.clear();
            return;
        }
        int size = this.zzoe.size();
        Integer[] numArr = (Integer[]) this.zzoe.toArray(new Integer[size]);
        Arrays.sort(numArr);
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < size; i3++) {
            int intValue = numArr[i3].intValue();
            this.zzoe.remove(Integer.valueOf(intValue));
            if (i != 0) {
                if (intValue == i2 + i) {
                    i++;
                } else {
                    this.zzof.onDataRangeRemoved(i2, i);
                }
            }
            i2 = intValue;
            i = 1;
        }
        if (i > 0) {
            this.zzof.onDataRangeRemoved(i2, i);
        }
    }

    @Override // com.google.android.gms.common.data.FilteredDataBuffer
    public final int computeRealPosition(int i) {
        if (this.zzoe.isEmpty()) {
            return i;
        }
        if (i < 0 || i >= getCount()) {
            StringBuilder sb = new StringBuilder(53);
            sb.append("Position ");
            sb.append(i);
            sb.append(" is out of bounds for this buffer");
            throw new IllegalArgumentException(sb.toString());
        }
        int count = this.mDataBuffer.getCount();
        int i2 = 0;
        for (int i3 = 0; i3 < count; i3++) {
            if (!this.zzoe.contains(Integer.valueOf(i3))) {
                if (i2 == i) {
                    return i3;
                }
                i2++;
            }
        }
        return -1;
    }

    @Override // com.google.android.gms.common.data.ExclusionFilterable
    public final void filterOut(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        ArrayList<Integer> arrayList = this.zzof.hasObservers() ? new ArrayList<>() : null;
        ArrayList<Integer> zza = zza(str, arrayList);
        if (!this.zzof.hasObservers()) {
            this.zzoe.addAll(zza);
            return;
        }
        int i = 0;
        int i2 = 0;
        for (int size = zza.size() - 1; size >= 0; size--) {
            int intValue = arrayList.get(size).intValue();
            if (!(intValue < 0)) {
                this.zzoe.add(Integer.valueOf(zza.get(size).intValue()));
                if (i != 0) {
                    if (intValue == i2 - 1) {
                        i2--;
                        i++;
                    } else {
                        this.zzof.onDataRangeRemoved(i2, i);
                    }
                }
                i = 1;
                i2 = intValue;
            }
        }
        if (i > 0) {
            this.zzof.onDataRangeRemoved(i2, i);
        }
    }

    @Override // com.google.android.gms.common.data.DataBuffer
    public final int getCount() {
        return this.mDataBuffer.getCount() - this.zzoe.size();
    }

    @Override // com.google.android.gms.common.data.FilteredDataBuffer, com.google.android.gms.common.data.DataBuffer, com.google.android.gms.common.api.Releasable
    public final void release() {
        super.release();
        this.zzof.clear();
    }

    @Override // com.google.android.gms.common.data.DataBufferObserver.Observable
    public final void removeObserver(DataBufferObserver dataBufferObserver) {
        this.zzof.removeObserver(dataBufferObserver);
    }

    @Override // com.google.android.gms.common.data.ExclusionFilterable
    public final void unfilter(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        ArrayList<Integer> arrayList = this.zzof.hasObservers() ? new ArrayList<>() : null;
        ArrayList<Integer> zza = zza(str, arrayList);
        if (!this.zzof.hasObservers()) {
            this.zzoe.removeAll(zza);
            return;
        }
        int i = 0;
        int i2 = 0;
        for (int size = zza.size() - 1; size >= 0; size--) {
            int intValue = arrayList.get(size).intValue();
            if (intValue < 0) {
                this.zzoe.remove(Integer.valueOf(zza.get(size).intValue()));
                int i3 = (-intValue) - 1;
                if (i != 0) {
                    if (i3 == i2) {
                        i++;
                    } else {
                        this.zzof.onDataRangeInserted(i2, i);
                    }
                }
                i = 1;
                i2 = i3;
            }
        }
        if (i > 0) {
            this.zzof.onDataRangeInserted(i2, i);
        }
    }
}
