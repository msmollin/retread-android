package com.treadly.Treadly.UI.TreadlyConnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.treadly.Treadly.R;
import com.treadly.client.lib.sdk.Model.DeviceInfo;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* loaded from: classes2.dex */
public class TreadlyDeviceConnectListAdapter extends ArrayAdapter<DeviceInfo> {
    private boolean allowSelecting;
    private Context context;
    private List<DeviceInfo> devices;
    private ConnectDeviceEventListener listener;

    /* loaded from: classes2.dex */
    public interface ConnectDeviceEventListener {
        void connectDeviceSelected(DeviceInfo deviceInfo);
    }

    public TreadlyDeviceConnectListAdapter(Context context, int i, List<DeviceInfo> list, ConnectDeviceEventListener connectDeviceEventListener) {
        super(context, i, list);
        this.allowSelecting = true;
        this.devices = list;
        this.context = context;
        this.listener = connectDeviceEventListener;
    }

    @Override // android.widget.ArrayAdapter, android.widget.BaseAdapter
    public void notifyDataSetChanged() {
        sortList();
        super.notifyDataSetChanged();
    }

    private void sortList() {
        Collections.sort(this.devices, new Comparator<DeviceInfo>() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyDeviceConnectListAdapter.1
            @Override // java.util.Comparator
            public int compare(DeviceInfo deviceInfo, DeviceInfo deviceInfo2) {
                return deviceInfo.getName().compareTo(deviceInfo2.getName());
            }
        });
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    @NonNull
    public View getView(int i, @Nullable View view, @NonNull ViewGroup viewGroup) {
        final DeviceInfo deviceInfo = this.devices.get(i);
        if (view == null) {
            view = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.list_view_device_connect_item, viewGroup, false);
            DeviceInfoViewHolder deviceInfoViewHolder = new DeviceInfoViewHolder();
            deviceInfoViewHolder.nameTextView = (TextView) view.findViewById(R.id.device_name_text_view);
            deviceInfoViewHolder.pairingConnectButton = (ImageView) view.findViewById(R.id.pairing_connect_button);
            view.setTag(deviceInfoViewHolder);
        }
        DeviceInfoViewHolder deviceInfoViewHolder2 = (DeviceInfoViewHolder) view.getTag();
        deviceInfoViewHolder2.nameTextView.setText(deviceInfo.getName());
        deviceInfoViewHolder2.pairingConnectButton.setEnabled(true);
        deviceInfoViewHolder2.pairingConnectButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyDeviceConnectListAdapter.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (!TreadlyDeviceConnectListAdapter.this.allowSelecting || TreadlyDeviceConnectListAdapter.this.listener == null) {
                    return;
                }
                TreadlyDeviceConnectListAdapter.this.listener.connectDeviceSelected(deviceInfo);
            }
        });
        return view;
    }

    public void addDevice(DeviceInfo deviceInfo) {
        if (deviceInfo.getName() == null || hasDevice(deviceInfo)) {
            return;
        }
        this.devices.add(deviceInfo);
        notifyDataSetChanged();
    }

    public void clearDevices() {
        this.devices.clear();
        notifyDataSetChanged();
    }

    private DeviceInfo getDevice(String str) {
        for (DeviceInfo deviceInfo : this.devices) {
            if (deviceInfo.getName() != null && deviceInfo.getName().equals(str)) {
                return deviceInfo;
            }
        }
        return null;
    }

    public boolean hasDevice(DeviceInfo deviceInfo) {
        if (deviceInfo.getName() == null) {
            return false;
        }
        for (DeviceInfo deviceInfo2 : this.devices) {
            if (deviceInfo2.getName() != null && deviceInfo2.getName().equals(deviceInfo.getName())) {
                return true;
            }
        }
        return false;
    }

    public void setAllowSelecting(boolean z) {
        this.allowSelecting = z;
    }

    /* loaded from: classes2.dex */
    private class DeviceInfoViewHolder {
        TextView nameTextView;
        ImageView pairingConnectButton;

        private DeviceInfoViewHolder() {
        }
    }
}
