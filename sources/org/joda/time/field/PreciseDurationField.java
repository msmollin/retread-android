package org.joda.time.field;

import org.joda.time.DurationFieldType;

/* loaded from: classes2.dex */
public class PreciseDurationField extends BaseDurationField {
    private static final long serialVersionUID = -8346152187724495365L;
    private final long iUnitMillis;

    @Override // org.joda.time.DurationField
    public final boolean isPrecise() {
        return true;
    }

    public PreciseDurationField(DurationFieldType durationFieldType, long j) {
        super(durationFieldType);
        this.iUnitMillis = j;
    }

    @Override // org.joda.time.DurationField
    public final long getUnitMillis() {
        return this.iUnitMillis;
    }

    @Override // org.joda.time.DurationField
    public long getValueAsLong(long j, long j2) {
        return j / this.iUnitMillis;
    }

    @Override // org.joda.time.DurationField
    public long getMillis(int i, long j) {
        return i * this.iUnitMillis;
    }

    @Override // org.joda.time.DurationField
    public long getMillis(long j, long j2) {
        return FieldUtils.safeMultiply(j, this.iUnitMillis);
    }

    @Override // org.joda.time.DurationField
    public long add(long j, int i) {
        return FieldUtils.safeAdd(j, i * this.iUnitMillis);
    }

    @Override // org.joda.time.DurationField
    public long add(long j, long j2) {
        return FieldUtils.safeAdd(j, FieldUtils.safeMultiply(j2, this.iUnitMillis));
    }

    @Override // org.joda.time.DurationField
    public long getDifferenceAsLong(long j, long j2) {
        return FieldUtils.safeSubtract(j, j2) / this.iUnitMillis;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof PreciseDurationField) {
            PreciseDurationField preciseDurationField = (PreciseDurationField) obj;
            return getType() == preciseDurationField.getType() && this.iUnitMillis == preciseDurationField.iUnitMillis;
        }
        return false;
    }

    public int hashCode() {
        long j = this.iUnitMillis;
        return ((int) (j ^ (j >>> 32))) + getType().hashCode();
    }
}
