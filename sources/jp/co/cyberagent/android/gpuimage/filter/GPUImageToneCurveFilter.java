package jp.co.cyberagent.android.gpuimage.filter;

import android.graphics.Point;
import android.graphics.PointF;
import android.opengl.GLES20;
import com.github.mikephil.charting.utils.Utils;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

/* loaded from: classes2.dex */
public class GPUImageToneCurveFilter extends GPUImageFilter {
    public static final String TONE_CURVE_FRAGMENT_SHADER = " varying highp vec2 textureCoordinate;\n uniform sampler2D inputImageTexture;\n uniform sampler2D toneCurveTexture;\n\n void main()\n {\n     lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     lowp float redCurveValue = texture2D(toneCurveTexture, vec2(textureColor.r, 0.0)).r;\n     lowp float greenCurveValue = texture2D(toneCurveTexture, vec2(textureColor.g, 0.0)).g;\n     lowp float blueCurveValue = texture2D(toneCurveTexture, vec2(textureColor.b, 0.0)).b;\n\n     gl_FragColor = vec4(redCurveValue, greenCurveValue, blueCurveValue, textureColor.a);\n }";
    private PointF[] blueControlPoints;
    private ArrayList<Float> blueCurve;
    private PointF[] greenControlPoints;
    private ArrayList<Float> greenCurve;
    private PointF[] redControlPoints;
    private ArrayList<Float> redCurve;
    private PointF[] rgbCompositeControlPoints;
    private ArrayList<Float> rgbCompositeCurve;
    private int[] toneCurveTexture;
    private int toneCurveTextureUniformLocation;

