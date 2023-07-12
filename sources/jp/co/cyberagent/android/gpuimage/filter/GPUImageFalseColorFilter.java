package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

/* loaded from: classes2.dex */
public class GPUImageFalseColorFilter extends GPUImageFilter {
    public static final String FALSECOLOR_FRAGMENT_SHADER = "precision lowp float;\n\nvarying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\nuniform float intensity;\nuniform vec3 firstColor;\nuniform vec3 secondColor;\n\nconst mediump vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);\n\nvoid main()\n{\nlowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\nfloat luminance = dot(textureColor.rgb, luminanceWeighting);\n\ngl_FragColor = vec4( mix(firstColor.rgb, secondColor.rgb, luminance), textureColor.a);\n}\n";
    private float[] firstColor;
    private int firstColorLocation;
    private float[] secondColor;
    private int secondColorLocation;

    public GPUImageFalseColorFilter() {
        this(0.0f, 0.0f, 0.5f, 1.0f, 0.0f, 0.0f);
    }

    public GPUImageFalseColorFilter(float f, float f2, float f3, float f4, float f5, float f6) {
        this(new float[]{f, f2, f3}, new float[]{f4, f5, f6});
    }

    public GPUImageFalseColorFilter(float[] fArr, float[] fArr2) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, FALSECOLOR_FRAGMENT_SHADER);
        this.firstColor = fArr;
        this.secondColor = fArr2;
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onInit() {
        super.onInit();
        this.firstColorLocation = GLES20.glGetUniformLocation(getProgram(), "firstColor");
        this.secondColorLocation = GLES20.glGetUniformLocation(getProgram(), "secondColor");
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onInitialized() {
        super.onInitialized();
        setFirstColor(this.firstColor);
        setSecondColor(this.secondColor);
    }

    public void setFirstColor(float[] fArr) {
        this.firstColor = fArr;
        setFloatVec3(this.firstColorLocation, fArr);
    }

    public void setSecondColor(float[] fArr) {
        this.secondColor = fArr;
        setFloatVec3(this.secondColorLocation, fArr);
    }
}
