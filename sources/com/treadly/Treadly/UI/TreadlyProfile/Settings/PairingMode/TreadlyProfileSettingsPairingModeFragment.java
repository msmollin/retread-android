package com.treadly.Treadly.UI.TreadlyProfile.Settings.PairingMode;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
import com.treadly.client.lib.sdk.Model.PairingModeTriggerType;

/* loaded from: classes2.dex */
public class TreadlyProfileSettingsPairingModeFragment extends BaseFragment {
    Switch beltPairingModeSwitch;
    public TreadlyProfileSettingsPairingModeFragmentListener listener;
    public PairingModeTriggerType pairingModeType;

    /* loaded from: classes2.dex */
    public interface TreadlyProfileSettingsPairingModeFragmentListener {
        void didSelectPairingModeType(PairingModeTriggerType pairingModeTriggerType);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_profile_settings_pairing_mode, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        ((TextView) view.findViewById(R.id.nav_title)).setText(R.string.pairing_mode_title);
        this.beltPairingModeSwitch = (Switch) view.findViewById(R.id.belt_pairing_mode_switch);
        this.beltPairingModeSwitch.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.PairingMode.-$$Lambda$TreadlyProfileSettingsPairingModeFragment$eiV8jdyMOWseeP-VuC1a981fzGY
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileSettingsPairingModeFragment.this.handleSwitchToggled();
            }
        });
        ((ImageButton) view.findViewById(R.id.nav_back_arrow)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.PairingMode.-$$Lambda$TreadlyProfileSettingsPairingModeFragment$9i9m6uTkUArvLYWkkoI68qb0Mpc
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileSettingsPairingModeFragment.this.popBackStack();
            }
        });
        updateView();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleSwitchToggled() {
        if (this.pairingModeType == null || this.listener == null) {
            return;
        }
        if (this.pairingModeType == PairingModeTriggerType.ir) {
            this.pairingModeType = PairingModeTriggerType.handrail;
        } else {
            this.pairingModeType = PairingModeTriggerType.ir;
        }
        this.listener.didSelectPairingModeType(this.pairingModeType);
        updateView();
    }

    void updateView() {
        this.beltPairingModeSwitch.setChecked(this.pairingModeType == PairingModeTriggerType.ir);
    }
}
