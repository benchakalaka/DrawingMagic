package com.drawingmagic.fragments;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.drawingmagic.R;
import com.drawingmagic.SuperActivity;
import com.drawingmagic.core.DrawingSettings;
import com.drawingmagic.core.DrawingView;
import com.drawingmagic.utils.AnimationUtils;
import com.drawingmagic.utils.Utils;

import net.steamcrafted.materialiconlib.MaterialIconView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

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

    // default values for round bitmap (background for imageviews)
    private static final int ROUND_BITMAP_WIDTH = 50;
    private static final int ROUND_BITMAP_HEIGHT = 50;

    // String resources
    @StringRes(R.string.draw_arrow)
    static String drawArrow;
    @StringRes(R.string.draw_rect)
    static String drawRect;
    @StringRes(R.string.draw_triangle)
    static String drawTriangle;
    @StringRes(R.string.draw_circle)
    static String drawCircle;
    @StringRes(R.string.draw_line)
    static String drawLine;
    @StringRes(R.string.free_drawing)
    static String freeDrawing;


    private final DrawingSettings drawingSettings = new DrawingSettings();
    private OnChangeDrawingSettingsListener listener;
    @ViewById
    MaterialIconView ivChangeBrushSize, ivSimple, ivLine, ivRectangle;
    @ViewById
    ImageView ivLineSelected, ivRectangleSelected, ivTriangle, ivTriangleSelected, ivCircle, ivArrow, ivCircleSelected, ivArrowSelected, ivFullGridSelected, ivPartlyGridSelected, ivNoGridSelected,
            ivColour0, ivColour1, ivColour2, ivColour3, ivColour4, ivColour5, ivColour6, ivColour7, ivColour8, ivColour9, ivColour10, ivColour11, ivSimpleSelected, ivCustomColour;
    @ViewById
    TextView tvTitle;
    @ViewById
    RelativeLayout rlDashed, rlStandardDrawing, rlFillShape, rlDisplayLinesWhileDrawing, rlLine, rlRectangle, rlTriangle, rlCircle, rlBrushSize, rlArrow, rlColourPicker, rlNoGrid, rlPartlyGrid, rlFullGrid;
    @ViewById
    SeekBar sbBrushSize;
    @ViewById
    CheckBox cbFillShapeInside, cbDisplayLinesWhileDrawing, cbDashed;

    @ViewById
    LinearLayout llTypeOfShapes, llCustomColours, llBrushSize, llGridTypeHeader, llGridType;

    @AfterViews
    void afterViews() {

        // check activity for inheritance from OnSelectTypeOfShapeListener
        try {
            this.listener = (OnChangeDrawingSettingsListener) getActivity();
        } catch (ClassCastException ex) {
            throw new ClassCastException(getActivity().getLocalClassName() + "must implement OnSelectTypeOfShapeListener");
        }

        cbDashed.setChecked(false);
        selectViewByTypesOfGrid(GridType.FULL_GRID);
        cbDisplayLinesWhileDrawing.setChecked(true);
        // Fill shape inside view
        cbFillShapeInside.setChecked(false);


        // set current brush size
        sbBrushSize.setProgress(5);
        sbBrushSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                drawingSettings.setBrushWidth(progress, (getActivity()).getResources().getDisplayMetrics());
                listener.onSetUpDrawingShapesOkClicked(drawingSettings);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        // color picker view
        ivCustomColour.setImageBitmap(Utils.createRoundImage(Color.BLUE, 50, 50));

        ivColour0.setImageBitmap(Utils.createRoundImage(ivColour0.getTag().toString(), ROUND_BITMAP_WIDTH, ROUND_BITMAP_HEIGHT));
        ivColour1.setImageBitmap(Utils.createRoundImage(ivColour1.getTag().toString(), ROUND_BITMAP_WIDTH, ROUND_BITMAP_HEIGHT));
        ivColour2.setImageBitmap(Utils.createRoundImage(ivColour2.getTag().toString(), ROUND_BITMAP_WIDTH, ROUND_BITMAP_HEIGHT));
        ivColour3.setImageBitmap(Utils.createRoundImage(ivColour3.getTag().toString(), ROUND_BITMAP_WIDTH, ROUND_BITMAP_HEIGHT));
        ivColour4.setImageBitmap(Utils.createRoundImage(ivColour4.getTag().toString(), ROUND_BITMAP_WIDTH, ROUND_BITMAP_HEIGHT));
        ivColour5.setImageBitmap(Utils.createRoundImage(ivColour5.getTag().toString(), ROUND_BITMAP_WIDTH, ROUND_BITMAP_HEIGHT));
        ivColour6.setImageBitmap(Utils.createRoundImage(ivColour6.getTag().toString(), ROUND_BITMAP_WIDTH, ROUND_BITMAP_HEIGHT));
        ivColour7.setImageBitmap(Utils.createRoundImage(ivColour7.getTag().toString(), ROUND_BITMAP_WIDTH, ROUND_BITMAP_HEIGHT));
        ivColour8.setImageBitmap(Utils.createRoundImage(ivColour8.getTag().toString(), ROUND_BITMAP_WIDTH, ROUND_BITMAP_HEIGHT));
        ivColour9.setImageBitmap(Utils.createRoundImage(ivColour9.getTag().toString(), ROUND_BITMAP_WIDTH, ROUND_BITMAP_HEIGHT));
        ivColour10.setImageBitmap(Utils.createRoundImage(ivColour10.getTag().toString(), ROUND_BITMAP_WIDTH, ROUND_BITMAP_HEIGHT));
        ivColour11.setImageBitmap(Utils.createRoundImage(ivColour11.getTag().toString(), ROUND_BITMAP_WIDTH, ROUND_BITMAP_HEIGHT));
    }


    public void setUpDrawingView(DrawingView drawingView, SuperActivity activity) {
        drawingSettings.setBrushWidth(drawingView.getDrawingData().getShape().getBrushWidth(), activity.getResources().getDisplayMetrics());
        drawingSettings.setFillInside(drawingView.getDrawingData().getShape().isFillInside());
        drawingSettings.setCurrentColour(drawingView.getDrawingData().getShape().getCurrentColour());
        drawingSettings.setDashedState(drawingView.getDrawingData().getShape().getDashedState());
        drawingSettings.setGridType(drawingView.getDrawingData().getShape().getGridType());
        drawingSettings.setCurrentShape(drawingView.getDrawingData().getShape().getCurrentShape());
        drawingSettings.setDisplayLinesWhileDrawing(drawingView.getDrawingData().getShape().isDisplayLinesWhileDrawing());
    }

    public FDrawingTools() {


    }

    private void playAnimationOnViewAndUnselectAllShapes(View target) {
        AnimationUtils.animate(target, AnimationTechniques.BOUNCE_IN_UP);
        unselectAllTypesOfShapes();
    }

    private void playAnimationOnViewAndUnselectAllGrids(View target) {
        AnimationUtils.animate(target, AnimationTechniques.BOUNCE_IN_UP);
        unselectAllTypeOfGrids();
    }

    /**
     * Hide all iv*Selected
     */
    private void unselectAllTypesOfShapes() {
        ivSimpleSelected.setVisibility(View.INVISIBLE);
        ivLineSelected.setVisibility(View.INVISIBLE);
        ivRectangleSelected.setVisibility(View.INVISIBLE);
        ivTriangleSelected.setVisibility(View.INVISIBLE);
        ivCircleSelected.setVisibility(View.INVISIBLE);
        ivArrowSelected.setVisibility(View.INVISIBLE);
    }

    private void unselectAllTypeOfGrids() {
        ivNoGridSelected.setVisibility(View.INVISIBLE);
        ivFullGridSelected.setVisibility(View.INVISIBLE);
        ivPartlyGridSelected.setVisibility(View.INVISIBLE);
    }

    private void selectViewByTypeOfGrid(int typeOfGrid) {
        switch (typeOfGrid) {
            case GridType.NO_GRID:
                ivNoGridSelected.setVisibility(View.VISIBLE);
                break;

            case GridType.FULL_GRID:
                ivFullGridSelected.setVisibility(View.VISIBLE);
                break;

            case GridType.PARTLY_GRID:
                ivPartlyGridSelected.setVisibility(View.VISIBLE);
                break;
        }
        drawingSettings.setGridType(typeOfGrid);
        listener.onSetUpDrawingShapesOkClicked(drawingSettings);
    }

    private void selectViewByTypesOfGrid(int gridType) {
        switch (gridType) {
            case GridType.NO_GRID:
                ivNoGridSelected.setVisibility(View.VISIBLE);
                rlNoGrid.performClick();
                break;

            case GridType.FULL_GRID:
                ivFullGridSelected.setVisibility(View.VISIBLE);
                rlFullGrid.performClick();
                break;

            case GridType.PARTLY_GRID:
                rlPartlyGrid.performClick();
                ivPartlyGridSelected.setVisibility(View.VISIBLE);
                break;
        }
    }


    private void selectViewByTypesOfShape(int typeOfShape) {
        listener.onSetUpDrawingShapesOkClicked(drawingSettings);
        switch (typeOfShape) {
            case ShapesType.ARROW:
                ivArrowSelected.setVisibility(View.VISIBLE);
                break;

            case ShapesType.CIRCLE:
                ivCircleSelected.setVisibility(View.VISIBLE);
                break;

            case ShapesType.LINE:
                ivLineSelected.setVisibility(View.VISIBLE);
                break;

            case ShapesType.RECTANGLE:
                ivRectangleSelected.setVisibility(View.VISIBLE);
                break;

            case ShapesType.STANDARD_DRAWING:
                ivSimpleSelected.setVisibility(View.VISIBLE);
                break;

            case ShapesType.TRIANGLE:
                ivTriangleSelected.setVisibility(View.VISIBLE);
                break;
        }
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
    public static String getStringMessageByTypesOfShape(int typeOfShape) {
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
        String colour = view.getTag().toString();
        ivCustomColour.setImageBitmap(Utils.createRoundImage(colour, ROUND_BITMAP_WIDTH, ROUND_BITMAP_HEIGHT));
        AnimationUtils.animate(ivCustomColour, AnimationTechniques.FADE_IN);
        AnimationUtils.animate(view, AnimationTechniques.FADE_IN);
        drawingSettings.setCurrentColour(Color.parseColor(colour));
        listener.onSetUpDrawingShapesOkClicked(drawingSettings);
    }

    @Click
    void ivColour0() {
        setUpCustomColourAndPlayAnimation(ivColour0);
    }

    @Click
    void ivColour1() {
        setUpCustomColourAndPlayAnimation(ivColour1);
    }

    @Click
    void ivColour2() {
        setUpCustomColourAndPlayAnimation(ivColour2);
    }

    @Click
    void ivColour3() {
        setUpCustomColourAndPlayAnimation(ivColour3);
    }

    @Click
    void ivColour4() {
        setUpCustomColourAndPlayAnimation(ivColour4);
    }

    @Click
    void ivColour5() {
        setUpCustomColourAndPlayAnimation(ivColour5);
    }

    @Click
    void ivColour6() {
        setUpCustomColourAndPlayAnimation(ivColour6);
    }

    @Click
    void ivColour7() {
        setUpCustomColourAndPlayAnimation(ivColour7);
    }

    @Click
    void ivColour8() {
        setUpCustomColourAndPlayAnimation(ivColour8);
    }

    @Click
    void ivColour9() {
        setUpCustomColourAndPlayAnimation(ivColour9);
    }

    @Click
    void ivColour10() {
        setUpCustomColourAndPlayAnimation(ivColour10);
    }

    @Click
    void ivColour11() {
        setUpCustomColourAndPlayAnimation(ivColour11);
    }


    @CheckedChange
    void cbDashed(CompoundButton cb, boolean isChecked) {
        drawingSettings.setDashedState(isChecked);
        listener.onSetUpDrawingShapesOkClicked(drawingSettings);
    }

    @Click
    void rlDashed() {
        AnimationUtils.animate(rlDashed, AnimationTechniques.BOUNCE_IN);
        cbDashed.performClick();
    }

    @CheckedChange
    void cbFillShapeInside(CompoundButton cb, boolean isChecked) {
        drawingSettings.setFillInside(isChecked);
        listener.onSetUpDrawingShapesOkClicked(drawingSettings);
    }

    @CheckedChange
    void cbDisplayLinesWhileDrawing(CompoundButton cb, boolean isChecked) {
        drawingSettings.setDisplayLinesWhileDrawing(isChecked);
        listener.onSetUpDrawingShapesOkClicked(drawingSettings);
    }


    @Click
    void rlDisplayLinesWhileDrawing() {
        AnimationUtils.animate(rlDisplayLinesWhileDrawing, AnimationTechniques.BOUNCE_IN);
        cbDisplayLinesWhileDrawing.performClick();
    }

    @Click
    void rlFillShape() {
        AnimationUtils.animate(rlFillShape, AnimationTechniques.BOUNCE_IN);
        cbFillShapeInside.performClick();
    }

    @Click
    void ivSimple() {
        playAnimationOnViewAndUnselectAllShapes(ivSimple);
        drawingSettings.setCurrentShape(ShapesType.STANDARD_DRAWING);
        selectViewByTypesOfShape(ShapesType.STANDARD_DRAWING);
    }

    @Click
    void ivLine() {
        playAnimationOnViewAndUnselectAllShapes(ivLine);
        drawingSettings.setCurrentShape(ShapesType.LINE);
        selectViewByTypesOfShape(ShapesType.LINE);
    }

    @Click
    void ivRectangle() {
        playAnimationOnViewAndUnselectAllShapes(ivRectangle);
        drawingSettings.setCurrentShape(ShapesType.RECTANGLE);
        selectViewByTypesOfShape(ShapesType.RECTANGLE);
    }

    @Click
    void ivTriangle() {
        playAnimationOnViewAndUnselectAllShapes(ivTriangle);
        drawingSettings.setCurrentShape(ShapesType.TRIANGLE);
        selectViewByTypesOfShape(ShapesType.TRIANGLE);
    }

    @Click
    void ivCircle() {
        playAnimationOnViewAndUnselectAllShapes(ivCircle);
        drawingSettings.setCurrentShape(ShapesType.CIRCLE);
        selectViewByTypesOfShape(ShapesType.CIRCLE);
    }

    @Click
    void ivArrow() {
        playAnimationOnViewAndUnselectAllShapes(ivArrow);
        drawingSettings.setCurrentShape(ShapesType.ARROW);
        selectViewByTypesOfShape(ShapesType.ARROW);
    }

    @Click
    void rlNoGrid() {
        playAnimationOnViewAndUnselectAllGrids(rlNoGrid);
        drawingSettings.setGridType(GridType.NO_GRID);
        selectViewByTypeOfGrid(GridType.NO_GRID);
    }

    @Click
    void rlPartlyGrid() {
        playAnimationOnViewAndUnselectAllGrids(rlPartlyGrid);
        drawingSettings.setGridType(GridType.PARTLY_GRID);
        selectViewByTypeOfGrid(GridType.PARTLY_GRID);
    }

    @Click
    void rlFullGrid() {
        playAnimationOnViewAndUnselectAllGrids(rlFullGrid);
        drawingSettings.setGridType(GridType.FULL_GRID);
        selectViewByTypeOfGrid(GridType.FULL_GRID);
    }


//    // Inflate the view for the fragment based on layout XML
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.hover_sample, container, false);
//        TextView tvLabel = (TextView) view.findViewById(R.id.description);
//        tvLabel.setText(page + " -- " + title);
//        return view;
//    }
}
