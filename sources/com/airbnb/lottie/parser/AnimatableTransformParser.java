package com.airbnb.lottie.parser;

import android.graphics.PointF;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.animatable.AnimatablePathValue;
import com.airbnb.lottie.model.animatable.AnimatableScaleValue;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import com.airbnb.lottie.model.animatable.AnimatableValue;
import com.airbnb.lottie.value.ScaleXY;
import java.io.IOException;

/* loaded from: classes.dex */
public class AnimatableTransformParser {
    private AnimatableTransformParser() {
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static AnimatableTransform parse(JsonReader jsonReader, LottieComposition lottieComposition) throws IOException {
        char c;
        boolean z = jsonReader.peek() == JsonToken.BEGIN_OBJECT;
        if (z) {
            jsonReader.beginObject();
        }
        AnimatablePathValue animatablePathValue = null;
        AnimatableScaleValue animatableScaleValue = null;
        AnimatableIntegerValue animatableIntegerValue = null;
        AnimatableValue<PointF, PointF> animatableValue = null;
        AnimatableFloatValue animatableFloatValue = null;
        AnimatableFloatValue animatableFloatValue2 = null;
        AnimatableFloatValue animatableFloatValue3 = null;
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            switch (nextName.hashCode()) {
                case 97:
                    if (nextName.equals("a")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case 111:
                    if (nextName.equals("o")) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                case 112:
                    if (nextName.equals("p")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case 114:
                    if (nextName.equals("r")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case 115:
                    if (nextName.equals("s")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case 3242:
                    if (nextName.equals("eo")) {
                        c = 7;
                        break;
                    }
                    c = 65535;
                    break;
                case 3656:
                    if (nextName.equals("rz")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case 3676:
                    if (nextName.equals("so")) {
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
                    jsonReader.beginObject();
                    while (jsonReader.hasNext()) {
                        if (jsonReader.nextName().equals("k")) {
                            animatablePathValue = AnimatablePathValueParser.parse(jsonReader, lottieComposition);
                        } else {
                            jsonReader.skipValue();
                        }
                    }
                    jsonReader.endObject();
                    continue;
                case 1:
                    animatableValue = AnimatablePathValueParser.parseSplitPath(jsonReader, lottieComposition);
                    continue;
                case 2:
                    animatableScaleValue = AnimatableValueParser.parseScale(jsonReader, lottieComposition);
                    continue;
                case 3:
                    lottieComposition.addWarning("Lottie doesn't support 3D layers.");
                    break;
                case 4:
                    break;
                case 5:
                    animatableIntegerValue = AnimatableValueParser.parseInteger(jsonReader, lottieComposition);
                    continue;
                case 6:
                    animatableFloatValue2 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
                    continue;
                case 7:
                    animatableFloatValue3 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
                    continue;
                default:
                    jsonReader.skipValue();
                    continue;
            }
            animatableFloatValue = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, false);
        }
        if (z) {
            jsonReader.endObject();
        }
        if (animatablePathValue == null) {
            Log.w(L.TAG, "Layer has no transform property. You may be using an unsupported layer type such as a camera.");
            animatablePathValue = new AnimatablePathValue();
        }
        AnimatablePathValue animatablePathValue2 = animatablePathValue;
        if (animatableScaleValue == null) {
            animatableScaleValue = new AnimatableScaleValue(new ScaleXY(1.0f, 1.0f));
        }
        AnimatableScaleValue animatableScaleValue2 = animatableScaleValue;
        if (animatableIntegerValue == null) {
            animatableIntegerValue = new AnimatableIntegerValue();
        }
        return new AnimatableTransform(animatablePathValue2, animatableValue, animatableScaleValue2, animatableFloatValue, animatableIntegerValue, animatableFloatValue2, animatableFloatValue3);
    }
}
