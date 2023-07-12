package com.google.firebase.components;

import java.util.HashSet;
import java.util.Set;

/* loaded from: classes.dex */
final class zzh {
    private final Component<?> zzaj;
    private final Set<zzh> zzak = new HashSet();
    private final Set<zzh> zzal = new HashSet();

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzh(Component<?> component) {
        this.zzaj = component;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zza(zzh zzhVar) {
        this.zzak.add(zzhVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zzb(zzh zzhVar) {
        this.zzal.add(zzhVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zzc(zzh zzhVar) {
        this.zzal.remove(zzhVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Set<zzh> zzf() {
        return this.zzak;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Component<?> zzk() {
        return this.zzaj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean zzl() {
        return this.zzal.isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean zzm() {
        return this.zzak.isEmpty();
    }
}
