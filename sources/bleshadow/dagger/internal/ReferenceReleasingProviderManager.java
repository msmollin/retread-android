package bleshadow.dagger.internal;

import bleshadow.dagger.releasablereferences.ReleasableReferenceManager;
import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@GwtIncompatible
/* loaded from: classes.dex */
public final class ReferenceReleasingProviderManager implements ReleasableReferenceManager {
    private final Queue<WeakReference<ReferenceReleasingProvider<?>>> providers = new ConcurrentLinkedQueue();
    private final Class<? extends Annotation> scope;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public enum Operation {
        RELEASE { // from class: bleshadow.dagger.internal.ReferenceReleasingProviderManager.Operation.1
            @Override // bleshadow.dagger.internal.ReferenceReleasingProviderManager.Operation
            void execute(ReferenceReleasingProvider<?> provider) {
                provider.releaseStrongReference();
            }
        },
        RESTORE { // from class: bleshadow.dagger.internal.ReferenceReleasingProviderManager.Operation.2
            @Override // bleshadow.dagger.internal.ReferenceReleasingProviderManager.Operation
            void execute(ReferenceReleasingProvider<?> provider) {
                provider.restoreStrongReference();
            }
        };

        abstract void execute(ReferenceReleasingProvider<?> provider);
    }

    public ReferenceReleasingProviderManager(Class<? extends Annotation> scope) {
        this.scope = (Class) Preconditions.checkNotNull(scope);
    }

    public void addProvider(ReferenceReleasingProvider<?> provider) {
        this.providers.add(new WeakReference<>(provider));
    }

    @Override // bleshadow.dagger.releasablereferences.ReleasableReferenceManager
    public Class<? extends Annotation> scope() {
        return this.scope;
    }

    @Override // bleshadow.dagger.releasablereferences.ReleasableReferenceManager
    public void releaseStrongReferences() {
        execute(Operation.RELEASE);
    }

    @Override // bleshadow.dagger.releasablereferences.ReleasableReferenceManager
    public void restoreStrongReferences() {
        execute(Operation.RESTORE);
    }

    private void execute(Operation operation) {
        Iterator<WeakReference<ReferenceReleasingProvider<?>>> it = this.providers.iterator();
        while (it.hasNext()) {
            ReferenceReleasingProvider<?> referenceReleasingProvider = it.next().get();
            if (referenceReleasingProvider == null) {
                it.remove();
            } else {
                operation.execute(referenceReleasingProvider);
            }
        }
    }
}
