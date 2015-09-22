package com.drawingmagic.utils;

import android.app.ActionBar;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
     * @param actionBar
     * @param customView
     */
    public static void configureCustomActionBar(ActionBar actionBar, View customView) {
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        //actionBar.setIcon(R.color.ab_background);
        if (null != customView) {
            actionBar.setCustomView(customView);
        }
    }

    // TODO: 17/09/2015 Replace with circle image view library

    /**
     * Create round bitmap
     *
     * @param colour
     * @param bitmapW
     * @param bitmapH
     * @return
     */
    public static Bitmap createRoundImage(String colour, int bitmapW, int bitmapH) {
        return createRoundImage(Color.parseColor(colour), bitmapW, bitmapH);
    }

    public static Bitmap createRoundImage(int colour, int bitmapW, int bitmapH) {
        Bitmap circleBitmap = Bitmap.createBitmap(bitmapW, bitmapH, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(circleBitmap);

        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setStrokeWidth(5);
        p.setColor(colour);
        c.drawCircle(circleBitmap.getWidth() - bitmapW / 2, circleBitmap.getHeight() - bitmapH / 2, bitmapW / 2, p);
        return circleBitmap;
    }

    public static Bitmap createRoundImageSelected(int colour, int selectionColour, int bitmapW, int bitmapH) {
        Bitmap circleBitmap = Bitmap.createBitmap(bitmapW, bitmapH, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(circleBitmap);

        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setStrokeWidth(5);
        p.setColor(colour);
        c.drawCircle(circleBitmap.getWidth() - bitmapW / 2, circleBitmap.getHeight() - bitmapH / 2, bitmapW / 2, p);

        p.setColor(selectionColour);
        c.drawCircle(circleBitmap.getWidth() - bitmapW / 2, circleBitmap.getHeight() - bitmapH / 2, bitmapW / 2 - 5, p);

        //p.setColor(colour);
        //c.drawCircle(circleBitmap.getWidth() - bitmapW / 2, circleBitmap.getHeight() - bitmapH / 2, bitmapW / 2 - 10, p);
        return circleBitmap;
    }
}
