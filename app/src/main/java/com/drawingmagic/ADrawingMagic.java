package com.drawingmagic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.drawingmagic.adapters.ViewPagerAdapter;
import com.drawingmagic.core.DrawingSettings;
import com.drawingmagic.core.DrawingView;
import com.drawingmagic.core.GPUImageFilterTools;
import com.drawingmagic.eventbus.Event;
import com.drawingmagic.fragments.FDrawingTools.OnChangeDrawingSettingsListener;
import com.drawingmagic.helpers.FilterItemHolder;
import com.drawingmagic.helpers.FrameProvider;
import com.drawingmagic.utils.Conditions;
import com.drawingmagic.utils.GraphicUtils;
import com.drawingmagic.utils.Logger;
import com.drawingmagic.utils.Notification;
import com.drawingmagic.utils.Utils;
import com.drawingmagic.views.ABSMenuApplyRestoreCancel_;
import com.drawingmagic.views.ViewMenuTop;
import com.drawingmagic.views.ViewMenuTop_;
import com.drawingmagic.views.abs.ActionBarView_;
import com.drawingmagic.views.menu.AdjusterMenu;
import com.drawingmagic.views.menu.AdjusterMenu_;
import com.drawingmagic.views.menu.CropperMenu;
import com.drawingmagic.views.menu.CropperMenu_;
import com.drawingmagic.views.menu.DrawingToolsMenu;
import com.drawingmagic.views.menu.DrawingToolsMenu_;
import com.drawingmagic.views.menu.SkewMenu;
import com.drawingmagic.views.menu.SkewMenu_;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.io.File;

import github.chenupt.springindicator.SpringIndicator;
import jp.co.cyberagent.android.gpuimage.GPUImageBulgeDistortionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSwirlFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

