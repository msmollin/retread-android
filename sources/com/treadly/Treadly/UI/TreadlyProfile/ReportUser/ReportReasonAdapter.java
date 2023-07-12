package com.treadly.Treadly.UI.TreadlyProfile.ReportUser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.treadly.Treadly.Data.Model.ReportReasonInfo;
import com.treadly.Treadly.R;
import java.util.List;

/* loaded from: classes2.dex */
public class ReportReasonAdapter extends RecyclerView.Adapter<ViewHolder> {
    ItemClickListener clickListener;
    Context context;
    List<ReportReasonInfo> reportReasons;

    /* loaded from: classes2.dex */
    public interface ItemClickListener {
        void onReportReasonSelected(View view, int i);
    }

    public ReportReasonAdapter(Context context, List<ReportReasonInfo> list) {
        this.context = context;
        this.reportReasons = list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.report_reason_single_item, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.reportReason.setText(this.reportReasons.get(i).title);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.reportReasons.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    /* loaded from: classes2.dex */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView reportReason;

        ViewHolder(View view) {
            super(view);
            this.reportReason = (TextView) view.findViewById(R.id.single_report_reason);
            this.reportReason.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ReportReasonAdapter.this.clickListener != null) {
                ReportReasonAdapter.this.clickListener.onReportReasonSelected(view, getAdapterPosition());
            }
        }
    }
}
