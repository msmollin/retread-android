package com.treadly.Treadly.UI.Onboarding.InviteCode;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.FragmentActivity;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Managers.TreadlyActivationManager;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper;
import com.treadly.Treadly.UI.Util.ActivityUtil;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;

/* loaded from: classes2.dex */
public class TreadlyOnboardingInviteCodeFragment extends BaseFragment {
    public static final String TAG = "INVITE_CODE";
    TextView attemptsLabel;
    AppCompatEditText inviteCode;
    protected final int MAX_ATTEMPTS = 3;
    public didFailAttemptsListener didFailAttempts = null;
    int attemptsLeft = 3;

    /* loaded from: classes2.dex */
    public interface didFailAttemptsListener {
        void onResponse();
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_onboarding_invite_code, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        ((ImageButton) view.findViewById(R.id.onboarding_invite_code_back_button)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.Onboarding.InviteCode.-$$Lambda$TreadlyOnboardingInviteCodeFragment$mOLJxVUzQs7LvIpV6ndxFz-TcIs
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyOnboardingInviteCodeFragment.this.popBackStack();
            }
        });
        this.attemptsLabel = (TextView) view.findViewById(R.id.onboarding_invite_code_attempts);
        this.inviteCode = (AppCompatEditText) view.findViewById(R.id.onboarding_invite_code_input);
        ((Button) view.findViewById(R.id.onboarding_invite_code_submit)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.Onboarding.InviteCode.-$$Lambda$ja1a_H5h3y81GU1CmDZonO1zZXM
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyOnboardingInviteCodeFragment.this.onSubmitPressed(view2);
            }
        });
        updateAttemptsLabel();
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        hideKeyboard();
    }

    @SuppressLint({"DefaultLocale"})
    void updateAttemptsLabel() {
        if (this.attemptsLabel == null) {
            return;
        }
        this.attemptsLabel.setText(getResources().getText(R.string.onboarding_invite_code_attempts_left).toString().replace("%%REPLACE%%", String.format("%d", Integer.valueOf(this.attemptsLeft))));
        this.attemptsLabel.setVisibility(this.attemptsLeft >= 3 ? 4 : 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSubmitPressed(View view) {
        if (this.attemptsLabel == null || this.inviteCode == null || this.inviteCode.getText() == null || this.inviteCode.getText().length() == 0) {
            return;
        }
        showLoading();
        VideoServiceHelper.activateInviteCode(this.inviteCode.getText().toString().trim(), new VideoServiceHelper.VideoResponseListener() { // from class: com.treadly.Treadly.UI.Onboarding.InviteCode.-$$Lambda$TreadlyOnboardingInviteCodeFragment$OVoMwsYI8KRHxXJEVPn4glRS-QE
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoResponseListener
            public final void onResponse(String str) {
                TreadlyOnboardingInviteCodeFragment.lambda$onSubmitPressed$2(TreadlyOnboardingInviteCodeFragment.this, str);
            }
        });
    }

    public static /* synthetic */ void lambda$onSubmitPressed$2(final TreadlyOnboardingInviteCodeFragment treadlyOnboardingInviteCodeFragment, String str) {
        treadlyOnboardingInviteCodeFragment.dismissLoading();
        if (str != null) {
            treadlyOnboardingInviteCodeFragment.attemptsLeft--;
            if (treadlyOnboardingInviteCodeFragment.attemptsLeft > 0) {
                ActivityUtil.runOnUiThread(treadlyOnboardingInviteCodeFragment.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.Onboarding.InviteCode.-$$Lambda$TreadlyOnboardingInviteCodeFragment$MFGyx_vpsUZWJ4uUAd8RmWQ1sso
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyOnboardingInviteCodeFragment.lambda$null$1(TreadlyOnboardingInviteCodeFragment.this);
                    }
                });
                return;
            }
            if (treadlyOnboardingInviteCodeFragment.didFailAttempts != null) {
                treadlyOnboardingInviteCodeFragment.didFailAttempts.onResponse();
            }
            treadlyOnboardingInviteCodeFragment.popBackStack();
            return;
        }
        treadlyOnboardingInviteCodeFragment.skipOnboarding();
    }

    public static /* synthetic */ void lambda$null$1(TreadlyOnboardingInviteCodeFragment treadlyOnboardingInviteCodeFragment) {
        treadlyOnboardingInviteCodeFragment.updateAttemptsLabel();
        treadlyOnboardingInviteCodeFragment.inviteCode.setText("");
    }

    void skipOnboarding() {
        TreadlyServiceManager.getInstance().updateOnboardingStatus(new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.Onboarding.InviteCode.TreadlyOnboardingInviteCodeFragment.1
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onSuccess(String str) {
                if (str == null) {
                    TreadlyActivationManager.shared.setUsingInviteCode(true);
                    MainActivity mainActivity = (MainActivity) TreadlyOnboardingInviteCodeFragment.this.getActivity();
                    TreadlyOnboardingInviteCodeFragment.this.hideKeyboard();
                    if (mainActivity != null) {
                        mainActivity.toConnect();
                        return;
                    }
                    return;
                }
                TreadlyOnboardingInviteCodeFragment.this.showBaseAlert("Oops!", "There was an error. Please try again.");
            }
        });
    }

    public void hideKeyboard() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService("input_method");
        View currentFocus = activity.getCurrentFocus();
        if (currentFocus == null) {
            currentFocus = new View(activity);
        }
        if (inputMethodManager == null) {
            return;
        }
        inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
    }
}
