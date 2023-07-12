package com.airbnb.lottie.parser;

import android.graphics.Color;
import android.graphics.Rect;
import android.util.JsonReader;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableTextFrame;
import com.airbnb.lottie.model.animatable.AnimatableTextProperties;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import com.airbnb.lottie.model.content.ContentModel;
import com.airbnb.lottie.model.layer.Layer;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.value.Keyframe;
import com.facebook.appevents.UserDataStore;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/* loaded from: classes.dex */
public class LayerParser {
    private LayerParser() {
    }

    public static Layer parse(LottieComposition lottieComposition) {
        Rect bounds = lottieComposition.getBounds();
        return new Layer(Collections.emptyList(), lottieComposition, "__container", -1L, Layer.LayerType.PreComp, -1L, null, Collections.emptyList(), new AnimatableTransform(), 0, 0, 0, 0.0f, 0.0f, bounds.width(), bounds.height(), null, null, Collections.emptyList(), Layer.MatteType.None, null);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v10 */
    /* JADX WARN: Type inference failed for: r2v12 */
    /* JADX WARN: Type inference failed for: r2v14 */
    /* JADX WARN: Type inference failed for: r2v16 */
    /* JADX WARN: Type inference failed for: r2v18 */
    /* JADX WARN: Type inference failed for: r2v20 */
    /* JADX WARN: Type inference failed for: r2v22 */
    /* JADX WARN: Type inference failed for: r2v24 */
    /* JADX WARN: Type inference failed for: r2v26 */
    /* JADX WARN: Type inference failed for: r2v28 */
    /* JADX WARN: Type inference failed for: r2v30 */
    /* JADX WARN: Type inference failed for: r2v32 */
    /* JADX WARN: Type inference failed for: r2v34 */
    /* JADX WARN: Type inference failed for: r2v36 */
    /* JADX WARN: Type inference failed for: r2v38 */
    /* JADX WARN: Type inference failed for: r2v40 */
    /* JADX WARN: Type inference failed for: r2v42 */
    /* JADX WARN: Type inference failed for: r2v44 */
    /* JADX WARN: Type inference failed for: r2v46 */
    /* JADX WARN: Type inference failed for: r2v48 */
    /* JADX WARN: Type inference failed for: r2v50 */
    /* JADX WARN: Type inference failed for: r2v51 */
    /* JADX WARN: Type inference failed for: r2v77 */
    /* JADX WARN: Type inference failed for: r2v8 */
    public static Layer parse(JsonReader jsonReader, LottieComposition lottieComposition) throws IOException {
        ArrayList arrayList;
        ?? r2;
        char c;
        Layer.MatteType matteType = Layer.MatteType.None;
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        jsonReader.beginObject();
        boolean z = false;
        Layer.MatteType matteType2 = matteType;
        Layer.LayerType layerType = null;
        String str = null;
        AnimatableTransform animatableTransform = null;
        AnimatableTextFrame animatableTextFrame = null;
        AnimatableTextProperties animatableTextProperties = null;
        AnimatableFloatValue animatableFloatValue = null;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        long j = 0;
        float f = 0.0f;
        float f2 = 0.0f;
        long j2 = -1;
        float f3 = 1.0f;
        String str2 = "UNSET";
        String str3 = null;
        float f4 = 0.0f;
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            switch (nextName.hashCode()) {
                case -995424086:
                    if (nextName.equals("parent")) {
                        r2 = 4;
                        break;
                    }
                    r2 = -1;
                    break;
                case -903568142:
                    if (nextName.equals("shapes")) {
                        r2 = 11;
                        break;
                    }
                    r2 = -1;
                    break;
                case 104:
                    if (nextName.equals("h")) {
                        r2 = 17;
                        break;
                    }
                    r2 = -1;
                    break;
                case 116:
                    if (nextName.equals("t")) {
                        r2 = 12;
                        break;
                    }
                    r2 = -1;
                    break;
                case 119:
                    if (nextName.equals("w")) {
                        r2 = 16;
                        break;
                    }
                    r2 = -1;
                    break;
                case 3177:
                    if (nextName.equals("cl")) {
                        r2 = 21;
                        break;
                    }
                    r2 = -1;
                    break;
                case 3233:
                    if (nextName.equals("ef")) {
                        r2 = 13;
                        break;
                    }
                    r2 = -1;
                    break;
                case 3367:
                    if (nextName.equals("ip")) {
                        r2 = 18;
                        break;
                    }
                    r2 = -1;
                    break;
                case 3432:
                    if (nextName.equals("ks")) {
                        r2 = 8;
                        break;
                    }
                    r2 = -1;
                    break;
                case 3519:
                    if (nextName.equals("nm")) {
                        r2 = z;
                        break;
                    }
                    r2 = -1;
                    break;
                case 3553:
                    if (nextName.equals("op")) {
                        r2 = 19;
                        break;
                    }
                    r2 = -1;
                    break;
                case 3664:
                    if (nextName.equals("sc")) {
                        r2 = 7;
                        break;
                    }
                    r2 = -1;
                    break;
                case 3669:
                    if (nextName.equals("sh")) {
                        r2 = 6;
                        break;
                    }
                    r2 = -1;
                    break;
                case 3679:
                    if (nextName.equals("sr")) {
                        r2 = 14;
                        break;
                    }
                    r2 = -1;
                    break;
                case 3681:
                    if (nextName.equals(UserDataStore.STATE)) {
                        r2 = 15;
                        break;
                    }
                    r2 = -1;
                    break;
                case 3684:
                    if (nextName.equals("sw")) {
                        r2 = 5;
                        break;
                    }
                    r2 = -1;
                    break;
                case 3705:
                    if (nextName.equals("tm")) {
                        r2 = 20;
                        break;
                    }
                    r2 = -1;
                    break;
                case 3712:
                    if (nextName.equals("tt")) {
                        r2 = 9;
                        break;
                    }
                    r2 = -1;
                    break;
                case 3717:
                    if (nextName.equals("ty")) {
                        r2 = 3;
                        break;
                    }
                    r2 = -1;
                    break;
                case 104415:
                    if (nextName.equals("ind")) {
                        r2 = 1;
                        break;
                    }
                    r2 = -1;
                    break;
                case 108390670:
                    if (nextName.equals("refId")) {
                        r2 = 2;
                        break;
                    }
                    r2 = -1;
                    break;
                case 1441620890:
                    if (nextName.equals("masksProperties")) {
                        r2 = 10;
                        break;
                    }
                    r2 = -1;
                    break;
                default:
                    r2 = -1;
                    break;
            }
            switch (r2) {
                case 0:
                    str2 = jsonReader.nextString();
                    break;
                case 1:
                    j = jsonReader.nextInt();
                    break;
                case 2:
                    str = jsonReader.nextString();
                    break;
                case 3:
                    int nextInt = jsonReader.nextInt();
                    if (nextInt < Layer.LayerType.Unknown.ordinal()) {
                        layerType = Layer.LayerType.values()[nextInt];
                        break;
                    } else {
                        layerType = Layer.LayerType.Unknown;
                        break;
                    }
                case 4:
                    j2 = jsonReader.nextInt();
                    break;
                case 5:
                    i = (int) (jsonReader.nextInt() * Utils.dpScale());
                    break;
                case 6:
                    i2 = (int) (jsonReader.nextInt() * Utils.dpScale());
                    break;
                case 7:
                    i3 = Color.parseColor(jsonReader.nextString());
                    break;
                case 8:
                    animatableTransform = AnimatableTransformParser.parse(jsonReader, lottieComposition);
                    break;
                case 9:
                    matteType2 = Layer.MatteType.values()[jsonReader.nextInt()];
                    break;
                case 10:
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        arrayList2.add(MaskParser.parse(jsonReader, lottieComposition));
                    }
                    jsonReader.endArray();
                    break;
                case 11:
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        ContentModel parse = ContentModelParser.parse(jsonReader, lottieComposition);
                        if (parse != null) {
                            arrayList3.add(parse);
                        }
                    }
                    jsonReader.endArray();
                    break;
                case 12:
                    jsonReader.beginObject();
                    while (jsonReader.hasNext()) {
                        String nextName2 = jsonReader.nextName();
                        int hashCode = nextName2.hashCode();
                        if (hashCode != 97) {
                            if (hashCode == 100 && nextName2.equals("d")) {
                                c = 0;
                            }
                            c = 65535;
                        } else {
                            if (nextName2.equals("a")) {
                                c = 1;
                            }
                            c = 65535;
                        }
                        switch (c) {
                            case 0:
                                animatableTextFrame = AnimatableValueParser.parseDocumentData(jsonReader, lottieComposition);
                                break;
                            case 1:
                                jsonReader.beginArray();
                                if (jsonReader.hasNext()) {
                                    animatableTextProperties = AnimatableTextPropertiesParser.parse(jsonReader, lottieComposition);
                                }
                                while (jsonReader.hasNext()) {
                                    jsonReader.skipValue();
                                }
                                jsonReader.endArray();
                                break;
                            default:
                                jsonReader.skipValue();
                                break;
                        }
                    }
                    jsonReader.endObject();
                    break;
                case 13:
                    jsonReader.beginArray();
                    ArrayList arrayList4 = new ArrayList();
                    while (jsonReader.hasNext()) {
                        jsonReader.beginObject();
                        while (jsonReader.hasNext()) {
                            String nextName3 = jsonReader.nextName();
                            if (((nextName3.hashCode() == 3519 && nextName3.equals("nm")) ? (char) 0 : (char) 65535) == 0) {
                                arrayList4.add(jsonReader.nextString());
                            } else {
                                jsonReader.skipValue();
                            }
                        }
                        jsonReader.endObject();
                    }
                    jsonReader.endArray();
                    lottieComposition.addWarning("Lottie doesn't support layer effects. If you are using them for  fills, strokes, trim paths etc. then try adding them directly as contents  in your shape. Found: " + arrayList4);
                    break;
                case 14:
                    f3 = (float) jsonReader.nextDouble();
                    break;
                case 15:
                    f2 = (float) jsonReader.nextDouble();
                    break;
                case 16:
                    i4 = (int) (jsonReader.nextInt() * Utils.dpScale());
                    break;
                case 17:
                    i5 = (int) (jsonReader.nextInt() * Utils.dpScale());
                    break;
                case 18:
                    f4 = (float) jsonReader.nextDouble();
                    break;
                case 19:
                    f = (float) jsonReader.nextDouble();
                    break;
                case 20:
                    animatableFloatValue = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, z);
                    break;
                case 21:
                    str3 = jsonReader.nextString();
                    break;
                default:
                    jsonReader.skipValue();
                    break;
            }
            z = false;
        }
        jsonReader.endObject();
        float f5 = f4 / f3;
        float f6 = f / f3;
        ArrayList arrayList5 = new ArrayList();
        if (f5 > 0.0f) {
            arrayList = arrayList5;
            arrayList.add(new Keyframe(lottieComposition, Float.valueOf(0.0f), Float.valueOf(0.0f), null, 0.0f, Float.valueOf(f5)));
        } else {
            arrayList = arrayList5;
        }
        if (f6 <= 0.0f) {
            f6 = lottieComposition.getEndFrame();
        }
        arrayList.add(new Keyframe(lottieComposition, Float.valueOf(1.0f), Float.valueOf(1.0f), null, f5, Float.valueOf(f6)));
        arrayList.add(new Keyframe(lottieComposition, Float.valueOf(0.0f), Float.valueOf(0.0f), null, f6, Float.valueOf(Float.MAX_VALUE)));
        if (str2.endsWith(".ai") || "ai".equals(str3)) {
            lottieComposition.addWarning("Convert your Illustrator layers to shape layers.");
        }
        return new Layer(arrayList3, lottieComposition, str2, j, layerType, j2, str, arrayList2, animatableTransform, i, i2, i3, f3, f2, i4, i5, animatableTextFrame, animatableTextProperties, arrayList, matteType2, animatableFloatValue);
    }
}
