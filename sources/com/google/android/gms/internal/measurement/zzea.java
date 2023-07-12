package com.google.android.gms.internal.measurement;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;

/* loaded from: classes.dex */
public final class zzea implements Parcelable.Creator<zzdz> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzdz createFromParcel(Parcel parcel) {
        int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        boolean z = false;
        int i = 0;
        boolean z2 = false;
        boolean z3 = true;
        boolean z4 = true;
        boolean z5 = true;
        long j = 0;
        long j2 = 0;
        long j3 = 0;
        long j4 = 0;
        String str = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        long j5 = -2147483648L;
        while (parcel.dataPosition() < validateObjectHeader) {
            int readHeader = SafeParcelReader.readHeader(parcel);
            switch (SafeParcelReader.getFieldId(readHeader)) {
                case 2:
                    str = SafeParcelReader.createString(parcel, readHeader);
                    break;
                case 3:
                    str2 = SafeParcelReader.createString(parcel, readHeader);
                    break;
                case 4:
                    str3 = SafeParcelReader.createString(parcel, readHeader);
                    break;
                case 5:
                    str4 = SafeParcelReader.createString(parcel, readHeader);
                    break;
                case 6:
                    j = SafeParcelReader.readLong(parcel, readHeader);
                    break;
                case 7:
                    j2 = SafeParcelReader.readLong(parcel, readHeader);
                    break;
                case 8:
                    str5 = SafeParcelReader.createString(parcel, readHeader);
                    break;
                case 9:
                    z3 = SafeParcelReader.readBoolean(parcel, readHeader);
                    break;
                case 10:
                    z = SafeParcelReader.readBoolean(parcel, readHeader);
                    break;
                case 11:
                    j5 = SafeParcelReader.readLong(parcel, readHeader);
                    break;
                case 12:
                    str6 = SafeParcelReader.createString(parcel, readHeader);
                    break;
                case 13:
                    j3 = SafeParcelReader.readLong(parcel, readHeader);
                    break;
                case 14:
                    j4 = SafeParcelReader.readLong(parcel, readHeader);
                    break;
                case 15:
                    i = SafeParcelReader.readInt(parcel, readHeader);
                    break;
                case 16:
                    z4 = SafeParcelReader.readBoolean(parcel, readHeader);
                    break;
                case 17:
                    z5 = SafeParcelReader.readBoolean(parcel, readHeader);
                    break;
                case 18:
                    z2 = SafeParcelReader.readBoolean(parcel, readHeader);
                    break;
                default:
                    SafeParcelReader.skipUnknownField(parcel, readHeader);
                    break;
            }
        }
        SafeParcelReader.ensureAtEnd(parcel, validateObjectHeader);
        return new zzdz(str, str2, str3, str4, j, j2, str5, z3, z, j5, str6, j3, j4, i, z4, z5, z2);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzdz[] newArray(int i) {
        return new zzdz[i];
    }
}
