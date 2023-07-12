package com.treadly.Treadly.Data.Managers;

import android.util.Log;
import com.treadly.Treadly.Data.Delegates.TreadlyUserEventDelegate;
import com.treadly.Treadly.Data.Model.UserInfo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class TreadlyUserEventManager {
    private static final TreadlyUserEventManager instance = new TreadlyUserEventManager();
    private static List<TreadlyUserEventDelegate> delegates = new ArrayList();

    private void sendOnReceiveUserVideoPrivateInviteDeclinedEvent(UserInfo userInfo) {
    }

    private void sendOnReceiveUserVideoPrivateInviteDeletedEvent(UserInfo userInfo) {
    }

    public static TreadlyUserEventManager getInstance() {
        return instance;
    }

    public void handleMessage(String str, String str2) {
        String userId = TreadlyServiceManager.getInstance().getUserId();
        int intValue = TreadlyEventHelper.parseMessageId(str2).intValue();
        if (intValue == 0) {
            UserInfo parseUserOnlineStatus = TreadlyEventHelper.parseUserOnlineStatus(str2);
            if (userId.equals(parseUserOnlineStatus.id)) {
                return;
            }
            if (parseUserOnlineStatus.online) {
                sendOnUserOnlineEvent(parseUserOnlineStatus);
                return;
            } else {
                sendOnUserOfflineEvent(parseUserOnlineStatus);
                return;
            }
        }
        switch (intValue) {
            case TreadlyEventHelper.MESSAGE_ID_USER_VIDEO_BROADCAST_INVITE_REQUEST /* 202 */:
                UserInfo parseVideoBroadcastInviteRequestEventMessage = TreadlyEventHelper.parseVideoBroadcastInviteRequestEventMessage(str2);
                if (parseVideoBroadcastInviteRequestEventMessage != null) {
                    sendOnReceiveUserVideoBoardcastInviteRequestEvent(parseVideoBroadcastInviteRequestEventMessage);
                    return;
                }
                return;
            case TreadlyEventHelper.MESSAGE_ID_USER_VIDEO_PRIVATE_INVITE_REQUEST /* 203 */:
                UserInfo parseVideoPrivateInviteRequestEventMessage = TreadlyEventHelper.parseVideoPrivateInviteRequestEventMessage(str2);
                if (parseVideoPrivateInviteRequestEventMessage != null) {
                    sendOnReceiveUserVideoPrivateInviteRequestEvent(parseVideoPrivateInviteRequestEventMessage);
                    return;
                }
                return;
            case TreadlyEventHelper.MESSAGE_ID_USER_BROADCAST_STARTED /* 204 */:
                UserInfo parseUserBroadcastStartedEventMessage = TreadlyEventHelper.parseUserBroadcastStartedEventMessage(str2);
                if (parseUserBroadcastStartedEventMessage != null) {
                    sendOnUserBroadcastStartedEvent(parseUserBroadcastStartedEventMessage);
                    return;
                }
                return;
            case TreadlyEventHelper.MESSAGE_ID_USER_BROADCAST_ENDED /* 205 */:
                UserInfo parseUserBroadcastEndedEventMessage = TreadlyEventHelper.parseUserBroadcastEndedEventMessage(str2);
                if (parseUserBroadcastEndedEventMessage != null) {
                    sendOnUserBroadcastEndedEvent(parseUserBroadcastEndedEventMessage);
                    return;
                }
                return;
            default:
                switch (intValue) {
                    case 208:
                        UserInfo parseVideoPrivateInviteDeletedEventMessage = TreadlyEventHelper.parseVideoPrivateInviteDeletedEventMessage(str2);
                        if (parseVideoPrivateInviteDeletedEventMessage != null) {
                            Log.d("UserEventManager", "Private Invite Request deleted");
                            sendOnReceiveUserVideoPrivateInviteDeletedEvent(parseVideoPrivateInviteDeletedEventMessage);
                            return;
                        }
                        return;
                    case TreadlyEventHelper.MESSAGE_ID_USER_VIDEO_PRIVATE_INVITE_DECLINED /* 209 */:
                        UserInfo parseVideoPrivateInviteDeclinedEventMessage = TreadlyEventHelper.parseVideoPrivateInviteDeclinedEventMessage(str2);
                        if (parseVideoPrivateInviteDeclinedEventMessage != null) {
                            Log.d("UserEventManager", "Private Invite Request declined");
                            sendOnReceiveUserVideoPrivateInviteDeclinedEvent(parseVideoPrivateInviteDeclinedEventMessage);
                            return;
                        }
                        return;
                    default:
                        return;
                }
        }
    }

    public void addDelegate(TreadlyUserEventDelegate treadlyUserEventDelegate) {
        delegates.add(treadlyUserEventDelegate);
    }

    public void removeDelegate(TreadlyUserEventDelegate treadlyUserEventDelegate) {
        Iterator<TreadlyUserEventDelegate> it = delegates.iterator();
        while (it.hasNext()) {
            if (treadlyUserEventDelegate == it.next()) {
                it.remove();
            }
        }
    }

    private void sendOnUserOnlineEvent(UserInfo userInfo) {
        for (TreadlyUserEventDelegate treadlyUserEventDelegate : delegates) {
            treadlyUserEventDelegate.onUserOnline(userInfo);
        }
    }

    private void sendOnUserOfflineEvent(UserInfo userInfo) {
        for (TreadlyUserEventDelegate treadlyUserEventDelegate : delegates) {
            treadlyUserEventDelegate.onUserOffline(userInfo);
        }
    }

    private void sendOnReceiveUserVideoPrivateInviteRequestEvent(UserInfo userInfo) {
        for (TreadlyUserEventDelegate treadlyUserEventDelegate : delegates) {
            treadlyUserEventDelegate.onReceiveUserVideoPrivateInviteRequest(userInfo);
        }
    }

    private void sendOnReceiveUserVideoBoardcastInviteRequestEvent(UserInfo userInfo) {
        for (TreadlyUserEventDelegate treadlyUserEventDelegate : delegates) {
            treadlyUserEventDelegate.onReceiveUserVideoBoardcastInviteRequest(userInfo);
        }
    }

    private void sendOnUserBroadcastStartedEvent(UserInfo userInfo) {
        for (TreadlyUserEventDelegate treadlyUserEventDelegate : delegates) {
            treadlyUserEventDelegate.onUserBroadcastStarted(userInfo);
        }
    }

    private void sendOnUserBroadcastEndedEvent(UserInfo userInfo) {
        for (TreadlyUserEventDelegate treadlyUserEventDelegate : delegates) {
            treadlyUserEventDelegate.onUserBroadcastEnded(userInfo);
        }
    }

    public void sendBroadcastStarted() {
        String createUserBroadcastStartedEventMessage;
        UserInfo userInfo = TreadlyServiceManager.getInstance().getUserInfo();
        if (userInfo == null || (createUserBroadcastStartedEventMessage = TreadlyEventHelper.createUserBroadcastStartedEventMessage(userInfo)) == null) {
            return;
        }
        TreadlyEventHelper.publishMessage(getUserAllTopic(), createUserBroadcastStartedEventMessage);
    }

    public void sendBroadcastEnded() {
        String createUserBroadcastEndedEventMessage;
        UserInfo userInfo = TreadlyServiceManager.getInstance().getUserInfo();
        if (userInfo == null || (createUserBroadcastEndedEventMessage = TreadlyEventHelper.createUserBroadcastEndedEventMessage(userInfo)) == null) {
            return;
        }
        TreadlyEventHelper.publishMessage(getUserAllTopic(), createUserBroadcastEndedEventMessage);
    }

    public void sendFriendAdded(String str) {
        String createUserFriendAddedEventMessage;
        UserInfo userInfo = TreadlyServiceManager.getInstance().getUserInfo();
        if (userInfo == null || (createUserFriendAddedEventMessage = TreadlyEventHelper.createUserFriendAddedEventMessage(userInfo)) == null) {
            return;
        }
        TreadlyEventHelper.publishMessage(getUserTopic(str), createUserFriendAddedEventMessage);
    }

    public void sendVideoBoardcastInviteRequest(String str) {
        String createVideoBroadcastInviteRequestEventMessage;
        UserInfo userInfo = TreadlyServiceManager.getInstance().getUserInfo();
        if (userInfo == null || (createVideoBroadcastInviteRequestEventMessage = TreadlyEventHelper.createVideoBroadcastInviteRequestEventMessage(userInfo)) == null) {
            return;
        }
        TreadlyEventHelper.publishMessage(getUserTopic(str), createVideoBroadcastInviteRequestEventMessage);
    }

    public void sendVideoPrivateInviteRequest(String str) {
        String createVideoPrivateInviteRequestEventMessage;
        UserInfo userInfo = TreadlyServiceManager.getInstance().getUserInfo();
        if (userInfo == null || (createVideoPrivateInviteRequestEventMessage = TreadlyEventHelper.createVideoPrivateInviteRequestEventMessage(userInfo)) == null) {
            return;
        }
        TreadlyEventHelper.publishMessage(getUserTopic(str), createVideoPrivateInviteRequestEventMessage);
    }

    public void sendVideoPrivateInviteDeleted(String str) {
        UserInfo userInfo = TreadlyServiceManager.getInstance().getUserInfo();
        if (userInfo == null) {
            Log.e("PRIVATE_STREAM", "invite delete user null");
            return;
        }
        String createVideoPrivateInviteDeletedEventMessage = TreadlyEventHelper.createVideoPrivateInviteDeletedEventMessage(userInfo);
        if (createVideoPrivateInviteDeletedEventMessage.isEmpty()) {
            return;
        }
        TreadlyEventHelper.publishMessage(getUserTopic(str), createVideoPrivateInviteDeletedEventMessage);
    }

    public void sendVideoPrivateInviteDeclined(String str) {
        UserInfo userInfo = TreadlyServiceManager.getInstance().getUserInfo();
        if (userInfo == null) {
            return;
        }
        String createVideoPrivateInviteDeclinedEventMessage = TreadlyEventHelper.createVideoPrivateInviteDeclinedEventMessage(userInfo);
        if (createVideoPrivateInviteDeclinedEventMessage.isEmpty()) {
            return;
        }
        TreadlyEventHelper.publishMessage(getUserTopic(str), createVideoPrivateInviteDeclinedEventMessage);
    }

    private String getUserTopic(String str) {
        return String.format("%s/%s/%s", TreadlyEventHelper.BASE_EVENT_TOPIC, TreadlyEventHelper.USER_EVENT_TOPIC, str);
    }

    private String getUserAllTopic() {
        return String.format("%s/%s/all", TreadlyEventHelper.BASE_EVENT_TOPIC, TreadlyEventHelper.USER_EVENT_TOPIC);
    }
}
