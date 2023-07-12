package com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
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
import com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedListAdapter;
import com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedListView;
import com.treadly.Treadly.UI.Util.ActivityUtil;
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
public class TreadlyActivityUnclaimedListView {
    PopupWindow blurBackgroundPopup;
    View blurView;
    Button claimAllButton;
    Button claimButton;
    Context context;
    DatePicker datePicker;
    ConstraintLayout datePickerHeader;
    PopupWindow datePickerPopup;
    View datePickerView;
    ImageButton deleteActivityButton;
    TextView descriptionPara;
    public String deviceAddress;
    DeviceUserStatsLogEventListener deviceUserStatsLogEventAdapter;
    boolean isSessionInfo;
    DeviceUserStatsUnclaimedLogInfo pendingClaimInfo;
    boolean pendingRefresh;
    UserRunningSessionInfo pendingSessionInfo;
    ImageButton popupExitButton;
    TextView popupTitle;
    View popupView;
    PopupWindow popupWindow;
    private final TreadlyServiceResponseEventListener sessionAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    TimePicker timePicker;
    List<DeviceUserStatsUnclaimedLogInfo> unclaimedActivities;
    TreadlyActivityUnclaimedListAdapter unclaimedAdapter;
    RecyclerView unclaimedRecycler;
    List<UserRunningSessionInfo> unclaimedSessionInfos;
    TextView warningText;

