package com.treadly.Treadly.UI.User;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.treadly.Treadly.R;

/* loaded from: classes2.dex */
public class ProfileFragment extends Fragment {
    private TextView profileName;
    private CircularImageView profileView;

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_profile_user, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.profileView = (CircularImageView) view.findViewById(R.id.profile_view);
        this.profileName = (TextView) view.findViewById(R.id.profile_name);
    }
}
