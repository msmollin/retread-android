package jp.co.cyberagent.android.gpuimage.filter;

/* loaded from: classes2.dex */
public class GPUImageGaussianBlurFilter extends GPUImageTwoPassTextureSamplingFilter {
    public static final String FRAGMENT_SHADER = "uniform sampler2D inputImageTexture;\n\nconst lowp int GAUSSIAN_SAMPLES = 9;\n\nvarying highp vec2 textureCoordinate;\nvarying highp vec2 blurCoordinates[GAUSSIAN_SAMPLES];\n\nvoid main()\n{\n\tlowp vec3 sum = vec3(0.0);\n   lowp vec4 fragColor=texture2D(inputImageTexture,textureCoordinate);\n\t\n    sum += texture2D(inputImageTexture, blurCoordinates[0]).rgb * 0.05;\n    sum += texture2D(inputImageTexture, blurCoordinates[1]).rgb * 0.09;\n    sum += texture2D(inputImageTexture, blurCoordinates[2]).rgb * 0.12;\n    sum += texture2D(inputImageTexture, blurCoordinates[3]).rgb * 0.15;\n    sum += texture2D(inputImageTexture, blurCoordinates[4]).rgb * 0.18;\n    sum += texture2D(inputImageTexture, blurCoordinates[5]).rgb * 0.15;\n    sum += texture2D(inputImageTexture, blurCoordinates[6]).rgb * 0.12;\n    sum += texture2D(inputImageTexture, blurCoordinates[7]).rgb * 0.09;\n    sum += texture2D(inputImageTexture, blurCoordinates[8]).rgb * 0.05;\n\n\tgl_FragColor = vec4(sum,fragColor.a);\n}";
    public static final String VERTEX_SHADER = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n\nconst int GAUSSIAN_SAMPLES = 9;\n\nuniform float texelWidthOffset;\nuniform float texelHeightOffset;\n\nvarying vec2 textureCoordinate;\nvarying vec2 blurCoordinates[GAUSSIAN_SAMPLES];\n\nvoid main()\n{\n\tgl_Position = position;\n\ttextureCoordinate = inputTextureCoordinate.xy;\n\t\n\t// Calculate the positions for the blur\n\tint multiplier = 0;\n\tvec2 blurStep;\n   vec2 singleStepOffset = vec2(texelHeightOffset, texelWidthOffset);\n    \n\tfor (int i = 0; i < GAUSSIAN_SAMPLES; i++)\n   {\n\t\tmultiplier = (i - ((GAUSSIAN_SAMPLES - 1) / 2));\n       // Blur in x (horizontal)\n       blurStep = float(multiplier) * singleStepOffset;\n\t\tblurCoordinates[i] = inputTextureCoordinate.xy + blurStep;\n\t}\n}\n";
    protected float blurSize;

    public GPUImageGaussianBlurFilter() {
        this(1.0f);
    }

    public GPUImageGaussianBlurFilter(float f) {
        super(VERTEX_SHADER, FRAGMENT_SHADER, VERTEX_SHADER, FRAGMENT_SHADER);
        this.blurSize = f;
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onInitialized() {
        super.onInitialized();
        setBlurSize(this.blurSize);
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageTwoPassTextureSamplingFilter
    public float getVerticalTexelOffsetRatio() {
        return this.blurSize;
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageTwoPassTextureSamplingFilter
    public float getHorizontalTexelOffsetRatio() {
        return this.blurSize;
    }

    public void setBlurSize(float f) {
        this.blurSize = f;
        runOnDraw(new Runnable() { // from class: jp.co.cyberagent.android.gpuimage.filter.GPUImageGaussianBlurFilter.1
            @Override // java.lang.Runnable
            public void run() {
                GPUImageGaussianBlurFilter.this.initTexelOffsets();
            }
        });
    }
}
