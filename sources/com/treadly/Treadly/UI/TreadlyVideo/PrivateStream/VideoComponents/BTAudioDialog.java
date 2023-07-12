package com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaRouter;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.BTAudioDialogAdapter;
import java.util.ArrayList;
import java.util.List;
import org.otwebrtc.MediaStreamTrack;

/* loaded from: classes2.dex */
public class BTAudioDialog {
    public static final String TAG = "BTAudioDialog";
    BluetoothA2dp audioDevice;
    AudioManager audioManager;
    PopupWindow blurBackgroundPopup;
    View blurView;
    Context context;
    RecyclerView deviceRecycler;
    List<MediaRouter.RouteInfo> devices;
    BTAudioDialogAdapter dialogAdapter;
    MediaRouter mediaRouter;
    TextView popupTitle;
    View popupView;
    PopupWindow popupWindow;
    int volume;
    SeekBar volumeBar;
    private final MediaRouter.Callback mediaRouterCallback = new MediaRouter.Callback() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.BTAudioDialog.2
        @Override // android.media.MediaRouter.Callback
        public void onRouteSelected(MediaRouter mediaRouter, int i, MediaRouter.RouteInfo routeInfo) {
            Log.d(BTAudioDialog.TAG, "onRouteSelected: route=" + routeInfo);
            BTAudioDialog.this.updateRouteDeviceList();
        }

        @Override // android.media.MediaRouter.Callback
        public void onRouteUnselected(MediaRouter mediaRouter, int i, MediaRouter.RouteInfo routeInfo) {
            Log.d(BTAudioDialog.TAG, "onRouteUnselected: route=" + routeInfo);
            BTAudioDialog.this.updateRouteDeviceList();
        }

        @Override // android.media.MediaRouter.Callback
        public void onRouteAdded(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
            Log.d(BTAudioDialog.TAG, "onRouteAdded: route=" + routeInfo);
            BTAudioDialog.this.updateRouteDeviceList();
        }

        @Override // android.media.MediaRouter.Callback
        public void onRouteRemoved(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
            Log.d(BTAudioDialog.TAG, "onRouteRemoved: route=" + routeInfo);
            BTAudioDialog.this.updateRouteDeviceList();
        }

        @Override // android.media.MediaRouter.Callback
        public void onRouteChanged(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
            Log.d(BTAudioDialog.TAG, "onRouteChanged: route=" + routeInfo);
        }

        @Override // android.media.MediaRouter.Callback
        public void onRouteGrouped(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo, MediaRouter.RouteGroup routeGroup, int i) {
            Log.d(BTAudioDialog.TAG, "onRouteGrouped: route=" + routeInfo);
        }

        @Override // android.media.MediaRouter.Callback
        public void onRouteUngrouped(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo, MediaRouter.RouteGroup routeGroup) {
            Log.d(BTAudioDialog.TAG, "onRouteUngrouped: route=" + routeInfo);
        }

