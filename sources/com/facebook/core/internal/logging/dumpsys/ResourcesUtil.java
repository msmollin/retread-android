package com.facebook.core.internal.logging.dumpsys;

import android.content.res.Resources;
import androidx.annotation.Nullable;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* loaded from: classes.dex */
class ResourcesUtil {
    private static int getResourcePackageId(int i) {
        return (i >>> 24) & 255;
    }

    private ResourcesUtil() {
    }

    public static String getIdStringQuietly(Object obj, @Nullable Resources resources, int i) {
        try {
            return getIdString(resources, i);
        } catch (Resources.NotFoundException unused) {
            return getFallbackIdString(i);
        }
    }

    public static String getIdString(@Nullable Resources resources, int i) throws Resources.NotFoundException {
        String str;
        String str2;
        if (resources == null) {
            return getFallbackIdString(i);
        }
        if (getResourcePackageId(i) != 127) {
            str = resources.getResourcePackageName(i);
            str2 = ":";
        } else {
            str = "";
            str2 = "";
        }
        String resourceTypeName = resources.getResourceTypeName(i);
        String resourceEntryName = resources.getResourceEntryName(i);
        StringBuilder sb = new StringBuilder(str.length() + 1 + str2.length() + resourceTypeName.length() + 1 + resourceEntryName.length());
        sb.append("@");
        sb.append(str);
        sb.append(str2);
        sb.append(resourceTypeName);
        sb.append(MqttTopic.TOPIC_LEVEL_SEPARATOR);
        sb.append(resourceEntryName);
        return sb.toString();
    }

    private static String getFallbackIdString(int i) {
        return MqttTopic.MULTI_LEVEL_WILDCARD + Integer.toHexString(i);
    }
}
