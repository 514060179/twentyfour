package com.yinghai.twentyfour.common.util;

import java.util.*;

public class MapUtil {

    /**
     * map过滤
     * */
    public static Map<String, Object> filterMapByKeyValue(Map<String, Object> map, String filterKey, String filterValue) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            Object val = entry.getValue();
            if (!filterKey.equals(key)) {
                if (!filterValue.equals(val)) {
                    resultMap.put(key, val);
                }
            }
        }
        return resultMap;
    }

    /**Map排序，按照Key排序*/
    public static Map<String, Object> sortMapByKey(Map<String, Object> map) {
        if (map == null || map.isEmpty()) { return null; }
        Map<String, Object> sortMap = new TreeMap<String, Object>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }
    static class MapKeyComparator implements Comparator<String>{

        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }
}