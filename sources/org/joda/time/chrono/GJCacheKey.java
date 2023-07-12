package org.joda.time.chrono;

import org.joda.time.DateTimeZone;
import org.joda.time.Instant;

/* loaded from: classes2.dex */
class GJCacheKey {
    private final Instant cutoverInstant;
    private final int minDaysInFirstWeek;
    private final DateTimeZone zone;

    /* JADX INFO: Access modifiers changed from: package-private */
    public GJCacheKey(DateTimeZone dateTimeZone, Instant instant, int i) {
        this.zone = dateTimeZone;
        this.cutoverInstant = instant;
        this.minDaysInFirstWeek = i;
    }

    public int hashCode() {
        return (((((this.cutoverInstant == null ? 0 : this.cutoverInstant.hashCode()) + 31) * 31) + this.minDaysInFirstWeek) * 31) + (this.zone != null ? this.zone.hashCode() : 0);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && (obj instanceof GJCacheKey)) {
            GJCacheKey gJCacheKey = (GJCacheKey) obj;
            if (this.cutoverInstant == null) {
                if (gJCacheKey.cutoverInstant != null) {
                    return false;
                }
            } else if (!this.cutoverInstant.equals(gJCacheKey.cutoverInstant)) {
                return false;
            }
            if (this.minDaysInFirstWeek != gJCacheKey.minDaysInFirstWeek) {
                return false;
            }
            if (this.zone == null) {
                if (gJCacheKey.zone != null) {
                    return false;
                }
            } else if (!this.zone.equals(gJCacheKey.zone)) {
                return false;
            }
            return true;
        }
        return false;
    }
}
