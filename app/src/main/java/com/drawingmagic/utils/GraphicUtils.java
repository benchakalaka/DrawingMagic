package com.drawingmagic.utils;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.provider.MediaStore;
import android.text.TextUtils;

import static android.graphics.Shader.TileMode.CLAMP;

/**
 * Created by ihor.karpachev on 05/10/2015.
 * Project: DrawingMagic
 * Package: com.drawingmagic.utils
 * Datascope Systems Ltd.
 */
public final class GraphicUtils {

    private GraphicUtils() {

    }

    // Flip type direction
    public static final int FLIP_VERTICAL = 1;
    public static final int FLIP_HORIZONTAL = 2;

    // Mirror type direction
    public static final int MIRROR_VERTICAL = 1;
    public static final int MIRROR_HORIZONTAL = 2;

    // Reflection gap between origin image and reflected image (gap space between original and reflected)
    private static final int REFLECTION_GAP = 4;

    // this will not scale but will flip on the Y axis
    private final static Matrix MATRIX = new Matrix();

    /**
     * Mirror reflection
     *
     * @param originalImage bitmap to be mirrored
     * @return mirrored bitmap
     */
    public static Bitmap applyReflection(Bitmap originalImage, int direction) {
        // get image size
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        int halfWidth = width >>> 1;
        int halfHeight = height >>> 1;

        MATRIX.reset();

        boolean isHorizontal = direction == MIRROR_HORIZONTAL;

        // this will not scale but will flip on the Y axis
        MATRIX.preScale(isHorizontal ? -1 : 1, isHorizontal ? 1 : -1);

        // create a Bitmap with the flip MATRIX applied to it.
        // we only want the bottom half of the image
        Bitmap reflectionImage = Bitmap.createBitmap(originalImage, isHorizontal ? halfWidth : 0, isHorizontal ? 0 : halfHeight, isHorizontal ? halfWidth : width, isHorizontal ? height : halfHeight, MATRIX, false);

        // create a new bitmap with same width but taller to fit reflection
        Bitmap bitmapWithReflection = Bitmap.createBitmap(isHorizontal ? (width + halfWidth) : width, isHorizontal ? height : (height + halfHeight), Bitmap.Config.ARGB_8888);

        // create a new Canvas with the bitmap that's big enough for
        // the image plus gap plus reflection
        Canvas canvas = new Canvas(bitmapWithReflection);
        // draw in the original image
        canvas.drawBitmap(originalImage, 0, 0, null);

        // draw in the gap
        Paint defaultPaint = new Paint();
        canvas.drawRect(isHorizontal ? width : 0, isHorizontal ? (height - REFLECTION_GAP) : height + REFLECTION_GAP, width, height, defaultPaint);
        // draw in the reflection
        canvas.drawBitmap(reflectionImage, isHorizontal ? width + REFLECTION_GAP : 0, isHorizontal ? 0 : height + REFLECTION_GAP, null);

        // create a shader that is a linear gradient that covers the reflection
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(isHorizontal ? bitmapWithReflection.getWidth() : 0, originalImage.getHeight(), 0, isHorizontal ? 0 : bitmapWithReflection.getHeight() + REFLECTION_GAP, 0x70ffffff, 0x00ffffff, CLAMP);
        // set the paint to use this shader (linear gradient)
        paint.setShader(shader);
        // set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        // draw a rectangle using the paint with our linear gradient
        canvas.drawRect(isHorizontal ? width + REFLECTION_GAP : 0, isHorizontal ? 0 : height, isHorizontal ? width + reflectionImage.getWidth() : width, isHorizontal ? height + reflectionImage.getHeight() : bitmapWithReflection.getHeight() + REFLECTION_GAP, paint);
        return bitmapWithReflection;
    }


