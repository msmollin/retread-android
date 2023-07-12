package com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.treadly.Treadly.Data.Model.UsersTrainerModes;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceUserStatsInfo;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/* loaded from: classes2.dex */
public class TreadlyVideoStatsAdapter extends RecyclerView.Adapter<ViewHolder> {
    private ItemClickListener clickListener;
    private Context context;
    private List<VideoServiceUserStatsInfo> friendsStats;
    private LayoutInflater inflater;
    private List<UsersTrainerModes> usersTrainerModes;

    /* loaded from: classes2.dex */
    public interface ItemClickListener {
        void onItemClick(View view, int i);
    }

    public void updateTrainerMode(int i) {
    }

    public TreadlyVideoStatsAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.friendsStats = new ArrayList();
        this.context = context;
    }

    public TreadlyVideoStatsAdapter(Context context, List<VideoServiceUserStatsInfo> list) {
        this.inflater = LayoutInflater.from(context);
        this.friendsStats = list;
        this.context = context;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(this.inflater.inflate(R.layout.video_stats_item, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.name.setText(this.friendsStats.get(i).name);
        viewHolder.speedValue.setText(String.valueOf(Math.round(this.friendsStats.get(i).speed * 100.0d) / 100.0d));
        viewHolder.avgValue.setText(String.valueOf(Math.round(this.friendsStats.get(i).averageSpeed * 100.0d) / 100.0d));
        viewHolder.steps.setText(String.valueOf(this.friendsStats.get(i).steps));
        viewHolder.distance.setText(String.valueOf(Math.round(this.friendsStats.get(i).distance * 100.0d) / 100.0d));
        viewHolder.calories.setText(String.valueOf(this.friendsStats.get(i).calories));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.friendsStats.size();
    }

    /* loaded from: classes2.dex */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView avgValue;
        TextView calories;
        TextView distance;
        TextView name;
        TextView speedValue;
        TextView steps;
        TextView trainerMode;

        ViewHolder(View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.video_stats_item_name);
            this.speedValue = (TextView) view.findViewById(R.id.video_stats_item_speed_value);
            this.avgValue = (TextView) view.findViewById(R.id.video_stats_item_avg_value);
            this.steps = (TextView) view.findViewById(R.id.video_stats_item_steps_value);
            this.calories = (TextView) view.findViewById(R.id.video_stats_item_calories_value);
            this.distance = (TextView) view.findViewById(R.id.video_stats_item_distance_value);
            this.trainerMode = (TextView) view.findViewById(R.id.video_stats_item_trainer_mode);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (TreadlyVideoStatsAdapter.this.clickListener != null) {
                TreadlyVideoStatsAdapter.this.clickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public void updateStats(List<VideoServiceUserStatsInfo> list) {
        list.sort(new Comparator() { // from class: com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.-$$Lambda$TreadlyVideoStatsAdapter$Mpy5MWKdPDcMoYPex4LkQz88bAY
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return TreadlyVideoStatsAdapter.lambda$updateStats$0((VideoServiceUserStatsInfo) obj, (VideoServiceUserStatsInfo) obj2);
            }
        });
        this.friendsStats = list;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$updateStats$0(VideoServiceUserStatsInfo videoServiceUserStatsInfo, VideoServiceUserStatsInfo videoServiceUserStatsInfo2) {
        return videoServiceUserStatsInfo.order >= videoServiceUserStatsInfo2.order ? 1 : -1;
    }
}
