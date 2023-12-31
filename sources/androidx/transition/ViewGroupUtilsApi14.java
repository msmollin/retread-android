package androidx.transition;

import android.animation.LayoutTransition;
import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
class ViewGroupUtilsApi14 {
    private static final int LAYOUT_TRANSITION_CHANGING = 4;
    private static final String TAG = "ViewGroupUtilsApi14";
    private static Method sCancelMethod;
    private static boolean sCancelMethodFetched;
    private static LayoutTransition sEmptyLayoutTransition;
    private static Field sLayoutSuppressedField;
    private static boolean sLayoutSuppressedFieldFetched;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0089  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0096  */
    /* JADX WARN: Removed duplicated region for block: B:44:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void suppressLayout(@androidx.annotation.NonNull android.view.ViewGroup r5, boolean r6) {
        /*
            android.animation.LayoutTransition r0 = androidx.transition.ViewGroupUtilsApi14.sEmptyLayoutTransition
            r1 = 1
            r2 = 0
            r3 = 0
            if (r0 != 0) goto L2a
            androidx.transition.ViewGroupUtilsApi14$1 r0 = new androidx.transition.ViewGroupUtilsApi14$1
            r0.<init>()
            androidx.transition.ViewGroupUtilsApi14.sEmptyLayoutTransition = r0
            android.animation.LayoutTransition r0 = androidx.transition.ViewGroupUtilsApi14.sEmptyLayoutTransition
            r4 = 2
            r0.setAnimator(r4, r3)
            android.animation.LayoutTransition r0 = androidx.transition.ViewGroupUtilsApi14.sEmptyLayoutTransition
            r0.setAnimator(r2, r3)
            android.animation.LayoutTransition r0 = androidx.transition.ViewGroupUtilsApi14.sEmptyLayoutTransition
            r0.setAnimator(r1, r3)
            android.animation.LayoutTransition r0 = androidx.transition.ViewGroupUtilsApi14.sEmptyLayoutTransition
            r4 = 3
            r0.setAnimator(r4, r3)
            android.animation.LayoutTransition r0 = androidx.transition.ViewGroupUtilsApi14.sEmptyLayoutTransition
            r4 = 4
            r0.setAnimator(r4, r3)
        L2a:
            if (r6 == 0) goto L4a
            android.animation.LayoutTransition r6 = r5.getLayoutTransition()
            if (r6 == 0) goto L44
            boolean r0 = r6.isRunning()
            if (r0 == 0) goto L3b
            cancelLayoutTransition(r6)
        L3b:
            android.animation.LayoutTransition r0 = androidx.transition.ViewGroupUtilsApi14.sEmptyLayoutTransition
            if (r6 == r0) goto L44
            int r0 = androidx.transition.R.id.transition_layout_save
            r5.setTag(r0, r6)
        L44:
            android.animation.LayoutTransition r6 = androidx.transition.ViewGroupUtilsApi14.sEmptyLayoutTransition
            r5.setLayoutTransition(r6)
            goto L9e
        L4a:
            r5.setLayoutTransition(r3)
            boolean r6 = androidx.transition.ViewGroupUtilsApi14.sLayoutSuppressedFieldFetched
            if (r6 != 0) goto L6a
            java.lang.Class<android.view.ViewGroup> r6 = android.view.ViewGroup.class
            java.lang.String r0 = "mLayoutSuppressed"
            java.lang.reflect.Field r6 = r6.getDeclaredField(r0)     // Catch: java.lang.NoSuchFieldException -> L61
            androidx.transition.ViewGroupUtilsApi14.sLayoutSuppressedField = r6     // Catch: java.lang.NoSuchFieldException -> L61
            java.lang.reflect.Field r6 = androidx.transition.ViewGroupUtilsApi14.sLayoutSuppressedField     // Catch: java.lang.NoSuchFieldException -> L61
            r6.setAccessible(r1)     // Catch: java.lang.NoSuchFieldException -> L61
            goto L68
        L61:
            java.lang.String r6 = "ViewGroupUtilsApi14"
            java.lang.String r0 = "Failed to access mLayoutSuppressed field by reflection"
            android.util.Log.i(r6, r0)
        L68:
            androidx.transition.ViewGroupUtilsApi14.sLayoutSuppressedFieldFetched = r1
        L6a:
            java.lang.reflect.Field r6 = androidx.transition.ViewGroupUtilsApi14.sLayoutSuppressedField
            if (r6 == 0) goto L87
            java.lang.reflect.Field r6 = androidx.transition.ViewGroupUtilsApi14.sLayoutSuppressedField     // Catch: java.lang.IllegalAccessException -> L80
            boolean r6 = r6.getBoolean(r5)     // Catch: java.lang.IllegalAccessException -> L80
            if (r6 == 0) goto L7e
            java.lang.reflect.Field r0 = androidx.transition.ViewGroupUtilsApi14.sLayoutSuppressedField     // Catch: java.lang.IllegalAccessException -> L7c
            r0.setBoolean(r5, r2)     // Catch: java.lang.IllegalAccessException -> L7c
            goto L7e
        L7c:
            r2 = r6
            goto L80
        L7e:
            r2 = r6
            goto L87
        L80:
            java.lang.String r6 = "ViewGroupUtilsApi14"
            java.lang.String r0 = "Failed to get mLayoutSuppressed field by reflection"
            android.util.Log.i(r6, r0)
        L87:
            if (r2 == 0) goto L8c
            r5.requestLayout()
        L8c:
            int r6 = androidx.transition.R.id.transition_layout_save
            java.lang.Object r6 = r5.getTag(r6)
            android.animation.LayoutTransition r6 = (android.animation.LayoutTransition) r6
            if (r6 == 0) goto L9e
            int r0 = androidx.transition.R.id.transition_layout_save
            r5.setTag(r0, r3)
            r5.setLayoutTransition(r6)
        L9e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.transition.ViewGroupUtilsApi14.suppressLayout(android.view.ViewGroup, boolean):void");
    }

    private static void cancelLayoutTransition(LayoutTransition layoutTransition) {
        if (!sCancelMethodFetched) {
            try {
                sCancelMethod = LayoutTransition.class.getDeclaredMethod("cancel", new Class[0]);
                sCancelMethod.setAccessible(true);
            } catch (NoSuchMethodException unused) {
                Log.i(TAG, "Failed to access cancel method by reflection");
            }
            sCancelMethodFetched = true;
        }
        if (sCancelMethod != null) {
            try {
                sCancelMethod.invoke(layoutTransition, new Object[0]);
            } catch (IllegalAccessException unused2) {
                Log.i(TAG, "Failed to access cancel method by reflection");
            } catch (InvocationTargetException unused3) {
                Log.i(TAG, "Failed to invoke cancel method by reflection");
            }
        }
    }

    private ViewGroupUtilsApi14() {
    }
}
