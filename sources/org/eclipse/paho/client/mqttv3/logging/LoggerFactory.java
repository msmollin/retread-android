package org.eclipse.paho.client.mqttv3.logging;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/* loaded from: classes2.dex */
public class LoggerFactory {
    private static final String CLASS_NAME;
    public static final String MQTT_CLIENT_MSG_CAT = "org.eclipse.paho.client.mqttv3.internal.nls.logcat";
    static /* synthetic */ Class class$0;
    static /* synthetic */ Class class$1;
    static /* synthetic */ Class class$2;
    private static String jsr47LoggerClassName;
    private static String overrideloggerClassName;

    static {
        Class<?> cls = class$0;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.logging.LoggerFactory");
                class$0 = cls;
            } catch (ClassNotFoundException e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        CLASS_NAME = cls.getName();
        overrideloggerClassName = null;
        Class<?> cls2 = class$1;
        if (cls2 == null) {
            try {
                cls2 = Class.forName("org.eclipse.paho.client.mqttv3.logging.JSR47Logger");
                class$1 = cls2;
            } catch (ClassNotFoundException e2) {
                throw new NoClassDefFoundError(e2.getMessage());
            }
        }
        jsr47LoggerClassName = cls2.getName();
    }

    public static Logger getLogger(String str, String str2) {
        String str3 = overrideloggerClassName;
        if (str3 == null) {
            str3 = jsr47LoggerClassName;
        }
        Logger logger = getLogger(str3, ResourceBundle.getBundle(str), str2, null);
        if (logger != null) {
            return logger;
        }
        throw new MissingResourceException("Error locating the logging class", CLASS_NAME, str2);
    }

    private static Logger getLogger(String str, ResourceBundle resourceBundle, String str2, String str3) {
        try {
            Class<?> cls = Class.forName(str);
            if (cls != null) {
                try {
                    Logger logger = (Logger) cls.newInstance();
                    logger.initialise(resourceBundle, str2, str3);
                    return logger;
                } catch (ExceptionInInitializerError unused) {
                    return null;
                } catch (IllegalAccessException unused2) {
                    return null;
                } catch (InstantiationException unused3) {
                    return null;
                } catch (SecurityException unused4) {
                    return null;
                }
            }
            return null;
        } catch (ClassNotFoundException unused5) {
            return null;
        } catch (NoClassDefFoundError unused6) {
            return null;
        }
    }

    public static String getLoggingProperty(String str) {
        try {
            Class<?> cls = Class.forName("java.util.logging.LogManager");
            Object invoke = cls.getMethod("getLogManager", new Class[0]).invoke(null, null);
            Class<?>[] clsArr = new Class[1];
            Class<?> cls2 = class$2;
            if (cls2 == null) {
                try {
                    cls2 = Class.forName("java.lang.String");
                    class$2 = cls2;
                } catch (ClassNotFoundException e) {
                    throw new NoClassDefFoundError(e.getMessage());
                }
            }
            clsArr[0] = cls2;
            return (String) cls.getMethod("getProperty", clsArr).invoke(invoke, str);
        } catch (Exception unused) {
            return null;
        }
    }

    public static void setLogger(String str) {
        overrideloggerClassName = str;
    }
}
