package com.polidea.rxandroidble2;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.content.ContentResolver;
import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import bleshadow.dagger.Binds;
import bleshadow.dagger.Component;
import bleshadow.dagger.Module;
import bleshadow.dagger.Provides;
import bleshadow.javax.inject.Named;
import bleshadow.javax.inject.Provider;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.polidea.rxandroidble2.RxBleAdapterStateObservable;
import com.polidea.rxandroidble2.helpers.LocationServicesOkObservable;
import com.polidea.rxandroidble2.internal.DeviceComponent;
import com.polidea.rxandroidble2.internal.scan.InternalToExternalScanResultConverter;
import com.polidea.rxandroidble2.internal.scan.RxBleInternalScanResult;
import com.polidea.rxandroidble2.internal.scan.ScanPreconditionsVerifier;
import com.polidea.rxandroidble2.internal.scan.ScanPreconditionsVerifierApi18;
import com.polidea.rxandroidble2.internal.scan.ScanPreconditionsVerifierApi24;
import com.polidea.rxandroidble2.internal.scan.ScanSetupBuilder;
import com.polidea.rxandroidble2.internal.scan.ScanSetupBuilderImplApi18;
import com.polidea.rxandroidble2.internal.scan.ScanSetupBuilderImplApi21;
import com.polidea.rxandroidble2.internal.scan.ScanSetupBuilderImplApi23;
import com.polidea.rxandroidble2.internal.serialization.ClientOperationQueue;
import com.polidea.rxandroidble2.internal.serialization.ClientOperationQueueImpl;
import com.polidea.rxandroidble2.internal.util.LocationServicesOkObservableApi23;
import com.polidea.rxandroidble2.internal.util.LocationServicesStatus;
import com.polidea.rxandroidble2.internal.util.LocationServicesStatusApi18;
import com.polidea.rxandroidble2.internal.util.LocationServicesStatusApi23;
import com.polidea.rxandroidble2.internal.util.ObservableUtil;
import com.polidea.rxandroidble2.scan.ScanResult;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component(modules = {ClientModule.class, ClientModuleBinder.class})
@ClientScope
/* loaded from: classes.dex */
public interface ClientComponent {

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    /* loaded from: classes.dex */
    public interface ClientComponentFinalizer {
        void onFinalize();
    }

    @Module
    /* loaded from: classes.dex */
    public static abstract class ClientModuleBinder {
        @Binds
        @ClientScope
        abstract ClientOperationQueue bindClientOperationQueue(ClientOperationQueueImpl clientOperationQueueImpl);

        @Binds
        @ClientScope
        abstract RxBleClient bindRxBleClient(RxBleClientImpl rxBleClientImpl);

        @Binds
        abstract Observable<RxBleAdapterStateObservable.BleAdapterState> bindStateObs(RxBleAdapterStateObservable rxBleAdapterStateObservable);

        @Named(NamedSchedulers.TIMEOUT)
        @Binds
        abstract Scheduler bindTimeoutScheduler(@Named("computation") Scheduler scheduler);

        @Binds
        abstract Function<RxBleInternalScanResult, ScanResult> provideScanResultMapper(InternalToExternalScanResultConverter internalToExternalScanResultConverter);
    }

    LocationServicesOkObservable locationServicesOkObservable();

    RxBleClient rxBleClient();

    /* loaded from: classes.dex */
    public static class NamedExecutors {
        public static final String BLUETOOTH_CALLBACKS = "executor_bluetooth_callbacks";
        public static final String BLUETOOTH_INTERACTION = "executor_bluetooth_interaction";
        public static final String CONNECTION_QUEUE = "executor_connection_queue";

        private NamedExecutors() {
        }
    }

    /* loaded from: classes.dex */
    public static class NamedSchedulers {
        public static final String BLUETOOTH_CALLBACKS = "bluetooth_callbacks";
        public static final String BLUETOOTH_INTERACTION = "bluetooth_interaction";
        public static final String COMPUTATION = "computation";
        public static final String TIMEOUT = "timeout";

        private NamedSchedulers() {
        }
    }

    /* loaded from: classes.dex */
    public static class PlatformConstants {
        public static final String BOOL_IS_ANDROID_WEAR = "android-wear";
        public static final String INT_DEVICE_SDK = "device-sdk";
        public static final String INT_TARGET_SDK = "target-sdk";

        private PlatformConstants() {
        }
    }

    /* loaded from: classes.dex */
    public static class NamedBooleanObservables {
        public static final String LOCATION_SERVICES_OK = "location-ok-boolean-observable";

        private NamedBooleanObservables() {
        }
    }

    /* loaded from: classes.dex */
    public static class BluetoothConstants {
        public static final String DISABLE_NOTIFICATION_VALUE = "disable-notification-value";
        public static final String ENABLE_INDICATION_VALUE = "enable-indication-value";
        public static final String ENABLE_NOTIFICATION_VALUE = "enable-notification-value";

        private BluetoothConstants() {
        }
    }

    @Module(subcomponents = {DeviceComponent.class})
    /* loaded from: classes.dex */
    public static class ClientModule {
        private final Context context;

