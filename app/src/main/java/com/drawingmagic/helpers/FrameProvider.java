package com.drawingmagic.helpers;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

/**
 * Created by Molochko Oleksandr on 10/22/15.
 * Package com.drawingmagic.helpers
 * Project DrawingMagic
 * Copyright (c) 2013-2015 Datascope Ltd. All Rights Reserved.
 */
public interface FrameProvider {
    String getFrameName();
    Drawable getFramePreview(Resources resources);
    Bitmap drawFrame(Paint paint, Resources resources, int width, int height);
}
