/*
 * Created by Ihor Karpachev, Copyright (c) 2015. .
 */

package com.drawingmagic.dialogs;


import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.drawingmagic.SuperActivity;
import com.drawingmagic.utils.Utils;

/**
 * Created by ihor.karpachev on 13/04/2015.
 * Project: Installer Application
 * Package: com.touchip.organizer.activities.custom.components.dialogs
 * Datascope Systems Ltd.
 */

/**
 * TODO: Java code refactoring, comments ....
 */
//@EViewGroup(R.layout.dialog_canvas_settings)
public class DialogCanvasSettings extends RelativeLayout {

    private final CanvasSettingsDialogBuilder builder;
    private final OnCanvasSettingsListener listener;


    //@ViewById
    TextView tvTitle, tvBitmapInfo;
    //@ViewById
    RelativeLayout rl90, rl180, rl270, rlAspectRatio;
    //@ViewById
    CheckBox cbKeepAspectRatio;
    //@ViewById
    Button btnOk, btnCancel, btnRestore;
    //@ViewById
    LinearLayout llRotationOption, llBitmapInformation;

    //@ViewById
    ImageView iv90Selected, iv180Selected, iv270Selected;

    private CanvasSettings canvasSettings;

    public DialogCanvasSettings(SuperActivity act, CanvasSettingsDialogBuilder builder) {
        super(act);

        this.builder = builder;
        // check activity for inheritance from OnCanvasSettingsListener
        try {
            this.listener = (OnCanvasSettingsListener) act;
        } catch (ClassCastException ex) {
            throw new ClassCastException(act.getLocalClassName() + "must implement OnCanvasSettingsListener");
        }

        canvasSettings = new CanvasSettings();
        canvasSettings.setKeepAspectRatio(builder.isAspectRatioSelected);
        canvasSettings.setRotateDegree(builder.selectedDegree);
    }

    /**
     * Get dialog builder
     *
     * @return instance of ShapeSettingsDialogBuilder
     */
    public static CanvasSettingsDialogBuilder builder() {
        return new CanvasSettingsDialogBuilder();
    }

    public void show() {
        builder.hostDialog.setContentView(this);
        builder.hostDialog.show();
    }

    public void showWithDialog(Dialog hostDialog) {
        builder.hostDialog = hostDialog;
        builder.hostDialog.setContentView(this);
        builder.hostDialog.show();
    }