        public ClientModule(Context context) {
            this.context = context;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Provides
        public Context provideApplicationContext() {
            return this.context;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Provides
        public BluetoothManager provideBluetoothManager() {
            return (BluetoothManager) this.context.getSystemService("bluetooth");
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Nullable
        @Provides
        public static BluetoothAdapter provideBluetoothAdapter() {
            return BluetoothAdapter.getDefaultAdapter();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Named(NamedSchedulers.COMPUTATION)
        @Provides
        public static Scheduler provideComputationScheduler() {
            return Schedulers.computation();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Named(PlatformConstants.INT_DEVICE_SDK)
        @Provides
        public static int provideDeviceSdk() {
            return Build.VERSION.SDK_INT;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Provides
        public ContentResolver provideContentResolver() {
            return this.context.getContentResolver();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Provides
        public LocationServicesStatus provideLocationServicesStatus(@Named("device-sdk") int i, Provider<LocationServicesStatusApi18> provider, Provider<LocationServicesStatusApi23> provider2) {
            if (i < 23) {
                return provider.get();
            }
            return provider2.get();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Named(NamedBooleanObservables.LOCATION_SERVICES_OK)
        @Provides
        public Observable<Boolean> provideLocationServicesOkObservable(@Named("device-sdk") int i, Provider<LocationServicesOkObservableApi23> provider) {
            if (i < 23) {
                return ObservableUtil.justOnNext(true);
            }
            return provider.get();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Named(NamedExecutors.CONNECTION_QUEUE)
        @ClientScope
        @Provides
        public static ExecutorService provideConnectionQueueExecutorService() {
            return Executors.newCachedThreadPool();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Named(NamedExecutors.BLUETOOTH_INTERACTION)
        @ClientScope
        @Provides
        public static ExecutorService provideBluetoothInteractionExecutorService() {
            return Executors.newSingleThreadExecutor();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Named(NamedExecutors.BLUETOOTH_CALLBACKS)
        @ClientScope
        @Provides
        public static ExecutorService provideBluetoothCallbacksExecutorService() {
            return Executors.newSingleThreadExecutor();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Named(NamedSchedulers.BLUETOOTH_INTERACTION)
        @ClientScope
        @Provides
        public static Scheduler provideBluetoothInteractionScheduler(@Named("executor_bluetooth_interaction") ExecutorService executorService) {
            return Schedulers.from(executorService);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Named(NamedSchedulers.BLUETOOTH_CALLBACKS)
        @ClientScope
        @Provides
        public static Scheduler provideBluetoothCallbacksScheduler(@Named("executor_bluetooth_callbacks") ExecutorService executorService) {
            return Schedulers.from(executorService);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Provides
        public static ClientComponentFinalizer provideFinalizationCloseable(@Named("executor_bluetooth_interaction") final ExecutorService executorService, @Named("executor_bluetooth_callbacks") final ExecutorService executorService2, @Named("executor_connection_queue") final ExecutorService executorService3) {
            return new ClientComponentFinalizer() { // from class: com.polidea.rxandroidble2.ClientComponent.ClientModule.1
                @Override // com.polidea.rxandroidble2.ClientComponent.ClientComponentFinalizer
                public void onFinalize() {
                    executorService.shutdown();
                    executorService2.shutdown();
                    executorService3.shutdown();
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Provides
        public LocationManager provideLocationManager() {
            return (LocationManager) this.context.getSystemService(FirebaseAnalytics.Param.LOCATION);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Named(PlatformConstants.INT_TARGET_SDK)
        @Provides
        public int provideTargetSdk() {
            try {
                return this.context.getPackageManager().getApplicationInfo(this.context.getPackageName(), 0).targetSdkVersion;
            } catch (Throwable unused) {
                return Integer.MAX_VALUE;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Named(PlatformConstants.BOOL_IS_ANDROID_WEAR)
        @SuppressLint({"InlinedApi"})
        @Provides
        public boolean provideIsAndroidWear(@Named("device-sdk") int i) {
            return i >= 20 && this.context.getPackageManager().hasSystemFeature("android.hardware.type.watch");
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @ClientScope
        @Provides
        public static ScanSetupBuilder provideScanSetupProvider(@Named("device-sdk") int i, Provider<ScanSetupBuilderImplApi18> provider, Provider<ScanSetupBuilderImplApi21> provider2, Provider<ScanSetupBuilderImplApi23> provider3) {
            if (i < 21) {
                return provider.get();
            }
            if (i < 23) {
                return provider2.get();
            }
            return provider3.get();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Named(BluetoothConstants.ENABLE_NOTIFICATION_VALUE)
        @Provides
        public static byte[] provideEnableNotificationValue() {
            return BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Named(BluetoothConstants.ENABLE_INDICATION_VALUE)
        @Provides
        public static byte[] provideEnableIndicationValue() {
            return BluetoothGattDescriptor.ENABLE_INDICATION_VALUE;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Named(BluetoothConstants.DISABLE_NOTIFICATION_VALUE)
        @Provides
        public static byte[] provideDisableNotificationValue() {
            return BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Provides
        public static ScanPreconditionsVerifier provideScanPreconditionVerifier(@Named("device-sdk") int i, Provider<ScanPreconditionsVerifierApi18> provider, Provider<ScanPreconditionsVerifierApi24> provider2) {
            if (i < 24) {
                return provider.get();
            }
            return provider2.get();
        }
    }
}
