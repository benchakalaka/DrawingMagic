package com.drawingmagic;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.drawingmagic.eventbus.Event;

import de.greenrobot.event.EventBus;

/**
 * Project DrawingMagic.
 * Created by ihorkarpachev.
 * On 13/09/15 at 18:11.
 */
public abstract class SuperActivity extends SherlockFragmentActivity {


    /**
     * Start manager on activity start
     */
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    public abstract void onEventMainThread(Event event);
}
