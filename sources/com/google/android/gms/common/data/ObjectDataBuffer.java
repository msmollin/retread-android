package com.google.android.gms.common.data;

import android.os.Bundle;
import com.google.android.gms.common.data.DataBufferObserver;
import com.google.android.gms.common.internal.Asserts;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

/* loaded from: classes.dex */
public final class ObjectDataBuffer<T> extends AbstractDataBuffer<T> implements DataBufferObserver.Observable, ObjectExclusionFilterable<T> {
    private final ArrayList<Integer> zzob;
    private final HashSet<Integer> zzoe;
    private DataBufferObserverSet zzof;
    private final ArrayList<T> zzog;

    public ObjectDataBuffer() {
        super(null);
        this.zzoe = new HashSet<>();
        this.zzob = new ArrayList<>();
        this.zzog = new ArrayList<>();
        this.zzof = new DataBufferObserverSet();
        zzcl();
    }

    public ObjectDataBuffer(ArrayList<T> arrayList) {
        super(null);
        this.zzoe = new HashSet<>();
        this.zzob = new ArrayList<>();
        this.zzog = arrayList;
        this.zzof = new DataBufferObserverSet();
        zzcl();
    }

    public ObjectDataBuffer(T... tArr) {
        super(null);
        this.zzoe = new HashSet<>();
        this.zzob = new ArrayList<>();
        this.zzog = new ArrayList<>(Arrays.asList(tArr));
        this.zzof = new DataBufferObserverSet();
        zzcl();
    }

    private final void zzcl() {
        this.zzob.clear();
        int size = this.zzog.size();
        for (int i = 0; i < size; i++) {
            if (!this.zzoe.contains(Integer.valueOf(i))) {
                this.zzob.add(Integer.valueOf(i));
            }
        }
    }

    public final void add(T t) {
        int size = this.zzog.size();
        this.zzog.add(t);
        zzcl();
        if (this.zzof.hasObservers()) {
            Asserts.checkState(!this.zzoe.contains(Integer.valueOf(size)));
            int size2 = this.zzob.size();
            Asserts.checkState(size2 > 0);
            int i = size2 - 1;
            Asserts.checkState(this.zzob.get(i).intValue() == size);
            this.zzof.onDataRangeInserted(i, 1);
        }
    }

    @Override // com.google.android.gms.common.data.DataBufferObserver.Observable
    public final void addObserver(DataBufferObserver dataBufferObserver) {
        this.zzof.addObserver(dataBufferObserver);
    }

    @Override // com.google.android.gms.common.data.ObjectExclusionFilterable
    public final void filterOut(T t) {
        int size = this.zzog.size();
        boolean z = false;
        int i = -1;
        int i2 = -1;
        int i3 = -1;
        for (int i4 = 0; i4 < size; i4++) {
            if (!this.zzoe.contains(Integer.valueOf(i4))) {
                i++;
                if (t.equals(this.zzog.get(i4))) {
                    this.zzoe.add(Integer.valueOf(i4));
                    if (this.zzof.hasObservers()) {
                        if (i2 < 0) {
                            i2 = i;
                            z = true;
                            i3 = 1;
                        } else {
                            i3++;
                        }
                    }
                    z = true;
                } else if (i2 >= 0) {
                    zzcl();
                    this.zzof.onDataRangeRemoved(i2, i3);
                    i -= i3;
                    z = false;
                    i2 = -1;
                    i3 = -1;
                }
            }
        }
        if (z) {
            zzcl();
        }
        if (i2 >= 0) {
            this.zzof.onDataRangeRemoved(i2, i3);
        }
    }

    public final void filterOutRaw(int i) {
        int i2;
        boolean add = this.zzoe.add(Integer.valueOf(i));
        if (this.zzof.hasObservers() && add) {
            i2 = 0;
            int size = this.zzob.size();
            while (i2 < size) {
                if (this.zzob.get(i2).intValue() == i) {
                    break;
                }
                i2++;
            }
        }
        i2 = -1;
        zzcl();
        if (i2 >= 0) {
            this.zzof.onDataRangeRemoved(i2, 1);
        }
    }

    @Override // com.google.android.gms.common.data.AbstractDataBuffer, com.google.android.gms.common.data.DataBuffer
    public final T get(int i) {
        return this.zzog.get(getRawPosition(i));
    }

    @Override // com.google.android.gms.common.data.AbstractDataBuffer, com.google.android.gms.common.data.DataBuffer
    public final int getCount() {
        return this.zzog.size() - this.zzoe.size();
    }

    @Override // com.google.android.gms.common.data.AbstractDataBuffer, com.google.android.gms.common.data.DataBuffer
    public final Bundle getMetadata() {
        return null;
    }

    public final int getPositionFromRawPosition(int i) {
        int i2 = -1;
        for (int i3 = 0; i3 <= i; i3++) {
            if (!this.zzoe.contains(Integer.valueOf(i3))) {
                i2++;
            }
        }
        return i2;
    }

    public final T getRaw(int i) {
        return this.zzog.get(i);
    }

    public final int getRawCount() {
        return this.zzog.size();
    }

