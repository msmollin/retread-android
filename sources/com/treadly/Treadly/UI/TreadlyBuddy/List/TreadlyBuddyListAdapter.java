package com.treadly.Treadly.UI.TreadlyBuddy.List;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.treadly.Treadly.Data.Model.BuddyProfileInfo;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyBuddy.List.TreadlyBuddyListAdapter;
import java.security.AccessController;
import java.util.List;

/* loaded from: classes2.dex */
public class TreadlyBuddyListAdapter extends RecyclerView.Adapter<ViewHolder> {
    private ItemClickListener mClickListener;
    private List<BuddyProfileInfo> mData;
    private LayoutInflater mInflater;

    /* loaded from: classes2.dex */
    public interface ItemClickListener {
        void onClick(View view, int i);
    }

    public TreadlyBuddyListAdapter(Context context, List<BuddyProfileInfo> list) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(this.mInflater.inflate(R.layout.buddy_info_row, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        BuddyProfileInfo buddyProfileInfo = this.mData.get(i);
        Picasso.get().load(Uri.parse(buddyProfileInfo.avatarPath)).tag(AccessController.getContext()).into(viewHolder.profilePicture);
        TextView textView = viewHolder.nameAndSteps;
        textView.setText(buddyProfileInfo.userName + "          " + buddyProfileInfo.step);
        viewHolder.descriptionText.setText(buddyProfileInfo.lookingForMessage);
        viewHolder.locationText.setText(buddyProfileInfo.location);
        StringBuilder sb = new StringBuilder();
        for (BuddyProfileInfo.BuddyInterest buddyInterest : buddyProfileInfo.interests) {
            sb.append(buddyInterest.title);
            sb.append(", ");
        }
        if (sb.length() == 0) {
            sb.append("< No Interests >");
        } else {
            sb.setLength(sb.length() - 2);
        }
        viewHolder.interestsText.setText(sb.toString());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mData.size();
    }

    /* loaded from: classes2.dex */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView descriptionText;
        public TextView interestsText;
        public TextView locationText;
        public TextView nameAndSteps;
        public CircularImageView profilePicture;

        public ViewHolder(final View view) {
            super(view);
            this.profilePicture = (CircularImageView) view.findViewById(R.id.find_buddy_item_avatar);
            this.nameAndSteps = (TextView) view.findViewById(R.id.find_buddy_item_name_and_steps);
            this.descriptionText = (TextView) view.findViewById(R.id.find_buddy_item_description);
            this.locationText = (TextView) view.findViewById(R.id.find_buddy_item_location);
            this.interestsText = (TextView) view.findViewById(R.id.find_buddy_item_interests);
            view.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.List.-$$Lambda$TreadlyBuddyListAdapter$ViewHolder$Vx-_2MIpbBxOyEArW6mUtRE1lWs
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    TreadlyBuddyListAdapter.ViewHolder.this.visitBuddyProfile(view);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void visitBuddyProfile(View view) {
            if (TreadlyBuddyListAdapter.this.mClickListener != null) {
                TreadlyBuddyListAdapter.this.mClickListener.onClick(view, getAdapterPosition());
            }
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
}
