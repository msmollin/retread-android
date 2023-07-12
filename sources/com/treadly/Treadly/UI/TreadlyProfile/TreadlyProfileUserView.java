package com.treadly.Treadly.UI.TreadlyProfile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.Data.Model.FriendRequestInfo;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.Data.Model.UserProfileInfo;
import com.treadly.Treadly.Data.Model.UserStatsInfo;
import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyProfile.ReportUser.TreadlyProfileReportContentFragment;
import com.treadly.Treadly.UI.TreadlyProfile.ReportUser.TreadlyReportCategory;
import com.treadly.Treadly.UI.TreadlyProfile.Settings.TreadlyProfileSettingsFragment;
import com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileEditBioFragment;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper;
import com.treadly.Treadly.UI.Util.ActivityUtil;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
import com.treadly.client.lib.sdk.Model.DistanceUnits;
import com.yalantis.ucrop.UCrop;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONException;

/* loaded from: classes2.dex */
public class TreadlyProfileUserView extends BaseFragment {
    public static final String TAG = "TREADLY_PROFILE_USER";
    private Map<Integer, UserStatsInfo> activityLookup;
    private Button addFriendButton;
    Context context;
    private TextView distanceValue;
    private ImageButton inviteFriendButton;
    public boolean isCurrentUser;
    ImageView leftArrow;
    private TextView locationText;
    private TextView numberFriends;
    private TextView numberSteps;
    private Uri outputUri;
    private TextView profileBio;
    private TextView profileName;
    private CircularImageView profilePicture;
    View profileView;
    ImageView rightArrow;
    private ImageButton settingsButton;
    Date targetDate;
    public String userAvatar;
    public String userId;
    public String userName;
    public UserProfileInfo userProfile;
    TreadlyProfileWeekStatsAdapter weekAdapter;
    ViewPager weekStatsViewPager;
    public List<UserStatsInfo> activityList = new ArrayList();
    public List<UserStatsInfo> weekStats = new ArrayList();
    int targetDay = 6;
    boolean hasInit = false;
    private DecimalFormat distanceFormatter = new DecimalFormat("0.0");
    private DecimalFormat stepsFormatter = new DecimalFormat("#,###");
    List<UserInfo> currentFriendList = new ArrayList();
    List<FriendRequestInfo> currentRequests = new ArrayList();
    TextView blockedUserLabel = null;
    Uri capturedUri = null;
    boolean didTakePhoto = false;
    boolean confirmed = false;

    private void inviteFriendsButtonPressed() {
    }

