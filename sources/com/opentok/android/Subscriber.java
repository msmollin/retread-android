package com.opentok.android;

import android.content.Context;
import com.opentok.android.SubscriberKit;

/* loaded from: classes.dex */
public class Subscriber extends SubscriberKit {

    /* loaded from: classes.dex */
    public static class Builder extends SubscriberKit.Builder {
        public Builder(Context context, Stream stream) {
            super(context, stream);
        }

        @Override // com.opentok.android.SubscriberKit.Builder
        public Subscriber build() {
            return new Subscriber(this.context, this.stream, this.renderer);
        }

        @Override // com.opentok.android.SubscriberKit.Builder
        public Builder renderer(BaseVideoRenderer baseVideoRenderer) {
            this.renderer = baseVideoRenderer;
            return this;
        }
    }

    @Deprecated
    public Subscriber(Context context, Stream stream) {
        this(context, stream, null);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected Subscriber(android.content.Context r1, com.opentok.android.Stream r2, com.opentok.android.BaseVideoRenderer r3) {
        /*
            r0 = this;
            if (r3 != 0) goto L8
            if (r1 == 0) goto L8
            com.opentok.android.BaseVideoRenderer r3 = com.opentok.android.VideoRenderFactory.constructRenderer(r1)
        L8:
            r0.<init>(r1, r2, r3)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.opentok.android.Subscriber.<init>(android.content.Context, com.opentok.android.Stream, com.opentok.android.BaseVideoRenderer):void");
    }
}
