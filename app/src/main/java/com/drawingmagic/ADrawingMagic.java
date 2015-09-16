package com.drawingmagic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.SeekBar;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.drawingmagic.adapters.ViewPagerAdapter;
import com.drawingmagic.core.DrawingSettings;
import com.drawingmagic.core.DrawingView;
import com.drawingmagic.core.GPUImageFilterTools;
import com.drawingmagic.fragments.FDrawingTools;
import com.drawingmagic.fragments.FDrawingTools.OnChangeDrawingSettingsListener;
import com.drawingmagic.utils.Conditions;
import com.drawingmagic.utils.Notification;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.SeekBarProgressChange;
import org.androidannotations.annotations.ViewById;

import github.chenupt.springindicator.SpringIndicator;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

import static android.widget.SeekBar.DRAWING_CACHE_QUALITY_HIGH;
import static com.drawingmagic.adapters.ViewPagerAdapter.*;
import static com.drawingmagic.core.DrawingView.ShapesType;
import static com.drawingmagic.fragments.FCanvasTools.OnChangeCanvasSettingsListener;
import static com.drawingmagic.fragments.FEffectsTools.OnChangeEffectListener;
import static jp.co.cyberagent.android.gpuimage.GPUImageView.OnPictureSavedListener;

/**
 * Project DrawingMagic.
 * Created by ihorkarpachev
 * On 13/09/15 at 17:44.
 */
@EActivity(R.layout.activity_drawing_magic)
public class ADrawingMagic extends SuperActivity implements OnChangeDrawingSettingsListener, OnChangeEffectListener, OnPictureSavedListener, OnChangeCanvasSettingsListener {


    private static final int DEFAULT_BRUSH_SIZE = 3;
    public static final int REQUEST_PICK_IMAGE = 1001;

    @ViewById
    DrawingView drawingView;

    @ViewById
    ViewPager viewPager;

    @ViewById
    SpringIndicator viewPagerIndicator;

    @ViewById
    GPUImageView gpuImage;

    @ViewById
    SeekBar seekBar;

    // View pager adapter
    private ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

    private GPUImageFilter mFilter;
    private GPUImageFilterTools.FilterAdjuster mFilterAdjuster;


    @AfterViews
    void afterViews() {
        drawingView.setDrawingCacheEnabled(true);
        drawingView.setDrawingCacheQuality(DRAWING_CACHE_QUALITY_HIGH);
        drawingView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        drawingView.setDrawingData(drawingView.builder().from(drawingView.getDrawingData()).
                withBrushWidth(DEFAULT_BRUSH_SIZE, getResources().getDisplayMetrics()).
                withLinesWhileDrawing(false).
                withShape(ShapesType.STANDARD_DRAWING).
                withColor(Color.BLACK).
                withGridEnabled(true).build());


        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setPageTransformer(true, new CubeOutTransformer());

        viewPagerIndicator.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case DRAWING_TOOLS_FRAGMENT:
                        drawingView.clearRedoPaths();
                        drawingView.setDrawingData(drawingView.builder().from(drawingView.getDrawingData()).withBitmap(gpuImage.getGPUImage().getBitmapWithFilterApplied()).withPaths(null).build());
                        gpuImage.setVisibility(View.GONE);
                        seekBar.setVisibility(View.GONE);
                        break;
                    case EFFECTS_TOOLS_FRAGMENT:
                        gpuImage.setVisibility(View.VISIBLE);
                        seekBar.setVisibility(View.VISIBLE);
                        drawingView.buildDrawingCache();
                        Bitmap b = drawingView.getDrawingCache();
                        gpuImage.setImage(b);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        ((FDrawingTools) viewPagerAdapter.getItem(DRAWING_TOOLS_FRAGMENT)).setUpDrawingView(drawingView, this);
    }

    @SeekBarProgressChange
    void seekBar() {
        if (Conditions.isNotNull(mFilterAdjuster)) {
            mFilterAdjuster.adjust(seekBar.getProgress());
        }

        gpuImage.requestRender();
    }


    @OnActivityResult(REQUEST_PICK_IMAGE)
    void onResult(int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }
        gpuImage.setImage(data.getData());
    }

    @Override
    public void onSetUpDrawingShapesOkClicked(DrawingSettings shape) {
        drawingView.setDrawingData(drawingView.builder().from(drawingView.getDrawingData()).withShape(shape).build());
    }

    @Override
    public void onNewFilterSelected(GPUImageFilter filter) {
        switchFilterTo(filter);
        gpuImage.requestRender();
    }

    private void saveImage() {
        String fileName = System.currentTimeMillis() + ".jpg";
        gpuImage.saveToPictures("GPUImage", fileName, this);
    }

    private void switchFilterTo(final GPUImageFilter filter) {
        if (mFilter == null || (filter != null && !mFilter.getClass().equals(filter.getClass()))) {
            mFilter = filter;
            gpuImage.setFilter(mFilter);
            mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
            seekBar.setVisibility(mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onPictureSaved(Uri uri) {
        Notification.showSuccess(this, "Saved: " + uri.toString());
    }

    @Override
    public void onCanvasSettingsChanged() {

    }
}
