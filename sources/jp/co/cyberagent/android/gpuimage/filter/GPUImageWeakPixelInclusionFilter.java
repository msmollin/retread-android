package jp.co.cyberagent.android.gpuimage.filter;

/* loaded from: classes2.dex */
public class GPUImageWeakPixelInclusionFilter extends GPUImage3x3TextureSamplingFilter {
    public static final String WEAKPIXEL_FRAGMENT_SHADER = "precision lowp float;\n\nvarying vec2 textureCoordinate;\nvarying vec2 leftTextureCoordinate;\nvarying vec2 rightTextureCoordinate;\n\nvarying vec2 topTextureCoordinate;\nvarying vec2 topLeftTextureCoordinate;\nvarying vec2 topRightTextureCoordinate;\n\nvarying vec2 bottomTextureCoordinate;\nvarying vec2 bottomLeftTextureCoordinate;\nvarying vec2 bottomRightTextureCoordinate;\n\nuniform sampler2D inputImageTexture;\n\nvoid main()\n{\nfloat bottomLeftIntensity = texture2D(inputImageTexture, bottomLeftTextureCoordinate).r;\nfloat topRightIntensity = texture2D(inputImageTexture, topRightTextureCoordinate).r;\nfloat topLeftIntensity = texture2D(inputImageTexture, topLeftTextureCoordinate).r;\nfloat bottomRightIntensity = texture2D(inputImageTexture, bottomRightTextureCoordinate).r;\nfloat leftIntensity = texture2D(inputImageTexture, leftTextureCoordinate).r;\nfloat rightIntensity = texture2D(inputImageTexture, rightTextureCoordinate).r;\nfloat bottomIntensity = texture2D(inputImageTexture, bottomTextureCoordinate).r;\nfloat topIntensity = texture2D(inputImageTexture, topTextureCoordinate).r;\nfloat centerIntensity = texture2D(inputImageTexture, textureCoordinate).r;\n\nfloat pixelIntensitySum = bottomLeftIntensity + topRightIntensity + topLeftIntensity + bottomRightIntensity + leftIntensity + rightIntensity + bottomIntensity + topIntensity + centerIntensity;\nfloat sumTest = step(1.5, pixelIntensitySum);\nfloat pixelTest = step(0.01, centerIntensity);\n\ngl_FragColor = vec4(vec3(sumTest * pixelTest), 1.0);\n}\n";

    public GPUImageWeakPixelInclusionFilter() {
        super(WEAKPIXEL_FRAGMENT_SHADER);
    }
}
