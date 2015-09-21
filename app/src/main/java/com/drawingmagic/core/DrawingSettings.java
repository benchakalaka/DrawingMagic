/*
 * Created by Ihor Karpachev, Copyright (c) 2015. .
 */

package com.drawingmagic.core;

import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import static com.drawingmagic.core.DrawingView.GridType;
import static com.drawingmagic.core.DrawingView.ShapesType;

/**
 * Class represents drawing settings
 */
public class DrawingSettings {
    private int currentShape = ShapesType.STANDARD_DRAWING;
    private int brushWidth = DrawingView.DEFAULT_BRUSH_SIZE;
    private int currentColour = Color.BLUE;
    private boolean fillInside = false;
    private boolean dashed = false;
    private boolean displayLinesWhileDrawing = false;
    private int gridType = GridType.FULL_GRID;

    public boolean getDashedState() {
        return this.dashed;
    }

    public void setDashedState(boolean state) {
        this.dashed = state;
    }

    public int getCurrentShape() {
        return currentShape;
    }

    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    public int getBrushWidth() {
        return brushWidth;
    }

    public void setBrushWidth(int brushWidth, DisplayMetrics metrics) {
        this.brushWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, brushWidth, metrics);
    }

    public int getCurrentColour() {
        return currentColour;
    }


    public void setCurrentColour(int currentColour) {
        this.currentColour = currentColour;
    }

    public boolean isDisplayLinesWhileDrawing() {
        return displayLinesWhileDrawing;
    }

    public void setDisplayLinesWhileDrawing(boolean displayLinesWhileDrawing) {
        this.displayLinesWhileDrawing = displayLinesWhileDrawing;
    }

    public boolean isFillInside() {
        return fillInside;
    }

    public void setFillInside(boolean fillInside) {
        this.fillInside = fillInside;
    }

    public int getGridType() {
        return gridType;
    }

    public void setGridType(int gridType) {
        this.gridType = gridType;
    }
}
