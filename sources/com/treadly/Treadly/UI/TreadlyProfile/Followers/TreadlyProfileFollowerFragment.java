package com.treadly.Treadly.UI.TreadlyProfile.Followers;

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
import com.treadly.Treadly.Data.Model.FollowInfo;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyProfile.Followers.TreadlyProfileFollowAdapter;
import com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileUserView;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class TreadlyProfileFollowerFragment extends BaseFragment implements TreadlyProfileFollowAdapter.ItemClickListener {
    public static final String TAG = "TREADLY_USER_FOLLOWERS_FRAGMENT";
    private TreadlyProfileFollowAdapter followerAdapter;
    private List<FollowInfo> followers = new ArrayList();
    private List<FollowInfo> following = new ArrayList();
    private TreadlyProfileFollowAdapter followingAdapter;
    private RecyclerView userFollowersListView;
    private RecyclerView userFollowingListView;

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_user_profile_follow, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        TextView textView = (TextView) view.findViewById(R.id.nav_title);
        ((ImageButton) view.findViewById(R.id.nav_back_arrow)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Followers.-$$Lambda$TreadlyProfileFollowerFragment$AQWPdBXG6yS8A8HRkN-ex4rcc0I
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileFollowerFragment.this.popBackStack();
            }
        });
        getFollowersAndFollowing();
        this.userFollowersListView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.followerAdapter = new TreadlyProfileFollowAdapter(getContext(), this.followers);
        this.followerAdapter.setClickListener(this);
        this.userFollowersListView.setAdapter(this.followerAdapter);
        this.followerAdapter.notifyDataSetChanged();
        this.userFollowingListView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.followingAdapter = new TreadlyProfileFollowAdapter(getContext(), this.following);
        this.followingAdapter.setClickListener(this);
        this.userFollowingListView.setAdapter(this.followingAdapter);
        this.followingAdapter.notifyDataSetChanged();
        hideBottomNavigation();
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        if (getActivity() != null) {
            ((MainActivity) getActivity()).toConnectNavBar();
        }
    }

    @Override // com.treadly.Treadly.UI.TreadlyProfile.Followers.TreadlyProfileFollowAdapter.ItemClickListener
    public void onAvatarClick(View view, int i, boolean z) {
        UserInfo userInfo = (z ? this.followers : this.following).get(i).userInfo;
        TreadlyProfileUserView treadlyProfileUserView = new TreadlyProfileUserView();
        treadlyProfileUserView.userId = userInfo.id;
        treadlyProfileUserView.userName = userInfo.name;
        treadlyProfileUserView.userAvatar = userInfo.avatarPath;
        treadlyProfileUserView.isCurrentUser = false;
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(TAG).add(R.id.activity_fragment_container_empty, treadlyProfileUserView, TreadlyProfileUserView.TAG).commit();
        }
    }

    @Override // com.treadly.Treadly.UI.TreadlyProfile.Followers.TreadlyProfileFollowAdapter.ItemClickListener
    public void onFollowClick(View view, int i, boolean z, final Button button) {
        try {
            FollowInfo followInfo = (z ? this.followers : this.following).get(i);
            if (button.getText().equals(getString(R.string.follow_button_text))) {
                showLoading();
                TreadlyServiceManager.getInstance().addFollowRequest(followInfo.userInfo.id, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Followers.TreadlyProfileFollowerFragment.1
                    @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                    public void onFollowResponse(String str) {
                        if (str != null) {
                            PrintStream printStream = System.out;
                            printStream.println("BTM :: ERROR FOLLOWING USER: " + str);
                        } else {
                            button.setText(TreadlyProfileFollowerFragment.this.getString(R.string.unfollow_button_text));
                        }
                        TreadlyProfileFollowerFragment.this.dismissLoading();
                    }
                });
            } else if (button.getText().equals(getString(R.string.unfollow_button_text))) {
                showLoading();
                TreadlyServiceManager.getInstance().unfollowUser(followInfo.userInfo.id, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Followers.TreadlyProfileFollowerFragment.2
                    @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                    public void onUnfollowResponse(String str) {
                        if (str != null) {
                            PrintStream printStream = System.out;
                            printStream.println("BTM :: ERROR UNFOLLOWING USER: " + str);
                        } else {
                            button.setText(TreadlyProfileFollowerFragment.this.getString(R.string.follow_button_text));
                        }
                        TreadlyProfileFollowerFragment.this.dismissLoading();
                    }
                });
            } else {
                System.out.println("BTM :: INVALID FOLLOW BUTTON STATE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getFollowersAndFollowing() {
        showLoading();
    }
}
