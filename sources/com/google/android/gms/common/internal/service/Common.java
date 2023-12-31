package com.google.android.gms.common.internal.service;

import com.google.android.gms.common.api.Api;

/* loaded from: classes.dex */
public final class Common {
    public static final Api.ClientKey<CommonClient> CLIENT_KEY = new Api.ClientKey<>();
    private static final Api.AbstractClientBuilder<CommonClient, Api.ApiOptions.NoOptions> CLIENT_BUILDER = new zza();
    public static final Api<Api.ApiOptions.NoOptions> API = new Api<>("Common.API", CLIENT_BUILDER, CLIENT_KEY);
    public static final CommonApi CommonApi = new CommonApiImpl();
}
