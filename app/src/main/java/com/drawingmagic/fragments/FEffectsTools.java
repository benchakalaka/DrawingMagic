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
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.drawingmagic.R;
import com.drawingmagic.eventbus.Event;
import com.drawingmagic.helpers.FilterItemHolder;
import com.drawingmagic.utils.Conditions;
import com.drawingmagic.utils.Log;
import com.drawingmagic.views.ImageFilterPreview;
import com.drawingmagic.views.ImageFilterPreview_;

import net.steamcrafted.materialiconlib.MaterialIconView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.SeekBarProgressChange;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

import static com.drawingmagic.core.GPUImageFilterTools.FilterType;

@EFragment(R.layout.fragment_effects)
public class FEffectsTools extends Fragment {

    public static final ArrayList<FilterItemHolder> FILTERS = new ArrayList<>();

    private void initFilters() {
        FILTERS.add(new FilterItemHolder(getString(R.string.contrast), FilterType.CONTRAST));
        FILTERS.add(new FilterItemHolder(getString(R.string.invert), FilterType.INVERT));
        FILTERS.add(new FilterItemHolder(getString(R.string.pixelation), FilterType.PIXELATION));
        FILTERS.add(new FilterItemHolder(getString(R.string.hue), FilterType.HUE));
        FILTERS.add(new FilterItemHolder(getString(R.string.gamma), FilterType.GAMMA));
        FILTERS.add(new FilterItemHolder(getString(R.string.brightness), FilterType.BRIGHTNESS));
        FILTERS.add(new FilterItemHolder(getString(R.string.sepia), FilterType.SEPIA));
        FILTERS.add(new FilterItemHolder(getString(R.string.grayscale), FilterType.GRAYSCALE));
        FILTERS.add(new FilterItemHolder(getString(R.string.sharpness), FilterType.SHARPEN));
        FILTERS.add(new FilterItemHolder(getString(R.string.sobel_edge_detection), FilterType.SOBEL_EDGE_DETECTION));
        FILTERS.add(new FilterItemHolder(getString(R.string.convultion), FilterType.THREE_X_THREE_CONVOLUTION));
        FILTERS.add(new FilterItemHolder(getString(R.string.emboss), FilterType.EMBOSS));
        FILTERS.add(new FilterItemHolder(getString(R.string.posterize), FilterType.POSTERIZE));
        FILTERS.add(new FilterItemHolder(getString(R.string.grouped_filter), FilterType.FILTER_GROUP));
        FILTERS.add(new FilterItemHolder(getString(R.string.saturation), FilterType.SATURATION));
        FILTERS.add(new FilterItemHolder(getString(R.string.exposure), FilterType.EXPOSURE));
        FILTERS.add(new FilterItemHolder(getString(R.string.highlight_shadow), FilterType.HIGHLIGHT_SHADOW));
        FILTERS.add(new FilterItemHolder(getString(R.string.monochrome), FilterType.MONOCHROME));
        FILTERS.add(new FilterItemHolder(getString(R.string.opacity), FilterType.OPACITY));
        FILTERS.add(new FilterItemHolder(getString(R.string.rgb), FilterType.RGB));
        FILTERS.add(new FilterItemHolder(getString(R.string.white_balance), FilterType.WHITE_BALANCE));
        FILTERS.add(new FilterItemHolder(getString(R.string.vignette), FilterType.VIGNETTE));
        FILTERS.add(new FilterItemHolder(getString(R.string.tone_curve), FilterType.TONE_CURVE));
        FILTERS.add(new FilterItemHolder(getString(R.string.lookup_amatorka), FilterType.LOOKUP_AMATORKA));
        FILTERS.add(new FilterItemHolder(getString(R.string.guassian_blur), FilterType.GAUSSIAN_BLUR));
        FILTERS.add(new FilterItemHolder(getString(R.string.crosshatch), FilterType.CROSSHATCH));
        FILTERS.add(new FilterItemHolder(getString(R.string.box_blur), FilterType.BOX_BLUR));
        FILTERS.add(new FilterItemHolder(getString(R.string.cga_color_space), FilterType.CGA_COLORSPACE));
        FILTERS.add(new FilterItemHolder(getString(R.string.dilation), FilterType.DILATION));
        FILTERS.add(new FilterItemHolder(getString(R.string.kuwahara), FilterType.KUWAHARA));
        FILTERS.add(new FilterItemHolder(getString(R.string.rgb_dilation), FilterType.RGB_DILATION));
        FILTERS.add(new FilterItemHolder(getString(R.string.sketch), FilterType.SKETCH));
        FILTERS.add(new FilterItemHolder(getString(R.string.toon), FilterType.TOON));
        FILTERS.add(new FilterItemHolder(getString(R.string.smooth_toon), FilterType.SMOOTH_TOON));
        FILTERS.add(new FilterItemHolder(getString(R.string.buldge_distortion), FilterType.BULGE_DISTORTION));
        FILTERS.add(new FilterItemHolder(getString(R.string.glass_sphere), FilterType.GLASS_SPHERE));
        FILTERS.add(new FilterItemHolder(getString(R.string.haze), FilterType.HAZE));
        FILTERS.add(new FilterItemHolder(getString(R.string.laplacian), FilterType.LAPLACIAN));
        FILTERS.add(new FilterItemHolder(getString(R.string.non_maximum_suppression), FilterType.NON_MAXIMUM_SUPPRESSION));
        FILTERS.add(new FilterItemHolder(getString(R.string.sphere_refraction), FilterType.SPHERE_REFRACTION));
        FILTERS.add(new FilterItemHolder(getString(R.string.swirl), FilterType.SWIRL));
        FILTERS.add(new FilterItemHolder(getString(R.string.weak_pixel_inclusion), FilterType.WEAK_PIXEL_INCLUSION));
        FILTERS.add(new FilterItemHolder(getString(R.string.false_color), FilterType.FALSE_COLOR));
        FILTERS.add(new FilterItemHolder(getString(R.string.color_balance), FilterType.COLOR_BALANCE));
        FILTERS.add(new FilterItemHolder(getString(R.string.levels_min_adjust), FilterType.LEVELS_FILTER_MIN));
        FILTERS.add(new FilterItemHolder(getString(R.string.biliteral_blur), FilterType.BILATERAL_BLUR));
        FILTERS.add(new FilterItemHolder(getString(R.string.blend_difference), FilterType.BLEND_DIFFERENCE));
        FILTERS.add(new FilterItemHolder(getString(R.string.blend_source_over), FilterType.BLEND_SOURCE_OVER));
        FILTERS.add(new FilterItemHolder(getString(R.string.blend_color_burn), FilterType.BLEND_COLOR_BURN));
        FILTERS.add(new FilterItemHolder(getString(R.string.blend_color_dodge), FilterType.BLEND_COLOR_DODGE));
        FILTERS.add(new FilterItemHolder(getString(R.string.blend_darken), FilterType.BLEND_DARKEN));
        FILTERS.add(new FilterItemHolder(getString(R.string.blend_dissolve), FilterType.BLEND_DISSOLVE));
        FILTERS.add(new FilterItemHolder(getString(R.string.blend_exclusion), FilterType.BLEND_EXCLUSION));
        FILTERS.add(new FilterItemHolder(getString(R.string.blend_hard_light), FilterType.BLEND_HARD_LIGHT));
        FILTERS.add(new FilterItemHolder(getString(R.string.blend_lighten), FilterType.BLEND_LIGHTEN));
        FILTERS.add(new FilterItemHolder(getString(R.string.blend_add), FilterType.BLEND_ADD));
        FILTERS.add(new FilterItemHolder(getString(R.string.blend_divide), FilterType.BLEND_DIVIDE));
        FILTERS.add(new FilterItemHolder(getString(R.string.blend_multiply), FilterType.BLEND_MULTIPLY));
        FILTERS.add(new FilterItemHolder(getString(R.string.blend_overlay), FilterType.BLEND_OVERLAY));
        FILTERS.add(new FilterItemHolder(getString(R.string.blend_screen), FilterType.BLEND_SCREEN));
        FILTERS.add(new FilterItemHolder(getString(R.string.blend_alpha), FilterType.BLEND_ALPHA));
        FILTERS.add(new FilterItemHolder(getString(R.string.blend_color), FilterType.BLEND_COLOR));
        FILTERS.add(new FilterItemHolder(getString(R.string.blend_hue), FilterType.BLEND_HUE));
        FILTERS.add(new FilterItemHolder(getString(R.string.blend_saturation), FilterType.BLEND_SATURATION));
        FILTERS.add(new FilterItemHolder(getString(R.string.blend_luminosity), FilterType.BLEND_LUMINOSITY));
        FILTERS.add(new FilterItemHolder(getString(R.string.blend_linear_burn), FilterType.BLEND_LINEAR_BURN));
        FILTERS.add(new FilterItemHolder(getString(R.string.blend_light), FilterType.BLEND_SOFT_LIGHT));
        FILTERS.add(new FilterItemHolder(getString(R.string.blend_substract), FilterType.BLEND_SUBTRACT));
        FILTERS.add(new FilterItemHolder(getString(R.string.blend_chroma_key), FilterType.BLEND_CHROMA_KEY));
        FILTERS.add(new FilterItemHolder(getString(R.string.blend_normal), FilterType.BLEND_NORMAL));

        Log.e("Filters has been initialized  - " + FILTERS.size());
    }

