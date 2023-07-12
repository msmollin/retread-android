package com.treadly.Treadly.UI.TreadlyBuddy.Info;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.treadly.Treadly.Data.Model.BuddyProfileInfo;
import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;

/* loaded from: classes2.dex */
public class TreadlyBuddyInfoFragment extends BaseFragment {
    private static final int DAYS_IN_WEEK = 7;
    public static final String TAG = "TREADLY_BUDDY_INFO";
    private static final int WEEKDAY_INDEX_FR = 5;
    private static final int WEEKDAY_INDEX_MO = 1;
    private static final int WEEKDAY_INDEX_SA = 6;
    private static final int WEEKDAY_INDEX_SU = 0;
    private static final int WEEKDAY_INDEX_TH = 4;
    private static final int WEEKDAY_INDEX_TU = 2;
    private static final int WEEKDAY_INDEX_WE = 3;
    private TextView audioIntroPlayText;
    private MediaPlayer audioPlayer;
    private TextView descriptionText;
    private TextView interestsText;
    private TextView locationText;
    private TextView participatingTeamsText;
    private CircularImageView profilePicture;
    private TextView timeAvailableText;
    public BuddyProfileInfo userInfo;
    private TextView usernameStepsText;
    private TextView videoIntroPlayText;
    private VideoView videoIntroView;
    private MediaController videoMediaController;
    private TextView videoViewStatusText;
    private View videoViewWrapper;
    private TextView[] weekdayBlockText;

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_buddy_info, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        hideBottomNavigation();
        initHeader(view);
        initVideoHeader(view);
        if (this.userInfo == null) {
            return;
        }
        this.profilePicture = (CircularImageView) view.findViewById(R.id.buddy_info_avatar);
        this.usernameStepsText = (TextView) view.findViewById(R.id.buddy_info_main_stats_name_and_steps);
        this.locationText = (TextView) view.findViewById(R.id.buddy_info_main_stats_location);
        this.interestsText = (TextView) view.findViewById(R.id.buddy_info_main_stats_interests);
        this.descriptionText = (TextView) view.findViewById(R.id.buddy_info_description_text);
        this.timeAvailableText = (TextView) view.findViewById(R.id.buddy_info_time_text);
        this.participatingTeamsText = (TextView) view.findViewById(R.id.buddy_info_participating_teams_text);
        this.audioIntroPlayText = (TextView) view.findViewById(R.id.buddy_info_audio_button);
        if (this.userInfo.audioPath != null && this.userInfo.audioPath.length() > 0) {
            this.audioIntroPlayText.setText(R.string.buddy_info_play_video_audio);
            this.audioIntroPlayText.setTextColor(ResourcesCompat.getColor(getResources(), R.color.buddy_media_available_text, null));
            this.audioIntroPlayText.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.Info.-$$Lambda$TreadlyBuddyInfoFragment$jyCCCnlOlSvyGF421PGuodoRXGs
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    TreadlyBuddyInfoFragment.this.playAudioIntro();
                }
            });
        } else {
            this.audioIntroPlayText.setText(R.string.buddy_info_no_video_audio);
            this.audioIntroPlayText.setTextColor(ResourcesCompat.getColor(getResources(), R.color.buddy_media_unavailable_text, null));
        }
        this.videoIntroPlayText = (TextView) view.findViewById(R.id.buddy_info_video_button);
        if (this.userInfo.videoPath != null && this.userInfo.videoPath.length() > 0) {
            this.videoIntroPlayText.setText(R.string.buddy_info_play_video_audio);
            this.videoIntroPlayText.setTextColor(ResourcesCompat.getColor(getResources(), R.color.buddy_media_available_text, null));
            this.videoIntroPlayText.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.Info.-$$Lambda$TreadlyBuddyInfoFragment$iJIoZ_pm4p5vfZAju5krXX1_pZ4
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    TreadlyBuddyInfoFragment.this.playVideoIntro();
                }
            });
        } else {
            this.videoIntroPlayText.setText(R.string.buddy_info_no_video_audio);
            this.videoIntroPlayText.setTextColor(ResourcesCompat.getColor(getResources(), R.color.buddy_media_unavailable_text, null));
        }
        this.videoViewStatusText = (TextView) view.findViewById(R.id.buddy_info_video_status);
        this.videoViewWrapper = view.findViewById(R.id.buddy_info_video_play_wrapper);
        this.videoIntroView = (VideoView) view.findViewById(R.id.buddy_info_video_play_view);
        this.videoIntroView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.Info.-$$Lambda$TreadlyBuddyInfoFragment$ZsaWtKu3e8aFmt_L7MDbIC_loyw
            @Override // android.media.MediaPlayer.OnPreparedListener
            public final void onPrepared(MediaPlayer mediaPlayer) {
                TreadlyBuddyInfoFragment.this.videoViewStatusText.setText(R.string.buddy_info_media_playing);
            }
        });
        this.videoIntroView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.Info.-$$Lambda$TreadlyBuddyInfoFragment$vCHU7k-0sQXkibx1sKlzt62-Vf0
            @Override // android.media.MediaPlayer.OnCompletionListener
            public final void onCompletion(MediaPlayer mediaPlayer) {
                TreadlyBuddyInfoFragment.this.videoViewStatusText.setText(R.string.buddy_info_media_finished);
            }
        });
        this.weekdayBlockText = new TextView[7];
        this.weekdayBlockText[0] = (TextView) view.findViewById(R.id.buddy_info_availability_su);
        this.weekdayBlockText[1] = (TextView) view.findViewById(R.id.buddy_info_availability_mo);
        this.weekdayBlockText[2] = (TextView) view.findViewById(R.id.buddy_info_availability_tu);
        this.weekdayBlockText[3] = (TextView) view.findViewById(R.id.buddy_info_availability_we);
        this.weekdayBlockText[4] = (TextView) view.findViewById(R.id.buddy_info_availability_th);
        this.weekdayBlockText[5] = (TextView) view.findViewById(R.id.buddy_info_availability_fr);
        this.weekdayBlockText[6] = (TextView) view.findViewById(R.id.buddy_info_availability_sa);
        Picasso.get().load(Uri.parse(this.userInfo.avatarPath)).tag(getContext()).into(this.profilePicture);
        TextView textView = this.usernameStepsText;
        textView.setText(this.userInfo.userName + "    Steps:" + this.userInfo.step);
        this.locationText.setText(this.userInfo.location);
        this.descriptionText.setText(this.userInfo.lookingForMessage);
        this.participatingTeamsText.setText(getString(R.string.buddy_info_group_label, Integer.valueOf(this.userInfo.groupCount)));
        if (this.userInfo.interests.size() == 0) {
            this.interestsText.setText("< No Interests >");
        } else {
            StringBuilder sb = new StringBuilder();
            for (BuddyProfileInfo.BuddyInterest buddyInterest : this.userInfo.interests) {
                sb.append(buddyInterest.title);
                sb.append(", ");
            }
            sb.setLength(sb.length() - 2);
            this.interestsText.setText(sb.toString());
        }
        if (this.userInfo.times.size() == 0) {
            this.timeAvailableText.setText("< No availability set >");
        } else {
            StringBuilder sb2 = new StringBuilder();
            for (BuddyProfileInfo.BuddyDayTimeType buddyDayTimeType : this.userInfo.times) {
                sb2.append(buddyDayTimeType.title);
                sb2.append(", ");
            }
            sb2.setLength(sb2.length() - 2);
            this.timeAvailableText.setText(sb2.toString());
        }
        for (BuddyProfileInfo.BuddyDayTime buddyDayTime : this.userInfo.daysOfWeek) {
            int i = buddyDayTime.dayOfWeek;
            if (i < 7) {
                this.weekdayBlockText[i].setBackgroundColor(getResources().getColor(R.color.buddy_day_available));
            }
        }
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        if (getActivity() != null) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.setOnBackPressedListener(null);
            mainActivity.showBottomNavigationView();
        }
    }

    private void initHeader(View view) {
        ((TextView) view.findViewById(R.id.nav_title)).setText(R.string.buddy_info_title);
        ((ImageView) view.findViewById(R.id.nav_back_arrow)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.Info.-$$Lambda$TreadlyBuddyInfoFragment$NCWh5rgHG7x5dkGPbC_YhTS_HjA
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyBuddyInfoFragment.lambda$initHeader$4(TreadlyBuddyInfoFragment.this, view2);
            }
        });
    }

    public static /* synthetic */ void lambda$initHeader$4(TreadlyBuddyInfoFragment treadlyBuddyInfoFragment, View view) {
        if (treadlyBuddyInfoFragment.getActivity() != null) {
            if (treadlyBuddyInfoFragment.audioPlayer != null) {
                treadlyBuddyInfoFragment.audioPlayer.release();
                treadlyBuddyInfoFragment.audioPlayer = null;
            }
            treadlyBuddyInfoFragment.getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    private void initVideoHeader(View view) {
        ((TextView) view.findViewById(R.id.buddy_video_title)).setText(R.string.buddy_info_video_intro);
        ((ImageView) view.findViewById(R.id.buddy_video_back_arrow)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.Info.-$$Lambda$TreadlyBuddyInfoFragment$UlJX66l9G7IG3-dfgjzlr2Z2cZk
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyBuddyInfoFragment.lambda$initVideoHeader$5(TreadlyBuddyInfoFragment.this, view2);
            }
        });
    }

    public static /* synthetic */ void lambda$initVideoHeader$5(TreadlyBuddyInfoFragment treadlyBuddyInfoFragment, View view) {
        treadlyBuddyInfoFragment.videoIntroView.stopPlayback();
        treadlyBuddyInfoFragment.videoIntroView.seekTo(0);
        treadlyBuddyInfoFragment.videoIntroView.setVisibility(8);
        treadlyBuddyInfoFragment.videoViewWrapper.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void playAudioIntro() {
        if (this.userInfo.audioPath != null && this.audioPlayer == null) {
            try {
                this.audioPlayer = new MediaPlayer();
                this.audioPlayer.setDataSource(this.userInfo.audioPath);
                this.audioIntroPlayText.setTextColor(ResourcesCompat.getColor(getResources(), R.color.buddy_media_unavailable_text, null));
                this.audioIntroPlayText.setText(R.string.buddy_info_media_loading);
                this.audioPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.Info.-$$Lambda$TreadlyBuddyInfoFragment$NJm0rF2YLRRNGxmJ7BlpX5uuwS8
                    @Override // android.media.MediaPlayer.OnPreparedListener
                    public final void onPrepared(MediaPlayer mediaPlayer) {
                        TreadlyBuddyInfoFragment.lambda$playAudioIntro$6(TreadlyBuddyInfoFragment.this, mediaPlayer);
                    }
                });
                this.audioPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.Info.-$$Lambda$TreadlyBuddyInfoFragment$qMBWKjII1V0_WskrsRoh1eGIYCs
                    @Override // android.media.MediaPlayer.OnCompletionListener
                    public final void onCompletion(MediaPlayer mediaPlayer) {
                        TreadlyBuddyInfoFragment.lambda$playAudioIntro$7(TreadlyBuddyInfoFragment.this, mediaPlayer);
                    }
                });
                this.audioPlayer.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error Loading Audio", 0).show();
                this.audioIntroPlayText.setTextColor(ResourcesCompat.getColor(getResources(), R.color.buddy_media_available_text, null));
                this.audioIntroPlayText.setText(R.string.buddy_info_play_video_audio);
                this.audioPlayer = null;
            }
        }
    }

    public static /* synthetic */ void lambda$playAudioIntro$6(TreadlyBuddyInfoFragment treadlyBuddyInfoFragment, MediaPlayer mediaPlayer) {
        treadlyBuddyInfoFragment.audioIntroPlayText.setText(R.string.buddy_info_media_playing);
        treadlyBuddyInfoFragment.audioPlayer.start();
    }

    public static /* synthetic */ void lambda$playAudioIntro$7(TreadlyBuddyInfoFragment treadlyBuddyInfoFragment, MediaPlayer mediaPlayer) {
        treadlyBuddyInfoFragment.audioIntroPlayText.setTextColor(ResourcesCompat.getColor(treadlyBuddyInfoFragment.getResources(), R.color.buddy_media_available_text, null));
        treadlyBuddyInfoFragment.audioIntroPlayText.setText(R.string.buddy_info_play_video_audio);
        treadlyBuddyInfoFragment.audioPlayer.release();
        treadlyBuddyInfoFragment.audioPlayer = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void playVideoIntro() {
        if (this.userInfo.videoPath == null) {
            return;
        }
        this.videoViewStatusText.setText(R.string.buddy_info_media_loading);
        this.videoViewWrapper.setVisibility(0);
        this.videoIntroView.setVisibility(0);
        this.videoIntroView.setVideoURI(Uri.parse(this.userInfo.videoPath));
        if (this.videoMediaController != null) {
            this.videoMediaController = new MediaController(getContext());
            this.videoMediaController.setAnchorView(this.videoIntroView);
            this.videoMediaController.setMediaPlayer(this.videoIntroView);
        }
        this.videoIntroView.start();
    }
}
