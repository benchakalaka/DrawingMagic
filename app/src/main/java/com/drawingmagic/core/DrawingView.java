package com.drawingmagic.core;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.drawingmagic.SuperActivity;
import com.drawingmagic.dialogs.DialogCanvasSettings;
import com.drawingmagic.utils.Conditions;
import com.drawingmagic.utils.Log;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageView;


/**
 * Project DrawingMagic.
 * Created by ihorkarpachev.
 * On 14/09/15 at 13:19.
 */
public class DrawingView extends View {
    // step for drawing coordinates (100 pixels)
    private static final float STEP = 100;
    // radius of round corner of rectangles
    private static final float RECTANGLE_RADIUS = 5;
    // single grid line length
    private static final int PARTLY_GRID_LINE_LENGTH = 20;
    // Shift from finger while drawing
    private static final int TEXT_SHIFT = 20;
    // Canvas BITMAP paint
    private static Paint PAINT_BITMAP = new Paint();
    // default paint for grid drawing
    private final TextPaint labelsPaint = new TextPaint();
    // paint for drawing text
    private final TextPaint textPaint = new TextPaint();
    // default paint for grid drawing
    private final Paint coordinatesPaint = new Paint();
    // rectangle of drawing for different types of shapes
    private final RectF rectangleOfDrawing = new RectF();
    // drawing
    private final Matrix m = new Matrix();
    private final Paint paint = new Paint();
    // Dashed effect for all shapes
    DashPathEffect dashedEffect = new DashPathEffect(new float[]{15, 15, 15, 15}, 0);
    long lastDoubleTouchTime = 0;
    long ONE_SECOND_IN_MILLISECONDS = 300;
    // zooming
    float zoom = 1.0f;
    float smoothZoom = 1.0f;
    float zoomX, zoomY;
    float smoothZoomX, smoothZoomY;
    // listener
    OnZoomCanvasCallback zoomListener;
    // current paint which contains user's brush settings
    private PaintSerializable currentPaint = new PaintSerializable();
    // current X,Y position lives in touchX/Y, start position is in touchDownX/Y
    private float touchX, touchY, touchDownX, touchDownY;
    // settings object
    private DrawingData drawingData = new DrawingData();
    // is user's finger touching canvas
    private boolean isFingerTouchingCanvas = false;
    // formatting int to two digits after dot
    private NumberFormat formatter = new DecimalFormat("#0.00");
    // Current path drawing contains shape drawing by user
    private PathSerializable currentPath = new PathSerializable();
    // list of paths for undo/redo operations
    private List<PathSerializable> redoPaths = new ArrayList<>();
    // path for any shape
    private PathSerializable shapePath = new PathSerializable();
    // Activity/Dialog/Fragment which will be notified in case of event arise
    private OnTouchCanvasCallback listener;
    // Rectangle for source image
    private Rect SOURCE_IMAGE_RECTANGLE;
    private Rect DESTINATION_IMAGE_RECT;
    private boolean erase = false;
    private Rect rect = new Rect();
    // Scaling objects
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.0f;
    private float scalePointX, scalePointY;
    /**
     * Maximum Zoom value
     */
    private float maxZoom = 5.0f;
    // minimap variables
    private boolean showMinimap = false;
    private int miniMapColor = Color.BLACK;
    private int miniMapHeight = -1;
    private String miniMapCaption;
    private float miniMapCaptionSize = 10.0f;
    private int miniMapCaptionColor = Color.WHITE;
    // touching variables
    private float startd;
    private boolean pinching;
    private float lastd;
    private float lastdx1, lastdy1;
    private float lastdx2, lastdy2;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDrawingCacheEnabled(true);
        setDrawingCacheQuality(DRAWING_CACHE_QUALITY_HIGH);
// init user's paint settings
        currentPaint.setStyle(Paint.Style.STROKE);
        currentPaint.setStrokeJoin(Paint.Join.ROUND);
        currentPaint.setStrokeCap(Paint.Cap.ROUND);
// init text paint
        labelsPaint.setFakeBoldText(true);
        labelsPaint.setTypeface(Typeface.SANS_SERIF);

        textPaint.setFakeBoldText(true);
        textPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        textPaint.setTextSize(30);
        textPaint.setUnderlineText(true);

// init Bitmap paint
        PAINT_BITMAP.setAntiAlias(true);
        PAINT_BITMAP.setFilterBitmap(true);
        PAINT_BITMAP.setDither(true);

