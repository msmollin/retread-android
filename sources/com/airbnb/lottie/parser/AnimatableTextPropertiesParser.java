package com.airbnb.lottie.parser;

import android.util.JsonReader;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableColorValue;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableTextProperties;
import java.io.IOException;

/* loaded from: classes.dex */
public class AnimatableTextPropertiesParser {
    private AnimatableTextPropertiesParser() {
    }

    public static AnimatableTextProperties parse(JsonReader jsonReader, LottieComposition lottieComposition) throws IOException {
        jsonReader.beginObject();
        AnimatableTextProperties animatableTextProperties = null;
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            char c = 65535;
            if (nextName.hashCode() == 97 && nextName.equals("a")) {
                c = 0;
            }
            if (c == 0) {
                animatableTextProperties = parseAnimatableTextProperties(jsonReader, lottieComposition);
            } else {
                jsonReader.skipValue();
            }
        }
        jsonReader.endObject();
        return animatableTextProperties == null ? new AnimatableTextProperties(null, null, null, null) : animatableTextProperties;
    }

    private static AnimatableTextProperties parseAnimatableTextProperties(JsonReader jsonReader, LottieComposition lottieComposition) throws IOException {
        jsonReader.beginObject();
        AnimatableColorValue animatableColorValue = null;
        AnimatableColorValue animatableColorValue2 = null;
        AnimatableFloatValue animatableFloatValue = null;
        AnimatableFloatValue animatableFloatValue2 = null;
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            char c = 65535;
            int hashCode = nextName.hashCode();
            if (hashCode != 116) {
                if (hashCode != 3261) {
                    if (hashCode != 3664) {
                        if (hashCode == 3684 && nextName.equals("sw")) {
                            c = 2;
                        }
                    } else if (nextName.equals("sc")) {
                        c = 1;
                    }
                } else if (nextName.equals("fc")) {
                    c = 0;
                }
            } else if (nextName.equals("t")) {
                c = 3;
            }
            switch (c) {
                case 0:
                    animatableColorValue = AnimatableValueParser.parseColor(jsonReader, lottieComposition);
                    break;
                case 1:
                    animatableColorValue2 = AnimatableValueParser.parseColor(jsonReader, lottieComposition);
                    break;
                case 2:
                    animatableFloatValue = AnimatableValueParser.parseFloat(jsonReader, lottieComposition);
                    break;
                case 3:
                    animatableFloatValue2 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition);
                    break;
                default:
                    jsonReader.skipValue();
                    break;
            }
        }
        jsonReader.endObject();
        return new AnimatableTextProperties(animatableColorValue, animatableColorValue2, animatableFloatValue, animatableFloatValue2);
    }
}
