package org.joda.time.base;

import com.facebook.appevents.AppEventsConstants;
import org.joda.convert.ToString;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.ReadableDuration;
import org.joda.time.format.FormatUtils;

/* loaded from: classes2.dex */
public abstract class AbstractDuration implements ReadableDuration {
    @Override // org.joda.time.ReadableDuration
    public Duration toDuration() {
        return new Duration(getMillis());
    }

    @Override // org.joda.time.ReadableDuration
    public Period toPeriod() {
        return new Period(getMillis());
    }

    @Override // java.lang.Comparable
    public int compareTo(ReadableDuration readableDuration) {
        int i = (getMillis() > readableDuration.getMillis() ? 1 : (getMillis() == readableDuration.getMillis() ? 0 : -1));
        if (i < 0) {
            return -1;
        }
        return i > 0 ? 1 : 0;
    }

    @Override // org.joda.time.ReadableDuration
    public boolean isEqual(ReadableDuration readableDuration) {
        if (readableDuration == null) {
            readableDuration = Duration.ZERO;
        }
        return compareTo(readableDuration) == 0;
    }

    @Override // org.joda.time.ReadableDuration
    public boolean isLongerThan(ReadableDuration readableDuration) {
        if (readableDuration == null) {
            readableDuration = Duration.ZERO;
        }
        return compareTo(readableDuration) > 0;
    }

    @Override // org.joda.time.ReadableDuration
    public boolean isShorterThan(ReadableDuration readableDuration) {
        if (readableDuration == null) {
            readableDuration = Duration.ZERO;
        }
        return compareTo(readableDuration) < 0;
    }

    @Override // org.joda.time.ReadableDuration
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof ReadableDuration) && getMillis() == ((ReadableDuration) obj).getMillis();
    }

    @Override // org.joda.time.ReadableDuration
    public int hashCode() {
        long millis = getMillis();
        return (int) (millis ^ (millis >>> 32));
    }

    @Override // org.joda.time.ReadableDuration
    @ToString
    public String toString() {
        long millis = getMillis();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("PT");
        boolean z = millis < 0;
        FormatUtils.appendUnpaddedInteger(stringBuffer, millis);
        while (true) {
            int i = 3;
            if (stringBuffer.length() >= (z ? 7 : 6)) {
                break;
            }
            if (!z) {
                i = 2;
            }
            stringBuffer.insert(i, AppEventsConstants.EVENT_PARAM_VALUE_NO);
        }
        if ((millis / 1000) * 1000 == millis) {
            stringBuffer.setLength(stringBuffer.length() - 3);
        } else {
            stringBuffer.insert(stringBuffer.length() - 3, ".");
        }
        stringBuffer.append('S');
        return stringBuffer.toString();
    }
}
