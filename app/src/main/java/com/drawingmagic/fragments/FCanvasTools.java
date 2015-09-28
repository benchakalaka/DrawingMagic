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

import static com.drawingmagic.utils.AnimationUtils.AnimationTechniques.ZOOM_IN;
import static com.theartofdev.edmodo.cropper.CropImageView.CropShape;

@EFragment(R.layout.fragment_canvas_tools)
public class FCanvasTools extends Fragment {

    @ViewById
    RelativeLayout rlRectangle, rlOval;

    @Click
    void rlRectangle() {
        AnimationUtils.animate(rlRectangle, ZOOM_IN);
        //EventBus.getDefault().post(new Event(Event.ON_APPLY_CROPPING));
        EventBus.getDefault().post(new Event(Event.ON_CHANGE_CROPPING_SHAPE, CropShape.RECTANGLE));
    }

    @Click
    void rlOval() {
        AnimationUtils.animate(rlOval, ZOOM_IN);
        //EventBus.getDefault().post(new Event(Event.ON_RESTORE_IMAGE_BEFORE_CROPPING));
        EventBus.getDefault().post(new Event(Event.ON_CHANGE_CROPPING_SHAPE, CropShape.OVAL));
    }
}
