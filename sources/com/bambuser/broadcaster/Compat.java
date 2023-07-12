package com.bambuser.broadcaster;

/* loaded from: classes.dex */
final class Compat {
    private Compat() {
    }

    static Object tryCall(String str, String str2) {
        try {
            return tryCall(Class.forName(str), str2);
        } catch (Exception unused) {
            return null;
        }
    }

    static Object tryCall(Class<?> cls, String str) {
        try {
            return cls.getMethod(str, new Class[0]).invoke(null, new Object[0]);
        } catch (Exception unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object tryCall(Object obj, String str) {
        try {
            return obj.getClass().getMethod(str, new Class[0]).invoke(obj, new Object[0]);
        } catch (Exception unused) {
            return null;
        }
    }

    static void trySet(Object obj, String str, Object obj2) {
        try {
            obj.getClass().getField(str).set(obj, obj2);
        } catch (Exception unused) {
        }
    }
}
