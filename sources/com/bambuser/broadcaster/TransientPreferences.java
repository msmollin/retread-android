package com.bambuser.broadcaster;

import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class TransientPreferences implements SharedPreferences {
    private final Map<String, Object> mMap = new HashMap();
    private final ArrayList<SharedPreferences.OnSharedPreferenceChangeListener> mListeners = new ArrayList<>();

    @Override // android.content.SharedPreferences
    public synchronized boolean contains(String str) {
        return this.mMap.containsKey(str);
    }

    @Override // android.content.SharedPreferences
    public synchronized Map<String, ?> getAll() {
        return this.mMap;
    }

    @Override // android.content.SharedPreferences
    public synchronized boolean getBoolean(String str, boolean z) {
        if (contains(str)) {
            return ((Boolean) this.mMap.get(str)).booleanValue();
        }
        return z;
    }

    @Override // android.content.SharedPreferences
    public synchronized float getFloat(String str, float f) {
        if (contains(str)) {
            return ((Float) this.mMap.get(str)).floatValue();
        }
        return f;
    }

    @Override // android.content.SharedPreferences
    public synchronized int getInt(String str, int i) {
        if (contains(str)) {
            return ((Integer) this.mMap.get(str)).intValue();
        }
        return i;
    }

    @Override // android.content.SharedPreferences
    public synchronized long getLong(String str, long j) {
        if (contains(str)) {
            return ((Long) this.mMap.get(str)).longValue();
        }
        return j;
    }

    @Override // android.content.SharedPreferences
    public synchronized String getString(String str, String str2) {
        if (contains(str)) {
            return (String) this.mMap.get(str);
        }
        return str2;
    }

    @Override // android.content.SharedPreferences
    public synchronized Set<String> getStringSet(String str, Set<String> set) {
        if (contains(str)) {
            return (Set) this.mMap.get(str);
        }
        return set;
    }

    @Override // android.content.SharedPreferences
    public SharedPreferences.Editor edit() {
        return new SharedPreferences.Editor() { // from class: com.bambuser.broadcaster.TransientPreferences.1
            private boolean mClear = false;
            private final Set<String> mRemoveKeys = new HashSet();
            private final Map<String, Object> mAddKeys = new HashMap();

            @Override // android.content.SharedPreferences.Editor
            public void apply() {
                List<SharedPreferences.OnSharedPreferenceChangeListener> list;
                HashSet<String> hashSet = new HashSet();
                synchronized (TransientPreferences.this) {
                    for (String str : this.mRemoveKeys) {
                        TransientPreferences.this.mMap.remove(str);
                    }
                    hashSet.addAll(this.mRemoveKeys);
                    if (this.mClear) {
                        hashSet.addAll(TransientPreferences.this.mMap.keySet());
                        TransientPreferences.this.mMap.clear();
                    }
                    TransientPreferences.this.mMap.putAll(this.mAddKeys);
                    hashSet.addAll(this.mAddKeys.keySet());
                    list = (List) TransientPreferences.this.mListeners.clone();
                }
                for (String str2 : hashSet) {
                    for (SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener : list) {
                        onSharedPreferenceChangeListener.onSharedPreferenceChanged(TransientPreferences.this, str2);
                    }
                }
            }

            @Override // android.content.SharedPreferences.Editor
            public SharedPreferences.Editor clear() {
                this.mClear = true;
                return this;
            }

            @Override // android.content.SharedPreferences.Editor
            public boolean commit() {
                apply();
                return true;
            }

            @Override // android.content.SharedPreferences.Editor
            public SharedPreferences.Editor putBoolean(String str, boolean z) {
                this.mAddKeys.put(str, Boolean.valueOf(z));
                return this;
            }

            @Override // android.content.SharedPreferences.Editor
            public SharedPreferences.Editor putFloat(String str, float f) {
                this.mAddKeys.put(str, Float.valueOf(f));
                return this;
            }

            @Override // android.content.SharedPreferences.Editor
            public SharedPreferences.Editor putInt(String str, int i) {
                this.mAddKeys.put(str, Integer.valueOf(i));
                return this;
            }

            @Override // android.content.SharedPreferences.Editor
            public SharedPreferences.Editor putLong(String str, long j) {
                this.mAddKeys.put(str, Long.valueOf(j));
                return this;
            }

            @Override // android.content.SharedPreferences.Editor
            public SharedPreferences.Editor putString(String str, String str2) {
                if (str2 != null) {
                    this.mAddKeys.put(str, str2);
                } else {
                    this.mRemoveKeys.add(str);
                }
                return this;
            }

            @Override // android.content.SharedPreferences.Editor
            public SharedPreferences.Editor putStringSet(String str, Set<String> set) {
                if (set != null) {
                    this.mAddKeys.put(str, set);
                } else {
                    this.mRemoveKeys.add(str);
                }
                return this;
            }

            @Override // android.content.SharedPreferences.Editor
            public SharedPreferences.Editor remove(String str) {
                this.mRemoveKeys.add(str);
                return this;
            }
        };
    }

    @Override // android.content.SharedPreferences
    public synchronized void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        this.mListeners.add(onSharedPreferenceChangeListener);
    }

    @Override // android.content.SharedPreferences
    public synchronized void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        this.mListeners.remove(onSharedPreferenceChangeListener);
    }
}
