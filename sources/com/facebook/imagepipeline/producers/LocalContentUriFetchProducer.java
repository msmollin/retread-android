package com.facebook.imagepipeline.producers;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import com.facebook.common.memory.PooledByteBufferFactory;
import com.facebook.common.util.UriUtil;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.request.ImageRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class LocalContentUriFetchProducer extends LocalFetchProducer {
    public static final String PRODUCER_NAME = "LocalContentUriFetchProducer";
    private static final String[] PROJECTION = {"_id", "_data"};
    private final ContentResolver mContentResolver;

    @Override // com.facebook.imagepipeline.producers.LocalFetchProducer
    protected String getProducerName() {
        return PRODUCER_NAME;
    }

    public LocalContentUriFetchProducer(Executor executor, PooledByteBufferFactory pooledByteBufferFactory, ContentResolver contentResolver) {
        super(executor, pooledByteBufferFactory);
        this.mContentResolver = contentResolver;
    }

    @Override // com.facebook.imagepipeline.producers.LocalFetchProducer
    protected EncodedImage getEncodedImage(ImageRequest imageRequest) throws IOException {
        EncodedImage cameraImage;
        InputStream createInputStream;
        Uri sourceUri = imageRequest.getSourceUri();
        if (!UriUtil.isLocalContactUri(sourceUri)) {
            return (!UriUtil.isLocalCameraUri(sourceUri) || (cameraImage = getCameraImage(sourceUri)) == null) ? getEncodedImage(this.mContentResolver.openInputStream(sourceUri), -1) : cameraImage;
        }
        if (sourceUri.toString().endsWith("/photo")) {
            createInputStream = this.mContentResolver.openInputStream(sourceUri);
        } else if (sourceUri.toString().endsWith("/display_photo")) {
            try {
                createInputStream = this.mContentResolver.openAssetFileDescriptor(sourceUri, "r").createInputStream();
            } catch (IOException unused) {
                throw new IOException("Contact photo does not exist: " + sourceUri);
            }
        } else {
            createInputStream = ContactsContract.Contacts.openContactPhotoInputStream(this.mContentResolver, sourceUri);
            if (createInputStream == null) {
                throw new IOException("Contact photo does not exist: " + sourceUri);
            }
        }
        return getEncodedImage(createInputStream, -1);
    }

    @Nullable
    private EncodedImage getCameraImage(Uri uri) throws IOException {
        Cursor query = this.mContentResolver.query(uri, PROJECTION, null, null, null);
        if (query == null) {
            return null;
        }
        try {
            if (query.getCount() == 0) {
                return null;
            }
            query.moveToFirst();
            String string = query.getString(query.getColumnIndex("_data"));
            if (string != null) {
                return getEncodedImage(new FileInputStream(string), getLength(string));
            }
            return null;
        } finally {
            query.close();
        }
    }

    private static int getLength(String str) {
        if (str == null) {
            return -1;
        }
        return (int) new File(str).length();
    }
}
