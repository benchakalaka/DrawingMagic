package com.drawingmagic.views;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.drawingmagic.R;
import com.drawingmagic.eventbus.Event;
import com.drawingmagic.helpers.FrameProvider;
import com.drawingmagic.utils.AnimationUtils;
import com.drawingmagic.utils.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;

/**
 * Project DrawingMagic.
 * Created by ihorkarpachev.
 * On 15/09/15 at 19:46.
 */
@EViewGroup(R.layout.image_frame_preview)
public class ImageFramePreview extends RelativeLayout {

    private FrameProvider frameInfo;

    @ViewById
    TextView tvDescription;

    @ViewById
    ImageView ivFrameIcon;

    @ViewById
    RelativeLayout rlRoot;


    public ImageFramePreview(Context context, FrameProvider frameItemHolder) {
        super(context);
        this.frameInfo = frameItemHolder;
    }


    @AfterViews
    void afterViews() {
        tvDescription.setText(frameInfo.getFrameName());
        ivFrameIcon.setImageDrawable(frameInfo.getFramePreview(getResources()));
        Logger.e("Create preview for Filter : " + frameInfo.getFrameName());
    }

    public FrameProvider getFrameInfo() {
        return this.frameInfo;
    }

    @Click
    void rlRoot() {
        AnimationUtils.animate(rlRoot, AnimationUtils.AnimationTechniques.ZOOM_IN);
        EventBus.getDefault().post(new Event(Event.ON_APPLY_FRAME, frameInfo));
    }
}