    public GPUImageToneCurveFilter() {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, TONE_CURVE_FRAGMENT_SHADER);
        this.toneCurveTexture = new int[]{-1};
        PointF[] pointFArr = {new PointF(0.0f, 0.0f), new PointF(0.5f, 0.5f), new PointF(1.0f, 1.0f)};
        this.rgbCompositeControlPoints = pointFArr;
        this.redControlPoints = pointFArr;
        this.greenControlPoints = pointFArr;
        this.blueControlPoints = pointFArr;
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onInit() {
        super.onInit();
        this.toneCurveTextureUniformLocation = GLES20.glGetUniformLocation(getProgram(), "toneCurveTexture");
        GLES20.glActiveTexture(33987);
        GLES20.glGenTextures(1, this.toneCurveTexture, 0);
        GLES20.glBindTexture(3553, this.toneCurveTexture[0]);
        GLES20.glTexParameteri(3553, 10241, 9729);
        GLES20.glTexParameteri(3553, 10240, 9729);
        GLES20.glTexParameteri(3553, 10242, 33071);
        GLES20.glTexParameteri(3553, 10243, 33071);
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    public void onInitialized() {
        super.onInitialized();
        setRgbCompositeControlPoints(this.rgbCompositeControlPoints);
        setRedControlPoints(this.redControlPoints);
        setGreenControlPoints(this.greenControlPoints);
        setBlueControlPoints(this.blueControlPoints);
    }

    @Override // jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
    protected void onDrawArraysPre() {
        if (this.toneCurveTexture[0] != -1) {
            GLES20.glActiveTexture(33987);
            GLES20.glBindTexture(3553, this.toneCurveTexture[0]);
            GLES20.glUniform1i(this.toneCurveTextureUniformLocation, 3);
        }
    }

    public void setFromCurveFileInputStream(InputStream inputStream) {
        try {
            readShort(inputStream);
            short readShort = readShort(inputStream);
            ArrayList arrayList = new ArrayList(readShort);
            for (int i = 0; i < readShort; i++) {
                int readShort2 = readShort(inputStream);
                PointF[] pointFArr = new PointF[readShort2];
                for (int i2 = 0; i2 < readShort2; i2++) {
                    pointFArr[i2] = new PointF(readShort(inputStream) * 0.003921569f, readShort(inputStream) * 0.003921569f);
                }
                arrayList.add(pointFArr);
            }
            inputStream.close();
            this.rgbCompositeControlPoints = (PointF[]) arrayList.get(0);
            this.redControlPoints = (PointF[]) arrayList.get(1);
            this.greenControlPoints = (PointF[]) arrayList.get(2);
            this.blueControlPoints = (PointF[]) arrayList.get(3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private short readShort(InputStream inputStream) throws IOException {
        return (short) ((inputStream.read() << 8) | inputStream.read());
    }

    public void setRgbCompositeControlPoints(PointF[] pointFArr) {
        this.rgbCompositeControlPoints = pointFArr;
        this.rgbCompositeCurve = createSplineCurve(this.rgbCompositeControlPoints);
        updateToneCurveTexture();
    }

    public void setRedControlPoints(PointF[] pointFArr) {
        this.redControlPoints = pointFArr;
        this.redCurve = createSplineCurve(this.redControlPoints);
        updateToneCurveTexture();
    }

    public void setGreenControlPoints(PointF[] pointFArr) {
        this.greenControlPoints = pointFArr;
        this.greenCurve = createSplineCurve(this.greenControlPoints);
        updateToneCurveTexture();
    }

    public void setBlueControlPoints(PointF[] pointFArr) {
        this.blueControlPoints = pointFArr;
        this.blueCurve = createSplineCurve(this.blueControlPoints);
        updateToneCurveTexture();
    }

    private void updateToneCurveTexture() {
        runOnDraw(new Runnable() { // from class: jp.co.cyberagent.android.gpuimage.filter.GPUImageToneCurveFilter.1
            @Override // java.lang.Runnable
            public void run() {
                GLES20.glActiveTexture(33987);
                GLES20.glBindTexture(3553, GPUImageToneCurveFilter.this.toneCurveTexture[0]);
                if (GPUImageToneCurveFilter.this.redCurve.size() < 256 || GPUImageToneCurveFilter.this.greenCurve.size() < 256 || GPUImageToneCurveFilter.this.blueCurve.size() < 256 || GPUImageToneCurveFilter.this.rgbCompositeCurve.size() < 256) {
                    return;
                }
                byte[] bArr = new byte[1024];
                for (int i = 0; i < 256; i++) {
                    int i2 = i * 4;
                    float f = i;
                    bArr[i2 + 2] = (byte) (((int) Math.min(Math.max(((Float) GPUImageToneCurveFilter.this.blueCurve.get(i)).floatValue() + f + ((Float) GPUImageToneCurveFilter.this.rgbCompositeCurve.get(i)).floatValue(), 0.0f), 255.0f)) & 255);
                    bArr[i2 + 1] = (byte) (((int) Math.min(Math.max(((Float) GPUImageToneCurveFilter.this.greenCurve.get(i)).floatValue() + f + ((Float) GPUImageToneCurveFilter.this.rgbCompositeCurve.get(i)).floatValue(), 0.0f), 255.0f)) & 255);
                    bArr[i2] = (byte) (((int) Math.min(Math.max(f + ((Float) GPUImageToneCurveFilter.this.redCurve.get(i)).floatValue() + ((Float) GPUImageToneCurveFilter.this.rgbCompositeCurve.get(i)).floatValue(), 0.0f), 255.0f)) & 255);
                    bArr[i2 + 3] = -1;
                }
                GLES20.glTexImage2D(3553, 0, 6408, 256, 1, 0, 6408, 5121, ByteBuffer.wrap(bArr));
            }
        });
    }

    private ArrayList<Float> createSplineCurve(PointF[] pointFArr) {
        if (pointFArr == null || pointFArr.length <= 0) {
            return null;
        }
        PointF[] pointFArr2 = (PointF[]) pointFArr.clone();
        Arrays.sort(pointFArr2, new Comparator<PointF>() { // from class: jp.co.cyberagent.android.gpuimage.filter.GPUImageToneCurveFilter.2
            @Override // java.util.Comparator
            public int compare(PointF pointF, PointF pointF2) {
                if (pointF.x < pointF2.x) {
                    return -1;
                }
                return pointF.x > pointF2.x ? 1 : 0;
            }
        });
        Point[] pointArr = new Point[pointFArr2.length];
        for (int i = 0; i < pointFArr.length; i++) {
            PointF pointF = pointFArr2[i];
            pointArr[i] = new Point((int) (pointF.x * 255.0f), (int) (pointF.y * 255.0f));
        }
        ArrayList<Point> createSplineCurve2 = createSplineCurve2(pointArr);
        Point point = createSplineCurve2.get(0);
        if (point.x > 0) {
            for (int i2 = point.x; i2 >= 0; i2--) {
                createSplineCurve2.add(0, new Point(i2, 0));
            }
        }
        Point point2 = createSplineCurve2.get(createSplineCurve2.size() - 1);
        if (point2.x < 255) {
            int i3 = point2.x;
            while (true) {
                i3++;
                if (i3 > 255) {
                    break;
                }
                createSplineCurve2.add(new Point(i3, 255));
            }
        }
        ArrayList<Float> arrayList = new ArrayList<>(createSplineCurve2.size());
        Iterator<Point> it = createSplineCurve2.iterator();
        while (it.hasNext()) {
            Point next = it.next();
            Point point3 = new Point(next.x, next.x);
            float sqrt = (float) Math.sqrt(Math.pow(point3.x - next.x, 2.0d) + Math.pow(point3.y - next.y, 2.0d));
            if (point3.y > next.y) {
                sqrt = -sqrt;
            }
            arrayList.add(Float.valueOf(sqrt));
        }
        return arrayList;
    }

    private ArrayList<Point> createSplineCurve2(Point[] pointArr) {
        Point[] pointArr2 = pointArr;
        ArrayList<Double> createSecondDerivative = createSecondDerivative(pointArr);
        int size = createSecondDerivative.size();
        if (size < 1) {
            return null;
        }
        double[] dArr = new double[size];
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            dArr[i2] = createSecondDerivative.get(i2).doubleValue();
        }
        ArrayList<Point> arrayList = new ArrayList<>(size + 1);
        while (i < size - 1) {
            Point point = pointArr2[i];
            int i3 = i + 1;
            Point point2 = pointArr2[i3];
            int i4 = point.x;
            while (i4 < point2.x) {
                double d = (i4 - point.x) / (point2.x - point.x);
                double d2 = 1.0d - d;
                double d3 = point2.x - point.x;
                int i5 = size;
                Point point3 = point;
                ArrayList<Point> arrayList2 = arrayList;
                double d4 = (point.y * d2) + (point2.y * d) + (((d3 * d3) / 6.0d) * (((((d2 * d2) * d2) - d2) * dArr[i]) + ((((d * d) * d) - d) * dArr[i3])));
                double d5 = 255.0d;
                if (d4 <= 255.0d) {
                    d5 = d4 < Utils.DOUBLE_EPSILON ? 0.0d : d4;
                }
                arrayList2.add(new Point(i4, (int) Math.round(d5)));
                i4++;
                arrayList = arrayList2;
                size = i5;
                point = point3;
            }
            i = i3;
            pointArr2 = pointArr;
        }
        ArrayList<Point> arrayList3 = arrayList;
        if (arrayList3.size() == 255) {
            arrayList3.add(pointArr[pointArr.length - 1]);
        }
        return arrayList3;
    }

    private ArrayList<Double> createSecondDerivative(Point[] pointArr) {
        int i;
        int length = pointArr.length;
        if (length <= 1) {
            return null;
        }
        double[][] dArr = (double[][]) Array.newInstance(double.class, length, 3);
        double[] dArr2 = new double[length];
        char c = 0;
        dArr[0][1] = 1.0d;
        double[] dArr3 = dArr[0];
        double d = Utils.DOUBLE_EPSILON;
        dArr3[0] = 0.0d;
        dArr[0][2] = 0.0d;
        int i2 = 1;
        while (true) {
            i = length - 1;
            if (i2 >= i) {
                break;
            }
            Point point = pointArr[i2 - 1];
            Point point2 = pointArr[i2];
            int i3 = i2 + 1;
            Point point3 = pointArr[i3];
            dArr[i2][c] = (point2.x - point.x) / 6.0d;
            dArr[i2][1] = (point3.x - point.x) / 3.0d;
            dArr[i2][2] = (point3.x - point2.x) / 6.0d;
            dArr2[i2] = ((point3.y - point2.y) / (point3.x - point2.x)) - ((point2.y - point.y) / (point2.x - point.x));
            i2 = i3;
            c = 0;
            d = Utils.DOUBLE_EPSILON;
        }
        double d2 = d;
        dArr2[c] = d2;
        dArr2[i] = d2;
        dArr[i][1] = 1.0d;
        dArr[i][c] = d2;
        dArr[i][2] = d2;
        int i4 = 1;
        while (i4 < length) {
            double d3 = dArr[i4][c];
            int i5 = i4 - 1;
            double d4 = d3 / dArr[i5][1];
            double[] dArr4 = dArr[i4];
            dArr4[1] = dArr4[1] - (dArr[i5][2] * d4);
            dArr[i4][0] = 0.0d;
            dArr2[i4] = dArr2[i4] - (d4 * dArr2[i5]);
            i4++;
            c = 0;
        }
        for (int i6 = length - 2; i6 >= 0; i6--) {
            int i7 = i6 + 1;
            double d5 = dArr[i6][2] / dArr[i7][1];
            double[] dArr5 = dArr[i6];
            dArr5[1] = dArr5[1] - (dArr[i7][0] * d5);
            dArr[i6][2] = 0.0d;
            dArr2[i6] = dArr2[i6] - (d5 * dArr2[i7]);
        }
        ArrayList<Double> arrayList = new ArrayList<>(length);
        for (int i8 = 0; i8 < length; i8++) {
            arrayList.add(Double.valueOf(dArr2[i8] / dArr[i8][1]));
        }
        return arrayList;
    }
}
