package com.drawingmagic.helpers;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;

/**
 * Created by ihor.karpachev on 16/09/2015.
 * Project: DrawingMagic
 * Package: com.drawingmagic.helpers
 * Datascope Systems Ltd.
 */
public class FrameResourceItemHolder implements FrameProvider {

    public final int frameResource;
    public final String frameName;

    public FrameResourceItemHolder(String filterName, @DrawableRes int resource) {
        this.frameName = filterName;
        this.frameResource = resource;
    }

    @Override
    public String getFrameName() {
        return frameName;
    }

    @Override
    public Drawable getFramePreview(Resources resources) {
        return resources.getDrawable(this.frameResource);
    }

    @Override
    public Bitmap drawFrame(Paint paint, Resources resources, int width, int height) {
        return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,frameResource),width,height,false);
    }

}
