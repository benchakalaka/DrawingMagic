package com.drawingmagic.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.drawingmagic.fragments.FCanvasTransformer_;
import com.drawingmagic.fragments.FCropperTools;
import com.drawingmagic.fragments.FCropperTools_;
import com.drawingmagic.fragments.FDrawingTools_;
import com.drawingmagic.fragments.FEffectsTools_;

import java.util.ArrayList;

/**
 * Project DrawingMagic.
 * Created by ihorkarpachev.
 * On 15/09/15 at 19:15.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    public static final int DRAWING_TOOLS_FRAGMENT = 0;
    public static final int CANVAS_TRANSFORMER_FRAGMENT = 1;
    public static final int EFFECTS_TOOLS_FRAGMENT = 2;
    public static final int CANVAS_CROPPER_TOOLS_FRAGMENT = 3;


    private final ArrayList<Fragment> fragments = new ArrayList<>();

    private final Fragment fragmentDrawingTools = new FDrawingTools_(), fragmentCanvasTransformerTools = new FCanvasTransformer_(), fragmentEffectsTools = new FEffectsTools_(), fragmentCanvasCroppingTools = new FCropperTools_();

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        fragments.add(DRAWING_TOOLS_FRAGMENT, fragmentDrawingTools);
        fragments.add(CANVAS_TRANSFORMER_FRAGMENT, fragmentCanvasTransformerTools);
        fragments.add(EFFECTS_TOOLS_FRAGMENT, fragmentEffectsTools);
        fragments.add(CANVAS_CROPPER_TOOLS_FRAGMENT, fragmentCanvasCroppingTools);
    }

    public FCropperTools getCropperToolsFragment() {
        return (FCropperTools) getItem(CANVAS_CROPPER_TOOLS_FRAGMENT);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return String.valueOf(position + 1);
    }

}
