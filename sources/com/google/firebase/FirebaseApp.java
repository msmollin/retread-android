package com.google.firebase;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.collection.ArrayMap;
import androidx.collection.ArraySet;
import androidx.core.content.ContextCompat;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.api.internal.BackgroundDetector;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Base64Utils;
import com.google.android.gms.common.util.PlatformVersion;
import com.google.android.gms.common.util.ProcessUtils;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.components.Component;
import com.google.firebase.components.zzc;
import com.google.firebase.components.zzg;
import com.google.firebase.internal.InternalTokenProvider;
import com.google.firebase.internal.InternalTokenResult;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.concurrent.GuardedBy;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* loaded from: classes.dex */
public class FirebaseApp {
    public static final String DEFAULT_APP_NAME = "[DEFAULT]";
    private final String mName;
    private final Context zzg;
    private final FirebaseOptions zzh;
    private final zzg zzi;
    private InternalTokenProvider zzo;
    private static final List<String> zza = Arrays.asList("com.google.firebase.auth.FirebaseAuth", "com.google.firebase.iid.FirebaseInstanceId");
    private static final List<String> zzb = Collections.singletonList("com.google.firebase.crash.FirebaseCrash");
    private static final List<String> zzc = Arrays.asList("com.google.android.gms.measurement.AppMeasurement");
    private static final List<String> zzd = Arrays.asList(new String[0]);
    private static final Set<String> zze = Collections.emptySet();
    private static final Object sLock = new Object();
    @GuardedBy("sLock")
    static final Map<String, FirebaseApp> zzf = new ArrayMap();
    private final AtomicBoolean zzj = new AtomicBoolean(false);
    private final AtomicBoolean zzk = new AtomicBoolean();
    private final List<IdTokenListener> zzl = new CopyOnWriteArrayList();
    private final List<BackgroundStateChangeListener> zzm = new CopyOnWriteArrayList();
    private final List<Object> zzn = new CopyOnWriteArrayList();
    private IdTokenListenersCountChangedListener zzp = new com.google.firebase.internal.zza();

    @KeepForSdk
    /* loaded from: classes.dex */
    public interface BackgroundStateChangeListener {
        @KeepForSdk
        void onBackgroundStateChanged(boolean z);
    }

    @KeepForSdk
    /* loaded from: classes.dex */
    public interface IdTokenListener {
        @KeepForSdk
        void onIdTokenChanged(@NonNull InternalTokenResult internalTokenResult);
    }

