package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.ReadWritableInterval;
import org.joda.time.ReadWritablePeriod;
import org.joda.time.ReadablePartial;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class StringConverter extends AbstractConverter implements InstantConverter, PartialConverter, DurationConverter, PeriodConverter, IntervalConverter {
    static final StringConverter INSTANCE = new StringConverter();

    protected StringConverter() {
    }

    @Override // org.joda.time.convert.AbstractConverter, org.joda.time.convert.InstantConverter
    public long getInstantMillis(Object obj, Chronology chronology) {
        return ISODateTimeFormat.dateTimeParser().withChronology(chronology).parseMillis((String) obj);
    }

    @Override // org.joda.time.convert.AbstractConverter, org.joda.time.convert.PartialConverter
    public int[] getPartialValues(ReadablePartial readablePartial, Object obj, Chronology chronology, DateTimeFormatter dateTimeFormatter) {
        if (dateTimeFormatter.getZone() != null) {
            chronology = chronology.withZone(dateTimeFormatter.getZone());
        }
        return chronology.get(readablePartial, dateTimeFormatter.withChronology(chronology).parseMillis((String) obj));
    }

    @Override // org.joda.time.convert.DurationConverter
    public long getDurationMillis(Object obj) {
        long parseLong;
        String str = (String) obj;
        int length = str.length();
        if (length >= 4 && ((str.charAt(0) == 'P' || str.charAt(0) == 'p') && (str.charAt(1) == 'T' || str.charAt(1) == 't'))) {
            int i = length - 1;
            if (str.charAt(i) == 'S' || str.charAt(i) == 's') {
                String substring = str.substring(2, i);
                int i2 = 0;
                int i3 = -1;
                for (int i4 = 0; i4 < substring.length(); i4++) {
                    if (substring.charAt(i4) < '0' || substring.charAt(i4) > '9') {
                        if (i4 == 0 && substring.charAt(0) == '-') {
                            i2 = 1;
                        } else if (i4 <= i2 || substring.charAt(i4) != '.' || i3 != -1) {
                            throw new IllegalArgumentException("Invalid format: \"" + str + '\"');
                        } else {
                            i3 = i4;
                        }
                    }
                }
                long j = 0;
                if (i3 > 0) {
                    long parseLong2 = Long.parseLong(substring.substring(i2, i3));
                    String substring2 = substring.substring(i3 + 1);
                    if (substring2.length() != 3) {
                        substring2 = (substring2 + "000").substring(0, 3);
                    }
                    long parseInt = Integer.parseInt(substring2);
                    parseLong = parseLong2;
                    j = parseInt;
                } else if (i2 != 0) {
                    parseLong = Long.parseLong(substring.substring(i2, substring.length()));
                } else {
                    parseLong = Long.parseLong(substring);
                }
                if (i2 != 0) {
                    return FieldUtils.safeAdd(FieldUtils.safeMultiply(-parseLong, 1000), -j);
                }
                return FieldUtils.safeAdd(FieldUtils.safeMultiply(parseLong, 1000), j);
            }
        }
        throw new IllegalArgumentException("Invalid format: \"" + str + '\"');
    }

    @Override // org.joda.time.convert.PeriodConverter
    public void setInto(ReadWritablePeriod readWritablePeriod, Object obj, Chronology chronology) {
        String str = (String) obj;
        PeriodFormatter standard = ISOPeriodFormat.standard();
        readWritablePeriod.clear();
        int parseInto = standard.parseInto(readWritablePeriod, str, 0);
        if (parseInto < str.length()) {
            if (parseInto < 0) {
                standard.withParseType(readWritablePeriod.getPeriodType()).parseMutablePeriod(str);
            }
            throw new IllegalArgumentException("Invalid format: \"" + str + '\"');
        }
    }

    @Override // org.joda.time.convert.IntervalConverter
    public void setInto(ReadWritableInterval readWritableInterval, Object obj, Chronology chronology) {
        Chronology chronology2;
        long add;
        Chronology chronology3;
        Chronology chronology4 = chronology;
        String str = (String) obj;
        int indexOf = str.indexOf(47);
        if (indexOf < 0) {
            throw new IllegalArgumentException("Format requires a '/' separator: " + str);
        }
        String substring = str.substring(0, indexOf);
        if (substring.length() <= 0) {
            throw new IllegalArgumentException("Format invalid: " + str);
        }
        String substring2 = str.substring(indexOf + 1);
        if (substring2.length() <= 0) {
            throw new IllegalArgumentException("Format invalid: " + str);
        }
        DateTimeFormatter withChronology = ISODateTimeFormat.dateTimeParser().withChronology(chronology4);
        PeriodFormatter standard = ISOPeriodFormat.standard();
        long j = 0;
        char charAt = substring.charAt(0);
        Period period = null;
        if (charAt == 'P' || charAt == 'p') {
            period = standard.withParseType(getPeriodType(substring)).parsePeriod(substring);
            chronology2 = null;
        } else {
            DateTime parseDateTime = withChronology.parseDateTime(substring);
            j = parseDateTime.getMillis();
            chronology2 = parseDateTime.getChronology();
        }
        char charAt2 = substring2.charAt(0);
        if (charAt2 != 'P' && charAt2 != 'p') {
            DateTime parseDateTime2 = withChronology.parseDateTime(substring2);
            add = parseDateTime2.getMillis();
            chronology3 = chronology2 != null ? chronology2 : parseDateTime2.getChronology();
            if (chronology4 != null) {
                chronology3 = chronology4;
            }
            if (period != null) {
                j = chronology3.add(period, add, -1);
            }
        } else if (period != null) {
            throw new IllegalArgumentException("Interval composed of two durations: " + str);
        } else {
            Period parsePeriod = standard.withParseType(getPeriodType(substring2)).parsePeriod(substring2);
            if (chronology4 == null) {
                chronology4 = chronology2;
            }
            add = chronology4.add(parsePeriod, j, 1);
            chronology3 = chronology4;
        }
        readWritableInterval.setInterval(j, add);
        readWritableInterval.setChronology(chronology3);
    }

    @Override // org.joda.time.convert.Converter
    public Class<?> getSupportedType() {
        return String.class;
    }
}