        mScaleDetector = new ScaleGestureDetector(this.getContext(), new ScaleListener());
    }

    /**
     * Convert position of canvas to letter using next map:
     * 1 -> A
     * 2 -> B
     * ...
     * 13 -> N
     * default: Z
     *
     * @param position int representation of position to be converted
     * @return string representation of letter
     */
    public static String convertPositionToLetter(int position) {
        switch (position) {
            case 0:
                return "A";
            case 1:
                return "B";
            case 2:
                return "C";
            case 3:
                return "D";
            case 4:
                return "E";
            case 5:
                return "F";
            case 6:
                return "G";
            case 7:
                return "H";
            case 8:
                return "I";
            case 9:
                return "J";
            case 10:
                return "K";
            case 11:
                return "L";
            case 12:
                return "M";
            case 13:
                return "N";
            case 14:
                return "O";
            case 15:
                return "P";
// ...
            default:
                return "Z";
        }
    }

    /**
     * Setter for eraser
     *
     * @param isErase enable eraser or not
     */
    public void setErase(boolean isErase) {
        this.erase = isErase;
        if (erase) {
            currentPaint.setColor(Color.WHITE);
// currentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        } else {
            currentPaint.setXfermode(null);
        }
    }

    /**
     * onDraw will be called after any touch event or invalidating drawing surface
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

// 1) draw simple bitmap
        canvas.save();
        canvas.scale(mScaleFactor, mScaleFactor, scalePointX, scalePointY);
        canvas.getClipBounds(rect);
        canvas.drawBitmap(drawingData.getCanvasBitmap(), SOURCE_IMAGE_RECTANGLE, DESTINATION_IMAGE_RECT, PAINT_BITMAP);

// 2) draw all previously stored paths
        for (PathSerializable path : drawingData.getPaths()) {
// if current path is not a text draw simple path
            if (TextUtils.isEmpty(path.getTextToDraw())) {
                PaintSerializable paint = path.getPaint().from(path.getPaint());
                if (paint.isDashed) {
                    paint.setPathEffect(dashedEffect);
                } else {
                    paint.setPathEffect(null);
                }
                canvas.drawPath(path, paint);
            } else {
// this is text, draw it
                canvas.drawText(path.getTextToDraw(), path.getTextX(), path.getTextY(), textPaint);
            }
        }
// 3) Draw grid OVER paths and start bitmap
        if (drawingData.isGridEnable() && (drawingData.shape.getGridType() != GridType.NO_GRID)) {
// draw Y axis - LETTERS (start from 0es -> A)
// TODO: replace magic number
            for (int i = 0; i < (getHeight() / STEP) + 1; i++) {
                canvas.drawLine(0, STEP * i, drawingData.getShape().getGridType() == GridType.PARTLY_GRID ? PARTLY_GRID_LINE_LENGTH : getWidth(), STEP * i, coordinatesPaint);
                canvas.drawText(convertPositionToLetter(i), 17, STEP * i + 15, labelsPaint);
            }

// draw X axis - NUMBERS (start from 0)
// TODO: replace magic number
            for (int i = 0; i < (getWidth() / STEP) + 1; i++) {
                canvas.drawLine(STEP * i, 0, STEP * i, drawingData.getShape().getGridType() == GridType.PARTLY_GRID ? PARTLY_GRID_LINE_LENGTH : getHeight(), coordinatesPaint);
                canvas.drawText(String.valueOf(i), STEP * i + 5, 20, labelsPaint);
            }
        }

// When user want to draw a text BUT hasn't touched the canvas,
// for convenience draw text in the middle of the screen
        if (drawingData.getShape().getCurrentShape() == ShapesType.DRAW_TEXT && !isFingerTouchingCanvas) {
            canvas.drawText(drawingData.getTextToDrawOnCanvas(),
                    getWidth() / 2, getHeight() / 2, textPaint);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2 + 30, 20, coordinatesPaint);
        }

// !!! if user has released finger from canvas - do not need to draw any addition shapes/lines/text etc.
        if (!isFingerTouchingCanvas) {
            return;
        }

// remove all previous drawn paths/shapes/lines etc...
        shapePath.reset();

        if (drawingData.getShape().getDashedState()) {
            currentPaint.setPathEffect(dashedEffect);
        } else {
            currentPaint.setPathEffect(null);
        }

// Draw current user shape
        switch (drawingData.getShape().getCurrentShape()) {
            case ShapesType.STANDARD_DRAWING:
                canvas.drawPath(currentPath, currentPaint);
                break;

            case ShapesType.CIRCLE:
                rectangleOfDrawing.set(touchDownX, touchDownY, touchX, touchY);
                shapePath.addOval(rectangleOfDrawing, Path.Direction.CW);
                canvas.drawPath(shapePath, currentPaint);
                break;

            case ShapesType.ARROW:
                canvas.drawPath(calculateArrow(shapePath), currentPaint);
                break;

            case ShapesType.DRAW_TEXT:
                canvas.drawText(drawingData.getTextToDrawOnCanvas(), touchX - TEXT_SHIFT, touchY - TEXT_SHIFT, textPaint);
                break;

            case ShapesType.LINE:
                canvas.drawPath(calculateLine(shapePath), currentPaint);
                break;

            case ShapesType.RECTANGLE:
                shapePath.addRoundRect(calculateRectangle(), RECTANGLE_RADIUS, RECTANGLE_RADIUS, Path.Direction.CW);
                canvas.drawPath(shapePath, currentPaint);
                break;

            case ShapesType.TRIANGLE:
                canvas.drawPath(calculateTriangle(shapePath), currentPaint);
                break;

        }

// 3) if user draw something & drawing line are enabled, draw coordinates of moving finger OVER paths and start canvas
        if ((drawingData.getShape().getCurrentColour() != Color.TRANSPARENT) && drawingData.getShape().isDisplayLinesWhileDrawing()) {

            coordinatesPaint.setTextSize(18);

// draw label if enabled
            if (!TextUtils.isEmpty(drawingData.getLabelWhenDrawing())) {
                canvas.drawText(drawingData.getLabelWhenDrawing(), touchX + TEXT_SHIFT, touchY + TEXT_SHIFT, coordinatesPaint);
            }

// draw coordinates
            canvas.drawText(("[ " + convertPositionToLetter((int) (touchY / STEP)) + " : " + formatter.format((int) touchX / STEP)) + "]", touchX - 90, touchY - 10, coordinatesPaint);

// draw two crossing horizontal and vertical lines on finger touch position
            canvas.drawLine(0, touchY, getWidth(), touchY, coordinatesPaint);
            canvas.drawLine(touchX, 0, touchX, getHeight(), coordinatesPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            Log.e("TWO FINGERS DRAWING VIEW, IGNORE");

            isFingerTouchingCanvas = false;
            mScaleDetector.onTouchEvent(event);
            processDoubleTouchEvent(event);

            lastDoubleTouchTime = System.currentTimeMillis();
            Log.e("lastDoubleTouchTime = " + lastDoubleTouchTime);

            return false;
        }

        long value = System.currentTimeMillis() - lastDoubleTouchTime;

        if (lastDoubleTouchTime != 0 && value < ONE_SECOND_IN_MILLISECONDS) {
            Log.e("System.currentTimeMillis() - lastDoubleTouchTime = " + value + " , so EXIT");
            return false;
        }

        float curX = event.getX() / mScaleFactor + rect.left;
        float curY = event.getY() / mScaleFactor + rect.top;
        touchY = curY;
        touchX = curX;

// respond to down, move and up events
        switch (event.getAction()) {
/**
 * User touched the canvas
 */
            case MotionEvent.ACTION_DOWN:
