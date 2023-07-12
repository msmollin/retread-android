package com.treadly.client.lib.sdk.Managers;

import android.util.SparseArray;
import com.github.mikephil.charting.utils.Utils;
import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.scan.ScanRecord;
import com.polidea.rxandroidble2.scan.ScanResult;
import com.treadly.client.lib.sdk.Bluetooth.BluetoothHandler;
import com.treadly.client.lib.sdk.Interfaces.ClientSystemQueue;
import com.treadly.client.lib.sdk.Listeners.BluetoothConnectionEventListener;
import com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener;
import com.treadly.client.lib.sdk.Model.BluetoothConnectionState;
import com.treadly.client.lib.sdk.Model.ConnectionStatusCode;
import com.treadly.client.lib.sdk.Model.DeviceConnectionEvent;
import com.treadly.client.lib.sdk.Model.DeviceConnectionStatus;
import com.treadly.client.lib.sdk.Model.DeviceInfo;
import com.treadly.client.lib.sdk.Model.ResponseMessage;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.BiConsumer;

/* loaded from: classes2.dex */
public class BleConnectionManager {
    private static final int CONNECTING_TIMEOUT = 30000;
    private static final String connectingTimeoutId = "CONNECTING_TIMEOUT";
    private static final String scanningTimeoutId = "SCANNING_TIMEOUT";
    public static BleConnectionManager shared = new BleConnectionManager();
    protected DeviceInfo connectedBlePeripheral;
    protected DeviceInfo connectingBlePeripheral;
    private Timer connectionTimer;
    private String connectionTimerContext;
    private boolean isConnectionTimerRunning;
    private boolean isReconnectionTimerRunning;
    private String reconnectName;
    private RxBleDevice reconnectPeripheral;
    private Timer reconnectionTimer;
    private final int MFR_ID_KEY = 89;
    protected ClientSystemQueue defaultQueue = new DefaultClientSystemQueue();
    private boolean managerEnabled = false;
    private HashMap<String, DeviceInfo> bluetoothPeripherals = new HashMap<>();
    private List<DeviceConnectionEventListener> currentDeviceConnectionListeners = new ArrayList();
    private List<BluetoothConnectionEventListener> currentBluetoothConnectionListener = new ArrayList();
    protected ConnectionStatusCode pendingDisconnectConnectionStatusCode = ConnectionStatusCode.noError;
    public DeviceConnectionStatus currentDeviceConnectionStatus = DeviceConnectionStatus.notConnected;

    private BleConnectionManager() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setManagerEnabled(boolean z) {
        this.managerEnabled = z;
    }

