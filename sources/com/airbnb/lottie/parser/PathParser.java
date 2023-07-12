package com.airbnb.lottie.parser;

import android.graphics.PointF;
import android.util.JsonReader;
import java.io.IOException;

/* loaded from: classes.dex */
public class PathParser implements ValueParser<PointF> {
    public static final PathParser INSTANCE = new PathParser();

    private PathParser() {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.airbnb.lottie.parser.ValueParser
    public PointF parse(JsonReader jsonReader, float f) throws IOException {
        return JsonUtils.jsonToPoint(jsonReader, f);
    }
}
