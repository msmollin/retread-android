package com.airbnb.lottie.parser;

import android.graphics.PointF;
import android.util.JsonReader;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import androidx.annotation.Nullable;
import androidx.collection.SparseArrayCompat;
import androidx.core.view.animation.PathInterpolatorCompat;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.value.Keyframe;
import java.io.IOException;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
class KeyframeParser {
    private static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();
    private static final float MAX_CP_VALUE = 100.0f;
    private static SparseArrayCompat<WeakReference<Interpolator>> pathInterpolatorCache;

    KeyframeParser() {
    }

    private static SparseArrayCompat<WeakReference<Interpolator>> pathInterpolatorCache() {
        if (pathInterpolatorCache == null) {
            pathInterpolatorCache = new SparseArrayCompat<>();
        }
        return pathInterpolatorCache;
    }

    @Nullable
    private static WeakReference<Interpolator> getInterpolator(int i) {
        WeakReference<Interpolator> weakReference;
        synchronized (KeyframeParser.class) {
            weakReference = pathInterpolatorCache().get(i);
        }
        return weakReference;
    }

    private static void putInterpolator(int i, WeakReference<Interpolator> weakReference) {
        synchronized (KeyframeParser.class) {
            pathInterpolatorCache.put(i, weakReference);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> Keyframe<T> parse(JsonReader jsonReader, LottieComposition lottieComposition, float f, ValueParser<T> valueParser, boolean z) throws IOException {
        if (z) {
            return parseKeyframe(lottieComposition, jsonReader, f, valueParser);
        }
        return parseStaticValue(jsonReader, f, valueParser);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private static <T> Keyframe<T> parseKeyframe(LottieComposition lottieComposition, JsonReader jsonReader, float f, ValueParser<T> valueParser) throws IOException {
        Interpolator interpolator;
        char c;
        jsonReader.beginObject();
        PointF pointF = null;
        PointF pointF2 = null;
        T t = null;
        T t2 = null;
        PointF pointF3 = null;
        PointF pointF4 = null;
        float f2 = 0.0f;
        boolean z = false;
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            switch (nextName.hashCode()) {
                case 101:
                    if (nextName.equals("e")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case 104:
                    if (nextName.equals("h")) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                case 105:
                    if (nextName.equals("i")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case 111:
                    if (nextName.equals("o")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case 115:
                    if (nextName.equals("s")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case 116:
                    if (nextName.equals("t")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case 3701:
                    if (nextName.equals("ti")) {
                        c = 7;
                        break;
                    }
                    c = 65535;
                    break;
                case 3707:
                    if (nextName.equals("to")) {
                        c = 6;
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
                    f2 = (float) jsonReader.nextDouble();
                    break;
                case 1:
                    t = valueParser.parse(jsonReader, f);
                    break;
                case 2:
                    t2 = valueParser.parse(jsonReader, f);
                    break;
                case 3:
                    pointF = JsonUtils.jsonToPoint(jsonReader, f);
                    break;
                case 4:
                    pointF2 = JsonUtils.jsonToPoint(jsonReader, f);
                    break;
                case 5:
                    z = jsonReader.nextInt() == 1;
                    break;
                case 6:
                    pointF3 = JsonUtils.jsonToPoint(jsonReader, f);
                    break;
                case 7:
                    pointF4 = JsonUtils.jsonToPoint(jsonReader, f);
                    break;
                default:
                    jsonReader.skipValue();
                    break;
            }
        }
        jsonReader.endObject();
        if (z) {
            interpolator = LINEAR_INTERPOLATOR;
            t2 = t;
        } else if (pointF != null && pointF2 != null) {
            float f3 = -f;
            pointF.x = MiscUtils.clamp(pointF.x, f3, f);
            pointF.y = MiscUtils.clamp(pointF.y, -100.0f, (float) MAX_CP_VALUE);
            pointF2.x = MiscUtils.clamp(pointF2.x, f3, f);
            pointF2.y = MiscUtils.clamp(pointF2.y, -100.0f, (float) MAX_CP_VALUE);
            int hashFor = Utils.hashFor(pointF.x, pointF.y, pointF2.x, pointF2.y);
            WeakReference<Interpolator> interpolator2 = getInterpolator(hashFor);
            Interpolator interpolator3 = interpolator2 != null ? interpolator2.get() : null;
            if (interpolator2 == null || interpolator3 == null) {
                interpolator3 = PathInterpolatorCompat.create(pointF.x / f, pointF.y / f, pointF2.x / f, pointF2.y / f);
                try {
                    putInterpolator(hashFor, new WeakReference(interpolator3));
                } catch (ArrayIndexOutOfBoundsException unused) {
                }
            }
            interpolator = interpolator3;
        } else {
            interpolator = LINEAR_INTERPOLATOR;
        }
        Keyframe<T> keyframe = new Keyframe<>(lottieComposition, t, t2, interpolator, f2, null);
        keyframe.pathCp1 = pointF3;
        keyframe.pathCp2 = pointF4;
        return keyframe;
    }

    private static <T> Keyframe<T> parseStaticValue(JsonReader jsonReader, float f, ValueParser<T> valueParser) throws IOException {
        return new Keyframe<>(valueParser.parse(jsonReader, f));
    }
}
