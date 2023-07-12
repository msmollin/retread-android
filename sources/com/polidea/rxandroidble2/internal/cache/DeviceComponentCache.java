package com.polidea.rxandroidble2.internal.cache;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import bleshadow.javax.inject.Inject;
import com.polidea.rxandroidble2.ClientScope;
import com.polidea.rxandroidble2.internal.DeviceComponent;
import com.polidea.rxandroidble2.internal.cache.DeviceComponentWeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@ClientScope
/* loaded from: classes.dex */
public class DeviceComponentCache implements Map<String, DeviceComponent> {
    private final HashMap<String, DeviceComponentWeakReference> cache;
    private final DeviceComponentWeakReference.Provider deviceComponentReferenceProvider;

    @Inject
    public DeviceComponentCache() {
        this(new DeviceComponentWeakReference.Provider() { // from class: com.polidea.rxandroidble2.internal.cache.DeviceComponentCache.1
            @Override // com.polidea.rxandroidble2.internal.cache.DeviceComponentWeakReference.Provider
            public DeviceComponentWeakReference provide(DeviceComponent deviceComponent) {
                return new DeviceComponentWeakReference(deviceComponent);
            }
        });
    }

    DeviceComponentCache(DeviceComponentWeakReference.Provider provider) {
        this.cache = new HashMap<>();
        this.deviceComponentReferenceProvider = provider;
    }

    @Override // java.util.Map
    public void clear() {
        this.cache.clear();
    }

    @Override // java.util.Map
    public boolean containsKey(Object obj) {
        return this.cache.containsKey(obj) && get(obj) != null;
    }

    @Override // java.util.Map
    public boolean containsValue(Object obj) {
        for (DeviceComponentWeakReference deviceComponentWeakReference : this.cache.values()) {
            if (deviceComponentWeakReference.contains(obj)) {
                return true;
            }
        }
        return false;
    }

    @Override // java.util.Map
    @NonNull
    public Set<Map.Entry<String, DeviceComponent>> entrySet() {
        HashSet hashSet = new HashSet();
        for (Map.Entry<String, DeviceComponentWeakReference> entry : this.cache.entrySet()) {
            DeviceComponentWeakReference value = entry.getValue();
            if (!value.isEmpty()) {
                hashSet.add(new CacheEntry(entry.getKey(), this.deviceComponentReferenceProvider.provide((DeviceComponent) value.get())));
            }
        }
        return hashSet;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Map
    @Nullable
    public DeviceComponent get(Object obj) {
        DeviceComponentWeakReference deviceComponentWeakReference = this.cache.get(obj);
        if (deviceComponentWeakReference != null) {
            return (DeviceComponent) deviceComponentWeakReference.get();
        }
        return null;
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        evictEmptyReferences();
        return this.cache.isEmpty();
    }

    @Override // java.util.Map
    @NonNull
    public Set<String> keySet() {
        return this.cache.keySet();
    }

    @Override // java.util.Map
    public DeviceComponent put(String str, DeviceComponent deviceComponent) {
        this.cache.put(str, this.deviceComponentReferenceProvider.provide(deviceComponent));
        evictEmptyReferences();
        return deviceComponent;
    }

    @Override // java.util.Map
    public void putAll(@NonNull Map<? extends String, ? extends DeviceComponent> map) {
        for (Map.Entry<? extends String, ? extends DeviceComponent> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Map
    public DeviceComponent remove(Object obj) {
        DeviceComponentWeakReference remove = this.cache.remove(obj);
        evictEmptyReferences();
        if (remove != null) {
            return (DeviceComponent) remove.get();
        }
        return null;
    }

    @Override // java.util.Map
    public int size() {
        evictEmptyReferences();
        return this.cache.size();
    }

    @Override // java.util.Map
    @NonNull
    public Collection<DeviceComponent> values() {
        ArrayList arrayList = new ArrayList();
        for (DeviceComponentWeakReference deviceComponentWeakReference : this.cache.values()) {
            if (!deviceComponentWeakReference.isEmpty()) {
                arrayList.add(deviceComponentWeakReference.get());
            }
        }
        return arrayList;
    }

    private void evictEmptyReferences() {
        Iterator<Map.Entry<String, DeviceComponentWeakReference>> it = this.cache.entrySet().iterator();
        while (it.hasNext()) {
            if (it.next().getValue().isEmpty()) {
                it.remove();
            }
        }
    }
}
