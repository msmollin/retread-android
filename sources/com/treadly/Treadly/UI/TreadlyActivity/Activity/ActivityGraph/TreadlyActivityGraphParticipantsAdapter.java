package com.treadly.Treadly.UI.TreadlyActivity.Activity.ActivityGraph;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.treadly.Treadly.Data.Model.UserRunningSessionParticipantInfo;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyActivity.Activity.ActivityGraph.TreadlyActivityGraphAdapter;
import java.io.PrintStream;
import java.util.List;

/* loaded from: classes2.dex */
public class TreadlyActivityGraphParticipantsAdapter extends RecyclerView.Adapter<ViewHolder> {
    private TreadlyActivityGraphAdapter.ItemClickListener clickListener;
    private Context context;
    private LayoutInflater inflater;
    private List<UserRunningSessionParticipantInfo> participants;

    /* loaded from: classes2.dex */
    public interface ItemClickListener {
        void onItemClick(View view, int i);
    }

    public TreadlyActivityGraphParticipantsAdapter(Context context, List<UserRunningSessionParticipantInfo> list) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.participants = list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(this.inflater.inflate(R.layout.participant_graph_avatar, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String str = this.participants.get(i).participantAvatar;
        PrintStream printStream = System.out;
        printStream.println("PROFILEURL: " + str);
        Picasso.get().load(str).fit().noFade().tag(this.context).into(viewHolder.participantAvatar);
    }

    /* loaded from: classes2.dex */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircularImageView participantAvatar;

        ViewHolder(View view) {
            super(view);
            this.participantAvatar = (CircularImageView) view.findViewById(R.id.participant_avatar_for_graph);
            this.participantAvatar.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (TreadlyActivityGraphParticipantsAdapter.this.clickListener != null) {
                TreadlyActivityGraphParticipantsAdapter.this.clickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.participants.size();
    }

    public UserRunningSessionParticipantInfo getItem(int i) {
        return this.participants.get(i);
    }
}
