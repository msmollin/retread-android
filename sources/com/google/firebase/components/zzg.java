package com.google.firebase.components;

import com.google.firebase.inject.Provider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public final class zzg implements ComponentContainer {
    private final List<Component<?>> zzah;
    private final Map<Class<?>, zzi<?>> zzai = new HashMap();

    public zzg(Iterable<ComponentRegistrar> iterable, Component<?>... componentArr) {
        zzh zzhVar;
        ArrayList<Component> arrayList = new ArrayList();
        for (ComponentRegistrar componentRegistrar : iterable) {
            arrayList.addAll(componentRegistrar.getComponents());
        }
        Collections.addAll(arrayList, componentArr);
        HashMap hashMap = new HashMap(arrayList.size());
        for (Component component : arrayList) {
            zzh zzhVar2 = new zzh(component);
            for (Class cls : component.zze()) {
                if (hashMap.put(cls, zzhVar2) != null) {
                    throw new IllegalArgumentException(String.format("Multiple components provide %s.", cls));
                }
            }
        }
        for (zzh zzhVar3 : hashMap.values()) {
            for (Dependency dependency : zzhVar3.zzk().zzf()) {
                if (dependency.zzp() && (zzhVar = (zzh) hashMap.get(dependency.zzn())) != null) {
                    zzhVar3.zza(zzhVar);
                    zzhVar.zzb(zzhVar3);
                }
            }
        }
        HashSet<zzh> hashSet = new HashSet(hashMap.values());
        HashSet hashSet2 = new HashSet();
        for (zzh zzhVar4 : hashSet) {
            if (zzhVar4.zzl()) {
                hashSet2.add(zzhVar4);
            }
        }
        ArrayList arrayList2 = new ArrayList();
        while (!hashSet2.isEmpty()) {
            zzh zzhVar5 = (zzh) hashSet2.iterator().next();
            hashSet2.remove(zzhVar5);
            arrayList2.add(zzhVar5.zzk());
            for (zzh zzhVar6 : zzhVar5.zzf()) {
                zzhVar6.zzc(zzhVar5);
                if (zzhVar6.zzl()) {
                    hashSet2.add(zzhVar6);
                }
            }
        }
        if (arrayList2.size() != arrayList.size()) {
            ArrayList arrayList3 = new ArrayList();
            for (zzh zzhVar7 : hashSet) {
                if (!zzhVar7.zzl() && !zzhVar7.zzm()) {
                    arrayList3.add(zzhVar7.zzk());
                }
            }
            throw new DependencyCycleException(arrayList3);
        }
        Collections.reverse(arrayList2);
        this.zzah = Collections.unmodifiableList(arrayList2);
        for (Component<?> component2 : this.zzah) {
            zzi<?> zziVar = new zzi<>(component2.zzg(), new zzl(component2.zzf(), this));
            for (Class<? super Object> cls2 : component2.zze()) {
                this.zzai.put(cls2, zziVar);
            }
        }
        for (Component<?> component3 : this.zzah) {
            for (Dependency dependency2 : component3.zzf()) {
                if (dependency2.zzo() && !this.zzai.containsKey(dependency2.zzn())) {
                    throw new MissingDependencyException(String.format("Unsatisfied dependency for component %s: %s", component3, dependency2.zzn()));
                }
            }
        }
    }

    @Override // com.google.firebase.components.ComponentContainer
    public final Object get(Class cls) {
        return ComponentContainer$$CC.get(this, cls);
    }

    @Override // com.google.firebase.components.ComponentContainer
    public final <T> Provider<T> getProvider(Class<T> cls) {
        zzk.zza(cls, "Null interface requested.");
        return this.zzai.get(cls);
    }

    public final void zzb(boolean z) {
        for (Component<?> component : this.zzah) {
            if (component.zzh() || (component.zzi() && z)) {
                get(component.zze().iterator().next());
            }
        }
    }
}
