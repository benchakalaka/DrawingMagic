/*
 * Created by Ihor Karpachev, Copyright (c) 2015. .
 */

package com.drawingmagic.utils;

import java.util.List;
import java.util.Set;

/**
 * Created by ihor.karpachev on 08/05/2015.
 * Project: Installer Application
 * Package: com.touchip.organizer.utils
 * Datascope Systems Ltd.
 */
public class Conditions {

    /**
     * Check list to null or empty value
     *
     * @param jArray
     * @return
     */
    public static boolean isNullOrEmpty(Set<?> jArray) {
        return (isNull(jArray)) || (jArray.isEmpty());
    }

    /**
     * Check list to null or empty value
     *
     * @param jArray
     * @return
     */
    public static boolean isNullOrEmpty(List<?> jArray) {
        return (isNull(jArray)) || (jArray.isEmpty());
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
