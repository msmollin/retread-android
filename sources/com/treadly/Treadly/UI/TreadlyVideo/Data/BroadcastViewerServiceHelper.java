package com.treadly.Treadly.UI.TreadlyVideo.Data;

import android.os.Handler;
import android.os.Looper;
import com.treadly.Treadly.Data.Model.TreadlyNetworkResponse;
import com.treadly.Treadly.Data.Model.UserComment;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.Data.Utility.RequestUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class BroadcastViewerServiceHelper {
    static final String SERVICE_URI_BASE = "https://env-staging.treadly.co:8080/vendor/tokbox/v1";
    static final String SERVICE_URI_ROUTE_BROADCAST_COMMENT_GET = "https://env-staging.treadly.co:8080/vendor/tokbox/v1/broadcast/comment/get";
    static final String SERVICE_URI_ROUTE_BROADCAST_VIEWER_GET = "https://env-staging.treadly.co:8080/vendor/tokbox/v1/broadcast/viewer/get";
    static final String SERVICE_URI_ROUTE_BROADCAST_VIEWER_UPDATE = "https://env-staging.treadly.co:8080/vendor/tokbox/v1/broadcast/viewer/update";
    static final String keyAvatar = "avatar";
    static final String keyBroadcastAvailable = "broadcast_available";
    static final String keyBroadcastCommentList = "broadcast_comment_list";
    static final String keyBroadcastId = "broadcast_id";
    static final String keyBroadcastViewerList = "broadcast_viewer_list";
    static final String keyComment = "comment";
    static final String keyCreatedAt = "created_at";
    static final String keyName = "name";
    static final String keyUserId = "user_id";

    /* loaded from: classes2.dex */
    public static class BroadcastViewerServiceHelperAdapter implements BroadcastViewerServiceHelperListener {
        @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastViewerServiceHelper.BroadcastViewerServiceHelperListener
        public void onGetViewerComments(String str, List<UserComment> list) {
        }

        @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastViewerServiceHelper.BroadcastViewerServiceHelperListener
        public void onGetViewers(String str, List<UserInfo> list) {
        }

        @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastViewerServiceHelper.BroadcastViewerServiceHelperListener
        public void onViewerUpdate(String str, List<String> list, Boolean bool) {
        }
    }

    /* loaded from: classes2.dex */
    public interface BroadcastViewerServiceHelperListener {
        void onGetViewerComments(String str, List<UserComment> list);

        void onGetViewers(String str, List<UserInfo> list);

        void onViewerUpdate(String str, List<String> list, Boolean bool);
    }

    public static void viewerUpdate(String str, String str2, final BroadcastViewerServiceHelperListener broadcastViewerServiceHelperListener) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyBroadcastId, str);
            jSONObject.put("user_id", str2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestUtil.shared.postJson(SERVICE_URI_ROUTE_BROADCAST_VIEWER_UPDATE, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastViewerServiceHelper.1
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
                if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                    BroadcastViewerServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastViewerServiceHelper.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            BroadcastViewerServiceHelperListener.this.onViewerUpdate("Error: Could not make request", null, null);
                        }
                    });
                }
                JSONObject jSONObject2 = treadlyNetworkResponse.response;
                JSONArray optJSONArray = jSONObject2.optJSONArray(BroadcastViewerServiceHelper.keyBroadcastViewerList);
                final Boolean valueOf = Boolean.valueOf(jSONObject2.optBoolean(BroadcastViewerServiceHelper.keyBroadcastAvailable));
                if (optJSONArray != null && valueOf != null) {
                    final ArrayList arrayList = new ArrayList();
                    for (int i = 0; i < optJSONArray.length(); i++) {
                        JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                        if (optJSONObject.toString() != null) {
                            arrayList.add(optJSONObject.toString());
                        }
                    }
                    BroadcastViewerServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastViewerServiceHelper.1.2
                        @Override // java.lang.Runnable
                        public void run() {
                            BroadcastViewerServiceHelperListener.this.onViewerUpdate(null, arrayList, valueOf);
                        }
                    });
                    return;
                }
                BroadcastViewerServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastViewerServiceHelper.1.3
                    @Override // java.lang.Runnable
                    public void run() {
                        BroadcastViewerServiceHelperListener.this.onViewerUpdate("Error: Could not parse results", null, null);
                    }
                });
            }
        });
    }

    public static void getViewers(String str, final BroadcastViewerServiceHelperListener broadcastViewerServiceHelperListener) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyBroadcastId, str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestUtil.shared.postJson(SERVICE_URI_ROUTE_BROADCAST_VIEWER_GET, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastViewerServiceHelper.2
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
                if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                    BroadcastViewerServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastViewerServiceHelper.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            BroadcastViewerServiceHelperListener.this.onGetViewers("Error: Could not make request", null);
                        }
                    });
                }
                JSONArray optJSONArray = treadlyNetworkResponse.response.optJSONArray(BroadcastViewerServiceHelper.keyBroadcastViewerList);
                if (optJSONArray != null) {
                    final ArrayList arrayList = new ArrayList();
                    for (int i = 0; i < optJSONArray.length(); i++) {
                        JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                        if (optJSONObject.toString() != null) {
                            arrayList.add(new UserInfo(optJSONObject.optString("user_id"), optJSONObject.optString("name"), ""));
                        }
                    }
                    BroadcastViewerServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastViewerServiceHelper.2.2
                        @Override // java.lang.Runnable
                        public void run() {
                            BroadcastViewerServiceHelperListener.this.onGetViewers(null, arrayList);
                        }
                    });
                    return;
                }
                BroadcastViewerServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastViewerServiceHelper.2.3
                    @Override // java.lang.Runnable
                    public void run() {
                        BroadcastViewerServiceHelperListener.this.onGetViewers("Error: Could not parse results", null);
                    }
                });
            }
        });
    }

    public static void getViewerComments(String str, final BroadcastViewerServiceHelperListener broadcastViewerServiceHelperListener) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyBroadcastId, str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestUtil.shared.postJson(SERVICE_URI_ROUTE_BROADCAST_COMMENT_GET, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastViewerServiceHelper.3
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
                if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                    BroadcastViewerServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastViewerServiceHelper.3.1
                        @Override // java.lang.Runnable
                        public void run() {
                            BroadcastViewerServiceHelperListener.this.onGetViewerComments("Error: Could not make request", null);
                        }
                    });
                }
                JSONArray optJSONArray = treadlyNetworkResponse.response.optJSONArray(BroadcastViewerServiceHelper.keyBroadcastCommentList);
                if (optJSONArray != null) {
                    final ArrayList arrayList = new ArrayList();
                    for (int i = 0; i < optJSONArray.length(); i++) {
                        JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                        if (optJSONObject.toString() != null) {
                            String optString = optJSONObject.optString("user_id");
                            String optString2 = optJSONObject.optString("name");
                            String optString3 = optJSONObject.optString("comment");
                            String optString4 = optJSONObject.optString(BroadcastViewerServiceHelper.keyCreatedAt);
                            String optString5 = optJSONObject.optString("avatar");
                            Date date = BroadcastViewerServiceHelper.getDate(optString4, "yyyy-MM-dd HH:mm:ss.SSS");
                            UserInfo userInfo = new UserInfo(optString, optString2, "");
                            userInfo.avatarPath = optString5;
                            arrayList.add(new UserComment(userInfo, optString3, date));
                        }
                    }
                    BroadcastViewerServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastViewerServiceHelper.3.2
                        @Override // java.lang.Runnable
                        public void run() {
                            BroadcastViewerServiceHelperListener.this.onGetViewerComments(null, arrayList);
                        }
                    });
                    return;
                }
                BroadcastViewerServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastViewerServiceHelper.3.3
                    @Override // java.lang.Runnable
                    public void run() {
                        BroadcastViewerServiceHelperListener.this.onGetViewerComments("Error: Could not parse results", null);
                    }
                });
            }
        });
    }

    static Date getDate(String str, String str2) {
        if (str == null) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(str2, Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return simpleDateFormat.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static void runOnMain(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
