package com.airbnb.lottie.parser;

import android.util.JsonReader;
import com.airbnb.lottie.model.DocumentData;
import java.io.IOException;

/* loaded from: classes.dex */
public class DocumentDataParser implements ValueParser<DocumentData> {
    public static final DocumentDataParser INSTANCE = new DocumentDataParser();

    private DocumentDataParser() {
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.airbnb.lottie.parser.ValueParser
    public DocumentData parse(JsonReader jsonReader, float f) throws IOException {
        char c;
        jsonReader.beginObject();
        boolean z = true;
        String str = null;
        String str2 = null;
        double d = 0.0d;
        double d2 = 0.0d;
        double d3 = 0.0d;
        double d4 = 0.0d;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            int hashCode = nextName.hashCode();
            if (hashCode == 102) {
                if (nextName.equals("f")) {
                    c = 1;
                }
                c = 65535;
            } else if (hashCode == 106) {
                if (nextName.equals("j")) {
                    c = 3;
                }
                c = 65535;
            } else if (hashCode == 3261) {
                if (nextName.equals("fc")) {
                    c = 7;
                }
                c = 65535;
            } else if (hashCode == 3452) {
                if (nextName.equals("lh")) {
                    c = 5;
                }
                c = 65535;
            } else if (hashCode == 3463) {
                if (nextName.equals("ls")) {
                    c = 6;
                }
                c = 65535;
            } else if (hashCode == 3543) {
                if (nextName.equals("of")) {
                    c = '\n';
                }
                c = 65535;
            } else if (hashCode == 3664) {
                if (nextName.equals("sc")) {
                    c = '\b';
                }
                c = 65535;
            } else if (hashCode == 3684) {
                if (nextName.equals("sw")) {
                    c = '\t';
                }
                c = 65535;
            } else if (hashCode != 3710) {
                switch (hashCode) {
                    case 115:
                        if (nextName.equals("s")) {
                            c = 2;
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
                    default:
                        c = 65535;
                        break;
                }
            } else {
                if (nextName.equals("tr")) {
                    c = 4;
                }
                c = 65535;
            }
            switch (c) {
                case 0:
                    str = jsonReader.nextString();
                    break;
                case 1:
                    str2 = jsonReader.nextString();
                    break;
                case 2:
                    d = jsonReader.nextDouble();
                    break;
                case 3:
                    i = jsonReader.nextInt();
                    break;
                case 4:
                    i2 = jsonReader.nextInt();
                    break;
                case 5:
                    d2 = jsonReader.nextDouble();
                    break;
                case 6:
                    d3 = jsonReader.nextDouble();
                    break;
                case 7:
                    i3 = JsonUtils.jsonToColor(jsonReader);
                    break;
                case '\b':
                    i4 = JsonUtils.jsonToColor(jsonReader);
                    break;
                case '\t':
                    d4 = jsonReader.nextDouble();
                    break;
                case '\n':
                    z = jsonReader.nextBoolean();
                    break;
                default:
                    jsonReader.skipValue();
                    break;
            }
        }
        jsonReader.endObject();
        return new DocumentData(str, str2, d, i, i2, d2, d3, i3, i4, d4, z);
    }
}
