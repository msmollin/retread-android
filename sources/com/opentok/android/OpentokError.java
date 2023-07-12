package com.opentok.android;

import androidx.core.view.PointerIconCompat;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;
import com.google.android.gms.common.ConnectionResult;
import com.opentok.impl.a;

/* loaded from: classes.dex */
public class OpentokError {
    protected ErrorCode errorCode;
    protected Domain errorDomain;
    protected String errorMessage;
    protected Exception exception;

    /* loaded from: classes.dex */
    public enum Domain {
        SessionErrorDomain,
        PublisherErrorDomain,
        SubscriberErrorDomain
    }

    /* loaded from: classes.dex */
    public enum ErrorCode {
        UnknownError(-1),
        AuthorizationFailure(PointerIconCompat.TYPE_WAIT),
        InvalidSessionId(1005),
        ConnectionFailed(PointerIconCompat.TYPE_CELL),
        NoMessagingServer(1503),
        ConnectionRefused(1023),
        SessionStateFailed(PointerIconCompat.TYPE_GRAB),
        P2PSessionMaxParticipants(1403),
        SessionConnectionTimeout(PointerIconCompat.TYPE_GRABBING),
        SessionInternalError(2000),
        SessionInvalidSignalType(1461),
        SessionSignalDataTooLong(1413),
        SessionSignalTypeTooLong(1414),
        SessionUnableToForceMute(1540),
        ConnectionDropped(1022),
        SessionDisconnected(PointerIconCompat.TYPE_ALIAS),
        PublisherInternalError(2000),
        PublisherWebRTCError(1610),
        PublisherUnableToPublish(ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED),
        PublisherUnexpectedPeerConnectionDisconnection(1710),
        PublisherCannotAccessCamera(1650),
        PublisherCameraAccessDenied(1670),
        ConnectionTimedOut(1542),
        SubscriberWebRTCError(1600),
        SubscriberServerCannotFindStream(1604),
        SubscriberStreamLimitExceeded(1605),
        SubscriberInternalError(2000),
        UnknownPublisherInstance(2003),
        UnknownSubscriberInstance(2004),
        SessionNullOrInvalidParameter(PointerIconCompat.TYPE_COPY),
        VideoCaptureFailed(PathInterpolatorCompat.MAX_NUM_POINTS),
        CameraFailed(3010),
        VideoRenderFailed(4000),
        SessionSubscriberNotFound(1112),
        SessionPublisherNotFound(1113),
        PublisherTimeout(1541),
        SessionBlockedCountry(1026),
        SessionConnectionLimitExceeded(1027),
        SessionUnexpectedGetSessionInfoResponse(2001),
        SessionIllegalState(PointerIconCompat.TYPE_VERTICAL_DOUBLE_ARROW);
        
        private int code;

        ErrorCode(int i) {
            this.code = i;
        }

        public static ErrorCode fromTypeCode(int i) {
            ErrorCode[] values;
            for (ErrorCode errorCode : values()) {
                if (errorCode.getErrorCode() == i) {
                    return errorCode;
                }
            }
            return UnknownError;
        }

        public int getErrorCode() {
            return this.code;
        }
    }

    public OpentokError(Domain domain, int i, Exception exc) {
        this.errorMessage = "(null description)";
        this.errorDomain = domain;
        this.errorCode = ErrorCode.fromTypeCode(i);
        this.exception = exc;
        if (exc == null) {
            this.errorMessage = a.a(i);
        }
    }

    public OpentokError(Domain domain, int i, String str) {
        this.errorMessage = str == null ? "(null description)" : str;
        this.errorDomain = domain;
        this.errorCode = ErrorCode.fromTypeCode(i);
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }

    public Domain getErrorDomain() {
        return this.errorDomain;
    }

    public Exception getException() {
        return this.exception;
    }

    public String getMessage() {
        return this.errorMessage;
    }
}
