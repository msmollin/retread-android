package com.treadly.Treadly.UI.TreadlyActivity.Activity.ActivityGraph;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.Utils;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.Data.Model.UserRunningSessionInfo;
import com.treadly.Treadly.Data.Model.UserRunningSessionParticipantInfo;
import com.treadly.Treadly.Data.Model.UserRunningSessionSegmentInfo;
import com.treadly.Treadly.R;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import org.joda.time.DateTimeConstants;

/* loaded from: classes2.dex */
public class TreadlyActivityGraph {
    TreadlyActivityGraphParticipantsAdapter adapter;
    TextView averageSpeedLabel;
    TextView averageSpeedNumber;
    TextView chartSpeedTitleLabel;
    TextView chartTimeTitleLabel;
    LineChart chartView;
    public Context context;
    Date startDate;
    UserRunningSessionParticipantInfo user;
    RecyclerView userCollectionView;
    private UserInfo userInfo;
    public double averageSpeed = Utils.DOUBLE_EPSILON;
    List<Date> finishedSegments = new ArrayList();
    List<Date> segmentStartDates = new ArrayList();
    Map<Integer, Date> segmentStartValues = new HashMap();
    List<Date> segmentEndDates = new ArrayList();
    Map<Integer, Date> segmentEndValues = new HashMap();
    List<UserRunningSessionParticipantInfo> users = new ArrayList();
    int index = 0;

    public TreadlyActivityGraph(View view, Context context, UserInfo userInfo) {
        this.context = context;
        this.userInfo = userInfo;
        this.averageSpeedLabel = (TextView) view.findViewById(R.id.avg_speed_label_graph);
        this.averageSpeedNumber = (TextView) view.findViewById(R.id.avg_speed_number_graph);
        this.chartView = (LineChart) view.findViewById(R.id.graph_view);
        this.userCollectionView = (RecyclerView) view.findViewById(R.id.friends_specific_graph_views);
        this.userCollectionView.setLayoutManager(new LinearLayoutManager(context, 0, false));
        this.adapter = new TreadlyActivityGraphParticipantsAdapter(context, this.users);
        this.userCollectionView.setAdapter(this.adapter);
        initializeGraphs();
    }

    void setAverageSpeed(double d) {
        this.averageSpeedNumber.setText(String.format("%.1f", Double.valueOf(d)));
    }

    void initializeGraphs() {
        this.chartView.getDescription().setEnabled(false);
        this.chartView.setDragEnabled(true);
        this.chartView.setPinchZoom(false);
        this.chartView.setScaleEnabled(false);
        this.chartView.getLegend().setEnabled(false);
        this.chartView.setXAxisRenderer(new CustomXAxisRenderer(this.chartView.getViewPortHandler(), this.chartView.getXAxis(), this.chartView.getTransformer(YAxis.AxisDependency.LEFT)));
        this.chartView.getXAxis().enableGridDashedLine(3.0f, 5.0f, 0.0f);
        this.chartView.getXAxis().setDrawGridLines(true);
        this.chartView.getXAxis().setDrawAxisLine(true);
        this.chartView.getXAxis().enableAxisLineDashedLine(3.0f, 5.0f, 0.0f);
        this.chartView.getXAxis().setTextColor(ContextCompat.getColor(this.context, R.color.activity_label_text));
        this.chartView.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        this.chartView.getXAxis().setGranularity(1.0f);
        this.chartView.getXAxis().setGranularityEnabled(true);
        this.chartView.getXAxis().setAxisMinimum(1.0f);
        this.chartView.getXAxis().setValueFormatter(new ValueFormatter() { // from class: com.treadly.Treadly.UI.TreadlyActivity.Activity.ActivityGraph.TreadlyActivityGraph.1
            @Override // com.github.mikephil.charting.formatter.ValueFormatter
            public String getAxisLabel(float f, AxisBase axisBase) {
                int i = (int) f;
                Date baseDateFor = TreadlyActivityGraph.this.getBaseDateFor(i);
                if (baseDateFor == null) {
                    return "-\n";
                }
                Date date = new Date(baseDateFor.getTime() + ((i - TreadlyActivityGraph.this.getBaseDateOffset(i)) * DateTimeConstants.MILLIS_PER_MINUTE));
                int highestVisibleX = (int) TreadlyActivityGraph.this.chartView.getHighestVisibleX();
                int ceil = (int) Math.ceil(TreadlyActivityGraph.this.chartView.getLowestVisibleX());
                boolean z = TreadlyActivityGraph.this.segmentStartValues.get(Integer.valueOf(i)) != null;
                boolean z2 = TreadlyActivityGraph.this.segmentEndValues.get(Integer.valueOf(i)) != null;
                String str = ("" + i) + "\n";
                if (i == highestVisibleX || i == ceil || z || z2) {
                    return str + TreadlyActivityGraph.this.convertTimestampToDate(date);
                }
                return str + " ";
            }
        });
        this.chartView.setMinOffset(17.0f);
        this.chartView.setExtraBottomOffset(20.0f);
        YAxis axisLeft = this.chartView.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setAxisMaximum(5.0f);
        axisLeft.setAxisMinimum(0.0f);
        axisLeft.enableAxisLineDashedLine(3.0f, 5.0f, 0.0f);
        axisLeft.enableGridDashedLine(3.0f, 5.0f, 0.0f);
        axisLeft.setDrawLimitLinesBehindData(true);
        axisLeft.setTextColor(ContextCompat.getColor(this.context, R.color.activity_label_text));
        axisLeft.setGranularity(1.0f);
        axisLeft.setSpaceBottom(0.5f);
        axisLeft.setDrawGridLines(true);
        axisLeft.setDrawAxisLine(true);
        this.chartView.getAxisRight().setEnabled(false);
        this.chartView.getLegend().setForm(Legend.LegendForm.LINE);
        clearDataCount();
    }

