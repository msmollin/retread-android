package com.treadly.client.lib.sdk.Bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.ParcelUuid;
import android.util.Log;
import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.exceptions.BleException;
import com.polidea.rxandroidble2.exceptions.BleScanException;
import com.polidea.rxandroidble2.scan.ScanFilter;
import com.polidea.rxandroidble2.scan.ScanResult;
import com.polidea.rxandroidble2.scan.ScanSettings;
import com.treadly.client.lib.sdk.Managers.BleConnectionManager;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.subjects.PublishSubject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/* loaded from: classes2.dex */
public class BluetoothHandler {
    public static final int BLE_DEVICE_VERSION_1 = 1;
    public static final int BLE_DEVICE_VERSION_2 = 2;
    public static final int BLE_DEVICE_VERSION_UNKNOWN = 0;
    public static final String TAG = "BluetoothHandler";
    private Disposable connectionDisposable;
    private Observable<RxBleConnection> connectionObservable;
    private RxBleConnection mRxBleConnection;
    private Disposable readDisposable;
    private RxBleClient rxBleClient;
    private Disposable scanDisposable;
    private Disposable writeDisposable;
    private static final UUID RX_SERVICE_UUID_BASE = UUID.fromString("6E400000-B5A3-F393-E0A9-E50E24DCCA9E");
    private static final UUID RX_SERVICE_UUID_MASK = UUID.fromString("11110000-1111-1111-1111-111111111111");
    private static final UUID RX_CHAR_UUID = UUID.fromString("6E400002-B5A3-F393-E0A9-E50E24DCCA9E");
    private static final UUID TX_CHAR_UUID = UUID.fromString("6E400003-B5A3-F393-E0A9-E50E24DCCA9E");
    private static BluetoothHandler instance = new BluetoothHandler();
    private List<RxBleDevice> bluetoothDevices = new ArrayList();
    private Map<String, Integer> bluetoothDevicesVersion = new HashMap();
    private RxBleDevice bleDevice = null;
    private PublishSubject<Boolean> disconnectTriggerSubject = PublishSubject.create();

    public static BluetoothHandler sharedInstance() {
        return instance;
    }

