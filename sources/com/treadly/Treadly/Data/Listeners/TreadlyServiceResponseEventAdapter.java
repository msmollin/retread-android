package com.treadly.Treadly.Data.Listeners;

import com.treadly.Treadly.Data.Model.FollowInfo;
import com.treadly.Treadly.Data.Model.FriendInviteInfo;
import com.treadly.Treadly.Data.Model.FriendRequestInfo;
import com.treadly.Treadly.Data.Model.UserActivityInfo;
import com.treadly.Treadly.Data.Model.UserDiscoverInfo;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.Data.Model.UserProfileInfo;
import com.treadly.Treadly.Data.Model.UserRunningSessionInfo;
import com.treadly.Treadly.Data.Model.UserStatsInfo;
import com.treadly.Treadly.Data.Model.UserTokenInfo;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;

/* loaded from: classes.dex */
public class TreadlyServiceResponseEventAdapter implements TreadlyServiceResponseEventListener {
    @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
    public void onActivitiesInfo(String str, List<UserActivityInfo> list) throws JSONException {
    }

    @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
    public void onCreateFriendInviteToken(String str, String str2) {
    }

    @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
    public void onDiscoverFollowersResponse(String str, List<UserDiscoverInfo> list) {
    }

    @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
    public void onFollowResponse(String str) {
    }

    @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
    public void onFriendInviteToken(String str, FriendInviteInfo friendInviteInfo) {
    }

    @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
    public void onGetFollowersResponse(String str, List<FollowInfo> list) {
    }

    @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
    public void onGetFollowingResponse(String str, List<FollowInfo> list) {
    }

    @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
    public void onGetFriendRequests(String str, List<FriendRequestInfo> list) {
    }

    @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
    public void onGetUnclaimedLogs(String str, List<UserRunningSessionInfo> list) {
    }

    @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
    public void onSendDeviceLogSession(String str, String str2, String str3, UserRunningSessionInfo userRunningSessionInfo) {
    }

    @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
    public void onSuccess(String str) {
    }

    @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
    public void onUnfollowResponse(String str) {
    }

    @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
    public void onUrlResponse(String str, String str2) {
    }

    @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
    public void onUserFriendsInfo(String str, List<UserInfo> list) throws JSONException {
    }

    @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
    public void onUserInfo(String str, UserInfo userInfo) {
    }

    @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
    public void onUserInfoProfile(String str, UserInfo userInfo, boolean z, boolean z2) {
    }

    @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
    public void onUserProfile(String str, UserProfileInfo userProfileInfo) {
    }

    @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
    public void onUserProfileRequest(String str) {
    }

    @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
    public void onUserRunningSessionsInfo(String str, ArrayList<UserRunningSessionInfo> arrayList) {
    }

    @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
    public void onUserStatsInfo(String str, ArrayList<UserStatsInfo> arrayList) {
    }

    @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
    public void onUserTokenInfoProfile(String str, UserTokenInfo userTokenInfo, boolean z, boolean z2) {
    }
}
