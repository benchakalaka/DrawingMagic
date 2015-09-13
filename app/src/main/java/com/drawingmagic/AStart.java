package com.drawingmagic;

import android.animation.Animator;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EApplication;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_start)
public class AStart extends SuperActivity {

    @ViewById
    ShimmerTextView stv;

    @AfterViews void afterViews(){
        Iconify.with(new FontAwesomeModule());
        Shimmer shimmer = new Shimmer();

        shimmer.setRepeatCount(1)
                .setDuration(3000)
                .setStartDelay(500).
                setAnimatorListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // // TODO: 13/09/15 shift label to the top, and show control panel
                        startActivity(new Intent(AStart.this, ActivityGallery.class));
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
        ;
        shimmer.start(stv);
    }
}
