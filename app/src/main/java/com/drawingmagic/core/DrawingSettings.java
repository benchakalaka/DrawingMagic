/*
 * Created by Ihor Karpachev, Copyright (c) 2015. .
 */

package com.drawingmagic.core;

import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Class represents drawing settings
 */
public class DrawingSettings {
    private int currentShape;
    private int brushWidth;
    private int currentColour;
    private boolean fillInside;
    private boolean dashed = false;
    private boolean displayLinesWhileDrawing = false;
    private int gridType;

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

    public void setCurrentColour(String currentColour) {
        this.currentColour = Color.parseColor(currentColour);
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
