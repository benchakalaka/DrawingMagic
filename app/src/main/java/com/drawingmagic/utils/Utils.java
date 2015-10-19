package com.drawingmagic.utils;

import android.app.Dialog;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.Window;

import com.drawingmagic.R;
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

    /**
     * Configure and set custom action bar
     *
     * @param actionBar  action bar instance
     * @param customView view to be set as AB
     */
    public static void configureCustomActionBar(ActionBar actionBar, View customView) {
        actionBar.setDefaultDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(customView.getContext().getResources().getColor(R.color.default_ab_background_colour)));
        actionBar.setHomeButtonEnabled(false);
        //actionBar.setIcon(R.color.ab_background);
        if (Conditions.isNotNull(customView)) {
            actionBar.setCustomView(customView);
        }

    }

    public static String getRealPathFromURI(SuperActivity activity, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(activity, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(columnIndex);
        cursor.close();
        return result;
    }
}
