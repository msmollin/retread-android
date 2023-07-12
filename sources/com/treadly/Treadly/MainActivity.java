package com.treadly.Treadly;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.treadly.Treadly.Data.Managers.AppActivityManager;
import com.treadly.Treadly.Data.Managers.DeviceUserStatsLogManager;
import com.treadly.Treadly.Data.Managers.MqttConnectionManager;
import com.treadly.Treadly.Data.Managers.RunningSessionManager;
import com.treadly.Treadly.Data.Managers.TreadlyActivationManager;
import com.treadly.Treadly.Data.Managers.TreadlyEventManager;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.Data.Managers.TreadlyUserEventManager;
import com.treadly.Treadly.Data.Model.UserProfileInfo;
import com.treadly.Treadly.Data.Model.UserStatsInfo;
import com.treadly.Treadly.UI.Onboarding.PostRegistration.TreadlyOnboardingPostRegistrationFragment;
import com.treadly.Treadly.UI.TempComingSoonFragment;
import com.treadly.Treadly.UI.TreadlyAccount.LoginActivity;
import com.treadly.Treadly.UI.TreadlyConnect.DailyGoalView.TreadlyConnectDailyGoalFragment;
import com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment;
import com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectSliderFragment;
import com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileUserView;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoUploaderManager;
import com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseActivity;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
import com.treadly.Treadly.UI.Util.OnBackPressedListener;
import com.treadly.Treadly.UI.Util.SharedPreferences;
import com.treadly.client.lib.sdk.Model.DistanceUnits;
import com.treadly.client.lib.sdk.TreadlyClientLib;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;

