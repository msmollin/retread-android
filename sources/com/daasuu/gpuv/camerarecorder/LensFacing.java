package com.daasuu.gpuv.camerarecorder;

/* loaded from: classes.dex */
public enum LensFacing {
    FRONT(0),
    BACK(1);
    
    private int facing;

    LensFacing(int i) {
        this.facing = i;
    }

    public int getFacing() {
        return this.facing;
    }
}
