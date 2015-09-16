/*
 * Copyright (C) 2012 CyberAgent
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.drawingmagic.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.drawingmagic.ADrawingMagic;
import com.drawingmagic.R;
import com.drawingmagic.helpers.FilterItemHolder;
import com.drawingmagic.utils.Conditions;
import com.drawingmagic.views.HoverView;
import com.drawingmagic.views.ImageFilterPreview;
import com.drawingmagic.views.ImageFilterPreview_;

import net.steamcrafted.materialiconlib.MaterialIconView;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.SeekBarProgressChange;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import static com.drawingmagic.core.GPUImageFilterTools.FilterType;
import static com.drawingmagic.views.HoverView.*;

@EFragment(R.layout.fragment_effects)
public class FEffectsTools extends Fragment {

    ArrayList<FilterItemHolder> filters = new ArrayList<>();

    @ViewById
    GridView gridView;

    @ViewById
    MaterialIconView mivApply, mivCancel;

    @ViewById
    SeekBar seekBar;

    @ViewById
    LinearLayout llBottomFilterSettingsMenu;

    private OnChangeEffectListener changeEffectListener;

    @SeekBarProgressChange
    void seekBar() {
        this.changeEffectListener.onChangeSeekBarProgress(seekBar.getProgress());
    }

    public void setCanAdjustStatus(boolean canAdjust) {
        seekBar.setVisibility(canAdjust ? View.VISIBLE : View.GONE);
        llBottomFilterSettingsMenu.setVisibility(View.VISIBLE);
    }


    public interface OnChangeEffectListener {
        void onNewFilterSelected(final FilterItemHolder filter);

        void onChangeSeekBarProgress(int progress);
    }

    @AfterViews
    void afterViews() {
        try {
            this.changeEffectListener = (OnChangeEffectListener) getActivity();
        } catch (ClassCastException ex) {
            throw new ClassCastException(getActivity().getLocalClassName() + "must implement OnChangeEffectListener");
        }

        /// TODO: 16/09/2015 name to resources
        filters.add(new FilterItemHolder("Contrast", FilterType.CONTRAST));
        filters.add(new FilterItemHolder("Invert", FilterType.INVERT));
        filters.add(new FilterItemHolder("Pixelation", FilterType.PIXELATION));
        filters.add(new FilterItemHolder("Hue", FilterType.HUE));
        filters.add(new FilterItemHolder("Gamma", FilterType.GAMMA));
        filters.add(new FilterItemHolder("Brightness", FilterType.BRIGHTNESS));
        filters.add(new FilterItemHolder("Sepia", FilterType.SEPIA));
        filters.add(new FilterItemHolder("Grayscale", FilterType.GRAYSCALE));
        filters.add(new FilterItemHolder("Sharpness", FilterType.SHARPEN));
        filters.add(new FilterItemHolder("Sobel Edge Detection", FilterType.SOBEL_EDGE_DETECTION));
        filters.add(new FilterItemHolder("3x3 Convolution", FilterType.THREE_X_THREE_CONVOLUTION));
        filters.add(new FilterItemHolder("Emboss", FilterType.EMBOSS));
        filters.add(new FilterItemHolder("Posterize", FilterType.POSTERIZE));
        filters.add(new FilterItemHolder("Grouped filters", FilterType.FILTER_GROUP));
        filters.add(new FilterItemHolder("Saturation", FilterType.SATURATION));
        filters.add(new FilterItemHolder("Exposure", FilterType.EXPOSURE));
        filters.add(new FilterItemHolder("Highlight Shadow", FilterType.HIGHLIGHT_SHADOW));
        filters.add(new FilterItemHolder("Monochrome", FilterType.MONOCHROME));
        filters.add(new FilterItemHolder("Opacity", FilterType.OPACITY));
        filters.add(new FilterItemHolder("RGB", FilterType.RGB));
        filters.add(new FilterItemHolder("White Balance", FilterType.WHITE_BALANCE));
        filters.add(new FilterItemHolder("Vignette", FilterType.VIGNETTE));
        filters.add(new FilterItemHolder("ToneCurve", FilterType.TONE_CURVE));
        filters.add(new FilterItemHolder("Blend (Difference)", FilterType.BLEND_DIFFERENCE));
        filters.add(new FilterItemHolder("Blend (Source Over)", FilterType.BLEND_SOURCE_OVER));
        filters.add(new FilterItemHolder("Blend (Color Burn)", FilterType.BLEND_COLOR_BURN));
        filters.add(new FilterItemHolder("Blend (Color Dodge)", FilterType.BLEND_COLOR_DODGE));
        filters.add(new FilterItemHolder("Blend (Darken)", FilterType.BLEND_DARKEN));
        filters.add(new FilterItemHolder("Blend (Dissolve)", FilterType.BLEND_DISSOLVE));
        filters.add(new FilterItemHolder("Blend (Exclusion)", FilterType.BLEND_EXCLUSION));
        filters.add(new FilterItemHolder("Blend (Hard Light)", FilterType.BLEND_HARD_LIGHT));
        filters.add(new FilterItemHolder("Blend (Lighten)", FilterType.BLEND_LIGHTEN));
        filters.add(new FilterItemHolder("Blend (Add)", FilterType.BLEND_ADD));
        filters.add(new FilterItemHolder("Blend (Divide)", FilterType.BLEND_DIVIDE));
        filters.add(new FilterItemHolder("Blend (Multiply)", FilterType.BLEND_MULTIPLY));
        filters.add(new FilterItemHolder("Blend (Overlay)", FilterType.BLEND_OVERLAY));
        filters.add(new FilterItemHolder("new FilterItemHolder(Blend (Screen)", FilterType.BLEND_SCREEN));
        filters.add(new FilterItemHolder("Blend (Alpha)", FilterType.BLEND_ALPHA));
        filters.add(new FilterItemHolder("Blend (Color)", FilterType.BLEND_COLOR));
        filters.add(new FilterItemHolder("Blend (Hue)", FilterType.BLEND_HUE));
        filters.add(new FilterItemHolder("Blend (Saturation)", FilterType.BLEND_SATURATION));
        filters.add(new FilterItemHolder("Blend (Luminosity)", FilterType.BLEND_LUMINOSITY));
        filters.add(new FilterItemHolder("Blend (Linear Burn)", FilterType.BLEND_LINEAR_BURN));
        filters.add(new FilterItemHolder("Blend (Soft Light)", FilterType.BLEND_SOFT_LIGHT));
        filters.add(new FilterItemHolder("Blend (Subtract)", FilterType.BLEND_SUBTRACT));
        filters.add(new FilterItemHolder("Blend (Chroma Key)", FilterType.BLEND_CHROMA_KEY));
        filters.add(new FilterItemHolder("Blend (Normal)", FilterType.BLEND_NORMAL));
        filters.add(new FilterItemHolder("Lookup (Amatorka)", FilterType.LOOKUP_AMATORKA));
        filters.add(new FilterItemHolder("Gaussian Blur", FilterType.GAUSSIAN_BLUR));
        filters.add(new FilterItemHolder("Crosshatch", FilterType.CROSSHATCH));
        filters.add(new FilterItemHolder("Box Blur", FilterType.BOX_BLUR));
        filters.add(new FilterItemHolder("CGA Color Space", FilterType.CGA_COLORSPACE));
        filters.add(new FilterItemHolder("Dilation", FilterType.DILATION));
        filters.add(new FilterItemHolder("Kuwahara", FilterType.KUWAHARA));
        filters.add(new FilterItemHolder("RGB Dilation", FilterType.RGB_DILATION));
        filters.add(new FilterItemHolder("Sketch", FilterType.SKETCH));
        filters.add(new FilterItemHolder("Toon", FilterType.TOON));
        filters.add(new FilterItemHolder("Smooth Toon", FilterType.SMOOTH_TOON));
        filters.add(new FilterItemHolder("Bulge Distortion", FilterType.BULGE_DISTORTION));
        filters.add(new FilterItemHolder("Glass Sphere", FilterType.GLASS_SPHERE));
        filters.add(new FilterItemHolder("Haze", FilterType.HAZE));
        filters.add(new FilterItemHolder("Laplacian", FilterType.LAPLACIAN));
        filters.add(new FilterItemHolder("Non Maximum Suppression", FilterType.NON_MAXIMUM_SUPPRESSION));
        filters.add(new FilterItemHolder("Sphere Refraction", FilterType.SPHERE_REFRACTION));
        filters.add(new FilterItemHolder("Swirl", FilterType.SWIRL));
        filters.add(new FilterItemHolder("Weak Pixel Inclusion", FilterType.WEAK_PIXEL_INCLUSION));
        filters.add(new FilterItemHolder("False Color", FilterType.FALSE_COLOR));
        filters.add(new FilterItemHolder("Color Balance", FilterType.COLOR_BALANCE));
        filters.add(new FilterItemHolder("Levels Min (Mid Adjust)", FilterType.LEVELS_FILTER_MIN));
        filters.add(new FilterItemHolder("Bilateral Blur", FilterType.BILATERAL_BLUR));

        gridView.setAdapter(new GridViewImageFilterAdapter(getActivity(), R.layout.image_filter_preview, filters));
    }


    /**
     * Adapter for grid view
     */
    private class GridViewImageFilterAdapter extends ArrayAdapter<FilterItemHolder> {

        public GridViewImageFilterAdapter(Context context, int resource, List<FilterItemHolder> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (Conditions.isNull(convertView)) {
                convertView = ImageFilterPreview_.build(getActivity(), getItem(position), changeEffectListener);
            }
            ((ImageFilterPreview) convertView).setUpView(getItem(position), changeEffectListener);
            return convertView;
        }
    }

    @Click
    void mivCancel() {
//        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//        photoPickerIntent.setType("image/*");
//        getActivity().startActivityForResult(photoPickerIntent, ADrawingMagic.REQUEST_PICK_IMAGE);
        changeEffectListener.onNewFilterSelected(null);
    }

    @Click
    void mivApply() {

    }
}
