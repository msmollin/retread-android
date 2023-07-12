package com.google.android.gms.common.server.response;

import com.google.android.gms.common.server.response.FastParser;
import java.io.BufferedReader;
import java.io.IOException;

/* loaded from: classes.dex */
final class zzc implements FastParser.zza<Float> {
    @Override // com.google.android.gms.common.server.response.FastParser.zza
    public final /* synthetic */ Float zzh(FastParser fastParser, BufferedReader bufferedReader) throws FastParser.ParseException, IOException {
        float zzg;
        zzg = fastParser.zzg(bufferedReader);
        return Float.valueOf(zzg);
    }
}
