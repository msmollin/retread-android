package com.google.android.gms.common.server.response;

import com.google.android.gms.common.server.response.FastParser;
import java.io.BufferedReader;
import java.io.IOException;

/* loaded from: classes.dex */
final class zzb implements FastParser.zza<Long> {
    @Override // com.google.android.gms.common.server.response.FastParser.zza
    public final /* synthetic */ Long zzh(FastParser fastParser, BufferedReader bufferedReader) throws FastParser.ParseException, IOException {
        long zze;
        zze = fastParser.zze(bufferedReader);
        return Long.valueOf(zze);
    }
}
