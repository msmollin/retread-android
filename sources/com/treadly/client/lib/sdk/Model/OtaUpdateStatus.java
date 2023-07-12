package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public enum OtaUpdateStatus {
    noError(0),
    otaError(6),
    otaErrorUnspecified(8),
    otaAuthExpired(9),
    otaAuthLeave(10),
    otaAssocExpire(11),
    otaAssocTooMany(12),
    otaNotAuthenticated(13),
    otaNotAssociated(14),
    otaAssocLeave(15),
    otaAssocNotAuthenticated(16),
    otaDisassocPowerCapBad(17),
    otaDisassocSupChanBad(18),
    otaIEInvalid(19),
    otaMicFailure(20),
    ota4WayHandshakeTimeout(21),
    otaGroupKeyUpdateTimeout(22),
    otaIEIn4WayDiffers(23),
    otaGroupCipherInvalid(24),
    otaPairwiseCipherInvalid(25),
    otaAkmpInvalid(26),
    otaUnsuppRsnIEVersion(27),
    otaInvalidRsnIECap(28),
    ota8021XAuthFailed(29),
    otaCipherSuiteRejected(30),
    otaBeaconTimeout(31),
    otaNoApFound(32),
    otaAuthFail(33),
    otaAssocFail(34),
    otaHandshakeTimeout(35),
    otaNoIp(36),
    otaDownloadFail(37),
    unknown(100),
    reconnectError(101),
    versionError(102),
    versionSuccess(103);
    
    private int value;

    OtaUpdateStatus(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }

    public static OtaUpdateStatus fromValue(int i) {
        OtaUpdateStatus[] values;
        for (OtaUpdateStatus otaUpdateStatus : values()) {
            if (otaUpdateStatus.value == i) {
                return otaUpdateStatus;
            }
        }
        return null;
    }
}
