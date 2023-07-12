package com.treadly.Treadly.UI.TreadlyProfile.ReportUser;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import com.treadly.Treadly.Data.Model.ReportReasonInfo;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;

/* loaded from: classes2.dex */
public class TreadlyProfileReportSubmitFragment extends BaseFragment {
    public static final String TAG = "Report_Submit_Fragment";
    ImageView backArrow;
    public String contentId;
    TextView headertitle;
    AppCompatEditText optionalDescription;
    public TreadlyReportCategory reportCategory;
    public ReportReasonInfo reportInfo;
    TextView reportReason;
    Button submitButton;
    View submitView;

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_profile_report_submit, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        this.submitView = view;
        this.headertitle = (TextView) view.findViewById(R.id.header_title);
        this.headertitle.setText("Report");
        this.backArrow = (ImageView) view.findViewById(R.id.back_arrow);
        this.backArrow.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.ReportUser.TreadlyProfileReportSubmitFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                TreadlyProfileReportSubmitFragment.this.getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        initViews();
    }

    void initViews() {
        this.reportReason = (TextView) this.submitView.findViewById(R.id.report_reason);
        this.reportReason.setText(this.reportInfo.title);
        this.submitButton = (Button) this.submitView.findViewById(R.id.report_reason_submit_button);
        this.submitButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.ReportUser.TreadlyProfileReportSubmitFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                TreadlyProfileReportSubmitFragment.this.submit();
            }
        });
        this.optionalDescription = (AppCompatEditText) this.submitView.findViewById(R.id.report_reason_text_field);
    }

    void submit() {
        String obj = this.optionalDescription.getText().toString();
        this.submitButton.setClickable(false);
        hideKeyboardFrom(getContext(), this.submitView);
        switch (this.reportCategory) {
            case user:
                VideoServiceHelper.createUserReport(this.contentId, this.reportInfo.id, obj, new VideoServiceHelper.VideoResponseListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.ReportUser.TreadlyProfileReportSubmitFragment.3
                    @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoResponseListener
                    public void onResponse(String str) {
                        TreadlyProfileReportSubmitFragment.this.handleSubmitResponse(str);
                    }
                });
                return;
            case video:
                VideoServiceHelper.createVideoReport(this.contentId, this.reportInfo.id, obj, new VideoServiceHelper.VideoResponseListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.ReportUser.TreadlyProfileReportSubmitFragment.4
                    @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoResponseListener
                    public void onResponse(String str) {
                        TreadlyProfileReportSubmitFragment.this.handleSubmitResponse(str);
                    }
                });
                return;
            default:
                return;
        }
    }

    void handleSubmitResponse(String str) {
        this.submitButton.setClickable(true);
        if (str != null) {
            showSubmitError();
        } else {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    void showSubmitError() {
        new AlertDialog.Builder(getContext()).setTitle("Error").setMessage("Error submitting report").setNeutralButton("Dismiss", (DialogInterface.OnClickListener) null).show();
    }

    public static void hideKeyboardFrom(Context context, View view) {
        ((InputMethodManager) context.getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
