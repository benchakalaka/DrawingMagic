package com.drawingmagic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.drawingmagic.adapters.ViewPagerAdapter;
import com.drawingmagic.core.DrawingSettings;
import com.drawingmagic.core.DrawingView;
import com.drawingmagic.core.GPUImageFilterTools;
import com.drawingmagic.eventbus.Event;
import com.drawingmagic.fragments.FAdjuster_;
import com.drawingmagic.fragments.FDrawingTools.OnChangeDrawingSettingsListener;
import com.drawingmagic.fragments.FMenuClearingTools_;
import com.drawingmagic.fragments.FTiltFragmentController_;
import com.drawingmagic.helpers.FilterItemHolder;
import com.drawingmagic.utils.Conditions;
import com.drawingmagic.utils.Log;
import com.drawingmagic.utils.Notification;
import com.drawingmagic.utils.Utils;
import com.drawingmagic.views.ABSMenuApplyRestoreCancel_;
import com.drawingmagic.views.abs.ABS_;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.io.File;

import github.chenupt.springindicator.SpringIndicator;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

import static android.graphics.Bitmap.Config;
import static android.view.View.GONE;
import static android.view.View.LAYER_TYPE_SOFTWARE;
import static android.view.View.VISIBLE;
import static com.drawingmagic.adapters.ViewPagerAdapter.CANVAS_SETTINGS_TOOLS_FRAGMENT;
import static com.drawingmagic.adapters.ViewPagerAdapter.CANVAS_TRANSFORMER_FRAGMENT;
import static com.drawingmagic.adapters.ViewPagerAdapter.DRAWING_TOOLS_FRAGMENT;
import static com.drawingmagic.adapters.ViewPagerAdapter.EFFECTS_TOOLS_FRAGMENT;
import static com.drawingmagic.core.DrawingView.GridType;
import static com.drawingmagic.core.DrawingView.ShapesType;
import static com.drawingmagic.eventbus.Event.ON_ADJUSTER_VALUE_CHANGED;
import static com.drawingmagic.eventbus.Event.ON_CLEAR_CANVAS;
import static com.drawingmagic.eventbus.Event.ON_REDO;
import static com.drawingmagic.eventbus.Event.ON_ROTATE;
import static com.drawingmagic.eventbus.Event.ON_TILT_FACTOR_X_CHANGED;
import static com.drawingmagic.eventbus.Event.ON_TILT_FACTOR_Y_CHANGED;
import static com.drawingmagic.eventbus.Event.ON_UNDO;
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
public class ADrawingMagic extends SuperActivity implements OnChangeDrawingSettingsListener, OnPictureSavedListener {

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


    private static final int MAXIMUM_ROTATION_DEGREE = 360;

    /**
     * BITMAP_ORIGIN -------> BITMAP_MODIFIED -------> (DrawingView, TransformView, GPUEffects, CropView)
     */
    public static Bitmap BITMAP_ORIGIN, BITMAP_MODIFIED;
    private Bitmap croppedBitmap;

    // View pager adapter
    private final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

    // Static final constants
    private static final int DEFAULT_BRUSH_SIZE = 3;
    public static final int REQUEST_PICK_IMAGE = 1001;
    private static final int DEFAULT_ASPECT_RATIO_VALUES = 20;
    private static final int SHOW_GUIDELINES_ALWAYS = 2;

    private GPUImageFilter currentFilter;
    private GPUImageFilterTools.FilterAdjuster filterAdjuster;

    // Transformation fragments
    Fragment fragmentRotation, fragmentSkew, fragmentUndoRedo;


