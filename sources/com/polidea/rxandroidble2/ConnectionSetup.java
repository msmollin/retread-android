package com.polidea.rxandroidble2;

import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class ConnectionSetup {
    public static final int DEFAULT_OPERATION_TIMEOUT = 30;
    public final boolean autoConnect;
    public final Timeout operationTimeout;
    public final boolean suppressOperationCheck;

    private ConnectionSetup(boolean z, boolean z2, Timeout timeout) {
        this.autoConnect = z;
        this.suppressOperationCheck = z2;
        this.operationTimeout = timeout;
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private boolean autoConnect = false;
        private boolean suppressOperationCheck = false;
        private Timeout operationTimeout = new Timeout(30, TimeUnit.SECONDS);

        public Builder setAutoConnect(boolean z) {
            this.autoConnect = z;
            return this;
        }

        public Builder setSuppressIllegalOperationCheck(boolean z) {
            this.suppressOperationCheck = z;
            return this;
        }

        public Builder setOperationTimeout(Timeout timeout) {
            this.operationTimeout = timeout;
            return this;
        }

        public ConnectionSetup build() {
            return new ConnectionSetup(this.autoConnect, this.suppressOperationCheck, this.operationTimeout);
        }
    }
}
