package com.treadly.Treadly.UI.TreadlyVideo;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.utils.Utils;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectStartState;
import com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.TreadlyPrivateStreamHostFragment;
import com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment;
import com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.TreadlyPublicStreamViewerFragment;
import com.treadly.client.lib.sdk.Model.ComponentInfo;
import com.treadly.client.lib.sdk.Model.DeviceStatus;
import com.treadly.client.lib.sdk.Model.DistanceUnits;
import com.treadly.client.lib.sdk.Model.HandrailStatus;
import com.treadly.client.lib.sdk.Model.VersionInfo;
import com.treadly.client.lib.sdk.TreadlyClientLib;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes2.dex */
public class TreadlyVideoSliderFragment extends Fragment {
    public static final String TAG = "VideoSlider";
    public ComponentInfo bleComponent;
    public DeviceStatus currentDeviceStatusInfo;
    public boolean isConnected;
    public boolean isPanning;
    private View lastSpeedView;
    public TreadlyVideoSliderFragmentEventListener listener;
    public TreadlyPrivateStreamHostFragment parentFragmentPrivate;
    public TreadlyPublicStreamHostFragment parentFragmentPublic;
    public TreadlyPublicStreamViewerFragment parentFragmentPublicViewer;
    private ConstraintLayout sliderSegment1;
    private ConstraintLayout sliderSegment2;
    private ConstraintLayout sliderSegment3;
    private ConstraintLayout sliderSegment4;
    private ConstraintLayout sliderSegment5;
    private LinearLayout sliderTenthLayout1;
    private LinearLayout sliderTenthLayout2;
    private LinearLayout sliderTenthLayout3;
    private LinearLayout sliderTenthLayout4;
    private LinearLayout sliderTenthLayout5;
    private LinearLayout[] sliderTenthLayoutArr;
    private ArrayList<View> sliderTenthList1;
    private ArrayList<View> sliderTenthList2;
    private ArrayList<View> sliderTenthList3;
    private ArrayList<View> sliderTenthList4;
    private ArrayList<View> sliderTenthList5;
    private TextView sliderTextView1;
    private TextView sliderTextView2;
    private TextView sliderTextView3;
    private TextView sliderTextView4;
    private TextView sliderTextView5;
    private View.OnTouchListener speedViewListener;
    protected TreadlyConnectStartState startState;
    private Button stopButton;

    /* loaded from: classes2.dex */
    public interface TreadlyVideoSliderFragmentEventListener {
        boolean getValueForHandrail(float f, DistanceUnits distanceUnits, HandrailStatus handrailStatus);

        void onImpactOccurred();

        void onPowerOffDevice();

        void onPowerOnDevice();

        void onSetSpeed(float f);

        void onShowAlert(String str, String str2);

        void onStopPressed();
    }

    public void setStartState(TreadlyConnectStartState treadlyConnectStartState) {
        this.startState = treadlyConnectStartState;
        if (this.stopButton != null) {
            this.stopButton.setText(treadlyConnectStartState != TreadlyConnectStartState.stopping ? R.string.stop_capitalized : R.string.stopping_capitalized);
        }
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_video_slider, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        initSpeedView(view);
    }