    @KeepForSdk
    /* loaded from: classes.dex */
    public interface IdTokenListenersCountChangedListener {
        @KeepForSdk
        void onListenerCountChanged(int i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @TargetApi(24)
    /* loaded from: classes.dex */
    public static class zza extends BroadcastReceiver {
        private static AtomicReference<zza> zzq = new AtomicReference<>();
        private final Context zzg;

        private zza(Context context) {
            this.zzg = context;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void zza(Context context) {
            if (zzq.get() == null) {
                zza zzaVar = new zza(context);
                if (zzq.compareAndSet(null, zzaVar)) {
                    context.registerReceiver(zzaVar, new IntentFilter("android.intent.action.USER_UNLOCKED"));
                }
            }
        }

        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            synchronized (FirebaseApp.sLock) {
                for (FirebaseApp firebaseApp : FirebaseApp.zzf.values()) {
                    firebaseApp.zzc();
                }
            }
            this.zzg.unregisterReceiver(this);
        }
    }

    private FirebaseApp(Context context, String str, FirebaseOptions firebaseOptions) {
        this.zzg = (Context) Preconditions.checkNotNull(context);
        this.mName = Preconditions.checkNotEmpty(str);
        this.zzh = (FirebaseOptions) Preconditions.checkNotNull(firebaseOptions);
        this.zzi = new zzg(new zzc(this.zzg).zzj(), Component.of(Context.class, this.zzg), Component.of(FirebaseApp.class, this), Component.of(FirebaseOptions.class, this.zzh));
    }

    public static List<FirebaseApp> getApps(Context context) {
        ArrayList arrayList;
        com.google.firebase.internal.zzb.zze(context);
        synchronized (sLock) {
            arrayList = new ArrayList(zzf.values());
            com.google.firebase.internal.zzb.zzq();
            Set<String> zzr = com.google.firebase.internal.zzb.zzr();
            zzr.removeAll(zzf.keySet());
            for (String str : zzr) {
                com.google.firebase.internal.zzb.zzb(str);
                arrayList.add(initializeApp(context, null, str));
            }
        }
        return arrayList;
    }

    @Nullable
    public static FirebaseApp getInstance() {
        FirebaseApp firebaseApp;
        synchronized (sLock) {
            firebaseApp = zzf.get(DEFAULT_APP_NAME);
            if (firebaseApp == null) {
                String myProcessName = ProcessUtils.getMyProcessName();
                StringBuilder sb = new StringBuilder(String.valueOf(myProcessName).length() + 116);
                sb.append("Default FirebaseApp is not initialized in this process ");
                sb.append(myProcessName);
                sb.append(". Make sure to call FirebaseApp.initializeApp(Context) first.");
                throw new IllegalStateException(sb.toString());
            }
        }
        return firebaseApp;
    }

    public static FirebaseApp getInstance(@NonNull String str) {
        FirebaseApp firebaseApp;
        String str2;
        synchronized (sLock) {
            firebaseApp = zzf.get(str.trim());
            if (firebaseApp == null) {
                List<String> zzb2 = zzb();
                if (zzb2.isEmpty()) {
                    str2 = "";
                } else {
                    String valueOf = String.valueOf(TextUtils.join(", ", zzb2));
                    str2 = valueOf.length() != 0 ? "Available app names: ".concat(valueOf) : new String("Available app names: ");
                }
                throw new IllegalStateException(String.format("FirebaseApp with name %s doesn't exist. %s", str, str2));
            }
        }
        return firebaseApp;
    }

    @Nullable
    public static FirebaseApp initializeApp(Context context) {
        synchronized (sLock) {
            if (zzf.containsKey(DEFAULT_APP_NAME)) {
                return getInstance();
            }
            FirebaseOptions fromResource = FirebaseOptions.fromResource(context);
            if (fromResource == null) {
                return null;
            }
            return initializeApp(context, fromResource);
        }
    }

    public static FirebaseApp initializeApp(Context context, FirebaseOptions firebaseOptions) {
        return initializeApp(context, firebaseOptions, DEFAULT_APP_NAME);
    }

    public static FirebaseApp initializeApp(Context context, FirebaseOptions firebaseOptions, String str) {
        FirebaseApp firebaseApp;
        com.google.firebase.internal.zzb.zze(context);
        if (PlatformVersion.isAtLeastIceCreamSandwich() && (context.getApplicationContext() instanceof Application)) {
            BackgroundDetector.initialize((Application) context.getApplicationContext());
            BackgroundDetector.getInstance().addListener(new com.google.firebase.zza());
        }
        String trim = str.trim();
        if (context.getApplicationContext() != null) {
            context = context.getApplicationContext();
        }
        synchronized (sLock) {
            StringBuilder sb = new StringBuilder(String.valueOf(trim).length() + 33);
            sb.append("FirebaseApp name ");
            sb.append(trim);
            sb.append(" already exists!");
            Preconditions.checkState(!zzf.containsKey(trim), sb.toString());
            Preconditions.checkNotNull(context, "Application context cannot be null.");
            firebaseApp = new FirebaseApp(context, trim, firebaseOptions);
            zzf.put(trim, firebaseApp);
        }
        com.google.firebase.internal.zzb.zzb(firebaseApp);
        firebaseApp.zzc();
        return firebaseApp;
    }

    @KeepForSdk
    public static void onBackgroundStateChanged(boolean z) {
        synchronized (sLock) {
            ArrayList arrayList = new ArrayList(zzf.values());
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                FirebaseApp firebaseApp = (FirebaseApp) obj;
                if (firebaseApp.zzj.get()) {
                    firebaseApp.zza(z);
                }
            }
        }
    }

