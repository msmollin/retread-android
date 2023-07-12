package com.treadly.Treadly.UI.TreadlyConnect.TreadlyFriends;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.treadly.Treadly.Data.Model.UserActivityInfo;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.R;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class TreadlyFriendsAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Map<String, UserActivityInfo> activityInfo;
    private ItemClickListener clickListener;
    private Context context;
    private List<UserInfo> friends;
    boolean fromConnect;
    private LayoutInflater inflater;

    /* loaded from: classes2.dex */
    public interface ItemClickListener {
        void onItemClick(View view, int i);
    }

    public TreadlyFriendsAdapter(Context context, List<UserInfo> list, Map<String, UserActivityInfo> map, boolean z) {
        this.inflater = LayoutInflater.from(context);
        this.friends = list;
        this.activityInfo = map;
        this.context = context;
        this.fromConnect = z;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate;
        if (this.fromConnect) {
            inflate = this.inflater.inflate(R.layout.connect_friends_single_friend, viewGroup, false);
        } else {
            inflate = this.inflater.inflate(R.layout.popup_friends_single_friend, viewGroup, false);
        }
        return new ViewHolder(inflate);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.circleCrop();
        Glide.with(this.context).load(Uri.parse(this.friends.get(i).avatarPath)).apply(requestOptions).into(viewHolder.friendPic);
        if (this.activityInfo.get(this.friends.get(i).id).isBroadcasting) {
            viewHolder.friendPic.setBackgroundResource(R.drawable.connect_friend_collection_broadcast_border);
        } else {
            viewHolder.friendPic.setBackgroundResource(R.drawable.connect_friend_collection_border);
        }
        viewHolder.friendUsername.setText(this.friends.get(i).name);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.friends.size();
    }

    /* loaded from: classes2.dex */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView friendPic;
        TextView friendUsername;

        ViewHolder(View view) {
            super(view);
            if (TreadlyFriendsAdapter.this.fromConnect) {
                this.friendPic = (ImageView) view.findViewById(R.id.connect_friend_cell);
            } else {
                this.friendPic = (ImageView) view.findViewById(R.id.popup_friend_cell);
            }
            this.friendUsername = (TextView) view.findViewById(R.id.connect_friend_name);
            this.friendPic.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (TreadlyFriendsAdapter.this.clickListener != null) {
                TreadlyFriendsAdapter.this.clickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public void setData(List<UserInfo> list, Map<String, UserActivityInfo> map) {
        this.friends = list;
        this.activityInfo = map;
    }
}
