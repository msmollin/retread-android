package com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.RecyclerView;
import com.treadly.Treadly.R;
import com.treadly.client.lib.sdk.Model.DeviceInfo;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/* loaded from: classes2.dex */
public class TreadlyDeviceConnectStatusListAdapter extends RecyclerView.Adapter<ViewHolder> {
    private final Context context;
    private final List<DeviceInfo> devices;
    private final ConnectDeviceEventListener listener;
    public DeviceInfo connectedDeviceInfo = null;
    public BluetoothDevice connectAudioDevice = null;
    public boolean isReconnecting = false;
    private boolean allowSelecting = true;
    public List<BluetoothDevice> audioDeviceList = new ArrayList();

    /* loaded from: classes2.dex */
    public interface ConnectDeviceEventListener {
        void audioHelpPressed(String str);

        void connectAudioSelected(String str);

        void connectDeviceSelected(DeviceInfo deviceInfo);

        void deviceHelpPressed(DeviceInfo deviceInfo);

        void disconnectAudioSelected(String str);

        void disconnectDeviceSelected(DeviceInfo deviceInfo);
    }

    public TreadlyDeviceConnectStatusListAdapter(Context context, List<DeviceInfo> list, ConnectDeviceEventListener connectDeviceEventListener) {
        this.devices = list;
        this.context = context;
        this.listener = connectDeviceEventListener;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_view_device_connect_status_item, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final DeviceInfo deviceInfo = this.devices.get(i);
        viewHolder.deviceNameTextView.setText(deviceInfo.getName());
        if (this.connectedDeviceInfo != null && deviceInfo.getName().equals(this.connectedDeviceInfo.getName())) {
            PrintStream printStream = System.out;
            printStream.println("NRS :: connectedDeviceInfo is not null, device=" + deviceInfo.getName() + " connected=" + this.connectedDeviceInfo.getName());
            viewHolder.deviceConnectStatusTextView.setText(R.string.connected);
            viewHolder.deviceConnectStatusTextView.setTextColor(this.context.getResources().getColor(R.color.green_1, null));
            viewHolder.deviceConnectButton.setBackgroundTintList(this.context.getColorStateList(R.color.red_1));
            viewHolder.deviceConnectButton.setText(R.string.disconnect);
            viewHolder.deviceConnectButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusListAdapter$qnUvu2rAoRBTLAiDv61tiqi2vL0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    TreadlyDeviceConnectStatusListAdapter.lambda$onBindViewHolder$0(TreadlyDeviceConnectStatusListAdapter.this, deviceInfo, view);
                }
            });
        } else {
            viewHolder.deviceConnectStatusTextView.setText(R.string.disconnected);
            viewHolder.deviceConnectStatusTextView.setTextColor(this.context.getResources().getColor(R.color.red_1, null));
            viewHolder.deviceConnectButton.setBackgroundTintList(this.context.getColorStateList(R.color.green_1));
            viewHolder.deviceConnectButton.setText(R.string.connect);
            viewHolder.deviceConnectButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusListAdapter$z4br6JCPicj0gSfTHknfT6-vao8
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    TreadlyDeviceConnectStatusListAdapter.lambda$onBindViewHolder$1(TreadlyDeviceConnectStatusListAdapter.this, deviceInfo, view);
                }
            });
        }
        viewHolder.deviceConnectButton.setEnabled(!this.isReconnecting);
        viewHolder.deviceConnectButton.setAlpha(this.isReconnecting ? 0.3f : 1.0f);
        viewHolder.deviceHelpButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusListAdapter$Gk3YAeN206T5eKBMBdcGv76HM2Y
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TreadlyDeviceConnectStatusListAdapter.this.listener.deviceHelpPressed(deviceInfo);
            }
        });
        if (deviceInfo.getGen() == DeviceInfo.DeviceGen.gen1) {
            viewHolder.speakerView.setVisibility(8);
            viewHolder.speakerImage.setVisibility(8);
            return;
        }
        viewHolder.speakerView.setVisibility(0);
        viewHolder.speakerImage.setVisibility(0);
        final AtomicReference atomicReference = new AtomicReference();
        Arrays.asList(deviceInfo.getName().split("-")).forEach(new Consumer() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusListAdapter$VjpjM9MwqzxsefyIrYxHHmw-gL8
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                TreadlyDeviceConnectStatusListAdapter.lambda$onBindViewHolder$3(atomicReference, (String) obj);
            }
        });
        viewHolder.audioNameTextView.setText((CharSequence) atomicReference.get());
        if (this.connectAudioDevice != null && this.connectAudioDevice.getName().equals(atomicReference.get())) {
            viewHolder.audioConnectStatusTextView.setText(R.string.connected);
            viewHolder.audioConnectStatusTextView.setTextColor(this.context.getColorStateList(R.color.green_1));
            viewHolder.audioConnectButton.setText(R.string.disconnect);
            viewHolder.audioConnectButton.setBackgroundTintList(this.context.getColorStateList(R.color.red_1));
            viewHolder.audioConnectButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusListAdapter$CBP3mmKbVGMUX9rTXfyL3NuaKXw
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    TreadlyDeviceConnectStatusListAdapter.lambda$onBindViewHolder$4(TreadlyDeviceConnectStatusListAdapter.this, atomicReference, view);
                }
            });
        } else {
            viewHolder.audioConnectStatusTextView.setText(R.string.disconnected);
            viewHolder.audioConnectStatusTextView.setTextColor(this.context.getColorStateList(R.color.red_1));
            viewHolder.audioConnectButton.setText(R.string.connect);
            viewHolder.audioConnectButton.setBackgroundTintList(this.context.getColorStateList(R.color.green_1));
            viewHolder.audioConnectButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusListAdapter$lUywBGRF-Yo5OS_iNyI07iC6kqQ
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    TreadlyDeviceConnectStatusListAdapter.lambda$onBindViewHolder$5(TreadlyDeviceConnectStatusListAdapter.this, atomicReference, view);
                }
            });
        }
        viewHolder.audioConnectButton.setEnabled(!this.isReconnecting);
        viewHolder.audioConnectButton.setAlpha(this.isReconnecting ? 0.3f : 1.0f);
        viewHolder.audioHelpButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusListAdapter$zDKBw8gdw01uqtLp3S1egRMOHwU
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TreadlyDeviceConnectStatusListAdapter.this.listener.audioHelpPressed((String) atomicReference.get());
            }
        });
    }

    public static /* synthetic */ void lambda$onBindViewHolder$0(TreadlyDeviceConnectStatusListAdapter treadlyDeviceConnectStatusListAdapter, DeviceInfo deviceInfo, View view) {
        if (!treadlyDeviceConnectStatusListAdapter.allowSelecting || treadlyDeviceConnectStatusListAdapter.listener == null) {
            return;
        }
        treadlyDeviceConnectStatusListAdapter.listener.disconnectDeviceSelected(deviceInfo);
    }

    public static /* synthetic */ void lambda$onBindViewHolder$1(TreadlyDeviceConnectStatusListAdapter treadlyDeviceConnectStatusListAdapter, DeviceInfo deviceInfo, View view) {
        if (!treadlyDeviceConnectStatusListAdapter.allowSelecting || treadlyDeviceConnectStatusListAdapter.listener == null) {
            return;
        }
        treadlyDeviceConnectStatusListAdapter.listener.connectDeviceSelected(deviceInfo);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$onBindViewHolder$3(AtomicReference atomicReference, String str) {
        if (str.equals("Treadly2") || str.equals(ExifInterface.GPS_DIRECTION_TRUE)) {
            return;
        }
        atomicReference.set("Treadly2-A-" + str);
    }

    public static /* synthetic */ void lambda$onBindViewHolder$4(TreadlyDeviceConnectStatusListAdapter treadlyDeviceConnectStatusListAdapter, AtomicReference atomicReference, View view) {
        if (!treadlyDeviceConnectStatusListAdapter.allowSelecting || treadlyDeviceConnectStatusListAdapter.listener == null) {
            return;
        }
        treadlyDeviceConnectStatusListAdapter.listener.disconnectAudioSelected((String) atomicReference.get());
    }

    public static /* synthetic */ void lambda$onBindViewHolder$5(TreadlyDeviceConnectStatusListAdapter treadlyDeviceConnectStatusListAdapter, AtomicReference atomicReference, View view) {
        if (!treadlyDeviceConnectStatusListAdapter.allowSelecting || treadlyDeviceConnectStatusListAdapter.listener == null) {
            return;
        }
        treadlyDeviceConnectStatusListAdapter.listener.connectAudioSelected((String) atomicReference.get());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.devices.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$sortList$7(DeviceInfo deviceInfo, DeviceInfo deviceInfo2) {
        return (int) (deviceInfo2.rssi - deviceInfo.rssi);
    }

    private void sortList() {
        this.devices.sort(new Comparator() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusListAdapter$RSilt1ItC4SFrg0WlUfBatgoAx8
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return TreadlyDeviceConnectStatusListAdapter.lambda$sortList$7((DeviceInfo) obj, (DeviceInfo) obj2);
            }
        });
    }

    public boolean isTreadlyAudioDevice(String str) {
        String[] split = str.split("-");
        return split.length == 3 && (split[0].equals("Treadly") || split[0].equals("Treadly2")) && split[1].equals(ExifInterface.GPS_MEASUREMENT_IN_PROGRESS);
    }

    public boolean addDevice(DeviceInfo deviceInfo) {
        if (deviceInfo.getName() == null || hasDevice(deviceInfo)) {
            return false;
        }
        this.devices.add(deviceInfo);
        sortList();
        notifyDataSetChanged();
        return true;
    }

    public void clearDevices() {
        clearDevices(false);
    }

    public void clearDevices(boolean z) {
        this.devices.clear();
        if (z) {
            return;
        }
        notifyDataSetChanged();
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

    /* loaded from: classes2.dex */
    public class ViewHolder extends RecyclerView.ViewHolder {
        Button audioConnectButton;
        TextView audioConnectStatusTextView;
        ImageButton audioHelpButton;
        TextView audioNameTextView;
        Button deviceConnectButton;
        TextView deviceConnectStatusTextView;
        ImageButton deviceHelpButton;
        TextView deviceNameTextView;
        ImageView speakerImage;
        ConstraintLayout speakerView;
        ImageView treadmillImage;

        ViewHolder(View view) {
            super(view);
            this.deviceNameTextView = (TextView) view.findViewById(R.id.device_connect_status_item_device_name);
            this.audioNameTextView = (TextView) view.findViewById(R.id.device_connect_status_item_speaker_name);
            this.audioConnectButton = (Button) view.findViewById(R.id.device_connect_status_item_speaker_connect_button);
            this.audioConnectStatusTextView = (TextView) view.findViewById(R.id.device_connect_status_item_speaker_status);
            this.deviceConnectButton = (Button) view.findViewById(R.id.device_connect_status_item_speed_control_connect_button);
            this.deviceConnectStatusTextView = (TextView) view.findViewById(R.id.device_connect_status_item_device_status);
            this.deviceHelpButton = (ImageButton) view.findViewById(R.id.device_connect_status_item_speed_help_button);
            this.audioHelpButton = (ImageButton) view.findViewById(R.id.device_connect_status_item_speaker_help_button);
            this.speakerView = (ConstraintLayout) view.findViewById(R.id.device_connect_status_item_speaker_view);
            this.speakerImage = (ImageView) view.findViewById(R.id.device_connect_status_item_speaker_image);
            this.treadmillImage = (ImageView) view.findViewById(R.id.device_connect_status_item_treadmill_image);
        }
    }
}
