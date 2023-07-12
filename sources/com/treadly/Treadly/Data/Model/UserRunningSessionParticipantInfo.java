package com.treadly.Treadly.Data.Model;

import java.util.Date;

/* loaded from: classes2.dex */
public class UserRunningSessionParticipantInfo {
    public Date joinedAt;
    public String participantAvatar;
    public String participantId;
    public String participantName;

    public UserRunningSessionParticipantInfo(Date date, String str) {
        this.joinedAt = date;
        this.participantId = str;
        this.participantName = null;
        this.participantAvatar = null;
    }

    public UserRunningSessionParticipantInfo(Date date, String str, String str2, String str3) {
        this.joinedAt = date;
        this.participantId = str;
        this.participantName = str2;
        this.participantAvatar = str3;
    }
}
