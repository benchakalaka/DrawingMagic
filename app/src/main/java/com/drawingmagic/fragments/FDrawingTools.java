package com.drawingmagic.fragments;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.drawingmagic.R;
import com.drawingmagic.core.DrawingSettings;
import com.drawingmagic.utils.AnimationUtils;
import com.drawingmagic.utils.Notification;
import com.drawingmagic.utils.Utils;

import net.steamcrafted.materialiconlib.MaterialIconView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.SeekBarProgressChange;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.res.StringRes;

import static com.drawingmagic.core.DrawingView.DEFAULT_BRUSH_SIZE;
import static com.drawingmagic.core.DrawingView.GridType;
import static com.drawingmagic.core.DrawingView.ShapesType;
import static com.drawingmagic.utils.AnimationUtils.AnimationTechniques;


/**
 * Project DrawingMagic.
 * Created by ihorkarpachev.
 * On 15/09/15 at 08:37.
 */
@EFragment(R.layout.fragment_drawing_tools)
public class FDrawingTools extends Fragment {

    // default values for round bitmap (background for image views)
    private static final int ROUND_BITMAP_DIAMETER = 40;

    // String resources
    @StringRes(R.string.draw_arrow)
    String drawArrow;
    @StringRes(R.string.draw_rect)
    String drawRect;
    @StringRes(R.string.draw_triangle)
    String drawTriangle;
    @StringRes(R.string.draw_circle)
    String drawCircle;
    @StringRes(R.string.draw_line)
    String drawLine;
    @StringRes(R.string.free_drawing)
    String freeDrawing;

    // Color resources
    @ColorRes(R.color.default_ab_background_colour)
    int selectionColour;
    @ColorRes(R.color.default_text_colour)
    int deselectedColour;


    private final DrawingSettings drawingSettings = new DrawingSettings();
    private OnChangeDrawingSettingsListener listener;

    @ViewById
    MaterialIconView mivChangeBrushSize, mivSimple, mivLine, mivRectangle, mivTriangle, mivArrow, mivCircle;

    @ViewById
    ImageView ivColour0, ivColour1, ivColour2, ivColour3, ivColour4, ivColour5, ivColour6, ivColour7, ivColour8, ivColour9, ivColour10, ivColour11;

    @ViewById
    RelativeLayout rlDashed, rlFillShape, rlDisplayLinesWhileDrawing, rlNoGrid, rlPartlyGrid, rlFullGrid;

    @ViewById
    SeekBar sbBrushSize;


//    @SeekBarProgressChange
//    void sbRotation(int rotateDegree) {
//        EventBus.getDefault().post(new Event(Event.ON_ROTATE, rotateDegree));
//    }

    @SeekBarProgressChange
    void sbBrushSize(int brushSize) {
        drawingSettings.setBrushWidth(brushSize, (getActivity()).getResources().getDisplayMetrics());
        listener.onSetUpDrawingShapesOkClicked(drawingSettings);
    }


    @AfterViews
    void afterViews() {
        // check activity for inheritance from OnSelectTypeOfShapeListener
        try {
            this.listener = (OnChangeDrawingSettingsListener) getActivity();
        } catch (ClassCastException ex) {
            throw new ClassCastException(getActivity().getLocalClassName() + "must implement OnSelectTypeOfShapeListener");
        }

        selectViewByTypeOfGrid(GridType.FULL_GRID);

        // set current brush size
        sbBrushSize.setProgress(DEFAULT_BRUSH_SIZE);

        // color picker view
        initColorPicker();
    }

    public FDrawingTools() {
    }

    @Click
    void rlNoGrid() {
        playAnimationOnView(rlNoGrid);
        selectViewByTypeOfGrid(GridType.NO_GRID);
    }

    @Click
    void rlPartlyGrid() {
        playAnimationOnView(rlPartlyGrid);
        selectViewByTypeOfGrid(GridType.PARTLY_GRID);
    }

    @Click
    void rlFullGrid() {
        playAnimationOnView(rlFullGrid);
        selectViewByTypeOfGrid(GridType.FULL_GRID);
    }

    private void selectShape(MaterialIconView icon) {
        playAnimationOnView(icon);
        deselectShapes();
        listener.onSetUpDrawingShapesOkClicked(drawingSettings);
        icon.setColor(selectionColour);
    }

    private void playAnimationOnView(View target) {
        AnimationUtils.animate(target, AnimationTechniques.ZOOM_IN);
    }

    /**
     * Deselect all types of shape
     */
    private void deselectShapes() {
        mivSimple.setColor(deselectedColour);
        mivArrow.setColor(deselectedColour);
        mivLine.setColor(deselectedColour);
        mivRectangle.setColor(deselectedColour);
        mivCircle.setColor(deselectedColour);
        mivTriangle.setColor(deselectedColour);
    }

    private void selectViewByTypeOfGrid(int typeOfGrid) {
        drawingSettings.setGridType(typeOfGrid);
        listener.onSetUpDrawingShapesOkClicked(drawingSettings);
    }

    /**
     * Any view has to implement current interface in order to interact with dialog
     */
    public interface OnChangeDrawingSettingsListener {
        void onSetUpDrawingShapesOkClicked(DrawingSettings shape);
    }


