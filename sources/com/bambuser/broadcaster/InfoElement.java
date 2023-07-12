package com.bambuser.broadcaster;

import org.w3c.dom.Element;

/* loaded from: classes.dex */
abstract class InfoElement {
    protected static final String LOGTAG = "InfoElement";
    private boolean mUpdated = false;
    protected boolean mInitialized = false;

    protected abstract void processElement(Element element);

    protected abstract boolean validState();

    public final void setUpdated(boolean z) {
        this.mUpdated = z;
    }

    public final boolean isUpdated() {
        return this.mUpdated;
    }
}
