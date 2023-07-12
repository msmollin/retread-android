package com.facebook.imagepipeline.producers;

import com.facebook.common.internal.Closeables;
import com.facebook.common.internal.ImmutableList;
import com.facebook.common.internal.ImmutableMap;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.VisibleForTesting;
import com.facebook.common.memory.PooledByteBufferFactory;
import com.facebook.common.memory.PooledByteBufferOutputStream;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.TriState;
import com.facebook.imageformat.DefaultImageFormats;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.nativecode.JpegTranscoder;
import com.facebook.imagepipeline.producers.JobScheduler;
import com.facebook.imagepipeline.request.ImageRequest;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ResizeAndRotateProducer implements Producer<EncodedImage> {
    @VisibleForTesting
    static final int DEFAULT_JPEG_QUALITY = 85;
    private static final String DOWNSAMPLE_ENUMERATOR_KEY = "downsampleEnumerator";
    private static final String FRACTION_KEY = "Fraction";
    private static final int FULL_ROUND = 360;
    private static final ImmutableList<Integer> INVERTED_EXIF_ORIENTATIONS = ImmutableList.of((Object[]) new Integer[]{2, 7, 4, 5});
    @VisibleForTesting
    static final int MAX_JPEG_SCALE_NUMERATOR = 8;
    @VisibleForTesting
    static final int MIN_TRANSFORM_INTERVAL_MS = 100;
    private static final String ORIGINAL_SIZE_KEY = "Original size";
    public static final String PRODUCER_NAME = "ResizeAndRotateProducer";
    private static final String REQUESTED_SIZE_KEY = "Requested size";
    private static final String ROTATION_ANGLE_KEY = "rotationAngle";
    private static final String SOFTWARE_ENUMERATOR_KEY = "softwareEnumerator";
    private final Executor mExecutor;
    private final Producer<EncodedImage> mInputProducer;
    private final PooledByteBufferFactory mPooledByteBufferFactory;
    private final boolean mResizingEnabled;
    private final boolean mUseDownsamplingRatio;

    @VisibleForTesting
    static int roundNumerator(float f, float f2) {
        return (int) (f2 + (f * 8.0f));
    }

    private static boolean shouldResize(int i) {
        return i < 8;
    }

    public ResizeAndRotateProducer(Executor executor, PooledByteBufferFactory pooledByteBufferFactory, boolean z, Producer<EncodedImage> producer, boolean z2) {
        this.mExecutor = (Executor) Preconditions.checkNotNull(executor);
        this.mPooledByteBufferFactory = (PooledByteBufferFactory) Preconditions.checkNotNull(pooledByteBufferFactory);
        this.mResizingEnabled = z;
        this.mInputProducer = (Producer) Preconditions.checkNotNull(producer);
        this.mUseDownsamplingRatio = z2;
    }

    @Override // com.facebook.imagepipeline.producers.Producer
    public void produceResults(Consumer<EncodedImage> consumer, ProducerContext producerContext) {
        this.mInputProducer.produceResults(new TransformingConsumer(consumer, producerContext), producerContext);
    }

    /* loaded from: classes.dex */
    private class TransformingConsumer extends DelegatingConsumer<EncodedImage, EncodedImage> {
        private boolean mIsCancelled;
        private final JobScheduler mJobScheduler;
        private final ProducerContext mProducerContext;

        public TransformingConsumer(final Consumer<EncodedImage> consumer, ProducerContext producerContext) {
            super(consumer);
            this.mIsCancelled = false;
            this.mProducerContext = producerContext;
            this.mJobScheduler = new JobScheduler(ResizeAndRotateProducer.this.mExecutor, new JobScheduler.JobRunnable() { // from class: com.facebook.imagepipeline.producers.ResizeAndRotateProducer.TransformingConsumer.1
                @Override // com.facebook.imagepipeline.producers.JobScheduler.JobRunnable
                public void run(EncodedImage encodedImage, int i) {
                    TransformingConsumer.this.doTransform(encodedImage, i);
                }
            }, 100);
            this.mProducerContext.addCallbacks(new BaseProducerContextCallbacks() { // from class: com.facebook.imagepipeline.producers.ResizeAndRotateProducer.TransformingConsumer.2
                @Override // com.facebook.imagepipeline.producers.BaseProducerContextCallbacks, com.facebook.imagepipeline.producers.ProducerContextCallbacks
                public void onIsIntermediateResultExpectedChanged() {
                    if (TransformingConsumer.this.mProducerContext.isIntermediateResultExpected()) {
                        TransformingConsumer.this.mJobScheduler.scheduleJob();
                    }
                }

                @Override // com.facebook.imagepipeline.producers.BaseProducerContextCallbacks, com.facebook.imagepipeline.producers.ProducerContextCallbacks
                public void onCancellationRequested() {
                    TransformingConsumer.this.mJobScheduler.clearJob();
                    TransformingConsumer.this.mIsCancelled = true;
                    consumer.onCancellation();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.facebook.imagepipeline.producers.BaseConsumer
        public void onNewResultImpl(@Nullable EncodedImage encodedImage, int i) {
            if (this.mIsCancelled) {
                return;
            }
            boolean isLast = isLast(i);
            if (encodedImage == null) {
                if (isLast) {
                    getConsumer().onNewResult(null, 1);
                    return;
                }
                return;
            }
            TriState shouldTransform = ResizeAndRotateProducer.shouldTransform(this.mProducerContext.getImageRequest(), encodedImage, ResizeAndRotateProducer.this.mResizingEnabled);
            if (isLast || shouldTransform != TriState.UNSET) {
                if (shouldTransform != TriState.YES) {
                    if (!this.mProducerContext.getImageRequest().getRotationOptions().canDeferUntilRendered() && encodedImage.getRotationAngle() != 0 && encodedImage.getRotationAngle() != -1) {
                        encodedImage = moveImage(encodedImage);
                        encodedImage.setRotationAngle(0);
                    }
                    getConsumer().onNewResult(encodedImage, i);
                } else if (this.mJobScheduler.updateJob(encodedImage, i)) {
                    if (isLast || this.mProducerContext.isIntermediateResultExpected()) {
                        this.mJobScheduler.scheduleJob();
                    }
                }
            }
        }

        private EncodedImage moveImage(EncodedImage encodedImage) {
            EncodedImage cloneOrNull = EncodedImage.cloneOrNull(encodedImage);
            encodedImage.close();
            return cloneOrNull;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r16v0, types: [com.facebook.imagepipeline.producers.ResizeAndRotateProducer$TransformingConsumer] */
        /* JADX WARN: Type inference failed for: r1v14, types: [boolean] */
        /* JADX WARN: Type inference failed for: r1v15 */
        /* JADX WARN: Type inference failed for: r1v19 */
        /* JADX WARN: Type inference failed for: r1v24 */
        /* JADX WARN: Type inference failed for: r1v25 */
        /* JADX WARN: Type inference failed for: r3v1, types: [com.facebook.imagepipeline.request.ImageRequest] */
        /* JADX WARN: Type inference failed for: r3v2 */
        /* JADX WARN: Type inference failed for: r3v3, types: [int] */
        /* JADX WARN: Type inference failed for: r3v4 */
        /* JADX WARN: Type inference failed for: r3v5 */
        public void doTransform(EncodedImage encodedImage, int i) {
            InputStream inputStream;
            this.mProducerContext.getListener().onProducerStart(this.mProducerContext.getId(), ResizeAndRotateProducer.PRODUCER_NAME);
            ?? imageRequest = this.mProducerContext.getImageRequest();
            PooledByteBufferOutputStream newOutputStream = ResizeAndRotateProducer.this.mPooledByteBufferFactory.newOutputStream();
            Map<String, String> map = null;
            try {
                try {
                    int softwareNumerator = ResizeAndRotateProducer.getSoftwareNumerator(imageRequest, encodedImage, ResizeAndRotateProducer.this.mResizingEnabled);
                    int determineSampleSize = DownsampleUtil.determineSampleSize(imageRequest, encodedImage);
                    int calculateDownsampleNumerator = ResizeAndRotateProducer.calculateDownsampleNumerator(determineSampleSize);
                    int i2 = ResizeAndRotateProducer.this.mUseDownsamplingRatio ? calculateDownsampleNumerator : softwareNumerator;
                    inputStream = encodedImage.getInputStream();
                    try {
                        ?? contains = ResizeAndRotateProducer.INVERTED_EXIF_ORIENTATIONS.contains(Integer.valueOf(encodedImage.getExifOrientation()));
                        try {
                            if (contains != 0) {
                                int forceRotatedInvertedExifOrientation = ResizeAndRotateProducer.getForceRotatedInvertedExifOrientation(imageRequest.getRotationOptions(), encodedImage);
                                Map<String, String> extraMap = getExtraMap(encodedImage, imageRequest, i2, calculateDownsampleNumerator, softwareNumerator, 0);
                                JpegTranscoder.transcodeJpegWithExifOrientation(inputStream, newOutputStream, forceRotatedInvertedExifOrientation, i2, 85);
                                contains = extraMap;
                            } else {
                                int rotationAngle = ResizeAndRotateProducer.getRotationAngle(imageRequest.getRotationOptions(), encodedImage);
                                Map<String, String> extraMap2 = getExtraMap(encodedImage, imageRequest, i2, calculateDownsampleNumerator, softwareNumerator, rotationAngle);
                                JpegTranscoder.transcodeJpeg(inputStream, newOutputStream, rotationAngle, i2, 85);
                                contains = extraMap2;
                            }
                            map = contains;
                        } catch (Exception e) {
                            e = e;
                            imageRequest = i;
                            map = contains;
                        }
                        try {
                            CloseableReference of = CloseableReference.of(newOutputStream.toByteBuffer());
                            try {
                                try {
                                    EncodedImage encodedImage2 = new EncodedImage(of);
                                    encodedImage2.setImageFormat(DefaultImageFormats.JPEG);
                                    try {
                                        encodedImage2.parseMetaData();
                                        this.mProducerContext.getListener().onProducerFinishWithSuccess(this.mProducerContext.getId(), ResizeAndRotateProducer.PRODUCER_NAME, map);
                                    } catch (Throwable th) {
                                        th = th;
                                    }
                                    try {
                                        getConsumer().onNewResult(encodedImage2, determineSampleSize != 1 ? i | 16 : i);
                                        EncodedImage.closeSafely(encodedImage2);
                                        CloseableReference.closeSafely(of);
                                        Closeables.closeQuietly(inputStream);
                                        newOutputStream.close();
                                    } catch (Throwable th2) {
                                        th = th2;
                                        EncodedImage.closeSafely(encodedImage2);
                                        throw th;
                                    }
                                } catch (Throwable th3) {
                                    th = th3;
                                    CloseableReference.closeSafely(of);
                                    throw th;
                                }
                            } catch (Throwable th4) {
                                th = th4;
                                CloseableReference.closeSafely(of);
                                throw th;
                            }
                        } catch (Exception e2) {
                            e = e2;
                            this.mProducerContext.getListener().onProducerFinishWithFailure(this.mProducerContext.getId(), ResizeAndRotateProducer.PRODUCER_NAME, e, map);
                            if (isLast(imageRequest)) {
                                getConsumer().onFailure(e);
                            }
                            Closeables.closeQuietly(inputStream);
                            newOutputStream.close();
                        }
                    } catch (Exception e3) {
                        e = e3;
                        imageRequest = i;
                    }
                } catch (Exception e4) {
                    e = e4;
                    imageRequest = i;
                    inputStream = null;
                } catch (Throwable th5) {
                    th = th5;
                    Closeables.closeQuietly((InputStream) null);
                    newOutputStream.close();
                    throw th;
                }
            } catch (Throwable th6) {
                th = th6;
                Closeables.closeQuietly((InputStream) null);
                newOutputStream.close();
                throw th;
            }
        }

        private Map<String, String> getExtraMap(EncodedImage encodedImage, ImageRequest imageRequest, int i, int i2, int i3, int i4) {
            String str;
            String str2;
            if (this.mProducerContext.getListener().requiresExtraMap(this.mProducerContext.getId())) {
                String str3 = encodedImage.getWidth() + "x" + encodedImage.getHeight();
                if (imageRequest.getResizeOptions() != null) {
                    str = imageRequest.getResizeOptions().width + "x" + imageRequest.getResizeOptions().height;
                } else {
                    str = "Unspecified";
                }
                if (i > 0) {
                    str2 = i + "/8";
                } else {
                    str2 = "";
                }
                HashMap hashMap = new HashMap();
                hashMap.put(ResizeAndRotateProducer.ORIGINAL_SIZE_KEY, str3);
                hashMap.put(ResizeAndRotateProducer.REQUESTED_SIZE_KEY, str);
                hashMap.put(ResizeAndRotateProducer.FRACTION_KEY, str2);
                hashMap.put("queueTime", String.valueOf(this.mJobScheduler.getQueuedTime()));
                hashMap.put(ResizeAndRotateProducer.DOWNSAMPLE_ENUMERATOR_KEY, Integer.toString(i2));
                hashMap.put(ResizeAndRotateProducer.SOFTWARE_ENUMERATOR_KEY, Integer.toString(i3));
                hashMap.put(ResizeAndRotateProducer.ROTATION_ANGLE_KEY, Integer.toString(i4));
                return ImmutableMap.copyOf((Map) hashMap);
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static TriState shouldTransform(ImageRequest imageRequest, EncodedImage encodedImage, boolean z) {
        if (encodedImage == null || encodedImage.getImageFormat() == ImageFormat.UNKNOWN) {
            return TriState.UNSET;
        }
        if (encodedImage.getImageFormat() != DefaultImageFormats.JPEG) {
            return TriState.NO;
        }
        return TriState.valueOf(shouldRotate(imageRequest.getRotationOptions(), encodedImage) || shouldResize(getSoftwareNumerator(imageRequest, encodedImage, z)));
    }

    @VisibleForTesting
    static float determineResizeRatio(ResizeOptions resizeOptions, int i, int i2) {
        if (resizeOptions == null) {
            return 1.0f;
        }
        float f = i;
        float f2 = i2;
        float max = Math.max(resizeOptions.width / f, resizeOptions.height / f2);
        if (f * max > resizeOptions.maxBitmapSize) {
            max = resizeOptions.maxBitmapSize / f;
        }
        return f2 * max > resizeOptions.maxBitmapSize ? resizeOptions.maxBitmapSize / f2 : max;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getSoftwareNumerator(ImageRequest imageRequest, EncodedImage encodedImage, boolean z) {
        ResizeOptions resizeOptions;
        int width;
        int height;
        if (z && (resizeOptions = imageRequest.getResizeOptions()) != null) {
            int rotationAngle = getRotationAngle(imageRequest.getRotationOptions(), encodedImage);
            boolean z2 = false;
            int forceRotatedInvertedExifOrientation = INVERTED_EXIF_ORIENTATIONS.contains(Integer.valueOf(encodedImage.getExifOrientation())) ? getForceRotatedInvertedExifOrientation(imageRequest.getRotationOptions(), encodedImage) : 0;
            if (rotationAngle == 90 || rotationAngle == 270 || forceRotatedInvertedExifOrientation == 5 || forceRotatedInvertedExifOrientation == 7) {
                z2 = true;
            }
            if (z2) {
                width = encodedImage.getHeight();
            } else {
                width = encodedImage.getWidth();
            }
            if (z2) {
                height = encodedImage.getWidth();
            } else {
                height = encodedImage.getHeight();
            }
            int roundNumerator = roundNumerator(determineResizeRatio(resizeOptions, width, height), resizeOptions.roundUpFraction);
            if (roundNumerator > 8) {
                return 8;
            }
            if (roundNumerator < 1) {
                return 1;
            }
            return roundNumerator;
        }
        return 8;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getRotationAngle(RotationOptions rotationOptions, EncodedImage encodedImage) {
        if (rotationOptions.rotationEnabled()) {
            int extractOrientationFromMetadata = extractOrientationFromMetadata(encodedImage);
            return rotationOptions.useImageMetadata() ? extractOrientationFromMetadata : (extractOrientationFromMetadata + rotationOptions.getForcedAngle()) % FULL_ROUND;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getForceRotatedInvertedExifOrientation(RotationOptions rotationOptions, EncodedImage encodedImage) {
        int indexOf = INVERTED_EXIF_ORIENTATIONS.indexOf(Integer.valueOf(encodedImage.getExifOrientation()));
        if (indexOf < 0) {
            throw new IllegalArgumentException("Only accepts inverted exif orientations");
        }
        return INVERTED_EXIF_ORIENTATIONS.get((indexOf + ((rotationOptions.useImageMetadata() ? 0 : rotationOptions.getForcedAngle()) / 90)) % INVERTED_EXIF_ORIENTATIONS.size()).intValue();
    }

    private static int extractOrientationFromMetadata(EncodedImage encodedImage) {
        int rotationAngle = encodedImage.getRotationAngle();
        if (rotationAngle == 90 || rotationAngle == 180 || rotationAngle == 270) {
            return encodedImage.getRotationAngle();
        }
        return 0;
    }

    private static boolean shouldRotate(RotationOptions rotationOptions, EncodedImage encodedImage) {
        return !rotationOptions.canDeferUntilRendered() && (getRotationAngle(rotationOptions, encodedImage) != 0 || shouldRotateUsingExifOrientation(rotationOptions, encodedImage));
    }

    private static boolean shouldRotateUsingExifOrientation(RotationOptions rotationOptions, EncodedImage encodedImage) {
        if (!rotationOptions.rotationEnabled() || rotationOptions.canDeferUntilRendered()) {
            encodedImage.setExifOrientation(0);
            return false;
        }
        return INVERTED_EXIF_ORIENTATIONS.contains(Integer.valueOf(encodedImage.getExifOrientation()));
    }

    @VisibleForTesting
    static int calculateDownsampleNumerator(int i) {
        return Math.max(1, 8 / i);
    }
}
