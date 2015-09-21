package com.drawingmagic;

import android.animation.Animator;
import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidviewhover.BlurLayout;
import com.drawingmagic.eventbus.Event;
import com.drawingmagic.views.HoverView_;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import static com.drawingmagic.views.HoverView.HooverMenuClickListener;

@EActivity(R.layout.activity_start)
public class AStart extends SuperActivity implements HooverMenuClickListener {

    @ViewById
    ShimmerTextView stv;

    @ViewById
    BlurLayout blProfile;


    @AfterViews
    void afterViews() {
        Shimmer shimmer = new Shimmer();
        BlurLayout.setGlobalDefaultDuration(350);

        shimmer.setDuration(2000)
                .setStartDelay(1000).
                setAnimatorListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        //  ADrawingMagic_.intent(AStart.this).start();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

        shimmer.start(stv);


        View hoverUserProfile = HoverView_.build(this);

        blProfile.setHoverView(hoverUserProfile);

        blProfile.setBlurDuration(500);

        blProfile.addChildAppearAnimator(hoverUserProfile, R.id.mivGallery, Techniques.SlideInRight);
        blProfile.addChildAppearAnimator(hoverUserProfile, R.id.mivTakeCameraPicture, Techniques.BounceIn);
        blProfile.addChildAppearAnimator(hoverUserProfile, R.id.mivEmptyCanvas, Techniques.SlideInLeft);

        blProfile.addChildDisappearAnimator(hoverUserProfile, R.id.mivGallery, Techniques.SlideOutRight);
        blProfile.addChildDisappearAnimator(hoverUserProfile, R.id.mivTakeCameraPicture, Techniques.SlideOutDown);
        blProfile.addChildDisappearAnimator(hoverUserProfile, R.id.mivEmptyCanvas, Techniques.SlideOutLeft);

        blProfile.addChildAppearAnimator(hoverUserProfile, R.id.content, Techniques.BounceIn);
        blProfile.addChildDisappearAnimator(hoverUserProfile, R.id.content, Techniques.FadeOutUp);

        blProfile.enableZoomBackground(true);

    }

    @Override
    public void onEventMainThread(Event event) {

    }

    @Override
    public void onMenuItemClick(int menuIdemId) {
        ADrawingMagic_.intent(AStart.this).selectedMenuItem(menuIdemId).start();
    }
}
