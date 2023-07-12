package com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamMessagingView;

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
import com.treadly.Treadly.Data.Model.UserComment;
import com.treadly.Treadly.R;
import java.util.List;

/* loaded from: classes2.dex */
public class TreadlyPublicStreamMessagingViewAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<UserComment> messages;

    public TreadlyPublicStreamMessagingViewAdapter(Context context, List<UserComment> list) {
        this.context = context;
        this.messages = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.messages.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(this.inflater.inflate(R.layout.public_broadcast_comment_single_row, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.circleCrop();
        Glide.with(this.context).load(Uri.parse(this.messages.get(i).user.avatarURL())).apply(requestOptions).into(viewHolder.avatar);
        String str = this.messages.get(i).user.name;
        viewHolder.username.setText(str);
        viewHolder.message.setText("\t" + this.messages.get(i).comment);
    }

    /* loaded from: classes2.dex */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView message;
        TextView username;

        ViewHolder(View view) {
            super(view);
            this.avatar = (ImageView) view.findViewById(R.id.host_public_broadcast_comment_avatar);
            this.username = (TextView) view.findViewById(R.id.host_public_broadcast_username);
            this.message = (TextView) view.findViewById(R.id.host_public_broadcast_message);
        }
    }
}
