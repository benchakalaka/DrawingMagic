package com.drawingmagic.fragments;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RelativeLayout;

import com.drawingmagic.R;
import com.drawingmagic.eventbus.Event;
import com.drawingmagic.utils.AnimationUtils;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;

/**
 * Project DrawingMagic.
 * Created by ihorkarpachev.
 * On 20/09/15 at 13:32.
 */
@EFragment(R.layout.fragment_clearing_tools)
public class FMenuClearingTools extends Fragment {

    @ViewById
    RelativeLayout rlClearCanvas, rlUndo, rlRedo;

    /**
     * Tag of these views are one of the constants { Event.ON_UNDO, Event.ON_REDO, Event.ON_CLEAR_CANVAS}
     *
     * @param clickedView view has been clicked
     */
    @Click({R.id.rlUndo, R.id.rlRedo, R.id.rlClearCanvas})
    void clearingToolsClicked(View clickedView) {
        EventBus.getDefault().post(new Event(Integer.parseInt(clickedView.getTag().toString())));
        AnimationUtils.animate(clickedView, AnimationUtils.AnimationTechniques.ZOOM_IN);
    }

    public FMenuClearingTools() {
    }
}