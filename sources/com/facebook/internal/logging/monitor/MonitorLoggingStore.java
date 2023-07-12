package com.facebook.internal.logging.monitor;

import android.content.Context;
import androidx.annotation.RestrictTo;
import com.facebook.FacebookSdk;
import com.facebook.internal.Utility;
import com.facebook.internal.logging.ExternalLog;
import com.facebook.internal.logging.LoggingStore;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* loaded from: classes.dex */
public class MonitorLoggingStore implements LoggingStore {
    public static final String PERSISTED_LOGS_FILENAME = "facebooksdk.monitoring.persistedlogs";
    private static MonitorLoggingStore monitorLoggingStore;

    private MonitorLoggingStore() {
    }

    public static synchronized MonitorLoggingStore getInstance() {
        MonitorLoggingStore monitorLoggingStore2;
        synchronized (MonitorLoggingStore.class) {
            if (monitorLoggingStore == null) {
                monitorLoggingStore = new MonitorLoggingStore();
            }
            monitorLoggingStore2 = monitorLoggingStore;
        }
        return monitorLoggingStore2;
    }

    @Override // com.facebook.internal.logging.LoggingStore
    public Collection<ExternalLog> readAndClearStore() {
        ArrayList arrayList = new ArrayList();
        Context applicationContext = FacebookSdk.getApplicationContext();
        ObjectInputStream objectInputStream = null;
        try {
            ObjectInputStream objectInputStream2 = new ObjectInputStream(new BufferedInputStream(applicationContext.openFileInput(PERSISTED_LOGS_FILENAME)));
            try {
                Collection<ExternalLog> collection = (Collection) objectInputStream2.readObject();
                Utility.closeQuietly(objectInputStream2);
                try {
                    applicationContext.getFileStreamPath(PERSISTED_LOGS_FILENAME).delete();
                } catch (Exception unused) {
                }
                return collection;
            } catch (Exception unused2) {
                objectInputStream = objectInputStream2;
                Utility.closeQuietly(objectInputStream);
                try {
                    applicationContext.getFileStreamPath(PERSISTED_LOGS_FILENAME).delete();
                    return arrayList;
                } catch (Exception unused3) {
                    return arrayList;
                }
            } catch (Throwable th) {
                th = th;
                objectInputStream = objectInputStream2;
                Utility.closeQuietly(objectInputStream);
                try {
                    applicationContext.getFileStreamPath(PERSISTED_LOGS_FILENAME).delete();
                } catch (Exception unused4) {
                }
                throw th;
            }
        } catch (Exception unused5) {
        } catch (Throwable th2) {
            th = th2;
        }
    }

    @Override // com.facebook.internal.logging.LoggingStore
    public void saveLogsToDisk(Collection<ExternalLog> collection) {
        Context applicationContext = FacebookSdk.getApplicationContext();
        ObjectOutputStream objectOutputStream = null;
        try {
            try {
                ObjectOutputStream objectOutputStream2 = new ObjectOutputStream(new BufferedOutputStream(applicationContext.openFileOutput(PERSISTED_LOGS_FILENAME, 0)));
                try {
                    objectOutputStream2.writeObject(collection);
                    Utility.closeQuietly(objectOutputStream2);
                } catch (Exception unused) {
                    objectOutputStream = objectOutputStream2;
                    try {
                        applicationContext.getFileStreamPath(PERSISTED_LOGS_FILENAME).delete();
                    } catch (Exception unused2) {
                        Utility.closeQuietly(objectOutputStream);
                    }
                } catch (Throwable th) {
                    th = th;
                    objectOutputStream = objectOutputStream2;
                    Utility.closeQuietly(objectOutputStream);
                    throw th;
                }
            } catch (Exception unused3) {
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }
}
