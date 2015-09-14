package com.drawingmagic.utils;

import android.app.Dialog;
import android.view.Window;

import com.drawingmagic.SuperActivity;

/**
 * Project DrawingMagic.
 * Created by ihorkarpachev.
 * On 14/09/15 at 13:40.
 */
public class Utils {
    /**
     * Configure dialog and return it
     *
     * @param activity which trigger event
     * @return fully configured dialog
     */
    public static Dialog getConfiguredDialog(SuperActivity activity) {
        Dialog d = new Dialog(activity);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.setCanceledOnTouchOutside(true);
        return d;
    }
}
