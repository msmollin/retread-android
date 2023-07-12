package com.treadly.Treadly.Data.Model;

/* loaded from: classes2.dex */
public class UsersTrainerModes {
    public TrainerModeState studentModeState;
    public TrainerModeEnabledState trainerModeEnabled = TrainerModeEnabledState.unknown;
    public TrainerModeState trainerModeState;
    public UserInfo user;

    public UsersTrainerModes(UserInfo userInfo, TrainerModeState trainerModeState, TrainerModeState trainerModeState2) {
        this.user = userInfo;
        this.trainerModeState = trainerModeState;
        this.studentModeState = trainerModeState2;
    }
}
