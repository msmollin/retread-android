package com.facebook.appevents.aam;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import com.facebook.appevents.InternalAppEventsLogger;
import com.facebook.appevents.UserDataStore;
import com.facebook.appevents.internal.AppEventUtility;
import com.facebook.internal.instrument.crashshield.CrashShieldHandler;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes.dex */
final class MetadataViewObserver implements ViewTreeObserver.OnGlobalFocusChangeListener {
    private static final int MAX_TEXT_LENGTH = 100;
    private static final String TAG = MetadataViewObserver.class.getCanonicalName();
    private static final Map<Integer, MetadataViewObserver> observers = new HashMap();
    private WeakReference<Activity> activityWeakReference;
    private final Set<String> processedText = new HashSet();
    private final Handler uiThreadHandler = new Handler(Looper.getMainLooper());
    private AtomicBoolean isTracking = new AtomicBoolean(false);

    static /* synthetic */ void access$000(MetadataViewObserver metadataViewObserver, View view) {
        if (CrashShieldHandler.isObjectCrashing(MetadataViewObserver.class)) {
            return;
        }
        try {
            metadataViewObserver.processEditText(view);
        } catch (Throwable th) {
            CrashShieldHandler.handleThrowable(th, MetadataViewObserver.class);
        }
    }

    private MetadataViewObserver(Activity activity) {
        this.activityWeakReference = new WeakReference<>(activity);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @UiThread
    public static void startTrackingActivity(Activity activity) {
        MetadataViewObserver metadataViewObserver;
        if (CrashShieldHandler.isObjectCrashing(MetadataViewObserver.class)) {
            return;
        }
        try {
            int hashCode = activity.hashCode();
            if (!observers.containsKey(Integer.valueOf(hashCode))) {
                metadataViewObserver = new MetadataViewObserver(activity);
                observers.put(Integer.valueOf(activity.hashCode()), metadataViewObserver);
            } else {
                metadataViewObserver = observers.get(Integer.valueOf(hashCode));
            }
            metadataViewObserver.startTracking();
        } catch (Throwable th) {
            CrashShieldHandler.handleThrowable(th, MetadataViewObserver.class);
        }
    }

    @UiThread
    static void stopTrackingActivity(Activity activity) {
        if (CrashShieldHandler.isObjectCrashing(MetadataViewObserver.class)) {
            return;
        }
        try {
            int hashCode = activity.hashCode();
            if (observers.containsKey(Integer.valueOf(hashCode))) {
                observers.remove(Integer.valueOf(hashCode));
                observers.get(Integer.valueOf(activity.hashCode())).stopTracking();
            }
        } catch (Throwable th) {
            CrashShieldHandler.handleThrowable(th, MetadataViewObserver.class);
        }
    }

    private void startTracking() {
        View rootView;
        if (CrashShieldHandler.isObjectCrashing(this)) {
            return;
        }
        try {
            if (this.isTracking.getAndSet(true) || (rootView = AppEventUtility.getRootView(this.activityWeakReference.get())) == null) {
                return;
            }
            ViewTreeObserver viewTreeObserver = rootView.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalFocusChangeListener(this);
            }
        } catch (Throwable th) {
            CrashShieldHandler.handleThrowable(th, this);
        }
    }

    private void stopTracking() {
        View rootView;
        if (CrashShieldHandler.isObjectCrashing(this)) {
            return;
        }
        try {
            if (this.isTracking.getAndSet(false) && (rootView = AppEventUtility.getRootView(this.activityWeakReference.get())) != null) {
                ViewTreeObserver viewTreeObserver = rootView.getViewTreeObserver();
                if (viewTreeObserver.isAlive()) {
                    viewTreeObserver.removeOnGlobalFocusChangeListener(this);
                }
            }
        } catch (Throwable th) {
            CrashShieldHandler.handleThrowable(th, this);
        }
    }

    @Override // android.view.ViewTreeObserver.OnGlobalFocusChangeListener
    public void onGlobalFocusChanged(@Nullable View view, @Nullable View view2) {
        if (CrashShieldHandler.isObjectCrashing(this)) {
            return;
        }
        if (view != null) {
            try {
                process(view);
            } catch (Throwable th) {
                CrashShieldHandler.handleThrowable(th, this);
                return;
            }
        }
        if (view2 != null) {
            process(view2);
        }
    }

