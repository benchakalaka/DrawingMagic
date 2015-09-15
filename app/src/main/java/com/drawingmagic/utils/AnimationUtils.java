/*
 * Created by Ihor Karpachev, Copyright (c) 2015. .
 */

package com.drawingmagic.utils;

import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;

/**
 * Created by ihor.karpachev on 22/05/2015.
 * Project: Installer Application
 * Package: com.touchip.organizer.utils
 * Datascope Systems Ltd.
 */
public class AnimationUtils {

    /**
     * Default duration of animations
     */
    private static int SLOW = 1000, NORMAL = 500, FAST = 300, VERY_FAST = 150;

    /**
     * Animation techniques
     */
    public enum AnimationTechniques {
        FADE_IN, FADE_OUT, PULSE, DROP_OUT, LANDING, TAKING_OFF, FLASH, RUBBER_BAND, SHAKE, SWING, WOBBLE,
        BOUNCE, TADA, STAND_UP, WAVE, HINGE, ROLL_IN, ROLL_OUT, BOUNCE_IN, BOUNCE_IN_DOWN, BOUNCE_IN_LEFT, BOUNCE_IN_RIGHT, BOUNCE_IN_UP,
        FADE_IN_UP, FADE_IN_DOWN, FADE_IN_LEFT, FADE_IN_RIGHT, FADE_OUT_UP, FADE_OUT_DOWN, FADE_OUT_LEFT, FADE_OUT_RIGHT, FLIP_IN_X, FLIP_OUT_X,
        FLIP_OUT_Y, ROTATE_IN, ROTATE_IN_DOWN_LEFT, ROTATE_IN_DOWN_RIGHT, SLIDE_IN_LEFT, SLIDE_IN_RIGHT, SLIDE_IN_UP, SLIDE_IN_DOWN, SLIDE_OUT_LEFT,
        SLIDE_OUT_RIGHT, SLIDE_OUT_UP, SLIDE_OUT_DOWN, ROTATE_IN_UP_LEFT, ROTATE_IN_UP_RIGHT, ROTATE_OUT, ROTATE_OUT_DOWN_LEFT, ROTATE_OUT_DOWN_RIGHT,
        ROTATE_OUT_UP_LEFT, ROTATE_OUT_UP_RIGHT, ZOOM_IN, ZOOM_IN_DOWN, ZOOM_IN_LEFT, ZOOM_IN_RIGHT, ZOOM_IN_UP, ZOOM_OUT, ZOOM_OUT_DOWN, ZOOM_OUT_LEFT, ZOOM_OUT_RIGHT, ZOOM_OUT_UP
    }

    /**
     * Play animation on NORMAL speed
     *
     * @param target              view to play animation on
     * @param animationTechniques which techniques to use
     */
    public static void animate(View target, AnimationTechniques animationTechniques) {
        YoYo.with(getActualTechniques(animationTechniques)).duration(NORMAL).playOn(target);
    }


    /**
     * Play animation on NORMAL speed and make view as GONE after animation is finished
     *
     * @param target              view to play animation on
     * @param animationTechniques which techniques to use
     */
    public static void animateAndGone(final View target, AnimationTechniques animationTechniques) {
        Animator.AnimatorListener listener = new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                target.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
        YoYo.with(getActualTechniques(animationTechniques)).withListener(listener).duration(NORMAL).playOn(target);
    }

    /**
     * Play animation with listener
     *
     * @param target              play on this view
     * @param animationTechniques which techniques to use
     * @param listener            listener
     */
    public static void animate(View target, AnimationTechniques animationTechniques, com.nineoldandroids.animation.Animator.AnimatorListener listener) {
        YoYo.with(getActualTechniques(animationTechniques)).duration(NORMAL).withListener(listener).playOn(target);
    }

    /**
     * Play animation on FAST speed
     *
     * @param target              view to play animation on
     * @param animationTechniques which techniques to use
     */
    public static void animateFast(View target, AnimationTechniques animationTechniques) {
        YoYo.with(getActualTechniques(animationTechniques)).duration(FAST).playOn(target);
    }

    /**
     * Play animation on VERY FAST speed
     *
     * @param target              view to play animation on
     * @param animationTechniques which techniques to use
     */
    public static void animateVeryFast(View target, AnimationTechniques animationTechniques) {
        YoYo.with(getActualTechniques(animationTechniques)).duration(VERY_FAST).playOn(target);
    }

    /**
     * Play animation on SLOW speed
     *
     * @param target              view to play animation on
     * @param animationTechniques which techniques to use
     */
    public static void animateSlow(View target, AnimationTechniques animationTechniques) {
        YoYo.with(getActualTechniques(animationTechniques)).duration(SLOW).playOn(target);
    }

    /**
     * Play animation on SLOW speed with specific listener
     *
     * @param target              view to play animation on
     * @param animationTechniques which techniques to use
     * @param listener            listener
     */
    public static void animateSlow(View target, AnimationTechniques animationTechniques, com.nineoldandroids.animation.Animator.AnimatorListener listener) {
        YoYo.with(getActualTechniques(animationTechniques)).duration(SLOW).withListener(listener).playOn(target);
    }

    /**
     * Play animation on specific duration
     *
     * @param target              view to play animation on
     * @param animationTechniques which techniques to use
     */
    public static void animate(View target, AnimationTechniques animationTechniques, int duration) {
        YoYo.with(getActualTechniques(animationTechniques)).duration(duration).playOn(target);
    }


