package com.treadly.Treadly.UI.TreadlyVideo.Data;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import com.facebook.appevents.AppEventsConstants;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoUploaderManager;
import com.treadly.Treadly.UI.Util.SharedPreferences;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.JSONException;

/* loaded from: classes2.dex */
public class VideoUploaderManager {
    private static final int INITIAL_UPLOAD_DELAY = 3000;
    private static final int PERIODIC_UPLOAD_DELAY = 60000;
    public static final int componentsCount_v1 = 6;
    public static final int componentsCount_v2 = 8;
    public static final int compositeIdIndex = 6;
    public static Context context = null;
    public static final int identifierIndex = 0;
    public static final String identifier_v1 = "Treadly-Recorded";
    public static final String identifier_v2 = "Treadly-Recorded-v2";
    public static final int recordingIdIndex = 7;
    public static final int serviceIdIndex = 1;
    public static final int serviceIndex = 3;
    public static VideoUploaderManager shared = new VideoUploaderManager();
    public static final int timestampIndex = 5;
    public static final int userIdIndex = 4;
    public static final int workoutIdIndex = 2;
    Timer uploadTimer = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public interface SendFileListener {
        void onResponse(boolean z);
    }

    public void start() throws JSONException {
        startUploadTimer();
    }

    public void stop() {
        stopUploadTimer();
    }

    public static String getFileName(String str, String str2, VideoServiceVideoService videoServiceVideoService, Date date, String str3, String str4, String str5) {
        return String.format(Locale.getDefault(), "%s:%s:%s:%s:%s:%d:%s:%s", identifier_v2, str, str2, videoServiceVideoService.getName(), str3, Long.valueOf(date.getTime()), str4, str5);
    }

