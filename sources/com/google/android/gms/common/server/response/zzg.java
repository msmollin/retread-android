package com.google.android.gms.common.server.response;

import com.google.android.gms.common.server.response.FastParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;

/* loaded from: classes.dex */
final class zzg implements FastParser.zza<BigInteger> {
    @Override // com.google.android.gms.common.server.response.FastParser.zza
    public final /* synthetic */ BigInteger zzh(FastParser fastParser, BufferedReader bufferedReader) throws FastParser.ParseException, IOException {
        BigInteger zzf;
        zzf = fastParser.zzf(bufferedReader);
        return zzf;
    }
}
