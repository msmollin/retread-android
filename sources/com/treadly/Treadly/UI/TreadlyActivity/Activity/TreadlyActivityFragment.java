package com.treadly.Treadly.UI.TreadlyActivity.Activity;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.github.mikephil.charting.utils.Utils;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.Data.Model.UserProfileInfo;
import com.treadly.Treadly.Data.Model.UserRunningSessionInfo;
import com.treadly.Treadly.Data.Model.UserStatsInfo;
import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.ThirdParty.ScrollDatePicker.DayScrollDatePicker;
import com.treadly.Treadly.UI.ThirdParty.ScrollDatePicker.OnDateSelectedListener;
import com.treadly.Treadly.UI.TreadlyActivity.Activity.ActivityGraph.TreadlyActivityGraphAdapter;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public class TreadlyActivityFragment extends BaseFragment {
    private static final int BAD_ACTIVITY_INDEX = -1;
    public ProgressBar activityProgress;
    public Date activityStartDate;
    public Date activityTargetDate;
    private ImageButton backArrow;
    public UserStatsInfo currentActivityInfo;
    public UserInfo currentUserInfo;
    public UserProfileInfo currentUserProfile;
    public int[] dateArray;
    public DayScrollDatePicker datePicker;
    TreadlyActivityGraphAdapter graphAdapter;
    RecyclerView graphListView;
    public TextView numberCalories;
    public TextView numberDistance;
    public TextView numberSteps;
    public TextView numberTime;
    public TextView percentageActivity;
    public int position;
    private ImageView progressMedal;
    public List<UserStatsInfo> activityInfoList = new ArrayList();
    public List<UserRunningSessionInfo> runningSessionInfos = new ArrayList();
    public boolean fromActivityList = false;
    public boolean fromConnectCalendar = false;
    private int time = 0;
    private int steps = 0;
    private double distance = Utils.DOUBLE_EPSILON;
    private int calories = 0;

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.setOnBackPressedListener(null);
        }
        return layoutInflater.inflate(R.layout.fragment_treadly_activity, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        System.out.println("DAILYGOAL RESUME");
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        if (getActivity() != null) {
            ((MainActivity) getActivity()).hideBottomNavigationView();
        }
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        if (this.fromConnectCalendar) {
            showBottomNavigation();
        }
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        this.backArrow = (ImageButton) view.findViewById(R.id.activity_back_arrow_specific);
        this.backArrow.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyActivity.Activity.TreadlyActivityFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (TreadlyActivityFragment.this.getActivity() != null) {
                    TreadlyActivityFragment.this.getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });
        initCalendar(view);
        this.numberSteps = (TextView) view.findViewById(R.id.number_steps_specific);
        this.numberTime = (TextView) view.findViewById(R.id.number_time_specific);
        this.numberDistance = (TextView) view.findViewById(R.id.number_distance_specific);
        this.numberCalories = (TextView) view.findViewById(R.id.number_calories_specific);
        this.activityProgress = (ProgressBar) view.findViewById(R.id.activity_progress_bar_specific);
        this.progressMedal = (ImageView) view.findViewById(R.id.activity_progress_medal);
        this.percentageActivity = (TextView) view.findViewById(R.id.activity_progress_percentage_specific);
        this.graphListView = (RecyclerView) view.findViewById(R.id.graph_list_view);
        this.graphListView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.graphAdapter = new TreadlyActivityGraphAdapter(getContext(), this.runningSessionInfos, this.currentUserInfo);
        this.graphListView.setAdapter(this.graphAdapter);
        getRunSessionInfos(this.currentActivityInfo);
        updateViews();
    }

    void initCalendar(View view) {
        Date date;
        long timeInMillis;
        this.datePicker = (DayScrollDatePicker) view.findViewById(R.id.calendar_top_scroller_specific);
        this.datePicker.setShowTitle(false);
        this.datePicker.setShowFullDate(false);
        Calendar calendar = Calendar.getInstance();
        if (this.currentActivityInfo != null) {
            calendar.setTime(this.currentActivityInfo.timestamp);
        } else {
            calendar.set(this.dateArray[0], this.dateArray[1], this.dateArray[2]);
        }
        Calendar calendar2 = Calendar.getInstance();
        if (this.activityInfoList.isEmpty()) {
            date = calendar.getTime();
        } else {
            date = this.activityInfoList.get(this.activityInfoList.size() - 1).timestamp;
        }
        calendar2.setTime(date);
        int i = calendar2.get(1);
        this.datePicker.setStartDate(1, calendar2.get(2) + 1, i);
        calendar2.setTime(new Date(System.currentTimeMillis()));
        int i2 = calendar2.get(1);
        int i3 = calendar2.get(5);
        this.datePicker.setEndDate(i3, calendar2.get(2) + 1, i2);
        this.datePicker.setInitialSelectedDate(calendar.get(5), calendar.get(2) + 1, calendar.get(1));
        if (this.currentActivityInfo != null) {
            timeInMillis = this.currentActivityInfo.timestamp.getTime() - date.getTime();
        } else {
            timeInMillis = calendar.getTimeInMillis() - date.getTime();
        }
        long j = timeInMillis / 86400000;
        this.datePicker.scrollToSelectedDate();
        this.datePicker.getSelectedDate(new OnDateSelectedListener() { // from class: com.treadly.Treadly.UI.TreadlyActivity.Activity.TreadlyActivityFragment.2
            @Override // com.treadly.Treadly.UI.ThirdParty.ScrollDatePicker.OnDateSelectedListener
            public void onDateSelected(@Nullable Date date2) {
                if (date2 != null) {
                    int activityIndexByDate = TreadlyActivityFragment.this.getActivityIndexByDate(date2);
                    if (activityIndexByDate == -1) {
                        TreadlyActivityFragment.this.currentActivityInfo = null;
                        TreadlyActivityFragment.this.updateViews();
                        return;
                    }
                    TreadlyActivityFragment.this.currentActivityInfo = TreadlyActivityFragment.this.activityInfoList.get(activityIndexByDate);
                    if (TreadlyActivityFragment.this.getActivity() != null) {
                        TreadlyActivityFragment.this.updateViews();
                    }
                }
            }
        });
    }

    public int getActivityIndexByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int i = calendar.get(5);
        int i2 = calendar.get(2);
        int i3 = calendar.get(1);
        int i4 = 0;
        for (UserStatsInfo userStatsInfo : this.activityInfoList) {
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(userStatsInfo.timestamp);
            int i5 = calendar2.get(5);
            int i6 = calendar2.get(2);
            int i7 = calendar2.get(1);
            if (i5 == i && i6 == i2 && i7 == i3) {
                return i4;
            }
            i4++;
        }
        return -1;
    }

    void updateViews() {
        updateStats();
        NumberFormat numberInstance = NumberFormat.getNumberInstance(Locale.US);
        this.numberSteps.setText(numberInstance.format(this.steps));
        this.numberTime.setText(setTime(this.time));
        this.numberDistance.setText(numberInstance.format(this.distance) + " " + this.currentUserProfile.units.name().toLowerCase());
        this.numberCalories.setText(numberInstance.format((long) this.calories));
        double dailyGoal = this.currentActivityInfo.getDailyGoal();
        double dailyGoalProgress = this.currentActivityInfo.getDailyGoalProgress();
        int i = dailyGoal > Utils.DOUBLE_EPSILON ? (int) ((100.0d * dailyGoalProgress) / dailyGoal) : 0;
        this.activityProgress.setMax((int) (dailyGoal * 1000.0d));
        this.activityProgress.setProgress((int) (1000.0d * dailyGoalProgress));
        Drawable drawable = ((LayerDrawable) this.activityProgress.getProgressDrawable()).getDrawable(1);
        if (dailyGoalProgress >= dailyGoal) {
            this.progressMedal.setVisibility(0);
            drawable.setTint(ContextCompat.getColor(getContext(), R.color.specific_activity_progress_complete));
            this.percentageActivity.setTextColor(ContextCompat.getColor(getContext(), R.color.specific_activity_progress_complete));
        } else {
            this.progressMedal.setVisibility(4);
            drawable.setTint(ContextCompat.getColor(getContext(), R.color.specific_activity_progress_progress));
            this.percentageActivity.setTextColor(ContextCompat.getColor(getContext(), R.color.percent_specific_activity));
        }
        this.percentageActivity.setText(numberInstance.format(i) + "%");
        getRunSessionInfos(this.currentActivityInfo);
        this.graphAdapter.notifyDataSetChanged();
    }

    void updateStats() {
        if (this.currentActivityInfo == null) {
            resetStats();
            return;
        }
        UserStatsInfo userStatsInfo = this.currentActivityInfo;
        if (this.currentUserProfile == null) {
            resetStats();
            return;
        }
        UserProfileInfo userProfileInfo = this.currentUserProfile;
        this.time = (int) userStatsInfo.duration;
        this.steps = userStatsInfo.steps;
        this.distance = userStatsInfo.distance;
        this.calories = userStatsInfo.calories;
    }

    void resetStats() {
        this.time = 0;
        this.steps = 0;
        this.distance = Utils.DOUBLE_EPSILON;
        this.calories = 0;
    }

    void getRunSessionInfos(UserStatsInfo userStatsInfo) {
        if (userStatsInfo == null || userStatsInfo.timestamp == null) {
            this.graphAdapter.userRunningSessionInfos = new ArrayList();
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(userStatsInfo.timestamp);
        int i = calendar.get(1);
        TreadlyServiceManager.getInstance().getDeviceUserRunningSessionsInfoByDate(calendar.get(5), calendar.get(2) + 1, i, null, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyActivity.Activity.TreadlyActivityFragment.3
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onUserRunningSessionsInfo(String str, ArrayList<UserRunningSessionInfo> arrayList) {
                if (str != null || arrayList == null) {
                    return;
                }
                TreadlyActivityFragment.this.runningSessionInfos = arrayList;
                TreadlyActivityFragment.this.graphAdapter.userRunningSessionInfos = TreadlyActivityFragment.this.runningSessionInfos;
                TreadlyActivityFragment.this.graphAdapter.notifyDataSetChanged();
            }
        });
    }

    String setTime(int i) {
        return String.format(Locale.getDefault(), "%02d:%02d", Integer.valueOf(i / 60), Integer.valueOf(i % 60));
    }
}
