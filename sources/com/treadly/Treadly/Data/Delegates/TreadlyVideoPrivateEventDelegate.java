package com.treadly.Treadly.Data.Delegates;

import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.Data.Model.UserTrainerMode;
import com.treadly.Treadly.Data.Model.UserVideoPrivateStateInfo;
import com.treadly.Treadly.Data.Model.UsersTrainerModes;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceUserStatsInfo;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public interface TreadlyVideoPrivateEventDelegate {
    void onReceiveVideoPrivateClientForceDisconnectEvent(String str);

    void onReceiveVideoPrivateStudentModeRequest(String str, UserTrainerMode userTrainerMode, String str2);

    void onReceiveVideoPrivateStudentModeResponse(String str, UserTrainerMode userTrainerMode, String str2);

    void onReceiveVideoPrivateTrainerModeRequest(String str, UserTrainerMode userTrainerMode);

    void onReceiveVideoPrivateTrainerModeResponse(String str, UserTrainerMode userTrainerMode);

    void onReceiveVideoPrivateUserStatsEvent(String str, VideoServiceUserStatsInfo videoServiceUserStatsInfo);

    void onReceiveVideoPrivateUserTreadmillConnected(String str, UserInfo userInfo);

    void onReceiveVideoPrivateUserTreadmillNotConnected(String str, UserInfo userInfo);

    void onReceiveVideoPrivateUsersStateChanged(String str, ArrayList<UserVideoPrivateStateInfo> arrayList);

    void onReceiveVideoPrivateUsersTrainerModes(String str, List<UsersTrainerModes> list);

    void onUserJoinVideoPrivateEvent(String str, UserInfo userInfo);

    void onUserLeaveVideoPrivateEvent(String str, UserInfo userInfo);

    void onUserPauseVideoPrivateEvent(String str, UserInfo userInfo);

    void onUserUnpauseVideoPrivateEvent(String str, UserInfo userInfo);
}
