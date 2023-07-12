package com.treadly.Treadly.UI.TreadlyAccount;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import androidx.annotation.Nullable;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Managers.AppActivityManager;
import com.treadly.Treadly.Data.Managers.DeviceUserStatsLogManager;
import com.treadly.Treadly.Data.Managers.MqttConnectionManager;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.Data.Managers.UniversalLinkManager;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileEditFragment;
import com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileVideoCollectionFragment;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseActivity;
import com.treadly.Treadly.UI.Util.OnBackPressedListener;
import com.treadly.Treadly.UI.Util.SharedPreferences;
import com.treadly.client.lib.sdk.Model.VersionInfo;
import com.treadly.client.lib.sdk.TreadlyClientLib;
import java.util.Locale;

/* loaded from: classes2.dex */
public class LoginActivity extends BaseActivity {
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private OnBackPressedListener backPressedListener;
    private Handler handler;
    private HandlerThread thread;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        MqttConnectionManager.getInstance();
        UniversalLinkManager universalLinkManager = UniversalLinkManager.getInstance();
        setContentView(R.layout.activity_main_frame_layout);
        this.thread = new HandlerThread("ServiceThread");
        this.thread.start();
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
        if ("android.intent.action.VIEW".equals(action) && data != null) {
            Log.d("INVITE", "queueing invite");
            universalLinkManager.queueFriendInviteLink = data.toString();
        }
        this.handler = new Handler(this.thread.getLooper());
        this.handler.post(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyAccount.LoginActivity.1
            @Override // java.lang.Runnable
            public void run() {
                TreadlyClientLib.shared.init(LoginActivity.this);
                SharedPreferences.shared.init(LoginActivity.this);
                DeviceUserStatsLogManager.getInstance();
                LoginActivity.this.checkPermission();
                if (LoginActivity.this.checkVersion()) {
                    LoginActivity.this.signIn();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkVersion() {
        VersionInfo versionInfo = "1.1.8".split("\\.").length == 3 ? new VersionInfo("1.1.8") : null;
        final VersionInfo latestAppVersion = TreadlyClientLib.shared.getLatestAppVersion();
        if (latestAppVersion == null || versionInfo == null || versionInfo.isGreaterThan(latestAppVersion) || versionInfo.isEqual(latestAppVersion) || !latestAppVersion.isMandatory) {
            return true;
        }
        runOnUiThread(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyAccount.LoginActivity.2
            @Override // java.lang.Runnable
            public void run() {
                LoginActivity.this.showUpdateAvailable(latestAppVersion);
            }
        });
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showUpdateAvailable(VersionInfo versionInfo) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(String.format(Locale.getDefault(), "A new version of Treadly is available. Please update to version %s now", versionInfo.getVersion()));
        builder.setTitle("Update Available");
        builder.setCancelable(false);
        runOnUiThread(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyAccount.LoginActivity.3
            @Override // java.lang.Runnable
            public void run() {
                builder.create().show();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void signIn() {
        String userEmail = SharedPreferences.shared.getUserEmail();
        String userPassword = SharedPreferences.shared.getUserPassword();
        String facebookUserId = SharedPreferences.shared.getFacebookUserId();
        String facebookToken = SharedPreferences.shared.getFacebookToken();
        if (userEmail != null && userPassword != null) {
            validLogin(userEmail, userPassword);
        } else if (facebookUserId == null || facebookToken == null) {
            toStartPageFragment();
        } else {
            validFBLogin(facebookUserId, facebookToken);
        }
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.backPressedListener != null) {
            this.backPressedListener.backAction();
        } else {
            super.onBackPressed();
        }
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.backPressedListener = onBackPressedListener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toStartPageFragment() {
        AppActivityManager.shared.handleAppLogin();
        getSupportFragmentManager().beginTransaction().add(R.id.activity_fragment_container_empty, new TreadlyAccountViewFragment(), TreadlyAccountViewFragment.TAG).commitAllowingStateLoss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toEditProfilePageFragment() {
        AppActivityManager.shared.handleAppLogin();
        TreadlyProfileEditFragment treadlyProfileEditFragment = new TreadlyProfileEditFragment();
        treadlyProfileEditFragment.hideNaviagtionBar = true;
        treadlyProfileEditFragment.useDefaultProfile = true;
        treadlyProfileEditFragment.fromLogin = true;
        getSupportFragmentManager().beginTransaction().add(R.id.activity_fragment_container_empty, treadlyProfileEditFragment, TreadlyProfileEditFragment.TAG).commit();
    }

    private void toVideoCollectionFragment(String str, boolean z) {
        TreadlyProfileVideoCollectionFragment treadlyProfileVideoCollectionFragment = new TreadlyProfileVideoCollectionFragment();
        treadlyProfileVideoCollectionFragment.userId = str;
        treadlyProfileVideoCollectionFragment.isCurrentUser = z;
        getSupportFragmentManager().beginTransaction().add(R.id.activity_fragment_container_empty, treadlyProfileVideoCollectionFragment, TreadlyProfileVideoCollectionFragment.TAG).commit();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toChangeUsernameFragment(UserInfo userInfo) {
        AppActivityManager.shared.handleAppLogin();
        TreadlyAccountChangeUsernameFragment treadlyAccountChangeUsernameFragment = new TreadlyAccountChangeUsernameFragment();
        treadlyAccountChangeUsernameFragment.userInfo = userInfo;
        getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.activity_fragment_container_empty, treadlyAccountChangeUsernameFragment, TreadlyAccountChangeUsernameFragment.TAG).commit();
    }

    private void validLogin(String str, String str2) {
        TreadlyServiceManager.getInstance().loginWithEmail(str, str2, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyAccount.LoginActivity.4
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onUserInfoProfile(String str3, UserInfo userInfo, boolean z, boolean z2) {
                if (str3 != null || userInfo == null) {
                    if (str3 != null) {
                        LoginActivity.this.toStartPageFragment();
                    }
                } else if (!z) {
                    LoginActivity.this.toEditProfilePageFragment();
                } else {
                    LoginActivity.this.toMainActivity();
                }
            }
        });
    }

    private void validFBLogin(String str, String str2) {
        TreadlyServiceManager.getInstance().authenticateWithFacebook(str2, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyAccount.LoginActivity.5
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onUserInfoProfile(String str3, UserInfo userInfo, boolean z, boolean z2) {
                if (str3 != null || userInfo == null) {
                    if (str3 != null) {
                        LoginActivity.this.toStartPageFragment();
                    }
                } else if (!z) {
                    LoginActivity.this.toChangeUsernameFragment(userInfo);
                } else {
                    LoginActivity.this.toMainActivity();
                }
            }
        });
    }

    public void toMainActivity() {
        AppActivityManager.shared.handleAppLogin();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.ACCESS_FINE_LOCATION") == 0) {
            return;
        }
        requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 1);
    }
}
