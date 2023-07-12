package com.google.android.gms.common.data;

import java.util.ArrayList;
import java.util.HashSet;

/* loaded from: classes.dex */
public final class PositionFilteredDataBuffer<T> extends FilteredDataBuffer<T> {
    private final ArrayList<Integer> zzob;
    private final HashSet<Integer> zzoe;

    public PositionFilteredDataBuffer(AbstractDataBuffer<T> abstractDataBuffer) {
        super(abstractDataBuffer);
        this.zzoe = new HashSet<>();
        this.zzob = new ArrayList<>();
        zzcl();
    }

    private final void zzcl() {
        this.zzob.clear();
        int count = this.mDataBuffer.getCount();
        for (int i = 0; i < count; i++) {
            if (!this.zzoe.contains(Integer.valueOf(i))) {
                this.zzob.add(Integer.valueOf(i));
            }
        }
    }

    public final void clearFilters() {
        this.zzoe.clear();
        zzcl();
    }

    @Override // com.google.android.gms.common.data.FilteredDataBuffer
    public final int computeRealPosition(int i) {
        if (i < 0 || i >= getCount()) {
            StringBuilder sb = new StringBuilder(53);
            sb.append("Position ");
            sb.append(i);
            sb.append(" is out of bounds for this buffer");
            throw new IllegalArgumentException(sb.toString());
        }
        return this.zzob.get(i).intValue();
    }

    public final void filterOut(int i) {
        if (i < 0 || i > this.mDataBuffer.getCount()) {
            return;
        }
        this.zzoe.add(Integer.valueOf(i));
        zzcl();
    }

    @Override // com.google.android.gms.common.data.DataBuffer
    public final int getCount() {
        return this.mDataBuffer.getCount() - this.zzoe.size();
    }

    public final void unfilter(int i) {
        this.zzoe.remove(Integer.valueOf(i));
        zzcl();
    }
}
