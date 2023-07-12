package com.facebook.imagepipeline.producers;

import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import com.facebook.common.memory.PooledByteBufferFactory;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.request.ImageRequest;
import java.io.IOException;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public class LocalResourceFetchProducer extends LocalFetchProducer {
    public static final String PRODUCER_NAME = "LocalResourceFetchProducer";
    private final Resources mResources;

    @Override // com.facebook.imagepipeline.producers.LocalFetchProducer
    protected String getProducerName() {
        return PRODUCER_NAME;
    }

    public LocalResourceFetchProducer(Executor executor, PooledByteBufferFactory pooledByteBufferFactory, Resources resources) {
        super(executor, pooledByteBufferFactory);
        this.mResources = resources;
    }

    @Override // com.facebook.imagepipeline.producers.LocalFetchProducer
    protected EncodedImage getEncodedImage(ImageRequest imageRequest) throws IOException {
        return getEncodedImage(this.mResources.openRawResource(getResourceId(imageRequest)), getLength(imageRequest));
    }

    private int getLength(ImageRequest imageRequest) {
        AssetFileDescriptor openRawResourceFd;
        AssetFileDescriptor assetFileDescriptor = null;
        try {
            openRawResourceFd = this.mResources.openRawResourceFd(getResourceId(imageRequest));
        } catch (Resources.NotFoundException unused) {
        } catch (Throwable th) {
            th = th;
        }
        try {
            int length = (int) openRawResourceFd.getLength();
            if (openRawResourceFd != null) {
                try {
                    openRawResourceFd.close();
                } catch (IOException unused2) {
                }
            }
            return length;
        } catch (Resources.NotFoundException unused3) {
            assetFileDescriptor = openRawResourceFd;
            if (assetFileDescriptor != null) {
                try {
                    assetFileDescriptor.close();
                } catch (IOException unused4) {
                }
            }
            return -1;
        } catch (Throwable th2) {
            th = th2;
            assetFileDescriptor = openRawResourceFd;
            if (assetFileDescriptor != null) {
                try {
                    assetFileDescriptor.close();
                } catch (IOException unused5) {
                }
            }
            throw th;
        }
    }

    private static int getResourceId(ImageRequest imageRequest) {
        return Integer.parseInt(imageRequest.getSourceUri().getPath().substring(1));
    }
}
