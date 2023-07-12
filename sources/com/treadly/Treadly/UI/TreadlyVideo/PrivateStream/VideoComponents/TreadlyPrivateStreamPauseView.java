package com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents;

import android.content.Context;
import android.net.Uri;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

/* loaded from: classes2.dex */
public class TreadlyPrivateStreamPauseView extends CircularImageView {
    public TreadlyPrivateStreamPauseView(Context context) {
        super(context);
    }

    public void setAvatar(String str) {
        Picasso.get().load(Uri.parse(str)).tag(getContext()).into(this);
    }
}
