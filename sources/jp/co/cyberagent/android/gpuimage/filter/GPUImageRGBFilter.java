package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

/* loaded from: classes2.dex */
public class GPUImageRGBFilter extends GPUImageFilter {
    public static final String RGB_FRAGMENT_SHADER = "  varying highp vec2 textureCoordinate;\n  \n  uniform sampler2D inputImageTexture;\n  uniform highp float red;\n  uniform highp float green;\n  uniform highp float blue;\n  \n  void main()\n  {\n      highp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n      \n      gl_FragColor = vec4(textureColor.r * red, textureColor.g * green, textureColor.b * blue, 1.0);\n  }\n";
    private float blue;
    private int blueLocation;
    private float green;
    private int greenLocation;
    private float red;
    private int redLocation;

    public GPUImageRGBFilter() {
        this(1.0f, 1.0f, 1.0f);
    }

    public GPUImageRGBFilter(float f, float f2, float f3) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, RGB_FRAGMENT_SHADER);
        this.red = f;
        this.green = f2;
        this.blue = f3;
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onInit() {
        super.onInit();
        this.redLocation = GLES20.glGetUniformLocation(getProgram(), "red");
        this.greenLocation = GLES20.glGetUniformLocation(getProgram(), "green");
        this.blueLocation = GLES20.glGetUniformLocation(getProgram(), "blue");
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onInitialized() {
        super.onInitialized();
        setRed(this.red);
        setGreen(this.green);
        setBlue(this.blue);
    }

    public void setRed(float f) {
        this.red = f;
        setFloat(this.redLocation, this.red);
    }

    public void setGreen(float f) {
        this.green = f;
        setFloat(this.greenLocation, this.green);
    }

    public void setBlue(float f) {
        this.blue = f;
        setFloat(this.blueLocation, this.blue);
    }
}
