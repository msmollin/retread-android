package com.daasuu.gpuv.egl.filter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Size;

/* loaded from: classes.dex */
public abstract class GlOverlayFilter extends GlFilter {
    private static final String FRAGMENT_SHADER = "precision mediump float;\nvarying vec2 vTextureCoord;\nuniform lowp sampler2D sTexture;\nuniform lowp sampler2D oTexture;\nvoid main() {\n   lowp vec4 textureColor = texture2D(sTexture, vTextureCoord);\n   lowp vec4 textureColor2 = texture2D(oTexture, vTextureCoord);\n   \n   gl_FragColor = mix(textureColor, textureColor2, textureColor2.a);\n}\n";
    private Bitmap bitmap;
    protected Size inputResolution;
    private int[] textures;

    protected abstract void drawCanvas(Canvas canvas);

    public GlOverlayFilter() {
        super("attribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\ngl_Position = aPosition;\nvTextureCoord = aTextureCoord.xy;\n}\n", FRAGMENT_SHADER);
        this.textures = new int[1];
        this.bitmap = null;
        this.inputResolution = new Size(1280, 720);
    }

    public void setResolution(Size size) {
        this.inputResolution = size;
    }

    @Override // com.daasuu.gpuv.egl.filter.GlFilter
    public void setFrameSize(int i, int i2) {
        super.setFrameSize(i, i2);
        setResolution(new Size(i, i2));
    }

    private void createBitmap() {
        releaseBitmap(this.bitmap);
        this.bitmap = Bitmap.createBitmap(this.inputResolution.getWidth(), this.inputResolution.getHeight(), Bitmap.Config.ARGB_8888);
    }

    @Override // com.daasuu.gpuv.egl.filter.GlFilter
    public void setup() {
        super.setup();
        GLES20.glGenTextures(1, this.textures, 0);
        GLES20.glBindTexture(3553, this.textures[0]);
        GLES20.glTexParameteri(3553, 10241, 9729);
        GLES20.glTexParameteri(3553, 10240, 9729);
        GLES20.glTexParameteri(3553, 10242, 33071);
        GLES20.glTexParameteri(3553, 10243, 33071);
        createBitmap();
    }

    @Override // com.daasuu.gpuv.egl.filter.GlFilter
    public void onDraw() {
        if (this.bitmap == null) {
            createBitmap();
        }
        if (this.bitmap.getWidth() != this.inputResolution.getWidth() || this.bitmap.getHeight() != this.inputResolution.getHeight()) {
            createBitmap();
        }
        this.bitmap.eraseColor(Color.argb(0, 0, 0, 0));
        Canvas canvas = new Canvas(this.bitmap);
        canvas.scale(1.0f, -1.0f, canvas.getWidth() / 2, canvas.getHeight() / 2);
        drawCanvas(canvas);
        int handle = getHandle("oTexture");
        GLES20.glActiveTexture(33987);
        GLES20.glBindTexture(3553, this.textures[0]);
        if (this.bitmap != null && !this.bitmap.isRecycled()) {
            GLUtils.texImage2D(3553, 0, 6408, this.bitmap, 0);
        }
        GLES20.glUniform1i(handle, 3);
    }

    public static void releaseBitmap(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        bitmap.recycle();
    }
}
