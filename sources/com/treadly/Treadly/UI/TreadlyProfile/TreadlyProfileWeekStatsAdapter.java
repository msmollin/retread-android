package com.treadly.Treadly.UI.TreadlyProfile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import com.treadly.Treadly.Data.Model.UserStatsInfo;
import com.treadly.Treadly.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public class TreadlyProfileWeekStatsAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<UserStatsInfo> userStatsInfos;

    @Override // androidx.viewpager.widget.PagerAdapter
    public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
        return view == obj;
    }

    public TreadlyProfileWeekStatsAdapter(Context context, List<UserStatsInfo> list) {
        this.context = context;
        this.userStatsInfos = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    @NonNull
    public Object instantiateItem(@NonNull ViewGroup viewGroup, int i) {
        UserStatsInfo userStatsInfo = this.userStatsInfos.get(i);
        ViewGroup viewGroup2 = (ViewGroup) this.inflater.inflate(R.layout.week_stats_page, viewGroup, false);
        ((TextView) viewGroup2.findViewById(R.id.week_stat_title)).setText(getPageTitle(i));
        TextView textView = (TextView) viewGroup2.findViewById(R.id.week_stat_steps_count);
        TextView textView2 = (TextView) viewGroup2.findViewById(R.id.week_stat_workout_number);
        TextView textView3 = (TextView) viewGroup2.findViewById(R.id.week_stat_workout_time);
        if (userStatsInfo != null) {
            textView.setText(String.valueOf(userStatsInfo.steps));
            if (userStatsInfo.statsCount == 1) {
                textView2.setText(String.valueOf(userStatsInfo.statsCount) + " WORKOUT");
            } else {
                textView2.setText(String.valueOf(userStatsInfo.statsCount) + " WORKOUTS");
            }
            textView3.setText(String.format("%02d:%02d:%02d", Integer.valueOf((int) (userStatsInfo.duration / 3600.0d)), Integer.valueOf((int) (userStatsInfo.duration / 60.0d)), Integer.valueOf(((int) userStatsInfo.duration) % 60)));
        } else {
            textView.setText(String.valueOf(0));
            textView3.setText(String.format("%02d:%02d:%02d", 0, 0, 0));
        }
        viewGroup.addView(viewGroup2);
        return viewGroup2;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public void destroyItem(@NonNull ViewGroup viewGroup, int i, @NonNull Object obj) {
        viewGroup.removeView((View) obj);
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public int getCount() {
        return this.userStatsInfos.size();
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    @Nullable
    public CharSequence getPageTitle(int i) {
        Date date;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        if (this.userStatsInfos.get(i) == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(System.currentTimeMillis()));
            int i2 = calendar.get(5);
            calendar.set(calendar.get(1), calendar.get(2), i2 - ((this.userStatsInfos.size() - 1) - i));
            date = calendar.getTime();
        } else {
            date = this.userStatsInfos.get(i).timestamp;
        }
        return simpleDateFormat.format(date);
    }
}
