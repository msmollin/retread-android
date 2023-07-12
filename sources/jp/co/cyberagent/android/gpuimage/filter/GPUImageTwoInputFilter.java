package jp.co.cyberagent.android.gpuimage.filter;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import jp.co.cyberagent.android.gpuimage.util.OpenGlUtils;
import jp.co.cyberagent.android.gpuimage.util.Rotation;
import jp.co.cyberagent.android.gpuimage.util.TextureRotationUtil;

/* loaded from: classes2.dex */
public class GPUImageTwoInputFilter extends GPUImageFilter {
    private static final String VERTEX_SHADER = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\nattribute vec4 inputTextureCoordinate2;\n \nvarying vec2 textureCoordinate;\nvarying vec2 textureCoordinate2;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n    textureCoordinate2 = inputTextureCoordinate2.xy;\n}";
    private Bitmap bitmap;
    private int filterInputTextureUniform2;
    private int filterSecondTextureCoordinateAttribute;
    private int filterSourceTexture2;
    private ByteBuffer texture2CoordinatesBuffer;

    public GPUImageTwoInputFilter(String str) {
        this(VERTEX_SHADER, str);
    }

    public GPUImageTwoInputFilter(String str, String str2) {
        super(str, str2);
        this.filterSourceTexture2 = -1;
        setRotation(Rotation.NORMAL, false, false);
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onInit() {
        super.onInit();
        this.filterSecondTextureCoordinateAttribute = GLES20.glGetAttribLocation(getProgram(), "inputTextureCoordinate2");
        this.filterInputTextureUniform2 = GLES20.glGetUniformLocation(getProgram(), "inputImageTexture2");
        GLES20.glEnableVertexAttribArray(this.filterSecondTextureCoordinateAttribute);
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onInitialized() {
        super.onInitialized();
        if (this.bitmap == null || this.bitmap.isRecycled()) {
            return;
        }
        setBitmap(this.bitmap);
    }

    public void setBitmap(final Bitmap bitmap) {
        if (bitmap == null || !bitmap.isRecycled()) {
            this.bitmap = bitmap;
            if (this.bitmap == null) {
                return;
            }
            runOnDraw(new Runnable() { // from class: jp.co.cyberagent.android.gpuimage.filter.GPUImageTwoInputFilter.1
                @Override // java.lang.Runnable
                public void run() {
                    if (GPUImageTwoInputFilter.this.filterSourceTexture2 != -1 || bitmap == null || bitmap.isRecycled()) {
                        return;
                    }
                    GLES20.glActiveTexture(33987);
                    GPUImageTwoInputFilter.this.filterSourceTexture2 = OpenGlUtils.loadTexture(bitmap, -1, false);
                }
            });
        }
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public void recycleBitmap() {
        if (this.bitmap == null || this.bitmap.isRecycled()) {
            return;
        }
        this.bitmap.recycle();
        this.bitmap = null;
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onDestroy() {
        super.onDestroy();
        GLES20.glDeleteTextures(1, new int[]{this.filterSourceTexture2}, 0);
        this.filterSourceTexture2 = -1;
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    protected void onDrawArraysPre() {
        GLES20.glEnableVertexAttribArray(this.filterSecondTextureCoordinateAttribute);
        GLES20.glActiveTexture(33987);
        GLES20.glBindTexture(3553, this.filterSourceTexture2);
        GLES20.glUniform1i(this.filterInputTextureUniform2, 3);
        this.texture2CoordinatesBuffer.position(0);
        GLES20.glVertexAttribPointer(this.filterSecondTextureCoordinateAttribute, 2, 5126, false, 0, (Buffer) this.texture2CoordinatesBuffer);
    }

    public void setRotation(Rotation rotation, boolean z, boolean z2) {
        float[] rotation2 = TextureRotationUtil.getRotation(rotation, z, z2);
        ByteBuffer order = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder());
        FloatBuffer asFloatBuffer = order.asFloatBuffer();
        asFloatBuffer.put(rotation2);
        asFloatBuffer.flip();
        this.texture2CoordinatesBuffer = order;
    }
}
