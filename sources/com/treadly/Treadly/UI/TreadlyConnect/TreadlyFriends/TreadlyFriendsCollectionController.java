package com.treadly.Treadly.UI.TreadlyConnect.TreadlyFriends;

import android.content.Context;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.treadly.Treadly.Data.Delegates.TreadlyServiceDelegate;
import com.treadly.Treadly.Data.Delegates.TreadlyUserEventAdapter;
import com.treadly.Treadly.Data.Delegates.TreadlyUserEventDelegate;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Managers.TreadlyActivationManager;
import com.treadly.Treadly.Data.Managers.TreadlyEventManager;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.Data.Managers.UserActivityHelper;
import com.treadly.Treadly.Data.Model.UserActivityInfo;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.UI.TreadlyConnect.TreadlyFriends.TreadlyFriendsAdapter;
import com.treadly.Treadly.UI.TreadlyVideo.Data.InviteServiceHelper;
import com.treadly.Treadly.UI.TreadlyVideo.Data.InviteServiceInviteInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import org.json.JSONException;

/* loaded from: classes2.dex */
public class TreadlyFriendsCollectionController implements TreadlyServiceDelegate {
    private Context context;
    private TreadlyFriendsAdapter friendAdapter;
    private RecyclerView friendsCollection;
    private FriendsListener friendsListener;
    private List<UserInfo> friendsOnline = new ArrayList();
    private Map<String, UserInfo> friendsList = new HashMap();
    private Map<String, UserActivityInfo> friendsActivityInfo = new HashMap();
    public List<InviteServiceInviteInfo> pendingPrivateVideoInvites = null;
    Timer activityTimer = new Timer();
    private TreadlyUserEventDelegate userDelegate = new TreadlyUserEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyFriends.TreadlyFriendsCollectionController.4
        @Override // com.treadly.Treadly.Data.Delegates.TreadlyUserEventAdapter, com.treadly.Treadly.Data.Delegates.TreadlyUserEventDelegate
        public void onReceiveUserVideoPrivateInviteRequest(UserInfo userInfo) {
            super.onReceiveUserVideoPrivateInviteRequest(userInfo);
            TreadlyFriendsCollectionController.this.updatePendingPrivateVideoInvite();
        }

        @Override // com.treadly.Treadly.Data.Delegates.TreadlyUserEventAdapter, com.treadly.Treadly.Data.Delegates.TreadlyUserEventDelegate
        public void onReceiveUserVideoPrivateInviteDeleted(UserInfo userInfo) {
            super.onReceiveUserVideoPrivateInviteDeleted(userInfo);
            TreadlyFriendsCollectionController.this.updatePendingPrivateVideoInvite();
        }

