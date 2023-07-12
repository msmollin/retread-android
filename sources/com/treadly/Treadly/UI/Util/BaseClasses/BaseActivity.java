package com.treadly.Treadly.UI.Util.BaseClasses;

import android.content.DialogInterface;
import androidx.appcompat.app.AppCompatActivity;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.Util.LoadingDialog;

/* loaded from: classes2.dex */
public class BaseActivity extends AppCompatActivity {
    LoadingDialog loadDialog = null;

    public void showLoadingDialog(boolean z) {
        if (this.loadDialog == null) {
            this.loadDialog = new LoadingDialog(this, R.style.myCommonDimDialog);
        }
        this.loadDialog.setCancelable(!z);
        this.loadDialog.setInverseBackgroundForced(false);
        this.loadDialog.setCanceledOnTouchOutside(!z);
        this.loadDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.treadly.Treadly.UI.Util.BaseClasses.BaseActivity.1
            @Override // android.content.DialogInterface.OnCancelListener
            public void onCancel(DialogInterface dialogInterface) {
                dialogInterface.dismiss();
            }
        });
        this.loadDialog.show();
    }

    public void closeLoadingDialog() {
        if (this.loadDialog != null) {
            this.loadDialog.dismiss();
        }
    }
}