    /**
     * Return animation by AnimationTechniques
     *
     * @param animationTechniques animation to find
     * @return actual animation resource
     */
    private static Techniques getActualTechniques(AnimationTechniques animationTechniques) {
        Techniques techniques;
        switch (animationTechniques) {

            case FADE_OUT:
                techniques = Techniques.FadeOut;
                break;

            case PULSE:
                techniques = Techniques.Pulse;
                break;

            case DROP_OUT:
                techniques = Techniques.DropOut;
                break;

            case LANDING:
                techniques = Techniques.Landing;
                break;

            case TAKING_OFF:
                techniques = Techniques.TakingOff;
                break;

            case FLASH:
                techniques = Techniques.Flash;
                break;

            case RUBBER_BAND:
                techniques = Techniques.RubberBand;
                break;

            case SHAKE:
                techniques = Techniques.Shake;
                break;

            case SWING:
                techniques = Techniques.Swing;
                break;

            case WOBBLE:
                techniques = Techniques.Wobble;
                break;

            case BOUNCE:
                techniques = Techniques.Bounce;
                break;

            case TADA:
                techniques = Techniques.Tada;
                break;

            case STAND_UP:
                techniques = Techniques.StandUp;
                break;

            case WAVE:
                techniques = Techniques.Wave;
                break;

            case HINGE:
                techniques = Techniques.Hinge;
                break;

            case ROLL_IN:
                techniques = Techniques.RollIn;
                break;

            case ROLL_OUT:
                techniques = Techniques.RollOut;
                break;

            case BOUNCE_IN:
                techniques = Techniques.BounceIn;
                break;

            case BOUNCE_IN_DOWN:
                techniques = Techniques.BounceInDown;
                break;

            case BOUNCE_IN_LEFT:
                techniques = Techniques.BounceInLeft;
                break;

            case BOUNCE_IN_RIGHT:
                techniques = Techniques.BounceInRight;
                break;

            case BOUNCE_IN_UP:
                techniques = Techniques.BounceInUp;
                break;

            case FADE_IN_UP:
                techniques = Techniques.FadeInUp;
                break;

            case FADE_IN_DOWN:
                techniques = Techniques.FadeInDown;
                break;

            case FADE_IN_LEFT:
                techniques = Techniques.FadeInLeft;
                break;

            case FADE_IN_RIGHT:
                techniques = Techniques.FadeInRight;
                break;

            case FADE_OUT_UP:
                techniques = Techniques.FadeOutRight;
                break;

            case FADE_OUT_DOWN:
                techniques = Techniques.FadeOutDown;
                break;

            case FADE_OUT_LEFT:
                techniques = Techniques.FadeOutLeft;
                break;

            case FADE_OUT_RIGHT:
                techniques = Techniques.FadeOutRight;
                break;

            case FLIP_IN_X:
                techniques = Techniques.FlipInX;
                break;

            case FLIP_OUT_X:
                techniques = Techniques.FlipOutX;
                break;

            case FLIP_OUT_Y:
                techniques = Techniques.FlipOutY;
                break;

            case ROTATE_IN:
                techniques = Techniques.RotateIn;
                break;

            case ROTATE_IN_DOWN_LEFT:
                techniques = Techniques.RotateInDownLeft;
                break;

            case ROTATE_IN_DOWN_RIGHT:
                techniques = Techniques.RotateInDownRight;
                break;

            case SLIDE_IN_LEFT:
                techniques = Techniques.SlideInLeft;
                break;

            case SLIDE_IN_RIGHT:
                techniques = Techniques.SlideInRight;
                break;

            case SLIDE_IN_UP:
                techniques = Techniques.SlideInUp;
                break;

            case SLIDE_IN_DOWN:
                techniques = Techniques.SlideInDown;
                break;

            case SLIDE_OUT_LEFT:
                techniques = Techniques.SlideOutLeft;
                break;

            case SLIDE_OUT_RIGHT:
                techniques = Techniques.SlideOutRight;
                break;

            case SLIDE_OUT_UP:
                techniques = Techniques.SlideOutUp;
                break;

            case SLIDE_OUT_DOWN:
                techniques = Techniques.SlideOutDown;
                break;

            case ROTATE_IN_UP_LEFT:
                techniques = Techniques.RotateInUpLeft;
                break;

            case ROTATE_IN_UP_RIGHT:
                techniques = Techniques.RotateInUpRight;
                break;

            case ROTATE_OUT:
                techniques = Techniques.RotateOut;
                break;

            case ROTATE_OUT_DOWN_LEFT:
                techniques = Techniques.RotateOutDownLeft;
                break;

            case ROTATE_OUT_DOWN_RIGHT:
                techniques = Techniques.RotateOutDownRight;
                break;

            case ROTATE_OUT_UP_LEFT:
                techniques = Techniques.RotateOutUpLeft;
                break;

            case ROTATE_OUT_UP_RIGHT:
                techniques = Techniques.RotateOutUpRight;
                break;

            case ZOOM_IN:
                techniques = Techniques.ZoomIn;
                break;

            case ZOOM_IN_DOWN:
                techniques = Techniques.ZoomInDown;
                break;

            case ZOOM_IN_LEFT:
                techniques = Techniques.ZoomInLeft;
                break;

            case ZOOM_IN_RIGHT:
                techniques = Techniques.ZoomInRight;
                break;

            case ZOOM_IN_UP:
                techniques = Techniques.ZoomInUp;
                break;

            case ZOOM_OUT:
                techniques = Techniques.ZoomOut;
                break;

            case ZOOM_OUT_DOWN:
                techniques = Techniques.ZoomOut;
                break;

            case ZOOM_OUT_LEFT:
                techniques = Techniques.ZoomOutLeft;
                break;

            case ZOOM_OUT_RIGHT:
                techniques = Techniques.ZoomOutRight;
                break;

            case ZOOM_OUT_UP:
                techniques = Techniques.ZoomOutUp;
                break;

            default:
                techniques = Techniques.FadeIn;
        }
        return techniques;
    }
}
