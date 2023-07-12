package com.facebook.appevents.codeless;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.VisibleForTesting;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.codeless.ViewIndexingTrigger;
import com.facebook.appevents.codeless.internal.Constants;
import com.facebook.appevents.internal.AppEventUtility;
import com.facebook.internal.AttributionIdentifiers;
import com.facebook.internal.FetchedAppSettings;
import com.facebook.internal.FetchedAppSettingsManager;
import com.facebook.internal.Utility;
import com.facebook.internal.instrument.crashshield.CrashShieldHandler;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONArray;
import org.json.JSONObject;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* loaded from: classes.dex */
public final class CodelessManager {
    @Nullable
    private static String deviceSessionID;
    @Nullable
    private static SensorManager sensorManager;
    @Nullable
    private static ViewIndexer viewIndexer;
    private static final ViewIndexingTrigger viewIndexingTrigger = new ViewIndexingTrigger();
    private static final AtomicBoolean isCodelessEnabled = new AtomicBoolean(true);
    private static final AtomicBoolean isAppIndexingEnabled = new AtomicBoolean(false);
    private static volatile Boolean isCheckingSession = false;
    private static CodelessSessionChecker codelessSessionChecker = new CodelessSessionChecker() { // from class: com.facebook.appevents.codeless.CodelessManager.1
        @Override // com.facebook.appevents.codeless.CodelessManager.CodelessSessionChecker
        public void checkCodelessSession(String str) {
            CodelessManager.checkCodelessSession(str);
        }
    };

    @VisibleForTesting
    /* loaded from: classes.dex */
    public interface CodelessSessionChecker {
        void checkCodelessSession(String str);
    }

    static boolean isDebugOnEmulator() {
        return CrashShieldHandler.isObjectCrashing(CodelessManager.class) ? false : false;
    }

    static /* synthetic */ CodelessSessionChecker access$000() {
        if (CrashShieldHandler.isObjectCrashing(CodelessManager.class)) {
            return null;
        }
        try {
            return codelessSessionChecker;
        } catch (Throwable th) {
            CrashShieldHandler.handleThrowable(th, CodelessManager.class);
            return null;
        }
    }

    static /* synthetic */ AtomicBoolean access$100() {
        if (CrashShieldHandler.isObjectCrashing(CodelessManager.class)) {
            return null;
        }
        try {
            return isAppIndexingEnabled;
        } catch (Throwable th) {
            CrashShieldHandler.handleThrowable(th, CodelessManager.class);
            return null;
        }
    }

    static /* synthetic */ String access$202(String str) {
        if (CrashShieldHandler.isObjectCrashing(CodelessManager.class)) {
            return null;
        }
        try {
            deviceSessionID = str;
            return str;
        } catch (Throwable th) {
            CrashShieldHandler.handleThrowable(th, CodelessManager.class);
            return null;
        }
    }

    static /* synthetic */ ViewIndexer access$300() {
        if (CrashShieldHandler.isObjectCrashing(CodelessManager.class)) {
            return null;
        }
        try {
            return viewIndexer;
        } catch (Throwable th) {
            CrashShieldHandler.handleThrowable(th, CodelessManager.class);
            return null;
        }
    }

    static /* synthetic */ Boolean access$402(Boolean bool) {
        if (CrashShieldHandler.isObjectCrashing(CodelessManager.class)) {
            return null;
        }
        try {
            isCheckingSession = bool;
            return bool;
        } catch (Throwable th) {
            CrashShieldHandler.handleThrowable(th, CodelessManager.class);
            return null;
        }
    }

