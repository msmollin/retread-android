package com.treadly.Treadly.UI.TreadlyActivity.Activity.ActivityGraph;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.Data.Model.UserRunningSessionInfo;
import com.treadly.Treadly.R;
import java.util.List;

/* loaded from: classes2.dex */
public class TreadlyActivityGraphAdapter extends RecyclerView.Adapter<ViewHolder> {
    private ItemClickListener clickListener;
    private Context context;
    private LayoutInflater inflater;
    private UserInfo userInfo;
    public List<UserRunningSessionInfo> userRunningSessionInfos;

    /* loaded from: classes2.dex */
    public interface ItemClickListener {
        void onItemClick(View view, int i);
    }

    public TreadlyActivityGraphAdapter(Context context, List<UserRunningSessionInfo> list, UserInfo userInfo) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.userRunningSessionInfos = list;
        this.userInfo = userInfo;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(this.inflater.inflate(R.layout.single_graph, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.graph.setDataCount(this.userRunningSessionInfos.get(i), i);
    }

    /* loaded from: classes2.dex */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TreadlyActivityGraph graph;

        ViewHolder(View view) {
            super(view);
            this.graph = new TreadlyActivityGraph(view, TreadlyActivityGraphAdapter.this.context, TreadlyActivityGraphAdapter.this.userInfo);
            view.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (TreadlyActivityGraphAdapter.this.clickListener != null) {
                TreadlyActivityGraphAdapter.this.clickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.userRunningSessionInfos.size();
    }

    public UserRunningSessionInfo getItem(int i) {
        return this.userRunningSessionInfos.get(i);
    }
}
