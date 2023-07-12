package com.treadly.Treadly.Data.Model;

/* loaded from: classes2.dex */
public class WorkoutInfo {
    public boolean allowComments;
    public boolean allowVoiceMessage;
    public String description;
    public StreamPermission permissionJoin;
    public StreamPermission permissionRecord;
    public String userId;

    public WorkoutInfo(String str, String str2, StreamPermission streamPermission, StreamPermission streamPermission2, boolean z, boolean z2) {
        this.userId = str;
        this.description = str2;
        this.permissionJoin = streamPermission;
        this.permissionRecord = streamPermission2;
        this.allowComments = z;
        this.allowVoiceMessage = z2;
    }
}
