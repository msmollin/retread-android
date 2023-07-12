package com.google.android.gms.common.server.response;

import com.google.android.gms.common.server.response.FastParser;
import java.io.BufferedReader;
import java.io.IOException;

/* loaded from: classes.dex */
final class zza implements FastParser.zza<Integer> {
    @Override // com.google.android.gms.common.server.response.FastParser.zza
    public final /* synthetic */ Integer zzh(FastParser fastParser, BufferedReader bufferedReader) throws FastParser.ParseException, IOException {
        int zzd;
        zzd = fastParser.zzd(bufferedReader);
        return Integer.valueOf(zzd);
    }
}
