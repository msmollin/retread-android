package com.treadly.Treadly.Data.Managers;

import android.util.Log;
import android.util.Pair;
import com.treadly.Treadly.Data.Delegates.TreadlyVideoPrivateEventDelegate;
import com.treadly.Treadly.Data.Model.TrainerModeState;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.Data.Model.UserTrainerMode;
import com.treadly.Treadly.Data.Model.UserVideoPrivateStateInfo;
import com.treadly.Treadly.Data.Model.UsersTrainerModes;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceUserStatsInfo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class TreadlyVideoPrivateEventManager implements TreadlyVideoPrivateEventDelegate {
    private static String TAG = "PRIVATE_EVENT_MANAGER";
    private String mJoinSessionId = null;
    private static TreadlyVideoPrivateEventManager instance = new TreadlyVideoPrivateEventManager();
    private static List<TreadlyVideoPrivateEventDelegate> delegates = new ArrayList();

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoPrivateEventDelegate
    public void onReceiveVideoPrivateClientForceDisconnectEvent(String str) {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoPrivateEventDelegate
    public void onReceiveVideoPrivateStudentModeRequest(String str, UserTrainerMode userTrainerMode, String str2) {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoPrivateEventDelegate
    public void onReceiveVideoPrivateStudentModeResponse(String str, UserTrainerMode userTrainerMode, String str2) {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoPrivateEventDelegate
    public void onReceiveVideoPrivateTrainerModeRequest(String str, UserTrainerMode userTrainerMode) {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoPrivateEventDelegate
    public void onReceiveVideoPrivateTrainerModeResponse(String str, UserTrainerMode userTrainerMode) {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoPrivateEventDelegate
    public void onReceiveVideoPrivateUserStatsEvent(String str, VideoServiceUserStatsInfo videoServiceUserStatsInfo) {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoPrivateEventDelegate
    public void onReceiveVideoPrivateUserTreadmillConnected(String str, UserInfo userInfo) {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoPrivateEventDelegate
    public void onReceiveVideoPrivateUserTreadmillNotConnected(String str, UserInfo userInfo) {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoPrivateEventDelegate
    public void onReceiveVideoPrivateUsersStateChanged(String str, ArrayList<UserVideoPrivateStateInfo> arrayList) {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoPrivateEventDelegate
    public void onReceiveVideoPrivateUsersTrainerModes(String str, List<UsersTrainerModes> list) {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoPrivateEventDelegate
    public void onUserJoinVideoPrivateEvent(String str, UserInfo userInfo) {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoPrivateEventDelegate
    public void onUserLeaveVideoPrivateEvent(String str, UserInfo userInfo) {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoPrivateEventDelegate
    public void onUserPauseVideoPrivateEvent(String str, UserInfo userInfo) {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoPrivateEventDelegate
    public void onUserUnpauseVideoPrivateEvent(String str, UserInfo userInfo) {
    }

    public static TreadlyVideoPrivateEventManager getInstance() {
        return instance;
    }

    public void handleMessage(String str, String str2) {
        String userId = TreadlyServiceManager.getInstance().getUserId();
        int intValue = TreadlyEventHelper.parseMessageId(str2).intValue();
        if (userId == null || intValue == 0) {
            return;
        }
        switch (intValue) {
            case 101:
                UserInfo parseJoinVideoPrivateEventMessage = TreadlyEventHelper.parseJoinVideoPrivateEventMessage(str2);
                if (parseJoinVideoPrivateEventMessage == null || userId.equals(parseJoinVideoPrivateEventMessage.id)) {
                    return;
                }
                sendOnUserJoinVideoPrivateEvent(str, parseJoinVideoPrivateEventMessage);
                return;
            case 102:
                UserInfo parseLeaveVideoPrivateEventMessage = TreadlyEventHelper.parseLeaveVideoPrivateEventMessage(str2);
                if (parseLeaveVideoPrivateEventMessage == null || userId.equals(parseLeaveVideoPrivateEventMessage.id)) {
                    return;
                }
                sendOnUserLeaveVideoPrivateEvent(str, parseLeaveVideoPrivateEventMessage);
                return;
            case 103:
                VideoServiceUserStatsInfo parseVideoPrivateUserStatsEventMessage = TreadlyEventHelper.parseVideoPrivateUserStatsEventMessage(str2);
                if (parseVideoPrivateUserStatsEventMessage == null || parseVideoPrivateUserStatsEventMessage.userId == null) {
                    return;
                }
                sendOnReceiveVideoPrivateUserStatsEvent(str, parseVideoPrivateUserStatsEventMessage);
                return;
            case 104:
                UserTrainerMode parseUserTrainerModeEventMessage = TreadlyEventHelper.parseUserTrainerModeEventMessage(str2);
                if (parseUserTrainerModeEventMessage != null) {
                    sendOnReceiveVideoPrivateTrainerModeRequestEvent(str, parseUserTrainerModeEventMessage);
                    return;
                }
                return;
            case 105:
                UserTrainerMode parseUserTrainerModeEventMessage2 = TreadlyEventHelper.parseUserTrainerModeEventMessage(str2);
                if (parseUserTrainerModeEventMessage2 != null) {
                    sendOnReceiveVideoPrivateTrainerModeResponseEvent(str, parseUserTrainerModeEventMessage2);
                    return;
                }
                return;
            case 106:
                Pair<UserTrainerMode, String> parseUserStudentModeEventMessage = TreadlyEventHelper.parseUserStudentModeEventMessage(str2);
                if (parseUserStudentModeEventMessage.first == null || parseUserStudentModeEventMessage.second == null) {
                    return;
                }
                sendOnReceiveVideoPrivateStudentModeRequestEvent(str, (UserTrainerMode) parseUserStudentModeEventMessage.first, (String) parseUserStudentModeEventMessage.second);
                return;
            case 107:
                Pair<UserTrainerMode, String> parseUserStudentModeEventMessage2 = TreadlyEventHelper.parseUserStudentModeEventMessage(str2);
                if (parseUserStudentModeEventMessage2.first == null || parseUserStudentModeEventMessage2.second == null) {
                    return;
                }
                sendOnReceiveVideoPrivateStudentModeResponseEvent(str, (UserTrainerMode) parseUserStudentModeEventMessage2.first, (String) parseUserStudentModeEventMessage2.second);
                return;
            case 108:
                sendOnReceiveVideoPrivateClientForceDisconnectEvent(str);
                return;
            case 109:
                List<UsersTrainerModes> parseVideoPrivateUsersTrainerModesEventMessage = TreadlyEventHelper.parseVideoPrivateUsersTrainerModesEventMessage(str2);
                if (parseVideoPrivateUsersTrainerModesEventMessage != null) {
                    sendOnReceiveVideoPrivateUsersTrainerModesEvent(str, parseVideoPrivateUsersTrainerModesEventMessage);
                    return;
                }
                return;
            case 110:
                UserInfo parsePauseVideoPrivateEventMessage = TreadlyEventHelper.parsePauseVideoPrivateEventMessage(str2);
                if (parsePauseVideoPrivateEventMessage == null || userId.equals(parsePauseVideoPrivateEventMessage.id)) {
                    return;
                }
                sendOnUserPauseVideoPrivate(str, parsePauseVideoPrivateEventMessage);
                return;
            case 111:
                UserInfo parseUnpauseVideoPrivateEventMessage = TreadlyEventHelper.parseUnpauseVideoPrivateEventMessage(str2);
                if (parseUnpauseVideoPrivateEventMessage == null || userId.equals(parseUnpauseVideoPrivateEventMessage.id)) {
                    return;
                }
                sendOnUserUnpauseVideoPrivate(str, parseUnpauseVideoPrivateEventMessage);
                return;
            case 112:
                ArrayList<UserVideoPrivateStateInfo> parseVideoPrivateUserStateChangedEventMessage = TreadlyEventHelper.parseVideoPrivateUserStateChangedEventMessage(str2);
                if (parseVideoPrivateUserStateChangedEventMessage != null) {
                    sendOnReceiveVideoPrivateUsersStateChangedEvent(str, parseVideoPrivateUserStateChangedEventMessage);
                    return;
                }
                return;
            case 113:
            default:
                return;
            case 114:
                UserInfo parseVideoPrivateUserTreadmillConnectedEventMessage = TreadlyEventHelper.parseVideoPrivateUserTreadmillConnectedEventMessage(str2);
                if (parseVideoPrivateUserTreadmillConnectedEventMessage != null) {
                    sendOnReceiveVideoPrivateUserTreadmillConnected(str, parseVideoPrivateUserTreadmillConnectedEventMessage);
                    return;
                }
                return;
            case 115:
                UserInfo parseVideoPrivateUserTreadmillNotConnectedEventMessage = TreadlyEventHelper.parseVideoPrivateUserTreadmillNotConnectedEventMessage(str2);
                if (parseVideoPrivateUserTreadmillNotConnectedEventMessage != null) {
                    sendOnReceiveVideoPrivateUserTreadmillNotConnected(str, parseVideoPrivateUserTreadmillNotConnectedEventMessage);
                    return;
                }
                return;
        }
    }

    public void addDelegate(TreadlyVideoPrivateEventDelegate treadlyVideoPrivateEventDelegate) {
        delegates.add(treadlyVideoPrivateEventDelegate);
    }

    public void removeDelegate(TreadlyVideoPrivateEventDelegate treadlyVideoPrivateEventDelegate) {
        Iterator<TreadlyVideoPrivateEventDelegate> it = delegates.iterator();
        while (it.hasNext()) {
            if (it.next() == treadlyVideoPrivateEventDelegate) {
                it.remove();
            }
        }
    }

    public void join(String str) {
        UserInfo userInfo = TreadlyServiceManager.getInstance().getUserInfo();
        if (userInfo == null) {
            return;
        }
        String createJoinVideoPrivateEventMessage = TreadlyEventHelper.createJoinVideoPrivateEventMessage(userInfo);
        if (createJoinVideoPrivateEventMessage.isEmpty()) {
            return;
        }
        String privateVideoTopic = getPrivateVideoTopic(str);
        TreadlyEventHelper.subscribeTopic(privateVideoTopic);
        TreadlyEventHelper.publishMessage(privateVideoTopic, createJoinVideoPrivateEventMessage);
        this.mJoinSessionId = str;
    }

    public void leave(String str) {
        UserInfo userInfo = TreadlyServiceManager.getInstance().getUserInfo();
        if (userInfo == null) {
            return;
        }
        String createLeaveVideoPrivateEventMessage = TreadlyEventHelper.createLeaveVideoPrivateEventMessage(userInfo);
        if (createLeaveVideoPrivateEventMessage.isEmpty()) {
            return;
        }
        String privateVideoTopic = getPrivateVideoTopic(str);
        TreadlyEventHelper.publishMessage(privateVideoTopic, createLeaveVideoPrivateEventMessage);
        TreadlyEventHelper.unsubscribeTopic(privateVideoTopic);
        this.mJoinSessionId = null;
    }

    public void pause(String str) {
        String createPauseVideoPrivateEventMessage;
        UserInfo userInfo = TreadlyServiceManager.getInstance().getUserInfo();
        if (userInfo == null || (createPauseVideoPrivateEventMessage = TreadlyEventHelper.createPauseVideoPrivateEventMessage(userInfo)) == null) {
            return;
        }
        TreadlyEventHelper.publishMessage(getPrivateVideoTopic(str), createPauseVideoPrivateEventMessage);
    }

    public void unpause(String str) {
        String createUnpauseVideoPrivateEventMessage;
        UserInfo userInfo = TreadlyServiceManager.getInstance().getUserInfo();
        if (userInfo == null || (createUnpauseVideoPrivateEventMessage = TreadlyEventHelper.createUnpauseVideoPrivateEventMessage(userInfo)) == null) {
            return;
        }
        TreadlyEventHelper.publishMessage(getPrivateVideoTopic(str), createUnpauseVideoPrivateEventMessage);
    }

    public void sendCurrentUserStats(String str, VideoServiceUserStatsInfo videoServiceUserStatsInfo) {
        String createVideoPrivateUserStatsEventMessage = TreadlyEventHelper.createVideoPrivateUserStatsEventMessage(videoServiceUserStatsInfo);
        if (createVideoPrivateUserStatsEventMessage != null) {
            String privateVideoTopic = getPrivateVideoTopic(str);
            Log.d("Private::sendCurrentStats", "message: " + createVideoPrivateUserStatsEventMessage);
            TreadlyEventHelper.publishMessage(privateVideoTopic, createVideoPrivateUserStatsEventMessage);
        }
    }

    public void sendTrainerModeRequest(String str, TrainerModeState trainerModeState) {
        String createTrainerModeRequestEventMessage;
        UserInfo userInfo = TreadlyServiceManager.getInstance().getUserInfo();
        if (userInfo == null || (createTrainerModeRequestEventMessage = TreadlyEventHelper.createTrainerModeRequestEventMessage(trainerModeState, userInfo)) == null) {
            return;
        }
        TreadlyEventHelper.publishMessage(getPrivateVideoTopic(str), createTrainerModeRequestEventMessage);
    }

    public void sendTrainerModeResponse(String str, TrainerModeState trainerModeState) {
        String createTrainerModeResponseEventMessage;
        UserInfo userInfo = TreadlyServiceManager.getInstance().getUserInfo();
        if (userInfo == null || (createTrainerModeResponseEventMessage = TreadlyEventHelper.createTrainerModeResponseEventMessage(trainerModeState, userInfo)) == null) {
            return;
        }
        TreadlyEventHelper.publishMessage(getPrivateVideoTopic(str), createTrainerModeResponseEventMessage);
    }

    public void sendStudentModeRequest(String str, TrainerModeState trainerModeState, String str2) {
        String createStudentModeRequestEventMessage;
        UserInfo userInfo = TreadlyServiceManager.getInstance().getUserInfo();
        if (userInfo == null || (createStudentModeRequestEventMessage = TreadlyEventHelper.createStudentModeRequestEventMessage(trainerModeState, userInfo, str2)) == null) {
            return;
        }
        TreadlyEventHelper.publishMessage(getPrivateVideoTopic(str), createStudentModeRequestEventMessage);
    }

    public void sendStudentModeResponse(String str, TrainerModeState trainerModeState, String str2) {
        String createStudentModeResponseEventMessage;
        UserInfo userInfo = TreadlyServiceManager.getInstance().getUserInfo();
        if (userInfo == null || (createStudentModeResponseEventMessage = TreadlyEventHelper.createStudentModeResponseEventMessage(trainerModeState, userInfo, str2)) == null) {
            return;
        }
        TreadlyEventHelper.publishMessage(getPrivateVideoTopic(str), createStudentModeResponseEventMessage);
    }

    public void sendUsersTrainerModes(String str, List<UsersTrainerModes> list) {
        String createVideoPrivateUsersTrainerModesEventMessage = TreadlyEventHelper.createVideoPrivateUsersTrainerModesEventMessage(list);
        if (createVideoPrivateUsersTrainerModesEventMessage != null) {
            TreadlyEventHelper.publishMessage(getPrivateVideoTopic(str), createVideoPrivateUsersTrainerModesEventMessage);
        }
    }

    public void sendUserConnecting(String str, String str2) {
        String createVideoPrivateUserConnectingEventMessage;
        UserInfo friendsInfoById = TreadlyServiceManager.getInstance().getFriendsInfoById(str2);
        if (friendsInfoById == null || (createVideoPrivateUserConnectingEventMessage = TreadlyEventHelper.createVideoPrivateUserConnectingEventMessage(friendsInfoById)) == null) {
            return;
        }
        TreadlyEventHelper.publishMessage(getPrivateVideoTopic(str), createVideoPrivateUserConnectingEventMessage);
    }

    public void sendTreadmillConnected(String str) {
        String createVideoPrivateUserTreadmillConnectedEventMessage;
        UserInfo userInfo = TreadlyServiceManager.getInstance().getUserInfo();
        if (userInfo == null || (createVideoPrivateUserTreadmillConnectedEventMessage = TreadlyEventHelper.createVideoPrivateUserTreadmillConnectedEventMessage(userInfo)) == null) {
            return;
        }
        TreadlyEventHelper.publishMessage(getPrivateVideoTopic(str), createVideoPrivateUserTreadmillConnectedEventMessage);
    }

    public void sendTreadmillNotConnected(String str) {
        String createVideoPrivateUserTreadmillNotConnectedEventMessage;
        UserInfo userInfo = TreadlyServiceManager.getInstance().getUserInfo();
        if (userInfo == null || (createVideoPrivateUserTreadmillNotConnectedEventMessage = TreadlyEventHelper.createVideoPrivateUserTreadmillNotConnectedEventMessage(userInfo)) == null) {
            return;
        }
        TreadlyEventHelper.publishMessage(getPrivateVideoTopic(str), createVideoPrivateUserTreadmillNotConnectedEventMessage);
    }

    private void sendOnUserJoinVideoPrivateEvent(String str, UserInfo userInfo) {
        for (TreadlyVideoPrivateEventDelegate treadlyVideoPrivateEventDelegate : delegates) {
            treadlyVideoPrivateEventDelegate.onUserJoinVideoPrivateEvent(str, userInfo);
        }
    }

    private void sendOnUserLeaveVideoPrivateEvent(String str, UserInfo userInfo) {
        for (TreadlyVideoPrivateEventDelegate treadlyVideoPrivateEventDelegate : delegates) {
            treadlyVideoPrivateEventDelegate.onUserLeaveVideoPrivateEvent(str, userInfo);
        }
    }

    private void sendOnUserPauseVideoPrivate(String str, UserInfo userInfo) {
        for (TreadlyVideoPrivateEventDelegate treadlyVideoPrivateEventDelegate : delegates) {
            treadlyVideoPrivateEventDelegate.onUserPauseVideoPrivateEvent(str, userInfo);
        }
    }

    private void sendOnUserUnpauseVideoPrivate(String str, UserInfo userInfo) {
        for (TreadlyVideoPrivateEventDelegate treadlyVideoPrivateEventDelegate : delegates) {
            treadlyVideoPrivateEventDelegate.onUserUnpauseVideoPrivateEvent(str, userInfo);
        }
    }

    private void sendOnReceiveVideoPrivateUserStatsEvent(String str, VideoServiceUserStatsInfo videoServiceUserStatsInfo) {
        for (TreadlyVideoPrivateEventDelegate treadlyVideoPrivateEventDelegate : delegates) {
            treadlyVideoPrivateEventDelegate.onReceiveVideoPrivateUserStatsEvent(str, videoServiceUserStatsInfo);
        }
    }

    private void sendOnReceiveVideoPrivateTrainerModeRequestEvent(String str, UserTrainerMode userTrainerMode) {
        for (TreadlyVideoPrivateEventDelegate treadlyVideoPrivateEventDelegate : delegates) {
            treadlyVideoPrivateEventDelegate.onReceiveVideoPrivateTrainerModeRequest(str, userTrainerMode);
        }
    }

    private void sendOnReceiveVideoPrivateTrainerModeResponseEvent(String str, UserTrainerMode userTrainerMode) {
        for (TreadlyVideoPrivateEventDelegate treadlyVideoPrivateEventDelegate : delegates) {
            treadlyVideoPrivateEventDelegate.onReceiveVideoPrivateTrainerModeResponse(str, userTrainerMode);
        }
    }

    private void sendOnReceiveVideoPrivateStudentModeRequestEvent(String str, UserTrainerMode userTrainerMode, String str2) {
        for (TreadlyVideoPrivateEventDelegate treadlyVideoPrivateEventDelegate : delegates) {
            treadlyVideoPrivateEventDelegate.onReceiveVideoPrivateStudentModeRequest(str, userTrainerMode, str2);
        }
    }

    private void sendOnReceiveVideoPrivateStudentModeResponseEvent(String str, UserTrainerMode userTrainerMode, String str2) {
        for (TreadlyVideoPrivateEventDelegate treadlyVideoPrivateEventDelegate : delegates) {
            treadlyVideoPrivateEventDelegate.onReceiveVideoPrivateStudentModeResponse(str, userTrainerMode, str2);
        }
    }

    private void sendOnReceiveVideoPrivateUsersTrainerModesEvent(String str, List<UsersTrainerModes> list) {
        for (TreadlyVideoPrivateEventDelegate treadlyVideoPrivateEventDelegate : delegates) {
            treadlyVideoPrivateEventDelegate.onReceiveVideoPrivateUsersTrainerModes(str, list);
        }
    }

    private void sendOnReceiveVideoPrivateUserTreadmillConnected(String str, UserInfo userInfo) {
        for (TreadlyVideoPrivateEventDelegate treadlyVideoPrivateEventDelegate : delegates) {
            treadlyVideoPrivateEventDelegate.onReceiveVideoPrivateUserTreadmillConnected(str, userInfo);
        }
    }

    private void sendOnReceiveVideoPrivateUserTreadmillNotConnected(String str, UserInfo userInfo) {
        for (TreadlyVideoPrivateEventDelegate treadlyVideoPrivateEventDelegate : delegates) {
            treadlyVideoPrivateEventDelegate.onReceiveVideoPrivateUserTreadmillNotConnected(str, userInfo);
        }
    }

    private void sendOnReceiveVideoPrivateClientForceDisconnectEvent(String str) {
        for (TreadlyVideoPrivateEventDelegate treadlyVideoPrivateEventDelegate : delegates) {
            treadlyVideoPrivateEventDelegate.onReceiveVideoPrivateClientForceDisconnectEvent(str);
        }
    }

    private void sendOnReceiveVideoPrivateUsersStateChangedEvent(String str, ArrayList<UserVideoPrivateStateInfo> arrayList) {
        for (TreadlyVideoPrivateEventDelegate treadlyVideoPrivateEventDelegate : delegates) {
            treadlyVideoPrivateEventDelegate.onReceiveVideoPrivateUsersStateChanged(str, arrayList);
        }
    }

    private String getPrivateVideoTopic(String str) {
        return String.format("%s/%s/%s", TreadlyEventHelper.BASE_EVENT_TOPIC, TreadlyEventHelper.VIDEO_PRIVATE_EVENT_TOPIC, str);
    }
}
