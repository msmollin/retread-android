package org.joda.time;

import java.util.SimpleTimeZone;
import java.util.TimeZone;

/* loaded from: classes2.dex */
final class UTCDateTimeZone extends DateTimeZone {
    static final DateTimeZone INSTANCE = new UTCDateTimeZone();
    private static final long serialVersionUID = -3513011772763289092L;

    @Override // org.joda.time.DateTimeZone
    public String getNameKey(long j) {
        return "UTC";
    }

    @Override // org.joda.time.DateTimeZone
    public int getOffset(long j) {
        return 0;
    }

    @Override // org.joda.time.DateTimeZone
    public int getOffsetFromLocal(long j) {
        return 0;
    }

    @Override // org.joda.time.DateTimeZone
    public int getStandardOffset(long j) {
        return 0;
    }

    @Override // org.joda.time.DateTimeZone
    public boolean isFixed() {
        return true;
    }

    @Override // org.joda.time.DateTimeZone
    public long nextTransition(long j) {
        return j;
    }

    @Override // org.joda.time.DateTimeZone
    public long previousTransition(long j) {
        return j;
    }

    public UTCDateTimeZone() {
        super("UTC");
    }

    @Override // org.joda.time.DateTimeZone
    public TimeZone toTimeZone() {
        return new SimpleTimeZone(0, getID());
    }

    @Override // org.joda.time.DateTimeZone
    public boolean equals(Object obj) {
        return obj instanceof UTCDateTimeZone;
    }

    @Override // org.joda.time.DateTimeZone
    public int hashCode() {
        return getID().hashCode();
    }
}