    private final void zza() {
        Preconditions.checkState(!this.zzk.get(), "FirebaseApp was deleted");
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static <T> void zza(Class<T> cls, T t, Iterable<String> iterable, boolean z) {
        for (String str : iterable) {
            if (z) {
                try {
                } catch (ClassNotFoundException unused) {
                    if (zze.contains(str)) {
                        throw new IllegalStateException(String.valueOf(str).concat(" is missing, but is required. Check if it has been removed by Proguard."));
                    }
                    Log.d("FirebaseApp", String.valueOf(str).concat(" is not linked. Skipping initialization."));
                } catch (IllegalAccessException e) {
                    String valueOf = String.valueOf(str);
                    Log.wtf("FirebaseApp", valueOf.length() != 0 ? "Failed to initialize ".concat(valueOf) : new String("Failed to initialize "), e);
                } catch (NoSuchMethodException unused2) {
                    throw new IllegalStateException(String.valueOf(str).concat("#getInstance has been removed by Proguard. Add keep rule to prevent it."));
                } catch (InvocationTargetException e2) {
                    Log.wtf("FirebaseApp", "Firebase API initialization failure.", e2);
                }
                if (zzd.contains(str)) {
                }
            }
            Method method = Class.forName(str).getMethod("getInstance", cls);
            int modifiers = method.getModifiers();
            if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers)) {
                method.invoke(null, t);
            }
        }
    }

    private final void zza(boolean z) {
        Log.d("FirebaseApp", "Notifying background state change listeners.");
        for (BackgroundStateChangeListener backgroundStateChangeListener : this.zzm) {
            backgroundStateChangeListener.onBackgroundStateChanged(z);
        }
    }

