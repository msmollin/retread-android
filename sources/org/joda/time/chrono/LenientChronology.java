package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.AssembledChronology;
import org.joda.time.field.LenientDateTimeField;

/* loaded from: classes2.dex */
public final class LenientChronology extends AssembledChronology {
    private static final long serialVersionUID = -3148237568046877177L;
    private transient Chronology iWithUTC;

    public static LenientChronology getInstance(Chronology chronology) {
        if (chronology == null) {
            throw new IllegalArgumentException("Must supply a chronology");
        }
        return new LenientChronology(chronology);
    }

    private LenientChronology(Chronology chronology) {
        super(chronology, null);
    }

    @Override // org.joda.time.chrono.BaseChronology, org.joda.time.Chronology
    public Chronology withUTC() {
        if (this.iWithUTC == null) {
            if (getZone() == DateTimeZone.UTC) {
                this.iWithUTC = this;
            } else {
                this.iWithUTC = getInstance(getBase().withUTC());
            }
        }
        return this.iWithUTC;
    }

    @Override // org.joda.time.chrono.BaseChronology, org.joda.time.Chronology
    public Chronology withZone(DateTimeZone dateTimeZone) {
        if (dateTimeZone == null) {
            dateTimeZone = DateTimeZone.getDefault();
        }
        if (dateTimeZone == DateTimeZone.UTC) {
            return withUTC();
        }
        return dateTimeZone == getZone() ? this : getInstance(getBase().withZone(dateTimeZone));
    }

    @Override // org.joda.time.chrono.AssembledChronology
    protected void assemble(AssembledChronology.Fields fields) {
        fields.year = convertField(fields.year);
        fields.yearOfEra = convertField(fields.yearOfEra);
        fields.yearOfCentury = convertField(fields.yearOfCentury);
        fields.centuryOfEra = convertField(fields.centuryOfEra);
        fields.era = convertField(fields.era);
        fields.dayOfWeek = convertField(fields.dayOfWeek);
        fields.dayOfMonth = convertField(fields.dayOfMonth);
        fields.dayOfYear = convertField(fields.dayOfYear);
        fields.monthOfYear = convertField(fields.monthOfYear);
        fields.weekOfWeekyear = convertField(fields.weekOfWeekyear);
        fields.weekyear = convertField(fields.weekyear);
        fields.weekyearOfCentury = convertField(fields.weekyearOfCentury);
        fields.millisOfSecond = convertField(fields.millisOfSecond);
        fields.millisOfDay = convertField(fields.millisOfDay);
        fields.secondOfMinute = convertField(fields.secondOfMinute);
        fields.secondOfDay = convertField(fields.secondOfDay);
        fields.minuteOfHour = convertField(fields.minuteOfHour);
        fields.minuteOfDay = convertField(fields.minuteOfDay);
        fields.hourOfDay = convertField(fields.hourOfDay);
        fields.hourOfHalfday = convertField(fields.hourOfHalfday);
        fields.clockhourOfDay = convertField(fields.clockhourOfDay);
        fields.clockhourOfHalfday = convertField(fields.clockhourOfHalfday);
        fields.halfdayOfDay = convertField(fields.halfdayOfDay);
    }

    private final DateTimeField convertField(DateTimeField dateTimeField) {
        return LenientDateTimeField.getInstance(dateTimeField, getBase());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof LenientChronology) {
            return getBase().equals(((LenientChronology) obj).getBase());
        }
        return false;
    }

    public int hashCode() {
        return (getBase().hashCode() * 7) + 236548278;
    }

    @Override // org.joda.time.chrono.BaseChronology, org.joda.time.Chronology
    public String toString() {
        return "LenientChronology[" + getBase().toString() + ']';
    }
}
