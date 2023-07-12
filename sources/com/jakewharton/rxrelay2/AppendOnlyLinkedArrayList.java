package com.jakewharton.rxrelay2;

import io.reactivex.functions.Predicate;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class AppendOnlyLinkedArrayList<T> {
    private final int capacity;
    private final Object[] head;
    private int offset;
    private Object[] tail;

    /* loaded from: classes.dex */
    public interface NonThrowingPredicate<T> extends Predicate<T> {
        @Override // io.reactivex.functions.Predicate
        boolean test(T t);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppendOnlyLinkedArrayList(int i) {
        this.capacity = i;
        this.head = new Object[i + 1];
        this.tail = this.head;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void add(T t) {
        int i = this.capacity;
        int i2 = this.offset;
        if (i2 == i) {
            Object[] objArr = new Object[i + 1];
            this.tail[i] = objArr;
            this.tail = objArr;
            i2 = 0;
        }
        this.tail[i2] = t;
        this.offset = i2 + 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void forEachWhile(NonThrowingPredicate<? super T> nonThrowingPredicate) {
        int i;
        int i2 = this.capacity;
        for (Object[] objArr = this.head; objArr != null; objArr = objArr[i2]) {
            while (i < i2) {
                Object obj = objArr[i];
                i = (obj == null || nonThrowingPredicate.test(obj)) ? 0 : i + 1;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean accept(Relay<? super T> relay) {
        Object[] objArr = this.head;
        int i = this.capacity;
        while (true) {
            if (objArr == null) {
                return false;
            }
            for (int i2 = 0; i2 < i; i2++) {
                Object obj = objArr[i2];
                if (obj == null) {
                    break;
                }
                relay.accept(obj);
            }
            objArr = objArr[i];
        }
    }
}
