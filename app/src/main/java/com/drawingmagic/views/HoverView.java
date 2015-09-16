package com.drawingmagic.views;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.drawingmagic.R;

import net.steamcrafted.materialiconlib.MaterialIconView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ihor.karpachev on 16/09/2015.
 * Project: DrawingMagic
 * Package: com.drawingmagic.views
 * Datascope Systems Ltd.
 */
@EViewGroup(R.layout.hover_view)
public class HoverView extends RelativeLayout {


    @ViewById
    TextView content;
    @ViewById
    MaterialIconView mivEmptyCanvas, mivTakeCameraPicture, mivGallery;
    @ViewById
    LinearLayout llGallery, llEmptyCanvas, llCamera;

    private HooverMenuClickListener listener;

    public static final int MENU_ITEM_GALLERY = 1;
    public static final int MENU_ITEM_EMPTY_CANVAS = 2;
    public static final int MENU_ITEM_CAMERA = 3;

    public HoverView(Context context) {
        super(context);
        // check activity for inheritance from HooverMenuClickListener
        try {
            this.listener = (HooverMenuClickListener) context;
        } catch (ClassCastException ex) {
            throw new ClassCastException(" Activity must implement HooverMenuClickListener");
        }
    }

    @Click
    void llGallery() {
        listener.onMenuItemClick(MENU_ITEM_GALLERY);
    }

    @Click
    void llEmptyCanvas() {
        listener.onMenuItemClick(MENU_ITEM_EMPTY_CANVAS);

    }

    @Click
    void llCamera() {
        listener.onMenuItemClick(MENU_ITEM_CAMERA);
    }

    public interface HooverMenuClickListener {
        void onMenuItemClick(int menuIdemId);
    }


}
