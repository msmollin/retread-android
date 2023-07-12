package com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.Data.Model.UserRunningSessionInfo;
import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedFragment;
import com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedListAdapter;
import com.treadly.Treadly.UI.Util.ActivityUtil;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
import com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventAdapter;
import com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventListener;
import com.treadly.client.lib.sdk.Model.DeviceUserStatsUnclaimedLogInfo;
import com.treadly.client.lib.sdk.TreadlyClientLib;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class TreadlyActivityUnclaimedFragment extends BaseFragment {
    public static final String TAG = "ACTIVITY_UNCLAIMED";
    public static boolean isDisplaying = false;
    Button claimAllButton;
    Button claimButton;
    DatePicker datePicker;
    ConstraintLayout datePickerHeader;
    PopupWindow datePickerPopup;
    View datePickerView;
    TextView description;
    SwipeRefreshLayout swipeRefreshLayout;
    TimePicker timePicker;
    public List<DeviceUserStatsUnclaimedLogInfo> unclaimedActivities;
    TreadlyActivityUnclaimedListAdapter unclaimedAdapter;
    RecyclerView unclaimedRecycler;
    public List<UserRunningSessionInfo> unclaimedSessionInfos;
    TextView warning;
    DeviceUserStatsUnclaimedLogInfo pendingClaimInfo = null;
    UserRunningSessionInfo pendingSessionInfo = null;
    public boolean isSessionInfo = false;
    public boolean showNavOnDetach = false;
    public String deviceAddress = null;
    boolean pendingRefresh = false;
    DeviceUserStatsLogEventListener deviceUserStatsLogEventAdapter = new DeviceUserStatsLogEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedFragment.2
        @Override // com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventAdapter, com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventListener
        public void onDeviceUserStatsUnclaimedUserStatsInfo(DeviceUserStatsUnclaimedLogInfo[] deviceUserStatsUnclaimedLogInfoArr) {
            TreadlyActivityUnclaimedFragment.this.pendingRefresh = false;
            if (deviceUserStatsUnclaimedLogInfoArr == null) {
                return;
            }
            TreadlyActivityUnclaimedFragment.this.processUnclaimedActivities(new ArrayList(Arrays.asList(deviceUserStatsUnclaimedLogInfoArr)));
        }

        @Override // com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventAdapter, com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventListener
        public void onDeviceUserStatsClaimUnclaimedUserStatsInfoResponse(boolean z) {
            TreadlyActivityUnclaimedFragment.this.handleClaimActivityResponse(z);
        }
    };
    private final TreadlyServiceResponseEventListener sessionAdapter = new AnonymousClass4();

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        isDisplaying = true;
        TreadlyClientLib.shared.addDeviceUserStatsLogEventListener(this.deviceUserStatsLogEventAdapter);
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_activity_unclaimed, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        ((ImageButton) view.findViewById(R.id.unclaimed_x)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.-$$Lambda$TreadlyActivityUnclaimedFragment$4Sy9rEpw16PSyvM_nbb7FfyRgbg
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyActivityUnclaimedFragment.this.popBackStack();
            }
        });
        this.description = (TextView) view.findViewById(R.id.unclaimed_paragraph);
        this.warning = (TextView) view.findViewById(R.id.unclaimed_warning);
        this.claimAllButton = (Button) view.findViewById(R.id.unclaimed_claim_all_button);
        this.claimAllButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.-$$Lambda$TreadlyActivityUnclaimedFragment$o1m-NKvSBzoUK4L-LZucemX_0hQ
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyActivityUnclaimedFragment.this.handleClaimAllSessions();
            }
        });
        initList(view);
        if (this.showNavOnDetach) {
            hideBottomNavigation(true);
        }
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        TreadlyClientLib.shared.removeDeviceUserStatsLogEventListener(this.deviceUserStatsLogEventAdapter);
        isDisplaying = false;
        if (this.showNavOnDetach) {
            showBottomNavigation(true);
        }
    }

    private void initList(View view) {
        boolean z = this.isSessionInfo;
        int i = R.string.unclaimed_popup_para;
        if (z) {
            this.unclaimedSessionInfos.sort(new Comparator() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.-$$Lambda$TreadlyActivityUnclaimedFragment$taVMEwBew5b7bMA5VxaOlnDzHio
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    return TreadlyActivityUnclaimedFragment.lambda$initList$2((UserRunningSessionInfo) obj, (UserRunningSessionInfo) obj2);
                }
            });
            TextView textView = this.description;
            if (this.unclaimedSessionInfos.size() == 0) {
                i = R.string.unclaimed_popup_para_no_activities;
            }
            textView.setText(i);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.description.getLayoutParams();
            layoutParams.height = -2;
            this.description.setLayoutParams(layoutParams);
            this.claimAllButton.setVisibility(this.unclaimedSessionInfos.size() != 0 ? 0 : 8);
        } else {
            this.unclaimedActivities.sort(new Comparator() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.-$$Lambda$TreadlyActivityUnclaimedFragment$JAXTyxUVdPJumJFtkew3sebz8EY
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    return TreadlyActivityUnclaimedFragment.lambda$initList$3((DeviceUserStatsUnclaimedLogInfo) obj, (DeviceUserStatsUnclaimedLogInfo) obj2);
                }
            });
            TextView textView2 = this.description;
            if (this.unclaimedActivities.size() == 0) {
                i = R.string.unclaimed_popup_para_no_activities;
            }
            textView2.setText(i);
            ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) this.description.getLayoutParams();
            layoutParams2.height = -2;
            this.description.setLayoutParams(layoutParams2);
            this.claimAllButton.setVisibility(8);
        }
        this.unclaimedRecycler = (RecyclerView) view.findViewById(R.id.unclaimed_recycler_list);
        if (this.isSessionInfo) {
            this.unclaimedAdapter = new TreadlyActivityUnclaimedListAdapter(getContext(), this.unclaimedSessionInfos, this.isSessionInfo);
        } else {
            this.unclaimedAdapter = new TreadlyActivityUnclaimedListAdapter(getContext(), this.unclaimedActivities);
        }
        this.unclaimedAdapter.setClickListener(new TreadlyActivityUnclaimedListAdapter.ItemClickListener() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedFragment.1
            @Override // com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedListAdapter.ItemClickListener
            public void onItemClick(View view2, int i2) {
                if (TreadlyActivityUnclaimedFragment.this.isSessionInfo) {
                    TreadlyActivityUnclaimedFragment.this.handleClaimSessionSelected(TreadlyActivityUnclaimedFragment.this.unclaimedSessionInfos.get(i2));
                } else {
                    TreadlyActivityUnclaimedFragment.this.handleClaimActivitySelected(TreadlyActivityUnclaimedFragment.this.unclaimedActivities.get(i2));
                }
            }

            @Override // com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedListAdapter.ItemClickListener
            public void onItemDelete(View view2, int i2) {
                if (TreadlyActivityUnclaimedFragment.this.isSessionInfo) {
                    TreadlyActivityUnclaimedFragment.this.handleDeleteSessionSelected(TreadlyActivityUnclaimedFragment.this.unclaimedSessionInfos.get(i2));
                }
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(1);
        this.unclaimedRecycler.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this.unclaimedRecycler.getContext(), linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.unclaimed_list_divider));
        this.unclaimedRecycler.addItemDecoration(dividerItemDecoration);
        this.unclaimedRecycler.setAdapter(this.unclaimedAdapter);
        this.swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.unclaimed_swipe_refresh_layout);
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.-$$Lambda$67IzxCXA9NV2xBCibq4tCPnHn5E
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public final void onRefresh() {
                TreadlyActivityUnclaimedFragment.this.refresh();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$initList$2(UserRunningSessionInfo userRunningSessionInfo, UserRunningSessionInfo userRunningSessionInfo2) {
        return Integer.parseInt(userRunningSessionInfo2.log_id) - Integer.parseInt(userRunningSessionInfo.log_id);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$initList$3(DeviceUserStatsUnclaimedLogInfo deviceUserStatsUnclaimedLogInfo, DeviceUserStatsUnclaimedLogInfo deviceUserStatsUnclaimedLogInfo2) {
        return deviceUserStatsUnclaimedLogInfo2.sequence - deviceUserStatsUnclaimedLogInfo.sequence;
    }

    public void dismiss() {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity == null || mainActivity.getSupportFragmentManager().findFragmentByTag(TAG) == null) {
            return;
        }
        mainActivity.getSupportFragmentManager().popBackStack();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void refresh() {
        if (this.isSessionInfo) {
            fetchUnclaimedSessions();
        } else {
            fetchUnclaimedActivities();
        }
    }

    private void showDatePicker(final DeviceUserStatsUnclaimedLogInfo deviceUserStatsUnclaimedLogInfo) {
        initDatePicker();
        this.claimButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.-$$Lambda$TreadlyActivityUnclaimedFragment$gvdplgRtStjUMNo2GPsLWAjdhzI
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TreadlyActivityUnclaimedFragment.lambda$showDatePicker$4(TreadlyActivityUnclaimedFragment.this, deviceUserStatsUnclaimedLogInfo, view);
            }
        });
        displayDatePicker();
    }

    public static /* synthetic */ void lambda$showDatePicker$4(TreadlyActivityUnclaimedFragment treadlyActivityUnclaimedFragment, DeviceUserStatsUnclaimedLogInfo deviceUserStatsUnclaimedLogInfo, View view) {
        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = treadlyActivityUnclaimedFragment.datePicker.getDayOfMonth();
        calendar.set(treadlyActivityUnclaimedFragment.datePicker.getYear(), treadlyActivityUnclaimedFragment.datePicker.getMonth(), dayOfMonth, 0, 0, 0);
        treadlyActivityUnclaimedFragment.claimActivity(deviceUserStatsUnclaimedLogInfo, treadlyActivityUnclaimedFragment.addTimeToDate(calendar.getTime()));
        treadlyActivityUnclaimedFragment.datePickerPopup.dismiss();
    }

    private void showDatePicker(final UserRunningSessionInfo userRunningSessionInfo) {
        initDatePicker();
        this.claimButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.-$$Lambda$TreadlyActivityUnclaimedFragment$1jwzFyT5Eu_HQiIjewxiSqStOzs
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TreadlyActivityUnclaimedFragment.lambda$showDatePicker$5(TreadlyActivityUnclaimedFragment.this, userRunningSessionInfo, view);
            }
        });
        displayDatePicker();
    }

    public static /* synthetic */ void lambda$showDatePicker$5(TreadlyActivityUnclaimedFragment treadlyActivityUnclaimedFragment, UserRunningSessionInfo userRunningSessionInfo, View view) {
        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = treadlyActivityUnclaimedFragment.datePicker.getDayOfMonth();
        calendar.set(treadlyActivityUnclaimedFragment.datePicker.getYear(), treadlyActivityUnclaimedFragment.datePicker.getMonth(), dayOfMonth, 0, 0, 0);
        userRunningSessionInfo.timestamp = treadlyActivityUnclaimedFragment.addTimeToDate(calendar.getTime());
        treadlyActivityUnclaimedFragment.handleClaimSessionSelected(userRunningSessionInfo);
        treadlyActivityUnclaimedFragment.datePickerPopup.dismiss();
    }

    private void initDatePicker() {
        this.datePickerView = ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(R.layout.unclaimed_date_picker, (ViewGroup) null);
        this.datePickerView.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.-$$Lambda$TreadlyActivityUnclaimedFragment$QUpjOksh02uwUdcEQnRyXcP7J0c
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TreadlyActivityUnclaimedFragment.this.datePickerPopup.dismiss();
            }
        });
        this.datePickerHeader = (ConstraintLayout) this.datePickerView.findViewById(R.id.unclaimed_date_picker_title_holder);
        this.datePickerHeader.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.-$$Lambda$TreadlyActivityUnclaimedFragment$q4TDKGgBxPKeLEAFKZsjKiMOeAk
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TreadlyActivityUnclaimedFragment.this.datePickerPopup.dismiss();
            }
        });
        this.datePicker = (DatePicker) this.datePickerView.findViewById(R.id.unclaimed_date_picker);
        this.datePicker.setMaxDate(System.currentTimeMillis());
        this.datePicker.setMinDate(0L);
        this.timePicker = (TimePicker) this.datePickerView.findViewById(R.id.unclaimed_time_picker);
        this.claimButton = (Button) this.datePickerView.findViewById(R.id.unclaimed_picker_claim_button);
    }

    private void displayDatePicker() {
        this.datePickerPopup = new PopupWindow(this.datePickerView, -1, -1);
        FragmentActivity activity = getActivity();
        if (activity == null) {
            this.datePickerPopup.showAsDropDown(this.datePickerView);
            return;
        }
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        this.datePickerPopup.showAtLocation(this.datePickerView, 80, 0, activity.getWindow().getDecorView().getHeight() - rect.bottom);
    }

    private Date addTimeToDate(Date date) {
        int hour = this.timePicker.getHour();
        return new Date(date.getTime() + (hour * 3600000) + (this.timePicker.getMinute() * 60000));
    }

    void handleClaimActivityResponse(final boolean z) {
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.-$$Lambda$TreadlyActivityUnclaimedFragment$rzupMcubS-CJODqZKg78_xX-w_U
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyActivityUnclaimedFragment.lambda$handleClaimActivityResponse$8(TreadlyActivityUnclaimedFragment.this, z);
            }
        });
    }

    public static /* synthetic */ void lambda$handleClaimActivityResponse$8(TreadlyActivityUnclaimedFragment treadlyActivityUnclaimedFragment, boolean z) {
        if (z) {
            if (treadlyActivityUnclaimedFragment.pendingClaimInfo != null) {
                boolean z2 = false;
                Iterator<DeviceUserStatsUnclaimedLogInfo> it = treadlyActivityUnclaimedFragment.unclaimedActivities.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    DeviceUserStatsUnclaimedLogInfo next = it.next();
                    if (next.logId == treadlyActivityUnclaimedFragment.pendingClaimInfo.logId) {
                        treadlyActivityUnclaimedFragment.unclaimedActivities.remove(next);
                        z2 = true;
                        break;
                    }
                }
                if (z2) {
                    treadlyActivityUnclaimedFragment.processUnclaimedActivities(treadlyActivityUnclaimedFragment.unclaimedActivities);
                } else {
                    treadlyActivityUnclaimedFragment.dismissLoading();
                }
            }
        } else {
            treadlyActivityUnclaimedFragment.showBaseAlert("Claim Activity", "The unclaimed activity has been claimed by another user");
        }
        treadlyActivityUnclaimedFragment.pendingClaimInfo = null;
        treadlyActivityUnclaimedFragment.fetchUnclaimedActivities();
    }

    void fetchUnclaimedActivities() {
        if (TreadlyClientLib.shared.getUnclaimedUserStatsLogInfo()) {
            showLoading(false);
            return;
        }
        dismissLoading();
        this.swipeRefreshLayout.setRefreshing(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedFragment$3  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 extends TreadlyServiceResponseEventAdapter {
        AnonymousClass3() {
        }

        @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
        public void onGetUnclaimedLogs(String str, final List<UserRunningSessionInfo> list) {
            super.onGetUnclaimedLogs(str, list);
            TreadlyActivityUnclaimedFragment.this.pendingRefresh = false;
            ActivityUtil.runOnUiThread(TreadlyActivityUnclaimedFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.-$$Lambda$TreadlyActivityUnclaimedFragment$3$fQyZCn6bfZnjmJLE932A6H3tkZg
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyActivityUnclaimedFragment.this.processUnclaimedSessions(list);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fetchUnclaimedSessions() {
        if (TreadlyServiceManager.getInstance().getUnclaimedUserStatsLogInfo(this.deviceAddress, new AnonymousClass3())) {
            showLoading(false);
            return;
        }
        dismissLoading();
        this.swipeRefreshLayout.setRefreshing(false);
    }

    void processUnclaimedActivities(List<DeviceUserStatsUnclaimedLogInfo> list) {
        dismissLoading();
        this.swipeRefreshLayout.setRefreshing(false);
        list.sort(new Comparator() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.-$$Lambda$TreadlyActivityUnclaimedFragment$6G4QO-Fm0MjEpcoFdrqFCRuyKWE
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return TreadlyActivityUnclaimedFragment.lambda$processUnclaimedActivities$9((DeviceUserStatsUnclaimedLogInfo) obj, (DeviceUserStatsUnclaimedLogInfo) obj2);
            }
        });
        this.unclaimedActivities = list;
        this.description.setText(this.unclaimedActivities.size() == 0 ? R.string.unclaimed_popup_para_no_activities : R.string.unclaimed_popup_para);
        this.unclaimedAdapter.setUnclaimedActivities(list);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$processUnclaimedActivities$9(DeviceUserStatsUnclaimedLogInfo deviceUserStatsUnclaimedLogInfo, DeviceUserStatsUnclaimedLogInfo deviceUserStatsUnclaimedLogInfo2) {
        return deviceUserStatsUnclaimedLogInfo.sequence - deviceUserStatsUnclaimedLogInfo2.sequence;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processUnclaimedSessions(List<UserRunningSessionInfo> list) {
        dismissLoading();
        this.swipeRefreshLayout.setRefreshing(false);
        list.sort(new Comparator() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.-$$Lambda$TreadlyActivityUnclaimedFragment$EfMQq3f-gaw3FmA81YQYxyPeLwE
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return TreadlyActivityUnclaimedFragment.lambda$processUnclaimedSessions$10((UserRunningSessionInfo) obj, (UserRunningSessionInfo) obj2);
            }
        });
        this.unclaimedSessionInfos = list;
        this.description.setText(this.unclaimedSessionInfos.size() == 0 ? R.string.unclaimed_popup_para_no_activities : R.string.unclaimed_popup_para);
        this.claimAllButton.setVisibility(this.unclaimedSessionInfos.size() == 0 ? 8 : 0);
        this.unclaimedAdapter.setUnclaimedSessions(this.unclaimedSessionInfos);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$processUnclaimedSessions$10(UserRunningSessionInfo userRunningSessionInfo, UserRunningSessionInfo userRunningSessionInfo2) {
        return Integer.parseInt(userRunningSessionInfo2.log_id) - Integer.parseInt(userRunningSessionInfo.log_id);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleClaimActivitySelected(DeviceUserStatsUnclaimedLogInfo deviceUserStatsUnclaimedLogInfo) {
        if (this.pendingClaimInfo != null || this.pendingRefresh || deviceUserStatsUnclaimedLogInfo == null) {
            return;
        }
        showDatePicker(deviceUserStatsUnclaimedLogInfo);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleClaimSessionSelected(UserRunningSessionInfo userRunningSessionInfo) {
        if (this.pendingSessionInfo != null || this.pendingRefresh || userRunningSessionInfo == null) {
            return;
        }
        if (userRunningSessionInfo.timestamp.getTime() < 1577836800) {
            showDatePicker(userRunningSessionInfo);
        } else if (TreadlyServiceManager.getInstance().claimUnclaimedSession(userRunningSessionInfo, this.sessionAdapter)) {
            this.pendingRefresh = true;
            this.pendingSessionInfo = userRunningSessionInfo;
            showLoading(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedFragment$4  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 extends TreadlyServiceResponseEventAdapter {
        AnonymousClass4() {
        }

        @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
        public void onSuccess(final String str) {
            super.onSuccess(str);
            ActivityUtil.runOnUiThread(TreadlyActivityUnclaimedFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.-$$Lambda$TreadlyActivityUnclaimedFragment$4$ZI2dRudQ1GGsMhfMBfUvEURwfT0
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyActivityUnclaimedFragment.AnonymousClass4.lambda$onSuccess$0(TreadlyActivityUnclaimedFragment.AnonymousClass4.this, str);
                }
            });
        }

        public static /* synthetic */ void lambda$onSuccess$0(AnonymousClass4 anonymousClass4, String str) {
            if (str == null) {
                if (TreadlyActivityUnclaimedFragment.this.pendingSessionInfo != null) {
                    boolean z = false;
                    Iterator<UserRunningSessionInfo> it = TreadlyActivityUnclaimedFragment.this.unclaimedSessionInfos.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        UserRunningSessionInfo next = it.next();
                        if (next.log_id.equals(TreadlyActivityUnclaimedFragment.this.pendingSessionInfo.log_id)) {
                            TreadlyActivityUnclaimedFragment.this.unclaimedSessionInfos.remove(next);
                            z = true;
                            break;
                        }
                    }
                    if (z) {
                        TreadlyActivityUnclaimedFragment.this.processUnclaimedSessions(TreadlyActivityUnclaimedFragment.this.unclaimedSessionInfos);
                    }
                }
            } else {
                TreadlyActivityUnclaimedFragment.this.showBaseAlert("Claim Activity", "The unclaimed activity has been claimed by another user");
            }
            TreadlyActivityUnclaimedFragment.this.pendingSessionInfo = null;
            TreadlyActivityUnclaimedFragment.this.fetchUnclaimedSessions();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handleClaimAllSessions() {
        if (this.deviceAddress != null && TreadlyServiceManager.getInstance().claimAllUnclaimedSessions(this.deviceAddress, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedFragment.5
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onSuccess(String str) {
                if (str == null) {
                    TreadlyActivityUnclaimedFragment.this.refresh();
                } else {
                    TreadlyActivityUnclaimedFragment.this.showBaseAlert("Claim All sessions", "Unable to claim all activities");
                }
            }
        })) {
            this.pendingRefresh = true;
            showLoading(false);
        }
    }

    protected void handleDeleteSessionSelected(UserRunningSessionInfo userRunningSessionInfo) {
        if (userRunningSessionInfo != null && TreadlyServiceManager.getInstance().deleteUnclaimedSession(userRunningSessionInfo, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedFragment.6
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onSuccess(String str) {
                if (str == null) {
                    TreadlyActivityUnclaimedFragment.this.refresh();
                } else {
                    TreadlyActivityUnclaimedFragment.this.showBaseAlert("Delete Unclaimed Session", "Unable to delete session");
                }
            }
        })) {
            this.pendingRefresh = true;
            this.pendingSessionInfo = userRunningSessionInfo;
            showLoading(false);
        }
    }

    private void claimActivity(DeviceUserStatsUnclaimedLogInfo deviceUserStatsUnclaimedLogInfo, Date date) {
        if (this.pendingClaimInfo != null || this.pendingRefresh || deviceUserStatsUnclaimedLogInfo == null || date == null || !TreadlyClientLib.shared.claimUnclaimedUsersStatsLogInfo(date, deviceUserStatsUnclaimedLogInfo.logId)) {
            return;
        }
        this.pendingRefresh = true;
        this.pendingClaimInfo = deviceUserStatsUnclaimedLogInfo;
        showLoading(false);
    }
}
