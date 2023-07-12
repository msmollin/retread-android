package com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.PublicStreamViewers;

import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.Data.Model.UserInfo;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class TreadlyPublicStreamViewersController {
    PublicBroadcastViewersListener listener;
    public String sessionId;
    public List<UserInfo> viewers = new ArrayList();
    Map<String, UserInfo> viewersLookup = new HashMap();
    Map<String, UserInfo> viewersLeft = new HashMap();

    /* loaded from: classes2.dex */
    public interface PublicBroadcastViewersListener {
        void viewersDidChange(List<UserInfo> list);
    }

    public void setInitialViewers(List<UserInfo> list) {
        UserInfo userInfoById;
        this.viewersLookup.clear();
        for (UserInfo userInfo : list) {
            if (this.viewersLeft.get(userInfo.id) == null && (userInfoById = TreadlyServiceManager.getInstance().getUserInfoById(userInfo.id)) != null) {
                this.viewersLookup.put(userInfo.id, userInfoById);
            }
        }
        this.viewers = new ArrayList(this.viewersLookup.values());
    }

    public void userJoined(UserInfo userInfo) {
        UserInfo userInfoById = TreadlyServiceManager.getInstance().getUserInfoById(userInfo.id);
        if (userInfoById == null) {
            return;
        }
        PrintStream printStream = System.out;
        printStream.println("VIEWERS: viewer: " + userInfo.name);
        this.viewersLookup.put(userInfo.id, userInfoById);
        this.viewersLeft.remove(userInfo.id);
        this.viewers = new ArrayList(this.viewersLookup.values());
        PrintStream printStream2 = System.out;
        printStream2.println("VIEWERS: On join: " + this.viewers);
    }

    public void userLeft(UserInfo userInfo) {
        this.viewersLeft.put(userInfo.id, userInfo);
        this.viewersLookup.remove(userInfo.id);
        this.viewers = new ArrayList(this.viewersLookup.values());
        PrintStream printStream = System.out;
        printStream.println("VIEWERS: On leave: " + this.viewers);
    }
}
