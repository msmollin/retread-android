package com.treadly.Treadly.UI.TreadlyBuddy.Manage;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyBuddy.Manage.TreadlyBuddyProfileEditAdapter;
import java.util.List;

/* loaded from: classes2.dex */
public class TreadlyBuddyProfileEditAdapter extends RecyclerView.Adapter<ViewHolder> {
    private ItemClickListener mClickListener;
    private List<Pair<String, Boolean>> mData;
    private LayoutInflater mInflater;

    /* loaded from: classes2.dex */
    public interface ItemClickListener {
        void onClick(View view, int i);
    }

    public TreadlyBuddyProfileEditAdapter(Context context, List<Pair<String, Boolean>> list) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(this.mInflater.inflate(R.layout.buddy_profile_edit_row, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Pair<String, Boolean> pair = this.mData.get(i);
        viewHolder.interestText.setText((CharSequence) pair.first);
        viewHolder.checkedImg.setVisibility(((Boolean) pair.second).booleanValue() ? 0 : 4);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mData.size();
    }

    /* loaded from: classes2.dex */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView checkedImg;
        public TextView interestText;

        public ViewHolder(final View view) {
            super(view);
            this.interestText = (TextView) view.findViewById(R.id.buddy_profile_edit_item_text);
            this.checkedImg = (ImageView) view.findViewById(R.id.buddy_profile_edit_item_check);
            view.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.Manage.-$$Lambda$TreadlyBuddyProfileEditAdapter$ViewHolder$nAthOkX05zr5vjOVtgbfYVWLSDg
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    TreadlyBuddyProfileEditAdapter.ViewHolder.this.interestClicked(view);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void interestClicked(View view) {
            if (TreadlyBuddyProfileEditAdapter.this.mClickListener != null) {
                TreadlyBuddyProfileEditAdapter.this.mClickListener.onClick(view, getAdapterPosition());
            }
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
}