    public static String getFileName(String str, String str2, VideoServiceVideoService videoServiceVideoService, Date date, String str3) {
        return String.format(Locale.getDefault(), "%s:%s:%s:%s:%s:%d:%s:%s", identifier_v2, str, str2, videoServiceVideoService.getName(), str3, Long.valueOf(date.getTime()), AppEventsConstants.EVENT_PARAM_VALUE_NO, AppEventsConstants.EVENT_PARAM_VALUE_NO);
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0045, code lost:
        if (r0.equals(com.treadly.Treadly.UI.TreadlyVideo.Data.VideoUploaderManager.identifier_v1) != false) goto L15;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.treadly.Treadly.UI.TreadlyVideo.Data.VideoUploadInfo parseFileName(java.lang.String r6) {
        /*
            boolean r0 = r6.isEmpty()
            r1 = 0
            if (r0 == 0) goto L8
            return r1
        L8:
            java.util.ArrayList r0 = new java.util.ArrayList
            java.lang.String r2 = ":"
            java.lang.String[] r2 = r6.split(r2)
            java.util.List r2 = java.util.Arrays.asList(r2)
            r0.<init>(r2)
            boolean r2 = r0.isEmpty()
            if (r2 == 0) goto L1e
            return r1
        L1e:
            r2 = 0
            java.lang.Object r0 = r0.get(r2)
            java.lang.String r0 = (java.lang.String) r0
            r3 = -1
            int r4 = r0.hashCode()
            r5 = -1332925722(0xffffffffb08d2ae6, float:-1.0271293E-9)
            if (r4 == r5) goto L3f
            r2 = 2077481667(0x7bd3dac3, float:2.2000233E36)
            if (r4 == r2) goto L35
            goto L48
        L35:
            java.lang.String r2 = "Treadly-Recorded-v2"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L48
            r2 = 1
            goto L49
        L3f:
            java.lang.String r4 = "Treadly-Recorded"
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L48
            goto L49
        L48:
            r2 = r3
        L49:
            switch(r2) {
                case 0: goto L52;
                case 1: goto L4d;
                default: goto L4c;
            }
        L4c:
            return r1
        L4d:
            com.treadly.Treadly.UI.TreadlyVideo.Data.VideoUploadInfo r6 = parseFileName_v2(r6)
            return r6
        L52:
            com.treadly.Treadly.UI.TreadlyVideo.Data.VideoUploadInfo r6 = parseFileName_v1(r6)
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoUploaderManager.parseFileName(java.lang.String):com.treadly.Treadly.UI.TreadlyVideo.Data.VideoUploadInfo");
    }

    public static VideoUploadInfo parseFileName_v1(String str) {
        VideoServiceVideoService fromString;
        ArrayList arrayList = new ArrayList(Arrays.asList(str.split(":")));
        if (arrayList.size() == 6 && (fromString = VideoServiceVideoService.fromString((String) arrayList.get(3))) != null) {
            return new VideoUploadInfo((String) arrayList.get(1), (String) arrayList.get(2), fromString, new Date(Integer.parseInt((String) arrayList.get(5))), (String) arrayList.get(4), AppEventsConstants.EVENT_PARAM_VALUE_NO, AppEventsConstants.EVENT_PARAM_VALUE_NO);
        }
        return null;
    }

    public static VideoUploadInfo parseFileName_v2(String str) {
        VideoServiceVideoService fromString;
        ArrayList arrayList = new ArrayList(Arrays.asList(str.split(":")));
        if (arrayList.size() == 8 && (fromString = VideoServiceVideoService.fromString((String) arrayList.get(3))) != null) {
            return new VideoUploadInfo((String) arrayList.get(1), (String) arrayList.get(2), fromString, new Date(Long.parseLong((String) arrayList.get(5))), (String) arrayList.get(4), (String) arrayList.get(6), (String) arrayList.get(7));
        }
        return null;
    }

    void startUploadTimer() {
        if (this.uploadTimer != null) {
            return;
        }
        this.uploadTimer = new Timer();
        this.uploadTimer.schedule(new TimerTask() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoUploaderManager.1
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                try {
                    VideoUploaderManager.this.checkUploads();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, 3000L, 60000L);
    }

    void stopUploadTimer() {
        if (this.uploadTimer == null) {
            return;
        }
        this.uploadTimer.cancel();
        this.uploadTimer.purge();
        this.uploadTimer = null;
    }

    void checkUploads() throws JSONException {
        File[] listFiles = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES).listFiles();
        Log.d("WILLG", String.valueOf(listFiles.length));
        if (listFiles == null || listFiles.length == 0) {
            return;
        }
        for (final File file : listFiles) {
            sendFile(Uri.parse(file.toString()), new SendFileListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoUploaderManager.2
                @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoUploaderManager.SendFileListener
                public void onResponse(boolean z) {
                    if (z) {
                        VideoUploaderManager.this.deleteFile(Uri.parse(file.toString()));
                    }
                }
            });
        }
    }

    void sendFile(Uri uri, final SendFileListener sendFileListener) throws JSONException {
        String uri2 = uri.toString();
        VideoUploadInfo parseFileName = parseFileName(uri2.substring(uri2.lastIndexOf(MqttTopic.TOPIC_LEVEL_SEPARATOR) + 1));
        if (parseFileName == null) {
            sendFileListener.onResponse(false);
            return;
        }
        switch (parseFileName.service) {
            case tokbox:
                VideoServiceHelper.uploadTokboxArchive(parseFileName.serviceId, parseFileName.createdAt, uri, null, null, new VideoServiceHelper.VideoUploadArchiveListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoUploaderManager.3
                    @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoUploadArchiveListener
                    public void onResponse(String str) {
                        sendFileListener.onResponse(str == null);
                    }
                });
                return;
            case bambuser:
                VideoServiceHelper.uploadBambuserArchive(parseFileName.serviceId, parseFileName.workoutId, parseFileName.createdAt, uri, null, null, new VideoServiceHelper.VideoUploadArchiveListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoUploaderManager.4
                    @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoUploadArchiveListener
                    public void onResponse(String str) {
                        if (str == null) {
                            sendFileListener.onResponse(true);
                        } else {
                            sendFileListener.onResponse(false);
                        }
                    }
                });
                return;
            case compositeTokbox:
                return;
            case buddyProfileVideo:
                VideoServiceHelper.uploadBuddyProfileVideo(parseFileName.serviceId, uri, new VideoServiceHelper.BuddyFileUploadListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoUploaderManager$HILrQxT2vaVurn1FMNsQKLh8aKs
                    @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.BuddyFileUploadListener
                    public final void onResponse(String str) {
                        VideoUploaderManager.lambda$sendFile$0(VideoUploaderManager.SendFileListener.this, str);
                    }
                });
                return;
            case buddyProfileAudio:
                VideoServiceHelper.uploadBuddyProfileAudio(parseFileName.serviceId, uri, new VideoServiceHelper.BuddyFileUploadListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoUploaderManager$M8Pqyg5HgY4jVQK2tLgviAHPVUw
                    @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.BuddyFileUploadListener
                    public final void onResponse(String str) {
                        VideoUploaderManager.lambda$sendFile$1(VideoUploaderManager.SendFileListener.this, str);
                    }
                });
                return;
            default:
                System.out.println("Error: Unsupported upload service type");
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$sendFile$0(SendFileListener sendFileListener, String str) {
        if (str == null) {
            sendFileListener.onResponse(true);
        } else {
            sendFileListener.onResponse(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$sendFile$1(SendFileListener sendFileListener, String str) {
        if (str == null) {
            sendFileListener.onResponse(true);
        } else {
            sendFileListener.onResponse(false);
        }
    }

    void deleteFile(Uri uri) {
        new File(uri.toString()).delete();
    }

    public void storeVideoFileNameAlias(String str, String str2) {
        SharedPreferences.shared.storeVideoFileNameAlias(str, str2);
    }

    public void removeVideoFileNameAlias(String str) {
        SharedPreferences.shared.removeVideoFileNameAlias(str);
    }

    public String getVideoFileNameAlias(String str) {
        return SharedPreferences.shared.getVideoFileNameAlias(str);
    }

    public static String generateVideoRecordingId() {
        return randomHexString(22);
    }

    private static String randomHexString(int i) {
        String str = "";
        for (int i2 = 0; i2 < i; i2++) {
            int nextInt = new Random().nextInt("abcdef0123456789".length());
            str = str + "abcdef0123456789".charAt(nextInt);
        }
        return str;
    }
}