    private void showFollowersAndFollowing() {
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            if (this.isCurrentUser) {
                mainActivity.showBottomNavigationView();
            } else {
                mainActivity.hideBottomNavigationView();
            }
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        if (this.hasInit) {
            ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileUserView.1
                @Override // java.lang.Runnable
                public void run() {
                    TreadlyProfileUserView.this.getProfileData();
                }
            });
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        showBottomNavigation();
    }

    @Override // androidx.fragment.app.Fragment
    public void onHiddenChanged(boolean z) {
        super.onHiddenChanged(z);
        if (z) {
            return;
        }
        showBottomNavigation();
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        ((MainActivity) getActivity()).setOnBackPressedListener(null);
        return layoutInflater.inflate(R.layout.fragment_treadly_profile_user, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        this.context = getContext();
        this.profileView = view;
        getProfileData();
        if (this.isCurrentUser) {
            getUserInfo();
        }
        this.settingsButton = (ImageButton) view.findViewById(R.id.profile_settings_button);
        if (this.isCurrentUser) {
            this.settingsButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileUserView.2
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    if (TreadlyProfileUserView.this.getActivity() != null) {
                        TreadlyProfileUserView.this.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.activity_fragment_container_empty, new TreadlyProfileSettingsFragment(), TreadlyProfileSettingsFragment.TAG).commit();
                    }
                }
            });
        } else {
            this.settingsButton.setBackgroundResource(R.drawable.user_profile_three_dots);
            this.settingsButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileUserView.3
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                }
            });
        }
        this.inviteFriendButton = (ImageButton) view.findViewById(R.id.profile_invite_friend_button);
        if (this.isCurrentUser) {
            this.inviteFriendButton.setVisibility(4);
        } else {
            this.inviteFriendButton.setBackgroundResource(R.drawable.back_arrow);
            this.inviteFriendButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileUserView.4
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    TreadlyProfileUserView.this.getActivity().getSupportFragmentManager().popBackStack();
                }
            });
        }
        this.addFriendButton = (Button) view.findViewById(R.id.profile_add_friend_button);
        this.addFriendButton.setVisibility(8);
        this.profilePicture = (CircularImageView) view.findViewById(R.id.profile_view);
        this.profilePicture.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileUserView.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (TreadlyProfileUserView.this.isCurrentUser) {
                    TreadlyProfileUserView.this.showMoreAlert();
                }
            }
        });
        this.profileName = (TextView) view.findViewById(R.id.profile_name);
        this.profileBio = (TextView) view.findViewById(R.id.profile_bio);
        this.profileBio.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileUserView.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (TreadlyProfileUserView.this.isCurrentUser) {
                    TreadlyProfileUserView.this.onBioPressed();
                }
            }
        });
        this.locationText = (TextView) view.findViewById(R.id.location_text_profile);
        this.numberFriends = (TextView) view.findViewById(R.id.number_friends_main_profile);
        this.distanceValue = (TextView) view.findViewById(R.id.distance_main_profile);
        this.numberSteps = (TextView) view.findViewById(R.id.number_steps_main_profile);
        this.weekStatsViewPager = (ViewPager) view.findViewById(R.id.week_stats_pager);
        this.weekStatsViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileUserView.7
            @Override // androidx.viewpager.widget.ViewPager.SimpleOnPageChangeListener, androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                TreadlyProfileUserView.this.targetDay = i;
                PrintStream printStream = System.out;
                printStream.println("TARGETDAY: " + TreadlyProfileUserView.this.targetDay);
            }
        });
        this.leftArrow = (ImageView) view.findViewById(R.id.week_stat_left_arrow);
        this.rightArrow = (ImageView) view.findViewById(R.id.week_stat_right_arrow);
        this.weekAdapter = new TreadlyProfileWeekStatsAdapter(getContext(), this.weekStats);
        this.weekStatsViewPager.setAdapter(this.weekAdapter);
        this.weekStatsViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileUserView.8
            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                if (i == TreadlyProfileUserView.this.weekAdapter.getCount() - 1) {
                    TreadlyProfileUserView.this.rightArrow.setBackgroundResource(R.drawable.right_arrow_week_stats_disabled);
                    TreadlyProfileUserView.this.leftArrow.setBackgroundResource(R.drawable.left_arrow_week_stats);
                } else if (i == 0) {
                    TreadlyProfileUserView.this.rightArrow.setBackgroundResource(R.drawable.right_arrow_week_stats);
                    TreadlyProfileUserView.this.leftArrow.setBackgroundResource(R.drawable.left_arrow_week_stats_disabled);
                } else {
                    TreadlyProfileUserView.this.rightArrow.setBackgroundResource(R.drawable.right_arrow_week_stats);
                    TreadlyProfileUserView.this.leftArrow.setBackgroundResource(R.drawable.left_arrow_week_stats);
                }
            }
        });
        TreadlyProfileVideoCollectionFragment treadlyProfileVideoCollectionFragment = new TreadlyProfileVideoCollectionFragment();
        treadlyProfileVideoCollectionFragment.userId = this.userId;
        if (this.isCurrentUser) {
            treadlyProfileVideoCollectionFragment.user = TreadlyServiceManager.getInstance().getUserInfo();
        } else {
            treadlyProfileVideoCollectionFragment.user = new UserInfo(this.userId, this.userName, "");
            treadlyProfileVideoCollectionFragment.user.avatarPath = this.userAvatar;
        }
        treadlyProfileVideoCollectionFragment.isCurrentUser = this.isCurrentUser;
        if (getActivity() != null && view.findViewById(R.id.discover_video_frame) != null) {
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.discover_video_frame, treadlyProfileVideoCollectionFragment, TreadlyProfileVideoCollectionFragment.TAG).commit();
        }
        setupBlockReportUI();
        initUi();
        this.hasInit = true;
    }

    public void initUi() {
        if (getContext() != null && this.userAvatar != null) {
            Picasso.get().load(Uri.parse(this.userAvatar)).tag(getContext()).into(this.profilePicture);
        }
        if (this.userProfile != null) {
            this.profileName.setText(this.userName);
            this.profileBio.setText(this.userProfile.descriptionProfile);
            this.locationText.setText(this.userProfile.city);
            this.numberFriends.setText(String.valueOf(this.userProfile.friendsCount));
            this.distanceValue.setText(String.valueOf(this.userProfile.distanceTotal));
            this.numberSteps.setText(String.valueOf(this.userProfile.stepsTotal));
        }
    }

    public void updateUi() {
        if (this.userProfile == null || getContext() == null) {
            return;
        }
        if (this.userAvatar == null) {
            this.userAvatar = "https://dgwxv5s2i5zkb.cloudfront.net/avatar/default.png";
        }
        Picasso.get().load(Uri.parse(this.userAvatar)).tag(getContext()).into(this.profilePicture);
        this.profileName.setText(this.userName);
        this.profileBio.setText(this.userProfile.descriptionProfile);
        this.locationText.setText(this.userProfile.city);
        this.numberFriends.setText(this.stepsFormatter.format(this.userProfile.friendsCount));
        TextView textView = this.distanceValue;
        Locale locale = Locale.getDefault();
        Object[] objArr = new Object[2];
        objArr[0] = this.distanceFormatter.format(this.userProfile.distanceTotal);
        objArr[1] = this.userProfile.units == DistanceUnits.MI ? "mi" : "km";
        textView.setText(String.format(locale, "%s%s", objArr));
        this.numberSteps.setText(this.stepsFormatter.format(this.userProfile.stepsTotal));
        if (this.blockedUserLabel != null) {
            setBlockedUserLabel(this.userProfile.isBlocked);
        }
    }

    private boolean isFriends(String str, List<UserInfo> list) {
        for (UserInfo userInfo : list) {
            if (userInfo.id.equals(str)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasSentRequest(String str, List<FriendRequestInfo> list) {
        for (FriendRequestInfo friendRequestInfo : list) {
            if (friendRequestInfo.receiver.id.equals(str)) {
                return true;
            }
        }
        return false;
    }

    private void addFriend() {
        isFriends(this.userId, this.currentFriendList);
        hasSentRequest(this.userId, this.currentRequests);
    }

    void setupBlockReportUI() {
        if (this.isCurrentUser) {
            return;
        }
        this.blockedUserLabel = (TextView) this.profileView.findViewById(R.id.block_user_text);
    }

    void setBlockedUserLabel(boolean z) {
        if (z) {
            this.blockedUserLabel.setVisibility(0);
        } else {
            this.blockedUserLabel.setVisibility(8);
        }
    }

    void onBlockReportPressed() {
        PopupMenu blockMenu = getBlockMenu(this.settingsButton);
        blockMenu.inflate(R.menu.block_report_user_popup);
        Menu menu = blockMenu.getMenu();
        if (this.userProfile.isBlocked) {
            menu.findItem(R.id.profile_block_user_popup).setTitle("Unblock User");
        } else {
            menu.findItem(R.id.profile_block_user_popup).setTitle("Block User");
        }
        blockMenu.show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileUserView$9  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass9 implements PopupMenu.OnMenuItemClickListener {
        AnonymousClass9() {
        }

        @Override // androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.profile_block_user_popup) {
                if (TreadlyProfileUserView.this.userProfile.isBlocked) {
                    VideoServiceHelper.deleteUserBlock(TreadlyProfileUserView.this.userId, new VideoServiceHelper.VideoResponseListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileUserView.9.1
                        @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoResponseListener
                        public void onResponse(String str) {
                            if (str == null) {
                                ActivityUtil.runOnUiThread(TreadlyProfileUserView.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileUserView.9.1.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        TreadlyProfileUserView.this.getProfileData();
                                        try {
                                            TreadlyProfileUserView.this.getFriends();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }
                    });
                } else {
                    VideoServiceHelper.createUserBlock(TreadlyProfileUserView.this.userId, new VideoServiceHelper.VideoResponseListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileUserView.9.2
                        @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoResponseListener
                        public void onResponse(String str) {
                            if (str == null) {
                                ActivityUtil.runOnUiThread(TreadlyProfileUserView.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileUserView.9.2.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        TreadlyProfileUserView.this.getProfileData();
                                        try {
                                            TreadlyProfileUserView.this.getFriends();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
                return true;
            } else if (itemId != R.id.profile_report_user_popup) {
                return false;
            } else {
                TreadlyProfileReportContentFragment treadlyProfileReportContentFragment = new TreadlyProfileReportContentFragment();
                treadlyProfileReportContentFragment.contentId = TreadlyProfileUserView.this.userId;
                treadlyProfileReportContentFragment.category = TreadlyReportCategory.user;
                TreadlyProfileUserView.this.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.activity_fragment_container_empty, treadlyProfileReportContentFragment).commit();
                return true;
            }
        }
    }

    private PopupMenu getBlockMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.setOnMenuItemClickListener(new AnonymousClass9());
        return popupMenu;
    }

    UserStatsInfo getActivityForDay(int i) {
        Date date;
        Calendar calendar = Calendar.getInstance();
        int i2 = 6 - i;
        if (this.targetDay != 0) {
            calendar.setTime(new Date(System.currentTimeMillis()));
            calendar.set(calendar.get(1), calendar.get(2), calendar.get(5) - i2);
            date = calendar.getTime();
        } else {
            date = new Date(System.currentTimeMillis());
        }
        if (date == null) {
            return null;
        }
        this.targetDate = date;
        calendar.setTime(this.targetDate);
        int i3 = calendar.get(5);
        int i4 = calendar.get(2);
        int i5 = calendar.get(1);
        for (UserStatsInfo userStatsInfo : this.activityList) {
            if (userStatsInfo.timestamp != null) {
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(userStatsInfo.timestamp);
                int i6 = calendar2.get(5);
                int i7 = calendar2.get(2);
                int i8 = calendar2.get(1);
                if (i3 == i6 && i4 == i7 && i5 == i8) {
                    return userStatsInfo;
                }
            }
        }
        return null;
    }

    Date getTargetDateForDay(int i) {
        Date date;
        Calendar calendar = Calendar.getInstance();
        int i2 = 6 - i;
        if (i2 != 0) {
            calendar.setTime(new Date(System.currentTimeMillis()));
            calendar.set(calendar.get(1), calendar.get(2), calendar.get(5) - i2);
            date = calendar.getTime();
        } else {
            date = new Date(System.currentTimeMillis());
        }
        if (date == null) {
            return null;
        }
        return date;
    }

    void getFriends() throws JSONException {
        TreadlyServiceManager.getInstance().getFriendsInfo(new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileUserView.10
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onUserFriendsInfo(String str, List<UserInfo> list) throws JSONException {
            }
        });
    }

    void getProfileData() {
        TreadlyServiceManager.getInstance().getUserProfileInfo(this.userId, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileUserView.11
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onUserProfile(String str, UserProfileInfo userProfileInfo) {
                if (str == null) {
                    TreadlyProfileUserView.this.userProfile = userProfileInfo;
                    TreadlyProfileUserView.this.updateUi();
                    TreadlyServiceManager.getInstance().getDeviceUserStatsInfo(TreadlyProfileUserView.this.userId, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileUserView.11.1
                        @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                        public void onUserStatsInfo(String str2, ArrayList<UserStatsInfo> arrayList) {
                            if (str2 != null || arrayList == null) {
                                return;
                            }
                            TreadlyProfileUserView.this.activityList.addAll(arrayList);
                            Collections.sort(TreadlyProfileUserView.this.activityList);
                            Collections.reverse(TreadlyProfileUserView.this.activityList);
                            TreadlyProfileUserView.this.weekStats.clear();
                            for (int i = 0; i < 7; i++) {
                                TreadlyProfileUserView.this.weekStats.add(TreadlyProfileUserView.this.getActivityForDay(i));
                            }
                            System.out.println("WEEKSTATS: " + TreadlyProfileUserView.this.weekStats);
                            TreadlyProfileUserView.this.weekAdapter.notifyDataSetChanged();
                            TreadlyProfileUserView.this.weekStatsViewPager.setCurrentItem(TreadlyProfileUserView.this.weekAdapter.getCount() + (-1), false);
                            TreadlyProfileUserView.this.dismissLoading();
                        }
                    });
                }
            }
        });
    }

    void getUserInfo() {
        UserInfo userInfo = TreadlyServiceManager.getInstance().getUserInfo();
        this.userAvatar = userInfo.avatarPath;
        this.userName = userInfo.name;
    }

    void onBioPressed() {
        if (this.userProfile.descriptionProfile == null) {
            return;
        }
        TreadlyProfileEditBioFragment treadlyProfileEditBioFragment = new TreadlyProfileEditBioFragment();
        treadlyProfileEditBioFragment.initialText = this.userProfile.descriptionProfile;
        treadlyProfileEditBioFragment.userId = this.userId;
        treadlyProfileEditBioFragment.listener = new TreadlyProfileEditBioFragment.TreadlyProfileEditBioFragmentListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileUserView.12
            @Override // com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileEditBioFragment.TreadlyProfileEditBioFragmentListener
            public void didUpdateBio(String str) {
                TreadlyProfileUserView.this.getProfileData();
            }
        };
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.activity_fragment_container_empty, treadlyProfileEditBioFragment).commit();
        }
    }

    void showMoreAlert() {
        new AlertDialog.Builder(this.context).setTitle("Change Profile Picture").setItems(new String[]{"Take Photo", "Choose Photo", "Cancel"}, new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileUserView.13
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        TreadlyProfileUserView.this.takePhoto();
                        return;
                    case 1:
                        TreadlyProfileUserView.this.choosePhoto();
                        return;
                    case 2:
                        dialogInterface.dismiss();
                        return;
                    default:
                        return;
                }
            }
        }).show();
    }

    private File createImageFile() throws IOException {
        if (getActivity() == null) {
            return null;
        }
        return File.createTempFile("temp_profile_picture", ".png", getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES));
    }

    void takePhoto() {
        File file;
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        try {
            file = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
            file = null;
        }
        if (file != null) {
            this.capturedUri = FileProvider.getUriForFile(this.context, "com.treadly.Treadly.fileprovider", file);
            intent.putExtra("output", this.capturedUri);
            startActivityForResult(intent, 0);
        }
    }

    void choosePhoto() {
        startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 1);
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 3) {
            if (i2 == -1) {
                uploadProfilePhoto(UCrop.getOutput(intent));
                return;
            }
            return;
        }
        switch (i) {
            case 0:
                if (i2 == -1) {
                    Uri uri = this.capturedUri;
                    this.didTakePhoto = true;
                    showConfirmAlert(uri);
                    return;
                }
                return;
            case 1:
                if (i2 == -1) {
                    Uri data = intent.getData();
                    this.didTakePhoto = false;
                    showConfirmAlert(data);
                    return;
                }
                return;
            default:
                return;
        }
    }

    void showConfirmAlert(final Uri uri) {
        new AlertDialog.Builder(this.context).setTitle((CharSequence) null).setMessage("Upload Photo").setPositiveButton("OK", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileUserView.15
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                TreadlyProfileUserView.this.confirmed = true;
                dialogInterface.dismiss();
                TreadlyProfileUserView.this.cropPhoto(uri);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileUserView.14
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                TreadlyProfileUserView.this.confirmed = false;
                dialogInterface.dismiss();
            }
        }).show();
    }

    void showAlertWithTimerAndPop(String str, String str2, int i) {
        final AlertDialog create = new AlertDialog.Builder(this.context).setTitle(str).setMessage(str2).create();
        create.show();
        if (getActivity() != null) {
            ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileUserView.16
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        create.dismiss();
                        TreadlyProfileUserView.this.getActivity().getSupportFragmentManager().popBackStack();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    void showAlertWithTimer(String str, String str2, final int i) {
        final AlertDialog create = new AlertDialog.Builder(this.context).setTitle(str).setMessage(str2).create();
        create.show();
        if (getActivity() != null) {
            ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileUserView.17
                @Override // java.lang.Runnable
                public void run() {
                    new Handler().postDelayed(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileUserView.17.1
                        @Override // java.lang.Runnable
                        public void run() {
                            create.dismiss();
                        }
                    }, i * 1000);
                }
            });
        }
    }

    public Bitmap scaleBitmapAndKeepRation(Bitmap bitmap, int i, int i2) {
        Matrix matrix = new Matrix();
        matrix.setRectToRect(new RectF(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight()), new RectF(0.0f, 0.0f, i2, i), Matrix.ScaleToFit.CENTER);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cropPhoto(Uri uri) {
        if (getContext() == null) {
            return;
        }
        try {
            startActivityForResult(UCrop.of(uri, Uri.fromFile(createImageFile())).withAspectRatio(1.0f, 1.0f).withOptions(getOptions()).getIntent(getContext()), 3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    UCrop.Options getOptions() {
        UCrop.Options options = new UCrop.Options();
        options.setHideBottomControls(true);
        options.setFreeStyleCropEnabled(true);
        return options;
    }

    void uploadProfilePhoto(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.context.getContentResolver(), uri);
            if (bitmap == null) {
                showAlertWithTimer("Error", "Error getting image", 2);
            }
            int width = bitmap.getWidth();
            Bitmap scaleBitmapAndKeepRation = scaleBitmapAndKeepRation(bitmap, (int) (bitmap.getHeight() * 0.3f), (int) (width * 0.3f));
            File file = null;
            if (getActivity() != null) {
                file = new File(getActivity().getFilesDir() + "/temp_profile.png");
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
                scaleBitmapAndKeepRation.compress(Bitmap.CompressFormat.PNG, 100, bufferedOutputStream);
                bufferedOutputStream.close();
            }
            if (file != null) {
                TreadlyServiceManager.getInstance().setUserImage(file, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileUserView.18
                    @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                    public void onSuccess(String str) {
                        final String str2 = str == null ? "Photo successfully uploaded" : "Error uploading photo";
                        if (str == null) {
                            TreadlyServiceManager.getInstance().refreshUserInfo(new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileUserView.18.1
                                @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                                public void onUserInfo(String str3, UserInfo userInfo) {
                                    if (str3 == null && userInfo.avatarPath != null) {
                                        TreadlyProfileUserView.this.updateUi();
                                        ((MainActivity) TreadlyProfileUserView.this.getActivity()).updateNavProfilePicture();
                                    }
                                    TreadlyProfileUserView.this.showAlertWithTimer(null, str2, 2);
                                }
                            });
                        } else {
                            TreadlyProfileUserView.this.showAlertWithTimer("Error", "Error fetching updated profile information", 2);
                        }
                    }
                });
            } else {
                showAlertWithTimer("Error", "Error getting image", 2);
            }
        } catch (Exception unused) {
            showAlertWithTimer("Error", "Error uploading photo", 2);
        }
    }
}
