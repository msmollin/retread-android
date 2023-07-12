package com.polidea.rxandroidble2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import androidx.annotation.NonNull;
import bleshadow.javax.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;

/* loaded from: classes.dex */
public class RxBleAdapterStateObservable extends Observable<BleAdapterState> {
    private static final String TAG = "AdapterStateObs";
    @NonNull
    private final Context context;

    /* loaded from: classes.dex */
    public static class BleAdapterState {
        private final boolean isUsable;
        public static final BleAdapterState STATE_ON = new BleAdapterState(true);
        public static final BleAdapterState STATE_OFF = new BleAdapterState(false);
        public static final BleAdapterState STATE_TURNING_ON = new BleAdapterState(false);
        public static final BleAdapterState STATE_TURNING_OFF = new BleAdapterState(false);

        private BleAdapterState(boolean z) {
            this.isUsable = z;
        }

        public boolean isUsable() {
            return this.isUsable;
        }
    }

    @Inject
    public RxBleAdapterStateObservable(@NonNull Context context) {
        this.context = context;
    }

    @Override // io.reactivex.Observable
    protected void subscribeActual(final Observer<? super BleAdapterState> observer) {
        final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.polidea.rxandroidble2.RxBleAdapterStateObservable.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(intent.getAction())) {
                    observer.onNext(RxBleAdapterStateObservable.mapToBleAdapterState(intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1)));
                }
            }
        };
        observer.onSubscribe(Disposables.fromAction(new Action() { // from class: com.polidea.rxandroidble2.RxBleAdapterStateObservable.2
            @Override // io.reactivex.functions.Action
            public void run() throws Exception {
                try {
                    RxBleAdapterStateObservable.this.context.unregisterReceiver(broadcastReceiver);
                } catch (IllegalArgumentException unused) {
                    Log.d(RxBleAdapterStateObservable.TAG, "The receiver is already not registered.");
                }
            }
        }));
        this.context.registerReceiver(broadcastReceiver, createFilter());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static BleAdapterState mapToBleAdapterState(int i) {
        switch (i) {
            case 11:
                return BleAdapterState.STATE_TURNING_ON;
            case 12:
                return BleAdapterState.STATE_ON;
            case 13:
                return BleAdapterState.STATE_TURNING_OFF;
            default:
                return BleAdapterState.STATE_OFF;
        }
    }

    private static IntentFilter createFilter() {
        return new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED");
    }
}
