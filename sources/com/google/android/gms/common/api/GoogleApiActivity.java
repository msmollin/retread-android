package com.google.android.gms.common.api;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import com.facebook.internal.NativeProtocol;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.internal.GoogleApiManager;
import com.google.android.gms.common.util.VisibleForTesting;

/* loaded from: classes.dex */
public class GoogleApiActivity extends Activity implements DialogInterface.OnCancelListener {
    @VisibleForTesting
    private int zzct = 0;

    public static PendingIntent zza(Context context, PendingIntent pendingIntent, int i) {
        return PendingIntent.getActivity(context, 0, zza(context, pendingIntent, i, true), 134217728);
    }

    public static Intent zza(Context context, PendingIntent pendingIntent, int i, boolean z) {
        Intent intent = new Intent(context, GoogleApiActivity.class);
        intent.putExtra("pending_intent", pendingIntent);
        intent.putExtra("failing_client_id", i);
        intent.putExtra("notify_manager", z);
        return intent;
    }

    @Override // android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1) {
            boolean booleanExtra = getIntent().getBooleanExtra("notify_manager", true);
            this.zzct = 0;
            setResult(i2, intent);
            if (booleanExtra) {
                GoogleApiManager zzb = GoogleApiManager.zzb(this);
                switch (i2) {
                    case -1:
                        zzb.zzr();
                        break;
                    case 0:
                        zzb.zza(new ConnectionResult(13, null), getIntent().getIntExtra("failing_client_id", -1));
                        break;
                }
            }
        } else if (i == 2) {
            this.zzct = 0;
            setResult(i2, intent);
        }
        finish();
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
        this.zzct = 0;
        setResult(0);
        finish();
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        String str;
        String str2;
        super.onCreate(bundle);
        if (bundle != null) {
            this.zzct = bundle.getInt("resolution");
        }
        if (this.zzct != 1) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                str = "GoogleApiActivity";
                str2 = "Activity started without extras";
            } else {
                PendingIntent pendingIntent = (PendingIntent) extras.get("pending_intent");
                Integer num = (Integer) extras.get(NativeProtocol.BRIDGE_ARG_ERROR_CODE);
                if (pendingIntent == null && num == null) {
                    str = "GoogleApiActivity";
                    str2 = "Activity started without resolution";
                } else if (pendingIntent == null) {
                    GoogleApiAvailability.getInstance().showErrorDialogFragment(this, num.intValue(), 2, this);
                    this.zzct = 1;
                    return;
                } else {
                    try {
                        startIntentSenderForResult(pendingIntent.getIntentSender(), 1, null, 0, 0, 0);
                        this.zzct = 1;
                        return;
                    } catch (IntentSender.SendIntentException e) {
                        Log.e("GoogleApiActivity", "Failed to launch pendingIntent", e);
                    }
                }
            }
            Log.e(str, str2);
            finish();
        }
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle bundle) {
        bundle.putInt("resolution", this.zzct);
        super.onSaveInstanceState(bundle);
    }
}
