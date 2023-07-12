package org.eclipse.paho.client.mqttv3.internal.websocket;

import com.treadly.client.lib.sdk.Model.OtaUpdateInfo;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Random;

/* loaded from: classes2.dex */
public class WebSocketFrame {
    public static final int frameLengthOverhead = 6;
    private boolean closeFlag;
    private boolean fin;
    private byte opcode;
    private byte[] payload;

    public byte getOpcode() {
        return this.opcode;
    }

    public boolean isFin() {
        return this.fin;
    }

    public byte[] getPayload() {
        return this.payload;
    }

    public boolean isCloseFlag() {
        return this.closeFlag;
    }

    public WebSocketFrame(byte b, boolean z, byte[] bArr) {
        this.closeFlag = false;
        this.opcode = b;
        this.fin = z;
        this.payload = bArr;
    }

    public WebSocketFrame(byte[] bArr) {
        this.closeFlag = false;
        ByteBuffer wrap = ByteBuffer.wrap(bArr);
        setFinAndOpCode(wrap.get());
        byte b = wrap.get();
        boolean z = (b & 128) != 0;
        int i = (byte) (b & Byte.MAX_VALUE);
        int i2 = i == 127 ? 8 : i == 126 ? 2 : 0;
        while (true) {
            i2--;
            if (i2 <= 0) {
                break;
            }
            i |= (wrap.get() & 255) << (i2 * 8);
        }
        byte[] bArr2 = null;
        if (z) {
            byte[] bArr3 = new byte[4];
            wrap.get(bArr3, 0, 4);
            bArr2 = bArr3;
        }
        this.payload = new byte[i];
        wrap.get(this.payload, 0, i);
        if (z) {
            for (int i3 = 0; i3 < this.payload.length; i3++) {
                byte[] bArr4 = this.payload;
                bArr4[i3] = (byte) (bArr4[i3] ^ bArr2[i3 % 4]);
            }
        }
    }

    private void setFinAndOpCode(byte b) {
        this.fin = (b & 128) != 0;
        this.opcode = (byte) (b & 15);
    }

    public WebSocketFrame(InputStream inputStream) throws IOException {
        byte[] bArr;
        this.closeFlag = false;
        setFinAndOpCode((byte) inputStream.read());
        int i = 2;
        if (this.opcode == 2) {
            byte read = (byte) inputStream.read();
            boolean z = (read & 128) != 0;
            int i2 = (byte) (read & Byte.MAX_VALUE);
            if (i2 == 127) {
                i = 8;
            } else if (i2 != 126) {
                i = 0;
            }
            i2 = i > 0 ? 0 : i2;
            while (true) {
                i--;
                if (i < 0) {
                    break;
                }
                i2 |= (((byte) inputStream.read()) & 255) << (i * 8);
            }
            if (z) {
                bArr = new byte[4];
                inputStream.read(bArr, 0, 4);
            } else {
                bArr = null;
            }
            this.payload = new byte[i2];
            int i3 = 0;
            int i4 = i2;
            while (i3 != i2) {
                int read2 = inputStream.read(this.payload, i3, i4);
                i3 += read2;
                i4 -= read2;
            }
            if (z) {
                for (int i5 = 0; i5 < this.payload.length; i5++) {
                    byte[] bArr2 = this.payload;
                    bArr2[i5] = (byte) (bArr2[i5] ^ bArr[i5 % 4]);
                }
            }
        } else if (this.opcode == 8) {
            this.closeFlag = true;
        } else {
            StringBuffer stringBuffer = new StringBuffer("Invalid Frame: Opcode: ");
            stringBuffer.append((int) this.opcode);
            throw new IOException(stringBuffer.toString());
        }
    }

    public byte[] encodeFrame() {
        int length = this.payload.length + 6;
        if (this.payload.length > 65535) {
            length += 8;
        } else if (this.payload.length >= 126) {
            length += 2;
        }
        ByteBuffer allocate = ByteBuffer.allocate(length);
        appendFinAndOpCode(allocate, this.opcode, this.fin);
        byte[] generateMaskingKey = generateMaskingKey();
        appendLengthAndMask(allocate, this.payload.length, generateMaskingKey);
        for (int i = 0; i < this.payload.length; i++) {
            byte[] bArr = this.payload;
            byte b = (byte) (bArr[i] ^ generateMaskingKey[i % 4]);
            bArr[i] = b;
            allocate.put(b);
        }
        allocate.flip();
        return allocate.array();
    }

    public static void appendLengthAndMask(ByteBuffer byteBuffer, int i, byte[] bArr) {
        if (bArr != null) {
            appendLength(byteBuffer, i, true);
            byteBuffer.put(bArr);
            return;
        }
        appendLength(byteBuffer, i, false);
    }

    private static void appendLength(ByteBuffer byteBuffer, int i, boolean z) {
        if (i < 0) {
            throw new IllegalArgumentException("Length cannot be negative");
        }
        int i2 = z ? -128 : 0;
        if (i <= 65535) {
            if (i >= 126) {
                byteBuffer.put((byte) (i2 | OtaUpdateInfo.urlMaxSize));
                byteBuffer.put((byte) (i >> 8));
                byteBuffer.put((byte) (i & 255));
                return;
            }
            byteBuffer.put((byte) (i | i2));
            return;
        }
        byteBuffer.put((byte) (i2 | 127));
        byteBuffer.put((byte) 0);
        byteBuffer.put((byte) 0);
        byteBuffer.put((byte) 0);
        byteBuffer.put((byte) 0);
        byteBuffer.put((byte) ((i >> 24) & 255));
        byteBuffer.put((byte) ((i >> 16) & 255));
        byteBuffer.put((byte) ((i >> 8) & 255));
        byteBuffer.put((byte) (i & 255));
    }

    public static void appendFinAndOpCode(ByteBuffer byteBuffer, byte b, boolean z) {
        byteBuffer.put((byte) ((b & 15) | (z ? (byte) 128 : (byte) 0)));
    }

    public static byte[] generateMaskingKey() {
        Random random = new Random();
        return new byte[]{(byte) random.nextInt(255), (byte) random.nextInt(255), (byte) random.nextInt(255), (byte) random.nextInt(255)};
    }
}
