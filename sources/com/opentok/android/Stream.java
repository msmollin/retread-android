package com.opentok.android;

import androidx.annotation.Nullable;
import com.opentok.otc.SWIGTYPE_p_otc_stream;
import com.opentok.otc.e;
import com.opentok.otc.otc_stream_video_type;
import java.util.Date;

/* loaded from: classes.dex */
public final class Stream implements Comparable {
    SWIGTYPE_p_otc_stream otc_stream;
    private boolean shouldDestroyOnFinalize;

    /* loaded from: classes.dex */
    public enum StreamVideoType {
        StreamVideoTypeCamera(1),
        StreamVideoTypeScreen(2),
        StreamVideoTypeCustom(3);
        
        private int videoType;

        StreamVideoType(int i) {
            this.videoType = i;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static StreamVideoType fromSwig(otc_stream_video_type otc_stream_video_typeVar) {
            StreamVideoType[] values;
            for (StreamVideoType streamVideoType : values()) {
                if (streamVideoType.getVideoType() == otc_stream_video_typeVar.a()) {
                    return streamVideoType;
                }
            }
            throw new IllegalArgumentException("unknown type " + otc_stream_video_typeVar);
        }

        static StreamVideoType fromType(int i) {
            StreamVideoType[] values;
            for (StreamVideoType streamVideoType : values()) {
                if (streamVideoType.getVideoType() == i) {
                    return streamVideoType;
                }
            }
            throw new IllegalArgumentException("unknown type " + i);
        }

        public int getVideoType() {
            return this.videoType;
        }
    }

    /* loaded from: classes.dex */
    static class swig_otc_stream extends SWIGTYPE_p_otc_stream {
        public swig_otc_stream(long j) {
            super(j, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Stream(long j, boolean z) {
        if (z) {
            this.otc_stream = e.a(new swig_otc_stream(j));
        } else {
            this.otc_stream = new swig_otc_stream(j);
        }
        this.shouldDestroyOnFinalize = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Stream(SWIGTYPE_p_otc_stream sWIGTYPE_p_otc_stream, boolean z) {
        this.otc_stream = sWIGTYPE_p_otc_stream;
        this.shouldDestroyOnFinalize = z;
    }

    @Override // java.lang.Comparable
    public int compareTo(Object obj) {
        return getStreamId().compareTo(((Stream) obj).getStreamId());
    }

    public boolean equals(@Nullable Object obj) {
        return (obj instanceof Stream) && ((Stream) obj).getStreamId().equals(getStreamId());
    }

    protected void finalize() {
        SWIGTYPE_p_otc_stream sWIGTYPE_p_otc_stream = this.otc_stream;
        if (sWIGTYPE_p_otc_stream != null && this.shouldDestroyOnFinalize) {
            e.b(sWIGTYPE_p_otc_stream);
        }
        super.finalize();
    }

    public Connection getConnection() {
        return new Connection(e.c(this.otc_stream), false);
    }

    public Date getCreationTime() {
        return new Date(e.d(this.otc_stream));
    }

    public String getName() {
        return e.f(this.otc_stream);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SWIGTYPE_p_otc_stream getOtc_stream() {
        return this.otc_stream;
    }

    @Deprecated
    public Session getSession() {
        return null;
    }

    public String getStreamId() {
        return e.e(this.otc_stream);
    }

    public StreamVideoType getStreamVideoType() {
        return StreamVideoType.fromSwig(e.h(this.otc_stream));
    }

    public int getVideoHeight() {
        return e.g(this.otc_stream);
    }

    public int getVideoWidth() {
        return e.i(this.otc_stream);
    }

    public boolean hasAudio() {
        return Utils.intToBoolean(e.j(this.otc_stream));
    }

    public boolean hasVideo() {
        return Utils.intToBoolean(e.k(this.otc_stream));
    }

    public int hashCode() {
        return getStreamId().hashCode();
    }

    public String toString() {
        return String.format("streamId=%s", getStreamId());
    }
}
