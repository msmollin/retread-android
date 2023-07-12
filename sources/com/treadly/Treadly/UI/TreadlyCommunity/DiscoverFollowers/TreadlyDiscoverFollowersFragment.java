package com.treadly.Treadly.UI.TreadlyCommunity.DiscoverFollowers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.Data.Model.UserDiscoverInfo;
import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyCommunity.DiscoverFollowers.TreadlyDiscoverFollowersAdapter;
import com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileUserView;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class TreadlyDiscoverFollowersFragment extends BaseFragment implements TreadlyDiscoverFollowersAdapter.ItemClickListener {
    public static final String TAG = "TREADLY_DISCOVER_FOLLOWERS_FRAGMENT";
    private TreadlyDiscoverFollowersAdapter adapter;
    private RecyclerView discoveredFollowersListView;
    private List<UserDiscoverInfo> discoveredUsers = new ArrayList();

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_discover_followers, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        ((TextView) view.findViewById(R.id.nav_title)).setText(R.string.discover_followers_title);
        ((ImageButton) view.findViewById(R.id.nav_back_arrow)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyCommunity.DiscoverFollowers.-$$Lambda$TreadlyDiscoverFollowersFragment$pRitbVjtZVL33IkshcXn_NN0HLU
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyDiscoverFollowersFragment.this.popBackStack();
            }
        });
        discoverFollowers();
        this.discoveredFollowersListView = (RecyclerView) view.findViewById(R.id.discover_followers_list);
        this.discoveredFollowersListView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.adapter = new TreadlyDiscoverFollowersAdapter(getContext(), this.discoveredUsers);
        this.adapter.setClickListener(this);
        this.discoveredFollowersListView.setAdapter(this.adapter);
        this.adapter.notifyDataSetChanged();
        hideBottomNavigation();
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        if (getActivity() != null) {
            ((MainActivity) getActivity()).toConnectNavBar();
        }
    }

    @Override // com.treadly.Treadly.UI.TreadlyCommunity.DiscoverFollowers.TreadlyDiscoverFollowersAdapter.ItemClickListener
    public void onAvatarClick(View view, int i) {
        TreadlyProfileUserView treadlyProfileUserView = new TreadlyProfileUserView();
        treadlyProfileUserView.userId = this.discoveredUsers.get(i).id;
        treadlyProfileUserView.userName = this.discoveredUsers.get(i).name;
        treadlyProfileUserView.userAvatar = this.discoveredUsers.get(i).avatarPath;
        treadlyProfileUserView.isCurrentUser = false;
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(TAG).add(R.id.activity_fragment_container_empty, treadlyProfileUserView, TreadlyProfileUserView.TAG).commit();
        }
    }

    @Override // com.treadly.Treadly.UI.TreadlyCommunity.DiscoverFollowers.TreadlyDiscoverFollowersAdapter.ItemClickListener
    public void onFollowClick(View view, int i, final Button button) {
        try {
            UserDiscoverInfo userDiscoverInfo = this.discoveredUsers.get(i);
            if (button.getText().equals(getString(R.string.follow_button_text))) {
                showLoading();
                TreadlyServiceManager.getInstance().addFollowRequest(userDiscoverInfo.id, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyCommunity.DiscoverFollowers.TreadlyDiscoverFollowersFragment.1
                    @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                    public void onFollowResponse(String str) {
                        if (str != null) {
                            PrintStream printStream = System.out;
                            printStream.println("BTM :: ERROR FOLLOWING USER: " + str);
                        } else {
                            button.setText(TreadlyDiscoverFollowersFragment.this.getString(R.string.unfollow_button_text));
                        }
                        TreadlyDiscoverFollowersFragment.this.dismissLoading();
                    }
                });
            } else if (button.getText().equals(getString(R.string.unfollow_button_text))) {
                showLoading();
                TreadlyServiceManager.getInstance().unfollowUser(userDiscoverInfo.id, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyCommunity.DiscoverFollowers.TreadlyDiscoverFollowersFragment.2
                    @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                    public void onUnfollowResponse(String str) {
                        if (str != null) {
                            PrintStream printStream = System.out;
                            printStream.println("BTM :: ERROR UNFOLLOWING USER: " + str);
                        } else {
                            button.setText(TreadlyDiscoverFollowersFragment.this.getString(R.string.follow_button_text));
                        }
                        TreadlyDiscoverFollowersFragment.this.dismissLoading();
                    }
                });
            } else {
                System.out.println("BTM :: INVALID FOLLOW BUTTON STATE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void discoverFollowers() {
        showLoading();
        TreadlyServiceManager.getInstance().discoverFollowers(new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyCommunity.DiscoverFollowers.TreadlyDiscoverFollowersFragment.3
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onDiscoverFollowersResponse(String str, List<UserDiscoverInfo> list) {
                if (str == null && list != null) {
                    TreadlyDiscoverFollowersFragment.this.discoveredUsers.addAll(list);
                    TreadlyDiscoverFollowersFragment.this.adapter.notifyDataSetChanged();
                } else {
                    PrintStream printStream = System.out;
                    printStream.println("BTM :: ERROR DISCOVERING USERS: " + str);
                }
                TreadlyDiscoverFollowersFragment.this.dismissLoading();
            }
        });
    }
}
