/*
 * Created by Ihor Karpachev, Copyright (c) 2015. .
 */

package com.drawingmagic.utils;

import android.app.Activity;

import com.github.pierry.simpletoast.SimpleToast;


/**
 * Display short notification, similar to toast
 * Created by ihor.karpachev on 28/05/2015.
 * Project: QUILT
 * Package: com.touchip.organizer.utils
 * Datascope Systems Ltd.
 */
public final class Notification {

    private Notification() {

    }

    /**
     * Show message with specific FontAwesomeImage
     *
     * @param activity       hosted activity
     * @param message        message to be displayed
     * @param fatImageString e.g. {fa-home}, {fa-close}
     */
    public static void showWithFontAwesomeImage(Activity activity, String message, String fatImageString, NotificationType type) {
        switch (type) {

            case SUCCESS:
                SimpleToast.ok(activity, message, fatImageString);
                break;

            case ERROR:
                SimpleToast.error(activity, message, fatImageString);
                break;

            case MUTED:
                SimpleToast.muted(activity, message, fatImageString);
                break;

            case WARNING:
                SimpleToast.warning(activity, message, fatImageString);
                break;

            case INFO:
                SimpleToast.info(activity, message, fatImageString);
                break;
            default:
                Logger.e(String.format("Unknown type of toast %s", type));
                break;
        }

    }

    /**
     * Show warning message
     *
     * @param activity context of activity
     * @param message  message to be displayed
     */
    public static void showMuted(Activity activity, String message) {
        SimpleToast.muted(activity, message);
    }

    /**
     * Show warning message
     *
     * @param activity context of activity
     * @param message  message id to be displayed
     */
    public static void showMuted(Activity activity, int message) {
        SimpleToast.muted(activity, activity.getResources().getString(message));
    }

    /**
     * Show warning message
     *
     * @param activity context of activity
     * @param message  message to be displayed
     */
    public static void showWarning(Activity activity, String message) {
        SimpleToast.warning(activity, message);
    }

    /**
     * Show warning message
     *
     * @param activity context of activity
     * @param message  message id to be displayed
     */
    public static void showWarning(Activity activity, int message) {
        SimpleToast.warning(activity, activity.getResources().getString(message));
    }

    /**
     * Show alert message
     *
     * @param activity context of activity
     * @param message  message to be displayed
     */
    public static void showError(Activity activity, String message) {
        SimpleToast.error(activity, message);
    }

    /**
     * Show alert message
     *
     * @param activity context of activity
     * @param message  message id to be displayed
     */
    public static void showError(Activity activity, int message) {
        SimpleToast.error(activity, activity.getResources().getString(message));
    }

    /**
     * Show success message
     *
     * @param activity context of activity
     * @param message  message to be displayed
     */
    public static void showSuccess(Activity activity, String message) {
        SimpleToast.ok(activity, message);
    }

    /**
     * Show success message
     *
     * @param activity context of activity
     * @param message  message id to be displayed
     */
    public static void showSuccess(Activity activity, int message) {
        SimpleToast.ok(activity, activity.getResources().getString(message));
    }

    /**
     * Show info message
     *
     * @param activity context of activity
     * @param message  message to be displayed
     */
    public static void showInfo(Activity activity, String message) {
        SimpleToast.info(activity, message);
    }

    /**
     * Show info message
     *
     * @param activity context of activity
     * @param message  message id to be displayed
     */
    public static void showInfo(Activity activity, int message) {
        SimpleToast.info(activity, activity.getResources().getString(message));
    }

    /**
     * Type of message to be displayed
     */
    public enum NotificationType {
        SUCCESS, ERROR, MUTED, WARNING, INFO
    }
}
