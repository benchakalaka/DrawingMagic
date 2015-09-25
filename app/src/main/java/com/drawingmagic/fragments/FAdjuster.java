package com.drawingmagic.fragments;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.drawingmagic.R;
import com.drawingmagic.eventbus.Event;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.SeekBarProgressChange;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;

/**
 * Project DrawingMagic.
 * Created by ihorkarpachev.
 * On 20/09/15 at 13:32.
 */
@EFragment(R.layout.fragment_adjuster)
public class FAdjuster extends Fragment {

    @ViewById
    TextView tvTitle, tvSeekBarTitle;
    @ViewById
    SeekBar sBar;

    @FragmentArg
    String fragmentTitle;

    @FragmentArg
    String adjusterTitle;

    @FragmentArg
    int progressMax;

    @FragmentArg
    int currentProgress;

    /**
     * Event ID to be sent on progress changed
     */
    @FragmentArg
    int eventId;


    @AfterViews void afterViews(){
        setAdjusterTitle(adjusterTitle);
        setFragmentTitle(fragmentTitle);
        setSeekBarCurrentMinMaxValues(progressMax, currentProgress);
    }

    /**
     * Set seek bar string title
     *
     * @param title string representation of title
     */
    private void setAdjusterTitle(String title) {
        tvSeekBarTitle.setVisibility(View.VISIBLE);
        tvSeekBarTitle.setText(!TextUtils.isEmpty(title) ? title : "");
    }

    /**
     * Set fragment string title
     *
     * @param title string representation of title
     */
    private void setFragmentTitle(String title) {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(!TextUtils.isEmpty(title) ? title : "");
    }

    private void setSeekBarCurrentMinMaxValues(int max, int value) {
        sBar.setMax(max);
        sBar.setProgress(value);
    }

    @SeekBarProgressChange
    void sBar(int progress) {
        EventBus.getDefault().post(new Event(eventId, progress));
    }
}
