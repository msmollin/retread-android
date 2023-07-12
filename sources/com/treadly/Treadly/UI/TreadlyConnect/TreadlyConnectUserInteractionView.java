package com.treadly.Treadly.UI.TreadlyConnect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import com.github.mikephil.charting.utils.Utils;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.R;
import com.treadly.client.lib.sdk.Model.UserInteractionStatus;
import com.treadly.client.lib.sdk.Model.UserInteractionSteps;
import com.treadly.client.lib.sdk.TreadlyClientLib;
import java.util.Locale;

/* loaded from: classes2.dex */
public class TreadlyConnectUserInteractionView extends ConstraintLayout {
    private TextView centerStrideTextView;
    private View centerStrideView;
    int constantEndPosition;
    private TextView constantEndTextView;
    private View constantEndView;
    int constantStartPosition;
    private TextView constantStartTextView;
    private View constantStartView;
    private Context context;
    private TextView currentTextView;
    public String deviceAddress;
    private Button factoryReset;
    int locationIncrements;
    int previousSequence;
    private TextView sequenceErrorCountTextView;
    int sequenceErrors;
    private TextView sequenceTextView;
    private ToggleButton startCaptureButton;
    int stepOnePosition;
    private TextView stepOneTextView;
    private View stepOneView;
    int stepOneWidth;
    int stepTwoPosition;
    private TextView stepTwoTextView;
    private View stepTwoView;
    int stepTwoWidth;
    int stridePosition;
    private FrameLayout treadmillView;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$null$1(DialogInterface dialogInterface, int i) {
    }

