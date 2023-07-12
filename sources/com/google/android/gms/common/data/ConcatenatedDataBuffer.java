package com.google.android.gms.common.data;

import android.content.Context;
import android.os.Bundle;
import com.google.android.gms.common.data.TextFilterable;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.ArrayList;
import java.util.Iterator;

@VisibleForTesting
/* loaded from: classes.dex */
public final class ConcatenatedDataBuffer<T> implements DataBuffer<T>, ExclusionFilterable, TextFilterable {
    private Bundle mBundle;
    private final ArrayList<DataBuffer<T>> zznf = new ArrayList<>();
    private final ArrayList<Integer> zzng = new ArrayList<>();
    private int zznh;

    public ConcatenatedDataBuffer() {
    }

    public ConcatenatedDataBuffer(DataBuffer<T> dataBuffer) {
        append(dataBuffer);
    }

    public static <T> ConcatenatedDataBuffer<T> extend(ConcatenatedDataBuffer<T> concatenatedDataBuffer, DataBuffer<T> dataBuffer) {
        ConcatenatedDataBuffer<T> concatenatedDataBuffer2 = new ConcatenatedDataBuffer<>();
        ArrayList<DataBuffer<T>> arrayList = ((ConcatenatedDataBuffer) concatenatedDataBuffer).zznf;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            DataBuffer<T> dataBuffer2 = arrayList.get(i);
            i++;
            concatenatedDataBuffer2.append(dataBuffer2);
        }
        concatenatedDataBuffer2.append(dataBuffer);
        return concatenatedDataBuffer2;
    }

    public final void append(DataBuffer<T> dataBuffer) {
        if (dataBuffer == null) {
            return;
        }
        synchronized (this) {
            if (this.zznf.isEmpty()) {
                this.mBundle = new Bundle();
                Bundle metadata = dataBuffer.getMetadata();
                if (metadata != null) {
                    this.mBundle.putString(DataBufferUtils.KEY_PREV_PAGE_TOKEN, metadata.getString(DataBufferUtils.KEY_PREV_PAGE_TOKEN));
                }
            }
            this.zznf.add(dataBuffer);
            computeCounts();
            Bundle metadata2 = dataBuffer.getMetadata();
            if (metadata2 != null) {
                this.mBundle.putString(DataBufferUtils.KEY_NEXT_PAGE_TOKEN, metadata2.getString(DataBufferUtils.KEY_NEXT_PAGE_TOKEN));
            } else {
                this.mBundle.remove(DataBufferUtils.KEY_NEXT_PAGE_TOKEN);
            }
        }
    }

    @Override // com.google.android.gms.common.data.ExclusionFilterable
    public final void clearFilters() {
        int size = this.zznf.size();
        for (int i = 0; i < size; i++) {
            DataBuffer<T> dataBuffer = this.zznf.get(i);
            if (dataBuffer instanceof ExclusionFilterable) {
                ((ExclusionFilterable) dataBuffer).clearFilters();
            }
        }
        computeCounts();
    }

    @Override // com.google.android.gms.common.data.DataBuffer
    @Deprecated
    public final void close() {
        release();
    }

    public final void computeCounts() {
        this.zzng.clear();
        int size = this.zznf.size();
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            DataBuffer<T> dataBuffer = this.zznf.get(i2);
            if (dataBuffer != null) {
                i += dataBuffer.getCount();
            }
            this.zzng.add(Integer.valueOf(i));
        }
        this.zznh = i;
    }

    @Override // com.google.android.gms.common.data.ExclusionFilterable
    public final void filterOut(String str) {
        int size = this.zznf.size();
        for (int i = 0; i < size; i++) {
            DataBuffer<T> dataBuffer = this.zznf.get(i);
            if (dataBuffer instanceof ExclusionFilterable) {
                ((ExclusionFilterable) dataBuffer).filterOut(str);
            }
        }
        computeCounts();
    }

    @Override // com.google.android.gms.common.data.DataBuffer
    public final T get(int i) {
        DataBuffer<T> dataBuffer;
        synchronized (this) {
            int size = this.zznf.size();
            for (int i2 = 0; i2 < size; i2++) {
                int intValue = this.zzng.get(i2).intValue();
                if (i < intValue && (dataBuffer = this.zznf.get(i2)) != null) {
                    return dataBuffer.get((i - intValue) + dataBuffer.getCount());
                }
            }
            return null;
        }
    }

    @Override // com.google.android.gms.common.data.DataBuffer
    public final int getCount() {
        int i;
        synchronized (this) {
            i = this.zznh;
        }
        return i;
    }

    @Override // com.google.android.gms.common.data.DataBuffer
    public final Bundle getMetadata() {
        Bundle bundle;
        synchronized (this) {
            bundle = this.mBundle;
        }
        return bundle;
    }

    @Override // com.google.android.gms.common.data.DataBuffer
    @Deprecated
    public final boolean isClosed() {
        return false;
    }

    @Override // com.google.android.gms.common.data.DataBuffer, java.lang.Iterable
    public final Iterator<T> iterator() {
        return new DataBufferIterator(this);
    }

    @Override // com.google.android.gms.common.data.DataBuffer, com.google.android.gms.common.api.Releasable
    public final void release() {
        synchronized (this) {
            int size = this.zznf.size();
            for (int i = 0; i < size; i++) {
                DataBuffer<T> dataBuffer = this.zznf.get(i);
                if (dataBuffer != null) {
                    dataBuffer.release();
                }
            }
            this.zznf.clear();
            this.zzng.clear();
            this.mBundle = null;
        }
    }

    @Override // com.google.android.gms.common.data.TextFilterable
    public final void setFilterTerm(Context context, TextFilterable.StringFilter stringFilter, String str) {
        int size = this.zznf.size();
        for (int i = 0; i < size; i++) {
            DataBuffer<T> dataBuffer = this.zznf.get(i);
            if (dataBuffer instanceof TextFilterable) {
                ((TextFilterable) dataBuffer).setFilterTerm(context, stringFilter, str);
            }
        }
        computeCounts();
    }

    @Override // com.google.android.gms.common.data.TextFilterable
    public final void setFilterTerm(Context context, String str) {
        int size = this.zznf.size();
        for (int i = 0; i < size; i++) {
            DataBuffer<T> dataBuffer = this.zznf.get(i);
            if (dataBuffer instanceof TextFilterable) {
                ((TextFilterable) dataBuffer).setFilterTerm(context, str);
            }
        }
        computeCounts();
    }

    @Override // com.google.android.gms.common.data.DataBuffer
    public final Iterator<T> singleRefIterator() {
        return iterator();
    }

    @Override // com.google.android.gms.common.data.ExclusionFilterable
    public final void unfilter(String str) {
        int size = this.zznf.size();
        for (int i = 0; i < size; i++) {
            DataBuffer<T> dataBuffer = this.zznf.get(i);
            if (dataBuffer instanceof ExclusionFilterable) {
                ((ExclusionFilterable) dataBuffer).unfilter(str);
            }
        }
        computeCounts();
    }
}
