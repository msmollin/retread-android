package com.facebook.internal.instrument.threadcheck;

import android.os.Looper;
import android.util.Log;
import androidx.annotation.RestrictTo;
import com.facebook.internal.instrument.InstrumentData;
import java.util.Locale;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* loaded from: classes.dex */
public class ThreadCheckHandler {
    private static final String TAG = ThreadCheckHandler.class.getCanonicalName();
    private static boolean enabled = false;

    private ThreadCheckHandler() {
    }

    public static void enable() {
        enabled = true;
    }

    public static void uiThreadViolationDetected(Class<?> cls, String str, String str2) {
        log("@UiThread", cls, str, str2);
    }

    public static void workerThreadViolationDetected(Class<?> cls, String str, String str2) {
        log("@WorkerThread", cls, str, str2);
    }

    private static void log(String str, Class<?> cls, String str2, String str3) {
        if (enabled) {
            String format = String.format(Locale.US, "%s annotation violation detected in %s.%s%s. Current looper is %s and main looper is %s.", str, cls.getName(), str2, str3, Looper.myLooper(), Looper.getMainLooper());
            Exception exc = new Exception();
            Log.e(TAG, format, exc);
            InstrumentData.Builder.build(exc, InstrumentData.Type.ThreadCheck).save();
        }
    }
}