    private void process(final View view) {
        if (CrashShieldHandler.isObjectCrashing(this)) {
            return;
        }
        try {
            runOnUIThread(new Runnable() { // from class: com.facebook.appevents.aam.MetadataViewObserver.1
                @Override // java.lang.Runnable
                public void run() {
                    if (CrashShieldHandler.isObjectCrashing(this)) {
                        return;
                    }
                    try {
                        if (view instanceof EditText) {
                            MetadataViewObserver.access$000(MetadataViewObserver.this, view);
                        }
                    } catch (Throwable th) {
                        CrashShieldHandler.handleThrowable(th, this);
                    }
                }
            });
        } catch (Throwable th) {
            CrashShieldHandler.handleThrowable(th, this);
        }
    }

    private void processEditText(View view) {
        if (CrashShieldHandler.isObjectCrashing(this)) {
            return;
        }
        try {
            String lowerCase = ((EditText) view).getText().toString().trim().toLowerCase();
            if (!lowerCase.isEmpty() && !this.processedText.contains(lowerCase) && lowerCase.length() <= 100) {
                this.processedText.add(lowerCase);
                HashMap hashMap = new HashMap();
                List<String> currentViewIndicators = MetadataMatcher.getCurrentViewIndicators(view);
                List<String> list = null;
                for (MetadataRule metadataRule : MetadataRule.getRules()) {
                    String preNormalize = preNormalize(metadataRule.getName(), lowerCase);
                    if (metadataRule.getValRule().isEmpty() || MetadataMatcher.matchValue(preNormalize, metadataRule.getValRule())) {
                        if (MetadataMatcher.matchIndicator(currentViewIndicators, metadataRule.getKeyRules())) {
                            putUserData(hashMap, metadataRule.getName(), preNormalize);
                        } else {
                            if (list == null) {
                                list = MetadataMatcher.getAroundViewIndicators(view);
                            }
                            if (MetadataMatcher.matchIndicator(list, metadataRule.getKeyRules())) {
                                putUserData(hashMap, metadataRule.getName(), preNormalize);
                            }
                        }
                    }
                }
                InternalAppEventsLogger.setInternalUserData(hashMap);
            }
        } catch (Throwable th) {
            CrashShieldHandler.handleThrowable(th, this);
        }
    }

    private static String preNormalize(String str, String str2) {
        if (CrashShieldHandler.isObjectCrashing(MetadataViewObserver.class)) {
            return null;
        }
        try {
            return "r2".equals(str) ? str2.replaceAll("[^\\d.]", "") : str2;
        } catch (Throwable th) {
            CrashShieldHandler.handleThrowable(th, MetadataViewObserver.class);
            return null;
        }
    }

    private static void putUserData(Map<String, String> map, String str, String str2) {
        if (CrashShieldHandler.isObjectCrashing(MetadataViewObserver.class)) {
            return;
        }
        char c = 65535;
        try {
            switch (str.hashCode()) {
                case 3585:
                    if (str.equals("r3")) {
                        c = 0;
                        break;
                    }
                    break;
                case 3586:
                    if (str.equals("r4")) {
                        c = 1;
                        break;
                    }
                    break;
                case 3587:
                    if (str.equals("r5")) {
                        c = 2;
                        break;
                    }
                    break;
                case 3588:
                    if (str.equals("r6")) {
                        c = 3;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    if (!str2.startsWith("m") && !str2.startsWith("b") && !str2.startsWith(UserDataStore.GENDER)) {
                        str2 = "f";
                        break;
                    }
                    str2 = "m";
                    break;
                case 1:
                case 2:
                    str2 = str2.replaceAll("[^a-z]+", "");
                    break;
                case 3:
                    if (str2.contains("-")) {
                        str2 = str2.split("-")[0];
                        break;
                    }
                    break;
            }
            map.put(str, str2);
        } catch (Throwable th) {
            CrashShieldHandler.handleThrowable(th, MetadataViewObserver.class);
        }
    }

    private void runOnUIThread(Runnable runnable) {
        if (CrashShieldHandler.isObjectCrashing(this)) {
            return;
        }
        try {
            if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
                runnable.run();
            } else {
                this.uiThreadHandler.post(runnable);
            }
        } catch (Throwable th) {
            CrashShieldHandler.handleThrowable(th, this);
        }
    }
}
