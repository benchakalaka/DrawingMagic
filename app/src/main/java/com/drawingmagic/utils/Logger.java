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
public final class Logger {

    private Logger() {

    }

    /**
     * Log with DEBUG level
     *
     * @param ex exception to log
     */
    public static void e(Exception ex) {
        Trail.debug(ex);
    }

    /**
     * Print map to console
     *
     * @param mp map to be printed
     */
    public static void print(Map<?, ?> mp) {
        if (null != mp) {
            for (Object key : mp.keySet()) {
                try {
                    e("( " + key + " => " + mp.get(key) + " )");
                } catch (Exception ex) {
                    e(ex);
                }
            }
        }
    }

    /**
     * Log with ERROR level
     *
     * @param message message to log
     */
    public static void e(String message) {
        Trail.error("......................................................" + message);
    }
}