    private void initSpeedView(View view) {
        this.sliderSegment1 = (ConstraintLayout) view.findViewById(R.id.video_slider_1);
        this.sliderSegment2 = (ConstraintLayout) view.findViewById(R.id.video_slider_2);
        this.sliderSegment3 = (ConstraintLayout) view.findViewById(R.id.video_slider_3);
        this.sliderSegment4 = (ConstraintLayout) view.findViewById(R.id.video_slider_4);
        this.sliderSegment5 = (ConstraintLayout) view.findViewById(R.id.video_slider_5);
        this.sliderSegment1.setBackgroundResource(R.color.slider_segment_disabled_bg);
        this.sliderSegment2.setBackgroundResource(R.color.slider_segment_disabled_bg);
        this.sliderSegment3.setBackgroundResource(R.color.slider_segment_disabled_bg);
        this.sliderSegment4.setBackgroundResource(R.color.slider_segment_disabled_bg);
        this.sliderSegment5.setBackgroundResource(R.color.slider_segment_disabled_bg);
        this.sliderTextView1 = (TextView) view.findViewById(R.id.video_slider_1_text);
        this.sliderTextView2 = (TextView) view.findViewById(R.id.video_slider_2_text);
        this.sliderTextView3 = (TextView) view.findViewById(R.id.video_slider_3_text);
        this.sliderTextView4 = (TextView) view.findViewById(R.id.video_slider_4_text);
        this.sliderTextView5 = (TextView) view.findViewById(R.id.video_slider_5_text);
        this.sliderTextView1.setTextColor(ContextCompat.getColor(getContext(), R.color.slider_segment_text_disabled));
        this.sliderTextView2.setTextColor(ContextCompat.getColor(getContext(), R.color.slider_segment_text_disabled));
        this.sliderTextView3.setTextColor(ContextCompat.getColor(getContext(), R.color.slider_segment_text_disabled));
        this.sliderTextView4.setTextColor(ContextCompat.getColor(getContext(), R.color.slider_segment_text_disabled));
        this.sliderTextView5.setTextColor(ContextCompat.getColor(getContext(), R.color.slider_segment_text_disabled));
        this.sliderTenthLayout1 = (LinearLayout) view.findViewById(R.id.video_slider_1_tenths);
        this.sliderTenthLayout2 = (LinearLayout) view.findViewById(R.id.video_slider_2_tenths);
        this.sliderTenthLayout3 = (LinearLayout) view.findViewById(R.id.video_slider_3_tenths);
        this.sliderTenthLayout4 = (LinearLayout) view.findViewById(R.id.video_slider_4_tenths);
        this.sliderTenthLayout5 = (LinearLayout) view.findViewById(R.id.video_slider_5_tenths);
        this.sliderTenthLayout1.setTag(1);
        this.sliderTenthLayout2.setTag(2);
        this.sliderTenthLayout3.setTag(3);
        this.sliderTenthLayout4.setTag(4);
        this.sliderTenthLayout5.setTag(5);
        this.sliderTenthLayoutArr = new LinearLayout[]{this.sliderTenthLayout1, this.sliderTenthLayout2, this.sliderTenthLayout3, this.sliderTenthLayout4, this.sliderTenthLayout5};
        this.sliderTenthList1 = new ArrayList<>(10);
        this.sliderTenthList2 = new ArrayList<>(10);
        this.sliderTenthList3 = new ArrayList<>(10);
        this.sliderTenthList4 = new ArrayList<>(10);
        this.sliderTenthList5 = new ArrayList<>(10);
        for (int i = 0; i < 5; i++) {
            for (int i2 = 0; i2 <= 9; i2++) {
                View view2 = new View(getContext());
                view2.setBackgroundColor(0);
                view2.setLayoutParams(new LinearLayout.LayoutParams(0, -1, 1.0f));
                view2.setTag(Integer.valueOf((((Integer) this.sliderTenthLayoutArr[i].getTag()).intValue() * 10) + i2 + 1));
                this.sliderTenthLayoutArr[i].addView(view2);
                switch (i) {
                    case 0:
                        this.sliderTenthList1.add(view2);
                        break;
                    case 1:
                        this.sliderTenthList2.add(view2);
                        break;
                    case 2:
                        this.sliderTenthList3.add(view2);
                        break;
                    case 3:
                        this.sliderTenthList4.add(view2);
                        break;
                    case 4:
                        this.sliderTenthList5.add(view2);
                        break;
                }
            }
        }
        this.lastSpeedView = null;
        this.isPanning = false;
        this.speedViewListener = new View.OnTouchListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.TreadlyVideoSliderFragment.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view3, MotionEvent motionEvent) {
                view3.getParent().requestDisallowInterceptTouchEvent(true);
                TreadlyVideoSliderFragment.this.speedDragged(view3, motionEvent);
                return true;
            }
        };
        view.setOnTouchListener(this.speedViewListener);
        this.stopButton = (Button) view.findViewById(R.id.stop_button);
        this.stopButton.setVisibility(8);
        this.stopButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.-$$Lambda$TreadlyVideoSliderFragment$Z-a_Y6rnPk-Nh48VEgX2hmJTRzU
            @Override // android.view.View.OnClickListener
            public final void onClick(View view3) {
                TreadlyVideoSliderFragment.lambda$initSpeedView$0(TreadlyVideoSliderFragment.this, view3);
            }
        });
    }

    public static /* synthetic */ void lambda$initSpeedView$0(TreadlyVideoSliderFragment treadlyVideoSliderFragment, View view) {
        if (treadlyVideoSliderFragment.listener != null) {
            if (treadlyVideoSliderFragment.startState == TreadlyConnectStartState.start || treadlyVideoSliderFragment.startState == TreadlyConnectStartState.starting) {
                treadlyVideoSliderFragment.listener.onImpactOccurred();
                treadlyVideoSliderFragment.listener.onStopPressed();
            }
        }
    }

    private boolean isInView(View view, float f, float f2) {
        int[] iArr = new int[2];
        Rect rect = new Rect();
        view.getDrawingRect(rect);
        view.getLocationOnScreen(iArr);
        rect.offset(iArr[0], iArr[1]);
        return rect.contains((int) f, (int) f2);
    }

    private View findSpeedView(float f, float f2) {
        if (isInView(this.sliderTenthLayout1, f, f2)) {
            return findPartialView(this.sliderTenthLayout1, this.sliderTenthList1, f, f2);
        }
        if (isInView(this.sliderTenthLayout2, f, f2)) {
            return findPartialView(this.sliderTenthLayout2, this.sliderTenthList2, f, f2);
        }
        if (isInView(this.sliderTenthLayout3, f, f2)) {
            return findPartialView(this.sliderTenthLayout3, this.sliderTenthList3, f, f2);
        }
        if (isInView(this.sliderTenthLayout4, f, f2)) {
            return findPartialView(this.sliderTenthLayout4, this.sliderTenthList4, f, f2);
        }
        if (isInView(this.sliderTenthLayout5, f, f2)) {
            return findPartialView(this.sliderTenthLayout5, this.sliderTenthList5, f, f2);
        }
        return null;
    }

    private View findPartialView(View view, ArrayList<View> arrayList, float f, float f2) {
        Iterator<View> it = arrayList.iterator();
        while (it.hasNext()) {
            View next = it.next();
            if (isInView(next, f, f2)) {
                return next;
            }
        }
        return view;
    }

    private void setSpeedPartialViewListBackground(ArrayList<View> arrayList, @ColorRes int i) {
        Iterator<View> it = arrayList.iterator();
        while (it.hasNext()) {
            it.next().setBackgroundResource(i);
        }
    }

    protected void resetSpeedView() {
        this.sliderTextView1.setTextColor(ContextCompat.getColor(getContext(), R.color.slider_segment_text_disabled));
        this.sliderTextView2.setTextColor(ContextCompat.getColor(getContext(), R.color.slider_segment_text_disabled));
        this.sliderTextView3.setTextColor(ContextCompat.getColor(getContext(), R.color.slider_segment_text_disabled));
        this.sliderTextView4.setTextColor(ContextCompat.getColor(getContext(), R.color.slider_segment_text_disabled));
        this.sliderTextView5.setTextColor(ContextCompat.getColor(getContext(), R.color.slider_segment_text_disabled));
        this.sliderSegment1.setBackgroundResource(R.color.slider_segment_disabled_bg);
        this.sliderSegment2.setBackgroundResource(R.color.slider_segment_disabled_bg);
        this.sliderSegment3.setBackgroundResource(R.color.slider_segment_disabled_bg);
        this.sliderSegment4.setBackgroundResource(R.color.slider_segment_disabled_bg);
        this.sliderSegment5.setBackgroundResource(R.color.slider_segment_disabled_bg);
        setSpeedPartialViewListBackground(this.sliderTenthList1, R.color.transparent);
        setSpeedPartialViewListBackground(this.sliderTenthList2, R.color.transparent);
        setSpeedPartialViewListBackground(this.sliderTenthList3, R.color.transparent);
        setSpeedPartialViewListBackground(this.sliderTenthList4, R.color.transparent);
        setSpeedPartialViewListBackground(this.sliderTenthList5, R.color.transparent);
        setStopButton(false);
    }

    private void setSpeedPartialViews(ArrayList<View> arrayList, float f, float f2) {
        int i = 0;
        while (i < arrayList.size()) {
            View view = arrayList.get(i);
            i++;
            if (f >= (((float) i) / 10.0f) + f2) {
                view.setBackgroundResource(R.color.slider_tenth_enabled_bg);
            } else {
                view.setBackgroundResource(R.color.transparent);
            }
        }
    }

    public void setSpeed(float f) {
        if (this.isPanning || this.sliderTenthLayout1 == null) {
            return;
        }
        setSpeedLabels(f);
    }

    public void setSpeedLabels(float f) {
        setSpeedPartialViews(this.sliderTenthList1, f, 0.0f);
        setSpeedPartialViews(this.sliderTenthList2, f, 1.0f);
        setSpeedPartialViews(this.sliderTenthList3, f, 2.0f);
        setSpeedPartialViews(this.sliderTenthList4, f, 3.0f);
        setSpeedPartialViews(this.sliderTenthList5, f, 4.0f);
        if (f < 0.1f) {
            this.sliderTextView1.setTextColor(ContextCompat.getColor(getContext(), R.color.slider_segment_text_disabled));
        } else if (f < 1.1f) {
            this.sliderTextView1.setTextColor(ContextCompat.getColor(getContext(), R.color.slider_segment_text_selected));
        } else {
            this.sliderTextView1.setTextColor(ContextCompat.getColor(getContext(), R.color.slider_segment_text_enabled));
        }
        if (f >= 1.0f) {
            this.sliderSegment1.setBackgroundResource(R.color.slider_segment_enabled_bg);
        } else {
            this.sliderSegment1.setBackgroundResource(R.color.slider_segment_disabled_bg);
        }
        if (f < 1.1f) {
            this.sliderTextView2.setTextColor(ContextCompat.getColor(getContext(), R.color.slider_segment_text_disabled));
        } else if (f < 2.1f) {
            this.sliderTextView2.setTextColor(ContextCompat.getColor(getContext(), R.color.slider_segment_text_selected));
        } else {
            this.sliderTextView2.setTextColor(ContextCompat.getColor(getContext(), R.color.slider_segment_text_enabled));
        }
        if (f >= 2.0f) {
            this.sliderSegment2.setBackgroundResource(R.color.slider_segment_enabled_bg);
        } else {
            this.sliderSegment2.setBackgroundResource(R.color.slider_segment_disabled_bg);
        }
        if (f < 2.1f) {
            this.sliderTextView3.setTextColor(ContextCompat.getColor(getContext(), R.color.slider_segment_text_disabled));
        } else if (f < 3.1f) {
            this.sliderTextView3.setTextColor(ContextCompat.getColor(getContext(), R.color.slider_segment_text_selected));
        } else {
            this.sliderTextView3.setTextColor(ContextCompat.getColor(getContext(), R.color.slider_segment_text_enabled));
        }
        if (f >= 3.0f) {
            this.sliderSegment3.setBackgroundResource(R.color.slider_segment_enabled_bg);
        } else {
            this.sliderSegment3.setBackgroundResource(R.color.slider_segment_disabled_bg);
        }
        if (f < 3.1f) {
            this.sliderTextView4.setTextColor(ContextCompat.getColor(getContext(), R.color.slider_segment_text_disabled));
        } else if (f < 4.1f) {
            this.sliderTextView4.setTextColor(ContextCompat.getColor(getContext(), R.color.slider_segment_text_selected));
        } else {
            this.sliderTextView4.setTextColor(ContextCompat.getColor(getContext(), R.color.slider_segment_text_enabled));
        }
        if (f >= 4.0f) {
            this.sliderSegment4.setBackgroundResource(R.color.slider_segment_enabled_bg);
        } else {
            this.sliderSegment4.setBackgroundResource(R.color.slider_segment_disabled_bg);
        }
        if (f >= 4.1f) {
            this.sliderTextView5.setTextColor(ContextCompat.getColor(getContext(), R.color.slider_segment_text_selected));
        } else {
            this.sliderTextView5.setTextColor(ContextCompat.getColor(getContext(), R.color.slider_segment_text_disabled));
        }
        if (f >= 5.0f) {
            this.sliderSegment5.setBackgroundResource(R.color.slider_segment_enabled_bg);
        } else {
            this.sliderSegment5.setBackgroundResource(R.color.slider_segment_disabled_bg);
        }
    }

    public void setStopButton(boolean z) {
        this.stopButton.setVisibility(z ? 0 : 8);
        this.sliderTextView1.setVisibility(z ? 4 : 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void speedDragged(View view, MotionEvent motionEvent) {
        if (this.currentDeviceStatusInfo == null) {
            return;
        }
        DeviceStatus deviceStatus = this.currentDeviceStatusInfo;
        if (!deviceStatus.getPairingModeState() && TreadlyClientLib.shared.isDeviceConnected() && checkSessionOwner(deviceStatus)) {
            switch (motionEvent.getAction()) {
                case 0:
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    this.isPanning = true;
                    View findSpeedView = findSpeedView(motionEvent.getRawX(), motionEvent.getRawY());
                    if (findSpeedView == null) {
                        return;
                    }
                    setSpeedLabels(getSpeedFromTag(((Integer) findSpeedView.getTag()).intValue()));
                    this.lastSpeedView = findSpeedView;
                    this.listener.onImpactOccurred();
                    return;
                case 1:
                    this.isPanning = false;
                    System.out.println("WG: ACTION UP");
                    View view2 = this.lastSpeedView;
                    if (view2 == null) {
                        this.listener.onPowerOffDevice();
                        return;
                    }
                    getSpeedFromTag(((Integer) view2.getTag()).intValue());
                    if (DistanceUnits.MI != null) {
                        DistanceUnits distanceUnits = DistanceUnits.MI;
                        setSpeedLabels(getSpeedFromTag(((Integer) view2.getTag()).intValue()));
                        this.listener.onSetSpeed(getSpeedFromTag(((Integer) view2.getTag()).intValue()));
                        this.listener.onImpactOccurred();
                        this.lastSpeedView = null;
                        return;
                    }
                    return;
                case 2:
                    this.isPanning = true;
                    View findSpeedView2 = findSpeedView(motionEvent.getRawX(), motionEvent.getRawY());
                    if (findSpeedView2 != null) {
                        if (findSpeedView2 != this.lastSpeedView) {
                            setSpeedLabels(getSpeedFromTag(((Integer) findSpeedView2.getTag()).intValue()));
                            if (isInStopView(motionEvent.getRawX(), motionEvent.getRawY())) {
                                this.listener.onStopPressed();
                            }
                            this.listener.onImpactOccurred();
                        }
                        this.lastSpeedView = findSpeedView2;
                        return;
                    } else if (isBelowSpeedView(motionEvent.getRawX(), motionEvent.getRawY(), getView())) {
                        setSpeedLabels(0.0f);
                        this.lastSpeedView = null;
                        return;
                    } else {
                        return;
                    }
                case 3:
                    view.getParent().requestDisallowInterceptTouchEvent(false);
                    this.isPanning = false;
                    return;
                default:
                    return;
            }
        }
    }

    private float getSpeedFromTag(int i) {
        return i >= 10 ? (Math.round((i * 10.0f) / 10.0f) - 10.0f) / 10.0f : i;
    }

    private boolean isBelowSpeedView(float f, float f2, View view) {
        int[] iArr = new int[2];
        view.getLocationOnScreen(iArr);
        float f3 = iArr[0];
        return ((f > f3 ? 1 : (f == f3 ? 0 : -1)) >= 0 && (f > (f3 + ((float) view.getWidth())) ? 1 : (f == (f3 + ((float) view.getWidth())) ? 0 : -1)) <= 0) && ((f2 > (iArr[1] + ((float) view.getHeight())) ? 1 : (f2 == (iArr[1] + ((float) view.getHeight())) ? 0 : -1)) >= 0);
    }

    boolean isInStopView(float f, float f2) {
        if (this.startState != TreadlyConnectStartState.start) {
            return false;
        }
        return isInView(this.sliderTenthLayout1, f, f2);
    }

    boolean checkSessionOwner(DeviceStatus deviceStatus) {
        VersionInfo versionInfo = new VersionInfo(3, 14, 0);
        this.bleComponent = getBleComponent();
        if (this.bleComponent == null) {
            return true;
        }
        VersionInfo versionInfo2 = this.bleComponent.getVersionInfo().getVersionInfo();
        if (versionInfo2.isGreaterThan(versionInfo) || versionInfo2.isEqual(versionInfo)) {
            if (!deviceStatus.isPoweredOn() || deviceStatus.getSpeedInfo().getTargetSpeed() <= Utils.DOUBLE_EPSILON) {
                return true;
            }
            return deviceStatus.isSessionOwnership();
        }
        return false;
    }

    private ComponentInfo getBleComponent() {
        if (this.parentFragmentPrivate != null) {
            return this.parentFragmentPrivate.getBleComponent();
        }
        if (this.parentFragmentPublic != null) {
            return this.parentFragmentPublic.getBleComponent();
        }
        if (this.parentFragmentPublicViewer != null) {
            return this.parentFragmentPublicViewer.getBleComponent();
        }
        return null;
    }
}
