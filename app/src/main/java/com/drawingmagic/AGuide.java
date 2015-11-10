package com.drawingmagic;

import android.content.res.TypedArray;
import android.widget.ImageView;
import android.widget.TextView;

import com.drawingmagic.eventbus.Event;
import com.drawingmagic.utils.AnimationUtils;
import com.drawingmagic.utils.Logger;
import com.drawingmagic.utils.Utils;
import com.drawingmagic.views.abs.ActionBarView_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;
import org.androidannotations.annotations.res.StringRes;

import static com.drawingmagic.utils.AnimationUtils.AnimationTechniques.BOUNCE_IN;
import static com.drawingmagic.utils.AnimationUtils.AnimationTechniques.FADE_IN;

/**
 * Created by ihor.karpachev on 10/11/2015.
 * Project: DrawingMagic
 * Package: com.drawingmagic
 * Datascope Systems Ltd.
 */
@EActivity(R.layout.activity_guide)
public class AGuide extends SuperActivity {

    @ViewById
    TextView tvGuideSteps, tvNext;

    @ViewById
    ImageView ivGuideSteps;

    @StringArrayRes(R.array.guide_array)
    String[] guideSteps;

    @StringRes(R.string.got_it)
    String gotIt;

    // current guide step index
    private int currentIndex = 0;
    // Array of guide images resources
    private TypedArray guideImages;

    @AfterViews
    void afterViews() {
        guideImages = getResources().obtainTypedArray(R.array.guide_images);
        Utils.configureActionBar(ActionBarView_.build(this));
        setNextGuideStep(currentIndex);
    }

    @Click
    void tvNext() {
        AnimationUtils.animate(tvNext, FADE_IN);
        setNextGuideStep(++currentIndex);
    }

    /**
     * Set next step guide
     *
     * @param nextGuideStepIndex index of next step
     */
    private void setNextGuideStep(int nextGuideStepIndex) {
        AnimationUtils.animate(ivGuideSteps, BOUNCE_IN);
        // Is it LAST step, Start application
        if (currentIndex == guideSteps.length - 1) {
            // TODO: 10/11/2015  Show guide only once, GotIt Clicked
            AStart_.intent(this).start();
            return;
        }

        // set next step of guide
        tvGuideSteps.setText(guideSteps[nextGuideStepIndex]);
        ivGuideSteps.setImageResource(guideImages.getResourceId(currentIndex, R.drawable.kid));

        // if this is last step, change text ot GotIt!;
        if (currentIndex == (guideSteps.length - 1)) {
            tvNext.setText(gotIt);
        }
    }

    @Override
    public void onEventMainThread(Event event) {
        Logger.e(event);
    }


}
