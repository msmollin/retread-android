package com.treadly.client.lib.sdk.Managers;

import com.facebook.common.util.UriUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class RequestUtil {
    private static final String CHARSET = "UTF-8";
    private static final String CRLF = "\r\n";
    private static final Logger LOGGER = Logger.getLogger(DeviceManager.class.getName());
    private static final OkHttpClient client = new OkHttpClient();

    RequestUtil() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static JSONObject postJson(String str, JSONObject jSONObject) {
        return doJsonRequest(str, jSONObject, "POST", 0);
    }

    static JSONObject postUrlEncoded(String str, HashMap<String, String> hashMap) {
        return doUrlEncodedRequest(str, hashMap, "POST");
    }

    static boolean postJsonFileDownload(String str, JSONObject jSONObject, File file) {
        return doJSONRequestFileDownload(str, jSONObject, "POST", file);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static JSONObject postJsonFileUpload(String str, JSONObject jSONObject, File file) {
        return doJsonRequestFileUpload(str, jSONObject, file);
    }

    static JSONObject putJson(String str, JSONObject jSONObject) {
        return doJsonRequest(str, jSONObject, "PUT", 0);
    }

    static JSONObject getJson(String str, int i) {
        return doJsonRequest(str, null, "GET", i);
    }

    static JSONObject getJsonWithError(String str, int i) {
        return doJsonRequestWithErrorResponse(str, null, "GET", i);
    }

    static JSONObject getJson(String str) {
        return doJsonRequest(str, null, "GET", 0);
    }

    private static JSONObject doJsonRequest(String str, JSONObject jSONObject, String str2, int i) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestMethod(str2);
            httpURLConnection.setAllowUserInteraction(false);
            if (i > 0) {
                httpURLConnection.setConnectTimeout(i);
            }
            if (jSONObject != null) {
                httpURLConnection.setDoOutput(true);
                PrintStream printStream = new PrintStream(httpURLConnection.getOutputStream());
                printStream.print(jSONObject.toString());
                printStream.close();
            }
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode < 200 || responseCode >= 400) {
                return null;
            }
            String convertStreamToString = convertStreamToString(httpURLConnection.getInputStream());
            if (convertStreamToString.startsWith("[")) {
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("unspent_transactions", new JSONArray(convertStreamToString));
                return jSONObject2;
            }
            return new JSONObject(convertStreamToString);
        } catch (Error e) {
            Logger logger = LOGGER;
            Level level = Level.WARNING;
            logger.log(level, "Error inside " + new Object() { // from class: com.treadly.client.lib.sdk.Managers.RequestUtil.2
            }.getClass().getEnclosingMethod().getName() + ":\n" + e.toString());
            return null;
        } catch (Exception e2) {
            Logger logger2 = LOGGER;
            Level level2 = Level.WARNING;
            logger2.log(level2, "Exception inside " + new Object() { // from class: com.treadly.client.lib.sdk.Managers.RequestUtil.1
            }.getClass().getEnclosingMethod().getName() + ":\n" + e2.toString());
            return null;
        }
    }

    private static JSONObject doUrlEncodedRequest(String str, HashMap<String, String> hashMap, String str2) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestMethod(str2);
            httpURLConnection.setAllowUserInteraction(false);
            if (hashMap != null) {
                httpURLConnection.setDoOutput(true);
                PrintStream printStream = new PrintStream(httpURLConnection.getOutputStream());
                printStream.print(getQuery(hashMap));
                printStream.close();
            }
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode < 200 || responseCode >= 400) {
                return null;
            }
            String convertStreamToString = convertStreamToString(httpURLConnection.getInputStream());
            if (convertStreamToString.startsWith("[")) {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("unspent_transactions", new JSONArray(convertStreamToString));
                return jSONObject;
            }
            return new JSONObject(convertStreamToString);
        } catch (Error e) {
            Logger logger = LOGGER;
            Level level = Level.WARNING;
            logger.log(level, "Error inside " + new Object() { // from class: com.treadly.client.lib.sdk.Managers.RequestUtil.4
            }.getClass().getEnclosingMethod().getName() + ":\n" + e.toString());
            return null;
        } catch (Exception e2) {
            Logger logger2 = LOGGER;
            Level level2 = Level.WARNING;
            logger2.log(level2, "Exception inside " + new Object() { // from class: com.treadly.client.lib.sdk.Managers.RequestUtil.3
            }.getClass().getEnclosingMethod().getName() + ":\n" + e2.toString());
            return null;
        }
    }

    private static boolean doJSONRequestFileDownload(String str, JSONObject jSONObject, String str2, File file) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestMethod(str2);
            httpURLConnection.setAllowUserInteraction(false);
            if (jSONObject != null) {
                httpURLConnection.setDoOutput(true);
                PrintStream printStream = new PrintStream(httpURLConnection.getOutputStream());
                printStream.print(jSONObject.toString());
                printStream.close();
            }
            InputStream inputStream = httpURLConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(file, false);
            if (inputStream == null) {
                return false;
            }
            byte[] bArr = new byte[8192];
            while (true) {
                int read = inputStream.read(bArr);
                if (read != -1) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    fileOutputStream.close();
                    return true;
                }
            }
        } catch (Error e) {
            Logger logger = LOGGER;
            Level level = Level.WARNING;
            logger.log(level, "Error inside " + new Object() { // from class: com.treadly.client.lib.sdk.Managers.RequestUtil.6
            }.getClass().getEnclosingMethod().getName() + ":\n" + e.toString());
            return false;
        } catch (Exception e2) {
            Logger logger2 = LOGGER;
            Level level2 = Level.WARNING;
            logger2.log(level2, "Exception inside " + new Object() { // from class: com.treadly.client.lib.sdk.Managers.RequestUtil.5
            }.getClass().getEnclosingMethod().getName() + ":\n" + e2.toString());
            return false;
        }
    }

    private static JSONObject doJsonRequestFileUpload(String str, JSONObject jSONObject, File file) {
        String obj;
        try {
            MultipartBody.Builder addFormDataPart = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart(UriUtil.LOCAL_FILE_SCHEME, file.getName(), RequestBody.create(MediaType.parse("text/csv"), file));
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                String optString = jSONObject.optString(next, null);
                if (optString != null) {
                    addFormDataPart.addFormDataPart(next, optString);
                }
            }
            Response execute = client.newCall(new Request.Builder().url(str).post(addFormDataPart.build()).build()).execute();
            if (execute == null || !execute.isSuccessful() || execute.body() == null || (obj = execute.body().toString()) == null) {
                return null;
            }
            PrintStream printStream = System.out;
            printStream.println("LGK :: Response String : " + obj);
            return new JSONObject(obj);
        } catch (Error e) {
            Logger logger = LOGGER;
            Level level = Level.WARNING;
            logger.log(level, "Error inside " + new Object() { // from class: com.treadly.client.lib.sdk.Managers.RequestUtil.8
            }.getClass().getEnclosingMethod().getName() + ":\n" + e.toString());
            return null;
        } catch (Exception e2) {
            Logger logger2 = LOGGER;
            Level level2 = Level.WARNING;
            logger2.log(level2, "Exception inside " + new Object() { // from class: com.treadly.client.lib.sdk.Managers.RequestUtil.7
            }.getClass().getEnclosingMethod().getName() + ":\n" + e2.toString());
            return null;
        }
    }

    private static String getQuery(HashMap<String, String> hashMap) throws UnsupportedEncodingException {
        try {
            StringBuilder sb = new StringBuilder();
            boolean z = true;
            for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                if (z) {
                    z = false;
                } else {
                    sb.append("&");
                }
                sb.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                sb.append("=");
                sb.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
            return sb.toString();
        } catch (Error e) {
            Logger logger = LOGGER;
            Level level = Level.WARNING;
            logger.log(level, "Error inside " + new Object() { // from class: com.treadly.client.lib.sdk.Managers.RequestUtil.10
            }.getClass().getEnclosingMethod().getName() + ":\n" + e.toString());
            return null;
        } catch (Exception e2) {
            Logger logger2 = LOGGER;
            Level level2 = Level.WARNING;
            logger2.log(level2, "Exception inside " + new Object() { // from class: com.treadly.client.lib.sdk.Managers.RequestUtil.9
            }.getClass().getEnclosingMethod().getName() + ":\n" + e2.toString());
            return null;
        }
    }

    private static String convertStreamToString(InputStream inputStream) {
        Scanner useDelimiter = new Scanner(inputStream).useDelimiter("\\A");
        return useDelimiter.hasNext() ? useDelimiter.next() : "";
    }

    private static JSONObject doJsonRequestWithErrorResponse(String str, JSONObject jSONObject, String str2, int i) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestMethod(str2);
            httpURLConnection.setAllowUserInteraction(false);
            if (i > 0) {
                httpURLConnection.setConnectTimeout(i);
            }
            if (jSONObject != null) {
                httpURLConnection.setDoOutput(true);
                PrintStream printStream = new PrintStream(httpURLConnection.getOutputStream());
                printStream.print(jSONObject.toString());
                printStream.close();
            }
            if (httpURLConnection.getResponseCode() >= 400) {
                return new JSONObject(convertStreamToString(httpURLConnection.getErrorStream()));
            }
            return new JSONObject(convertStreamToString(httpURLConnection.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