    public TreadlyActivityUnclaimedListView(Context context, List<DeviceUserStatsUnclaimedLogInfo> list) {
        this.unclaimedActivities = new ArrayList();
        this.pendingClaimInfo = null;
        this.pendingSessionInfo = null;
        this.pendingRefresh = false;
        this.isSessionInfo = false;
        this.deviceAddress = null;
        this.deviceUserStatsLogEventAdapter = new DeviceUserStatsLogEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedListView.8
            @Override // com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventAdapter, com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventListener
            public void onDeviceUserStatsUnclaimedUserStatsInfo(final DeviceUserStatsUnclaimedLogInfo[] deviceUserStatsUnclaimedLogInfoArr) {
                TreadlyActivityUnclaimedListView.this.pendingRefresh = false;
                TreadlyActivityUnclaimedListView.this.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedListView.8.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (deviceUserStatsUnclaimedLogInfoArr == null) {
                            return;
                        }
                        TreadlyActivityUnclaimedListView.this.processUnclaimedActivities(new ArrayList(Arrays.asList(deviceUserStatsUnclaimedLogInfoArr)));
                    }
                });
            }

            @Override // com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventAdapter, com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventListener
            public void onDeviceUserStatsClaimUnclaimedUserStatsInfoResponse(boolean z) {
                TreadlyActivityUnclaimedListView.this.handleClaimActivityResponse(z);
            }
        };
        this.sessionAdapter = new AnonymousClass16();
        this.context = context;
        this.unclaimedActivities = list;
        TreadlyClientLib.shared.addDeviceUserStatsLogEventListener(this.deviceUserStatsLogEventAdapter);
        initializeWindow();
    }

    public TreadlyActivityUnclaimedListView(Context context, List<UserRunningSessionInfo> list, @Nullable boolean z) {
        this.unclaimedActivities = new ArrayList();
        this.pendingClaimInfo = null;
        this.pendingSessionInfo = null;
        this.pendingRefresh = false;
        this.isSessionInfo = false;
        this.deviceAddress = null;
        this.deviceUserStatsLogEventAdapter = new DeviceUserStatsLogEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedListView.8
            @Override // com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventAdapter, com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventListener
            public void onDeviceUserStatsUnclaimedUserStatsInfo(final DeviceUserStatsUnclaimedLogInfo[] deviceUserStatsUnclaimedLogInfoArr) {
                TreadlyActivityUnclaimedListView.this.pendingRefresh = false;
                TreadlyActivityUnclaimedListView.this.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedListView.8.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (deviceUserStatsUnclaimedLogInfoArr == null) {
                            return;
                        }
                        TreadlyActivityUnclaimedListView.this.processUnclaimedActivities(new ArrayList(Arrays.asList(deviceUserStatsUnclaimedLogInfoArr)));
                    }
                });
            }

            @Override // com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventAdapter, com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventListener
            public void onDeviceUserStatsClaimUnclaimedUserStatsInfoResponse(boolean z2) {
                TreadlyActivityUnclaimedListView.this.handleClaimActivityResponse(z2);
            }
        };
        this.sessionAdapter = new AnonymousClass16();
        this.context = context;
        this.unclaimedSessionInfos = list;
        this.isSessionInfo = true;
        initializeWindow();
    }

    private void initializeWindow() {
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService("layout_inflater");
        this.popupView = layoutInflater.inflate(R.layout.unclaimed_activity_list, (ViewGroup) null);
        this.popupTitle = (TextView) this.popupView.findViewById(R.id.unclaimed_popup_title);
        this.popupExitButton = (ImageButton) this.popupView.findViewById(R.id.unclaimed_popup_x);
        this.popupExitButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedListView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                TreadlyActivityUnclaimedListView.this.popupWindow.dismiss();
            }
        });
        this.descriptionPara = (TextView) this.popupView.findViewById(R.id.unclaimed_popup_paragraph);
        this.claimAllButton = (Button) this.popupView.findViewById(R.id.unclaimed_popup_claim_all_button);
        this.claimAllButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.-$$Lambda$TreadlyActivityUnclaimedListView$wDqYU9p1T0AoX5q2XKsTg3aJzPE
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TreadlyActivityUnclaimedListView.this.handleClaimAllSessions();
            }
        });
        initList();
        this.blurView = layoutInflater.inflate(R.layout.blur_popup, (ViewGroup) null);
        this.blurView.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedListView.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                TreadlyActivityUnclaimedListView.this.popupWindow.dismiss();
            }
        });
        this.blurBackgroundPopup = new PopupWindow(this.blurView, -1, -1);
        this.popupWindow = new PopupWindow(this.popupView, (int) this.context.getResources().getDimension(R.dimen.unclaimed_list_width), (int) this.context.getResources().getDimension(R.dimen.unclaimed_list_height));
        this.popupWindow.setTouchable(true);
        this.popupWindow.setFocusable(true);
        this.popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(this.context, R.drawable.unclaimed_list_background));
        this.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedListView.3
            @Override // android.widget.PopupWindow.OnDismissListener
            public void onDismiss() {
                TreadlyActivityUnclaimedListView.this.handleDismiss();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDismiss() {
        TreadlyClientLib.shared.removeDeviceUserStatsLogEventListener(this.deviceUserStatsLogEventAdapter);
        if (this.blurBackgroundPopup != null) {
            this.blurBackgroundPopup.dismiss();
        }
        if (this.datePickerPopup != null) {
            this.datePickerPopup.dismiss();
        }
    }

    private void initList() {
        boolean z = this.isSessionInfo;
        int i = R.string.unclaimed_popup_para;
        if (z) {
            this.unclaimedSessionInfos.sort(new Comparator() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.-$$Lambda$TreadlyActivityUnclaimedListView$yWTyz3eV0pIj7YGREnjdhf2DUhY
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    return TreadlyActivityUnclaimedListView.lambda$initList$1((UserRunningSessionInfo) obj, (UserRunningSessionInfo) obj2);
                }
            });
            TextView textView = this.descriptionPara;
            if (this.unclaimedSessionInfos.size() == 0) {
                i = R.string.unclaimed_popup_para_no_activities;
            }
            textView.setText(i);
            this.claimAllButton.setVisibility(this.unclaimedSessionInfos.size() != 0 ? 0 : 8);
        } else {
            this.unclaimedActivities.sort(new Comparator<DeviceUserStatsUnclaimedLogInfo>() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedListView.4
                @Override // java.util.Comparator
                public int compare(DeviceUserStatsUnclaimedLogInfo deviceUserStatsUnclaimedLogInfo, DeviceUserStatsUnclaimedLogInfo deviceUserStatsUnclaimedLogInfo2) {
                    return deviceUserStatsUnclaimedLogInfo.sequence < deviceUserStatsUnclaimedLogInfo2.sequence ? 1 : -1;
                }
            });
            TextView textView2 = this.descriptionPara;
            if (this.unclaimedActivities.size() == 0) {
                i = R.string.unclaimed_popup_para_no_activities;
            }
            textView2.setText(i);
            this.claimAllButton.setVisibility(8);
        }
        this.unclaimedRecycler = (RecyclerView) this.popupView.findViewById(R.id.unclaimed_popup_recycler_list);
        if (this.isSessionInfo) {
            this.unclaimedAdapter = new TreadlyActivityUnclaimedListAdapter(this.context, this.unclaimedSessionInfos, this.isSessionInfo);
        } else {
            this.unclaimedAdapter = new TreadlyActivityUnclaimedListAdapter(this.context, this.unclaimedActivities);
        }
        this.unclaimedAdapter.setClickListener(new TreadlyActivityUnclaimedListAdapter.ItemClickListener() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedListView.5
            @Override // com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedListAdapter.ItemClickListener
            public void onItemClick(View view, int i2) {
                if (TreadlyActivityUnclaimedListView.this.isSessionInfo) {
                    TreadlyActivityUnclaimedListView.this.handleClaimSessionSelected(TreadlyActivityUnclaimedListView.this.unclaimedSessionInfos.get(i2));
                } else {
                    TreadlyActivityUnclaimedListView.this.handleClaimActivitySelected(TreadlyActivityUnclaimedListView.this.unclaimedActivities.get(i2));
                }
            }

            @Override // com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedListAdapter.ItemClickListener
            public void onItemDelete(View view, int i2) {
                if (TreadlyActivityUnclaimedListView.this.isSessionInfo) {
                    TreadlyActivityUnclaimedListView.this.handleDeleteSessionSelected(TreadlyActivityUnclaimedListView.this.unclaimedSessionInfos.get(i2));
                }
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context);
        linearLayoutManager.setOrientation(1);
        this.unclaimedRecycler.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this.unclaimedRecycler.getContext(), linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this.context, R.drawable.unclaimed_list_divider));
        this.unclaimedRecycler.addItemDecoration(dividerItemDecoration);
        this.unclaimedRecycler.setAdapter(this.unclaimedAdapter);
        this.swipeRefreshLayout = (SwipeRefreshLayout) this.popupView.findViewById(R.id.unclaimed_popup_swipe_refresh_layout);
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedListView.6
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public void onRefresh() {
                TreadlyActivityUnclaimedListView.this.refresh();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$initList$1(UserRunningSessionInfo userRunningSessionInfo, UserRunningSessionInfo userRunningSessionInfo2) {
        if (Integer.parseInt(userRunningSessionInfo.log_id) < Integer.parseInt(userRunningSessionInfo2.log_id)) {
            return 1;
        }
        return userRunningSessionInfo.log_id.equals(userRunningSessionInfo2.log_id) ? 0 : -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refresh() {
        if (this.isSessionInfo) {
            fetchUnclaimedSessions();
        } else {
            fetchUnclaimedActivities();
        }
    }

    public void showListPopup() {
        this.blurBackgroundPopup.showAsDropDown(this.blurView);
        this.popupWindow.showAtLocation(this.popupView, 48, 0, (int) this.context.getResources().getDimension(R.dimen._60ssp));
    }

    public void dismissPopups() {
        if (this.datePickerPopup != null) {
            this.datePickerPopup.dismiss();
        }
        if (this.popupWindow != null) {
            this.popupWindow.dismiss();
        }
    }

    private void showDatePicker(final DeviceUserStatsUnclaimedLogInfo deviceUserStatsUnclaimedLogInfo) {
        initDatePicker();
        this.claimButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedListView.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int dayOfMonth = TreadlyActivityUnclaimedListView.this.datePicker.getDayOfMonth();
                calendar.set(TreadlyActivityUnclaimedListView.this.datePicker.getYear(), TreadlyActivityUnclaimedListView.this.datePicker.getMonth(), dayOfMonth, 0, 0, 0);
                TreadlyActivityUnclaimedListView.this.claimActivity(deviceUserStatsUnclaimedLogInfo, TreadlyActivityUnclaimedListView.this.addTimeToDate(calendar.getTime()));
                TreadlyActivityUnclaimedListView.this.datePickerPopup.dismiss();
            }
        });
        displayDatePicker();
    }

    private void showDatePicker(final UserRunningSessionInfo userRunningSessionInfo) {
        initDatePicker();
        this.claimButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.-$$Lambda$TreadlyActivityUnclaimedListView$HtEIhg5XxVYddphCUvjPe9HHa4Q
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TreadlyActivityUnclaimedListView.lambda$showDatePicker$2(TreadlyActivityUnclaimedListView.this, userRunningSessionInfo, view);
            }
        });
        displayDatePicker();
    }

    public static /* synthetic */ void lambda$showDatePicker$2(TreadlyActivityUnclaimedListView treadlyActivityUnclaimedListView, UserRunningSessionInfo userRunningSessionInfo, View view) {
        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = treadlyActivityUnclaimedListView.datePicker.getDayOfMonth();
        calendar.set(treadlyActivityUnclaimedListView.datePicker.getYear(), treadlyActivityUnclaimedListView.datePicker.getMonth(), dayOfMonth, 0, 0, 0);
        userRunningSessionInfo.timestamp = treadlyActivityUnclaimedListView.addTimeToDate(calendar.getTime());
        treadlyActivityUnclaimedListView.handleClaimSessionSelected(userRunningSessionInfo);
        treadlyActivityUnclaimedListView.datePickerPopup.dismiss();
    }

    private void initDatePicker() {
        this.datePickerView = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.unclaimed_date_picker, (ViewGroup) null);
        this.datePickerView.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.-$$Lambda$TreadlyActivityUnclaimedListView$H3AiL_MYa_RwAk8PGVIjoEFmYv0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TreadlyActivityUnclaimedListView.this.datePickerPopup.dismiss();
            }
        });
        this.datePickerHeader = (ConstraintLayout) this.datePickerView.findViewById(R.id.unclaimed_date_picker_title_holder);
        this.datePickerHeader.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.-$$Lambda$TreadlyActivityUnclaimedListView$1nNb-n_UuUVRNeYkP_iFJygqJxU
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TreadlyActivityUnclaimedListView.this.datePickerPopup.dismiss();
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
        Activity activity = (Activity) this.context;
        if (activity == null) {
            this.datePickerPopup.showAsDropDown(this.datePickerView);
            return;
        }
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        this.datePickerPopup.showAtLocation(this.datePickerView, 80, 0, activity.getWindow().getDecorView().getHeight() - rect.bottom);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Date addTimeToDate(Date date) {
        int hour = this.timePicker.getHour();
        return new Date(date.getTime() + (hour * 3600000) + (this.timePicker.getMinute() * 60000));
    }

    void runOnMain(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleClaimActivityResponse(final boolean z) {
        runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedListView.9
            @Override // java.lang.Runnable
            public void run() {
                TreadlyActivityUnclaimedListView.this.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedListView.9.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (z) {
                            if (TreadlyActivityUnclaimedListView.this.pendingClaimInfo != null) {
                                boolean z2 = false;
                                Iterator<DeviceUserStatsUnclaimedLogInfo> it = TreadlyActivityUnclaimedListView.this.unclaimedActivities.iterator();
                                while (true) {
                                    if (!it.hasNext()) {
                                        break;
                                    }
                                    DeviceUserStatsUnclaimedLogInfo next = it.next();
                                    if (next.logId == TreadlyActivityUnclaimedListView.this.pendingClaimInfo.logId) {
                                        TreadlyActivityUnclaimedListView.this.unclaimedActivities.remove(next);
                                        z2 = true;
                                        break;
                                    }
                                }
                                if (z2) {
                                    TreadlyActivityUnclaimedListView.this.processUnclaimedActivities(TreadlyActivityUnclaimedListView.this.unclaimedActivities);
                                }
                            }
                        } else {
                            TreadlyActivityUnclaimedListView.this.showInfoAlert((MainActivity) TreadlyActivityUnclaimedListView.this.context, "Claim Activity", "The unclaimed activity has been claimed by another user");
                        }
                        TreadlyActivityUnclaimedListView.this.pendingClaimInfo = null;
                        TreadlyActivityUnclaimedListView.this.fetchUnclaimedActivities();
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handleClaimAllSessions() {
        if (this.deviceAddress != null && TreadlyServiceManager.getInstance().claimAllUnclaimedSessions(this.deviceAddress, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedListView.10
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onSuccess(String str) {
                if (str == null) {
                    TreadlyActivityUnclaimedListView.this.refresh();
                    return;
                }
                TreadlyActivityUnclaimedListView.this.showInfoAlert((MainActivity) TreadlyActivityUnclaimedListView.this.context, "Claim All Sessions", "Unable to claim all activities");
            }
        })) {
            this.pendingRefresh = true;
            MainActivity mainActivity = (MainActivity) this.context;
            if (mainActivity != null) {
                mainActivity.showLoadingDialog(false);
            }
        }
    }

    protected void handleDeleteSessionSelected(UserRunningSessionInfo userRunningSessionInfo) {
        if (userRunningSessionInfo != null && TreadlyServiceManager.getInstance().deleteUnclaimedSession(userRunningSessionInfo, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedListView.11
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onSuccess(String str) {
                if (str == null) {
                    TreadlyActivityUnclaimedListView.this.refresh();
                    return;
                }
                TreadlyActivityUnclaimedListView.this.showInfoAlert((MainActivity) TreadlyActivityUnclaimedListView.this.context, "Delete Unclaimed Session", "Unable to delete session");
            }
        })) {
            this.pendingRefresh = true;
            this.pendingSessionInfo = userRunningSessionInfo;
            MainActivity mainActivity = (MainActivity) this.context;
            if (mainActivity != null) {
                mainActivity.showLoadingDialog(false);
            }
        }
    }

    protected void showInfoAlert(MainActivity mainActivity, String str, String str2) {
        if (mainActivity == null) {
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setMessage(str2);
        builder.setTitle(str);
        builder.setNeutralButton("Dismiss", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedListView.12
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        ActivityUtil.runOnUiThread(mainActivity, new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedListView.13
            @Override // java.lang.Runnable
            public void run() {
                builder.create().show();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processUnclaimedActivities(List<DeviceUserStatsUnclaimedLogInfo> list) {
        MainActivity mainActivity = (MainActivity) this.context;
        if (mainActivity != null) {
            mainActivity.closeLoadingDialog();
        }
        this.swipeRefreshLayout.setRefreshing(false);
        list.sort(new Comparator<DeviceUserStatsUnclaimedLogInfo>() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedListView.14
            @Override // java.util.Comparator
            public int compare(DeviceUserStatsUnclaimedLogInfo deviceUserStatsUnclaimedLogInfo, DeviceUserStatsUnclaimedLogInfo deviceUserStatsUnclaimedLogInfo2) {
                return deviceUserStatsUnclaimedLogInfo.sequence < deviceUserStatsUnclaimedLogInfo2.sequence ? 1 : -1;
            }
        });
        this.unclaimedActivities = list;
        this.descriptionPara.setText(this.unclaimedActivities.size() == 0 ? R.string.unclaimed_popup_para_no_activities : R.string.unclaimed_popup_para);
        this.unclaimedAdapter.setUnclaimedActivities(list);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processUnclaimedSessions(List<UserRunningSessionInfo> list) {
        MainActivity mainActivity = (MainActivity) this.context;
        if (mainActivity != null) {
            mainActivity.closeLoadingDialog();
        }
        this.swipeRefreshLayout.setRefreshing(false);
        list.sort(new Comparator() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.-$$Lambda$TreadlyActivityUnclaimedListView$mdwvL9O_GE_dWSxO7dIlhWTAduo
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return TreadlyActivityUnclaimedListView.lambda$processUnclaimedSessions$5((UserRunningSessionInfo) obj, (UserRunningSessionInfo) obj2);
            }
        });
        this.unclaimedSessionInfos = list;
        this.descriptionPara.setText(this.unclaimedSessionInfos.size() == 0 ? R.string.unclaimed_popup_para_no_activities : R.string.unclaimed_popup_para);
        this.claimAllButton.setVisibility(this.unclaimedSessionInfos.size() == 0 ? 8 : 0);
        this.unclaimedAdapter.setUnclaimedSessions(this.unclaimedSessionInfos);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$processUnclaimedSessions$5(UserRunningSessionInfo userRunningSessionInfo, UserRunningSessionInfo userRunningSessionInfo2) {
        return Integer.parseInt(userRunningSessionInfo2.log_id) - Integer.parseInt(userRunningSessionInfo.log_id);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fetchUnclaimedActivities() {
        MainActivity mainActivity = (MainActivity) this.context;
        if (TreadlyClientLib.shared.getUnclaimedUserStatsLogInfo()) {
            if (mainActivity != null) {
                mainActivity.showLoadingDialog(false);
                return;
            }
            return;
        }
        if (mainActivity != null) {
            mainActivity.closeLoadingDialog();
        }
        this.swipeRefreshLayout.setRefreshing(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedListView$15  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass15 extends TreadlyServiceResponseEventAdapter {
        AnonymousClass15() {
        }

        @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
        public void onGetUnclaimedLogs(String str, final List<UserRunningSessionInfo> list) {
            super.onGetUnclaimedLogs(str, list);
            TreadlyActivityUnclaimedListView.this.pendingRefresh = false;
            TreadlyActivityUnclaimedListView.this.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.-$$Lambda$TreadlyActivityUnclaimedListView$15$vm1pq6H5MaawiGIT66RV48LBCKs
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyActivityUnclaimedListView.this.processUnclaimedSessions(list);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fetchUnclaimedSessions() {
        MainActivity mainActivity = (MainActivity) this.context;
        if (TreadlyServiceManager.getInstance().getUnclaimedUserStatsLogInfo(this.deviceAddress, new AnonymousClass15())) {
            if (mainActivity != null) {
                mainActivity.showLoadingDialog(false);
                return;
            }
            return;
        }
        if (mainActivity != null) {
            mainActivity.closeLoadingDialog();
        }
        this.swipeRefreshLayout.setRefreshing(false);
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
            MainActivity mainActivity = (MainActivity) this.context;
            if (mainActivity != null) {
                mainActivity.showLoadingDialog(false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedListView$16  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass16 extends TreadlyServiceResponseEventAdapter {
        AnonymousClass16() {
        }

        @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
        public void onSuccess(final String str) {
            super.onSuccess(str);
            TreadlyActivityUnclaimedListView.this.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.-$$Lambda$TreadlyActivityUnclaimedListView$16$iY1lCGPYaW5-8HPwInUCRUVU-Bc
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyActivityUnclaimedListView.AnonymousClass16.lambda$onSuccess$0(TreadlyActivityUnclaimedListView.AnonymousClass16.this, str);
                }
            });
        }

        public static /* synthetic */ void lambda$onSuccess$0(AnonymousClass16 anonymousClass16, String str) {
            if (str == null) {
                if (TreadlyActivityUnclaimedListView.this.pendingSessionInfo != null) {
                    boolean z = false;
                    Iterator<UserRunningSessionInfo> it = TreadlyActivityUnclaimedListView.this.unclaimedSessionInfos.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        UserRunningSessionInfo next = it.next();
                        if (next.log_id.equals(TreadlyActivityUnclaimedListView.this.pendingSessionInfo.log_id)) {
                            TreadlyActivityUnclaimedListView.this.unclaimedSessionInfos.remove(next);
                            z = true;
                            break;
                        }
                    }
                    if (z) {
                        TreadlyActivityUnclaimedListView.this.processUnclaimedSessions(TreadlyActivityUnclaimedListView.this.unclaimedSessionInfos);
                    }
                }
            } else {
                TreadlyActivityUnclaimedListView.this.showInfoAlert((MainActivity) TreadlyActivityUnclaimedListView.this.context, "Claim Activity", "The unclaimed activity has been claimed by another user");
            }
            TreadlyActivityUnclaimedListView.this.pendingSessionInfo = null;
            TreadlyActivityUnclaimedListView.this.fetchUnclaimedSessions();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void claimActivity(DeviceUserStatsUnclaimedLogInfo deviceUserStatsUnclaimedLogInfo, Date date) {
        if (this.pendingClaimInfo != null || this.pendingRefresh || deviceUserStatsUnclaimedLogInfo == null || date == null || !TreadlyClientLib.shared.claimUnclaimedUsersStatsLogInfo(date, deviceUserStatsUnclaimedLogInfo.logId)) {
            return;
        }
        this.pendingRefresh = true;
        this.pendingClaimInfo = deviceUserStatsUnclaimedLogInfo;
        MainActivity mainActivity = (MainActivity) this.context;
        if (mainActivity != null) {
            mainActivity.showLoadingDialog(false);
        }
    }
}
