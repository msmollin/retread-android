package com.treadly.Treadly.UI.TreadlyConnect.TreadlyFriends;

import android.view.View;
import com.treadly.Treadly.Data.Model.UserActivityInfo;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.UI.TreadlyVideo.Data.InviteServiceInviteInfo;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public interface FriendsListener {
    void addFriendsSelected();

    void friendSelected(View view, UserInfo userInfo, List<InviteServiceInviteInfo> list);

    void friendsChanged(int i, int i2);

    void friendsUpdated(Map<String, UserActivityInfo> map, List<UserInfo> list, List<InviteServiceInviteInfo> list2);
}
