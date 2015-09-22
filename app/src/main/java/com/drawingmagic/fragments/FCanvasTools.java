package com.drawingmagic.fragments;

import android.support.v4.app.Fragment;

import com.drawingmagic.R;
import com.drawingmagic.eventbus.Event;

import net.steamcrafted.materialiconlib.MaterialIconView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.fragment_canvas_tools)
public class FCanvasTools extends Fragment {

    @ViewById
    MaterialIconView mivApply, mivCancel;

    @Click
    void mivApply() {
        EventBus.getDefault().post(new Event(Event.ON_APPLY_CROPPING));
    }

    @Click
    void mivCancel() {
        EventBus.getDefault().post(new Event(Event.ON_RESTORE_IMAGE_BEFORE_CROPPING));
    }
}
