package com.drawingmagic.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.drawingmagic.R;
import com.drawingmagic.core.DrawingView;
import com.drawingmagic.eventbus.Event;
import com.drawingmagic.utils.AnimationUtils;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;

import de.greenrobot.event.EventBus;

/**
 * Created by Molochko Oleksandr on 10/22/15.
 * Package com.drawingmagic.views
 * Project DrawingMagic
 * Copyright (c) 2013-2015 Datascope Ltd. All Rights Reserved.
 */
@EViewGroup(R.layout.view_menu_drawing_tools_top)
public class ViewMenuTop extends RelativeLayout {
    @ViewById
    MaterialIconView mivGridState;
    @StringArrayRes(R.array.grid_state_icons)
    public static String[] GRID_STATE_ICONS;
    private int currentStatePosition = 0;

    public ViewMenuTop(Context context) {
        super(context);
    }

    public ViewMenuTop(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewMenuTop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @AfterViews
    void afterViews() {
        this.currentStatePosition = 0;
        this.mivGridState.setIcon(MaterialDrawableBuilder.IconValue.valueOf(GRID_STATE_ICONS[currentStatePosition++].toUpperCase()));
    }

    @Click
    void mivGridState(View view) {
        MaterialIconView iconView = (MaterialIconView) view;
        if (currentStatePosition >= GRID_STATE_ICONS.length) {
            currentStatePosition = 0;
        }
        EventBus.getDefault().post(new Event(Event.ON_GRID_TYPE_CHANGED, DrawingView.GridType.GRID_TYPES[currentStatePosition]));
        iconView.setIcon(MaterialDrawableBuilder.IconValue.valueOf(GRID_STATE_ICONS[currentStatePosition++].toUpperCase()));
        playAnimationOnView(view);
    }

    private void playAnimationOnView(View target) {
        AnimationUtils.animate(target, AnimationUtils.AnimationTechniques.ZOOM_IN);
    }
}
