package org.joda.time.format;

import java.io.IOException;
import java.io.Writer;
import java.util.Locale;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadablePartial;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class InternalPrinterDateTimePrinter implements DateTimePrinter, InternalPrinter {
    private final InternalPrinter underlying;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static DateTimePrinter of(InternalPrinter internalPrinter) {
        if (internalPrinter instanceof DateTimePrinterInternalPrinter) {
            return ((DateTimePrinterInternalPrinter) internalPrinter).getUnderlying();
        }
        if (internalPrinter instanceof DateTimePrinter) {
            return (DateTimePrinter) internalPrinter;
        }
        if (internalPrinter == null) {
            return null;
        }
        return new InternalPrinterDateTimePrinter(internalPrinter);
    }

    private InternalPrinterDateTimePrinter(InternalPrinter internalPrinter) {
        this.underlying = internalPrinter;
    }

    @Override // org.joda.time.format.DateTimePrinter, org.joda.time.format.InternalPrinter
    public int estimatePrintedLength() {
        return this.underlying.estimatePrintedLength();
    }

    @Override // org.joda.time.format.DateTimePrinter
    public void printTo(StringBuffer stringBuffer, long j, Chronology chronology, int i, DateTimeZone dateTimeZone, Locale locale) {
        try {
            this.underlying.printTo(stringBuffer, j, chronology, i, dateTimeZone, locale);
        } catch (IOException unused) {
        }
    }

    @Override // org.joda.time.format.DateTimePrinter
    public void printTo(Writer writer, long j, Chronology chronology, int i, DateTimeZone dateTimeZone, Locale locale) throws IOException {
        this.underlying.printTo(writer, j, chronology, i, dateTimeZone, locale);
    }

    @Override // org.joda.time.format.InternalPrinter
    public void printTo(Appendable appendable, long j, Chronology chronology, int i, DateTimeZone dateTimeZone, Locale locale) throws IOException {
        this.underlying.printTo(appendable, j, chronology, i, dateTimeZone, locale);
    }

    @Override // org.joda.time.format.DateTimePrinter
    public void printTo(StringBuffer stringBuffer, ReadablePartial readablePartial, Locale locale) {
        try {
            this.underlying.printTo(stringBuffer, readablePartial, locale);
        } catch (IOException unused) {
        }
    }

    @Override // org.joda.time.format.DateTimePrinter
    public void printTo(Writer writer, ReadablePartial readablePartial, Locale locale) throws IOException {
        this.underlying.printTo(writer, readablePartial, locale);
    }

    @Override // org.joda.time.format.InternalPrinter
    public void printTo(Appendable appendable, ReadablePartial readablePartial, Locale locale) throws IOException {
        this.underlying.printTo(appendable, readablePartial, locale);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof InternalPrinterDateTimePrinter) {
            return this.underlying.equals(((InternalPrinterDateTimePrinter) obj).underlying);
        }
        return false;
    }
}
