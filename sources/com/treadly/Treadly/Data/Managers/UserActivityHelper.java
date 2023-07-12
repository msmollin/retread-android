package com.treadly.Treadly.Data.Managers;

import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener;
import com.treadly.Treadly.Data.Model.TreadlyNetworkResponse;
import com.treadly.Treadly.Data.Model.UserActivityInfo;
import com.treadly.Treadly.Data.Utility.RequestUtil;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class UserActivityHelper {
    public static final String SERVICE_URI_BASE = "https://env-staging.treadly.co:8080/vendor/tokbox/v1";
    public static final String SERVICE_URI_ROUTE_USERS_ACTIVITY_GET = "https://env-staging.treadly.co:8080/vendor/tokbox/v1/users/activity/get";
    public static final String keyIsBroadcasting = "is_broadcasting";
    public static final String keyIsOnline = "is_online";
    public static final String keyUserActivityList = "user_activity_list";
    public static final String keyUserId = "user_id";
    public static final String keyUserIdFrom = "user_id_from";
    public static final String keyUserIdList = "user_id_list";

    public static void get(List<String> list, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) throws JSONException {
        String userId = TreadlyServiceManager.getInstance().getUserId();
        JSONArray jSONArray = new JSONArray();
        for (String str : list) {
            jSONArray.put(str);
        }
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(keyUserIdList, jSONArray);
        jSONObject.put("user_id_from", userId);
        RequestUtil.shared.postJson(SERVICE_URI_ROUTE_USERS_ACTIVITY_GET, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.Data.Managers.UserActivityHelper.1
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
                if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                    TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.UserActivityHelper.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            try {
                                TreadlyServiceResponseEventListener.this.onActivitiesInfo("Error: Could not make request", null);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    return;
                }
                JSONArray jSONArray2 = treadlyNetworkResponse.response.getJSONArray(UserActivityHelper.keyUserActivityList);
                if (jSONArray2 != null) {
                    final ArrayList arrayList = new ArrayList();
                    for (int i = 0; i < jSONArray2.length(); i++) {
                        JSONObject jSONObject2 = jSONArray2.getJSONObject(i);
                        arrayList.add(new UserActivityInfo(jSONObject2.optString("user_id"), jSONObject2.optBoolean("is_online"), jSONObject2.optBoolean(UserActivityHelper.keyIsBroadcasting)));
                    }
                    TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.UserActivityHelper.1.2
                        @Override // java.lang.Runnable
                        public void run() {
                            try {
                                TreadlyServiceResponseEventListener.this.onActivitiesInfo(null, arrayList);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    return;
                }
                TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.UserActivityHelper.1.3
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            TreadlyServiceResponseEventListener.this.onActivitiesInfo("Error: Could not parse results", null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
