package com.treadly.Treadly.UI.TreadlyBuddy.Manage;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.Data.Model.BuddyProfileInfo;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyBuddy.Manage.Interests.TreadlyBuddyProfileEditInterestsFragment;
import com.treadly.Treadly.UI.TreadlyBuddy.Manage.TimeOfDay.TreadlyBuddyProfileEditTimeFragment;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceVideoService;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoUploaderManager;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;
import org.json.JSONException;

/* loaded from: classes2.dex */
public class TreadlyBuddyProfileEditFragment extends BaseFragment implements TreadlyBuddyProfileEditInterestsFragment.OnReturnListener, TreadlyBuddyProfileEditTimeFragment.OnReturnListener, TextWatcher {
    private static final String AUDIO_FILE_SUFFIX = ".m4a";
    private static final int DAYS_IN_WEEK = 7;
    private static final int MAX_MEDIA_TIME_MILLIS = 30000;
    public static final String TAG = "TREADLY_BUDDY_PROFILE_EDIT";
    private static final String VIDEO_FILE_SUFFIX = ".mp4";
    private static final int VIDEO_REQUEST_CODE = 100;
    private static final int WEEKDAY_INDEX_FR = 5;
    private static final int WEEKDAY_INDEX_MO = 1;
    private static final int WEEKDAY_INDEX_SA = 6;
    private static final int WEEKDAY_INDEX_SU = 0;
    private static final int WEEKDAY_INDEX_TH = 4;
    private static final int WEEKDAY_INDEX_TU = 2;
    private static final int WEEKDAY_INDEX_WE = 3;
    private TextView audioTextButton;
    private TextView audioTextStatus;
    private BuddyProfileInfo buddyInfo;
    private boolean buddyProfileCreated;
    private TextView createTextButton;
    private TextView deleteTextButton;
    private TextView interestsTextButton;
    private EditText profileDescriptionText;
    private TextView updateTextButton;
    private TextView videoTextButton;
    private TextView videoTextStatus;
    private TextView[] weekdayBlockText;
    private Intent videoData = null;
    private MediaRecorder mediaRecorder = null;
    private String audioData = null;

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_buddy_profile_edit, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        initHeader(view);
        getBuddyProfileInfo();
        this.profileDescriptionText = (EditText) view.findViewById(R.id.buddy_profile_edit_description_text);
        this.profileDescriptionText.addTextChangedListener(this);
        this.videoTextButton = (TextView) view.findViewById(R.id.buddy_profile_edit_video);
        this.videoTextButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.Manage.-$$Lambda$TreadlyBuddyProfileEditFragment$uJds7E5FrwIQ0uc7Kkn8Lg0G9NA
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyBuddyProfileEditFragment.this.onVideoClicked();
            }
        });
        this.videoTextStatus = (TextView) view.findViewById(R.id.buddy_profile_video_status);
        this.audioTextButton = (TextView) view.findViewById(R.id.buddy_profile_edit_audio);
        this.audioTextButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.Manage.-$$Lambda$TreadlyBuddyProfileEditFragment$q9-c110IsqSw_jyNHoRMAoWD-K4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyBuddyProfileEditFragment.this.onAudioClicked();
            }
        });
        this.audioTextStatus = (TextView) view.findViewById(R.id.buddy_profile_audio_status);
        this.interestsTextButton = (TextView) view.findViewById(R.id.buddy_profile_edit_interests);
        this.interestsTextButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.Manage.-$$Lambda$TreadlyBuddyProfileEditFragment$jwYdw_dJb1Dv71CUhdmundMOlo0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyBuddyProfileEditFragment.this.onInterestsClicked();
            }
        });
        this.weekdayBlockText = new TextView[7];
        this.weekdayBlockText[0] = (TextView) view.findViewById(R.id.buddy_edit_availability_su);
        this.weekdayBlockText[1] = (TextView) view.findViewById(R.id.buddy_edit_availability_mo);
        this.weekdayBlockText[2] = (TextView) view.findViewById(R.id.buddy_edit_availability_tu);
        this.weekdayBlockText[3] = (TextView) view.findViewById(R.id.buddy_edit_availability_we);
        this.weekdayBlockText[4] = (TextView) view.findViewById(R.id.buddy_edit_availability_th);
        this.weekdayBlockText[5] = (TextView) view.findViewById(R.id.buddy_edit_availability_fr);
        this.weekdayBlockText[6] = (TextView) view.findViewById(R.id.buddy_edit_availability_sa);
        this.weekdayBlockText[0].setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.Manage.-$$Lambda$TreadlyBuddyProfileEditFragment$fXmw3fcPZODU9eR333qCWZ_FW0I
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyBuddyProfileEditFragment.this.onDayClicked(0);
            }
        });
        this.weekdayBlockText[0].setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.Manage.-$$Lambda$TreadlyBuddyProfileEditFragment$D55s1CjXT2yMHF2STZVLc_qhHKg
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyBuddyProfileEditFragment.this.onDayClicked(0);
            }
        });
        this.weekdayBlockText[1].setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.Manage.-$$Lambda$TreadlyBuddyProfileEditFragment$YdAVJ_HRx9IgxOcN7hfXrJgBt3Y
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyBuddyProfileEditFragment.this.onDayClicked(1);
            }
        });
        this.weekdayBlockText[2].setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.Manage.-$$Lambda$TreadlyBuddyProfileEditFragment$Ci8cj0FykrrSX5daJTLRSa9fe0w
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyBuddyProfileEditFragment.this.onDayClicked(2);
            }
        });
        this.weekdayBlockText[3].setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.Manage.-$$Lambda$TreadlyBuddyProfileEditFragment$FuRc_YBD3jhbT760FgvF6nfZgyY
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyBuddyProfileEditFragment.this.onDayClicked(3);
            }
        });
        this.weekdayBlockText[4].setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.Manage.-$$Lambda$TreadlyBuddyProfileEditFragment$ZVjG2-1CeKS004ilQXeN6U_RHZA
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyBuddyProfileEditFragment.this.onDayClicked(4);
            }
        });
        this.weekdayBlockText[5].setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.Manage.-$$Lambda$TreadlyBuddyProfileEditFragment$uHzydpuKHVQVmMy8RT8eH_ubXfs
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyBuddyProfileEditFragment.this.onDayClicked(5);
            }
        });
        this.weekdayBlockText[6].setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.Manage.-$$Lambda$TreadlyBuddyProfileEditFragment$ojfndZE8iK6sv8MpY5YRZDSdQEQ
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyBuddyProfileEditFragment.this.onDayClicked(6);
            }
        });
        this.createTextButton = (TextView) view.findViewById(R.id.buddy_profile_edit_create_button);
        this.updateTextButton = (TextView) view.findViewById(R.id.buddy_profile_edit_update_button);
        this.deleteTextButton = (TextView) view.findViewById(R.id.buddy_profile_edit_delete_button);
    }

    @Override // com.treadly.Treadly.UI.TreadlyBuddy.Manage.Interests.TreadlyBuddyProfileEditInterestsFragment.OnReturnListener
    public void onBuddyProfileEditInterestReturn() {
        updateUi();
    }

    @Override // com.treadly.Treadly.UI.TreadlyBuddy.Manage.TimeOfDay.TreadlyBuddyProfileEditTimeFragment.OnReturnListener
    public void onBuddyProfileEditTimeOfDayReturn() {
        updateUi();
    }

    private void initHeader(View view) {
        ((TextView) view.findViewById(R.id.nav_title)).setText(R.string.buddy_profile_settings_title);
        ((ImageView) view.findViewById(R.id.nav_back_arrow)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.Manage.-$$Lambda$TreadlyBuddyProfileEditFragment$YvOyLUxVOmNg6_-k8a4TLzBTOQc
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyBuddyProfileEditFragment.lambda$initHeader$11(TreadlyBuddyProfileEditFragment.this, view2);
            }
        });
    }

    public static /* synthetic */ void lambda$initHeader$11(TreadlyBuddyProfileEditFragment treadlyBuddyProfileEditFragment, View view) {
        if (treadlyBuddyProfileEditFragment.mediaRecorder != null) {
            treadlyBuddyProfileEditFragment.mediaRecorder.release();
        }
        treadlyBuddyProfileEditFragment.popBackStack();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onVideoClicked() {
        if (getActivity() == null) {
            return;
        }
        startActivityForResult(new Intent("android.media.action.VIDEO_CAPTURE"), 100);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAudioClicked() {
        if (getActivity() == null) {
            return;
        }
        if (this.mediaRecorder == null) {
            String absolutePath = getContext().getExternalCacheDir().getAbsolutePath();
            this.audioData = absolutePath + "/buddy_profile_audio_clip" + AUDIO_FILE_SUFFIX;
            this.mediaRecorder = new MediaRecorder();
            this.mediaRecorder.setAudioSource(1);
            this.mediaRecorder.setOutputFormat(2);
            this.mediaRecorder.setAudioEncoder(3);
            this.mediaRecorder.setOutputFile(this.audioData);
            try {
                this.mediaRecorder.prepare();
            } catch (Exception e) {
                this.mediaRecorder = null;
                e.printStackTrace();
            }
            this.mediaRecorder.start();
            this.audioTextButton.setText(R.string.buddy_profile_edit_audio_stop);
            return;
        }
        this.mediaRecorder.release();
        this.mediaRecorder = null;
        try {
            if (getMediaFileDuration(Uri.parse(this.audioData)) > 30000) {
                Toast.makeText(getContext(), "Error: Maximum audio time is 30 seconds", 1).show();
                this.audioData = null;
            } else {
                Toast.makeText(getContext(), "Audio successfully saved", 0).show();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(getContext(), "Error parsing audio", 0).show();
        }
        this.audioTextButton.setText(R.string.buddy_profile_edit_audio_start);
        updateUi();
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && i == 100) {
            if (getMediaFileDuration(intent.getData()) > 30000) {
                Toast.makeText(getContext(), "Error: Maximum video time is 30 seconds", 1).show();
                return;
            }
            Toast.makeText(getContext(), "Video successfully saved", 0).show();
            this.videoData = intent;
            updateUi();
        }
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        this.buddyInfo.lookingForMessage = charSequence.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onInterestsClicked() {
        TreadlyBuddyProfileEditInterestsFragment treadlyBuddyProfileEditInterestsFragment = new TreadlyBuddyProfileEditInterestsFragment();
        treadlyBuddyProfileEditInterestsFragment.retListener = this;
        treadlyBuddyProfileEditInterestsFragment.userInterests = this.buddyInfo.interests;
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.activity_fragment_container_empty, treadlyBuddyProfileEditInterestsFragment, TreadlyBuddyProfileEditInterestsFragment.TAG).commit();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDayClicked(int i) {
        TreadlyBuddyProfileEditTimeFragment treadlyBuddyProfileEditTimeFragment = new TreadlyBuddyProfileEditTimeFragment();
        treadlyBuddyProfileEditTimeFragment.retListener = this;
        treadlyBuddyProfileEditTimeFragment.userDaysAvailable = this.buddyInfo.daysOfWeek;
        treadlyBuddyProfileEditTimeFragment.dayId = i;
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.activity_fragment_container_empty, treadlyBuddyProfileEditTimeFragment, TreadlyBuddyProfileEditTimeFragment.TAG).commit();
        }
    }

    private void getBuddyProfileInfo() {
        showLoading();
        final UserInfo userInfo = TreadlyServiceManager.getInstance().getUserInfo();
        VideoServiceHelper.getBuddyProfileInfo(userInfo.id, new VideoServiceHelper.BuddyGetProfileListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.Manage.-$$Lambda$TreadlyBuddyProfileEditFragment$kwdF223crZos6wwJ-qyXfStw2qY
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.BuddyGetProfileListener
            public final void onResponse(String str, BuddyProfileInfo buddyProfileInfo) {
                TreadlyBuddyProfileEditFragment.lambda$getBuddyProfileInfo$12(TreadlyBuddyProfileEditFragment.this, userInfo, str, buddyProfileInfo);
            }
        });
    }

    public static /* synthetic */ void lambda$getBuddyProfileInfo$12(TreadlyBuddyProfileEditFragment treadlyBuddyProfileEditFragment, UserInfo userInfo, String str, BuddyProfileInfo buddyProfileInfo) {
        if (str != null || buddyProfileInfo == null) {
            treadlyBuddyProfileEditFragment.buddyInfo = new BuddyProfileInfo(userInfo.id, userInfo.name, userInfo.avatarPath);
            treadlyBuddyProfileEditFragment.buddyProfileCreated = false;
        } else {
            treadlyBuddyProfileEditFragment.buddyInfo = buddyProfileInfo;
            treadlyBuddyProfileEditFragment.buddyProfileCreated = true;
        }
        treadlyBuddyProfileEditFragment.updateUi();
        treadlyBuddyProfileEditFragment.dismissLoading();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createBuddyProfile() {
        if (this.mediaRecorder != null) {
            this.mediaRecorder.release();
            this.mediaRecorder = null;
        }
        showLoading();
        VideoServiceHelper.createBuddyProfile(this.buddyInfo, new VideoServiceHelper.BuddyCreateProfileListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.Manage.-$$Lambda$TreadlyBuddyProfileEditFragment$jeeh1TTFgwuc2f8skpBLJf_U4nI
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.BuddyCreateProfileListener
            public final void onResponse(String str, String str2) {
                TreadlyBuddyProfileEditFragment.lambda$createBuddyProfile$13(TreadlyBuddyProfileEditFragment.this, str, str2);
            }
        });
    }

    public static /* synthetic */ void lambda$createBuddyProfile$13(TreadlyBuddyProfileEditFragment treadlyBuddyProfileEditFragment, String str, String str2) {
        if (str != null) {
            PrintStream printStream = System.out;
            printStream.println("BTM :: ERROR CREATING BUDDY PROFILE: " + str);
            return;
        }
        treadlyBuddyProfileEditFragment.prepareAndSendMedia(str2);
        treadlyBuddyProfileEditFragment.dismissLoading();
        treadlyBuddyProfileEditFragment.popBackStack();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBuddyProfile() {
        if (this.mediaRecorder != null) {
            this.mediaRecorder.release();
            this.mediaRecorder = null;
        }
        showLoading();
        VideoServiceHelper.updateBuddyProfile(this.buddyInfo, new VideoServiceHelper.BuddyUpdateProfileListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.Manage.-$$Lambda$TreadlyBuddyProfileEditFragment$5PpkuiiFI9NjHKGmtlNF4ZBEbuY
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.BuddyUpdateProfileListener
            public final void onResponse(String str, String str2) {
                TreadlyBuddyProfileEditFragment.lambda$updateBuddyProfile$14(TreadlyBuddyProfileEditFragment.this, str, str2);
            }
        });
    }

    public static /* synthetic */ void lambda$updateBuddyProfile$14(TreadlyBuddyProfileEditFragment treadlyBuddyProfileEditFragment, String str, String str2) {
        if (str != null) {
            PrintStream printStream = System.out;
            printStream.println("BTM :: ERROR UPDATING BUDDY PROFILE: " + str);
            return;
        }
        treadlyBuddyProfileEditFragment.prepareAndSendMedia(str2);
        treadlyBuddyProfileEditFragment.dismissLoading();
        treadlyBuddyProfileEditFragment.popBackStack();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deleteBuddyProfile() {
        if (this.mediaRecorder != null) {
            this.mediaRecorder.release();
            this.mediaRecorder = null;
        }
        showLoading();
        VideoServiceHelper.deleteBuddyProfile(new VideoServiceHelper.BuddyDeleteProfileListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.Manage.-$$Lambda$TreadlyBuddyProfileEditFragment$oaZB8S2x8emSmvTylVdOU12ZfIc
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.BuddyDeleteProfileListener
            public final void onResponse(String str) {
                TreadlyBuddyProfileEditFragment.lambda$deleteBuddyProfile$15(TreadlyBuddyProfileEditFragment.this, str);
            }
        });
    }

    public static /* synthetic */ void lambda$deleteBuddyProfile$15(TreadlyBuddyProfileEditFragment treadlyBuddyProfileEditFragment, String str) {
        if (str != null) {
            PrintStream printStream = System.out;
            printStream.println("BTM :: ERROR UPDATING BUDDY PROFILE: " + str);
            return;
        }
        treadlyBuddyProfileEditFragment.dismissLoading();
        treadlyBuddyProfileEditFragment.popBackStack();
    }

    private void prepareAndSendMedia(String str) {
        if (this.videoData == null && this.audioData == null) {
            return;
        }
        try {
            if (this.videoData != null) {
                prepareMediaFile(this.videoData, str, VideoServiceVideoService.buddyProfileVideo);
            }
            if (this.audioData != null) {
                prepareMediaFile(this.audioData, str, VideoServiceVideoService.buddyProfileAudio);
            }
            VideoUploaderManager.context = getContext();
            VideoUploaderManager.shared.start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void prepareMediaFile(String str, String str2, VideoServiceVideoService videoServiceVideoService) {
        try {
            String fileName = VideoUploaderManager.getFileName(str2, "-", videoServiceVideoService, new Date(System.currentTimeMillis()), TreadlyServiceManager.getInstance().getUserId());
            File externalFilesDir = getContext().getExternalFilesDir(Environment.DIRECTORY_MOVIES);
            if (externalFilesDir == null) {
                System.out.println("BTM :: COULD NOT FIND STORAGE DIR FOR MEDIA");
                return;
            }
            FileInputStream createInputStream = getContext().getContentResolver().openAssetFileDescriptor(Uri.fromFile(new File(str)), "r").createInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(new File(externalFilesDir, fileName));
            byte[] bArr = new byte[1024];
            while (true) {
                int read = createInputStream.read(bArr);
                if (read > 0) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    createInputStream.close();
                    fileOutputStream.close();
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getMediaFileDuration(Uri uri) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(getContext(), uri);
        return Integer.parseInt(mediaMetadataRetriever.extractMetadata(9));
    }

    private void prepareMediaFile(Intent intent, String str, VideoServiceVideoService videoServiceVideoService) {
        try {
            String fileName = VideoUploaderManager.getFileName(str, "-", videoServiceVideoService, new Date(System.currentTimeMillis()), TreadlyServiceManager.getInstance().getUserId());
            File externalFilesDir = getContext().getExternalFilesDir(Environment.DIRECTORY_MOVIES);
            if (externalFilesDir == null) {
                System.out.println("BTM :: COULD NOT FIND STORAGE DIR FOR MEDIA");
                return;
            }
            FileInputStream createInputStream = getContext().getContentResolver().openAssetFileDescriptor(intent.getData(), "r").createInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(new File(externalFilesDir, fileName + VIDEO_FILE_SUFFIX));
            byte[] bArr = new byte[1024];
            while (true) {
                int read = createInputStream.read(bArr);
                if (read > 0) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    createInputStream.close();
                    fileOutputStream.close();
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUi() {
        if (this.buddyInfo == null) {
            return;
        }
        this.profileDescriptionText.setText(this.buddyInfo.lookingForMessage);
        if (!this.buddyProfileCreated) {
            this.createTextButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.Manage.-$$Lambda$TreadlyBuddyProfileEditFragment$e8--jCEEOd6ucMkQalC0v5qPtGA
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    TreadlyBuddyProfileEditFragment.this.createBuddyProfile();
                }
            });
            this.updateTextButton.setTextColor(getResources().getColor(R.color.buddy_day_unavailable));
            this.deleteTextButton.setTextColor(getResources().getColor(R.color.buddy_day_unavailable));
        } else {
            this.createTextButton.setTextColor(getResources().getColor(R.color.buddy_day_unavailable));
            this.updateTextButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.Manage.-$$Lambda$TreadlyBuddyProfileEditFragment$OgEVsYs1Nuf_NFgMk3O6iq4xztc
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    TreadlyBuddyProfileEditFragment.this.updateBuddyProfile();
                }
            });
            this.deleteTextButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.Manage.-$$Lambda$TreadlyBuddyProfileEditFragment$USKT03CMCZcHq28V29o0Wp1J2WA
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    TreadlyBuddyProfileEditFragment.this.deleteBuddyProfile();
                }
            });
        }
        for (int i = 0; i < 7; i++) {
            this.weekdayBlockText[i].setBackgroundColor(getResources().getColor(R.color.buddy_day_unavailable));
        }
        for (BuddyProfileInfo.BuddyDayTime buddyDayTime : this.buddyInfo.daysOfWeek) {
            int i2 = buddyDayTime.dayOfWeek;
            if (i2 < 7) {
                this.weekdayBlockText[i2].setBackgroundColor(getResources().getColor(R.color.buddy_day_available));
            }
        }
        if (this.videoData != null || this.buddyInfo.videoPath.length() > 0) {
            this.videoTextStatus.setText(R.string.buddy_profile_media_status_set);
        } else {
            this.videoTextStatus.setText(R.string.buddy_profile_media_status_unset);
        }
        if (this.audioData != null || this.buddyInfo.audioPath.length() > 0) {
            this.audioTextStatus.setText(R.string.buddy_profile_media_status_set);
        } else {
            this.audioTextStatus.setText(R.string.buddy_profile_media_status_unset);
        }
    }
}
