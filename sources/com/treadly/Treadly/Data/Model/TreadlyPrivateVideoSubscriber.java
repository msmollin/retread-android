package com.treadly.Treadly.Data.Model;

import com.opentok.android.Publisher;
import com.opentok.android.Subscriber;

/* loaded from: classes2.dex */
public class TreadlyPrivateVideoSubscriber {
    public String id;
    public boolean isHost;
    public Publisher publisher;
    public Subscriber subscriber;
    public UserVideoPrivateStateInfo userInfo;

    public TreadlyPrivateVideoSubscriber(String str, boolean z) {
        this.id = "";
        this.isHost = false;
        this.userInfo = null;
        this.id = str;
        this.isHost = z;
    }

    public TreadlyPrivateVideoSubscriber(UserVideoPrivateStateInfo userVideoPrivateStateInfo, boolean z) {
        this.id = "";
        this.isHost = false;
        this.userInfo = null;
        this.id = userVideoPrivateStateInfo.id;
        this.isHost = z;
        this.userInfo = userVideoPrivateStateInfo;
        updateState();
    }

    public TreadlyPrivateVideoSubscriber(String str, Subscriber subscriber, boolean z) {
        this.id = "";
        this.isHost = false;
        this.userInfo = null;
        this.id = str;
        this.isHost = z;
        this.subscriber = subscriber;
    }

    public TreadlyPrivateVideoSubscriber(String str, Publisher publisher, boolean z) {
        this.id = "";
        this.isHost = false;
        this.userInfo = null;
        this.id = str;
        this.isHost = z;
        this.publisher = publisher;
    }

    public void updateState() {
        UserVideoPrivateStateInfo userVideoPrivateStateInfo = this.userInfo;
        if (userVideoPrivateStateInfo == null || userVideoPrivateStateInfo.status.equals("connecting") || userVideoPrivateStateInfo.status.equals("paused")) {
            return;
        }
        userVideoPrivateStateInfo.status.equals("active");
    }
}
