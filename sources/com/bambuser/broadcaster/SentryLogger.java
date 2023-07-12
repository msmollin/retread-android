package com.bambuser.broadcaster;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import java.lang.ref.WeakReference;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class SentryLogger {
    private static final String LOGTAG = "SentryLogger";
    private static final String SENTRY_KEY = "80464d18c6284fe79d6503b1189bd795";
    private static final String SENTRY_SECRET = "2f6a9c58bd554a638a5a1c4055ef0371";
    private static final String SENTRY_URL = "https://vsentry.bambuser.com/api/116/store/";
    private static WeakReference<Context> sContextRef;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public enum Level {
        FATAL,
        ERROR,
        WARNING,
        INFO,
        DEBUG
    }

    SentryLogger() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void initLogger(Context context) {
        sContextRef = new WeakReference<>(context.getApplicationContext());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Type inference failed for: r6v0, types: [com.bambuser.broadcaster.SentryLogger$1] */
    public static void asyncMessage(final String str, final Level level, final JSONObject jSONObject, final Throwable th) {
        new Thread("SentryLoggerThread") { // from class: com.bambuser.broadcaster.SentryLogger.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                Pair<Integer, String> postMessage = SentryLogger.postMessage(str, level, jSONObject, th);
                Log.i(SentryLogger.LOGTAG, "sentry response code " + postMessage.first);
            }
        }.start();
    }

    /* JADX WARN: Can't wrap try/catch for region: R(15:1|2|3|(1:5)(1:42)|(1:7)|8|(12:28|29|(4:31|32|33|34)|37|38|11|12|13|15|(1:17)|19|20)|10|11|12|13|15|(0)|19|20) */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0135  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0136  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0139  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x013c  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0140  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0143  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0167 A[Catch: Exception -> 0x016c, TRY_LEAVE, TryCatch #0 {Exception -> 0x016c, blocks: (B:19:0x011c, B:20:0x0132, B:27:0x0145, B:29:0x0167), top: B:36:0x011c }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static android.util.Pair<java.lang.Integer, java.lang.String> postMessage(java.lang.String r18, com.bambuser.broadcaster.SentryLogger.Level r19, org.json.JSONObject r20, java.lang.Throwable r21) {
        /*
            Method dump skipped, instructions count: 442
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bambuser.broadcaster.SentryLogger.postMessage(java.lang.String, com.bambuser.broadcaster.SentryLogger$Level, org.json.JSONObject, java.lang.Throwable):android.util.Pair");
    }
}
