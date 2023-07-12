package com.treadly.Treadly.UI.TreadlyConnect.AlertView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import com.squareup.picasso.Picasso;
import com.treadly.Treadly.Data.Managers.DeviceUserStatsLogManager;
import com.treadly.Treadly.R;
import com.yalantis.ucrop.UCrop;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

/* loaded from: classes2.dex */
public class TreadlyConnectPauseView extends FrameLayout {
    public static final String TAG = "CONNECT_PAUSE_VIEW";
    public Activity activity;
    private TextView averageSpeed;
    private TextView calories;
    private int dailyGoal;
    private CardView dailyGoalBackground;
    private CardView dailyGoalProgress;
    private TextView distance;
    private Button finishButton;
    public Fragment fragment;
    public TreadlyConnectPauseViewListener listener;
    private Button resumeButton;
    private CardView snapshotCardView;
    private EditText snapshotCommentText;
    private RelativeLayout snapshotContainer;
    private ImageView snapshotImageView;
    private TextView steps;
    private Uri tempUrl;
    private TextView totalGoal;

    /* loaded from: classes2.dex */
    public interface TreadlyConnectPauseViewListener {
        void onDidPressResume();

        void onDidPressStop();
    }

    public TreadlyConnectPauseView(@NonNull Context context) {
        super(context);
        this.tempUrl = null;
        configure();
    }

