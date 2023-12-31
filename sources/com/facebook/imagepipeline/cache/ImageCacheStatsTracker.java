package com.facebook.imagepipeline.cache;

import com.facebook.cache.common.CacheKey;

/* loaded from: classes.dex */
public interface ImageCacheStatsTracker {
    void onBitmapCacheHit(CacheKey cacheKey);

    void onBitmapCacheMiss();

    void onBitmapCachePut();

    void onDiskCacheGetFail();

    void onDiskCacheHit();

    void onDiskCacheMiss();

    void onMemoryCacheHit(CacheKey cacheKey);

    void onMemoryCacheMiss();

    void onMemoryCachePut();

    void onStagingAreaHit(CacheKey cacheKey);

    void onStagingAreaMiss();

    void registerBitmapMemoryCache(CountingMemoryCache<?, ?> countingMemoryCache);

    void registerEncodedMemoryCache(CountingMemoryCache<?, ?> countingMemoryCache);
}
