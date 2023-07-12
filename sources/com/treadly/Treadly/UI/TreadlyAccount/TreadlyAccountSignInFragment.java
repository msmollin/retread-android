package com.treadly.Treadly.UI.TreadlyAccount;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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
import com.treadly.Treadly.UI.Util.SharedPreferences;
import java.io.PrintStream;
import java.util.Arrays;

/* loaded from: classes2.dex */
public class TreadlyAccountSignInFragment extends Fragment {
    public static final String TAG = "TREADLY_ACCOUNT_SIGNIN";
    private ImageView backArrowButton;
    private AppCompatEditText emailTextField;
    private TextView forgotPasswordButton;
    private AppCompatEditText passwordTextField;
    private Button signInButton;
    private TextView signUpHeaderButton;
    private TextView titleLabel;
    protected boolean hideSignUpButton = false;
    private boolean signingIn = false;
    private boolean viewsSetup = false;
    private CallbackManager callbackManager = CallbackManager.Factory.create();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$alertDialog$4(DialogInterface dialogInterface, int i) {
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_account_signin, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.titleLabel = (TextView) view.findViewById(R.id.header_title_v2);
        this.titleLabel.setText("Login");
        this.emailTextField = (AppCompatEditText) view.findViewById(R.id.email_text_input);
        this.passwordTextField = (AppCompatEditText) view.findViewById(R.id.password_text_input);
        Typeface typeface = this.passwordTextField.getTypeface();
        this.passwordTextField.setInputType(GmsClientSupervisor.DEFAULT_BIND_FLAGS);
        this.passwordTextField.setTypeface(typeface);
        this.signInButton = (Button) view.findViewById(R.id.sign_in_button);
        this.signInButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyAccount.-$$Lambda$TreadlyAccountSignInFragment$zIXwQuJH3n4fkdUhFbiSrsI8c6Y
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyAccountSignInFragment.this.onSignInButtonPressed();
            }
        });
        this.signUpHeaderButton = (TextView) view.findViewById(R.id.reset_title_sign_up);
        initNavigation(view);
        this.forgotPasswordButton = (TextView) view.findViewById(R.id.forgot_link);
        this.forgotPasswordButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyAccount.-$$Lambda$TreadlyAccountSignInFragment$2ftg1xaXTGuZKqG3xGDktfE1suk
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyAccountSignInFragment.this.onForgotPasswordPressed();
            }
        });
        setUpFacebookSignIn();
    }

    private void initNavigation(View view) {
        this.backArrowButton = (ImageView) view.findViewById(R.id.back_arrow_v2);
        this.backArrowButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyAccount.-$$Lambda$TreadlyAccountSignInFragment$CLJV4Srv2j6_hxK_l4mWNqduA04
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyAccountSignInFragment.lambda$initNavigation$2(TreadlyAccountSignInFragment.this, view2);
            }
        });
        if (this.hideSignUpButton) {
            this.signUpHeaderButton.setVisibility(8);
        } else {
            this.signUpHeaderButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyAccount.-$$Lambda$TreadlyAccountSignInFragment$55WQxlbLN_ZRyNR_9tOyYlasVdA
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    TreadlyAccountSignInFragment.this.onSignUpButtonPressed();
                }
            });
        }
    }

    public static /* synthetic */ void lambda$initNavigation$2(TreadlyAccountSignInFragment treadlyAccountSignInFragment, View view) {
        FragmentActivity activity = treadlyAccountSignInFragment.getActivity();
        if (activity != null) {
            activity.getSupportFragmentManager().popBackStack();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSignUpButtonPressed() {
        TreadlyAccountSignUpFragment treadlyAccountSignUpFragment = new TreadlyAccountSignUpFragment();
        treadlyAccountSignUpFragment.hideSignInButton = true;
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.activity_fragment_container_empty, treadlyAccountSignUpFragment, TreadlyAccountSignUpFragment.TAG).commit();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onForgotPasswordPressed() {
        TreadlyAccountResetPasswordFragment treadlyAccountResetPasswordFragment = new TreadlyAccountResetPasswordFragment();
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.activity_fragment_container_empty, treadlyAccountResetPasswordFragment, TreadlyAccountResetPasswordFragment.TAG).commit();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSignInButtonPressed() {
        if (this.signingIn) {
            return;
        }
        hideKeyboard();
        final String trim = this.emailTextField.getText().toString().trim();
        final String obj = this.passwordTextField.getText().toString();
        if (trim.length() == 0) {
            alertDialog("Sign In Error", "Email or Password is invalid.");
        } else if (obj.length() == 0) {
            alertDialog("Sign In Error", "Email or Password is invalid.");
        } else {
            this.signingIn = true;
            TreadlyServiceManager.getInstance().loginWithEmail(trim, obj, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyAccount.TreadlyAccountSignInFragment.1
                @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                public void onUserInfoProfile(String str, UserInfo userInfo, boolean z, boolean z2) {
                    TreadlyAccountSignInFragment.this.signingIn = false;
                    if (str != null || userInfo == null) {
                        if (str != null) {
                            TreadlyAccountSignInFragment.this.alertDialog("Sign In Error", str);
                            return;
                        }
                        return;
                    }
                    SharedPreferences.shared.storeUserEmail(trim);
                    SharedPreferences.shared.storeUserPassword(obj);
                    if (!z) {
                        if (TreadlyAccountSignInFragment.this.getActivity() != null) {
                            TreadlyAccountChangeUsernameFragment treadlyAccountChangeUsernameFragment = new TreadlyAccountChangeUsernameFragment();
                            treadlyAccountChangeUsernameFragment.userInfo = userInfo;
                            TreadlyAccountSignInFragment.this.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.activity_fragment_container_empty, treadlyAccountChangeUsernameFragment, TreadlyAccountChangeUsernameFragment.TAG).commit();
                            return;
                        }
                        return;
                    }
                    LoginActivity loginActivity = (LoginActivity) TreadlyAccountSignInFragment.this.getActivity();
                    if (loginActivity != null) {
                        loginActivity.toMainActivity();
                    }
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
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyAccount.-$$Lambda$TreadlyAccountSignInFragment$bNHT9-qM-FSIYP1PhSPxGg0S8-8
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                TreadlyAccountSignInFragment.lambda$alertDialog$4(dialogInterface, i);
            }
        });
        builder.create().show();
    }

    private void setUpFacebookSignIn() {
        final LoginButton loginButton = (LoginButton) getView().findViewById(R.id.login_button_fb);
        ((ImageButton) getView().findViewById(R.id.facebook_sign_up_button)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyAccount.-$$Lambda$TreadlyAccountSignInFragment$Uw3UllLXD_E-ZzhJpoYcGv2lMtg
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LoginButton.this.performClick();
            }
        });
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        loginButton.setFragment(this);
        LoginManager.getInstance().registerCallback(this.callbackManager, new FacebookCallback<LoginResult>() { // from class: com.treadly.Treadly.UI.TreadlyAccount.TreadlyAccountSignInFragment.2
            @Override // com.facebook.FacebookCallback
            public void onCancel() {
            }

            @Override // com.facebook.FacebookCallback
            public void onSuccess(final LoginResult loginResult) {
                TreadlyServiceManager.getInstance().authenticateWithFacebook(loginResult.getAccessToken().getToken(), new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyAccount.TreadlyAccountSignInFragment.2.1
                    @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                    public void onUserInfoProfile(String str, UserInfo userInfo, boolean z, boolean z2) {
                        TreadlyAccountSignInFragment.this.signingIn = false;
                        if (str != null || userInfo == null) {
                            TreadlyAccountSignInFragment.this.alertDialog("Sign In Error", "Email or password is invalid.");
                            return;
                        }
                        String token = loginResult.getAccessToken().getToken();
                        String str2 = userInfo.id;
                        SharedPreferences.shared.storeFacebookToken(token);
                        SharedPreferences.shared.storeFacebookUserId(str2);
                        if (!z) {
                            if (TreadlyAccountSignInFragment.this.getActivity() != null) {
                                TreadlyAccountChangeUsernameFragment treadlyAccountChangeUsernameFragment = new TreadlyAccountChangeUsernameFragment();
                                treadlyAccountChangeUsernameFragment.userInfo = userInfo;
                                TreadlyAccountSignInFragment.this.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.activity_fragment_container_empty, treadlyAccountChangeUsernameFragment, TreadlyAccountChangeUsernameFragment.TAG).commit();
                                return;
                            }
                            return;
                        }
                        LoginActivity loginActivity = (LoginActivity) TreadlyAccountSignInFragment.this.getActivity();
                        if (loginActivity != null) {
                            loginActivity.toMainActivity();
                        }
                    }
                });
            }

            @Override // com.facebook.FacebookCallback
            public void onError(FacebookException facebookException) {
                PrintStream printStream = System.out;
                printStream.println("TreadlyAccountSignInFragment: " + toString());
                TreadlyAccountSignInFragment.this.alertDialog("Error", "Error logging into Facebook");
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
