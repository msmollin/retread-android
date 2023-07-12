package com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.treadly.Treadly.Data.Model.UserRunningSessionInfo;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedListAdapter;
import com.treadly.client.lib.sdk.Model.DeviceUserStatsUnclaimedLogInfo;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public class TreadlyActivityUnclaimedListAdapter extends RecyclerView.Adapter<ViewHolder> {
    private ItemClickListener clickListener;
    private Context context;
    private LayoutInflater inflater;
    private boolean sessionInfo = false;
    private List<DeviceUserStatsUnclaimedLogInfo> unclaimedLogInfos;
    private List<UserRunningSessionInfo> unclaimedSessionInfo;

    /* loaded from: classes2.dex */
    public interface ItemClickListener {
        void onItemClick(View view, int i);

        void onItemDelete(View view, int i);
    }

    public TreadlyActivityUnclaimedListAdapter(Context context, List<DeviceUserStatsUnclaimedLogInfo> list) {
        this.inflater = LayoutInflater.from(context);
        this.unclaimedLogInfos = list;
        this.context = context;
    }

    public TreadlyActivityUnclaimedListAdapter(Context context, List<UserRunningSessionInfo> list, @Nullable boolean z) {
        this.inflater = LayoutInflater.from(context);
        this.unclaimedSessionInfo = list;
        this.context = context;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(this.inflater.inflate(R.layout.unclaimed_activity_list_item, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (this.sessionInfo) {
            viewHolder.numberSteps.setText(String.valueOf(this.unclaimedSessionInfo.get(i).steps));
            if (this.unclaimedSessionInfo.get(i).timestamp.getTime() > 1577836800) {
                viewHolder.itemDate.setText(formatDate(this.unclaimedSessionInfo.get(i).timestamp));
                return;
            } else {
                viewHolder.itemDate.setText(R.string.unclaimed_date_not_avil);
                return;
            }
        }
        viewHolder.numberSteps.setText(String.valueOf(this.unclaimedLogInfos.get(i).finalSteps - this.unclaimedLogInfos.get(i).initialSteps));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        if (this.sessionInfo) {
            return this.unclaimedSessionInfo.size();
        }
        return this.unclaimedLogInfos.size();
    }

    /* loaded from: classes2.dex */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button claimButton;
        ImageButton deleteButton;
        TextView itemDate;
        TextView numberSteps;

        ViewHolder(View view) {
            super(view);
            this.numberSteps = (TextView) view.findViewById(R.id.unclaimed_item_number_steps);
            this.claimButton = (Button) view.findViewById(R.id.claim_unclaimed_item_button);
            this.deleteButton = (ImageButton) view.findViewById(R.id.delete_unclaimed_item_button);
            this.itemDate = (TextView) view.findViewById(R.id.unclaimed_item_date);
            this.claimButton.setOnClickListener(this);
            this.deleteButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.-$$Lambda$WhMAr1a8PDzqZWY_dsqKkU87mLE
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    TreadlyActivityUnclaimedListAdapter.ViewHolder.this.onDeleteClick(view2);
                }
            });
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (TreadlyActivityUnclaimedListAdapter.this.clickListener != null) {
                TreadlyActivityUnclaimedListAdapter.this.clickListener.onItemClick(view, getAdapterPosition());
            }
        }

        public void onDeleteClick(View view) {
            if (TreadlyActivityUnclaimedListAdapter.this.clickListener != null) {
                TreadlyActivityUnclaimedListAdapter.this.clickListener.onItemDelete(view, getAdapterPosition());
            }
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public void setUnclaimedActivities(List<DeviceUserStatsUnclaimedLogInfo> list) {
        this.unclaimedLogInfos = list;
        notifyDataSetChanged();
    }

    public void setUnclaimedSessions(List<UserRunningSessionInfo> list) {
        this.unclaimedSessionInfo = list;
        notifyDataSetChanged();
    }

    private String formatDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        simpleDateFormat.setTimeZone(Calendar.getInstance().getTimeZone());
        return simpleDateFormat.format(date);
    }
}
