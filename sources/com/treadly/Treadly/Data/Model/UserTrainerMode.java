package com.treadly.Treadly.Data.Model;

/* loaded from: classes2.dex */
public class UserTrainerMode {
    public TrainerModeState state;
    public UserInfo user;

    public UserTrainerMode(UserInfo userInfo, TrainerModeState trainerModeState) {
        this.user = userInfo;
        this.state = trainerModeState;
    }
}
