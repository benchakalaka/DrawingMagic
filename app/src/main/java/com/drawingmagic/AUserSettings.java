package com.drawingmagic;

import com.drawingmagic.eventbus.Event;

import org.androidannotations.annotations.EActivity;

/**
 * Project DrawingMagic,
 * Created by ihorkarpachev
 * On 13/09/15 at 17:47.
 */
@EActivity(R.layout.activity_user_settings)
public class AUserSettings extends SuperActivity {
    @Override
    public void onEventMainThread(Event event) {
    }
}
