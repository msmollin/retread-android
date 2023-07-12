package com.google.android.gms.internal.measurement;

import java.lang.Comparable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class zzaay<K extends Comparable<K>, V> extends AbstractMap<K, V> {
    private boolean zzbls;
    private final int zzbtx;
    private List<zzabd> zzbty;
    private Map<K, V> zzbtz;
    private volatile zzabf zzbua;
    private Map<K, V> zzbub;

    private zzaay(int i) {
        this.zzbtx = i;
        this.zzbty = Collections.emptyList();
        this.zzbtz = Collections.emptyMap();
        this.zzbub = Collections.emptyMap();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzaay(int i, zzaaz zzaazVar) {
        this(i);
    }

    private final int zza(K k) {
        int size = this.zzbty.size() - 1;
        if (size >= 0) {
            int compareTo = k.compareTo((Comparable) this.zzbty.get(size).getKey());
            if (compareTo > 0) {
                return -(size + 2);
            }
            if (compareTo == 0) {
                return size;
            }
        }
        int i = 0;
        while (i <= size) {
            int i2 = (i + size) / 2;
            int compareTo2 = k.compareTo((Comparable) this.zzbty.get(i2).getKey());
            if (compareTo2 < 0) {
                size = i2 - 1;
            } else if (compareTo2 <= 0) {
                return i2;
            } else {
                i = i2 + 1;
            }
        }
        return -(i + 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <FieldDescriptorType extends zzzo<FieldDescriptorType>> zzaay<FieldDescriptorType, Object> zzag(int i) {
        return new zzaaz(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final V zzai(int i) {
        zzul();
        V v = (V) this.zzbty.remove(i).getValue();
        if (!this.zzbtz.isEmpty()) {
            Iterator<Map.Entry<K, V>> it = zzum().entrySet().iterator();
            this.zzbty.add(new zzabd(this, it.next()));
            it.remove();
        }
        return v;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zzul() {
        if (this.zzbls) {
            throw new UnsupportedOperationException();
        }
    }

    private final SortedMap<K, V> zzum() {
        zzul();
        if (this.zzbtz.isEmpty() && !(this.zzbtz instanceof TreeMap)) {
            this.zzbtz = new TreeMap();
            this.zzbub = ((TreeMap) this.zzbtz).descendingMap();
        }
        return (SortedMap) this.zzbtz;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        zzul();
        if (!this.zzbty.isEmpty()) {
            this.zzbty.clear();
        }
        if (this.zzbtz.isEmpty()) {
            return;
        }
        this.zzbtz.clear();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object obj) {
        Comparable comparable = (Comparable) obj;
        return zza((zzaay<K, V>) comparable) >= 0 || this.zzbtz.containsKey(comparable);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        if (this.zzbua == null) {
            this.zzbua = new zzabf(this, null);
        }
        return this.zzbua;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof zzaay) {
            zzaay zzaayVar = (zzaay) obj;
            int size = size();
            if (size != zzaayVar.size()) {
                return false;
            }
            int zzuj = zzuj();
            if (zzuj != zzaayVar.zzuj()) {
                return entrySet().equals(zzaayVar.entrySet());
            }
            for (int i = 0; i < zzuj; i++) {
                if (!zzah(i).equals(zzaayVar.zzah(i))) {
                    return false;
                }
            }
            if (zzuj != size) {
                return this.zzbtz.equals(zzaayVar.zzbtz);
            }
            return true;
        }
        return super.equals(obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractMap, java.util.Map
    public V get(Object obj) {
        Comparable comparable = (Comparable) obj;
        int zza = zza((zzaay<K, V>) comparable);
        return zza >= 0 ? (V) this.zzbty.get(zza).getValue() : this.zzbtz.get(comparable);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int hashCode() {
        int zzuj = zzuj();
        int i = 0;
        for (int i2 = 0; i2 < zzuj; i2++) {
            i += this.zzbty.get(i2).hashCode();
        }
        return this.zzbtz.size() > 0 ? i + this.zzbtz.hashCode() : i;
    }

    public final boolean isImmutable() {
        return this.zzbls;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractMap, java.util.Map
    public /* synthetic */ Object put(Object obj, Object obj2) {
        return zza((zzaay<K, V>) ((Comparable) obj), (Comparable) obj2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractMap, java.util.Map
    public V remove(Object obj) {
        zzul();
        Comparable comparable = (Comparable) obj;
        int zza = zza((zzaay<K, V>) comparable);
        if (zza >= 0) {
            return (V) zzai(zza);
        }
        if (this.zzbtz.isEmpty()) {
            return null;
        }
        return this.zzbtz.remove(comparable);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        return this.zzbty.size() + this.zzbtz.size();
    }

    public final V zza(K k, V v) {
        zzul();
        int zza = zza((zzaay<K, V>) k);
        if (zza >= 0) {
            return (V) this.zzbty.get(zza).setValue(v);
        }
        zzul();
        if (this.zzbty.isEmpty() && !(this.zzbty instanceof ArrayList)) {
            this.zzbty = new ArrayList(this.zzbtx);
        }
        int i = -(zza + 1);
        if (i >= this.zzbtx) {
            return zzum().put(k, v);
        }
        if (this.zzbty.size() == this.zzbtx) {
            zzabd remove = this.zzbty.remove(this.zzbtx - 1);
            zzum().put((K) remove.getKey(), (V) remove.getValue());
        }
        this.zzbty.add(i, new zzabd(this, k, v));
        return null;
    }

    public final Map.Entry<K, V> zzah(int i) {
        return this.zzbty.get(i);
    }

    public void zzrg() {
        if (this.zzbls) {
            return;
        }
        this.zzbtz = this.zzbtz.isEmpty() ? Collections.emptyMap() : Collections.unmodifiableMap(this.zzbtz);
        this.zzbub = this.zzbub.isEmpty() ? Collections.emptyMap() : Collections.unmodifiableMap(this.zzbub);
        this.zzbls = true;
    }

    public final int zzuj() {
        return this.zzbty.size();
    }

    public final Iterable<Map.Entry<K, V>> zzuk() {
        return this.zzbtz.isEmpty() ? zzaba.zzun() : this.zzbtz.entrySet();
    }
}