// user has touched canvas
                isFingerTouchingCanvas = true;
                touchDownX = curX;
                touchDownY = curY;
                switch (drawingData.getShape().getCurrentShape()) {
                    case ShapesType.STANDARD_DRAWING:
                        currentPath.moveTo(touchX, touchY);
                        break;
                }
                break;

/**
 * User moving finger
 */
            case MotionEvent.ACTION_MOVE:
                switch (drawingData.getShape().getCurrentShape()) {
                    case ShapesType.STANDARD_DRAWING:
                        currentPath.lineTo(touchX, touchY);
                        break;
                }
                break;

/**
 * User released finger from screen
 */
            case MotionEvent.ACTION_UP:
// indicates is user's finger is still touching canvas
                isFingerTouchingCanvas = false;
                PaintSerializable ps = new PaintSerializable();
                ps.brushStrokeWith = currentPaint.getStrokeWidth();
                ps.colour = currentPaint.getColor();
                ps.isFillInside = currentPaint.isFillInside;
                ps.isDashed = this.drawingData.getShape().getDashedState();

                PathSerializable newPath = new PathSerializable();
                newPath.setPaint(ps);
                newPath.setSavedCanvasX(getWidth());
                newPath.setSavedCanvasY(getHeight());

                switch (drawingData.getShape().getCurrentShape()) {
                    case ShapesType.STANDARD_DRAWING:
                        currentPath.setPaint(ps);
                        currentPath.setSavedCanvasX(getWidth());
                        currentPath.setSavedCanvasY(getHeight());
                        drawingData.paths.add(currentPath);
// Re init path in order to separate every free drawing
                        currentPath = new PathSerializable();
                        break;

                    case ShapesType.CIRCLE:
                        rectangleOfDrawing.set(touchDownX, touchDownY, touchX, touchY);
                        newPath.addOval(rectangleOfDrawing, Path.Direction.CW);
                        newPath.setPaint(ps);
                        drawingData.paths.add(newPath);
                        break;

                    case ShapesType.ARROW:
                        drawingData.paths.add(calculateArrow(newPath));
                        break;

                    case ShapesType.LINE:
                        drawingData.paths.add(calculateLine(newPath));
                        break;

                    case ShapesType.DRAW_TEXT:
                        newPath.setDrawText(touchX - TEXT_SHIFT, touchY - TEXT_SHIFT, drawingData.getTextToDrawOnCanvas());
                        drawingData.paths.add(newPath);
                        drawingData.getShape().setCurrentShape(ShapesType.STANDARD_DRAWING);
                        break;

                    case ShapesType.TRIANGLE:
                        drawingData.paths.add(calculateTriangle(newPath));
                        break;

                    case ShapesType.RECTANGLE:
                        newPath.addRoundRect(calculateRectangle(), RECTANGLE_RADIUS, RECTANGLE_RADIUS, Path.Direction.CW);
                        drawingData.paths.add(newPath);
                        break;
                }
                if (null != listener) this.listener.userHasReleasedFinger();
        }
