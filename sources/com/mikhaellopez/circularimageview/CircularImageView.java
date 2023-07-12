package com.mikhaellopez.circularimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.ViewCompat;

/* loaded from: classes.dex */
public class CircularImageView extends AppCompatImageView {
    private static final float DEFAULT_BORDER_WIDTH = 4.0f;
    private static final float DEFAULT_SHADOW_RADIUS = 8.0f;
    private float borderWidth;
    private int canvasSize;
    private ColorFilter colorFilter;
    private Drawable drawable;
    private Bitmap image;
    private Paint paint;
    private Paint paintBackground;
    private Paint paintBorder;
    private int shadowColor;
    private ShadowGravity shadowGravity;
    private float shadowRadius;

    public CircularImageView(Context context) {
        this(context, null);
    }

    public CircularImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CircularImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.shadowColor = ViewCompat.MEASURED_STATE_MASK;
        this.shadowGravity = ShadowGravity.BOTTOM;
        init(context, attributeSet, i);
    }

    private void init(Context context, AttributeSet attributeSet, int i) {
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paintBorder = new Paint();
        this.paintBorder.setAntiAlias(true);
        this.paintBackground = new Paint();
        this.paintBackground.setAntiAlias(true);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.CircularImageView, i, 0);
        if (obtainStyledAttributes.getBoolean(R.styleable.CircularImageView_civ_border, true)) {
            setBorderWidth(obtainStyledAttributes.getDimension(R.styleable.CircularImageView_civ_border_width, getContext().getResources().getDisplayMetrics().density * DEFAULT_BORDER_WIDTH));
            setBorderColor(obtainStyledAttributes.getColor(R.styleable.CircularImageView_civ_border_color, -1));
        }
        setBackgroundColor(obtainStyledAttributes.getColor(R.styleable.CircularImageView_civ_background_color, -1));
        if (obtainStyledAttributes.getBoolean(R.styleable.CircularImageView_civ_shadow, false)) {
            this.shadowRadius = DEFAULT_SHADOW_RADIUS;
            drawShadow(obtainStyledAttributes.getFloat(R.styleable.CircularImageView_civ_shadow_radius, this.shadowRadius), obtainStyledAttributes.getColor(R.styleable.CircularImageView_civ_shadow_color, this.shadowColor));
            this.shadowGravity = ShadowGravity.fromValue(obtainStyledAttributes.getInteger(R.styleable.CircularImageView_civ_shadow_gravity, ShadowGravity.BOTTOM.getValue()));
        }
        obtainStyledAttributes.recycle();
    }

    public void setBorderWidth(float f) {
        this.borderWidth = f;
        requestLayout();
        invalidate();
    }

    public void setBorderColor(int i) {
        if (this.paintBorder != null) {
            this.paintBorder.setColor(i);
        }
        invalidate();
    }

    @Override // android.view.View
    public void setBackgroundColor(int i) {
        if (this.paintBackground != null) {
            this.paintBackground.setColor(i);
        }
        invalidate();
    }

    public void addShadow() {
        if (this.shadowRadius == 0.0f) {
            this.shadowRadius = DEFAULT_SHADOW_RADIUS;
        }
        drawShadow(this.shadowRadius, this.shadowColor);
        invalidate();
    }

    public void setShadowRadius(float f) {
        drawShadow(f, this.shadowColor);
        invalidate();
    }

    public void setShadowColor(int i) {
        drawShadow(this.shadowRadius, i);
        invalidate();
    }

    public void setShadowGravity(ShadowGravity shadowGravity) {
        this.shadowGravity = shadowGravity;
        invalidate();
    }

    @Override // android.widget.ImageView
    public void setColorFilter(ColorFilter colorFilter) {
        if (this.colorFilter == colorFilter) {
            return;
        }
        this.colorFilter = colorFilter;
        this.drawable = null;
        invalidate();
    }

    @Override // android.widget.ImageView
    public ImageView.ScaleType getScaleType() {
        ImageView.ScaleType scaleType = super.getScaleType();
        return (scaleType == null || scaleType != ImageView.ScaleType.CENTER_INSIDE) ? ImageView.ScaleType.CENTER_CROP : scaleType;
    }

    @Override // android.widget.ImageView
    public void setScaleType(ImageView.ScaleType scaleType) {
        if (scaleType != ImageView.ScaleType.CENTER_CROP && scaleType != ImageView.ScaleType.CENTER_INSIDE) {
            throw new IllegalArgumentException(String.format("ScaleType %s not supported. Just ScaleType.CENTER_CROP & ScaleType.CENTER_INSIDE are available for this library.", scaleType));
        }
        super.setScaleType(scaleType);
    }

    @Override // android.widget.ImageView, android.view.View
    public void onDraw(Canvas canvas) {
        loadBitmap();
        if (this.image == null) {
            return;
        }
        if (!isInEditMode()) {
            this.canvasSize = Math.min(canvas.getWidth(), canvas.getHeight());
        }
        float f = this.shadowRadius * 2.0f;
        float f2 = ((int) (this.canvasSize - (this.borderWidth * 2.0f))) / 2;
        canvas.drawCircle(this.borderWidth + f2, this.borderWidth + f2, (this.borderWidth + f2) - f, this.paintBorder);
        float f3 = f2 - f;
        canvas.drawCircle(this.borderWidth + f2, this.borderWidth + f2, f3, this.paintBackground);
        canvas.drawCircle(this.borderWidth + f2, f2 + this.borderWidth, f3, this.paint);
    }

    private void loadBitmap() {
        if (this.drawable == getDrawable()) {
            return;
        }
        this.drawable = getDrawable();
        this.image = drawableToBitmap(this.drawable);
        updateShader();
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.canvasSize = Math.min(i, i2);
        if (this.image != null) {
            updateShader();
        }
    }

    private void drawShadow(float f, int i) {
        float f2;
        float f3;
        this.shadowRadius = f;
        this.shadowColor = i;
        setLayerType(1, this.paintBorder);
        float f4 = 0.0f;
        switch (this.shadowGravity) {
            case CENTER:
            default:
                f2 = 0.0f;
                break;
            case TOP:
                f2 = (-f) / 2.0f;
                break;
            case BOTTOM:
                f2 = f / 2.0f;
                break;
            case START:
                f3 = (-f) / 2.0f;
                f4 = f3;
                f2 = 0.0f;
                break;
            case END:
                f3 = f / 2.0f;
                f4 = f3;
                f2 = 0.0f;
                break;
        }
        this.paintBorder.setShadowLayer(f, f4, f2, i);
    }

    private void updateShader() {
        float width;
        float height;
        float width2;
        if (this.image == null) {
            return;
        }
        BitmapShader bitmapShader = new BitmapShader(this.image, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float f = 0.0f;
        switch (AnonymousClass1.$SwitchMap$android$widget$ImageView$ScaleType[getScaleType().ordinal()]) {
            case 1:
                if (this.image.getWidth() * getHeight() > getWidth() * this.image.getHeight()) {
                    width = getHeight() / this.image.getHeight();
                    width2 = 0.5f * (getWidth() - (this.image.getWidth() * width));
                    float f2 = width2;
                    height = 0.0f;
                    f = f2;
                    break;
                } else {
                    width = getWidth() / this.image.getWidth();
                    height = 0.5f * (getHeight() - (this.image.getHeight() * width));
                    break;
                }
            case 2:
                if (this.image.getWidth() * getHeight() < getWidth() * this.image.getHeight()) {
                    width = getHeight() / this.image.getHeight();
                    width2 = 0.5f * (getWidth() - (this.image.getWidth() * width));
                    float f22 = width2;
                    height = 0.0f;
                    f = f22;
                    break;
                } else {
                    width = getWidth() / this.image.getWidth();
                    height = 0.5f * (getHeight() - (this.image.getHeight() * width));
                    break;
                }
            default:
                width = 0.0f;
                height = 0.0f;
                break;
        }
        Matrix matrix = new Matrix();
        matrix.setScale(width, width);
        matrix.postTranslate(f, height);
        bitmapShader.setLocalMatrix(matrix);
        this.paint.setShader(bitmapShader);
        this.paint.setColorFilter(this.colorFilter);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.mikhaellopez.circularimageview.CircularImageView$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$widget$ImageView$ScaleType = new int[ImageView.ScaleType.values().length];

        static {
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.CENTER_CROP.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.CENTER_INSIDE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            $SwitchMap$com$mikhaellopez$circularimageview$CircularImageView$ShadowGravity = new int[ShadowGravity.values().length];
            try {
                $SwitchMap$com$mikhaellopez$circularimageview$CircularImageView$ShadowGravity[ShadowGravity.CENTER.ordinal()] = 1;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$mikhaellopez$circularimageview$CircularImageView$ShadowGravity[ShadowGravity.TOP.ordinal()] = 2;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$mikhaellopez$circularimageview$CircularImageView$ShadowGravity[ShadowGravity.BOTTOM.ordinal()] = 3;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$mikhaellopez$circularimageview$CircularImageView$ShadowGravity[ShadowGravity.START.ordinal()] = 4;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$mikhaellopez$circularimageview$CircularImageView$ShadowGravity[ShadowGravity.END.ordinal()] = 5;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        try {
            Bitmap createBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return createBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onMeasure(int i, int i2) {
        setMeasuredDimension(measureWidth(i), measureHeight(i2));
    }

    private int measureWidth(int i) {
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        return (mode == 1073741824 || mode == Integer.MIN_VALUE) ? size : this.canvasSize;
    }

    private int measureHeight(int i) {
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        if (mode != 1073741824 && mode != Integer.MIN_VALUE) {
            size = this.canvasSize;
        }
        return size + 2;
    }

    /* loaded from: classes.dex */
    public enum ShadowGravity {
        CENTER,
        TOP,
        BOTTOM,
        START,
        END;

        public int getValue() {
            switch (this) {
                case CENTER:
                    return 1;
                case TOP:
                    return 2;
                case BOTTOM:
                    return 3;
                case START:
                    return 4;
                case END:
                    return 5;
                default:
                    throw new IllegalArgumentException("Not value available for this ShadowGravity: " + this);
            }
        }

        public static ShadowGravity fromValue(int i) {
            switch (i) {
                case 1:
                    return CENTER;
                case 2:
                    return TOP;
                case 3:
                    return BOTTOM;
                case 4:
                    return START;
                case 5:
                    return END;
                default:
                    throw new IllegalArgumentException("This value is not supported for ShadowGravity: " + i);
            }
        }
    }
}
