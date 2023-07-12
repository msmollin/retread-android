package com.bambuser.broadcaster;

import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/* loaded from: classes.dex */
final class XMLUtils {
    private static final String LOGTAG = "XMLUtils";
    private static BroadcastElement sBroadcastInfo;
    private static List<BroadcastInfoObserver> sObservers = new LinkedList();

    /* loaded from: classes.dex */
    public interface BroadcastInfoObserver {
        void onNewInfo();
    }

    XMLUtils() {
    }

    public static Document parse(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        try {
            try {
                return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
            } catch (IOException e) {
                Log.e(LOGTAG, "DocumentBuilder IOException", e);
                return null;
            } catch (SAXException e2) {
                Log.e(LOGTAG, "DocumentBuilder SAXException", e2);
                return null;
            }
        } catch (ParserConfigurationException e3) {
            Log.e(LOGTAG, "DocumentBuilderFactory ParserConfigurationException", e3);
            return null;
        }
    }

    public static BroadcastElement parseBroadcastInfo(InputStream inputStream) {
        Document parse = parse(inputStream);
        if (parse == null) {
            return null;
        }
        Element documentElement = parse.getDocumentElement();
        if (documentElement == null || !BroadcastElement.ELEMENT.equals(documentElement.getNodeName())) {
            Log.w(LOGTAG, "missing or unknown root element");
            return null;
        }
        BroadcastElement broadcastElement = new BroadcastElement();
        broadcastElement.processElement(documentElement);
        if (broadcastElement.validState()) {
            return broadcastElement;
        }
        return null;
    }

    public static synchronized void setBroadcastInfo(BroadcastElement broadcastElement) {
        synchronized (XMLUtils.class) {
            sBroadcastInfo = broadcastElement;
            LinkedList<BroadcastInfoObserver> linkedList = new LinkedList();
            linkedList.addAll(sObservers);
            for (BroadcastInfoObserver broadcastInfoObserver : linkedList) {
                if (broadcastInfoObserver != null) {
                    broadcastInfoObserver.onNewInfo();
                }
            }
        }
    }

    public static synchronized BroadcastElement getBroadcastInfo() {
        BroadcastElement broadcastElement;
        synchronized (XMLUtils.class) {
            broadcastElement = sBroadcastInfo;
        }
        return broadcastElement;
    }

    public static synchronized void registerForNewInfoCallbacks(BroadcastInfoObserver broadcastInfoObserver) {
        synchronized (XMLUtils.class) {
            if (broadcastInfoObserver != null) {
                sObservers.add(broadcastInfoObserver);
            }
        }
    }

    public static synchronized void unregisterFromNewInfoCallbacks(BroadcastInfoObserver broadcastInfoObserver) {
        synchronized (XMLUtils.class) {
            sObservers.remove(broadcastInfoObserver);
        }
    }
}
