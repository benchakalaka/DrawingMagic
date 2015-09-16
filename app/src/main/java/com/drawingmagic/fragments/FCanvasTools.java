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

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.drawingmagic.ADrawingMagic;
import com.drawingmagic.R;
import com.theartofdev.edmodo.cropper.CropImageView;

import net.steamcrafted.materialiconlib.MaterialIconView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import static android.widget.CompoundButton.OnCheckedChangeListener;

@EFragment(R.layout.fragment_canvas_tools)
public class FCanvasTools extends Fragment {

    // Static final constants
    private static final int DEFAULT_ASPECT_RATIO_VALUES = 20;
    private static final int ROTATE_NINETY_DEGREES = 90;
    @ViewById
    MaterialIconView mivApply, mivCancel;


    Bitmap croppedImage;

    @ViewById(R.id.Button_rotate)
    Button rotateButton;

    @ViewById(R.id.cropShapeToggle)
    ToggleButton cropShapeToggle;

    private OnChangeCanvasSettingsListener changeCanvasSettingsListener;

    public interface OnChangeCanvasSettingsListener {
        void onCanvasSettingsChanged();
    }

    @Click
    void mivApply() {
        croppedImage = ((ADrawingMagic) getActivity()).cropImageView.getCroppedImage();
        ((ADrawingMagic) getActivity()).cropImageView.setImageBitmap(croppedImage);
    }

    @Click
    void mivCancel() {
        ((ADrawingMagic) getActivity()).cropImageView.setImageResource(R.drawable.daimajia);
    }


    @AfterViews
    void afterViews() {
        // check activity for inheritance from OnChangeCanvasSettingsListener
        try {
            this.changeCanvasSettingsListener = (OnChangeCanvasSettingsListener) getActivity();
        } catch (ClassCastException ex) {
            throw new ClassCastException(getActivity().getLocalClassName() + "must implement OnChangeCanvasSettingsListener");
        }


        //Sets the rotate button
        rotateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((ADrawingMagic) getActivity()).cropImageView.rotateImage(ROTATE_NINETY_DEGREES);
            }
        });


        // Sets crop shape
        cropShapeToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((ADrawingMagic) getActivity()).cropImageView.setCropShape(isChecked ? CropImageView.CropShape.OVAL : CropImageView.CropShape.RECTANGLE);
            }
        });

        // Sets initial aspect ratio to 10/10, for demonstration purposes
        ((ADrawingMagic) getActivity()).cropImageView.setAspectRatio(DEFAULT_ASPECT_RATIO_VALUES, DEFAULT_ASPECT_RATIO_VALUES);
    }
}
