package com.bambuser.broadcaster;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;
import java.util.List;
import org.otwebrtc.MediaStreamTrack;

/* loaded from: classes.dex */
final class BluetoothCompat {
    private static final String LOGTAG = "BluetoothCompat";

    BluetoothCompat() {
    }

    static BluetoothAdapter getDefaultAdapter() {
        try {
            return BluetoothAdapter.getDefaultAdapter();
        } catch (Exception unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getAdapterState() {
        Integer num = (Integer) Compat.tryCall(getDefaultAdapter(), "getState");
        if (num != null) {
            return num.intValue();
        }
        return 10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static BluetoothDevice getFirstConnectedDevice(BluetoothProfile bluetoothProfile) {
        List list = (List) Compat.tryCall(bluetoothProfile, "getConnectedDevices");
        Object obj = (list == null || list.isEmpty()) ? null : list.get(0);
        if (obj instanceof BluetoothDevice) {
            return (BluetoothDevice) obj;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void getBtProfile(Context context, BluetoothProfile.ServiceListener serviceListener, int i) {
        BluetoothAdapter defaultAdapter = getDefaultAdapter();
        if (defaultAdapter == null || context == null || serviceListener == null) {
            return;
        }
        try {
            defaultAdapter.getProfileProxy(context, serviceListener, i);
        } catch (Exception e) {
            Log.v(LOGTAG, "Exception in getBtProfile: " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void closeBtProfile(int i, BluetoothProfile bluetoothProfile) {
        BluetoothAdapter defaultAdapter = getDefaultAdapter();
        if (defaultAdapter == null || bluetoothProfile == null) {
            return;
        }
        try {
            defaultAdapter.closeProfileProxy(i, bluetoothProfile);
        } catch (Exception e) {
            Log.v(LOGTAG, "Exception in closeBtProfile: " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isBtScoAvailable(Context context) {
        Boolean bool = (Boolean) Compat.tryCall(context.getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND), "isBluetoothScoAvailableOffCall");
        if (bool != null) {
            return bool.booleanValue();
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void startBtSco(Context context) {
        if (isBtScoAvailable(context)) {
            Compat.tryCall(context.getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND), "startBluetoothSco");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void stopBtSco(Context context) {
        if (isBtScoAvailable(context)) {
            Compat.tryCall(context.getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND), "stopBluetoothSco");
        }
    }
}
