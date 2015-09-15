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

import com.drawingmagic.ADrawingMagic;
import com.drawingmagic.R;
import com.drawingmagic.core.GPUImageFilterTools;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

import static com.drawingmagic.core.GPUImageFilterTools.OnGpuImageFilterChosenListener;

@EFragment(R.layout.fragment_canvas_tools)
public class FCanvasTools extends Fragment {


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
    }
}
