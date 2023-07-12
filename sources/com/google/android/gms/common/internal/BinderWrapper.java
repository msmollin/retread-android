package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.annotation.KeepName;

@KeepName
/* loaded from: classes.dex */
public final class BinderWrapper implements Parcelable {
    public static final Parcelable.Creator<BinderWrapper> CREATOR = new zza();
    private IBinder zzry;

    public BinderWrapper() {
        this.zzry = null;
    }

    public BinderWrapper(IBinder iBinder) {
        this.zzry = null;
        this.zzry = iBinder;
    }

    private BinderWrapper(Parcel parcel) {
        this.zzry = null;
        this.zzry = parcel.readStrongBinder();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ BinderWrapper(Parcel parcel, zza zzaVar) {
        this(parcel);
    }

    @Override // android.os.Parcelable
    public final int describeContents() {
        return 0;
    }

    public final IBinder getBinder() {
        return this.zzry;
    }

    public final void setBinder(IBinder iBinder) {
        this.zzry = iBinder;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeStrongBinder(this.zzry);
    }
}
