package com.treadly.Treadly.Data.Managers;

import com.treadly.Treadly.Data.Delegates.TreadlyVideoBroadcastEventDelegate;
import com.treadly.Treadly.Data.Model.UserComment;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceUserStatsInfo;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.paho.client.mqttv3.IMqttToken;

/* loaded from: classes2.dex */
public class TreadlyVideoBroadcastEventManager {
    private List<TreadlyVideoBroadcastEventDelegate> delegates = new ArrayList();
    private String joinBroadcastId;
    private String joinPendingBroadcastId;
    private IMqttToken joinPendingToken;

    public void join(String str) {
        String createJoinVideoBroadcastEventMessage;
        UserInfo userInfo = TreadlyServiceManager.getInstance().getUserInfo();
        if (userInfo == null || (createJoinVideoBroadcastEventMessage = TreadlyEventHelper.createJoinVideoBroadcastEventMessage(userInfo)) == null) {
            return;
        }
        String broadcastVideoTopic = getBroadcastVideoTopic(str);
        TreadlyEventHelper.subscribeTopic(broadcastVideoTopic);
        this.joinBroadcastId = str;
        this.joinPendingToken = TreadlyEventHelper.publishMessage(broadcastVideoTopic, createJoinVideoBroadcastEventMessage);
        this.joinPendingBroadcastId = str;
    }

    public void leave(String str) {
        String createLeaveVideoBroadcastEventMessage;
        UserInfo userInfo = TreadlyServiceManager.getInstance().getUserInfo();
        if (userInfo == null || (createLeaveVideoBroadcastEventMessage = TreadlyEventHelper.createLeaveVideoBroadcastEventMessage(userInfo)) == null) {
            return;
        }
        String broadcastVideoTopic = getBroadcastVideoTopic(str);
        TreadlyEventHelper.publishMessage(broadcastVideoTopic, createLeaveVideoBroadcastEventMessage);
        TreadlyEventHelper.unsubscribeTopic(broadcastVideoTopic);
        this.joinBroadcastId = null;
    }

    public void sendComment(String str, String str2) {
        String createVideoBroadcastCommentEventMessage;
        UserInfo userInfo = TreadlyServiceManager.getInstance().getUserInfo();
        if (userInfo == null || (createVideoBroadcastCommentEventMessage = TreadlyEventHelper.createVideoBroadcastCommentEventMessage(userInfo, str2)) == null) {
            return;
        }
        TreadlyEventHelper.publishMessage(getBroadcastVideoTopic(str), createVideoBroadcastCommentEventMessage);
    }

    public void sendBroadcastEnded(String str) {
        String createVideoBroadcastEndedEventMessage = TreadlyEventHelper.createVideoBroadcastEndedEventMessage();
        if (createVideoBroadcastEndedEventMessage == null) {
            return;
        }
        TreadlyEventHelper.publishMessage(getBroadcastVideoTopic(str), createVideoBroadcastEndedEventMessage);
    }

    public void sendCurrentUserStats(String str, VideoServiceUserStatsInfo videoServiceUserStatsInfo) {
        String createVideoBroadcastUserStatsEventMessage = TreadlyEventHelper.createVideoBroadcastUserStatsEventMessage(videoServiceUserStatsInfo);
        if (createVideoBroadcastUserStatsEventMessage != null) {
            TreadlyEventHelper.publishMessage(getBroadcastVideoTopic(str), createVideoBroadcastUserStatsEventMessage);
        }
    }

    public void handlePrepareSubscriptions() {
        UserInfo userInfo;
        String createJoinVideoBroadcastEventMessage;
        if (this.joinBroadcastId == null || (userInfo = TreadlyServiceManager.getInstance().getUserInfo()) == null || (createJoinVideoBroadcastEventMessage = TreadlyEventHelper.createJoinVideoBroadcastEventMessage(userInfo)) == null) {
            return;
        }
        String broadcastVideoTopic = getBroadcastVideoTopic(this.joinBroadcastId);
        TreadlyEventHelper.subscribeTopic(broadcastVideoTopic);
        TreadlyEventHelper.publishMessage(broadcastVideoTopic, createJoinVideoBroadcastEventMessage);
    }

