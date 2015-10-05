package com.drawingmagic;

import com.vk.sdk.VKScope;

/**
 * Project DrawingMagic.
 * Created by ihorkarpachev.
 * On 20/09/15 at 10:37.
 */
public class GlobalConstants {

    public static class VKConstatns {
        public static final String VK_KEY = "5088122";

        public static final String[] VK_SCOPE = new String[]{
                VKScope.FRIENDS,
                VKScope.WALL,
                VKScope.PHOTOS,
                VKScope.NOHTTPS,
                VKScope.STATUS,
        };
    }
}
