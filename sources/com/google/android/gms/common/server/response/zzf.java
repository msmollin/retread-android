package com.google.android.gms.common.server.response;

import com.google.android.gms.common.server.response.FastParser;
import java.io.BufferedReader;
import java.io.IOException;

/* loaded from: classes.dex */
final class zzf implements FastParser.zza<String> {
    @Override // com.google.android.gms.common.server.response.FastParser.zza
    public final /* synthetic */ String zzh(FastParser fastParser, BufferedReader bufferedReader) throws FastParser.ParseException, IOException {
        String zzc;
        zzc = fastParser.zzc(bufferedReader);
        return zzc;
    }
}
