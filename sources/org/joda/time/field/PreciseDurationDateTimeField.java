package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;

/* loaded from: classes2.dex */
public abstract class PreciseDurationDateTimeField extends BaseDateTimeField {
    private static final long serialVersionUID = 5004523158306266035L;
    private final DurationField iUnitField;
    final long iUnitMillis;

    @Override // org.joda.time.field.BaseDateTimeField, org.joda.time.DateTimeField
    public int getMinimumValue() {
        return 0;
    }

    @Override // org.joda.time.DateTimeField
    public boolean isLenient() {
        return false;
    }

    public PreciseDurationDateTimeField(DateTimeFieldType dateTimeFieldType, DurationField durationField) {
        super(dateTimeFieldType);
        if (!durationField.isPrecise()) {
            throw new IllegalArgumentException("Unit duration field must be precise");
        }
        this.iUnitMillis = durationField.getUnitMillis();
        if (this.iUnitMillis < 1) {
            throw new IllegalArgumentException("The unit milliseconds must be at least 1");
        }
        this.iUnitField = durationField;
    }

    @Override // org.joda.time.field.BaseDateTimeField, org.joda.time.DateTimeField
    public long set(long j, int i) {
        FieldUtils.verifyValueBounds(this, i, getMinimumValue(), getMaximumValueForSet(j, i));
        return j + ((i - get(j)) * this.iUnitMillis);
    }

    @Override // org.joda.time.field.BaseDateTimeField, org.joda.time.DateTimeField
    public long roundFloor(long j) {
        if (j >= 0) {
            return j - (j % this.iUnitMillis);
        }
        long j2 = j + 1;
        return (j2 - (j2 % this.iUnitMillis)) - this.iUnitMillis;
    }

    @Override // org.joda.time.field.BaseDateTimeField, org.joda.time.DateTimeField
    public long roundCeiling(long j) {
        if (j > 0) {
            long j2 = j - 1;
            return (j2 - (j2 % this.iUnitMillis)) + this.iUnitMillis;
        }
        return j - (j % this.iUnitMillis);
    }

    @Override // org.joda.time.field.BaseDateTimeField, org.joda.time.DateTimeField
    public long remainder(long j) {
        if (j >= 0) {
            return j % this.iUnitMillis;
        }
        return (((j + 1) % this.iUnitMillis) + this.iUnitMillis) - 1;
    }

    @Override // org.joda.time.field.BaseDateTimeField, org.joda.time.DateTimeField
    public DurationField getDurationField() {
        return this.iUnitField;
    }

    public final long getUnitMillis() {
        return this.iUnitMillis;
    }

    protected int getMaximumValueForSet(long j, int i) {
        return getMaximumValue(j);
    }
}
