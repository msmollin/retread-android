package com.treadly.Treadly.UI.TreadlyProfile.InviteFriends;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.Data.Model.FriendInviteInfo;
import com.treadly.Treadly.UI.Util.ActivityUtil;

/* loaded from: classes2.dex */
public class TreadlyInviteFriendsDialog extends AppCompatDialogFragment {
    public String message = null;
    public FriendInviteInfo invite = null;
    public boolean error = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$onCreateDialog$0(DialogInterface dialogInterface, int i) {
    }

    @Override // androidx.appcompat.app.AppCompatDialogFragment, androidx.fragment.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder message = new AlertDialog.Builder(getActivity()).setTitle("Friend Invite").setMessage(this.message);
        if (this.error || this.invite == null) {
            message.setNeutralButton("Dismiss", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.InviteFriends.-$$Lambda$TreadlyInviteFriendsDialog$dXM12JqlpmiEhfo_2Il71wprD2Q
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    TreadlyInviteFriendsDialog.lambda$onCreateDialog$0(dialogInterface, i);
                }
            });
        } else {
            message.setPositiveButton("Accept", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.InviteFriends.-$$Lambda$TreadlyInviteFriendsDialog$brVbzw6aRaizbOJ4YzTGGGN9iU4
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    ActivityUtil.runOnUiThread(r0.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.InviteFriends.-$$Lambda$TreadlyInviteFriendsDialog$bnPR58s249DN0er9NF4-aDuI-Uc
                        @Override // java.lang.Runnable
                        public final void run() {
                            TreadlyServiceManager.getInstance().acceptFriendInvite(r0.invite.token, r0.invite.userId, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyProfile.InviteFriends.TreadlyInviteFriendsDialog.1
                                @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                                public void onSuccess(String str) {
                                    super.onSuccess(str);
                                }
                            });
                        }
                    });
                }
            });
            message.setNegativeButton("Decline", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.InviteFriends.-$$Lambda$TreadlyInviteFriendsDialog$2btT_IZwdHwMSoBZcH1--AsV7Ws
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    ActivityUtil.runOnUiThread(r0.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.InviteFriends.-$$Lambda$TreadlyInviteFriendsDialog$FEA15gRYrTZI4UuDa0sZqjWG5ag
                        @Override // java.lang.Runnable
                        public final void run() {
                            TreadlyServiceManager.getInstance().declineFriendInvite(r0.invite.token, r0.invite.userId, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyProfile.InviteFriends.TreadlyInviteFriendsDialog.2
                                @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                                public void onSuccess(String str) {
                                    super.onSuccess(str);
                                }
                            });
                        }
                    });
                }
            });
        }
        return message.create();
    }
}
