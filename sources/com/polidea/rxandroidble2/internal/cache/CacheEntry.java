package com.polidea.rxandroidble2.internal.cache;

import com.polidea.rxandroidble2.internal.DeviceComponent;
import java.util.Map;

/* loaded from: classes.dex */
class CacheEntry implements Map.Entry<String, DeviceComponent> {
    private final DeviceComponentWeakReference deviceComponentWeakReference;
    private final String string;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CacheEntry(String str, DeviceComponentWeakReference deviceComponentWeakReference) {
        this.string = str;
        this.deviceComponentWeakReference = deviceComponentWeakReference;
    }

    @Override // java.util.Map.Entry
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof CacheEntry) {
            CacheEntry cacheEntry = (CacheEntry) obj;
            return this.string.equals(cacheEntry.getKey()) && this.deviceComponentWeakReference.equals(cacheEntry.deviceComponentWeakReference);
        }
        return false;
    }

    @Override // java.util.Map.Entry
    public String getKey() {
        return this.string;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Map.Entry
    public DeviceComponent getValue() {
        return (DeviceComponent) this.deviceComponentWeakReference.get();
    }

    @Override // java.util.Map.Entry
    public int hashCode() {
        return (this.string.hashCode() * 31) + this.deviceComponentWeakReference.hashCode();
    }

    @Override // java.util.Map.Entry
    public DeviceComponent setValue(DeviceComponent deviceComponent) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
