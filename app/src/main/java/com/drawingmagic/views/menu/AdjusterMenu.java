package com.drawingmagic.views.menu;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.drawingmagic.R;
import com.drawingmagic.eventbus.Event;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.SeekBarProgressChange;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;

/**
 * Project DrawingMagic.
 * Created by ihorkarpachev.
 * On 20/09/15 at 13:32.
 */
@EViewGroup(R.layout.view_adjuster_menu)
public class AdjusterMenu extends RelativeLayout {
    // TODO: 25/10/2015 Refactor names of methods
    @ViewById
    TextView tvTitle, tvSeekBarTitle;
    @ViewById
    SeekBar sBar;

    String fragmentTitle;

    String adjusterTitle;

    int progressMax;

    int currentProgress;

    /**
     * Event ID to be sent on progress changed (-1 by default)
     */
    int eventIdToBeSent = -1;

    public AdjusterMenu(Context context) {
        super(context);
    }

    /**
     * Set seek bar string title
     *
     * @param title string representation of title
     */
    public void setSeekBarAdjusterTitle(String title) {
        tvSeekBarTitle.setVisibility(View.VISIBLE);
        tvSeekBarTitle.setText(!TextUtils.isEmpty(title) ? title : "");
    }

    /**
     * Set fragment string title
     *
     * @param title string representation of title
     */
    public void setAdjusterTitle(String title) {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(!TextUtils.isEmpty(title) ? title : "");
    }

    public void setSeekBarCurrentAndMaxValues(int currentProgress, int max) {
        sBar.setMax(max);
        sBar.setProgress(currentProgress);
    }


    @SeekBarProgressChange
    void sBar(int progress, boolean fromUser) {
        EventBus.getDefault().post(new Event(eventIdToBeSent, progress));
    }


    public void setEventIdToBeSent(int eventIdToBeSent) {
        this.eventIdToBeSent = eventIdToBeSent;
    }
}
