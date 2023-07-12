package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;
import android.opengl.Matrix;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/* loaded from: classes2.dex */
public class GPUImageTransformFilter extends GPUImageFilter {
    public static final String TRANSFORM_VERTEX_SHADER = "attribute vec4 position;\n attribute vec4 inputTextureCoordinate;\n \n uniform mat4 transformMatrix;\n uniform mat4 orthographicMatrix;\n \n varying vec2 textureCoordinate;\n \n void main()\n {\n     gl_Position = transformMatrix * vec4(position.xyz, 1.0) * orthographicMatrix;\n     textureCoordinate = inputTextureCoordinate.xy;\n }";
    private boolean anchorTopLeft;
    private boolean ignoreAspectRatio;
    private float[] orthographicMatrix;
    private int orthographicMatrixUniform;
    private float[] transform3D;
    private int transformMatrixUniform;

    public GPUImageTransformFilter() {
        super(TRANSFORM_VERTEX_SHADER, GPUImageFilter.NO_FILTER_FRAGMENT_SHADER);
        this.orthographicMatrix = new float[16];
        Matrix.orthoM(this.orthographicMatrix, 0, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f);
        this.transform3D = new float[16];
        Matrix.setIdentityM(this.transform3D, 0);
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onInit() {
        super.onInit();
        this.transformMatrixUniform = GLES20.glGetUniformLocation(getProgram(), "transformMatrix");
        this.orthographicMatrixUniform = GLES20.glGetUniformLocation(getProgram(), "orthographicMatrix");
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onInitialized() {
        super.onInitialized();
        setUniformMatrix4f(this.transformMatrixUniform, this.transform3D);
        setUniformMatrix4f(this.orthographicMatrixUniform, this.orthographicMatrix);
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onOutputSizeChanged(int i, int i2) {
        super.onOutputSizeChanged(i, i2);
        if (this.ignoreAspectRatio) {
            return;
        }
        float f = i2;
        float f2 = i;
        Matrix.orthoM(this.orthographicMatrix, 0, -1.0f, 1.0f, ((-1.0f) * f) / f2, (f * 1.0f) / f2, -1.0f, 1.0f);
        setUniformMatrix4f(this.orthographicMatrixUniform, this.orthographicMatrix);
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onDraw(int i, FloatBuffer floatBuffer, FloatBuffer floatBuffer2) {
        if (!this.ignoreAspectRatio) {
            floatBuffer.position(0);
            floatBuffer.get(r0);
            float outputHeight = getOutputHeight() / getOutputWidth();
            float[] fArr = {0.0f, fArr[1] * outputHeight, 0.0f, fArr[3] * outputHeight, 0.0f, fArr[5] * outputHeight, 0.0f, fArr[7] * outputHeight};
            floatBuffer = ByteBuffer.allocateDirect(fArr.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            floatBuffer.put(fArr).position(0);
        }
        super.onDraw(i, floatBuffer, floatBuffer2);
    }

    public void setTransform3D(float[] fArr) {
        this.transform3D = fArr;
        setUniformMatrix4f(this.transformMatrixUniform, fArr);
    }

    public float[] getTransform3D() {
        return this.transform3D;
    }

    public void setIgnoreAspectRatio(boolean z) {
        this.ignoreAspectRatio = z;
        if (z) {
            Matrix.orthoM(this.orthographicMatrix, 0, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f);
            setUniformMatrix4f(this.orthographicMatrixUniform, this.orthographicMatrix);
            return;
        }
        onOutputSizeChanged(getOutputWidth(), getOutputHeight());
    }

    public boolean ignoreAspectRatio() {
        return this.ignoreAspectRatio;
    }

    public void setAnchorTopLeft(boolean z) {
        this.anchorTopLeft = z;
        setIgnoreAspectRatio(this.ignoreAspectRatio);
    }

    public boolean anchorTopLeft() {
        return this.anchorTopLeft;
    }
}
