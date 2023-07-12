package com.google.android.gms.internal.measurement;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.Iterator;

@SafeParcelable.Class(creator = "EventParamsCreator")
@SafeParcelable.Reserved({1})
/* loaded from: classes.dex */
public final class zzer extends AbstractSafeParcelable implements Iterable<String> {
    public static final Parcelable.Creator<zzer> CREATOR = new zzet();
    @SafeParcelable.Field(getter = "z", id = 2)
    private final Bundle zzafy;

    /* JADX INFO: Access modifiers changed from: package-private */
    @SafeParcelable.Constructor
    public zzer(@SafeParcelable.Param(id = 2) Bundle bundle) {
        this.zzafy = bundle;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Object get(String str) {
        return this.zzafy.get(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Long getLong(String str) {
        return Long.valueOf(this.zzafy.getLong(str));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String getString(String str) {
        return this.zzafy.getString(str);
    }

    @Override // java.lang.Iterable
    public final Iterator<String> iterator() {
        return new zzes(this);
    }

    public final int size() {
        return this.zzafy.size();
    }

    public final String toString() {
        return this.zzafy.toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeBundle(parcel, 2, zzif(), false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Double zzbh(String str) {
        return Double.valueOf(this.zzafy.getDouble(str));
    }

    public final Bundle zzif() {
        return new Bundle(this.zzafy);
    }
}
