package com.treadly.Treadly.UI.Onboarding.NotFound;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;

/* loaded from: classes2.dex */
public class TreadlyOnboardingNotFoundFragment extends BaseFragment {
    public static final String TAG = "ONBOARDING_NOT_FOUND";
    public InviteCodeListener inviteCodeListener;
    public boolean showInviteButton;
    public TryAgainListener tryAgainListener;

    /* loaded from: classes2.dex */
    public interface InviteCodeListener {
        void onInviteCodePressed();
    }

    /* loaded from: classes2.dex */
    public interface TryAgainListener {
        void onTryAgainPressed();
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_onboarding_no_device_found, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        ((Button) view.findViewById(R.id.onboarding_no_device_found_try_again_button)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.Onboarding.NotFound.-$$Lambda$TreadlyOnboardingNotFoundFragment$YdjqsmYvRd5Q6LnZKp5BR0sLBSw
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyOnboardingNotFoundFragment.lambda$onViewCreated$0(TreadlyOnboardingNotFoundFragment.this, view2);
            }
        });
        Button button = (Button) view.findViewById(R.id.onboarding_no_device_found_invite_code_button);
        button.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.Onboarding.NotFound.-$$Lambda$TreadlyOnboardingNotFoundFragment$D0ptF0iM5TRSJSLHvIT6JcCaWWk
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyOnboardingNotFoundFragment.lambda$onViewCreated$1(TreadlyOnboardingNotFoundFragment.this, view2);
            }
        });
        button.setVisibility(this.showInviteButton ? 0 : 8);
        ((TextView) view.findViewById(R.id.onboarding_no_device_found_item_3)).setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static /* synthetic */ void lambda$onViewCreated$0(TreadlyOnboardingNotFoundFragment treadlyOnboardingNotFoundFragment, View view) {
        treadlyOnboardingNotFoundFragment.popBackStack();
        if (treadlyOnboardingNotFoundFragment.tryAgainListener != null) {
            treadlyOnboardingNotFoundFragment.tryAgainListener.onTryAgainPressed();
        }
    }

    public static /* synthetic */ void lambda$onViewCreated$1(TreadlyOnboardingNotFoundFragment treadlyOnboardingNotFoundFragment, View view) {
        treadlyOnboardingNotFoundFragment.popBackStack();
        if (treadlyOnboardingNotFoundFragment.inviteCodeListener != null) {
            treadlyOnboardingNotFoundFragment.inviteCodeListener.onInviteCodePressed();
        }
    }
}
