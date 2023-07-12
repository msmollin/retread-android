package com.treadly.Treadly.UI.Util;

import android.app.Activity;
import androidx.fragment.app.FragmentActivity;

/* loaded from: classes2.dex */
public class ActivityUtil {
    public static void runOnUiThread(Activity activity, Runnable runnable) {
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(runnable);
    }

    public static void popBackStackImmediate(FragmentActivity fragmentActivity) {
        if (fragmentActivity == null) {
            return;
        }
        fragmentActivity.getSupportFragmentManager().popBackStackImmediate();
    }
}
