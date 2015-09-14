/*
 * Created by Ihor Karpachev, Copyright (c) 2015. .
 */

package com.drawingmagic.utils;

import com.mauriciotogneri.trail.Trail;

import java.util.Map;

/**
 * Created by ihor.karpachev on 07/05/2015.
 * Project: Installer Application
 * Package: com.touchip.organizer.utils
 * Datascope Systems Ltd.
 */
public class Log {

    /**
     * Log with DEBUG level
     *
     * @param ex exception to log
     */
    public static void e(Exception ex) {
        // providing only an exception
        Trail.debug(ex);
    }

    /**
     * Print map to console
     *
     * @param mp map to be printed
     */
    @SuppressWarnings("rawtypes")
    public static void print(Map mp) {
        if (null != mp) {
            for (Object key : mp.keySet()) {
                try {
                    e("( " + key + " => " + mp.get(key) + " )");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Log with INFO level
     *
     * @param message message to log
     */
    public static void i(String message) {
        Trail.info("......................................................" + message);
    }

    /**
     * Log with ERROR level
     *
     * @param message message to log
     */
    public static void e(String message) {
        Trail.error("......................................................" + message);
    }

    /**
     * Log with INFO level
     *
     * @param message message to log
     */
    public static void i(int message) {
        Trail.info("......................................................" + message);
    }
}
