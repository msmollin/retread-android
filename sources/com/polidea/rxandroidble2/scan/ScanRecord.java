package com.polidea.rxandroidble2.scan;

import android.os.ParcelUuid;
import android.util.SparseArray;
import androidx.annotation.Nullable;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public interface ScanRecord {
    int getAdvertiseFlags();

    byte[] getBytes();

    @Nullable
    String getDeviceName();

    SparseArray<byte[]> getManufacturerSpecificData();

    @Nullable
    byte[] getManufacturerSpecificData(int i);

    Map<ParcelUuid, byte[]> getServiceData();

    @Nullable
    byte[] getServiceData(ParcelUuid parcelUuid);

    @Nullable
    List<ParcelUuid> getServiceUuids();

    int getTxPowerLevel();
}
