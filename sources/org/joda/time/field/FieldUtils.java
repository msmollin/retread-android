package org.joda.time.field;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

/* loaded from: classes2.dex */
public class FieldUtils {
    private FieldUtils() {
    }

    public static int safeNegate(int i) {
        if (i != Integer.MIN_VALUE) {
            return -i;
        }
        throw new ArithmeticException("Integer.MIN_VALUE cannot be negated");
    }

    public static int safeAdd(int i, int i2) {
        int i3 = i + i2;
        if ((i ^ i3) >= 0 || (i ^ i2) < 0) {
            return i3;
        }
        throw new ArithmeticException("The calculation caused an overflow: " + i + " + " + i2);
    }

    public static long safeAdd(long j, long j2) {
        long j3 = j + j2;
        if ((j ^ j3) >= 0 || (j ^ j2) < 0) {
            return j3;
        }
        throw new ArithmeticException("The calculation caused an overflow: " + j + " + " + j2);
    }

    public static long safeSubtract(long j, long j2) {
        long j3 = j - j2;
        if ((j ^ j3) >= 0 || (j ^ j2) >= 0) {
            return j3;
        }
        throw new ArithmeticException("The calculation caused an overflow: " + j + " - " + j2);
    }

    public static int safeMultiply(int i, int i2) {
        long j = i * i2;
        if (j < -2147483648L || j > 2147483647L) {
            throw new ArithmeticException("Multiplication overflows an int: " + i + " * " + i2);
        }
        return (int) j;
    }

    public static long safeMultiply(long j, int i) {
        switch (i) {
            case -1:
                if (j != Long.MIN_VALUE) {
                    return -j;
                }
                throw new ArithmeticException("Multiplication overflows a long: " + j + " * " + i);
            case 0:
                return 0L;
            case 1:
                return j;
            default:
                long j2 = i;
                long j3 = j * j2;
                if (j3 / j2 == j) {
                    return j3;
                }
                throw new ArithmeticException("Multiplication overflows a long: " + j + " * " + i);
        }
    }

    public static long safeMultiply(long j, long j2) {
        if (j2 == 1) {
            return j;
        }
        if (j == 1) {
            return j2;
        }
        if (j == 0 || j2 == 0) {
            return 0L;
        }
        long j3 = j * j2;
        if (j3 / j2 != j || ((j == Long.MIN_VALUE && j2 == -1) || (j2 == Long.MIN_VALUE && j == -1))) {
            throw new ArithmeticException("Multiplication overflows a long: " + j + " * " + j2);
        }
        return j3;
    }

    public static long safeDivide(long j, long j2) {
        if (j == Long.MIN_VALUE && j2 == -1) {
            throw new ArithmeticException("Multiplication overflows a long: " + j + " / " + j2);
        }
        return j / j2;
    }

    public static long safeDivide(long j, long j2, RoundingMode roundingMode) {
        if (j == Long.MIN_VALUE && j2 == -1) {
            throw new ArithmeticException("Multiplication overflows a long: " + j + " / " + j2);
        }
        return new BigDecimal(j).divide(new BigDecimal(j2), roundingMode).longValue();
    }

    public static int safeToInt(long j) {
        if (-2147483648L > j || j > 2147483647L) {
            throw new ArithmeticException("Value cannot fit in an int: " + j);
        }
        return (int) j;
    }

    public static int safeMultiplyToInt(long j, long j2) {
        return safeToInt(safeMultiply(j, j2));
    }

    public static void verifyValueBounds(DateTimeField dateTimeField, int i, int i2, int i3) {
        if (i < i2 || i > i3) {
            throw new IllegalFieldValueException(dateTimeField.getType(), Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3));
        }
    }

    public static void verifyValueBounds(DateTimeFieldType dateTimeFieldType, int i, int i2, int i3) {
        if (i < i2 || i > i3) {
            throw new IllegalFieldValueException(dateTimeFieldType, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3));
        }
    }

    public static void verifyValueBounds(String str, int i, int i2, int i3) {
        if (i < i2 || i > i3) {
            throw new IllegalFieldValueException(str, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3));
        }
    }

    public static int getWrappedValue(int i, int i2, int i3, int i4) {
        return getWrappedValue(i + i2, i3, i4);
    }

    public static int getWrappedValue(int i, int i2, int i3) {
        if (i2 < i3) {
            int i4 = (i3 - i2) + 1;
            int i5 = i - i2;
            if (i5 >= 0) {
                return (i5 % i4) + i2;
            }
            int i6 = (-i5) % i4;
            return i6 == 0 ? i2 + 0 : (i4 - i6) + i2;
        }
        throw new IllegalArgumentException("MIN > MAX");
    }

    public static boolean equals(Object obj, Object obj2) {
        if (obj == obj2) {
            return true;
        }
        if (obj == null || obj2 == null) {
            return false;
        }
        return obj.equals(obj2);
    }
}
