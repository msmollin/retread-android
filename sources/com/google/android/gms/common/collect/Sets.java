package com.google.android.gms.common.collect;

import com.google.android.gms.common.internal.Preconditions;
import java.util.HashSet;
import java.util.Set;

/* loaded from: classes.dex */
public class Sets {
    public static <E> Set<E> difference(Set<? extends E> set, Set<? extends E> set2) {
        Preconditions.checkNotNull(set);
        Preconditions.checkNotNull(set2);
        HashSet hashSet = new HashSet();
        for (E e : set) {
            if (!set2.contains(e)) {
                hashSet.add(e);
            }
        }
        return hashSet;
    }

    public static <E> Set<E> union(Iterable<Set<E>> iterable) {
        Preconditions.checkNotNull(iterable);
        HashSet hashSet = new HashSet();
        for (Set<E> set : iterable) {
            hashSet.addAll(set);
        }
        return hashSet;
    }

    public static <E> Set<E> union(Set<? extends E> set, Set<? extends E> set2) {
        Preconditions.checkNotNull(set);
        Preconditions.checkNotNull(set2);
        HashSet hashSet = new HashSet(set);
        hashSet.addAll(set2);
        return hashSet;
    }
}
