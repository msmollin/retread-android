package org.otwebrtc;

/* loaded from: classes2.dex */
public interface Predicate<T> {
    default Predicate<T> and(final Predicate<? super T> predicate) {
        return new Predicate<T>() { // from class: org.otwebrtc.Predicate.2
            @Override // org.otwebrtc.Predicate
            public boolean test(T t) {
                return Predicate.this.test(t) && predicate.test(t);
            }
        };
    }

    default Predicate<T> negate() {
        return new Predicate<T>() { // from class: org.otwebrtc.Predicate.3
            @Override // org.otwebrtc.Predicate
            public boolean test(T t) {
                return !Predicate.this.test(t);
            }
        };
    }

    default Predicate<T> or(final Predicate<? super T> predicate) {
        return new Predicate<T>() { // from class: org.otwebrtc.Predicate.1
            @Override // org.otwebrtc.Predicate
            public boolean test(T t) {
                return Predicate.this.test(t) || predicate.test(t);
            }
        };
    }

    boolean test(T t);
}