    //@AfterViews
    void afterViews() {
        tvTitle.setText(builder.title);
        selectViewByTypesOfShape(builder.selectedDegree);

        // build degree view
        if (builder.displayRotationDegree) {
            // display shapes relative layout
            llRotationOption.setVisibility(View.VISIBLE);

            // display visible rotation degrees
            for (int degree : builder.degreesValuesVisible) {
                switch (degree) {
                    case RotateBy._90:
                        rl90.setVisibility(View.VISIBLE);
                        break;

                    case RotateBy._180:
                        rl180.setVisibility(View.VISIBLE);
                        break;

                    case RotateBy._270:
                        rl270.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }
        // build display of aspect ratio
        if (builder.displayAspectRatio) {
            rlAspectRatio.setVisibility(View.VISIBLE);
            cbKeepAspectRatio.performClick();
        }

        // build bitmap info
        if (builder.displayBitmapInfo) {
            llBitmapInformation.setVisibility(View.VISIBLE);
            tvBitmapInfo.setText(builder.bitmapInformation);
        }
    }

    //@CheckedChange
    void cbKeepAspectRatio(CompoundButton cb, boolean isChecked) {
        canvasSettings.setKeepAspectRatio(isChecked);
    }


    //@Click
    void rlAspectRatio() {
        //rlAspectRatio.startAnimation(Utils.AnimationManager.load(R.anim.bounce));
        cbKeepAspectRatio.performClick();
    }

    //@Click
    void rl90() {
        playAnimationOnViewAndUnselectAllShapes(iv90Selected);
        canvasSettings.setRotateDegree(RotateBy._90);
        selectViewByTypesOfShape(RotateBy._90);
    }

    //@Click
    void rl180() {
        playAnimationOnViewAndUnselectAllShapes(iv180Selected);
        canvasSettings.setRotateDegree(RotateBy._180);
        selectViewByTypesOfShape(RotateBy._180);
    }

    //@Click
    void rl270() {
        playAnimationOnViewAndUnselectAllShapes(iv270Selected);
        canvasSettings.setRotateDegree(RotateBy._270);
        selectViewByTypesOfShape(RotateBy._270);
    }


    private void playAnimationOnViewAndUnselectAllShapes(View target) {
        unselectAllTypesOfShapes();
    }

    private void unselectAllTypesOfShapes() {
        iv90Selected.setVisibility(View.INVISIBLE);
        iv180Selected.setVisibility(View.INVISIBLE);
        iv270Selected.setVisibility(View.INVISIBLE);
    }

    private void selectViewByTypesOfShape(int rotateBy) {
        switch (rotateBy) {
            case RotateBy._90:
                iv90Selected.setVisibility(View.VISIBLE);
                break;

            case RotateBy._180:
                iv180Selected.setVisibility(View.VISIBLE);
                break;

            case RotateBy._270:
                iv270Selected.setVisibility(View.VISIBLE);
                break;

        }
    }

    //@Click
    void btnOk() {
        listener.onSetUpCanvasSettingsOkClicked(canvasSettings);
        builder.hostDialog.dismiss();
    }

    //@Click
    void btnRestore() {
        listener.onRestoreDefaultCanvasSettingsClicked();
        builder.hostDialog.dismiss();
    }

    //@Click
    void btnCancel() {
        builder.hostDialog.dismiss();
    }

    /**
     * Any view has to implement current interface in order to interact with dialog
     */
    public interface OnCanvasSettingsListener {
        void onSetUpCanvasSettingsOkClicked(CanvasSettings canvasSettings);

        void onRestoreDefaultCanvasSettingsClicked();
    }

    /**
     * Degree for rotation of the bitmap/shape etc...
     */
    public static class RotateBy {
        public static final int _90 = 90;
        public static final int _180 = 180;
        public static final int _270 = 270;
        /**
         * For configuring purposes
         */
        public static final int[] ALL_TYPES_OF_ROTATION = {_90, _180, _270};

        // hide constructor, only static constants exposed
        private RotateBy() {
        }
    }

    /**
     * Class represents Canvas settings
     */
    public static class CanvasSettings {
        // init default values, rotate degree ==0, means that there is no rotation required
        private int rotateDegree = 0;
        private boolean keepAspectRatio = true;

        public int getRotateDegree() {
            return rotateDegree;
        }

        public void setRotateDegree(int rotateDegree) {
            this.rotateDegree = rotateDegree;
        }

        public boolean isKeepAspectRatio() {
            return keepAspectRatio;
        }

        public void setKeepAspectRatio(boolean keepAspectRatio) {
            this.keepAspectRatio = keepAspectRatio;
        }
    }


    /**
     * Builder pattern
     */
    public static class CanvasSettingsDialogBuilder {
        // hide all settings views by default
        boolean displayRotationDegree = false;
        boolean displayAspectRatio = false;
        boolean isAspectRatioSelected = false;
        boolean displayBitmapInfo = false;

        // some default values for dialog
        String title;
        String bitmapInformation;
        int selectedDegree;
        int[] degreesValuesVisible = RotateBy.ALL_TYPES_OF_ROTATION;
        private Dialog hostDialog;

        /**
         * Init host dialog for this view
         *
         * @param info dialog which hosts current view
         * @return ShapeSettingsDialogBuilder instance
         */
        public CanvasSettingsDialogBuilder withBitmapInformation(String info) {
            this.bitmapInformation = info;
            return this;
        }

        /**
         * Init host dialog for this view
         *
         * @param hostDialog dialog which hosts current view
         * @return ShapeSettingsDialogBuilder instance
         */
        public CanvasSettingsDialogBuilder withDialog(Dialog hostDialog) {
            this.hostDialog = hostDialog;
            return this;
        }

        /**
         * Init title string
         *
         * @param title title to be set for dialog
         * @return ShapeSettingsDialogBuilder instance
         */
        public CanvasSettingsDialogBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * Init shape to be selected
         *
         * @param selectedDegree list of init shapes
         * @return ShapeSettingsDialogBuilder instance
         */
        public CanvasSettingsDialogBuilder withSelectedRotation(int selectedDegree) {
            this.selectedDegree = selectedDegree;
            return this;
        }

        /**
         * Init set of shapes to be displayed
         *
         * @param degrees list of init shapes
         * @return ShapeSettingsDialogBuilder instance
         */
        public CanvasSettingsDialogBuilder withVisibleRotations(int... degrees) {
            degreesValuesVisible = degrees;
            return this;
        }

        /**
         * Init fill shapes state
         *
         * @param isDisplayAspectRatio boolean value for init fill shape state
         * @return ShapeSettingsDialogBuilder instance
         */
        public CanvasSettingsDialogBuilder withAspectRatioState(boolean isDisplayAspectRatio) {
            this.isAspectRatioSelected = isDisplayAspectRatio;
            return this;
        }

        /**
         * Add shapes picker to main layout
         *
         * @return ShapeSettingsDialogBuilder instance
         */
        public CanvasSettingsDialogBuilder displayAspectRatio() {
            displayAspectRatio = true;
            return this;
        }

        /**
         * Add shapes brush width to main layout
         *
         * @return ShapeSettingsDialogBuilder instance
         */
        public CanvasSettingsDialogBuilder displayRotationValues() {
            displayRotationDegree = true;
            return this;
        }

        /**
         * Add shapes brush width to main layout
         *
         * @return ShapeSettingsDialogBuilder instance
         */
        public CanvasSettingsDialogBuilder displayBitmapInfo() {
            displayBitmapInfo = true;
            return this;
        }
    }
}