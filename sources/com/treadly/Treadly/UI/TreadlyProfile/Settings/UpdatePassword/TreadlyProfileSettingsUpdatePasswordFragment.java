package com.treadly.Treadly.UI.TreadlyProfile.Settings.UpdatePassword;

import android.content.Context;
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
import androidx.appcompat.widget.AppCompatEditText;
import com.google.android.gms.common.internal.GmsClientSupervisor;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
import com.treadly.Treadly.UI.Util.SharedPreferences;

/* loaded from: classes2.dex */
public class TreadlyProfileSettingsUpdatePasswordFragment extends BaseFragment {
    Context context;
    boolean isChangingPassword = false;
    private AppCompatEditText newPasswordTextField;

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_profile_settings_update_password, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        this.context = getContext();
        ((TextView) view.findViewById(R.id.nav_title)).setText("Change Password");
        this.newPasswordTextField = (AppCompatEditText) view.findViewById(R.id.new_password_text_change);
        this.newPasswordTextField.setInputType(GmsClientSupervisor.DEFAULT_BIND_FLAGS);
        ((ImageView) view.findViewById(R.id.nav_back_arrow)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.UpdatePassword.-$$Lambda$TreadlyProfileSettingsUpdatePasswordFragment$3DSWjXupK3aMiWWwzcOp65-FCCs
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileSettingsUpdatePasswordFragment.this.popBackStack();
            }
        });
        ((ImageButton) view.findViewById(R.id.reset_password_button)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.UpdatePassword.-$$Lambda$TreadlyProfileSettingsUpdatePasswordFragment$KfClphG1eY3QYxhSiJXiNCehejw
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileSettingsUpdatePasswordFragment.this.onUpdatePressed();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onUpdatePressed() {
        if (this.newPasswordTextField.getText() == null) {
            return;
        }
        final String obj = this.newPasswordTextField.getText().toString();
        if (obj.isEmpty()) {
            showBaseAlert("Error", "Password is empty");
            return;
        }
        this.isChangingPassword = true;
        TreadlyServiceManager.getInstance().changePassword(obj, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.UpdatePassword.TreadlyProfileSettingsUpdatePasswordFragment.1
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onSuccess(String str) {
                TreadlyProfileSettingsUpdatePasswordFragment.this.hideKeyboard(TreadlyProfileSettingsUpdatePasswordFragment.this.newPasswordTextField, TreadlyProfileSettingsUpdatePasswordFragment.this.context);
                TreadlyProfileSettingsUpdatePasswordFragment.this.isChangingPassword = false;
                if (str == null) {
                    SharedPreferences.shared.storeUserPassword(obj);
                    TreadlyProfileSettingsUpdatePasswordFragment.this.popBackStack();
                    return;
                }
                TreadlyProfileSettingsUpdatePasswordFragment.this.showBaseAlert("Error Changing Password", str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideKeyboard(View view, Context context) {
        ((InputMethodManager) context.getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
