package com.treadly.Treadly.UI.TreadlyVideo.Data;

import android.os.Handler;
import android.os.Looper;
import com.treadly.Treadly.Data.Managers.TreadlyEventManager;
import com.treadly.Treadly.Data.Model.TreadlyNetworkResponse;
import com.treadly.Treadly.Data.Utility.RequestUtil;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class BroadcastInviteServiceHelper {
    public static final String SERVICE_URI_BASE = "https://env-staging.treadly.co:8080/vendor/tokbox/v1";
    public static final String SERVICE_URI_ROUTE_BROADCAST_INVITE_ACCEPT = "https://env-staging.treadly.co:8080/vendor/tokbox/v1/broadcast/invite/accept";
    public static final String SERVICE_URI_ROUTE_BROADCAST_INVITE_DECLINE = "https://env-staging.treadly.co:8080/vendor/tokbox/v1/broadcast/invite/decline";
    public static final String SERVICE_URI_ROUTE_BROADCAST_INVITE_PENDING_USER = "https://env-staging.treadly.co:8080/vendor/tokbox/v1/broadcast/invite/pending/user";
    public static final String SERVICE_URI_ROUTE_BROADCAST_INVITE_REQUEST = "https://env-staging.treadly.co:8080/vendor/tokbox/v1/broadcast/invite/request";
    public static final String keyBroadcastInviteInfoList = "broadcast_invite_info_list";
    public static final String keyUserIdFrom = "user_id_from";
    public static final String keyUserIdTo = "user_id_to";

    /* loaded from: classes2.dex */
    public interface BroadcastInviteResponseListener {
        void onInviteInfoList(String str, List<InviteServiceInviteInfo> list);

        void onSuccess(String str);
    }

    public static void requestBroadcastInvite(String str, final String str2, final BroadcastInviteResponseListener broadcastInviteResponseListener) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("user_id_from", str);
            jSONObject.put("user_id_to", str2);
            requestBasicUsersPost(SERVICE_URI_ROUTE_BROADCAST_INVITE_REQUEST, jSONObject, new BroadcastInviteResponseListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastInviteServiceHelper.1
                @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastInviteServiceHelper.BroadcastInviteResponseListener
                public void onInviteInfoList(String str3, List<InviteServiceInviteInfo> list) {
                }

                @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastInviteServiceHelper.BroadcastInviteResponseListener
                public void onSuccess(String str3) {
                    if (str3 == null) {
                        TreadlyEventManager.getInstance().sendUserVideoBoardcastInviteRequest(str2);
                    }
                    broadcastInviteResponseListener.onSuccess(str3);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void acceptBroadcastInvite(String str, String str2, BroadcastInviteResponseListener broadcastInviteResponseListener) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("user_id_from", str);
            jSONObject.put("user_id_to", str2);
            requestBasicUsersPost(SERVICE_URI_ROUTE_BROADCAST_INVITE_ACCEPT, jSONObject, broadcastInviteResponseListener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void declineBroadcastInvite(String str, String str2, BroadcastInviteResponseListener broadcastInviteResponseListener) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("user_id_from", str);
            jSONObject.put("user_id_to", str2);
            requestBasicUsersPost(SERVICE_URI_ROUTE_BROADCAST_INVITE_DECLINE, jSONObject, broadcastInviteResponseListener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getPendingBroadcastInviteInfoList(String str, final BroadcastInviteResponseListener broadcastInviteResponseListener) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("user_id_to", str);
            RequestUtil.shared.postJson(SERVICE_URI_ROUTE_BROADCAST_INVITE_PENDING_USER, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastInviteServiceHelper.2
                @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
                public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
                    if (treadlyNetworkResponse == null || !treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                        BroadcastInviteServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastInviteServiceHelper.2.1
                            @Override // java.lang.Runnable
                            public void run() {
                                BroadcastInviteResponseListener.this.onInviteInfoList("Error: Could not make request", null);
                            }
                        });
                        return;
                    }
                    JSONArray optJSONArray = treadlyNetworkResponse.response.optJSONArray(BroadcastInviteServiceHelper.keyBroadcastInviteInfoList);
                    if (optJSONArray != null) {
                        final ArrayList arrayList = new ArrayList();
                        for (int i = 0; i < optJSONArray.length(); i++) {
                            JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                            arrayList.add(new InviteServiceInviteInfo(optJSONObject.optString("user_id_from"), optJSONObject.optString("user_id_to")));
                        }
                        BroadcastInviteServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastInviteServiceHelper.2.2
                            @Override // java.lang.Runnable
                            public void run() {
                                BroadcastInviteResponseListener.this.onInviteInfoList(null, arrayList);
                            }
                        });
                        return;
                    }
                    BroadcastInviteServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastInviteServiceHelper.2.3
                        @Override // java.lang.Runnable
                        public void run() {
                            BroadcastInviteResponseListener.this.onInviteInfoList("Error: Could not parse results", null);
                        }
                    });
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            broadcastInviteResponseListener.onInviteInfoList(e.toString(), null);
        }
    }

    private static void requestBasicUsersPost(String str, JSONObject jSONObject, final BroadcastInviteResponseListener broadcastInviteResponseListener) {
        RequestUtil.shared.postJson(str, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastInviteServiceHelper.3
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
                if (treadlyNetworkResponse == null || !treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                    BroadcastInviteServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastInviteServiceHelper.3.1
                        @Override // java.lang.Runnable
                        public void run() {
                            BroadcastInviteResponseListener.this.onSuccess("Error: Could not make request");
                        }
                    });
                }
                JSONObject jSONObject2 = treadlyNetworkResponse.response;
                BroadcastInviteServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastInviteServiceHelper.3.2
                    @Override // java.lang.Runnable
                    public void run() {
                        BroadcastInviteResponseListener.this.onSuccess(null);
                    }
                });
            }
        });
        runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastInviteServiceHelper.4
            @Override // java.lang.Runnable
            public void run() {
                BroadcastInviteResponseListener.this.onSuccess("Error: Could not make request");
            }
        });
    }

    static void runOnMain(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
