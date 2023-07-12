package com.google.android.gms.internal.stable;

/* loaded from: classes.dex */
final class zzo extends zzl {
    private final zzm zzahm = new zzm();

    @Override // com.google.android.gms.internal.stable.zzl
    public final void zza(Throwable th, Throwable th2) {
        if (th2 == th) {
            throw new IllegalArgumentException("Self suppression is not allowed.", th2);
        }
        if (th2 == null) {
            throw new NullPointerException("The suppressed exception cannot be null.");
        }
        this.zzahm.zza(th, true).add(th2);
    }
}
