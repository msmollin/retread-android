package org.joda.time.format;

import java.io.IOException;
import java.io.Writer;
import java.util.Locale;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadablePartial;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class DateTimePrinterInternalPrinter implements InternalPrinter {
    private final DateTimePrinter underlying;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static InternalPrinter of(DateTimePrinter dateTimePrinter) {
        if (dateTimePrinter instanceof InternalPrinterDateTimePrinter) {
            return (InternalPrinter) dateTimePrinter;
        }
        if (dateTimePrinter == null) {
            return null;
        }
        return new DateTimePrinterInternalPrinter(dateTimePrinter);
    }

    private DateTimePrinterInternalPrinter(DateTimePrinter dateTimePrinter) {
        this.underlying = dateTimePrinter;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DateTimePrinter getUnderlying() {
        return this.underlying;
    }

    @Override // org.joda.time.format.InternalPrinter
    public int estimatePrintedLength() {
        return this.underlying.estimatePrintedLength();
    }

    @Override // org.joda.time.format.InternalPrinter
    public void printTo(Appendable appendable, long j, Chronology chronology, int i, DateTimeZone dateTimeZone, Locale locale) throws IOException {
        if (appendable instanceof StringBuffer) {
            this.underlying.printTo((StringBuffer) appendable, j, chronology, i, dateTimeZone, locale);
        } else if (appendable instanceof Writer) {
            this.underlying.printTo((Writer) appendable, j, chronology, i, dateTimeZone, locale);
        } else {
            StringBuffer stringBuffer = new StringBuffer(estimatePrintedLength());
            this.underlying.printTo(stringBuffer, j, chronology, i, dateTimeZone, locale);
            appendable.append(stringBuffer);
        }
    }

    @Override // org.joda.time.format.InternalPrinter
    public void printTo(Appendable appendable, ReadablePartial readablePartial, Locale locale) throws IOException {
        if (appendable instanceof StringBuffer) {
            this.underlying.printTo((StringBuffer) appendable, readablePartial, locale);
        } else if (appendable instanceof Writer) {
            this.underlying.printTo((Writer) appendable, readablePartial, locale);
        } else {
            StringBuffer stringBuffer = new StringBuffer(estimatePrintedLength());
            this.underlying.printTo(stringBuffer, readablePartial, locale);
            appendable.append(stringBuffer);
        }
    }
}
