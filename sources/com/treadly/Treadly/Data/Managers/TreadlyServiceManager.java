package com.treadly.Treadly.Data.Managers;

import android.util.Log;
import androidx.annotation.Nullable;
import com.treadly.Treadly.Data.Delegates.TreadlyServiceDelegate;
import com.treadly.Treadly.Data.Delegates.TreadlyUserEventDelegate;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener;
import com.treadly.Treadly.Data.Model.UserActivityInfo;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.Data.Model.UserProfileRequest;
import com.treadly.Treadly.Data.Model.UserRunningSessionInfo;
import com.treadly.Treadly.Data.Model.UserRunningSessionParticipantInfo;
import com.treadly.Treadly.Data.Model.UserTokenInfo;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import org.joda.time.DateTimeConstants;
import org.json.JSONException;

/* loaded from: classes2.dex */
public class TreadlyServiceManager implements TreadlyUserEventDelegate {
    public static final String errorUserNotLoggedIn = "Error: No user currently logged in";
    private static final TreadlyServiceManager instance = new TreadlyServiceManager();
    private List<TreadlyServiceDelegate> delegates = new ArrayList();
    private List<UserInfo> friendsInfo;
    public boolean isOnboarding;
    public UserTokenInfo tokenInfo;
    private UserInfo userInfo;
    private List<UserInfo> usersInfo;

    public void authenticateWithInstagram(String str, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
    }

