package com.treadly.Treadly.UI.TreadlyAccount;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.R;

/* loaded from: classes2.dex */
public class TreadlyAccountResetPasswordFragment extends Fragment {
    public static final String TAG = "TREADLY_ACCOUNT_RESET_PASSWORD";
    private ImageView backArrowButton;
    private AppCompatEditText emailTextField;
    private boolean isResetting = false;
    private TextView loginHeaderButton;
    private Button resetPasswordButton;
    private TextView titleLabel;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$alertDialog$3(DialogInterface dialogInterface, int i) {
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_account_reset_password, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.titleLabel = (TextView) view.findViewById(R.id.header_title_v2);
        this.titleLabel.setText("Reset Password");
        this.emailTextField = (AppCompatEditText) view.findViewById(R.id.new_password_text_change);
        this.backArrowButton = (ImageView) view.findViewById(R.id.back_arrow_v2);
        this.backArrowButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyAccount.-$$Lambda$TreadlyAccountResetPasswordFragment$Sxo6e5bWLGRjybKVOJy8CbYXuWA
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyAccountResetPasswordFragment.lambda$onViewCreated$0(TreadlyAccountResetPasswordFragment.this, view2);
            }
        });
        this.resetPasswordButton = (Button) view.findViewById(R.id.reset_password_button);
        this.resetPasswordButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyAccount.-$$Lambda$TreadlyAccountResetPasswordFragment$yQYJsbORfF1Y-_ue6nvyHj-TS4c
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyAccountResetPasswordFragment.this.onResetPasswordButtonPressed();
            }
        });
        this.loginHeaderButton = (TextView) view.findViewById(R.id.reset_header_login);
        this.loginHeaderButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyAccount.-$$Lambda$TreadlyAccountResetPasswordFragment$ppdHbtMDrWSxVlqDTMaJ6rjSlaE
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyAccountResetPasswordFragment.this.onSignInButtonPressed();
            }
        });
    }

    public static /* synthetic */ void lambda$onViewCreated$0(TreadlyAccountResetPasswordFragment treadlyAccountResetPasswordFragment, View view) {
        FragmentActivity activity = treadlyAccountResetPasswordFragment.getActivity();
        if (activity != null) {
            activity.getSupportFragmentManager().popBackStack();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSignInButtonPressed() {
        TreadlyAccountSignInFragment treadlyAccountSignInFragment = new TreadlyAccountSignInFragment();
        treadlyAccountSignInFragment.hideSignUpButton = true;
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.activity_fragment_container_empty, treadlyAccountSignInFragment, TreadlyAccountSignInFragment.TAG).commit();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onResetPasswordButtonPressed() {
        final String obj = this.emailTextField.getText().toString();
        if (obj.length() == 0) {
            alertDialog("Reset Password Error", "Email is not valid");
        } else if (this.isResetting) {
        } else {
            this.isResetting = true;
            TreadlyServiceManager.getInstance().resetPassword(obj, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyAccount.TreadlyAccountResetPasswordFragment.1
                @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                public void onSuccess(String str) {
                    TreadlyAccountResetPasswordFragment.this.isResetting = false;
                    if (str != null) {
                        TreadlyAccountResetPasswordFragment.this.alertDialog("Reset Password Error", str);
                        return;
                    }
                    TreadlyAccountResetPasswordFragment treadlyAccountResetPasswordFragment = TreadlyAccountResetPasswordFragment.this;
                    treadlyAccountResetPasswordFragment.alertDialog("Reset Password Successful", "A reset password link has been sent to " + obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void alertDialog(String str, String str2) {
        if (getContext() == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(str2);
        builder.setTitle(str);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyAccount.-$$Lambda$TreadlyAccountResetPasswordFragment$5_NhGKC33Sg9j70RPIuURamaoq4
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                TreadlyAccountResetPasswordFragment.lambda$alertDialog$3(dialogInterface, i);
            }
        });
        builder.create().show();
    }
}
