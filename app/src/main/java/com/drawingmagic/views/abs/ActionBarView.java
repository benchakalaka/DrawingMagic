package com.drawingmagic.views.abs;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.drawingmagic.R;
import com.drawingmagic.eventbus.Event;

import net.steamcrafted.materialiconlib.MaterialIconView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;

/**
 * Created by ihor.karpachev on 22/09/2015.
 * Project: DrawingMagic
 * Package: com.drawingmagic.views.abs
 * Datascope Systems Ltd.
 */
@EViewGroup(R.layout.abs)
public class ActionBarView extends RelativeLayout {

    @ViewById
    MaterialIconView ivMenu;

    @ViewById
    TextView tvTitle;

    @ViewById
    FrameLayout flRightMenuHolder;

    public ActionBarView(Context context) {
        super(context);
    }

    /**
     * Set ABS title, or app_name value from resources if string is null or empty
     *
     * @param title string representation of title to be set
     * @return ABS instance
     */
    public ActionBarView withTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
        return this;
    }

    /**
     * Set visibility of menu button to View.VISIBLE
     * Activity can accept on Event.ON_ABS_MENU_CLICKED event
     *
     * @return ABS instance
     */
    public ActionBarView withMenuButton() {
        ivMenu.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * Add menu to right panel
     *
     * @param rightMenu view to be added
     * @return ABS instance
     */
    public ActionBarView withRightMenu(View rightMenu) {
        flRightMenuHolder.setVisibility(View.VISIBLE);
        flRightMenuHolder.addView(rightMenu);
        return this;
    }

    @Click
    void ivMenu() {
        EventBus.getDefault().post(new Event(Event.ON_ABS_MENU_CLICKED));
    }
}
