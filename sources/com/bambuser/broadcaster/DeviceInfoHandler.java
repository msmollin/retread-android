package com.bambuser.broadcaster;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;
import android.util.Size;
import com.bambuser.broadcaster.SentryLogger;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.imagepipeline.common.RotationOptions;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class DeviceInfoHandler {
    private static final String LOGTAG = "DeviceInfoHandler";
    private static List<CamInfo> sCameras;

    DeviceInfoHandler() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getModel() {
        String systemPropertyString = getSystemPropertyString("ro.modversion");
        StringBuilder sb = new StringBuilder(128);
        sb.append("Android, ");
        sb.append(Build.MODEL);
        sb.append(" (");
        sb.append(Build.DEVICE);
        sb.append(", ");
        sb.append(Build.DISPLAY);
        if (!systemPropertyString.equals("")) {
            sb.append(" [");
            sb.append(systemPropertyString);
            sb.append("]");
        }
        sb.append(", ");
        sb.append(Build.VERSION.RELEASE);
        sb.append(")");
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException unused) {
            return "";
        }
    }

    private static void findSupportedPreviewSizes(Camera.Parameters parameters, Collection<Resolution> collection) {
        List<Camera.Size> supportedPreviewSizes;
        if (parameters == null || (supportedPreviewSizes = parameters.getSupportedPreviewSizes()) == null) {
            return;
        }
        for (Camera.Size size : supportedPreviewSizes) {
            if (size.width >= 176 && size.height >= 144 && size.width <= 1280 && size.height <= 720) {
                collection.add(new Resolution(size.width, size.height));
            } else {
                Log.d(LOGTAG, "not listing resolution: " + size.width + "x" + size.height);
            }
        }
        if (!collection.isEmpty() || supportedPreviewSizes.isEmpty()) {
            return;
        }
        for (Camera.Size size2 : supportedPreviewSizes) {
            if (size2.width > 0 && size2.height > 0) {
                collection.add(new Resolution(size2.width, size2.height));
            }
        }
    }

    private static void findSupportedPictureSizes(Camera.Parameters parameters, Collection<Resolution> collection) {
        List<Camera.Size> supportedPictureSizes;
        if (parameters == null || (supportedPictureSizes = parameters.getSupportedPictureSizes()) == null) {
            return;
        }
        for (Camera.Size size : supportedPictureSizes) {
            collection.add(new Resolution(size.width, size.height));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void findSupportedResolutions(Camera.Parameters parameters, CamInfo camInfo) {
        HashSet hashSet = new HashSet();
        findSupportedPreviewSizes(parameters, hashSet);
        if (hashSet.isEmpty()) {
            hashSet.add(new Resolution(176, 144));
            hashSet.add(new Resolution(320, 240));
            hashSet.add(new Resolution(352, 288));
            hashSet.add(new Resolution(480, 320));
        }
        if (isSonyXperiaM2() || isSamsGalaxyS4Snapdragon() || isSamsGalaxyNoteIIISnapdragon()) {
            Iterator it = hashSet.iterator();
            while (it.hasNext()) {
                if (((Resolution) it.next()).getHeight() > 720) {
                    it.remove();
                }
            }
        }
        LinkedList linkedList = new LinkedList(hashSet);
        Collections.sort(linkedList);
        camInfo.setPreviewList(linkedList);
        HashSet hashSet2 = new HashSet();
        findSupportedPictureSizes(parameters, hashSet2);
        LinkedList linkedList2 = new LinkedList(hashSet2);
        Collections.sort(linkedList2);
        camInfo.setPictureList(linkedList2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Resolution getDefaultResolution(CamInfo camInfo) {
        List<Resolution> previewList = camInfo != null ? camInfo.getPreviewList() : null;
        if (previewList == null || previewList.isEmpty()) {
            Log.w(LOGTAG, "no default resolution found, using hardcoded default");
            return new Resolution(176, 144);
        }
        Resolution resolution = previewList.get(0);
        if (isMediaCodecSupported() || NativeUtils.hasArm64() || NativeUtils.hasNeon()) {
            for (Resolution resolution2 : previewList) {
                if (resolution2.getHeight() <= 320) {
                    resolution = resolution2;
                }
            }
        }
        return resolution;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static CamInfo findCamInfo(Context context, String str) {
        for (CamInfo camInfo : getSupportedCameras(context)) {
            if (camInfo.mId.equals(str)) {
                return camInfo;
            }
        }
        return null;
    }

    private static CamInfo createCamInfo(int i) {
        try {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(i, cameraInfo);
            return new CamInfo(i, cameraInfo.facing, cameraInfo.orientation, cameraInfo.canDisableShutterSound);
        } catch (Exception e) {
            Log.w(LOGTAG, "createCamInfo exception: " + e);
            return null;
        }
    }

    @TargetApi(21)
    private static CamInfo createCamInfo(Object obj, String str) {
        try {
            CameraCharacteristics cameraCharacteristics = ((CameraManager) obj).getCameraCharacteristics(str);
            CamInfo camInfo = new CamInfo(str, ((Integer) cameraCharacteristics.get(CameraCharacteristics.LENS_FACING)).intValue(), ((Integer) cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)).intValue());
            StreamConfigurationMap streamConfigurationMap = (StreamConfigurationMap) cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            Size[] outputSizes = streamConfigurationMap.getOutputSizes(35);
            LinkedList linkedList = new LinkedList();
            if (outputSizes != null) {
                for (Size size : outputSizes) {
                    Log.i(LOGTAG, "createCamInfo adding preview size: " + size);
                    linkedList.add(new Resolution(size.getWidth(), size.getHeight()));
                }
            }
            Collections.sort(linkedList);
            camInfo.setPreviewList(linkedList);
            LinkedList linkedList2 = new LinkedList();
            Size[] outputSizes2 = streamConfigurationMap.getOutputSizes(256);
            if (outputSizes2 != null) {
                for (Size size2 : outputSizes2) {
                    Log.i(LOGTAG, "createCamInfo adding jpeg size: " + size2);
                    linkedList2.add(new Resolution(size2.getWidth(), size2.getHeight()));
                }
            }
            Collections.sort(linkedList2);
            camInfo.setPictureList(linkedList2);
            return camInfo;
        } catch (Exception e) {
            Log.w(LOGTAG, "createCamInfo exception: " + e);
            SentryLogger.asyncMessage("createCamInfo exception", SentryLogger.Level.WARNING, null, e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @TargetApi(21)
    public static int getCamera2HardwareLevel(Context context, String str) {
        try {
            Integer num = (Integer) ((CameraManager) context.getSystemService("camera")).getCameraCharacteristics(str).get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
            if (num != null) {
                return num.intValue();
            }
            return 2;
        } catch (Throwable unused) {
            return 2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean useCamera2API(Context context) {
        return Build.VERSION.SDK_INT >= 22 && getCamera2HardwareLevel(context, AppEventsConstants.EVENT_PARAM_VALUE_NO) != 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static List<CamInfo> getSupportedCameras(Context context) {
        if (sCameras != null) {
            return sCameras;
        }
        LinkedList linkedList = new LinkedList();
        int i = 0;
        if (!useCamera2API(context)) {
            int numberOfCameras = Camera.getNumberOfCameras();
            while (i < numberOfCameras) {
                CamInfo createCamInfo = createCamInfo(i);
                if (createCamInfo != null) {
                    Log.i(LOGTAG, "Found camera with facing " + createCamInfo.mFacing + " and orientation " + createCamInfo.mOrientation);
                    linkedList.add(createCamInfo);
                }
                i++;
            }
        } else {
            Object systemService = context.getSystemService("camera");
            String[] strArr = (String[]) Compat.tryCall(systemService, "getCameraIdList");
            while (i < strArr.length) {
                CamInfo createCamInfo2 = createCamInfo(systemService, strArr[i]);
                if (createCamInfo2 != null) {
                    Log.i(LOGTAG, "Found camera with facing " + createCamInfo2.mFacing + " and orientation " + createCamInfo2.mOrientation);
                    linkedList.add(createCamInfo2);
                }
                i++;
            }
        }
        sCameras = linkedList;
        return linkedList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getFrameRotation(CamInfo camInfo, int i) {
        int i2 = 0;
        boolean z = camInfo != null && camInfo.isFront();
        int i3 = camInfo != null ? camInfo.mOrientation : 90;
        switch (i) {
            case 1:
                i2 = 90;
                break;
            case 2:
                i2 = RotationOptions.ROTATE_180;
                break;
            case 3:
                i2 = RotationOptions.ROTATE_270;
                break;
        }
        if (z) {
            return (i3 + i2) % 360;
        }
        return ((i3 - i2) + 360) % 360;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean scanResolutions(CamInfo camInfo) {
        boolean z;
        Camera camera = null;
        try {
            Camera open = Camera.open(Integer.parseInt(camInfo.mId));
            try {
                findSupportedResolutions(open.getParameters(), camInfo);
                z = true;
                camera = open;
            } catch (Exception e) {
                e = e;
                camera = open;
                Log.w(LOGTAG, "Couldn't read supported resolutions from camera " + camInfo.mId + ": " + e);
                z = false;
                camera.release();
                return z;
            }
        } catch (Exception e2) {
            e = e2;
        }
        try {
            camera.release();
        } catch (Exception unused) {
        }
        return z;
    }

    static void requestPermissions(Activity activity, List<String> list, int i) {
        try {
            Activity.class.getMethod("requestPermissions", String[].class, Integer.TYPE).invoke(activity, (String[]) list.toArray(new String[list.size()]), Integer.valueOf(i));
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean hasPermission(Context context, String str) {
        if (Build.VERSION.SDK_INT < 23) {
            return context.checkCallingOrSelfPermission(str) == 0;
        }
        try {
            return ((Integer) context.getClass().getMethod("checkSelfPermission", String.class).invoke(context, str)).intValue() == 0;
        } catch (Exception unused) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isNetworkType(Context context, int i) {
        NetworkInfo activeNetworkInfo;
        return hasPermission(context, "android.permission.ACCESS_NETWORK_STATE") && (activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo()) != null && activeNetworkInfo.getType() == i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static WifiManager.WifiLock getWifiLock(Context context) {
        return ((WifiManager) context.getApplicationContext().getSystemService("wifi")).createWifiLock(3, LOGTAG);
    }

    private static String getSystemPropertyString(String str) {
        try {
            String str2 = (String) Class.forName("android.os.SystemProperties").getMethod("get", String.class).invoke(null, str);
            return str2 != null ? str2 : "";
        } catch (Throwable unused) {
            return "";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isMediaCodecSupported() {
        return Build.VERSION.SDK_INT >= 23 || isMSM8x60() || isSnapdragon400() || isSnapdragon800() || isSnapdragon805() || isSnapdragon64bit() || isExynos5() || isTegraK1() || isGoogleGN() || isGoogleN4() || isGoogleN5() || isGoogleN7() || isGoogleGlass() || isSamsGalaxyNoteII() || isSamsGalaxySIII() || isSamsGalaxyS4Exynos() || isSamsGalaxyS4Snapdragon() || isSamsGalaxyNoteIIISnapdragon() || isSamsGalaxyNote_10_1_2014Snapdragon() || isSamsGalaxyS5Snapdragon() || isSonyXperiaSP() || isSonyXperiaV() || isSonyXperiaT() || isSonyXperiaTX() || isSonyXperiaTabletZ() || isSonyXperiaZR() || isSonyXperiaZ() || isSonyXperiaZL() || isSonyXperiaZUltra() || isSonyXperiaZ1() || isSonyXperiaZ1Compact() || isSonyXperiaT2Ultra() || isSonyXperiaM2() || isSonyXperiaZ2() || isSonyXperiaZ2Tablet();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean useRecordingHint() {
        return isGoogleN4();
    }

    static boolean isSonyXperiaV() {
        return Build.DEVICE.equalsIgnoreCase("LT25i");
    }

    static boolean isSonyXperiaTX() {
        return Build.DEVICE.equalsIgnoreCase("LT29i");
    }

    static boolean isSonyXperiaT() {
        return Build.DEVICE.equalsIgnoreCase("LT30p");
    }

    static boolean isSonyXperiaZL() {
        return Build.DEVICE.equalsIgnoreCase("C6503") || Build.DEVICE.equalsIgnoreCase("C6502") || Build.DEVICE.equalsIgnoreCase("C6506");
    }

    static boolean isSonyXperiaZ() {
        return Build.DEVICE.equalsIgnoreCase("C6603") || Build.DEVICE.equalsIgnoreCase("C6602") || Build.DEVICE.equalsIgnoreCase("L36h");
    }

    static boolean isSonyXperiaSP() {
        return Build.DEVICE.equalsIgnoreCase("C5303") || Build.DEVICE.equalsIgnoreCase("C5302") || Build.DEVICE.equalsIgnoreCase("C5306");
    }

    static boolean isSonyXperiaZR() {
        return Build.DEVICE.equalsIgnoreCase("C5503") || Build.DEVICE.equalsIgnoreCase("C5502");
    }

    static boolean isSonyXperiaZUltra() {
        return Build.DEVICE.equalsIgnoreCase("C6802") || Build.DEVICE.equalsIgnoreCase("C6833") || Build.DEVICE.equalsIgnoreCase("C6806") || Build.DEVICE.equalsIgnoreCase("C6843");
    }

    static boolean isSonyXperiaZ1() {
        return Build.DEVICE.equalsIgnoreCase("C6902") || Build.DEVICE.equalsIgnoreCase("C6903") || Build.DEVICE.equalsIgnoreCase("C6906") || Build.DEVICE.equalsIgnoreCase("C6916") || Build.DEVICE.equalsIgnoreCase("C6943") || Build.DEVICE.equalsIgnoreCase("SOL23") || Build.DEVICE.equalsIgnoreCase("SO-01F");
    }

    static boolean isSonyXperiaZ1Compact() {
        return Build.DEVICE.equalsIgnoreCase("D5503") || Build.DEVICE.equalsIgnoreCase("SO-02F");
    }

    static boolean isSonyXperiaTabletZ() {
        return Build.DEVICE.equalsIgnoreCase("SGP311") || Build.DEVICE.equalsIgnoreCase("SGP312") || Build.DEVICE.equalsIgnoreCase("SGP321");
    }

    static boolean isSonyXperiaT2Ultra() {
        return Build.DEVICE.equalsIgnoreCase("D5302") || Build.DEVICE.equalsIgnoreCase("D5303") || Build.DEVICE.equalsIgnoreCase("D5306") || Build.DEVICE.equalsIgnoreCase("D5316") || Build.DEVICE.equalsIgnoreCase("D5322");
    }

    static boolean isSonyXperiaZ2() {
        return Build.DEVICE.equalsIgnoreCase("D6502") || Build.DEVICE.equalsIgnoreCase("D6503") || Build.DEVICE.equalsIgnoreCase("D6506") || Build.DEVICE.equalsIgnoreCase("D6508") || Build.DEVICE.equalsIgnoreCase("D6543") || Build.DEVICE.equalsIgnoreCase("SO-03F");
    }

    static boolean isSonyXperiaZ2Tablet() {
        return Build.DEVICE.equalsIgnoreCase("SGP521") || Build.DEVICE.equalsIgnoreCase("SGP541") || Build.DEVICE.equalsIgnoreCase("SGP551") || Build.DEVICE.equalsIgnoreCase("SO-05F");
    }

    static boolean isSonyXperiaM2() {
        return Build.DEVICE.equalsIgnoreCase("D2302") || Build.DEVICE.equalsIgnoreCase("D2303") || Build.DEVICE.equalsIgnoreCase("D2305") || Build.DEVICE.equalsIgnoreCase("D2306");
    }

    static boolean isGoogleGN() {
        return Build.DEVICE.equalsIgnoreCase("maguro") || Build.DEVICE.equalsIgnoreCase("toro");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isGoogleN7() {
        return Build.DEVICE.equalsIgnoreCase("grouper") || Build.DEVICE.equalsIgnoreCase("tilapia");
    }

    static boolean isGoogleN4() {
        return Build.DEVICE.equalsIgnoreCase("mako");
    }

    static boolean isGoogleN5() {
        return Build.DEVICE.equalsIgnoreCase("hammerhead");
    }

    static boolean isGoogleGlass() {
        return Build.DEVICE.equalsIgnoreCase("glass-1");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isSamsGalaxyNoteII() {
        return Build.MODEL.equalsIgnoreCase("GT-N7100") || Build.DEVICE.startsWith("t0lte");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isSamsGalaxySIII() {
        return (Build.MODEL.startsWith("GT-I9300") && Build.DEVICE.equalsIgnoreCase("m0")) || (Build.MODEL.startsWith("GT-I9305") && Build.DEVICE.equalsIgnoreCase("m3")) || ((Build.MODEL.startsWith("SCH-I939") && Build.DEVICE.equalsIgnoreCase("m0ctc")) || Build.DEVICE.equalsIgnoreCase("SC-03E") || (Build.MODEL.startsWith("SHV-E210") && Build.DEVICE.startsWith("c1")));
    }

    static boolean isSamsGalaxyS4Exynos() {
        return Build.MODEL.equalsIgnoreCase("GT-I9500") && Build.DEVICE.equalsIgnoreCase("ja3g");
    }

    static boolean isSamsGalaxyS4Snapdragon() {
        return Build.DEVICE.startsWith("jflte") && (Build.MODEL.equalsIgnoreCase("GT-I9505") || Build.MODEL.equalsIgnoreCase("GT-I9505G") || Build.MODEL.equalsIgnoreCase("SGH-M919") || Build.MODEL.equalsIgnoreCase("SAMSUNG-SGH-I337") || Build.MODEL.equalsIgnoreCase("SPH-L720") || Build.MODEL.equalsIgnoreCase("SGH-I337M") || Build.MODEL.equalsIgnoreCase("SGH-M919V") || Build.MODEL.equalsIgnoreCase("SCH-R970") || Build.MODEL.equalsIgnoreCase("SCH-R970C") || Build.MODEL.equalsIgnoreCase("SCH-R970X") || Build.MODEL.equalsIgnoreCase("SCH-I545"));
    }

    static boolean isSamsGalaxyNoteIIISnapdragon() {
        return Build.MODEL.contains("SM-N900") && Build.DEVICE.startsWith("hlte");
    }

    static boolean isSamsGalaxyS5Snapdragon() {
        return Build.MODEL.contains("SM-G900") && Build.DEVICE.startsWith("klte");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isSamsGalaxyTabLTE() {
        return Build.DEVICE.equalsIgnoreCase("GT-P7320") || Build.DEVICE.equalsIgnoreCase("GT-P7320T");
    }

    static boolean isSamsGalaxyNote_10_1_2014Snapdragon() {
        return Build.MODEL.equalsIgnoreCase("SM-P605") && Build.DEVICE.equalsIgnoreCase("lt03lte");
    }

    static boolean isMSM8x60() {
        String boardPlatform = getBoardPlatform();
        return Build.BOARD.equalsIgnoreCase("msm8660_surf") || boardPlatform.equalsIgnoreCase("msm8960") || boardPlatform.equalsIgnoreCase("msm8660") || boardPlatform.equalsIgnoreCase("msm8260");
    }

    static boolean isSnapdragon400() {
        String boardPlatform = getBoardPlatform();
        return boardPlatform.equalsIgnoreCase("msm8926") || boardPlatform.equalsIgnoreCase("msm8626") || boardPlatform.equalsIgnoreCase("msm8226") || boardPlatform.equalsIgnoreCase("msm8928") || boardPlatform.equalsIgnoreCase("msm8628") || boardPlatform.equalsIgnoreCase("msm8228");
    }

    static boolean isSnapdragon800() {
        String boardPlatform = getBoardPlatform();
        return boardPlatform.equalsIgnoreCase("msm8974") || boardPlatform.equalsIgnoreCase("msm8674") || boardPlatform.equalsIgnoreCase("msm8274");
    }

    static boolean isSnapdragon805() {
        return getBoardPlatform().equalsIgnoreCase("apq8084");
    }

    static boolean isSnapdragon64bit() {
        String boardPlatform = getBoardPlatform();
        return boardPlatform.equalsIgnoreCase("msm8916") || boardPlatform.equalsIgnoreCase("msm8992") || boardPlatform.equalsIgnoreCase("msm8994");
    }

    static boolean isExynos5() {
        return getBoardPlatform().equalsIgnoreCase("exynos5");
    }

    static boolean isTegraK1() {
        String boardPlatform = getBoardPlatform();
        return boardPlatform.equalsIgnoreCase("tegra132") || boardPlatform.equalsIgnoreCase("tegra124");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getBoardPlatform() {
        return getSystemPropertyString("ro.board.platform");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isCameraDisabledByDPM(Context context) {
        return ((DevicePolicyManager) context.getSystemService("device_policy")).getCameraDisabled(null);
    }
}