    public final int getRawPosition(int i) {
        if (i < 0 || i >= getCount()) {
            StringBuilder sb = new StringBuilder(53);
            sb.append("Position ");
            sb.append(i);
            sb.append(" is out of bounds for this buffer");
            throw new IllegalArgumentException(sb.toString());
        }
        return this.zzob.get(i).intValue();
    }

    public final void insertRaw(int i, T t) {
        this.zzog.add(i, t);
        HashSet hashSet = new HashSet(this.zzoe.size());
        Iterator<Integer> it = this.zzoe.iterator();
        int i2 = i;
        while (it.hasNext()) {
            Integer next = it.next();
            if (next.intValue() < i) {
                i2--;
            } else {
                hashSet.add(Integer.valueOf(next.intValue() + 1));
                it.remove();
            }
        }
        Iterator it2 = hashSet.iterator();
        while (it2.hasNext()) {
            this.zzoe.add((Integer) it2.next());
        }
        zzcl();
        if (this.zzof.hasObservers()) {
            this.zzof.onDataRangeInserted(i2, 1);
        }
    }

    public final boolean isRawPositionFiltered(int i) {
        return this.zzoe.contains(Integer.valueOf(i));
    }

    public final void notifyChanged(T t) {
        if (this.zzof.hasObservers()) {
            int size = this.zzob.size();
            for (int i = 0; i < size; i++) {
                if (t.equals(this.zzog.get(this.zzob.get(i).intValue()))) {
                    this.zzof.onDataRangeChanged(i, 1);
                }
            }
        }
    }

    @Override // com.google.android.gms.common.data.AbstractDataBuffer, com.google.android.gms.common.data.DataBuffer, com.google.android.gms.common.api.Releasable
    public final void release() {
        this.zzof.clear();
    }

    @Override // com.google.android.gms.common.data.DataBufferObserver.Observable
    public final void removeObserver(DataBufferObserver dataBufferObserver) {
        this.zzof.removeObserver(dataBufferObserver);
    }

    public final void removeRaw(int i) {
        this.zzog.remove(i);
        boolean remove = this.zzoe.remove(Integer.valueOf(i));
        HashSet hashSet = new HashSet(this.zzoe.size());
        Iterator<Integer> it = this.zzoe.iterator();
        int i2 = i;
        while (it.hasNext()) {
            Integer next = it.next();
            if (next.intValue() < i) {
                i2--;
            } else {
                it.remove();
                hashSet.add(Integer.valueOf(next.intValue() - 1));
            }
        }
        Iterator it2 = hashSet.iterator();
        while (it2.hasNext()) {
            this.zzoe.add((Integer) it2.next());
        }
        zzcl();
        if (remove || !this.zzof.hasObservers()) {
            return;
        }
        this.zzof.onDataRangeRemoved(i2, 1);
    }

    public final boolean setRaw(int i, T t) {
        this.zzog.set(i, t);
        boolean z = !this.zzoe.contains(Integer.valueOf(i));
        if (z && this.zzof.hasObservers()) {
            int i2 = 0;
            int size = this.zzob.size();
            while (true) {
                if (i2 >= size) {
                    break;
                } else if (this.zzob.get(i2).intValue() == i) {
                    this.zzof.onDataRangeChanged(i2, 1);
                    break;
                } else {
                    i2++;
                }
            }
        }
        return z;
    }

    public final void unfilter(T t) {
        int i;
        int size = this.zzog.size();
        boolean z = false;
        int i2 = 0;
        int i3 = -1;
        int i4 = -1;
        for (i = 0; i < size; i = i + 1) {
            if (this.zzoe.contains(Integer.valueOf(i))) {
                if (t.equals(this.zzog.get(i))) {
                    this.zzoe.remove(Integer.valueOf(i));
                    if (this.zzof.hasObservers()) {
                        if (i3 < 0) {
                            i3 = i2;
                            z = true;
                            i4 = 1;
                        } else {
                            i4++;
                        }
                    }
                    z = true;
                } else if (this.zzof.hasObservers()) {
                    if (i3 < 0) {
                    }
                    zzcl();
                    this.zzof.onDataRangeInserted(i3, i4);
                    i2 += i4;
                    z = false;
                    i3 = -1;
                    i4 = -1;
                }
            } else {
                i2++;
                i = i3 < 0 ? i + 1 : 0;
                zzcl();
                this.zzof.onDataRangeInserted(i3, i4);
                i2 += i4;
                z = false;
                i3 = -1;
                i4 = -1;
            }
        }
        if (z) {
            zzcl();
        }
        if (i3 >= 0) {
            this.zzof.onDataRangeInserted(i3, i4);
        }
    }

    public final void unfilterRaw(int i) {
        boolean remove = this.zzoe.remove(Integer.valueOf(i));
        zzcl();
        if (this.zzof.hasObservers() && remove) {
            int i2 = -1;
            int i3 = 0;
            int size = this.zzob.size();
            while (true) {
                if (i3 >= size) {
                    break;
                } else if (this.zzob.get(i3).intValue() == i) {
                    i2 = i3;
                    break;
                } else {
                    i3++;
                }
            }
            if (i2 >= 0) {
                this.zzof.onDataRangeInserted(i2, 1);
            }
        }
    }
}
