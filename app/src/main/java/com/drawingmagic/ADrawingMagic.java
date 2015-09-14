package com.drawingmagic;

import android.graphics.Color;

import com.drawingmagic.core.DrawingView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import static com.drawingmagic.core.DrawingView.ShapesType;

/**
 * Project DrawingMagic.
 * Created by ihorkarpachev
 * On 13/09/15 at 17:44.
 */
@EActivity(R.layout.activity_drawing_magic)
public class ADrawingMagic extends SuperActivity{


    private static final int DEFAULT_BRUSH_SIZE = 3;
    @ViewById
    DrawingView drawingView;

    @AfterViews
    void afterViews() {
        // init and setup drawingview
        drawingView.setDrawingData(drawingView.builder().from(drawingView.getDrawingData()).
                withBrushWidth(DEFAULT_BRUSH_SIZE, getResources().getDisplayMetrics()).
                withLinesWhileDrawing(false).
                withShape(ShapesType.STANDARD_DRAWING).
                withColor(Color.BLACK).
                withGridEnabled(true).build());
    }
}
