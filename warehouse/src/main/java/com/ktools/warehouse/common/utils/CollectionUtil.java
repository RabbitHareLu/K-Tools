package com.ktools.warehouse.common.utils;

import java.util.Collection;
import java.util.Map;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年08月24日 17:43
 */
public class CollectionUtil {

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }
}
