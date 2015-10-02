package com.drawingmagic.fragments;

import android.support.v4.app.Fragment;
import android.view.View;

import com.drawingmagic.R;
import com.drawingmagic.eventbus.Event;
import com.drawingmagic.utils.AnimationUtils;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Touch;

import de.greenrobot.event.EventBus;

import static com.drawingmagic.utils.AnimationUtils.AnimationTechniques.ZOOM_IN;

@EFragment(R.layout.fragment_menu_cropper)
public class FMenuCropper extends Fragment {

    @Click
    void mivApply() {
        EventBus.getDefault().post(new Event(Event.ON_APPLY_CROPPING));
    }

    @Click
    void mivCroppingShape() {
        EventBus.getDefault().post(new Event(Event.ON_CHANGE_CROPPING_SHAPE));
    }

    @Touch({R.id.mivApply, R.id.mivCroppingShape})
    boolean onTouch(View viewBeenTouched) {
        AnimationUtils.animate(viewBeenTouched, ZOOM_IN);
        return false;
    }
}
