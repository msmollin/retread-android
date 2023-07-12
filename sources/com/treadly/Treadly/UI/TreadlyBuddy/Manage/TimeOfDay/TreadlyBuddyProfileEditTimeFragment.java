package com.treadly.Treadly.UI.TreadlyBuddy.Manage.TimeOfDay;

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
public class TreadlyBuddyProfileEditTimeFragment extends BaseFragment implements TreadlyBuddyProfileEditAdapter.ItemClickListener {
    public static final String TAG = "TREADLY_BUDDY_PROFILE_EDIT_TOD";
    private TreadlyBuddyProfileEditAdapter adapter;
    private RecyclerView daysAvailableView;
    public OnReturnListener retListener;
    public List<BuddyProfileInfo.BuddyDayTime> userDaysAvailable;
    public int dayId = -1;
    private List<BuddyProfileInfo.BuddyDayTimeType> timeOfDayList = new ArrayList();
    private List<Pair<String, Boolean>> adapterList = new ArrayList();

    /* loaded from: classes2.dex */
    public interface OnReturnListener {
        void onBuddyProfileEditTimeOfDayReturn();
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_buddy_profile_edit_tod, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        initHeader(view);
        if (this.userDaysAvailable == null || this.dayId == -1) {
            System.out.println("BTM :: TreadlyBuddyProfileEditTimeFragment.onViewCreated() error: userDaysAvailable or dayId not initialized");
            return;
        }
        getBuddyTimesOfDay();
        this.daysAvailableView = (RecyclerView) view.findViewById(R.id.buddy_tod_list);
        this.daysAvailableView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.adapter = new TreadlyBuddyProfileEditAdapter(getContext(), this.adapterList);
        this.adapter.setClickListener(this);
        this.daysAvailableView.setAdapter(this.adapter);
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
                if (i2 < this.userDaysAvailable.size()) {
                    if (this.userDaysAvailable.get(i2).dayTime.title.equals(str) && this.userDaysAvailable.get(i2).dayOfWeek == this.dayId) {
                        this.userDaysAvailable.remove(i2);
                        break;
                    }
                    i2++;
                } else {
                    break;
                }
            }
        } else {
            String str2 = null;
            while (true) {
                if (i2 >= this.timeOfDayList.size()) {
                    break;
                } else if (this.timeOfDayList.get(i2).title.equals(str)) {
                    str2 = this.timeOfDayList.get(i2).id;
                    break;
                } else {
                    i2++;
                }
            }
            if (str2 != null) {
                this.userDaysAvailable.add(new BuddyProfileInfo.BuddyDayTime("", this.dayId, new BuddyProfileInfo.BuddyDayTimeType(str, str2)));
            }
        }
        updateUi();
    }

    private void initHeader(View view) {
        ((TextView) view.findViewById(R.id.nav_title)).setText(R.string.buddy_profile_settings_edit_tod_title);
        ((ImageView) view.findViewById(R.id.nav_back_arrow)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.Manage.TimeOfDay.-$$Lambda$TreadlyBuddyProfileEditTimeFragment$cAYxdZhbYyySoVEPQGOoG04JuuQ
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyBuddyProfileEditTimeFragment.lambda$initHeader$0(TreadlyBuddyProfileEditTimeFragment.this, view2);
            }
        });
    }

    public static /* synthetic */ void lambda$initHeader$0(TreadlyBuddyProfileEditTimeFragment treadlyBuddyProfileEditTimeFragment, View view) {
        treadlyBuddyProfileEditTimeFragment.popBackStack();
        treadlyBuddyProfileEditTimeFragment.retListener.onBuddyProfileEditTimeOfDayReturn();
    }

    private void updateUi() {
        this.adapter.notifyDataSetChanged();
    }

    private void getBuddyTimesOfDay() {
        showLoading();
        VideoServiceHelper.getBuddyProfileTimesOfDay(new VideoServiceHelper.BuddyGetTimeOfDayListener() { // from class: com.treadly.Treadly.UI.TreadlyBuddy.Manage.TimeOfDay.-$$Lambda$TreadlyBuddyProfileEditTimeFragment$0MU2_12ywVmcCKcfHMsa2xfSVVs
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.BuddyGetTimeOfDayListener
            public final void onResponse(String str, List list) {
                TreadlyBuddyProfileEditTimeFragment.lambda$getBuddyTimesOfDay$1(TreadlyBuddyProfileEditTimeFragment.this, str, list);
            }
        });
    }

    public static /* synthetic */ void lambda$getBuddyTimesOfDay$1(TreadlyBuddyProfileEditTimeFragment treadlyBuddyProfileEditTimeFragment, String str, List list) {
        if (str != null || list == null) {
            PrintStream printStream = System.out;
            printStream.println("BTM :: ERROR REQUESTING BUDDY TIMES OF DAY + " + str);
            return;
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            BuddyProfileInfo.BuddyDayTimeType buddyDayTimeType = (BuddyProfileInfo.BuddyDayTimeType) it.next();
            boolean z = false;
            Iterator<BuddyProfileInfo.BuddyDayTime> it2 = treadlyBuddyProfileEditTimeFragment.userDaysAvailable.iterator();
            while (true) {
                if (it2.hasNext()) {
                    BuddyProfileInfo.BuddyDayTime next = it2.next();
                    if (next.dayTime.title.equals(buddyDayTimeType.title) && next.dayOfWeek == treadlyBuddyProfileEditTimeFragment.dayId) {
                        z = true;
                        break;
                    }
                }
            }
            Pair<String, Boolean> pair = new Pair<>(buddyDayTimeType.title, Boolean.valueOf(z));
            treadlyBuddyProfileEditTimeFragment.timeOfDayList.add(buddyDayTimeType);
            treadlyBuddyProfileEditTimeFragment.adapterList.add(pair);
        }
        treadlyBuddyProfileEditTimeFragment.updateUi();
        treadlyBuddyProfileEditTimeFragment.dismissLoading();
    }
}
