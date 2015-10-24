package com.drawingmagic.fragments;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.drawingmagic.R;
import com.drawingmagic.eventbus.Event;
import com.drawingmagic.helpers.FrameProvider;
import com.drawingmagic.helpers.FrameResourceItemHolder;
import com.drawingmagic.utils.AnimationUtils;
import com.drawingmagic.views.ImageFramePreview_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.LinkedList;
import java.util.List;

import de.greenrobot.event.EventBus;

import static com.drawingmagic.eventbus.Event.FLIP;
import static com.drawingmagic.eventbus.Event.ON_ROTATE_TRANSFORMATION;
import static com.drawingmagic.eventbus.Event.ON_SKEW_TRANSFORMATION;
import static com.drawingmagic.utils.AnimationUtils.AnimationTechniques.FADE_IN;
import static com.drawingmagic.utils.GraphicUtils.FLIP_HORIZONTAL;
import static com.drawingmagic.utils.GraphicUtils.FLIP_VERTICAL;
import static com.drawingmagic.utils.GraphicUtils.MIRROR_HORIZONTAL;
import static com.drawingmagic.utils.GraphicUtils.MIRROR_VERTICAL;

/**
 * Project DrawingMagic.
 * Created by ihorkarpachev.
 * On 23/09/15 at 13:32.
 */
@EFragment(R.layout.fragment_canvas_transformer)
public class FCanvasTransformer extends Fragment {

    public static final List<FrameProvider> FRAMES_ARRAY = new LinkedList<>();
    @ViewById
    LinearLayout llFramesContainer;
    public void initFrames() {
        FRAMES_ARRAY.add(new FrameResourceItemHolder("Sweets", R.drawable.frame_choco));
        FRAMES_ARRAY.add(new FrameResourceItemHolder("Angel", R.drawable.frame_pink_angel));
        FRAMES_ARRAY.add(new FrameResourceItemHolder("Scratch", R.drawable.frame_semitransparent));
        FRAMES_ARRAY.add(new FrameResourceItemHolder("Winter", R.drawable.frame_winter));
        FRAMES_ARRAY.add(new FrameResourceItemHolder("Rose", R.drawable.frame_pink_rose));
        FRAMES_ARRAY.add(new FrameResourceItemHolder("Circle Fire", R.drawable.frame_fire_circle));
        FRAMES_ARRAY.add(new FrameProvider() {
            @Override
            public String getFrameName() {
                return "Blur Line";
            }

            @Override
            public Drawable getFramePreview(Resources resources) {
                return resources.getDrawable(R.drawable.ic_launcher);
            }

            @Override
            public Bitmap drawFrame(Paint paintOriginal, Resources resources, int width, int height) {
                Paint paint = new Paint();
                paint.setStrokeWidth(paintOriginal.getStrokeWidth());
                paint.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.NORMAL));
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(paintOriginal.getColor());
                Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
                Bitmap bmp = Bitmap.createBitmap(width, height, conf); // this creates a MUTABLE bitmap
                Canvas canvas = new Canvas();
                canvas.setBitmap(bmp);
                canvas.drawRect(0, 0, width, height, paint);
                return bmp;
            }
        });
        FRAMES_ARRAY.add(new FrameProvider() {
            @Override
            public String getFrameName() {
                return "Rectangle";
            }

            @Override
            public Drawable getFramePreview(Resources resources) {
                return resources.getDrawable(R.drawable.info);
            }

            @Override
            public Bitmap drawFrame(Paint paintOriginal, Resources resources, int width, int height) {
                Paint paint = new Paint();
                paint.setStrokeWidth(paintOriginal.getStrokeWidth());
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(paintOriginal.getColor());
                Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
                Bitmap bmp = Bitmap.createBitmap(width, height, conf); // this creates a MUTABLE bitmap
                Canvas canvas = new Canvas();
                canvas.setBitmap(bmp);
                canvas.drawRect(0, 0, width, height, paint);
                return bmp;
            }
        });
    }
    @AfterViews
    void afterViews() {
        initFrames();
        for(int i=0;i<FRAMES_ARRAY.size();i++) {
            llFramesContainer.addView(ImageFramePreview_.build(getActivity(), FRAMES_ARRAY.get(i)));
        }
    }

    @ViewById
    RelativeLayout rlRotate, rlSkew, rlFlipHorizontal, rlFlipVertical, rlMirrorHorizontal, rlMirrorVertical;

    public FCanvasTransformer() {
    }

    @Click
    void rlRotate() {
        AnimationUtils.animate(rlRotate, FADE_IN);
        EventBus.getDefault().post(new Event(ON_ROTATE_TRANSFORMATION));
    }

    @Click
    void rlSkew() {
        AnimationUtils.animate(rlSkew, FADE_IN);
        EventBus.getDefault().post(new Event(ON_SKEW_TRANSFORMATION));
    }

    @Click
    void rlFlipHorizontal() {
        AnimationUtils.animate(rlFlipHorizontal, FADE_IN);
        EventBus.getDefault().post(new Event(FLIP, FLIP_HORIZONTAL));
    }

    @Click
    void rlFlipVertical() {
        AnimationUtils.animate(rlFlipVertical, FADE_IN);
        EventBus.getDefault().post(new Event(FLIP, FLIP_VERTICAL));
    }

    @Click
    void rlMirrorHorizontal() {
        AnimationUtils.animate(rlMirrorHorizontal, FADE_IN);
        EventBus.getDefault().post(new Event(Event.MIRROR, MIRROR_HORIZONTAL));
    }

    @Click
    void rlMirrorVertical() {
        AnimationUtils.animate(rlMirrorVertical, FADE_IN);
        EventBus.getDefault().post(new Event(Event.MIRROR, MIRROR_VERTICAL));
    }

}
