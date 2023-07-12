package com.facebook.imagepipeline.producers;

import android.net.Uri;
import com.facebook.common.internal.VisibleForTesting;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.producers.NetworkFetcher;
import com.treadly.Treadly.Data.Managers.TreadlyEventHelper;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/* loaded from: classes.dex */
public class HttpUrlConnectionNetworkFetcher extends BaseNetworkFetcher<FetchState> {
    public static final int HTTP_DEFAULT_TIMEOUT = 30000;
    public static final int HTTP_PERMANENT_REDIRECT = 308;
    public static final int HTTP_TEMPORARY_REDIRECT = 307;
    private static final int MAX_REDIRECTS = 5;
    private static final int NUM_NETWORK_THREADS = 3;
    private final ExecutorService mExecutorService;
    private int mHttpConnectionTimeout;

    private static boolean isHttpRedirect(int i) {
        switch (i) {
            case GenericDraweeHierarchyBuilder.DEFAULT_FADE_DURATION /* 300 */:
            case TreadlyEventHelper.MESSAGE_ID_GROUP_INFO_STATE_CHANGED /* 301 */:
            case TreadlyEventHelper.MESSAGE_ID_GROUP_MEMBER_STATE_CHANGED /* 302 */:
            case TreadlyEventHelper.MESSAGE_ID_GROUP_VIDEO_STATE_CHANGED /* 303 */:
            case 307:
            case 308:
                return true;
            case TreadlyEventHelper.MESSAGE_ID_GROUP_POST_STATE_CHANGED /* 304 */:
            case 305:
            case 306:
            default:
                return false;
        }
    }

    private static boolean isHttpSuccess(int i) {
        return i >= 200 && i < 300;
    }

    public HttpUrlConnectionNetworkFetcher() {
        this(Executors.newFixedThreadPool(3));
    }

    public HttpUrlConnectionNetworkFetcher(int i) {
        this(Executors.newFixedThreadPool(3));
        this.mHttpConnectionTimeout = i;
    }

    @VisibleForTesting
    HttpUrlConnectionNetworkFetcher(ExecutorService executorService) {
        this.mExecutorService = executorService;
    }

    @Override // com.facebook.imagepipeline.producers.NetworkFetcher
    public FetchState createFetchState(Consumer<EncodedImage> consumer, ProducerContext producerContext) {
        return new FetchState(consumer, producerContext);
    }

    @Override // com.facebook.imagepipeline.producers.NetworkFetcher
    public void fetch(final FetchState fetchState, final NetworkFetcher.Callback callback) {
        final Future<?> submit = this.mExecutorService.submit(new Runnable() { // from class: com.facebook.imagepipeline.producers.HttpUrlConnectionNetworkFetcher.1
            @Override // java.lang.Runnable
            public void run() {
                HttpUrlConnectionNetworkFetcher.this.fetchSync(fetchState, callback);
            }
        });
        fetchState.getContext().addCallbacks(new BaseProducerContextCallbacks() { // from class: com.facebook.imagepipeline.producers.HttpUrlConnectionNetworkFetcher.2
            @Override // com.facebook.imagepipeline.producers.BaseProducerContextCallbacks, com.facebook.imagepipeline.producers.ProducerContextCallbacks
            public void onCancellationRequested() {
                if (submit.cancel(false)) {
                    callback.onCancellation();
                }
            }
        });
    }

    /* JADX WARN: Removed duplicated region for block: B:37:0x004d  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0048 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @com.facebook.common.internal.VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void fetchSync(com.facebook.imagepipeline.producers.FetchState r4, com.facebook.imagepipeline.producers.NetworkFetcher.Callback r5) {
        /*
            r3 = this;
            r0 = 0
            android.net.Uri r4 = r4.getUri()     // Catch: java.lang.Throwable -> L31 java.io.IOException -> L34
            r1 = 5
            java.net.HttpURLConnection r3 = r3.downloadFrom(r4, r1)     // Catch: java.lang.Throwable -> L31 java.io.IOException -> L34
            if (r3 == 0) goto L25
            java.io.InputStream r4 = r3.getInputStream()     // Catch: java.lang.Throwable -> L1e java.io.IOException -> L20
            r0 = -1
            r5.onResponse(r4, r0)     // Catch: java.lang.Throwable -> L15 java.io.IOException -> L18
            goto L26
        L15:
            r5 = move-exception
            r0 = r4
            goto L46
        L18:
            r0 = move-exception
            r2 = r4
            r4 = r3
            r3 = r0
            r0 = r2
            goto L36
        L1e:
            r5 = move-exception
            goto L46
        L20:
            r4 = move-exception
            r2 = r4
            r4 = r3
            r3 = r2
            goto L36
        L25:
            r4 = r0
        L26:
            if (r4 == 0) goto L2b
            r4.close()     // Catch: java.io.IOException -> L2b
        L2b:
            if (r3 == 0) goto L43
            r3.disconnect()
            goto L43
        L31:
            r5 = move-exception
            r3 = r0
            goto L46
        L34:
            r3 = move-exception
            r4 = r0
        L36:
            r5.onFailure(r3)     // Catch: java.lang.Throwable -> L44
            if (r0 == 0) goto L3e
            r0.close()     // Catch: java.io.IOException -> L3e
        L3e:
            if (r4 == 0) goto L43
            r4.disconnect()
        L43:
            return
        L44:
            r5 = move-exception
            r3 = r4
        L46:
            if (r0 == 0) goto L4b
            r0.close()     // Catch: java.io.IOException -> L4b
        L4b:
            if (r3 == 0) goto L50
            r3.disconnect()
        L50:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.imagepipeline.producers.HttpUrlConnectionNetworkFetcher.fetchSync(com.facebook.imagepipeline.producers.FetchState, com.facebook.imagepipeline.producers.NetworkFetcher$Callback):void");
    }

    private HttpURLConnection downloadFrom(Uri uri, int i) throws IOException {
        HttpURLConnection openConnectionTo = openConnectionTo(uri);
        openConnectionTo.setConnectTimeout(this.mHttpConnectionTimeout);
        int responseCode = openConnectionTo.getResponseCode();
        if (isHttpSuccess(responseCode)) {
            return openConnectionTo;
        }
        if (isHttpRedirect(responseCode)) {
            String headerField = openConnectionTo.getHeaderField("Location");
            openConnectionTo.disconnect();
            Uri parse = headerField == null ? null : Uri.parse(headerField);
            String scheme = uri.getScheme();
            if (i > 0 && parse != null && !parse.getScheme().equals(scheme)) {
                return downloadFrom(parse, i - 1);
            }
            throw new IOException(i == 0 ? error("URL %s follows too many redirects", uri.toString()) : error("URL %s returned %d without a valid redirect", uri.toString(), Integer.valueOf(responseCode)));
        }
        openConnectionTo.disconnect();
        throw new IOException(String.format("Image URL %s returned HTTP code %d", uri.toString(), Integer.valueOf(responseCode)));
    }

    @VisibleForTesting
    static HttpURLConnection openConnectionTo(Uri uri) throws IOException {
        return (HttpURLConnection) UriUtil.uriToUrl(uri).openConnection();
    }

    private static String error(String str, Object... objArr) {
        return String.format(Locale.getDefault(), str, objArr);
    }
}
