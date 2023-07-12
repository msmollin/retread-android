package com.facebook.internal.instrument;

import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import com.facebook.internal.Utility;
import java.io.File;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* loaded from: classes.dex */
public final class InstrumentData {
    private static final String PARAM_APP_VERSION = "app_version";
    private static final String PARAM_CALLSTACK = "callstack";
    private static final String PARAM_DEVICE_MODEL = "device_model";
    private static final String PARAM_DEVICE_OS = "device_os_version";
    private static final String PARAM_FEATURE_NAMES = "feature_names";
    private static final String PARAM_REASON = "reason";
    private static final String PARAM_TIMESTAMP = "timestamp";
    private static final String PARAM_TYPE = "type";
    private static final String UNKNOWN = "Unknown";
    @Nullable
    private String appVersion;
    @Nullable
    private String cause;
    @Nullable
    private JSONArray featureNames;
    private String filename;
    @Nullable
    private String stackTrace;
    @Nullable
    private Long timestamp;
    private Type type;

    /* loaded from: classes.dex */
    public enum Type {
        Unknown,
        Analysis,
        CrashReport,
        CrashShield,
        ThreadCheck;

        @Override // java.lang.Enum
        public String toString() {
            switch (this) {
                case Analysis:
                    return "Analysis";
                case CrashReport:
                    return "CrashReport";
                case CrashShield:
                    return "CrashShield";
                case ThreadCheck:
                    return "ThreadCheck";
                default:
                    return "Unknown";
            }
        }

        public String getLogPrefix() {
            switch (this) {
                case Analysis:
                    return InstrumentUtility.ANALYSIS_REPORT_PREFIX;
                case CrashReport:
                    return InstrumentUtility.CRASH_REPORT_PREFIX;
                case CrashShield:
                    return InstrumentUtility.CRASH_SHIELD_PREFIX;
                case ThreadCheck:
                    return InstrumentUtility.THREAD_CHECK_PREFIX;
                default:
                    return "Unknown";
            }
        }
    }

    private InstrumentData(JSONArray jSONArray) {
        this.type = Type.Analysis;
        this.timestamp = Long.valueOf(System.currentTimeMillis() / 1000);
        this.featureNames = jSONArray;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(InstrumentUtility.ANALYSIS_REPORT_PREFIX);
        stringBuffer.append(this.timestamp.toString());
        stringBuffer.append(".json");
        this.filename = stringBuffer.toString();
    }

    private InstrumentData(Throwable th, Type type) {
        this.type = type;
        this.appVersion = Utility.getAppVersion();
        this.cause = InstrumentUtility.getCause(th);
        this.stackTrace = InstrumentUtility.getStackTrace(th);
        this.timestamp = Long.valueOf(System.currentTimeMillis() / 1000);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(type.getLogPrefix());
        stringBuffer.append(this.timestamp.toString());
        stringBuffer.append(".json");
        this.filename = stringBuffer.toString();
    }

    private InstrumentData(File file) {
        this.filename = file.getName();
        this.type = getType(this.filename);
        JSONObject readFile = InstrumentUtility.readFile(this.filename, true);
        if (readFile != null) {
            this.timestamp = Long.valueOf(readFile.optLong("timestamp", 0L));
            this.appVersion = readFile.optString(PARAM_APP_VERSION, null);
            this.cause = readFile.optString(PARAM_REASON, null);
            this.stackTrace = readFile.optString(PARAM_CALLSTACK, null);
            this.featureNames = readFile.optJSONArray(PARAM_FEATURE_NAMES);
        }
    }

    private static Type getType(String str) {
        if (str.startsWith(InstrumentUtility.CRASH_REPORT_PREFIX)) {
            return Type.CrashReport;
        }
        if (str.startsWith(InstrumentUtility.CRASH_SHIELD_PREFIX)) {
            return Type.CrashShield;
        }
        if (str.startsWith(InstrumentUtility.THREAD_CHECK_PREFIX)) {
            return Type.ThreadCheck;
        }
        if (str.startsWith(InstrumentUtility.ANALYSIS_REPORT_PREFIX)) {
            return Type.Analysis;
        }
        return Type.Unknown;
    }

    public int compareTo(InstrumentData instrumentData) {
        if (this.timestamp == null) {
            return -1;
        }
        if (instrumentData.timestamp == null) {
            return 1;
        }
        return instrumentData.timestamp.compareTo(this.timestamp);
    }

    public boolean isValid() {
        switch (this.type) {
            case Analysis:
                return (this.featureNames == null || this.timestamp == null) ? false : true;
            case CrashReport:
            case CrashShield:
            case ThreadCheck:
                return (this.stackTrace == null || this.timestamp == null) ? false : true;
            default:
                return false;
        }
    }

    public void save() {
        if (isValid()) {
            InstrumentUtility.writeFile(this.filename, toString());
        }
    }

    public void clear() {
        InstrumentUtility.deleteFile(this.filename);
    }

    @Nullable
    public String toString() {
        JSONObject parameters = getParameters();
        if (parameters == null) {
            return null;
        }
        return parameters.toString();
    }

    @Nullable
    private JSONObject getParameters() {
        switch (this.type) {
            case Analysis:
                return getAnalysisReportParameters();
            case CrashReport:
            case CrashShield:
            case ThreadCheck:
                return getExceptionReportParameters();
            default:
                return null;
        }
    }

    @Nullable
    private JSONObject getAnalysisReportParameters() {
        JSONObject jSONObject = new JSONObject();
        try {
            if (this.featureNames != null) {
                jSONObject.put(PARAM_FEATURE_NAMES, this.featureNames);
            }
            if (this.timestamp != null) {
                jSONObject.put("timestamp", this.timestamp);
            }
            return jSONObject;
        } catch (JSONException unused) {
            return null;
        }
    }

    @Nullable
    private JSONObject getExceptionReportParameters() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("device_os_version", Build.VERSION.RELEASE);
            jSONObject.put("device_model", Build.MODEL);
            if (this.appVersion != null) {
                jSONObject.put(PARAM_APP_VERSION, this.appVersion);
            }
            if (this.timestamp != null) {
                jSONObject.put("timestamp", this.timestamp);
            }
            if (this.cause != null) {
                jSONObject.put(PARAM_REASON, this.cause);
            }
            if (this.stackTrace != null) {
                jSONObject.put(PARAM_CALLSTACK, this.stackTrace);
            }
            if (this.type != null) {
                jSONObject.put("type", this.type);
            }
            return jSONObject;
        } catch (JSONException unused) {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static class Builder {
        public static InstrumentData load(File file) {
            return new InstrumentData(file);
        }

        public static InstrumentData build(Throwable th, Type type) {
            return new InstrumentData(th, type);
        }

        public static InstrumentData build(JSONArray jSONArray) {
            return new InstrumentData(jSONArray);
        }
    }
}
