package com.google.android.gms.common.internal;

import android.content.Context;
import android.content.res.Resources;
import com.google.android.gms.common.R;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class StringResourceValueReader {
    private final Resources zzvb;
    private final String zzvc;

    public StringResourceValueReader(Context context) {
        Preconditions.checkNotNull(context);
        this.zzvb = context.getResources();
        this.zzvc = this.zzvb.getResourcePackageName(R.string.common_google_play_services_unknown_issue);
    }

    @Nullable
    public String getString(String str) {
        int identifier = this.zzvb.getIdentifier(str, "string", this.zzvc);
        if (identifier == 0) {
            return null;
        }
        return this.zzvb.getString(identifier);
    }
}
