package com.drawingmagic.utils;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.provider.MediaStore;
import android.text.TextUtils;

/**
 * Created by ihor.karpachev on 05/10/2015.
 * Project: DrawingMagic
 * Package: com.drawingmagic.utils
 * Datascope Systems Ltd.
 */
public class GraphicUtils {


    /**
     * Save image to gallery
     *
     * @param contentResolver activity's content resolver
     * @param picture         picture to be saved, if null has given as a param, runtime exception will be thrown
     * @param title           picture title
     * @param description     picture description
     * @return string representation of path where image has been saved
     */
    public static String saveImageToGallery(ContentResolver contentResolver, Bitmap picture, String title, String description) {
        if (Conditions.isNull(picture)) {
            throw new RuntimeException("Picture is NULL");
        }
        return MediaStore.Images.Media.insertImage(contentResolver, picture, TextUtils.isEmpty(title) ? "" : title, TextUtils.isEmpty(description) ? "" : description);
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


    public static Bitmap decodeSampledBitmapFromResource(String filename, int reqWidth, int reqHeight) {

        Log.e("W: " + reqWidth + " , H:" + reqHeight);

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filename, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        Log.e("inSampleSize =  " + options.inSampleSize);

        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = true;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filename, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