    public static void onActivityResumed(Activity activity) {
        if (CrashShieldHandler.isObjectCrashing(CodelessManager.class)) {
            return;
        }
        try {
            if (isCodelessEnabled.get()) {
                CodelessMatcher.getInstance().add(activity);
                Context applicationContext = activity.getApplicationContext();
                final String applicationId = FacebookSdk.getApplicationId();
                final FetchedAppSettings appSettingsWithoutQuery = FetchedAppSettingsManager.getAppSettingsWithoutQuery(applicationId);
                if ((appSettingsWithoutQuery != null && appSettingsWithoutQuery.getCodelessEventsEnabled()) || isDebugOnEmulator()) {
                    sensorManager = (SensorManager) applicationContext.getSystemService("sensor");
                    if (sensorManager == null) {
                        return;
                    }
                    Sensor defaultSensor = sensorManager.getDefaultSensor(1);
                    viewIndexer = new ViewIndexer(activity);
                    viewIndexingTrigger.setOnShakeListener(new ViewIndexingTrigger.OnShakeListener() { // from class: com.facebook.appevents.codeless.CodelessManager.2
                        @Override // com.facebook.appevents.codeless.ViewIndexingTrigger.OnShakeListener
                        public void onShake() {
                            boolean z = FetchedAppSettings.this != null && FetchedAppSettings.this.getCodelessEventsEnabled();
                            boolean z2 = FacebookSdk.getCodelessSetupEnabled();
                            if (z && z2) {
                                CodelessManager.access$000().checkCodelessSession(applicationId);
                            }
                        }
                    });
                    sensorManager.registerListener(viewIndexingTrigger, defaultSensor, 2);
                    if (appSettingsWithoutQuery != null && appSettingsWithoutQuery.getCodelessEventsEnabled()) {
                        viewIndexer.schedule();
                    }
                }
                if (!isDebugOnEmulator() || isAppIndexingEnabled.get()) {
                    return;
                }
                codelessSessionChecker.checkCodelessSession(applicationId);
            }
        } catch (Throwable th) {
            CrashShieldHandler.handleThrowable(th, CodelessManager.class);
        }
    }

    public static void onActivityPaused(Activity activity) {
        if (CrashShieldHandler.isObjectCrashing(CodelessManager.class)) {
            return;
        }
        try {
            if (isCodelessEnabled.get()) {
                CodelessMatcher.getInstance().remove(activity);
                if (viewIndexer != null) {
                    viewIndexer.unschedule();
                }
                if (sensorManager != null) {
                    sensorManager.unregisterListener(viewIndexingTrigger);
                }
            }
        } catch (Throwable th) {
            CrashShieldHandler.handleThrowable(th, CodelessManager.class);
        }
    }

    public static void onActivityDestroyed(Activity activity) {
        if (CrashShieldHandler.isObjectCrashing(CodelessManager.class)) {
            return;
        }
        try {
            CodelessMatcher.getInstance().destroy(activity);
        } catch (Throwable th) {
            CrashShieldHandler.handleThrowable(th, CodelessManager.class);
        }
    }

    public static void enable() {
        if (CrashShieldHandler.isObjectCrashing(CodelessManager.class)) {
            return;
        }
        try {
            isCodelessEnabled.set(true);
        } catch (Throwable th) {
            CrashShieldHandler.handleThrowable(th, CodelessManager.class);
        }
    }

    public static void disable() {
        if (CrashShieldHandler.isObjectCrashing(CodelessManager.class)) {
            return;
        }
        try {
            isCodelessEnabled.set(false);
        } catch (Throwable th) {
            CrashShieldHandler.handleThrowable(th, CodelessManager.class);
        }
    }

