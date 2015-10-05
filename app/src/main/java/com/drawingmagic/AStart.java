package com.drawingmagic;

import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidviewhover.BlurLayout;
import com.drawingmagic.eventbus.Event;
import com.drawingmagic.utils.Utils;
import com.drawingmagic.views.HoverView_;
import com.drawingmagic.views.abs.ABS_;
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

    private static final int DEFAULT_GLOBAL_BLUR_DURATION = 500;
    private static final int DEFAULT_SHIMMER_DURATION = 1500;

    private Shimmer shimmer = new Shimmer();

    @AfterViews
    void afterViews() {
        Utils.configureCustomActionBar(getActionBar(), ABS_.build(this));

        BlurLayout.setGlobalDefaultDuration(DEFAULT_GLOBAL_BLUR_DURATION);

        shimmer.setDuration(DEFAULT_SHIMMER_DURATION);
        shimmer.start(stv);

        View hoverUserProfile = HoverView_.build(this);

        blProfile.setHoverView(hoverUserProfile);

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
