package com.treadly.Treadly.UI.TreadlyProfile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.PointerIconCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.github.mikephil.charting.utils.Utils;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.Data.Model.UserDailyGoalType;
import com.treadly.Treadly.Data.Model.UserProfileInfo;
import com.treadly.Treadly.Data.Model.UserProfileRequest;
import com.treadly.Treadly.Data.Utility.NotificationCenter.NotificationCenter;
import com.treadly.Treadly.Data.Utility.NotificationCenter.NotificationType;
import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyAccount.LoginActivity;
import com.treadly.Treadly.UI.Util.ActivityUtil;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
import com.treadly.Treadly.UI.Util.OnBackPressedListener;
import com.treadly.client.lib.sdk.Model.DistanceUnits;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: classes2.dex */
public class TreadlyProfileEditFragment extends BaseFragment {
    public static final String TAG = "TREADLY_PROFILE_EDIT";
    private ImageView backArrow;
    ConstraintLayout dailyGoalBlur;
    int dailyGoalCalories;
    Button dailyGoalCaloriesButton;
    double dailyGoalDistance;
    Button dailyGoalDistanceButton;
    double dailyGoalDuration;
    Button dailyGoalDurationButton;
    private TextView dailyGoalLabel;
    private NumberPicker dailyGoalPicker;
    private TextView dailyGoalPickerTitle;
    private ConstraintLayout dailyGoalPickerTitleHolder;
    int dailyGoalSteps;
    Button dailyGoalStepsButton;
    private TextView dailyGoalText;
    UserDailyGoalType dailyGoalType;
    private Button dailyGoalUpdateButton;
    private NumberPicker datePicker;
    ConstraintLayout dateSelector;
    boolean dateSelectorOpen;
    private ImageButton decreaseGoalButton;
    double displayedDailyGoalDistance;
    public boolean fromLogin;
    ImageButton genderFemaleButton;
    ImageButton genderMaleButton;
    private AlertDialog goalWarningAlert;
    private NumberPicker heightPicker;
    ConstraintLayout heightSelector;
    boolean heightSelectorOpen;
    private ImageButton increaseGoalButton;
    int initialDailyCaloriesGoal;
    double initialDailyDistanceGoal;
    double initialDailyDurationGoal;
    int initialDailyStepsGoal;
    UserDailyGoalType initialGoalType;
    DistanceUnits initialUnits;
    private ImageButton saveButton;
    private ScrollView scrollView;
    private TextView titleLabel;
    ImageButton unitsKMButton;
    ImageButton unitsMIButton;
    public boolean useDefaultProfile;
    private NumberPicker weightPicker;
    ConstraintLayout weightSelector;
    boolean weightSelectorOpen;
    private final int ageMinimum = 13;
    private final double miToKm = 1.609344d;
    String userId = TreadlyServiceManager.getInstance().getUserId();
    private UserProfileInfo profile = null;
    public boolean hideNaviagtionBar = false;
    List<Integer> ageValueList = new ArrayList();
    List<String> ageStringList = new ArrayList();
    int ageSelectedIndex = 0;
    List<Double> weightValueList = new ArrayList();
    List<String> weightStringList = new ArrayList();
    int weightSelectedIndex = 0;
    List<Double> heightValueList = new ArrayList();
    List<String> heightStringList = new ArrayList();
    int heightSelectedIndex = 0;
    int dailyGoalSelectedIndex = 0;
    DistanceUnits units = DistanceUnits.MI;
    boolean isMale = true;
    Timer plusButtonTimer = new Timer();
    Timer minusButtonTimer = new Timer();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$onCreateView$0() {
    }

    boolean checkAgeRequirement(int i) {
        return i >= 13;
    }

    void setupDailyGoalParameters() {
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        if (!this.fromLogin) {
            ((MainActivity) getActivity()).hideBottomNavigationView();
        } else {
            ((LoginActivity) getActivity()).setOnBackPressedListener(new OnBackPressedListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.-$$Lambda$TreadlyProfileEditFragment$dYZok5AZdsXOjfUD7jrOl_XbULw
                @Override // com.treadly.Treadly.UI.Util.OnBackPressedListener
                public final void backAction() {
                    TreadlyProfileEditFragment.lambda$onCreateView$0();
                }
            });
        }
        return layoutInflater.inflate(R.layout.fragment_treadly_profile_edit, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        setupView(view);
        setupAgeView(view);
        setupAgeParameters();
        setupWeightView(view);
        setupWeightParameters();
        setupHeightView(view);
        setupHeightParameters();
        setupGenderView(view);
        setupUnitsView(view);
        setupDailyGoalTypeView(view);
        setupDailyGoalView(view);
        setupDailyGoalParameters();
        if (this.useDefaultProfile) {
            setupDefaultProfile();
            updateView();
            return;
        }
        getProfile();
    }