    // // TODO: 23/10/2015 Delete comments when main method is ready
//    public static Bitmap applyReflectionWorkingCopy(Bitmap originalImage, int direction) {
//        // gap space between original and reflected
//        // get image size
//        int width = originalImage.getWidth();
//        int height = originalImage.getHeight();
//
//        MATRIX.reset();
//
//        boolean isHorizontal = direction == MIRROR_HORIZONTAL;
//
//        // this will not scale but will flip on the Y axis
//        MATRIX.preScale(isHorizontal ? -1 : 1, isHorizontal ? 1 : -1);
//
//        // create a Bitmap with the flip MATRIX applied to it.
//        // we only want the bottom half of the image
//        Bitmap reflectionImage = Bitmap.createBitmap(originalImage, isHorizontal ? width / 2 : 0, isHorizontal ? 0 : height / 2, isHorizontal ? width / 2 : width, isHorizontal ? height : height / 2, MATRIX, false);
//
//        // create a new bitmap with same width but taller to fit reflection
//        Bitmap bitmapWithReflection = Bitmap.createBitmap(isHorizontal ? (width + width / 2) : width, isHorizontal ? height : (height + height / 2), Bitmap.Config.ARGB_8888);
//
//
//        // create a new Canvas with the bitmap that's big enough for
//        // the image plus gap plus reflection
//        Canvas canvas = new Canvas(bitmapWithReflection);
//        // draw in the original image
//        canvas.drawBitmap(originalImage, 0, 0, null);
//
//        // draw in the gap
//        Paint defaultPaint = new Paint();
//        canvas.drawRect(isHorizontal ? width : 0, isHorizontal ? (height - REFLECTION_GAP) : height + REFLECTION_GAP, width, height, defaultPaint);
//        // draw in the reflection
//        canvas.drawBitmap(reflectionImage, isHorizontal ? width + REFLECTION_GAP : 0, isHorizontal ? 0 : height + REFLECTION_GAP, null);
//
//        // create a shader that is a linear gradient that covers the reflection
//        Paint paint = new Paint();
//        LinearGradient shader = new LinearGradient(isHorizontal ? bitmapWithReflection.getWidth() : 0, originalImage.getHeight(), 0, isHorizontal ? 0 : bitmapWithReflection.getHeight() + REFLECTION_GAP, 0x70ffffff, 0x00ffffff, CLAMP);
//        //LinearGradient shader = new LinearGradient(                                                0, originalImage.getHeight(), 0,                   bitmapWithReflection.getHeight() + REFLECTION_GAP, 0x70ffffff, 0x00ffffff, CLAMP);
//        // set the paint to use this shader (linear gradient)
//        paint.setShader(shader);
//        // set the Transfer mode to be porter duff and destination in
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
//        // draw a rectangle using the paint with our linear gradient
//        canvas.drawRect(isHorizontal ? width + REFLECTION_GAP : 0, isHorizontal ? 0 : height, isHorizontal ? width + reflectionImage.getWidth() : width, isHorizontal ? height + reflectionImage.getHeight() : bitmapWithReflection.getHeight() + REFLECTION_GAP, paint);
//        //canvas.drawRect(                                       0,                    height,                                                     width, bitmapWithReflection.getHeight() + REFLECTION_GAP, paint);
//        return bitmapWithReflection;
//    }

    /**
     * Flip Bitmap over X or Y Axis, depends on direction
     *
     * @param src  source bitmap
     * @param type flip direction Horizontal and Vertical
     * @return
     */
    public static Bitmap flip(Bitmap src, int type) {
        // create new MATRIX for transformation
        Matrix matrix = new Matrix();
        // if vertical
        if (type == FLIP_VERTICAL) {
            // y = y * -1
            matrix.preScale(1.0f, -1.0f);
        }
        // if horizontal
        else if (type == FLIP_HORIZONTAL) {
            // x = x * -1
            matrix.preScale(-1.0f, 1.0f);
            // unknown type
        } else {
            return null;
        }

        // return transformed image
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }


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

        return circleBitmap;
    }


    public static Bitmap decodeSampledBitmapFromResource(String filename, int reqWidth, int reqHeight) {

        Logger.e("W: " + reqWidth + " , H:" + reqHeight);

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filename, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        Logger.e("inSampleSize =  " + options.inSampleSize);

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
