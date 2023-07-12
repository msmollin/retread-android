package com.treadly.Treadly.UI.TreadlyProfile.Settings.Maintenance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import com.github.mikephil.charting.utils.Utils;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.Util.ActivityUtil;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
import com.treadly.client.lib.sdk.Listeners.RequestEventAdapter;
import com.treadly.client.lib.sdk.Listeners.RequestEventListener;
import com.treadly.client.lib.sdk.Model.ComponentInfo;
import com.treadly.client.lib.sdk.Model.ComponentType;
import com.treadly.client.lib.sdk.Model.DeviceStatus;
import com.treadly.client.lib.sdk.Model.MaintenanceInfo;
import com.treadly.client.lib.sdk.Model.VersionInfo;
import com.treadly.client.lib.sdk.TreadlyClientLib;
import java.util.ArrayList;
import java.util.Locale;

/* loaded from: classes2.dex */
public class TreadlyProfileSettingsMaintenance extends BaseFragment {
    private static final int MAX_THRESHOLD = 500000;
    private static final int MAX_THRESHOLD_V1 = 250000;
    private static final int MIN_THRESHOLD = 50000;
    private static final int THRESHOLD_INCREMENT = 50000;
    ImageView backArrow;
    ComponentInfo bleComponent;
    private String[] displayValues;
    Button doneButton;
    ProgressBar maintenanceProgressBar;
    TextView maintenanceTitle;
    TextView maintenanceWarningText;
    TextView paragraph;
    int pendingStepsTreshold;
    ImageView pickerDownArrow;
    TextView progressBarTitle;
    View progressLine;
    TextView resetMaintenanceText;
    TextView steps;
    TextView stepsPerm;
    TextView stepsUnderLineLabel;
    private int[] thresholdValues;
    ConstraintLayout titleHolder;
    NumberPicker warningPicker;
    DeviceStatus deviceStatus = null;
    boolean pendingMaintenanceReset = false;
    boolean pendingTresholdUpdate = false;
    boolean displayResetSuccess = false;
    boolean isPaused = false;
    double current_speed = Utils.DOUBLE_EPSILON;
    private NumberPicker.OnValueChangeListener warningPickerChangeListener = new NumberPicker.OnValueChangeListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Maintenance.TreadlyProfileSettingsMaintenance.7
        @Override // android.widget.NumberPicker.OnValueChangeListener
        public void onValueChange(NumberPicker numberPicker, int i, int i2) {
            TreadlyProfileSettingsMaintenance.this.pendingStepsTreshold = TreadlyProfileSettingsMaintenance.this.getThresholdForNumberPickerValue(i2);
        }
    };
    RequestEventListener requestAdapter = new AnonymousClass10();

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_treadly_profile_settings_maintenance, viewGroup, false);
        TreadlyClientLib.shared.addRequestEventListener(this.requestAdapter);
        this.progressLine = inflate.findViewById(R.id.progress_steps_line);
        this.stepsUnderLineLabel = (TextView) inflate.findViewById(R.id.steps_label_under);
        this.progressBarTitle = (TextView) inflate.findViewById(R.id.maintenance_progress_title);
        this.paragraph = (TextView) inflate.findViewById(R.id.maintenance_paragraph);
        this.maintenanceTitle = (TextView) inflate.findViewById(R.id.nav_title);
        this.maintenanceTitle.setText(R.string.maintenance_title);
        this.backArrow = (ImageView) inflate.findViewById(R.id.nav_back_arrow);
        this.backArrow.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Maintenance.TreadlyProfileSettingsMaintenance.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (TreadlyProfileSettingsMaintenance.this.getActivity() != null) {
                    TreadlyProfileSettingsMaintenance.this.getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });
        this.titleHolder = (ConstraintLayout) inflate.findViewById(R.id.maintenance_picker_title_holder);
        this.titleHolder.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Maintenance.TreadlyProfileSettingsMaintenance.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                TreadlyProfileSettingsMaintenance.this.hideWarningPickerItems();
            }
        });
        warningPickerInit(inflate);
        this.doneButton = (Button) inflate.findViewById(R.id.picker_done_button);
        this.doneButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Maintenance.TreadlyProfileSettingsMaintenance.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                TreadlyProfileSettingsMaintenance.this.onSetMaintenanceWarning();
            }
        });
        this.maintenanceWarningText = (TextView) inflate.findViewById(R.id.maintenance_set_warning_threshold);
        this.maintenanceWarningText.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Maintenance.TreadlyProfileSettingsMaintenance.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                TreadlyProfileSettingsMaintenance.this.onClickMaintenanceWarning();
            }
        });
        this.stepsPerm = (TextView) inflate.findViewById(R.id.total_steps_maintenance_number);
        this.steps = (TextView) inflate.findViewById(R.id.steps_maintenance_number);
        this.resetMaintenanceText = (TextView) inflate.findViewById(R.id.maintenance_reset_warning_text);
        this.resetMaintenanceText.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Maintenance.TreadlyProfileSettingsMaintenance.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                TreadlyProfileSettingsMaintenance.this.onResetPressed();
            }
        });
        this.maintenanceProgressBar = (ProgressBar) inflate.findViewById(R.id.maintenance_progress_bar);
        LayerDrawable layerDrawable = (LayerDrawable) this.maintenanceProgressBar.getProgressDrawable();
        Drawable drawable = layerDrawable.getDrawable(1);
        Drawable drawable2 = layerDrawable.getDrawable(0);
        drawable.setTint(ContextCompat.getColor(getContext(), R.color.maintenance_progress_foreground));
        drawable2.setTint(ContextCompat.getColor(getContext(), R.color.maintenance_progress_background));
        TreadlyClientLib.shared.getDeviceComponentList();
        updateProgressLine();
        inflate.setOnTouchListener(new View.OnTouchListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Maintenance.TreadlyProfileSettingsMaintenance.6
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (TreadlyProfileSettingsMaintenance.this.pendingTresholdUpdate) {
                    TreadlyProfileSettingsMaintenance.this.hideWarningPickerItems();
                    return false;
                }
                return false;
            }
        });
        return inflate;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateProgressLine() {
        if (this.bleComponent == null) {
            return;
        }
        boolean z = !this.bleComponent.getVersionInfo().getVersionInfo().isGreaterThan(new VersionInfo(2, 0, 0));
        if (this.maintenanceProgressBar.getProgress() == 0 || this.maintenanceProgressBar.getProgress() == this.maintenanceProgressBar.getMax() || z) {
            this.progressLine.setVisibility(4);
        } else {
            this.progressLine.setVisibility(0);
        }
        int width = this.maintenanceProgressBar.getWidth();
        int progress = this.maintenanceProgressBar.getProgress();
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.progressLine.getLayoutParams();
        layoutParams.setMarginEnd((int) (width * (1.0d - (((progress * 1.0d) / this.maintenanceProgressBar.getMax()) * 1.0d))));
        this.progressLine.setLayoutParams(layoutParams);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        TreadlyClientLib.shared.removeRequestEventListener(this.requestAdapter);
    }

    private void warningPickerInit(View view) {
        this.warningPicker = (NumberPicker) view.findViewById(R.id.maintenance_warning_picker);
        this.warningPicker.setWrapSelectorWheel(false);
        this.warningPicker.setDescendantFocusability(393216);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initThresholdValues(int i) {
        int i2;
        if (this.thresholdValues == null && this.displayValues == null) {
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            int i3 = MAX_THRESHOLD;
            if (i <= MAX_THRESHOLD) {
                i3 = MAX_THRESHOLD_V1;
            }
            while (true) {
                if (i3 < 50000) {
                    break;
                }
                arrayList.add(String.format(Locale.getDefault(), "%dK STEPS", Integer.valueOf(i3 / 1000)));
                arrayList2.add(Integer.valueOf(i3));
                i3 -= 50000;
            }
            String[] strArr = new String[arrayList.size()];
            arrayList.toArray(strArr);
            this.warningPicker.setMaxValue(strArr.length > 0 ? strArr.length - 1 : 0);
            this.warningPicker.setMinValue(0);
            this.warningPicker.setDisplayedValues(strArr);
            this.warningPicker.setOnValueChangedListener(this.warningPickerChangeListener);
            this.displayValues = strArr;
            int[] iArr = new int[arrayList2.size()];
            for (i2 = 0; i2 < arrayList2.size(); i2++) {
                iArr[i2] = ((Integer) arrayList2.get(i2)).intValue();
            }
            this.thresholdValues = iArr;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onResetPressed() {
        hideWarningPickerItems();
        if (canUpdateMaintenance()) {
            pauseDevice();
            if (this.bleComponent == null) {
                return;
            }
            VersionInfo versionInfo = this.bleComponent.getVersionInfo().getVersionInfo();
            if (!versionInfo.isGreaterThan(new VersionInfo(2, 0, 0))) {
                TreadlyClientLib.shared.factoryReset();
                return;
            }
            if (requiresPause(versionInfo)) {
                this.pendingMaintenanceReset = true;
            }
            this.displayResetSuccess = true;
            TreadlyClientLib.shared.resetMaintenance();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSetMaintenanceWarning() {
        if (!canUpdateMaintenance()) {
            hideWarningPickerItems();
            return;
        }
        int value = this.warningPicker.getValue();
        if (value >= this.thresholdValues.length) {
            return;
        }
        int i = this.thresholdValues[value];
        TreadlyClientLib.shared.setMaintenanceStepThreshold(i);
        pauseDevice();
        if (hasVersion() && requiresPause(this.bleComponent.getVersionInfo().getVersionInfo())) {
            this.pendingMaintenanceReset = true;
        }
        this.displayResetSuccess = false;
        TreadlyClientLib.shared.resetMaintenance(true);
        hideWarningPickerItems();
        this.maintenanceProgressBar.setProgress(0);
        this.maintenanceProgressBar.setMin(0);
        this.maintenanceProgressBar.setMax(i);
        this.maintenanceProgressBar.setProgress(i);
        updateProgressLine();
        this.steps.setText(String.format(Locale.getDefault(), "%,d", Integer.valueOf(i)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getNumberPickerValue(int i) {
        for (int i2 = 0; i2 < this.thresholdValues.length; i2++) {
            if (this.thresholdValues[i2] == i) {
                return i2;
            }
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getThresholdForNumberPickerValue(int i) {
        if (i >= this.thresholdValues.length) {
            return 0;
        }
        return this.thresholdValues[i];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onClickMaintenanceWarning() {
        showWarningPickerItems();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideWarningPickerItems() {
        this.titleHolder.setVisibility(8);
        this.warningPicker.setVisibility(8);
        this.doneButton.setVisibility(8);
        this.pendingTresholdUpdate = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void versionOneHideViews() {
        hideWarningPickerItems();
        this.maintenanceProgressBar.setVisibility(8);
        this.steps.setVisibility(8);
        this.maintenanceWarningText.setVisibility(8);
        this.paragraph.setVisibility(8);
        this.progressBarTitle.setVisibility(8);
        this.progressLine.setVisibility(8);
        this.stepsUnderLineLabel.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideUnsupportedGen3Views() {
        versionOneHideViews();
        this.resetMaintenanceText.setVisibility(8);
        this.paragraph.setVisibility(0);
        this.paragraph.setText(R.string.maintenance_update_requirement_text);
    }

    private void showWarningPickerItems() {
        this.titleHolder.setVisibility(0);
        this.warningPicker.setVisibility(0);
        this.doneButton.setVisibility(0);
        this.pendingStepsTreshold = getThresholdForNumberPickerValue(this.warningPicker.getValue());
        this.pendingTresholdUpdate = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enableMaintenanceButtons(boolean z) {
        this.maintenanceWarningText.setAlpha(z ? 1.0f : 0.3f);
        this.resetMaintenanceText.setAlpha(z ? 1.0f : 0.3f);
        this.maintenanceWarningText.setEnabled(z);
        this.resetMaintenanceText.setEnabled(z);
        if (!this.pendingTresholdUpdate || z) {
            return;
        }
        hideWarningPickerItems();
    }

    protected void showAlert(String str, String str2) {
        if (getContext() == null) {
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(str2);
        builder.setTitle(str);
        builder.setNeutralButton("Dismiss", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Maintenance.TreadlyProfileSettingsMaintenance.8
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Maintenance.TreadlyProfileSettingsMaintenance.9
            @Override // java.lang.Runnable
            public void run() {
                builder.create().show();
            }
        });
    }

    private void pauseDevice() {
        if (this.bleComponent == null) {
            return;
        }
        VersionInfo versionInfo = this.bleComponent.getVersionInfo().getVersionInfo();
        if (versionInfo != null) {
            if (!requiresPause(versionInfo)) {
                return;
            }
            if (versionInfo.isGreaterThan(new VersionInfo(2, 14, 3))) {
                TreadlyClientLib.shared.pauseDevice();
                return;
            }
        }
        if (this.isPaused || this.current_speed <= Utils.DOUBLE_EPSILON) {
            return;
        }
        TreadlyClientLib.shared.powerDevice();
    }

    /* renamed from: com.treadly.Treadly.UI.TreadlyProfile.Settings.Maintenance.TreadlyProfileSettingsMaintenance$10  reason: invalid class name */
    /* loaded from: classes2.dex */
    class AnonymousClass10 extends RequestEventAdapter {
        AnonymousClass10() {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventAdapter, com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestStatusResponse(final boolean z, final DeviceStatus deviceStatus) {
            ActivityUtil.runOnUiThread(TreadlyProfileSettingsMaintenance.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Maintenance.TreadlyProfileSettingsMaintenance.10.1
                @Override // java.lang.Runnable
                public void run() {
                    if (!z || deviceStatus == null) {
                        return;
                    }
                    TreadlyProfileSettingsMaintenance.this.deviceStatus = deviceStatus;
                    final MaintenanceInfo maintenanceInfo = deviceStatus.getMaintenanceInfo();
                    final int totalSteps = deviceStatus.getTotalSteps();
                    TreadlyProfileSettingsMaintenance.this.isPaused = deviceStatus.getPauseState();
                    TreadlyProfileSettingsMaintenance.this.current_speed = deviceStatus.getSpeedInfo().getTargetSpeed();
                    TreadlyProfileSettingsMaintenance.this.initThresholdValues(maintenanceInfo.getMaxThreshold());
                    if (TreadlyProfileSettingsMaintenance.this.pendingMaintenanceReset) {
                        if (maintenanceInfo.isMaintenaceRequired() || maintenanceInfo.getSteps() != 0) {
                            TreadlyClientLib.shared.resetMaintenance();
                        } else {
                            TreadlyProfileSettingsMaintenance.this.pendingMaintenanceReset = false;
                            if (TreadlyProfileSettingsMaintenance.this.displayResetSuccess) {
                                TreadlyProfileSettingsMaintenance.this.showAlert("Reset Maintenance", "Maintenance successfully reset");
                            }
                            TreadlyProfileSettingsMaintenance.this.displayResetSuccess = false;
                        }
                    }
                    ActivityUtil.runOnUiThread(TreadlyProfileSettingsMaintenance.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Maintenance.TreadlyProfileSettingsMaintenance.10.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (TreadlyProfileSettingsMaintenance.this.warningPicker.getVisibility() == 8) {
                                TreadlyProfileSettingsMaintenance.this.warningPicker.setValue(TreadlyProfileSettingsMaintenance.this.getNumberPickerValue(maintenanceInfo.getStepThreshold()));
                            }
                            TreadlyProfileSettingsMaintenance.this.enableMaintenanceButtons(TreadlyProfileSettingsMaintenance.this.canUpdateMaintenance());
                            TreadlyProfileSettingsMaintenance.this.maintenanceProgressBar.setProgress(0);
                            TreadlyProfileSettingsMaintenance.this.maintenanceProgressBar.setMin(0);
                            TreadlyProfileSettingsMaintenance.this.maintenanceProgressBar.setMax(maintenanceInfo.getStepThreshold());
                            TreadlyProfileSettingsMaintenance.this.maintenanceProgressBar.setProgress(maintenanceInfo.getStepThreshold() - maintenanceInfo.getSteps());
                            TreadlyProfileSettingsMaintenance.this.updateProgressLine();
                            if (TreadlyProfileSettingsMaintenance.this.pendingTresholdUpdate) {
                                int steps = TreadlyProfileSettingsMaintenance.this.pendingStepsTreshold - maintenanceInfo.getSteps();
                                if (steps <= 0) {
                                    steps = 0;
                                }
                                TreadlyProfileSettingsMaintenance.this.steps.setText(String.format(Locale.getDefault(), "%,d", Integer.valueOf(steps)));
                            } else {
                                int stepThreshold = maintenanceInfo.getStepThreshold() - maintenanceInfo.getSteps();
                                if (stepThreshold <= 0) {
                                    stepThreshold = 0;
                                }
                                TreadlyProfileSettingsMaintenance.this.steps.setText(String.format(Locale.getDefault(), "%,d", Integer.valueOf(stepThreshold)));
                            }
                            TreadlyProfileSettingsMaintenance.this.stepsPerm.setText(String.format("%,d", Integer.valueOf(totalSteps)));
                        }
                    });
                }
            });
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventAdapter, com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestGetDeviceComponentsListResponse(boolean z, ComponentInfo[] componentInfoArr) {
            if (z) {
                for (ComponentInfo componentInfo : componentInfoArr) {
                    if (componentInfo.getType() == ComponentType.bleBoard) {
                        TreadlyProfileSettingsMaintenance.this.bleComponent = componentInfo;
                        VersionInfo versionInfo = TreadlyProfileSettingsMaintenance.this.bleComponent.getVersionInfo().getVersionInfo();
                        if (versionInfo != null) {
                            if (!versionInfo.isGreaterThan(new VersionInfo(2, 0, 0))) {
                                TreadlyProfileSettingsMaintenance.this.versionOneHideViews();
                                return;
                            } else if (TreadlyProfileSettingsMaintenance.this.supportsMaintenance()) {
                                return;
                            } else {
                                TreadlyProfileSettingsMaintenance.this.hideUnsupportedGen3Views();
                                return;
                            }
                        }
                        return;
                    }
                }
            }
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventAdapter, com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestResetMaintenancResponse(boolean z) {
            if (!TreadlyProfileSettingsMaintenance.this.hasVersion() || TreadlyProfileSettingsMaintenance.this.requiresPause()) {
                return;
            }
            if (TreadlyProfileSettingsMaintenance.this.displayResetSuccess) {
                TreadlyProfileSettingsMaintenance.this.showAlert("Reset Maintenance", z ? "Maintenance successfully reset" : "Error resetting maintenance");
            }
            TreadlyProfileSettingsMaintenance.this.displayResetSuccess = false;
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventAdapter, com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestFactoryResetResponse(boolean z) {
            TreadlyProfileSettingsMaintenance.this.showAlert("Reset Maintenance", z ? "Maintenance successfully reset" : "Error resetting maintenance.  Please verify the treadmill is idle.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasVersion() {
        return (this.bleComponent == null || this.bleComponent.getVersionInfo() == null) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean requiresPause() {
        if (hasVersion()) {
            return requiresPause(this.bleComponent.getVersionInfo().getVersionInfo());
        }
        return false;
    }

    private boolean requiresPause(VersionInfo versionInfo) {
        if (versionInfo == null) {
            return false;
        }
        VersionInfo versionInfo2 = new VersionInfo(3, 31, 0);
        return (versionInfo.isGreaterThan(versionInfo2) || versionInfo.isEqual(versionInfo2)) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean canUpdateMaintenance() {
        if (hasVersion()) {
            return checkSessionOwner(this.deviceStatus);
        }
        return false;
    }

    boolean checkSessionOwner(DeviceStatus deviceStatus) {
        VersionInfo versionInfo = new VersionInfo(3, 14, 0);
        ComponentInfo componentInfo = this.bleComponent;
        if (componentInfo == null) {
            return true;
        }
        VersionInfo versionInfo2 = componentInfo.getVersionInfo().getVersionInfo();
        if ((versionInfo2.isGreaterThan(versionInfo) || versionInfo2.isEqual(versionInfo)) && deviceStatus.isPoweredOn() && deviceStatus.getSpeedInfo().getTargetSpeed() > Utils.DOUBLE_EPSILON) {
            return deviceStatus.isSessionOwnership();
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean supportsMaintenance() {
        ComponentInfo componentInfo = this.bleComponent;
        if (componentInfo == null || componentInfo.getVersionInfo() == null) {
            return false;
        }
        VersionInfo versionInfo = componentInfo.getVersionInfo().getVersionInfo();
        if (isGen3()) {
            VersionInfo versionInfo2 = new VersionInfo(3, 31, 0);
            return versionInfo.isGreaterThan(versionInfo2) || versionInfo.isEqual(versionInfo2);
        }
        return true;
    }

    boolean isGen3() {
        if (this.bleComponent == null || this.bleComponent.getVersionInfo() == null) {
            return false;
        }
        VersionInfo versionInfo = new VersionInfo(3, 0, 0);
        return this.bleComponent.getVersionInfo().isGreaterThan(versionInfo) || this.bleComponent.getVersionInfo().isEqual(versionInfo);
    }
}
