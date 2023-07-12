package bleshadow.dagger.internal;

import bleshadow.dagger.MembersInjector;

/* loaded from: classes.dex */
public final class MembersInjectors {
    public static <T> MembersInjector<T> noOp() {
        return NoOpMembersInjector.INSTANCE;
    }

    /* loaded from: classes.dex */
    private enum NoOpMembersInjector implements MembersInjector<Object> {
        INSTANCE;

        @Override // bleshadow.dagger.MembersInjector
        public void injectMembers(Object instance) {
            Preconditions.checkNotNull(instance, "Cannot inject members into a null reference");
        }
    }

    private MembersInjectors() {
    }
}
