package com.polidea.rxandroidble2;

import bleshadow.dagger.internal.Factory;
import bleshadow.dagger.internal.Preconditions;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.ClientComponent;
import com.polidea.rxandroidble2.internal.util.LocationServicesOkObservableApi23;
import io.reactivex.Observable;

/* loaded from: classes.dex */
public final class ClientComponent_ClientModule_ProvideLocationServicesOkObservableFactory implements Factory<Observable<Boolean>> {
    private final Provider<Integer> deviceSdkProvider;
    private final Provider<LocationServicesOkObservableApi23> locationServicesOkObservableApi23Provider;
    private final ClientComponent.ClientModule module;

    public ClientComponent_ClientModule_ProvideLocationServicesOkObservableFactory(ClientComponent.ClientModule clientModule, Provider<Integer> provider, Provider<LocationServicesOkObservableApi23> provider2) {
        this.module = clientModule;
        this.deviceSdkProvider = provider;
        this.locationServicesOkObservableApi23Provider = provider2;
    }

    @Override // bleshadow.javax.inject.Provider
    public Observable<Boolean> get() {
        return (Observable) Preconditions.checkNotNull(this.module.provideLocationServicesOkObservable(this.deviceSdkProvider.get().intValue(), this.locationServicesOkObservableApi23Provider), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static ClientComponent_ClientModule_ProvideLocationServicesOkObservableFactory create(ClientComponent.ClientModule clientModule, Provider<Integer> provider, Provider<LocationServicesOkObservableApi23> provider2) {
        return new ClientComponent_ClientModule_ProvideLocationServicesOkObservableFactory(clientModule, provider, provider2);
    }

    public static Observable<Boolean> proxyProvideLocationServicesOkObservable(ClientComponent.ClientModule clientModule, int i, Provider<LocationServicesOkObservableApi23> provider) {
        return (Observable) Preconditions.checkNotNull(clientModule.provideLocationServicesOkObservable(i, provider), "Cannot return null from a non-@Nullable @Provides method");
    }
}