import static android.graphics.Bitmap.Config;
import static android.view.View.GONE;
import static android.view.View.LAYER_TYPE_SOFTWARE;
import static android.view.View.VISIBLE;
import static com.drawingmagic.GlobalConstants.DIVISION_COORDINATES_COEFFICIENT;
import static com.drawingmagic.GlobalConstants.HALF_MAX_PROGRESS;
import static com.drawingmagic.GlobalConstants.MAX_PROGRESS;
import static com.drawingmagic.GlobalConstants.MAX_TILT_FACTOR;
import static com.drawingmagic.adapters.ViewPagerAdapter.CANVAS_CROPPER_TOOLS_FRAGMENT;
import static com.drawingmagic.adapters.ViewPagerAdapter.CANVAS_TRANSFORMER_FRAGMENT;
import static com.drawingmagic.adapters.ViewPagerAdapter.DRAWING_TOOLS_FRAGMENT;
import static com.drawingmagic.adapters.ViewPagerAdapter.EFFECTS_TOOLS_FRAGMENT;
import static com.drawingmagic.core.DrawingView.GridType;
import static com.drawingmagic.core.DrawingView.OnTouchCanvasCallback;
import static com.drawingmagic.core.DrawingView.ShapesType;
import static com.drawingmagic.eventbus.Event.FLIP;
import static com.drawingmagic.eventbus.Event.MIRROR;
import static com.drawingmagic.eventbus.Event.ON_ABS_MENU_APPLY;
import static com.drawingmagic.eventbus.Event.ON_ABS_MENU_CANCEL;
import static com.drawingmagic.eventbus.Event.ON_ABS_MENU_CLICKED;
import static com.drawingmagic.eventbus.Event.ON_ABS_MENU_RESTORE;
import static com.drawingmagic.eventbus.Event.ON_ADJUSTER_VALUE_CHANGED;
import static com.drawingmagic.eventbus.Event.ON_ADJUST_FILTER_LEVEL;
import static com.drawingmagic.eventbus.Event.ON_APPLY_CROPPING;
import static com.drawingmagic.eventbus.Event.ON_APPLY_DRAWING_ON_CANVAS;
import static com.drawingmagic.eventbus.Event.ON_APPLY_EFFECT;
import static com.drawingmagic.eventbus.Event.ON_APPLY_FRAME;
import static com.drawingmagic.eventbus.Event.ON_CHANGE_CROPPING_SHAPE;
import static com.drawingmagic.eventbus.Event.ON_CLEAR_CANVAS;
import static com.drawingmagic.eventbus.Event.ON_FINAL_SAVE_IMAGE;
import static com.drawingmagic.eventbus.Event.ON_GRID_TYPE_CHANGED;
import static com.drawingmagic.eventbus.Event.ON_REDO;
import static com.drawingmagic.eventbus.Event.ON_RESTORE_IMAGE_BEFORE_CROPPING;
import static com.drawingmagic.eventbus.Event.ON_RESTORE_IMAGE_BEFORE_DRAWING;
import static com.drawingmagic.eventbus.Event.ON_ROTATE;
import static com.drawingmagic.eventbus.Event.ON_ROTATE_TRANSFORMATION;
import static com.drawingmagic.eventbus.Event.ON_SKEW_TRANSFORMATION;
import static com.drawingmagic.eventbus.Event.ON_TILT_FACTOR_X_CHANGED;
import static com.drawingmagic.eventbus.Event.ON_TILT_FACTOR_Y_CHANGED;
import static com.drawingmagic.eventbus.Event.ON_UNDO;
import static com.drawingmagic.utils.AnimationUtils.AnimationTechniques.FADE_IN;
import static com.drawingmagic.utils.AnimationUtils.AnimationTechniques.FLIP_IN_X;
import static com.drawingmagic.utils.AnimationUtils.animateSlow;
import static com.drawingmagic.utils.GraphicUtils.FLIP_HORIZONTAL;
import static com.drawingmagic.utils.GraphicUtils.FLIP_VERTICAL;
import static com.drawingmagic.utils.GraphicUtils.MIRROR_HORIZONTAL;
import static com.drawingmagic.utils.GraphicUtils.MIRROR_VERTICAL;
import static com.drawingmagic.utils.GraphicUtils.decodeSampledBitmapFromResource;
import static com.drawingmagic.utils.GraphicUtils.flip;
import static com.drawingmagic.utils.GraphicUtils.saveImageToGallery;
import static com.drawingmagic.views.HoverView.DRAWING_CACHE_QUALITY_HIGH;
import static com.drawingmagic.views.HoverView.MENU_ITEM_CAMERA;
import static com.drawingmagic.views.HoverView.MENU_ITEM_EMPTY_CANVAS;
import static com.drawingmagic.views.HoverView.MENU_ITEM_GALLERY;
import static com.theartofdev.edmodo.cropper.CropImageView.CropShape;
import static jp.co.cyberagent.android.gpuimage.GPUImage.ScaleType.CENTER_INSIDE;
import static jp.co.cyberagent.android.gpuimage.GPUImageView.OnPictureSavedListener;

/**
 * Project DrawingMagic.
 * Created by ihorkarpachev
 * On 13/09/15 at 17:44.
 */
@EActivity(R.layout.activity_drawing_magic)
public class ADrawingMagic extends SuperActivity implements OnChangeDrawingSettingsListener, OnPictureSavedListener, OnTouchCanvasCallback {

    @ViewById
    DrawingView drawingView;
    @ViewById
    ViewPager viewPager;
    @ViewById
    SpringIndicator viewPagerIndicator;
    @ViewById
    GPUImageView gpuImage;
    @ViewById
    CropImageView cropImageView;
    @Extra
    int selectedMenuItem;
    @ViewById
    RelativeLayout container, rlBottomMenuHolder;
    @StringRes(R.string.rotate)
    String rotatePicture;
    @StringRes(R.string.angle)
    String rotateAngle;


    /**
     * BITMAP_ORIGIN -------> BITMAP_MODIFIED -------> (DrawingView, TransformView, GPUEffects, CropView)
     */
    private Bitmap bitmapOrigin, bitmapModified;

    // View pager adapter
    private final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

    // Static final constants
    private static final int DEFAULT_BRUSH_SIZE = 3;
    public static final int REQUEST_PICK_IMAGE = 1001;
    private static final int DEFAULT_ASPECT_RATIO_VALUES = 20;
    private static final int SHOW_GUIDELINES_ALWAYS = 2;
    private ViewMenuTop menuViewTop;
    private GPUImageFilter currentFilter;
    private GPUImageFilterTools.FilterAdjuster filterAdjuster;


    private AdjusterMenu adjusterMenu;
    private DrawingToolsMenu drawingToolsMenu;
    private CropperMenu cropperMenu;
    private SkewMenu skewMenu;

