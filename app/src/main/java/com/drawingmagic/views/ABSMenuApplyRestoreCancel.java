package com.drawingmagic.views;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.drawingmagic.R;
import com.drawingmagic.eventbus.Event;
import com.drawingmagic.utils.AnimationUtils;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;

import static com.drawingmagic.utils.AnimationUtils.AnimationTechniques;

/**
 * Created by ihor.karpachev on 16/09/2015.
 * Project: DrawingMagic
 * Package: com.drawingmagic.views
 * Datascope Systems Ltd.
 */
@EViewGroup(R.layout.abs_menu_apply_restore_cancel)
public class ABSMenuApplyRestoreCancel extends RelativeLayout {


    @ViewById
    LinearLayout llApply, llRestore, llCancel;


    public ABSMenuApplyRestoreCancel(Context context) {
        super(context);
    }

    @Click
    void llApply() {
        AnimationUtils.animate(llApply, AnimationTechniques.FADE_IN);
        EventBus.getDefault().post(new Event(Event.ON_ABS_MENU_APPLY));
    }

    @Click
    void llRestore() {
        AnimationUtils.animate(llRestore, AnimationTechniques.FADE_IN);
        EventBus.getDefault().post(new Event(Event.ON_ABS_MENU_RESTORE));
    }

    @Click
    void llCancel() {
        AnimationUtils.animate(llCancel, AnimationTechniques.FADE_IN);
        EventBus.getDefault().post(new Event(Event.ON_ABS_MENU_CANCEL));
    }
}