    void setupView(View view) {
        this.titleLabel = (TextView) view.findViewById(R.id.header_title);
        if (this.fromLogin) {
            this.titleLabel.setText("Your Info");
        } else {
            this.titleLabel.setText("Profile Settings");
        }
        this.backArrow = (ImageView) view.findViewById(R.id.back_arrow);
        if (this.fromLogin) {
            this.backArrow.setVisibility(4);
        } else {
            this.backArrow.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.-$$Lambda$TreadlyProfileEditFragment$PQ1cPcKTowkFDw1mCMsAspePP_c
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    TreadlyProfileEditFragment.this.popBackStack();
                }
            });
        }
        this.scrollView = (ScrollView) view.findViewById(R.id.scroll_edit);
        this.scrollView.fullScroll(33);
        this.saveButton = (ImageButton) view.findViewById(R.id.continue_button);
        if (!this.fromLogin) {
            this.saveButton.setBackgroundResource(R.drawable.update_button_edit);
        }
        this.saveButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.-$$Lambda$TreadlyProfileEditFragment$VBtiM2IHEnsvmrCrMbRUk_WZg9E
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileEditFragment.this.onSaveButtonPressed();
            }
        });
    }

    void setupAgeView(View view) {
        this.dateSelectorOpen = false;
        this.dateSelector = (ConstraintLayout) view.findViewById(R.id.date_selector_header);
        this.dateSelector.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.-$$Lambda$TreadlyProfileEditFragment$LfjYrewtifEhU9EuEzq13ouG__A
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileEditFragment.lambda$setupAgeView$3(TreadlyProfileEditFragment.this, view2);
            }
        });
        this.datePicker = (NumberPicker) view.findViewById(R.id.date_picker);
        this.datePicker.setMinValue(1900);
        this.datePicker.setMaxValue(Calendar.getInstance().get(1));
        this.datePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.-$$Lambda$TreadlyProfileEditFragment$23_rN8zOqMMYErfSw3Jz67Gz7mc
            @Override // android.widget.NumberPicker.OnValueChangeListener
            public final void onValueChange(NumberPicker numberPicker, int i, int i2) {
                TreadlyProfileEditFragment.this.ageSelectedIndex = i2 - 1900;
            }
        });
    }

    public static /* synthetic */ void lambda$setupAgeView$3(TreadlyProfileEditFragment treadlyProfileEditFragment, View view) {
        if (!treadlyProfileEditFragment.dateSelectorOpen) {
            treadlyProfileEditFragment.datePicker.setVisibility(0);
            treadlyProfileEditFragment.dateSelectorOpen = true;
            return;
        }
        treadlyProfileEditFragment.datePicker.setVisibility(8);
        treadlyProfileEditFragment.dateSelectorOpen = false;
    }

    void setupAgeParameters() {
        int i = Calendar.getInstance().get(1);
        for (int i2 = 1900; i2 <= i; i2++) {
            this.ageValueList.add(Integer.valueOf(i2));
            this.ageStringList.add(String.valueOf(i2));
        }
        if (this.profile != null) {
            this.datePicker.setValue(this.ageValueList.indexOf(Integer.valueOf(this.profile.birthdateYear)) + 1900);
            return;
        }
        this.datePicker.setValue(this.ageValueList.indexOf(1985) + 1900);
    }

    void setupWeightView(View view) {
        this.weightSelector = (ConstraintLayout) view.findViewById(R.id.weight_selector);
        this.weightSelectorOpen = false;
        this.weightSelector.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.-$$Lambda$TreadlyProfileEditFragment$X1NZKFAp__Ek-wzxIOvW2hw_ge4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileEditFragment.lambda$setupWeightView$5(TreadlyProfileEditFragment.this, view2);
            }
        });
        this.weightPicker = (NumberPicker) view.findViewById(R.id.weight_picker);
        this.weightPicker.setMinValue(0);
        this.weightPicker.setMaxValue(GenericDraweeHierarchyBuilder.DEFAULT_FADE_DURATION);
        this.weightPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.-$$Lambda$TreadlyProfileEditFragment$evRMVWz0oiwQo7yw87EYmESCFjY
            @Override // android.widget.NumberPicker.OnValueChangeListener
            public final void onValueChange(NumberPicker numberPicker, int i, int i2) {
                TreadlyProfileEditFragment.this.weightSelectedIndex = i2;
            }
        });
    }

    public static /* synthetic */ void lambda$setupWeightView$5(TreadlyProfileEditFragment treadlyProfileEditFragment, View view) {
        if (!treadlyProfileEditFragment.weightSelectorOpen) {
            treadlyProfileEditFragment.weightPicker.setVisibility(0);
            treadlyProfileEditFragment.weightSelectorOpen = true;
            return;
        }
        treadlyProfileEditFragment.weightPicker.setVisibility(8);
        treadlyProfileEditFragment.weightSelectorOpen = false;
    }

    void setupWeightParameters() {
        for (double d = Utils.DOUBLE_EPSILON; d < 300.0d; d += 1.0d) {
            String format = String.format("%.0f", Double.valueOf(d));
            this.weightValueList.add(Double.valueOf(Math.round(d) * 1.0d));
            this.weightStringList.add(format);
        }
        if (this.profile != null) {
            this.weightPicker.setValue(this.weightValueList.indexOf(Double.valueOf(this.profile.weight)));
        } else {
            this.weightPicker.setValue(this.weightPicker.getMinValue() + (this.weightPicker.getMaxValue() / 2));
        }
    }

    void setupHeightView(View view) {
        this.heightSelector = (ConstraintLayout) view.findViewById(R.id.height_selector);
        this.heightSelectorOpen = false;
        this.heightSelector.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.-$$Lambda$TreadlyProfileEditFragment$IBlSVuEp7j54AGpU60JReGKkgBw
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileEditFragment.lambda$setupHeightView$7(TreadlyProfileEditFragment.this, view2);
            }
        });
        this.heightPicker = (NumberPicker) view.findViewById(R.id.height_picker);
        this.heightPicker.setMaxValue(119);
        this.heightPicker.setMinValue(0);
        for (int i = 0; i < 10; i++) {
            for (int i2 = 0; i2 < 12; i2++) {
                this.heightValueList.add(Double.valueOf((i * 12) + i2));
                this.heightStringList.add(i + "," + i2);
            }
        }
        this.heightPicker.setFormatter(new NumberPicker.Formatter() { // from class: com.treadly.Treadly.UI.TreadlyProfile.-$$Lambda$TreadlyProfileEditFragment$eqopZ1ZvY8AoFrYrMkQPcSitJZE
            @Override // android.widget.NumberPicker.Formatter
            public final String format(int i3) {
                String str;
                str = TreadlyProfileEditFragment.this.heightStringList.get(i3);
                return str;
            }
        });
        this.heightPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.-$$Lambda$TreadlyProfileEditFragment$rCk6BoaA6QNAcXOObsCULJdKqY0
            @Override // android.widget.NumberPicker.OnValueChangeListener
            public final void onValueChange(NumberPicker numberPicker, int i3, int i4) {
                TreadlyProfileEditFragment.this.heightSelectedIndex = i4;
            }
        });
    }

    public static /* synthetic */ void lambda$setupHeightView$7(TreadlyProfileEditFragment treadlyProfileEditFragment, View view) {
        if (!treadlyProfileEditFragment.heightSelectorOpen) {
            treadlyProfileEditFragment.heightPicker.setVisibility(0);
            treadlyProfileEditFragment.heightSelectorOpen = true;
            return;
        }
        treadlyProfileEditFragment.heightPicker.setVisibility(8);
        treadlyProfileEditFragment.heightSelectorOpen = false;
    }

    void setupHeightParameters() {
        if (this.profile != null) {
            this.heightPicker.setValue(this.heightValueList.indexOf(Double.valueOf(this.profile.height)));
            return;
        }
        this.heightPicker.setValue(this.heightPicker.getMinValue() + (this.heightPicker.getMaxValue() / 2));
    }

    void setupGenderView(View view) {
        this.genderMaleButton = (ImageButton) view.findViewById(R.id.male_button_edit);
        this.genderMaleButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.-$$Lambda$TreadlyProfileEditFragment$aQ-Gfiwl7FeKL1laSwemFQRw2zg
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileEditFragment.lambda$setupGenderView$10(TreadlyProfileEditFragment.this, view2);
            }
        });
        this.genderFemaleButton = (ImageButton) view.findViewById(R.id.female_button_edit);
        this.genderFemaleButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.-$$Lambda$TreadlyProfileEditFragment$B-frzRrgKdumJTzyDGAbxJ-RG4A
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileEditFragment.lambda$setupGenderView$11(TreadlyProfileEditFragment.this, view2);
            }
        });
        if (this.profile == null || this.profile.gender == null) {
            return;
        }
        this.isMale = this.profile.gender.equals("male");
    }

    public static /* synthetic */ void lambda$setupGenderView$10(TreadlyProfileEditFragment treadlyProfileEditFragment, View view) {
        treadlyProfileEditFragment.isMale = true;
        treadlyProfileEditFragment.genderFemaleButton.setBackgroundResource(R.drawable.female_button_not_pressed);
        treadlyProfileEditFragment.genderMaleButton.setBackgroundResource(R.drawable.male_button_pressed);
    }

    public static /* synthetic */ void lambda$setupGenderView$11(TreadlyProfileEditFragment treadlyProfileEditFragment, View view) {
        treadlyProfileEditFragment.isMale = false;
        treadlyProfileEditFragment.genderMaleButton.setBackgroundResource(R.drawable.male_button_not_pressed);
        treadlyProfileEditFragment.genderFemaleButton.setBackgroundResource(R.drawable.female_button_pressed);
    }

    void setupUnitsView(View view) {
        this.unitsMIButton = (ImageButton) view.findViewById(R.id.miles_button_edit);
        this.unitsMIButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.-$$Lambda$TreadlyProfileEditFragment$jz2g2xb-NPrxiPS1JqOfm_eYbv0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileEditFragment.lambda$setupUnitsView$12(TreadlyProfileEditFragment.this, view2);
            }
        });
        this.unitsKMButton = (ImageButton) view.findViewById(R.id.kilometers_button_edit);
        this.unitsKMButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.-$$Lambda$TreadlyProfileEditFragment$eFHHZLUYnFKvZn4rymIyV04zN2M
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileEditFragment.lambda$setupUnitsView$13(TreadlyProfileEditFragment.this, view2);
            }
        });
    }

    public static /* synthetic */ void lambda$setupUnitsView$12(TreadlyProfileEditFragment treadlyProfileEditFragment, View view) {
        treadlyProfileEditFragment.units = DistanceUnits.MI;
        treadlyProfileEditFragment.unitsMIButton.setBackgroundResource(R.drawable.miles_button_pressed);
        treadlyProfileEditFragment.unitsKMButton.setBackgroundResource(R.drawable.kilometers_button_not_pressed);
        treadlyProfileEditFragment.displayedDailyGoalDistance = treadlyProfileEditFragment.dailyGoalDistance;
        treadlyProfileEditFragment.setDailyGoalTitle();
        treadlyProfileEditFragment.setDailyGoalType();
    }

    public static /* synthetic */ void lambda$setupUnitsView$13(TreadlyProfileEditFragment treadlyProfileEditFragment, View view) {
        treadlyProfileEditFragment.units = DistanceUnits.KM;
        treadlyProfileEditFragment.unitsKMButton.setBackgroundResource(R.drawable.kilometers_button_pressed);
        treadlyProfileEditFragment.unitsMIButton.setBackgroundResource(R.drawable.miles_button_not_pressed);
        treadlyProfileEditFragment.displayedDailyGoalDistance = treadlyProfileEditFragment.dailyGoalDistance * 1.609344d;
        treadlyProfileEditFragment.setDailyGoalTitle();
        treadlyProfileEditFragment.setDailyGoalType();
    }

    void setupDailyGoalView(View view) {
        this.increaseGoalButton = (ImageButton) view.findViewById(R.id.plus_button_edit_profile);
        this.decreaseGoalButton = (ImageButton) view.findViewById(R.id.minus_button_edit_profile);
        this.dailyGoalText = (TextView) view.findViewById(R.id.daily_steps_goal_value);
        this.dailyGoalText.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.-$$Lambda$TreadlyProfileEditFragment$6U4ZJhl7a2DN-BsMkJZkW1m9QpQ
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileEditFragment.this.onDailyGoalTextPressed();
            }
        });
        this.dailyGoalLabel = (TextView) view.findViewById(R.id.calories_label_under_control);
        this.dailyGoalPickerTitleHolder = (ConstraintLayout) view.findViewById(R.id.daily_goal_picker_title_holder);
        this.dailyGoalPickerTitleHolder.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.-$$Lambda$TreadlyProfileEditFragment$EcutcwPrp85CmofuAKbvOApQCCg
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileEditFragment.this.dismissDailyGoalPicker();
            }
        });
        this.dailyGoalPickerTitle = (TextView) view.findViewById(R.id.daily_goal_picker_title);
        this.dailyGoalPicker = (NumberPicker) view.findViewById(R.id.daily_goal_picker);
        this.dailyGoalUpdateButton = (Button) view.findViewById(R.id.daily_goal_picker_done_button);
        this.dailyGoalUpdateButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.-$$Lambda$TreadlyProfileEditFragment$PNWnqC8Qg1FPxgGMzY1mFnUIjps
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileEditFragment.lambda$setupDailyGoalView$16(TreadlyProfileEditFragment.this, view2);
            }
        });
        this.dailyGoalBlur = (ConstraintLayout) view.findViewById(R.id.profile_edit_blur);
        this.dailyGoalBlur.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.-$$Lambda$TreadlyProfileEditFragment$OIwR66Gbp9nT9Yn-muFDOgVzvw0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileEditFragment.this.dismissDailyGoalPicker();
            }
        });
        this.increaseGoalButton.setClipToOutline(true);
        this.decreaseGoalButton.setClipToOutline(true);
        this.increaseGoalButton.setOnTouchListener(new View.OnTouchListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileEditFragment.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                TreadlyProfileEditFragment.this.dailyGoalPlusButtonPressed(view2, motionEvent);
                return false;
            }
        });
        this.decreaseGoalButton.setOnTouchListener(new View.OnTouchListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileEditFragment.2
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                TreadlyProfileEditFragment.this.dailyGoalMinusButtonPressed(view2, motionEvent);
                return false;
            }
        });
        if (this.profile != null) {
            this.dailyGoalType = this.profile.settingDailyGoalType;
            this.dailyGoalCalories = this.profile.settingCaloriesGoal;
            this.dailyGoalSteps = this.profile.settingStepsGoal;
            this.dailyGoalDistance = this.profile.settingDistanceGoal;
            this.displayedDailyGoalDistance = this.dailyGoalDistance * (this.units == DistanceUnits.KM ? 1.609344d : 1.0d);
            this.dailyGoalDuration = this.profile.settingDurationGoal;
        } else {
            this.dailyGoalType = UserDailyGoalType.calories;
            this.dailyGoalCalories = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
            this.dailyGoalSteps = 1000;
            this.dailyGoalDistance = 5.0d;
            this.displayedDailyGoalDistance = this.dailyGoalDistance;
            this.dailyGoalDuration = 3600.0d;
            if (!this.fromLogin) {
                this.dailyGoalText.setVisibility(4);
            }
        }
        this.dailyGoalText.setText(String.format("%,d", Integer.valueOf(this.dailyGoalCalories)));
    }

    public static /* synthetic */ void lambda$setupDailyGoalView$16(TreadlyProfileEditFragment treadlyProfileEditFragment, View view) {
        String str = "--";
        switch (treadlyProfileEditFragment.dailyGoalType) {
            case calories:
                treadlyProfileEditFragment.dailyGoalCalories = treadlyProfileEditFragment.dailyGoalPicker.getValue() * 100;
                str = String.format(Locale.getDefault(), "%,d", Integer.valueOf(treadlyProfileEditFragment.dailyGoalCalories));
                break;
            case steps:
                treadlyProfileEditFragment.dailyGoalSteps = treadlyProfileEditFragment.dailyGoalPicker.getValue() * 5;
                str = String.format(Locale.getDefault(), "%,d", Integer.valueOf(treadlyProfileEditFragment.dailyGoalSteps));
                break;
            case distance:
                treadlyProfileEditFragment.displayedDailyGoalDistance = treadlyProfileEditFragment.dailyGoalPicker.getValue() * 0.1f;
                treadlyProfileEditFragment.dailyGoalDistance = treadlyProfileEditFragment.displayedDailyGoalDistance / (treadlyProfileEditFragment.units == DistanceUnits.KM ? 1.609344d : 1.0d);
                DecimalFormat decimalFormat = new DecimalFormat();
                decimalFormat.setMinimumFractionDigits(1);
                decimalFormat.setMaximumFractionDigits(2);
                str = decimalFormat.format(treadlyProfileEditFragment.displayedDailyGoalDistance);
                break;
            case duration:
                treadlyProfileEditFragment.dailyGoalDuration = treadlyProfileEditFragment.dailyGoalPicker.getValue() * 5 * 60;
                str = String.format(Locale.getDefault(), "%,d", Integer.valueOf(((int) treadlyProfileEditFragment.dailyGoalDuration) / 60));
                break;
        }
        treadlyProfileEditFragment.dailyGoalText.setText(str);
        treadlyProfileEditFragment.dismissDailyGoalPicker();
    }

    void setupDailyGoalTypeView(View view) {
        this.dailyGoalCaloriesButton = (Button) view.findViewById(R.id.daily_goal_selector_calories);
        this.dailyGoalCaloriesButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.-$$Lambda$TreadlyProfileEditFragment$R-8KI2MGVV6IykPzc_6xZdArB84
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileEditFragment.lambda$setupDailyGoalTypeView$18(TreadlyProfileEditFragment.this, view2);
            }
        });
        this.dailyGoalStepsButton = (Button) view.findViewById(R.id.daily_goal_selector_steps);
        this.dailyGoalStepsButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.-$$Lambda$TreadlyProfileEditFragment$laTIyvg2m8TCEjsLp5UI1Fviu2k
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileEditFragment.lambda$setupDailyGoalTypeView$19(TreadlyProfileEditFragment.this, view2);
            }
        });
        this.dailyGoalDistanceButton = (Button) view.findViewById(R.id.daily_goal_selector_distance);
        this.dailyGoalDistanceButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.-$$Lambda$TreadlyProfileEditFragment$waO5BkNxuyE-_m_rLZV112mul1s
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileEditFragment.lambda$setupDailyGoalTypeView$20(TreadlyProfileEditFragment.this, view2);
            }
        });
        this.dailyGoalDurationButton = (Button) view.findViewById(R.id.daily_goal_selector_duration);
        this.dailyGoalDurationButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.-$$Lambda$TreadlyProfileEditFragment$kHRYbXsDiclmk48A94BBcLVdxM4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileEditFragment.lambda$setupDailyGoalTypeView$21(TreadlyProfileEditFragment.this, view2);
            }
        });
        this.dailyGoalType = this.profile != null ? this.profile.settingDailyGoalType : UserDailyGoalType.none;
    }

    public static /* synthetic */ void lambda$setupDailyGoalTypeView$18(TreadlyProfileEditFragment treadlyProfileEditFragment, View view) {
        if (treadlyProfileEditFragment.profile == null) {
            return;
        }
        treadlyProfileEditFragment.dailyGoalType = UserDailyGoalType.calories;
        treadlyProfileEditFragment.setDailyGoalType();
    }

    public static /* synthetic */ void lambda$setupDailyGoalTypeView$19(TreadlyProfileEditFragment treadlyProfileEditFragment, View view) {
        if (treadlyProfileEditFragment.profile == null) {
            return;
        }
        treadlyProfileEditFragment.dailyGoalType = UserDailyGoalType.steps;
        treadlyProfileEditFragment.setDailyGoalType();
    }

    public static /* synthetic */ void lambda$setupDailyGoalTypeView$20(TreadlyProfileEditFragment treadlyProfileEditFragment, View view) {
        if (treadlyProfileEditFragment.profile == null) {
            return;
        }
        treadlyProfileEditFragment.dailyGoalType = UserDailyGoalType.distance;
        treadlyProfileEditFragment.setDailyGoalType();
    }

    public static /* synthetic */ void lambda$setupDailyGoalTypeView$21(TreadlyProfileEditFragment treadlyProfileEditFragment, View view) {
        if (treadlyProfileEditFragment.profile == null) {
            return;
        }
        treadlyProfileEditFragment.dailyGoalType = UserDailyGoalType.duration;
        treadlyProfileEditFragment.setDailyGoalType();
    }

    void dailyGoalPlusButtonPressed(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action != 3) {
            switch (action) {
                case 0:
                    startPlusButtonTimer();
                    return;
                case 1:
                    stopPlusButtonTimer();
                    return;
                default:
                    return;
            }
        }
        stopPlusButtonTimer();
    }

    void dailyGoalMinusButtonPressed(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action != 3) {
            switch (action) {
                case 0:
                    startMinusButtonTimer();
                    return;
                case 1:
                    stopMinusButtonTimer();
                    return;
                default:
                    return;
            }
        }
        stopMinusButtonTimer();
    }

    void startPlusButtonTimer() {
        stopPlusButtonTimer();
        this.plusButtonTimer = new Timer();
        this.plusButtonTimer.schedule(new AnonymousClass3(), 0L, 200L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileEditFragment$3  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 extends TimerTask {
        AnonymousClass3() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            ActivityUtil.runOnUiThread(TreadlyProfileEditFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.-$$Lambda$TreadlyProfileEditFragment$3$xtZWVZfFSOXtUnDfET6lZf9OSv8
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyProfileEditFragment.this.dailyGoalPlusTimer();
                }
            });
        }
    }

    void stopPlusButtonTimer() {
        this.plusButtonTimer.cancel();
        this.plusButtonTimer.purge();
    }

    void startMinusButtonTimer() {
        stopMinusButtonTimer();
        this.minusButtonTimer = new Timer();
        this.minusButtonTimer.schedule(new AnonymousClass4(), 0L, 200L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileEditFragment$4  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 extends TimerTask {
        AnonymousClass4() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            ActivityUtil.runOnUiThread(TreadlyProfileEditFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.-$$Lambda$TreadlyProfileEditFragment$4$mfb5f9cB-JGjRhsD0BfGdcaoA0A
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyProfileEditFragment.this.dailyGoalMinusTimer();
                }
            });
        }
    }

    void stopMinusButtonTimer() {
        this.minusButtonTimer.cancel();
        this.minusButtonTimer.purge();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dailyGoalPlusTimer() {
        switch (this.dailyGoalType) {
            case calories:
                if (this.dailyGoalCalories < 5000) {
                    this.dailyGoalCalories += 5;
                    break;
                }
                break;
            case steps:
                if (this.dailyGoalSteps < 100000) {
                    this.dailyGoalSteps += 100;
                    break;
                }
                break;
            case distance:
                if (this.dailyGoalDistance < 20.0d) {
                    this.displayedDailyGoalDistance = Math.ceil((this.displayedDailyGoalDistance + 1.0E-4d) / 0.10000000149011612d) * 0.10000000149011612d;
                    this.dailyGoalDistance = this.displayedDailyGoalDistance * (this.units == DistanceUnits.KM ? 0.621371192237334d : 1.0d);
                    break;
                }
                break;
            case duration:
                if (this.dailyGoalDuration < 86400.0d) {
                    this.dailyGoalDuration += 300.0d;
                    break;
                }
                break;
        }
        setDailyGoalTitle();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dailyGoalMinusTimer() {
        switch (this.dailyGoalType) {
            case calories:
                if (this.dailyGoalCalories > 5) {
                    this.dailyGoalCalories -= 5;
                    break;
                }
                break;
            case steps:
                if (this.dailyGoalSteps > 100) {
                    this.dailyGoalSteps -= 100;
                    break;
                }
                break;
            case distance:
                if (this.dailyGoalDistance > 0.10000000149011612d) {
                    this.displayedDailyGoalDistance = Math.floor((this.displayedDailyGoalDistance - 1.0E-4d) / 0.10000000149011612d) * 0.10000000149011612d;
                    this.dailyGoalDistance = this.displayedDailyGoalDistance * (this.units == DistanceUnits.KM ? 0.621371192237334d : 1.0d);
                    break;
                }
                break;
            case duration:
                if (this.dailyGoalDuration > 300.0d) {
                    this.dailyGoalDuration -= 300.0d;
                    break;
                }
                break;
        }
        setDailyGoalTitle();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSaveButtonPressed() {
        if (this.profile == null) {
            return;
        }
        UserDailyGoalType userDailyGoalType = this.profile.currentDailyGoalType;
        if (userDailyGoalType == UserDailyGoalType.none) {
            updateProfile();
        } else if (userDailyGoalType != this.dailyGoalType) {
            displayGoalWarning();
        } else {
            switch (userDailyGoalType) {
                case calories:
                    if (this.profile.currentCaloriesGoal >= 0 && this.profile.currentCaloriesGoal != this.dailyGoalCalories) {
                        displayGoalWarning();
                        return;
                    }
                    break;
                case steps:
                    if (this.profile.currentStepsGoal >= 0 && this.profile.currentStepsGoal != this.dailyGoalSteps) {
                        displayGoalWarning();
                        return;
                    }
                    break;
                case distance:
                    if (this.profile.currentDistanceGoal >= Utils.DOUBLE_EPSILON && this.profile.currentDistanceGoal != this.dailyGoalDistance) {
                        displayGoalWarning();
                        return;
                    }
                    break;
                case duration:
                    if (this.profile.currentDurationGoal >= Utils.DOUBLE_EPSILON && this.profile.currentDurationGoal != this.dailyGoalDuration) {
                        displayGoalWarning();
                        return;
                    }
                    break;
            }
            updateProfile();
        }
    }

    void displayGoalWarning() {
        this.goalWarningAlert = new AlertDialog.Builder(getContext()).create();
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.-$$Lambda$TreadlyProfileEditFragment$tqgaqSLs_pfqH-4NslUnRPliCyg
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyProfileEditFragment.lambda$displayGoalWarning$23(TreadlyProfileEditFragment.this);
            }
        });
    }

    public static /* synthetic */ void lambda$displayGoalWarning$23(final TreadlyProfileEditFragment treadlyProfileEditFragment) {
        treadlyProfileEditFragment.goalWarningAlert.setTitle("Goal Update");
        treadlyProfileEditFragment.goalWarningAlert.setMessage("Changes to your daily goal will take effect tomorrow");
        treadlyProfileEditFragment.goalWarningAlert.setButton(-1, "Confirm", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.-$$Lambda$TreadlyProfileEditFragment$7uefuN_edGixJfQ9Ke3cKDAFaUU
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                TreadlyProfileEditFragment.this.updateProfile();
            }
        });
        treadlyProfileEditFragment.goalWarningAlert.setButton(-2, "Cancel", (DialogInterface.OnClickListener) null);
        treadlyProfileEditFragment.goalWarningAlert.show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateProfile() {
        if (this.profile != null) {
            final boolean z = this.initialDailyCaloriesGoal != this.dailyGoalCalories;
            boolean z2 = this.initialDailyStepsGoal != this.dailyGoalSteps;
            boolean z3 = this.initialDailyDistanceGoal != this.dailyGoalDistance;
            boolean z4 = this.initialDailyDurationGoal != this.dailyGoalDuration;
            boolean z5 = this.initialGoalType != this.dailyGoalType;
            boolean z6 = this.initialUnits != this.units;
            this.profile.birthdateYear = this.ageValueList.get(this.ageSelectedIndex).intValue();
            this.profile.units = this.units;
            this.profile.weight = this.weightValueList.get(this.weightSelectedIndex).doubleValue();
            this.profile.height = this.heightValueList.get(this.heightSelectedIndex).doubleValue();
            this.profile.gender = this.isMale ? "male" : "female";
            switch (this.dailyGoalType) {
                case calories:
                    this.profile.settingCaloriesGoal = this.dailyGoalCalories;
                    this.profile.settingDailyGoalType = this.dailyGoalType;
                    break;
                case steps:
                    this.profile.settingStepsGoal = this.dailyGoalSteps;
                    this.profile.settingDailyGoalType = this.dailyGoalType;
                    break;
                case distance:
                    this.profile.settingDistanceGoal = this.dailyGoalDistance;
                    this.profile.settingDailyGoalType = this.dailyGoalType;
                    break;
                case duration:
                    this.profile.settingDurationGoal = this.dailyGoalDuration;
                    this.profile.settingDailyGoalType = this.dailyGoalType;
                    break;
            }
            this.profile.age = getAgeFromBirthYear(this.profile.birthdateYear);
            if (!checkAgeRequirement(this.profile.age)) {
                showAgeRequirementError();
                return;
            }
            final boolean z7 = z2;
            final boolean z8 = z3;
            final boolean z9 = z4;
            final boolean z10 = z5;
            final boolean z11 = z6;
            TreadlyServiceManager.getInstance().updateUserProfile(new UserProfileRequest(this.profile.age, this.profile.weight, this.profile.height, this.profile.gender, this.profile.settingDailyGoalType, this.profile.settingCaloriesGoal, this.profile.settingStepsGoal, this.profile.settingDistanceGoal, this.profile.settingDurationGoal, this.profile.birthdateYear, this.profile.units, this.profile.deviceAddress), new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileEditFragment.5
                @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                public void onSuccess(String str) {
                    if (str == null) {
                        boolean z12 = TreadlyProfileEditFragment.this.hideNaviagtionBar;
                        if (z || z7 || z8 || z9 || z10 || z11) {
                            NotificationCenter.postNotification(TreadlyProfileEditFragment.this.getContext(), NotificationType.onProfileChanged, new HashMap());
                            NotificationCenter.postNotification(TreadlyProfileEditFragment.this.getContext(), NotificationType.onDailyGoalChanged, new HashMap());
                        }
                        if (TreadlyProfileEditFragment.this.fromLogin) {
                            LoginActivity loginActivity = (LoginActivity) TreadlyProfileEditFragment.this.getActivity();
                            if (loginActivity != null) {
                                loginActivity.toMainActivity();
                                return;
                            }
                            return;
                        } else if (TreadlyProfileEditFragment.this.getActivity() != null) {
                            TreadlyProfileEditFragment.this.getActivity().getSupportFragmentManager().popBackStack();
                            return;
                        } else {
                            return;
                        }
                    }
                    TreadlyProfileEditFragment.this.showBaseAlert("Error", "Failed to save profile.");
                }
            });
        }
    }

    void showAgeRequirementError() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.getDefault(), "You must be at least %d years of age or older", 13));
        sb.append(this.fromLogin ? " to register" : "");
        showBaseAlert("Error", sb.toString());
    }

    void setupDefaultProfile() {
        this.profile = new UserProfileInfo("", 30, 128.0d, 5.7d, "unspecified", "", "", UserDailyGoalType.calories, UserDailyGoalType.none, 2000, -1, 1000, -1, 5.0d, -1.0d, 3600.0d, -1.0d, 0, 0, Utils.DOUBLE_EPSILON, 1985, TimeZone.getDefault().toString(), DistanceUnits.MI, "");
    }

    void getProfile() {
        if (this.profile != null) {
            return;
        }
        showLoading();
        TreadlyServiceManager.getInstance().getUserProfileInfo(this.userId, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileEditFragment.6
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onUserProfile(String str, UserProfileInfo userProfileInfo) {
                TreadlyProfileEditFragment.this.profile = userProfileInfo;
                TreadlyProfileEditFragment.this.initialDailyCaloriesGoal = userProfileInfo.settingCaloriesGoal;
                TreadlyProfileEditFragment.this.initialDailyStepsGoal = userProfileInfo.settingStepsGoal;
                TreadlyProfileEditFragment.this.initialDailyDistanceGoal = userProfileInfo.settingDistanceGoal;
                TreadlyProfileEditFragment.this.initialDailyDurationGoal = userProfileInfo.settingDurationGoal;
                TreadlyProfileEditFragment.this.initialGoalType = userProfileInfo.settingDailyGoalType;
                TreadlyProfileEditFragment.this.initialUnits = userProfileInfo.units;
                TreadlyProfileEditFragment.this.updateView();
            }
        });
    }

    void updateView() {
        dismissLoading();
        int indexOf = this.ageValueList.indexOf(Integer.valueOf(this.profile.birthdateYear));
        if (indexOf == -1) {
            indexOf = this.ageValueList.indexOf(1985);
        }
        this.datePicker.setValue(indexOf + 1900);
        this.ageSelectedIndex = indexOf;
        int indexOf2 = this.weightValueList.indexOf(Double.valueOf(this.profile.weight));
        if (indexOf2 == -1) {
            indexOf2 = this.weightValueList.indexOf(Double.valueOf(1.0d));
        }
        this.weightPicker.setValue(indexOf2);
        this.weightSelectedIndex = indexOf2;
        int indexOf3 = this.heightValueList.indexOf(Double.valueOf(this.profile.height));
        if (indexOf3 == -1) {
            indexOf3 = this.heightValueList.indexOf(Double.valueOf(1.0d));
        }
        this.heightPicker.setValue(indexOf3);
        this.heightSelectedIndex = indexOf3;
        if (this.profile != null) {
            this.isMale = this.profile.gender.equals("male");
            if (this.isMale) {
                this.genderFemaleButton.setBackgroundResource(R.drawable.female_button_not_pressed);
                this.genderMaleButton.setBackgroundResource(R.drawable.male_button_pressed);
            } else {
                this.genderMaleButton.setBackgroundResource(R.drawable.male_button_not_pressed);
                this.genderFemaleButton.setBackgroundResource(R.drawable.female_button_pressed);
            }
            this.units = this.profile.units;
            if (this.units == DistanceUnits.MI) {
                this.unitsMIButton.setBackgroundResource(R.drawable.miles_button_pressed);
                this.unitsKMButton.setBackgroundResource(R.drawable.kilometers_button_not_pressed);
            } else {
                this.unitsMIButton.setBackgroundResource(R.drawable.miles_button_not_pressed);
                this.unitsKMButton.setBackgroundResource(R.drawable.kilometers_button_pressed);
            }
            this.dailyGoalCalories = this.profile.settingCaloriesGoal;
            this.dailyGoalSteps = this.profile.settingStepsGoal;
            this.dailyGoalDistance = this.profile.settingDistanceGoal;
            this.displayedDailyGoalDistance = this.dailyGoalDistance * (this.units == DistanceUnits.KM ? 1.609344d : 1.0d);
            this.dailyGoalDuration = this.profile.settingDurationGoal;
            this.dailyGoalType = this.profile.settingDailyGoalType;
            setDailyGoalType();
            return;
        }
        this.isMale = false;
        this.units = DistanceUnits.MI;
        this.dailyGoalCalories = 0;
        this.dailyGoalSteps = 0;
        this.dailyGoalDistance = Utils.DOUBLE_EPSILON;
        this.displayedDailyGoalDistance = Utils.DOUBLE_EPSILON;
        this.dailyGoalDuration = Utils.DOUBLE_EPSILON;
        this.dailyGoalType = UserDailyGoalType.none;
    }

    int getAgeFromBirthYear(int i) {
        return Calendar.getInstance().get(1) - i;
    }

    void setDailyGoalType() {
        Button button = this.dailyGoalCaloriesButton;
        Resources resources = getResources();
        UserDailyGoalType userDailyGoalType = this.dailyGoalType;
        UserDailyGoalType userDailyGoalType2 = UserDailyGoalType.calories;
        int i = R.color.dark_7;
        button.setBackgroundTintList(resources.getColorStateList(userDailyGoalType == userDailyGoalType2 ? R.color.accent_2 : R.color.dark_7, null));
        this.dailyGoalStepsButton.setBackgroundTintList(getResources().getColorStateList(this.dailyGoalType == UserDailyGoalType.steps ? R.color.accent_2 : R.color.dark_7, null));
        this.dailyGoalDistanceButton.setBackgroundTintList(getResources().getColorStateList(this.dailyGoalType == UserDailyGoalType.distance ? R.color.accent_2 : R.color.dark_7, null));
        Button button2 = this.dailyGoalDurationButton;
        Resources resources2 = getResources();
        if (this.dailyGoalType == UserDailyGoalType.duration) {
            i = R.color.accent_2;
        }
        button2.setBackgroundTintList(resources2.getColorStateList(i, null));
        switch (this.dailyGoalType) {
            case calories:
                this.dailyGoalLabel.setText(R.string.profile_goal_calories);
                this.dailyGoalText.setVisibility(0);
                break;
            case steps:
                this.dailyGoalLabel.setText(R.string.profile_goal_steps);
                this.dailyGoalText.setVisibility(0);
                break;
            case distance:
                if (this.units == DistanceUnits.MI) {
                    this.dailyGoalLabel.setText(R.string.profile_goal_distance_miles);
                } else if (this.units == DistanceUnits.KM) {
                    this.dailyGoalLabel.setText(R.string.profile_goal_distance_kilometers);
                } else {
                    this.dailyGoalLabel.setText(R.string.profile_goal_distance);
                }
                this.dailyGoalText.setVisibility(0);
                break;
            case duration:
                this.dailyGoalLabel.setText(R.string.profile_goal_duration);
                this.dailyGoalText.setVisibility(0);
                break;
            default:
                this.dailyGoalText.setVisibility(4);
                break;
        }
        setDailyGoalTitle();
    }

    void setDailyGoalTitle() {
        switch (this.dailyGoalType) {
            case calories:
                this.dailyGoalText.setText(String.format(Locale.getDefault(), "%,d", Integer.valueOf(this.dailyGoalCalories)));
                return;
            case steps:
                this.dailyGoalText.setText(String.format(Locale.getDefault(), "%,d", Integer.valueOf(this.dailyGoalSteps)));
                return;
            case distance:
                DecimalFormat decimalFormat = new DecimalFormat();
                decimalFormat.setMinimumFractionDigits(1);
                decimalFormat.setMaximumFractionDigits(2);
                this.dailyGoalText.setText(decimalFormat.format(this.displayedDailyGoalDistance));
                return;
            case duration:
                this.dailyGoalText.setText(String.format(Locale.getDefault(), "%,d", Integer.valueOf(((int) this.dailyGoalDuration) / 60)));
                return;
            default:
                this.dailyGoalText.setVisibility(4);
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDailyGoalTextPressed() {
        switch (this.dailyGoalType) {
            case calories:
                displayCaloriesPicker();
                return;
            case steps:
                displayStepsPicker();
                return;
            case distance:
                displayDistancePicker();
                return;
            case duration:
                displayDurationPicker();
                return;
            default:
                return;
        }
    }

    private void displayCaloriesPicker() {
        String[] strArr = new String[PointerIconCompat.TYPE_CONTEXT_MENU];
        int i = 0;
        while (i < 1001) {
            int i2 = i + 1;
            strArr[i] = String.format(Locale.getDefault(), "%,d", Integer.valueOf(i2 * 5));
            i = i2;
        }
        this.dailyGoalPicker.setMaxValue(1000);
        this.dailyGoalPicker.setMinValue(1);
        this.dailyGoalPicker.setWrapSelectorWheel(false);
        this.dailyGoalPicker.setDisplayedValues(strArr);
        this.dailyGoalPicker.setValue(this.dailyGoalCalories / 5);
        this.dailyGoalPicker.setDescendantFocusability(393216);
        this.dailyGoalPickerTitleHolder.setVisibility(0);
        this.dailyGoalPickerTitle.setText(R.string.profile_picker_title_calories);
        this.dailyGoalPicker.setVisibility(0);
        this.dailyGoalUpdateButton.setVisibility(0);
        this.dailyGoalBlur.setVisibility(0);
    }

    private void displayStepsPicker() {
        String[] strArr = new String[PointerIconCompat.TYPE_CONTEXT_MENU];
        int i = 0;
        while (i < 1001) {
            int i2 = i + 1;
            strArr[i] = String.format(Locale.getDefault(), "%,d", Integer.valueOf(i2 * 100));
            i = i2;
        }
        this.dailyGoalPicker.setMaxValue(1000);
        this.dailyGoalPicker.setMinValue(1);
        this.dailyGoalPicker.setWrapSelectorWheel(false);
        this.dailyGoalPicker.setDisplayedValues(strArr);
        this.dailyGoalPicker.setValue(this.dailyGoalSteps / 100);
        this.dailyGoalPicker.setDescendantFocusability(393216);
        this.dailyGoalPickerTitleHolder.setVisibility(0);
        this.dailyGoalPickerTitle.setText(R.string.profile_picker_title_steps);
        this.dailyGoalPicker.setVisibility(0);
        this.dailyGoalUpdateButton.setVisibility(0);
        this.dailyGoalBlur.setVisibility(0);
    }

    private void displayDistancePicker() {
        int i = ((int) ((this.units == DistanceUnits.KM ? 32.0f : 20.0f) / 0.1f)) + 1;
        String[] strArr = new String[i];
        int i2 = 0;
        while (i2 < i) {
            int i3 = i2 + 1;
            strArr[i2] = String.format(Locale.getDefault(), "%.1f", Float.valueOf(i3 * 0.1f));
            i2 = i3;
        }
        this.dailyGoalPicker.setMaxValue(i - 1);
        this.dailyGoalPicker.setMinValue(1);
        this.dailyGoalPicker.setWrapSelectorWheel(false);
        this.dailyGoalPicker.setDisplayedValues(strArr);
        this.dailyGoalPicker.setValue((int) (this.displayedDailyGoalDistance / 0.10000000149011612d));
        this.dailyGoalPicker.setDescendantFocusability(393216);
        this.dailyGoalPickerTitleHolder.setVisibility(0);
        this.dailyGoalPickerTitle.setText(R.string.profile_picker_title_distance);
        this.dailyGoalPicker.setVisibility(0);
        this.dailyGoalUpdateButton.setVisibility(0);
        this.dailyGoalBlur.setVisibility(0);
    }

    private void displayDurationPicker() {
        String[] strArr = new String[289];
        int i = 0;
        while (i < 289) {
            int i2 = i + 1;
            strArr[i] = String.format(Locale.getDefault(), "%,d", Integer.valueOf(i2 * 5));
            i = i2;
        }
        this.dailyGoalPicker.setMaxValue(288);
        this.dailyGoalPicker.setMinValue(1);
        this.dailyGoalPicker.setWrapSelectorWheel(false);
        this.dailyGoalPicker.setDisplayedValues(strArr);
        this.dailyGoalPicker.setValue(((int) this.dailyGoalDuration) / GenericDraweeHierarchyBuilder.DEFAULT_FADE_DURATION);
        this.dailyGoalPicker.setDescendantFocusability(393216);
        this.dailyGoalPickerTitleHolder.setVisibility(0);
        this.dailyGoalPickerTitle.setText(R.string.profile_picker_title_steps);
        this.dailyGoalPicker.setVisibility(0);
        this.dailyGoalUpdateButton.setVisibility(0);
        this.dailyGoalBlur.setVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissDailyGoalPicker() {
        this.dailyGoalBlur.setVisibility(8);
        this.dailyGoalPickerTitleHolder.setVisibility(8);
        this.dailyGoalPicker.setVisibility(8);
        this.dailyGoalUpdateButton.setVisibility(8);
    }

    private void hideKeyboard(View view, Context context) {
        ((InputMethodManager) context.getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
