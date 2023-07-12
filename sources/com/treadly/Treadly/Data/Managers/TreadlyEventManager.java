package com.treadly.Treadly.Data.Managers;

import com.treadly.Treadly.Data.Delegates.MqttConnectionManagerDelegate;
import com.treadly.Treadly.Data.Delegates.TreadlyUserEventDelegate;
import com.treadly.Treadly.Data.Delegates.TreadlyVideoBroadcastEventDelegate;
import com.treadly.Treadly.Data.Delegates.TreadlyVideoPrivateEventDelegate;
import com.treadly.Treadly.Data.Managers.TreadlyVideoArchiveEventManager;
import com.treadly.Treadly.Data.Model.TrainerModeState;
import com.treadly.Treadly.Data.Model.UsersTrainerModes;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceUserStatsInfo;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.paho.client.mqttv3.MqttToken;

/* loaded from: classes2.dex */
public class TreadlyEventManager implements MqttConnectionManagerDelegate {
    private static TreadlyEventManager instance = new TreadlyEventManager();
    private TreadlyUserEventManager userEventManager = new TreadlyUserEventManager();
    private TreadlyRunningSessionEventManager runningSessionEventManager = new TreadlyRunningSessionEventManager();
    private TreadlyVideoBroadcastEventManager videoBroadcastEventManager = new TreadlyVideoBroadcastEventManager();
    private TreadlyVideoPrivateEventManager videoPrivateEventManager = new TreadlyVideoPrivateEventManager();
    private TreadlyVideoArchiveEventManager videoArchiveEventManager = new TreadlyVideoArchiveEventManager();
    private List<TreadlyUserEventDelegate> delegates = new ArrayList();

    public static TreadlyEventManager getInstance() {
        return instance;
    }

    private TreadlyEventManager() {
        MqttConnectionManager.getInstance().addDelegate(this);
    }

    public void startRunningSessionWithParticipants(ArrayList<String> arrayList) {
        this.runningSessionEventManager.startWithParticipants(arrayList);
    }

