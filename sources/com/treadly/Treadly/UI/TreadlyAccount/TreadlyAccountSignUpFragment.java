package com.treadly.Treadly.UI.TreadlyAccount;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.internal.GmsClientSupervisor;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileEditFragment;
import com.treadly.Treadly.UI.Util.SharedPreferences;
import java.io.PrintStream;
import java.util.Arrays;

/* loaded from: classes2.dex */
public class TreadlyAccountSignUpFragment extends Fragment {
    public static final String TAG = "TREADLY_ACCOUNT_SIGNUP";
    private ImageView backArrowButton;
    private Button continueButton;
    private AppCompatEditText emailTextField;
    private TextView forgotPasswordButton;
    private TextView loginHeaderButton;
    private AppCompatEditText nameTextField;
    private AppCompatEditText passwordTextField;
    private TextView termsAndPrivacy;
    private TextView titleLabel;
    protected boolean hideSignInButton = false;
    private boolean isSigningUp = false;
    private boolean viewsSetup = false;
    private CallbackManager callbackManager = CallbackManager.Factory.create();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$alertDialog$4(DialogInterface dialogInterface, int i) {
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_account_signup, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.titleLabel = (TextView) view.findViewById(R.id.header_title_v2);
        this.titleLabel.setText("Register");
        this.nameTextField = (AppCompatEditText) view.findViewById(R.id.username_text_input_signup);
        this.emailTextField = (AppCompatEditText) view.findViewById(R.id.email_text_input_signup);
        this.passwordTextField = (AppCompatEditText) view.findViewById(R.id.password_text_input_signup);
        Typeface typeface = this.passwordTextField.getTypeface();
        this.passwordTextField.setInputType(GmsClientSupervisor.DEFAULT_BIND_FLAGS);
        this.passwordTextField.setTypeface(typeface);
        this.continueButton = (Button) view.findViewById(R.id.continue_button);
        this.continueButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyAccount.-$$Lambda$TreadlyAccountSignUpFragment$tZNZznngfRCoOKADJk-ywgHPasg
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyAccountSignUpFragment.this.onSignUpButtonPressed();
            }
        });
        this.loginHeaderButton = (TextView) view.findViewById(R.id.register_title_login);
        initNavigation(view);
        this.forgotPasswordButton = (TextView) view.findViewById(R.id.forgot_link_signup);
        this.forgotPasswordButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyAccount.-$$Lambda$TreadlyAccountSignUpFragment$4ulyEuzw89kwnAiuSM5ijFCecOc
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyAccountSignUpFragment.this.onForgotPasswordPressed();
            }
        });
        this.termsAndPrivacy = (TextView) view.findViewById(R.id.terms_and_privacy_text_bottom);
        this.termsAndPrivacy.setMovementMethod(LinkMovementMethod.getInstance());
        setUpFacebookSignUp();
    }

    private void initNavigation(View view) {
        this.backArrowButton = (ImageView) view.findViewById(R.id.back_arrow_v2);
        this.backArrowButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyAccount.-$$Lambda$TreadlyAccountSignUpFragment$nNOHUtPmFh0HSdPXdWl4vRQQogg
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyAccountSignUpFragment.lambda$initNavigation$2(TreadlyAccountSignUpFragment.this, view2);
            }
        });
        if (this.hideSignInButton) {
            this.loginHeaderButton.setVisibility(8);
        } else {
            this.loginHeaderButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyAccount.-$$Lambda$TreadlyAccountSignUpFragment$VDCwN3DQhig913JbG_CZSLIDzAY
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    TreadlyAccountSignUpFragment.this.onSignInButtonPressed();
                }
            });
        }
    }

    public static /* synthetic */ void lambda$initNavigation$2(TreadlyAccountSignUpFragment treadlyAccountSignUpFragment, View view) {
        FragmentActivity activity = treadlyAccountSignUpFragment.getActivity();
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
    public void onForgotPasswordPressed() {
        TreadlyAccountResetPasswordFragment treadlyAccountResetPasswordFragment = new TreadlyAccountResetPasswordFragment();
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.activity_fragment_container_empty, treadlyAccountResetPasswordFragment).commit();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSignUpButtonPressed() {
        if (this.isSigningUp) {
            return;
        }
        hideKeyboard();
        final String obj = this.nameTextField.getText().toString();
        final String obj2 = this.emailTextField.getText().toString();
        final String obj3 = this.passwordTextField.getText().toString();
        if (obj.length() == 0) {
            alertDialog("Sign Up Error", "Name, email or password is invalid.");
        } else if (obj2.length() == 0) {
            alertDialog("Sign Up Error", "Name, email or password is invalid.");
        } else if (obj3.length() == 0) {
            alertDialog("Sign Up Error", "Name, email or password is invalid.");
        } else {
            this.isSigningUp = true;
            TreadlyServiceManager.getInstance().registerWithEmail(obj, obj2, obj3, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyAccount.TreadlyAccountSignUpFragment.1
                @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                public void onUserInfoProfile(String str, UserInfo userInfo, final boolean z, boolean z2) {
                    TreadlyAccountSignUpFragment.this.isSigningUp = false;
                    if (str != null || userInfo == null) {
                        if (str != null) {
                            TreadlyAccountSignUpFragment.this.alertDialog("Sign Up Error", "Name, email or password is invalid.");
                            return;
                        }
                        return;
                    }
                    SharedPreferences.shared.storeUserEmail(obj2);
                    SharedPreferences.shared.storeUserPassword(obj3);
                    TreadlyServiceManager.getInstance().changeUsername(obj, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyAccount.TreadlyAccountSignUpFragment.1.1
                        @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                        public void onSuccess(String str2) {
                            if (!z) {
                                if (TreadlyAccountSignUpFragment.this.getActivity() != null) {
                                    TreadlyProfileEditFragment treadlyProfileEditFragment = new TreadlyProfileEditFragment();
                                    treadlyProfileEditFragment.hideNaviagtionBar = true;
                                    treadlyProfileEditFragment.useDefaultProfile = true;
                                    treadlyProfileEditFragment.fromLogin = true;
                                    if (TreadlyAccountSignUpFragment.this.getActivity() != null) {
                                        TreadlyAccountSignUpFragment.this.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.activity_fragment_container_empty, treadlyProfileEditFragment, TreadlyProfileEditFragment.TAG).commit();
                                        return;
                                    }
                                    return;
                                }
                                return;
                            }
                            LoginActivity loginActivity = (LoginActivity) TreadlyAccountSignUpFragment.this.getActivity();
                            if (loginActivity != null) {
                                loginActivity.toMainActivity();
                            }
                        }
                    });
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
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyAccount.-$$Lambda$TreadlyAccountSignUpFragment$sE9KcIrvgiJb9uv3S6MjT-uUUAM
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                TreadlyAccountSignUpFragment.lambda$alertDialog$4(dialogInterface, i);
            }
        });
        builder.create().show();
    }

    private void setUpFacebookSignUp() {
        final LoginButton loginButton = (LoginButton) getView().findViewById(R.id.login_button_fb);
        ((ImageButton) getView().findViewById(R.id.facebook_sign_up_button)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyAccount.-$$Lambda$TreadlyAccountSignUpFragment$EO8CnaXam0ayfUS9PPBzJIULPV0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LoginButton.this.performClick();
            }
        });
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        loginButton.setFragment(this);
        LoginManager.getInstance().registerCallback(this.callbackManager, new FacebookCallback<LoginResult>() { // from class: com.treadly.Treadly.UI.TreadlyAccount.TreadlyAccountSignUpFragment.2
            @Override // com.facebook.FacebookCallback
            public void onCancel() {
            }

            @Override // com.facebook.FacebookCallback
            public void onSuccess(final LoginResult loginResult) {
                System.out.println("WG::: FACEBOOK SUCCESS");
                TreadlyServiceManager.getInstance().authenticateWithFacebook(loginResult.getAccessToken().getToken(), new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyAccount.TreadlyAccountSignUpFragment.2.1
                    @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                    public void onUserInfoProfile(String str, UserInfo userInfo, boolean z, boolean z2) {
                        TreadlyAccountSignUpFragment.this.isSigningUp = false;
                        if (str != null || userInfo == null) {
                            TreadlyAccountSignUpFragment.this.alertDialog("Sign Up Error", "Name, email or password is invalid.");
                            return;
                        }
                        String token = loginResult.getAccessToken().getToken();
                        String str2 = userInfo.id;
                        SharedPreferences.shared.storeFacebookToken(token);
                        SharedPreferences.shared.storeFacebookUserId(str2);
                        if (!z) {
                            if (TreadlyAccountSignUpFragment.this.getActivity() != null) {
                                TreadlyAccountChangeUsernameFragment treadlyAccountChangeUsernameFragment = new TreadlyAccountChangeUsernameFragment();
                                treadlyAccountChangeUsernameFragment.userInfo = userInfo;
                                TreadlyAccountSignUpFragment.this.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.activity_fragment_container_empty, treadlyAccountChangeUsernameFragment, TreadlyAccountChangeUsernameFragment.TAG).commit();
                                return;
                            }
                            return;
                        }
                        LoginActivity loginActivity = (LoginActivity) TreadlyAccountSignUpFragment.this.getActivity();
                        if (loginActivity != null) {
                            loginActivity.toMainActivity();
                        }
                    }
                });
            }

            @Override // com.facebook.FacebookCallback
            public void onError(FacebookException facebookException) {
                PrintStream printStream = System.out;
                printStream.println("TreadlyAccountSignUpFragment: " + toString());
                TreadlyAccountSignUpFragment.this.alertDialog("Error", "Error logging into Facebook");
            }
        });
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        this.callbackManager.onActivityResult(i, i2, intent);
    }

    private void hideKeyboard() {
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
