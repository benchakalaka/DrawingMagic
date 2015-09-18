package com.drawingmagic.views;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.drawingmagic.R;
import com.drawingmagic.helpers.FilterItemHolder;

import net.steamcrafted.materialiconlib.MaterialIconView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import static com.drawingmagic.fragments.FEffectsTools.OnChangeEffectListener;
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

    private OnChangeEffectListener changeEffectListener;

    public ImageFilterPreview(Context context, FilterItemHolder filterItem, OnChangeEffectListener changeEffectListener) {
        super(context);
        this.filterDescriptor = filterItem;
        this.changeEffectListener = changeEffectListener;
    }

    public void setUpView(FilterItemHolder filterItem, OnChangeEffectListener changeEffectListener) {
        this.filterDescriptor = filterItem;
        this.changeEffectListener = changeEffectListener;
        afterViews();
    }


    @AfterViews
    void afterViews() {
        tvDescription.setText(filterDescriptor.filterName);
        mivImage.setIcon(IconValue.XBOX_CONTROLLER);
        //Log.e("Create preview for Filter : " + filterDescriptor.filterName);
    }

    public FilterItemHolder getFilter() {
        return this.filterDescriptor;
    }

    @Click
    void rlRoot() {
        changeEffectListener.onNewFilterSelected(this.filterDescriptor);
    }
}
