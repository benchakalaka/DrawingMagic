package com.drawingmagic;

import android.animation.Animator;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidviewhover.BlurLayout;
import com.drawingmagic.fragments.FEffectsTools;
import com.drawingmagic.utils.Notification;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import static com.drawingmagic.utils.AnimationUtils.AnimationTechniques;
import static com.drawingmagic.utils.AnimationUtils.animate;

@EActivity(R.layout.activity_start)
public class AStart extends SuperActivity {

    @ViewById
    ShimmerTextView stv;

    @ViewById
    Button bGallery, bCamera, bDrawingMagic;

    @ViewById
    BlurLayout mSampleLayout, mSampleLayout2, mSampleLayout3, mSampleLayout4;

    @Click
    void bGallery() {
        startActivity(new Intent(this, FEffectsTools.class));
    }

    @Click
    void bDrawingMagic() {
        ADrawingMagic_.intent(this).start();
    }

    @Click
    void bCamera() {
        ActivityCamera_.intent(this).start();
    }

    @AfterViews void afterViews(){
        Shimmer shimmer = new Shimmer();

        shimmer.setRepeatCount(1)
                .setDuration(3000)
                .setStartDelay(500).
                setAnimatorListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        ADrawingMagic_.intent(AStart.this).start();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // // TODO: 13/09/15 shift label to the top, and show control panel

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

        shimmer.start(stv);

        BlurLayout.setGlobalDefaultDuration(450);

        View hover = LayoutInflater.from(this).inflate(R.layout.hover_sample, null);
        View hover2 = LayoutInflater.from(this).inflate(R.layout.hover_sample2, null);
        View hover3 = LayoutInflater.from(this).inflate(R.layout.hover_sample3, null);
        View hover4 = LayoutInflater.from(this).inflate(R.layout.hover_sample4, null);

        hover.findViewById(R.id.heart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate(v, AnimationTechniques.TADA);
            }
        });


        hover.findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate(v, AnimationTechniques.SWING);
            }
        });

        //sample 2

        hover2.findViewById(R.id.description).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notification.showSuccess(AStart.this, "Pretty Cool, Right?");
            }
        });


        hover4.findViewById(R.id.cat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notification.showSuccess(AStart.this, "hover4 Cool, Right?");
            }
        });

        hover4.findViewById(R.id.mail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mSampleLayout.setHoverView(hover);
        mSampleLayout2.setHoverView(hover2);
        mSampleLayout3.setHoverView(hover3);
        mSampleLayout4.setHoverView(hover4);

        mSampleLayout.setBlurDuration(550);
        mSampleLayout.addChildAppearAnimator(hover, R.id.heart, Techniques.FlipInX, 550, 0);
        mSampleLayout.addChildAppearAnimator(hover, R.id.share, Techniques.FlipInX, 550, 250);
        mSampleLayout.addChildAppearAnimator(hover, R.id.more, Techniques.FlipInX, 550, 500);

        mSampleLayout.addChildDisappearAnimator(hover, R.id.heart, Techniques.FlipOutX, 550, 500);
        mSampleLayout.addChildDisappearAnimator(hover, R.id.share, Techniques.FlipOutX, 550, 250);
        mSampleLayout.addChildDisappearAnimator(hover, R.id.more, Techniques.FlipOutX, 550, 0);

        mSampleLayout.addChildAppearAnimator(hover, R.id.description, Techniques.FadeInUp);
        mSampleLayout.addChildDisappearAnimator(hover, R.id.description, Techniques.FadeOutDown);

        mSampleLayout2.addChildAppearAnimator(hover2, R.id.description, Techniques.FadeInUp);
        mSampleLayout2.addChildDisappearAnimator(hover2, R.id.description, Techniques.FadeOutDown);
        mSampleLayout2.addChildAppearAnimator(hover2, R.id.image, Techniques.DropOut, 1200);
        mSampleLayout2.addChildDisappearAnimator(hover2, R.id.image, Techniques.FadeOutUp);
        mSampleLayout2.setBlurDuration(1000);

        //sample3
        mSampleLayout3.addChildAppearAnimator(hover3, R.id.eye, Techniques.Landing);
        mSampleLayout3.addChildDisappearAnimator(hover3, R.id.eye, Techniques.TakingOff);
        mSampleLayout3.enableZoomBackground(true);
        mSampleLayout3.setBlurDuration(1200);

        //sample 4
        mSampleLayout4.addChildAppearAnimator(hover4, R.id.cat, Techniques.SlideInLeft);
        mSampleLayout4.addChildAppearAnimator(hover4, R.id.mail, Techniques.SlideInRight);

        mSampleLayout4.addChildDisappearAnimator(hover4, R.id.cat, Techniques.SlideOutLeft);
        mSampleLayout4.addChildDisappearAnimator(hover4, R.id.mail, Techniques.SlideOutRight);

        mSampleLayout4.addChildAppearAnimator(hover4, R.id.content, Techniques.BounceIn);
        mSampleLayout4.addChildDisappearAnimator(hover4, R.id.content, Techniques.FadeOutUp);

    }
}