    public TreadlyConnectUserInteractionView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.locationIncrements = 112;
        this.context = context;
        inflate();
        initAdminViews();
    }

    private void inflate() {
        inflate(this.context, R.layout.view_treadly_connect_user_interaction, this);
    }

    private void initAdminViews() {
        this.sequenceTextView = (TextView) findViewById(R.id.sequence);
        this.sequenceErrorCountTextView = (TextView) findViewById(R.id.sequence_error);
        this.stepOneTextView = (TextView) findViewById(R.id.step_one);
        this.stepTwoTextView = (TextView) findViewById(R.id.step_two);
        this.constantStartTextView = (TextView) findViewById(R.id.constant_start);
        this.constantEndTextView = (TextView) findViewById(R.id.constant_end);
        this.centerStrideTextView = (TextView) findViewById(R.id.center_stride);
        this.stepOneView = findViewById(R.id.step_one_view);
        this.stepTwoView = findViewById(R.id.step_two_view);
        this.constantStartView = findViewById(R.id.constant_zone_start_view);
        this.constantEndView = findViewById(R.id.constant_zone_end_view);
        this.centerStrideView = findViewById(R.id.center_stride_view);
        this.currentTextView = (TextView) findViewById(R.id.current_text_view);
        this.stepOneView.setBackgroundResource(R.color.red);
        this.stepTwoView.setBackgroundResource(R.color.blue);
        this.startCaptureButton = (ToggleButton) findViewById(R.id.start_capture_button);
        this.startCaptureButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectUserInteractionView.1
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    compoundButton.setTextColor(ContextCompat.getColor(TreadlyConnectUserInteractionView.this.getContext(), R.color.white));
                    compoundButton.setBackgroundColor(ContextCompat.getColor(TreadlyConnectUserInteractionView.this.getContext(), R.color.com_facebook_blue));
                    TreadlyClientLib.shared.setEnableUserInteraction(true);
                    TreadlyClientLib.shared.getUserInteractionStatus();
                    return;
                }
                compoundButton.setTextColor(ContextCompat.getColor(TreadlyConnectUserInteractionView.this.getContext(), R.color.com_facebook_blue));
                compoundButton.setBackgroundColor(ContextCompat.getColor(TreadlyConnectUserInteractionView.this.getContext(), R.color.transparent));
                TreadlyClientLib.shared.setEnableUserInteraction(false);
            }
        });
        this.treadmillView = (FrameLayout) findViewById(R.id.step_layout);
        this.factoryReset = (Button) findViewById(R.id.user_iteraction_factory_reset);
        this.factoryReset.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyConnectUserInteractionView$wWS2Ch1vI977mLmADGr3l-Tm1I8
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                new AlertDialog.Builder(r0.getContext()).setTitle("Factory Reset").setMessage("Are you sure you want to factory reset the device").setPositiveButton("yes", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyConnectUserInteractionView$pfH0qASOpQkb6NSybL98mc2RL9w
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        TreadlyConnectUserInteractionView.lambda$null$0(TreadlyConnectUserInteractionView.this, dialogInterface, i);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyConnectUserInteractionView$H6zQCG6VYq_p_RobEbTq8SPBEKA
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        TreadlyConnectUserInteractionView.lambda$null$1(dialogInterface, i);
                    }
                }).show();
            }
        });
    }

    public static /* synthetic */ void lambda$null$0(TreadlyConnectUserInteractionView treadlyConnectUserInteractionView, DialogInterface dialogInterface, int i) {
        TreadlyClientLib.shared.factoryReset();
        TreadlyServiceManager.getInstance().resetSingleUserMode(treadlyConnectUserInteractionView.deviceAddress, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectUserInteractionView.2
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onSuccess(String str) {
            }
        });
    }

    public void handleDeviceConnected() {
        reset();
        TreadlyClientLib.shared.getUserInteractionStatus();
    }

    public void handleDeviceDisconnected() {
        reset();
    }

    private void reset() {
        this.stepOneTextView.setText("");
        this.stepTwoTextView.setText("");
        this.sequenceTextView.setText("");
        this.sequenceErrorCountTextView.setText("");
        this.constantStartTextView.setText("");
        this.constantEndTextView.setText("");
        this.centerStrideTextView.setText("");
        this.currentTextView.setText("");
        this.stepOnePosition = 0;
        this.stepOneWidth = 0;
        setLayoutParams(this.stepOneView, this.stepOneWidth, this.stepOnePosition);
        this.stepTwoPosition = 0;
        this.stepTwoWidth = 0;
        setLayoutParams(this.stepTwoView, this.stepTwoWidth, this.stepTwoPosition);
        this.constantStartPosition = 0;
        setLayoutParams(this.constantStartView, 1, this.constantStartPosition);
        this.constantEndPosition = 0;
        setLayoutParams(this.constantEndView, 1, this.constantEndPosition);
        this.stridePosition = 0;
        setLayoutParams(this.centerStrideView, 1, this.stridePosition);
        this.startCaptureButton.setChecked(false);
        this.previousSequence = 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void updateSteps(UserInteractionSteps userInteractionSteps) {
        if (userInteractionSteps.sequence > 0 && userInteractionSteps.sequence - 1 != this.previousSequence) {
            this.sequenceErrors++;
        }
        this.previousSequence = userInteractionSteps.sequence;
        this.sequenceTextView.setText(String.valueOf(userInteractionSteps.sequence));
        this.sequenceErrorCountTextView.setText(String.valueOf(this.sequenceErrors));
        this.stepOneTextView.setText("End: " + String.valueOf(userInteractionSteps.stepOne.endLocation) + " : Start " + String.valueOf(userInteractionSteps.stepOne.startLocation));
        this.stepOneWidth = (int) getWidthForLocation(userInteractionSteps.stepOne.startLocation, userInteractionSteps.stepOne.endLocation);
        this.stepOnePosition = (int) getDistanceForLocation(userInteractionSteps.stepOne.endLocation);
        setLayoutParams(this.stepOneView, this.stepOneWidth, this.stepOnePosition);
        this.stepTwoTextView.setText("End: " + String.valueOf(userInteractionSteps.stepTwo.endLocation) + " : Start " + String.valueOf(userInteractionSteps.stepTwo.startLocation));
        this.stepTwoWidth = (int) getWidthForLocation(userInteractionSteps.stepTwo.startLocation, userInteractionSteps.stepTwo.endLocation);
        this.stepTwoPosition = (int) getDistanceForLocation(userInteractionSteps.stepTwo.endLocation);
        setLayoutParams(this.stepTwoView, this.stepTwoWidth, this.stepTwoPosition);
        this.stridePosition = (int) getDistanceForLocation(userInteractionSteps.centerStride);
        this.centerStrideTextView.setText(intToString(userInteractionSteps.centerStride));
        setLayoutParams(this.centerStrideView, 1, this.stridePosition);
    }

    public void updateCurrent(int i) {
        if (this.currentTextView != null) {
            this.currentTextView.setText(String.format(Locale.getDefault(), "%d", Integer.valueOf(i)));
        }
    }

    private String intToString(int i) {
        return String.format(Locale.getDefault(), "%d", Integer.valueOf(i));
    }

    private double getDistanceForLocation(int i) {
        double width = this.treadmillView.getWidth();
        return width - ((this.locationIncrements > 0 ? width / this.locationIncrements : Utils.DOUBLE_EPSILON) * i);
    }

    private double getWidthForLocation(int i, int i2) {
        if (i2 < i) {
            return Utils.DOUBLE_EPSILON;
        }
        double abs = Math.abs(i2 - i) * (this.locationIncrements > 0 ? this.treadmillView.getWidth() / this.locationIncrements : 0.0d);
        if (abs == Utils.DOUBLE_EPSILON) {
            return 1.0d;
        }
        return abs;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void updateZones(UserInteractionStatus userInteractionStatus) {
        this.startCaptureButton.setChecked(userInteractionStatus.enabled);
        this.locationIncrements = userInteractionStatus.maxZonePosition;
        this.constantStartTextView.setText(String.valueOf(userInteractionStatus.constantZoneStart));
        this.constantEndTextView.setText(String.valueOf(userInteractionStatus.constantZoneEnd));
        this.constantStartPosition = (int) getDistanceForLocation(userInteractionStatus.constantZoneStart);
        setLayoutParams(this.constantStartView, 1, this.constantStartPosition);
        this.constantEndPosition = (int) getDistanceForLocation(userInteractionStatus.constantZoneEnd);
        setLayoutParams(this.constantEndView, 1, this.constantEndPosition);
    }

    private void setLayoutParams(View view, int i, int i2) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        layoutParams.setMarginStart(i2);
        layoutParams.width = i;
        view.setLayoutParams(layoutParams);
    }
}
