package com.treadly.Treadly.UI.TreadlyProfile.Settings.PairedPhones;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.Util.ActivityUtil;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
import com.treadly.client.lib.sdk.Listeners.RequestEventAdapter;
import com.treadly.client.lib.sdk.Listeners.RequestEventListener;
import com.treadly.client.lib.sdk.Model.DeviceStatus;
import com.treadly.client.lib.sdk.TreadlyClientLib;

/* loaded from: classes2.dex */
public class TreadlyProfileSettingsPairedPhonesFragment extends BaseFragment {
    ImageButton backArrow;
    TextView numberOfPhones;
    TextView removePairedPhonesText;
    TextView removePhonesHeader;
    RequestEventListener requestAdapter = new RequestEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.PairedPhones.TreadlyProfileSettingsPairedPhonesFragment.3
        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventAdapter, com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestStatusResponse(boolean z, final DeviceStatus deviceStatus) {
            if (!z || deviceStatus == null) {
                return;
            }
            ActivityUtil.runOnUiThread(TreadlyProfileSettingsPairedPhonesFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.PairedPhones.TreadlyProfileSettingsPairedPhonesFragment.3.1
                @Override // java.lang.Runnable
                public void run() {
                    if (deviceStatus.getPairedPhonesCounter() > 0) {
                        TreadlyProfileSettingsPairedPhonesFragment.this.numberOfPhones.setText(String.format("%d", Integer.valueOf(deviceStatus.getPairedPhonesCounter() - 1)));
                    } else {
                        TreadlyProfileSettingsPairedPhonesFragment.this.numberOfPhones.setText(String.valueOf(0));
                    }
                }
            });
        }
    };

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_profile_settings_paired_phones, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        this.removePhonesHeader = (TextView) view.findViewById(R.id.nav_title);
        this.removePhonesHeader.setText("Paired Phones");
        this.backArrow = (ImageButton) view.findViewById(R.id.nav_back_arrow);
        this.backArrow.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.PairedPhones.-$$Lambda$TreadlyProfileSettingsPairedPhonesFragment$BmoDxFitDu_lce0UcJOH3JgEYHM
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileSettingsPairedPhonesFragment.this.popBackStack();
            }
        });
        this.numberOfPhones = (TextView) view.findViewById(R.id.number_phones);
        this.numberOfPhones.setText(String.valueOf(0));
        this.removePairedPhonesText = (TextView) view.findViewById(R.id.remove_paired_phones_text);
        this.removePairedPhonesText.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.PairedPhones.-$$Lambda$TreadlyProfileSettingsPairedPhonesFragment$Tf1JSwDgjDvk9rSZqMxXLfx5Ei4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileSettingsPairedPhonesFragment.this.showDeletePairedPhonesAlert();
            }
        });
        TreadlyClientLib.shared.addRequestEventListener(this.requestAdapter);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        TreadlyClientLib.shared.removeRequestEventListener(this.requestAdapter);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showDeletePairedPhonesAlert() {
        final AlertDialog.Builder negativeButton = new AlertDialog.Builder(getContext()).setTitle("Remove Paired Phones").setMessage("Are you sure you want to remove all other paired phones?").setPositiveButton("Yes", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.PairedPhones.TreadlyProfileSettingsPairedPhonesFragment.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                TreadlyClientLib.shared.setDeletePairList(false);
            }
        }).setNegativeButton("No", (DialogInterface.OnClickListener) null);
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.PairedPhones.TreadlyProfileSettingsPairedPhonesFragment.2
            @Override // java.lang.Runnable
            public void run() {
                negativeButton.show();
            }
        });
    }
}
