package com.daasuu.gpuv.egl.filter;

import android.opengl.GLES20;
import android.util.Pair;
import com.daasuu.gpuv.egl.GlFramebufferObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/* loaded from: classes.dex */
public class GlFilterGroup extends GlFilter {
    private final Collection<GlFilter> filters;
    private final ArrayList<Pair<GlFilter, GlFramebufferObject>> list;
    private int prevTexName;

    public GlFilterGroup(GlFilter... glFilterArr) {
        this(Arrays.asList(glFilterArr));
    }

    public GlFilterGroup(Collection<GlFilter> collection) {
        this.list = new ArrayList<>();
        this.filters = collection;
    }

    @Override // com.daasuu.gpuv.egl.filter.GlFilter
    public void setup() {
        super.setup();
        if (this.filters != null) {
            int size = this.filters.size();
            int i = 0;
            for (GlFilter glFilter : this.filters) {
                glFilter.setup();
                i++;
                this.list.add(Pair.create(glFilter, i < size ? new GlFramebufferObject() : null));
            }
        }
    }

    @Override // com.daasuu.gpuv.egl.filter.GlFilter
    public void release() {
        Iterator<Pair<GlFilter, GlFramebufferObject>> it = this.list.iterator();
        while (it.hasNext()) {
            Pair<GlFilter, GlFramebufferObject> next = it.next();
            if (next.first != null) {
                ((GlFilter) next.first).release();
            }
            if (next.second != null) {
                ((GlFramebufferObject) next.second).release();
            }
        }
        this.list.clear();
        super.release();
    }

    @Override // com.daasuu.gpuv.egl.filter.GlFilter
    public void setFrameSize(int i, int i2) {
        super.setFrameSize(i, i2);
        Iterator<Pair<GlFilter, GlFramebufferObject>> it = this.list.iterator();
        while (it.hasNext()) {
            Pair<GlFilter, GlFramebufferObject> next = it.next();
            if (next.first != null) {
                ((GlFilter) next.first).setFrameSize(i, i2);
            }
            if (next.second != null) {
                ((GlFramebufferObject) next.second).setup(i, i2);
            }
        }
    }

    @Override // com.daasuu.gpuv.egl.filter.GlFilter
    public void draw(int i, GlFramebufferObject glFramebufferObject) {
        this.prevTexName = i;
        Iterator<Pair<GlFilter, GlFramebufferObject>> it = this.list.iterator();
        while (it.hasNext()) {
            Pair<GlFilter, GlFramebufferObject> next = it.next();
            if (next.second != null) {
                if (next.first != null) {
                    ((GlFramebufferObject) next.second).enable();
                    GLES20.glClear(16384);
                    ((GlFilter) next.first).draw(this.prevTexName, (GlFramebufferObject) next.second);
                }
                this.prevTexName = ((GlFramebufferObject) next.second).getTexName();
            } else {
                if (glFramebufferObject != null) {
                    glFramebufferObject.enable();
                } else {
                    GLES20.glBindFramebuffer(36160, 0);
                }
                if (next.first != null) {
                    ((GlFilter) next.first).draw(this.prevTexName, glFramebufferObject);
                }
            }
        }
    }
}
