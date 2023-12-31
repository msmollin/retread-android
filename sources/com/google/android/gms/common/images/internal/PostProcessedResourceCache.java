package com.google.android.gms.common.images.internal;

import android.graphics.drawable.Drawable;
import androidx.collection.LruCache;
import com.google.android.gms.common.internal.Objects;

/* loaded from: classes.dex */
public final class PostProcessedResourceCache extends LruCache<PostProcessedResource, Drawable> {

    /* loaded from: classes.dex */
    public static final class PostProcessedResource {
        public final int postProcessingFlags;
        public final int resId;

        public PostProcessedResource(int i, int i2) {
            this.resId = i;
            this.postProcessingFlags = i2;
        }

        public final boolean equals(Object obj) {
            if (obj instanceof PostProcessedResource) {
                if (this == obj) {
                    return true;
                }
                PostProcessedResource postProcessedResource = (PostProcessedResource) obj;
                return postProcessedResource.resId == this.resId && postProcessedResource.postProcessingFlags == this.postProcessingFlags;
            }
            return false;
        }

        public final int hashCode() {
            return Objects.hashCode(Integer.valueOf(this.resId), Integer.valueOf(this.postProcessingFlags));
        }
    }

    public PostProcessedResourceCache() {
        super(10);
    }
}
