package com.treadly.Treadly.Data.Delegates;

import com.treadly.Treadly.Data.Model.UserInfo;
import java.util.List;
import org.json.JSONException;

/* loaded from: classes.dex */
public class TreadlyServiceAdapter implements TreadlyServiceDelegate {
    @Override // com.treadly.Treadly.Data.Delegates.TreadlyServiceDelegate
    public void onCreateFriendInviteToken(String str, String str2) {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyServiceDelegate
    public void onUserFriendsUpdate(List<UserInfo> list) throws JSONException {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyServiceDelegate
    public void onUserLogin(UserInfo userInfo) {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyServiceDelegate
    public void onUserLogout(UserInfo userInfo) {
    }
}