    public void addRunningSessionParticipants(ArrayList<String> arrayList) {
        this.runningSessionEventManager.startWithParticipants(arrayList);
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x0048, code lost:
        if (r2.equals(com.treadly.Treadly.Data.Managers.TreadlyEventHelper.USER_EVENT_TOPIC) != false) goto L16;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void processMessage(java.lang.String r8, java.lang.String r9) {
        /*
            r7 = this;
            java.lang.String[] r8 = com.treadly.Treadly.Data.Managers.TreadlyEventHelper.getTopicLevels(r8)
            r0 = 0
            r1 = r8[r0]
            java.lang.String r2 = "treadly"
            boolean r1 = r1.equals(r2)
            if (r1 != 0) goto L10
            return
        L10:
            r1 = 1
            r2 = r8[r1]
            r3 = -1
            int r4 = r2.hashCode()
            r5 = -2126307160(0xffffffff814320a8, float:-3.5839273E-38)
            r6 = 2
            if (r4 == r5) goto L4b
            r5 = 3599307(0x36ebcb, float:5.043703E-39)
            if (r4 == r5) goto L42
            r0 = 1735056359(0x676adbe7, float:1.10909024E24)
            if (r4 == r0) goto L38
            r0 = 1873624740(0x6fad3ea4, float:1.0723327E29)
            if (r4 == r0) goto L2e
            goto L55
        L2e:
            java.lang.String r0 = "videopublic"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L55
            r0 = r1
            goto L56
        L38:
            java.lang.String r0 = "videoarchive"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L55
            r0 = 3
            goto L56
        L42:
            java.lang.String r1 = "user"
            boolean r1 = r2.equals(r1)
            if (r1 == 0) goto L55
            goto L56
        L4b:
            java.lang.String r0 = "videoprivate"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L55
            r0 = r6
            goto L56
        L55:
            r0 = r3
        L56:
            switch(r0) {
                case 0: goto L72;
                case 1: goto L6a;
                case 2: goto L62;
                case 3: goto L5a;
                default: goto L59;
            }
        L59:
            goto L7b
        L5a:
            com.treadly.Treadly.Data.Managers.TreadlyVideoArchiveEventManager r7 = r7.videoArchiveEventManager
            r8 = r8[r6]
            r7.handleMessage(r8, r9)
            goto L7b
        L62:
            com.treadly.Treadly.Data.Managers.TreadlyVideoPrivateEventManager r7 = r7.videoPrivateEventManager
            r8 = r8[r6]
            r7.handleMessage(r8, r9)
            goto L7b
        L6a:
            com.treadly.Treadly.Data.Managers.TreadlyVideoBroadcastEventManager r7 = r7.videoBroadcastEventManager
            r8 = r8[r6]
            r7.handleMessage(r8, r9)
            goto L7b
        L72:
            com.treadly.Treadly.Data.Managers.TreadlyUserEventManager r7 = com.treadly.Treadly.Data.Managers.TreadlyUserEventManager.getInstance()
            r8 = r8[r6]
            r7.handleMessage(r8, r9)
        L7b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.treadly.Treadly.Data.Managers.TreadlyEventManager.processMessage(java.lang.String, java.lang.String):void");
    }

    @Override // com.treadly.Treadly.Data.Delegates.MqttConnectionManagerDelegate
    public void prepareSubscriptions() {
        this.videoBroadcastEventManager.handlePrepareSubscriptions();
        this.videoArchiveEventManager.handlePrepareSubscriptions();
    }

    @Override // com.treadly.Treadly.Data.Delegates.MqttConnectionManagerDelegate
    public void onReceiveMessage(String str, String str2) {
        processMessage(str, str2);
    }

    @Override // com.treadly.Treadly.Data.Delegates.MqttConnectionManagerDelegate
    public void onPublishMessageSent(MqttToken mqttToken) {
        this.videoBroadcastEventManager.handleMessageSent(mqttToken);
    }

    public void addDelegate(TreadlyUserEventDelegate treadlyUserEventDelegate) {
        this.delegates.add(treadlyUserEventDelegate);
    }

    public void removeDelegate(TreadlyUserEventDelegate treadlyUserEventDelegate) {
        this.delegates.remove(treadlyUserEventDelegate);
    }

    public void addVideoBroadcastDelegate(TreadlyVideoBroadcastEventDelegate treadlyVideoBroadcastEventDelegate) {
        this.videoBroadcastEventManager.addDelegate(treadlyVideoBroadcastEventDelegate);
    }

    public void removeVideoBroadcastDelegate(TreadlyVideoBroadcastEventDelegate treadlyVideoBroadcastEventDelegate) {
        this.videoBroadcastEventManager.removeDelegate(treadlyVideoBroadcastEventDelegate);
    }

    public void joinVideoBroadcast(String str) {
        this.videoBroadcastEventManager.join(str);
    }

    public void leaveVideoBroadcast(String str) {
        this.videoBroadcastEventManager.leave(str);
    }

    public void sendVideoBroadcastComment(String str, String str2) {
        this.videoBroadcastEventManager.sendComment(str, str2);
    }

    public void sendVideoBroadcastEnded(String str) {
        this.videoBroadcastEventManager.sendBroadcastEnded(str);
    }

    public void sendVideoBroadcastCurrentUserStats(String str, VideoServiceUserStatsInfo videoServiceUserStatsInfo) {
        this.videoBroadcastEventManager.sendCurrentUserStats(str, videoServiceUserStatsInfo);
    }

    public void addUserDelegate(TreadlyUserEventDelegate treadlyUserEventDelegate) {
        this.userEventManager.addDelegate(treadlyUserEventDelegate);
    }

    public void removeUserDelegate(TreadlyUserEventDelegate treadlyUserEventDelegate) {
        this.userEventManager.removeDelegate(treadlyUserEventDelegate);
    }

    public void sendUserBroadcastEnded() {
        this.userEventManager.sendBroadcastEnded();
    }

    public void sendUserBroadcastStarted() {
        this.userEventManager.sendBroadcastStarted();
    }

    public void sendUserFriendAdded(String str) {
        this.userEventManager.sendFriendAdded(str);
    }

    public void sendUserVideoBoardcastInviteRequest(String str) {
        this.userEventManager.sendVideoBoardcastInviteRequest(str);
    }

    public void sendUserVideoPrivateInviteRequest(String str) {
        this.userEventManager.sendVideoPrivateInviteRequest(str);
    }

    public void sendUserVideoPrivateInviteDeleted(String str) {
        this.userEventManager.sendVideoPrivateInviteDeleted(str);
    }

    public void sendUserVideoPrivateInviteDeclined(String str) {
        this.userEventManager.sendVideoPrivateInviteDeclined(str);
    }

    public void addVideoPrivateDelegate(TreadlyVideoPrivateEventDelegate treadlyVideoPrivateEventDelegate) {
        this.videoPrivateEventManager.addDelegate(treadlyVideoPrivateEventDelegate);
    }

    public void removeVideoPrivateDelegate(TreadlyVideoPrivateEventDelegate treadlyVideoPrivateEventDelegate) {
        this.videoPrivateEventManager.removeDelegate(treadlyVideoPrivateEventDelegate);
    }

    public void joinVideoPrivate(String str) {
        this.videoPrivateEventManager.join(str);
    }

    public void leaveVideoPrivate(String str) {
        this.videoPrivateEventManager.leave(str);
    }

    public void pauseVideoPrivate(String str) {
        this.videoPrivateEventManager.pause(str);
    }

    public void unpauseVideoPrivate(String str) {
        this.videoPrivateEventManager.unpause(str);
    }

    public void sendVideoPrivateCurrentUserStats(String str, VideoServiceUserStatsInfo videoServiceUserStatsInfo) {
        this.videoPrivateEventManager.sendCurrentUserStats(str, videoServiceUserStatsInfo);
    }

    public void sendVideoPrivateTrainerModeRequest(String str, TrainerModeState trainerModeState) {
        this.videoPrivateEventManager.sendTrainerModeRequest(str, trainerModeState);
    }

    public void sendVideoPrivateTrainerModeResponse(String str, TrainerModeState trainerModeState) {
        this.videoPrivateEventManager.sendTrainerModeResponse(str, trainerModeState);
    }

    public void sendVideoPrivateStudentModeRequest(String str, TrainerModeState trainerModeState, String str2) {
        this.videoPrivateEventManager.sendStudentModeRequest(str, trainerModeState, str2);
    }

    public void sendVideoPrivateStudentModeResponse(String str, TrainerModeState trainerModeState, String str2) {
        this.videoPrivateEventManager.sendStudentModeResponse(str, trainerModeState, str2);
    }

    public void sendVideoPrivateUsersTrainerModes(String str, List<UsersTrainerModes> list) {
        this.videoPrivateEventManager.sendUsersTrainerModes(str, list);
    }

    public void sendVideoPrivateUserConnecting(String str, String str2) {
        this.videoPrivateEventManager.sendUserConnecting(str, str2);
    }

    public void sendVideoPrivateTreadmillConnected(String str) {
        this.videoPrivateEventManager.sendTreadmillConnected(str);
    }

    public void sendVideoPrivateTreadmillNotConnected(String str) {
        this.videoPrivateEventManager.sendTreadmillNotConnected(str);
    }

    public void addVideoArchiveDelegate(TreadlyVideoArchiveEventManager.TreadlyVideoArchiveEventDelegate treadlyVideoArchiveEventDelegate) {
        this.videoArchiveEventManager.addDelegate(treadlyVideoArchiveEventDelegate);
    }

    public void removeVideoArchiveDelegate(TreadlyVideoArchiveEventManager.TreadlyVideoArchiveEventDelegate treadlyVideoArchiveEventDelegate) {
        this.videoArchiveEventManager.removeDelegate(treadlyVideoArchiveEventDelegate);
    }

    public void subscribeVideoArchive(String str) {
        this.videoArchiveEventManager.subscribe(str);
    }

    public void unsubscribeVideoArchive(String str) {
        this.videoArchiveEventManager.unsubscribe(str);
    }
}