    void clearDataCount() {
        LineDataSet lineDataSet = new LineDataSet(new ArrayList(), "Stats");
        this.startDate = null;
        this.segmentStartValues.clear();
        this.segmentStartDates.clear();
        this.segmentEndDates.clear();
        this.segmentEndValues.clear();
        this.chartView.setData(new LineData(lineDataSet));
        this.chartView.getXAxis().setLabelCount(9, false);
        this.chartView.getXAxis().setAxisMaximum(9.25f);
        this.chartView.setVisibleXRangeMaximum(8.25f);
        this.chartView.zoom(0.0f, 0.0f, 0.0f, 0.0f);
        this.chartView.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDataCount(UserRunningSessionInfo userRunningSessionInfo, int i) {
        Iterator it;
        Date date;
        this.averageSpeed = ((userRunningSessionInfo.distance / (userRunningSessionInfo.duration / 3600.0d)) * 10.0d) / 10.0d;
        setAverageSpeed(this.averageSpeed);
        this.index = i;
        this.user = new UserRunningSessionParticipantInfo(null, null, this.userInfo.name, this.userInfo.avatarPath);
        this.users.clear();
        this.users.addAll(userRunningSessionInfo.particpants);
        this.users.add(0, this.user);
        this.adapter.notifyDataSetChanged();
        ArrayList arrayList = new ArrayList();
        this.finishedSegments.clear();
        this.segmentStartValues.clear();
        this.segmentStartDates.clear();
        this.segmentEndDates.clear();
        this.segmentEndValues.clear();
        List<UserRunningSessionSegmentInfo> list = userRunningSessionInfo.segments;
        if (userRunningSessionInfo.finishedAt == null) {
            return;
        }
        this.finishedSegments.add(userRunningSessionInfo.finishedAt);
        System.out.println("GRAPH: FINISHED AT: " + userRunningSessionInfo.finishedAt);
        if (list.size() > 0) {
            ArrayList arrayList2 = new ArrayList(list);
            Collections.sort(arrayList2);
            Date date2 = ((UserRunningSessionSegmentInfo) arrayList2.get(0)).createdAt;
            if (date2 != null) {
                if (((UserRunningSessionSegmentInfo) arrayList2.get(0)).speed != Utils.DOUBLE_EPSILON) {
                    UserRunningSessionSegmentInfo userRunningSessionSegmentInfo = new UserRunningSessionSegmentInfo(new Date(date2.getTime() - 1000), ((UserRunningSessionSegmentInfo) arrayList2.get(0)).steps, Utils.DOUBLE_EPSILON);
                    arrayList.add(userRunningSessionSegmentInfo);
                    appendSegmentStart(userRunningSessionSegmentInfo.createdAt);
                } else {
                    appendSegmentStart(date2);
                }
            }
        }
        for (UserRunningSessionSegmentInfo userRunningSessionSegmentInfo2 : list) {
            if (userRunningSessionSegmentInfo2.createdAt != null) {
                arrayList.add(userRunningSessionSegmentInfo2);
            }
        }
        if (arrayList.size() > 0 && arrayList.get(arrayList.size() - 1) != null && ((UserRunningSessionSegmentInfo) arrayList.get(arrayList.size() - 1)).createdAt != null) {
            UserRunningSessionSegmentInfo userRunningSessionSegmentInfo3 = (UserRunningSessionSegmentInfo) arrayList.get(arrayList.size() - 1);
            Date date3 = userRunningSessionSegmentInfo3.createdAt;
            if (userRunningSessionSegmentInfo3.speed != Utils.DOUBLE_EPSILON) {
                UserRunningSessionSegmentInfo userRunningSessionSegmentInfo4 = new UserRunningSessionSegmentInfo(new Date(date3.getTime() + 1000), userRunningSessionSegmentInfo3.steps, Utils.DOUBLE_EPSILON);
                arrayList.add(userRunningSessionSegmentInfo4);
                appendSegmentEnd(userRunningSessionSegmentInfo4.createdAt);
            } else {
                appendSegmentEnd(date3);
            }
        }
        Collections.sort(arrayList);
        if (arrayList.isEmpty()) {
            return;
        }
        Date date4 = new Date((((UserRunningSessionSegmentInfo) arrayList.get(0)).createdAt.getTime() / 60000) * 60000);
        this.segmentStartValues.put(1, date4);
        System.out.println("GRAPH: BASEDATE: " + date4);
        Date endDateFor = getEndDateFor(((UserRunningSessionSegmentInfo) arrayList.get(0)).createdAt);
        this.segmentEndValues.put(1, endDateFor);
        System.out.println("GRAPH: ENDDATE: " + endDateFor);
        ArrayList arrayList3 = new ArrayList();
        Iterator it2 = arrayList.iterator();
        double d = 1.0d;
        double d2 = 0.0d;
        double d3 = -1.0d;
        double d4 = 1.0d;
        double d5 = 0.0d;
        while (it2.hasNext()) {
            UserRunningSessionSegmentInfo userRunningSessionSegmentInfo5 = (UserRunningSessionSegmentInfo) it2.next();
            Date baseDateFor = getBaseDateFor(userRunningSessionSegmentInfo5.createdAt);
            if (baseDateFor.getTime() > date4.getTime()) {
                double d6 = (int) (d2 + d);
                Date endDateFor2 = getEndDateFor(userRunningSessionSegmentInfo5.createdAt);
                if (endDateFor2.getTime() > endDateFor.getTime()) {
                    this.segmentEndValues.put(Integer.valueOf((int) d6), endDateFor2);
                    if (Math.abs(baseDateFor.getTime() - endDateFor.getTime()) > 60000) {
                        PrintStream printStream = System.out;
                        StringBuilder sb = new StringBuilder();
                        sb.append("GRAPH: 1SECOND?: ");
                        date = endDateFor2;
                        it = it2;
                        sb.append(Math.abs(baseDateFor.getTime() - endDateFor.getTime()));
                        printStream.println(sb.toString());
                        d6 += 1.0d;
                    } else {
                        date = endDateFor2;
                        it = it2;
                    }
                } else {
                    it = it2;
                    date = endDateFor;
                }
                System.out.println("GRAPH: xValueOffset: " + d6);
                this.segmentStartValues.put(Integer.valueOf((int) d6), baseDateFor);
                date4 = baseDateFor;
                d4 = d6;
                endDateFor = date;
            } else {
                it = it2;
            }
            if (d3 >= Utils.DOUBLE_EPSILON && d3 != userRunningSessionSegmentInfo5.speed) {
                System.out.println("GRAPH FIRSTDIFF: " + (userRunningSessionSegmentInfo5.createdAt.getTime() - date4.getTime()));
                Entry entry = new Entry((float) ((((double) ((userRunningSessionSegmentInfo5.createdAt.getTime() - date4.getTime()) - 100)) / 60000.0d) + d4), (float) d3);
                System.out.println("GRAPH: PREVENTRY: " + entry);
                arrayList3.add(entry);
            }
            double time = ((userRunningSessionSegmentInfo5.createdAt.getTime() - date4.getTime()) / 60000.0d) + d4;
            if (time > d5) {
                d5 = time;
            }
            double d7 = userRunningSessionSegmentInfo5.speed;
            Entry entry2 = new Entry((float) time, (float) userRunningSessionSegmentInfo5.speed);
            System.out.println("GRAPH: ENTRY: " + entry2);
            arrayList3.add(entry2);
            d2 = time;
            d3 = d7;
            it2 = it;
            d = 1.0d;
        }
        System.out.println("GRAPH: SEGMENT START: " + this.segmentStartDates);
        System.out.println("GRAPH: SEGMENT END:" + this.segmentEndDates);
        LineDataSet lineDataSet = new LineDataSet(arrayList3, "label");
        lineDataSet.setDrawIcons(false);
        lineDataSet.setColor(ContextCompat.getColor(this.context, R.color.activity_graph_line));
        lineDataSet.setLineWidth(2.0f);
        lineDataSet.setCircleRadius(0.0f);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setFormSize(15.0f);
        lineDataSet.setDrawValues(false);
        lineDataSet.setFillAlpha(1);
        lineDataSet.setDrawFilled(false);
        this.chartView.setData(new LineData(lineDataSet));
        this.chartView.getXAxis().setLabelCount(9, false);
        XAxis xAxis = this.chartView.getXAxis();
        if (d5 <= 9.0d) {
            d5 = 9.25d;
        }
        xAxis.setAxisMaximum((float) d5);
        this.chartView.setVisibleXRangeMaximum(8.25f);
        this.chartView.setAutoScaleMinMaxEnabled(true);
        ((LineData) this.chartView.getData()).notifyDataChanged();
        this.chartView.notifyDataSetChanged();
        this.chartView.invalidate();
    }

    void appendSegmentStart(Date date) {
        Date nearestMin = toNearestMin(date);
        Iterator<Date> it = this.segmentStartDates.iterator();
        boolean z = false;
        while (it.hasNext()) {
            if (it.next().getTime() == nearestMin.getTime()) {
                z = true;
            }
        }
        if (z) {
            return;
        }
        this.segmentStartDates.add(nearestMin);
    }

    void appendSegmentEnd(Date date) {
        Date nextMin = toNextMin(date);
        Iterator<Date> it = this.segmentEndDates.iterator();
        boolean z = false;
        while (it.hasNext()) {
            if (it.next().getTime() == nextMin.getTime()) {
                z = true;
            }
        }
        if (z) {
            return;
        }
        this.segmentEndDates.add(nextMin);
    }

    Date toNearestMin(Date date) {
        return new Date((date.getTime() / 60000) * 60000);
    }

    Date toNextMin(Date date) {
        return new Date(((long) Math.ceil(date.getTime() / 60000.0d)) * 60000);
    }

    Date getBaseDateFor(Date date) {
        Date date2 = date;
        for (Date date3 : this.segmentStartDates) {
            if (date.getTime() >= date3.getTime()) {
                date2 = date3;
            }
        }
        return date2;
    }

    Date getEndDateFor(Date date) {
        for (Date date2 : this.segmentEndDates) {
            if (date.getTime() <= date2.getTime()) {
                return date2;
            }
        }
        return date;
    }

    Date getBaseDateFor(int i) {
        Date date = null;
        for (Map.Entry entry : new TreeMap(this.segmentStartValues).entrySet()) {
            if (i >= ((Integer) entry.getKey()).intValue()) {
                date = (Date) entry.getValue();
            }
        }
        return date;
    }

    int getBaseDateOffset(int i) {
        int i2 = 0;
        for (Map.Entry entry : new TreeMap(this.segmentStartValues).entrySet()) {
            if (i >= ((Integer) entry.getKey()).intValue()) {
                i2 = ((Integer) entry.getKey()).intValue();
            }
        }
        return i2;
    }

    String convertTimestampToDate(Date date) {
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date);
    }
}
