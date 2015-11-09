package com.drawingmagic.views.menu;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.drawingmagic.R;
import com.drawingmagic.eventbus.Event;
import com.drawingmagic.utils.AnimationUtils;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.Touch;

import de.greenrobot.event.EventBus;

import static com.drawingmagic.utils.AnimationUtils.AnimationTechniques.ZOOM_IN;

@EViewGroup(R.layout.view_menu_cropper)
public class CropperMenu extends LinearLayout {

    public CropperMenu(Context context) {
        super(context);
    }

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
