package com.drawingmagic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.drawingmagic.adapters.ViewPagerAdapter;
import com.drawingmagic.core.DrawingSettings;
import com.drawingmagic.core.DrawingView;
import com.drawingmagic.core.GPUImageFilterTools;
import com.drawingmagic.eventbus.Event;
import com.drawingmagic.fragments.FDrawingTools.OnChangeDrawingSettingsListener;
import com.drawingmagic.helpers.FilterItemHolder;
import com.drawingmagic.utils.Conditions;
import com.drawingmagic.utils.Notification;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import github.chenupt.springindicator.SpringIndicator;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

import static com.drawingmagic.adapters.ViewPagerAdapter.CANVAS_SETTINGS_TOOLS_FRAGMENT;
import static com.drawingmagic.adapters.ViewPagerAdapter.DRAWING_TOOLS_FRAGMENT;
import static com.drawingmagic.adapters.ViewPagerAdapter.EFFECTS_TOOLS_FRAGMENT;
import static com.drawingmagic.core.DrawingView.ShapesType;
import static com.drawingmagic.fragments.FCanvasTools.OnChangeCanvasSettingsListener;
import static com.drawingmagic.fragments.FEffectsTools.OnChangeEffectListener;
import static com.drawingmagic.views.HoverView.DRAWING_CACHE_QUALITY_HIGH;
import static com.drawingmagic.views.HoverView.MENU_ITEM_CAMERA;
import static com.drawingmagic.views.HoverView.MENU_ITEM_EMPTY_CANVAS;
import static com.drawingmagic.views.HoverView.MENU_ITEM_GALLERY;
import static jp.co.cyberagent.android.gpuimage.GPUImageView.OnPictureSavedListener;

/**
 * Project DrawingMagic.
 * Created by ihorkarpachev
 * On 13/09/15 at 17:44.
 */
@EActivity(R.layout.activity_drawing_magic)
public class ADrawingMagic extends SuperActivity implements OnChangeDrawingSettingsListener, OnChangeEffectListener, OnPictureSavedListener, OnChangeCanvasSettingsListener {

    @ViewById
    DrawingView drawingView;

    @ViewById
    ViewPager viewPager;

    @ViewById
    SpringIndicator viewPagerIndicator;

    @ViewById
    GPUImageView gpuImage;

    @ViewById
    public
    CropImageView cropImageView;

    @Extra
    int selectedMenuItem;

    public static Bitmap ORIGIN_BITMAP;

    // View pager adapter
    private final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());


    private static final int DEFAULT_BRUSH_SIZE = 3;
    public static final int REQUEST_PICK_IMAGE = 1001;

    private GPUImageFilter currentFilter;
    private GPUImageFilterTools.FilterAdjuster filterAdjuster;


    @AfterViews
    void afterViews() {

        // init drawing view
        initDrawingView();

        // init view pager
        initViewPager();

        // for some unknown reason, if gpuImage had visibility GONE defined
        // in xml layout file, setImage will not work
        gpuImage.setVisibility(View.GONE);
    }

    /**
     * Init View Pager
     */
    private void initViewPager() {
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setPageTransformer(true, new CubeOutTransformer());
        viewPagerIndicator.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case DRAWING_TOOLS_FRAGMENT:
                        drawingView.clearRedoPaths();
                        drawingView.setDrawingData(drawingView.builder().from(drawingView.getDrawingData()).withBitmap(gpuImage.getGPUImage().getBitmapWithFilterApplied()).withPaths(null).build());

                        gpuImage.setVisibility(View.GONE);
                        cropImageView.setVisibility(View.GONE);
                        drawingView.setVisibility(View.VISIBLE);
                        break;

                    case EFFECTS_TOOLS_FRAGMENT:
                        gpuImage.setVisibility(View.VISIBLE);

                        gpuImage.setImage(ORIGIN_BITMAP.copy(Bitmap.Config.ARGB_8888, true));
                        gpuImage.requestRender();

                        //Log.e(gpuImage.getWidth() + " h : "+gpuImage.getHeight());

                        cropImageView.setVisibility(View.GONE);
                        drawingView.setVisibility(View.GONE);
                        break;

                    case CANVAS_SETTINGS_TOOLS_FRAGMENT:
                        cropImageView.setVisibility(View.VISIBLE);
                        gpuImage.setVisibility(View.GONE);
                        drawingView.setVisibility(View.GONE);
                        cropImageView.setImageBitmap(ORIGIN_BITMAP.copy(Bitmap.Config.ARGB_8888, true));
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    /**
     * Init Drawing View
     */
    private void initDrawingView() {
        drawingView.setDrawingCacheEnabled(true);
        drawingView.setDrawingCacheQuality(DRAWING_CACHE_QUALITY_HIGH);
        drawingView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        drawingView.setDrawingData(drawingView.builder().from(drawingView.getDrawingData()).
                withBrushWidth(DEFAULT_BRUSH_SIZE, getResources().getDisplayMetrics()).
                withLinesWhileDrawing(false).
                withShape(ShapesType.STANDARD_DRAWING).
                withColor(Color.BLACK).
                withGridEnabled(true).build());
    }


    @AfterExtras
    void afterExtras() {
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
    }

    @Override
    public void onChangeSeekBarProgress(int progress) {
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
    void onResult(int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }

        try {
            ORIGIN_BITMAP = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        gpuImage.setImage(data.getData());
        drawingView.setDrawingData(drawingView.builder().from(drawingView.getDrawingData()).withBitmap(ORIGIN_BITMAP).withPaths(null).build());
    }

    @Override
    public void onSetUpDrawingShapesOkClicked(DrawingSettings shape) {
        drawingView.setDrawingData(drawingView.builder().from(drawingView.getDrawingData()).withShape(shape).build());
    }

    @Override
    public void onNewFilterSelected(FilterItemHolder filterItemHolder) {
        // User pressed X
        if (filterItemHolder == null) {
            gpuImage.getFilter().destroy();
            gpuImage.requestRender();
            return;
        }

        switchFilterTo(GPUImageFilterTools.createFilterForType(this, filterItemHolder.filter));
        gpuImage.requestRender();
        Notification.showSuccess(this, filterItemHolder.filterName);
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
    public void onCanvasSettingsChanged() {

    }

    @Override
    public void onEventMainThread(Event event) {

    }


    private void pickUpImageFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, ADrawingMagic.REQUEST_PICK_IMAGE);
    }
}
