package jp.co.cyberagent.android.gpuimage.filter;

/* loaded from: classes2.dex */
public class GPUImageCGAColorspaceFilter extends GPUImageFilter {
    public static final String CGACOLORSPACE_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\n\nvoid main()\n{\nhighp vec2 sampleDivisor = vec2(1.0 / 200.0, 1.0 / 320.0);\n//highp vec4 colorDivisor = vec4(colorDepth);\n\nhighp vec2 samplePos = textureCoordinate - mod(textureCoordinate, sampleDivisor);\nhighp vec4 color = texture2D(inputImageTexture, samplePos );\n\n//gl_FragColor = texture2D(inputImageTexture, samplePos );\nmediump vec4 colorCyan = vec4(85.0 / 255.0, 1.0, 1.0, 1.0);\nmediump vec4 colorMagenta = vec4(1.0, 85.0 / 255.0, 1.0, 1.0);\nmediump vec4 colorWhite = vec4(1.0, 1.0, 1.0, 1.0);\nmediump vec4 colorBlack = vec4(0.0, 0.0, 0.0, 1.0);\n\nmediump vec4 endColor;\nhighp float blackDistance = distance(color, colorBlack);\nhighp float whiteDistance = distance(color, colorWhite);\nhighp float magentaDistance = distance(color, colorMagenta);\nhighp float cyanDistance = distance(color, colorCyan);\n\nmediump vec4 finalColor;\n\nhighp float colorDistance = min(magentaDistance, cyanDistance);\ncolorDistance = min(colorDistance, whiteDistance);\ncolorDistance = min(colorDistance, blackDistance); \n\nif (colorDistance == blackDistance) {\nfinalColor = colorBlack;\n} else if (colorDistance == whiteDistance) {\nfinalColor = colorWhite;\n} else if (colorDistance == cyanDistance) {\nfinalColor = colorCyan;\n} else {\nfinalColor = colorMagenta;\n}\n\ngl_FragColor = finalColor;\n}\n";

    public GPUImageCGAColorspaceFilter() {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, CGACOLORSPACE_FRAGMENT_SHADER);
    }
}
