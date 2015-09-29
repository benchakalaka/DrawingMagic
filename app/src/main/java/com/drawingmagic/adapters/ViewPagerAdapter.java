package com.drawingmagic.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.drawingmagic.fragments.FCanvasTools;
import com.drawingmagic.fragments.FCanvasTools_;
import com.drawingmagic.fragments.FCanvasTransformer_;
import com.drawingmagic.fragments.FDrawingTools;
import com.drawingmagic.fragments.FDrawingTools_;
import com.drawingmagic.fragments.FEffectsTools;
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
    public static final int CANVAS_SETTINGS_TOOLS_FRAGMENT = 3;


    private final ArrayList<Fragment> fragments = new ArrayList<>();

    private final Fragment fDrawingTools = new FDrawingTools_(), fCanvasTransformerTools = new FCanvasTransformer_(), fEffectsTools = new FEffectsTools_(), fCanvasCroppingTools = new FCanvasTools_();

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        fragments.add(DRAWING_TOOLS_FRAGMENT, fDrawingTools);
        fragments.add(CANVAS_TRANSFORMER_FRAGMENT, fCanvasTransformerTools);
        fragments.add(EFFECTS_TOOLS_FRAGMENT, fEffectsTools);
        fragments.add(CANVAS_SETTINGS_TOOLS_FRAGMENT, fCanvasCroppingTools);
    }

    public FDrawingTools getCanvasTransformerFragment() {
        return (FDrawingTools) getItem(CANVAS_TRANSFORMER_FRAGMENT);
    }

    public FDrawingTools getDrawingToolsFragment() {
        return (FDrawingTools) getItem(DRAWING_TOOLS_FRAGMENT);
    }

    public FEffectsTools getEffectsToolsFragment() {
        return (FEffectsTools) getItem(EFFECTS_TOOLS_FRAGMENT);
    }

    public FCanvasTools getCanvasToolsFragment() {
        return (FCanvasTools) getItem(CANVAS_SETTINGS_TOOLS_FRAGMENT);
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
