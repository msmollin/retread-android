package com.treadly.Treadly.Data.Managers;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class TreadlyVideoArchiveEventManager {
    List<TreadlyVideoArchiveEventDelegate> delegates = new ArrayList();
    String subscribeArchiveId;

    /* loaded from: classes2.dex */
    public interface TreadlyVideoArchiveEventDelegate {
        void onReceiveVideoArchiveLike(String str, Integer num);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void subscribe(String str) {
        TreadlyEventHelper.subscribeTopic(getArchiveVideoTopic(str));
        this.subscribeArchiveId = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unsubscribe(String str) {
        TreadlyEventHelper.unsubscribeTopic(getArchiveVideoTopic(str));
        this.subscribeArchiveId = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handlePrepareSubscriptions() {
        if (this.subscribeArchiveId != null) {
            TreadlyEventHelper.subscribeTopic(getArchiveVideoTopic(this.subscribeArchiveId));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleMessage(String str, String str2) {
        Integer parseVideoLikeEventMessage;
        String userId = TreadlyServiceManager.getInstance().getUserId();
        Integer parseMessageId = TreadlyEventHelper.parseMessageId(str2);
        if (userId == null || str2 == null || parseMessageId.intValue() != 601 || (parseVideoLikeEventMessage = TreadlyEventHelper.parseVideoLikeEventMessage(str2)) == null) {
            return;
        }
        sendOnReceiveVideoArchiveLikeEvent(str, parseVideoLikeEventMessage);
    }

    String getArchiveVideoTopic(String str) {
        return String.format("%s/%s/%s", TreadlyEventHelper.BASE_EVENT_TOPIC, TreadlyEventHelper.VIDEO_ARCHIVE_EVENT_TOPIC, str);
    }

    public void addDelegate(TreadlyVideoArchiveEventDelegate treadlyVideoArchiveEventDelegate) {
        this.delegates.add(treadlyVideoArchiveEventDelegate);
    }

    public void removeDelegate(TreadlyVideoArchiveEventDelegate treadlyVideoArchiveEventDelegate) {
        this.delegates.remove(treadlyVideoArchiveEventDelegate);
    }

    private void sendOnReceiveVideoArchiveLikeEvent(String str, Integer num) {
        for (TreadlyVideoArchiveEventDelegate treadlyVideoArchiveEventDelegate : this.delegates) {
            treadlyVideoArchiveEventDelegate.onReceiveVideoArchiveLike(str, num);
        }
    }
}