    public void handleMessage(String str, String str2) {
        String userId = TreadlyServiceManager.getInstance().getUserId();
        if (userId == null) {
            return;
        }
        switch (TreadlyEventHelper.parseMessageId(str2).intValue()) {
            case 1:
                UserInfo parseJoinVideoBroadcastEventMessage = TreadlyEventHelper.parseJoinVideoBroadcastEventMessage(str2);
                if (userId.equals(parseJoinVideoBroadcastEventMessage.id)) {
                    return;
                }
                sendOnUserJoinVideoBroadcastEvent(str, parseJoinVideoBroadcastEventMessage);
                return;
            case 2:
                UserInfo parseLeaveVideoBroadcastEventmessage = TreadlyEventHelper.parseLeaveVideoBroadcastEventmessage(str2);
                if (userId.equals(parseLeaveVideoBroadcastEventmessage.id)) {
                    return;
                }
                sendOnUserLeaveVideoBroadcastEvent(str, parseLeaveVideoBroadcastEventmessage);
                return;
            case 3:
                UserComment parseVideoBroadcastCommentEventMessage = TreadlyEventHelper.parseVideoBroadcastCommentEventMessage(str2);
                if (userId.equals(parseVideoBroadcastCommentEventMessage.user.id)) {
                    return;
                }
                sendOnReceiveVideoBroadcastUserCommentEvent(str, parseVideoBroadcastCommentEventMessage);
                return;
            case 4:
            default:
                return;
            case 5:
                VideoServiceUserStatsInfo parseVideoBroadcastUserStatsEventMessage = TreadlyEventHelper.parseVideoBroadcastUserStatsEventMessage(str2);
                if (parseVideoBroadcastUserStatsEventMessage != null) {
                    sendOnReceiveVideoBroadcastUserStatsEvent(str, parseVideoBroadcastUserStatsEventMessage);
                    return;
                }
                return;
            case 6:
                Integer parseVideoLikeEventMessage = TreadlyEventHelper.parseVideoLikeEventMessage(str2);
                if (parseVideoLikeEventMessage != null) {
                    sendOnReceiveVideoBroadcastLikeEvent(str, parseVideoLikeEventMessage.intValue());
                    return;
                }
                return;
        }
    }

    public void handleMessageSent(IMqttToken iMqttToken) {
        String str;
        IMqttToken iMqttToken2 = this.joinPendingToken;
        if (iMqttToken2 == null || (str = this.joinPendingBroadcastId) == null || iMqttToken != iMqttToken2) {
            return;
        }
        sendOnJoinedVideoBroadcastEvent(str);
        this.joinPendingToken = null;
        this.joinPendingBroadcastId = null;
    }

    private String getBroadcastVideoTopic(String str) {
        return String.format("%s/%s/%s", TreadlyEventHelper.BASE_EVENT_TOPIC, TreadlyEventHelper.VIDEO_PUBLIC_EVENT_TOPIC, str);
    }

    public void addDelegate(TreadlyVideoBroadcastEventDelegate treadlyVideoBroadcastEventDelegate) {
        this.delegates.add(treadlyVideoBroadcastEventDelegate);
    }

    public void removeDelegate(TreadlyVideoBroadcastEventDelegate treadlyVideoBroadcastEventDelegate) {
        this.delegates.remove(treadlyVideoBroadcastEventDelegate);
    }

    private void sendOnUserJoinVideoBroadcastEvent(String str, UserInfo userInfo) {
        for (TreadlyVideoBroadcastEventDelegate treadlyVideoBroadcastEventDelegate : this.delegates) {
            treadlyVideoBroadcastEventDelegate.onUserJoinVideoBroadcast(str, userInfo);
        }
    }

    private void sendOnUserLeaveVideoBroadcastEvent(String str, UserInfo userInfo) {
        for (TreadlyVideoBroadcastEventDelegate treadlyVideoBroadcastEventDelegate : this.delegates) {
            treadlyVideoBroadcastEventDelegate.onUserLeaveVideoBroadcast(str, userInfo);
        }
    }

    private void sendOnReceiveVideoBroadcastUserCommentEvent(String str, UserComment userComment) {
        for (TreadlyVideoBroadcastEventDelegate treadlyVideoBroadcastEventDelegate : this.delegates) {
            treadlyVideoBroadcastEventDelegate.onReceiveVideoBroadcastUserComment(str, userComment);
        }
    }

    private void sendOnJoinedVideoBroadcastEvent(String str) {
        for (TreadlyVideoBroadcastEventDelegate treadlyVideoBroadcastEventDelegate : this.delegates) {
            treadlyVideoBroadcastEventDelegate.onJoinedVideoBroadcast(str);
        }
    }

    private void sendOnVideoBroadcastEndedEvent(String str) {
        for (TreadlyVideoBroadcastEventDelegate treadlyVideoBroadcastEventDelegate : this.delegates) {
            treadlyVideoBroadcastEventDelegate.onVideoBroadcastEnded(str);
        }
    }

    private void sendOnReceiveVideoBroadcastUserStatsEvent(String str, VideoServiceUserStatsInfo videoServiceUserStatsInfo) {
        for (TreadlyVideoBroadcastEventDelegate treadlyVideoBroadcastEventDelegate : this.delegates) {
            treadlyVideoBroadcastEventDelegate.onReceiveVideoBroadcastUserStats(str, videoServiceUserStatsInfo);
        }
    }

    private void sendOnReceiveVideoBroadcastLikeEvent(String str, int i) {
        for (TreadlyVideoBroadcastEventDelegate treadlyVideoBroadcastEventDelegate : this.delegates) {
            treadlyVideoBroadcastEventDelegate.onReceiveVideoBroadcastLike(str, i);
        }
    }
}
