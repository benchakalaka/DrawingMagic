package com.drawingmagic.utils;

import android.app.Dialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.Window;

import com.drawingmagic.R;
import com.drawingmagic.SuperActivity;

import static android.content.Intent.ACTION_SEND;
import static android.content.Intent.EXTRA_STREAM;
import static android.content.Intent.EXTRA_SUBJECT;
import static android.content.Intent.EXTRA_TEXT;
import static android.content.Intent.createChooser;

/**
 * Project DrawingMagic.
 * Created by ihorkarpachev.
 * On 14/09/15 at 13:40.
 */
public final class Utils {

    private Utils() {

    }

    /**
     * Share image via existing social networks or email
     *
     * @param activityHost host activity
     * @param uriPath      path to image
     */
    public static void shareImage(SuperActivity activityHost, String uriPath) {
        Intent sharingIntent = new Intent(ACTION_SEND);
        sharingIntent.setType("application/octet-stream");
        sharingIntent.setType("image/png");
        sharingIntent.putExtra(EXTRA_SUBJECT, "Drawing App");
        sharingIntent.putExtra(EXTRA_TEXT, "This is the text that will be shared.").putExtra(EXTRA_STREAM, Uri.parse(uriPath));
        activityHost.startActivity(createChooser(sharingIntent, "Share image using"));
    }

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
