package com.treadly.Treadly.UI.TreadlyBuddy.Manage.Interests;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.treadly.Treadly.Data.Model.BuddyProfileInfo;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyBuddy.Manage.TreadlyBuddyProfileEditAdapter;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class TreadlyBuddyProfileEditInterestsFragment extends BaseFragment implements TreadlyBuddyProfileEditAdapter.ItemClickListener {
    public static final String TAG = "TREADLY_BUDDY_PROFILE_EDIT_INTERESTS";
    private TreadlyBuddyProfileEditAdapter adapter;
    private RecyclerView interestListView;
    public OnReturnListener retListener;
    public List<BuddyProfileInfo.BuddyInterest> userInterests;
    private List<BuddyProfileInfo.BuddyInterest> interestList = new ArrayList();
    private List<Pair<String, Boolean>> adapterList = new ArrayList();

    /* loaded from: classes2.dex */
    public interface OnReturnListener {
        void onBuddyProfileEditInterestReturn();
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_buddy_profile_edit_interests, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        initHeader(view);
        if (this.userInterests == null) {
            System.out.println("BTM :: TreadlyBuddyProfileEditInterestsFragment.onViewCreated() error: userIntests == null");
            return;
        }
        getBuddyInterests();
        this.interestListView = (RecyclerView) view.findViewById(R.id.buddy_interest_list);
        this.interestListView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.adapter = new TreadlyBuddyProfileEditAdapter(getContext(), this.adapterList);
        this.adapter.setClickListener(this);
        this.interestListView.setAdapter(this.adapter);
        this.adapter.notifyDataSetChanged();
    }

    @Override // com.treadly.Treadly.UI.TreadlyBuddy.Manage.TreadlyBuddyProfileEditAdapter.ItemClickListener
    public void onClick(View view, int i) {
        String str = (String) this.adapterList.get(i).first;
        boolean booleanValue = ((Boolean) this.adapterList.get(i).second).booleanValue();
        this.adapterList.remove(i);
        this.adapterList.add(i, new Pair<>(str, Boolean.valueOf(!booleanValue)));
        int i2 = 0;
        if (booleanValue) {
            while (true) {
                if (i2 >= this.userInterests.size()) {
                    break;
                } else if (this.userInterests.get(i2).title.equals(str)) {
                    this.userInterests.remove(i2);
                    break;
                } else {
                    i2++;
                }
            }
        } else {
            String str2 = null;
            while (true) {
                if (i2 >= this.interestList.size()) {
                    break;
                } else if (this.interestList.get(i2).title.equals(str)) {
                    str2 = this.interestList.get(i2).id;
                    break;
                } else {
                    i2++;
                }
            }
            if (str2 != null) {
                this.userInterests.add(new BuddyProfileInfo.BuddyInterest(str, str2));
            }
        }
        updateUi();
    }

    private void initHeader(View view) {
        ((TextView) view.findViewById(R.id.nav_title)).setText(R.string.buddy_profile_settings_edit_interests_title);
        ((ImageView) view.findViewById(R.id.nav_back_arrow)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.Manage.Interests.-$$Lambda$TreadlyBuddyProfileEditInterestsFragment$6NcfIlj6QXsf9LngqPyAIMnr0Og
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyBuddyProfileEditInterestsFragment.lambda$initHeader$0(TreadlyBuddyProfileEditInterestsFragment.this, view2);
            }
        });
    }

    public static /* synthetic */ void lambda$initHeader$0(TreadlyBuddyProfileEditInterestsFragment treadlyBuddyProfileEditInterestsFragment, View view) {
        treadlyBuddyProfileEditInterestsFragment.popBackStack();
        treadlyBuddyProfileEditInterestsFragment.retListener.onBuddyProfileEditInterestReturn();
    }

    private void updateUi() {
        this.adapter.notifyDataSetChanged();
    }

    private void getBuddyInterests() {
        showLoading();
        VideoServiceHelper.getBuddyProfileInterests(new VideoServiceHelper.BuddyGetInterestsListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.Manage.Interests.-$$Lambda$TreadlyBuddyProfileEditInterestsFragment$Z34z020K7Nu4aiM4JvewH4Qf-ec
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.BuddyGetInterestsListener
            public final void onResponse(String str, List list) {
                TreadlyBuddyProfileEditInterestsFragment.lambda$getBuddyInterests$1(TreadlyBuddyProfileEditInterestsFragment.this, str, list);
            }
        });
    }

    public static /* synthetic */ void lambda$getBuddyInterests$1(TreadlyBuddyProfileEditInterestsFragment treadlyBuddyProfileEditInterestsFragment, String str, List list) {
        if (str != null || list == null) {
            PrintStream printStream = System.out;
            printStream.println("BTM :: ERROR REQUESTING BUDDY INTERESTS + " + str);
            return;
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            BuddyProfileInfo.BuddyInterest buddyInterest = (BuddyProfileInfo.BuddyInterest) it.next();
            boolean z = false;
            Iterator<BuddyProfileInfo.BuddyInterest> it2 = treadlyBuddyProfileEditInterestsFragment.userInterests.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                } else if (buddyInterest.title.equals(it2.next().title)) {
                    z = true;
                    break;
                }
            }
            Pair<String, Boolean> pair = new Pair<>(buddyInterest.title, Boolean.valueOf(z));
            treadlyBuddyProfileEditInterestsFragment.interestList.add(buddyInterest);
            treadlyBuddyProfileEditInterestsFragment.adapterList.add(pair);
        }
        treadlyBuddyProfileEditInterestsFragment.updateUi();
        treadlyBuddyProfileEditInterestsFragment.dismissLoading();
    }
}
