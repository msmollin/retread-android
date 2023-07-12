package com.treadly.Treadly.Data.Delegates;

import com.treadly.Treadly.Data.Model.UserInfo;

/* loaded from: classes.dex */
public interface TreadlyUserEventDelegate {
    void onReceiveUserVideoBoardcastInviteRequest(UserInfo userInfo);

    void onReceiveUserVideoPrivateInviteDeclined(UserInfo userInfo);

    void onReceiveUserVideoPrivateInviteDeleted(UserInfo userInfo);

    void onReceiveUserVideoPrivateInviteRequest(UserInfo userInfo);

    void onUserBroadcastEnded(UserInfo userInfo);

    void onUserBroadcastStarted(UserInfo userInfo);

    void onUserFriendAdded(UserInfo userInfo);

    void onUserFriendRemoved(UserInfo userInfo);

    void onUserGroupMemberRequestStateChanged(String str);

    void onUserOffline(UserInfo userInfo);

    void onUserOnline(UserInfo userInfo);

    void onUserSessionExpired();
}
