package com.treadly.Treadly.UI.TreadlyConnect.Video;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/* loaded from: classes2.dex */
public class TreadlyInviteRequestDialog extends DialogFragment {
    TreadlyInviteRequestListener listener;
    public String name;

    /* loaded from: classes2.dex */
    public interface TreadlyInviteRequestListener {
        void onAccept();

        void onDecline();
    }

    @Override // androidx.fragment.app.DialogFragment
    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(String.format("%s has invited you to join their live stream", this.name)).setPositiveButton("Accept", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.Video.TreadlyInviteRequestDialog.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                TreadlyInviteRequestDialog.this.listener.onAccept();
            }
        }).setNegativeButton("Decline", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.Video.TreadlyInviteRequestDialog.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                TreadlyInviteRequestDialog.this.listener.onDecline();
            }
        });
        return builder.create();
    }
}