    private static List<String> zzb() {
        ArraySet arraySet = new ArraySet();
        synchronized (sLock) {
            for (FirebaseApp firebaseApp : zzf.values()) {
                arraySet.add(firebaseApp.getName());
            }
            if (com.google.firebase.internal.zzb.zzq() != null) {
                arraySet.addAll(com.google.firebase.internal.zzb.zzr());
            }
        }
        ArrayList arrayList = new ArrayList(arraySet);
        Collections.sort(arrayList);
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zzc() {
        boolean isDeviceProtectedStorage = ContextCompat.isDeviceProtectedStorage(this.zzg);
        if (isDeviceProtectedStorage) {
            zza.zza(this.zzg);
        } else {
            this.zzi.zzb(isDefaultApp());
        }
        zza(FirebaseApp.class, this, zza, isDeviceProtectedStorage);
        if (isDefaultApp()) {
            zza(FirebaseApp.class, this, zzb, isDeviceProtectedStorage);
            zza(Context.class, this.zzg, zzc, isDeviceProtectedStorage);
        }
    }

    @KeepForSdk
    public void addBackgroundStateChangeListener(BackgroundStateChangeListener backgroundStateChangeListener) {
        zza();
        if (this.zzj.get() && BackgroundDetector.getInstance().isInBackground()) {
            backgroundStateChangeListener.onBackgroundStateChanged(true);
        }
        this.zzm.add(backgroundStateChangeListener);
    }

    @KeepForSdk
    public void addIdTokenListener(@NonNull IdTokenListener idTokenListener) {
        zza();
        Preconditions.checkNotNull(idTokenListener);
        this.zzl.add(idTokenListener);
        this.zzp.onListenerCountChanged(this.zzl.size());
    }

    public boolean equals(Object obj) {
        if (obj instanceof FirebaseApp) {
            return this.mName.equals(((FirebaseApp) obj).getName());
        }
        return false;
    }

    @KeepForSdk
    public <T> T get(Class<T> cls) {
        zza();
        return (T) this.zzi.get(cls);
    }

    @NonNull
    public Context getApplicationContext() {
        zza();
        return this.zzg;
    }

    @NonNull
    public String getName() {
        zza();
        return this.mName;
    }

    @NonNull
    public FirebaseOptions getOptions() {
        zza();
        return this.zzh;
    }

    @KeepForSdk
    public String getPersistenceKey() {
        String encodeUrlSafeNoPadding = Base64Utils.encodeUrlSafeNoPadding(getName().getBytes());
        String encodeUrlSafeNoPadding2 = Base64Utils.encodeUrlSafeNoPadding(getOptions().getApplicationId().getBytes());
        StringBuilder sb = new StringBuilder(String.valueOf(encodeUrlSafeNoPadding).length() + 1 + String.valueOf(encodeUrlSafeNoPadding2).length());
        sb.append(encodeUrlSafeNoPadding);
        sb.append(MqttTopic.SINGLE_LEVEL_WILDCARD);
        sb.append(encodeUrlSafeNoPadding2);
        return sb.toString();
    }

    @KeepForSdk
    public Task<GetTokenResult> getToken(boolean z) {
        zza();
        return this.zzo == null ? Tasks.forException(new FirebaseApiNotAvailableException("firebase-auth is not linked, please fall back to unauthenticated mode.")) : this.zzo.getAccessToken(z);
    }

    @Nullable
    public final String getUid() throws FirebaseApiNotAvailableException {
        zza();
        if (this.zzo != null) {
            return this.zzo.getUid();
        }
        throw new FirebaseApiNotAvailableException("firebase-auth is not linked, please fall back to unauthenticated mode.");
    }

    public int hashCode() {
        return this.mName.hashCode();
    }

    @VisibleForTesting
    @KeepForSdk
    public boolean isDefaultApp() {
        return DEFAULT_APP_NAME.equals(getName());
    }

    @KeepForSdk
    @UiThread
    public void notifyIdTokenListeners(@NonNull InternalTokenResult internalTokenResult) {
        Log.d("FirebaseApp", "Notifying auth state listeners.");
        int i = 0;
        for (IdTokenListener idTokenListener : this.zzl) {
            idTokenListener.onIdTokenChanged(internalTokenResult);
            i++;
        }
        Log.d("FirebaseApp", String.format("Notified %d auth state listeners.", Integer.valueOf(i)));
    }

    @KeepForSdk
    public void removeBackgroundStateChangeListener(BackgroundStateChangeListener backgroundStateChangeListener) {
        zza();
        this.zzm.remove(backgroundStateChangeListener);
    }

    public void setAutomaticResourceManagementEnabled(boolean z) {
        zza();
        if (this.zzj.compareAndSet(!z, z)) {
            boolean isInBackground = BackgroundDetector.getInstance().isInBackground();
            if (z && isInBackground) {
                zza(true);
            } else if (z || !isInBackground) {
            } else {
                zza(false);
            }
        }
    }

    @KeepForSdk
    public void setIdTokenListenersCountChangedListener(@NonNull IdTokenListenersCountChangedListener idTokenListenersCountChangedListener) {
        this.zzp = (IdTokenListenersCountChangedListener) Preconditions.checkNotNull(idTokenListenersCountChangedListener);
        this.zzp.onListenerCountChanged(this.zzl.size());
    }

    @KeepForSdk
    public void setTokenProvider(@NonNull InternalTokenProvider internalTokenProvider) {
        this.zzo = (InternalTokenProvider) Preconditions.checkNotNull(internalTokenProvider);
    }

    public String toString() {
        return Objects.toStringHelper(this).add("name", this.mName).add("options", this.zzh).toString();
    }

    public final void zza(@NonNull IdTokenListener idTokenListener) {
        zza();
        Preconditions.checkNotNull(idTokenListener);
        this.zzl.remove(idTokenListener);
        this.zzp.onListenerCountChanged(this.zzl.size());
    }
}
