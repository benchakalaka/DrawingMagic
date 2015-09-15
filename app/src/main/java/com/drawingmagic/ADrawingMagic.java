package com.drawingmagic;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.drawingmagic.core.DrawingSettings;
import com.drawingmagic.core.DrawingView;
import com.drawingmagic.fragments.FDrawingTools;
import com.drawingmagic.fragments.FDrawingTools.OnSelectTypeOfShapeListener;
import com.drawingmagic.fragments.FDrawingTools_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import github.chenupt.springindicator.SpringIndicator;

import static com.drawingmagic.core.DrawingView.ShapesType;

/**
 * Project DrawingMagic.
 * Created by ihorkarpachev
 * On 13/09/15 at 17:44.
 */
@EActivity(R.layout.activity_drawing_magic)
public class ADrawingMagic extends SuperActivity implements OnSelectTypeOfShapeListener {


    private static final int DEFAULT_BRUSH_SIZE = 3;

    @ViewById
    DrawingView drawingView;

    @ViewById
    ViewPager viewPager;

    @ViewById
    SpringIndicator viewPagerIndicator;

    MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());


    @AfterViews
    void afterViews() {
        // init and setup drawingview
        drawingView.setDrawingData(drawingView.builder().from(drawingView.getDrawingData()).
                withBrushWidth(DEFAULT_BRUSH_SIZE, getResources().getDisplayMetrics()).
                withLinesWhileDrawing(false).
                withShape(ShapesType.STANDARD_DRAWING).
                withColor(Color.BLACK).
                withGridEnabled(true).build());


        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(true, new RotateUpTransformer());

        viewPagerIndicator.setViewPager(viewPager);

        ((FDrawingTools)adapter.getItem(0)).setUpDrawingView(drawingView, this);
    }


    @Override
    public void onSetUpDrawingShapesOkClicked(DrawingSettings shape) {

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
                case 0: // Fragment # 0 - This will show FirstFragment
                    return new FDrawingTools_();
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return new FDrawingTools_();
                case 2: // Fragment # 1 - This will show SecondFragment
                    return new FDrawingTools_();
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