        @Override // android.media.MediaRouter.Callback
        public void onRouteVolumeChanged(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
            Log.d(BTAudioDialog.TAG, "onRouteVolumeChanged: route=" + routeInfo);
        }
    };
    BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

    public BTAudioDialog(@NonNull Context context) {
        this.context = context;
        if (this.btAdapter == null) {
            return;
        }
        this.audioManager = (AudioManager) context.getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
        this.volume = this.audioManager.getStreamVolume(3);
        this.audioManager.getDevices(2);
        this.devices = new ArrayList();
        this.dialogAdapter = new BTAudioDialogAdapter(context, this.devices);
        this.dialogAdapter.setClickListener(new BTAudioDialogAdapter.ItemClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.-$$Lambda$BTAudioDialog$JrnDrWFn2x2IPQlaKev7fO53DV4
            @Override // com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.BTAudioDialogAdapter.ItemClickListener
            public final void onItemClick(View view, int i) {
                BTAudioDialog.this.handleItemClick(view, i);
            }
        });
        this.mediaRouter = (MediaRouter) context.getSystemService("media_router");
        updateRouteDeviceList();
    }

    public void show() {
        initializeWindow();
        showPopup();
    }

    private void initializeWindow() {
        this.mediaRouter.addCallback(1, this.mediaRouterCallback, 1);
        updateRouteDeviceList();
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService("layout_inflater");
        this.popupView = layoutInflater.inflate(R.layout.video_bt_dialog, (ViewGroup) null);
        this.popupTitle = (TextView) this.popupView.findViewById(R.id.video_bt_dialog_title);
        this.volumeBar = (SeekBar) this.popupView.findViewById(R.id.video_bt_dialog_volume);
        this.volumeBar.setMax(this.audioManager.getStreamMaxVolume(3));
        this.volumeBar.setProgress(this.volume);
        this.volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.BTAudioDialog.1
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                BTAudioDialog.this.volume = i;
                BTAudioDialog.this.audioManager.setStreamVolume(3, BTAudioDialog.this.volume, 0);
            }
        });
        this.deviceRecycler = (RecyclerView) this.popupView.findViewById(R.id.video_bt_dialog_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context);
        linearLayoutManager.setOrientation(1);
        this.deviceRecycler.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this.deviceRecycler.getContext(), linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this.context, R.drawable.unclaimed_list_divider));
        this.deviceRecycler.addItemDecoration(dividerItemDecoration);
        this.deviceRecycler.setAdapter(this.dialogAdapter);
        this.blurView = layoutInflater.inflate(R.layout.blur_popup, (ViewGroup) null);
        this.blurView.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.-$$Lambda$BTAudioDialog$JP6gvm5DUdL0NpdBGJTO5hV3xlU
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                BTAudioDialog.this.popupWindow.dismiss();
            }
        });
        this.blurBackgroundPopup = new PopupWindow(this.blurView, -1, -1);
        this.popupWindow = new PopupWindow(this.popupView, (int) this.context.getResources().getDimension(R.dimen._275sdp), (int) this.context.getResources().getDimension(R.dimen._430sdp));
        this.popupWindow.setTouchable(true);
        this.popupWindow.setFocusable(true);
        this.popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(this.context, R.drawable.unclaimed_list_background));
        this.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.-$$Lambda$BTAudioDialog$IHOF4fb41rGw1IzE0QRTXbItmWo
            @Override // android.widget.PopupWindow.OnDismissListener
            public final void onDismiss() {
                BTAudioDialog.this.handleDismiss();
            }
        });
    }

    private void showPopup() {
        this.blurBackgroundPopup.showAsDropDown(this.blurView);
        this.popupWindow.showAtLocation(this.popupView, 48, 0, (int) this.context.getResources().getDimension(R.dimen._60sdp));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleItemClick(View view, int i) {
        if (i != 0) {
            this.mediaRouter.selectRoute(1, this.mediaRouter.getRouteAt(i));
        } else {
            this.mediaRouter.selectRoute(1, this.mediaRouter.getDefaultRoute());
        }
        this.volumeBar.setProgress(this.mediaRouter.getRouteAt(i).getVolume());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDismiss() {
        if (this.blurBackgroundPopup != null) {
            this.blurBackgroundPopup.dismiss();
        }
        this.mediaRouter.removeCallback(this.mediaRouterCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRouteDeviceList() {
        this.devices = new ArrayList();
        for (int i = 0; i < this.mediaRouter.getRouteCount(); i++) {
            this.mediaRouter.getRouteAt(i).setTag(Boolean.valueOf(isRouteSelected(i)));
            this.devices.add(this.mediaRouter.getRouteAt(i));
        }
        runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.-$$Lambda$BTAudioDialog$vShIlMcY-Xk8kgBOndujZETFQJE
            @Override // java.lang.Runnable
            public final void run() {
                BTAudioDialog.lambda$updateRouteDeviceList$1(BTAudioDialog.this);
            }
        });
    }

    public static /* synthetic */ void lambda$updateRouteDeviceList$1(BTAudioDialog bTAudioDialog) {
        bTAudioDialog.dialogAdapter.updateDevices(bTAudioDialog.devices);
        bTAudioDialog.dialogAdapter.notifyDataSetChanged();
    }

    private boolean isRouteSelected(int i) {
        return this.mediaRouter.getSelectedRoute(1).equals(this.mediaRouter.getRouteAt(i));
    }

    void runOnMain(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
