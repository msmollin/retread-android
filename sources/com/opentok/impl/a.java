package com.opentok.impl;

import com.opentok.android.OpentokError;

/* loaded from: classes.dex */
public class a extends OpentokError {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.opentok.impl.a$a  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class C0016a {
        static final /* synthetic */ int[] a;

        static {
            int[] iArr = new int[OpentokError.ErrorCode.values().length];
            a = iArr;
            try {
                iArr[OpentokError.ErrorCode.InvalidSessionId.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                a[OpentokError.ErrorCode.AuthorizationFailure.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                a[OpentokError.ErrorCode.UnknownPublisherInstance.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                a[OpentokError.ErrorCode.UnknownSubscriberInstance.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                a[OpentokError.ErrorCode.SessionInvalidSignalType.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                a[OpentokError.ErrorCode.SessionSignalDataTooLong.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                a[OpentokError.ErrorCode.SessionSignalTypeTooLong.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                a[OpentokError.ErrorCode.ConnectionFailed.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                a[OpentokError.ErrorCode.SessionDisconnected.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                a[OpentokError.ErrorCode.VideoCaptureFailed.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                a[OpentokError.ErrorCode.CameraFailed.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                a[OpentokError.ErrorCode.VideoRenderFailed.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                a[OpentokError.ErrorCode.SessionNullOrInvalidParameter.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                a[OpentokError.ErrorCode.SessionIllegalState.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                a[OpentokError.ErrorCode.SessionUnableToForceMute.ordinal()] = 15;
            } catch (NoSuchFieldError unused15) {
            }
        }
    }

    public a(OpentokError.Domain domain, int i) {
        super(domain, i, a(i));
    }

    public static String a(int i) {
        switch (C0016a.a[OpentokError.ErrorCode.fromTypeCode(i).ordinal()]) {
            case 1:
                return "Unable to connect: an invalid session ID was provided.";
            case 2:
                return "Authorization Failure - Invalid credentials were provided.";
            case 3:
                return "Cannot unpublish: An unknown Publisher instance was passed into Session.unpublish().";
            case 4:
                return "Cannot unsubscribe: An unknown Subscriber instance was passed into Session.unsubscribe().";
            case 5:
                return "Invalid signal type.";
            case 6:
                return "Signal data too long.";
            case 7:
                return "Signal type too long.";
            case 8:
                return "Unable to connect to the session: check the network connection.";
            case 9:
                return "Cannot publish: the client is not connected to the OpenTok session.";
            case 10:
                return "Video capture has failed";
            case 11:
                return "The camera of the device has failed. ";
            case 12:
                return "Video render has failed";
            case 13:
                return "Token null or invalid parameter.";
            case 14:
                return "Unable to connect to a session that is already connected or unable to subscribe to a stream that is no longer in the session.";
            case 15:
                return "Unable to send a force mute request. This might happen because this client's capabilities are limited.";
            default:
                return "(null description)";
        }
    }
}