    @StringRes(R.string.effect_adjuster)
    String effectAdjuster;
    private final int MAX_ROTATION_DEGREE_ONE_DIRECTION = 25;


    @AfterViews
    void afterViews() {
        Utils.configureCustomActionBar(getSupportActionBar(), ActionBarView_.build(this).withRightMenu(ABSMenuApplyRestoreCancel_.build(this)).withMenuButton());

        // init drawing view
        initDrawingView();

        // init view pager
        initViewPager();

        // for some unknown reason, if gpuImage had visibility GONE defined
        // in xml layout file, setImage method of GPUImageView doesn't not work
        gpuImage.setVisibility(GONE);
        gpuImage.setScaleType(CENTER_INSIDE);


        cropImageView.setGuidelines(SHOW_GUIDELINES_ALWAYS);
        cropImageView.setAspectRatio(DEFAULT_ASPECT_RATIO_VALUES, DEFAULT_ASPECT_RATIO_VALUES);
        cropImageView.setImageBitmap(bitmapModified);
        cropImageView.setCropShape(CropShape.RECTANGLE);

        switch (selectedMenuItem) {
            case MENU_ITEM_CAMERA:
                ActivityCamera_.intent(this).start();
                break;

            case MENU_ITEM_EMPTY_CANVAS:

                break;

            case MENU_ITEM_GALLERY:
                pickUpImageFromGallery();
                break;

            default:
                break;
        }


        createMenuViews();

        // Hide all views
        hideAllMenuViews();
        // Set clearing tools as a first
        drawingToolsMenu.setVisibility(VISIBLE);

        menuViewTop = configureAndAddTopMenu(container);
    }

    private void setupAdjuster(String adjusterTitle, String adjusterLabel, int value, int max, int eventId) {
        adjusterMenu.setAdjusterTitle(adjusterTitle);
        adjusterMenu.setSeekBarAdjusterTitle(adjusterLabel);
        adjusterMenu.setSeekBarCurrentAndMaxValues(value, max);
        adjusterMenu.setEventIdToBeSent(eventId);

    }

    private void createMenuViews() {

        // Create Drawing tools
        drawingToolsMenu = DrawingToolsMenu_.build(this);

        // Create rotate adjuster
        adjusterMenu = AdjusterMenu_.build(this);
        setupAdjuster(rotateAngle, rotatePicture, MAX_ROTATION_DEGREE_ONE_DIRECTION / 2, MAX_ROTATION_DEGREE_ONE_DIRECTION, ON_ROTATE);

        // Create Skew menu
        skewMenu = SkewMenu_.build(this);

        // Create cropper
        cropperMenu = CropperMenu_.build(this);

        // Add all views to view holder
        rlBottomMenuHolder.addView(drawingToolsMenu);
        rlBottomMenuHolder.addView(adjusterMenu);
        rlBottomMenuHolder.addView(skewMenu);
        rlBottomMenuHolder.addView(cropperMenu);
    }

    private void hideAllMenuViews() {
        drawingToolsMenu.setVisibility(View.GONE);
        adjusterMenu.setVisibility(View.GONE);
        skewMenu.setVisibility(View.GONE);
        cropperMenu.setVisibility(View.GONE);
    }


