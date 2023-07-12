package com.treadly.Treadly.UI.PushNotifications.Firebase.Actions;

import android.os.Handler;
import android.os.Looper;
import androidx.fragment.app.FragmentActivity;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.PushNotifications.Firebase.FirebaseNotificationCategory;
import com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment;
import com.treadly.Treadly.UI.TreadlyVideo.Data.InviteServiceHelper;
import com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.TreadlyPrivateStreamHostFragment;
import java.util.Map;

/* loaded from: classes2.dex */
public class FcmPrivateVideoInviteAction {
    private static final String TAG = "FcmPrivateVideoInviteAction";
    static String identifier = FirebaseNotificationCategory.privateVideoInvite.name();

    public static void handleNotification(Map<String, String> map, final FragmentActivity fragmentActivity) {
        final String str = map.get("user_id_from");
        String str2 = map.get("user_id_to");
        if (str == null || str2 == null) {
            return;
        }
        final String str3 = map.get("user_name_from");
        InviteServiceHelper.acceptInvite(str, str2, new InviteServiceHelper.InviteServiceHelperRequestBasicUsersPost() { // from class: com.treadly.Treadly.UI.PushNotifications.Firebase.Actions.-$$Lambda$FcmPrivateVideoInviteAction$zloJdetm8ILsALvnR3Rb-4aS7Ac
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.InviteServiceHelper.InviteServiceHelperRequestBasicUsersPost
            public final void onResponse(String str4) {
                FcmPrivateVideoInviteAction.lambda$handleNotification$0(str, str3, fragmentActivity, str4);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$handleNotification$0(String str, String str2, FragmentActivity fragmentActivity, String str3) {
        if (str3 == null) {
            presentPrivateVideoPage(str, str2, fragmentActivity);
        }
    }

    public static void presentPrivateVideoPage(String str, String str2, FragmentActivity fragmentActivity) {
        TreadlyPrivateStreamHostFragment treadlyPrivateStreamHostFragment = new TreadlyPrivateStreamHostFragment();
        treadlyPrivateStreamHostFragment.isHost = false;
        treadlyPrivateStreamHostFragment.userId = str;
        fragmentActivity.getSupportFragmentManager().beginTransaction().addToBackStack(TreadlyConnectFragment.TAG).add(R.id.activity_fragment_container_empty, treadlyPrivateStreamHostFragment).commit();
    }

    private void runOnMain(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
