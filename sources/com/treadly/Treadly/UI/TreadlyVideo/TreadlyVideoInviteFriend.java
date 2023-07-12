package com.treadly.Treadly.UI.TreadlyVideo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.treadly.Treadly.Data.Listeners.TreadlyVideoInviteListener;
import com.treadly.Treadly.Data.Model.UserInfo;
import java.util.List;

/* loaded from: classes2.dex */
public class TreadlyVideoInviteFriend extends DialogFragment {
    private List<UserInfo> friendsList = null;
    private CharSequence[] friendsListNames = null;
    private TreadlyVideoInviteListener listener;

    @Override // androidx.fragment.app.DialogFragment
    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Invite Friend").setItems(this.friendsListNames, new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.TreadlyVideoInviteFriend.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                for (UserInfo userInfo : TreadlyVideoInviteFriend.this.friendsList) {
                    if (userInfo.name.equals(TreadlyVideoInviteFriend.this.friendsListNames[i]) && TreadlyVideoInviteFriend.this.listener != null) {
                        TreadlyVideoInviteFriend.this.listener.onResponse(userInfo);
                    }
                }
            }
        });
        return builder.create();
    }

    public void setFriendsList(List<UserInfo> list) {
        this.friendsListNames = new CharSequence[list.size()];
        int i = 0;
        for (UserInfo userInfo : list) {
            this.friendsListNames[i] = userInfo.name;
            i++;
        }
        this.friendsList = list;
    }

    public void setOnClickListener(TreadlyVideoInviteListener treadlyVideoInviteListener) {
        this.listener = treadlyVideoInviteListener;
    }
}
