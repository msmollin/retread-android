package com.google.firebase.components;

import android.content.Context;
import android.util.Log;
import androidx.annotation.VisibleForTesting;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class zzc {
    private final Context mContext;
    private final zzf zzag;

    public zzc(Context context) {
        this(context, new zze());
    }

    @VisibleForTesting
    private zzc(Context context, zzf zzfVar) {
        this.mContext = context;
        this.zzag = zzfVar;
    }

    private static List<ComponentRegistrar> zza(List<String> list) {
        String str;
        String str2;
        Object[] objArr;
        ArrayList arrayList = new ArrayList();
        for (String str3 : list) {
            try {
                Class<?> cls = Class.forName(str3);
                if (ComponentRegistrar.class.isAssignableFrom(cls)) {
                    arrayList.add((ComponentRegistrar) cls.newInstance());
                } else {
                    Log.w("ComponentDiscovery", String.format("Class %s is not an instance of %s", str3, "com.google.firebase.components.ComponentRegistrar"));
                }
            } catch (ClassNotFoundException e) {
                e = e;
                str = "ComponentDiscovery";
                str2 = "Class %s is not an found.";
                objArr = new Object[]{str3};
                Log.w(str, String.format(str2, objArr), e);
            } catch (IllegalAccessException e2) {
                e = e2;
                str = "ComponentDiscovery";
                str2 = "Could not instantiate %s.";
                objArr = new Object[]{str3};
                Log.w(str, String.format(str2, objArr), e);
            } catch (InstantiationException e3) {
                e = e3;
                str = "ComponentDiscovery";
                str2 = "Could not instantiate %s.";
                objArr = new Object[]{str3};
                Log.w(str, String.format(str2, objArr), e);
            }
        }
        return arrayList;
    }

    public final List<ComponentRegistrar> zzj() {
        return zza(this.zzag.zzc(this.mContext));
    }
}
