package com.bambuser.broadcaster;

import android.util.Log;
import org.w3c.dom.Element;

/* loaded from: classes.dex */
final class BroadcastElement extends InfoElement {
    public static final String ATTRIBUTE_BROADCAST_ID = "broadcastId";
    public static final String ATTRIBUTE_URL = "url";
    public static final String ATTRIBUTE_VID = "vid";
    public static final String ELEMENT = "broadcast";
    private int mTag = 0;
    private String mVid = "";
    private String mUrl = "";
    private String mBroadcastId = "";

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.bambuser.broadcaster.InfoElement
    public void processElement(Element element) {
        if (element == null || !ELEMENT.equals(element.getTagName())) {
            return;
        }
        if (element.hasAttribute(ATTRIBUTE_VID)) {
            this.mVid = element.getAttribute(ATTRIBUTE_VID);
        } else {
            Log.w("InfoElement", "Missing vid attribute");
        }
        if (element.hasAttribute("url")) {
            this.mUrl = element.getAttribute("url");
        }
        if (element.hasAttribute(ATTRIBUTE_BROADCAST_ID)) {
            this.mBroadcastId = element.getAttribute(ATTRIBUTE_BROADCAST_ID);
        }
        this.mInitialized = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.bambuser.broadcaster.InfoElement
    public boolean validState() {
        return this.mInitialized && this.mVid != null && this.mVid.length() > 0 && this.mUrl != null;
    }

    public void setTag(int i) {
        this.mTag = i;
    }

    public int getTag() {
        return this.mTag;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public String getVid() {
        return this.mVid;
    }

    public String getBroadcastId() {
        return this.mBroadcastId;
    }
}
