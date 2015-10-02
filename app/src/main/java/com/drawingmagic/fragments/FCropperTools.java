package com.drawingmagic.fragments;

import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.drawingmagic.R;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_canvas_tools)
public class FCropperTools extends Fragment {

    @ViewById
    public ImageView ivFinalImage;

//    @Click
//    void rlRectangle() {
//        AnimationUtils.animate(rlRectangle, ZOOM_IN);
//    }
//
//    @Click
//    void rlOval() {
//        AnimationUtils.animate(rlOval, ZOOM_IN);
//        //EventBus.getDefault().post(new Event(Event.ON_RESTORE_IMAGE_BEFORE_CROPPING));
//        EventBus.getDefault().post(new Event(Event.ON_CHANGE_CROPPING_SHAPE, CropShape.OVAL));
//    }
}
