package com.drawingmagic.fragments;

import android.support.v4.app.Fragment;
import android.view.View;

import com.drawingmagic.R;
import com.drawingmagic.eventbus.Event;
import com.drawingmagic.utils.AnimationUtils;

import net.steamcrafted.materialiconlib.MaterialIconView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;

import static com.drawingmagic.eventbus.Event.ON_APPLY_DRAWING_ON_CANVAS;
import static com.drawingmagic.eventbus.Event.ON_RESTORE_IMAGE_BEFORE_DRAWING;

/**
 * Project DrawingMagic.
 * Created by ihorkarpachev.
 * On 20/09/15 at 13:32.
 */
@EFragment(R.layout.fragment_menu_drawing_tools)
public class FMenuDrawingTools extends Fragment {
    @ViewById
    MaterialIconView mivApply, mivCancel;

    /**
     * Tag of these views are one of the constants { Event.ON_UNDO, Event.ON_REDO, Event.ON_CLEAR_CANVAS}
     *
     * @param clickedView view has been clicked
     */
    @Click({R.id.rlUndo, R.id.rlRedo, R.id.rlClearCanvas})
    void clearingToolsClicked(View clickedView) {
        AnimationUtils.animate(clickedView, AnimationUtils.AnimationTechniques.ZOOM_IN);
        EventBus.getDefault().post(new Event(Integer.parseInt(clickedView.getTag().toString())));
    }

    @Click
    void mivApply() {
        AnimationUtils.animate(mivApply, AnimationUtils.AnimationTechniques.ZOOM_IN);
        EventBus.getDefault().post(new Event(ON_APPLY_DRAWING_ON_CANVAS));
    }

    @Click
    void mivCancel() {
        AnimationUtils.animate(mivCancel, AnimationUtils.AnimationTechniques.ZOOM_IN);
        EventBus.getDefault().post(new Event(ON_RESTORE_IMAGE_BEFORE_DRAWING));
    }

    public FMenuDrawingTools() {
    }
}