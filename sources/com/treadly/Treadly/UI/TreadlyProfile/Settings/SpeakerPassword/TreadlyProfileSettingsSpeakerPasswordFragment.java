package com.treadly.Treadly.UI.TreadlyProfile.Settings.SpeakerPassword;

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
import com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventAdapter;
import com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener;
import com.treadly.client.lib.sdk.Listeners.RequestEventAdapter;
import com.treadly.client.lib.sdk.Listeners.RequestEventListener;
import com.treadly.client.lib.sdk.Model.DeviceConnectionEvent;
import com.treadly.client.lib.sdk.Model.DeviceConnectionStatus;
import com.treadly.client.lib.sdk.TreadlyClientLib;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

/* loaded from: classes2.dex */
public class TreadlyProfileSettingsSpeakerPasswordFragment extends BaseFragment {
    ImageButton backArrow;
    String btAudioPassword;
    TextView btAudioPasswordLabel;
    TextView generateText;
    TextView speakerTitle;
    Timer responseTimer = new Timer();
    boolean isSettingPassword = false;
    RequestEventListener requestAdapter = new RequestEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.SpeakerPassword.TreadlyProfileSettingsSpeakerPasswordFragment.6
        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventAdapter, com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestSetBtAudioPassword(boolean z) {
            TreadlyProfileSettingsSpeakerPasswordFragment.this.stopResponseTimer();
            if (z) {
                TreadlyClientLib.shared.getBtAudioPassword();
                TreadlyProfileSettingsSpeakerPasswordFragment.this.startGetPasswordResponseTimer();
                return;
            }
            TreadlyProfileSettingsSpeakerPasswordFragment.this.isSettingPassword = false;
            TreadlyProfileSettingsSpeakerPasswordFragment.this.showAlert("Error", "Error getting Bluetooth audio password.");
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventAdapter, com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestGetBtAudioPAssword(boolean z, int[] iArr) {
            TreadlyProfileSettingsSpeakerPasswordFragment.this.isSettingPassword = false;
            TreadlyProfileSettingsSpeakerPasswordFragment.this.stopResponseTimer();
            if (z && iArr != null) {
                TreadlyProfileSettingsSpeakerPasswordFragment.this.updatePassword(iArr);
            } else {
                TreadlyProfileSettingsSpeakerPasswordFragment.this.showAlert("Error", "Error getting Bluetooth audio password.");
            }
        }
    };
    DeviceConnectionEventListener deviceConnectAdapter = new DeviceConnectionEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.SpeakerPassword.TreadlyProfileSettingsSpeakerPasswordFragment.10
        @Override // com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventAdapter, com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener
        public void onDeviceConnectionChanged(DeviceConnectionEvent deviceConnectionEvent) {
            if ((deviceConnectionEvent.getStatus() == DeviceConnectionStatus.notConnected || deviceConnectionEvent.getStatus() == DeviceConnectionStatus.disconnecting) && TreadlyProfileSettingsSpeakerPasswordFragment.this.getActivity() != null) {
                TreadlyProfileSettingsSpeakerPasswordFragment.this.getActivity().getSupportFragmentManager().popBackStack();
            }
        }
    };

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_profile_settings_bluetooth_audio, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        this.btAudioPasswordLabel = (TextView) view.findViewById(R.id.speaker_password);
        this.generateText = (TextView) view.findViewById(R.id.generate_speaker_password_text);
        this.generateText.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.SpeakerPassword.-$$Lambda$TreadlyProfileSettingsSpeakerPasswordFragment$neyqXaPfOCG9Mj3kOgQ-Fp2Tcxw
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileSettingsSpeakerPasswordFragment.this.generatePassword();
            }
        });
        this.speakerTitle = (TextView) view.findViewById(R.id.nav_title);
        this.speakerTitle.setText("Speaker Password");
        this.backArrow = (ImageButton) view.findViewById(R.id.nav_back_arrow);
        this.backArrow.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.SpeakerPassword.-$$Lambda$TreadlyProfileSettingsSpeakerPasswordFragment$xyXkZjOaReFYkO4ndzPWTismDpw
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileSettingsSpeakerPasswordFragment.this.popBackStack();
            }
        });
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        TreadlyClientLib.shared.addRequestEventListener(this.requestAdapter);
        TreadlyClientLib.shared.addDeviceConnectionEventListener(this.deviceConnectAdapter);
        start();
    }

    byte[] intArraytoByteArray(int[] iArr) {
        byte[] bArr = new byte[iArr.length];
        for (int i = 0; i < iArr.length; i++) {
            bArr[i] = (byte) iArr[i];
        }
        return bArr;
    }

    void setBtAudioText(String str) {
        this.btAudioPassword = str;
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.SpeakerPassword.TreadlyProfileSettingsSpeakerPasswordFragment.1
            @Override // java.lang.Runnable
            public void run() {
                TreadlyProfileSettingsSpeakerPasswordFragment.this.btAudioPasswordLabel.setText(TreadlyProfileSettingsSpeakerPasswordFragment.this.btAudioPassword);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void generatePassword() {
        if (this.btAudioPassword.isEmpty() || this.isSettingPassword) {
            return;
        }
        TreadlyClientLib.shared.setBtAudioPassword(intArraytoByteArray(generateRandomPassword()));
        this.isSettingPassword = true;
        startSetPasswordResponseTimer();
    }

    int[] generateRandomPassword() {
        int[] iArr = new int[6];
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            iArr[i] = random.nextInt(10);
        }
        return iArr;
    }

    void start() {
        setBtAudioText("");
        TreadlyClientLib.shared.getBtAudioPassword();
        startGetPasswordResponseTimer();
    }

    void updatePassword(int[] iArr) {
        String str = "";
        if (iArr == null) {
            return;
        }
        for (int i : iArr) {
            str = str + i;
        }
        setBtAudioText(str);
    }

    void startGetPasswordResponseTimer() {
        stopResponseTimer();
        this.responseTimer = new Timer();
        this.responseTimer.schedule(new TimerTask() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.SpeakerPassword.TreadlyProfileSettingsSpeakerPasswordFragment.2
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                TreadlyProfileSettingsSpeakerPasswordFragment.this.getResponseTimeout();
            }
        }, 5000L);
    }

    void startSetPasswordResponseTimer() {
        stopResponseTimer();
        this.responseTimer = new Timer();
        this.responseTimer.schedule(new TimerTask() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.SpeakerPassword.TreadlyProfileSettingsSpeakerPasswordFragment.3
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                TreadlyProfileSettingsSpeakerPasswordFragment.this.setResponseTimer();
            }
        }, 5000L);
    }

    void stopResponseTimer() {
        this.responseTimer.cancel();
        this.responseTimer.purge();
    }

    void getResponseTimeout() {
        this.isSettingPassword = false;
        showTryAgainAlert("Error", "Error getting Bluetooth audio password.", new Callable<Void>() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.SpeakerPassword.TreadlyProfileSettingsSpeakerPasswordFragment.4
            @Override // java.util.concurrent.Callable
            public Void call() throws Exception {
                TreadlyProfileSettingsSpeakerPasswordFragment.this.start();
                return null;
            }
        });
    }

    void setResponseTimer() {
        this.isSettingPassword = false;
        showTryAgainAlert("Error", "Error getting Bluetooth audio password.", new Callable<Void>() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.SpeakerPassword.TreadlyProfileSettingsSpeakerPasswordFragment.5
            @Override // java.util.concurrent.Callable
            public Void call() throws Exception {
                TreadlyProfileSettingsSpeakerPasswordFragment.this.generatePassword();
                return null;
            }
        });
    }

    void showAlert(String str, String str2) {
        final AlertDialog.Builder neutralButton = new AlertDialog.Builder(getContext()).setTitle(str).setMessage(str2).setNeutralButton("Dismiss", (DialogInterface.OnClickListener) null);
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.SpeakerPassword.TreadlyProfileSettingsSpeakerPasswordFragment.7
            @Override // java.lang.Runnable
            public void run() {
                neutralButton.show();
            }
        });
    }

    void showTryAgainAlert(String str, String str2, final Callable<Void> callable) {
        final AlertDialog.Builder positiveButton = new AlertDialog.Builder(getContext()).setTitle(str).setMessage(str2).setNegativeButton("Dismiss", (DialogInterface.OnClickListener) null).setPositiveButton("Try Again", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.SpeakerPassword.TreadlyProfileSettingsSpeakerPasswordFragment.8
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    callable.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.SpeakerPassword.TreadlyProfileSettingsSpeakerPasswordFragment.9
            @Override // java.lang.Runnable
            public void run() {
                positiveButton.show();
            }
        });
    }
}
