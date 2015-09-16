package com.drawingmagic.core;

import android.graphics.Paint;

import java.io.Serializable;

public class PaintSerializable extends Paint implements Serializable {
    private static final long serialVersionUID = 2166800575010102231L;
    public int colour;
    public float brushStrokeWith;
    public boolean isFillInside;
    public boolean isDashed;


    public PaintSerializable(int ditherFlag) {
        super(ditherFlag);
    }

    public PaintSerializable() {
    }

    /**
     * Obtain new instance of paint from existing one
     *
     * @param paint
     * @return
     */
    public PaintSerializable from(PaintSerializable paint) {
        paint.setStrokeWidth(paint.brushStrokeWith);
        paint.setColor(paint.colour);
        paint.setAntiAlias(true);
        paint.setStyle(paint.isFillInside ? Style.FILL_AND_STROKE : Style.STROKE);
        paint.setStrokeJoin(Join.ROUND);
        paint.setStrokeCap(Cap.ROUND);
        return paint;
    }

}
