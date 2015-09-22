package com.drawingmagic.views;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.drawingmagic.R;
import com.drawingmagic.eventbus.Event;
import com.drawingmagic.helpers.FilterItemHolder;
import com.drawingmagic.utils.AnimationUtils;
import com.drawingmagic.utils.Log;

import net.steamcrafted.materialiconlib.MaterialIconView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;

import static net.steamcrafted.materialiconlib.MaterialDrawableBuilder.IconValue;

/**
 * Project DrawingMagic.
 * Created by ihorkarpachev.
 * On 15/09/15 at 19:46.
 */
@EViewGroup(R.layout.image_filter_preview)
public class ImageFilterPreview extends RelativeLayout {

    private FilterItemHolder filterDescriptor;

    @ViewById
    TextView tvDescription;

    @ViewById
    MaterialIconView mivImage;

    @ViewById
    RelativeLayout rlRoot;


    public ImageFilterPreview(Context context, FilterItemHolder filterItem) {
        super(context);
        this.filterDescriptor = filterItem;
    }

    public void setUpView(FilterItemHolder filterItem) {
        this.filterDescriptor = filterItem;
        afterViews();
    }


    @AfterViews
    void afterViews() {
        tvDescription.setText(filterDescriptor.getFilterName());
        mivImage.setIcon(IconValue.XBOX_CONTROLLER);
        Log.e("Create preview for Filter : " + filterDescriptor.getFilterName());
    }

    public FilterItemHolder getFilter() {
        return this.filterDescriptor;
    }

    @Click
    void rlRoot() {
        AnimationUtils.animate(rlRoot, AnimationUtils.AnimationTechniques.ZOOM_IN);
        EventBus.getDefault().post(new Event(Event.ON_APPLY_EFFECT, filterDescriptor));
    }
}
