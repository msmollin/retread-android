package com.treadly.Treadly.Data.Model.DynamicBanner.DynamicBannerTypes;

import androidx.annotation.Nullable;
import com.treadly.Treadly.Data.Model.DynamicBanner.DynamicBannerAppPage;
import com.treadly.Treadly.Data.Model.DynamicBanner.DynamicBannerInfo;
import com.treadly.Treadly.Data.Model.DynamicBanner.DynamicBannerType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class DefaultMessageBannerInfo extends DynamicBannerInfo {
    public String body;
    public List<String> boldText;
    public HashMap<String, String> links;
    public String title;

    public DefaultMessageBannerInfo(DynamicBannerAppPage dynamicBannerAppPage, int i, DynamicBannerType dynamicBannerType, int i2, int i3, String str) {
        super(dynamicBannerAppPage, i, dynamicBannerType, i2, i3, str);
        try {
            JSONObject jSONObject = new JSONObject(str);
            this.links = new HashMap<>();
            if (jSONObject.has("links")) {
                final JSONObject jSONObject2 = jSONObject.getJSONObject("links");
                jSONObject2.keys().forEachRemaining(new Consumer() { // from class: com.treadly.Treadly.Data.Model.DynamicBanner.DynamicBannerTypes.-$$Lambda$DefaultMessageBannerInfo$Kg4LHKXeCMNbvIyf0oaYadPekAE
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        DefaultMessageBannerInfo.this.links.put(r2, jSONObject2.getString((String) obj));
                    }
                });
            }
            this.boldText = new ArrayList();
            if (jSONObject.has("bold")) {
                JSONArray jSONArray = jSONObject.getJSONArray("bold");
                for (int i4 = 0; i4 < jSONArray.length(); i4++) {
                    try {
                        this.boldText.add(jSONArray.getString(i4));
                    } catch (JSONException unused) {
                    }
                }
            }
            this.title = "";
            if (jSONObject.has("title")) {
                this.title = jSONObject.getString("title");
            }
            this.body = "";
            if (jSONObject.has("body")) {
                this.body = jSONObject.getString("body");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static DefaultMessageBannerInfo fromBannerInfo(@Nullable DynamicBannerInfo dynamicBannerInfo) {
        if (dynamicBannerInfo == null) {
            return null;
        }
        return new DefaultMessageBannerInfo(dynamicBannerInfo.appPage, dynamicBannerInfo.displayTime, dynamicBannerInfo.bannerType, dynamicBannerInfo.bannerVersion, dynamicBannerInfo.bannerPriority, dynamicBannerInfo.bannerData);
    }
}
