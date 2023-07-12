package com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.R;

/* loaded from: classes2.dex */
public class TreadlyPrivateStreamPauseFragment extends Fragment {
    private CircularImageView profilePicture;

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_private_stream_pause, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.profilePicture = (CircularImageView) view.findViewById(R.id.private_pause_avatar);
    }

    public void setAvatar(String str) {
        if (!str.isEmpty() || getContext() != null) {
            Picasso.get().load(Uri.parse(str)).tag(getContext()).into(this.profilePicture);
            return;
        }
        Picasso.get().load(Uri.parse(new UserInfo("", "", "").avatarURL())).tag(getContext()).into(this.profilePicture);
    }
}
