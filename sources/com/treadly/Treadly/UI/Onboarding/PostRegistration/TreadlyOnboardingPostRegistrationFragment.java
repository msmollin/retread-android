package com.treadly.Treadly.UI.Onboarding.PostRegistration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.Onboarding.Searching.TreadlyOnboardingSearchingFragment;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
import com.treadly.client.lib.sdk.TreadlyClientLib;

/* loaded from: classes2.dex */
public class TreadlyOnboardingPostRegistrationFragment extends BaseFragment {
    public static final String TAG = "ONBOARDING_POST_REGISTRATION";

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_onboarding_post_registration, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        TextView textView = (TextView) view.findViewById(R.id.onboarding_postregistration_lets_get_you_treading_label);
        TextView textView2 = (TextView) view.findViewById(R.id.onboarding_postregistration_plug_your_label);
        ((Button) view.findViewById(R.id.onboarding_postregistration_cancel_button)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.Onboarding.PostRegistration.-$$Lambda$TreadlyOnboardingPostRegistrationFragment$I_IJgXNACjwBvstdMkMeF8GtZ8w
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyOnboardingPostRegistrationFragment.this.logout(view2);
            }
        });
        ((Button) view.findViewById(R.id.onboarding_postregistration_start_setup_button)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.Onboarding.PostRegistration.-$$Lambda$TreadlyOnboardingPostRegistrationFragment$i9k-86K32U4V8oY4RnhZyfmc6Ik
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyOnboardingPostRegistrationFragment.this.toSearching(view2);
            }
        });
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        addBackStackCallback();
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment
    public void onFragmentReturning() {
        super.onFragmentReturning();
        TreadlyClientLib.shared.disconnect();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toSearching(@Nullable View view) {
        addFragmentToStack(new TreadlyOnboardingSearchingFragment(), TreadlyOnboardingSearchingFragment.TAG, TAG, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logout(@Nullable View view) {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.logout();
        }
    }
}