// notify canvas that it should be redrawn
        invalidate();
        return true;
    }

    /**
     * Undo functionality for list of paths.
     * Removes last path in UNDO list and add it to the end of REDO list
     */
    public void undo() {
        if (!drawingData.paths.isEmpty()) {
            redoPaths.add(drawingData.paths.remove(drawingData.getPaths().size() - 1));
            invalidate();
        }
    }

    /**
     * Redo functionality for list of paths.
     * Removes last path in REDO list and add it to the end of UNDO list
     */
    public void redo() {
        if (!redoPaths.isEmpty()) {
            drawingData.paths.add(redoPaths.remove(redoPaths.size() - 1));
            invalidate();
        }
    }

    /**
     * Return hash map of paths with text, key int = position in paths array and value = path itself
     *
     * @return hash map of paths
     */
    public HashMap<Integer, PathSerializable> getOnlyPathsWithText() {
        HashMap<Integer, PathSerializable> retList = new HashMap<>();
        for (int i = 0; i < drawingData.paths.size(); i++) {
            if (!TextUtils.isEmpty(drawingData.paths.get(i).getTextToDraw())) {
                retList.put(i, drawingData.paths.get(i));
            }
        }
        return retList;
    }

    /**
     * Clear array list of redo paths, using to disable redo functionality
     */
    public void clearRedoPaths() {
        redoPaths = new ArrayList<>();
    }

    /**
     * Calculate arrow from user finger coordinates
     *
     * @param path path to add arrow
     * @return path with arrow
     */
    private PathSerializable calculateArrow(PathSerializable path) {
        path = calculateLine(path);
        int barb = 20;
        float dy = touchDownY - touchY, dx = touchDownX - touchX;
        double theta = Math.atan2(dy, dx), phi = Math.toRadians(140);
        double x, y, rho = theta + phi;
        for (int j = 0; j < 2; j++) {
            x = touchX - barb * Math.cos(rho);
            y = touchY - barb * Math.sin(rho);
            path.moveTo(touchX, touchY);
            path.lineTo((float) x, (float) y);
            rho = theta - phi;
        }
        return path;
    }

    /**
     * Calculate line from user finger location
     *
     * @param path path to draw line on
     * @return path with line
     */
    private PathSerializable calculateLine(PathSerializable path) {
        path.moveTo(touchDownX, touchDownY);
        path.lineTo(touchX, touchY);
        return path;
    }

    /**
     * Calculate rectangle when user movies finger
     *
     * @return rectangle with appropriate coordinates
     */
    private RectF calculateRectangle() {
        if (touchX < touchDownX) {
            if (touchY <
                    touchDownY) {
                rectangleOfDrawing.set(touchX, touchY, touchDownX, touchDownY);
            } else {
                rectangleOfDrawing.set(touchX, touchDownY, touchDownX, touchY);
            }
        } else {
            if (touchY > touchDownY) {
                rectangleOfDrawing.set(touchDownX, touchDownY, touchX, touchY);
            } else {
                rectangleOfDrawing.set(touchDownX, touchY, touchX, touchDownY);
            }
        }
        return rectangleOfDrawing;
    }

    /**
     * Calculate triangle from current position of user finger + finger touchDownX/touchDownY
     *
     * @param pathToCalculate triangle will be added to this path
     * @return path with triangle inside
     */
    private PathSerializable calculateTriangle(PathSerializable pathToCalculate) {
        pathToCalculate.moveTo(touchDownX, touchDownY);
        pathToCalculate.lineTo(touchX, touchY);
        pathToCalculate.lineTo(touchDownX + (touchDownX - touchX), touchY);
        pathToCalculate.lineTo(touchDownX, touchDownY);
        return pathToCalculate;
    }

    /**
     * Return current settings of drawingView
     *
     * @return drawingData object contains all settings
     */
    public DrawingData getDrawingData() {
        return drawingData;
    }

