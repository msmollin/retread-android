package com.treadly.Treadly.UI.TreadlyProfile.Followers;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.treadly.Treadly.Data.Model.FollowInfo;
import com.treadly.Treadly.R;
import java.security.AccessController;
import java.util.List;

/* loaded from: classes2.dex */
public class TreadlyProfileFollowAdapter extends RecyclerView.Adapter<ViewHolder> {
    public boolean followers;
    private ItemClickListener mClickListener;
    private List<FollowInfo> mData;
    private LayoutInflater mInflater;

    /* loaded from: classes2.dex */
    public interface ItemClickListener {
        void onAvatarClick(View view, int i, boolean z);

        void onFollowClick(View view, int i, boolean z, Button button);
    }

    public TreadlyProfileFollowAdapter(Context context, List<FollowInfo> list) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(this.mInflater.inflate(R.layout.follow_user_row, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        FollowInfo followInfo = this.mData.get(i);
        viewHolder.nameText.setText(followInfo.userInfo.name);
        TextView textView = viewHolder.stepsText;
        textView.setText("" + followInfo.steps);
        Picasso.get().load(Uri.parse(followInfo.userInfo.avatarPath)).tag(AccessController.getContext()).into(viewHolder.profilePicture);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mData.size();
    }

    /* loaded from: classes2.dex */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public Button followButton;
        public TextView nameText;
        public CircularImageView profilePicture;
        public TextView stepsText;

        public ViewHolder(View view) {
            super(view);
        }

        private void visitProfile(View view) {
            if (TreadlyProfileFollowAdapter.this.mClickListener != null) {
                TreadlyProfileFollowAdapter.this.mClickListener.onAvatarClick(view, getAdapterPosition(), TreadlyProfileFollowAdapter.this.followers);
            }
        }

        private void followUser(View view) {
            if (TreadlyProfileFollowAdapter.this.mClickListener != null) {
                TreadlyProfileFollowAdapter.this.mClickListener.onFollowClick(view, getAdapterPosition(), TreadlyProfileFollowAdapter.this.followers, this.followButton);
            }
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
}
