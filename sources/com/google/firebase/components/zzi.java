package com.google.firebase.components;

import com.google.firebase.inject.Provider;

/* loaded from: classes.dex */
final class zzi<T> implements Provider<T> {
    private static final Object zzaq = new Object();
    private volatile Object zzar = zzaq;
    private volatile Provider<T> zzas;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzi(final ComponentFactory<T> componentFactory, final ComponentContainer componentContainer) {
        this.zzas = new Provider(componentFactory, componentContainer) { // from class: com.google.firebase.components.zzj
            private final ComponentFactory zzat;
            private final ComponentContainer zzau;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.zzat = componentFactory;
                this.zzau = componentContainer;
            }

            @Override // com.google.firebase.inject.Provider
            public final Object get() {
                Object create;
                create = this.zzat.create(this.zzau);
                return create;
            }
        };
    }

    @Override // com.google.firebase.inject.Provider
    public final T get() {
        T t = (T) this.zzar;
        if (t == zzaq) {
            synchronized (this) {
                t = this.zzar;
                if (t == zzaq) {
                    t = this.zzas.get();
                    this.zzar = t;
                    this.zzas = null;
                }
            }
        }
        return t;
    }
}
