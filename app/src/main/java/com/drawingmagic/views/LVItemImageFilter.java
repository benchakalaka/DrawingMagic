package com.drawingmagic.views;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.drawingmagic.R;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Project DrawingMagic.
 * Created by ihorkarpachev.
 * On 15/09/15 at 19:46.
 */
@EViewGroup(R.layout.item_image_filter)
public class LVItemImageFilter extends RelativeLayout {

    @ViewById
    public TextView tvDescription;

    public LVItemImageFilter(Context context) {
        super(context);
    }
}
