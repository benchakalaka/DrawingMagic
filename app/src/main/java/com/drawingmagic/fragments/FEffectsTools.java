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

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.ListView;

import com.drawingmagic.ADrawingMagic;
import com.drawingmagic.R;
import com.drawingmagic.adapters.EffectsAdapter;
import com.drawingmagic.core.GPUImageFilterTools;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

import static com.drawingmagic.adapters.EffectsAdapter.*;
import static com.drawingmagic.core.GPUImageFilterTools.FilterType;

@EFragment(R.layout.fragment_effects)
public class FEffectsTools extends Fragment {

    @ViewById
    ListView lvEffects;


    private OnChangeEffectListener changeEffectListener;

    public interface OnChangeEffectListener {
        void onNewFilterSelected(final GPUImageFilter filter);
    }


    @AfterViews
    void afterViews() {
        try {
            this.changeEffectListener = (OnChangeEffectListener) getActivity();
        } catch (ClassCastException ex) {
            throw new ClassCastException(getActivity().getLocalClassName() + "must implement OnChangeEffectListener");
        }

        ArrayList<FilterItem> filters = new ArrayList<>();

        filters.add(new FilterItem("Contrast", FilterType.CONTRAST));
        filters.add(new FilterItem("Invert", FilterType.INVERT));
        filters.add(new FilterItem("Pixelation", FilterType.PIXELATION));
        filters.add(new FilterItem("Hue", FilterType.HUE));
        filters.add(new FilterItem("Gamma", FilterType.GAMMA));
        filters.add(new FilterItem("Brightness", FilterType.BRIGHTNESS));
        filters.add(new FilterItem("Sepia", FilterType.SEPIA));
        filters.add(new FilterItem("Grayscale", FilterType.GRAYSCALE));
        filters.add(new FilterItem("Sharpness", FilterType.SHARPEN));
        filters.add(new FilterItem("Sobel Edge Detection", FilterType.SOBEL_EDGE_DETECTION));
        filters.add(new FilterItem("3x3 Convolution", FilterType.THREE_X_THREE_CONVOLUTION));
        filters.add(new FilterItem("Emboss", FilterType.EMBOSS));
        filters.add(new FilterItem("Posterize", FilterType.POSTERIZE));
        filters.add(new FilterItem("Grouped filters", FilterType.FILTER_GROUP));
        filters.add(new FilterItem("Saturation", FilterType.SATURATION));
        filters.add(new FilterItem("Exposure", FilterType.EXPOSURE));
        filters.add(new FilterItem("Highlight Shadow", FilterType.HIGHLIGHT_SHADOW));
        filters.add(new FilterItem("Monochrome", FilterType.MONOCHROME));
        filters.add(new FilterItem("Opacity", FilterType.OPACITY));
        filters.add(new FilterItem("RGB", FilterType.RGB));
        filters.add(new FilterItem("White Balance", FilterType.WHITE_BALANCE));
        filters.add(new FilterItem("Vignette", FilterType.VIGNETTE));
        filters.add(new FilterItem("ToneCurve", FilterType.TONE_CURVE));
        filters.add(new FilterItem("Blend (Difference)", FilterType.BLEND_DIFFERENCE));
        filters.add(new FilterItem("Blend (Source Over)", FilterType.BLEND_SOURCE_OVER));
        filters.add(new FilterItem("Blend (Color Burn)", FilterType.BLEND_COLOR_BURN));
        filters.add(new FilterItem("Blend (Color Dodge)", FilterType.BLEND_COLOR_DODGE));
        filters.add(new FilterItem("Blend (Darken)", FilterType.BLEND_DARKEN));
        filters.add(new FilterItem("Blend (Dissolve)", FilterType.BLEND_DISSOLVE));
        filters.add(new FilterItem("Blend (Exclusion)", FilterType.BLEND_EXCLUSION));
        filters.add(new FilterItem("Blend (Hard Light)", FilterType.BLEND_HARD_LIGHT));
        filters.add(new FilterItem("Blend (Lighten)", FilterType.BLEND_LIGHTEN));
        filters.add(new FilterItem("Blend (Add)", FilterType.BLEND_ADD));
        filters.add(new FilterItem("Blend (Divide)", FilterType.BLEND_DIVIDE));
        filters.add(new FilterItem("Blend (Multiply)", FilterType.BLEND_MULTIPLY));
        filters.add(new FilterItem("Blend (Overlay)", FilterType.BLEND_OVERLAY));
        filters.add(new FilterItem("new FilterItem(Blend (Screen)", FilterType.BLEND_SCREEN));
        filters.add(new FilterItem("Blend (Alpha)", FilterType.BLEND_ALPHA));
        filters.add(new FilterItem("Blend (Color)", FilterType.BLEND_COLOR));
        filters.add(new FilterItem("Blend (Hue)", FilterType.BLEND_HUE));
        filters.add(new FilterItem("Blend (Saturation)", FilterType.BLEND_SATURATION));
        filters.add(new FilterItem("Blend (Luminosity)", FilterType.BLEND_LUMINOSITY));
        filters.add(new FilterItem("Blend (Linear Burn)", FilterType.BLEND_LINEAR_BURN));
        filters.add(new FilterItem("Blend (Soft Light)", FilterType.BLEND_SOFT_LIGHT));
        filters.add(new FilterItem("Blend (Subtract)", FilterType.BLEND_SUBTRACT));
        filters.add(new FilterItem("Blend (Chroma Key)", FilterType.BLEND_CHROMA_KEY));
        filters.add(new FilterItem("Blend (Normal)", FilterType.BLEND_NORMAL));
        filters.add(new FilterItem("Lookup (Amatorka)", FilterType.LOOKUP_AMATORKA));
        filters.add(new FilterItem("Gaussian Blur", FilterType.GAUSSIAN_BLUR));
        filters.add(new FilterItem("Crosshatch", FilterType.CROSSHATCH));
        filters.add(new FilterItem("Box Blur", FilterType.BOX_BLUR));
        filters.add(new FilterItem("CGA Color Space", FilterType.CGA_COLORSPACE));
        filters.add(new FilterItem("Dilation", FilterType.DILATION));
        filters.add(new FilterItem("Kuwahara", FilterType.KUWAHARA));
        filters.add(new FilterItem("RGB Dilation", FilterType.RGB_DILATION));
        filters.add(new FilterItem("Sketch", FilterType.SKETCH));
        filters.add(new FilterItem("Toon", FilterType.TOON));
        filters.add(new FilterItem("Smooth Toon", FilterType.SMOOTH_TOON));
        filters.add(new FilterItem("Bulge Distortion", FilterType.BULGE_DISTORTION));
        filters.add(new FilterItem("Glass Sphere", FilterType.GLASS_SPHERE));
        filters.add(new FilterItem("Haze", FilterType.HAZE));
        filters.add(new FilterItem("Laplacian", FilterType.LAPLACIAN));
        filters.add(new FilterItem("Non Maximum Suppression", FilterType.NON_MAXIMUM_SUPPRESSION));
        filters.add(new FilterItem("Sphere Refraction", FilterType.SPHERE_REFRACTION));
        filters.add(new FilterItem("Swirl", FilterType.SWIRL));
        filters.add(new FilterItem("Weak Pixel Inclusion", FilterType.WEAK_PIXEL_INCLUSION));
        filters.add(new FilterItem("False Color", FilterType.FALSE_COLOR));
        filters.add(new FilterItem("Color Balance", FilterType.COLOR_BALANCE));
        filters.add(new FilterItem("Levels Min (Mid Adjust)", FilterType.LEVELS_FILTER_MIN));
        filters.add(new FilterItem("Bilateral Blur", FilterType.BILATERAL_BLUR));

        lvEffects.setAdapter(new EffectsAdapter(getActivity(), filters));
    }

    @ItemClick void lvEffects(FilterItem item){
        changeEffectListener.onNewFilterSelected(GPUImageFilterTools.createFilterForType(getActivity(), item.filter));
    }

    @Click
    void button_save() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        getActivity().startActivityForResult(photoPickerIntent, ADrawingMagic.REQUEST_PICK_IMAGE);
    }
}
