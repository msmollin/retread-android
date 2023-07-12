package com.treadly.Treadly.UI.TreadlyProfile.InviteFriends;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Managers.UniversalLinkManager;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.Util.ActivityUtil;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;

/* loaded from: classes2.dex */
public class TreadlyInviteFriendsFragment extends BaseFragment {
    ImageView backArrow;
    TextView copyLinkText;
    EditText friendCodeBox;
    TextView inviteFriendsTitle;

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        hideBottomNavigation();
        initUrl();
        return layoutInflater.inflate(R.layout.fragment_treadly_invite_friends, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        showBottomNavigation();
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    @SuppressLint({"ClickableViewAccessibility"})
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        this.inviteFriendsTitle = (TextView) view.findViewById(R.id.header_title);
        this.inviteFriendsTitle.setText("Invite Friends");
        this.backArrow = (ImageView) view.findViewById(R.id.back_arrow);
        this.backArrow.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.InviteFriends.-$$Lambda$TreadlyInviteFriendsFragment$c63zj5KAR8eNCdePvb_wNH2_I3M
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyInviteFriendsFragment.this.getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        this.friendCodeBox = (EditText) view.findViewById(R.id.invite_friend_code_box);
        this.friendCodeBox.setKeyListener(null);
        this.copyLinkText = (TextView) view.findViewById(R.id.invite_friend_copy_link);
        this.copyLinkText.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.InviteFriends.-$$Lambda$TreadlyInviteFriendsFragment$GzAiRBBLOJQ6nzNbZE1ujHifrEk
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ((ClipboardManager) r0.getContext().getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("friend_code", TreadlyInviteFriendsFragment.this.friendCodeBox.getText().toString()));
            }
        });
        this.copyLinkText.setOnTouchListener(new View.OnTouchListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.InviteFriends.-$$Lambda$TreadlyInviteFriendsFragment$9-L0Sm8oxtVe0OPjgF7LnIAGn00
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view2, MotionEvent motionEvent) {
                return TreadlyInviteFriendsFragment.lambda$onViewCreated$2(TreadlyInviteFriendsFragment.this, view2, motionEvent);
            }
        });
    }

    public static /* synthetic */ boolean lambda$onViewCreated$2(TreadlyInviteFriendsFragment treadlyInviteFriendsFragment, View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0:
                treadlyInviteFriendsFragment.copyLinkText.setTextColor(ResourcesCompat.getColor(treadlyInviteFriendsFragment.getResources(), R.color.invite_friend_copy_link_pressed, null));
                return false;
            case 1:
                treadlyInviteFriendsFragment.copyLinkText.setTextColor(ResourcesCompat.getColor(treadlyInviteFriendsFragment.getResources(), R.color.invite_friend_copy_link, null));
                return false;
            default:
                return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyProfile.InviteFriends.TreadlyInviteFriendsFragment$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends TreadlyServiceResponseEventAdapter {
        AnonymousClass1() {
        }

        @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
        public void onCreateFriendInviteToken(String str, final String str2) {
            super.onCreateFriendInviteToken(str, str2);
            if (str2 != null) {
                ActivityUtil.runOnUiThread(TreadlyInviteFriendsFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.InviteFriends.-$$Lambda$TreadlyInviteFriendsFragment$1$0qK7FQ4rnL2YMQo6ME4CqBZCN9w
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyInviteFriendsFragment.this.updateLink(str2);
                    }
                });
            }
        }
    }

    private void initUrl() {
        UniversalLinkManager.getInstance().getFriendInviteUniversalLink(new AnonymousClass1());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLink(String str) {
        if (this.friendCodeBox != null) {
            EditText editText = this.friendCodeBox;
            if (str == null) {
                str = "";
            }
            editText.setText(str);
        }
    }
}
