package com.treadly.Treadly.UI.TreadlyActivity.ActivityCalendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.Data.Model.UserProfileInfo;
import com.treadly.Treadly.Data.Model.UserStatsInfo;
import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.ThirdParty.Calendar.interfaces.OnDateSelectedListener;
import com.treadly.Treadly.UI.ThirdParty.Calendar.objects.CalendarDate;
import com.treadly.Treadly.UI.ThirdParty.Calendar.views.CustomCalendarView;
import com.treadly.Treadly.UI.TreadlyActivity.Activity.TreadlyActivityFragment;
import com.treadly.Treadly.UI.TreadlyActivity.ActivityList.TreadlyActivityListFragment;
import com.treadly.Treadly.UI.Util.ActivityUtil;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class TreadlyActivityCalendarFragment extends BaseFragment implements OnDateSelectedListener {
    public static final String TAG = "TREADLY_ACTIVITY_CALENDAR";
    private TextView allActivtyButton;
    private CustomCalendarView calendarView;
    private View emptySpace;
    UserProfileInfo userProfile;
    public ActivityCalendarListener listener = null;
    public boolean toActivity = false;
    List<UserStatsInfo> activityList = null;

    /* loaded from: classes2.dex */
    public interface ActivityCalendarListener {
        void onDetach();
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_activity_calendar, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        getData();
        initCalendar(view);
        this.allActivtyButton = (TextView) view.findViewById(R.id.all_activity_button);
        this.allActivtyButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyActivity.ActivityCalendar.-$$Lambda$TreadlyActivityCalendarFragment$Mv6yex149sZjIsCeAI_obx0YrZg
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyActivityCalendarFragment.lambda$onViewCreated$0(TreadlyActivityCalendarFragment.this, view2);
            }
        });
        this.emptySpace = view.findViewById(R.id.empty_space_calendar);
        this.emptySpace.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyActivity.ActivityCalendar.-$$Lambda$TreadlyActivityCalendarFragment$tAm8cQfOFIBK66UL7JrezaXILSU
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyActivityCalendarFragment.this.popBackStack();
            }
        });
    }

    public static /* synthetic */ void lambda$onViewCreated$0(TreadlyActivityCalendarFragment treadlyActivityCalendarFragment, View view) {
        TreadlyActivityListFragment treadlyActivityListFragment = new TreadlyActivityListFragment();
        if (treadlyActivityCalendarFragment.getActivity() != null) {
            MainActivity mainActivity = (MainActivity) treadlyActivityCalendarFragment.getActivity();
            mainActivity.getSupportFragmentManager().popBackStack();
            mainActivity.getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.activity_fragment_container_empty, treadlyActivityListFragment, TreadlyActivityListFragment.TAG).commit();
        }
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        if (this.listener != null) {
            this.listener.onDetach();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onHiddenChanged(boolean z) {
        super.onHiddenChanged(z);
        if (!z || getActivity() == null) {
            return;
        }
        onDetach();
        getActivity().getSupportFragmentManager().beginTransaction().detach(this).commit();
    }

    private void initCalendar(View view) {
        this.calendarView = (CustomCalendarView) view.findViewById(R.id.activity_calendar);
        showLoading();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCalendarView() {
        dismissLoading();
        this.calendarView.setData(this.activityList, this.userProfile);
        this.calendarView.setOnDateSelectedListener(this);
    }

    private UserStatsInfo getActivity(int i, int i2, int i3) {
        Calendar calendar = Calendar.getInstance();
        for (UserStatsInfo userStatsInfo : this.activityList) {
            if (userStatsInfo.timestamp != null) {
                calendar.setTime(userStatsInfo.timestamp);
                int i4 = calendar.get(5);
                int i5 = calendar.get(2) + 1;
                int i6 = calendar.get(1);
                if (i == i4 && i2 == i5 && i3 == i6) {
                    return userStatsInfo;
                }
            }
        }
        return null;
    }

    private void getData() {
        String userId = TreadlyServiceManager.getInstance().getUserId();
        if (userId == null) {
            return;
        }
        TreadlyServiceManager.getInstance().getUserProfileInfo(userId, new AnonymousClass1(userId));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyActivity.ActivityCalendar.TreadlyActivityCalendarFragment$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends TreadlyServiceResponseEventAdapter {
        final /* synthetic */ String val$userId;

        AnonymousClass1(String str) {
            this.val$userId = str;
        }

        @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
        public void onUserProfile(String str, UserProfileInfo userProfileInfo) {
            if (str != null) {
                TreadlyActivityCalendarFragment.this.dismissLoading();
                return;
            }
            TreadlyActivityCalendarFragment.this.userProfile = userProfileInfo;
            TreadlyServiceManager.getInstance().getDeviceUserStatsInfo(this.val$userId, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyActivity.ActivityCalendar.TreadlyActivityCalendarFragment.1.1
                @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                public void onUserStatsInfo(String str2, ArrayList<UserStatsInfo> arrayList) {
                    TreadlyActivityCalendarFragment.this.dismissLoading();
                    if (str2 != null || arrayList == null) {
                        return;
                    }
                    if (TreadlyActivityCalendarFragment.this.activityList == null) {
                        TreadlyActivityCalendarFragment.this.activityList = new ArrayList();
                    }
                    TreadlyActivityCalendarFragment.this.activityList.clear();
                    Iterator<UserStatsInfo> it = arrayList.iterator();
                    while (it.hasNext()) {
                        UserStatsInfo next = it.next();
                        if (next.timestamp != null) {
                            TreadlyActivityCalendarFragment.this.activityList.add(next);
                        }
                    }
                    Collections.sort(TreadlyActivityCalendarFragment.this.activityList, new Comparator<UserStatsInfo>() { // from class: com.treadly.Treadly.UI.TreadlyActivity.ActivityCalendar.TreadlyActivityCalendarFragment.1.1.1
                        @Override // java.util.Comparator
                        public int compare(UserStatsInfo userStatsInfo, UserStatsInfo userStatsInfo2) {
                            if (userStatsInfo.timestamp.getTime() == userStatsInfo2.timestamp.getTime()) {
                                return 0;
                            }
                            return userStatsInfo.timestamp.getTime() > userStatsInfo2.timestamp.getTime() ? -1 : 1;
                        }
                    });
                    if (TreadlyActivityCalendarFragment.this.getActivity() != null) {
                        ActivityUtil.runOnUiThread(TreadlyActivityCalendarFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyActivity.ActivityCalendar.TreadlyActivityCalendarFragment.1.1.2
                            @Override // java.lang.Runnable
                            public void run() {
                                TreadlyActivityCalendarFragment.this.updateCalendarView();
                            }
                        });
                    }
                }
            });
        }
    }

    @Override // com.treadly.Treadly.UI.ThirdParty.Calendar.interfaces.OnDateSelectedListener
    public void onDateSelected(CalendarDate calendarDate) {
        TreadlyActivityFragment treadlyActivityFragment = new TreadlyActivityFragment();
        treadlyActivityFragment.dateArray = new int[]{calendarDate.getYear(), calendarDate.getMonth(), calendarDate.getDay()};
        treadlyActivityFragment.activityInfoList = this.activityList;
        treadlyActivityFragment.currentActivityInfo = getActivity(calendarDate.getDay(), calendarDate.getMonth() + 1, calendarDate.getYear());
        treadlyActivityFragment.currentUserProfile = this.userProfile;
        treadlyActivityFragment.currentUserInfo = TreadlyServiceManager.getInstance().getUserInfo();
        treadlyActivityFragment.fromConnectCalendar = true;
        treadlyActivityFragment.fromActivityList = false;
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().popBackStack();
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity_fragment_container_empty, treadlyActivityFragment).addToBackStack(TAG).commit();
        }
    }
}
