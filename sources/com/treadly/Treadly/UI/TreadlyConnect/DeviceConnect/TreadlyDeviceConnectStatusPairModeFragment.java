package com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
import com.treadly.client.lib.sdk.Model.DeviceInfo;

/* loaded from: classes2.dex */
public class TreadlyDeviceConnectStatusPairModeFragment extends BaseFragment {
    public static final String TAG = "DEVICE_CONNECT_STATUS_PAIR_MODE";
    public DeviceInfo.DeviceGen deviceType = DeviceInfo.DeviceGen.gen3;
    TextView directions;
    ImageView treadmillImage;

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_device_connect_status_pair_mode, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        ((ImageButton) view.findViewById(R.id.device_connect_status_pair_mode_dismiss_button)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusPairModeFragment$XZOVf23M-aMu7U6sFa5avPKWQKk
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyDeviceConnectStatusPairModeFragment.this.popBackStack();
            }
        });
        ((Button) view.findViewById(R.id.device_connect_status_pair_mode_close_button)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusPairModeFragment$zgLGpImMSdLlyfa8zxgL79G0rns
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyDeviceConnectStatusPairModeFragment.this.popBackStack();
            }
        });
        this.directions = (TextView) view.findViewById(R.id.device_connect_status_pair_mode_directions_one);
        this.treadmillImage = (ImageView) view.findViewById(R.id.device_connect_status_pair_mode_image);
        switch (this.deviceType) {
            case gen1:
                configureGen1();
                return;
            case gen2:
                configureGen2();
                return;
            case gen3:
                configureGen3();
                return;
            default:
                return;
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        hideBottomNavigation();
    }

    void configureGen3() {
        int color = getResources().getColor(R.color.accent, null);
        String string = getString(R.string.device_connect_status_pair_mode_gen3_directions);
        SpannableString spannableString = new SpannableString(string);
        spannableString.setSpan(new ForegroundColorSpan(color), string.indexOf("PAUSE"), string.indexOf("PAUSE") + "PAUSE".length(), 0);
        spannableString.setSpan(new StyleSpan(1), string.indexOf("PAUSE"), string.indexOf("PAUSE") + "PAUSE".length(), 0);
        this.directions.setText(spannableString, TextView.BufferType.SPANNABLE);
    }

    void configureGen2() {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.treadmillImage.getLayoutParams();
        layoutParams.leftMargin = 0;
        this.treadmillImage.setLayoutParams(layoutParams);
        this.treadmillImage.setScaleType(ImageView.ScaleType.CENTER);
        this.treadmillImage.setImageDrawable(getResources().getDrawable(R.drawable.gen_2_tap_image, null));
    }

    void configureGen1() {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.treadmillImage.getLayoutParams();
        layoutParams.leftMargin = 0;
        this.treadmillImage.setLayoutParams(layoutParams);
        this.treadmillImage.setScaleType(ImageView.ScaleType.CENTER);
        this.treadmillImage.setImageDrawable(getResources().getDrawable(R.drawable.gen_1_tap_image, null));
    }
}
