package org.joda.time.chrono;

import org.joda.time.Chronology;

/* loaded from: classes2.dex */
abstract class BasicGJChronology extends BasicChronology {
    private static final long FEB_29 = 5097600000L;
    private static final long serialVersionUID = 538276888268L;
    private static final int[] MIN_DAYS_PER_MONTH_ARRAY = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static final int[] MAX_DAYS_PER_MONTH_ARRAY = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static final long[] MIN_TOTAL_MILLIS_BY_MONTH_ARRAY = new long[12];
    private static final long[] MAX_TOTAL_MILLIS_BY_MONTH_ARRAY = new long[12];

    static {
        long j = 0;
        int i = 0;
        long j2 = 0;
        while (i < 11) {
            j += MIN_DAYS_PER_MONTH_ARRAY[i] * 86400000;
            int i2 = i + 1;
            MIN_TOTAL_MILLIS_BY_MONTH_ARRAY[i2] = j;
            j2 += MAX_DAYS_PER_MONTH_ARRAY[i] * 86400000;
            MAX_TOTAL_MILLIS_BY_MONTH_ARRAY[i2] = j2;
            i = i2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BasicGJChronology(Chronology chronology, Object obj, int i) {
        super(chronology, obj, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.joda.time.chrono.BasicChronology
    public boolean isLeapDay(long j) {
        return dayOfMonth().get(j) == 29 && monthOfYear().isLeap(j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0033, code lost:
        if (r12 < 5062500) goto L13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0046, code lost:
        if (r12 < 12825000) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x005e, code lost:
        if (r12 < 20587500) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0082, code lost:
        if (r12 < 4978125) goto L13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x008e, code lost:
        if (r12 < 12740625) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x009f, code lost:
        if (r12 < 20503125) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:?, code lost:
        return 2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:?, code lost:
        return 3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:?, code lost:
        return 5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:?, code lost:
        return 6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:?, code lost:
        return 8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:?, code lost:
        return 9;
     */
    @Override // org.joda.time.chrono.BasicChronology
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int getMonthOfYear(long r12, int r14) {
        /*
            r11 = this;
            long r0 = r11.getYearMillis(r14)
            long r12 = r12 - r0
            r0 = 10
            long r12 = r12 >> r0
            int r12 = (int) r12
            boolean r11 = r11.isLeapYear(r14)
            r13 = 12
            r14 = 11
            r1 = 9
            r2 = 8
            r3 = 6
            r4 = 5
            r5 = 3
            r6 = 2
            r7 = 7
            r8 = 4
            r9 = 1
            r10 = 2615625(0x27e949, float:3.665271E-39)
            if (r11 == 0) goto L72
            r11 = 15356250(0xea515a, float:2.151869E-38)
            if (r12 >= r11) goto L4e
            r11 = 7678125(0x7528ad, float:1.0759345E-38)
            if (r12 >= r11) goto L3b
            if (r12 >= r10) goto L30
        L2d:
            r13 = r9
            goto Lae
        L30:
            r11 = 5062500(0x4d3f64, float:7.094073E-39)
            if (r12 >= r11) goto L38
        L35:
            r13 = r6
            goto Lae
        L38:
            r13 = r5
            goto Lae
        L3b:
            r11 = 10209375(0x9bc85f, float:1.4306382E-38)
            if (r12 >= r11) goto L43
        L40:
            r13 = r8
            goto Lae
        L43:
            r11 = 12825000(0xc3b1a8, float:1.7971653E-38)
            if (r12 >= r11) goto L4b
        L48:
            r13 = r4
            goto Lae
        L4b:
            r13 = r3
            goto Lae
        L4e:
            r11 = 23118750(0x160c39e, float:4.128265E-38)
            if (r12 >= r11) goto L64
            r11 = 17971875(0x1123aa3, float:2.6858035E-38)
            if (r12 >= r11) goto L5b
        L58:
            r13 = r7
            goto Lae
        L5b:
            r11 = 20587500(0x13a23ec, float:3.4188577E-38)
            if (r12 >= r11) goto L62
        L60:
            r13 = r2
            goto Lae
        L62:
            r13 = r1
            goto Lae
        L64:
            r11 = 25734375(0x188ace7, float:5.020661E-38)
            if (r12 >= r11) goto L6b
        L69:
            r13 = r0
            goto Lae
        L6b:
            r11 = 28265625(0x1af4c99, float:6.439476E-38)
            if (r12 >= r11) goto Lae
        L70:
            r13 = r14
            goto Lae
        L72:
            r11 = 15271875(0xe907c3, float:2.1400455E-38)
            if (r12 >= r11) goto L91
            r11 = 7593750(0x73df16, float:1.064111E-38)
            if (r12 >= r11) goto L85
            if (r12 >= r10) goto L7f
            goto L2d
        L7f:
            r11 = 4978125(0x4bf5cd, float:6.975839E-39)
            if (r12 >= r11) goto L38
            goto L35
        L85:
            r11 = 10125000(0x9a7ec8, float:1.4188147E-38)
            if (r12 >= r11) goto L8b
            goto L40
        L8b:
            r11 = 12740625(0xc26811, float:1.7853418E-38)
            if (r12 >= r11) goto L4b
            goto L48
        L91:
            r11 = 23034375(0x15f7a07, float:4.1046182E-38)
            if (r12 >= r11) goto La2
            r11 = 17887500(0x110f10c, float:2.6621566E-38)
            if (r12 >= r11) goto L9c
            goto L58
        L9c:
            r11 = 20503125(0x138da55, float:3.3952108E-38)
            if (r12 >= r11) goto L62
            goto L60
        La2:
            r11 = 25650000(0x1876350, float:4.9733674E-38)
            if (r12 >= r11) goto La8
            goto L69
        La8:
            r11 = 28181250(0x1ae0302, float:6.392182E-38)
            if (r12 >= r11) goto Lae
            goto L70
        Lae:
            return r13
        */
        throw new UnsupportedOperationException("Method not decompiled: org.joda.time.chrono.BasicGJChronology.getMonthOfYear(long, int):int");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.joda.time.chrono.BasicChronology
    public int getDaysInYearMonth(int i, int i2) {
        if (isLeapYear(i)) {
            return MAX_DAYS_PER_MONTH_ARRAY[i2 - 1];
        }
        return MIN_DAYS_PER_MONTH_ARRAY[i2 - 1];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.joda.time.chrono.BasicChronology
    public int getDaysInMonthMax(int i) {
        return MAX_DAYS_PER_MONTH_ARRAY[i - 1];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.joda.time.chrono.BasicChronology
    public int getDaysInMonthMaxForSet(long j, int i) {
        if (i > 28 || i < 1) {
            return getDaysInMonthMax(j);
        }
        return 28;
    }

    @Override // org.joda.time.chrono.BasicChronology
    long getTotalMillisByYearMonth(int i, int i2) {
        if (isLeapYear(i)) {
            return MAX_TOTAL_MILLIS_BY_MONTH_ARRAY[i2 - 1];
        }
        return MIN_TOTAL_MILLIS_BY_MONTH_ARRAY[i2 - 1];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.joda.time.chrono.BasicChronology
    public long getYearDifference(long j, long j2) {
        int year = getYear(j);
        int year2 = getYear(j2);
        long yearMillis = j - getYearMillis(year);
        long yearMillis2 = j2 - getYearMillis(year2);
        if (yearMillis2 >= FEB_29) {
            if (isLeapYear(year2)) {
                if (!isLeapYear(year)) {
                    yearMillis2 -= 86400000;
                }
            } else if (yearMillis >= FEB_29 && isLeapYear(year)) {
                yearMillis -= 86400000;
            }
        }
        int i = year - year2;
        if (yearMillis < yearMillis2) {
            i--;
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.joda.time.chrono.BasicChronology
    public long setYear(long j, int i) {
        int year = getYear(j);
        int dayOfYear = getDayOfYear(j, year);
        int millisOfDay = getMillisOfDay(j);
        if (dayOfYear > 59) {
            if (isLeapYear(year)) {
                if (!isLeapYear(i)) {
                    dayOfYear--;
                }
            } else if (isLeapYear(i)) {
                dayOfYear++;
            }
        }
        return getYearMonthDayMillis(i, 1, dayOfYear) + millisOfDay;
    }
}
