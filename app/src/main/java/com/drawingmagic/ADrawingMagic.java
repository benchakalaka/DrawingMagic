package com.drawingmagic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.SeekBar;

import com.ToxicBakery.viewpager.transforms.CubeInTransformer;
import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.drawingmagic.core.DrawingSettings;
import com.drawingmagic.core.DrawingView;
import com.drawingmagic.core.GPUImageFilterTools;
import com.drawingmagic.fragments.FCanvasTools;
import com.drawingmagic.fragments.FCanvasTools_;
import com.drawingmagic.fragments.FDrawingTools;
import com.drawingmagic.fragments.FDrawingTools.OnChangeDrawingSettingsListener;
import com.drawingmagic.fragments.FDrawingTools_;
import com.drawingmagic.fragments.FEffectsTools_;
import com.drawingmagic.utils.Notification;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import github.chenupt.springindicator.SpringIndicator;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

import static android.widget.SeekBar.*;
import static com.drawingmagic.fragments.FCanvasTools.*;
import static com.drawingmagic.fragments.FEffectsTools.*;
import static com.drawingmagic.core.DrawingView.ShapesType;
import static jp.co.cyberagent.android.gpuimage.GPUImageView.*;

/**
 * Project DrawingMagic.
 * Created by ihorkarpachev
 * On 13/09/15 at 17:44.
 */
@EActivity(R.layout.activity_drawing_magic)
public class ADrawingMagic extends SuperActivity implements OnChangeDrawingSettingsListener, OnChangeEffectListener, OnPictureSavedListener, OnSeekBarChangeListener, OnChangeCanvasSettingsListener {


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

    MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());

    private GPUImageFilter mFilter;
    private GPUImageFilterTools.FilterAdjuster mFilterAdjuster;


    @Override
    public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
        if (mFilterAdjuster != null) {
            mFilterAdjuster.adjust(progress);
        }

        gpuImage.requestRender();
    }

    @Override
    public void onStartTrackingTouch(final SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(final SeekBar seekBar) {
    }


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

        seekBar.setOnSeekBarChangeListener(this);


        viewPager.setAdapter(adapter);

        viewPager.setPageTransformer(true, new CubeInTransformer());

        viewPagerIndicator.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        updateView1();
                        break;
                    case 1:
                        updateView2();
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        ((FDrawingTools) adapter.getItem(0)).setUpDrawingView(drawingView, this);
    }

    @Background void updateView1 (){
        drawingView.clearRedoPaths();
        drawingView.setDrawingData(drawingView.builder().from(drawingView.getDrawingData()).withBitmap(gpuImage.getGPUImage().getBitmapWithFilterApplied()).withPaths(null).build());
        gpuImage.setVisibility(View.GONE);
        seekBar.setVisibility(View.GONE);
    }


    @Background void updateView2 (){
        gpuImage.setVisibility(View.VISIBLE);
        seekBar.setVisibility(View.VISIBLE);
        drawingView.buildDrawingCache();
        Bitmap b = drawingView.getDrawingCache();
        gpuImage.setImage(b);
    }

    @Override
    // TODO: 15/09/2015 Replace with AA
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case REQUEST_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    gpuImage.setImage(data.getData());
                } else {
                    finish();
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
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
//        gpuImage.saveToPictures("GPUImage", fileName, 1600, 1600, this);
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

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 3;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FDrawingTools_();
                case 1:
                    return new FEffectsTools_();
                case 2: // Fragment # 1 - This will show SecondFragment
                    return new FCanvasTools_();
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }

}
