package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.LinearSystem;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class Chain {
    private static final boolean DEBUG = false;

    Chain() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void applyChainConstraints(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, int i) {
        int i2;
        int i3;
        ChainHead[] chainHeadArr;
        if (i == 0) {
            int i4 = constraintWidgetContainer.mHorizontalChainsSize;
            chainHeadArr = constraintWidgetContainer.mHorizontalChainsArray;
            i3 = i4;
            i2 = 0;
        } else {
            i2 = 2;
            i3 = constraintWidgetContainer.mVerticalChainsSize;
            chainHeadArr = constraintWidgetContainer.mVerticalChainsArray;
        }
        for (int i5 = 0; i5 < i3; i5++) {
            ChainHead chainHead = chainHeadArr[i5];
            chainHead.define();
            if (constraintWidgetContainer.optimizeFor(4)) {
                if (!Optimizer.applyChainOptimized(constraintWidgetContainer, linearSystem, i, i2, chainHead)) {
                    applyChainConstraints(constraintWidgetContainer, linearSystem, i, i2, chainHead);
                }
            } else {
                applyChainConstraints(constraintWidgetContainer, linearSystem, i, i2, chainHead);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0035, code lost:
        if (r2.mHorizontalChainStyle == 2) goto L14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0037, code lost:
        r5 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0039, code lost:
        r5 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0053, code lost:
        if (r2.mVerticalChainStyle == 2) goto L14;
     */
    /* JADX WARN: Removed duplicated region for block: B:200:0x03b4  */
    /* JADX WARN: Removed duplicated region for block: B:201:0x03b7  */
    /* JADX WARN: Removed duplicated region for block: B:214:0x03dc  */
    /* JADX WARN: Removed duplicated region for block: B:264:0x04b8  */
    /* JADX WARN: Removed duplicated region for block: B:270:0x04f1  */
    /* JADX WARN: Removed duplicated region for block: B:276:0x0509 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:280:0x051b  */
    /* JADX WARN: Removed duplicated region for block: B:281:0x0520  */
    /* JADX WARN: Removed duplicated region for block: B:284:0x0526  */
    /* JADX WARN: Removed duplicated region for block: B:285:0x052b  */
    /* JADX WARN: Removed duplicated region for block: B:287:0x052f  */
    /* JADX WARN: Removed duplicated region for block: B:293:0x0541  */
    /* JADX WARN: Removed duplicated region for block: B:295:0x054b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:320:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0164  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x019e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static void applyChainConstraints(androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer r48, androidx.constraintlayout.solver.LinearSystem r49, int r50, int r51, androidx.constraintlayout.solver.widgets.ChainHead r52) {
        /*
            Method dump skipped, instructions count: 1391
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.Chain.applyChainConstraints(androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer, androidx.constraintlayout.solver.LinearSystem, int, int, androidx.constraintlayout.solver.widgets.ChainHead):void");
    }
}
