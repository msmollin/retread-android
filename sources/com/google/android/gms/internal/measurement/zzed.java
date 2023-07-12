package com.google.android.gms.internal.measurement;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

@SafeParcelable.Class(creator = "ConditionalUserPropertyParcelCreator")
/* loaded from: classes.dex */
public final class zzed extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzed> CREATOR = new zzee();
    @SafeParcelable.Field(id = 6)
    public boolean active;
    @SafeParcelable.Field(id = 5)
    public long creationTimestamp;
    @SafeParcelable.Field(id = 3)
    public String origin;
    @SafeParcelable.Field(id = 2)
    public String packageName;
    @SafeParcelable.Field(id = 11)
    public long timeToLive;
    @SafeParcelable.Field(id = 7)
    public String triggerEventName;
    @SafeParcelable.Field(id = 9)
    public long triggerTimeout;
    @SafeParcelable.Field(id = 4)
    public zzjx zzaep;
    @SafeParcelable.Field(id = 8)
    public zzeu zzaeq;
    @SafeParcelable.Field(id = 10)
    public zzeu zzaer;
    @SafeParcelable.Field(id = 12)
    public zzeu zzaes;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzed(zzed zzedVar) {
        Preconditions.checkNotNull(zzedVar);
        this.packageName = zzedVar.packageName;
        this.origin = zzedVar.origin;
        this.zzaep = zzedVar.zzaep;
        this.creationTimestamp = zzedVar.creationTimestamp;
        this.active = zzedVar.active;
        this.triggerEventName = zzedVar.triggerEventName;
        this.zzaeq = zzedVar.zzaeq;
        this.triggerTimeout = zzedVar.triggerTimeout;
        this.zzaer = zzedVar.zzaer;
        this.timeToLive = zzedVar.timeToLive;
        this.zzaes = zzedVar.zzaes;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @SafeParcelable.Constructor
    public zzed(@SafeParcelable.Param(id = 2) String str, @SafeParcelable.Param(id = 3) String str2, @SafeParcelable.Param(id = 4) zzjx zzjxVar, @SafeParcelable.Param(id = 5) long j, @SafeParcelable.Param(id = 6) boolean z, @SafeParcelable.Param(id = 7) String str3, @SafeParcelable.Param(id = 8) zzeu zzeuVar, @SafeParcelable.Param(id = 9) long j2, @SafeParcelable.Param(id = 10) zzeu zzeuVar2, @SafeParcelable.Param(id = 11) long j3, @SafeParcelable.Param(id = 12) zzeu zzeuVar3) {
        this.packageName = str;
        this.origin = str2;
        this.zzaep = zzjxVar;
        this.creationTimestamp = j;
        this.active = z;
        this.triggerEventName = str3;
        this.zzaeq = zzeuVar;
        this.triggerTimeout = j2;
        this.zzaer = zzeuVar2;
        this.timeToLive = j3;
        this.zzaes = zzeuVar3;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.packageName, false);
        SafeParcelWriter.writeString(parcel, 3, this.origin, false);
        SafeParcelWriter.writeParcelable(parcel, 4, this.zzaep, i, false);
        SafeParcelWriter.writeLong(parcel, 5, this.creationTimestamp);
        SafeParcelWriter.writeBoolean(parcel, 6, this.active);
        SafeParcelWriter.writeString(parcel, 7, this.triggerEventName, false);
        SafeParcelWriter.writeParcelable(parcel, 8, this.zzaeq, i, false);
        SafeParcelWriter.writeLong(parcel, 9, this.triggerTimeout);
        SafeParcelWriter.writeParcelable(parcel, 10, this.zzaer, i, false);
        SafeParcelWriter.writeLong(parcel, 11, this.timeToLive);
        SafeParcelWriter.writeParcelable(parcel, 12, this.zzaes, i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
