package com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents;

import android.util.Log;
import android.view.View;
import com.opentok.android.Publisher;
import com.opentok.android.Subscriber;
import com.treadly.Treadly.Data.Model.UserVideoPrivateStateInfo;

/* loaded from: classes2.dex */
public class TreadlyPrivateVideoSubscriber {
    public String id;
    public boolean isHost;
    private TreadlyPrivateStreamPauseView pauseView;
    private Publisher publisher;
    private Subscriber subscriber;
    private View view;
    private UserVideoPrivateStateInfo userInfo = null;
    private int resId = -1;

    public void setSubscriber(Subscriber subscriber) {
        if (subscriber == null) {
            return;
        }
        Log.i("private", "setSubscriber");
        this.subscriber = subscriber;
        setView(this.subscriber.getView());
    }

    public Subscriber getSubscriber() {
        return this.subscriber;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
        setView(this.publisher.getView());
    }

    public Publisher getPublisher() {
        return this.publisher;
    }

    public void setView(View view) {
        View view2 = this.view;
        this.view = view;
        if (this.resId <= -1 || this.view == null) {
            return;
        }
        Log.d("private", "resId: " + this.resId);
        this.view.setId(this.resId);
    }

    public View getView() {
        return this.view;
    }

    public TreadlyPrivateStreamPauseView getPauseView() {
        return this.pauseView;
    }

    public void setUserInfo(UserVideoPrivateStateInfo userVideoPrivateStateInfo) {
        this.userInfo = userVideoPrivateStateInfo;
    }

    public UserVideoPrivateStateInfo getUserInfo() {
        return this.userInfo;
    }

    public void setResId(int i) {
        this.resId = i;
        if (this.view != null) {
            this.view.setId(this.resId);
        }
    }

    public int getResId() {
        return this.resId;
    }

    public TreadlyPrivateVideoSubscriber(String str, boolean z) {
        this.id = str;
        this.isHost = z;
    }

    public TreadlyPrivateVideoSubscriber(UserVideoPrivateStateInfo userVideoPrivateStateInfo, boolean z) {
        this.id = userVideoPrivateStateInfo.id;
        this.isHost = z;
        setUserInfo(userVideoPrivateStateInfo);
    }

    public TreadlyPrivateVideoSubscriber(String str, Subscriber subscriber, boolean z) {
        this.id = str;
        this.isHost = z;
        setSubscriber(subscriber);
    }

    public TreadlyPrivateVideoSubscriber(String str, Publisher publisher, boolean z) {
        this.id = str;
        this.isHost = z;
        setPublisher(publisher);
    }

    private void updateState() {
        if (this.userInfo == null) {
            return;
        }
        if (this.userInfo.status.equals("connecting")) {
            Log.i("private", "connecting state");
            dismissPauseView();
        } else if (this.userInfo.status.equals("paused") && this.pauseView == null) {
            Log.i("private", "paused state");
            addPauseView();
        } else if (this.userInfo.status.equals("active")) {
            Log.i("private", "active state");
            dismissPauseView();
        }
    }

    public void addPauseView() {
        dismissPauseView();
        if (this.view == null || this.resId == -1) {
            return;
        }
        Log.d("private", "addPauseView resId: " + this.resId);
        this.pauseView = new TreadlyPrivateStreamPauseView(this.view.getContext());
        this.pauseView.setAvatar(this.userInfo.avatarURL());
        this.pauseView.setId(this.resId);
        this.pauseView.bringToFront();
    }

    public void dismissPauseView() {
        if (this.pauseView != null) {
            this.pauseView = null;
        }
    }
}
