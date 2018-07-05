package edu.hcmut.thesis;

import java.util.Map;

public class CommonUtils {
    public static <K, V, O extends V> V getOrCreate(K key, Map<K, V> map, Class<O> c) {
        V result = map.get(key);
        if (result == null) {
            synchronized (map) {
                result = map.get(key);
                if (result == null) {
                    try {
                        result = c.newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException("Error in getAndCreateIfNotExist", e);
                    }

                    map.put(key, result);
                }
            }
        }
        return result;
    }
}