    @AfterViews
    void afterViews() {
        Utils.configureCustomActionBar(getActionBar(), ABS_.build(this).withRightMenu(ABSMenuApplyRestoreCancel_.build(this)).withMenuButton());

        // init drawing view
        initDrawingView();

        // init view pager
        initViewPager();

        // for some unknown reason, if gpuImage had visibility GONE defined
        // in xml layout file, setImage will not work
        gpuImage.setVisibility(GONE);
        gpuImage.setScaleType(CENTER_INSIDE);


        cropImageView.setGuidelines(SHOW_GUIDELINES_ALWAYS);
        cropImageView.setAspectRatio(DEFAULT_ASPECT_RATIO_VALUES, DEFAULT_ASPECT_RATIO_VALUES);
        cropImageView.setImageBitmap(BITMAP_MODIFIED);
        // // TODO: 22/09/2015 Make Picker of shape
        cropImageView.setCropShape(CropShape.OVAL);

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

        // Set clearing tools as a first
        getSupportFragmentManager().beginTransaction().replace(R.id.flFragmentHolder, new FMenuClearingTools_()).commit();

        // set current value in the middle of seek bar (half of MAXIMUM_ROTATION_DEGREE)
        fragmentRotation = FAdjuster_.builder().
                adjusterTitle(rotateAngle).
                fragmentTitle(rotatePicture).
                currentProgress(MAXIMUM_ROTATION_DEGREE >> 1).
                progressMax(MAXIMUM_ROTATION_DEGREE).
                eventId(Event.ON_ROTATE).build();

        fragmentSkew = new FTiltFragmentController_();
        fragmentUndoRedo = new FMenuClearingTools_();
    }


