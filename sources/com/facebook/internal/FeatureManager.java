package com.facebook.internal;

import androidx.annotation.RestrictTo;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.InputDeviceCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import com.facebook.FacebookSdk;
import com.facebook.internal.FetchedAppGateKeepersManager;
import java.util.HashMap;
import java.util.Map;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* loaded from: classes.dex */
public final class FeatureManager {
    private static final String FEATURE_MANAGER_STORE = "com.facebook.internal.FEATURE_MANAGER";
    private static final Map<Feature, String[]> featureMapping = new HashMap();

    /* loaded from: classes.dex */
    public interface Callback {
        void onCompleted(boolean z);
    }

    public static void checkFeature(final Feature feature, final Callback callback) {
        FetchedAppGateKeepersManager.loadAppGateKeepersAsync(new FetchedAppGateKeepersManager.Callback() { // from class: com.facebook.internal.FeatureManager.1
            @Override // com.facebook.internal.FetchedAppGateKeepersManager.Callback
            public void onCompleted() {
                Callback.this.onCompleted(FeatureManager.isEnabled(feature));
            }
        });
    }

    public static boolean isEnabled(Feature feature) {
        if (Feature.Unknown == feature) {
            return false;
        }
        if (Feature.Core == feature) {
            return true;
        }
        String string = FacebookSdk.getApplicationContext().getSharedPreferences(FEATURE_MANAGER_STORE, 0).getString(feature.toKey(), null);
        if (string == null || !string.equals(FacebookSdk.getSdkVersion())) {
            Feature parent = feature.getParent();
            if (parent == feature) {
                return getGKStatus(feature);
            }
            return isEnabled(parent) && getGKStatus(feature);
        }
        return false;
    }

    public static void disableFeature(Feature feature) {
        FacebookSdk.getApplicationContext().getSharedPreferences(FEATURE_MANAGER_STORE, 0).edit().putString(feature.toKey(), FacebookSdk.getSdkVersion()).apply();
    }

    public static Feature getFeature(String str) {
        initializeFeatureMapping();
        for (Map.Entry<Feature, String[]> entry : featureMapping.entrySet()) {
            for (String str2 : entry.getValue()) {
                if (str.startsWith(str2)) {
                    return entry.getKey();
                }
            }
        }
        return Feature.Unknown;
    }

    private static synchronized void initializeFeatureMapping() {
        synchronized (FeatureManager.class) {
            if (featureMapping.isEmpty()) {
                featureMapping.put(Feature.AAM, new String[]{"com.facebook.appevents.aam."});
                featureMapping.put(Feature.CodelessEvents, new String[]{"com.facebook.appevents.codeless."});
                featureMapping.put(Feature.ErrorReport, new String[]{"com.facebook.internal.instrument.errorreport."});
                featureMapping.put(Feature.PrivacyProtection, new String[]{"com.facebook.appevents.ml."});
                featureMapping.put(Feature.SuggestedEvents, new String[]{"com.facebook.appevents.suggestedevents."});
                featureMapping.put(Feature.RestrictiveDataFiltering, new String[]{"com.facebook.appevents.restrictivedatafilter.RestrictiveDataManager"});
                featureMapping.put(Feature.IntelligentIntegrity, new String[]{"com.facebook.appevents.integrity.IntegrityManager"});
                featureMapping.put(Feature.EventDeactivation, new String[]{"com.facebook.appevents.eventdeactivation."});
                featureMapping.put(Feature.OnDeviceEventProcessing, new String[]{"com.facebook.appevents.ondeviceprocessing."});
                featureMapping.put(Feature.Monitoring, new String[]{"com.facebook.internal.logging.monitor"});
            }
        }
    }

    private static boolean getGKStatus(Feature feature) {
        return FetchedAppGateKeepersManager.getGateKeeperForKey(feature.toKey(), FacebookSdk.getApplicationId(), defaultStatus(feature));
    }

