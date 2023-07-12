package com.treadly.Treadly.Data.Delegates;

import com.treadly.Treadly.Data.Model.UserInfo;
import java.util.List;
import org.json.JSONException;

/* loaded from: classes.dex */
public interface TreadlyServiceDelegate {
    void onCreateFriendInviteToken(String str, String str2);

    void onUserFriendsUpdate(List<UserInfo> list) throws JSONException;

    void onUserLogin(UserInfo userInfo);

    void onUserLogout(UserInfo userInfo);
}
