package com.treadly.Treadly.UI.TreadlyProfile.ReportUser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.treadly.Treadly.Data.Model.ReportReasonInfo;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyProfile.ReportUser.ReportReasonAdapter;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class TreadlyProfileReportContentFragment extends BaseFragment implements ReportReasonAdapter.ItemClickListener {
    ReportReasonAdapter adapter;
    ImageView backArrow;
    public TreadlyReportCategory category;
    public String contentId;
    TextView headertitle;
    List<ReportReasonInfo> reportContent = new ArrayList();
    View reportContentView;
    RecyclerView reportReasonRecycler;

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_profile_report_content, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        this.reportContentView = view;
        this.headertitle = (TextView) view.findViewById(R.id.header_title);
        this.headertitle.setText("Report");
        this.backArrow = (ImageView) view.findViewById(R.id.back_arrow);
        this.backArrow.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.ReportUser.TreadlyProfileReportContentFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                TreadlyProfileReportContentFragment.this.getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        initViews();
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        loadReportReasons();
    }

    void initViews() {
        this.reportReasonRecycler = (RecyclerView) this.reportContentView.findViewById(R.id.report_reasons_list);
        this.reportReasonRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        this.adapter = new ReportReasonAdapter(getContext(), this.reportContent);
        this.adapter.setClickListener(this);
        this.reportReasonRecycler.setAdapter(this.adapter);
    }

    void loadReportReasons() {
        VideoServiceHelper.getReportReasons(new VideoServiceHelper.VideoGetReportReasonsListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.ReportUser.TreadlyProfileReportContentFragment.2
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoGetReportReasonsListener
            public void onResponse(String str, List<ReportReasonInfo> list) {
                if (str != null) {
                    return;
                }
                TreadlyProfileReportContentFragment.this.reportContent.addAll(list);
                TreadlyProfileReportContentFragment.this.adapter.notifyDataSetChanged();
            }
        });
    }

    @Override // com.treadly.Treadly.UI.TreadlyProfile.ReportUser.ReportReasonAdapter.ItemClickListener
    public void onReportReasonSelected(View view, int i) {
        TreadlyProfileReportSubmitFragment treadlyProfileReportSubmitFragment = new TreadlyProfileReportSubmitFragment();
        treadlyProfileReportSubmitFragment.reportInfo = this.adapter.reportReasons.get(i);
        treadlyProfileReportSubmitFragment.contentId = this.contentId;
        treadlyProfileReportSubmitFragment.reportCategory = this.category;
        getActivity().getSupportFragmentManager().popBackStack();
        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.activity_fragment_container_empty, treadlyProfileReportSubmitFragment, TreadlyProfileReportSubmitFragment.TAG).commit();
    }
}
