package com.github.mmin18.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.RSRuntimeException;
import androidx.renderscript.Allocation;
import androidx.renderscript.Element;
import androidx.renderscript.RenderScript;
import androidx.renderscript.ScriptIntrinsicBlur;

/* loaded from: classes.dex */
public class AndroidXBlurImpl implements BlurImpl {
    static Boolean DEBUG;
    private Allocation mBlurInput;
    private Allocation mBlurOutput;
    private ScriptIntrinsicBlur mBlurScript;
    private RenderScript mRenderScript;

    @Override // com.github.mmin18.widget.BlurImpl
    public boolean prepare(Context context, Bitmap bitmap, float f) {
        if (this.mRenderScript == null) {
            try {
                this.mRenderScript = RenderScript.create(context);
                this.mBlurScript = ScriptIntrinsicBlur.create(this.mRenderScript, Element.U8_4(this.mRenderScript));
            } catch (RSRuntimeException e) {
                if (isDebug(context)) {
                    throw e;
                }
                release();
                return false;
            }
        }
        this.mBlurScript.setRadius(f);
        this.mBlurInput = Allocation.createFromBitmap(this.mRenderScript, bitmap, Allocation.MipmapControl.MIPMAP_NONE, 1);
        this.mBlurOutput = Allocation.createTyped(this.mRenderScript, this.mBlurInput.getType());
        return true;
    }

    @Override // com.github.mmin18.widget.BlurImpl
    public void release() {
        if (this.mBlurInput != null) {
            this.mBlurInput.destroy();
            this.mBlurInput = null;
        }
        if (this.mBlurOutput != null) {
            this.mBlurOutput.destroy();
            this.mBlurOutput = null;
        }
        if (this.mBlurScript != null) {
            this.mBlurScript.destroy();
            this.mBlurScript = null;
        }
        if (this.mRenderScript != null) {
            this.mRenderScript.destroy();
            this.mRenderScript = null;
        }
    }

    @Override // com.github.mmin18.widget.BlurImpl
    public void blur(Bitmap bitmap, Bitmap bitmap2) {
        this.mBlurInput.copyFrom(bitmap);
        this.mBlurScript.setInput(this.mBlurInput);
        this.mBlurScript.forEach(this.mBlurOutput);
        this.mBlurOutput.copyTo(bitmap2);
    }

    static boolean isDebug(Context context) {
        if (DEBUG == null && context != null) {
            DEBUG = Boolean.valueOf((context.getApplicationInfo().flags & 2) != 0);
        }
        return DEBUG == Boolean.TRUE;
    }
}
