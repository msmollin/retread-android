package bleshadow.dagger.internal;

import bleshadow.dagger.releasablereferences.ReleasableReferenceManager;
import bleshadow.dagger.releasablereferences.TypedReleasableReferenceManager;
import java.lang.annotation.Annotation;

@GwtIncompatible
/* loaded from: classes.dex */
public final class TypedReleasableReferenceManagerDecorator<M extends Annotation> implements TypedReleasableReferenceManager<M> {
    private final ReleasableReferenceManager delegate;
    private final M metadata;

    public TypedReleasableReferenceManagerDecorator(ReleasableReferenceManager delegate, M metadata) {
        this.delegate = (ReleasableReferenceManager) Preconditions.checkNotNull(delegate);
        this.metadata = (M) Preconditions.checkNotNull(metadata);
    }

    @Override // bleshadow.dagger.releasablereferences.ReleasableReferenceManager
    public Class<? extends Annotation> scope() {
        return this.delegate.scope();
    }

    @Override // bleshadow.dagger.releasablereferences.TypedReleasableReferenceManager
    public M metadata() {
        return this.metadata;
    }

    @Override // bleshadow.dagger.releasablereferences.ReleasableReferenceManager
    public void releaseStrongReferences() {
        this.delegate.releaseStrongReferences();
    }

    @Override // bleshadow.dagger.releasablereferences.ReleasableReferenceManager
    public void restoreStrongReferences() {
        this.delegate.restoreStrongReferences();
    }
}
