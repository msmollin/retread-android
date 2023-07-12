package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

/* loaded from: classes2.dex */
public class GPUImageCrosshatchFilter extends GPUImageFilter {
    public static final String CROSSHATCH_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\nuniform sampler2D inputImageTexture;\nuniform highp float crossHatchSpacing;\nuniform highp float lineWidth;\nconst highp vec3 W = vec3(0.2125, 0.7154, 0.0721);\nvoid main()\n{\nhighp float luminance = dot(texture2D(inputImageTexture, textureCoordinate).rgb, W);\nlowp vec4 colorToDisplay = vec4(1.0, 1.0, 1.0, 1.0);\nif (luminance < 1.00)\n{\nif (mod(textureCoordinate.x + textureCoordinate.y, crossHatchSpacing) <= lineWidth)\n{\ncolorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);\n}\n}\nif (luminance < 0.75)\n{\nif (mod(textureCoordinate.x - textureCoordinate.y, crossHatchSpacing) <= lineWidth)\n{\ncolorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);\n}\n}\nif (luminance < 0.50)\n{\nif (mod(textureCoordinate.x + textureCoordinate.y - (crossHatchSpacing / 2.0), crossHatchSpacing) <= lineWidth)\n{\ncolorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);\n}\n}\nif (luminance < 0.3)\n{\nif (mod(textureCoordinate.x - textureCoordinate.y - (crossHatchSpacing / 2.0), crossHatchSpacing) <= lineWidth)\n{\ncolorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);\n}\n}\ngl_FragColor = colorToDisplay;\n}\n";
    private float crossHatchSpacing;
    private int crossHatchSpacingLocation;
    private float lineWidth;
    private int lineWidthLocation;

    public GPUImageCrosshatchFilter() {
        this(0.03f, 0.003f);
    }

    public GPUImageCrosshatchFilter(float f, float f2) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, CROSSHATCH_FRAGMENT_SHADER);
        this.crossHatchSpacing = f;
        this.lineWidth = f2;
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onInit() {
        super.onInit();
        this.crossHatchSpacingLocation = GLES20.glGetUniformLocation(getProgram(), "crossHatchSpacing");
        this.lineWidthLocation = GLES20.glGetUniformLocation(getProgram(), "lineWidth");
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onInitialized() {
        super.onInitialized();
        setCrossHatchSpacing(this.crossHatchSpacing);
        setLineWidth(this.lineWidth);
    }

    public void setCrossHatchSpacing(float f) {
        float outputWidth = getOutputWidth() != 0 ? 1.0f / getOutputWidth() : 4.8828125E-4f;
        if (f < outputWidth) {
            this.crossHatchSpacing = outputWidth;
        } else {
            this.crossHatchSpacing = f;
        }
        setFloat(this.crossHatchSpacingLocation, this.crossHatchSpacing);
    }

    public void setLineWidth(float f) {
        this.lineWidth = f;
        setFloat(this.lineWidthLocation, this.lineWidth);
    }
}