    public void init(Activity activity) {
        this.rxBleClient = RxBleClient.create(activity);
        RxBleClient.setLogLevel(2);
        registerBluetoothIntents(activity.getApplicationContext());
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() { // from class: com.treadly.client.lib.sdk.Bluetooth.BluetoothHandler.1
            @Override // io.reactivex.functions.Consumer
            public void accept(Throwable th) throws Exception {
                if ((th instanceof UndeliverableException) && (th.getCause() instanceof BleException)) {
                    th.printStackTrace();
                    return;
                }
                throw new Exception(th);
            }
        });
    }

    private void registerBluetoothIntents(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.bluetooth.device.action.BOND_STATE_CHANGED");
        intentFilter.addAction("android.bluetooth.device.action.PAIRING_REQUEST");
        context.registerReceiver(new BroadcastReceiver() { // from class: com.treadly.client.lib.sdk.Bluetooth.BluetoothHandler.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                if (BluetoothHandler.this.bleDevice == null || BluetoothHandler.this.bleDevice.getName() == null || !BluetoothHandler.this.bleDevice.getName().equals(bluetoothDevice.getName())) {
                    return;
                }
                char c = 65535;
                int hashCode = action.hashCode();
                if (hashCode != -223687943) {
                    if (hashCode == 2116862345 && action.equals("android.bluetooth.device.action.BOND_STATE_CHANGED")) {
                        c = 0;
                    }
                } else if (action.equals("android.bluetooth.device.action.PAIRING_REQUEST")) {
                    c = 1;
                }
                switch (c) {
                    case 0:
                        intent.getIntExtra("android.bluetooth.device.extra.PREVIOUS_BOND_STATE", 0);
                        switch (bluetoothDevice.getBondState()) {
                            case 10:
                                BluetoothHandler.this.disconnect();
                                return;
                            case 11:
                            case 12:
                            default:
                                return;
                        }
                    case 1:
                        intent.getIntExtra("android.bluetooth.device.extra.PAIRING_VARIANT", Integer.MIN_VALUE);
                        return;
                    default:
                        return;
                }
            }
        }, intentFilter);
    }

    public void scanDevices() {
        if (this.scanDisposable != null) {
            return;
        }
        this.bluetoothDevices.clear();
        this.bluetoothDevicesVersion.clear();
        this.scanDisposable = this.rxBleClient.scanBleDevices(new ScanSettings.Builder().setScanMode(2).setCallbackType(1).build(), new ScanFilter.Builder().setServiceUuid(new ParcelUuid(RX_SERVICE_UUID_BASE), new ParcelUuid(RX_SERVICE_UUID_MASK)).build()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<ScanResult>() { // from class: com.treadly.client.lib.sdk.Bluetooth.BluetoothHandler.3
            @Override // io.reactivex.functions.Consumer
            public void accept(ScanResult scanResult) throws Exception {
                scanResult.getScanRecord().getServiceUuids();
                BluetoothHandler.this.addBluetoothDevice(scanResult.getBleDevice(), scanResult);
            }
        }, new Consumer<Throwable>() { // from class: com.treadly.client.lib.sdk.Bluetooth.BluetoothHandler.4
            @Override // io.reactivex.functions.Consumer
            public void accept(Throwable th) throws Exception {
                int reason;
                if ((th instanceof BleScanException) && (reason = ((BleScanException) th).getReason()) >= 0 && reason <= 4) {
                    BluetoothHandler.this.scanDisposable = null;
                }
                Log.d(BluetoothHandler.TAG, "Error scanning for devices, Throwable: " + th);
            }
        });
    }

    public void stopScanDevices() {
        if (this.scanDisposable != null) {
            this.scanDisposable.dispose();
            this.scanDisposable = null;
        }
    }

    public boolean isScanningDevices() {
        return this.scanDisposable != null;
    }

    public void sendMessage(byte[] bArr) {
        if (this.mRxBleConnection != null) {
            this.writeDisposable = this.mRxBleConnection.writeCharacteristic(RX_CHAR_UUID, bArr).subscribe(new Consumer<Object>() { // from class: com.treadly.client.lib.sdk.Bluetooth.BluetoothHandler.5
                @Override // io.reactivex.functions.Consumer
                public void accept(Object obj) throws Exception {
                }
            }, new Consumer<Throwable>() { // from class: com.treadly.client.lib.sdk.Bluetooth.BluetoothHandler.6
                @Override // io.reactivex.functions.Consumer
                public void accept(Throwable th) throws Exception {
                    Log.d(BluetoothHandler.TAG, "error writing to BLE device, Throwable: " + th);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addBluetoothDevice(RxBleDevice rxBleDevice, ScanResult scanResult) {
        if (this.bluetoothDevices == null || rxBleDevice == null || rxBleDevice.getName() == null) {
            return;
        }
        this.bluetoothDevices.add(rxBleDevice);
        BleConnectionManager.shared.serialDidDiscoverPeripheral(rxBleDevice, scanResult);
    }

    public void connect(final RxBleDevice rxBleDevice) {
        if (this.connectionDisposable == null) {
            this.bleDevice = rxBleDevice;
            this.connectionObservable = prepareConnectionObservable(this.bleDevice);
            this.connectionDisposable = this.connectionObservable.takeUntil(this.disconnectTriggerSubject).subscribe(new Consumer<RxBleConnection>() { // from class: com.treadly.client.lib.sdk.Bluetooth.BluetoothHandler.7
                @Override // io.reactivex.functions.Consumer
                public void accept(RxBleConnection rxBleConnection) throws Exception {
                    Log.d(BluetoothHandler.TAG, "Connection successful: " + rxBleDevice.getName());
                    BluetoothHandler.this.connectionSuccessful(rxBleConnection);
                }
            }, new Consumer<Throwable>() { // from class: com.treadly.client.lib.sdk.Bluetooth.BluetoothHandler.8
                @Override // io.reactivex.functions.Consumer
                public void accept(Throwable th) throws Exception {
                    Log.d(BluetoothHandler.TAG, "Connection error: " + th);
                    BluetoothHandler.this.handleDisconnect(BluetoothHandler.this.bleDevice);
                }
            }, new Action() { // from class: com.treadly.client.lib.sdk.Bluetooth.BluetoothHandler.9
                @Override // io.reactivex.functions.Action
                public void run() throws Exception {
                    Log.d(BluetoothHandler.TAG, "Connection done");
                    BluetoothHandler.this.handleDisconnect(BluetoothHandler.this.bleDevice);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void connectionSuccessful(RxBleConnection rxBleConnection) {
        this.mRxBleConnection = rxBleConnection;
        this.readDisposable = prepareReadDisposable();
        BleConnectionManager.shared.serialIsReady(this.bleDevice);
    }

    private Observable<RxBleConnection> prepareConnectionObservable(RxBleDevice rxBleDevice) {
        return rxBleDevice.establishConnection(false).takeUntil(this.disconnectTriggerSubject);
    }

    private Disposable prepareReadDisposable() {
        return this.mRxBleConnection.setupNotification(TX_CHAR_UUID).flatMap(new Function<Observable<byte[]>, Observable<byte[]>>() { // from class: com.treadly.client.lib.sdk.Bluetooth.BluetoothHandler.12
            @Override // io.reactivex.functions.Function
            public Observable<byte[]> apply(Observable<byte[]> observable) throws Exception {
                return observable;
            }
        }).takeUntil(this.disconnectTriggerSubject).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<byte[]>() { // from class: com.treadly.client.lib.sdk.Bluetooth.BluetoothHandler.10
            @Override // io.reactivex.functions.Consumer
            public void accept(byte[] bArr) throws Exception {
                BleConnectionManager.shared.serialDidReceiveBytes(bArr);
            }
        }, new Consumer<Throwable>() { // from class: com.treadly.client.lib.sdk.Bluetooth.BluetoothHandler.11
            @Override // io.reactivex.functions.Consumer
            public void accept(Throwable th) throws Exception {
                Log.d(BluetoothHandler.TAG, "Error reading bytes: Throwable: " + th);
            }
        });
    }

    public void disconnect() {
        Log.d(TAG, "Disconnect called");
        this.disconnectTriggerSubject.onNext(true);
    }

    public void handleDisconnect(RxBleDevice rxBleDevice) {
        System.out.println("LGK :: Handle disconnect");
        if (this.connectionDisposable != null) {
            this.connectionDisposable.dispose();
            this.connectionDisposable = null;
        }
        if (this.connectionObservable != null) {
            this.connectionObservable = null;
        }
        if (this.bleDevice != null) {
            this.bleDevice = null;
        }
        if (this.readDisposable != null) {
            this.readDisposable.dispose();
            this.readDisposable = null;
        }
        if (this.writeDisposable != null) {
            this.writeDisposable.dispose();
            this.writeDisposable = null;
        }
        BleConnectionManager.shared.serialDidDisconnect(rxBleDevice);
    }

    public boolean isSerialReady() {
        return this.readDisposable != null;
    }

    public boolean isBluetoothPoweredOn() {
        return (this.rxBleClient == null || this.rxBleClient.getState() == null || !this.rxBleClient.getState().equals(RxBleClient.State.READY)) ? false : true;
    }

    public RxBleClient.State getBluetoothState() {
        if (this.rxBleClient != null) {
            return this.rxBleClient.getState();
        }
        return null;
    }
}
