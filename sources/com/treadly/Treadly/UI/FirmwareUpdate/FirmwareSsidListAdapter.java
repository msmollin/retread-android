package com.treadly.Treadly.UI.FirmwareUpdate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.treadly.Treadly.R;
import com.treadly.client.lib.sdk.Model.WifiApInfo;
import java.util.List;

/* loaded from: classes2.dex */
public class FirmwareSsidListAdapter extends RecyclerView.Adapter<ViewHolder> {
    private ItemClickListener mClickListener;
    private List<WifiApInfo> mData;
    private LayoutInflater mInflater;

    /* loaded from: classes2.dex */
    public interface ItemClickListener {
        void onItemClick(View view, int i);
    }

    public FirmwareSsidListAdapter(Context context, List<WifiApInfo> list) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(this.mInflater.inflate(R.layout.ssid_row, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.myTextView.setText(this.mData.get(i).ssid);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mData.size();
    }

    /* loaded from: classes2.dex */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View view) {
            super(view);
            this.myTextView = (TextView) view.findViewById(R.id.ssid_item);
            view.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (FirmwareSsidListAdapter.this.mClickListener != null) {
                FirmwareSsidListAdapter.this.mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    WifiApInfo getItem(int i) {
        return this.mData.get(i);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
}
