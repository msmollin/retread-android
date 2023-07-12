package com.treadly.Treadly.UI.TreadlyCommunity.DiscoverFollowers;

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
import com.treadly.Treadly.Data.Model.UserDiscoverInfo;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyCommunity.DiscoverFollowers.TreadlyDiscoverFollowersAdapter;
import java.security.AccessController;
import java.util.List;

/* loaded from: classes2.dex */
public class TreadlyDiscoverFollowersAdapter extends RecyclerView.Adapter<ViewHolder> {
    private ItemClickListener mClickListener;
    private List<UserDiscoverInfo> mData;
    private LayoutInflater mInflater;

    /* loaded from: classes2.dex */
    public interface ItemClickListener {
        void onAvatarClick(View view, int i);

        void onFollowClick(View view, int i, Button button);
    }

    public TreadlyDiscoverFollowersAdapter(Context context, List<UserDiscoverInfo> list) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(this.mInflater.inflate(R.layout.discover_user_row, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        UserDiscoverInfo userDiscoverInfo = this.mData.get(i);
        viewHolder.nameText.setText(userDiscoverInfo.name);
        TextView textView = viewHolder.stepsText;
        textView.setText("" + userDiscoverInfo.stepsTotal);
        viewHolder.descriptionText.setText(userDiscoverInfo.descriptionProfile);
        Picasso.get().load(Uri.parse(userDiscoverInfo.avatarPath)).tag(AccessController.getContext()).into(viewHolder.profilePicture);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mData.size();
    }

    /* loaded from: classes2.dex */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView descriptionText;
        public Button followButton;
        public TextView nameText;
        public CircularImageView profilePicture;
        public TextView stepsText;

        public ViewHolder(final View view) {
            super(view);
            this.nameText = (TextView) view.findViewById(R.id.discover_user_item_name);
            this.stepsText = (TextView) view.findViewById(R.id.discover_user_item_steps);
            this.descriptionText = (TextView) view.findViewById(R.id.discover_user_item_description);
            this.profilePicture = (CircularImageView) view.findViewById(R.id.discover_user_item_avatar);
            this.profilePicture.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyCommunity.DiscoverFollowers.-$$Lambda$TreadlyDiscoverFollowersAdapter$ViewHolder$BKbyrSjes838z2ZKMwGOZkKfUX8
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    TreadlyDiscoverFollowersAdapter.ViewHolder.this.visitProfile(view);
                }
            });
            this.followButton = (Button) view.findViewById(R.id.discover_user_follow_button);
            this.followButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyCommunity.DiscoverFollowers.-$$Lambda$TreadlyDiscoverFollowersAdapter$ViewHolder$2BMeN-s7EWa5lb12vjAqHjNg-uE
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    TreadlyDiscoverFollowersAdapter.ViewHolder.this.followDiscoveredUser(view);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void visitProfile(View view) {
            if (TreadlyDiscoverFollowersAdapter.this.mClickListener != null) {
                TreadlyDiscoverFollowersAdapter.this.mClickListener.onAvatarClick(view, getAdapterPosition());
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void followDiscoveredUser(View view) {
            if (TreadlyDiscoverFollowersAdapter.this.mClickListener != null) {
                TreadlyDiscoverFollowersAdapter.this.mClickListener.onFollowClick(view, getAdapterPosition(), this.followButton);
            }
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
}
