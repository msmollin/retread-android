package org.joda.time.field;

import java.util.Locale;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.ReadablePartial;

/* loaded from: classes2.dex */
public abstract class BaseDateTimeField extends DateTimeField {
    private final DateTimeFieldType iType;

    @Override // org.joda.time.DateTimeField
    public abstract int get(long j);

    @Override // org.joda.time.DateTimeField
    public abstract DurationField getDurationField();

    @Override // org.joda.time.DateTimeField
    public int getLeapAmount(long j) {
        return 0;
    }

    @Override // org.joda.time.DateTimeField
    public DurationField getLeapDurationField() {
        return null;
    }

    @Override // org.joda.time.DateTimeField
    public abstract int getMaximumValue();

    @Override // org.joda.time.DateTimeField
    public abstract int getMinimumValue();

    @Override // org.joda.time.DateTimeField
    public abstract DurationField getRangeDurationField();

    @Override // org.joda.time.DateTimeField
    public boolean isLeap(long j) {
        return false;
    }

    @Override // org.joda.time.DateTimeField
    public final boolean isSupported() {
        return true;
    }

    @Override // org.joda.time.DateTimeField
    public abstract long roundFloor(long j);

    @Override // org.joda.time.DateTimeField
    public abstract long set(long j, int i);

    /* JADX INFO: Access modifiers changed from: protected */
    public BaseDateTimeField(DateTimeFieldType dateTimeFieldType) {
        if (dateTimeFieldType == null) {
            throw new IllegalArgumentException("The type must not be null");
        }
        this.iType = dateTimeFieldType;
    }

    @Override // org.joda.time.DateTimeField
    public final DateTimeFieldType getType() {
        return this.iType;
    }

    @Override // org.joda.time.DateTimeField
    public final String getName() {
        return this.iType.getName();
    }

    @Override // org.joda.time.DateTimeField
    public String getAsText(long j, Locale locale) {
        return getAsText(get(j), locale);
    }

    @Override // org.joda.time.DateTimeField
    public final String getAsText(long j) {
        return getAsText(j, (Locale) null);
    }

    @Override // org.joda.time.DateTimeField
    public String getAsText(ReadablePartial readablePartial, int i, Locale locale) {
        return getAsText(i, locale);
    }

    @Override // org.joda.time.DateTimeField
    public final String getAsText(ReadablePartial readablePartial, Locale locale) {
        return getAsText(readablePartial, readablePartial.get(getType()), locale);
    }

    @Override // org.joda.time.DateTimeField
    public String getAsText(int i, Locale locale) {
        return Integer.toString(i);
    }

    @Override // org.joda.time.DateTimeField
    public String getAsShortText(long j, Locale locale) {
        return getAsShortText(get(j), locale);
    }

    @Override // org.joda.time.DateTimeField
    public final String getAsShortText(long j) {
        return getAsShortText(j, (Locale) null);
    }

    @Override // org.joda.time.DateTimeField
    public String getAsShortText(ReadablePartial readablePartial, int i, Locale locale) {
        return getAsShortText(i, locale);
    }

    @Override // org.joda.time.DateTimeField
    public final String getAsShortText(ReadablePartial readablePartial, Locale locale) {
        return getAsShortText(readablePartial, readablePartial.get(getType()), locale);
    }

    @Override // org.joda.time.DateTimeField
    public String getAsShortText(int i, Locale locale) {
        return getAsText(i, locale);
    }

    @Override // org.joda.time.DateTimeField
    public long add(long j, int i) {
        return getDurationField().add(j, i);
    }

