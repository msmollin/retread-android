package org.joda.time.tz;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.joda.time.DateTimeUtils;

/* loaded from: classes2.dex */
public class DefaultNameProvider implements NameProvider {
    private HashMap<Locale, Map<String, Map<String, Object>>> iByLocaleCache = createCache();
    private HashMap<Locale, Map<String, Map<Boolean, Object>>> iByLocaleCache2 = createCache();

    @Override // org.joda.time.tz.NameProvider
    public String getShortName(Locale locale, String str, String str2) {
        String[] nameSet = getNameSet(locale, str, str2);
        if (nameSet == null) {
            return null;
        }
        return nameSet[0];
    }

    @Override // org.joda.time.tz.NameProvider
    public String getName(Locale locale, String str, String str2) {
        String[] nameSet = getNameSet(locale, str, str2);
        if (nameSet == null) {
            return null;
        }
        return nameSet[1];
    }

    private synchronized String[] getNameSet(Locale locale, String str, String str2) {
        Object[] objArr;
        Object[] objArr2 = null;
        if (locale == null || str == null || str2 == null) {
            return null;
        }
        Map<String, Map<String, Object>> map = this.iByLocaleCache.get(locale);
        if (map == null) {
            HashMap<Locale, Map<String, Map<String, Object>>> hashMap = this.iByLocaleCache;
            HashMap createCache = createCache();
            hashMap.put(locale, createCache);
            map = createCache;
        }
        Map<String, Object> map2 = map.get(str);
        if (map2 == null) {
            map2 = createCache();
            map.put(str, map2);
            Object[][] zoneStrings = DateTimeUtils.getDateFormatSymbols(Locale.ENGLISH).getZoneStrings();
            int length = zoneStrings.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    objArr = null;
                    break;
                }
                objArr = zoneStrings[i];
                if (objArr != null && objArr.length >= 5 && str.equals(objArr[0])) {
                    break;
                }
                i++;
            }
            Object[][] zoneStrings2 = DateTimeUtils.getDateFormatSymbols(locale).getZoneStrings();
            int length2 = zoneStrings2.length;
            int i2 = 0;
            while (true) {
                if (i2 < length2) {
                    Object[] objArr3 = zoneStrings2[i2];
                    if (objArr3 != null && objArr3.length >= 5 && str.equals(objArr3[0])) {
                        objArr2 = objArr3;
                        break;
                    }
                    i2++;
                } else {
                    break;
                }
            }
            if (objArr != null && objArr2 != null) {
                map2.put(objArr[2], new String[]{objArr2[2], objArr2[1]});
                if (objArr[2].equals(objArr[4])) {
                    map2.put(objArr[4] + "-Summer", new String[]{objArr2[4], objArr2[3]});
                } else {
                    map2.put(objArr[4], new String[]{objArr2[4], objArr2[3]});
                }
            }
        }
        return (String[]) map2.get(str2);
    }

    public String getShortName(Locale locale, String str, String str2, boolean z) {
        String[] nameSet = getNameSet(locale, str, str2, z);
        if (nameSet == null) {
            return null;
        }
        return nameSet[0];
    }

    public String getName(Locale locale, String str, String str2, boolean z) {
        String[] nameSet = getNameSet(locale, str, str2, z);
        if (nameSet == null) {
            return null;
        }
        return nameSet[1];
    }

    private synchronized String[] getNameSet(Locale locale, String str, String str2, boolean z) {
        String[] strArr;
        String[] strArr2 = null;
        if (locale == null || str == null || str2 == null) {
            return null;
        }
        if (str.startsWith("Etc/")) {
            str = str.substring(4);
        }
        Map<String, Map<Boolean, Object>> map = this.iByLocaleCache2.get(locale);
        if (map == null) {
            HashMap<Locale, Map<String, Map<Boolean, Object>>> hashMap = this.iByLocaleCache2;
            HashMap createCache = createCache();
            hashMap.put(locale, createCache);
            map = createCache;
        }
        Map<Boolean, Object> map2 = map.get(str);
        if (map2 == null) {
            map2 = createCache();
            map.put(str, map2);
            String[][] zoneStrings = DateTimeUtils.getDateFormatSymbols(Locale.ENGLISH).getZoneStrings();
            int length = zoneStrings.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    strArr = null;
                    break;
                }
                strArr = zoneStrings[i];
                if (strArr != null && strArr.length >= 5 && str.equals(strArr[0])) {
                    break;
                }
                i++;
            }
            String[][] zoneStrings2 = DateTimeUtils.getDateFormatSymbols(locale).getZoneStrings();
            int length2 = zoneStrings2.length;
            int i2 = 0;
            while (true) {
                if (i2 < length2) {
                    String[] strArr3 = zoneStrings2[i2];
                    if (strArr3 != null && strArr3.length >= 5 && str.equals(strArr3[0])) {
                        strArr2 = strArr3;
                        break;
                    }
                    i2++;
                } else {
                    break;
                }
            }
            if (strArr != null && strArr2 != null) {
                map2.put(Boolean.TRUE, new String[]{strArr2[2], strArr2[1]});
                map2.put(Boolean.FALSE, new String[]{strArr2[4], strArr2[3]});
            }
        }
        return (String[]) map2.get(Boolean.valueOf(z));
    }

    private HashMap createCache() {
        return new HashMap(7);
    }
}
