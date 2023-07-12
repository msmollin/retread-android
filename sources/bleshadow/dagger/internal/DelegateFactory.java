package bleshadow.dagger.internal;

import bleshadow.javax.inject.Provider;

/* loaded from: classes.dex */
public final class DelegateFactory<T> implements Factory<T> {
    private Provider<T> delegate;

    @Override // bleshadow.javax.inject.Provider
    public T get() {
        if (this.delegate == null) {
            throw new IllegalStateException();
        }
        return this.delegate.get();
    }

    public void setDelegatedProvider(Provider<T> delegate) {
        if (delegate == null) {
            throw new IllegalArgumentException();
        }
        if (this.delegate != null) {
            throw new IllegalStateException();
        }
        this.delegate = delegate;
    }
}
