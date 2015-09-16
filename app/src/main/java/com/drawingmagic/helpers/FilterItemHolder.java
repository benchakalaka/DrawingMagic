package com.drawingmagic.helpers;

import com.drawingmagic.core.GPUImageFilterTools;

/**
 * Created by ihor.karpachev on 16/09/2015.
 * Project: DrawingMagic
 * Package: com.drawingmagic.helpers
 * Datascope Systems Ltd.
 */
public class FilterItemHolder {
    public FilterItemHolder(String filterName, GPUImageFilterTools.FilterType filter) {
        this.filterName = filterName;
        this.filter = filter;
    }

    public String filterName;
    public GPUImageFilterTools.FilterType filter;
}
