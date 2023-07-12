package com.treadly.Treadly.Data.Managers;

import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener;
import com.treadly.Treadly.Data.Model.FriendInviteInfo;
import com.treadly.Treadly.Data.Model.UserInfo;
import java.util.List;
import org.json.JSONException;

/* loaded from: classes2.dex */
public class UniversalLinkManager {
    private static final String SERVICE_URI_BASE = "https://v.treadly.co/app";
    private static final String SERVICE_URI_ROUTE_INVITE = "https://v.treadly.co/app/invite";
    private static final UniversalLinkManager instance = new UniversalLinkManager();
    public String queueFriendInviteLink = null;

    /* loaded from: classes2.dex */
    public interface UniversalLinkResponseListener {
        void onResponse(String str, FriendInviteInfo friendInviteInfo);
    }

    private boolean isProcessingUniversalLinkAllow() {
        return false;
    }

    public static UniversalLinkManager getInstance() {
        return instance;
    }

    public boolean processQueueUniversalLink(UniversalLinkResponseListener universalLinkResponseListener) {
        if (isProcessingUniversalLinkAllow() && this.queueFriendInviteLink != null && this.queueFriendInviteLink.startsWith("https://v.treadly.co/app") && this.queueFriendInviteLink.startsWith(SERVICE_URI_ROUTE_INVITE)) {
            String str = this.queueFriendInviteLink;
            this.queueFriendInviteLink = null;
            return handleFriendInviteUniversalLink(str, universalLinkResponseListener);
        }
        return false;
    }

    public void getFriendInviteUniversalLink(final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        TreadlyServiceManager.getInstance().createFriendInviteToken(new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.Data.Managers.UniversalLinkManager.1
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onCreateFriendInviteToken(String str, String str2) {
                if (str2 != null) {
                    treadlyServiceResponseEventListener.onCreateFriendInviteToken(null, UniversalLinkManager.this.getFriendInviteUniversalLinkByToken(str2));
                    return;
                }
                treadlyServiceResponseEventListener.onCreateFriendInviteToken("Could not create friend invite link", null);
            }
        });
    }

    private boolean handleFriendInviteUniversalLink(String str, final UniversalLinkResponseListener universalLinkResponseListener) {
        String parseFriendInviteTokenFromUniversalLink = parseFriendInviteTokenFromUniversalLink(str);
        if (parseFriendInviteTokenFromUniversalLink == null) {
            return false;
        }
        final UserInfo userInfo = TreadlyServiceManager.getInstance().getUserInfo();
        if (userInfo == null) {
            this.queueFriendInviteLink = str;
            return true;
        }
        TreadlyServiceManager.getInstance().getFriendInviteInfoByToken(parseFriendInviteTokenFromUniversalLink, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.Data.Managers.UniversalLinkManager.2
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onFriendInviteToken(String str2, final FriendInviteInfo friendInviteInfo) {
                if (friendInviteInfo == null) {
                    universalLinkResponseListener.onResponse("Could not process friend invite. Please try again or request another friend invite", null);
                } else if (friendInviteInfo.userId.equals(userInfo.id)) {
                    universalLinkResponseListener.onResponse("Cannot process friend invite. Invite was created by you", null);
                } else {
                    try {
                        TreadlyServiceManager.getInstance().getFriendsInfo(new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.Data.Managers.UniversalLinkManager.2.1
                            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                            public void onUserFriendsInfo(String str3, List<UserInfo> list) throws JSONException {
                                super.onUserFriendsInfo(str3, list);
                                if (list != null) {
                                    for (UserInfo userInfo2 : list) {
                                        if (friendInviteInfo.userId.equals(userInfo2.id)) {
                                            universalLinkResponseListener.onResponse("Cannot process friend invite. Invite was created by a friend that is already added", null);
                                            return;
                                        }
                                    }
                                    universalLinkResponseListener.onResponse(null, friendInviteInfo);
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return true;
    }

    private String parseFriendInviteTokenFromUniversalLink(String str) {
        String replace = str.replace("https://v.treadly.co/app/invite/", "");
        if (replace.isEmpty() || str.equals(replace)) {
            return null;
        }
        return replace;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getFriendInviteUniversalLinkByToken(String str) {
        return String.format("%s/%s", SERVICE_URI_ROUTE_INVITE, str);
    }
}