    /**
     * Init View Pager
     */
    private void initViewPager() {
        viewPager.setAdapter(viewPagerAdapter);
        // // TODO: 24/09/2015 replace magic number
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageTransformer(true, new CubeOutTransformer());
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
                                                                   getSupportFragmentManager().beginTransaction().replace(R.id.flFragmentHolder, fragmentUndoRedo).commit();
                                                                   break;

                                                               case CANVAS_TRANSFORMER_FRAGMENT:
                                                                   drawingView.setVisibility(VISIBLE);
                                                                   gpuImage.setVisibility(GONE);
                                                                   cropImageView.setVisibility(GONE);
                                                                   getSupportFragmentManager().beginTransaction().remove(fragmentUndoRedo).commit();
                                                                   break;

                                                               case EFFECTS_TOOLS_FRAGMENT:
                                                                   drawingView.setVisibility(GONE);
                                                                   gpuImage.setVisibility(VISIBLE);
                                                                   cropImageView.setVisibility(GONE);
                                                                   break;

                                                               case CANVAS_SETTINGS_TOOLS_FRAGMENT:
                                                                   drawingView.setVisibility(GONE);
                                                                   gpuImage.setVisibility(GONE);
                                                                   cropImageView.setVisibility(VISIBLE);
                                                                   cropImageView.setImageBitmap(BITMAP_MODIFIED);
                                                                   break;

                                                               default:
                                                                   Log.e("onPageSelected: Unknown page position  : " + viewPager.getCurrentItem());
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
                drawingView.setDrawingData(drawingView.builder().from(drawingView.getDrawingData()).withBitmap(BITMAP_MODIFIED).withPaths(null).build());
                break;

            // Cancel any effect
            case EFFECTS_TOOLS_FRAGMENT:
                applyEffect(null);
                break;

            // Cancel cropping
            case CANVAS_SETTINGS_TOOLS_FRAGMENT:
                croppedBitmap = BITMAP_MODIFIED;
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
                Notification.showError(this, "TODO ");
                break;

            // Cancel any effect
            case EFFECTS_TOOLS_FRAGMENT:
                Notification.showError(this, "TODO ");
                break;

            // Cancel cropping
            case CANVAS_SETTINGS_TOOLS_FRAGMENT:
                cropImageView.setImageBitmap(BITMAP_MODIFIED);
                break;

            default:
                break;
        }
    }

    private void onApplyImageTransformationChanges() {
        switch (viewPager.getCurrentItem()) {
            case DRAWING_TOOLS_FRAGMENT:
                int gridType = drawingView.getDrawingData().getShape().getGridType();
                drawingView.setGridType(GridType.NO_GRID);
                BITMAP_MODIFIED = drawingView.getDrawingCache().copy(Config.RGB_565, true);
                drawingView.clearRedoPaths();
                drawingView.setGridType(gridType);
                drawingView.resetAllTransformation();
                break;

            case EFFECTS_TOOLS_FRAGMENT:
                BITMAP_MODIFIED = gpuImage.getGPUImage().getBitmapWithFilterApplied();
                applyEffect(null);
                break;

            case CANVAS_SETTINGS_TOOLS_FRAGMENT:
                croppedBitmap = cropImageView.getCropShape() == CropShape.RECTANGLE ? cropImageView.getCroppedImage() : cropImageView.getCroppedOvalImage();
                break;

            default:
                break;
        }

        drawingView.setDrawingData(drawingView.builder().from(drawingView.getDrawingData()).withBitmap(BITMAP_MODIFIED).withPaths(null).build());

        gpuImage.getGPUImage().deleteImage();
        gpuImage.setImage(BITMAP_MODIFIED);

        cropImageView.setImageBitmap(croppedBitmap);
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
    }

    private void adjustFilter(int progress) {
        if (Conditions.isNotNull(filterAdjuster)) {
            filterAdjuster.adjust(progress);
        }
        gpuImage.requestRender();
    }

    private void switchFilterTo(final GPUImageFilter filter) {
        if (Conditions.isNull(currentFilter) || (!currentFilter.getClass().equals(filter.getClass()))) {
            currentFilter = filter;
            gpuImage.setFilter(currentFilter);
            filterAdjuster = new GPUImageFilterTools.FilterAdjuster(currentFilter);
            viewPagerAdapter.getEffectsToolsFragment().setCanAdjustStatus(filterAdjuster.canAdjust());
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
                    if (Conditions.isNotNull(BITMAP_ORIGIN)) {
                        BITMAP_ORIGIN.recycle();
                    }

                    if (Conditions.isNotNull(BITMAP_MODIFIED)) {
                        BITMAP_MODIFIED.recycle();
                    }

                    BITMAP_ORIGIN = Utils.decodeSampledBitmapFromResource(new File(Utils.getRealPathFromURI(ADrawingMagic.this, data.getData())).getAbsolutePath(), drawingView.getWidth(), drawingView.getHeight());
                    BITMAP_MODIFIED = BITMAP_ORIGIN.copy(Config.RGB_565, true);
                    drawingView.setDrawingData(drawingView.builder().from(drawingView.getDrawingData()).withBitmap(BITMAP_MODIFIED).withPaths(null).build());
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        gpuImage.setImage(data.getData());
    }


    @Override
    public void onSetUpDrawingShapesOkClicked(DrawingSettings shape) {
        drawingView.setDrawingData(drawingView.builder().from(drawingView.getDrawingData()).withShape(shape).build());
    }

    private void applyEffect(FilterItemHolder filterItemHolder) {
        // User pressed X
        if (filterItemHolder == null) {
            // equivalent to restore default image
            gpuImage.setFilter(new GPUImageFilter());
            return;
        }

        switchFilterTo(GPUImageFilterTools.createFilterForType(this, filterItemHolder.getFilter()));
        gpuImage.requestRender();
        Notification.showSuccess(this, filterItemHolder.getFilterName());
    }

    private void saveImage() {
        String fileName = System.currentTimeMillis() + ".jpg";
        gpuImage.saveToPictures("GPUImage", fileName, this);
    }

    @Override
    public void onPictureSaved(Uri uri) {
        Notification.showSuccess(this, "Saved: " + uri.toString());
    }


    @Override
    public void onEventMainThread(Event event) {
        Log.e(event.toString());
        switch (event.type) {

            case Event.ON_SKEW_TRANSFORMATION:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragmentHolder, fragmentSkew).commit();
                break;

            case Event.ON_ROTATE_TRANSFORMATION:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragmentHolder, fragmentRotation).commit();
                break;

            case Event.ON_ABS_MENU_APPLY:
                onApplyImageTransformationChanges();
                break;

            case Event.ON_ABS_MENU_RESTORE:
                restoreOriginalImageBeforeTransformation();
                break;

            case Event.ON_ABS_MENU_CANCEL:
                cancelCurrentTransformation();
                break;

            case Event.ON_ABS_MENU_CLICKED:
                onBackPressed();
                break;

            case Event.ON_ADJUST_FILTER_LEVEL:
                adjustFilter((int) event.payload);
                Log.e("  ON_ADJUST_FILTER_LEVEL : " + (int) event.payload);
                break;


            case Event.ON_APPLY_EFFECT:
                applyEffect((FilterItemHolder) event.payload);
                Log.e("  ON_APPLY_EFFECT : " + ((FilterItemHolder) event.payload).getFilterName());
                break;


            case Event.ON_RESTORE_IMAGE_BEFORE_CROPPING:
                cropImageView.setImageBitmap(BITMAP_MODIFIED);
                break;


            case Event.ON_APPLY_CROPPING:
                cropImageView.setImageBitmap(cropImageView.getCroppedImage());
                break;


            case ON_ADJUSTER_VALUE_CHANGED:
                //drawingView.setRotationDegree((int) event.payload);
                Log.e("  ON_ADJUSTER_VALUE_CHANGED : " + (int) event.payload);
                break;


            case ON_TILT_FACTOR_X_CHANGED:
                float tiltFactorX = (int) event.payload;
                // if progress more then a half (tiltFactorX % 50) / 100f  ====> i.e (55 % 50 = 5 and 5 / 100 ==0.05), tiltFactor == 0.05
                // if progress less then a half (tiltFactorX - 50) / 100f ====> i.e - (23 - 50 = 17 and 17/100)== -0.17)
                //// TODO: 21/09/2015 Replace magic numbers
                // replace with InternalMath class
                tiltFactorX = tiltFactorX > 50 ? (tiltFactorX % 50 / 100f) : ((tiltFactorX - 50) / 100f);
                drawingView.setTiltFactorX((int) event.payload == 100 ? 0.5f : tiltFactorX);
                Log.e("  ON_TILT_FACTOR_X_CHANGED : " + tiltFactorX);
                break;


            case ON_TILT_FACTOR_Y_CHANGED:
                float tiltFactorY = (int) event.payload;
                tiltFactorY = tiltFactorY > 50 ? (tiltFactorY % 50 / 100f) : ((tiltFactorY - 50) / 100f);
                drawingView.setTiltFactorY((int) event.payload == 100 ? 0.5f : tiltFactorY);
                Log.e("  ON_TILT_FACTOR_Y_CHANGED : " + tiltFactorY);
                break;


            case ON_UNDO:
                drawingView.undo();
                break;


            case ON_REDO:
                drawingView.redo();
                break;


            case ON_CLEAR_CANVAS:
                drawingView.clearRedoPaths();
                drawingView.setDrawingData(drawingView.builder().from(drawingView.getDrawingData()).withPaths(null).build());
                break;


            case ON_ROTATE:
                int progress = (int) event.payload;

                switch (progress) {
                    case 0: {
                        progress = 180;
                        break;
                    }

                    case 360: {
                        progress = -180;
                        break;
                    }

                    default:
                        progress = progress > 180 ? progress % 180 : progress - 180;
                        break;
                }
                setRotationDegree(progress);
                break;


            default:
                Log.e("Unknown event " + event);
        }
    }

    private void pickUpImageFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, ADrawingMagic.REQUEST_PICK_IMAGE);
    }

    public void setRotationDegree(float degree) {
        drawingView.setRotationDegree(degree);
    }
}
