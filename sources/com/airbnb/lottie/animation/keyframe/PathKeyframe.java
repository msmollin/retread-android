package com.airbnb.lottie.animation.keyframe;

import android.graphics.Path;
import android.graphics.PointF;
import androidx.annotation.Nullable;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.value.Keyframe;

/* loaded from: classes.dex */
public class PathKeyframe extends Keyframe<PointF> {
    @Nullable
    private Path path;

    public PathKeyframe(LottieComposition lottieComposition, Keyframe<PointF> keyframe) {
        super(lottieComposition, keyframe.startValue, keyframe.endValue, keyframe.interpolator, keyframe.startFrame, keyframe.endFrame);
        boolean z = (this.endValue == 0 || this.startValue == 0 || !((PointF) this.startValue).equals(((PointF) this.endValue).x, ((PointF) this.endValue).y)) ? false : true;
        if (this.endValue == 0 || z) {
            return;
        }
        this.path = Utils.createPath((PointF) this.startValue, (PointF) this.endValue, keyframe.pathCp1, keyframe.pathCp2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nullable
    public Path getPath() {
        return this.path;
    }
}
