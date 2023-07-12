package com.treadly.Treadly.UI.TreadlyProfile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.Util.ActivityUtil;
import com.treadly.Treadly.UI.Util.OnBackPressedListener;

/* loaded from: classes2.dex */
public class TreadlyProfileEditBioFragment extends Fragment {
    ImageView backArrow;
    AppCompatEditText bioTextField;
    Context context;
    boolean emptyText;
    String endText;
    String initialText;
    TreadlyProfileEditBioFragmentListener listener;
    TextWatcher textWatcher = new TextWatcher() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileEditBioFragment.7
        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            TreadlyProfileEditBioFragment.this.emptyText = TreadlyProfileEditBioFragment.this.bioTextField.getText().toString().isEmpty();
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            TreadlyProfileEditBioFragment.this.emptyText = TreadlyProfileEditBioFragment.this.bioTextField.getText().toString().isEmpty();
            if (TreadlyProfileEditBioFragment.this.emptyText) {
                TreadlyProfileEditBioFragment.this.bioTextField.setHint("Enter Bio");
            }
        }
    };
    ImageButton updateBioButton;
    String userId;

    /* loaded from: classes2.dex */
    public interface TreadlyProfileEditBioFragmentListener {
        void didUpdateBio(String str);
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        if (getActivity() != null) {
            ((MainActivity) getActivity()).hideBottomNavigationView();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        if (getActivity() != null) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.setOnBackPressedListener(new OnBackPressedListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileEditBioFragment.1
                @Override // com.treadly.Treadly.UI.Util.OnBackPressedListener
                public void backAction() {
                }
            });
            mainActivity.showBottomNavigationView();
        }
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        final MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setOnBackPressedListener(new OnBackPressedListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileEditBioFragment.2
            @Override // com.treadly.Treadly.UI.Util.OnBackPressedListener
            public void backAction() {
                mainActivity.getSupportFragmentManager().popBackStack();
            }
        });
        return layoutInflater.inflate(R.layout.fragment_treadly_profile_edit_bio, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        this.context = getContext();
        this.backArrow = (ImageView) view.findViewById(R.id.bio_back_arrow);
        this.backArrow.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileEditBioFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (TreadlyProfileEditBioFragment.this.getActivity() != null) {
                    TreadlyProfileEditBioFragment.this.getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });
        this.bioTextField = (AppCompatEditText) view.findViewById(R.id.bio_text_field);
        this.bioTextField.addTextChangedListener(this.textWatcher);
        this.bioTextField.setImeOptions(6);
        if (this.initialText != null) {
            this.bioTextField.setText(this.initialText);
            this.emptyText = this.bioTextField.getText().toString().isEmpty();
        }
        if (this.emptyText) {
            this.bioTextField.setHint("Enter Bio");
        }
        this.updateBioButton = (ImageButton) view.findViewById(R.id.bio_update_button);
        this.updateBioButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileEditBioFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                TreadlyProfileEditBioFragment.this.updateButtonPressed();
            }
        });
    }

    void updateButtonPressed() {
        if (this.bioTextField.getText() == null) {
            return;
        }
        this.updateBioButton.setEnabled(false);
        TreadlyServiceManager.getInstance().updateUserProfileDescription(this.emptyText ? "" : this.bioTextField.getText().toString(), new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileEditBioFragment.5
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onSuccess(String str) {
                TreadlyProfileEditBioFragment.this.updateBioButton.setEnabled(true);
                if (str == null) {
                    if (TreadlyProfileEditBioFragment.this.listener != null) {
                        TreadlyProfileEditBioFragment.this.listener.didUpdateBio(TreadlyProfileEditBioFragment.this.bioTextField.getText().toString());
                        TreadlyProfileEditBioFragment.this.hideKeyboard(TreadlyProfileEditBioFragment.this.bioTextField, TreadlyProfileEditBioFragment.this.context);
                        if (TreadlyProfileEditBioFragment.this.getActivity() != null) {
                            TreadlyProfileEditBioFragment.this.getActivity().getSupportFragmentManager().popBackStack();
                            return;
                        }
                        return;
                    }
                    return;
                }
                TreadlyProfileEditBioFragment.this.showAlert("Error", "Error updating profile bio");
            }
        });
    }

    void showAlert(String str, String str2) {
        final AlertDialog.Builder neutralButton = new AlertDialog.Builder(getContext()).setTitle(str).setMessage(str2).setNeutralButton("Dismiss", (DialogInterface.OnClickListener) null);
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileEditBioFragment.6
            @Override // java.lang.Runnable
            public void run() {
                neutralButton.show();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideKeyboard(View view, Context context) {
        ((InputMethodManager) context.getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
