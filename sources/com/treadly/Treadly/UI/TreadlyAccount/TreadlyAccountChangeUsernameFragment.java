package com.treadly.Treadly.UI.TreadlyAccount;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileEditFragment;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;

/* loaded from: classes2.dex */
public class TreadlyAccountChangeUsernameFragment extends BaseFragment {
    public static final String TAG = "TREADLY_ACCOUNT_CHANGE_USERNAME";
    public UserInfo userInfo;
    private AppCompatEditText usernameTextField;
    private boolean settingUsername = false;
    public boolean fromSettings = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$alertDialog$3(DialogInterface dialogInterface, int i) {
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_account_change_username, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        TextView textView = (TextView) view.findViewById(R.id.nav_title);
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.nav_back_arrow);
        TextView textView2 = (TextView) view.findViewById(R.id.header_title_v2);
        ImageView imageView = (ImageView) view.findViewById(R.id.back_arrow_v2);
        if (this.fromSettings) {
            textView.setText("Change Username");
            imageButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyAccount.-$$Lambda$TreadlyAccountChangeUsernameFragment$JeqBlVdqUUsWekIMTmjYIKYOkWY
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    TreadlyAccountChangeUsernameFragment.this.popBackStack();
                }
            });
            imageView.setVisibility(8);
            textView2.setVisibility(4);
        } else {
            textView2.setText("Set Username");
            imageView.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyAccount.-$$Lambda$TreadlyAccountChangeUsernameFragment$_RRm7ykOInPEVPNi48UUv3QJWfE
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    TreadlyAccountChangeUsernameFragment.this.popBackStack();
                }
            });
            imageButton.setVisibility(8);
            textView.setVisibility(4);
        }
        this.usernameTextField = (AppCompatEditText) view.findViewById(R.id.username_text_input_facebook);
        ((ImageButton) view.findViewById(R.id.change_username_button)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyAccount.-$$Lambda$TreadlyAccountChangeUsernameFragment$9UIUntOMnf3I8qGu4g043YHDZow
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyAccountChangeUsernameFragment.this.onSaveButtonPressed();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideKeyboard(View view, Context context) {
        ((InputMethodManager) context.getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSaveButtonPressed() {
        if (this.settingUsername) {
            return;
        }
        final String obj = this.usernameTextField.getText().toString();
        if (obj.length() == 0) {
            alertDialog("Set Username Error", "Username is empty");
            return;
        }
        this.settingUsername = true;
        TreadlyServiceManager.getInstance().changeUsername(obj, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyAccount.TreadlyAccountChangeUsernameFragment.1
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onSuccess(String str) {
                if (TreadlyAccountChangeUsernameFragment.this.getView() != null && TreadlyAccountChangeUsernameFragment.this.getContext() != null) {
                    TreadlyAccountChangeUsernameFragment.this.hideKeyboard(TreadlyAccountChangeUsernameFragment.this.getView(), TreadlyAccountChangeUsernameFragment.this.getContext());
                }
                TreadlyAccountChangeUsernameFragment.this.settingUsername = false;
                if (str != null) {
                    TreadlyAccountChangeUsernameFragment.this.alertDialog("Set Username Error", str);
                    return;
                }
                TreadlyAccountChangeUsernameFragment.this.userInfo.name = obj;
                if (TreadlyAccountChangeUsernameFragment.this.fromSettings) {
                    TreadlyAccountChangeUsernameFragment.this.popBackStack();
                    return;
                }
                TreadlyProfileEditFragment treadlyProfileEditFragment = new TreadlyProfileEditFragment();
                treadlyProfileEditFragment.hideNaviagtionBar = true;
                treadlyProfileEditFragment.useDefaultProfile = true;
                treadlyProfileEditFragment.fromLogin = true;
                if (TreadlyAccountChangeUsernameFragment.this.getActivity() != null) {
                    TreadlyAccountChangeUsernameFragment.this.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.activity_fragment_container_empty, treadlyProfileEditFragment, TreadlyProfileEditFragment.TAG).commit();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void alertDialog(String str, String str2) {
        if (getContext() == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(str2);
        builder.setTitle(str);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyAccount.-$$Lambda$TreadlyAccountChangeUsernameFragment$iGMwFuyHb0zjKHSZZeEFiNztiVA
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                TreadlyAccountChangeUsernameFragment.lambda$alertDialog$3(dialogInterface, i);
            }
        });
        builder.create().show();
    }
}
