package com.google.firebase.iid;

import android.os.Bundle;

/* loaded from: classes.dex */
final class zzaa extends zzab<Void> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzaa(int i, int i2, Bundle bundle) {
        super(i, 2, bundle);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.firebase.iid.zzab
    public final void zzb(Bundle bundle) {
        if (bundle.getBoolean("ack", false)) {
            finish(null);
        } else {
            zza(new zzac(4, "Invalid response to one way request"));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.firebase.iid.zzab
    public final boolean zzw() {
        return true;
    }
}
