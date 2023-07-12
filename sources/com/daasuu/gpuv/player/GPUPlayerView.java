package com.daasuu.gpuv.player;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import com.daasuu.gpuv.egl.GlConfigChooser;
import com.daasuu.gpuv.egl.GlContextFactory;
import com.daasuu.gpuv.egl.filter.GlFilter;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.video.VideoListener;

/* loaded from: classes.dex */
public class GPUPlayerView extends GLSurfaceView implements VideoListener {
    private static final String TAG = "GPUPlayerView";
    private SimpleExoPlayer player;
    private PlayerScaleType playerScaleType;
    private final GPUPlayerRenderer renderer;
    private float videoAspect;

    public void onRenderedFirstFrame() {
    }

    public GPUPlayerView(Context context) {
        this(context, null);
    }

    public GPUPlayerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.videoAspect = 1.0f;
        this.playerScaleType = PlayerScaleType.RESIZE_FIT_WIDTH;
        setEGLContextFactory(new GlContextFactory());
        setEGLConfigChooser(new GlConfigChooser(false));
        this.renderer = new GPUPlayerRenderer(this);
        setRenderer(this.renderer);
    }

    public GPUPlayerView setSimpleExoPlayer(SimpleExoPlayer simpleExoPlayer) {
        if (this.player != null) {
            this.player.release();
            this.player = null;
        }
        this.player = simpleExoPlayer;
        this.player.addVideoListener(this);
        this.renderer.setSimpleExoPlayer(simpleExoPlayer);
        return this;
    }

    public void setGlFilter(GlFilter glFilter) {
        this.renderer.setGlFilter(glFilter);
    }

    public void setPlayerScaleType(PlayerScaleType playerScaleType) {
        this.playerScaleType = playerScaleType;
        requestLayout();
    }

    @Override // android.view.SurfaceView, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        switch (this.playerScaleType) {
            case RESIZE_FIT_WIDTH:
                measuredHeight = (int) (measuredWidth / this.videoAspect);
                break;
            case RESIZE_FIT_HEIGHT:
                measuredWidth = (int) (measuredHeight * this.videoAspect);
                break;
        }
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override // android.opengl.GLSurfaceView
    public void onPause() {
        super.onPause();
        this.renderer.release();
    }

    public void onVideoSizeChanged(int i, int i2, int i3, float f) {
        this.videoAspect = (i / i2) * f;
        requestLayout();
    }
}
