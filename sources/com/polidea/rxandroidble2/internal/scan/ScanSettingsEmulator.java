package com.polidea.rxandroidble2.internal.scan;

import androidx.annotation.IntRange;
import bleshadow.javax.inject.Inject;
import bleshadow.javax.inject.Named;
import com.android.volley.DefaultRetryPolicy;
import com.polidea.rxandroidble2.internal.RxBleLog;
import com.polidea.rxandroidble2.internal.util.ObservableUtil;
import com.polidea.rxandroidble2.scan.ScanCallbackType;
import com.yalantis.ucrop.view.CropImageView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class ScanSettingsEmulator {
    private ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult> emulateFirstMatch;
    private final Scheduler scheduler;
    private ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult> emulateMatchLost = new ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult>() { // from class: com.polidea.rxandroidble2.internal.scan.ScanSettingsEmulator.5
        @Override // io.reactivex.ObservableTransformer
        /* renamed from: apply */
        public ObservableSource<RxBleInternalScanResult> apply2(Observable<RxBleInternalScanResult> observable) {
            return observable.debounce(10L, TimeUnit.SECONDS, ScanSettingsEmulator.this.scheduler).map(ScanSettingsEmulator.this.toMatchLost());
        }
    };
    private ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult> emulateFirstMatchAndMatchLost = new ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult>() { // from class: com.polidea.rxandroidble2.internal.scan.ScanSettingsEmulator.7
        @Override // io.reactivex.ObservableTransformer
        /* renamed from: apply */
        public ObservableSource<RxBleInternalScanResult> apply2(Observable<RxBleInternalScanResult> observable) {
            return observable.publish(new Function<Observable<RxBleInternalScanResult>, Observable<RxBleInternalScanResult>>() { // from class: com.polidea.rxandroidble2.internal.scan.ScanSettingsEmulator.7.1
                @Override // io.reactivex.functions.Function
                public Observable<RxBleInternalScanResult> apply(Observable<RxBleInternalScanResult> observable2) {
                    return Observable.merge(observable2.compose(ScanSettingsEmulator.this.emulateFirstMatch), observable2.compose(ScanSettingsEmulator.this.emulateMatchLost));
                }
            });
        }
    };

    @Inject
    public ScanSettingsEmulator(@Named("computation") final Scheduler scheduler) {
        this.scheduler = scheduler;
        this.emulateFirstMatch = new ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult>() { // from class: com.polidea.rxandroidble2.internal.scan.ScanSettingsEmulator.1
            private final Function<RxBleInternalScanResult, Observable<?>> emitAfterTimerFunc = new Function<RxBleInternalScanResult, Observable<?>>() { // from class: com.polidea.rxandroidble2.internal.scan.ScanSettingsEmulator.1.1
                @Override // io.reactivex.functions.Function
                public Observable<?> apply(RxBleInternalScanResult rxBleInternalScanResult) {
                    return AnonymousClass1.this.timerObservable;
                }
            };
            private final Function<Observable<RxBleInternalScanResult>, Observable<RxBleInternalScanResult>> takeFirstFromEachWindowFunc = new Function<Observable<RxBleInternalScanResult>, Observable<RxBleInternalScanResult>>() { // from class: com.polidea.rxandroidble2.internal.scan.ScanSettingsEmulator.1.2
                @Override // io.reactivex.functions.Function
                public Observable<RxBleInternalScanResult> apply(Observable<RxBleInternalScanResult> observable) {
                    return observable.take(1L);
                }
            };
            private final Observable<Long> timerObservable;
            private Function<RxBleInternalScanResult, RxBleInternalScanResult> toFirstMatchFunc;

            {
                this.toFirstMatchFunc = ScanSettingsEmulator.this.toFirstMatch();
                this.timerObservable = Observable.timer(10L, TimeUnit.SECONDS, scheduler);
            }

            @Override // io.reactivex.ObservableTransformer
            /* renamed from: apply */
            public ObservableSource<RxBleInternalScanResult> apply2(Observable<RxBleInternalScanResult> observable) {
                return observable.publish(new Function<Observable<RxBleInternalScanResult>, ObservableSource<RxBleInternalScanResult>>() { // from class: com.polidea.rxandroidble2.internal.scan.ScanSettingsEmulator.1.3
                    @Override // io.reactivex.functions.Function
                    public ObservableSource<RxBleInternalScanResult> apply(Observable<RxBleInternalScanResult> observable2) throws Exception {
                        return observable2.window(observable2.switchMap(AnonymousClass1.this.emitAfterTimerFunc)).flatMap(AnonymousClass1.this.takeFirstFromEachWindowFunc).map(AnonymousClass1.this.toFirstMatchFunc);
                    }
                });
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult> emulateScanMode(int i) {
        switch (i) {
            case -1:
                RxBleLog.d("Cannot emulate opportunistic scan mode since it is OS dependent - fallthrough to low power", new Object[0]);
                break;
            case 0:
                break;
            case 1:
                return scanModeBalancedTransformer();
            default:
                return ObservableUtil.identityTransformer();
        }
        return scanModeLowPowerTransformer();
    }

    private ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult> scanModeBalancedTransformer() {
        return repeatedWindowTransformer(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS);
    }

    private ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult> scanModeLowPowerTransformer() {
        return repeatedWindowTransformer(CropImageView.DEFAULT_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION);
    }

    private ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult> repeatedWindowTransformer(@IntRange(from = 0, to = 4999) final int i) {
        final long max = Math.max(TimeUnit.SECONDS.toMillis(5L) - i, 0L);
        return new ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult>() { // from class: com.polidea.rxandroidble2.internal.scan.ScanSettingsEmulator.2
            @Override // io.reactivex.ObservableTransformer
            /* renamed from: apply */
            public ObservableSource<RxBleInternalScanResult> apply2(Observable<RxBleInternalScanResult> observable) {
                return observable.take(i, TimeUnit.MILLISECONDS, ScanSettingsEmulator.this.scheduler).repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() { // from class: com.polidea.rxandroidble2.internal.scan.ScanSettingsEmulator.2.1
                    @Override // io.reactivex.functions.Function
                    public ObservableSource<?> apply(Observable<Object> observable2) throws Exception {
                        return observable2.delay(max, TimeUnit.MILLISECONDS, ScanSettingsEmulator.this.scheduler);
                    }
                });
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult> emulateCallbackType(int i) {
        if (i != 2) {
            if (i != 4) {
                if (i == 6) {
                    return splitByAddressAndForEach(this.emulateFirstMatchAndMatchLost);
                }
                return ObservableUtil.identityTransformer();
            }
            return splitByAddressAndForEach(this.emulateMatchLost);
        }
        return splitByAddressAndForEach(this.emulateFirstMatch);
    }

    private ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult> splitByAddressAndForEach(final ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult> observableTransformer) {
        return new ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult>() { // from class: com.polidea.rxandroidble2.internal.scan.ScanSettingsEmulator.3
            @Override // io.reactivex.ObservableTransformer
            /* renamed from: apply */
            public ObservableSource<RxBleInternalScanResult> apply2(Observable<RxBleInternalScanResult> observable) {
                return observable.groupBy(new Function<RxBleInternalScanResult, String>() { // from class: com.polidea.rxandroidble2.internal.scan.ScanSettingsEmulator.3.2
                    @Override // io.reactivex.functions.Function
                    public String apply(RxBleInternalScanResult rxBleInternalScanResult) {
                        return rxBleInternalScanResult.getBluetoothDevice().getAddress();
                    }
                }).flatMap(new Function<GroupedObservable<String, RxBleInternalScanResult>, Observable<RxBleInternalScanResult>>() { // from class: com.polidea.rxandroidble2.internal.scan.ScanSettingsEmulator.3.1
                    @Override // io.reactivex.functions.Function
                    public Observable<RxBleInternalScanResult> apply(GroupedObservable<String, RxBleInternalScanResult> groupedObservable) {
                        return groupedObservable.compose(observableTransformer);
                    }
                });
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Function<RxBleInternalScanResult, RxBleInternalScanResult> toFirstMatch() {
        return new Function<RxBleInternalScanResult, RxBleInternalScanResult>() { // from class: com.polidea.rxandroidble2.internal.scan.ScanSettingsEmulator.4
            @Override // io.reactivex.functions.Function
            public RxBleInternalScanResult apply(RxBleInternalScanResult rxBleInternalScanResult) {
                return new RxBleInternalScanResult(rxBleInternalScanResult.getBluetoothDevice(), rxBleInternalScanResult.getRssi(), rxBleInternalScanResult.getTimestampNanos(), rxBleInternalScanResult.getScanRecord(), ScanCallbackType.CALLBACK_TYPE_FIRST_MATCH);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Function<RxBleInternalScanResult, RxBleInternalScanResult> toMatchLost() {
        return new Function<RxBleInternalScanResult, RxBleInternalScanResult>() { // from class: com.polidea.rxandroidble2.internal.scan.ScanSettingsEmulator.6
            @Override // io.reactivex.functions.Function
            public RxBleInternalScanResult apply(RxBleInternalScanResult rxBleInternalScanResult) {
                return new RxBleInternalScanResult(rxBleInternalScanResult.getBluetoothDevice(), rxBleInternalScanResult.getRssi(), rxBleInternalScanResult.getTimestampNanos(), rxBleInternalScanResult.getScanRecord(), ScanCallbackType.CALLBACK_TYPE_MATCH_LOST);
            }
        };
    }
}
