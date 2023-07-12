package com.treadly.Treadly.UI.Util.BaseClasses;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.Util.ActivityUtil;
import java.util.List;

/* loaded from: classes2.dex */
public class BaseFragment extends Fragment {
    AlertDialog baseDialog = null;

    public void onFragmentReturning() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void showLoading() {
        showLoading(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void showLoading(final boolean z) {
        final BaseActivity baseActivity = (BaseActivity) getActivity();
        if (baseActivity != null) {
            ActivityUtil.runOnUiThread(baseActivity, new Runnable() { // from class: com.treadly.Treadly.UI.Util.BaseClasses.-$$Lambda$BaseFragment$L0kp1XhWMar9FAcexHt85z_8hjc
                @Override // java.lang.Runnable
                public final void run() {
                    BaseActivity.this.showLoadingDialog(z);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void dismissLoading() {
        final BaseActivity baseActivity = (BaseActivity) getActivity();
        if (baseActivity != null) {
            baseActivity.getClass();
            ActivityUtil.runOnUiThread(baseActivity, new Runnable() { // from class: com.treadly.Treadly.UI.Util.BaseClasses.-$$Lambda$eXZLv4kyvJAq7bP_0Q8D_dA3ETk
                @Override // java.lang.Runnable
                public final void run() {
                    BaseActivity.this.closeLoadingDialog();
                }
            });
        }
    }

    protected void unblockBottomNavigation() {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.blockBottomNavigation = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void hideBottomNavigation() {
        hideBottomNavigation(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void hideBottomNavigation(boolean z) {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.blockBottomNavigation = z;
            mainActivity.hideBottomNavigationView();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void showBottomNavigation() {
        showBottomNavigation(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void showBottomNavigation(boolean z) {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            if (z) {
                mainActivity.blockBottomNavigation = false;
            }
            mainActivity.showBottomNavigationView();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        dismissBaseAlert();
    }

    public void addFragmentToStack(Fragment fragment, @Nullable String str, @Nullable String str2, boolean z) {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        if (z) {
            activity.getSupportFragmentManager().beginTransaction().add(R.id.activity_fragment_container_empty, fragment, str).addToBackStack(str2).commit();
        } else {
            activity.getSupportFragmentManager().beginTransaction().add(R.id.activity_fragment_container_empty, fragment, str).commit();
        }
    }

    public void addFragmentToStack(Fragment fragment, String str, boolean z) {
        addFragmentToStack(fragment, str, null, z);
    }

    public void addFragmentToStack(Fragment fragment, String str) {
        addFragmentToStack(fragment, str, null, false);
    }

    public void addFragmentToStack(Fragment fragment, boolean z) {
        addFragmentToStack(fragment, null, z);
    }

    public void addFragmentToStack(Fragment fragment) {
        addFragmentToStack(fragment, (String) null);
    }

    public void replaceFragmentOnStack(Fragment fragment, @Nullable String str) {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.activity_fragment_container_empty, fragment, str).commit();
    }

    public void replaceFragmentOnStack(Fragment fragment) {
        replaceFragmentOnStack(fragment, null);
    }

    public void popBackStack() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        activity.getSupportFragmentManager().popBackStack();
    }

    public void popBackStack(String str) {
        Fragment findFragmentByTag;
        FragmentActivity activity = getActivity();
        if (activity == null || (findFragmentByTag = activity.getSupportFragmentManager().findFragmentByTag(str)) == null) {
            return;
        }
        activity.getSupportFragmentManager().popBackStack(findFragmentByTag.getId(), 1);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void clearBackStack() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        for (int i = 0; i < activity.getSupportFragmentManager().getBackStackEntryCount(); i++) {
            activity.getSupportFragmentManager().popBackStack();
        }
    }

    public void addBackStackCallback() {
        final FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        activity.getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() { // from class: com.treadly.Treadly.UI.Util.BaseClasses.-$$Lambda$BaseFragment$EksedZf3tsS2EGuIM6MnPZalpd4
            @Override // androidx.fragment.app.FragmentManager.OnBackStackChangedListener
            public final void onBackStackChanged() {
                BaseFragment.lambda$addBackStackCallback$1(BaseFragment.this, activity);
            }
        });
    }

    public static /* synthetic */ void lambda$addBackStackCallback$1(BaseFragment baseFragment, FragmentActivity fragmentActivity) {
        FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();
        if (supportFragmentManager != null) {
            List<Fragment> fragments = supportFragmentManager.getFragments();
            if (fragments.size() <= 0 || fragments.get(fragments.size() - 1) != baseFragment) {
                return;
            }
            baseFragment.onFragmentReturning();
        }
    }

    public boolean isBaseAlertShowing() {
        return this.baseDialog != null && this.baseDialog.isShowing();
    }

    public void showBaseAlert(String str, String str2) {
        if (getContext() == null) {
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(str2);
        builder.setTitle(str);
        builder.setNeutralButton("Dismiss", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.Util.BaseClasses.-$$Lambda$BaseFragment$lJfALAush0tP0kPrXhT_ITw3DaI
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                BaseFragment.this.dismissBaseAlert();
            }
        });
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.Util.BaseClasses.-$$Lambda$BaseFragment$GPJEuj92Is0E83WDyyecmz5qjq4
            @Override // java.lang.Runnable
            public final void run() {
                BaseFragment.this.setBaseAlert(builder.create());
            }
        });
    }

    public void showBaseAlert() {
        if (this.baseDialog != null) {
            this.baseDialog.show();
        }
    }

    public void setBaseAlert(AlertDialog alertDialog) {
        setBaseAlert(alertDialog, true);
    }

    public void setBaseAlert(AlertDialog.Builder builder) {
        setBaseAlert(builder.create(), true);
    }

    public void setBaseAlert(AlertDialog.Builder builder, boolean z) {
        setBaseAlert(builder.create(), z);
    }

    public void setBaseAlert(final AlertDialog alertDialog, boolean z) {
        dismissBaseAlert();
        this.baseDialog = alertDialog;
        if (z) {
            FragmentActivity activity = getActivity();
            alertDialog.getClass();
            ActivityUtil.runOnUiThread(activity, new Runnable() { // from class: com.treadly.Treadly.UI.Util.BaseClasses.-$$Lambda$j665dBpBy52SvBIQg12_qD0TqdY
                @Override // java.lang.Runnable
                public final void run() {
                    alertDialog.show();
                }
            });
        }
    }

    public void dismissBaseAlert() {
        dismissBaseAlert(true);
    }

    public void dismissBaseAlert(boolean z) {
        if (this.baseDialog != null) {
            FragmentActivity activity = getActivity();
            final AlertDialog alertDialog = this.baseDialog;
            alertDialog.getClass();
            ActivityUtil.runOnUiThread(activity, new Runnable() { // from class: com.treadly.Treadly.UI.Util.BaseClasses.-$$Lambda$fkj0H4KJ3WjKu8UpsKAOo483BTo
                @Override // java.lang.Runnable
                public final void run() {
                    alertDialog.dismiss();
                }
            });
        }
        if (z) {
            this.baseDialog = null;
        }
    }
}
