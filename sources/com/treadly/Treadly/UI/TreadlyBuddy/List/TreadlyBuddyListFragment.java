package com.treadly.Treadly.UI.TreadlyBuddy.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.treadly.Treadly.Data.Model.BuddyProfileInfo;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyBuddy.Info.TreadlyBuddyInfoFragment;
import com.treadly.Treadly.UI.TreadlyBuddy.List.TreadlyBuddyListAdapter;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class TreadlyBuddyListFragment extends BaseFragment implements TreadlyBuddyListAdapter.ItemClickListener {
    public static final String TAG = "TREADLY_BUDDY_LIST_FRAGMENT";
    private TreadlyBuddyListAdapter adapter;
    private RecyclerView buddyListView;
    private List<BuddyProfileInfo> candidateBuddies = new ArrayList();

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_buddy_list, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        requestSuggestedBuddies();
        this.buddyListView = (RecyclerView) view.findViewById(R.id.find_buddy_list);
        this.buddyListView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.adapter = new TreadlyBuddyListAdapter(getContext(), this.candidateBuddies);
        this.adapter.setClickListener(this);
        this.buddyListView.setAdapter(this.adapter);
        this.adapter.notifyDataSetChanged();
    }

    @Override // com.treadly.Treadly.UI.TreadlyBuddy.List.TreadlyBuddyListAdapter.ItemClickListener
    public void onClick(View view, int i) {
        System.out.println("BTM :: Get buddy info");
        TreadlyBuddyInfoFragment treadlyBuddyInfoFragment = new TreadlyBuddyInfoFragment();
        treadlyBuddyInfoFragment.userInfo = this.candidateBuddies.get(i);
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.activity_fragment_container_empty, treadlyBuddyInfoFragment, TreadlyBuddyInfoFragment.TAG).commit();
        }
    }

    private void requestSuggestedBuddies() {
        showLoading();
        VideoServiceHelper.getBuddyProfileInfoAll(new VideoServiceHelper.BuddyListAllListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.List.-$$Lambda$TreadlyBuddyListFragment$fivRdSe9QGip2qqnt_nvY5VnJKM
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.BuddyListAllListener
            public final void onResponse(String str, List list) {
                TreadlyBuddyListFragment.lambda$requestSuggestedBuddies$0(TreadlyBuddyListFragment.this, str, list);
            }
        });
    }

    public static /* synthetic */ void lambda$requestSuggestedBuddies$0(TreadlyBuddyListFragment treadlyBuddyListFragment, String str, List list) {
        treadlyBuddyListFragment.dismissLoading();
        if (str != null || list == null) {
            PrintStream printStream = System.out;
            printStream.println("BTM :: ERROR REQUESTING BUDDIES + " + str);
            return;
        }
        treadlyBuddyListFragment.candidateBuddies.addAll(list);
        treadlyBuddyListFragment.adapter.notifyDataSetChanged();
    }
}