    static void checkCodelessSession(final String str) {
        if (CrashShieldHandler.isObjectCrashing(CodelessManager.class)) {
            return;
        }
        try {
            if (isCheckingSession.booleanValue()) {
                return;
            }
            isCheckingSession = true;
            FacebookSdk.getExecutor().execute(new Runnable() { // from class: com.facebook.appevents.codeless.CodelessManager.3
                @Override // java.lang.Runnable
                public void run() {
                    if (CrashShieldHandler.isObjectCrashing(this)) {
                        return;
                    }
                    try {
                        boolean z = true;
                        GraphRequest newPostRequest = GraphRequest.newPostRequest(null, String.format(Locale.US, "%s/app_indexing_session", str), null, null);
                        Bundle parameters = newPostRequest.getParameters();
                        if (parameters == null) {
                            parameters = new Bundle();
                        }
                        AttributionIdentifiers attributionIdentifiers = AttributionIdentifiers.getAttributionIdentifiers(FacebookSdk.getApplicationContext());
                        JSONArray jSONArray = new JSONArray();
                        jSONArray.put(Build.MODEL != null ? Build.MODEL : "");
                        if (attributionIdentifiers != null && attributionIdentifiers.getAndroidAdvertiserId() != null) {
                            jSONArray.put(attributionIdentifiers.getAndroidAdvertiserId());
                        } else {
                            jSONArray.put("");
                        }
                        jSONArray.put(AppEventsConstants.EVENT_PARAM_VALUE_NO);
                        jSONArray.put(AppEventUtility.isEmulator() ? AppEventsConstants.EVENT_PARAM_VALUE_YES : AppEventsConstants.EVENT_PARAM_VALUE_NO);
                        Locale currentLocale = Utility.getCurrentLocale();
                        jSONArray.put(currentLocale.getLanguage() + "_" + currentLocale.getCountry());
                        String jSONArray2 = jSONArray.toString();
                        parameters.putString(Constants.DEVICE_SESSION_ID, CodelessManager.getCurrentDeviceSessionID());
                        parameters.putString(Constants.EXTINFO, jSONArray2);
                        newPostRequest.setParameters(parameters);
                        JSONObject jSONObject = newPostRequest.executeAndWait().getJSONObject();
                        AtomicBoolean access$100 = CodelessManager.access$100();
                        if (jSONObject == null || !jSONObject.optBoolean(Constants.APP_INDEXING_ENABLED, false)) {
                            z = false;
                        }
                        access$100.set(z);
                        if (!CodelessManager.access$100().get()) {
                            CodelessManager.access$202(null);
                        } else if (CodelessManager.access$300() != null) {
                            CodelessManager.access$300().schedule();
                        }
                        CodelessManager.access$402(false);
                    } catch (Throwable th) {
                        CrashShieldHandler.handleThrowable(th, this);
                    }
                }
            });
        } catch (Throwable th) {
            CrashShieldHandler.handleThrowable(th, CodelessManager.class);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getCurrentDeviceSessionID() {
        if (CrashShieldHandler.isObjectCrashing(CodelessManager.class)) {
            return null;
        }
        try {
            if (deviceSessionID == null) {
                deviceSessionID = UUID.randomUUID().toString();
            }
            return deviceSessionID;
        } catch (Throwable th) {
            CrashShieldHandler.handleThrowable(th, CodelessManager.class);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean getIsAppIndexingEnabled() {
        if (CrashShieldHandler.isObjectCrashing(CodelessManager.class)) {
            return false;
        }
        try {
            return isAppIndexingEnabled.get();
        } catch (Throwable th) {
            CrashShieldHandler.handleThrowable(th, CodelessManager.class);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void updateAppIndexing(Boolean bool) {
        if (CrashShieldHandler.isObjectCrashing(CodelessManager.class)) {
            return;
        }
        try {
            isAppIndexingEnabled.set(bool.booleanValue());
        } catch (Throwable th) {
            CrashShieldHandler.handleThrowable(th, CodelessManager.class);
        }
    }

    @VisibleForTesting
    static void setCodelessSessionChecker(CodelessSessionChecker codelessSessionChecker2) {
        if (CrashShieldHandler.isObjectCrashing(CodelessManager.class)) {
            return;
        }
        try {
            codelessSessionChecker = codelessSessionChecker2;
        } catch (Throwable th) {
            CrashShieldHandler.handleThrowable(th, CodelessManager.class);
        }
    }
}
