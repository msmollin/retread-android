package com.treadly.Treadly.UI.TreadlyProfile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.treadly.Treadly.Data.Model.StreamPermission;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceVideoInfo;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class TreadlyProfileVideoCollectionAdapter extends RecyclerView.Adapter<ViewHolder> {
    private ItemClickListener clickListener;
    private final Context context;
    boolean isCurrentUser;
    public TreadlyProfileVideoCollectionAdapterEventListener listener;
    UserInfo user;
    private List<VideoServiceVideoInfo> videoInfos = new ArrayList();

    /* loaded from: classes2.dex */
    public interface ItemClickListener {
        void onVideoSelected(View view, int i);
    }

    /* loaded from: classes2.dex */
    public interface TreadlyProfileVideoCollectionAdapterEventListener {
        void onVideoSelected(VideoServiceVideoInfo videoServiceVideoInfo);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        return i;
    }

    public TreadlyProfileVideoCollectionAdapter(Context context, UserInfo userInfo) {
        this.context = context;
        this.user = userInfo;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.video_cell, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String str = getItem(i).thumbnailUrl;
        if (str.isEmpty() || str.equals("null")) {
            str = this.user.avatarPath;
        }
        Picasso.get().load(str).fit().noFade().tag(this.context).into(viewHolder.iv);
        if (getItem(i).permission == StreamPermission.friendsStream || getItem(i).permission == StreamPermission.publicStream) {
            viewHolder.videoIcon.setBackgroundResource(R.drawable.video_friends);
        } else {
            viewHolder.videoIcon.setBackgroundResource(R.drawable.video_lock);
        }
    }

    public VideoServiceVideoInfo getItem(int i) {
        return this.videoInfos.get(i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.videoInfos.size();
    }

    public void setVideoInfos(List<VideoServiceVideoInfo> list) {
        this.videoInfos = list;
        notifyDataSetChanged();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    /* loaded from: classes2.dex */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iv;
        ImageView videoIcon;

        ViewHolder(View view) {
            super(view);
            this.iv = (ImageView) view.findViewById(R.id.video_image_cell);
            this.videoIcon = (ImageView) view.findViewById(R.id.video_image_cell_icon);
            this.iv.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (TreadlyProfileVideoCollectionAdapter.this.clickListener != null) {
                TreadlyProfileVideoCollectionAdapter.this.clickListener.onVideoSelected(view, getAdapterPosition());
            }
        }
    }
}
