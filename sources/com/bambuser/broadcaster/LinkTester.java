package com.bambuser.broadcaster;

import android.os.AsyncTask;
import android.os.SystemClock;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;
import javax.net.ssl.HttpsURLConnection;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class LinkTester {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface Callback {
        void onDone(long j);
    }

    LinkTester() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Type inference failed for: r0v0, types: [com.bambuser.broadcaster.LinkTester$1] */
    public static void fetch(final String str, final Callback callback) {
        new AsyncTask<Void, Void, Long>() { // from class: com.bambuser.broadcaster.LinkTester.1
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Removed duplicated region for block: B:22:0x0062  */
            /* JADX WARN: Removed duplicated region for block: B:24:0x006e  */
            @Override // android.os.AsyncTask
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public java.lang.Long doInBackground(java.lang.Void... r11) {
                /*
                    r10 = this;
                    r11 = 32768(0x8000, float:4.5918E-41)
                    byte[] r11 = new byte[r11]
                    r0 = 0
                    java.net.URL r2 = new java.net.URL     // Catch: java.lang.Exception -> L5c
                    java.lang.String r10 = r1     // Catch: java.lang.Exception -> L5c
                    r2.<init>(r10)     // Catch: java.lang.Exception -> L5c
                    java.net.URLConnection r10 = r2.openConnection()     // Catch: java.lang.Exception -> L5c
                    java.net.HttpURLConnection r10 = (java.net.HttpURLConnection) r10     // Catch: java.lang.Exception -> L5c
                    boolean r2 = r10 instanceof javax.net.ssl.HttpsURLConnection     // Catch: java.lang.Exception -> L5c
                    if (r2 == 0) goto L22
                    r2 = r10
                    javax.net.ssl.HttpsURLConnection r2 = (javax.net.ssl.HttpsURLConnection) r2     // Catch: java.lang.Exception -> L5c
                    com.bambuser.broadcaster.ModernTlsSocketFactory r3 = com.bambuser.broadcaster.ModernTlsSocketFactory.getInstance()     // Catch: java.lang.Exception -> L5c
                    r2.setSSLSocketFactory(r3)     // Catch: java.lang.Exception -> L5c
                L22:
                    r2 = 10000(0x2710, float:1.4013E-41)
                    r10.setConnectTimeout(r2)     // Catch: java.lang.Exception -> L5c
                    r10.setReadTimeout(r2)     // Catch: java.lang.Exception -> L5c
                    r2 = 0
                    r10.setUseCaches(r2)     // Catch: java.lang.Exception -> L5c
                    long r2 = android.os.SystemClock.elapsedRealtime()     // Catch: java.lang.Exception -> L5c
                    r10.connect()     // Catch: java.lang.Exception -> L5c
                    int r4 = r10.getResponseCode()     // Catch: java.lang.Exception -> L5c
                    java.io.InputStream r5 = r10.getInputStream()     // Catch: java.lang.Exception -> L5c
                    r6 = 200(0xc8, float:2.8E-43)
                    if (r4 != r6) goto L53
                    r6 = r0
                L42:
                    int r4 = r5.read(r11)     // Catch: java.lang.Exception -> L51
                    if (r4 <= 0) goto L4b
                    long r8 = (long) r4     // Catch: java.lang.Exception -> L51
                    long r6 = r6 + r8
                    goto L42
                L4b:
                    long r8 = android.os.SystemClock.elapsedRealtime()     // Catch: java.lang.Exception -> L51
                    long r8 = r8 - r2
                    goto L55
                L51:
                    r8 = r0
                    goto L5e
                L53:
                    r6 = r0
                    r8 = r6
                L55:
                    r5.close()     // Catch: java.lang.Exception -> L5e
                    r10.disconnect()     // Catch: java.lang.Exception -> L5e
                    goto L5e
                L5c:
                    r6 = r0
                    r8 = r6
                L5e:
                    int r10 = (r8 > r0 ? 1 : (r8 == r0 ? 0 : -1))
                    if (r10 <= 0) goto L6e
                    r10 = 8
                    long r6 = r6 * r10
                    r10 = 1000(0x3e8, double:4.94E-321)
                    long r6 = r6 * r10
                    long r6 = r6 / r8
                    java.lang.Long r10 = java.lang.Long.valueOf(r6)
                    return r10
                L6e:
                    java.lang.Long r10 = java.lang.Long.valueOf(r0)
                    return r10
                */
                throw new UnsupportedOperationException("Method not decompiled: com.bambuser.broadcaster.LinkTester.AnonymousClass1.doInBackground(java.lang.Void[]):java.lang.Long");
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(Long l) {
                callback.onDone(l.longValue());
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static UploadTest post(String str, int i, Callback callback) {
        return new UploadTest(str, i, callback);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class UploadTest {
        private final Callback mCallback;
        private volatile int mTargetBytes;
        private final String mUrl;
        private volatile long mStartTime = 0;
        private volatile long mDurationMillis = 0;
        private volatile int mUploadedBytes = 0;

        /* JADX WARN: Type inference failed for: r3v1, types: [com.bambuser.broadcaster.LinkTester$UploadTest$1] */
        UploadTest(String str, int i, Callback callback) {
            this.mUrl = str;
            this.mTargetBytes = i;
            this.mCallback = callback;
            new AsyncTask<Void, Void, Void>() { // from class: com.bambuser.broadcaster.LinkTester.UploadTest.1
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // android.os.AsyncTask
                public Void doInBackground(Void... voidArr) {
                    byte[] bArr = new byte[32768];
                    Random random = new Random();
                    try {
                        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(UploadTest.this.mUrl).openConnection();
                        if (httpURLConnection instanceof HttpsURLConnection) {
                            ((HttpsURLConnection) httpURLConnection).setSSLSocketFactory(ModernTlsSocketFactory.getInstance());
                        }
                        httpURLConnection.setConnectTimeout(10000);
                        httpURLConnection.setReadTimeout(10000);
                        httpURLConnection.setUseCaches(false);
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setFixedLengthStreamingMode(UploadTest.this.mTargetBytes);
                        httpURLConnection.connect();
                        OutputStream outputStream = httpURLConnection.getOutputStream();
                        long elapsedRealtime = SystemClock.elapsedRealtime();
                        UploadTest.this.mStartTime = System.currentTimeMillis();
                        do {
                            int i2 = UploadTest.this.mTargetBytes;
                            if (i2 <= UploadTest.this.mUploadedBytes) {
                                break;
                            }
                            int min = Math.min(bArr.length, i2 - UploadTest.this.mUploadedBytes);
                            random.nextBytes(bArr);
                            outputStream.write(bArr, 0, min);
                            UploadTest.this.mUploadedBytes += min;
                            UploadTest.this.mDurationMillis = SystemClock.elapsedRealtime() - elapsedRealtime;
                        } while (UploadTest.this.mDurationMillis <= 10000);
                        outputStream.close();
                        if (httpURLConnection.getResponseCode() == 200) {
                            LinkTester.read(httpURLConnection.getInputStream());
                        } else {
                            LinkTester.read(httpURLConnection.getErrorStream());
                        }
                        httpURLConnection.disconnect();
                        return null;
                    } catch (Exception unused) {
                        return null;
                    }
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // android.os.AsyncTask
                public void onPostExecute(Void r3) {
                    if (UploadTest.this.mCallback == null || UploadTest.this.mTargetBytes <= 0) {
                        return;
                    }
                    UploadTest.this.mCallback.onDone(UploadTest.this.getBitrate());
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public long getBitrate() {
            long j = this.mDurationMillis;
            long j2 = this.mUploadedBytes;
            if (j > 0) {
                return ((j2 * 8) * 1000) / j;
            }
            return 0L;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public long getStartTime() {
            return this.mStartTime;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void cancel() {
            this.mTargetBytes = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String read(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        scanner.useDelimiter("\\A");
        String next = scanner.hasNext() ? scanner.next() : "";
        scanner.close();
        return next;
    }
}
