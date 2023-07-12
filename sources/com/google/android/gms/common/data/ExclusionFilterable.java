package com.google.android.gms.common.data;

/* loaded from: classes.dex */
public interface ExclusionFilterable {
    void clearFilters();

    void filterOut(String str);

    void unfilter(String str);
}
