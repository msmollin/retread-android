package com.facebook.imagepipeline.cache;

import com.facebook.cache.common.CacheKey;
import com.facebook.common.internal.Supplier;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.imagepipeline.cache.CountingMemoryCache;
import com.facebook.imagepipeline.image.CloseableImage;

/* loaded from: classes.dex */
public class BitmapCountingMemoryCacheFactory {
    public static CountingMemoryCache<CacheKey, CloseableImage> get(Supplier<MemoryCacheParams> supplier, MemoryTrimmableRegistry memoryTrimmableRegistry) {
        return get(supplier, memoryTrimmableRegistry, new BitmapMemoryCacheTrimStrategy());
    }

    public static CountingMemoryCache<CacheKey, CloseableImage> get(Supplier<MemoryCacheParams> supplier, MemoryTrimmableRegistry memoryTrimmableRegistry, CountingMemoryCache.CacheTrimStrategy cacheTrimStrategy) {
        CountingMemoryCache<CacheKey, CloseableImage> countingMemoryCache = new CountingMemoryCache<>(new ValueDescriptor<CloseableImage>() { // from class: com.facebook.imagepipeline.cache.BitmapCountingMemoryCacheFactory.1
            @Override // com.facebook.imagepipeline.cache.ValueDescriptor
            public int getSizeInBytes(CloseableImage closeableImage) {
                return closeableImage.getSizeInBytes();
            }
        }, cacheTrimStrategy, supplier);
        memoryTrimmableRegistry.registerMemoryTrimmable(countingMemoryCache);
        return countingMemoryCache;
    }
}
