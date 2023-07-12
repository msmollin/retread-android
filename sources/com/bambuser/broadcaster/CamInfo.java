package com.bambuser.broadcaster;

import java.util.LinkedList;
import java.util.List;

/* loaded from: classes.dex */
class CamInfo {
    final boolean mDisableShuttersound;
    final int mFacing;
    final String mId;
    String mName;
    final int mOrientation;
    private List<Resolution> mPictureResolutions;
    private List<Resolution> mPreviewResolutions;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CamInfo(int i, int i2, int i3, boolean z) {
        this.mName = "";
        this.mPreviewResolutions = new LinkedList();
        this.mPictureResolutions = new LinkedList();
        this.mId = String.valueOf(i);
        this.mFacing = i2 == 1 ? 0 : 1;
        this.mOrientation = i3;
        this.mDisableShuttersound = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CamInfo(String str, int i, int i2) {
        this.mName = "";
        this.mPreviewResolutions = new LinkedList();
        this.mPictureResolutions = new LinkedList();
        this.mId = str;
        this.mFacing = i;
        this.mOrientation = i2;
        this.mDisableShuttersound = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isFront() {
        return this.mFacing == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isRear() {
        return this.mFacing == 1;
    }

    boolean isExternal() {
        return this.mFacing == 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized List<Resolution> getPreviewList() {
        return this.mPreviewResolutions;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized List<Resolution> getPictureList() {
        return this.mPictureResolutions;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setPreviewList(List<Resolution> list) {
        this.mPreviewResolutions = list;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setPictureList(List<Resolution> list) {
        this.mPictureResolutions = list;
    }
}
