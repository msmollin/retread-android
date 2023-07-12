package com.facebook.soloader;

import com.bambuser.broadcaster.Movino;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

/* loaded from: classes.dex */
public final class MinElf {
    public static final int DT_NEEDED = 1;
    public static final int DT_NULL = 0;
    public static final int DT_STRTAB = 5;
    public static final int ELF_MAGIC = 1179403647;
    public static final int PN_XNUM = 65535;
    public static final int PT_DYNAMIC = 2;
    public static final int PT_LOAD = 1;

    public static String[] extract_DT_NEEDED(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            return extract_DT_NEEDED(fileInputStream.getChannel());
        } finally {
            fileInputStream.close();
        }
    }

    public static String[] extract_DT_NEEDED(FileChannel fileChannel) throws IOException {
        long j;
        long j2;
        int i;
        long j3;
        boolean z;
        long j4;
        long j5;
        long j6;
        long j7;
        long j8;
        long j9;
        long j10;
        long j11;
        long j12;
        long j13;
        long j14;
        long j15;
        long j16;
        ByteBuffer allocate = ByteBuffer.allocate(8);
        allocate.order(ByteOrder.LITTLE_ENDIAN);
        if (getu32(fileChannel, allocate, 0L) != 1179403647) {
            throw new ElfError("file is not ELF");
        }
        boolean z2 = getu8(fileChannel, allocate, 4L) == 1;
        if (getu8(fileChannel, allocate, 5L) == 2) {
            allocate.order(ByteOrder.BIG_ENDIAN);
        }
        if (z2) {
            j = getu32(fileChannel, allocate, 28L);
        } else {
            j = get64(fileChannel, allocate, 32L);
        }
        if (z2) {
            j2 = getu16(fileChannel, allocate, 44L);
        } else {
            j2 = getu16(fileChannel, allocate, 56L);
        }
        if (z2) {
            i = getu16(fileChannel, allocate, 42L);
        } else {
            i = getu16(fileChannel, allocate, 54L);
        }
        if (j2 == 65535) {
            if (z2) {
                j15 = getu32(fileChannel, allocate, 32L);
            } else {
                j15 = get64(fileChannel, allocate, 40L);
            }
            if (z2) {
                j16 = getu32(fileChannel, allocate, j15 + 28);
            } else {
                j16 = getu32(fileChannel, allocate, j15 + 44);
            }
            j2 = j16;
        }
        long j17 = j;
        long j18 = 0;
        while (true) {
            if (j18 >= j2) {
                j3 = 0;
                break;
            }
            if (z2) {
                j14 = getu32(fileChannel, allocate, j17 + 0);
            } else {
                j14 = getu32(fileChannel, allocate, j17 + 0);
            }
            if (j14 != 2) {
                j17 += i;
                j18++;
            } else if (z2) {
                j3 = getu32(fileChannel, allocate, j17 + 4);
            } else {
                j3 = get64(fileChannel, allocate, j17 + 8);
            }
        }
        long j19 = 0;
        if (j3 == 0) {
            throw new ElfError("ELF file does not contain dynamic linking information");
        }
        long j20 = j3;
        long j21 = 0;
        int i2 = 0;
        while (true) {
            if (z2) {
                z = z2;
                j4 = getu32(fileChannel, allocate, j20 + j19);
            } else {
                z = z2;
                j4 = get64(fileChannel, allocate, j20 + j19);
            }
            long j22 = j3;
            if (j4 == 1) {
                if (i2 == Integer.MAX_VALUE) {
                    throw new ElfError("malformed DT_NEEDED section");
                }
                i2++;
            } else if (j4 == 5) {
                if (z) {
                    j5 = getu32(fileChannel, allocate, j20 + 4);
                } else {
                    j5 = get64(fileChannel, allocate, j20 + 8);
                }
                j21 = j5;
            }
            long j23 = 16;
            j20 += z ? 8L : 16L;
            j19 = 0;
            if (j4 != 0) {
                z2 = z;
                j3 = j22;
            } else if (j21 == 0) {
                throw new ElfError("Dynamic section string-table not found");
            } else {
                int i3 = 0;
                while (true) {
                    if (i3 >= j2) {
                        j6 = 0;
                        break;
                    }
                    if (z) {
                        j9 = getu32(fileChannel, allocate, j + j19);
                    } else {
                        j9 = getu32(fileChannel, allocate, j + j19);
                    }
                    if (j9 == 1) {
                        if (z) {
                            j11 = getu32(fileChannel, allocate, j + 8);
                        } else {
                            j11 = get64(fileChannel, allocate, j + j23);
                        }
                        if (z) {
                            j10 = j2;
                            j12 = getu32(fileChannel, allocate, j + 20);
                        } else {
                            j10 = j2;
                            j12 = get64(fileChannel, allocate, j + 40);
                        }
                        if (j11 <= j21 && j21 < j12 + j11) {
                            if (z) {
                                j13 = getu32(fileChannel, allocate, j + 4);
                            } else {
                                j13 = get64(fileChannel, allocate, j + 8);
                            }
                            j6 = j13 + (j21 - j11);
                        }
                    } else {
                        j10 = j2;
                    }
                    j += i;
                    i3++;
                    j2 = j10;
                    j23 = 16;
                    j19 = 0;
                }
                long j24 = 0;
                if (j6 == 0) {
                    throw new ElfError("did not find file offset of DT_STRTAB table");
                }
                String[] strArr = new String[i2];
                int i4 = 0;
                while (true) {
                    if (z) {
                        j7 = getu32(fileChannel, allocate, j22 + j24);
                    } else {
                        j7 = get64(fileChannel, allocate, j22 + j24);
                    }
                    if (j7 == 1) {
                        if (z) {
                            j8 = getu32(fileChannel, allocate, j22 + 4);
                        } else {
                            j8 = get64(fileChannel, allocate, j22 + 8);
                        }
                        strArr[i4] = getSz(fileChannel, allocate, j8 + j6);
                        if (i4 == Integer.MAX_VALUE) {
                            throw new ElfError("malformed DT_NEEDED section");
                        }
                        i4++;
                    }
                    j22 += z ? 8L : 16L;
                    if (j7 == 0) {
                        if (i4 == strArr.length) {
                            return strArr;
                        }
                        throw new ElfError("malformed DT_NEEDED section");
                    }
                    j24 = 0;
                }
            }
        }
    }

    private static String getSz(FileChannel fileChannel, ByteBuffer byteBuffer, long j) throws IOException {
        StringBuilder sb = new StringBuilder();
        while (true) {
            long j2 = 1 + j;
            short u8Var = getu8(fileChannel, byteBuffer, j);
            if (u8Var != 0) {
                sb.append((char) u8Var);
                j = j2;
            } else {
                return sb.toString();
            }
        }
    }

    private static void read(FileChannel fileChannel, ByteBuffer byteBuffer, int i, long j) throws IOException {
        int read;
        byteBuffer.position(0);
        byteBuffer.limit(i);
        while (byteBuffer.remaining() > 0 && (read = fileChannel.read(byteBuffer, j)) != -1) {
            j += read;
        }
        if (byteBuffer.remaining() > 0) {
            throw new ElfError("ELF file truncated");
        }
        byteBuffer.position(0);
    }

    private static long get64(FileChannel fileChannel, ByteBuffer byteBuffer, long j) throws IOException {
        read(fileChannel, byteBuffer, 8, j);
        return byteBuffer.getLong();
    }

    private static long getu32(FileChannel fileChannel, ByteBuffer byteBuffer, long j) throws IOException {
        read(fileChannel, byteBuffer, 4, j);
        return byteBuffer.getInt() & Movino.ONES_32;
    }

    private static int getu16(FileChannel fileChannel, ByteBuffer byteBuffer, long j) throws IOException {
        read(fileChannel, byteBuffer, 2, j);
        return byteBuffer.getShort() & 65535;
    }

    private static short getu8(FileChannel fileChannel, ByteBuffer byteBuffer, long j) throws IOException {
        read(fileChannel, byteBuffer, 1, j);
        return (short) (byteBuffer.get() & 255);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ElfError extends RuntimeException {
        ElfError(String str) {
            super(str);
        }
    }
}
