package com.treadly.Treadly.Data.Delegates;

import com.treadly.Treadly.Data.Model.UserComment;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceUserStatsInfo;

/* loaded from: classes.dex */
public class TreadlyVideoBroadcastEventAdapter implements TreadlyVideoBroadcastEventDelegate {
    @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoBroadcastEventDelegate
    public void onJoinedVideoBroadcast(String str) {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoBroadcastEventDelegate
    public void onReceiveVideoBroadcastLike(String str, int i) {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoBroadcastEventDelegate
    public void onReceiveVideoBroadcastUserComment(String str, UserComment userComment) {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoBroadcastEventDelegate
    public void onReceiveVideoBroadcastUserStats(String str, VideoServiceUserStatsInfo videoServiceUserStatsInfo) {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoBroadcastEventDelegate
    public void onUserJoinVideoBroadcast(String str, UserInfo userInfo) {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoBroadcastEventDelegate
    public void onUserLeaveVideoBroadcast(String str, UserInfo userInfo) {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoBroadcastEventDelegate
    public void onVideoBroadcastEnded(String str) {
    }
}
