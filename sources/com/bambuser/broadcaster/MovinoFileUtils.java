package com.bambuser.broadcaster;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/* loaded from: classes.dex */
final class MovinoFileUtils {
    private static final char[] HEXCHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final String INFO_SUFFIX = ".info";
    private static final String LOGTAG = "MovinoFileUtils";
    public static final long MINIMUM_FREE_BYTES = 5242880;
    private static final String MOVINOFILE_SUFFIX = ".movino";
    private static final String MOVINO_DIR = "complement";

    private MovinoFileUtils() {
    }

    public static MovinoData createMovinoData(Context context, String str, String str2, String str3) {
        MovinoData movinoData = new MovinoData(str);
        movinoData.setMovinoFile(createFile(context, str + MOVINOFILE_SUFFIX));
        movinoData.setInfoFile(createFile(context, str + INFO_SUFFIX));
        movinoData.setTitle(str2);
        movinoData.setUsername(str3);
        movinoData.setTimestamp(System.currentTimeMillis());
        movinoData.store();
        return movinoData;
    }

    private static File createFile(Context context, String str) {
        File[] externalFilesDirs;
        File internalMovinoDir = getInternalMovinoDir(context);
        long availableBytes = new StatFs(internalMovinoDir.getPath()).getAvailableBytes();
        long j = 0;
        File file = null;
        for (File file2 : getExternalFilesDirs(context)) {
            if (file2 != null && isExternalMounted(file2)) {
                try {
                    long availableBytes2 = new StatFs(file2.getPath()).getAvailableBytes();
                    if (availableBytes2 <= j || availableBytes2 <= MINIMUM_FREE_BYTES) {
                        availableBytes2 = j;
                    } else {
                        file = file2;
                    }
                    j = availableBytes2;
                } catch (Exception unused) {
                }
            }
        }
        if (file != null && j > availableBytes) {
            File file3 = new File(file, MOVINO_DIR);
            if (!file3.exists()) {
                file3.mkdirs();
            }
            if (file3.exists() && file3.canWrite()) {
                File file4 = new File(file3, str);
                if (file4.exists()) {
                    return file4;
                }
                try {
                    file4.createNewFile();
                    return file4;
                } catch (IOException e) {
                    Log.w(LOGTAG, "could not create new file on largest external storage: " + e);
                }
            }
        }
        if (availableBytes > MINIMUM_FREE_BYTES) {
            File file5 = new File(internalMovinoDir, str);
            if (file5.exists()) {
                return file5;
            }
            try {
                file5.createNewFile();
                return file5;
            } catch (IOException e2) {
                Log.w(LOGTAG, "could not create new file on internal storage: " + e2);
            }
        }
        return null;
    }

    public static MovinoData getMovinoData(Context context, String str) {
        return getMovinoData(context, str, getFile(context, str + MOVINOFILE_SUFFIX));
    }

    private static MovinoData getMovinoData(Context context, String str, File file) {
        MovinoData movinoData = new MovinoData(str);
        movinoData.setMovinoFile(file);
        movinoData.setInfoFile(getFile(context, str + INFO_SUFFIX));
        movinoData.load();
        return movinoData;
    }

    private static File getFile(Context context, String str) {
        File[] externalFilesDirs;
        File file = new File(getInternalMovinoDir(context), str);
        if (file.exists()) {
            return file;
        }
        for (File file2 : getExternalFilesDirs(context)) {
            if (file2 != null && isExternalMounted(file2)) {
                File file3 = new File(file2, MOVINO_DIR);
                if (file3.exists()) {
                    File file4 = new File(file3, str);
                    if (file4.exists()) {
                        return file4;
                    }
                } else {
                    continue;
                }
            }
        }
        return null;
    }

    private static File getInternalMovinoDir(Context context) {
        return context.getDir(MOVINO_DIR, 0);
    }

    private static boolean isExternalMounted(File file) {
        String str = "unknown";
        try {
            str = Build.VERSION.SDK_INT >= 21 ? (String) Environment.class.getMethod("getExternalStorageState", File.class).invoke(null, file) : Environment.getStorageState(file);
        } catch (Exception unused) {
        }
        return "mounted".equals(str);
    }

    private static List<File> getFileList(Context context, FilenameFilter filenameFilter) {
        File[] externalFilesDirs;
        File[] listFiles;
        LinkedList linkedList = new LinkedList();
        File[] listFiles2 = getInternalMovinoDir(context).listFiles(filenameFilter);
        if (listFiles2 != null) {
            Collections.addAll(linkedList, listFiles2);
        }
        for (File file : getExternalFilesDirs(context)) {
            if (file != null && isExternalMounted(file) && (listFiles = new File(file, MOVINO_DIR).listFiles(filenameFilter)) != null) {
                Collections.addAll(linkedList, listFiles);
            }
        }
        return linkedList;
    }

    private static File[] getExternalFilesDirs(Context context) {
        try {
            return context.getExternalFilesDirs(null);
        } catch (Exception unused) {
            return new File[0];
        }
    }

    public static int getMovinoFileCount(Context context) {
        return getFileList(context, new MovinoFileNameFilter()).size();
    }

    public static List<MovinoData> getMovinoDataList(Context context) {
        List<File> fileList = getFileList(context, new MovinoFileNameFilter());
        HashSet hashSet = new HashSet(fileList.size());
        ArrayList arrayList = new ArrayList(fileList.size());
        for (File file : fileList) {
            String basename = getBasename(file.getName());
            hashSet.add(basename);
            arrayList.add(getMovinoData(context, basename, file));
        }
        for (File file2 : getFileList(context, new InfoNameFilter())) {
            if (!hashSet.contains(getBasename(file2.getName()))) {
                file2.delete();
            }
        }
        return arrayList;
    }

    public static String getBasename(String str) {
        int lastIndexOf = str.lastIndexOf(46);
        return lastIndexOf > 0 ? str.substring(0, lastIndexOf) : str;
    }

    public static String getRandomName() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            sb.append(HEXCHARS[random.nextInt(HEXCHARS.length)]);
        }
        return sb.toString();
    }

    /* loaded from: classes.dex */
    private static final class MovinoFileNameFilter implements FilenameFilter {
        private MovinoFileNameFilter() {
        }

        @Override // java.io.FilenameFilter
        public boolean accept(File file, String str) {
            return str.endsWith(MovinoFileUtils.MOVINOFILE_SUFFIX);
        }
    }

    /* loaded from: classes.dex */
    private static final class InfoNameFilter implements FilenameFilter {
        private InfoNameFilter() {
        }

        @Override // java.io.FilenameFilter
        public boolean accept(File file, String str) {
            return str.endsWith(MovinoFileUtils.INFO_SUFFIX);
        }
    }
}
