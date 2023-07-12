package com.treadly.client.lib.sdk.Managers;

import android.app.Activity;
import android.content.SharedPreferences;
import com.treadly.client.lib.sdk.Model.VersionInfo;
import java.io.PrintStream;
import java.util.Random;

/* loaded from: classes2.dex */
public class SharedPreferences {
    private static int AUTHENTICATE_SECRET_LENGTH = 8;
    private static final String SHARED_PREFERENCES_KEY = "Share";
    public static final SharedPreferences shared = new SharedPreferences();
    private android.content.SharedPreferences preferences;

    private SharedPreferences() {
    }

    public void init(Activity activity) {
        this.preferences = activity.getSharedPreferences(SHARED_PREFERENCES_KEY, 0);
    }

    public void saveAuthenticateSecret(String str, byte[] bArr) {
        if (str == null || bArr == null) {
            return;
        }
        SharedPreferences.Editor edit = this.preferences.edit();
        for (int i = 0; i < bArr.length; i++) {
            edit.putInt(str + "_authenticate_secret_" + i, bArr[i]);
        }
        edit.apply();
    }

    public byte[] getAuthenticateSecret(String str) {
        byte[] bArr = null;
        if (str == null) {
            return null;
        }
        byte[] bArr2 = new byte[AUTHENTICATE_SECRET_LENGTH];
        int i = 0;
        while (true) {
            if (i >= AUTHENTICATE_SECRET_LENGTH) {
                bArr = bArr2;
                break;
            }
            if (!this.preferences.contains(str + "_authenticate_secret_" + i)) {
                break;
            }
            bArr2[i] = (byte) this.preferences.getInt(str + "_authenticate_secret_" + i, 0);
            i++;
        }
        if (bArr != null) {
            return bArr;
        }
        Random random = new Random();
        byte[] bArr3 = new byte[AUTHENTICATE_SECRET_LENGTH];
        random.nextBytes(bArr3);
        saveAuthenticateSecret(str, bArr3);
        return bArr3;
    }

    public void saveMacAddress(String str, String str2) {
        if (str == null || str2 == null) {
            return;
        }
        SharedPreferences.Editor edit = this.preferences.edit();
        edit.putString(str + "_mac_address", str2);
        edit.apply();
    }

    public String getMacAddress(String str) {
        if (str == null) {
            return null;
        }
        android.content.SharedPreferences sharedPreferences = this.preferences;
        String string = sharedPreferences.getString(str + "_mac_address", null);
        PrintStream printStream = System.out;
        printStream.println("LGK :: GET MAC ADDRESS: " + string);
        return string;
    }

    public void storeOtaPreUpdateVersion(String str, VersionInfo versionInfo) {
        if (str == null || str.length() < 6) {
            return;
        }
        String substring = str.substring(str.length() - 6, str.length());
        SharedPreferences.Editor edit = this.preferences.edit();
        if (versionInfo != null && versionInfo.getVersion() != null) {
            edit.putString(substring + "_ota_update_version", versionInfo.getVersion());
        } else {
            edit.remove(substring + "_ota_update_version");
        }
        edit.apply();
    }

    public VersionInfo getOtaPreUpdateVersion(String str) {
        if (str == null || str.length() < 6) {
            return null;
        }
        String substring = str.substring(str.length() - 6, str.length());
        android.content.SharedPreferences sharedPreferences = this.preferences;
        String string = sharedPreferences.getString(substring + "_ota_update_version", null);
        if (string != null) {
            return new VersionInfo(string);
        }
        return null;
    }
}
