package com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents;

import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.media.MediaRouter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.treadly.Treadly.R;
import java.util.List;

/* loaded from: classes2.dex */
public class BTAudioDialogAdapter extends RecyclerView.Adapter<ViewHolder> {
    BluetoothManager btManager;
    private ItemClickListener clickListener;
    private Context context;
    private List<MediaRouter.RouteInfo> devices;
    private LayoutInflater inflater;

    /* loaded from: classes2.dex */
    public interface ItemClickListener {
        void onItemClick(View view, int i);
    }

    public BTAudioDialogAdapter(Context context, List<MediaRouter.RouteInfo> list) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.devices = list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(this.inflater.inflate(R.layout.video_bt_dialog_item, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.name.setText(this.devices.get(i).getName());
        viewHolder.connected.setVisibility(((Boolean) this.devices.get(i).getTag()).booleanValue() ? 0 : 4);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.devices.size();
    }

    /* loaded from: classes2.dex */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {
        TextView connected;
        TextView name;

        ViewHolder(View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.video_bt_dialog_item_name);
            this.name.setOnClickListener(this);
            this.name.setOnTouchListener(this);
            this.connected = (TextView) view.findViewById(R.id.video_bt_dialog_item_connected);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Log.d("btaudio", "clicked");
            if (BTAudioDialogAdapter.this.clickListener != null) {
                BTAudioDialogAdapter.this.clickListener.onItemClick(view, getAdapterPosition());
            }
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getActionMasked()) {
                case 0:
                    this.name.setTextColor(BTAudioDialogAdapter.this.context.getResources().getColor(R.color.bt_dialog_item_clicked, null));
                    return false;
                case 1:
                    this.name.setTextColor(BTAudioDialogAdapter.this.context.getResources().getColor(R.color.bt_dialog_item, null));
                    return false;
                default:
                    return false;
            }
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public void updateDevices(List<MediaRouter.RouteInfo> list) {
        this.devices = list;
    }
}
