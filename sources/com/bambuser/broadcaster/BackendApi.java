package com.bambuser.broadcaster;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.util.Pair;
import com.facebook.internal.logging.monitor.MonitorLogServerProtocol;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONObject;

/* loaded from: classes.dex */
public final class BackendApi {
    private static final String API_TICKET_URL = "https://api.bambuser.com/file/ticket.json";
    static final String CDN_BENCHMARKPAYLOADS_URL = "https://cdn.bambuser.net/benchmarkPayloads";
    static final String CDN_BROADCASTTICKET_URL = "https://cdn.bambuser.net/broadcastTickets";
    static final String CDN_CONTENTREQUESTS_URL = "https://cdn.bambuser.net/contentRequests";
    static final String CDN_UPLOADTICKET_URL = "https://cdn.bambuser.net/uploadTickets";
    private static final String LOGTAG = "BackendApi";
    public static final String TICKET_FILE_AUTHOR = "author";
    public static final String TICKET_FILE_CREATED = "created";
    public static final String TICKET_FILE_CUSTOM_DATA = "custom_data";
    public static final String TICKET_FILE_NAME = "filename";
    public static final String TICKET_FILE_POS_ACCURACY = "position_accuracy";
    public static final String TICKET_FILE_POS_LAT = "lat";
    public static final String TICKET_FILE_POS_LON = "lon";
    public static final String TICKET_FILE_TITLE = "title";
    public static final String TICKET_FILE_TYPE = "type";

    private BackendApi() {
    }

    static String getUploadTicket(Context context, Map<String, String> map, String str) {
        if (map == null || !map.containsKey("type") || !map.containsKey(TICKET_FILE_NAME)) {
            throw new IllegalArgumentException("type and filename parameters are mandatory");
        }
        if (str == null || str.length() <= 0) {
            throw new IllegalArgumentException("basicAuth String missing");
        }
        map.put("platform", "Android");
        map.put("platform_version", String.valueOf(Build.VERSION.SDK_INT));
        map.put("manufacturer", Build.MANUFACTURER);
        map.put(MonitorLogServerProtocol.PARAM_DEVICE_MODEL, DeviceInfoHandler.getModel());
        map.put("client_version", Broadcaster.getClientVersion(context));
        HashMap hashMap = new HashMap();
        hashMap.put("Authorization", "Basic " + str);
        return post(API_TICKET_URL, map, hashMap);
    }

    public static Pair<Integer, String> getUploadTicketForApplicationId(Context context, Map<String, String> map, String str) {
        if (map == null || !map.containsKey("type") || !map.containsKey(TICKET_FILE_NAME)) {
            throw new IllegalArgumentException("type and filename parameters are mandatory");
        }
        if (str == null || str.length() <= 0) {
            throw new IllegalArgumentException("applicationId String missing");
        }
        map.put("platform", "Android");
        map.put("platform_version", String.valueOf(Build.VERSION.SDK_INT));
        map.put("manufacturer", Build.MANUFACTURER);
        map.put(MonitorLogServerProtocol.PARAM_DEVICE_MODEL, DeviceInfoHandler.getModel());
        map.put("client_version", Broadcaster.getClientVersion(context));
        HashMap hashMap = new HashMap();
        hashMap.put("Accept", "application/vnd.bambuser.cdn.v1+json");
        hashMap.put("X-Bambuser-ClientVersion", Broadcaster.getClientVersion(context));
        hashMap.put("X-Bambuser-ClientPlatform", "Android " + Build.VERSION.RELEASE);
        hashMap.put("X-Bambuser-ApplicationId", str);
        JSONObject jSONObject = new JSONObject();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            try {
                jSONObject.put(entry.getKey(), entry.getValue());
            } catch (Exception unused) {
            }
        }
        return jsonEncodedRequest(CDN_UPLOADTICKET_URL, jSONObject, "POST", hashMap);
    }

    static String get(String str, Map<String, String> map) {
        return urlEncodedRequest(str, map, null, null);
    }

    static String post(String str, Map<String, String> map) {
        return urlEncodedRequest(str, map, "POST", null);
    }

    static String post(String str, Map<String, String> map, Map<String, String> map2) {
        return urlEncodedRequest(str, map, "POST", map2);
    }

    private static String urlEncodedRequest(String str, Map<String, String> map, String str2, Map<String, String> map2) {
        boolean z = str2 == null || str2.equalsIgnoreCase("GET");
        if (z && map != null && map.size() > 0) {
            str = str + "?" + getEncodedQuery(map);
        }
        byte[] bArr = null;
        if (map2 == null) {
            map2 = new HashMap<>();
        }
        map2.put("Accept-Charset", "utf-8");
        if (!z) {
            if (map != null && !map.isEmpty()) {
                bArr = getEncodedQuery(map).getBytes();
            }
            map2.put("Content-Type", "application/x-www-form-urlencoded");
        }
        return (String) request(str, str2, map2, bArr).second;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Pair<Integer, String> jsonEncodedRequest(String str, JSONObject jSONObject, String str2, Map<String, String> map) {
        if (map == null) {
            map = new HashMap<>();
        }
        byte[] bArr = null;
        if (jSONObject != null) {
            bArr = jSONObject.toString().getBytes();
            map.put("Content-Type", "application/json");
        }
        return request(str, str2, map, bArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Pair<Integer, String> get(String str) {
        return request(str, null, null, null);
    }

    private static Pair<Integer, String> request(String str, String str2, Map<String, String> map, byte[] bArr) {
        int i;
        String read;
        String str3 = "";
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            if (httpURLConnection instanceof HttpsURLConnection) {
                ((HttpsURLConnection) httpURLConnection).setSSLSocketFactory(ModernTlsSocketFactory.getInstance());
            }
            if (map != null) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    httpURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(20000);
            httpURLConnection.setUseCaches(false);
            if (str2 != null && !str2.equalsIgnoreCase("GET")) {
                httpURLConnection.setRequestMethod(str2);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                if (bArr != null) {
                    outputStream.write(bArr);
                }
                outputStream.close();
            }
            httpURLConnection.connect();
            i = httpURLConnection.getResponseCode();
            try {
                if (i == 200) {
                    read = read(httpURLConnection.getInputStream());
                } else {
                    read = read(httpURLConnection.getErrorStream());
                }
                str3 = read;
                if (i < 200 || i >= 300) {
                    Log.w(LOGTAG, "response code: " + i);
                }
                httpURLConnection.disconnect();
            } catch (Exception e) {
                e = e;
                Log.w(LOGTAG, "Exception in communication: " + e);
                return new Pair<>(Integer.valueOf(i), str3);
            }
        } catch (Exception e2) {
            e = e2;
            i = 0;
        }
        return new Pair<>(Integer.valueOf(i), str3);
    }

    private static String read(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        scanner.useDelimiter("\\A");
        String next = scanner.hasNext() ? scanner.next() : "";
        scanner.close();
        return next;
    }

    private static String getEncodedQuery(Map<String, String> map) {
        Uri.Builder builder = new Uri.Builder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue());
        }
        return builder.build().getEncodedQuery();
    }
}