    /**
     * Return string representation of shape (i.e typeOfShape = ShapesType.RECTANGLE -> "Draw rectangle")
     *
     * @param typeOfShape int representation of shape
     * @return string representation
     */
    public String getStringMessageByTypesOfShape(int typeOfShape) {
        switch (typeOfShape) {
            case ShapesType.ARROW:
                return drawArrow;
            case ShapesType.CIRCLE:
                return drawCircle;
            case ShapesType.LINE:
                return drawLine;
            case ShapesType.RECTANGLE:
                return drawRect;
            case ShapesType.STANDARD_DRAWING:
                return freeDrawing;
            case ShapesType.TRIANGLE:
                return drawTriangle;
        }
        return "?";
    }


    private void setUpCustomColourAndPlayAnimation(View view) {
        playAnimationOnView(view);
        // tag of the colors views is the hex representation of colour i.e. 44FF24CC
        drawingSettings.setCurrentColour(Color.parseColor(view.getTag().toString()));
        listener.onSetUpDrawingShapesOkClicked(drawingSettings);
        initColorPicker();
        ((ImageView) view).setImageBitmap(Utils.createRoundImageSelected(Color.parseColor(view.getTag().toString()), selectionColour, ROUND_BITMAP_DIAMETER, ROUND_BITMAP_DIAMETER));
    }

    private void initColorPicker() {
        ivColour0.setImageBitmap(Utils.createRoundImage(ivColour0.getTag().toString(), ROUND_BITMAP_DIAMETER, ROUND_BITMAP_DIAMETER));
        ivColour1.setImageBitmap(Utils.createRoundImage(ivColour1.getTag().toString(), ROUND_BITMAP_DIAMETER, ROUND_BITMAP_DIAMETER));
        ivColour2.setImageBitmap(Utils.createRoundImage(ivColour2.getTag().toString(), ROUND_BITMAP_DIAMETER, ROUND_BITMAP_DIAMETER));
        ivColour3.setImageBitmap(Utils.createRoundImage(ivColour3.getTag().toString(), ROUND_BITMAP_DIAMETER, ROUND_BITMAP_DIAMETER));
        ivColour4.setImageBitmap(Utils.createRoundImage(ivColour4.getTag().toString(), ROUND_BITMAP_DIAMETER, ROUND_BITMAP_DIAMETER));
        ivColour5.setImageBitmap(Utils.createRoundImage(ivColour5.getTag().toString(), ROUND_BITMAP_DIAMETER, ROUND_BITMAP_DIAMETER));
        ivColour6.setImageBitmap(Utils.createRoundImage(ivColour6.getTag().toString(), ROUND_BITMAP_DIAMETER, ROUND_BITMAP_DIAMETER));
        ivColour7.setImageBitmap(Utils.createRoundImage(ivColour7.getTag().toString(), ROUND_BITMAP_DIAMETER, ROUND_BITMAP_DIAMETER));
        ivColour8.setImageBitmap(Utils.createRoundImage(ivColour8.getTag().toString(), ROUND_BITMAP_DIAMETER, ROUND_BITMAP_DIAMETER));
        ivColour9.setImageBitmap(Utils.createRoundImage(ivColour9.getTag().toString(), ROUND_BITMAP_DIAMETER, ROUND_BITMAP_DIAMETER));
        ivColour10.setImageBitmap(Utils.createRoundImage(ivColour10.getTag().toString(), ROUND_BITMAP_DIAMETER, ROUND_BITMAP_DIAMETER));
        ivColour11.setImageBitmap(Utils.createRoundImage(ivColour11.getTag().toString(), ROUND_BITMAP_DIAMETER, ROUND_BITMAP_DIAMETER));
    }

    @Click({R.id.ivColour0, R.id.ivColour1, R.id.ivColour2, R.id.ivColour3, R.id.ivColour4, R.id.ivColour5, R.id.ivColour6, R.id.ivColour7, R.id.ivColour8, R.id.ivColour9, R.id.ivColour10, R.id.ivColour11})
    void colorPicked(View clickedView) {
        setUpCustomColourAndPlayAnimation(clickedView);
    }

    @Click({R.id.mivSimple, R.id.mivLine, R.id.mivRectangle, R.id.mivTriangle, R.id.mivArrow, R.id.mivCircle})
    void shapePicked(View shape) {
        // tag of the shapes views = one of the ShapeType value
        int typeOfShape = Integer.parseInt(shape.getTag().toString());

        drawingSettings.setCurrentShape(typeOfShape);
        Notification.showSuccess(getActivity(), getStringMessageByTypesOfShape(typeOfShape));
        selectShape((MaterialIconView) shape);
    }

    @Click
    void rlDashed() {
        drawingSettings.setDashedState(!drawingSettings.getDashedState());
        listener.onSetUpDrawingShapesOkClicked(drawingSettings);
        playAnimationOnView(rlDashed);
    }

    @Click
    void rlFillShape() {
        drawingSettings.setFillInside(!drawingSettings.isFillInside());
        listener.onSetUpDrawingShapesOkClicked(drawingSettings);
        playAnimationOnView(rlFillShape);
    }

    @Click
    void rlDisplayLinesWhileDrawing() {
        drawingSettings.setDisplayLinesWhileDrawing(!drawingSettings.isDisplayLinesWhileDrawing());
        playAnimationOnView(rlDisplayLinesWhileDrawing);
        listener.onSetUpDrawingShapesOkClicked(drawingSettings);
    }
}
