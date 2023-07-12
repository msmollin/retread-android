package com.polidea.rxandroidble2.helpers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/* loaded from: classes.dex */
public class AdvertisedServiceUUIDExtractor {
    private static final String UUID_BASE = "%08x-0000-1000-8000-00805f9b34fb";

    public List<UUID> extractUUIDs(byte[] bArr) {
        byte b;
        ArrayList arrayList = new ArrayList();
        ByteBuffer order = ByteBuffer.wrap(bArr).order(ByteOrder.LITTLE_ENDIAN);
        while (order.remaining() > 2 && (b = order.get()) != 0) {
            switch (order.get()) {
                case 2:
                case 3:
                    while (b >= 2) {
                        arrayList.add(UUID.fromString(String.format(UUID_BASE, Short.valueOf(order.getShort()))));
                        b = (byte) (b - 2);
                    }
                    continue;
                case 4:
                case 5:
                    while (b >= 4) {
                        arrayList.add(UUID.fromString(String.format(UUID_BASE, Integer.valueOf(order.getInt()))));
                        b = (byte) (b - 4);
                    }
                    break;
                case 6:
                case 7:
                    break;
                default:
                    order.position((order.position() + b) - 1);
                    continue;
            }
            while (b >= 16) {
                arrayList.add(new UUID(order.getLong(), order.getLong()));
                b = (byte) (b - 16);
            }
        }
        return arrayList;
    }

    @NonNull
    public Set<UUID> toDistinctSet(@Nullable UUID[] uuidArr) {
        if (uuidArr == null) {
            uuidArr = new UUID[0];
        }
        return new HashSet(Arrays.asList(uuidArr));
    }
}
