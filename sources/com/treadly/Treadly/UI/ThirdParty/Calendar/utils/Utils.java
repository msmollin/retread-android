package com.treadly.Treadly.UI.ThirdParty.Calendar.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/* loaded from: classes2.dex */
public class Utils {
    public static int getScreenWidth(Context context) {
        return getPointSize(context).x;
    }

    public static int getScreenHeight(Context context) {
        return getPointSize(context).y;
    }

    private static Point getPointSize(Context context) {
        Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        return point;
    }
}
