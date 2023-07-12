package jp.co.cyberagent.android.gpuimage.filter;

import android.content.Context;
import android.graphics.PointF;
import android.opengl.GLES20;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.Scanner;
import jp.co.cyberagent.android.gpuimage.util.OpenGlUtils;

/* loaded from: classes2.dex */
public class GPUImageFilter {
    public static final String NO_FILTER_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n \nuniform sampler2D inputImageTexture;\n \nvoid main()\n{\n     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n}";
    public static final String NO_FILTER_VERTEX_SHADER = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}";
    private final String fragmentShader;
    private int glAttribPosition;
    private int glAttribTextureCoordinate;
    private int glProgId;
    private int glUniformTexture;
    private boolean isInitialized;
    private int outputHeight;
    private int outputWidth;
    private final LinkedList<Runnable> runOnDraw;
    private final String vertexShader;

    public void onDestroy() {
    }

    protected void onDrawArraysPre() {
    }

    public void onInitialized() {
    }

    public GPUImageFilter() {
        this(NO_FILTER_VERTEX_SHADER, NO_FILTER_FRAGMENT_SHADER);
    }

    public GPUImageFilter(String str, String str2) {
        this.runOnDraw = new LinkedList<>();
        this.vertexShader = str;
        this.fragmentShader = str2;
    }

    private final void init() {
        onInit();
        onInitialized();
    }

    public void onInit() {
        this.glProgId = OpenGlUtils.loadProgram(this.vertexShader, this.fragmentShader);
        this.glAttribPosition = GLES20.glGetAttribLocation(this.glProgId, "position");
        this.glUniformTexture = GLES20.glGetUniformLocation(this.glProgId, "inputImageTexture");
        this.glAttribTextureCoordinate = GLES20.glGetAttribLocation(this.glProgId, "inputTextureCoordinate");
        this.isInitialized = true;
    }

    public void ifNeedInit() {
        if (this.isInitialized) {
            return;
        }
        init();
    }

    public final void destroy() {
        this.isInitialized = false;
        GLES20.glDeleteProgram(this.glProgId);
        onDestroy();
    }

    public void onOutputSizeChanged(int i, int i2) {
        this.outputWidth = i;
        this.outputHeight = i2;
    }

    public void onDraw(int i, FloatBuffer floatBuffer, FloatBuffer floatBuffer2) {
        GLES20.glUseProgram(this.glProgId);
        runPendingOnDrawTasks();
        if (this.isInitialized) {
            floatBuffer.position(0);
            GLES20.glVertexAttribPointer(this.glAttribPosition, 2, 5126, false, 0, (Buffer) floatBuffer);
            GLES20.glEnableVertexAttribArray(this.glAttribPosition);
            floatBuffer2.position(0);
            GLES20.glVertexAttribPointer(this.glAttribTextureCoordinate, 2, 5126, false, 0, (Buffer) floatBuffer2);
            GLES20.glEnableVertexAttribArray(this.glAttribTextureCoordinate);
            if (i != -1) {
                GLES20.glActiveTexture(33984);
                GLES20.glBindTexture(3553, i);
                GLES20.glUniform1i(this.glUniformTexture, 0);
            }
            onDrawArraysPre();
            GLES20.glDrawArrays(5, 0, 4);
            GLES20.glDisableVertexAttribArray(this.glAttribPosition);
            GLES20.glDisableVertexAttribArray(this.glAttribTextureCoordinate);
            GLES20.glBindTexture(3553, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void runPendingOnDrawTasks() {
        synchronized (this.runOnDraw) {
            while (!this.runOnDraw.isEmpty()) {
                this.runOnDraw.removeFirst().run();
            }
        }
    }

    public boolean isInitialized() {
        return this.isInitialized;
    }

    public int getOutputWidth() {
        return this.outputWidth;
    }

    public int getOutputHeight() {
        return this.outputHeight;
    }

    public int getProgram() {
        return this.glProgId;
    }

    public int getAttribPosition() {
        return this.glAttribPosition;
    }

    public int getAttribTextureCoordinate() {
        return this.glAttribTextureCoordinate;
    }

    public int getUniformTexture() {
        return this.glUniformTexture;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setInteger(final int i, final int i2) {
        runOnDraw(new Runnable() { // from class: jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter.1
            @Override // java.lang.Runnable
            public void run() {
                GPUImageFilter.this.ifNeedInit();
                GLES20.glUniform1i(i, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setFloat(final int i, final float f) {
        runOnDraw(new Runnable() { // from class: jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter.2
            @Override // java.lang.Runnable
            public void run() {
                GPUImageFilter.this.ifNeedInit();
                GLES20.glUniform1f(i, f);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setFloatVec2(final int i, final float[] fArr) {
        runOnDraw(new Runnable() { // from class: jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter.3
            @Override // java.lang.Runnable
            public void run() {
                GPUImageFilter.this.ifNeedInit();
                GLES20.glUniform2fv(i, 1, FloatBuffer.wrap(fArr));
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setFloatVec3(final int i, final float[] fArr) {
        runOnDraw(new Runnable() { // from class: jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter.4
            @Override // java.lang.Runnable
            public void run() {
                GPUImageFilter.this.ifNeedInit();
                GLES20.glUniform3fv(i, 1, FloatBuffer.wrap(fArr));
            }
        });
    }

    protected void setFloatVec4(final int i, final float[] fArr) {
        runOnDraw(new Runnable() { // from class: jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter.5
            @Override // java.lang.Runnable
            public void run() {
                GPUImageFilter.this.ifNeedInit();
                GLES20.glUniform4fv(i, 1, FloatBuffer.wrap(fArr));
            }
        });
    }

    protected void setFloatArray(final int i, final float[] fArr) {
        runOnDraw(new Runnable() { // from class: jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter.6
            @Override // java.lang.Runnable
            public void run() {
                GPUImageFilter.this.ifNeedInit();
                GLES20.glUniform1fv(i, fArr.length, FloatBuffer.wrap(fArr));
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setPoint(final int i, final PointF pointF) {
        runOnDraw(new Runnable() { // from class: jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter.7
            @Override // java.lang.Runnable
            public void run() {
                GPUImageFilter.this.ifNeedInit();
                GLES20.glUniform2fv(i, 1, new float[]{pointF.x, pointF.y}, 0);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setUniformMatrix3f(final int i, final float[] fArr) {
        runOnDraw(new Runnable() { // from class: jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter.8
            @Override // java.lang.Runnable
            public void run() {
                GPUImageFilter.this.ifNeedInit();
                GLES20.glUniformMatrix3fv(i, 1, false, fArr, 0);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setUniformMatrix4f(final int i, final float[] fArr) {
        runOnDraw(new Runnable() { // from class: jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter.9
            @Override // java.lang.Runnable
            public void run() {
                GPUImageFilter.this.ifNeedInit();
                GLES20.glUniformMatrix4fv(i, 1, false, fArr, 0);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void runOnDraw(Runnable runnable) {
        synchronized (this.runOnDraw) {
            this.runOnDraw.addLast(runnable);
        }
    }

    public static String loadShader(String str, Context context) {
        try {
            InputStream open = context.getAssets().open(str);
            String convertStreamToString = convertStreamToString(open);
            open.close();
            return convertStreamToString;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String convertStreamToString(InputStream inputStream) {
        Scanner useDelimiter = new Scanner(inputStream).useDelimiter("\\A");
        return useDelimiter.hasNext() ? useDelimiter.next() : "";
    }
}
