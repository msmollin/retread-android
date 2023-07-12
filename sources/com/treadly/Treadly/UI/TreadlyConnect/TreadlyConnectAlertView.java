package com.treadly.Treadly.UI.TreadlyConnect;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.treadly.Treadly.R;

/* loaded from: classes2.dex */
public class TreadlyConnectAlertView extends Dialog implements View.OnClickListener {
    Context context;
    public Dialog dialog;
    ImageView dismissButton;
    ImageView imageView;
    public boolean isEmergency;
    TextView messageLabel;
    TextView titleLabel;

    public TreadlyConnectAlertView(Context context) {
        super(context);
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        if (this.isEmergency) {
            setContentView(R.layout.emergency_alert);
            this.imageView = (ImageView) findViewById(R.id.emergency_dismiss_button);
        } else {
            setContentView(R.layout.pause_alert);
            this.imageView = (ImageView) findViewById(R.id.pause_dismiss_button);
        }
        this.imageView.setOnClickListener(this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.emergency_dismiss_button) {
            dismiss();
        } else if (id != R.id.pause_dismiss_button) {
        } else {
            dismiss();
        }
    }
}
