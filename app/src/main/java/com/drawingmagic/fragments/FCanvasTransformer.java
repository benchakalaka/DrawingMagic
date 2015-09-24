package com.drawingmagic.fragments;

import android.support.v4.app.Fragment;
import android.widget.RelativeLayout;

import com.drawingmagic.R;
import com.drawingmagic.eventbus.Event;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;

/**
 * Project DrawingMagic.
 * Created by ihorkarpachev.
 * On 23/09/15 at 13:32.
 */
@EFragment(R.layout.fragment_canvas_transformer)
public class FCanvasTransformer extends Fragment {

    @ViewById
    RelativeLayout rlRotate, rlSkew;

    public FCanvasTransformer() {
    }

    @Click
    void rlRotate() {
        EventBus.getDefault().post(new Event(Event.ON_ROTATE_TRANSFORMATION));
    }

    @Click
    void rlSkew() {
        EventBus.getDefault().post(new Event(Event.ON_SKEW_TRANSFORMATION));
    }
}