/* loaded from: classes.dex */
public class MainActivity extends BaseActivity {
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    public static final String SOCIAL_CHANNEL_ID = "Treadly_Social_Notifications";
    public BaseFragment activeFragment;
    private OnBackPressedListener backPressedListener;
    private BottomNavigationView bottomNavigationView;
    public TreadlyConnectFragment connectFragment;
    public TreadlyProfileUserView profileUserView;
    public TempComingSoonFragment tempComingSoonFragment;
    private UserProfileInfo userProfile;
    private boolean fromConnect = true;
    private List<UserStatsInfo> activityInfoList = new ArrayList();
    private boolean backButtonEnabled = true;
    public boolean adminMode = false;
    public boolean blockBottomNavigation = false;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!TreadlyServiceManager.getInstance().isLogIn()) {
            toLogin();
            return;
        }
        MqttConnectionManager.getInstance();
        TreadlyUserEventManager.getInstance();
        TreadlyEventManager.getInstance();
        try {
            VideoUploaderManager.context = this;
            VideoUploaderManager.shared.start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_main_frame_layout);
        TreadlyActivationManager.shared.activity = this;
        DeviceUserStatsLogManager.getInstance().activity = this;
        initBottomNavigation();
        if (!TreadlyServiceManager.getInstance().isOnboarding) {
            toConnect();
        } else {
            toOnboarding();
        }
        checkPermission();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStart() {
        super.onStart();
        RunningSessionManager.getInstance().fetchDailyGoals();
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        TreadlyActivationManager.shared.activity = null;
        DeviceUserStatsLogManager.getInstance().activity = null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        TreadlyPublicStreamHostFragment treadlyPublicStreamHostFragment = (TreadlyPublicStreamHostFragment) getSupportFragmentManager().findFragmentByTag(TreadlyPublicStreamHostFragment.TAG);
        if (treadlyPublicStreamHostFragment != null) {
            treadlyPublicStreamHostFragment.onActivityPause();
        }
    }

    public static Drawable drawableFromUrl(String str) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
        httpURLConnection.connect();
        return new BitmapDrawable(Resources.getSystem(), BitmapFactory.decodeStream(httpURLConnection.getInputStream()));
    }

    public void updateNavProfilePicture() {
        Glide.with((FragmentActivity) this).load(Uri.parse(TreadlyServiceManager.getInstance().getUserInfo().avatarPath)).apply(RequestOptions.circleCropTransform()).into((RequestBuilder<Drawable>) new SimpleTarget<Drawable>() { // from class: com.treadly.Treadly.MainActivity.1
            @Override // com.bumptech.glide.request.target.Target
            public /* bridge */ /* synthetic */ void onResourceReady(@NonNull Object obj, @Nullable Transition transition) {
                onResourceReady((Drawable) obj, (Transition<? super Drawable>) transition);
            }

            public void onResourceReady(@NonNull Drawable drawable, @Nullable Transition<? super Drawable> transition) {
                MainActivity.this.bottomNavigationView.getMenu().findItem(R.id.navigation_profile).setIcon(drawable);
            }
        });
    }

    public void setFromConnect() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments.get(fragments.size() - 1).getTag().equals(TreadlyConnectSliderFragment.TAG)) {
            this.fromConnect = true;
        } else {
            this.fromConnect = false;
        }
    }

    private void initBottomNavigation() {
        this.bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        this.bottomNavigationView.setItemIconTintList(null);
        disableShiftMode(this.bottomNavigationView);
        updateNavProfilePicture();
        setSpeedIconValue(Utils.DOUBLE_EPSILON, DistanceUnits.MI);
        this.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() { // from class: com.treadly.Treadly.MainActivity.2
            @Override // com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_connect /* 2131362400 */:
                        AppActivityManager.shared.handleTabSelected(2);
                        MainActivity.this.getWindow().getDecorView().performHapticFeedback(1, 2);
                        MainActivity.this.toConnectNavBar();
                        break;
                    case R.id.navigation_groups /* 2131362401 */:
                        AppActivityManager.shared.handleTabSelected(1);
                        MainActivity.this.getWindow().getDecorView().performHapticFeedback(1, 2);
                        MainActivity.this.toComingSoon();
                        break;
                    case R.id.navigation_header_container /* 2131362402 */:
                    default:
                        return false;
                    case R.id.navigation_home /* 2131362403 */:
                        AppActivityManager.shared.handleTabSelected(0);
                        MainActivity.this.getWindow().getDecorView().performHapticFeedback(1, 2);
                        MainActivity.this.toComingSoon();
                        break;
                    case R.id.navigation_inbox /* 2131362404 */:
                        AppActivityManager.shared.handleTabSelected(3);
                        MainActivity.this.getWindow().getDecorView().performHapticFeedback(1, 2);
                        MainActivity.this.toComingSoon();
                        break;
                    case R.id.navigation_profile /* 2131362405 */:
                        AppActivityManager.shared.handleTabSelected(4);
                        MainActivity.this.getWindow().getDecorView().performHapticFeedback(1, 2);
                        MainActivity.this.toProfile(TreadlyServiceManager.getInstance().getUserId());
                        break;
                }
                return true;
            }
        });
    }

    public void enableBottomBar(boolean z) {
        this.bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        for (int i = 0; i < this.bottomNavigationView.getMenu().size(); i++) {
            this.bottomNavigationView.getMenu().getItem(i).setEnabled(z);
        }
    }

    public void setSpeedIconValue(double d, DistanceUnits distanceUnits) {
        TextView textView = (TextView) findViewById(R.id.connect_icon_whole_number);
        TextView textView2 = (TextView) findViewById(R.id.connect_icon_part_number);
        TextView textView3 = (TextView) findViewById(R.id.connect_icon_units);
        if (distanceUnits == DistanceUnits.MI) {
            textView3.setText("Mph");
        } else {
            textView3.setText("Kph");
        }
        int floor = (int) Math.floor(d);
        textView.setText(String.valueOf(floor));
        textView2.setText(String.valueOf(((int) (d * 10.0d)) - (floor * 10)));
    }

    @SuppressLint({"RestrictedApi"})
    private void disableShiftMode(BottomNavigationView bottomNavigationView) {
        if (bottomNavigationView != null) {
            bottomNavigationView.setLabelVisibilityMode(1);
            bottomNavigationView.setItemHorizontalTranslationEnabled(false);
        }
    }

    public void toLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void logout() {
        SharedPreferences.shared.storeUserEmail(null);
        SharedPreferences.shared.storeUserPassword(null);
        SharedPreferences.shared.storeFacebookToken(null);
        SharedPreferences.shared.storeFacebookUserId(null);
        TreadlyClientLib.shared.disconnect();
        TreadlyServiceManager.getInstance().logout();
        RunningSessionManager.getInstance().reset();
        DeviceUserStatsLogManager.getInstance().clear();
        AppActivityManager.shared.clear();
        clearConnectFragment();
        toLogin();
    }

    public void toConnect() {
        this.connectFragment = new TreadlyConnectFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_fragment_container_empty, this.connectFragment, TreadlyConnectFragment.TAG).commit();
    }

    public void toVid() {
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_fragment_container_empty, new TreadlyConnectDailyGoalFragment()).commit();
    }

    public void toComingSoon() {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        hideAllFragments(beginTransaction);
        if (this.tempComingSoonFragment == null) {
            this.tempComingSoonFragment = new TempComingSoonFragment();
            beginTransaction.add(R.id.activity_fragment_container_empty, this.tempComingSoonFragment);
        } else {
            beginTransaction.show(this.tempComingSoonFragment);
        }
        beginTransaction.commit();
    }

    public void toOnboarding() {
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_fragment_container_empty, new TreadlyOnboardingPostRegistrationFragment(), TreadlyOnboardingPostRegistrationFragment.TAG).commit();
    }

    private void hideAllFragments(FragmentTransaction fragmentTransaction) {
        FragmentTransaction beginTransaction = (fragmentTransaction != null || getSupportFragmentManager() == null) ? fragmentTransaction : getSupportFragmentManager().beginTransaction();
        if (beginTransaction == null) {
            return;
        }
        hideFragment(this.tempComingSoonFragment, beginTransaction);
        hideFragment(this.connectFragment, beginTransaction);
        hideFragment(this.profileUserView, beginTransaction);
        if (fragmentTransaction == null) {
            beginTransaction.commit();
        }
    }

    private void hideFragment(BaseFragment baseFragment, FragmentTransaction fragmentTransaction) {
        if (baseFragment == null || fragmentTransaction == null) {
            return;
        }
        fragmentTransaction.hide(baseFragment);
    }

    public void clearConnectFragment() {
        if (this.connectFragment != null) {
            this.connectFragment.removeEventListeners();
        }
    }

    public void toConnectNavBar() {
        hideAllFragments(null);
        getSupportFragmentManager().beginTransaction().show(this.connectFragment).commit();
    }

    public void toProfile(String str) {
        hideAllFragments(null);
        TreadlyProfileUserView treadlyProfileUserView = new TreadlyProfileUserView();
        treadlyProfileUserView.userId = str;
        treadlyProfileUserView.isCurrentUser = true;
        showLoadingDialog(true);
        getSupportFragmentManager().beginTransaction().addToBackStack(TreadlyConnectFragment.TAG).add(R.id.activity_fragment_container_empty, treadlyProfileUserView, TreadlyProfileUserView.TAG).commit();
        this.profileUserView = treadlyProfileUserView;
    }

    public void hideBottomNavigationView() {
        if (this.bottomNavigationView == null) {
            return;
        }
        this.bottomNavigationView.setVisibility(8);
    }

    public void showBottomNavigationView() {
        if (this.blockBottomNavigation || this.bottomNavigationView == null) {
            return;
        }
        this.bottomNavigationView.setVisibility(0);
    }

    public void enableBackButton(boolean z) {
        this.backButtonEnabled = z;
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.backButtonEnabled) {
            if (this.backPressedListener != null) {
                this.backPressedListener.backAction();
            } else {
                super.onBackPressed();
            }
        }
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.backPressedListener = onBackPressedListener;
    }

    public OnBackPressedListener getOnBackPressedListener() {
        return this.backPressedListener;
    }

    public void enableActivationTabs(boolean z) {
        MenuItem item = this.bottomNavigationView.getMenu().getItem(0);
        MenuItem item2 = this.bottomNavigationView.getMenu().getItem(1);
        MenuItem item3 = this.bottomNavigationView.getMenu().getItem(3);
        if (item != null) {
            item.setEnabled(z);
        }
        if (item2 != null) {
            item2.setEnabled(z);
        }
        if (item3 != null) {
            item3.setEnabled(z);
        }
    }

    public void checkPermission() {
        if (checkSelfPermission("android.permission.ACCESS_FINE_LOCATION") != 0) {
            requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 1);
        }
        if (!hasPermission("android.permission.CAMERA") && !hasPermission("android.permission.RECORD_AUDIO")) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.CAMERA", "android.permission.RECORD_AUDIO"}, 1);
        } else if (!hasPermission("android.permission.RECORD_AUDIO")) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.RECORD_AUDIO"}, 1);
        } else if (hasPermission("android.permission.CAMERA")) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.CAMERA"}, 1);
        }
    }

    public boolean hasPermission(String str) {
        return checkSelfPermission(str) == 0;
    }
}
