package com.treadly.Treadly.Data.Utility;

import androidx.recyclerview.widget.ItemTouchHelper;
import com.bumptech.glide.load.Key;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.Data.Model.TreadlyNetworkResponse;
import com.treadly.Treadly.Data.Utility.RequestUtil;
import com.treadly.client.lib.sdk.Managers.DeviceManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class RequestUtil {
    static final String CRYPTOCOMPARE_API_URI = "https://min-api.cryptocompare.com/data/price?fsym=%s&tsyms=%s";
    static final String NODE_JSONRPC_BLOCK_HEIGHT_KEY = "blockHeight";
    static final String NODE_JSONRPC_BLOCK_NUMBER_KEY = "blockNumber";
    static final String NODE_JSONRPC_DATA_KEY = "data";
    static final String NODE_JSONRPC_ERROR_KEY = "error";
    static final String NODE_JSONRPC_ERROR_MESSAGE_KEY = "error_message";
    static final String NODE_JSONRPC_HASH_KEY = "hash";
    static final String NODE_JSONRPC_HEIGHT_KEY = "height";
    static final String NODE_JSONRPC_MESSAGE_ID_KEY = "id";
    static final String NODE_JSONRPC_METHOD_KEY = "method";
    static final String NODE_JSONRPC_PARAMS_KEY = "params";
    static final String NODE_JSONRPC_RESULT_KEY = "result";
    static final String NODE_JSONRPC_TO_KEY = "to";
    static final String NODE_JSONRPC_VERSION_KEY = "jsonrpc";
    protected static final String NODE_JSONRPC_VERSION_VALUE = "2.0";
    static final String NODE_REQUEST_SUCCESS_VALUE = "success";
    static int REQUEST_ERROR = -1;
    private static final int WEB_REQUEST_THREAD_POOL_THREADS = 16;
    ExecutorService webThreadPool = Executors.newFixedThreadPool(16);
    private static final Logger LOGGER = Logger.getLogger(DeviceManager.class.getName());
    public static final RequestUtil shared = new RequestUtil();
    private static final OkHttpClient client = new OkHttpClient();

    /* loaded from: classes2.dex */
    public interface RequestUtilListener {
        void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException;
    }

    private RequestUtil() {
    }

    public void postJson(final String str, final JSONObject jSONObject, final RequestUtilListener requestUtilListener) {
        this.webThreadPool.execute(new Runnable() { // from class: com.treadly.Treadly.Data.Utility.RequestUtil.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    requestUtilListener.onResponse(RequestUtil.postJson(str, jSONObject));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void postJson(final String str, final JSONObject jSONObject, final JSONObject jSONObject2, final RequestUtilListener requestUtilListener) {
        this.webThreadPool.execute(new Runnable() { // from class: com.treadly.Treadly.Data.Utility.RequestUtil.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    requestUtilListener.onResponse(RequestUtil.postJson(str, jSONObject, jSONObject2));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void postJsonFileUpload(final String str, final JSONObject jSONObject, final JSONObject jSONObject2, final File file, final RequestUtilListener requestUtilListener) {
        this.webThreadPool.execute(new Runnable() { // from class: com.treadly.Treadly.Data.Utility.RequestUtil.3
            @Override // java.lang.Runnable
            public void run() {
                try {
                    requestUtilListener.onResponse(RequestUtil.postJsonFileUpload(str, jSONObject, jSONObject2, file));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    static TreadlyNetworkResponse postJsonFileUpload(String str, JSONObject jSONObject, JSONObject jSONObject2, File file) {
        return doJsonRequestFileUpload(str, jSONObject, jSONObject2, file);
    }

    static TreadlyNetworkResponse postJson(String str, JSONObject jSONObject) {
        return doJsonRequest(str, jSONObject, "POST", 0, null);
    }

    static TreadlyNetworkResponse postJson(String str, JSONObject jSONObject, JSONObject jSONObject2) {
        return doJsonRequest(str, jSONObject, "POST", 0, jSONObject2);
    }

    static TreadlyNetworkResponse postUrlEncoded(String str, HashMap<String, String> hashMap) {
        return doUrlEncodedRequest(str, hashMap, "POST");
    }

    static boolean postJsonFileDownload(String str, JSONObject jSONObject, File file) {
        return doJSONRequestFileDownload(str, jSONObject, "POST", file);
    }

    static TreadlyNetworkResponse putJson(String str, JSONObject jSONObject) {
        return doJsonRequest(str, jSONObject, "PUT", 0, null);
    }

    public void getJson(final String str, final int i, final RequestUtilListener requestUtilListener) {
        this.webThreadPool.execute(new Runnable() { // from class: com.treadly.Treadly.Data.Utility.RequestUtil.4
            @Override // java.lang.Runnable
            public void run() {
                try {
                    requestUtilListener.onResponse(RequestUtil.getJson(str, i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getJson(final String str, final RequestUtilListener requestUtilListener) {
        this.webThreadPool.execute(new Runnable() { // from class: com.treadly.Treadly.Data.Utility.RequestUtil.5
            @Override // java.lang.Runnable
            public void run() {
                try {
                    requestUtilListener.onResponse(RequestUtil.getJson(str));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getJson(final String str, final JSONObject jSONObject, final RequestUtilListener requestUtilListener) {
        this.webThreadPool.execute(new Runnable() { // from class: com.treadly.Treadly.Data.Utility.RequestUtil.6
            @Override // java.lang.Runnable
            public void run() {
                try {
                    requestUtilListener.onResponse(RequestUtil.getJson(str, jSONObject));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void postOctet(final String str, final byte[] bArr, final RequestUtilListener requestUtilListener) {
        this.webThreadPool.execute(new Runnable() { // from class: com.treadly.Treadly.Data.Utility.-$$Lambda$RequestUtil$FYvbWyxw_tKOwYtPhyZiz4IQ8FM
            @Override // java.lang.Runnable
            public final void run() {
                RequestUtil.lambda$postOctet$0(RequestUtil.RequestUtilListener.this, str, bArr);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$postOctet$0(RequestUtilListener requestUtilListener, String str, byte[] bArr) {
        try {
            requestUtilListener.onResponse(postOctet(str, bArr));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    static TreadlyNetworkResponse getJson(String str, int i, JSONObject jSONObject) {
        return doJsonRequest(str, null, "GET", i, jSONObject);
    }

    static TreadlyNetworkResponse getJson(String str, int i) {
        return doJsonRequest(str, null, "GET", i, null);
    }

    static TreadlyNetworkResponse getJsonWithError(String str, int i) {
        return doJsonRequestWithErrorResponse(str, null, "GET", i);
    }

    static TreadlyNetworkResponse getJson(String str, JSONObject jSONObject) {
        return doJsonRequest(str, null, "GET", 0, jSONObject);
    }

    static TreadlyNetworkResponse getJson(String str) {
        return doJsonRequest(str, null, "GET", 0, null);
    }

    static TreadlyNetworkResponse postOctet(String str, byte[] bArr) {
        return doOctetRequest(str, bArr, "POST", 0);
    }

    private static TreadlyNetworkResponse doJsonRequest(String str, JSONObject jSONObject, String str2, int i, JSONObject jSONObject2) {
        JSONObject jSONObject3;
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestMethod(str2);
            httpURLConnection.setAllowUserInteraction(false);
            if (jSONObject2 != null) {
                Iterator<String> keys = jSONObject2.keys();
                while (keys.hasNext()) {
                    String next = keys.next();
                    String optString = jSONObject2.optString(next);
                    if (optString != null) {
                        httpURLConnection.setRequestProperty(next, optString);
                    }
                }
            }
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
            if (responseCode >= 200 && responseCode < 400) {
                jSONObject3 = new JSONObject(convertStreamToString(httpURLConnection.getInputStream()));
            } else {
                jSONObject3 = new JSONObject(convertStreamToString(httpURLConnection.getErrorStream()));
            }
            return new TreadlyNetworkResponse(responseCode, jSONObject3);
        } catch (Error e) {
            Logger logger = LOGGER;
            Level level = Level.WARNING;
            logger.log(level, "Error inside " + new Object() { // from class: com.treadly.Treadly.Data.Utility.RequestUtil.8
            }.getClass().getEnclosingMethod().getName() + ":\n" + e.toString());
            return new TreadlyNetworkResponse();
        } catch (Exception e2) {
            Logger logger2 = LOGGER;
            Level level2 = Level.WARNING;
            logger2.log(level2, "Exception inside " + new Object() { // from class: com.treadly.Treadly.Data.Utility.RequestUtil.7
            }.getClass().getEnclosingMethod().getName() + ":\n" + e2.toString());
            return new TreadlyNetworkResponse();
        }
    }

    private static TreadlyNetworkResponse doJsonRequestFileUpload(String str, JSONObject jSONObject, JSONObject jSONObject2, File file) {
        try {
            final FileInputStream fileInputStream = new FileInputStream(file);
            final long length = file.length();
            MultipartBody.Builder addFormDataPart = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("video_file", file.getName(), new RequestBody() { // from class: com.treadly.Treadly.Data.Utility.RequestUtil.9
                @Override // okhttp3.RequestBody
                public MediaType contentType() {
                    return MediaType.parse("video");
                }

                @Override // okhttp3.RequestBody
                public long contentLength() throws IOException {
                    return length;
                }

                @Override // okhttp3.RequestBody
                public void writeTo(BufferedSink bufferedSink) throws IOException {
                    byte[] bArr = new byte[65536];
                    while (true) {
                        int read = fileInputStream.read(bArr);
                        if (read == -1) {
                            return;
                        }
                        bufferedSink.write(bArr, 0, read);
                    }
                }
            });
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                String optString = jSONObject.optString(next, null);
                if (optString != null) {
                    addFormDataPart.addFormDataPart(next, optString);
                }
            }
            MultipartBody build = addFormDataPart.build();
            Headers.Builder builder = new Headers.Builder();
            Iterator<String> keys2 = jSONObject2.keys();
            while (keys2.hasNext()) {
                String next2 = keys2.next();
                String optString2 = jSONObject2.optString(next2);
                if (optString2 != null) {
                    builder.add(next2, optString2);
                }
            }
            Response execute = client.newCall(new Request.Builder().url(str).post(build).headers(builder.build()).build()).execute();
            if (execute == null || !execute.isSuccessful() || execute.body() == null) {
                return null;
            }
            String string = execute.body().string();
            if (string == null && string.contains("error")) {
                return new TreadlyNetworkResponse(-1, null);
            }
            return new TreadlyNetworkResponse(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION, new JSONObject(string));
        } catch (Error e) {
            Logger logger = LOGGER;
            Level level = Level.WARNING;
            logger.log(level, "Error inside " + new Object() { // from class: com.treadly.Treadly.Data.Utility.RequestUtil.11
            }.getClass().getEnclosingMethod().getName() + ":\n" + e.toString());
            return null;
        } catch (Exception e2) {
            Logger logger2 = LOGGER;
            Level level2 = Level.WARNING;
            logger2.log(level2, "Exception inside " + new Object() { // from class: com.treadly.Treadly.Data.Utility.RequestUtil.10
            }.getClass().getEnclosingMethod().getName() + ":\n" + e2.toString());
            return null;
        }
    }

    private static TreadlyNetworkResponse doUrlEncodedRequest(String str, HashMap<String, String> hashMap, String str2) {
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
            return new TreadlyNetworkResponse(responseCode, (responseCode < 200 || responseCode >= 400) ? null : new JSONObject(convertStreamToString(httpURLConnection.getInputStream())));
        } catch (Error e) {
            Logger logger = LOGGER;
            Level level = Level.WARNING;
            logger.log(level, "Error inside " + new Object() { // from class: com.treadly.Treadly.Data.Utility.RequestUtil.13
            }.getClass().getEnclosingMethod().getName() + ":\n" + e.toString());
            return new TreadlyNetworkResponse();
        } catch (Exception e2) {
            Logger logger2 = LOGGER;
            Level level2 = Level.WARNING;
            logger2.log(level2, "Exception inside " + new Object() { // from class: com.treadly.Treadly.Data.Utility.RequestUtil.12
            }.getClass().getEnclosingMethod().getName() + ":\n" + e2.toString());
            return new TreadlyNetworkResponse();
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
            logger.log(level, "Error inside " + new Object() { // from class: com.treadly.Treadly.Data.Utility.RequestUtil.15
            }.getClass().getEnclosingMethod().getName() + ":\n" + e.toString());
            return false;
        } catch (Exception e2) {
            Logger logger2 = LOGGER;
            Level level2 = Level.WARNING;
            logger2.log(level2, "Exception inside " + new Object() { // from class: com.treadly.Treadly.Data.Utility.RequestUtil.14
            }.getClass().getEnclosingMethod().getName() + ":\n" + e2.toString());
            return false;
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
                sb.append(URLEncoder.encode(entry.getKey(), Key.STRING_CHARSET_NAME));
                sb.append("=");
                sb.append(URLEncoder.encode(entry.getValue(), Key.STRING_CHARSET_NAME));
            }
            return sb.toString();
        } catch (Error e) {
            Logger logger = LOGGER;
            Level level = Level.WARNING;
            logger.log(level, "Error inside " + new Object() { // from class: com.treadly.Treadly.Data.Utility.RequestUtil.17
            }.getClass().getEnclosingMethod().getName() + ":\n" + e.toString());
            return null;
        } catch (Exception e2) {
            Logger logger2 = LOGGER;
            Level level2 = Level.WARNING;
            logger2.log(level2, "Exception inside " + new Object() { // from class: com.treadly.Treadly.Data.Utility.RequestUtil.16
            }.getClass().getEnclosingMethod().getName() + ":\n" + e2.toString());
            return null;
        }
    }

    private static String convertStreamToString(InputStream inputStream) {
        Scanner useDelimiter = new Scanner(inputStream).useDelimiter("\\A");
        return useDelimiter.hasNext() ? useDelimiter.next() : "";
    }

    private static TreadlyNetworkResponse doJsonRequestWithErrorResponse(String str, JSONObject jSONObject, String str2, int i) {
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
            if (httpURLConnection.getResponseCode() >= 400) {
                return new TreadlyNetworkResponse(responseCode, new JSONObject(convertStreamToString(httpURLConnection.getErrorStream())));
            }
            return new TreadlyNetworkResponse(responseCode, new JSONObject(convertStreamToString(httpURLConnection.getInputStream())));
        } catch (Exception e) {
            e.printStackTrace();
            return new TreadlyNetworkResponse();
        }
    }

    private static TreadlyNetworkResponse doOctetRequest(String str, byte[] bArr, String str2, int i) {
        JSONObject jSONObject;
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            httpURLConnection.setRequestProperty("Authorization", TreadlyServiceManager.getInstance().tokenInfo.token);
            httpURLConnection.setRequestProperty("Content-Type", "application/octet-stream");
            httpURLConnection.setRequestMethod(str2);
            httpURLConnection.setAllowUserInteraction(false);
            if (i > 0) {
                httpURLConnection.setConnectTimeout(i);
            }
            if (bArr != null) {
                httpURLConnection.setDoOutput(true);
                PrintStream printStream = new PrintStream(httpURLConnection.getOutputStream());
                printStream.write(bArr);
                printStream.close();
            }
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode >= 200 && responseCode < 400) {
                jSONObject = new JSONObject(convertStreamToString(httpURLConnection.getInputStream()));
            } else {
                jSONObject = new JSONObject(convertStreamToString(httpURLConnection.getErrorStream()));
            }
            return new TreadlyNetworkResponse(responseCode, jSONObject);
        } catch (Error e) {
            Logger logger = LOGGER;
            Level level = Level.WARNING;
            logger.log(level, "Error inside " + new Object() { // from class: com.treadly.Treadly.Data.Utility.RequestUtil.19
            }.getClass().getEnclosingMethod().getName() + ":\n" + e.toString());
            return new TreadlyNetworkResponse();
        } catch (Exception e2) {
            Logger logger2 = LOGGER;
            Level level2 = Level.WARNING;
            logger2.log(level2, "Exception inside " + new Object() { // from class: com.treadly.Treadly.Data.Utility.RequestUtil.18
            }.getClass().getEnclosingMethod().getName() + ":\n" + e2.toString());
            return new TreadlyNetworkResponse();
        }
    }
}
