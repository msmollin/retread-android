package com.google.android.gms.common.server.response;

import com.google.android.gms.common.server.response.FastParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;

/* loaded from: classes.dex */
final class zzh implements FastParser.zza<BigDecimal> {
    @Override // com.google.android.gms.common.server.response.FastParser.zza
    public final /* synthetic */ BigDecimal zzh(FastParser fastParser, BufferedReader bufferedReader) throws FastParser.ParseException, IOException {
        BigDecimal zzi;
        zzi = fastParser.zzi(bufferedReader);
        return zzi;
    }
}
