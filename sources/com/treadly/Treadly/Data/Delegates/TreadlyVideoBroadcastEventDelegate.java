package com.treadly.Treadly.Data.Delegates;

import com.treadly.Treadly.Data.Model.UserComment;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceUserStatsInfo;

/* loaded from: classes.dex */
public interface TreadlyVideoBroadcastEventDelegate {
    void onJoinedVideoBroadcast(String str);

    void onReceiveVideoBroadcastLike(String str, int i);

    void onReceiveVideoBroadcastUserComment(String str, UserComment userComment);

    void onReceiveVideoBroadcastUserStats(String str, VideoServiceUserStatsInfo videoServiceUserStatsInfo);

    void onUserJoinVideoBroadcast(String str, UserInfo userInfo);

    void onUserLeaveVideoBroadcast(String str, UserInfo userInfo);

    void onVideoBroadcastEnded(String str);
}
