package com.treadly.Treadly.Data.Managers;

import com.treadly.Treadly.Data.Model.UserInfo;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class TreadlyRunningSessionEventManager {
    public String currentSession = null;

    public void startWithParticipants(ArrayList<String> arrayList) {
        UserInfo userInfo = TreadlyServiceManager.getInstance().getUserInfo();
        if (userInfo == null) {
            return;
        }
        TreadlyEventHelper.createRunningSessionParticipantStartEventMessage(userInfo.id, arrayList);
    }
}
