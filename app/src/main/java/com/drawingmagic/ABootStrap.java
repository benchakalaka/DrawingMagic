package com.drawingmagic;

import android.content.Intent;

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
public class ABootStrap extends AwesomeSplash {
    @Override
    public void animationsFinished() {
        startActivity(new Intent(this,AStart_.class));
    }

    @Override
    public void initSplash(ConfigSplash configSplash) {
          /* you don't have to override every property */

        //Customize Circular Reveal
        configSplash.setTitleSplash("Drawing Magic, be magician in art !");
        configSplash.setTitleTextColor(R.color.white);
        configSplash.setTitleTextSize(30f); //float value
        configSplash.setAnimTitleDuration(3000);
        configSplash.setAnimTitleTechnique(Techniques.FadeInUp);
        configSplash.setBackgroundColor(R.color.vk_share_blue_color); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(2000); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

    }
}
