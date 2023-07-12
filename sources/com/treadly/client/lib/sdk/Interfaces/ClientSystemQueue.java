package com.treadly.client.lib.sdk.Interfaces;

/* loaded from: classes2.dex */
public interface ClientSystemQueue {
    void post(Runnable runnable);

    void postDelayed(Runnable runnable, int i);

    void stop();
}
