package com.treadly.Treadly.UI.TreadlyActivity.ActivityList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.github.mikephil.charting.utils.Utils;
import com.treadly.Treadly.Data.Model.UserProfileInfo;
import com.treadly.Treadly.Data.Model.UserStatsInfo;
import com.treadly.Treadly.R;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public class TreadlyActivityListAdapter extends RecyclerView.Adapter<ViewHolder> {
    private ItemClickListener clickListener;
    private Context context;
    public UserProfileInfo currentProfile;
    private LayoutInflater inflater;
    private List<UserStatsInfo> userStatsInfos;

    /* loaded from: classes2.dex */
    public interface ItemClickListener {
        void onItemClick(View view, int i);
    }

    public TreadlyActivityListAdapter(Context context, List<UserStatsInfo> list) {
        this.inflater = LayoutInflater.from(context);
        this.userStatsInfos = list;
        this.context = context;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(this.inflater.inflate(R.layout.activity_list_single_row, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        NumberFormat numberInstance = NumberFormat.getNumberInstance(Locale.US);
        UserStatsInfo userStatsInfo = this.userStatsInfos.get(i);
        viewHolder.activityDate.setText(new SimpleDateFormat("MM/dd/yyy").format(userStatsInfo.timestamp));
        viewHolder.activitySteps.setText(numberInstance.format(userStatsInfo.steps));
        viewHolder.activityDistance.setText(numberInstance.format(userStatsInfo.distance) + " " + this.currentProfile.units.toString().toLowerCase());
        viewHolder.activityCalories.setText(numberInstance.format((long) userStatsInfo.calories));
        Drawable drawable = ((LayerDrawable) viewHolder.activityProgress.getProgressDrawable()).getDrawable(1);
        double dailyGoal = userStatsInfo.getDailyGoal();
        double dailyGoalProgress = userStatsInfo.getDailyGoalProgress();
        if (dailyGoalProgress >= dailyGoal) {
            viewHolder.progressMedal.setVisibility(0);
            drawable.setTint(ContextCompat.getColor(this.context, R.color.specific_activity_progress_complete));
            viewHolder.activityPercentage.setTextColor(ContextCompat.getColor(this.context, R.color.specific_activity_progress_complete));
        } else {
            viewHolder.progressMedal.setVisibility(4);
            drawable.setTint(ContextCompat.getColor(this.context, R.color.specific_activity_progress_progress));
            viewHolder.activityPercentage.setTextColor(ContextCompat.getColor(this.context, R.color.percent_specific_activity));
        }
        int i2 = dailyGoal > Utils.DOUBLE_EPSILON ? (int) ((dailyGoalProgress * 100.0d) / dailyGoal) : 0;
        viewHolder.activityPercentage.setText(numberInstance.format(i2) + "%");
        viewHolder.activityProgress.setMax((int) (dailyGoal * 100.0d));
        viewHolder.activityProgress.setProgress((int) (dailyGoalProgress * 100.0d));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.userStatsInfos.size();
    }

    /* loaded from: classes2.dex */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView activity;
        TextView activityCalories;
        TextView activityDate;
        TextView activityDistance;
        TextView activityPercentage;
        ProgressBar activityProgress;
        TextView activitySteps;
        ImageView progressMedal;

        ViewHolder(View view) {
            super(view);
            this.activity = (CardView) view.findViewById(R.id.activity_card);
            this.activityDate = (TextView) view.findViewById(R.id.activity_card_date);
            this.activitySteps = (TextView) view.findViewById(R.id.activity_card_steps);
            this.activityDistance = (TextView) view.findViewById(R.id.activity_card_distance);
            this.activityCalories = (TextView) view.findViewById(R.id.activity_card_calories);
            this.activityPercentage = (TextView) view.findViewById(R.id.activity_card_progress_percentage);
            this.activityProgress = (ProgressBar) view.findViewById(R.id.activity_card_progress_bar);
            this.progressMedal = (ImageView) view.findViewById(R.id.activity_card_medal);
            this.activity.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (TreadlyActivityListAdapter.this.clickListener != null) {
                TreadlyActivityListAdapter.this.clickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    UserStatsInfo getItem(int i) {
        return this.userStatsInfos.get(i);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
}
