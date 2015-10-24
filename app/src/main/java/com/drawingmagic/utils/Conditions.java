/*
 * Created by Ihor Karpachev, Copyright (c) 2015. .
 */

package com.drawingmagic.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ihor.karpachev on 08/05/2015.
 * Project: Installer Application
 * Package: com.touchip.organizer.utils
 * Datascope Systems Ltd.
 */
public final class Conditions {

    private Conditions() {
    }

    /**
     * Check list to null or empty value
     *
     * @param jArray array to be checked
     * @return true if array is null or empty, false otherwise
     */
    public static boolean isNullOrEmpty(Set<?> jArray) {
        return (isNull(jArray)) || (jArray.isEmpty());
    }

    /**
     * Check map to null or empty value
     *
     * @param map list to be checked
     * @return true if map is null or empty, false otherwise
     */
    public static boolean isNullOrEmpty(Map<?, ?> map) {
        return (isNull(map)) || (map.isEmpty());
    }

    /**
     * Check list to null or empty value
     *
     * @param list list to be checked
     * @return true if list is null or empty, false otherwise
     */
    public static boolean isNullOrEmpty(List<?> list) {
        return (isNull(list)) || (list.isEmpty());
    }

    /**
     * Check string array to null or empty value
     *
     * @param stringArray string array to check
     * @return true if array is empty or null, false otherwise
     */
    public static boolean isNullOrEmpty(String[] stringArray) {
        return (isNull(stringArray)) || (stringArray.length == 0);
    }

    /**
     * Check if object is null
     *
     * @param obj object to check
     * @return true if object is null, otherwise return false
     */
    public static boolean isNull(Object obj) {
        return (null == obj);
    }

    /**
     * Check if object is NOT null
     *
     * @param obj object to check
     * @return true if object is null, otherwise return false
     */
    public static boolean isNotNull(Object obj) {
        return (null != obj);
    }
}