    @Override // org.joda.time.DateTimeField
    public long add(long j, long j2) {
        return getDurationField().add(j, j2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x0058, code lost:
        if (r10 >= 0) goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x005a, code lost:
        r1 = getMinimumValue(r7, r9);
        r2 = r9[r8] + r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0065, code lost:
        if (r2 < r1) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0067, code lost:
        r9[r8] = (int) r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x006b, code lost:
        if (r0 != null) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x006d, code lost:
        if (r8 == 0) goto L36;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x006f, code lost:
        r0 = r7.getField(r8 - 1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0085, code lost:
        if (getRangeDurationField().getType() != r0.getDurationField().getType()) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x008f, code lost:
        throw new java.lang.IllegalArgumentException("Fields invalid for add");
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0097, code lost:
        throw new java.lang.IllegalArgumentException("Maximum value exceeded for add");
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0098, code lost:
        r10 = r10 - ((r1 - 1) - r9[r8]);
        r9 = r0.add(r7, r8 - 1, r9, -1);
        r9[r8] = getMaximumValue(r7, r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00b2, code lost:
        return set(r7, r8, r9, r9[r8]);
     */
    @Override // org.joda.time.DateTimeField
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int[] add(org.joda.time.ReadablePartial r7, int r8, int[] r9, int r10) {
        /*
            r6 = this;
            if (r10 != 0) goto L3
            return r9
        L3:
            r0 = 0
        L4:
            if (r10 <= 0) goto L58
            int r1 = r6.getMaximumValue(r7, r9)
            r2 = r9[r8]
            int r2 = r2 + r10
            long r2 = (long) r2
            long r4 = (long) r1
            int r4 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r4 > 0) goto L17
            int r1 = (int) r2
            r9[r8] = r1
            goto L58
        L17:
            if (r0 != 0) goto L44
            if (r8 == 0) goto L3c
            int r0 = r8 + (-1)
            org.joda.time.DateTimeField r0 = r7.getField(r0)
            org.joda.time.DurationField r2 = r6.getRangeDurationField()
            org.joda.time.DurationFieldType r2 = r2.getType()
            org.joda.time.DurationField r3 = r0.getDurationField()
            org.joda.time.DurationFieldType r3 = r3.getType()
            if (r2 != r3) goto L34
            goto L44
        L34:
            java.lang.IllegalArgumentException r6 = new java.lang.IllegalArgumentException
            java.lang.String r7 = "Fields invalid for add"
            r6.<init>(r7)
            throw r6
        L3c:
            java.lang.IllegalArgumentException r6 = new java.lang.IllegalArgumentException
            java.lang.String r7 = "Maximum value exceeded for add"
            r6.<init>(r7)
            throw r6
        L44:
            int r1 = r1 + 1
            r2 = r9[r8]
            int r1 = r1 - r2
            int r10 = r10 - r1
            int r1 = r8 + (-1)
            r2 = 1
            int[] r9 = r0.add(r7, r1, r9, r2)
            int r1 = r6.getMinimumValue(r7, r9)
            r9[r8] = r1
            goto L4
        L58:
            if (r10 >= 0) goto Lac
            int r1 = r6.getMinimumValue(r7, r9)
            r2 = r9[r8]
            int r2 = r2 + r10
            long r2 = (long) r2
            long r4 = (long) r1
            int r4 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r4 < 0) goto L6b
            int r10 = (int) r2
            r9[r8] = r10
            goto Lac
        L6b:
            if (r0 != 0) goto L98
            if (r8 == 0) goto L90
            int r0 = r8 + (-1)
            org.joda.time.DateTimeField r0 = r7.getField(r0)
            org.joda.time.DurationField r2 = r6.getRangeDurationField()
            org.joda.time.DurationFieldType r2 = r2.getType()
            org.joda.time.DurationField r3 = r0.getDurationField()
            org.joda.time.DurationFieldType r3 = r3.getType()
            if (r2 != r3) goto L88
            goto L98
        L88:
            java.lang.IllegalArgumentException r6 = new java.lang.IllegalArgumentException
            java.lang.String r7 = "Fields invalid for add"
            r6.<init>(r7)
            throw r6
        L90:
            java.lang.IllegalArgumentException r6 = new java.lang.IllegalArgumentException
            java.lang.String r7 = "Maximum value exceeded for add"
            r6.<init>(r7)
            throw r6
        L98:
            int r1 = r1 + (-1)
            r2 = r9[r8]
            int r1 = r1 - r2
            int r10 = r10 - r1
            int r1 = r8 + (-1)
            r2 = -1
            int[] r9 = r0.add(r7, r1, r9, r2)
            int r1 = r6.getMaximumValue(r7, r9)
            r9[r8] = r1
            goto L58
        Lac:
            r10 = r9[r8]
            int[] r6 = r6.set(r7, r8, r9, r10)
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: org.joda.time.field.BaseDateTimeField.add(org.joda.time.ReadablePartial, int, int[], int):int[]");
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x005d, code lost:
        if (r10 >= 0) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x005f, code lost:
        r1 = getMinimumValue(r7, r9);
        r2 = r9[r8] + r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x006a, code lost:
        if (r2 < r1) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x006c, code lost:
        r9[r8] = (int) r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0070, code lost:
        if (r0 != null) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0072, code lost:
        if (r8 != 0) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0074, code lost:
        r10 = r10 - ((r1 - 1) - r9[r8]);
        r9[r8] = getMaximumValue(r7, r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0081, code lost:
        r0 = r7.getField(r8 - 1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0097, code lost:
        if (getRangeDurationField().getType() != r0.getDurationField().getType()) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x00a1, code lost:
        throw new java.lang.IllegalArgumentException("Fields invalid for add");
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00a2, code lost:
        r10 = r10 - ((r1 - 1) - r9[r8]);
        r9 = r0.addWrapPartial(r7, r8 - 1, r9, -1);
        r9[r8] = getMaximumValue(r7, r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x00bc, code lost:
        return set(r7, r8, r9, r9[r8]);
     */
    @Override // org.joda.time.DateTimeField
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int[] addWrapPartial(org.joda.time.ReadablePartial r7, int r8, int[] r9, int r10) {
        /*
            r6 = this;
            if (r10 != 0) goto L3
            return r9
        L3:
            r0 = 0
        L4:
            if (r10 <= 0) goto L5d
            int r1 = r6.getMaximumValue(r7, r9)
            r2 = r9[r8]
            int r2 = r2 + r10
            long r2 = (long) r2
            long r4 = (long) r1
            int r4 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r4 > 0) goto L17
            int r1 = (int) r2
            r9[r8] = r1
            goto L5d
        L17:
            if (r0 != 0) goto L49
            if (r8 != 0) goto L28
            int r1 = r1 + 1
            r2 = r9[r8]
            int r1 = r1 - r2
            int r10 = r10 - r1
            int r1 = r6.getMinimumValue(r7, r9)
            r9[r8] = r1
            goto L4
        L28:
            int r0 = r8 + (-1)
            org.joda.time.DateTimeField r0 = r7.getField(r0)
            org.joda.time.DurationField r2 = r6.getRangeDurationField()
            org.joda.time.DurationFieldType r2 = r2.getType()
            org.joda.time.DurationField r3 = r0.getDurationField()
            org.joda.time.DurationFieldType r3 = r3.getType()
            if (r2 != r3) goto L41
            goto L49
        L41:
            java.lang.IllegalArgumentException r6 = new java.lang.IllegalArgumentException
            java.lang.String r7 = "Fields invalid for add"
            r6.<init>(r7)
            throw r6
        L49:
            int r1 = r1 + 1
            r2 = r9[r8]
            int r1 = r1 - r2
            int r10 = r10 - r1
            int r1 = r8 + (-1)
            r2 = 1
            int[] r9 = r0.addWrapPartial(r7, r1, r9, r2)
            int r1 = r6.getMinimumValue(r7, r9)
            r9[r8] = r1
            goto L4
        L5d:
            if (r10 >= 0) goto Lb6
            int r1 = r6.getMinimumValue(r7, r9)
            r2 = r9[r8]
            int r2 = r2 + r10
            long r2 = (long) r2
            long r4 = (long) r1
            int r4 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r4 < 0) goto L70
            int r10 = (int) r2
            r9[r8] = r10
            goto Lb6
        L70:
            if (r0 != 0) goto La2
            if (r8 != 0) goto L81
            int r1 = r1 + (-1)
            r2 = r9[r8]
            int r1 = r1 - r2
            int r10 = r10 - r1
            int r1 = r6.getMaximumValue(r7, r9)
            r9[r8] = r1
            goto L5d
        L81:
            int r0 = r8 + (-1)
            org.joda.time.DateTimeField r0 = r7.getField(r0)
            org.joda.time.DurationField r2 = r6.getRangeDurationField()
            org.joda.time.DurationFieldType r2 = r2.getType()
            org.joda.time.DurationField r3 = r0.getDurationField()
            org.joda.time.DurationFieldType r3 = r3.getType()
            if (r2 != r3) goto L9a
            goto La2
        L9a:
            java.lang.IllegalArgumentException r6 = new java.lang.IllegalArgumentException
            java.lang.String r7 = "Fields invalid for add"
            r6.<init>(r7)
            throw r6
        La2:
            int r1 = r1 + (-1)
            r2 = r9[r8]
            int r1 = r1 - r2
            int r10 = r10 - r1
            int r1 = r8 + (-1)
            r2 = -1
            int[] r9 = r0.addWrapPartial(r7, r1, r9, r2)
            int r1 = r6.getMaximumValue(r7, r9)
            r9[r8] = r1
            goto L5d
        Lb6:
            r10 = r9[r8]
            int[] r6 = r6.set(r7, r8, r9, r10)
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: org.joda.time.field.BaseDateTimeField.addWrapPartial(org.joda.time.ReadablePartial, int, int[], int):int[]");
    }

    @Override // org.joda.time.DateTimeField
    public long addWrapField(long j, int i) {
        return set(j, FieldUtils.getWrappedValue(get(j), i, getMinimumValue(j), getMaximumValue(j)));
    }

    @Override // org.joda.time.DateTimeField
    public int[] addWrapField(ReadablePartial readablePartial, int i, int[] iArr, int i2) {
        return set(readablePartial, i, iArr, FieldUtils.getWrappedValue(iArr[i], i2, getMinimumValue(readablePartial), getMaximumValue(readablePartial)));
    }

    @Override // org.joda.time.DateTimeField
    public int getDifference(long j, long j2) {
        return getDurationField().getDifference(j, j2);
    }

    @Override // org.joda.time.DateTimeField
    public long getDifferenceAsLong(long j, long j2) {
        return getDurationField().getDifferenceAsLong(j, j2);
    }

    @Override // org.joda.time.DateTimeField
    public int[] set(ReadablePartial readablePartial, int i, int[] iArr, int i2) {
        FieldUtils.verifyValueBounds(this, i2, getMinimumValue(readablePartial, iArr), getMaximumValue(readablePartial, iArr));
        iArr[i] = i2;
        while (true) {
            i++;
            if (i >= readablePartial.size()) {
                return iArr;
            }
            DateTimeField field = readablePartial.getField(i);
            if (iArr[i] > field.getMaximumValue(readablePartial, iArr)) {
                iArr[i] = field.getMaximumValue(readablePartial, iArr);
            }
            if (iArr[i] < field.getMinimumValue(readablePartial, iArr)) {
                iArr[i] = field.getMinimumValue(readablePartial, iArr);
            }
        }
    }

    @Override // org.joda.time.DateTimeField
    public long set(long j, String str, Locale locale) {
        return set(j, convertText(str, locale));
    }

    @Override // org.joda.time.DateTimeField
    public final long set(long j, String str) {
        return set(j, str, null);
    }

    @Override // org.joda.time.DateTimeField
    public int[] set(ReadablePartial readablePartial, int i, int[] iArr, String str, Locale locale) {
        return set(readablePartial, i, iArr, convertText(str, locale));
    }

    protected int convertText(String str, Locale locale) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException unused) {
            throw new IllegalFieldValueException(getType(), str);
        }
    }

    @Override // org.joda.time.DateTimeField
    public int getMinimumValue(long j) {
        return getMinimumValue();
    }

    @Override // org.joda.time.DateTimeField
    public int getMinimumValue(ReadablePartial readablePartial) {
        return getMinimumValue();
    }

    @Override // org.joda.time.DateTimeField
    public int getMinimumValue(ReadablePartial readablePartial, int[] iArr) {
        return getMinimumValue(readablePartial);
    }

    @Override // org.joda.time.DateTimeField
    public int getMaximumValue(long j) {
        return getMaximumValue();
    }

    @Override // org.joda.time.DateTimeField
    public int getMaximumValue(ReadablePartial readablePartial) {
        return getMaximumValue();
    }

    @Override // org.joda.time.DateTimeField
    public int getMaximumValue(ReadablePartial readablePartial, int[] iArr) {
        return getMaximumValue(readablePartial);
    }

    @Override // org.joda.time.DateTimeField
    public int getMaximumTextLength(Locale locale) {
        int maximumValue = getMaximumValue();
        if (maximumValue >= 0) {
            if (maximumValue < 10) {
                return 1;
            }
            if (maximumValue < 100) {
                return 2;
            }
            if (maximumValue < 1000) {
                return 3;
            }
        }
        return Integer.toString(maximumValue).length();
    }

    @Override // org.joda.time.DateTimeField
    public int getMaximumShortTextLength(Locale locale) {
        return getMaximumTextLength(locale);
    }

    @Override // org.joda.time.DateTimeField
    public long roundCeiling(long j) {
        long roundFloor = roundFloor(j);
        return roundFloor != j ? add(roundFloor, 1) : j;
    }

    @Override // org.joda.time.DateTimeField
    public long roundHalfFloor(long j) {
        long roundFloor = roundFloor(j);
        long roundCeiling = roundCeiling(j);
        return j - roundFloor <= roundCeiling - j ? roundFloor : roundCeiling;
    }

    @Override // org.joda.time.DateTimeField
    public long roundHalfCeiling(long j) {
        long roundFloor = roundFloor(j);
        long roundCeiling = roundCeiling(j);
        return roundCeiling - j <= j - roundFloor ? roundCeiling : roundFloor;
    }

    @Override // org.joda.time.DateTimeField
    public long roundHalfEven(long j) {
        long roundFloor = roundFloor(j);
        long roundCeiling = roundCeiling(j);
        long j2 = j - roundFloor;
        long j3 = roundCeiling - j;
        return j2 < j3 ? roundFloor : (j3 >= j2 && (get(roundCeiling) & 1) != 0) ? roundFloor : roundCeiling;
    }

    @Override // org.joda.time.DateTimeField
    public long remainder(long j) {
        return j - roundFloor(j);
    }

    @Override // org.joda.time.DateTimeField
    public String toString() {
        return "DateTimeField[" + getName() + ']';
    }
}
