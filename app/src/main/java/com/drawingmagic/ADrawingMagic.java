package com.drawingmagic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.drawingmagic.adapters.ViewPagerAdapter;
import com.drawingmagic.core.DrawingSettings;
import com.drawingmagic.core.DrawingView;
import com.drawingmagic.core.GPUImageFilterTools;
import com.drawingmagic.eventbus.Event;
import com.drawingmagic.fragments.FDrawingTools.OnChangeDrawingSettingsListener;
import com.drawingmagic.fragments.FMenuAdjuster_;
import com.drawingmagic.fragments.FMenuCropper_;
import com.drawingmagic.fragments.FMenuDrawingTools_;
import com.drawingmagic.fragments.FTiltFragmentController_;
import com.drawingmagic.helpers.FilterItemHolder;
import com.drawingmagic.utils.Conditions;
import com.drawingmagic.utils.GraphicUtils;
import com.drawingmagic.utils.Logger;
import com.drawingmagic.utils.Notification;
import com.drawingmagic.utils.Utils;
import com.drawingmagic.views.ABSMenuApplyRestoreCancel_;
import com.drawingmagic.views.abs.ActionBarView_;
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
import static com.drawingmagic.eventbus.Event.ON_CHANGE_CROPPING_SHAPE;
import static com.drawingmagic.eventbus.Event.ON_CLEAR_CANVAS;
import static com.drawingmagic.eventbus.Event.ON_FINAL_SAVE_IMAGE;
import static com.drawingmagic.eventbus.Event.ON_FINISHED_ROTATION;
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
    @ViewById
    FrameLayout flFragmentHolder;
    @Extra
    int selectedMenuItem;

    @StringRes(R.string.rotate)
    String rotatePicture;

    @StringRes(R.string.angle)
    String rotateAngle;


    /**
     * bitmapOrigin -------> bitmapModified -------> (DrawingView, TransformView, GPUEffects, CropView)
     */
    private Bitmap bitmapOrigin, bitmapModified;

    // View pager adapter
    private final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

    // Static final constants
    private static final int DEFAULT_BRUSH_SIZE = 3;
    public static final int REQUEST_PICK_IMAGE = 1001;
    private static final int DEFAULT_ASPECT_RATIO_VALUES = 20;
    private static final int SHOW_GUIDELINES_ALWAYS = 2;

    private GPUImageFilter currentFilter;
    private GPUImageFilterTools.FilterAdjuster filterAdjuster;
    // TODO: 27/09/15 Change names of fragments for adjusting as menu
    // Transformation/Menu fragments
    Fragment fragmentMenuRotation, fragmentMenuSkew, fragmentMenuDrawingTools, fragmentMenuAdjustEffectLevel, fragmentMenuCropper;

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

        fragmentMenuCropper = new FMenuCropper_();
        fragmentMenuDrawingTools = new FMenuDrawingTools_();

        // set current value in the middle of seek bar (half of MAXIMUM_ROTATION_DEGREE)
        fragmentMenuRotation = FMenuAdjuster_.builder().
                adjusterTitle(rotateAngle).
                fragmentTitle(rotatePicture).
                currentProgress(MAX_ROTATION_DEGREE_ONE_DIRECTION).
                progressMax(MAX_ROTATION_DEGREE_ONE_DIRECTION << 1).
                finishedProgress(ON_FINISHED_ROTATION).
                eventId(ON_ROTATE).build();

        fragmentMenuSkew = new FTiltFragmentController_();

        // Set clearing tools as a first
        getSupportFragmentManager().beginTransaction().add(R.id.flFragmentHolder, fragmentMenuDrawingTools).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.flFragmentHolder, fragmentMenuRotation).commit();
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
                                                                   cropImageView.setVisibility(GONE);
                                                                   drawingView.setDrawingEnabled(true);
                                                                   getSupportFragmentManager().beginTransaction().hide(fragmentMenuRotation).commit();
                                                                   getSupportFragmentManager().beginTransaction().show(fragmentMenuDrawingTools).commit();
                                                                   break;

                                                               case CANVAS_TRANSFORMER_FRAGMENT:
                                                                   // TODO: 27/09/15 Remember selected transform tools and display this fragment
                                                                   getSupportFragmentManager().beginTransaction().show(fragmentMenuRotation).commit();
                                                                   getSupportFragmentManager().beginTransaction().hide(fragmentMenuDrawingTools).commit();
                                                                   drawingView.setVisibility(VISIBLE);
                                                                   gpuImage.setVisibility(GONE);
                                                                   cropImageView.setVisibility(GONE);
                                                                   drawingView.setDrawingEnabled(false);
                                                                   drawingView.setGridType(GridType.NO_GRID);
                                                                   break;

                                                               case EFFECTS_TOOLS_FRAGMENT:
                                                                   drawingView.setVisibility(GONE);
                                                                   gpuImage.setVisibility(VISIBLE);
                                                                   cropImageView.setVisibility(GONE);
                                                                   drawingView.setDrawingEnabled(true);
                                                                   getSupportFragmentManager().beginTransaction().remove(fragmentMenuRotation).commit();
                                                                   getSupportFragmentManager().beginTransaction().remove(fragmentMenuSkew).commit();
                                                                   if (Conditions.isNotNull(currentFilter)) {
                                                                       getSupportFragmentManager().beginTransaction().replace(R.id.flFragmentHolder, fragmentMenuAdjustEffectLevel).commit();
                                                                   }
                                                                   break;

                                                               case CANVAS_CROPPER_TOOLS_FRAGMENT:
                                                                   drawingView.setVisibility(GONE);
                                                                   gpuImage.setVisibility(GONE);
                                                                   cropImageView.setVisibility(VISIBLE);
                                                                   drawingView.setDrawingEnabled(true);
                                                                   cropImageView.setImageBitmap(bitmapModified);
                                                                   getSupportFragmentManager().beginTransaction().replace(R.id.flFragmentHolder, fragmentMenuCropper).commit();
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

            // TODO: 28/09/15 Replace with creation only once
            // create adjustable fragment
            fragmentMenuAdjustEffectLevel = FMenuAdjuster_.builder().
                    adjusterTitle(filterName).
                    fragmentTitle(effectAdjuster).
                    currentProgress(50).
                    progressMax(100).
                    eventId(ON_ADJUST_FILTER_LEVEL).build();

            getSupportFragmentManager().beginTransaction().replace(R.id.flFragmentHolder, fragmentMenuAdjustEffectLevel).commit();

            // Filter is not adjustable, remove adjust menu
            if (!filterAdjuster.canAdjust()) {
                getSupportFragmentManager().beginTransaction().remove(fragmentMenuAdjustEffectLevel).commit();
                Logger.e("Filter " + filterName + " is not adjustable, hide seekBar");
            }
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
                // TODO: Repla```ce weird shift
                float x = (motionEvent.getX() / 1000f) + 0.2f;
                float y = (motionEvent.getY() / 1000f) + 0.2f;
                if (currentFilter instanceof GPUImageSwirlFilter) {
                    ((GPUImageSwirlFilter) currentFilter).setCenter(new PointF(x, y));
                }

                if (currentFilter instanceof GPUImageBulgeDistortionFilter) {
                    ((GPUImageBulgeDistortionFilter) currentFilter).setCenter(new PointF(x, y));
                }
                Logger.e(String.format("x:%s y:%s", x, y));
                gpuImage.requestRender();

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
            drawingView.setDrawingData(drawingView.builder().from(drawingView.getDrawingData()).withBitmap(GraphicUtils.applyReflection(drawingView.getDrawingData().getCanvasBitmap())).build());
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

            case ON_FINISHED_ROTATION:
                onApplyImageTransformationChanges();
                break;

            case ON_CHANGE_CROPPING_SHAPE:
                invertCroppingShape();
                break;

            case ON_SKEW_TRANSFORMATION:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragmentHolder, fragmentMenuSkew).commit();
                break;

            case ON_ROTATE_TRANSFORMATION:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragmentHolder, fragmentMenuRotation).commit();
                break;

            case ON_ABS_MENU_APPLY:
                onApplyImageTransformationChanges();
                break;

            case ON_ABS_MENU_RESTORE:
                restoreOriginalImageBeforeTransformation();
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
                // if progress more then a half (tiltFactorX % 50) / 100f  ====> i.e (55 % 50 = 5 and 5 / 100 ==0.05), tiltFactor == 0.05
                // if progress less then a half (tiltFactorX - 50) / 100f ====> i.e - (23 - 50 = 17 and 17/100)== -0.17)
                //// TODO: 21/09/2015 Replace magic numbers
                // replace with InternalMath class
                tiltFactorX = tiltFactorX > 50 ? (tiltFactorX % 50 / 100f) : ((tiltFactorX - 50) / 100f);
                drawingView.setTiltFactorX((int) event.payload == 100 ? 0.5f : tiltFactorX);
                Logger.e("  ON_TILT_FACTOR_X_CHANGED : " + tiltFactorX);
                break;


            case ON_TILT_FACTOR_Y_CHANGED:
                float tiltFactorY = (int) event.payload;
                tiltFactorY = tiltFactorY > 50 ? (tiltFactorY % 50 / 100f) : ((tiltFactorY - 50) / 100f);
                drawingView.setTiltFactorY((int) event.payload == 100 ? 0.5f : tiltFactorY);
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

    public void setRotationDegree(float degree) {
        drawingView.setRotationDegree(degree);
    }

    public void setRotationDegree(float degree, boolean zoomIn) {
        drawingView.setRotationDegree(degree, zoomIn);
    }


    @Override
    public void userHasReleasedFinger() {
        //onApplyImageTransformationChanges();
    }
}