    public UserInfo getFriendInfoById(String str) {
        return null;
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyUserEventDelegate
    public void onReceiveUserVideoBoardcastInviteRequest(UserInfo userInfo) {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyUserEventDelegate
    public void onReceiveUserVideoPrivateInviteDeclined(UserInfo userInfo) {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyUserEventDelegate
    public void onReceiveUserVideoPrivateInviteDeleted(UserInfo userInfo) {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyUserEventDelegate
    public void onReceiveUserVideoPrivateInviteRequest(UserInfo userInfo) {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyUserEventDelegate
    public void onUserFriendAdded(UserInfo userInfo) {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyUserEventDelegate
    public void onUserFriendRemoved(UserInfo userInfo) {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyUserEventDelegate
    public void onUserGroupMemberRequestStateChanged(String str) {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyUserEventDelegate
    public void onUserSessionExpired() {
    }

    public static TreadlyServiceManager getInstance() {
        return instance;
    }

    private TreadlyServiceManager() {
        TreadlyUserEventManager.getInstance().addDelegate(this);
    }

    public void registerWithEmail(String str, final String str2, final String str3, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        TreadlyServiceHelper.register(str, str2, str3, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceManager.1
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onUserInfo(String str4, UserInfo userInfo) {
                if (str4 == null) {
                    TreadlyServiceManager.this.loginWithEmail(str2, str3, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceManager.1.1
                        @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                        public void onUserInfoProfile(String str5, UserInfo userInfo2, boolean z, boolean z2) {
                            if (userInfo2 != null) {
                                treadlyServiceResponseEventListener.onUserInfoProfile(null, userInfo2, z, z2);
                            } else {
                                treadlyServiceResponseEventListener.onUserInfoProfile(str5, null, false, false);
                            }
                        }
                    });
                } else {
                    treadlyServiceResponseEventListener.onUserInfoProfile(str4, null, false, false);
                }
            }
        });
    }

    public void sendDeviceRunningSession(UserRunningSessionInfo userRunningSessionInfo, ArrayList<UserRunningSessionParticipantInfo> arrayList, String str, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        UserTokenInfo userTokenInfo = this.tokenInfo;
        if (userTokenInfo == null) {
            treadlyServiceResponseEventListener.onSuccess(errorUserNotLoggedIn);
        } else {
            TreadlyServiceHelper.sendDeviceRunningSession(userTokenInfo.token, userRunningSessionInfo, arrayList, str, treadlyServiceResponseEventListener);
        }
    }

    public void sendDeviceLogSession(byte[] bArr, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        UserTokenInfo userTokenInfo = this.tokenInfo;
        if (userTokenInfo == null) {
            treadlyServiceResponseEventListener.onSendDeviceLogSession(errorUserNotLoggedIn, null, null, null);
        } else {
            TreadlyServiceHelper.sendDeviceLogSession(userTokenInfo.token, bArr, treadlyServiceResponseEventListener);
        }
    }

    public void sendActivtyPostImage(String str, File file, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        UserTokenInfo userTokenInfo = this.tokenInfo;
        if (userTokenInfo == null) {
            treadlyServiceResponseEventListener.onUrlResponse(errorUserNotLoggedIn, null);
        } else {
            TreadlyServiceHelper.sendActivtyPostImage(userTokenInfo.token, str, file, treadlyServiceResponseEventListener);
        }
    }

    public void sendActivtyPost(String str, String str2, String str3, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        UserTokenInfo userTokenInfo = this.tokenInfo;
        if (userTokenInfo == null) {
            treadlyServiceResponseEventListener.onSuccess(errorUserNotLoggedIn);
        } else {
            TreadlyServiceHelper.sendActivtyPost(userTokenInfo.token, str, str2, str3, treadlyServiceResponseEventListener);
        }
    }

    public void loginWithEmail(String str, String str2, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        TreadlyServiceHelper.login(str, str2, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceManager.2
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onUserTokenInfoProfile(String str3, final UserTokenInfo userTokenInfo, final boolean z, final boolean z2) {
                if (userTokenInfo != null) {
                    TreadlyServiceHelper.getCurrentUserInfo(userTokenInfo.token, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceManager.2.1
                        @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                        public void onUserInfo(String str4, UserInfo userInfo) {
                            if (userInfo != null) {
                                TreadlyServiceManager.this.tokenInfo = userTokenInfo;
                                TreadlyServiceManager.this.userInfo = userInfo;
                                TreadlyServiceManager.this.isOnboarding = !z2;
                                TreadlyServiceManager.this.onLoginEvent();
                                treadlyServiceResponseEventListener.onUserInfoProfile(null, userInfo, z, z2);
                                return;
                            }
                            treadlyServiceResponseEventListener.onUserInfoProfile(str4, null, false, false);
                        }
                    });
                } else {
                    treadlyServiceResponseEventListener.onUserInfoProfile(str3, null, false, false);
                }
            }
        });
    }

    public void authenticateWithFacebook(String str, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        TreadlyServiceHelper.authenticateWithFacebook(str, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceManager.3
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onUserTokenInfoProfile(String str2, final UserTokenInfo userTokenInfo, final boolean z, final boolean z2) {
                if (userTokenInfo != null) {
                    TreadlyServiceHelper.getCurrentUserInfo(userTokenInfo.token, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceManager.3.1
                        @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                        public void onUserInfo(String str3, UserInfo userInfo) {
                            if (userInfo != null) {
                                TreadlyServiceManager.this.tokenInfo = userTokenInfo;
                                TreadlyServiceManager.this.userInfo = userInfo;
                                TreadlyServiceManager.this.isOnboarding = !z2;
                                TreadlyServiceManager.this.onLoginEvent();
                                treadlyServiceResponseEventListener.onUserInfoProfile(null, userInfo, z, z2);
                                return;
                            }
                            treadlyServiceResponseEventListener.onUserInfoProfile(str3, null, false, false);
                        }
                    });
                } else {
                    treadlyServiceResponseEventListener.onUserInfoProfile(str2, null, false, false);
                }
            }
        });
    }

    public void resetPassword(String str, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        TreadlyServiceHelper.resetPassword(str, treadlyServiceResponseEventListener);
    }

    public void changeUsername(String str, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        String str2;
        UserTokenInfo userTokenInfo = this.tokenInfo;
        if (userTokenInfo != null && (str2 = userTokenInfo.token) != null) {
            TreadlyServiceHelper.changeUsername(str, str2, treadlyServiceResponseEventListener);
        } else {
            treadlyServiceResponseEventListener.onSuccess("Not currently logged in");
        }
    }

    public void changePassword(String str, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        if (this.tokenInfo == null) {
            return;
        }
        String str2 = this.tokenInfo.token;
        if (str2 != null) {
            TreadlyServiceHelper.changePassword(str, str2, treadlyServiceResponseEventListener);
        } else {
            treadlyServiceResponseEventListener.onSuccess("Error getting password");
        }
    }

    public void updateUserProfileDescription(String str, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        if (this.tokenInfo == null) {
            return;
        }
        String str2 = this.tokenInfo.token;
        if (str2 != null) {
            TreadlyServiceHelper.updateUserProfileDescription(str, str2, treadlyServiceResponseEventListener);
        } else {
            treadlyServiceResponseEventListener.onSuccess(errorUserNotLoggedIn);
        }
    }

    public void refreshUserInfo(final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        if (this.tokenInfo == null) {
            return;
        }
        String str = this.tokenInfo.token;
        if (str != null) {
            TreadlyServiceHelper.getCurrentUserInfo(str, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceManager.4
                @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                public void onUserInfo(String str2, UserInfo userInfo) {
                    if (userInfo != null) {
                        TreadlyServiceManager.this.userInfo = userInfo;
                        treadlyServiceResponseEventListener.onUserInfo(null, TreadlyServiceManager.this.userInfo);
                        return;
                    }
                    treadlyServiceResponseEventListener.onUserInfo("Error: Could not get user info user token", null);
                }
            });
        } else {
            treadlyServiceResponseEventListener.onUserInfo("Error: Could not login with email and password", null);
        }
    }

    public void logout() {
        onLogoutEvent();
        this.tokenInfo = null;
        this.userInfo = null;
        this.isOnboarding = false;
        this.usersInfo = new ArrayList();
        this.friendsInfo = new ArrayList();
    }

    public void onLoginEvent() {
        UserInfo userInfo = this.userInfo;
        if (userInfo != null) {
            refreshFriendsInfo(new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceManager.5
                @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                public void onSuccess(String str) {
                    TreadlyServiceManager.this.sendOnUserFriendsUpdateEvent(TreadlyServiceManager.this.friendsInfo);
                }
            });
            TreadlyEventManager.getInstance().addUserDelegate(this);
            sendOnUserLoginEvent(userInfo);
        }
    }