//./////////////////////////////// Minimap

    public void setDrawingData(DrawingData drawingData) {
        this.drawingData = drawingData;
// init brush's size, colour etc..
        initPaint();
// apply canvas settings
        initCanvasSettings();
// drawing settings has been changed, redraw everything
        invalidate();

// Set source rectangle
        if (Conditions.isNotNull(drawingData.canvasBitmap)) {
            SOURCE_IMAGE_RECTANGLE = new Rect(0, 0, drawingData.getCanvasBitmap().getWidth(), drawingData.getCanvasBitmap().getHeight());
        }
    }

    private void initCanvasSettings() {
// if rotate factor != 0, there is an rotation required the bitmap
        if (0 != drawingData.getCanvasSettings().getRotateDegree()) {
// rotate with same zoom, and specific rotate factor
            drawingData.setCanvasBitmap(rotateBitmapZoom(drawingData.getCanvasBitmap(), drawingData.getCanvasSettings().getRotateDegree(), 1, drawingData.getCanvasSettings().isKeepAspectRatio()));
            drawingData.getCanvasSettings().setRotateDegree(0);
        }
    }

    /**
     * Rotate bitmap and zoom it
     *
     * @param bitmap                  original bitmap
     * @param degree                  bitmap will be rotated for this value
     * @param zoom                    zoom factor, 1 if the is no zoom required
     * @param isNeedToKeepAspectRatio true if image should keep aspect ratio, false otherwise (image will be stretched to @see DrawingView.getWidth(), DrawingView.getHeight())
     * @return rotated bitmap
     */
    private Bitmap rotateBitmapZoom(Bitmap bitmap, float degree, float zoom, boolean isNeedToKeepAspectRatio) {
// rotate bitmap using matrix
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);

