package com.opentok.otc;

/* loaded from: classes.dex */
public class otc_signal_options {
    private transient long a;
    protected transient boolean b;

    public otc_signal_options() {
        this(opentokJNI.new_otc_signal_options(), true);
    }

    protected otc_signal_options(long j, boolean z) {
        this.b = z;
        this.a = j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static long a(otc_signal_options otc_signal_optionsVar) {
        if (otc_signal_optionsVar == null) {
            return 0L;
        }
        return otc_signal_optionsVar.a;
    }

    public synchronized void a() {
        long j = this.a;
        if (j != 0) {
            if (this.b) {
                this.b = false;
                opentokJNI.delete_otc_signal_options(j);
            }
            this.a = 0L;
        }
    }

    public void a(int i) {
        opentokJNI.otc_signal_options_retry_after_reconnect_set(this.a, this, i);
    }

    protected void finalize() {
        a();
    }
}
