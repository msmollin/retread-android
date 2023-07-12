package com.treadly.Treadly.UI.Util;

import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes2.dex */
public class IndexedPagerSnapHelper extends PagerSnapHelper {
    public int currentIndex = 0;

    @Override // androidx.recyclerview.widget.PagerSnapHelper, androidx.recyclerview.widget.SnapHelper
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int i, int i2) {
        this.currentIndex = super.findTargetSnapPosition(layoutManager, i, i2);
        return this.currentIndex;
    }
}