    public void addDeviceConnectionEventListener(final DeviceConnectionEventListener deviceConnectionEventListener) {
        this.defaultQueue.post(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.BleConnectionManager.1
            @Override // java.lang.Runnable
            public void run() {
                boolean z = false;
                for (Object obj : BleConnectionManager.this.currentDeviceConnectionListeners) {
                    if (obj == deviceConnectionEventListener) {
                        z = true;
                    }
                }
                if (z) {
                    return;
                }
                BleConnectionManager.this.currentDeviceConnectionListeners.add(deviceConnectionEventListener);
            }
        });
    }

    public void removeDeviceConnectionEventListener(final DeviceConnectionEventListener deviceConnectionEventListener) {
        this.defaultQueue.post(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.BleConnectionManager.2
            @Override // java.lang.Runnable
            public void run() {
                BleConnectionManager.this.currentDeviceConnectionListeners.remove(deviceConnectionEventListener);
            }
        });
    }

    public void sendOnDeviceConnectionChangedEvent(final DeviceConnectionEvent deviceConnectionEvent) {
        this.defaultQueue.post(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.BleConnectionManager.3
            @Override // java.lang.Runnable
            public void run() {
                ListIterator listIterator = BleConnectionManager.this.currentDeviceConnectionListeners.listIterator();
                while (listIterator.hasNext()) {
                    ((DeviceConnectionEventListener) listIterator.next()).onDeviceConnectionChanged(deviceConnectionEvent);
                }
            }
        });
    }

    public void sendOnDeviceConnectionDeviceDiscovered(final DeviceInfo deviceInfo) {
        this.defaultQueue.post(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.BleConnectionManager.4
            @Override // java.lang.Runnable
            public void run() {
                ListIterator listIterator = BleConnectionManager.this.currentDeviceConnectionListeners.listIterator();
                while (listIterator.hasNext()) {
                    ((DeviceConnectionEventListener) listIterator.next()).onDeviceConnectionDeviceDiscovered(deviceInfo);
                }
            }
        });
    }

    public void addBluetoothConnectionEventListener(BluetoothConnectionEventListener bluetoothConnectionEventListener) {
        ListIterator<BluetoothConnectionEventListener> listIterator = this.currentBluetoothConnectionListener.listIterator();
        boolean z = false;
        while (listIterator.hasNext()) {
            if (listIterator.next() == bluetoothConnectionEventListener) {
                z = true;
            }
        }
        if (z) {
            return;
        }
        this.currentBluetoothConnectionListener.add(bluetoothConnectionEventListener);
    }

    public void removeBluetoothConnectionEventListener(BluetoothConnectionEventListener bluetoothConnectionEventListener) {
        this.currentBluetoothConnectionListener.remove(bluetoothConnectionEventListener);
    }

    private void sendOnBluetoothConnectionStateChangedEvent(BluetoothConnectionState bluetoothConnectionState) {
        ListIterator<BluetoothConnectionEventListener> listIterator = this.currentBluetoothConnectionListener.listIterator();
        while (listIterator.hasNext()) {
            listIterator.next().onBluetoohConnectionStateChanged(bluetoothConnectionState);
        }
    }

    public BluetoothConnectionState getBluetoothConnectionState() {
        RxBleClient.State bluetoothState = BluetoothHandler.sharedInstance().getBluetoothState();
        if (bluetoothState == null) {
            return BluetoothConnectionState.unknown;
        }
        if (AnonymousClass8.$SwitchMap$com$polidea$rxandroidble2$RxBleClient$State[bluetoothState.ordinal()] == 1) {
            return BluetoothConnectionState.poweredOn;
        }
        return BluetoothConnectionState.poweredOff;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.client.lib.sdk.Managers.BleConnectionManager$8  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass8 {
        static final /* synthetic */ int[] $SwitchMap$com$polidea$rxandroidble2$RxBleClient$State = new int[RxBleClient.State.values().length];

        static {
            try {
                $SwitchMap$com$polidea$rxandroidble2$RxBleClient$State[RxBleClient.State.READY.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    public void serialDidDiscoverPeripheral(RxBleDevice rxBleDevice, ScanResult scanResult) {
        String parseName;
        DeviceInfo deviceInfo;
        if (!this.managerEnabled || rxBleDevice == null || rxBleDevice.getName() == null || (parseName = parseName(scanResult)) == null) {
            return;
        }
        if (this.bluetoothPeripherals.containsKey(getPeripheralKey(rxBleDevice))) {
            deviceInfo = this.bluetoothPeripherals.get(rxBleDevice.getName());
            deviceInfo.name = parseName;
            deviceInfo.priority = parsePriority(scanResult);
            deviceInfo.peripheral = rxBleDevice;
            deviceInfo.rssi = parseRssi(scanResult);
        } else {
            deviceInfo = new DeviceInfo(rxBleDevice.getName(), parsePriority(scanResult), parseRssi(scanResult));
            deviceInfo.peripheral = rxBleDevice;
            this.bluetoothPeripherals.put(rxBleDevice.getName(), deviceInfo);
        }
        this.bluetoothPeripherals.put(getPeripheralKey(rxBleDevice), deviceInfo);
        if (handleReconnect(deviceInfo, scanResult)) {
            return;
        }
        sendOnDeviceConnectionDeviceDiscovered(deviceInfo);
    }

    public void serialDidDisconnect(RxBleDevice rxBleDevice) {
        if (!DeviceManager.shared.isExpectingDisconnect()) {
            this.currentDeviceConnectionStatus = DeviceConnectionStatus.notConnected;
            sendOnDeviceConnectionChangedEvent(new DeviceConnectionEvent(DeviceConnectionStatus.notConnected, this.pendingDisconnectConnectionStatusCode, this.connectedBlePeripheral));
        }
        this.pendingDisconnectConnectionStatusCode = ConnectionStatusCode.noError;
        this.connectedBlePeripheral = null;
        this.connectingBlePeripheral = null;
    }

    public void serialDidReceiveBytes(byte[] bArr) {
        ResponseMessage parseMessage;
        if (this.managerEnabled && (parseMessage = Message.parseMessage(bArr)) != null) {
            DeviceManager.shared.handleProcessMessage(parseMessage);
        }
    }

    public void serialIsReady(RxBleDevice rxBleDevice) {
        if (rxBleDevice == null || rxBleDevice.getName() == null) {
            return;
        }
        this.reconnectPeripheral = null;
        this.reconnectName = null;
        PrintStream printStream = System.out;
        printStream.println("LGK :: Serial Is Ready " + rxBleDevice.getName());
        stopConnectingTimer();
        stopReconnectTimer();
        String peripheralKey = getPeripheralKey(rxBleDevice);
        if (peripheralKey == null) {
            return;
        }
        DeviceInfo deviceInfo = this.bluetoothPeripherals.get(peripheralKey);
        if (deviceInfo == null || deviceInfo.peripheral != rxBleDevice) {
            deviceInfo = new DeviceInfo(rxBleDevice.getName(), 0, Utils.DOUBLE_EPSILON);
        }
        this.connectedBlePeripheral = deviceInfo;
        this.connectingBlePeripheral = null;
        DeviceManager.shared.handleSerialIsReady();
    }

    private String parseName(ScanResult scanResult) {
        ScanRecord scanRecord;
        if (scanResult == null || (scanRecord = scanResult.getScanRecord()) == null) {
            return null;
        }
        return scanRecord.getDeviceName();
    }

    private int parseRssi(ScanResult scanResult) {
        if (scanResult == null) {
            return -1;
        }
        return scanResult.getRssi();
    }

    private int parsePriority(ScanResult scanResult) {
        ScanRecord scanRecord;
        SparseArray<byte[]> manufacturerSpecificData;
        byte[] bArr;
        if (scanResult == null || (scanRecord = scanResult.getScanRecord()) == null || (manufacturerSpecificData = scanRecord.getManufacturerSpecificData()) == null || (bArr = manufacturerSpecificData.get(89)) == null || bArr.length <= 0) {
            return -1;
        }
        return bArr[0];
    }

    private String getPeripheralKey(RxBleDevice rxBleDevice) {
        if (rxBleDevice == null) {
            return null;
        }
        return rxBleDevice.getName();
    }

    public void connectDevice(DeviceInfo deviceInfo) {
        if (deviceInfo.name != null && this.bluetoothPeripherals.containsKey(deviceInfo.name)) {
            stopScanning();
            BluetoothHandler.sharedInstance().connect(this.bluetoothPeripherals.get(deviceInfo.name).peripheral);
            DeviceManager.shared.reset();
        }
    }

    public void disconnect() {
        if (this.managerEnabled) {
            stopConnectingTimer();
            stopReconnectTimer();
            BluetoothHandler.sharedInstance().stopScanDevices();
            BluetoothHandler.sharedInstance().disconnect();
            DeviceManager.shared.reset();
        }
    }

    public void startScanning(long j) {
        startScanning(j, true);
    }

    public void startScanning(long j, boolean z) {
        if (this.managerEnabled) {
            if (!z && BluetoothHandler.sharedInstance().isScanningDevices()) {
                this.bluetoothPeripherals.forEach(new BiConsumer<String, DeviceInfo>() { // from class: com.treadly.client.lib.sdk.Managers.BleConnectionManager.5
                    @Override // java.util.function.BiConsumer
                    public void accept(String str, DeviceInfo deviceInfo) {
                        if (BleConnectionManager.this.handleReconnect(deviceInfo)) {
                            return;
                        }
                        BleConnectionManager.this.sendOnDeviceConnectionDeviceDiscovered(deviceInfo);
                    }
                });
            } else {
                stopScanning();
                this.bluetoothPeripherals.clear();
            }
            BluetoothHandler.sharedInstance().scanDevices();
            startConnectingTimer(j, scanningTimeoutId);
        }
    }

    public void stopScanning() {
        if (this.managerEnabled) {
            stopConnectingTimer();
            BluetoothHandler.sharedInstance().stopScanDevices();
        }
    }

    public DeviceConnectionStatus getConnectionStatus() {
        return BluetoothHandler.sharedInstance().isSerialReady() ? DeviceConnectionStatus.connected : DeviceConnectionStatus.notConnected;
    }

    public boolean isDeviceConnected() {
        return getConnectionStatus() == DeviceConnectionStatus.connected;
    }

    public boolean isDeviceConnecting() {
        return (this.isConnectionTimerRunning && this.connectionTimerContext.equals(connectingTimeoutId)) || this.isReconnectionTimerRunning;
    }

    private void startConnectingTimer(long j, String str) {
        if (this.managerEnabled) {
            stopConnectingTimer();
            this.connectionTimer = new Timer();
            this.connectionTimer.schedule(new TimerTask() { // from class: com.treadly.client.lib.sdk.Managers.BleConnectionManager.6
                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    BleConnectionManager.this.timeoutConnecting();
                }
            }, j);
            this.isConnectionTimerRunning = true;
            this.connectionTimerContext = str;
        }
    }

    private void stopConnectingTimer() {
        if (this.connectionTimer != null) {
            this.connectionTimer.cancel();
        }
        this.isConnectionTimerRunning = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void timeoutConnecting() {
        this.isConnectionTimerRunning = false;
        BluetoothHandler.sharedInstance().stopScanDevices();
        BluetoothHandler.sharedInstance().disconnect();
        sendOnDeviceConnectionChangedEvent(new DeviceConnectionEvent(DeviceConnectionStatus.notConnected, ConnectionStatusCode.noError, this.connectedBlePeripheral));
    }

    public void sendBytes(byte[] bArr) {
        if (bArr == null) {
            return;
        }
        BluetoothHandler.sharedInstance().sendMessage(bArr);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void clearReconnectDevice() {
        this.reconnectPeripheral = null;
        this.reconnectName = null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void initReconnect(DeviceInfo deviceInfo, int i) {
        this.reconnectPeripheral = deviceInfo.peripheral;
        this.reconnectName = deviceInfo.getName();
        startScanning(i);
    }

    private void reconnect(DeviceInfo deviceInfo) {
        stopScanning();
        RxBleDevice rxBleDevice = deviceInfo.peripheral;
        if (rxBleDevice == null) {
            return;
        }
        BluetoothHandler.sharedInstance().connect(rxBleDevice);
        startReconnectTimer(30000);
        DeviceManager.shared.reset();
        this.connectingBlePeripheral = deviceInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean handleReconnect(DeviceInfo deviceInfo) {
        if (this.reconnectPeripheral != null) {
            if (deviceInfo.name != null) {
                if (deviceInfo.name.equals(this.reconnectName) || hasEqualDeviceAddresses(deviceInfo.name, this.reconnectName)) {
                    reconnect(deviceInfo);
                    return true;
                }
                return true;
            }
            return true;
        }
        return false;
    }

    private boolean handleReconnect(DeviceInfo deviceInfo, ScanResult scanResult) {
        if (this.reconnectPeripheral != null) {
            String parseName = parseName(scanResult);
            if (parseName != null) {
                if (parseName.equals(this.reconnectName) || hasEqualDeviceAddresses(parseName, this.reconnectName)) {
                    reconnect(deviceInfo);
                    return true;
                }
                return true;
            }
            return true;
        }
        return false;
    }

    private boolean hasEqualDeviceAddresses(String str, String str2) {
        if (str == null || str.length() < 6 || str2 == null || str2.length() < 6) {
            return false;
        }
        return str.substring(str.length() - 6, str.length()).toLowerCase().equals(str2.substring(str2.length() - 6, str2.length()).toLowerCase());
    }

    private void startReconnectTimer(int i) {
        stopReconnectTimer();
        this.reconnectionTimer = new Timer();
        this.reconnectionTimer.schedule(new TimerTask() { // from class: com.treadly.client.lib.sdk.Managers.BleConnectionManager.7
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                BleConnectionManager.this.timeoutReconnecting();
            }
        }, i);
        this.isReconnectionTimerRunning = true;
    }

    private void stopReconnectTimer() {
        if (this.reconnectionTimer != null) {
            this.reconnectionTimer.cancel();
        }
        this.isReconnectionTimerRunning = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void timeoutReconnecting() {
        this.isReconnectionTimerRunning = false;
        BluetoothHandler.sharedInstance().disconnect();
        BluetoothHandler.sharedInstance().stopScanDevices();
        this.currentDeviceConnectionStatus = DeviceConnectionStatus.notConnected;
        sendOnDeviceConnectionChangedEvent(new DeviceConnectionEvent(DeviceConnectionStatus.notConnected, ConnectionStatusCode.noError, this.connectedBlePeripheral));
        DeviceManager.shared.handleSerialDidDisconnect();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isBluetoothPoweredOn() {
        return BluetoothHandler.sharedInstance().isBluetoothPoweredOn();
    }

    protected boolean isIgnoringStateChange() {
        return ActiveDeviceManager.sharedInstance.handleIgnoringStateChange();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void sendStateChangeEvents() {
        if (this.managerEnabled) {
            DeviceConnectionStatus deviceConnectionStatus = BluetoothHandler.sharedInstance().isSerialReady() ? DeviceConnectionStatus.connecting : DeviceConnectionStatus.disconnecting;
            DeviceConnectionEvent deviceConnectionEvent = new DeviceConnectionEvent(deviceConnectionStatus, ConnectionStatusCode.noError, this.connectedBlePeripheral);
            this.currentDeviceConnectionStatus = deviceConnectionStatus;
            sendOnDeviceConnectionChangedEvent(deviceConnectionEvent);
            sendOnBluetoothConnectionStateChangedEvent(getBluetoothConnectionState());
        }
    }
}
