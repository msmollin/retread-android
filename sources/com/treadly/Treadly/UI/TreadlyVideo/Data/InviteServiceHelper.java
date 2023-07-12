package com.treadly.Treadly.UI.TreadlyVideo.Data;

import android.util.Log;
import com.treadly.Treadly.Data.Managers.TreadlyEventManager;
import com.treadly.Treadly.Data.Model.TreadlyNetworkResponse;
import com.treadly.Treadly.Data.Utility.RequestUtil;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class InviteServiceHelper {
    public static final String SERVICE_URI_BASE = "https://env-staging.treadly.co:8080/vendor/tokbox/v1";
    public static final String SERVICE_URI_ROUTE_INVITE_ACCEPT = "https://env-staging.treadly.co:8080/vendor/tokbox/v1/invite/accept";
    public static final String SERVICE_URI_ROUTE_INVITE_DECLINE = "https://env-staging.treadly.co:8080/vendor/tokbox/v1/invite/decline";
    public static final String SERVICE_URI_ROUTE_INVITE_DELETE = "https://env-staging.treadly.co:8080/vendor/tokbox/v1/invite/delete";
    public static final String SERVICE_URI_ROUTE_INVITE_PENDING_USER = "https://env-staging.treadly.co:8080/vendor/tokbox/v1/invite/pending/user";
    public static final String SERVICE_URI_ROUTE_INVITE_REQUEST = "https://env-staging.treadly.co:8080/vendor/tokbox/v1/invite/request";
    public static final String keyInviteInfoList = "invite_info_list";
    public static final String keyUserIdFrom = "user_id_from";
    public static final String keyUserIdTo = "user_id_to";

    /* loaded from: classes2.dex */
    public interface InviteServiceHelperInfoList {
        void onResponse(String str, List<InviteServiceInviteInfo> list);
    }

    /* loaded from: classes2.dex */
    public interface InviteServiceHelperInviteListener {
        void onResponse(String str);
    }

    /* loaded from: classes2.dex */
    public interface InviteServiceHelperRequestBasicUsersPost {
        void onResponse(String str);
    }

    public static void requestInvite(final String str, String str2, final String str3, final InviteServiceHelperInviteListener inviteServiceHelperInviteListener) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("user_id_from", str2);
            jSONObject.put("user_id_to", str3);
            requestBasicUsersPost(SERVICE_URI_ROUTE_INVITE_REQUEST, jSONObject, new InviteServiceHelperRequestBasicUsersPost() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.InviteServiceHelper.1
                @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.InviteServiceHelper.InviteServiceHelperRequestBasicUsersPost
                public void onResponse(String str4) {
                    if (str4 == null) {
                        TreadlyEventManager.getInstance().sendUserVideoPrivateInviteRequest(str3);
                        TreadlyEventManager.getInstance().sendVideoPrivateUserConnecting(str, str3);
                    }
                    inviteServiceHelperInviteListener.onResponse(str4);
                }
            });
        } catch (JSONException e) {
            Log.e("InviteServiceHelper", "handleInvite exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void deleteInvite(String str, final String str2, final InviteServiceHelperInviteListener inviteServiceHelperInviteListener) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("user_id_from", str);
            jSONObject.put("user_id_to", str2);
            requestBasicUsersPost(SERVICE_URI_ROUTE_INVITE_DELETE, jSONObject, new InviteServiceHelperRequestBasicUsersPost() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.InviteServiceHelper.2
                @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.InviteServiceHelper.InviteServiceHelperRequestBasicUsersPost
                public void onResponse(String str3) {
                    if (str3 == null) {
                        TreadlyEventManager.getInstance().sendUserVideoPrivateInviteDeleted(str2);
                    }
                    inviteServiceHelperInviteListener.onResponse(str3);
                }
            });
        } catch (JSONException e) {
            Log.e("INVITE_HELPER", e.getMessage());
        }
    }

    public static void acceptInvite(String str, String str2, InviteServiceHelperRequestBasicUsersPost inviteServiceHelperRequestBasicUsersPost) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("user_id_from", str);
            jSONObject.put("user_id_to", str2);
            requestBasicUsersPost(SERVICE_URI_ROUTE_INVITE_ACCEPT, jSONObject, inviteServiceHelperRequestBasicUsersPost);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void declineInvite(final String str, String str2, final InviteServiceHelperRequestBasicUsersPost inviteServiceHelperRequestBasicUsersPost) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("user_id_from", str);
            jSONObject.put("user_id_to", str2);
            requestBasicUsersPost(SERVICE_URI_ROUTE_INVITE_DECLINE, jSONObject, new InviteServiceHelperRequestBasicUsersPost() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.InviteServiceHelper.3
                @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.InviteServiceHelper.InviteServiceHelperRequestBasicUsersPost
                public void onResponse(String str3) {
                    if (str3 == null) {
                        TreadlyEventManager.getInstance().sendUserVideoPrivateInviteDeclined(str);
                    }
                    inviteServiceHelperRequestBasicUsersPost.onResponse(str3);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getPendingInviteInfoList(String str, final InviteServiceHelperInfoList inviteServiceHelperInfoList) {
        Log.d("InviteServiceHelper", "getPendingInviteInfoList()");
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("user_id_to", str);
            Log.d("InviteServiceHelper", "Sending request");
            RequestUtil.shared.postJson(SERVICE_URI_ROUTE_INVITE_PENDING_USER, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.InviteServiceHelper.4
                @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
                public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
                    if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                        VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.InviteServiceHelper.4.1
                            @Override // java.lang.Runnable
                            public void run() {
                                InviteServiceHelperInfoList.this.onResponse("Error: Could not make request", null);
                            }
                        });
                        return;
                    }
                    try {
                        JSONArray jSONArray = treadlyNetworkResponse.response.getJSONArray(InviteServiceHelper.keyInviteInfoList);
                        final ArrayList arrayList = new ArrayList();
                        for (int i = 0; i < jSONArray.length(); i++) {
                            JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                            arrayList.add(new InviteServiceInviteInfo(jSONObject2.optString("user_id_from"), jSONObject2.optString("user_id_to")));
                        }
                        VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.InviteServiceHelper.4.2
                            @Override // java.lang.Runnable
                            public void run() {
                                InviteServiceHelperInfoList.this.onResponse(null, arrayList);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            Log.e("InviteServiceHelper", "json error: " + e.getMessage());
        }
    }

    private static void requestBasicUsersPost(String str, JSONObject jSONObject, final InviteServiceHelperRequestBasicUsersPost inviteServiceHelperRequestBasicUsersPost) {
        RequestUtil.shared.postJson(str, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.InviteServiceHelper.5
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
                if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                    VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.InviteServiceHelper.5.1
                        @Override // java.lang.Runnable
                        public void run() {
                            InviteServiceHelperRequestBasicUsersPost.this.onResponse("Error: Could not make request");
                        }
                    });
                } else if (treadlyNetworkResponse.response.optString("status").equals("ok")) {
                    VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.InviteServiceHelper.5.2
                        @Override // java.lang.Runnable
                        public void run() {
                            InviteServiceHelperRequestBasicUsersPost.this.onResponse(null);
                        }
                    });
                } else {
                    VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.InviteServiceHelper.5.3
                        @Override // java.lang.Runnable
                        public void run() {
                            InviteServiceHelperRequestBasicUsersPost.this.onResponse("Error: Could not make request");
                        }
                    });
                }
            }
        });
    }
}