        @Override // com.treadly.Treadly.Data.Delegates.TreadlyUserEventAdapter, com.treadly.Treadly.Data.Delegates.TreadlyUserEventDelegate
        public void onReceiveUserVideoPrivateInviteDeclined(UserInfo userInfo) {
            super.onReceiveUserVideoPrivateInviteDeclined(userInfo);
            TreadlyFriendsCollectionController.this.updatePendingPrivateVideoInvite();
        }
    };

    private void startActivityTimer() {
    }

    private void stopActivityTimer() {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyServiceDelegate
    public void onCreateFriendInviteToken(String str, String str2) {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyServiceDelegate
    public void onUserLogin(UserInfo userInfo) {
    }

    public TreadlyFriendsCollectionController(Context context, RecyclerView recyclerView, boolean z) {
        this.friendsCollection = recyclerView;
        this.context = context;
        this.friendAdapter = new TreadlyFriendsAdapter(context, this.friendsOnline, this.friendsActivityInfo, z);
        this.friendAdapter.setClickListener(new TreadlyFriendsAdapter.ItemClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyFriends.TreadlyFriendsCollectionController.1
            @Override // com.treadly.Treadly.UI.TreadlyConnect.TreadlyFriends.TreadlyFriendsAdapter.ItemClickListener
            public void onItemClick(View view, int i) {
                if (i < 0) {
                    return;
                }
                TreadlyFriendsCollectionController.this.friendsListener.friendSelected(view, (UserInfo) TreadlyFriendsCollectionController.this.friendsOnline.get(i), TreadlyFriendsCollectionController.this.pendingPrivateVideoInvites);
            }
        });
        this.friendsCollection.setAdapter(this.friendAdapter);
        TreadlyServiceManager.getInstance().addDelegate(this);
        TreadlyEventManager.getInstance().addUserDelegate(this.userDelegate);
    }

    private void updateActivity() throws JSONException {
        if (TreadlyServiceManager.getInstance().getUserId() != null) {
            getFriends();
        } else {
            stopActivityTimer();
        }
    }

    public void getFriends() throws JSONException {
        TreadlyServiceManager.getInstance().getFriendsInfo(new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyFriends.TreadlyFriendsCollectionController.2
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onUserFriendsInfo(String str, List<UserInfo> list) throws JSONException {
                if (TreadlyActivationManager.shared.hasActivatedDevice() && list != null && str == null) {
                    TreadlyFriendsCollectionController.this.updateFriend(list);
                    TreadlyFriendsCollectionController.this.updatePendingPrivateVideoInvite();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFriend(final List<UserInfo> list) throws JSONException {
        ArrayList arrayList = new ArrayList();
        for (UserInfo userInfo : list) {
            arrayList.add(userInfo.id);
        }
        UserActivityHelper.get(arrayList, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyFriends.TreadlyFriendsCollectionController.3
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onActivitiesInfo(String str, List<UserActivityInfo> list2) throws JSONException {
                if (list2 == null || !TreadlyActivationManager.shared.hasActivatedDevice()) {
                    return;
                }
                TreadlyFriendsCollectionController.this.friendsOnline.clear();
                TreadlyFriendsCollectionController.this.friendsActivityInfo.clear();
                TreadlyFriendsCollectionController.this.friendsList.clear();
                for (UserInfo userInfo2 : list) {
                    TreadlyFriendsCollectionController.this.friendsList.put(userInfo2.id, userInfo2);
                }
                for (UserActivityInfo userActivityInfo : list2) {
                    TreadlyFriendsCollectionController.this.friendsActivityInfo.put(userActivityInfo.id, userActivityInfo);
                    if (userActivityInfo.isOnline && TreadlyFriendsCollectionController.this.friendsList.containsKey(userActivityInfo.id)) {
                        TreadlyFriendsCollectionController.this.friendsOnline.add(TreadlyFriendsCollectionController.this.friendsList.get(userActivityInfo.id));
                    }
                }
                TreadlyFriendsCollectionController.this.friendsListener.friendsUpdated(TreadlyFriendsCollectionController.this.friendsActivityInfo, TreadlyFriendsCollectionController.this.friendsOnline, TreadlyFriendsCollectionController.this.pendingPrivateVideoInvites);
                TreadlyFriendsCollectionController.this.friendsListener.friendsChanged(TreadlyFriendsCollectionController.this.friendsOnline.size(), list.size());
                TreadlyFriendsCollectionController.this.friendAdapter.setData(TreadlyFriendsCollectionController.this.friendsOnline, TreadlyFriendsCollectionController.this.friendsActivityInfo);
                TreadlyFriendsCollectionController.this.friendAdapter.notifyDataSetChanged();
            }
        });
    }

    public void resetFriends() {
        this.friendsList.clear();
        this.friendsActivityInfo.clear();
        this.friendsOnline.clear();
        this.friendAdapter.notifyDataSetChanged();
        if (this.friendsListener != null) {
            this.friendsListener.friendsChanged(this.friendsOnline.size(), this.friendsList.size());
        }
    }

    public void setFriendsListener(FriendsListener friendsListener) {
        this.friendsListener = friendsListener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePendingPrivateVideoInvite() {
        String userId = TreadlyServiceManager.getInstance().getUserId();
        if (userId == null) {
            return;
        }
        InviteServiceHelper.getPendingInviteInfoList(userId, new InviteServiceHelper.InviteServiceHelperInfoList() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyFriends.-$$Lambda$TreadlyFriendsCollectionController$F1nAGkgGj7dDXFwz_lB74FbshEw
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.InviteServiceHelper.InviteServiceHelperInfoList
            public final void onResponse(String str, List list) {
                TreadlyFriendsCollectionController.lambda$updatePendingPrivateVideoInvite$0(TreadlyFriendsCollectionController.this, str, list);
            }
        });
    }

    public static /* synthetic */ void lambda$updatePendingPrivateVideoInvite$0(TreadlyFriendsCollectionController treadlyFriendsCollectionController, String str, List list) {
        if (str != null || list == null) {
            return;
        }
        treadlyFriendsCollectionController.pendingPrivateVideoInvites = list;
        if (treadlyFriendsCollectionController.friendsListener != null) {
            treadlyFriendsCollectionController.friendsListener.friendsUpdated(treadlyFriendsCollectionController.friendsActivityInfo, treadlyFriendsCollectionController.friendsOnline, treadlyFriendsCollectionController.pendingPrivateVideoInvites);
        }
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyServiceDelegate
    public void onUserLogout(UserInfo userInfo) {
        TreadlyServiceManager.getInstance().removeDelegate(this);
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyServiceDelegate
    public void onUserFriendsUpdate(List<UserInfo> list) throws JSONException {
        if (TreadlyActivationManager.shared.hasActivatedDevice()) {
            updateFriend(list);
        }
    }
}
