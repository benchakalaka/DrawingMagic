package com.drawingmagic.fragments;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.drawingmagic.R;
import com.drawingmagic.eventbus.Event;
import com.drawingmagic.utils.AnimationUtils;
import com.github.clans.fab.FloatingActionButton;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;

import static com.drawingmagic.eventbus.Event.*;
import static com.drawingmagic.utils.AnimationUtils.AnimationTechniques.*;

@EFragment(R.layout.fragment_cropping_tools)
public class FCropperTools extends Fragment {

    @ViewById
    public ImageView ivFinalImage;

    @ViewById
    FloatingActionButton fabSave;

    @Click
    void fabSave() {
        EventBus.getDefault().post(new Event(ON_FINAL_SAVE_IMAGE));
    }

    @Touch({R.id.fabSave, R.id.fabMenu})
    boolean onTouch(View clickedView) {
        AnimationUtils.animate(clickedView, ZOOM_IN);
        return false;
    }
//
//    @Click
//    void rlOval() {
//        AnimationUtils.animate(rlOval, ZOOM_IN);
//        //EventBus.getDefault().post(new Event(Event.ON_RESTORE_IMAGE_BEFORE_CROPPING));
//        EventBus.getDefault().post(new Event(Event.ON_CHANGE_CROPPING_SHAPE, CropShape.OVAL));
//    }
}
