package com.drawingmagic.fragments;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.drawingmagic.R;
import com.drawingmagic.eventbus.Event;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.SeekBarProgressChange;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;

import static com.drawingmagic.eventbus.Event.ON_ADJUSTER_VALUE_CHANGED;

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

    /**
     * Set seek bar string title
     *
     * @param title string representation of title
     */
    public void setAdjusterTitle(String title) {
        tvSeekBarTitle.setVisibility(View.VISIBLE);
        tvSeekBarTitle.setText(!TextUtils.isEmpty(title) ? title : "");
    }

    /**
     * Set fragment string title
     *
     * @param title string representation of title
     */
    public void setFragmentTitle(String title) {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(!TextUtils.isEmpty(title) ? title : "");
    }

    public void setSeekBarCurrentMinMaxValues(int max, int value) {
        sBar.setMax(max);
        sBar.setProgress(value);
    }

    @SeekBarProgressChange
    void sBar(int progress) {
        EventBus.getDefault().post(new Event(ON_ADJUSTER_VALUE_CHANGED, progress));
    }
}
