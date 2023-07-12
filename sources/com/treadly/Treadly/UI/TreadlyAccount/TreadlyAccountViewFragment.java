package com.treadly.Treadly.UI.TreadlyAccount;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.treadly.Treadly.R;

/* loaded from: classes2.dex */
public class TreadlyAccountViewFragment extends Fragment {
    public static final String TAG = "TREADLY_ACCOUNT_VIEW";

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_splash, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        ((Button) view.findViewById(R.id.create_account_button)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyAccount.-$$Lambda$TreadlyAccountViewFragment$STv4XpJfNMGUQ-t3X7f6qPkWL8U
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyAccountViewFragment.lambda$onViewCreated$0(TreadlyAccountViewFragment.this, view2);
            }
        });
        ((TextView) view.findViewById(R.id.splash_link)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyAccount.-$$Lambda$TreadlyAccountViewFragment$ZeKMJSIxCD1W6mVjTQpGpplNeS4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyAccountViewFragment.lambda$onViewCreated$1(TreadlyAccountViewFragment.this, view2);
            }
        });
    }

    public static /* synthetic */ void lambda$onViewCreated$0(TreadlyAccountViewFragment treadlyAccountViewFragment, View view) {
        TreadlyAccountSignUpFragment treadlyAccountSignUpFragment = new TreadlyAccountSignUpFragment();
        if (treadlyAccountViewFragment.getActivity() != null) {
            treadlyAccountViewFragment.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.activity_fragment_container_empty, treadlyAccountSignUpFragment, TreadlyAccountSignUpFragment.TAG).commit();
        }
    }

    public static /* synthetic */ void lambda$onViewCreated$1(TreadlyAccountViewFragment treadlyAccountViewFragment, View view) {
        TreadlyAccountSignInFragment treadlyAccountSignInFragment = new TreadlyAccountSignInFragment();
        if (treadlyAccountViewFragment.getActivity() != null) {
            treadlyAccountViewFragment.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.activity_fragment_container_empty, treadlyAccountSignInFragment, TreadlyAccountSignInFragment.TAG).commit();
        }
    }
}
