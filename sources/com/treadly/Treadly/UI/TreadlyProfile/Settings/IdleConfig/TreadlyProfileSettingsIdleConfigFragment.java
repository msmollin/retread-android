package com.treadly.Treadly.UI.TreadlyProfile.Settings.IdleConfig;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import com.facebook.appevents.AppEventsConstants;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.Util.ActivityUtil;
import com.treadly.Treadly.UI.Util.InputFilterMinMax;
import com.treadly.client.lib.sdk.TreadlyClientLib;

/* loaded from: classes2.dex */
public class TreadlyProfileSettingsIdleConfigFragment extends Fragment {
    ImageView backArrow;
    Context context;
    SwitchCompat enableIdlePauseSwitch;
    AppCompatEditText idleConfigTextField;
    TextView idleConfigTitle;
    public boolean isIdlePauseEnabled;
    public TreadlyProfileSettingsIdleConfigFragmentListener listener;
    public byte timeout = 0;
    ImageButton updateIdleConfigButton;

    /* loaded from: classes2.dex */
    public interface TreadlyProfileSettingsIdleConfigFragmentListener {
        void didUpdateIdlePauseConfig(boolean z, byte b);
    }

    void setIdleEnabled() {
        if (this.enableIdlePauseSwitch == null) {
            return;
        }
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.IdleConfig.TreadlyProfileSettingsIdleConfigFragment.1
            @Override // java.lang.Runnable
            public void run() {
                TreadlyProfileSettingsIdleConfigFragment.this.enableIdlePauseSwitch.setChecked(TreadlyProfileSettingsIdleConfigFragment.this.isIdlePauseEnabled);
                if (TreadlyProfileSettingsIdleConfigFragment.this.isIdlePauseEnabled) {
                    TreadlyProfileSettingsIdleConfigFragment.this.enableIdlePauseSwitch.setThumbResource(R.drawable.idle_thumb_enabled_selector);
                } else {
                    TreadlyProfileSettingsIdleConfigFragment.this.enableIdlePauseSwitch.setThumbResource(R.drawable.idle_thumb_selector);
                }
            }
        });
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_profile_settings_idle_config, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        this.context = getContext();
        this.idleConfigTitle = (TextView) view.findViewById(R.id.header_title);
        this.idleConfigTitle.setText("Idle Pause Settings");
        this.enableIdlePauseSwitch = (SwitchCompat) view.findViewById(R.id.idle_enable_switch);
        this.enableIdlePauseSwitch.setChecked(this.isIdlePauseEnabled);
        if (this.isIdlePauseEnabled) {
            this.enableIdlePauseSwitch.setThumbResource(R.drawable.idle_thumb_enabled_selector);
        } else {
            this.enableIdlePauseSwitch.setThumbResource(R.drawable.idle_thumb_selector);
        }
        this.enableIdlePauseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.IdleConfig.TreadlyProfileSettingsIdleConfigFragment.2
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                TreadlyProfileSettingsIdleConfigFragment.this.enableSwitched();
            }
        });
        this.idleConfigTextField = (AppCompatEditText) view.findViewById(R.id.idle_pause_time_edit_text);
        this.idleConfigTextField.setFilters(new InputFilter[]{new InputFilterMinMax(AppEventsConstants.EVENT_PARAM_VALUE_NO, "255")});
        this.idleConfigTextField.setText(String.valueOf((int) this.timeout));
        this.updateIdleConfigButton = (ImageButton) view.findViewById(R.id.idle_config_update_button);
        this.updateIdleConfigButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.IdleConfig.TreadlyProfileSettingsIdleConfigFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                TreadlyProfileSettingsIdleConfigFragment.this.updateIdlePauseConfig();
            }
        });
        this.backArrow = (ImageView) view.findViewById(R.id.back_arrow);
        this.backArrow.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.IdleConfig.TreadlyProfileSettingsIdleConfigFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (TreadlyProfileSettingsIdleConfigFragment.this.getActivity() != null) {
                    TreadlyProfileSettingsIdleConfigFragment.this.getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });
    }

    void updateIdlePauseConfig() {
        if (this.idleConfigTextField.getText().toString().isEmpty()) {
            showAlert("Error", "Could not set duration");
            return;
        }
        byte parseInt = (byte) Integer.parseInt(this.idleConfigTextField.getText().toString());
        if (TreadlyClientLib.shared.setIdlePauseConfig(this.isIdlePauseEnabled, parseInt)) {
            this.listener.didUpdateIdlePauseConfig(this.isIdlePauseEnabled, parseInt);
            hideKeyboard(this.idleConfigTextField, this.context);
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            } else {
                showAlert("Error", "Error setting idle pause config");
            }
        }
    }

    void enableSwitched() {
        this.isIdlePauseEnabled = this.enableIdlePauseSwitch.isChecked();
        setIdleEnabled();
    }

    void showAlert(String str, String str2) {
        final AlertDialog.Builder neutralButton = new AlertDialog.Builder(getContext()).setTitle(str).setMessage(str2).setNeutralButton("Dismiss", (DialogInterface.OnClickListener) null);
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.IdleConfig.TreadlyProfileSettingsIdleConfigFragment.5
            @Override // java.lang.Runnable
            public void run() {
                neutralButton.show();
            }
        });
    }

    private void hideKeyboard(View view, Context context) {
        ((InputMethodManager) context.getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
