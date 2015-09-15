package com.drawingmagic.utils;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
}
