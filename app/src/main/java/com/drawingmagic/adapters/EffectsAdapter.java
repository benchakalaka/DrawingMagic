package com.drawingmagic.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.drawingmagic.R;
import com.drawingmagic.core.GPUImageFilterTools;
import com.drawingmagic.views.LVItemImageFilter;
import com.drawingmagic.views.LVItemImageFilter_;

import java.util.List;

/**
 * Project DrawingMagic.
 * Created by ihorkarpachev.
 * On 15/09/15 at 20:30.
 */
public class EffectsAdapter extends ArrayAdapter<EffectsAdapter.FilterItem> {

    public EffectsAdapter(Context context, List<FilterItem> filterItemList) {
        super(context, R.layout.item_image_filter, filterItemList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LVItemImageFilter item = LVItemImageFilter_.build(getContext());
        item.tvDescription.setText(getItem(position).filterName);
        return item;
    }

    /**
     * Filter item holder
     */
    public static class FilterItem {
        public FilterItem(String filterName, GPUImageFilterTools.FilterType filter) {
            this.filterName = filterName;
            this.filter = filter;
        }

        public String filterName;
        public GPUImageFilterTools.FilterType filter;
    }

}