    public TreadlyConnectPauseView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.tempUrl = null;
        configure();
    }

    public TreadlyConnectPauseView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.tempUrl = null;
        configure();
    }

    public TreadlyConnectPauseView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.tempUrl = null;
        configure();
    }

    private void configure() {
        View inflate = inflate(getContext(), R.layout.layout_treadly_connect_pause, this);
        this.finishButton = (Button) inflate.findViewById(R.id.finish_button);
        this.finishButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.AlertView.-$$Lambda$TreadlyConnectPauseView$o2ms_OjB8MYivtzavw1Y75KawFE
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TreadlyConnectPauseView.this.handleFinishPressed();
            }
        });
        this.resumeButton = (Button) inflate.findViewById(R.id.resume_button);
        this.resumeButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.AlertView.-$$Lambda$TreadlyConnectPauseView$qIsUi1bw3ho-66M9p8aHrIkqHyI
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TreadlyConnectPauseView.this.handleResumePressed();
            }
        });
        this.snapshotImageView = (ImageView) inflate.findViewById(R.id.snapshot_image_view);
        this.snapshotImageView.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.AlertView.-$$Lambda$TreadlyConnectPauseView$w2inU01JDdEyaP1DmoLqWj2RTe4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TreadlyConnectPauseView.this.handleSnapshotPressed();
            }
        });
        this.snapshotCardView = (CardView) inflate.findViewById(R.id.snapshot_card_view);
        this.snapshotCardView.setClipToOutline(true);
        this.snapshotContainer = (RelativeLayout) inflate.findViewById(R.id.snapshot_container);
        this.snapshotContainer.setClipToOutline(true);
        this.snapshotCommentText = (EditText) inflate.findViewById(R.id.snapshot_edit_text);
        this.snapshotCommentText.setFilters(new InputFilter[]{new InputFilter() { // from class: com.treadly.Treadly.UI.TreadlyConnect.AlertView.-$$Lambda$TreadlyConnectPauseView$lXaRJh7X93pRyKEzyRpWjaevpgk
            @Override // android.text.InputFilter
            public final CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
                return TreadlyConnectPauseView.lambda$configure$3(charSequence, i, i2, spanned, i3, i4);
            }
        }, new InputFilter.LengthFilter(32)});
        this.snapshotCommentText.setText(snapshotComment() != null ? snapshotComment() : "");
        this.snapshotCommentText.addTextChangedListener(new TextWatcher() { // from class: com.treadly.Treadly.UI.TreadlyConnect.AlertView.TreadlyConnectPauseView.1
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence.length() == 0) {
                    TreadlyConnectPauseView.this.setSnapshotComment(null);
                } else {
                    TreadlyConnectPauseView.this.setSnapshotComment(charSequence.toString());
                }
            }
        });
        this.steps = (TextView) inflate.findViewById(R.id.steps);
        this.distance = (TextView) inflate.findViewById(R.id.distance);
        this.calories = (TextView) inflate.findViewById(R.id.calories);
        this.averageSpeed = (TextView) inflate.findViewById(R.id.average_speed);
        this.totalGoal = (TextView) inflate.findViewById(R.id.daily_goal_text);
        this.dailyGoalProgress = (CardView) inflate.findViewById(R.id.daily_goal_progress);
        this.dailyGoalBackground = (CardView) inflate.findViewById(R.id.daily_goal_background);
        ((FrameLayout.LayoutParams) this.dailyGoalProgress.getLayoutParams()).rightMargin = this.dailyGoalBackground.getWidth();
        setSnapshotImageView();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ CharSequence lambda$configure$3(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
        if (charSequence != null) {
            String charSequence2 = charSequence.toString();
            if (charSequence2.contains("\n")) {
                return charSequence2.replaceAll("\n", "");
            }
            return null;
        }
        return null;
    }

    public void setStats(String str, String str2, String str3, String str4, int i) {
        if (this.calories != null) {
            this.calories.setText(str);
        }
        if (this.steps != null) {
            this.steps.setText(str2);
        }
        if (this.averageSpeed != null) {
            this.averageSpeed.setText(str3);
        }
        if (this.distance != null) {
            this.distance.setText(str4);
        }
        this.dailyGoal = i;
        if (this.totalGoal != null) {
            this.totalGoal.setText(String.format("%s%%", new DecimalFormat("#,###,###").format(i)));
        }
    }

    public void updateCalories(String str, int i, String str2) {
        if (this.calories != null) {
            this.calories.setText(str);
        }
        this.dailyGoal = i;
        if (this.totalGoal != null) {
            this.totalGoal.setText(String.format("%s%%", new DecimalFormat("#,###,###").format(i)));
        }
        if (this.averageSpeed != null) {
            this.averageSpeed.setText(str2);
        }
    }

    private void setDailyGoal() {
        if (this.dailyGoalBackground == null) {
            return;
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.dailyGoalProgress.getLayoutParams();
        float width = this.dailyGoalBackground.getWidth();
        int max = (int) Math.max(width - (Math.min(1.0f, this.dailyGoal / 100.0f) * width), 0.0f);
        if (max != layoutParams.rightMargin) {
            layoutParams.rightMargin = max;
            this.dailyGoalProgress.setLayoutParams(layoutParams);
        }
    }

    private Uri snapshotUrl() {
        return DeviceUserStatsLogManager.getInstance().pendingActivityImage;
    }

    private void setSnapshotUrl(Uri uri) {
        DeviceUserStatsLogManager.getInstance().pendingActivityImage = uri;
        setSnapshotImageView();
    }

    private String snapshotComment() {
        return DeviceUserStatsLogManager.getInstance().pendingActivityComment;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSnapshotComment(String str) {
        DeviceUserStatsLogManager.getInstance().pendingActivityComment = str;
    }

    private void setSnapshotImageView() {
        if (this.snapshotImageView == null) {
            return;
        }
        if (snapshotUrl() != null) {
            Picasso.get().load(snapshotUrl()).tag(getContext()).into(this.snapshotImageView);
        } else {
            this.snapshotImageView.setImageDrawable(getContext().getDrawable(R.drawable.snapshot_icon));
        }
        setSnapshotConstraints();
    }

    private void setSnapshotConstraints() {
        if (this.snapshotCardView != null) {
            this.snapshotCardView.setRadius(this.snapshotCardView.getHeight() / 2.0f);
        }
        this.snapshotImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleFinishPressed() {
        if (this.listener != null) {
            this.listener.onDidPressStop();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleResumePressed() {
        if (this.listener != null) {
            this.listener.onDidPressResume();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSnapshotPressed() {
        showMoreAlert();
    }

    void showMoreAlert() {
        new AlertDialog.Builder(getContext()).setTitle("Change Profile Picture").setItems(new String[]{"Take Photo", "Choose Photo", "Cancel"}, new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.AlertView.-$$Lambda$TreadlyConnectPauseView$vYM3hP_AET7mP0QW4iiJHxgbitQ
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                TreadlyConnectPauseView.lambda$showMoreAlert$4(TreadlyConnectPauseView.this, dialogInterface, i);
            }
        }).show();
    }

    public static /* synthetic */ void lambda$showMoreAlert$4(TreadlyConnectPauseView treadlyConnectPauseView, DialogInterface dialogInterface, int i) {
        switch (i) {
            case 0:
                treadlyConnectPauseView.takePhoto();
                return;
            case 1:
                treadlyConnectPauseView.choosePhoto();
                return;
            case 2:
                dialogInterface.dismiss();
                return;
            default:
                return;
        }
    }

    void takePhoto() {
        if (this.fragment == null) {
            return;
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File file = null;
        try {
            file = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (file != null) {
            this.tempUrl = FileProvider.getUriForFile(getContext(), "com.treadly.Treadly.fileprovider", file);
            intent.putExtra("output", this.tempUrl);
            this.fragment.startActivityForResult(intent, 0);
        }
    }

    void choosePhoto() {
        this.fragment.startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 1);
    }

    private File createImageFile() throws IOException {
        if (this.activity == null) {
            return null;
        }
        return File.createTempFile("temp_snapshot_picture", ".png", this.activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
    }

    public void handleOnActivityResult(int i, int i2, Intent intent) {
        if (i == 3 || i == 69) {
            if (i2 == -1) {
                setSnapshotUrl(UCrop.getOutput(intent));
                return;
            } else if (i2 == 96) {
                setSnapshotUrl(null);
                return;
            } else {
                return;
            }
        }
        switch (i) {
            case 0:
                if (i2 == -1) {
                    cropPhoto(this.tempUrl);
                    return;
                } else {
                    setSnapshotUrl(null);
                    return;
                }
            case 1:
                if (i2 == -1) {
                    cropPhoto(intent.getData());
                    return;
                } else {
                    setSnapshotUrl(null);
                    return;
                }
            default:
                return;
        }
    }

    private void cropPhoto(Uri uri) {
        try {
            this.fragment.startActivityForResult(UCrop.of(uri, Uri.fromFile(createImageFile())).withAspectRatio(1.0f, 1.0f).withOptions(getOptions()).getIntent(getContext()), 3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    UCrop.Options getOptions() {
        UCrop.Options options = new UCrop.Options();
        options.setHideBottomControls(true);
        options.setFreeStyleCropEnabled(true);
        return options;
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        setDailyGoal();
        setSnapshotConstraints();
    }
}
