package com.treadly.Treadly.UI.TreadlyActivity.ActivityList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.Data.Model.UserProfileInfo;
import com.treadly.Treadly.Data.Model.UserStatsInfo;
import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyActivity.Activity.TreadlyActivityFragment;
import com.treadly.Treadly.UI.TreadlyActivity.ActivityList.TreadlyActivityListAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class TreadlyActivityListFragment extends Fragment {
    public static final String TAG = "TREADLY_ACTIVITY_LIST";
    public List<UserStatsInfo> activityInfoList = new ArrayList();
    public RecyclerView activityListView;
    public Date activityStartDate;
    public Date activityTargetDate;
    private TreadlyActivityListAdapter adapter;
    private ImageButton backArrow;
    public UserStatsInfo currentActivityInfo;
    public UserProfileInfo currentUserProfile;

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.setOnBackPressedListener(null);
        }
        return layoutInflater.inflate(R.layout.fragment_treadly_activity_list, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        this.backArrow = (ImageButton) view.findViewById(R.id.activity_list_back_arrow);
        this.backArrow.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyActivity.ActivityList.TreadlyActivityListFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (TreadlyActivityListFragment.this.getActivity() != null) {
                    TreadlyActivityListFragment.this.getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });
        this.activityListView = (RecyclerView) view.findViewById(R.id.activity_list_view);
        this.activityListView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.activityListView.setHasFixedSize(true);
        this.activityListView.setNestedScrollingEnabled(false);
        getData();
        this.adapter = new TreadlyActivityListAdapter(getContext(), this.activityInfoList);
        this.adapter.setClickListener(new TreadlyActivityListAdapter.ItemClickListener() { // from class: com.treadly.Treadly.UI.TreadlyActivity.ActivityList.TreadlyActivityListFragment.2
            @Override // com.treadly.Treadly.UI.TreadlyActivity.ActivityList.TreadlyActivityListAdapter.ItemClickListener
            public void onItemClick(View view2, int i) {
                TreadlyActivityFragment treadlyActivityFragment = new TreadlyActivityFragment();
                treadlyActivityFragment.activityInfoList = TreadlyActivityListFragment.this.activityInfoList;
                treadlyActivityFragment.currentActivityInfo = TreadlyActivityListFragment.this.activityInfoList.get(i);
                treadlyActivityFragment.currentUserProfile = TreadlyActivityListFragment.this.currentUserProfile;
                treadlyActivityFragment.currentUserInfo = TreadlyServiceManager.getInstance().getUserInfo();
                treadlyActivityFragment.fromActivityList = true;
                treadlyActivityFragment.fromConnectCalendar = false;
                if (TreadlyActivityListFragment.this.getActivity() != null) {
                    TreadlyActivityListFragment.this.getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity_fragment_container_empty, treadlyActivityFragment).addToBackStack(TreadlyActivityListFragment.TAG).commit();
                }
            }
        });
        this.activityListView.setAdapter(this.adapter);
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        if (getActivity() != null) {
            ((MainActivity) getActivity()).hideBottomNavigationView();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        if (getActivity() != null) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.setOnBackPressedListener(null);
            mainActivity.showBottomNavigationView();
        }
    }

    private void getUserProfile() {
        String userId = TreadlyServiceManager.getInstance().getUserId();
        if (userId == null) {
            return;
        }
        TreadlyServiceManager.getInstance().getUserProfileInfo(userId, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyActivity.ActivityList.TreadlyActivityListFragment.3
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onUserProfile(String str, UserProfileInfo userProfileInfo) {
                if (str == null) {
                    TreadlyActivityListFragment.this.currentUserProfile = userProfileInfo;
                }
            }
        });
    }

    private void getData() {
        final String userId = TreadlyServiceManager.getInstance().getUserId();
        if (userId == null) {
            return;
        }
        TreadlyServiceManager.getInstance().getUserProfileInfo(userId, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyActivity.ActivityList.TreadlyActivityListFragment.4
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onUserProfile(String str, UserProfileInfo userProfileInfo) {
                if (str == null) {
                    TreadlyActivityListFragment.this.currentUserProfile = userProfileInfo;
                    TreadlyActivityListFragment.this.adapter.currentProfile = userProfileInfo;
                    TreadlyServiceManager.getInstance().getDeviceUserStatsInfo(userId, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyActivity.ActivityList.TreadlyActivityListFragment.4.1
                        @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                        public void onUserStatsInfo(String str2, ArrayList<UserStatsInfo> arrayList) {
                            if (str2 != null || arrayList == null) {
                                return;
                            }
                            TreadlyActivityListFragment.this.activityInfoList.clear();
                            Iterator<UserStatsInfo> it = arrayList.iterator();
                            while (it.hasNext()) {
                                UserStatsInfo next = it.next();
                                if (next.timestamp != null) {
                                    TreadlyActivityListFragment.this.activityInfoList.add(next);
                                }
                            }
                            Collections.sort(TreadlyActivityListFragment.this.activityInfoList);
                            TreadlyActivityListFragment.this.reloadData();
                        }
                    });
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reloadData() {
        if (this.adapter != null) {
            this.adapter.notifyDataSetChanged();
        }
    }
}
