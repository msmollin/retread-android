package com.facebook.soloader;

import android.os.StrictMode;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import javax.annotation.Nullable;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* loaded from: classes.dex */
public class DirectorySoSource extends SoSource {
    public static final int ON_LD_LIBRARY_PATH = 2;
    public static final int RESOLVE_DEPENDENCIES = 1;
    protected final int flags;
    protected final File soDirectory;

    public DirectorySoSource(File file, int i) {
        this.soDirectory = file;
        this.flags = i;
    }

    @Override // com.facebook.soloader.SoSource
    public int loadLibrary(String str, int i, StrictMode.ThreadPolicy threadPolicy) throws IOException {
        return loadLibraryFrom(str, i, this.soDirectory, threadPolicy);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int loadLibraryFrom(String str, int i, File file, StrictMode.ThreadPolicy threadPolicy) throws IOException {
        File file2 = new File(file, str);
        if (!file2.exists()) {
            Log.d("SoLoader", str + " not found on " + file.getCanonicalPath());
            return 0;
        }
        Log.d("SoLoader", str + " found on " + file.getCanonicalPath());
        if ((i & 1) != 0 && (this.flags & 2) != 0) {
            Log.d("SoLoader", str + " loaded implicitly");
            return 2;
        }
        if ((this.flags & 1) != 0) {
            loadDependencies(file2, i, threadPolicy);
        } else {
            Log.d("SoLoader", "Not resolving dependencies for " + str);
        }
        try {
            SoLoader.sSoFileLoader.load(file2.getAbsolutePath(), i);
            return 1;
        } catch (UnsatisfiedLinkError e) {
            if (e.getMessage().contains("bad ELF magic")) {
                Log.d("SoLoader", "Corrupted lib file detected");
                return 3;
            }
            throw e;
        }
    }

    private void loadDependencies(File file, int i, StrictMode.ThreadPolicy threadPolicy) throws IOException {
        String[] dependencies = getDependencies(file);
        Log.d("SoLoader", "Loading lib dependencies: " + Arrays.toString(dependencies));
        for (String str : dependencies) {
            if (!str.startsWith(MqttTopic.TOPIC_LEVEL_SEPARATOR)) {
                SoLoader.loadLibraryBySoName(str, i | 1, threadPolicy);
            }
        }
    }

    private static String[] getDependencies(File file) throws IOException {
        if (SoLoader.SYSTRACE_LIBRARY_LOADING) {
            Api18TraceUtils.beginTraceSection("SoLoader.getElfDependencies[" + file.getName() + "]");
        }
        try {
            return MinElf.extract_DT_NEEDED(file);
        } finally {
            if (SoLoader.SYSTRACE_LIBRARY_LOADING) {
                Api18TraceUtils.endSection();
            }
        }
    }

    @Override // com.facebook.soloader.SoSource
    @Nullable
    public File unpackLibrary(String str) throws IOException {
        File file = new File(this.soDirectory, str);
        if (file.exists()) {
            return file;
        }
        return null;
    }

    @Override // com.facebook.soloader.SoSource
    public void addToLdLibraryPath(Collection<String> collection) {
        collection.add(this.soDirectory.getAbsolutePath());
    }

    @Override // com.facebook.soloader.SoSource
    public String toString() {
        String name;
        try {
            name = String.valueOf(this.soDirectory.getCanonicalPath());
        } catch (IOException unused) {
            name = this.soDirectory.getName();
        }
        return getClass().getName() + "[root = " + name + " flags = " + this.flags + ']';
    }
}
