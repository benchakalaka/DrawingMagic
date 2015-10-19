package com.drawingmagic.core;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.drawingmagic.SuperActivity;
import com.drawingmagic.dialogs.DialogCanvasSettings;
import com.drawingmagic.utils.Conditions;
import com.drawingmagic.utils.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


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
    private static final Paint PAINT_BITMAP = new Paint();
    // paint for drawing text
    private final TextPaint textPaint = new TextPaint();
    // default paint for grid drawing
    private final Paint coordinatesPaint = new Paint();
    // rectangle of drawing for different types of shapes
    private final RectF rectangleOfDrawing = new RectF();
    // drawing
    private final Matrix SCALE_TO_FIT_CENTER_MATRIX = new Matrix();
    // Dashed effect for all shapes
    private final DashPathEffect dashedEffect = new DashPathEffect(new float[]{15, 15, 15, 15}, 0);
    long lastDoubleTouchTime = 0;
    private final static long ONE_SECOND_IN_MILLISECONDS = 300;
    // current paint which contains user's brush settings
    private final PaintSerializable currentPaint = new PaintSerializable();
    // current X,Y position lives in touchX/Y, start position is in touchDownX/Y
    private float touchX, touchY, touchDownX, touchDownY;
    // settings object
    private DrawingData drawingData = new DrawingData();
    // is user's finger touching canvas
    private boolean isFingerTouchingCanvas = false;
    // Current path drawing contains shape drawing by user
    private PathSerializable currentPath = new PathSerializable();
    // list of paths for undo/redo operations
    private List<PathSerializable> redoPaths = new ArrayList<>();
    // path for any shape
    private final PathSerializable shapePath = new PathSerializable();
    // Activity/Dialog/Fragment which will be notified in case of event arise
    private OnTouchCanvasCallback listener;
    // Rectangle for source image
    private final Rect rect = new Rect();
    // Default brush zie
    public static final int DEFAULT_BRUSH_SIZE = 5;
    // Default text size
    public static final int DEFAULT_TEXT_SIZE = 30;
    // Flip type direction
    public static final int FLIP_VERTICAL = 1;
    public static final int FLIP_HORIZONTAL = 2;
    // Scaling objects
    private final ScaleGestureDetector mScaleDetector;
    private float scalePointX, scalePointY;
    // Max zoom value
    private boolean isDrawingEnabled = true;
    private final static float MAX_ZOOM_FACTOR = 5.0f;
    private final static float MIN_ZOOM = 1.0f;
    // rotation factor
    private float rotationDegree = 0f;

    /**
     * Do we need to apply transformation to canvas, or skip it
     */
    private boolean isMatrixTransformationApplied = false;
    private final Matrix transformMatrix = new Matrix();
    private final static float CIRCLE_TEXT_RADIUS = 20;
    private static final float SCALE_DELTA = 0.05f;
    private static final float DEFAULT_ROTATE_SCALE_FACTOR = 1.0f;
    private float mScaleFactor = DEFAULT_ROTATE_SCALE_FACTOR;
    private boolean scaleWithZoom = false;
    private float currentScaleZoomFactor = DEFAULT_ROTATE_SCALE_FACTOR;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDrawingCacheEnabled(true);
        setDrawingCacheQuality(DRAWING_CACHE_QUALITY_HIGH);

        // init user's paint settings
        currentPaint.setStyle(Paint.Style.STROKE);
        currentPaint.setStrokeJoin(Paint.Join.ROUND);
        currentPaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint.setFakeBoldText(true);
        textPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        textPaint.setTextSize(DEFAULT_TEXT_SIZE);
        textPaint.setUnderlineText(true);

        // init Bitmap paint
        PAINT_BITMAP.setAntiAlias(true);
        PAINT_BITMAP.setFilterBitmap(true);
        PAINT_BITMAP.setDither(true);

        mScaleDetector = new ScaleGestureDetector(this.getContext(), new ScaleListener());
    }

    public void setDrawingEnabled(boolean enabled) {
        this.isDrawingEnabled = enabled;
    }
    public void setDrawingData(DrawingData drawingData) {
        this.drawingData = drawingData;
        // init brush's size, colour etc..
        initPaint();
        // apply canvas settings

        // drawing settings has been changed, redraw everything
        invalidate();

        // Set source rectangle
        scaleBitmapToCenter();
    }

    private void scaleBitmapToCenter() {
        if (Conditions.isNotNull(drawingData.canvasBitmap)) {
            SCALE_TO_FIT_CENTER_MATRIX.reset();
            RectF sourceRect = new RectF(0, 0, (float) drawingData.canvasBitmap.getWidth(), (float) drawingData.canvasBitmap.getHeight());
            RectF destRect = new RectF(0, 0, (float) getWidth(), getHeight());
            SCALE_TO_FIT_CENTER_MATRIX.setRectToRect(sourceRect, destRect, Matrix.ScaleToFit.CENTER);
        }
    }

    public static Bitmap flip(Bitmap src, int type) {
        // create new matrix for transformation
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
     * onDraw will be called after any touch event or invalidating drawing surface
     */

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // already implemented
        // flip()

        canvas.setMatrix(transformMatrix);
        canvas.drawColor(Color.BLACK);

        // 1) draw simple bitmap
        canvas.scale(mScaleFactor, mScaleFactor, scalePointX, scalePointY);
        canvas.getClipBounds(rect);

        canvas.drawBitmap(drawingData.getCanvasBitmap(), SCALE_TO_FIT_CENTER_MATRIX, PAINT_BITMAP);

        //canvas.restore();
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

        // When user want to draw a text BUT hasn't touched the canvas,
        // for convenience draw text in the middle of the screen
        if (drawingData.getShape().getCurrentShape() == ShapesType.DRAW_TEXT && !isFingerTouchingCanvas) {
            canvas.drawText(drawingData.getTextToDrawOnCanvas(), getWidth() / 2, getHeight() / 2, textPaint);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2 + CIRCLE_TEXT_RADIUS, CIRCLE_TEXT_RADIUS, coordinatesPaint);
        }

        // 3) Draw grid OVER paths and start bitmap
        if (drawingData.isGridEnable() && (drawingData.shape.getGridType() != GridType.NO_GRID)) {
            for (int i = 0; i < (getHeight() / STEP); i++) {
                canvas.drawLine(0, STEP * i, drawingData.getShape().getGridType() == GridType.PARTLY_GRID ? PARTLY_GRID_LINE_LENGTH : getWidth(), STEP * i, coordinatesPaint);
            }

            for (int i = 0; i < (getWidth() / STEP); i++) {
                canvas.drawLine(STEP * i, 0, STEP * i, drawingData.getShape().getGridType() == GridType.PARTLY_GRID ? PARTLY_GRID_LINE_LENGTH : getHeight(), coordinatesPaint);
            }

            // draw rectangle border around image
            // left border
            canvas.drawLine(0, 0, 0, getHeight() - 1, coordinatesPaint);
            // right border
            canvas.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight() - 1, coordinatesPaint);
            // top border
            canvas.drawLine(0, 0, getWidth() - 1, 0, coordinatesPaint);
            // bottom border
            canvas.drawLine(0, getHeight() - 1, getWidth() - 1, getHeight() - 1, coordinatesPaint);

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
            default:
                Log.e("Unknown SHAPE");
                return;

        }

        // 3) if user draw something & drawing line are enabled, draw coordinates of moving finger OVER paths and start canvas
        if ((drawingData.getShape().getCurrentColour() != Color.TRANSPARENT) && drawingData.getShape().isDisplayLinesWhileDrawing()) {

            coordinatesPaint.setTextSize(DEFAULT_TEXT_SIZE);
            // draw two crossing horizontal and vertical lines on finger touch position
            canvas.drawLine(0, touchY, getWidth(), touchY, coordinatesPaint);
            canvas.drawLine(touchX, 0, touchX, getHeight(), coordinatesPaint);
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getPointerCount() == 2) {
            Log.e("TWO FINGERS DRAWING VIEW, IGNORE");

            isFingerTouchingCanvas = false;
            event.setLocation(event.getX() / mScaleFactor + rect.left, event.getY() / mScaleFactor + rect.top);
            mScaleDetector.onTouchEvent(event);

            lastDoubleTouchTime = System.currentTimeMillis();
            Log.e("lastDoubleTouchTime = " + lastDoubleTouchTime);

            return false;
        }

        long value = System.currentTimeMillis() - lastDoubleTouchTime;

        if (lastDoubleTouchTime != 0 && value < ONE_SECOND_IN_MILLISECONDS) {
            Log.e("System.currentTimeMillis() - lastDoubleTouchTime = " + value + " , so EXIT");
            return false;
        }
        float curX = event.getX() / (mScaleFactor * (currentScaleZoomFactor)) + rect.left;
        float curY = event.getY() / (mScaleFactor * (currentScaleZoomFactor)) + rect.top;
        touchY = curY;
        touchX = curX;
        if(isDrawingEnabled) {
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
                    if (drawingData.getShape().getCurrentShape() == ShapesType.STANDARD_DRAWING) {
                        currentPath.moveTo(touchX, touchY);

                    }
                    break;

                /**
                 * User moving finger
                 */
                case MotionEvent.ACTION_MOVE:
                    if (drawingData.getShape().getCurrentShape() == ShapesType.STANDARD_DRAWING) {
                        currentPath.lineTo(touchX, touchY);
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
                        default:
                            Log.e("MotionEvent.ACTION_UP Unknown SHAPE");
                            break;
                    }
                    if (null != listener) this.listener.userHasReleasedFinger();


                default:
                    // ignore rest of the events
                    break;
            }
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
            Log.e(drawingData.paths.size() + " - Paths left in list");
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
     * Clear all paths
     */
    public void clearCanvas() {
        clearRedoPaths();
        drawingData.getPaths().clear();
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

        coordinatesPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // change size of the bitmap, accordingly to drawing view size
        synchronized (drawingData.getCanvasBitmap()) {
            drawingData.setCanvasBitmap(Bitmap.createScaledBitmap(drawingData.getCanvasBitmap(), w, h, false));
        }
    }


    /**
     * Set callback for user interaction with DrawingView
     *
     * @param act activity which will be listening the events
     */
    public void setListener(SuperActivity act) {
        try {
            this.listener = (OnTouchCanvasCallback) act;
        } catch (ClassCastException ex) {
            throw new ClassCastException(act.getLocalClassName() + " must implement OnTouchCanvasCallback");
        }
    }

    //// TODO: 21/09/2015 Replace to up
    private float tiltFactorX = 0f;
    private float tiltFactorY = 0f;

    public void setRotationDegree(float degree) {
        this.rotationDegree = degree;
        configureTransformationMatrix();
    }

    public void setRotationDegree(float degree, boolean scaleZoomIn) {
        this.rotationDegree = degree;
        this.scaleWithZoom = scaleZoomIn;
        configureTransformationMatrix();
    }

    public void setTiltFactorX(float tiltFactorX) {
        this.tiltFactorX = tiltFactorX;
        configureTransformationMatrix();
    }

    public void setTiltFactorY(float tiltFactorY) {
        this.tiltFactorY = tiltFactorY;
        configureTransformationMatrix();
    }

    private void configureTransformationMatrix() {
        transformMatrix.setSkew(tiltFactorX, tiltFactorY, getWidth() / 2, getHeight() / 2);
        transformMatrix.postRotate(rotationDegree, getWidth() / 2, getHeight() / 2);
        if (scaleWithZoom) {

            currentScaleZoomFactor = DEFAULT_ROTATE_SCALE_FACTOR + Math.abs(rotationDegree * SCALE_DELTA);
            transformMatrix.postScale(
                    currentScaleZoomFactor,
                    currentScaleZoomFactor,
                    getWidth() / 2, getHeight() / 2
            );
        }
        invalidate();
    }

    public void resetAllTransformation() {
        rotationDegree = 0;
        tiltFactorX = 0;
        tiltFactorY = 0;
        transformMatrix.reset();
        invalidate();
    }

    public void setGridType(int gridType) {
        drawingData.getShape().setGridType(gridType);
        invalidate();
    }


    /**
     * Touch canvas interface.
     */
    public interface OnTouchCanvasCallback {
        void userHasReleasedFinger();
    }


    /**
     * Types of grid
     */
    public static class GridType {
        public static final int NO_GRID = -1;
        public static final int PARTLY_GRID = 0;
        public static final int FULL_GRID = 1;
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
            mScaleFactor = Math.max(MIN_ZOOM, Math.min(mScaleFactor, MAX_ZOOM_FACTOR));

            mScaleFactor = mScaleFactor < MIN_ZOOM ? MIN_ZOOM : mScaleFactor;
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
        private int textSize = DEFAULT_TEXT_SIZE;
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
        private boolean displayDashedMenu = true;

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

        public void setCanvasBitmap(Bitmap canvasBitmap) {
            this.canvasBitmap = canvasBitmap;
        }

        public void setDisplayLinesWhileDrawing(boolean displayLinesWhileDrawing) {
            this.displayLinesWhileDrawing = displayLinesWhileDrawing;
        }
    }
}
