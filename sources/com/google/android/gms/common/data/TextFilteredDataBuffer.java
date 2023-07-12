package com.google.android.gms.common.data;

import android.content.Context;
import android.text.TextUtils;
import com.google.android.gms.common.data.TextFilterable;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.ArrayList;
import java.util.Locale;

/* loaded from: classes.dex */
public final class TextFilteredDataBuffer<T> extends FilteredDataBuffer<T> implements TextFilterable {
    private final ArrayList<Integer> zzob;
    private AbstractDataBuffer<T> zzoc;
    private final String zzoo;
    private String zzop;
    private TextFilterable.StringFilter zzoq;
    private Locale zzor;

    public TextFilteredDataBuffer(AbstractDataBuffer<T> abstractDataBuffer, String str) {
        super(abstractDataBuffer);
        this.zzob = new ArrayList<>();
        this.zzoc = abstractDataBuffer;
        this.zzoo = str;
    }

    private final String zzh(String str) {
        String lowerCase = str.toLowerCase(this.zzor);
        StringBuilder sb = new StringBuilder();
        int length = lowerCase.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isIdentifierIgnorable(lowerCase.charAt(i))) {
                sb.append(lowerCase.charAt(i));
            }
        }
        return sb.toString();
    }

    @Override // com.google.android.gms.common.data.FilteredDataBuffer
    public final int computeRealPosition(int i) {
        if (TextUtils.isEmpty(this.zzop)) {
            return i;
        }
        if (i < 0 || i >= this.zzob.size()) {
            StringBuilder sb = new StringBuilder(53);
            sb.append("Position ");
            sb.append(i);
            sb.append(" is out of bounds for this buffer");
            throw new IllegalArgumentException(sb.toString());
        }
        return this.zzob.get(i).intValue();
    }

    @Override // com.google.android.gms.common.data.DataBuffer
    public final int getCount() {
        return TextUtils.isEmpty(this.zzop) ? this.mDataBuffer.getCount() : this.zzob.size();
    }

    @Override // com.google.android.gms.common.data.TextFilterable
    public final void setFilterTerm(Context context, TextFilterable.StringFilter stringFilter, String str) {
        Preconditions.checkNotNull(stringFilter);
        this.zzop = str;
        this.zzoq = stringFilter;
        if (TextUtils.isEmpty(this.zzop)) {
            this.zzob.clear();
            return;
        }
        this.zzor = context.getResources().getConfiguration().locale;
        this.zzop = zzh(this.zzop);
        this.zzob.clear();
        DataHolder dataHolder = this.zzoc.mDataHolder;
        String str2 = this.zzoo;
        boolean z = this.zzoc instanceof EntityBuffer;
        int count = this.zzoc.getCount();
        for (int i = 0; i < count; i++) {
            int zzi = z ? ((EntityBuffer) this.zzoc).zzi(i) : i;
            String string = dataHolder.getString(str2, zzi, dataHolder.getWindowIndex(zzi));
            if (!TextUtils.isEmpty(string) && this.zzoq.matches(zzh(string), this.zzop)) {
                this.zzob.add(Integer.valueOf(i));
            }
        }
    }

    @Override // com.google.android.gms.common.data.TextFilterable
    @VisibleForTesting
    public final void setFilterTerm(Context context, String str) {
        setFilterTerm(context, CONTAINS, str);
    }
}