// apply zoom factor
        float newHeight = bitmap.getHeight() * zoom;
        float newWidth = bitmap.getWidth() / 100 * (100.0f / bitmap.getHeight() * newHeight);
        if (isNeedToKeepAspectRatio) {
            return Bitmap.createBitmap(bitmap, 0, 0, (int) newWidth, (int) newHeight, matrix, true);
        } else {
            return Bitmap.createScaledBitmap(Bitmap.createBitmap(bitmap, 0, 0, (int) newWidth, (int) newHeight, matrix, true), getWidth(), getHeight(), true);
        }
    }

    /**
     * Get dialog builder
     *
     * @return instance of ShapeSettingsDialogBuilder
     */
    public DrawingDataBuilder builder() {
        return new DrawingDataBuilder();
    }

    private void initPaint() {
        currentPaint.setColor(drawingData.getShape().getCurrentColour());
        currentPaint.setAntiAlias(true);
        currentPaint.isFillInside = drawingData.getShape().isFillInside();
        currentPaint.setStyle(currentPaint.isFillInside ? Paint.Style.FILL_AND_STROKE : Paint.Style.STROKE);
        currentPaint.setStrokeWidth(drawingData.getShape().getBrushWidth());

        textPaint.setUnderlineText(drawingData.getTextSettings().isUnderline());
        textPaint.setTypeface(drawingData.getTextSettings().getTextStyle());
        textPaint.setTextSize(drawingData.getTextSettings().getTextSize());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
// change size of the bitmap, accordingly to drawing view size
        synchronized (drawingData.getCanvasBitmap()) {
            drawingData.setCanvasBitmap(Bitmap.createScaledBitmap(drawingData.getCanvasBitmap(), w, h, false));
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            DESTINATION_IMAGE_RECT = new Rect(0, 0, getWidth(), getHeight());
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    private Bitmap convertBitmap(byte[] imageArray) {
        Bitmap bitmap;
        int defaultBitmapSize = 100;

// There is no data from server, return default bitmap
        if (Conditions.isNull(imageArray)) {
            Log.e("Server return empty/error length array");
            return Bitmap.createBitmap(defaultBitmapSize, defaultBitmapSize, Bitmap.Config.RGB_565);
        }

        try {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            options.inPreferQualityOverSpeed = true;
            options.inDither = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;

            bitmap = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length, options);
            bitmap.setHasAlpha(false);
        } catch (Exception ex) {
            bitmap = Bitmap.createBitmap(defaultBitmapSize, defaultBitmapSize, Bitmap.Config.RGB_565);
            Log.e("Error occurred during decoding byte array");
            ex.printStackTrace();
        }
        return bitmap;
    }

    public float getZoom() {
        return zoom;
    }

    public float getMaxZoom() {
        return maxZoom;
    }

    public void setMaxZoom(final float maxZoom) {
        if (maxZoom < 1.0f) {
            return;
        }

        this.maxZoom = maxZoom;
    }

    public boolean isMiniMapEnabled() {
        return showMinimap;
    }

    public void setMiniMapEnabled(final boolean showMiniMap) {
        this.showMinimap = showMiniMap;
    }

    public int getMiniMapHeight() {
        return miniMapHeight;
    }

    public void setMiniMapHeight(final int miniMapHeight) {
        if (miniMapHeight < 0) {
            return;
        }
        this.miniMapHeight = miniMapHeight;
    }

    public int getMiniMapColor() {
        return miniMapColor;
    }

    public void setMiniMapColor(final int color) {
        miniMapColor = color;
    }

    public String getMiniMapCaption() {
        return miniMapCaption;
    }

    public void setMiniMapCaption(final String miniMapCaption) {
        this.miniMapCaption = miniMapCaption;
    }

    public float getMiniMapCaptionSize() {
        return miniMapCaptionSize;
    }

    public void setMiniMapCaptionSize(final float size) {
        miniMapCaptionSize = size;
    }

    public int getMiniMapCaptionColor() {
        return
                miniMapCaptionColor;
    }

    public void setMiniMapCaptionColor(final int color) {
        miniMapCaptionColor = color;
    }

    public void smoothZoomTo(final float zoom, final float x, final float y) {
        smoothZoom = clamp(1.0f, zoom, maxZoom);
        smoothZoomX = x;
        smoothZoomY = y;
        if (zoomListener != null) {
            zoomListener.onZoomStarted(smoothZoom, x, y);
        }
    }

    public OnZoomCanvasCallback getListener() {
        return zoomListener;
    }

    /**
     * Set callback for user interaction with DrawingView
     *
     * @param act activity which will be listening the events
     */
    public void setListener(SuperActivity act) {
// check activity for inheritance from TouchCanvasCallback
        try {
            this.listener = (OnTouchCanvasCallback) act;
        } catch (ClassCastException ex) {
            throw new ClassCastException(act.getLocalClassName() + " must implement TouchCanvasCallback");
        }
    }

    public void setListner(final OnZoomCanvasCallback listener) {
        this.zoomListener = listener;
    }

    private void processDoubleTouchEvent(final MotionEvent ev) {
        final float x1 = ev.getX(0);
        final float dx1 = x1 - lastdx1;
        lastdx1 = x1;
        final float y1 = ev.getY(0);
        final float dy1 = y1 - lastdy1;
        lastdy1 = y1;
        final float x2 = ev.getX(1);
        final float dx2 = x2 - lastdx2;
        lastdx2 = x2;
        final float y2 = ev.getY(1);
        final float dy2 = y2 - lastdy2;
        lastdy2 = y2;

// pointers distance
        final float d = (float) Math.hypot(x2 - x1, y2 - y1);
        final float dd = d - lastd;
        lastd = d;
        final float ld = Math.abs(d - startd);

        Math.atan2(y2 - y1, x2 - x1);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startd = d;
                pinching = false;
                break;

            case MotionEvent.ACTION_MOVE:
                if (pinching || ld > 30.0f) {
                    pinching = true;
                    final float dxk = 0.5f * (dx1 + dx2);
                    final float dyk = 0.5f * (dy1 + dy2);
                    smoothZoomTo(Math.max(1.0f, zoom * d / (d - dd)), zoomX - dxk / zoom, zoomY - dyk / zoom);
                }
                break;

            case MotionEvent.ACTION_UP:
                break;

            default:
                pinching = false;
                break;
        }
    }

    private float clamp(final float min, final float value, final float max) {
        return Math.max(min, Math.min(value, max));
    }

    private float lerp(final float a, final float b, final float k) {
        return a + (b - a) * k;
    }

    private float bias(final float a, final float b, final float k) {
        return Math.abs(b - a) >= k ? a + k * Math.signum(b - a) : b;
    }

    @Override
    protected void dispatchDraw(final Canvas canvas) {
// do zoom
        zoom = lerp(bias(zoom, smoothZoom, 0.05f), smoothZoom, 0.2f);
        smoothZoomX = clamp(0.5f * getWidth() / smoothZoom, smoothZoomX, getWidth() - 0.5f * getWidth() / smoothZoom);
        smoothZoomY = clamp(0.5f * getHeight() / smoothZoom, smoothZoomY, getHeight() - 0.5f * getHeight() / smoothZoom);

        zoomX = lerp(bias(zoomX, smoothZoomX, 0.1f), smoothZoomX, 0.35f);
        zoomY = lerp(bias(zoomY, smoothZoomY, 0.1f), smoothZoomY, 0.35f);
        if (zoom != smoothZoom && zoomListener != null) {
            zoomListener.onZooming(zoom, zoomX, zoomY);
        }

// prepare matrix
        m.setTranslate(0.5f * getWidth(), 0.5f * getHeight());
        m.preScale(zoom, zoom);
        m.preTranslate(-clamp(0.5f * getWidth() / zoom, zoomX, getWidth() - 0.5f * getWidth() / zoom), -clamp(0.5f * getHeight() / zoom, zoomY, getHeight() - 0.5f * getHeight() / zoom));

// draw minimap
        if (showMinimap) {
            if (miniMapHeight < 0) {
                miniMapHeight = (int) (getHeight() / 4 / mScaleFactor);
            }

            float margin = 2f;

            canvas.translate(margin + rect.left, margin + rect.top);

            paint.setColor(0x80000000 | 0x00ffffff & miniMapColor);
            final float w = miniMapHeight * (float) getWidth() / getHeight() / mScaleFactor;
            final float h = miniMapHeight / mScaleFactor;
            canvas.drawRect(0.0f, 0.0f, w, h, paint);

            paint.setColor(0x80000000 | 0x00ffffff & miniMapColor);
            final float dx = w * zoomX / getWidth();
            final float dy = h * zoomY / getHeight();

            float left = dx - 0.5f * w / mScaleFactor;
            float top = dy - 0.5f * h / mScaleFactor;

            float right = dx + 0.5f * w / mScaleFactor;
            float bottom = dy + 0.5f * h / mScaleFactor;

            canvas.drawRect(left, top, right, bottom, paint);

// Draw caption
            if (!TextUtils.isEmpty(miniMapCaption)) {
                paint.setTextSize(miniMapCaptionSize);
                paint.setColor(miniMapCaptionColor);
                paint.setAntiAlias(true);
                canvas.drawText(miniMapCaption, margin, margin + miniMapCaptionSize, paint);
                paint.setAntiAlias(false);
            }

            canvas.translate(margin - rect.left, margin - rect.top);
        }
    }

    /**
     * Touch canvas interface.
     */
    public interface OnTouchCanvasCallback {
        void userHasReleasedFinger();
    }

    /**
     * Zooming listener interface.
     */
    public interface OnZoomCanvasCallback {
        void onZoomStarted(float zoom, float zoomX, float zoomY);

        void onZooming(float zoom, float zoomX, float zoomY);
    }

    /**
     * Types of grid
     */
    public static class GridType {
        public static final int NO_GRID = -1;
        public static final int PARTLY_GRID = 0;
        public static final int FULL_GRID = 1;
        /**
         * For configuring purposes
         */
        public static int[] ALL_TYPES_OF_GRIDS = {NO_GRID, PARTLY_GRID, FULL_GRID};
    }

    /**
     * Types of shapes
     */
    public static class ShapesType {
        public static final int STANDARD_DRAWING = -1;
        public static final int CIRCLE = 0;
        public static final int RECTANGLE = 1;
        public static final int TRIANGLE = 2;
        public static final int LINE = 3;
        public static final int DRAW_TEXT = 4;
        public static final int ARROW = 5;
        /**
         * For configuring purposes
         */
        public static int[] ALL_TYPES_OF_SHAPES = {STANDARD_DRAWING, CIRCLE, RECTANGLE, TRIANGLE, LINE, DRAW_TEXT, ARROW};

        // hide constructor, only static constants exposed
        private ShapesType() {
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scalePointX = detector.getFocusX();
            scalePointY = detector.getFocusY();
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, maxZoom));
            mScaleFactor = mScaleFactor < 1 ? 1 : mScaleFactor;
            invalidate();

            return true;
        }
    }

    public class DrawingDataBuilder {

        private DrawingView.DrawingData drawingData = new DrawingData();

        public DrawingDataBuilder from(DrawingView.DrawingData drawingData) {
            this.drawingData = drawingData;
            return this;
        }

        public DrawingDataBuilder withTextSettings(TextSettings textSettings) {
            this.drawingData.setTextSettings(textSettings);
            return this;
        }

        public DrawingDataBuilder withCanvasSettings(DialogCanvasSettings.CanvasSettings canvasSettings) {
            this.drawingData.setCanvasSettings(canvasSettings);
            return this;
        }

        /**
         * While user drawing display vertical + horizontal line
         *
         * @param state display lines while drawing if true, hide lines otherwise
         * @return ShapeSettingsDialogBuilder instance
         */
        public DrawingDataBuilder withLinesWhileDrawing(boolean state) {
            this.drawingData.setDisplayLinesWhileDrawing(state);
            return this;
        }

        public DrawingDataBuilder withDashedState(boolean state) {
            this.drawingData.displayDashedMenu = true;
            this.drawingData.getShape().setDashedState(state);
            return this;
        }

        public DrawingDataBuilder withShape(DrawingSettings shape) {
            this.drawingData.setShape(shape);
            return this;
        }

        public DrawingDataBuilder withGridEnabled(boolean isEnabled) {
            drawingData.setGridEnable(isEnabled);
            return this;
        }

        public DrawingDataBuilder withPaths(List<PathSerializable> paths) {
            drawingData.setPaths(paths);
            return this;
        }

        public DrawingDataBuilder withLabel(String label) {
            drawingData.setLabelWhenDrawing(label);
            return this;
        }

        public DrawingDataBuilder displayLinesWhileDrawing(boolean state) {
            drawingData.setDisplayLinesWhileDrawing(state);
            return this;
        }

        public DrawingDataBuilder withBitmap(Bitmap bitmap) {
            drawingData.setCanvasBitmap(bitmap);
            return this;
        }

        public DrawingDataBuilder withBitmap(byte[] bitmap) {
            drawingData.setCanvasBitmap(bitmap);
            return this;
        }

        public DrawingDataBuilder withTextToDrawOnCanvas(String textToDrawOnCanvas) {
            drawingData.setTextToDrawOnCanvas(textToDrawOnCanvas);
            return this;
        }

        public DrawingDataBuilder withColor(int color) {
            drawingData.getShape().setCurrentColour(color);
            return this;
        }

        public DrawingDataBuilder withColor(String color) {
            drawingData.getShape().setCurrentColour(Color.parseColor(color));
            return this;
        }

        public DrawingDataBuilder withBrushWidth(int brushWidth, DisplayMetrics metrics) {
            drawingData.getShape().setBrushWidth(brushWidth, metrics);
            return this;
        }

        public DrawingDataBuilder withShape(int shape) {
            drawingData.getShape().setCurrentShape(shape);
            return this;
        }

        public DrawingData build() {

            return drawingData;
        }


    }

    /**
     * Text settings for drawingView
     */
    public class TextSettings {
        // default text size
        private int textSize = 20;
        // underline text
        private boolean isUnderline = false;
        // default text style
        private Typeface textStyle = Typeface.defaultFromStyle(Typeface.NORMAL);

        public Typeface getTextStyle() {
            return textStyle;
        }

        public void setTextStyle(Typeface textStyle) {
            this.textStyle = textStyle;
        }

        public boolean isUnderline() {
            return isUnderline;
        }

        public void setIsUnderline(boolean isUnderline) {
            this.isUnderline = isUnderline;
        }

        public int getTextSize() {
            return textSize;
        }

        public void setTextSize(int textSize) {
            this.textSize = textSize;
        }
    }

    /**
     * Configuration class for drawing view
     */
    public class DrawingData {
        // encapsulate shape settings
        private DrawingSettings shape = new DrawingSettings();
        // Text settings
        private TextSettings textSettings = new TextSettings();
        // draw X,Y axis grid
        private boolean isGridEnable = false;
        // paths to be drawn
        private List<PathSerializable> paths = new ArrayList<>();
        // string label to be displayed while drawing
        private String labelWhenDrawing = "";
        // background canvas
        private Bitmap canvasBitmap;
        // text to draw on canvas
        private String textToDrawOnCanvas = null;
        private DialogCanvasSettings.CanvasSettings canvasSettings;
        // Display lines while user drawing
        private boolean displayLinesWhileDrawing = false;
        // Display lines while user drawing
        private boolean
                displayDashedMenu = true;

        public DialogCanvasSettings.CanvasSettings getCanvasSettings() {
            if (null == canvasSettings) {
                canvasSettings = new DialogCanvasSettings.CanvasSettings();
            }
            return canvasSettings;
        }

        public void setCanvasSettings(DialogCanvasSettings.CanvasSettings canvasSettings) {
            this.canvasSettings = canvasSettings;
        }

        public TextSettings getTextSettings() {
            if (null == textSettings) {
                textSettings = new TextSettings();
            }
            return textSettings;
        }

        public void setTextSettings(TextSettings textSettings) {
            this.textSettings = textSettings;
        }

        public String getTextToDrawOnCanvas() {
            return textToDrawOnCanvas;
        }

        public void setTextToDrawOnCanvas(String textToDrawOnCanvas) {
            shape.setCurrentShape(ShapesType.DRAW_TEXT);
            if (TextUtils.isEmpty(textToDrawOnCanvas)) {
                textToDrawOnCanvas = "";
            }
            this.textToDrawOnCanvas = textToDrawOnCanvas;
        }

        public DrawingSettings getShape() {
            return shape;
        }

        public void setShape(DrawingSettings shape) {
            this.shape = shape;
        }

        public boolean isGridEnable() {
            return isGridEnable;
        }

        public void setGridEnable(boolean isGridEnable) {
            this.isGridEnable = isGridEnable;
        }

        public List<PathSerializable> getPaths() {
            if (null == paths) {
                paths = new ArrayList<>();
            }
            return paths;
        }

        public void setPaths(List<PathSerializable> paths) {
            if (null == paths) {
                paths = new ArrayList<>();
            }
            this.paths = paths;
        }

        public String getLabelWhenDrawing() {
            return labelWhenDrawing;
        }

        public void setLabelWhenDrawing(String labelWhenDrawing) {
            if (TextUtils.isEmpty(labelWhenDrawing)) {
                labelWhenDrawing = "";
            }
            this.labelWhenDrawing = labelWhenDrawing;
        }

        public Bitmap getCanvasBitmap() {
            if (null == canvasBitmap) {
                canvasBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            }
            return canvasBitmap;
        }

        public void setCanvasBitmap(byte[] canvasBitmap) {
            this.canvasBitmap = convertBitmap(canvasBitmap);
        }

        public void setCanvasBitmap(Bitmap canvasBitmap) {
            this.canvasBitmap = canvasBitmap;
        }

        public void setDisplayLinesWhileDrawing(boolean displayLinesWhileDrawing) {
            this.displayLinesWhileDrawing = displayLinesWhileDrawing;
        }
    }
}
