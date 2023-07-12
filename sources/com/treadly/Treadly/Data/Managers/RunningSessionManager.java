package com.treadly.Treadly.Data.Managers;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.github.mikephil.charting.utils.Utils;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener;
import com.treadly.Treadly.Data.Managers.AppActivityManager;
import com.treadly.Treadly.Data.Managers.DeviceUserStatsLogManager;
import com.treadly.Treadly.Data.Model.UserDailyGoalType;
import com.treadly.Treadly.Data.Model.UserNotificationSettingInfo;
import com.treadly.Treadly.Data.Model.UserProfileInfo;
import com.treadly.Treadly.Data.Model.UserStatsInfo;
import com.treadly.Treadly.Data.Utility.NotificationCenter.NotificationCenter;
import com.treadly.Treadly.Data.Utility.NotificationCenter.NotificationType;
import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyConnect.DailyGoalView.TreadlyConnectDailyGoalAnimationView;
import com.treadly.Treadly.UI.TreadlyConnect.DailyGoalView.TreadlyConnectDailyGoalFragment;
import com.treadly.Treadly.UI.TreadlyConnect.DailyGoalView.TreadlyDailyGoalType;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper;
import com.treadly.Treadly.UI.Util.OnBackPressedListener;
import com.treadly.Treadly.UI.Util.SharedPreferences;
import com.treadly.client.lib.sdk.Model.DistanceUnits;
import com.treadly.client.lib.sdk.Model.VersionInfo;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class RunningSessionManager {
    public Context context;
    public int currentCalories;
    public int currentDailyCalories;
    public double currentDailyDistance;
    public double currentDailyDuration;
    public int currentDailySteps;
    public double currentDistance;
    public double currentDuration;
    private OnBackPressedListener currentListener;
    public int currentSteps;
    public VersionInfo customboardVersion;
    TreadlyConnectDailyGoalFragment daily100Fragment;
    public double dailyDistance;
    public double dailyDuration;
    public int dailySteps;
    int getCurrentDailyCalories;
    private MainActivity mainActivity;
    public Boolean sentPendingActivity;
    public int totalDailyCalories;
    public double totalDailyDistance;
    public double totalDailyDuration;
    public int totalDailySteps;
    private static final RunningSessionManager instance = new RunningSessionManager();
    public static double miToKm = 1.60934d;
    public static double kmToMi = 0.621371d;
    public UserProfileInfo userProfile = null;
    public UserNotificationSettingInfo userNotifications = null;
    public boolean displayedDailyGoal50 = false;
    public boolean displayedDailyGoal100 = false;
    public boolean inProgress = false;
    boolean didInit50 = false;
    boolean didInit100 = false;
    boolean didDisplay50 = false;
    boolean didDisplay100 = false;
    public int steps = 0;
    public double distance = Utils.DOUBLE_EPSILON;
    public double speed = Utils.DOUBLE_EPSILON;
    public int duration = 0;
    public int calories = 0;
    public int initDuration = 0;
    public double initDistance = Utils.DOUBLE_EPSILON;
    public int initSteps = 0;
    public double averageSpeed = Utils.DOUBLE_EPSILON;
    public DistanceUnits units = DistanceUnits.MI;

    private double calculateBmr(int i, double d, double d2, boolean z) {
        double d3 = z ? (((d * 4.536d) + (d2 * 15.88d)) - (i * 5.0d)) + 5.0d : (((d * 4.536d) + (d2 * 15.88d)) - (i * 5.0d)) - 161.0d;
        return d3 >= Utils.DOUBLE_EPSILON ? d3 : Utils.DOUBLE_EPSILON;
    }

    private double calculateMetsPerHourKmh(double d) {
        if (d <= 3.2d) {
            return 2.8d;
        }
        if (d <= 4.0d) {
            return 3.0d;
        }
        if (d <= 4.8d) {
            return 3.5d;
        }
        if (d <= 5.6d) {
            return 4.3d;
        }
        if (d <= 6.4d) {
            return 5.0d;
        }
        if (d <= 7.2d) {
            return 7.0d;
        }
        if (d <= 8.0d) {
            return 8.0d;
        }
        return Utils.DOUBLE_EPSILON;
    }

    private double calculateMetsPerHourMph(double d) {
        if (d <= 2.0d) {
            return 2.8d;
        }
        if (d <= 2.5d) {
            return 3.0d;
        }
        if (d <= 3.0d) {
            return 3.5d;
        }
        if (d <= 3.5d) {
            return 4.3d;
        }
        if (d <= 4.0d) {
            return 5.0d;
        }
        if (d <= 4.5d) {
            return 7.0d;
        }
        if (d <= 5.0d) {
            return 8.0d;
        }
        return Utils.DOUBLE_EPSILON;
    }

    public void dismissDailyGoal50() {
    }

    public void displayDailyGoal50() {
    }

    public void initDailyGoal50() {
    }

    public static RunningSessionManager getInstance() {
        return instance;
    }

    public void setTotalDailyCalories(int i) {
        this.totalDailyCalories = i;
        if (this.context == null || this.userProfile == null || this.userProfile.dailyGoalType() != UserDailyGoalType.calories) {
            return;
        }
        NotificationCenter.postNotification(this.context, NotificationType.didUpdateCurrentCalories, new HashMap());
    }

    public void setTotalDailySteps(int i) {
        this.totalDailySteps = i;
        if (this.context == null || this.userProfile == null || this.userProfile.dailyGoalType() != UserDailyGoalType.steps) {
            return;
        }
        NotificationCenter.postNotification(this.context, NotificationType.didUpdateCurrentSteps, new HashMap());
    }

    public void setTotalDailyDistance(double d) {
        this.totalDailyDistance = d;
        if (this.context == null || this.userProfile == null || this.userProfile.dailyGoalType() != UserDailyGoalType.distance) {
            return;
        }
        NotificationCenter.postNotification(this.context, NotificationType.didUpdateCurrentDistance, new HashMap());
    }

    public void setTotalDailyDuration(double d) {
        this.totalDailyDuration = d;
        if (this.context == null || this.userProfile == null || this.userProfile.dailyGoalType() != UserDailyGoalType.duration) {
            return;
        }
        NotificationCenter.postNotification(this.context, NotificationType.didUpdateCurrentDuration, new HashMap());
    }

    public int dailyGoalPercentage() {
        if (this.userProfile != null && this.userProfile.caloriesGoal() > 0) {
            return (int) ((this.calories * 100.0d) / this.userProfile.caloriesGoal());
        }
        return 0;
    }

    public int totalDailyGoalPercentage() {
        if (this.userProfile != null && this.userProfile.caloriesGoal() > 0) {
            return (int) ((Math.max(this.totalDailyCalories, 0) * 100.0d) / this.userProfile.caloriesGoal());
        }
        return 0;
    }

    public void initRunningSessionManager(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void setLastBackListener(OnBackPressedListener onBackPressedListener) {
        this.currentListener = onBackPressedListener;
    }

    public void startRunSession(UserProfileInfo userProfileInfo, UserNotificationSettingInfo userNotificationSettingInfo, int i, double d, int i2) {
        this.initDuration = 0;
        this.initDistance = Utils.DOUBLE_EPSILON;
        this.initSteps = 0;
        this.userNotifications = userNotificationSettingInfo;
        int lastTotalCalories = getLastTotalCalories();
        if (lastTotalCalories >= 0) {
            this.calories = lastTotalCalories;
        }
        int lastTotalSteps = getLastTotalSteps();
        if (lastTotalSteps >= 0) {
            this.dailySteps = lastTotalSteps;
        }
        double lastTotalDistance = getLastTotalDistance();
        if (lastTotalDistance >= Utils.DOUBLE_EPSILON) {
            this.dailyDistance = lastTotalDistance;
        }
        double lastTotalDuration = getLastTotalDuration();
        if (lastTotalDuration >= Utils.DOUBLE_EPSILON) {
            this.dailyDuration = lastTotalDuration;
        }
        this.inProgress = true;
        clearAnimations();
        TreadlyEventManager.getInstance().startRunningSessionWithParticipants(new ArrayList<>());
        fetchCurrentCalories();
        fetchDailyGoals();
    }

    public void updateRunSession(int i, double d, double d2, int i2, DistanceUnits distanceUnits, boolean z) {
        double d3;
        double d4;
        DistanceUnits distanceUnits2;
        double d5;
        double d6;
        double d7;
        if (this.userProfile == null) {
            return;
        }
        int i3 = this.userProfile.birthdateYear;
        double d8 = this.userProfile.weight;
        String str = this.userProfile.gender;
        double d9 = this.userProfile.height;
        int i4 = this.currentDailyCalories;
        int i5 = this.currentDailySteps;
        double d10 = this.currentDailyDistance;
        double d11 = this.currentDailyDuration;
        int i6 = this.totalDailyCalories;
        int i7 = this.totalDailySteps;
        double d12 = this.totalDailyDistance;
        double d13 = this.totalDailyDuration;
        if (i2 >= this.initDuration) {
            d3 = d13;
            if (this.distance != d) {
                this.duration = i2 - this.initDuration;
            }
        } else {
            d3 = d13;
        }
        if (i >= this.initSteps) {
            this.steps = i - this.initSteps;
        }
        if (d >= this.initDistance) {
            this.distance = d - this.initDistance;
        }
        if (this.duration > 0) {
            d5 = this.distance / (this.duration / 3600.0d);
            this.averageSpeed = ((int) ((this.distance / (this.duration / 3600.0d)) * 10.0d)) / 10.0d;
            distanceUnits2 = distanceUnits;
            d4 = Utils.DOUBLE_EPSILON;
        } else {
            d4 = Utils.DOUBLE_EPSILON;
            this.averageSpeed = Utils.DOUBLE_EPSILON;
            distanceUnits2 = distanceUnits;
            d5 = 0.0d;
        }
        this.units = distanceUnits2;
        this.speed = d2;
        int ageFromBirthYear = getAgeFromBirthYear(i3);
        int i8 = (d2 > d4 ? 1 : (d2 == d4 ? 0 : -1));
        boolean z2 = false;
        if (i8 > 0 || (z && this.currentSteps <= 0)) {
            if (z) {
                int i9 = this.steps + i5;
                if (i9 >= this.dailySteps) {
                    this.currentSteps = this.steps;
                    this.dailySteps = i9;
                }
            } else {
                this.dailySteps = i7;
                this.currentSteps = 0;
            }
            processSteps(this.dailySteps);
        }
        if (i8 > 0 || (z && this.currentDistance <= Utils.DOUBLE_EPSILON)) {
            if (z) {
                if (this.units == DistanceUnits.MI) {
                    d7 = this.distance;
                } else {
                    d7 = (this.distance * 0.621369d) + (this.distance > Utils.DOUBLE_EPSILON ? 1.0E-4d : Utils.DOUBLE_EPSILON);
                }
                double d14 = d7 + d10;
                if (d14 >= this.dailyDistance) {
                    this.currentDistance = this.distance;
                    this.dailyDistance = d14;
                } else {
                    this.dailyDistance = d12;
                    d6 = Utils.DOUBLE_EPSILON;
                    this.currentDistance = Utils.DOUBLE_EPSILON;
                    processDistance(this.dailyDistance);
                }
            }
            d6 = Utils.DOUBLE_EPSILON;
            processDistance(this.dailyDistance);
        } else {
            d6 = Utils.DOUBLE_EPSILON;
        }
        if (i8 > 0 || (z && this.currentDuration <= d6)) {
            if (z) {
                double d15 = this.duration + d11;
                if (d15 >= this.dailyDuration) {
                    this.currentDuration = this.duration;
                    this.dailyDuration = d15;
                } else {
                    this.dailyDuration = d3;
                    this.currentDuration = Utils.DOUBLE_EPSILON;
                }
            }
            processDuration(this.dailyDuration);
        }
        if (i8 > 0 || (z && this.currentCalories <= 0)) {
            if (z) {
                int calculateCalories = calculateCalories(ageFromBirthYear, d9, d8, str, Double.valueOf(d5), this.duration, this.steps, distanceUnits);
                int i10 = calculateCalories + i4;
                if (i10 >= this.calories) {
                    this.calories = i10;
                    this.currentCalories = calculateCalories;
                }
            } else {
                this.calories = i4;
                this.currentCalories = 0;
            }
            processCalories(this.calories);
            if (this.customboardVersion != null) {
                VersionInfo versionInfo = new VersionInfo(2, 14, 0);
                VersionInfo versionInfo2 = new VersionInfo(3, 14, 0);
                if ((this.customboardVersion.isGreaterThan(versionInfo) || this.customboardVersion.isEqual(versionInfo)) && versionInfo2.isGreaterThan(this.customboardVersion)) {
                    z2 = true;
                }
            }
        }
        if (z || z2 || (DeviceUserStatsLogManager.getInstance().isLogging && this.sentPendingActivity == null)) {
            DeviceUserStatsLogManager.getInstance().sendPendingActivity(new DeviceUserStatsLogManager.DeviceUserStatsResultsListener() { // from class: com.treadly.Treadly.Data.Managers.RunningSessionManager.1
                @Override // com.treadly.Treadly.Data.Managers.DeviceUserStatsLogManager.DeviceUserStatsResultsListener
                public void onResults(String str2) {
                    RunningSessionManager.this.sentPendingActivity = Boolean.valueOf(str2 == null);
                }
            });
        }
    }

    public void stopRunSession() {
        if (this.inProgress) {
            this.inProgress = false;
        }
    }

    public void reset() {
        this.userProfile = null;
        this.userNotifications = null;
        this.calories = 0;
        this.dailySteps = 0;
        this.dailyDistance = Utils.DOUBLE_EPSILON;
        this.dailyDuration = Utils.DOUBLE_EPSILON;
        this.currentCalories = 0;
        this.currentSteps = 0;
        this.currentDistance = Utils.DOUBLE_EPSILON;
        this.currentDuration = Utils.DOUBLE_EPSILON;
        this.steps = 0;
        this.speed = Utils.DOUBLE_EPSILON;
        this.duration = 0;
        this.distance = Utils.DOUBLE_EPSILON;
        this.averageSpeed = Utils.DOUBLE_EPSILON;
        this.units = DistanceUnits.MI;
        this.customboardVersion = null;
        setTotalDailyCalories(-1);
        setTotalDailySteps(-1);
        setTotalDailyDistance(-1.0d);
    }

    public void clearStats() {
        this.calories = Math.max(getLastTotalCalories(), 0);
        this.dailySteps = Math.max(getLastTotalSteps(), 0);
        this.dailyDistance = Math.max(getLastTotalDistance(), (double) Utils.DOUBLE_EPSILON);
        this.dailyDuration = Math.max(getLastTotalDuration(), (double) Utils.DOUBLE_EPSILON);
        this.currentCalories = 0;
        this.currentSteps = 0;
        this.currentDistance = Utils.DOUBLE_EPSILON;
        this.currentDuration = Utils.DOUBLE_EPSILON;
        this.steps = 0;
        this.speed = Utils.DOUBLE_EPSILON;
        this.duration = 0;
        this.distance = Utils.DOUBLE_EPSILON;
        this.averageSpeed = Utils.DOUBLE_EPSILON;
        this.units = DistanceUnits.MI;
    }

    private int calculateCalories(int i, double d, double d2, String str, Double d3, int i2, int i3, DistanceUnits distanceUnits) {
        if (i3 <= 0) {
            return 0;
        }
        double calculateBmr = ((calculateBmr(i, d2, d, str != null && str.equals("male")) * calculateMets(d3.doubleValue(), this.units, i2)) / 24.0d) * 1.0d;
        if (calculateBmr >= Utils.DOUBLE_EPSILON) {
            return (int) calculateBmr;
        }
        return 0;
    }

    private double calculateMets(double d, DistanceUnits distanceUnits, int i) {
        return (i / 3600.0d) * (distanceUnits == DistanceUnits.MI ? calculateMetsPerHourMph(d) : calculateMetsPerHourKmh(d));
    }

    private int getAgeFromBirthYear(int i) {
        return Calendar.getInstance().get(1) - i;
    }

    public void fetchDailyGoals() {
        this.displayedDailyGoal50 = true;
        this.displayedDailyGoal100 = true;
        AppActivityManager.shared.hasDailyGoal50(new AppActivityManager.AppActivityManagerHasDailyGoalResponse() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$RunningSessionManager$sgAbnqhzqvZNoUTiNYqCU6-eXTg
            @Override // com.treadly.Treadly.Data.Managers.AppActivityManager.AppActivityManagerHasDailyGoalResponse
            public final void onResponse(String str, boolean z) {
                RunningSessionManager.lambda$fetchDailyGoals$0(RunningSessionManager.this, str, z);
            }
        });
        AppActivityManager.shared.hasDailyGoal100(new AppActivityManager.AppActivityManagerHasDailyGoalResponse() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$RunningSessionManager$aJHgKa1gKTbEuGG3TvnNXJvrqTg
            @Override // com.treadly.Treadly.Data.Managers.AppActivityManager.AppActivityManagerHasDailyGoalResponse
            public final void onResponse(String str, boolean z) {
                RunningSessionManager.lambda$fetchDailyGoals$1(RunningSessionManager.this, str, z);
            }
        });
    }

    public static /* synthetic */ void lambda$fetchDailyGoals$0(RunningSessionManager runningSessionManager, String str, boolean z) {
        if (str != null) {
            z = true;
        }
        runningSessionManager.displayedDailyGoal50 = z;
    }

    public static /* synthetic */ void lambda$fetchDailyGoals$1(RunningSessionManager runningSessionManager, String str, boolean z) {
        if (str != null) {
            z = true;
        }
        runningSessionManager.displayedDailyGoal100 = z;
    }

    public void fetchCurrentCalories() {
        this.currentDailyCalories = -1;
        this.currentDailySteps = -1;
        this.currentDailyDistance = -1.0d;
        this.currentDailyDuration = -1.0d;
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final int i = calendar.get(5);
        final int i2 = calendar.get(2);
        final int i3 = calendar.get(1);
        TreadlyServiceManager.getInstance().getDeviceUserStatsInfo(true, (TreadlyServiceResponseEventListener) new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.Data.Managers.RunningSessionManager.2
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onUserStatsInfo(String str, ArrayList<UserStatsInfo> arrayList) {
                if (str != null || arrayList == null) {
                    return;
                }
                Iterator<UserStatsInfo> it = arrayList.iterator();
                while (it.hasNext()) {
                    UserStatsInfo next = it.next();
                    Date date2 = next.timestamp;
                    if (date2 != null) {
                        Calendar calendar2 = Calendar.getInstance();
                        calendar2.setTime(date2);
                        int i4 = calendar2.get(5);
                        int i5 = calendar2.get(2);
                        int i6 = calendar2.get(1);
                        if (i4 == i && i5 == i2 && i6 == i3) {
                            RunningSessionManager.this.calories = next.calories;
                            RunningSessionManager.this.dailySteps = next.steps;
                            RunningSessionManager.this.dailyDistance = next.distance;
                            RunningSessionManager.this.dailyDuration = next.duration;
                            RunningSessionManager.this.currentDailyCalories = next.calories;
                            RunningSessionManager.this.currentDailySteps = next.steps;
                            RunningSessionManager.this.currentDailyDistance = next.distance;
                            RunningSessionManager.this.currentDailyDuration = next.duration;
                            RunningSessionManager.this.currentCalories = 0;
                            RunningSessionManager.this.currentSteps = 0;
                            RunningSessionManager.this.currentDistance = Utils.DOUBLE_EPSILON;
                            RunningSessionManager.this.currentDuration = Utils.DOUBLE_EPSILON;
                            return;
                        }
                    }
                }
                RunningSessionManager.this.calories = 0;
                RunningSessionManager.this.dailySteps = 0;
                RunningSessionManager.this.dailyDistance = Utils.DOUBLE_EPSILON;
                RunningSessionManager.this.dailyDuration = Utils.DOUBLE_EPSILON;
                RunningSessionManager.this.currentDailyCalories = 0;
                RunningSessionManager.this.currentDailySteps = 0;
                RunningSessionManager.this.currentDailyDistance = Utils.DOUBLE_EPSILON;
                RunningSessionManager.this.currentDailyDuration = Utils.DOUBLE_EPSILON;
                RunningSessionManager.this.currentCalories = 0;
                RunningSessionManager.this.currentSteps = 0;
                RunningSessionManager.this.currentDistance = Utils.DOUBLE_EPSILON;
                RunningSessionManager.this.currentDuration = Utils.DOUBLE_EPSILON;
            }
        });
        TreadlyServiceManager.getInstance().getDeviceUserStatsInfo(new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.Data.Managers.RunningSessionManager.3
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onUserStatsInfo(String str, ArrayList<UserStatsInfo> arrayList) {
                if (str != null || arrayList == null) {
                    return;
                }
                Iterator<UserStatsInfo> it = arrayList.iterator();
                while (it.hasNext()) {
                    UserStatsInfo next = it.next();
                    Date date2 = next.timestamp;
                    if (date2 != null) {
                        Calendar calendar2 = Calendar.getInstance();
                        calendar2.setTime(date2);
                        int i4 = calendar2.get(5);
                        int i5 = calendar2.get(2);
                        int i6 = calendar2.get(1);
                        if (i4 == i && i5 == i2 && i6 == i3) {
                            RunningSessionManager.this.setTotalDailyCalories(next.calories);
                            RunningSessionManager.this.setTotalDailySteps(next.steps);
                            RunningSessionManager.this.setTotalDailyDistance(next.distance);
                            RunningSessionManager.this.setTotalDailyDuration(next.duration);
                            return;
                        }
                    }
                }
                RunningSessionManager.this.setTotalDailyCalories(0);
                RunningSessionManager.this.setTotalDailySteps(0);
                RunningSessionManager.this.setTotalDailyDistance(Utils.DOUBLE_EPSILON);
                RunningSessionManager.this.setTotalDailyDuration(Utils.DOUBLE_EPSILON);
            }
        });
    }

    public void processCalories(int i) {
        if (this.userProfile == null || this.userProfile.dailyGoalType() != UserDailyGoalType.calories || this.userNotifications == null) {
            return;
        }
        double d = i;
        if (d < this.userProfile.caloriesGoal() * 0.45d) {
            return;
        }
        boolean z = false;
        boolean z2 = this.userNotifications.dailyGoal50Achieved && this.userNotifications.enable;
        if (this.userNotifications.dailyGoal100Achieved && this.userNotifications.enable) {
            z = true;
        }
        if (d >= this.userProfile.caloriesGoal() * 0.95d) {
            if (!this.didInit100 && z && !this.displayedDailyGoal100) {
                initDailyGoal100();
                this.didInit100 = true;
                this.didInit50 = true;
            }
            if (d < this.userProfile.caloriesGoal() || this.didDisplay100 || !z || this.displayedDailyGoal100) {
                return;
            }
            displayDailyGoal100();
            this.didDisplay100 = true;
            this.displayedDailyGoal100 = true;
            this.didDisplay50 = true;
            this.displayedDailyGoal50 = true;
            AppActivityManager.shared.sendDailyGoal50Event();
            AppActivityManager.shared.sendDailyGoal100Event();
        } else if (d >= this.userProfile.caloriesGoal() * 0.45d) {
            if (!this.didInit50 && z2 && !this.displayedDailyGoal50) {
                initDailyGoal50();
                this.didInit50 = true;
            }
            if (d < this.userProfile.caloriesGoal() * 0.5d || this.didDisplay50 || !z2 || this.displayedDailyGoal50) {
                return;
            }
            displayDailyGoal50();
            this.didDisplay50 = true;
            this.displayedDailyGoal50 = true;
            AppActivityManager.shared.sendDailyGoal50Event();
        }
    }

    public void processSteps(int i) {
        if (this.userProfile == null || this.userProfile.dailyGoalType() != UserDailyGoalType.steps || this.userNotifications == null) {
            return;
        }
        double d = i;
        if (d >= this.userProfile.stepsGoal() * 0.45d) {
            boolean z = false;
            boolean z2 = this.userNotifications.dailyGoal50Achieved && this.userNotifications.enable;
            if (this.userNotifications.dailyGoal100Achieved && this.userNotifications.enable) {
                z = true;
            }
            if (d >= this.userProfile.stepsGoal() * 0.95d) {
                if (!this.didInit100 && z && !this.displayedDailyGoal100) {
                    initDailyGoal100();
                    this.didInit100 = true;
                    this.didInit50 = true;
                }
                if (d < this.userProfile.stepsGoal() || this.didDisplay100 || !z || this.displayedDailyGoal100) {
                    return;
                }
                displayDailyGoal100();
                this.didDisplay100 = true;
                this.displayedDailyGoal100 = true;
                this.didDisplay50 = true;
                this.displayedDailyGoal50 = true;
                AppActivityManager.shared.sendDailyGoal50Event();
                AppActivityManager.shared.sendDailyGoal100Event();
            } else if (d >= this.userProfile.stepsGoal() * 0.45d) {
                if (!this.didInit50 && z2 && !this.displayedDailyGoal50) {
                    initDailyGoal50();
                    this.didInit50 = true;
                }
                if (d < this.userProfile.stepsGoal() * 0.5d || this.didDisplay50 || !z2 || this.displayedDailyGoal50) {
                    return;
                }
                displayDailyGoal50();
                this.didDisplay50 = true;
                this.displayedDailyGoal50 = true;
                AppActivityManager.shared.sendDailyGoal50Event();
            }
        }
    }

    public void processDistance(double d) {
        if (this.userProfile == null || this.userProfile.dailyGoalType() != UserDailyGoalType.distance || this.userNotifications == null || d < this.userProfile.getDailyGoal() * 0.45d) {
            return;
        }
        boolean z = false;
        boolean z2 = this.userNotifications.dailyGoal50Achieved && this.userNotifications.enable;
        if (this.userNotifications.dailyGoal100Achieved && this.userNotifications.enable) {
            z = true;
        }
        if (d >= this.userProfile.getDailyGoal() * 0.95d) {
            if (!this.didInit100 && z && !this.displayedDailyGoal100) {
                initDailyGoal100();
                this.didInit100 = true;
                this.didInit50 = true;
            }
            if (d < this.userProfile.getDailyGoal() || this.didDisplay100 || !z || this.displayedDailyGoal100) {
                return;
            }
            displayDailyGoal100();
            this.didDisplay100 = true;
            this.displayedDailyGoal100 = true;
            this.didDisplay50 = true;
            this.displayedDailyGoal50 = true;
            AppActivityManager.shared.sendDailyGoal50Event();
            AppActivityManager.shared.sendDailyGoal100Event();
        } else if (d >= this.userProfile.getDailyGoal() * 0.45d) {
            if (!this.didInit50 && z2 && !this.displayedDailyGoal50) {
                initDailyGoal50();
                this.didInit50 = true;
            }
            if (d < this.userProfile.getDailyGoal() * 0.5d || this.didDisplay50 || !z2 || this.displayedDailyGoal50) {
                return;
            }
            displayDailyGoal50();
            this.didDisplay50 = true;
            this.displayedDailyGoal50 = true;
            AppActivityManager.shared.sendDailyGoal50Event();
        }
    }

    public void processDuration(double d) {
        if (this.userProfile == null || this.userProfile.dailyGoalType() != UserDailyGoalType.duration || this.userNotifications == null || d < this.userProfile.durationGoal() * 0.45d) {
            return;
        }
        boolean z = false;
        boolean z2 = this.userNotifications.dailyGoal50Achieved && this.userNotifications.enable;
        if (this.userNotifications.dailyGoal100Achieved && this.userNotifications.enable) {
            z = true;
        }
        if (d >= this.userProfile.durationGoal() * 0.95d) {
            if (!this.didInit100 && z && !this.displayedDailyGoal100) {
                initDailyGoal100();
                this.didInit100 = true;
                this.didInit50 = true;
            }
            if (d < this.userProfile.durationGoal() || this.didDisplay100 || !z || this.displayedDailyGoal100) {
                return;
            }
            displayDailyGoal100();
            this.didDisplay100 = true;
            this.displayedDailyGoal100 = true;
            this.didDisplay50 = true;
            this.displayedDailyGoal50 = true;
            AppActivityManager.shared.sendDailyGoal50Event();
            AppActivityManager.shared.sendDailyGoal100Event();
        } else if (d >= this.userProfile.durationGoal() * 0.45d) {
            if (!this.didInit50 && z2 && !this.displayedDailyGoal50) {
                initDailyGoal50();
                this.didInit50 = true;
            }
            if (d < this.userProfile.durationGoal() * 0.5d || this.didDisplay50 || !z2 || this.displayedDailyGoal50) {
                return;
            }
            displayDailyGoal50();
            this.didDisplay50 = true;
            this.displayedDailyGoal50 = true;
            AppActivityManager.shared.sendDailyGoal50Event();
        }
    }

    public void clearAnimations() {
        this.didInit50 = false;
        this.didInit100 = false;
        this.didDisplay50 = false;
        this.didDisplay100 = false;
    }

    public void dismissDailyGoal100() {
        this.mainActivity.getSupportFragmentManager().popBackStack();
        this.daily100Fragment = null;
    }

    public void initDailyGoal100() {
        this.daily100Fragment = new TreadlyConnectDailyGoalFragment();
        this.daily100Fragment.type = TreadlyDailyGoalType.dailyGoal100;
        this.daily100Fragment.animationView = new TreadlyConnectDailyGoalAnimationView(this.mainActivity);
        this.daily100Fragment.loadAnimation();
        this.daily100Fragment.dailyGoalListener = new TreadlyConnectDailyGoalFragment.TreadlyConnectDailyGoalFragmentListener() { // from class: com.treadly.Treadly.Data.Managers.RunningSessionManager.4
            @Override // com.treadly.Treadly.UI.TreadlyConnect.DailyGoalView.TreadlyConnectDailyGoalFragment.TreadlyConnectDailyGoalFragmentListener
            public void didPressClose() {
                RunningSessionManager.this.dismissDailyGoal100();
                RunningSessionManager.this.mainActivity.setOnBackPressedListener(RunningSessionManager.this.currentListener);
            }
        };
    }

    public void displayDailyGoal100() {
        if (this.daily100Fragment == null) {
            initDailyGoal100();
        }
        setLastBackListener(this.mainActivity.getOnBackPressedListener());
        FragmentManager supportFragmentManager = this.mainActivity.getSupportFragmentManager();
        String str = null;
        if (supportFragmentManager != null) {
            List<Fragment> fragments = supportFragmentManager.getFragments();
            if (!fragments.isEmpty()) {
                str = fragments.get(fragments.size() - 1).getTag();
            }
        }
        this.mainActivity.getSupportFragmentManager().beginTransaction().addToBackStack(str).add(R.id.activity_fragment_container_empty, this.daily100Fragment).commit();
    }

    public void checkDailyGoals() {
        if (this.inProgress) {
            return;
        }
        TreadlyServiceManager.getInstance().getUserProfileInfo(TreadlyServiceManager.getInstance().getUserId(), new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.Data.Managers.RunningSessionManager.5
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onUserProfile(String str, UserProfileInfo userProfileInfo) {
                if (str != null || userProfileInfo == null) {
                    return;
                }
                RunningSessionManager.this.checkDailyGoalEvents(userProfileInfo);
            }
        });
    }

    public void checkDailyGoalEvents(final UserProfileInfo userProfileInfo) {
        AppActivityManager.shared.hasDailyGoal50(new AppActivityManager.AppActivityManagerHasDailyGoalResponse() { // from class: com.treadly.Treadly.Data.Managers.RunningSessionManager.6
            @Override // com.treadly.Treadly.Data.Managers.AppActivityManager.AppActivityManagerHasDailyGoalResponse
            public void onResponse(String str, boolean z) {
                if (str == null) {
                    RunningSessionManager.this.displayedDailyGoal50 = z;
                    AppActivityManager.shared.hasDailyGoal100(new AppActivityManager.AppActivityManagerHasDailyGoalResponse() { // from class: com.treadly.Treadly.Data.Managers.RunningSessionManager.6.1
                        @Override // com.treadly.Treadly.Data.Managers.AppActivityManager.AppActivityManagerHasDailyGoalResponse
                        public void onResponse(String str2, boolean z2) {
                            if (str2 == null) {
                                RunningSessionManager.this.displayedDailyGoal100 = z2;
                                RunningSessionManager.this.checkDailyGoalNotifications(userProfileInfo);
                                return;
                            }
                            RunningSessionManager.this.displayedDailyGoal100 = true;
                        }
                    });
                    return;
                }
                RunningSessionManager.this.displayedDailyGoal50 = true;
                RunningSessionManager.this.displayedDailyGoal100 = true;
            }
        });
    }

    public void checkDailyGoalNotifications(final UserProfileInfo userProfileInfo) {
        VideoServiceHelper.getNotificationSettings(new VideoServiceHelper.VideoNotificationsListener() { // from class: com.treadly.Treadly.Data.Managers.RunningSessionManager.7
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoNotificationsListener
            public void onResponse(String str, UserNotificationSettingInfo userNotificationSettingInfo) {
                if (str != null || userNotificationSettingInfo == null) {
                    return;
                }
                RunningSessionManager.this.checkDailyGoalCalories(userProfileInfo, userNotificationSettingInfo);
            }
        });
    }

    public void checkDailyGoalCalories(UserProfileInfo userProfileInfo, UserNotificationSettingInfo userNotificationSettingInfo) {
        Calendar calendar = Calendar.getInstance();
        final int i = calendar.get(5);
        final int i2 = calendar.get(2);
        final int i3 = calendar.get(1);
        TreadlyServiceManager.getInstance().getDeviceUserStatsInfo(new AnonymousClass8(i, i2, i3, userProfileInfo, userNotificationSettingInfo));
        TreadlyServiceManager.getInstance().getDeviceUserStatsInfo(true, (TreadlyServiceResponseEventListener) new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.Data.Managers.RunningSessionManager.9
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onUserStatsInfo(String str, ArrayList<UserStatsInfo> arrayList) {
                if (str != null || arrayList == null) {
                    return;
                }
                Iterator<UserStatsInfo> it = arrayList.iterator();
                while (it.hasNext()) {
                    final UserStatsInfo next = it.next();
                    if (next.timestamp != null) {
                        Calendar calendar2 = Calendar.getInstance();
                        calendar2.setTime(next.timestamp);
                        int i4 = calendar2.get(5);
                        int i5 = calendar2.get(2);
                        int i6 = calendar2.get(1);
                        if (i4 == i && i5 == i2 && i6 == i3) {
                            RunningSessionManager.this.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.RunningSessionManager.9.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    RunningSessionManager.this.calories = next.calories;
                                    RunningSessionManager.this.dailySteps = next.steps;
                                    RunningSessionManager.this.dailyDistance = next.distance;
                                    RunningSessionManager.this.dailyDuration = next.duration;
                                    RunningSessionManager.this.currentDailyCalories = next.calories;
                                    RunningSessionManager.this.currentDailySteps = next.steps;
                                    RunningSessionManager.this.currentDailyDistance = next.distance;
                                    RunningSessionManager.this.currentDailyDuration = next.duration;
                                    RunningSessionManager.this.currentCalories = 0;
                                    RunningSessionManager.this.currentSteps = 0;
                                    RunningSessionManager.this.currentDistance = Utils.DOUBLE_EPSILON;
                                    RunningSessionManager.this.currentDuration = Utils.DOUBLE_EPSILON;
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.Data.Managers.RunningSessionManager$8  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass8 extends TreadlyServiceResponseEventAdapter {
        final /* synthetic */ UserProfileInfo val$profile;
        final /* synthetic */ UserNotificationSettingInfo val$settings;
        final /* synthetic */ int val$todaysDay;
        final /* synthetic */ int val$todaysMonth;
        final /* synthetic */ int val$todaysYear;

        AnonymousClass8(int i, int i2, int i3, UserProfileInfo userProfileInfo, UserNotificationSettingInfo userNotificationSettingInfo) {
            this.val$todaysDay = i;
            this.val$todaysMonth = i2;
            this.val$todaysYear = i3;
            this.val$profile = userProfileInfo;
            this.val$settings = userNotificationSettingInfo;
        }

        @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
        public void onUserStatsInfo(String str, ArrayList<UserStatsInfo> arrayList) {
            if (str != null || arrayList == null) {
                return;
            }
            Iterator<UserStatsInfo> it = arrayList.iterator();
            while (it.hasNext()) {
                final UserStatsInfo next = it.next();
                if (next.timestamp != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(next.timestamp);
                    int i = calendar.get(5);
                    int i2 = calendar.get(2);
                    int i3 = calendar.get(1);
                    if (i == this.val$todaysDay && i2 == this.val$todaysMonth && i3 == this.val$todaysYear) {
                        RunningSessionManager runningSessionManager = RunningSessionManager.this;
                        final UserProfileInfo userProfileInfo = this.val$profile;
                        final UserNotificationSettingInfo userNotificationSettingInfo = this.val$settings;
                        runningSessionManager.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$RunningSessionManager$8$DvK3bqfDSMN9QRHMCFqGv-s8kDw
                            @Override // java.lang.Runnable
                            public final void run() {
                                RunningSessionManager.this.checkDailyGoalProgress(userProfileInfo, userNotificationSettingInfo, r3.calories, r3.steps, r3.distance, next.duration);
                            }
                        });
                        RunningSessionManager.this.setTotalDailyCalories(next.calories);
                        RunningSessionManager.this.setTotalDailySteps(next.steps);
                        RunningSessionManager.this.setTotalDailyDistance(next.distance);
                        RunningSessionManager.this.setTotalDailyDuration(next.duration);
                    }
                }
            }
        }
    }

    public void checkDailyGoalProgress(UserProfileInfo userProfileInfo, UserNotificationSettingInfo userNotificationSettingInfo, int i, int i2, double d, double d2) {
        double dailyGoal = userProfileInfo.getDailyGoal();
        int i3 = AnonymousClass10.$SwitchMap$com$treadly$Treadly$Data$Model$UserDailyGoalType[userProfileInfo.dailyGoalType().ordinal()];
        double d3 = Utils.DOUBLE_EPSILON;
        switch (i3) {
            case 1:
                d2 = i;
                break;
            case 2:
                d2 = i2;
                break;
            case 3:
                if (d > Utils.DOUBLE_EPSILON) {
                    d3 = 1.0E-4d;
                }
                d2 = d + d3;
                break;
            case 4:
                break;
            default:
                d2 = 0.0d;
                break;
        }
        double d4 = 0.5d * dailyGoal;
        if (d2 < d4) {
            return;
        }
        boolean z = false;
        boolean z2 = userNotificationSettingInfo.dailyGoal50Achieved && userNotificationSettingInfo.enable;
        if (userNotificationSettingInfo.dailyGoal100Achieved && userNotificationSettingInfo.enable) {
            z = true;
        }
        int i4 = (d2 > dailyGoal ? 1 : (d2 == dailyGoal ? 0 : -1));
        if (i4 < 0) {
            if (d2 >= d4) {
                if (!this.didInit50 && z2 && !this.displayedDailyGoal50) {
                    initDailyGoal50();
                    this.didInit50 = true;
                }
                if (this.didDisplay50 || !z2 || this.displayedDailyGoal50) {
                    return;
                }
                displayDailyGoal50();
                this.didDisplay50 = true;
                this.displayedDailyGoal50 = true;
                AppActivityManager.shared.sendDailyGoal50Event();
                return;
            }
            return;
        }
        if (!this.didInit100 && z && !this.displayedDailyGoal100) {
            initDailyGoal100();
            this.didInit100 = true;
            this.didInit50 = true;
        }
        if (i4 < 0 || this.didDisplay100 || !z || this.displayedDailyGoal100) {
            return;
        }
        displayDailyGoal100();
        this.didDisplay100 = true;
        this.displayedDailyGoal100 = true;
        this.didDisplay50 = true;
        this.displayedDailyGoal50 = true;
        AppActivityManager.shared.sendDailyGoal50Event();
        AppActivityManager.shared.sendDailyGoal100Event();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.Data.Managers.RunningSessionManager$10  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass10 {
        static final /* synthetic */ int[] $SwitchMap$com$treadly$Treadly$Data$Model$UserDailyGoalType = new int[UserDailyGoalType.values().length];

        static {
            try {
                $SwitchMap$com$treadly$Treadly$Data$Model$UserDailyGoalType[UserDailyGoalType.calories.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$treadly$Treadly$Data$Model$UserDailyGoalType[UserDailyGoalType.steps.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$treadly$Treadly$Data$Model$UserDailyGoalType[UserDailyGoalType.distance.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$treadly$Treadly$Data$Model$UserDailyGoalType[UserDailyGoalType.duration.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    public int getLastTotalCalories() {
        Date runningSessionLastTotalCaloriesTime;
        String userId = TreadlyServiceManager.getInstance().getUserId();
        if (userId == null || (runningSessionLastTotalCaloriesTime = SharedPreferences.shared.getRunningSessionLastTotalCaloriesTime(userId)) == null || !runningSessionLastTotalCaloriesTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
            return -1;
        }
        return SharedPreferences.shared.getRunningSessionLastTotalCalories(userId);
    }

    public void setLastTotalCalories(int i) {
        String userId = TreadlyServiceManager.getInstance().getUserId();
        if (userId == null) {
            return;
        }
        SharedPreferences.shared.setRunningSessionLastTotalCaloriesTime(new Date(), userId);
        SharedPreferences.shared.setRunningSessionLastTotalCalories(i, userId);
    }

    public int getLastTotalSteps() {
        Date runningSessionLastTotalStepsTime;
        String userId = TreadlyServiceManager.getInstance().getUserId();
        if (userId == null || (runningSessionLastTotalStepsTime = SharedPreferences.shared.getRunningSessionLastTotalStepsTime(userId)) == null || !runningSessionLastTotalStepsTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
            return -1;
        }
        return SharedPreferences.shared.getRunningSessionLastTotalSteps(userId);
    }

    public void setLastTotalSteps(int i) {
        String userId = TreadlyServiceManager.getInstance().getUserId();
        if (userId == null) {
            return;
        }
        SharedPreferences.shared.setRunningSessionLastTotalStepsTime(new Date(), userId);
        SharedPreferences.shared.setRunningSessionLastTotalSteps(i, userId);
    }

    public double getLastTotalDistance() {
        Date runningSessionLastTotalDistanceTime;
        String userId = TreadlyServiceManager.getInstance().getUserId();
        if (userId == null || (runningSessionLastTotalDistanceTime = SharedPreferences.shared.getRunningSessionLastTotalDistanceTime(userId)) == null || !runningSessionLastTotalDistanceTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
            return -1.0d;
        }
        return SharedPreferences.shared.getRunningSessionLastTotalDistance(userId);
    }

    public void setLastTotalDistance(double d) {
        String userId = TreadlyServiceManager.getInstance().getUserId();
        if (userId == null) {
            return;
        }
        SharedPreferences.shared.setRunningSessionLastTotalDistanceTime(new Date(), userId);
        SharedPreferences.shared.setRunningSessionLastTotalDistance(d, userId);
    }

    public double getLastTotalDuration() {
        Date runningSessionLastTotalDurationTime;
        String userId = TreadlyServiceManager.getInstance().getUserId();
        if (userId == null || (runningSessionLastTotalDurationTime = SharedPreferences.shared.getRunningSessionLastTotalDurationTime(userId)) == null || !runningSessionLastTotalDurationTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
            return -1.0d;
        }
        return SharedPreferences.shared.getRunningSessionLastTotalDuration(userId);
    }

    public void setLastTotalDuration(double d) {
        String userId = TreadlyServiceManager.getInstance().getUserId();
        if (userId == null) {
            return;
        }
        SharedPreferences.shared.setRunningSessionLastTotalDurationTime(new Date(), userId);
        SharedPreferences.shared.setRunningSessionLastTotalDuration(d, userId);
    }

    public int getLastDailyGoal() {
        Date runningSessionLastDailyGoalTime;
        String userId = TreadlyServiceManager.getInstance().getUserId();
        if (userId == null || (runningSessionLastDailyGoalTime = SharedPreferences.shared.getRunningSessionLastDailyGoalTime(userId)) == null || !runningSessionLastDailyGoalTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
            return -1;
        }
        return SharedPreferences.shared.getRunningSessionLastDailyGoal(userId);
    }

    public void setLastDailyGoal(int i) {
        String userId = TreadlyServiceManager.getInstance().getUserId();
        if (userId == null) {
            return;
        }
        SharedPreferences.shared.setRunningSessionLastDailyGoalTime(new Date(), userId);
        SharedPreferences.shared.setRunningSessionLastDailyGoal(i, userId);
    }

    public String getLastDailyGoalTarget() {
        Date runningSessionLastDailyGoalTargetTime;
        String userId = TreadlyServiceManager.getInstance().getUserId();
        return (userId == null || (runningSessionLastDailyGoalTargetTime = SharedPreferences.shared.getRunningSessionLastDailyGoalTargetTime(userId)) == null || !runningSessionLastDailyGoalTargetTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) ? "" : SharedPreferences.shared.getRunningSessionLastDailyGoalTarget(userId);
    }

    public void setLastDailyGoalTarget(String str) {
        String userId = TreadlyServiceManager.getInstance().getUserId();
        if (userId == null) {
            return;
        }
        SharedPreferences.shared.setRunningSessionLastDailyGoalTargetTime(new Date(), userId);
        SharedPreferences.shared.setRunningSessionLastDailyGoalTarget(str, userId);
    }

    void runOnMain(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
