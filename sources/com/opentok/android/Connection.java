package com.opentok.android;

import androidx.annotation.Nullable;
import com.opentok.otc.SWIGTYPE_p_otc_connection;
import com.opentok.otc.e;
import java.util.Date;

/* loaded from: classes.dex */
public final class Connection {
    private SWIGTYPE_p_otc_connection otc_connection;
    private boolean shouldDestroyOnFinalize;

    /* loaded from: classes.dex */
    static class swig_otc_connection extends SWIGTYPE_p_otc_connection {
        public swig_otc_connection(long j) {
            super(j, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Connection(long j, boolean z) {
        if (z) {
            this.otc_connection = e.a(new swig_otc_connection(j));
        } else {
            this.otc_connection = new swig_otc_connection(j);
        }
        this.shouldDestroyOnFinalize = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Connection(SWIGTYPE_p_otc_connection sWIGTYPE_p_otc_connection, boolean z) {
        this.otc_connection = sWIGTYPE_p_otc_connection;
        this.shouldDestroyOnFinalize = z;
    }

    public boolean equals(@Nullable Object obj) {
        return (obj instanceof Connection) && getConnectionId().equals(((Connection) obj).getConnectionId());
    }

    protected void finalize() {
        SWIGTYPE_p_otc_connection sWIGTYPE_p_otc_connection = this.otc_connection;
        if (sWIGTYPE_p_otc_connection != null && this.shouldDestroyOnFinalize) {
            e.b(sWIGTYPE_p_otc_connection);
        }
        super.finalize();
    }

    public String getConnectionId() {
        return e.e(this.otc_connection);
    }

    public Date getCreationTime() {
        return new Date(e.c(this.otc_connection));
    }

    public String getData() {
        return e.d(this.otc_connection);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SWIGTYPE_p_otc_connection getOtcConnection() {
        return this.otc_connection;
    }

    public int hashCode() {
        return getConnectionId().hashCode();
    }
}
