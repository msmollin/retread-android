package com.bambuser.broadcaster;

import com.bambuser.broadcaster.Connection;
import java.util.Map;

/* loaded from: classes.dex */
class MovinoUpgradeConnection extends HttpUpgradeConnection {
    /* JADX INFO: Access modifiers changed from: package-private */
    public MovinoUpgradeConnection(String str, int i, boolean z, Connection.Observer observer) {
        super(str, i, z, observer);
    }

    @Override // com.bambuser.broadcaster.HttpUpgradeConnection
    void sendInitialRequest() {
        byte[] uTF8FromString = MovinoUtils.getUTF8FromString("GET /movino HTTP/1.1\r\nUpgrade: movino\r\nConnection: Upgrade\r\n\r\n");
        send(new RawPacket(null, uTF8FromString.length, null).write(uTF8FromString));
    }

    @Override // com.bambuser.broadcaster.HttpUpgradeConnection
    boolean verifyHttpResponseHeaders(Map<String, String> map) {
        String str = map.get("Connection");
        String str2 = map.get("Upgrade");
        return str != null && str2 != null && str.contains("Upgrade") && str2.contains("movino");
    }
}
