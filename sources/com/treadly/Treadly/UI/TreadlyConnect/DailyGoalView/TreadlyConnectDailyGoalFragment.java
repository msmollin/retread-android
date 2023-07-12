package com.treadly.Treadly.UI.TreadlyConnect.DailyGoalView;

import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyActivity.ActivityCalendar.TreadlyActivityCalendarFragment;
import com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectSliderFragment;
import com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileVideoCollectionFragment;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
import java.util.List;

/* loaded from: classes2.dex */
public class TreadlyConnectDailyGoalFragment extends BaseFragment {
    private static final int BADTYPE = -5;
    public TreadlyConnectDailyGoalAnimationView animationView;
    public TreadlyConnectDailyGoalAnimationView attachedView;
    ImageButton closebutton;
    public TreadlyConnectDailyGoalFragmentListener dailyGoalListener;
    public Uri firstImage;
    TextView goalMessageLabel;
    public TreadlyDailyGoalType type = TreadlyDailyGoalType.dailyGoal100;
    public Uri uri;

    /* loaded from: classes2.dex */
    public interface TreadlyConnectDailyGoalFragmentListener {
        void didPressClose();
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        ((MainActivity) getActivity()).setOnBackPressedListener(null);
        hideBottomNavigation();
        return layoutInflater.inflate(R.layout.fragment_treadly_daily_goal_animation_player, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        if (getActivity() != null) {
            FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
            String str = null;
            if (supportFragmentManager != null) {
                List<Fragment> fragments = supportFragmentManager.getFragments();
                if (!fragments.isEmpty()) {
                    str = fragments.get(fragments.size() - 1).getTag();
                }
            }
            if (str == null) {
                hideBottomNavigation();
            } else if (str.equals(TreadlyConnectSliderFragment.TAG) || str.equals(TreadlyProfileVideoCollectionFragment.TAG) || str.equals(TreadlyActivityCalendarFragment.TAG)) {
                showBottomNavigation();
            } else {
                hideBottomNavigation();
            }
        }
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        ((FrameLayout) view.findViewById(R.id.animation_video)).addView(this.animationView);
        this.animationView.play();
        this.closebutton = (ImageButton) view.findViewById(R.id.animation_close_button);
        this.closebutton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DailyGoalView.TreadlyConnectDailyGoalFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                TreadlyConnectDailyGoalFragment.this.dailyGoalListener.didPressClose();
            }
        });
        this.goalMessageLabel = (TextView) view.findViewById(R.id.animation_goal_message_label);
        this.goalMessageLabel.setText(Html.fromHtml(getString(getGoalText()), 0));
    }

    public void loadAnimation() {
        this.animationView.configure(this.type);
    }

    public Uri getGoalUrl() {
        switch (this.type) {
            case dailyGoal50:
                return Uri.parse("https://dgwxv5s2i5zkb.cloudfront.net/daily_goal_animations/dailyGoal50_android.mp4");
            case dailyGoal100:
                return Uri.parse("https://dgwxv5s2i5zkb.cloudfront.net/daily_goal_animations/dailyGoal100_android.mp4");
            default:
                return null;
        }
    }

    public int getGoalText() {
        switch (this.type) {
            case dailyGoal50:
                return R.string.daily_goal_50;
            case dailyGoal100:
                return R.string.daily_goal_100;
            default:
                return BADTYPE;
        }
    }
}
