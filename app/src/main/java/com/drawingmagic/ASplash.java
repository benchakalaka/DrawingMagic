package com.drawingmagic;

import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

/**
 * Created by Molochko Oleksandr on 10/19/15.
 * Package com.drawingmagic
 * Project DrawingMagic
 * Copyright (c) 2013-2015 Datascope Ltd. All Rights Reserved.
 */
public class ASplash extends AwesomeSplash {

    private final int TITLE_TEXT_SIZE = 30;
    private final int ANIMATION_DURATION = 300;
    private final int ANIMATION_REVEAL_DURATION = 1000;


    @Override
    public void animationsFinished() {
        AStart_.intent(this).start();
    }

    @Override
    public void initSplash(ConfigSplash configSplash) {
        //Customize Circular Reveal
        configSplash.setTitleSplash(getResources().getString(R.string.splash_screen_title));
        configSplash.setTitleTextColor(R.color.white);
        configSplash.setTitleTextSize(TITLE_TEXT_SIZE);
        configSplash.setAnimTitleDuration(ANIMATION_DURATION);
        configSplash.setAnimTitleTechnique(Techniques.FadeInUp);
        configSplash.setBackgroundColor(R.color.default_background_colour);
        configSplash.setAnimCircularRevealDuration(ANIMATION_REVEAL_DURATION);
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM);
    }
}
