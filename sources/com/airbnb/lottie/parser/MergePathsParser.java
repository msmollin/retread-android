package com.airbnb.lottie.parser;

import android.util.JsonReader;
import com.airbnb.lottie.model.content.MergePaths;
import java.io.IOException;

/* loaded from: classes.dex */
class MergePathsParser {
    private MergePathsParser() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static MergePaths parse(JsonReader jsonReader) throws IOException {
        String str = null;
        MergePaths.MergePathsMode mergePathsMode = null;
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            char c = 65535;
            int hashCode = nextName.hashCode();
            if (hashCode != 3488) {
                if (hashCode == 3519 && nextName.equals("nm")) {
                    c = 0;
                }
            } else if (nextName.equals("mm")) {
                c = 1;
            }
            switch (c) {
                case 0:
                    str = jsonReader.nextString();
                    break;
                case 1:
                    mergePathsMode = MergePaths.MergePathsMode.forId(jsonReader.nextInt());
                    break;
                default:
                    jsonReader.skipValue();
                    break;
            }
        }
        return new MergePaths(str, mergePathsMode);
    }
}
