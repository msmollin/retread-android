package com.facebook.internal;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;

/* loaded from: classes.dex */
public class FragmentWrapper {
    private Fragment nativeFragment;
    private androidx.fragment.app.Fragment supportFragment;

    public FragmentWrapper(androidx.fragment.app.Fragment fragment) {
        Validate.notNull(fragment, "fragment");
        this.supportFragment = fragment;
    }

    public FragmentWrapper(Fragment fragment) {
        Validate.notNull(fragment, "fragment");
        this.nativeFragment = fragment;
    }

    public Fragment getNativeFragment() {
        return this.nativeFragment;
    }

    public androidx.fragment.app.Fragment getSupportFragment() {
        return this.supportFragment;
    }

    public void startActivityForResult(Intent intent, int i) {
        if (this.supportFragment != null) {
            this.supportFragment.startActivityForResult(intent, i);
        } else {
            this.nativeFragment.startActivityForResult(intent, i);
        }
    }

    public final Activity getActivity() {
        if (this.supportFragment != null) {
            return this.supportFragment.getActivity();
        }
        return this.nativeFragment.getActivity();
    }
}
