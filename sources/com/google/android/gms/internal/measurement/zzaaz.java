package com.google.android.gms.internal.measurement;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: Add missing generic type declarations: [FieldDescriptorType] */
/* loaded from: classes.dex */
public final class zzaaz<FieldDescriptorType> extends zzaay<FieldDescriptorType, Object> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzaaz(int i) {
        super(i, null);
    }

    @Override // com.google.android.gms.internal.measurement.zzaay
    public final void zzrg() {
        if (!isImmutable()) {
            for (int i = 0; i < zzuj(); i++) {
                Map.Entry<FieldDescriptorType, Object> zzah = zzah(i);
                if (((zzzo) zzah.getKey()).zztk()) {
                    zzah.setValue(Collections.unmodifiableList((List) zzah.getValue()));
                }
            }
            for (Map.Entry<FieldDescriptorType, Object> entry : zzuk()) {
                if (((zzzo) entry.getKey()).zztk()) {
                    entry.setValue(Collections.unmodifiableList((List) entry.getValue()));
                }
            }
        }
        super.zzrg();
    }
}
