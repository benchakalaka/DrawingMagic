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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.drawingmagic.R;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import static android.widget.AdapterView.OnItemSelectedListener;
import static android.widget.CompoundButton.*;
import static android.widget.SeekBar.OnSeekBarChangeListener;

@EFragment(R.layout.fragment_canvas_tools)
public class FCanvasTools extends Fragment {

    // Static final constants
    private static final int DEFAULT_ASPECT_RATIO_VALUES = 20;
    private static final int ROTATE_NINETY_DEGREES = 90;
    private static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
    private static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";
    private static final int ON_TOUCH = 1;

    // Instance variables
    private int mAspectRatioX = DEFAULT_ASPECT_RATIO_VALUES;
    private int mAspectRatioY = DEFAULT_ASPECT_RATIO_VALUES;

    Bitmap croppedImage;

    @ViewById(R.id.CropImageView)
    CropImageView cropImageView;


    @ViewById(R.id.croppedImageView)
    ImageView croppedImageView;


    @ViewById(R.id.aspectRatioXSeek)
    SeekBar aspectRatioXSeek;

    @ViewById(R.id.aspectRatioYSeek)
    SeekBar aspectRatioYSeek;

    @ViewById(R.id.showGuidelinesSpin)
    Spinner showGuidelinesSpin;

    @ViewById(R.id.aspectRatioNum)
    TextView aspectRatioNum;

    @ViewById(R.id.Button_rotate)
    Button rotateButton;

    @ViewById(R.id.Button_crop)
    Button cropButton;

    @ViewById(R.id.Button_load)
    Button loadButton;

    @ViewById(R.id.fixedAspectRatioToggle)
    ToggleButton fixedAspectRatioToggle;

    @ViewById(R.id.cropShapeToggle)
    ToggleButton cropShapeToggle;


    private OnChangeCanvasSettingsListener changeCanvasSettingsListener;

    public interface OnChangeCanvasSettingsListener {
        void onCanvasSettingsChanged();
    }

    @AfterViews
    void afterViews() {
        // check activity for inheritance from OnChangeCanvasSettingsListener
        try {
            this.changeCanvasSettingsListener = (OnChangeCanvasSettingsListener) getActivity();
        } catch (ClassCastException ex) {
            throw new ClassCastException(getActivity().getLocalClassName() + "must implement OnChangeCanvasSettingsListener");
        }


        // Sets sliders to be disabled until fixedAspectRatio is set
        aspectRatioXSeek.setEnabled(false);
        aspectRatioYSeek.setEnabled(false);

        // Set initial spinner value
        showGuidelinesSpin.setSelection(ON_TOUCH);

        //Sets the rotate button

        rotateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cropImageView.rotateImage(ROTATE_NINETY_DEGREES);
            }
        });

        // Sets fixedAspectRatio
        fixedAspectRatioToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cropImageView.setFixedAspectRatio(isChecked);
                if (isChecked) {
                    aspectRatioXSeek.setEnabled(true);
                    aspectRatioYSeek.setEnabled(true);
                } else {
                    aspectRatioXSeek.setEnabled(false);
                    aspectRatioYSeek.setEnabled(false);
                }
            }
        });

        // Sets crop shape
        cropShapeToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cropImageView.setCropShape(isChecked ? CropImageView.CropShape.OVAL : CropImageView.CropShape.RECTANGLE);
            }
        });

        // Sets initial aspect ratio to 10/10, for demonstration purposes
        cropImageView.setAspectRatio(DEFAULT_ASPECT_RATIO_VALUES, DEFAULT_ASPECT_RATIO_VALUES);

        aspectRatioXSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar aspectRatioXSeek, int progress, boolean fromUser) {
                mAspectRatioX = progress;
                cropImageView.setAspectRatio(progress, mAspectRatioY);
                aspectRatioNum.setText("(" + mAspectRatioX + ", " + mAspectRatioY + ")");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        aspectRatioYSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar aspectRatioYSeek, int progress, boolean fromUser) {
                mAspectRatioY = progress;
                cropImageView.setAspectRatio(mAspectRatioX, progress);
                aspectRatioNum.setText("(" + mAspectRatioX + ", " + mAspectRatioY + ")");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Sets up the Spinner
        showGuidelinesSpin.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cropImageView.setGuidelines(i);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        cropButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                croppedImage = cropImageView.getCroppedImage();
                croppedImageView.setImageBitmap(croppedImage);
            }
        });

        loadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //startActivityForResult(getPickImageChooserIntent(), 200);
            }
        });

    }
}