    private static boolean defaultStatus(Feature feature) {
        switch (feature) {
            case RestrictiveDataFiltering:
            case Instrument:
            case CrashReport:
            case CrashShield:
            case ThreadCheck:
            case ErrorReport:
            case AAM:
            case PrivacyProtection:
            case SuggestedEvents:
            case IntelligentIntegrity:
            case ModelRequest:
            case EventDeactivation:
            case OnDeviceEventProcessing:
            case OnDevicePostInstallEventProcessing:
            case ChromeCustomTabsPrefetching:
            case Monitoring:
            case IgnoreAppSwitchToLoggedOut:
                return false;
            default:
                return true;
        }
    }

    /* loaded from: classes.dex */
    public enum Feature {
        Unknown(-1),
        Core(0),
        AppEvents(65536),
        CodelessEvents(65792),
        RestrictiveDataFiltering(66048),
        AAM(66304),
        PrivacyProtection(66560),
        SuggestedEvents(66561),
        IntelligentIntegrity(66562),
        ModelRequest(66563),
        EventDeactivation(66816),
        OnDeviceEventProcessing(67072),
        OnDevicePostInstallEventProcessing(67073),
        Instrument(131072),
        CrashReport(131328),
        CrashShield(131329),
        ThreadCheck(131330),
        ErrorReport(131584),
        Monitoring(196608),
        Login(16777216),
        ChromeCustomTabsPrefetching(16842752),
        IgnoreAppSwitchToLoggedOut(16908288),
        Share(33554432),
        Places(50331648);
        
        private final int code;

        Feature(int i) {
            this.code = i;
        }

        @Override // java.lang.Enum
        public String toString() {
            switch (this) {
                case RestrictiveDataFiltering:
                    return "RestrictiveDataFiltering";
                case Instrument:
                    return "Instrument";
                case CrashReport:
                    return "CrashReport";
                case CrashShield:
                    return "CrashShield";
                case ThreadCheck:
                    return "ThreadCheck";
                case ErrorReport:
                    return "ErrorReport";
                case AAM:
                    return "AAM";
                case PrivacyProtection:
                    return "PrivacyProtection";
                case SuggestedEvents:
                    return "SuggestedEvents";
                case IntelligentIntegrity:
                    return "IntelligentIntegrity";
                case ModelRequest:
                    return "ModelRequest";
                case EventDeactivation:
                    return "EventDeactivation";
                case OnDeviceEventProcessing:
                    return "OnDeviceEventProcessing";
                case OnDevicePostInstallEventProcessing:
                    return "OnDevicePostInstallEventProcessing";
                case ChromeCustomTabsPrefetching:
                    return "ChromeCustomTabsPrefetching";
                case Monitoring:
                    return "Monitoring";
                case IgnoreAppSwitchToLoggedOut:
                    return "IgnoreAppSwitchToLoggedOut";
                case Core:
                    return "CoreKit";
                case AppEvents:
                    return "AppEvents";
                case CodelessEvents:
                    return "CodelessEvents";
                case Login:
                    return "LoginKit";
                case Share:
                    return "ShareKit";
                case Places:
                    return "PlacesKit";
                default:
                    return "unknown";
            }
        }

        static Feature fromInt(int i) {
            Feature[] values;
            for (Feature feature : values()) {
                if (feature.code == i) {
                    return feature;
                }
            }
            return Unknown;
        }

        String toKey() {
            return "FBSDKFeature" + toString();
        }

        public Feature getParent() {
            if ((this.code & 255) > 0) {
                return fromInt(this.code & InputDeviceCompat.SOURCE_ANY);
            }
            if ((this.code & MotionEventCompat.ACTION_POINTER_INDEX_MASK) > 0) {
                return fromInt(this.code & SupportMenu.CATEGORY_MASK);
            }
            if ((this.code & 16711680) > 0) {
                return fromInt(this.code & ViewCompat.MEASURED_STATE_MASK);
            }
            return fromInt(0);
        }
    }
}
