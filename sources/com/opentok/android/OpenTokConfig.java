package com.opentok.android;

/* loaded from: classes.dex */
public class OpenTokConfig {
    private static boolean dumpClientLoggingToFileEnabled = false;
    private static boolean jniLogsEnabled = false;
    private static boolean otkitLogsEnabled = false;
    private static PreferH264 preferH264Codec = PreferH264.NOT_SET;
    private static String rtcStatsReportFilePath = null;
    private static boolean webrtcLogsEnabled = false;

    /* loaded from: classes.dex */
    public enum PreferH264 {
        NOT_SET,
        ENABLE,
        DISABLE
    }

    public static boolean getDumpClientLoggingToFile() {
        return dumpClientLoggingToFileEnabled;
    }

    public static boolean getJNILogs() {
        return jniLogsEnabled;
    }

    public static boolean getOTKitLogs() {
        return otkitLogsEnabled;
    }

    public static PreferH264 getPreferH264Codec() {
        return preferH264Codec;
    }

    public static String getRTCStatsReportFilePath() {
        return rtcStatsReportFilePath;
    }

    public static boolean getWebRTCLogs() {
        return webrtcLogsEnabled;
    }

    public static void setDumpClientLoggingToFile(boolean z) {
        dumpClientLoggingToFileEnabled = z;
    }

    public static void setJNILogs(boolean z) {
        jniLogsEnabled = z;
    }

    public static void setOTKitLogs(boolean z) {
        otkitLogsEnabled = z;
    }

    public static void setPreferH264Codec(PreferH264 preferH264) {
        preferH264Codec = preferH264;
    }

    public static void setRTCStatsReportFilePath(String str) {
        rtcStatsReportFilePath = str;
    }

    public static void setWebRTCLogs(boolean z) {
        webrtcLogsEnabled = z;
    }
}
