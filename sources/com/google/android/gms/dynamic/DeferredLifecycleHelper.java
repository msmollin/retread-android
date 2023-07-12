package com.google.android.gms.dynamic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.internal.ConnectionErrorMessages;
import com.google.android.gms.dynamic.LifecycleDelegate;
import java.util.LinkedList;

/* loaded from: classes.dex */
public abstract class DeferredLifecycleHelper<T extends LifecycleDelegate> {
    private T zzabc;
    private Bundle zzabd;
    private LinkedList<zza> zzabe;
    private final OnDelegateCreatedListener<T> zzabf = new com.google.android.gms.dynamic.zza(this);

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public interface zza {
        int getState();

        void zza(LifecycleDelegate lifecycleDelegate);
    }

    public static void showGooglePlayUnavailableMessage(FrameLayout frameLayout) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        Context context = frameLayout.getContext();
        int isGooglePlayServicesAvailable = googleApiAvailability.isGooglePlayServicesAvailable(context);
        String errorMessage = ConnectionErrorMessages.getErrorMessage(context, isGooglePlayServicesAvailable);
        String errorDialogButtonMessage = ConnectionErrorMessages.getErrorDialogButtonMessage(context, isGooglePlayServicesAvailable);
        LinearLayout linearLayout = new LinearLayout(frameLayout.getContext());
        linearLayout.setOrientation(1);
        linearLayout.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
        frameLayout.addView(linearLayout);
        TextView textView = new TextView(frameLayout.getContext());
        textView.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
        textView.setText(errorMessage);
        linearLayout.addView(textView);
        Intent errorResolutionIntent = googleApiAvailability.getErrorResolutionIntent(context, isGooglePlayServicesAvailable, null);
        if (errorResolutionIntent != null) {
            Button button = new Button(context);
            button.setId(16908313);
            button.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
            button.setText(errorDialogButtonMessage);
            linearLayout.addView(button);
            button.setOnClickListener(new zze(context, errorResolutionIntent));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ Bundle zza(DeferredLifecycleHelper deferredLifecycleHelper, Bundle bundle) {
        deferredLifecycleHelper.zzabd = null;
        return null;
    }

    private final void zza(Bundle bundle, zza zzaVar) {
        if (this.zzabc != null) {
            zzaVar.zza(this.zzabc);
            return;
        }
        if (this.zzabe == null) {
            this.zzabe = new LinkedList<>();
        }
        this.zzabe.add(zzaVar);
        if (bundle != null) {
            if (this.zzabd == null) {
                this.zzabd = (Bundle) bundle.clone();
            } else {
                this.zzabd.putAll(bundle);
            }
        }
        createDelegate(this.zzabf);
    }

    private final void zzm(int i) {
        while (!this.zzabe.isEmpty() && this.zzabe.getLast().getState() >= i) {
            this.zzabe.removeLast();
        }
    }

    protected abstract void createDelegate(OnDelegateCreatedListener<T> onDelegateCreatedListener);

    public T getDelegate() {
        return this.zzabc;
    }

    protected void handleGooglePlayUnavailable(FrameLayout frameLayout) {
        showGooglePlayUnavailableMessage(frameLayout);
    }

    public void onCreate(Bundle bundle) {
        zza(bundle, new zzc(this, bundle));
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        FrameLayout frameLayout = new FrameLayout(layoutInflater.getContext());
        zza(bundle, new zzd(this, frameLayout, layoutInflater, viewGroup, bundle));
        if (this.zzabc == null) {
            handleGooglePlayUnavailable(frameLayout);
        }
        return frameLayout;
    }

    public void onDestroy() {
        if (this.zzabc != null) {
            this.zzabc.onDestroy();
        } else {
            zzm(1);
        }
    }

    public void onDestroyView() {
        if (this.zzabc != null) {
            this.zzabc.onDestroyView();
        } else {
            zzm(2);
        }
    }

    public void onInflate(Activity activity, Bundle bundle, Bundle bundle2) {
        zza(bundle2, new zzb(this, activity, bundle, bundle2));
    }

    public void onLowMemory() {
        if (this.zzabc != null) {
            this.zzabc.onLowMemory();
        }
    }

    public void onPause() {
        if (this.zzabc != null) {
            this.zzabc.onPause();
        } else {
            zzm(5);
        }
    }

    public void onResume() {
        zza((Bundle) null, new zzg(this));
    }

    public void onSaveInstanceState(Bundle bundle) {
        if (this.zzabc != null) {
            this.zzabc.onSaveInstanceState(bundle);
        } else if (this.zzabd != null) {
            bundle.putAll(this.zzabd);
        }
    }

    public void onStart() {
        zza((Bundle) null, new zzf(this));
    }

    public void onStop() {
        if (this.zzabc != null) {
            this.zzabc.onStop();
        } else {
            zzm(4);
        }
    }
}
