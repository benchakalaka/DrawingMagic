package com.drawingmagic;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

/**
 * Project DrawingMagic.
 * Created by ihorkarpachev.
 * On 13/09/15 at 18:11.
 */
public abstract class SuperActivity extends SherlockFragmentActivity {

    @Override
    protected void onResume() {
        Iconify.with(new FontAwesomeModule());
        super.onResume();
    }
}
