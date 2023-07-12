package com.opentok.otc;

/* loaded from: classes.dex */
public class otc_session_capabilities {
    private transient long a;
    protected transient boolean b;

    public otc_session_capabilities() {
        this(opentokJNI.new_otc_session_capabilities(), true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public otc_session_capabilities(long j, boolean z) {
        this.b = z;
        this.a = j;
    }

    public synchronized void a() {
        long j = this.a;
        if (j != 0) {
            if (this.b) {
                this.b = false;
                opentokJNI.delete_otc_session_capabilities(j);
            }
            this.a = 0L;
        }
    }

    public int b() {
        return opentokJNI.otc_session_capabilities_force_mute_get(this.a, this);
    }

    public int c() {
        return opentokJNI.otc_session_capabilities_publish_get(this.a, this);
    }

    protected void finalize() {
        a();
    }
}
