package com.treadly.Treadly.Data.Model;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class BuddyProfileInfo {
    public String audioPath;
    public String avatarPath;
    public List<BuddyDayTime> daysOfWeek;
    public int groupCount;
    public String id;
    public List<BuddyInterest> interests;
    public String location;
    public String lookingForMessage;
    public String lookingForMessageShort;
    public List<MemberInfo> participants;
    public int step;
    public List<BuddyDayTimeType> times;
    public String userId;
    public String userName;
    public String videoPath;
    public String videoThumbnailPath;

    public String avatarURL() {
        return (this.avatarPath != null || this.avatarPath.isEmpty()) ? this.avatarPath : "https://dgwxv5s2i5zkb.cloudfront.net/avatar/default.png";
    }

    public BuddyProfileInfo(String str, String str2, String str3) {
        this.userId = str;
        this.userName = str2;
        this.avatarPath = str3;
        this.id = "";
        this.step = 0;
        this.groupCount = 0;
        this.lookingForMessage = "";
        this.lookingForMessageShort = "";
        this.location = "";
        this.interests = new ArrayList();
        this.audioPath = "";
        this.videoPath = "";
        this.videoThumbnailPath = "";
        this.daysOfWeek = new ArrayList();
        this.times = new ArrayList();
        this.participants = new ArrayList();
    }

    public BuddyProfileInfo(String str, String str2, String str3, String str4, int i, int i2, String str5, String str6, String str7, String str8, String str9, String str10, List<BuddyInterest> list, List<BuddyDayTime> list2, List<BuddyDayTimeType> list3, List<MemberInfo> list4) {
        this.id = str;
        this.userId = str2;
        this.userName = str3;
        this.avatarPath = str4;
        this.step = i;
        this.groupCount = i2;
        this.lookingForMessage = str5;
        this.lookingForMessageShort = str6;
        this.location = str7;
        this.audioPath = str8;
        this.videoPath = str9;
        this.videoThumbnailPath = str10;
        this.interests = list;
        this.daysOfWeek = list2;
        this.times = list3;
        this.participants = list4;
    }

    /* loaded from: classes2.dex */
    public static class BuddyInterest {
        public String id;
        public String title;

        public BuddyInterest(String str, String str2) {
            this.title = str;
            this.id = str2;
        }
    }

    /* loaded from: classes2.dex */
    public enum BuddyTime {
        earlyMorning(0),
        morning(1),
        noonTime(2),
        afterNoon(3),
        evening(4);
        
        private final int value;

        BuddyTime(int i) {
            this.value = i;
        }

        public int getValue() {
            return this.value;
        }

        public static BuddyTime fromValue(int i) {
            BuddyTime[] values;
            for (BuddyTime buddyTime : values()) {
                if (buddyTime.value == i) {
                    return buddyTime;
                }
            }
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static class BuddyDayTimeType {
        public String id;
        public String title;

        public BuddyDayTimeType(String str, String str2) {
            this.title = str;
            this.id = str2;
        }
    }

    /* loaded from: classes2.dex */
    public static class BuddyDayTime {
        public int dayOfWeek;
        public BuddyDayTimeType dayTime;
        public String id;

        public BuddyDayTime(String str, int i, BuddyDayTimeType buddyDayTimeType) {
            this.id = str;
            this.dayOfWeek = i;
            this.dayTime = buddyDayTimeType;
        }
    }
}
