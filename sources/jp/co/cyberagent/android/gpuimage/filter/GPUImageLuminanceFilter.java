package jp.co.cyberagent.android.gpuimage.filter;

/* loaded from: classes2.dex */
public class GPUImageLuminanceFilter extends GPUImageFilter {
    public static final String LUMINANCE_FRAGMENT_SHADER = "precision highp float;\n\nvarying vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\n\n// Values from \"Graphics Shaders: Theory and Practice\" by Bailey and Cunningham\nconst highp vec3 W = vec3(0.2125, 0.7154, 0.0721);\n\nvoid main()\n{\n    lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n    float luminance = dot(textureColor.rgb, W);\n    \n    gl_FragColor = vec4(vec3(luminance), textureColor.a);\n}";

    public GPUImageLuminanceFilter() {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, LUMINANCE_FRAGMENT_SHADER);
    }
}
