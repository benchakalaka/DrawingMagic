package com.drawingmagic;

import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.drawingmagic.adapters.ViewPagerAdapter;
import com.drawingmagic.core.DrawingSettings;
import com.drawingmagic.core.DrawingView;
import com.drawingmagic.core.GPUImageFilterTools;
import com.drawingmagic.eventbus.Event;
import com.drawingmagic.fragments.FAdjuster_;
import com.drawingmagic.fragments.FDrawingTools.OnChangeDrawingSettingsListener;
import com.drawingmagic.helpers.FilterItemHolder;
import com.drawingmagic.utils.Conditions;
import com.drawingmagic.utils.Log;
import com.drawingmagic.utils.Notification;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.io.File;

import github.chenupt.springindicator.SpringIndicator;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

import static android.view.View.GONE;
import static android.view.View.LAYER_TYPE_SOFTWARE;
import static android.view.View.VISIBLE;
import static com.drawingmagic.adapters.ViewPagerAdapter.CANVAS_SETTINGS_TOOLS_FRAGMENT;
import static com.drawingmagic.adapters.ViewPagerAdapter.DRAWING_TOOLS_FRAGMENT;
import static com.drawingmagic.adapters.ViewPagerAdapter.EFFECTS_TOOLS_FRAGMENT;
import static com.drawingmagic.core.DrawingView.ShapesType;
import static com.drawingmagic.eventbus.Event.ON_CLEAR_CANVAS;
import static com.drawingmagic.eventbus.Event.ON_REDO;
import static com.drawingmagic.eventbus.Event.ON_TILT_FACTOR_X_CHANGED;
import static com.drawingmagic.eventbus.Event.ON_TILT_FACTOR_Y_CHANGED;
import static com.drawingmagic.eventbus.Event.ON_UNDO;
import static com.drawingmagic.fragments.FCanvasTools.OnChangeCanvasSettingsListener;
import static com.drawingmagic.fragments.FEffectsTools.OnChangeEffectListener;
import static com.drawingmagic.views.HoverView.DRAWING_CACHE_QUALITY_HIGH;
import static com.drawingmagic.views.HoverView.MENU_ITEM_CAMERA;
import static com.drawingmagic.views.HoverView.MENU_ITEM_EMPTY_CANVAS;
import static com.drawingmagic.views.HoverView.MENU_ITEM_GALLERY;
import static jp.co.cyberagent.android.gpuimage.GPUImage.ScaleType.CENTER_INSIDE;
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

    @ViewById
    FrameLayout flFragmentHolder;

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
        gpuImage.setVisibility(GONE);
        gpuImage.setScaleType(CENTER_INSIDE);

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

     //  getSupportFragmentManager().beginTransaction().add(R.id.flFragmentHolder, new FTiltFragmentController_()).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.flFragmentHolder, new FAdjuster_()).commit();

        //fAdjuster.setSeekBarCurrentMinMaxValues(180, 90);
      //  getSupportFragmentManager().beginTransaction().replace(R.id.flFragmentHolder, fAdjuster).commit();

    }

    /**
     * Init View Pager
     */
    private void initViewPager() {
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageTransformer(true, new CubeOutTransformer());
        viewPagerIndicator.setViewPager(viewPager);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case DRAWING_TOOLS_FRAGMENT:
                        drawingView.clearRedoPaths();
                        drawingView.setDrawingData(drawingView.builder().from(drawingView.getDrawingData()).withBitmap(gpuImage.getGPUImage().getBitmapWithFilterApplied()).build());

                        gpuImage.setVisibility(GONE);
                        cropImageView.setVisibility(GONE);
                        drawingView.setVisibility(VISIBLE);
                        gpuImage.getGPUImage().deleteImage();
                        break;

                    case EFFECTS_TOOLS_FRAGMENT:
                        gpuImage.setVisibility(VISIBLE);
                        gpuImage.setImage(ORIGIN_BITMAP);
                        gpuImage.requestRender();
                        cropImageView.setVisibility(GONE);
                        drawingView.setVisibility(GONE);
                        break;

                    case CANVAS_SETTINGS_TOOLS_FRAGMENT:
                        cropImageView.setVisibility(VISIBLE);
                        gpuImage.setVisibility(GONE);
                        drawingView.setVisibility(GONE);
                        cropImageView.setImageBitmap(ORIGIN_BITMAP);
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
        drawingView.setLayerType(LAYER_TYPE_SOFTWARE, null);

        drawingView.setDrawingData(drawingView.builder().from(drawingView.getDrawingData()).
                withBrushWidth(DEFAULT_BRUSH_SIZE, getResources().getDisplayMetrics()).
                withLinesWhileDrawing(false).
                withShape(ShapesType.STANDARD_DRAWING).
                withColor(Color.BLACK).
                withGridEnabled(true).build());
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
    void onResult(int resultCode, final Intent data) {
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }

        try {
            drawingView.post(new Runnable() {
                @Override
                public void run() {
                    if (ORIGIN_BITMAP != null) {
                        ORIGIN_BITMAP.recycle();
                    }

                    ORIGIN_BITMAP = decodeSampledBitmapFromResource(new File(getRealPathFromURI(data.getData())).getAbsolutePath(), drawingView.getWidth(), drawingView.getHeight());
                    drawingView.setDrawingData(drawingView.builder().from(drawingView.getDrawingData()).withBitmap(ORIGIN_BITMAP).withPaths(null).build());
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        gpuImage.setImage(data.getData());
    }


    public static Bitmap decodeSampledBitmapFromResource(String filename, int reqWidth, int reqHeight) {

        Log.e("W: " + reqWidth + " , H:" + reqHeight);

        // First decode with inJustDecodeBounds=true to check dimensions
        final Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filename, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        Log.e("inSampleSize =  " + options.inSampleSize);

        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = true;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filename, options);
    }

    public static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    @Override
    protected void onPause() {
        if (Conditions.isNotNull(ORIGIN_BITMAP)) {
            ORIGIN_BITMAP.recycle();
            ORIGIN_BITMAP = null;
        }
        super.onPause();
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
        Log.e(event.toString());
        switch (event.type) {
            case ON_TILT_FACTOR_X_CHANGED:
                drawingView.setRotationX((int)event.payload);
                break;

            case ON_TILT_FACTOR_Y_CHANGED:
                drawingView.setRotationY((int)event.payload);
                break;

            case ON_UNDO:
                drawingView.undo();
                break;

            case ON_REDO:
                drawingView.redo();
                break;

            case ON_CLEAR_CANVAS:
                drawingView.clearRedoPaths();
                drawingView.setDrawingData(drawingView.builder().from(drawingView.getDrawingData()).withPaths(null).build());

                break;

            default:
                Log.e("Unknown event " + event);
        }
    }


    private void pickUpImageFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, ADrawingMagic.REQUEST_PICK_IMAGE);
    }

    public void setSkewFactor(float skewFactor) {
        drawingView.setRotationY(45 + skewFactor);
        //drawingView.setRotationY(skewFactor-10);
        Log.e("Skew factor : " + skewFactor);
        // drawingView.setSkewFactor(skewFactor);
    }
}
