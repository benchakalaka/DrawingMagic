package com.drawingmagic.views.menu;


import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.drawingmagic.R;
import com.drawingmagic.eventbus.Event;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.SeekBarProgressChange;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;

import static com.drawingmagic.eventbus.Event.ON_TILT_FACTOR_X_CHANGED;
import static com.drawingmagic.eventbus.Event.ON_TILT_FACTOR_Y_CHANGED;

/**
 * Project DrawingMagic.
 * Created by ihorkarpachev.
 * On 20/09/15 at 11:22.
 */
@EViewGroup(R.layout.view_skew_menu)
public class SkewMenu extends RelativeLayout {

    @ViewById
    SeekBar seekBarTiltFactorX, seekBarTiltFactorY;

    public SkewMenu(Context context) {
        super(context);
    }

    @SeekBarProgressChange
    void seekBarTiltFactorX(int progressX) {
        EventBus.getDefault().post(new Event(ON_TILT_FACTOR_X_CHANGED, progressX));
    }

    @SeekBarProgressChange
    void seekBarTiltFactorY(int progressY) {
        EventBus.getDefault().post(new Event(ON_TILT_FACTOR_Y_CHANGED, progressY));
    }

}