    private void sendOnUserLoginEvent(UserInfo userInfo) {
        for (TreadlyServiceDelegate treadlyServiceDelegate : this.delegates) {
            treadlyServiceDelegate.onUserLogin(userInfo);
        }
    }

    private void sendOnUserLogoutEvent(UserInfo userInfo) {
        for (TreadlyServiceDelegate treadlyServiceDelegate : this.delegates) {
            treadlyServiceDelegate.onUserLogout(userInfo);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendOnUserFriendsUpdateEvent(List<UserInfo> list) {
        for (TreadlyServiceDelegate treadlyServiceDelegate : this.delegates) {
            try {
                treadlyServiceDelegate.onUserFriendsUpdate(list);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void onLogoutEvent() {
        if (this.userInfo == null) {
            return;
        }
        TreadlyEventManager.getInstance().removeUserDelegate(this);
        sendOnUserLogoutEvent(this.userInfo);
    }

    public boolean isLogIn() {
        return this.userInfo != null;
    }

    public String getUserId() {
        if (this.userInfo == null) {
            return null;
        }
        return this.userInfo.id;
    }

    public String getName() {
        if (this.userInfo == null) {
            return null;
        }
        return this.userInfo.name;
    }

    public UserInfo getUserInfo() {
        return this.userInfo;
    }

    public UserInfo getUserInfoById(String str) {
        if (this.usersInfo == null) {
            return null;
        }
        for (UserInfo userInfo : this.usersInfo) {
            if (userInfo.id.equals(str)) {
                return userInfo;
            }
        }
        return null;
    }

    public void getUserProfileInfo(String str, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        UserTokenInfo userTokenInfo = this.tokenInfo;
        if (userTokenInfo == null) {
            treadlyServiceResponseEventListener.onUserProfile(errorUserNotLoggedIn, null);
            return;
        }
        TreadlyServiceHelper.getUserProfileInfo(userTokenInfo.token, str, getLocaleTimeZone(TimeZone.getDefault()), treadlyServiceResponseEventListener);
    }

    public UserInfo getFriendsInfoById(String str) {
        for (UserInfo userInfo : this.friendsInfo) {
            if (userInfo.id.equals(str)) {
                return userInfo;
            }
        }
        return null;
    }

    public void acceptFriendRequest(String str, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        if (this.tokenInfo == null) {
            treadlyServiceResponseEventListener.onSuccess(errorUserNotLoggedIn);
        } else {
            TreadlyServiceHelper.acceptFriendRequest(this.tokenInfo.token, str, treadlyServiceResponseEventListener);
        }
    }

    public void declineFriendRequest(String str, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        if (this.tokenInfo == null) {
            treadlyServiceResponseEventListener.onSuccess(errorUserNotLoggedIn);
        } else {
            TreadlyServiceHelper.declineFriendRequest(this.tokenInfo.token, str, treadlyServiceResponseEventListener);
        }
    }

    public void createFriendInviteToken(TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        if (this.tokenInfo == null) {
            treadlyServiceResponseEventListener.onCreateFriendInviteToken(errorUserNotLoggedIn, null);
        } else {
            TreadlyServiceHelper.createFriendInviteToken(this.tokenInfo.token, treadlyServiceResponseEventListener);
        }
    }

    public void getFriendInviteInfoByToken(String str, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        if (this.tokenInfo == null) {
            treadlyServiceResponseEventListener.onCreateFriendInviteToken(errorUserNotLoggedIn, null);
        } else {
            TreadlyServiceHelper.getFriendInviteInfoByToken(this.tokenInfo.token, str, treadlyServiceResponseEventListener);
        }
    }

    public void acceptFriendInvite(String str, String str2, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        if (this.tokenInfo == null) {
            treadlyServiceResponseEventListener.onSuccess(errorUserNotLoggedIn);
        } else {
            TreadlyServiceHelper.acceptFriendInvite(this.tokenInfo.token, str, str2, treadlyServiceResponseEventListener);
        }
    }

    public void declineFriendInvite(String str, String str2, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        if (this.tokenInfo == null) {
            treadlyServiceResponseEventListener.onSuccess(errorUserNotLoggedIn);
        } else {
            TreadlyServiceHelper.declineFriendInvite(this.tokenInfo.token, str, str2, treadlyServiceResponseEventListener);
        }
    }

    public void getDeviceUserStatsInfo(TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        getDeviceUserStatsInfo(null, false, treadlyServiceResponseEventListener);
    }

    public void getDeviceUserStatsInfo(boolean z, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        getDeviceUserStatsInfo(null, z, treadlyServiceResponseEventListener);
    }

    public void getDeviceUserStatsInfo(String str, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        getDeviceUserStatsInfo(str, false, treadlyServiceResponseEventListener);
    }

    public void getDeviceUserStatsInfo(String str, boolean z, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        UserTokenInfo userTokenInfo = this.tokenInfo;
        if (userTokenInfo == null) {
            treadlyServiceResponseEventListener.onUserStatsInfo(errorUserNotLoggedIn, null);
            return;
        }
        TreadlyServiceHelper.getDeviceUserStatsInfo(userTokenInfo.token, getLocaleTimeZone(TimeZone.getDefault()), str, z, treadlyServiceResponseEventListener);
    }

    public void getDeviceUserRunningSessionsInfoByDate(int i, int i2, int i3, @Nullable String str, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        if (this.tokenInfo == null) {
            treadlyServiceResponseEventListener.onUserRunningSessionsInfo(errorUserNotLoggedIn, null);
        }
        TreadlyServiceHelper.getDeviceUserRunningSessionsInfoByDate(this.tokenInfo.token, i, i2, i3, getLocaleTimeZone(TimeZone.getDefault()), str, treadlyServiceResponseEventListener);
    }

    public void refreshFriendsInfo(final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        try {
            getFriendsInfo(new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceManager.6
                @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                public void onSuccess(String str) {
                    treadlyServiceResponseEventListener.onSuccess(str);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getSentFriendRequest(TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        if (this.tokenInfo == null) {
            treadlyServiceResponseEventListener.onGetFriendRequests(errorUserNotLoggedIn, null);
        }
    }

    public void getFriendsInfo(final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) throws JSONException {
        UserTokenInfo userTokenInfo = this.tokenInfo;
        if (userTokenInfo == null) {
            treadlyServiceResponseEventListener.onUserFriendsInfo(errorUserNotLoggedIn, null);
        } else {
            TreadlyServiceHelper.getCurrentUserFriendsInfo(userTokenInfo.token, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceManager.7
                @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                public void onUserFriendsInfo(String str, final List<UserInfo> list) throws JSONException {
                    if (list != null) {
                        ArrayList arrayList = new ArrayList();
                        for (UserInfo userInfo : list) {
                            arrayList.add(userInfo.id);
                        }
                        UserActivityHelper.get(arrayList, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceManager.7.1
                            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                            public void onActivitiesInfo(String str2, List<UserActivityInfo> list2) throws JSONException {
                                if (list2 != null) {
                                    TreadlyServiceManager.this.friendsInfo = list;
                                    for (UserActivityInfo userActivityInfo : list2) {
                                        TreadlyServiceManager.this.updateFriendStatus(userActivityInfo.id, Boolean.valueOf(userActivityInfo.isOnline), Boolean.valueOf(userActivityInfo.isBroadcasting));
                                    }
                                    treadlyServiceResponseEventListener.onUserFriendsInfo(null, TreadlyServiceManager.this.friendsInfo);
                                    return;
                                }
                                treadlyServiceResponseEventListener.onUserFriendsInfo("Error: Could not get friends info using token", null);
                            }
                        });
                        return;
                    }
                    treadlyServiceResponseEventListener.onUserFriendsInfo("Error: Could not get friends info using token", null);
                }
            });
        }
    }

    public boolean updateFriendStatus(String str) {
        return updateFriendStatus(str, null, null);
    }

    public boolean updateFriendStatus(String str, Boolean bool, Boolean bool2) {
        for (int i = 0; i < this.friendsInfo.size(); i++) {
            if (this.friendsInfo.get(i).id.equals(str)) {
                if (bool != null) {
                    this.friendsInfo.get(i).online = bool.booleanValue();
                }
                if (bool2 != null) {
                    this.friendsInfo.get(i).broadcasting = bool2.booleanValue();
                    return true;
                }
                return true;
            }
        }
        return false;
    }

    public void refreshUsersInfo(final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        getUsersInfo("", false, false, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceManager.8
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onUserFriendsInfo(String str, List<UserInfo> list) throws JSONException {
                treadlyServiceResponseEventListener.onSuccess(str);
            }
        });
    }

    public void setUserImage(File file, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        UserTokenInfo userTokenInfo = this.tokenInfo;
        if (userTokenInfo == null) {
            treadlyServiceResponseEventListener.onSuccess(errorUserNotLoggedIn);
        } else {
            TreadlyServiceHelper.sendUserAvatar(userTokenInfo.id, userTokenInfo.token, file, treadlyServiceResponseEventListener);
        }
    }

    public void getUsersInfo(String str, Boolean bool, final Boolean bool2, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        Log.d("ServiceManager", "getUsersInfo");
        UserTokenInfo userTokenInfo = this.tokenInfo;
        if (userTokenInfo == null) {
            Log.d("ServiceManager", "token is null");
            try {
                treadlyServiceResponseEventListener.onUserFriendsInfo(errorUserNotLoggedIn, null);
                return;
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
        }
        TreadlyServiceHelper.getUserAll(userTokenInfo.token, str, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceManager.9
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onUserFriendsInfo(String str2, List<UserInfo> list) throws JSONException {
                Log.d("ServiceManager", "onUserFriendsInfo from getUserAll");
                if (str2 == null) {
                    TreadlyServiceManager.this.usersInfo = list;
                    if (!bool2.booleanValue()) {
                        treadlyServiceResponseEventListener.onUserFriendsInfo(null, TreadlyServiceManager.this.usersInfo);
                        return;
                    } else {
                        TreadlyServiceManager.this.getFriendsInfo(new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceManager.9.1
                            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                            public void onUserFriendsInfo(String str3, List<UserInfo> list2) throws JSONException {
                                if (str3 != null) {
                                    Log.e("ServiceManager", str3);
                                    treadlyServiceResponseEventListener.onUserFriendsInfo("Error: Could not get friends info using token", null);
                                }
                                HashMap hashMap = new HashMap();
                                for (UserInfo userInfo : list2) {
                                    hashMap.put(userInfo.id, userInfo);
                                }
                                treadlyServiceResponseEventListener.onUserFriendsInfo(null, TreadlyServiceManager.this.usersInfo);
                            }
                        });
                        return;
                    }
                }
                Log.e("ServiceManager", str2);
                treadlyServiceResponseEventListener.onUserFriendsInfo("Error: Could not get users info using token", null);
            }
        });
    }

    public boolean getUnclaimedUserStatsLogInfo(String str, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        if (str.isEmpty()) {
            return false;
        }
        UserTokenInfo userTokenInfo = this.tokenInfo;
        if (userTokenInfo == null) {
            treadlyServiceResponseEventListener.onGetUnclaimedLogs(errorUserNotLoggedIn, null);
            return false;
        }
        TreadlyServiceHelper.getUnclaimedLogInfo(userTokenInfo.token, str, treadlyServiceResponseEventListener);
        return true;
    }

    public boolean claimUnclaimedSession(UserRunningSessionInfo userRunningSessionInfo, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        UserTokenInfo userTokenInfo;
        if (userRunningSessionInfo.log_id.equals("") || (userTokenInfo = this.tokenInfo) == null) {
            return false;
        }
        TreadlyServiceHelper.claimUnclaimedSession(userTokenInfo.token, userRunningSessionInfo, treadlyServiceResponseEventListener);
        return true;
    }

    public boolean claimAllUnclaimedSessions(String str, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        UserTokenInfo userTokenInfo;
        if (str == null || str.isEmpty() || (userTokenInfo = this.tokenInfo) == null) {
            return false;
        }
        TreadlyServiceHelper.claimAllUnclaimedSessions(userTokenInfo.token, str, treadlyServiceResponseEventListener);
        return true;
    }

    public boolean deleteUnclaimedSession(UserRunningSessionInfo userRunningSessionInfo, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        UserTokenInfo userTokenInfo;
        if (userRunningSessionInfo.log_id.equals("") || (userTokenInfo = this.tokenInfo) == null) {
            return false;
        }
        TreadlyServiceHelper.deleteUnclaimedSession(userTokenInfo.token, userRunningSessionInfo, treadlyServiceResponseEventListener);
        return true;
    }

    public void getSingleUserMode(String str, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        UserTokenInfo userTokenInfo;
        if (str.equals("") || (userTokenInfo = this.tokenInfo) == null) {
            return;
        }
        TreadlyServiceHelper.getSingleUserMode(userTokenInfo.token, str, treadlyServiceResponseEventListener);
    }

    public void addSingleUserMode(String str, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        UserTokenInfo userTokenInfo;
        if (str.equals("") || (userTokenInfo = this.tokenInfo) == null) {
            return;
        }
        TreadlyServiceHelper.addSingleUserMode(userTokenInfo.token, str, treadlyServiceResponseEventListener);
    }

    public void removeSingleUserMode(String str, TreadlyServiceResponseEventAdapter treadlyServiceResponseEventAdapter) {
        UserTokenInfo userTokenInfo;
        if (str.equals("") || (userTokenInfo = this.tokenInfo) == null) {
            return;
        }
        TreadlyServiceHelper.removeSingleUserMode(userTokenInfo.token, str, treadlyServiceResponseEventAdapter);
    }

    public void resetSingleUserMode(String str, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        UserTokenInfo userTokenInfo;
        if (str.equals("") || (userTokenInfo = this.tokenInfo) == null) {
            return;
        }
        TreadlyServiceHelper.resetSingleUserMode(userTokenInfo.token, str, treadlyServiceResponseEventListener);
    }

    public void updateOnboardingStatus(TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        UserTokenInfo userTokenInfo = this.tokenInfo;
        if (userTokenInfo == null) {
            treadlyServiceResponseEventListener.onSuccess(errorUserNotLoggedIn);
        } else {
            TreadlyServiceHelper.updateOnboardingStatus(userTokenInfo.token, treadlyServiceResponseEventListener);
        }
    }

    public void addDelegate(TreadlyServiceDelegate treadlyServiceDelegate) {
        this.delegates.add(treadlyServiceDelegate);
    }

    public void removeDelegate(TreadlyServiceDelegate treadlyServiceDelegate) {
        Iterator<TreadlyServiceDelegate> it = this.delegates.iterator();
        while (it.hasNext()) {
            if (it.next() == treadlyServiceDelegate) {
                it.remove();
            }
        }
    }

    public void updateUserProfile(UserProfileRequest userProfileRequest, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        if (this.tokenInfo == null) {
            treadlyServiceResponseEventListener.onSuccess(errorUserNotLoggedIn);
        }
        TreadlyServiceHelper.updateUserProfile(this.tokenInfo.token, userProfileRequest, treadlyServiceResponseEventListener);
    }

    public void discoverFollowers(TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        UserTokenInfo userTokenInfo = this.tokenInfo;
        if (userTokenInfo == null) {
            return;
        }
        TreadlyServiceHelper.discoverFollowers(userTokenInfo.token, treadlyServiceResponseEventListener);
    }

    public void addFollowRequest(String str, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        UserTokenInfo userTokenInfo = this.tokenInfo;
        if (userTokenInfo == null) {
            return;
        }
        TreadlyServiceHelper.addFollowRequest(userTokenInfo.token, str, treadlyServiceResponseEventListener);
    }

    public void unfollowUser(String str, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        UserTokenInfo userTokenInfo = this.tokenInfo;
        if (userTokenInfo == null) {
            return;
        }
        TreadlyServiceHelper.unfollowUser(userTokenInfo.token, str, treadlyServiceResponseEventListener);
    }

    public void getCurrentUserFollowersInfo(String str, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        UserTokenInfo userTokenInfo = this.tokenInfo;
        if (userTokenInfo == null) {
            return;
        }
        TreadlyServiceHelper.getCurrentUserFollowersInfo(userTokenInfo.token, str, treadlyServiceResponseEventListener);
    }

    public void getCurrentUserFollowingInfo(String str, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        UserTokenInfo userTokenInfo = this.tokenInfo;
        if (userTokenInfo == null) {
            return;
        }
        TreadlyServiceHelper.getCurrentUserFollowingInfo(userTokenInfo.token, str, treadlyServiceResponseEventListener);
    }

    int getLocaleTimeZone(TimeZone timeZone) {
        return (timeZone.getRawOffset() + timeZone.getDSTSavings()) / DateTimeConstants.MILLIS_PER_HOUR;
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyUserEventDelegate
    public void onUserOnline(UserInfo userInfo) {
        if (updateFriendStatus(userInfo.id, true, null)) {
            sendOnUserFriendsUpdateEvent(this.friendsInfo);
        }
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyUserEventDelegate
    public void onUserOffline(UserInfo userInfo) {
        if (updateFriendStatus(userInfo.id, false, null)) {
            sendOnUserFriendsUpdateEvent(this.friendsInfo);
        }
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyUserEventDelegate
    public void onUserBroadcastStarted(UserInfo userInfo) {
        if (updateFriendStatus(userInfo.id, null, true)) {
            sendOnUserFriendsUpdateEvent(this.friendsInfo);
        }
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyUserEventDelegate
    public void onUserBroadcastEnded(UserInfo userInfo) {
        if (updateFriendStatus(userInfo.id, null, false)) {
            sendOnUserFriendsUpdateEvent(this.friendsInfo);
        }
    }
}
