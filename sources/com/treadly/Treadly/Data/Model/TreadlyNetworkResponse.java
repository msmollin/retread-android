package com.treadly.Treadly.Data.Model;

import org.json.JSONObject;

/* loaded from: classes2.dex */
public class TreadlyNetworkResponse {
    public JSONObject response;
    public int statusCode;

    public TreadlyNetworkResponse() {
        this.statusCode = -1;
        this.response = null;
    }

    public TreadlyNetworkResponse(int i, JSONObject jSONObject) {
        this.statusCode = i;
        this.response = jSONObject;
    }

    public boolean ok() {
        return this.statusCode >= 200 && this.statusCode < 300;
    }
}
