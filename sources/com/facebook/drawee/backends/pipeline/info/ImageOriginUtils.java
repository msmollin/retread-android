package com.facebook.drawee.backends.pipeline.info;

import com.facebook.imagepipeline.producers.BitmapMemoryCacheGetProducer;
import com.facebook.imagepipeline.producers.BitmapMemoryCacheProducer;
import com.facebook.imagepipeline.producers.DiskCacheReadProducer;
import com.facebook.imagepipeline.producers.EncodedMemoryCacheProducer;
import com.facebook.imagepipeline.producers.NetworkFetchProducer;

/* loaded from: classes.dex */
public class ImageOriginUtils {
    public static String toString(int i) {
        switch (i) {
            case 0:
                return "network";
            case 1:
                return "disk";
            case 2:
                return "memory_encoded";
            case 3:
                return "memory_bitmap";
            default:
                return "unknown";
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static int mapProducerNameToImageOrigin(String str) {
        char c;
        switch (str.hashCode()) {
            case -1914072202:
                if (str.equals(BitmapMemoryCacheGetProducer.PRODUCER_NAME)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -1307634203:
                if (str.equals(EncodedMemoryCacheProducer.PRODUCER_NAME)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case -1224383234:
                if (str.equals(NetworkFetchProducer.PRODUCER_NAME)) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 656304759:
                if (str.equals(DiskCacheReadProducer.PRODUCER_NAME)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 957714404:
                if (str.equals(BitmapMemoryCacheProducer.PRODUCER_NAME)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
            case 1:
                return 3;
            case 2:
                return 2;
            case 3:
                return 1;
            case 4:
                return 0;
            default:
                return -1;
        }
    }

    private ImageOriginUtils() {
    }
}
