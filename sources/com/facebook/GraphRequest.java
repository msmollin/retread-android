package com.facebook;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Pair;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.Key;
import com.facebook.GraphRequestBatch;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.internal.AttributionIdentifiers;
import com.facebook.internal.InternalSettings;
import com.facebook.internal.Logger;
import com.facebook.internal.NativeProtocol;
import com.facebook.internal.ServerProtocol;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import com.facebook.internal.instrument.crashshield.CrashShieldHandler;
import com.treadly.Treadly.Data.Managers.TreadlyEventHelper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class GraphRequest {
    private static final String ACCEPT_LANGUAGE_HEADER = "Accept-Language";
    public static final String ACCESS_TOKEN_PARAM = "access_token";
    private static final String ATTACHED_FILES_PARAM = "attached_files";
    private static final String ATTACHMENT_FILENAME_PREFIX = "file";
    private static final String BATCH_APP_ID_PARAM = "batch_app_id";
    private static final String BATCH_BODY_PARAM = "body";
    private static final String BATCH_ENTRY_DEPENDS_ON_PARAM = "depends_on";
    private static final String BATCH_ENTRY_NAME_PARAM = "name";
    private static final String BATCH_ENTRY_OMIT_RESPONSE_ON_SUCCESS_PARAM = "omit_response_on_success";
    private static final String BATCH_METHOD_PARAM = "method";
    private static final String BATCH_PARAM = "batch";
    private static final String BATCH_RELATIVE_URL_PARAM = "relative_url";
    private static final String CAPTION_PARAM = "caption";
    private static final String CONTENT_ENCODING_HEADER = "Content-Encoding";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String DEBUG_KEY = "__debug__";
    private static final String DEBUG_MESSAGES_KEY = "messages";
    private static final String DEBUG_MESSAGE_KEY = "message";
    private static final String DEBUG_MESSAGE_LINK_KEY = "link";
    private static final String DEBUG_MESSAGE_TYPE_KEY = "type";
    private static final String DEBUG_PARAM = "debug";
    private static final String DEBUG_SEVERITY_INFO = "info";
    private static final String DEBUG_SEVERITY_WARNING = "warning";
    public static final String FIELDS_PARAM = "fields";
    private static final String FORMAT_JSON = "json";
    private static final String FORMAT_PARAM = "format";
    private static final String GRAPH_PATH_FORMAT = "%s/%s";
    private static final String ISO_8601_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final int MAXIMUM_BATCH_SIZE = 50;
    private static final String ME = "me";
    private static final String MIME_BOUNDARY;
    private static final String MY_FRIENDS = "me/friends";
    private static final String MY_PHOTOS = "me/photos";
    private static final String PICTURE_PARAM = "picture";
    private static final String SDK_ANDROID = "android";
    private static final String SDK_PARAM = "sdk";
    private static final String SEARCH = "search";
    public static final String TAG = "GraphRequest";
    private static final String USER_AGENT_BASE = "FBAndroidSDK";
    private static final String USER_AGENT_HEADER = "User-Agent";
    private static final String VIDEOS_SUFFIX = "/videos";
    private static String defaultBatchApplicationId;
    private static volatile String userAgent;
    private static Pattern versionPattern = Pattern.compile("^/?v\\d+\\.\\d+/(.*)");
    @Nullable
    private AccessToken accessToken;
    private String batchEntryDependsOn;
    private String batchEntryName;
    private boolean batchEntryOmitResultOnSuccess;
    private Callback callback;
    private JSONObject graphObject;
    private String graphPath;
    private HttpMethod httpMethod;
    private String overriddenURL;
    private Bundle parameters;
    private boolean skipClientToken;
    private Object tag;
    private String version;

    /* loaded from: classes.dex */
    public interface Callback {
        void onCompleted(GraphResponse graphResponse);
    }

    /* loaded from: classes.dex */
    public interface GraphJSONArrayCallback {
        void onCompleted(JSONArray jSONArray, GraphResponse graphResponse);
    }

    /* loaded from: classes.dex */
    public interface GraphJSONObjectCallback {
        void onCompleted(JSONObject jSONObject, GraphResponse graphResponse);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public interface KeyValueSerializer {
        void writeString(String str, String str2) throws IOException;
    }

    /* loaded from: classes.dex */
    public interface OnProgressCallback extends Callback {
        void onProgress(long j, long j2);
    }

    private static String getDefaultPhotoPathIfNull(String str) {
        return str == null ? "me/photos" : str;
    }

    static {
        char[] charArray = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        StringBuilder sb = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        int nextInt = secureRandom.nextInt(11) + 30;
        for (int i = 0; i < nextInt; i++) {
            sb.append(charArray[secureRandom.nextInt(charArray.length)]);
        }
        MIME_BOUNDARY = sb.toString();
    }

    public GraphRequest() {
        this(null, null, null, null, null);
    }

    public GraphRequest(@Nullable AccessToken accessToken, String str) {
        this(accessToken, str, null, null, null);
    }

    public GraphRequest(@Nullable AccessToken accessToken, String str, Bundle bundle, HttpMethod httpMethod) {
        this(accessToken, str, bundle, httpMethod, null);
    }

    public GraphRequest(@Nullable AccessToken accessToken, String str, Bundle bundle, HttpMethod httpMethod, Callback callback) {
        this(accessToken, str, bundle, httpMethod, callback, null);
    }

    public GraphRequest(@Nullable AccessToken accessToken, String str, Bundle bundle, HttpMethod httpMethod, Callback callback, String str2) {
        this.batchEntryOmitResultOnSuccess = true;
        this.skipClientToken = false;
        this.accessToken = accessToken;
        this.graphPath = str;
        this.version = str2;
        setCallback(callback);
        setHttpMethod(httpMethod);
        if (bundle != null) {
            this.parameters = new Bundle(bundle);
        } else {
            this.parameters = new Bundle();
        }
        if (this.version == null) {
            this.version = FacebookSdk.getGraphApiVersion();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public GraphRequest(@Nullable AccessToken accessToken, URL url) {
        this.batchEntryOmitResultOnSuccess = true;
        this.skipClientToken = false;
        this.accessToken = accessToken;
        this.overriddenURL = url.toString();
        setHttpMethod(HttpMethod.GET);
        this.parameters = new Bundle();
    }

    public static GraphRequest newDeleteObjectRequest(@Nullable AccessToken accessToken, String str, Callback callback) {
        return new GraphRequest(accessToken, str, null, HttpMethod.DELETE, callback);
    }

    public static GraphRequest newMeRequest(@Nullable AccessToken accessToken, final GraphJSONObjectCallback graphJSONObjectCallback) {
        return new GraphRequest(accessToken, ME, null, null, new Callback() { // from class: com.facebook.GraphRequest.1
            @Override // com.facebook.GraphRequest.Callback
            public void onCompleted(GraphResponse graphResponse) {
                if (GraphJSONObjectCallback.this != null) {
                    GraphJSONObjectCallback.this.onCompleted(graphResponse.getJSONObject(), graphResponse);
                }
            }
        });
    }

    public static GraphRequest newPostRequest(@Nullable AccessToken accessToken, String str, JSONObject jSONObject, Callback callback) {
        GraphRequest graphRequest = new GraphRequest(accessToken, str, null, HttpMethod.POST, callback);
        graphRequest.setGraphObject(jSONObject);
        return graphRequest;
    }

    public static GraphRequest newMyFriendsRequest(@Nullable AccessToken accessToken, final GraphJSONArrayCallback graphJSONArrayCallback) {
        return new GraphRequest(accessToken, MY_FRIENDS, null, null, new Callback() { // from class: com.facebook.GraphRequest.2
            @Override // com.facebook.GraphRequest.Callback
            public void onCompleted(GraphResponse graphResponse) {
                if (GraphJSONArrayCallback.this != null) {
                    JSONObject jSONObject = graphResponse.getJSONObject();
                    GraphJSONArrayCallback.this.onCompleted(jSONObject != null ? jSONObject.optJSONArray("data") : null, graphResponse);
                }
            }
        });
    }

    public static GraphRequest newGraphPathRequest(@Nullable AccessToken accessToken, String str, Callback callback) {
        return new GraphRequest(accessToken, str, null, null, callback);
    }

    public static GraphRequest newPlacesSearchRequest(@Nullable AccessToken accessToken, Location location, int i, int i2, String str, final GraphJSONArrayCallback graphJSONArrayCallback) {
        if (location == null && Utility.isNullOrEmpty(str)) {
            throw new FacebookException("Either location or searchText must be specified.");
        }
        Bundle bundle = new Bundle(5);
        bundle.putString("type", "place");
        bundle.putInt("limit", i2);
        if (location != null) {
            bundle.putString("center", String.format(Locale.US, "%f,%f", Double.valueOf(location.getLatitude()), Double.valueOf(location.getLongitude())));
            bundle.putInt(TreadlyEventHelper.keyDistance, i);
        }
        if (!Utility.isNullOrEmpty(str)) {
            bundle.putString("q", str);
        }
        return new GraphRequest(accessToken, "search", bundle, HttpMethod.GET, new Callback() { // from class: com.facebook.GraphRequest.3
            @Override // com.facebook.GraphRequest.Callback
            public void onCompleted(GraphResponse graphResponse) {
                if (GraphJSONArrayCallback.this != null) {
                    JSONObject jSONObject = graphResponse.getJSONObject();
                    GraphJSONArrayCallback.this.onCompleted(jSONObject != null ? jSONObject.optJSONArray("data") : null, graphResponse);
                }
            }
        });
    }

    public static GraphRequest newUploadPhotoRequest(@Nullable AccessToken accessToken, String str, Bitmap bitmap, String str2, Bundle bundle, Callback callback) {
        String defaultPhotoPathIfNull = getDefaultPhotoPathIfNull(str);
        Bundle bundle2 = new Bundle();
        if (bundle != null) {
            bundle2.putAll(bundle);
        }
        bundle2.putParcelable("picture", bitmap);
        if (str2 != null && !str2.isEmpty()) {
            bundle2.putString("caption", str2);
        }
        return new GraphRequest(accessToken, defaultPhotoPathIfNull, bundle2, HttpMethod.POST, callback);
    }

    public static GraphRequest newUploadPhotoRequest(@Nullable AccessToken accessToken, String str, File file, String str2, Bundle bundle, Callback callback) throws FileNotFoundException {
        String defaultPhotoPathIfNull = getDefaultPhotoPathIfNull(str);
        ParcelFileDescriptor open = ParcelFileDescriptor.open(file, 268435456);
        Bundle bundle2 = new Bundle();
        if (bundle != null) {
            bundle2.putAll(bundle);
        }
        bundle2.putParcelable("picture", open);
        if (str2 != null && !str2.isEmpty()) {
            bundle2.putString("caption", str2);
        }
        return new GraphRequest(accessToken, defaultPhotoPathIfNull, bundle2, HttpMethod.POST, callback);
    }

    public static GraphRequest newUploadPhotoRequest(@Nullable AccessToken accessToken, String str, Uri uri, String str2, Bundle bundle, Callback callback) throws FileNotFoundException {
        String defaultPhotoPathIfNull = getDefaultPhotoPathIfNull(str);
        if (Utility.isFileUri(uri)) {
            return newUploadPhotoRequest(accessToken, defaultPhotoPathIfNull, new File(uri.getPath()), str2, bundle, callback);
        }
        if (!Utility.isContentUri(uri)) {
            throw new FacebookException("The photo Uri must be either a file:// or content:// Uri");
        }
        Bundle bundle2 = new Bundle();
        if (bundle != null) {
            bundle2.putAll(bundle);
        }
        bundle2.putParcelable("picture", uri);
        if (str2 != null && !str2.isEmpty()) {
            bundle2.putString("caption", str2);
        }
        return new GraphRequest(accessToken, defaultPhotoPathIfNull, bundle2, HttpMethod.POST, callback);
    }

    public static GraphRequest newCustomAudienceThirdPartyIdRequest(@Nullable AccessToken accessToken, Context context, String str, Callback callback) {
        String androidAdvertiserId;
        if (str == null && accessToken != null) {
            str = accessToken.getApplicationId();
        }
        if (str == null) {
            str = Utility.getMetadataApplicationId(context);
        }
        if (str == null) {
            throw new FacebookException("Facebook App ID cannot be determined");
        }
        String str2 = str + "/custom_audience_third_party_id";
        AttributionIdentifiers attributionIdentifiers = AttributionIdentifiers.getAttributionIdentifiers(context);
        Bundle bundle = new Bundle();
        if (accessToken == null) {
            if (attributionIdentifiers == null) {
                throw new FacebookException("There is no access token and attribution identifiers could not be retrieved");
            }
            if (attributionIdentifiers.getAttributionId() != null) {
                androidAdvertiserId = attributionIdentifiers.getAttributionId();
            } else {
                androidAdvertiserId = attributionIdentifiers.getAndroidAdvertiserId();
            }
            if (attributionIdentifiers.getAttributionId() != null) {
                bundle.putString("udid", androidAdvertiserId);
            }
        }
        if (FacebookSdk.getLimitEventAndDataUsage(context) || (attributionIdentifiers != null && attributionIdentifiers.isTrackingLimited())) {
            bundle.putString("limit_event_usage", AppEventsConstants.EVENT_PARAM_VALUE_YES);
        }
        return new GraphRequest(accessToken, str2, bundle, HttpMethod.GET, callback);
    }

    public static GraphRequest newCustomAudienceThirdPartyIdRequest(@Nullable AccessToken accessToken, Context context, Callback callback) {
        return newCustomAudienceThirdPartyIdRequest(accessToken, context, null, callback);
    }

    public final JSONObject getGraphObject() {
        return this.graphObject;
    }

    public final void setGraphObject(JSONObject jSONObject) {
        this.graphObject = jSONObject;
    }

    public final String getGraphPath() {
        return this.graphPath;
    }

    public final void setGraphPath(String str) {
        this.graphPath = str;
    }

    public final HttpMethod getHttpMethod() {
        return this.httpMethod;
    }

    public final void setHttpMethod(HttpMethod httpMethod) {
        if (this.overriddenURL != null && httpMethod != HttpMethod.GET) {
            throw new FacebookException("Can't change HTTP method on request with overridden URL.");
        }
        if (httpMethod == null) {
            httpMethod = HttpMethod.GET;
        }
        this.httpMethod = httpMethod;
    }

    public final String getVersion() {
        return this.version;
    }

    public final void setVersion(String str) {
        this.version = str;
    }

    public final void setSkipClientToken(boolean z) {
        this.skipClientToken = z;
    }

    public final Bundle getParameters() {
        return this.parameters;
    }

    public final void setParameters(Bundle bundle) {
        this.parameters = bundle;
    }

    @Nullable
    public final AccessToken getAccessToken() {
        return this.accessToken;
    }

    public final void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public final String getBatchEntryName() {
        return this.batchEntryName;
    }

    public final void setBatchEntryName(String str) {
        this.batchEntryName = str;
    }

    public final String getBatchEntryDependsOn() {
        return this.batchEntryDependsOn;
    }

    public final void setBatchEntryDependsOn(String str) {
        this.batchEntryDependsOn = str;
    }

    public final boolean getBatchEntryOmitResultOnSuccess() {
        return this.batchEntryOmitResultOnSuccess;
    }

    public final void setBatchEntryOmitResultOnSuccess(boolean z) {
        this.batchEntryOmitResultOnSuccess = z;
    }

    public static final String getDefaultBatchApplicationId() {
        return defaultBatchApplicationId;
    }

    public static final void setDefaultBatchApplicationId(String str) {
        defaultBatchApplicationId = str;
    }

    public final Callback getCallback() {
        return this.callback;
    }

    public final void setCallback(final Callback callback) {
        if (FacebookSdk.isLoggingBehaviorEnabled(LoggingBehavior.GRAPH_API_DEBUG_INFO) || FacebookSdk.isLoggingBehaviorEnabled(LoggingBehavior.GRAPH_API_DEBUG_WARNING)) {
            this.callback = new Callback() { // from class: com.facebook.GraphRequest.4
                @Override // com.facebook.GraphRequest.Callback
                public void onCompleted(GraphResponse graphResponse) {
                    JSONObject jSONObject = graphResponse.getJSONObject();
                    JSONObject optJSONObject = jSONObject != null ? jSONObject.optJSONObject(GraphRequest.DEBUG_KEY) : null;
                    JSONArray optJSONArray = optJSONObject != null ? optJSONObject.optJSONArray(GraphRequest.DEBUG_MESSAGES_KEY) : null;
                    if (optJSONArray != null) {
                        for (int i = 0; i < optJSONArray.length(); i++) {
                            JSONObject optJSONObject2 = optJSONArray.optJSONObject(i);
                            String optString = optJSONObject2 != null ? optJSONObject2.optString("message") : null;
                            String optString2 = optJSONObject2 != null ? optJSONObject2.optString("type") : null;
                            String optString3 = optJSONObject2 != null ? optJSONObject2.optString("link") : null;
                            if (optString != null && optString2 != null) {
                                LoggingBehavior loggingBehavior = LoggingBehavior.GRAPH_API_DEBUG_INFO;
                                if (optString2.equals(GraphRequest.DEBUG_SEVERITY_WARNING)) {
                                    loggingBehavior = LoggingBehavior.GRAPH_API_DEBUG_WARNING;
                                }
                                if (!Utility.isNullOrEmpty(optString3)) {
                                    optString = optString + " Link: " + optString3;
                                }
                                Logger.log(loggingBehavior, GraphRequest.TAG, optString);
                            }
                        }
                    }
                    if (callback != null) {
                        callback.onCompleted(graphResponse);
                    }
                }
            };
        } else {
            this.callback = callback;
        }
    }

    public final void setTag(Object obj) {
        this.tag = obj;
    }

    public final Object getTag() {
        return this.tag;
    }

    public final GraphResponse executeAndWait() {
        return executeAndWait(this);
    }

    public final GraphRequestAsyncTask executeAsync() {
        return executeBatchAsync(this);
    }

    public static HttpURLConnection toHttpConnection(GraphRequest... graphRequestArr) {
        return toHttpConnection(Arrays.asList(graphRequestArr));
    }

    public static HttpURLConnection toHttpConnection(Collection<GraphRequest> collection) {
        Validate.notEmptyAndContainsNoNulls(collection, "requests");
        return toHttpConnection(new GraphRequestBatch(collection));
    }

    public static HttpURLConnection toHttpConnection(GraphRequestBatch graphRequestBatch) {
        URL url;
        HttpURLConnection createConnection;
        validateFieldsParamForGetRequests(graphRequestBatch);
        try {
            if (graphRequestBatch.size() == 1) {
                url = new URL(graphRequestBatch.get(0).getUrlForSingleRequest());
            } else {
                url = new URL(ServerProtocol.getGraphUrlBase());
            }
            HttpURLConnection httpURLConnection = null;
            try {
                createConnection = createConnection(url);
            } catch (IOException | JSONException e) {
                e = e;
            }
            try {
                serializeToUrlConnection(graphRequestBatch, createConnection);
                return createConnection;
            } catch (IOException | JSONException e2) {
                e = e2;
                httpURLConnection = createConnection;
                Utility.disconnectQuietly(httpURLConnection);
                throw new FacebookException("could not construct request body", e);
            }
        } catch (MalformedURLException e3) {
            throw new FacebookException("could not construct URL for request", e3);
        }
    }

    public static GraphResponse executeAndWait(GraphRequest graphRequest) {
        List<GraphResponse> executeBatchAndWait = executeBatchAndWait(graphRequest);
        if (executeBatchAndWait == null || executeBatchAndWait.size() != 1) {
            throw new FacebookException("invalid state: expected a single response");
        }
        return executeBatchAndWait.get(0);
    }

    public static List<GraphResponse> executeBatchAndWait(GraphRequest... graphRequestArr) {
        Validate.notNull(graphRequestArr, "requests");
        return executeBatchAndWait(Arrays.asList(graphRequestArr));
    }

    public static List<GraphResponse> executeBatchAndWait(Collection<GraphRequest> collection) {
        return executeBatchAndWait(new GraphRequestBatch(collection));
    }

    public static List<GraphResponse> executeBatchAndWait(GraphRequestBatch graphRequestBatch) {
        HttpURLConnection httpConnection;
        Validate.notEmptyAndContainsNoNulls(graphRequestBatch, "requests");
        HttpURLConnection httpURLConnection = null;
        try {
            try {
                httpConnection = toHttpConnection(graphRequestBatch);
            } catch (Exception e) {
                List<GraphResponse> constructErrorResponses = GraphResponse.constructErrorResponses(graphRequestBatch.getRequests(), null, new FacebookException(e));
                runCallbacks(graphRequestBatch, constructErrorResponses);
                Utility.disconnectQuietly(null);
                return constructErrorResponses;
            }
        } catch (Throwable th) {
            th = th;
        }
        try {
            List<GraphResponse> executeConnectionAndWait = executeConnectionAndWait(httpConnection, graphRequestBatch);
            Utility.disconnectQuietly(httpConnection);
            return executeConnectionAndWait;
        } catch (Throwable th2) {
            th = th2;
            httpURLConnection = httpConnection;
            Utility.disconnectQuietly(httpURLConnection);
            throw th;
        }
    }

    public static GraphRequestAsyncTask executeBatchAsync(GraphRequest... graphRequestArr) {
        Validate.notNull(graphRequestArr, "requests");
        return executeBatchAsync(Arrays.asList(graphRequestArr));
    }

    public static GraphRequestAsyncTask executeBatchAsync(Collection<GraphRequest> collection) {
        return executeBatchAsync(new GraphRequestBatch(collection));
    }

    public static GraphRequestAsyncTask executeBatchAsync(GraphRequestBatch graphRequestBatch) {
        Validate.notEmptyAndContainsNoNulls(graphRequestBatch, "requests");
        GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequestAsyncTask(graphRequestBatch);
        graphRequestAsyncTask.executeOnExecutor(FacebookSdk.getExecutor(), new Void[0]);
        return graphRequestAsyncTask;
    }

    public static List<GraphResponse> executeConnectionAndWait(HttpURLConnection httpURLConnection, Collection<GraphRequest> collection) {
        return executeConnectionAndWait(httpURLConnection, new GraphRequestBatch(collection));
    }

    public static List<GraphResponse> executeConnectionAndWait(HttpURLConnection httpURLConnection, GraphRequestBatch graphRequestBatch) {
        List<GraphResponse> fromHttpConnection = GraphResponse.fromHttpConnection(httpURLConnection, graphRequestBatch);
        Utility.disconnectQuietly(httpURLConnection);
        int size = graphRequestBatch.size();
        if (size != fromHttpConnection.size()) {
            throw new FacebookException(String.format(Locale.US, "Received %d responses while expecting %d", Integer.valueOf(fromHttpConnection.size()), Integer.valueOf(size)));
        }
        runCallbacks(graphRequestBatch, fromHttpConnection);
        AccessTokenManager.getInstance().extendAccessTokenIfNeeded();
        return fromHttpConnection;
    }

    public static GraphRequestAsyncTask executeConnectionAsync(HttpURLConnection httpURLConnection, GraphRequestBatch graphRequestBatch) {
        return executeConnectionAsync(null, httpURLConnection, graphRequestBatch);
    }

    public static GraphRequestAsyncTask executeConnectionAsync(Handler handler, HttpURLConnection httpURLConnection, GraphRequestBatch graphRequestBatch) {
        Validate.notNull(httpURLConnection, "connection");
        GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequestAsyncTask(httpURLConnection, graphRequestBatch);
        graphRequestBatch.setCallbackHandler(handler);
        graphRequestAsyncTask.executeOnExecutor(FacebookSdk.getExecutor(), new Void[0]);
        return graphRequestAsyncTask;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{Request: ");
        sb.append(" accessToken: ");
        sb.append(this.accessToken == null ? "null" : this.accessToken);
        sb.append(", graphPath: ");
        sb.append(this.graphPath);
        sb.append(", graphObject: ");
        sb.append(this.graphObject);
        sb.append(", httpMethod: ");
        sb.append(this.httpMethod);
        sb.append(", parameters: ");
        sb.append(this.parameters);
        sb.append("}");
        return sb.toString();
    }

    static void runCallbacks(final GraphRequestBatch graphRequestBatch, List<GraphResponse> list) {
        int size = graphRequestBatch.size();
        final ArrayList arrayList = new ArrayList();
        for (int i = 0; i < size; i++) {
            GraphRequest graphRequest = graphRequestBatch.get(i);
            if (graphRequest.callback != null) {
                arrayList.add(new Pair(graphRequest.callback, list.get(i)));
            }
        }
        if (arrayList.size() > 0) {
            Runnable runnable = new Runnable() { // from class: com.facebook.GraphRequest.5
                @Override // java.lang.Runnable
                public void run() {
                    if (CrashShieldHandler.isObjectCrashing(this)) {
                        return;
                    }
                    try {
                        Iterator it = arrayList.iterator();
                        while (it.hasNext()) {
                            Pair pair = (Pair) it.next();
                            ((Callback) pair.first).onCompleted((GraphResponse) pair.second);
                        }
                        for (GraphRequestBatch.Callback callback : graphRequestBatch.getCallbacks()) {
                            callback.onBatchCompleted(graphRequestBatch);
                        }
                    } catch (Throwable th) {
                        CrashShieldHandler.handleThrowable(th, this);
                    }
                }
            };
            Handler callbackHandler = graphRequestBatch.getCallbackHandler();
            if (callbackHandler == null) {
                runnable.run();
            } else {
                callbackHandler.post(runnable);
            }
        }
    }

    private static HttpURLConnection createConnection(URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty(USER_AGENT_HEADER, getUserAgent());
        httpURLConnection.setRequestProperty(ACCEPT_LANGUAGE_HEADER, Locale.getDefault().toString());
        httpURLConnection.setChunkedStreamingMode(0);
        return httpURLConnection;
    }

    private void addCommonParameters() {
        if (this.accessToken != null) {
            if (!this.parameters.containsKey("access_token")) {
                String token = this.accessToken.getToken();
                Logger.registerAccessToken(token);
                this.parameters.putString("access_token", token);
            }
        } else if (!this.skipClientToken && !this.parameters.containsKey("access_token")) {
            String applicationId = FacebookSdk.getApplicationId();
            String clientToken = FacebookSdk.getClientToken();
            if (!Utility.isNullOrEmpty(applicationId) && !Utility.isNullOrEmpty(clientToken)) {
                this.parameters.putString("access_token", applicationId + "|" + clientToken);
            } else {
                Utility.logd(TAG, "Warning: Request without access token missing application ID or client token.");
            }
        }
        this.parameters.putString("sdk", "android");
        this.parameters.putString(FORMAT_PARAM, FORMAT_JSON);
        if (FacebookSdk.isLoggingBehaviorEnabled(LoggingBehavior.GRAPH_API_DEBUG_INFO)) {
            this.parameters.putString("debug", DEBUG_SEVERITY_INFO);
        } else if (FacebookSdk.isLoggingBehaviorEnabled(LoggingBehavior.GRAPH_API_DEBUG_WARNING)) {
            this.parameters.putString("debug", DEBUG_SEVERITY_WARNING);
        }
    }

    private String appendParametersToBaseUrl(String str, Boolean bool) {
        if (bool.booleanValue() || this.httpMethod != HttpMethod.POST) {
            Uri.Builder buildUpon = Uri.parse(str).buildUpon();
            for (String str2 : this.parameters.keySet()) {
                Object obj = this.parameters.get(str2);
                if (obj == null) {
                    obj = "";
                }
                if (!isSupportedParameterType(obj)) {
                    if (this.httpMethod == HttpMethod.GET) {
                        throw new IllegalArgumentException(String.format(Locale.US, "Unsupported parameter type for GET request: %s", obj.getClass().getSimpleName()));
                    }
                } else {
                    buildUpon.appendQueryParameter(str2, parameterToString(obj).toString());
                }
            }
            return buildUpon.toString();
        }
        return str;
    }

    final String getRelativeUrlForBatchedRequest() {
        if (this.overriddenURL != null) {
            throw new FacebookException("Can't override URL for a batch request");
        }
        String format = String.format(GRAPH_PATH_FORMAT, ServerProtocol.getGraphUrlBase(), getGraphPathWithVersion());
        addCommonParameters();
        Uri parse = Uri.parse(appendParametersToBaseUrl(format, true));
        return String.format("%s?%s", parse.getPath(), parse.getQuery());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String getUrlForSingleRequest() {
        String graphUrlBase;
        if (this.overriddenURL != null) {
            return this.overriddenURL.toString();
        }
        if (getHttpMethod() == HttpMethod.POST && this.graphPath != null && this.graphPath.endsWith(VIDEOS_SUFFIX)) {
            graphUrlBase = ServerProtocol.getGraphVideoUrlBase();
        } else {
            graphUrlBase = ServerProtocol.getGraphUrlBase();
        }
        String format = String.format(GRAPH_PATH_FORMAT, graphUrlBase, getGraphPathWithVersion());
        addCommonParameters();
        return appendParametersToBaseUrl(format, false);
    }

    private String getGraphPathWithVersion() {
        return versionPattern.matcher(this.graphPath).matches() ? this.graphPath : String.format(GRAPH_PATH_FORMAT, this.version, this.graphPath);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class Attachment {
        private final GraphRequest request;
        private final Object value;

        public Attachment(GraphRequest graphRequest, Object obj) {
            this.request = graphRequest;
            this.value = obj;
        }

        public GraphRequest getRequest() {
            return this.request;
        }

        public Object getValue() {
            return this.value;
        }
    }

    private void serializeToBatch(JSONArray jSONArray, Map<String, Attachment> map) throws JSONException, IOException {
        JSONObject jSONObject = new JSONObject();
        if (this.batchEntryName != null) {
            jSONObject.put("name", this.batchEntryName);
            jSONObject.put(BATCH_ENTRY_OMIT_RESPONSE_ON_SUCCESS_PARAM, this.batchEntryOmitResultOnSuccess);
        }
        if (this.batchEntryDependsOn != null) {
            jSONObject.put(BATCH_ENTRY_DEPENDS_ON_PARAM, this.batchEntryDependsOn);
        }
        String relativeUrlForBatchedRequest = getRelativeUrlForBatchedRequest();
        jSONObject.put(BATCH_RELATIVE_URL_PARAM, relativeUrlForBatchedRequest);
        jSONObject.put("method", this.httpMethod);
        if (this.accessToken != null) {
            Logger.registerAccessToken(this.accessToken.getToken());
        }
        ArrayList arrayList = new ArrayList();
        for (String str : this.parameters.keySet()) {
            Object obj = this.parameters.get(str);
            if (isSupportedAttachmentType(obj)) {
                String format = String.format(Locale.ROOT, "%s%d", "file", Integer.valueOf(map.size()));
                arrayList.add(format);
                map.put(format, new Attachment(this, obj));
            }
        }
        if (!arrayList.isEmpty()) {
            jSONObject.put(ATTACHED_FILES_PARAM, TextUtils.join(",", arrayList));
        }
        if (this.graphObject != null) {
            final ArrayList arrayList2 = new ArrayList();
            processGraphObject(this.graphObject, relativeUrlForBatchedRequest, new KeyValueSerializer() { // from class: com.facebook.GraphRequest.6
                @Override // com.facebook.GraphRequest.KeyValueSerializer
                public void writeString(String str2, String str3) throws IOException {
                    arrayList2.add(String.format(Locale.US, "%s=%s", str2, URLEncoder.encode(str3, Key.STRING_CHARSET_NAME)));
                }
            });
            jSONObject.put(BATCH_BODY_PARAM, TextUtils.join("&", arrayList2));
        }
        jSONArray.put(jSONObject);
    }

    private static boolean hasOnProgressCallbacks(GraphRequestBatch graphRequestBatch) {
        for (GraphRequestBatch.Callback callback : graphRequestBatch.getCallbacks()) {
            if (callback instanceof GraphRequestBatch.OnProgressCallback) {
                return true;
            }
        }
        Iterator<GraphRequest> it = graphRequestBatch.iterator();
        while (it.hasNext()) {
            if (it.next().getCallback() instanceof OnProgressCallback) {
                return true;
            }
        }
        return false;
    }

    private static void setConnectionContentType(HttpURLConnection httpURLConnection, boolean z) {
        if (z) {
            httpURLConnection.setRequestProperty(CONTENT_TYPE_HEADER, "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty(CONTENT_ENCODING_HEADER, "gzip");
            return;
        }
        httpURLConnection.setRequestProperty(CONTENT_TYPE_HEADER, getMimeContentType());
    }

    private static boolean isGzipCompressible(GraphRequestBatch graphRequestBatch) {
        Iterator<GraphRequest> it = graphRequestBatch.iterator();
        while (it.hasNext()) {
            GraphRequest next = it.next();
            for (String str : next.parameters.keySet()) {
                if (isSupportedAttachmentType(next.parameters.get(str))) {
                    return false;
                }
            }
        }
        return true;
    }

    static final boolean shouldWarnOnMissingFieldsParam(GraphRequest graphRequest) {
        String version = graphRequest.getVersion();
        if (Utility.isNullOrEmpty(version)) {
            return true;
        }
        if (version.startsWith("v")) {
            version = version.substring(1);
        }
        String[] split = version.split("\\.");
        if (split.length < 2 || Integer.parseInt(split[0]) <= 2) {
            return Integer.parseInt(split[0]) >= 2 && Integer.parseInt(split[1]) >= 4;
        }
        return true;
    }

    static final void validateFieldsParamForGetRequests(GraphRequestBatch graphRequestBatch) {
        Iterator<GraphRequest> it = graphRequestBatch.iterator();
        while (it.hasNext()) {
            GraphRequest next = it.next();
            if (HttpMethod.GET.equals(next.getHttpMethod()) && shouldWarnOnMissingFieldsParam(next)) {
                Bundle parameters = next.getParameters();
                if (!parameters.containsKey("fields") || Utility.isNullOrEmpty(parameters.getString("fields"))) {
                    Logger.log(LoggingBehavior.DEVELOPER_ERRORS, 5, "Request", "starting with Graph API v2.4, GET requests for /%s should contain an explicit \"fields\" parameter.", next.getGraphPath());
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x00ce  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static final void serializeToUrlConnection(com.facebook.GraphRequestBatch r13, java.net.HttpURLConnection r14) throws java.io.IOException, org.json.JSONException {
        /*
            com.facebook.internal.Logger r6 = new com.facebook.internal.Logger
            com.facebook.LoggingBehavior r0 = com.facebook.LoggingBehavior.REQUESTS
            java.lang.String r1 = "Request"
            r6.<init>(r0, r1)
            int r2 = r13.size()
            boolean r5 = isGzipCompressible(r13)
            r0 = 0
            r1 = 1
            if (r2 != r1) goto L1c
            com.facebook.GraphRequest r3 = r13.get(r0)
            com.facebook.HttpMethod r3 = r3.httpMethod
            goto L1e
        L1c:
            com.facebook.HttpMethod r3 = com.facebook.HttpMethod.POST
        L1e:
            java.lang.String r4 = r3.name()
            r14.setRequestMethod(r4)
            setConnectionContentType(r14, r5)
            java.net.URL r4 = r14.getURL()
            java.lang.String r7 = "Request:\n"
            r6.append(r7)
            java.lang.String r7 = "Id"
            java.lang.String r8 = r13.getId()
            r6.appendKeyValue(r7, r8)
            java.lang.String r7 = "URL"
            r6.appendKeyValue(r7, r4)
            java.lang.String r7 = "Method"
            java.lang.String r8 = r14.getRequestMethod()
            r6.appendKeyValue(r7, r8)
            java.lang.String r7 = "User-Agent"
            java.lang.String r8 = "User-Agent"
            java.lang.String r8 = r14.getRequestProperty(r8)
            r6.appendKeyValue(r7, r8)
            java.lang.String r7 = "Content-Type"
            java.lang.String r8 = "Content-Type"
            java.lang.String r8 = r14.getRequestProperty(r8)
            r6.appendKeyValue(r7, r8)
            int r7 = r13.getTimeout()
            r14.setConnectTimeout(r7)
            int r7 = r13.getTimeout()
            r14.setReadTimeout(r7)
            com.facebook.HttpMethod r7 = com.facebook.HttpMethod.POST
            if (r3 != r7) goto L71
            r0 = r1
        L71:
            if (r0 != 0) goto L77
            r6.log()
            return
        L77:
            r14.setDoOutput(r1)
            r0 = 0
            java.io.BufferedOutputStream r1 = new java.io.BufferedOutputStream     // Catch: java.lang.Throwable -> Lca
            java.io.OutputStream r14 = r14.getOutputStream()     // Catch: java.lang.Throwable -> Lca
            r1.<init>(r14)     // Catch: java.lang.Throwable -> Lca
            if (r5 == 0) goto L8f
            java.util.zip.GZIPOutputStream r14 = new java.util.zip.GZIPOutputStream     // Catch: java.lang.Throwable -> L8c
            r14.<init>(r1)     // Catch: java.lang.Throwable -> L8c
            goto L90
        L8c:
            r13 = move-exception
            r14 = r1
            goto Lcc
        L8f:
            r14 = r1
        L90:
            boolean r0 = hasOnProgressCallbacks(r13)     // Catch: java.lang.Throwable -> Lc8
            if (r0 == 0) goto Lba
            com.facebook.ProgressNoopOutputStream r0 = new com.facebook.ProgressNoopOutputStream     // Catch: java.lang.Throwable -> Lc8
            android.os.Handler r1 = r13.getCallbackHandler()     // Catch: java.lang.Throwable -> Lc8
            r0.<init>(r1)     // Catch: java.lang.Throwable -> Lc8
            r8 = 0
            r7 = r13
            r9 = r2
            r10 = r4
            r11 = r0
            r12 = r5
            processRequest(r7, r8, r9, r10, r11, r12)     // Catch: java.lang.Throwable -> Lc8
            int r1 = r0.getMaxProgress()     // Catch: java.lang.Throwable -> Lc8
            java.util.Map r10 = r0.getProgressMap()     // Catch: java.lang.Throwable -> Lc8
            com.facebook.ProgressOutputStream r0 = new com.facebook.ProgressOutputStream     // Catch: java.lang.Throwable -> Lc8
            long r11 = (long) r1     // Catch: java.lang.Throwable -> Lc8
            r7 = r0
            r8 = r14
            r9 = r13
            r7.<init>(r8, r9, r10, r11)     // Catch: java.lang.Throwable -> Lc8
            r14 = r0
        Lba:
            r0 = r13
            r1 = r6
            r3 = r4
            r4 = r14
            processRequest(r0, r1, r2, r3, r4, r5)     // Catch: java.lang.Throwable -> Lc8
            r14.close()
            r6.log()
            return
        Lc8:
            r13 = move-exception
            goto Lcc
        Lca:
            r13 = move-exception
            r14 = r0
        Lcc:
            if (r14 == 0) goto Ld1
            r14.close()
        Ld1:
            throw r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.GraphRequest.serializeToUrlConnection(com.facebook.GraphRequestBatch, java.net.HttpURLConnection):void");
    }

    private static void processRequest(GraphRequestBatch graphRequestBatch, Logger logger, int i, URL url, OutputStream outputStream, boolean z) throws IOException, JSONException {
        Serializer serializer = new Serializer(outputStream, logger, z);
        if (i == 1) {
            GraphRequest graphRequest = graphRequestBatch.get(0);
            HashMap hashMap = new HashMap();
            for (String str : graphRequest.parameters.keySet()) {
                Object obj = graphRequest.parameters.get(str);
                if (isSupportedAttachmentType(obj)) {
                    hashMap.put(str, new Attachment(graphRequest, obj));
                }
            }
            if (logger != null) {
                logger.append("  Parameters:\n");
            }
            serializeParameters(graphRequest.parameters, serializer, graphRequest);
            if (logger != null) {
                logger.append("  Attachments:\n");
            }
            serializeAttachments(hashMap, serializer);
            if (graphRequest.graphObject != null) {
                processGraphObject(graphRequest.graphObject, url.getPath(), serializer);
                return;
            }
            return;
        }
        String batchAppId = getBatchAppId(graphRequestBatch);
        if (Utility.isNullOrEmpty(batchAppId)) {
            throw new FacebookException("App ID was not specified at the request or Settings.");
        }
        serializer.writeString(BATCH_APP_ID_PARAM, batchAppId);
        HashMap hashMap2 = new HashMap();
        serializeRequestsAsJSON(serializer, graphRequestBatch, hashMap2);
        if (logger != null) {
            logger.append("  Attachments:\n");
        }
        serializeAttachments(hashMap2, serializer);
    }

    private static boolean isMeRequest(String str) {
        Matcher matcher = versionPattern.matcher(str);
        if (matcher.matches()) {
            str = matcher.group(1);
        }
        return str.startsWith("me/") || str.startsWith("/me/");
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0029  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void processGraphObject(org.json.JSONObject r6, java.lang.String r7, com.facebook.GraphRequest.KeyValueSerializer r8) throws java.io.IOException {
        /*
            boolean r0 = isMeRequest(r7)
            r1 = 1
            r2 = 0
            if (r0 == 0) goto L1e
            java.lang.String r0 = ":"
            int r0 = r7.indexOf(r0)
            java.lang.String r3 = "?"
            int r7 = r7.indexOf(r3)
            r3 = 3
            if (r0 <= r3) goto L1e
            r3 = -1
            if (r7 == r3) goto L1c
            if (r0 >= r7) goto L1e
        L1c:
            r7 = r1
            goto L1f
        L1e:
            r7 = r2
        L1f:
            java.util.Iterator r0 = r6.keys()
        L23:
            boolean r3 = r0.hasNext()
            if (r3 == 0) goto L44
            java.lang.Object r3 = r0.next()
            java.lang.String r3 = (java.lang.String) r3
            java.lang.Object r4 = r6.opt(r3)
            if (r7 == 0) goto L3f
            java.lang.String r5 = "image"
            boolean r5 = r3.equalsIgnoreCase(r5)
            if (r5 == 0) goto L3f
            r5 = r1
            goto L40
        L3f:
            r5 = r2
        L40:
            processGraphObjectProperty(r3, r4, r8, r5)
            goto L23
        L44:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.GraphRequest.processGraphObject(org.json.JSONObject, java.lang.String, com.facebook.GraphRequest$KeyValueSerializer):void");
    }

    private static void processGraphObjectProperty(String str, Object obj, KeyValueSerializer keyValueSerializer, boolean z) throws IOException {
        Class<?> cls = obj.getClass();
        if (JSONObject.class.isAssignableFrom(cls)) {
            JSONObject jSONObject = (JSONObject) obj;
            if (z) {
                Iterator<String> keys = jSONObject.keys();
                while (keys.hasNext()) {
                    String next = keys.next();
                    processGraphObjectProperty(String.format("%s[%s]", str, next), jSONObject.opt(next), keyValueSerializer, z);
                }
            } else if (jSONObject.has("id")) {
                processGraphObjectProperty(str, jSONObject.optString("id"), keyValueSerializer, z);
            } else if (jSONObject.has("url")) {
                processGraphObjectProperty(str, jSONObject.optString("url"), keyValueSerializer, z);
            } else if (jSONObject.has(NativeProtocol.OPEN_GRAPH_CREATE_OBJECT_KEY)) {
                processGraphObjectProperty(str, jSONObject.toString(), keyValueSerializer, z);
            }
        } else if (JSONArray.class.isAssignableFrom(cls)) {
            JSONArray jSONArray = (JSONArray) obj;
            int length = jSONArray.length();
            for (int i = 0; i < length; i++) {
                processGraphObjectProperty(String.format(Locale.ROOT, "%s[%d]", str, Integer.valueOf(i)), jSONArray.opt(i), keyValueSerializer, z);
            }
        } else if (String.class.isAssignableFrom(cls) || Number.class.isAssignableFrom(cls) || Boolean.class.isAssignableFrom(cls)) {
            keyValueSerializer.writeString(str, obj.toString());
        } else if (Date.class.isAssignableFrom(cls)) {
            keyValueSerializer.writeString(str, new SimpleDateFormat(ISO_8601_FORMAT_STRING, Locale.US).format((Date) obj));
        }
    }

    private static void serializeParameters(Bundle bundle, Serializer serializer, GraphRequest graphRequest) throws IOException {
        for (String str : bundle.keySet()) {
            Object obj = bundle.get(str);
            if (isSupportedParameterType(obj)) {
                serializer.writeObject(str, obj, graphRequest);
            }
        }
    }

    private static void serializeAttachments(Map<String, Attachment> map, Serializer serializer) throws IOException {
        for (String str : map.keySet()) {
            Attachment attachment = map.get(str);
            if (isSupportedAttachmentType(attachment.getValue())) {
                serializer.writeObject(str, attachment.getValue(), attachment.getRequest());
            }
        }
    }

    private static void serializeRequestsAsJSON(Serializer serializer, Collection<GraphRequest> collection, Map<String, Attachment> map) throws JSONException, IOException {
        JSONArray jSONArray = new JSONArray();
        for (GraphRequest graphRequest : collection) {
            graphRequest.serializeToBatch(jSONArray, map);
        }
        serializer.writeRequestsAsJson(BATCH_PARAM, jSONArray, collection);
    }

    private static String getMimeContentType() {
        return String.format("multipart/form-data; boundary=%s", MIME_BOUNDARY);
    }

    private static String getUserAgent() {
        if (userAgent == null) {
            userAgent = String.format("%s.%s", USER_AGENT_BASE, FacebookSdkVersion.BUILD);
            String customUserAgent = InternalSettings.getCustomUserAgent();
            if (!Utility.isNullOrEmpty(customUserAgent)) {
                userAgent = String.format(Locale.ROOT, GRAPH_PATH_FORMAT, userAgent, customUserAgent);
            }
        }
        return userAgent;
    }

    private static String getBatchAppId(GraphRequestBatch graphRequestBatch) {
        String applicationId;
        if (!Utility.isNullOrEmpty(graphRequestBatch.getBatchApplicationId())) {
            return graphRequestBatch.getBatchApplicationId();
        }
        Iterator<GraphRequest> it = graphRequestBatch.iterator();
        while (it.hasNext()) {
            AccessToken accessToken = it.next().accessToken;
            if (accessToken != null && (applicationId = accessToken.getApplicationId()) != null) {
                return applicationId;
            }
        }
        if (!Utility.isNullOrEmpty(defaultBatchApplicationId)) {
            return defaultBatchApplicationId;
        }
        return FacebookSdk.getApplicationId();
    }

    private static boolean isSupportedAttachmentType(Object obj) {
        return (obj instanceof Bitmap) || (obj instanceof byte[]) || (obj instanceof Uri) || (obj instanceof ParcelFileDescriptor) || (obj instanceof ParcelableResourceWithMimeType);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isSupportedParameterType(Object obj) {
        return (obj instanceof String) || (obj instanceof Boolean) || (obj instanceof Number) || (obj instanceof Date);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String parameterToString(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        }
        if ((obj instanceof Boolean) || (obj instanceof Number)) {
            return obj.toString();
        }
        if (obj instanceof Date) {
            return new SimpleDateFormat(ISO_8601_FORMAT_STRING, Locale.US).format(obj);
        }
        throw new IllegalArgumentException("Unsupported parameter type.");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class Serializer implements KeyValueSerializer {
        private boolean firstWrite = true;
        private final Logger logger;
        private final OutputStream outputStream;
        private boolean useUrlEncode;

        public Serializer(OutputStream outputStream, Logger logger, boolean z) {
            this.useUrlEncode = false;
            this.outputStream = outputStream;
            this.logger = logger;
            this.useUrlEncode = z;
        }

        public void writeObject(String str, Object obj, GraphRequest graphRequest) throws IOException {
            if (this.outputStream instanceof RequestOutputStream) {
                ((RequestOutputStream) this.outputStream).setCurrentRequest(graphRequest);
            }
            if (GraphRequest.isSupportedParameterType(obj)) {
                writeString(str, GraphRequest.parameterToString(obj));
            } else if (obj instanceof Bitmap) {
                writeBitmap(str, (Bitmap) obj);
            } else if (obj instanceof byte[]) {
                writeBytes(str, (byte[]) obj);
            } else if (obj instanceof Uri) {
                writeContentUri(str, (Uri) obj, null);
            } else if (obj instanceof ParcelFileDescriptor) {
                writeFile(str, (ParcelFileDescriptor) obj, null);
            } else if (obj instanceof ParcelableResourceWithMimeType) {
                ParcelableResourceWithMimeType parcelableResourceWithMimeType = (ParcelableResourceWithMimeType) obj;
                Parcelable resource = parcelableResourceWithMimeType.getResource();
                String mimeType = parcelableResourceWithMimeType.getMimeType();
                if (resource instanceof ParcelFileDescriptor) {
                    writeFile(str, (ParcelFileDescriptor) resource, mimeType);
                } else if (resource instanceof Uri) {
                    writeContentUri(str, (Uri) resource, mimeType);
                } else {
                    throw getInvalidTypeError();
                }
            } else {
                throw getInvalidTypeError();
            }
        }

        private RuntimeException getInvalidTypeError() {
            return new IllegalArgumentException("value is not a supported type.");
        }

        public void writeRequestsAsJson(String str, JSONArray jSONArray, Collection<GraphRequest> collection) throws IOException, JSONException {
            if (!(this.outputStream instanceof RequestOutputStream)) {
                writeString(str, jSONArray.toString());
                return;
            }
            RequestOutputStream requestOutputStream = (RequestOutputStream) this.outputStream;
            writeContentDisposition(str, null, null);
            write("[", new Object[0]);
            int i = 0;
            for (GraphRequest graphRequest : collection) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                requestOutputStream.setCurrentRequest(graphRequest);
                if (i > 0) {
                    write(",%s", jSONObject.toString());
                } else {
                    write("%s", jSONObject.toString());
                }
                i++;
            }
            write("]", new Object[0]);
            if (this.logger != null) {
                this.logger.appendKeyValue("    " + str, jSONArray.toString());
            }
        }

        @Override // com.facebook.GraphRequest.KeyValueSerializer
        public void writeString(String str, String str2) throws IOException {
            writeContentDisposition(str, null, null);
            writeLine("%s", str2);
            writeRecordBoundary();
            if (this.logger != null) {
                Logger logger = this.logger;
                logger.appendKeyValue("    " + str, str2);
            }
        }

        public void writeBitmap(String str, Bitmap bitmap) throws IOException {
            writeContentDisposition(str, str, "image/png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, this.outputStream);
            writeLine("", new Object[0]);
            writeRecordBoundary();
            if (this.logger != null) {
                Logger logger = this.logger;
                logger.appendKeyValue("    " + str, "<Image>");
            }
        }

        public void writeBytes(String str, byte[] bArr) throws IOException {
            writeContentDisposition(str, str, "content/unknown");
            this.outputStream.write(bArr);
            writeLine("", new Object[0]);
            writeRecordBoundary();
            if (this.logger != null) {
                Logger logger = this.logger;
                logger.appendKeyValue("    " + str, String.format(Locale.ROOT, "<Data: %d>", Integer.valueOf(bArr.length)));
            }
        }

        public void writeContentUri(String str, Uri uri, String str2) throws IOException {
            int copyAndCloseInputStream;
            if (str2 == null) {
                str2 = "content/unknown";
            }
            writeContentDisposition(str, str, str2);
            if (this.outputStream instanceof ProgressNoopOutputStream) {
                ((ProgressNoopOutputStream) this.outputStream).addProgress(Utility.getContentSize(uri));
                copyAndCloseInputStream = 0;
            } else {
                copyAndCloseInputStream = Utility.copyAndCloseInputStream(FacebookSdk.getApplicationContext().getContentResolver().openInputStream(uri), this.outputStream) + 0;
            }
            writeLine("", new Object[0]);
            writeRecordBoundary();
            if (this.logger != null) {
                this.logger.appendKeyValue("    " + str, String.format(Locale.ROOT, "<Data: %d>", Integer.valueOf(copyAndCloseInputStream)));
            }
        }

        public void writeFile(String str, ParcelFileDescriptor parcelFileDescriptor, String str2) throws IOException {
            int copyAndCloseInputStream;
            if (str2 == null) {
                str2 = "content/unknown";
            }
            writeContentDisposition(str, str, str2);
            if (this.outputStream instanceof ProgressNoopOutputStream) {
                ((ProgressNoopOutputStream) this.outputStream).addProgress(parcelFileDescriptor.getStatSize());
                copyAndCloseInputStream = 0;
            } else {
                copyAndCloseInputStream = Utility.copyAndCloseInputStream(new ParcelFileDescriptor.AutoCloseInputStream(parcelFileDescriptor), this.outputStream) + 0;
            }
            writeLine("", new Object[0]);
            writeRecordBoundary();
            if (this.logger != null) {
                this.logger.appendKeyValue("    " + str, String.format(Locale.ROOT, "<Data: %d>", Integer.valueOf(copyAndCloseInputStream)));
            }
        }

        public void writeRecordBoundary() throws IOException {
            if (this.useUrlEncode) {
                this.outputStream.write("&".getBytes());
            } else {
                writeLine("--%s", GraphRequest.MIME_BOUNDARY);
            }
        }

        public void writeContentDisposition(String str, String str2, String str3) throws IOException {
            if (!this.useUrlEncode) {
                write("Content-Disposition: form-data; name=\"%s\"", str);
                if (str2 != null) {
                    write("; filename=\"%s\"", str2);
                }
                writeLine("", new Object[0]);
                if (str3 != null) {
                    writeLine("%s: %s", GraphRequest.CONTENT_TYPE_HEADER, str3);
                }
                writeLine("", new Object[0]);
                return;
            }
            this.outputStream.write(String.format("%s=", str).getBytes());
        }

        public void write(String str, Object... objArr) throws IOException {
            if (!this.useUrlEncode) {
                if (this.firstWrite) {
                    this.outputStream.write("--".getBytes());
                    this.outputStream.write(GraphRequest.MIME_BOUNDARY.getBytes());
                    this.outputStream.write("\r\n".getBytes());
                    this.firstWrite = false;
                }
                this.outputStream.write(String.format(str, objArr).getBytes());
                return;
            }
            this.outputStream.write(URLEncoder.encode(String.format(Locale.US, str, objArr), Key.STRING_CHARSET_NAME).getBytes());
        }

        public void writeLine(String str, Object... objArr) throws IOException {
            write(str, objArr);
            if (this.useUrlEncode) {
                return;
            }
            write("\r\n", new Object[0]);
        }
    }

    /* loaded from: classes.dex */
    public static class ParcelableResourceWithMimeType<RESOURCE extends Parcelable> implements Parcelable {
        public static final Parcelable.Creator<ParcelableResourceWithMimeType> CREATOR = new Parcelable.Creator<ParcelableResourceWithMimeType>() { // from class: com.facebook.GraphRequest.ParcelableResourceWithMimeType.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ParcelableResourceWithMimeType createFromParcel(Parcel parcel) {
                return new ParcelableResourceWithMimeType(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ParcelableResourceWithMimeType[] newArray(int i) {
                return new ParcelableResourceWithMimeType[i];
            }
        };
        private final String mimeType;
        private final RESOURCE resource;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 1;
        }

        public String getMimeType() {
            return this.mimeType;
        }

        public RESOURCE getResource() {
            return this.resource;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.mimeType);
            parcel.writeParcelable(this.resource, i);
        }

        public ParcelableResourceWithMimeType(RESOURCE resource, String str) {
            this.mimeType = str;
            this.resource = resource;
        }

        private ParcelableResourceWithMimeType(Parcel parcel) {
            this.mimeType = parcel.readString();
            this.resource = (RESOURCE) parcel.readParcelable(FacebookSdk.getApplicationContext().getClassLoader());
        }
    }
}