    private ViewMenuTop configureAndAddTopMenu(ViewGroup container) {
        ViewMenuTop view = ViewMenuTop_.build(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        view.setLayoutParams(params);
        container.addView(view);
        return view;
    }

    /**
     * Init View Pager
     */
    private void initViewPager() {
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(viewPagerAdapter.getCount());
        viewPagerIndicator.setViewPager(viewPager);
        viewPagerIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                                       @Override
                                                       public void onPageScrolled(int i, float v, int i1) {

                                                       }

                                                       @Override
                                                       public void onPageSelected(int position) {
                                                           switch (position) {
                                                               case DRAWING_TOOLS_FRAGMENT:
                                                                   drawingView.setVisibility(VISIBLE);
                                                                   gpuImage.setVisibility(GONE);
                                                                   menuViewTop.setVisibility(VISIBLE);
                                                                   cropImageView.setVisibility(GONE);
                                                                   drawingView.setDrawingEnabled(true);
                                                                   // Hide all views
                                                                   hideAllMenuViews();
                                                                   // Set clearing tools as a first
                                                                   drawingToolsMenu.setVisibility(VISIBLE);
                                                                   break;

                                                               case CANVAS_TRANSFORMER_FRAGMENT:
                                                                   // TODO: 27/09/15 Remember selected transform tools and display this fragment
                                                                   // Hide all views
                                                                   hideAllMenuViews();
                                                                   adjusterMenu.setVisibility(VISIBLE);
                                                                   //////////////////////////////////
                                                                   drawingView.setVisibility(VISIBLE);
                                                                   gpuImage.setVisibility(GONE);
                                                                   cropImageView.setVisibility(GONE);
                                                                   menuViewTop.setVisibility(GONE);
                                                                   drawingView.setDrawingEnabled(false);
                                                                   drawingView.setGridType(GridType.NO_GRID);
                                                                   break;

                                                               case EFFECTS_TOOLS_FRAGMENT:
                                                                   drawingView.setVisibility(GONE);
                                                                   gpuImage.setVisibility(VISIBLE);
                                                                   cropImageView.setVisibility(GONE);
                                                                   menuViewTop.setVisibility(GONE);
                                                                   drawingView.setDrawingEnabled(true);
                                                                   hideAllMenuViews();
                                                                   if (Conditions.isNotNull(currentFilter)) {
                                                                       //// TODO: 26/10/2015 Filter name
                                                                       setupAdjuster("TODO !", "TODO 2", MAX_PROGRESS >>> 1, MAX_PROGRESS, ON_ADJUST_FILTER_LEVEL);
                                                                   }
                                                                   break;

                                                               case CANVAS_CROPPER_TOOLS_FRAGMENT:
                                                                   drawingView.setVisibility(GONE);
                                                                   gpuImage.setVisibility(GONE);
                                                                   menuViewTop.setVisibility(GONE);
                                                                   cropImageView.setVisibility(VISIBLE);
                                                                   drawingView.setDrawingEnabled(true);
                                                                   cropImageView.setImageBitmap(bitmapModified);
                                                                   hideAllMenuViews();
                                                                   cropperMenu.setVisibility(VISIBLE);
                                                                   break;

                                                               default:
                                                                   Logger.e("onPageSelected: Unknown page position  : " + viewPager.getCurrentItem());
                                                                   break;

                                                           }
                                                       }

                                                       @Override
                                                       public void onPageScrollStateChanged(int i) {

                                                       }
                                                   }
        );
    }

    /**
     * cancel clicked from ABS menu
     * Reset all transformation from current selected fragment
     */
    private void cancelCurrentTransformation() {
        switch (viewPager.getCurrentItem()) {
            // Cancel any drawing transformation
            case DRAWING_TOOLS_FRAGMENT:
                drawingView.resetAllTransformation();
                drawingView.clearRedoPaths();
                drawingView.setDrawingData(drawingView.builder().from(drawingView.getDrawingData()).withBitmap(bitmapModified).withPaths(null).build());
                break;

            // Cancel any effect
            case EFFECTS_TOOLS_FRAGMENT:
                applyEffect(null);
                break;

            // Cancel cropping
            case CANVAS_CROPPER_TOOLS_FRAGMENT:
                Bitmap croppedBitmap = bitmapModified;
                cropImageView.setImageBitmap(croppedBitmap);
                break;

            default:
                break;
        }
    }

    private void restoreOriginalImageBeforeTransformation() {
        switch (viewPager.getCurrentItem()) {
            // Cancel any drawing transformation
            case DRAWING_TOOLS_FRAGMENT:
                bitmapModified = bitmapOrigin.copy(Config.RGB_565, true);
                onApplyImageTransformationChanges();
                break;

            // Cancel any effect
            case EFFECTS_TOOLS_FRAGMENT:
                Notification.showError(this, "TODO ");
                break;

            // Cancel cropping
            case CANVAS_CROPPER_TOOLS_FRAGMENT:
                cropImageView.setImageBitmap(bitmapModified);
                break;

            default:
                break;
        }
    }

    private void onApplyImageTransformationChanges() {
        int gridType = drawingView.getDrawingData().getShape().getGridType();

        switch (viewPager.getCurrentItem()) {
            case DRAWING_TOOLS_FRAGMENT:
                drawingView.setGridType(GridType.NO_GRID);
                drawingView.setDrawingData(drawingView.builder().from(drawingView.getDrawingData()).withBitmap(bitmapOrigin).build());
                bitmapModified = drawingView.getDrawingCache().copy(Config.RGB_565, true);
                drawingView.setGridType(gridType);
                break;

            case CANVAS_TRANSFORMER_FRAGMENT:
                bitmapModified = drawingView.getDrawingCache().copy(Config.RGB_565, true);
                //drawingView.setGridType(gridType);
                drawingView.resetAllTransformation();
                break;

            case EFFECTS_TOOLS_FRAGMENT:
                bitmapModified = gpuImage.getGPUImage().getBitmapWithFilterApplied();
                applyEffect(null);
                break;

            case CANVAS_CROPPER_TOOLS_FRAGMENT:
                // // TODO: 02/10/2015 direct access to field
                viewPagerAdapter.getCropperToolsFragment().ivFinalImage.setImageBitmap(cropImageView.getCropShape() == CropShape.RECTANGLE ? cropImageView.getCroppedImage() : cropImageView.getCroppedOvalImage());
                break;

            default:
                break;
        }

        drawingView.setDrawingData(drawingView.builder().from(drawingView.getDrawingData()).withBitmap(bitmapModified).build());

        gpuImage.getGPUImage().deleteImage();
        gpuImage.setImage(bitmapModified);
    }

    /**
     * Init Drawing View
     */
    private void initDrawingView() {
        drawingView.setDrawingCacheEnabled(true);
        drawingView.setDrawingCacheQuality(DRAWING_CACHE_QUALITY_HIGH);
        drawingView.setLayerType(LAYER_TYPE_SOFTWARE, null);

        drawingView.setDrawingData(drawingView.builder().from(drawingView.getDrawingData()).
                withBrushWidth(DEFAULT_BRUSH_SIZE, getResources().getDisplayMetrics()).
                withLinesWhileDrawing(false).
                withShape(ShapesType.STANDARD_DRAWING).
                withColor(Color.BLACK).
                withGridEnabled(true).build());
        drawingView.setListener(this);
    }

    private void adjustFilter(int progress) {
        if (Conditions.isNotNull(filterAdjuster)) {
            filterAdjuster.adjust(progress);
        }
        gpuImage.requestRender();
    }

    private void switchFilterTo(final GPUImageFilter filter, String filterName) {
        if (Conditions.isNull(currentFilter) || (!currentFilter.getClass().equals(filter.getClass()))) {
            currentFilter = filter;
            gpuImage.setFilter(currentFilter);
            filterAdjuster = new GPUImageFilterTools.FilterAdjuster(currentFilter);
            setupAdjuster(filterName ,filterName, MAX_PROGRESS >>> 1, MAX_PROGRESS,ON_ADJUST_FILTER_LEVEL );
            adjusterMenu.setVisibility(filterAdjuster.canAdjust() ? VISIBLE : GONE);
        }
    }


    @OnActivityResult(REQUEST_PICK_IMAGE)
    void onResult(int resultCode, final Intent data) {

        if (resultCode != RESULT_OK) {
            finish();
            return;
        }

        try {
            drawingView.post(new Runnable() {
                @Override
                public void run() {
                    if (Conditions.isNotNull(bitmapOrigin)) {
                        bitmapOrigin.recycle();
                    }

                    if (Conditions.isNotNull(bitmapModified)) {
                        bitmapModified.recycle();
                    }

                    Logger.e("FilePath:" + Utils.getRealPathFromURI(ADrawingMagic.this, data.getData()));
                    bitmapOrigin = decodeSampledBitmapFromResource(new File(Utils.getRealPathFromURI(ADrawingMagic.this, data.getData())).getAbsolutePath(), drawingView.getWidth(), drawingView.getHeight());
                    bitmapModified = bitmapOrigin.copy(Config.RGB_565, true);
                    drawingView.setDrawingData(drawingView.builder().from(drawingView.getDrawingData()).withBitmap(bitmapModified).withPaths(null).build());
                }
            });
        } catch (Exception ex) {
            Logger.e(ex);
        }
        gpuImage.setImage(data.getData());

    }

    @Touch
    void gpuImage(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_DOWN:
                float x = (motionEvent.getX() / DIVISION_COORDINATES_COEFFICIENT);
                float y = (motionEvent.getY() / DIVISION_COORDINATES_COEFFICIENT);

                //  TODO: 23/10/2015 Replace with common adjustable interface
                if (currentFilter instanceof GPUImageSwirlFilter) {
                    ((GPUImageSwirlFilter) currentFilter).setCenter(new PointF(x, y));
                }

                if (currentFilter instanceof GPUImageBulgeDistortionFilter) {
                    ((GPUImageBulgeDistortionFilter) currentFilter).setCenter(new PointF(x, y));
                }
                Logger.e(String.format("x:%s y:%s", x, y));
                gpuImage.requestRender();

                break;

            default:
                break;
        }
        // Something Here
    }


    @Override
    public void onSetUpDrawingShapesOkClicked(DrawingSettings shape) {
        drawingView.setDrawingData(drawingView.builder().from(drawingView.getDrawingData()).withShape(shape).build());
    }

    private void applyEffect(FilterItemHolder filterItemHolder) {
        // User pressed X
        if (Conditions.isNull(filterItemHolder)) {
            // equivalent to restore default image
            gpuImage.setFilter(new GPUImageFilter());
            return;
        }

        switchFilterTo(GPUImageFilterTools.createFilterForType(this, filterItemHolder.getFilter()), filterItemHolder.getFilterName());
        gpuImage.requestRender();
        Notification.showSuccess(this, filterItemHolder.getFilterName());
    }

    private void saveImage() {
        //// TODO: 05/10/2015 String to resources
        String fileName = String.format("Drawing magic %s", System.currentTimeMillis() + ".jpg");
        Bitmap finalImage = cropImageView.getCropShape() == CropShape.RECTANGLE ? cropImageView.getCroppedImage() : cropImageView.getCroppedOvalImage();
        String savedToPath = saveImageToGallery(getContentResolver(), finalImage, fileName, "Drawing Magic");
        Utils.shareImage(this, savedToPath);
        Notification.showSuccess(this, String.format("Successfully saved to %s", savedToPath));
    }

    @Override
    public void onPictureSaved(Uri uri) {
        Notification.showSuccess(this, "Saved: " + uri.toString());
    }

    /**
     * Flip image by X or Y axis
     *
     * @param direction flip direction, Horizontal or Vertical
     */
    private void flipImage(int direction) {
        drawingView.setDrawingData(drawingView.builder().from(drawingView.getDrawingData()).withBitmap(flip(drawingView.getDrawingData().getCanvasBitmap(), (direction == FLIP_HORIZONTAL) ? direction : FLIP_VERTICAL)).build());
        animateSlow(drawingView, direction == FLIP_HORIZONTAL ? FADE_IN : FLIP_IN_X);
    }

    private void mirrorImage(int direction) {
        if (direction == MIRROR_VERTICAL) {
            drawingView.setDrawingData(drawingView.builder().from(drawingView.getDrawingData()).withBitmap(GraphicUtils.applyReflection(drawingView.getDrawingData().getCanvasBitmap(), MIRROR_VERTICAL)).build());
        }

        if (direction == MIRROR_HORIZONTAL) {
            drawingView.setDrawingData(drawingView.builder().from(drawingView.getDrawingData()).withBitmap(GraphicUtils.applyReflection(drawingView.getDrawingData().getCanvasBitmap(), MIRROR_HORIZONTAL)).build());
        }
    }


    @Override
    public void onEventMainThread(Event event) {
        Logger.e(event.toString());
        switch (event.type) {
            case FLIP:
                flipImage((int) event.payload);
                break;

            case MIRROR:
                mirrorImage((int) event.payload);
                break;

            case ON_RESTORE_IMAGE_BEFORE_DRAWING:
                restoreOriginalImageBeforeTransformation();
                break;

            case ON_APPLY_DRAWING_ON_CANVAS:
                onApplyImageTransformationChanges();
                break;

            case ON_FINAL_SAVE_IMAGE:
                saveImage();
                break;

            case ON_GRID_TYPE_CHANGED:
                Logger.e("SET GRID TYPE " + event.payload);
                drawingView.setGridType((Integer) event.payload);
                break;

            case ON_CHANGE_CROPPING_SHAPE:
                invertCroppingShape();
                break;
            case ON_SKEW_TRANSFORMATION:
                hideAllMenuViews();
                skewMenu.setVisibility(VISIBLE);
                break;

            case ON_ROTATE_TRANSFORMATION:
                hideAllMenuViews();
                adjusterMenu.setVisibility(VISIBLE);
                break;

            case ON_ABS_MENU_APPLY:
                onApplyImageTransformationChanges();
                break;

            case ON_ABS_MENU_RESTORE:
                restoreOriginalImageBeforeTransformation();
                break;
            case ON_APPLY_FRAME:
                drawingView.getDrawingData().setFrame((FrameProvider) event.payload);
                drawingView.invalidate();
                break;
            case ON_ABS_MENU_CANCEL:
                cancelCurrentTransformation();
                break;

            case ON_ABS_MENU_CLICKED:
                onBackPressed();
                break;

            case ON_ADJUST_FILTER_LEVEL:
                adjustFilter((int) event.payload);
                Logger.e("  ON_ADJUST_FILTER_LEVEL : " + (int) event.payload);
                break;


            case ON_APPLY_EFFECT:
                applyEffect((FilterItemHolder) event.payload);
                Logger.e("  ON_APPLY_EFFECT : " + ((FilterItemHolder) event.payload).getFilterName());
                break;


            case ON_RESTORE_IMAGE_BEFORE_CROPPING:
                cropImageView.setImageBitmap(bitmapModified);
                break;


            case ON_APPLY_CROPPING:
                onApplyImageTransformationChanges();
                break;


            case ON_ADJUSTER_VALUE_CHANGED:
                //drawingView.setRotationDegree((int) event.payload);
                Logger.e("  ON_ADJUSTER_VALUE_CHANGED : " + (int) event.payload);
                break;


            case ON_TILT_FACTOR_X_CHANGED:
                float tiltFactorX = (int) event.payload;
                tiltFactorX = tiltFactorX > (HALF_MAX_PROGRESS) ? (tiltFactorX % HALF_MAX_PROGRESS / MAX_PROGRESS) : ((tiltFactorX - HALF_MAX_PROGRESS) / MAX_PROGRESS);
                drawingView.setTiltFactorX((int) event.payload == MAX_PROGRESS ? MAX_TILT_FACTOR : tiltFactorX);
                Logger.e("  ON_TILT_FACTOR_X_CHANGED : " + tiltFactorX);
                break;


            case ON_TILT_FACTOR_Y_CHANGED:
                float tiltFactorY = (int) event.payload;
                tiltFactorY = tiltFactorY > HALF_MAX_PROGRESS ? (tiltFactorY % HALF_MAX_PROGRESS / MAX_PROGRESS) : ((tiltFactorY - HALF_MAX_PROGRESS) / MAX_PROGRESS);
                drawingView.setTiltFactorY((int) event.payload == MAX_PROGRESS ? MAX_TILT_FACTOR : tiltFactorY);
                Logger.e("  ON_TILT_FACTOR_Y_CHANGED : " + tiltFactorY);
                break;


            case ON_UNDO:
                drawingView.undo();
                onApplyImageTransformationChanges();
                break;


            case ON_REDO:
                drawingView.redo();
                onApplyImageTransformationChanges();
                break;


            case ON_CLEAR_CANVAS:
                drawingView.clearCanvas();
                onApplyImageTransformationChanges();
                break;


            case ON_ROTATE:
                int progress = (int) event.payload;
                int realProgress = progress - MAX_ROTATION_DEGREE_ONE_DIRECTION;
                setRotationDegree(realProgress, true);
                break;


            default:
                Logger.e("Unknown event " + event);
        }

    }

    private void invertCroppingShape() {
        cropImageView.setCropShape(cropImageView.getCropShape() == CropShape.OVAL ? CropShape.RECTANGLE : CropShape.OVAL);
    }

    private void pickUpImageFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, ADrawingMagic.REQUEST_PICK_IMAGE);
    }


    public void setRotationDegree(float degree, boolean zoomIn) {
        drawingView.setRotationDegree(degree, zoomIn);
    }


    @Override
    public void userHasReleasedFinger() {
        //onApplyImageTransformationChanges();
    }
}