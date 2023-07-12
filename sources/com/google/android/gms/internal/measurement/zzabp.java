package com.google.android.gms.internal.measurement;

/* JADX WARN: Enum visitor error
jadx.core.utils.exceptions.JadxRuntimeException: Init of enum zzbvr uses external variables
	at jadx.core.dex.visitors.EnumVisitor.createEnumFieldByConstructor(EnumVisitor.java:444)
	at jadx.core.dex.visitors.EnumVisitor.processEnumFieldByField(EnumVisitor.java:368)
	at jadx.core.dex.visitors.EnumVisitor.processEnumFieldByWrappedInsn(EnumVisitor.java:333)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromFilledArray(EnumVisitor.java:318)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromInsn(EnumVisitor.java:258)
	at jadx.core.dex.visitors.EnumVisitor.convertToEnum(EnumVisitor.java:151)
	at jadx.core.dex.visitors.EnumVisitor.visit(EnumVisitor.java:100)
 */
/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* loaded from: classes.dex */
public class zzabp {
    public static final zzabp zzbvj = new zzabp("DOUBLE", 0, zzabu.DOUBLE, 1);
    public static final zzabp zzbvk = new zzabp("FLOAT", 1, zzabu.FLOAT, 5);
    public static final zzabp zzbvl = new zzabp("INT64", 2, zzabu.LONG, 0);
    public static final zzabp zzbvm = new zzabp("UINT64", 3, zzabu.LONG, 0);
    public static final zzabp zzbvn = new zzabp("INT32", 4, zzabu.INT, 0);
    public static final zzabp zzbvo = new zzabp("FIXED64", 5, zzabu.LONG, 1);
    public static final zzabp zzbvp = new zzabp("FIXED32", 6, zzabu.INT, 5);
    public static final zzabp zzbvq = new zzabp("BOOL", 7, zzabu.BOOLEAN, 0);
    public static final zzabp zzbvr;
    public static final zzabp zzbvs;
    public static final zzabp zzbvt;
    public static final zzabp zzbvu;
    public static final zzabp zzbvv;
    public static final zzabp zzbvw;
    public static final zzabp zzbvx;
    public static final zzabp zzbvy;
    public static final zzabp zzbvz;
    public static final zzabp zzbwa;
    private static final /* synthetic */ zzabp[] zzbwd;
    private final zzabu zzbwb;
    private final int zzbwc;

    static {
        final zzabu zzabuVar = zzabu.STRING;
        zzbvr = new zzabp("STRING", 8, zzabuVar, 2) { // from class: com.google.android.gms.internal.measurement.zzabq
        };
        final zzabu zzabuVar2 = zzabu.MESSAGE;
        zzbvs = new zzabp("GROUP", 9, zzabuVar2, 3) { // from class: com.google.android.gms.internal.measurement.zzabr
        };
        final zzabu zzabuVar3 = zzabu.MESSAGE;
        zzbvt = new zzabp("MESSAGE", 10, zzabuVar3, 2) { // from class: com.google.android.gms.internal.measurement.zzabs
        };
        final zzabu zzabuVar4 = zzabu.BYTE_STRING;
        zzbvu = new zzabp("BYTES", 11, zzabuVar4, 2) { // from class: com.google.android.gms.internal.measurement.zzabt
        };
        zzbvv = new zzabp("UINT32", 12, zzabu.INT, 0);
        zzbvw = new zzabp("ENUM", 13, zzabu.ENUM, 0);
        zzbvx = new zzabp("SFIXED32", 14, zzabu.INT, 5);
        zzbvy = new zzabp("SFIXED64", 15, zzabu.LONG, 1);
        zzbvz = new zzabp("SINT32", 16, zzabu.INT, 0);
        zzbwa = new zzabp("SINT64", 17, zzabu.LONG, 0);
        zzbwd = new zzabp[]{zzbvj, zzbvk, zzbvl, zzbvm, zzbvn, zzbvo, zzbvp, zzbvq, zzbvr, zzbvs, zzbvt, zzbvu, zzbvv, zzbvw, zzbvx, zzbvy, zzbvz, zzbwa};
    }

    private zzabp(String str, int i, zzabu zzabuVar, int i2) {
        this.zzbwb = zzabuVar;
        this.zzbwc = i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzabp(String str, int i, zzabu zzabuVar, int i2, zzabo zzaboVar) {
        this(str, i, zzabuVar, i2);
    }

    public static zzabp[] values() {
        return (zzabp[]) zzbwd.clone();
    }

    public final zzabu zzuv() {
        return this.zzbwb;
    }
}
