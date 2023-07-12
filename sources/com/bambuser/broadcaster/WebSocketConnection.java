package com.bambuser.broadcaster;

import android.util.Base64;
import android.util.Log;
import com.bambuser.broadcaster.Connection;
import com.bambuser.broadcaster.RawPacket;
import com.bumptech.glide.load.Key;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class WebSocketConnection {
    private static final String LOGTAG = "WebSocketConnection";
    private static final int MAX_LATE_PING = 1;
    private static final int PING_INTERVAL = 5;
    private volatile boolean mClosed;
    private final Connection mConn;
    private final Connection.Observer mConnObserver;
    private final byte[] mCtrlBuf;
    private int mCurOpcode;
    private String mExpectedAccept;
    private volatile boolean mGotReply;
    private final byte[] mHeaderBuf;
    private String mHost;
    private long mLastPingSent;
    private long mLastPongReceived;
    private final byte[] mMaskBuf;
    private byte[] mMsgBuf;
    private int mMsgBufPos;
    private Observer mObserver;
    private final RawPacket.Observer mPacketSendObserver;
    private String mPath;
    private Timer mPingTimer;
    private final Random mRand;
    private final String mSubprotocols;
    private int mUnsentPackets;
    private final String mUserAgent;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface Observer {
        void onConnected(String str);

        void onConnectionClosed();

        void onReceivedBinary(ByteBuffer byteBuffer);

        void onReceivedString(String str);
    }

    static /* synthetic */ int access$910(WebSocketConnection webSocketConnection) {
        int i = webSocketConnection.mUnsentPackets;
        webSocketConnection.mUnsentPackets = i - 1;
        return i;
    }

    WebSocketConnection(URI uri, String str, Observer observer) {
        this(uri, str, null, observer);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WebSocketConnection(URI uri, String str, String str2, Observer observer) {
        int i;
        boolean z;
        this.mConnObserver = new Connection.Observer() { // from class: com.bambuser.broadcaster.WebSocketConnection.1
            @Override // com.bambuser.broadcaster.Connection.Observer
            public void onReceived(ByteBuffer byteBuffer) {
                WebSocketConnection.this.handleWebSocketData(byteBuffer);
            }

            @Override // com.bambuser.broadcaster.Connection.Observer
            public void onConnected() {
                if (WebSocketConnection.this.mConn.getError() != ConnectionError.NONE) {
                    onConnectionClosed();
                }
            }

            @Override // com.bambuser.broadcaster.Connection.Observer
            public void onConnectionClosed() {
                WebSocketConnection.this.onConnectionClosed();
            }
        };
        this.mMaskBuf = new byte[4];
        this.mCtrlBuf = new byte[128];
        this.mHeaderBuf = new byte[14];
        this.mPacketSendObserver = new RawPacket.Observer() { // from class: com.bambuser.broadcaster.WebSocketConnection.2
            @Override // com.bambuser.broadcaster.RawPacket.Observer
            public void onBlockEnqueued() {
            }

            @Override // com.bambuser.broadcaster.RawPacket.Observer
            public void onBlockSent() {
                synchronized (WebSocketConnection.this) {
                    WebSocketConnection.access$910(WebSocketConnection.this);
                    if (WebSocketConnection.this.mClosed && WebSocketConnection.this.mUnsentPackets == 0) {
                        WebSocketConnection.this.onConnectionClosed();
                    }
                }
            }
        };
        this.mSubprotocols = str;
        this.mUserAgent = str2;
        this.mObserver = observer;
        this.mRand = new Random();
        String scheme = uri.getScheme();
        if (scheme == null) {
            throw new IllegalArgumentException("Bad websocket uri");
        }
        if (scheme.equals("wss")) {
            i = 443;
            z = true;
        } else if (!scheme.equals("ws")) {
            throw new IllegalArgumentException("Bad websocket protocol " + scheme);
        } else {
            i = 80;
            z = false;
        }
        boolean z2 = z;
        int port = uri.getPort() > 0 ? uri.getPort() : i;
        this.mPath = uri.getRawPath();
        if (this.mPath == null || this.mPath.isEmpty()) {
            this.mPath = MqttTopic.TOPIC_LEVEL_SEPARATOR;
        }
        if (uri.getRawQuery() != null) {
            this.mPath += "?" + uri.getRawQuery();
        }
        this.mHost = uri.getHost();
        if (this.mHost == null) {
            throw new IllegalArgumentException("Bad host in websocket uri");
        }
        if (uri.getPort() > 0) {
            this.mHost += ":" + uri.getPort();
        }
        this.mConn = new WebSocketUpgradeConnection(uri.getHost(), port, z2, this.mConnObserver);
        this.mConn.connect();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendWebsocketHandshake() {
        byte[] bArr = new byte[16];
        this.mRand.nextBytes(bArr);
        String encodeToString = Base64.encodeToString(bArr, 2);
        String str = encodeToString + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(str.getBytes());
            this.mExpectedAccept = Base64.encodeToString(messageDigest.digest(), 2);
            String str2 = ((("GET " + this.mPath + " HTTP/1.1\r\n") + "Host: " + this.mHost + "\r\n") + "Upgrade: websocket\r\n") + "Connection: Upgrade\r\n";
            if (this.mSubprotocols != null) {
                str2 = str2 + "Sec-WebSocket-Protocol: " + this.mSubprotocols + "\r\n";
            }
            String str3 = (str2 + "Sec-WebSocket-Key: " + encodeToString + "\r\n") + "Sec-WebSocket-Version: 13\r\n";
            if (this.mUserAgent != null && this.mUserAgent.length() > 0) {
                str3 = str3 + "User-Agent: " + this.mUserAgent + "\r\n";
            }
            try {
                byte[] bytes = (str3 + "\r\n").getBytes(Key.STRING_CHARSET_NAME);
                this.mConn.send(new RawPacket(null, bytes.length, null).write(bytes));
            } catch (Exception unused) {
                onConnectionClosed();
            }
        } catch (Exception unused2) {
            onConnectionClosed();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isConnected() {
        return this.mGotReply && !this.mClosed;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void handleWebSocketData(ByteBuffer byteBuffer) {
        int i;
        byte[] bArr;
        int i2;
        int i3;
        byte[] bArr2;
        int i4;
        if (byteBuffer.remaining() < 2) {
            return;
        }
        int i5 = byteBuffer.get(byteBuffer.position() + 1) & Byte.MAX_VALUE;
        int i6 = byteBuffer.get(byteBuffer.position() + 1) & 128;
        int i7 = i6 != 0 ? 6 : 2;
        if (i5 == 126) {
            i7 += 2;
        } else if (i5 == 127) {
            i7 += 8;
        }
        int i8 = byteBuffer.get(byteBuffer.position() + 0) & 128;
        int i9 = byteBuffer.get(byteBuffer.position() + 0) & 112;
        int i10 = byteBuffer.get(byteBuffer.position() + 0) & 15;
        if (byteBuffer.remaining() < i7) {
            return;
        }
        if (i5 == 126) {
            i = 0;
            for (int i11 = 0; i11 < 2; i11++) {
                i = (i << 8) | (byteBuffer.get(byteBuffer.position() + 2 + i11) & 255);
            }
        } else if (i5 == 127) {
            i = 0;
            for (int i12 = 0; i12 < 8; i12++) {
                i = (i << 8) | (byteBuffer.get(byteBuffer.position() + 2 + i12) & 255);
            }
        } else {
            i = i5;
        }
        if (byteBuffer.remaining() < i7 + i) {
            return;
        }
        if (i9 != 0) {
            this.mClosed = true;
        }
        if (i6 != 0) {
            byteBuffer.position((byteBuffer.position() + i7) - 4);
            byteBuffer.get(this.mMaskBuf);
        } else {
            byteBuffer.position(byteBuffer.position() + i7);
        }
        if (i10 >= 8) {
            bArr = this.mCtrlBuf;
            i3 = 125;
            i2 = 0;
        } else {
            bArr = this.mMsgBuf;
            i2 = this.mMsgBufPos;
            i3 = 1048576;
        }
        if ((i10 >= 3 && i10 <= 7) || i10 >= 11) {
            this.mClosed = true;
        }
        if (i10 >= 8) {
            if (i > 125) {
                this.mClosed = true;
            }
            if (i8 == 0) {
                this.mClosed = true;
            }
        } else if (i10 != 0) {
            if (this.mMsgBufPos > 0) {
                this.mClosed = true;
            }
            this.mCurOpcode = i10;
            this.mMsgBufPos = 0;
        } else if (this.mCurOpcode == 0) {
            this.mClosed = true;
        }
        if (i > i3 || i2 > i3 || (i4 = i2 + i) > i3) {
            if (i10 < 8) {
                this.mMsgBufPos = 0;
            }
            bArr2 = null;
        } else {
            if (bArr != null && i4 < bArr.length) {
                bArr2 = bArr;
            }
            bArr2 = new byte[i4 * 2];
            if (bArr != null) {
                System.arraycopy(bArr, 0, bArr2, 0, i2);
            }
            this.mMsgBuf = bArr2;
        }
        if (bArr2 != null) {
            byteBuffer.get(bArr2, i2, i);
            if (i6 != 0) {
                for (int i13 = 0; i13 < i; i13++) {
                    int i14 = i2 + i13;
                    bArr2[i14] = (byte) (bArr2[i14] ^ this.mMaskBuf[i13 & 3]);
                }
            }
        } else {
            byteBuffer.position(byteBuffer.position() + i);
        }
        if (i10 < 8) {
            this.mMsgBufPos += i;
        }
        int i15 = i2 + i;
        if (i10 >= 8 || i8 != 0) {
            if (bArr2 != null) {
                if (i10 == 0) {
                    i10 = this.mCurOpcode;
                }
                handleMessage(i10, bArr2, i15);
                if (i10 < 8) {
                    this.mMsgBuf = null;
                }
            }
            if (i10 < 8) {
                this.mMsgBufPos = 0;
                this.mCurOpcode = 0;
            }
        }
        if (this.mClosed && this.mUnsentPackets == 0) {
            onConnectionClosed();
        }
    }

    private void handleMessage(int i, byte[] bArr, int i2) {
        if (this.mClosed) {
            return;
        }
        if (i == 1) {
            try {
                String str = new String(bArr, 0, i2, Key.STRING_CHARSET_NAME);
                if (this.mObserver != null) {
                    this.mObserver.onReceivedString(str);
                }
            } catch (Exception e) {
                Log.w(LOGTAG, "exception when converting from utf8: " + e);
                this.mClosed = true;
            }
        } else if (i == 2) {
            if (this.mObserver != null) {
                this.mObserver.onReceivedBinary(ByteBuffer.wrap(bArr, 0, i2));
            }
        } else if (i == 8) {
            sendClose();
        } else if (i == 9) {
            sendPacket(10, bArr, 0, i2);
        } else if (i == 10 && i2 == 8) {
            this.mLastPongReceived = ByteBuffer.wrap(bArr, 0, i2).order(ByteOrder.BIG_ENDIAN).getLong();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onConnectionClosed() {
        if (this.mObserver != null) {
            this.mObserver.onConnectionClosed();
        }
        disconnect();
    }

    private synchronized void sendPacket(int i, byte[] bArr, int i2, int i3) {
        if (this.mGotReply && !this.mClosed) {
            if (i == 8) {
                this.mClosed = true;
            }
            RawPacket rawPacket = new RawPacket(null, i3 + 14, this.mPacketSendObserver);
            this.mHeaderBuf[0] = (byte) (i | 128);
            this.mHeaderBuf[1] = Byte.MIN_VALUE;
            if (i3 < 126) {
                byte[] bArr2 = this.mHeaderBuf;
                bArr2[1] = (byte) (bArr2[1] | i3);
                rawPacket.write(this.mHeaderBuf, 0, 2);
            } else if (i3 < 65536) {
                byte[] bArr3 = this.mHeaderBuf;
                bArr3[1] = (byte) (126 | bArr3[1]);
                this.mHeaderBuf[2] = (byte) ((i3 >> 8) & 255);
                this.mHeaderBuf[3] = (byte) ((i3 >> 0) & 255);
                rawPacket.write(this.mHeaderBuf, 0, 4);
            } else {
                byte[] bArr4 = this.mHeaderBuf;
                bArr4[1] = (byte) (bArr4[1] | Byte.MAX_VALUE);
                byte[] bArr5 = this.mHeaderBuf;
                byte[] bArr6 = this.mHeaderBuf;
                byte[] bArr7 = this.mHeaderBuf;
                this.mHeaderBuf[5] = 0;
                bArr7[4] = 0;
                bArr6[3] = 0;
                bArr5[2] = 0;
                this.mHeaderBuf[6] = (byte) ((i3 >> 24) & 255);
                this.mHeaderBuf[7] = (byte) ((i3 >> 16) & 255);
                this.mHeaderBuf[8] = (byte) ((i3 >> 8) & 255);
                this.mHeaderBuf[9] = (byte) ((i3 >> 0) & 255);
                rawPacket.write(this.mHeaderBuf, 0, 10);
            }
            this.mRand.nextBytes(this.mHeaderBuf);
            rawPacket.write(this.mHeaderBuf, 0, 4);
            if (bArr != null) {
                ByteBuffer dataBuffer = rawPacket.getDataBuffer();
                byte[] array = dataBuffer.array();
                int arrayOffset = dataBuffer.arrayOffset() + dataBuffer.position();
                for (int i4 = 0; i4 < i3; i4++) {
                    array[arrayOffset + i4] = (byte) (bArr[i2 + i4] ^ this.mHeaderBuf[i4 & 3]);
                }
                dataBuffer.position(dataBuffer.position() + i3);
            }
            this.mUnsentPackets++;
            this.mConn.send(rawPacket);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void sendString(String str) {
        byte[] uTF8FromString = MovinoUtils.getUTF8FromString(str);
        sendPacket(1, uTF8FromString, 0, uTF8FromString.length);
    }

    void sendBinary(ByteBuffer byteBuffer) {
        sendPacket(2, byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), byteBuffer.remaining());
    }

    private void sendClose() {
        sendPacket(8, null, 0, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void close() {
        if (!this.mGotReply) {
            disconnect();
            return;
        }
        sendClose();
        synchronized (this) {
            this.mObserver = null;
        }
    }

    void disconnect() {
        synchronized (this) {
            this.mClosed = true;
            this.mObserver = null;
            if (this.mPingTimer != null) {
                this.mPingTimer.cancel();
                this.mPingTimer = null;
            }
        }
        this.mConn.disconnect();
    }

    /* loaded from: classes.dex */
    private final class WebSocketUpgradeConnection extends HttpUpgradeConnection {
        WebSocketUpgradeConnection(String str, int i, boolean z, Connection.Observer observer) {
            super(str, i, z, observer);
        }

        @Override // com.bambuser.broadcaster.HttpUpgradeConnection
        void sendInitialRequest() {
            WebSocketConnection.this.sendWebsocketHandshake();
        }

        @Override // com.bambuser.broadcaster.HttpUpgradeConnection
        boolean verifyHttpResponseHeaders(Map<String, String> map) {
            String str = map.get("Connection");
            String str2 = map.get("Upgrade");
            String str3 = map.get("Sec-WebSocket-Accept");
            String str4 = map.get("Sec-WebSocket-Protocol");
            if (str2 == null || str == null || str3 == null || !str.toLowerCase(Locale.US).contains("upgrade") || !str2.toLowerCase(Locale.US).contains("websocket") || !str3.equals(WebSocketConnection.this.mExpectedAccept)) {
                return false;
            }
            WebSocketConnection.this.mGotReply = true;
            if (WebSocketConnection.this.mObserver != null) {
                WebSocketConnection.this.mObserver.onConnected(str4);
            }
            WebSocketConnection.this.mPingTimer = new Timer("PingTimer");
            WebSocketConnection.this.mPingTimer.scheduleAtFixedRate(new TimerTask() { // from class: com.bambuser.broadcaster.WebSocketConnection.WebSocketUpgradeConnection.1
                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    WebSocketConnection.this.keepalive();
                }
            }, 5000L, 5000L);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void keepalive() {
        if (this.mClosed) {
            return;
        }
        if (this.mLastPongReceived < this.mLastPingSent - 1) {
            Log.w(LOGTAG, "websocket ping timeout, last sent " + this.mLastPingSent + ", last received " + this.mLastPongReceived);
            this.mClosed = true;
            onConnectionClosed();
            return;
        }
        this.mLastPingSent++;
        ByteBuffer order = ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN);
        order.putLong(this.mLastPingSent);
        order.flip();
        sendPacket(9, order.array(), order.arrayOffset() + order.position(), order.remaining());
    }
}
