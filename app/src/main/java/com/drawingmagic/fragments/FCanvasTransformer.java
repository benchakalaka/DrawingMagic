package com.drawingmagic.fragments;

import android.support.v4.app.Fragment;
import android.widget.RelativeLayout;

import com.drawingmagic.R;
import com.drawingmagic.eventbus.Event;
import com.drawingmagic.utils.AnimationUtils;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;

import static com.drawingmagic.eventbus.Event.FLIP;
import static com.drawingmagic.eventbus.Event.ON_ROTATE_TRANSFORMATION;
import static com.drawingmagic.eventbus.Event.ON_SKEW_TRANSFORMATION;
import static com.drawingmagic.utils.AnimationUtils.AnimationTechniques.FADE_IN;
import static com.drawingmagic.utils.GraphicUtils.FLIP_HORIZONTAL;
import static com.drawingmagic.utils.GraphicUtils.FLIP_VERTICAL;

/**
 * Project DrawingMagic.
 * Created by ihorkarpachev.
 * On 23/09/15 at 13:32.
 */
@EFragment(R.layout.fragment_canvas_transformer)
public class FCanvasTransformer extends Fragment {

    @ViewById
    RelativeLayout rlRotate, rlSkew, rlFlipHorizontal, rlFlipVertical;

    public FCanvasTransformer() {
    }

    @Click
    void rlRotate() {
        AnimationUtils.animate(rlRotate, FADE_IN);
        EventBus.getDefault().post(new Event(ON_ROTATE_TRANSFORMATION));
    }

    @Click
    void rlSkew() {
        AnimationUtils.animate(rlSkew, FADE_IN);
        EventBus.getDefault().post(new Event(ON_SKEW_TRANSFORMATION));
    }

    @Click
    void rlFlipHorizontal() {
        AnimationUtils.animate(rlFlipHorizontal, FADE_IN);
        EventBus.getDefault().post(new Event(FLIP, FLIP_HORIZONTAL));
    }

    @Click
    void rlFlipVertical() {
        AnimationUtils.animate(rlFlipVertical, FADE_IN);
        EventBus.getDefault().post(new Event(FLIP, FLIP_VERTICAL));
    }
}