    @ViewById
    GridView gridView;

    @ViewById
    MaterialIconView mivApply, mivCancel;

    @ViewById
    SeekBar seekBar;

    @ViewById
    LinearLayout llBottomFilterSettingsMenu;


    @SeekBarProgressChange
    void seekBar(int progress) {
        EventBus.getDefault().post(new Event(Event.ON_ADJUST_FILTER_LEVEL, progress));
    }

    public void setCanAdjustStatus(boolean canAdjust) {
        seekBar.setVisibility(canAdjust ? View.VISIBLE : View.GONE);
        llBottomFilterSettingsMenu.setVisibility(View.VISIBLE);
    }


    @AfterViews
    void afterViews() {
        // lazy init filters
        if (Conditions.isNullOrEmpty(FILTERS)) {
            initFilters();
        }

        gridView.setAdapter(new GridViewImageFilterAdapter(getActivity(), R.layout.image_filter_preview, FILTERS));
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
                convertView = ImageFilterPreview_.build(getActivity(), getItem(position));
            }
            ((ImageFilterPreview) convertView).setUpView(getItem(position));
            return convertView;
        }
    }

    @Click
    void mivCancel() {
        EventBus.getDefault().post(new Event(Event.ON_APPLY_EFFECT, null));
    }

    @Click
    void mivApply() {

    }
}
