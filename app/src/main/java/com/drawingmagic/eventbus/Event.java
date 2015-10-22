package com.drawingmagic.eventbus;

/**
 * Class represents event which is sent to EventBus Currently in app this is only one type of event,
 * that can be sent to EventBus. Differentiation is possible by type class field
 */
public final class Event {
    public static final int ON_UNDO = 1;
    public static final int ON_REDO = 2;
    public static final int ON_CLEAR_CANVAS = 3;
    public static final int ON_TILT_FACTOR_X_CHANGED = 4;
    public static final int ON_TILT_FACTOR_Y_CHANGED = 5;
    public static final int ON_ADJUSTER_VALUE_CHANGED = 6;
    public static final int ON_ROTATE = 7;
    public static final int ON_APPLY_CROPPING = 8;
    public static final int ON_RESTORE_IMAGE_BEFORE_CROPPING = 9;
    public static final int ON_APPLY_EFFECT = 10;
    public static final int ON_ADJUST_FILTER_LEVEL = 11;
    public static final int ON_ABS_MENU_CLICKED = 12;
    public static final int ON_ABS_MENU_APPLY = 13;
    public static final int ON_ABS_MENU_RESTORE = 14;
    public static final int ON_ABS_MENU_CANCEL = 15;
    public static final int ON_ROTATE_TRANSFORMATION = 16;
    public static final int ON_SKEW_TRANSFORMATION = 17;
    public static final int ON_CHANGE_CROPPING_SHAPE = 18;
    public static final int ON_FINISHED_ROTATION = 19;
    public static final int ON_FINAL_SAVE_IMAGE = 20;
    public static final int SHARE_VK = 21;
    public static final int SHARE_FACEBOOK = 22;
    public static final int SHARE_TWITTER = 23;
    public static final int SHARE_ODNOKLASSNIKI = 24;
    public static final int ON_APPLY_DRAWING_ON_CANVAS = 25;
    public static final int ON_RESTORE_IMAGE_BEFORE_DRAWING = 26;
    public static final int FLIP = 27;
    public static final int MIRROR = 28;


    /**
     * Type of created event. Used for specifying type of event without creating class hierarchy of
     * different types of events. Possible types of events are class constants.
     */
    public final int type;

    /**
     * Payload object is used for passing any non-primitive data. It is possible to path any type,
     * cause all classes are derived from Object in Java. Type of passed payload is determined by
     * event type at the receiver side It is highly recommended not to pass very complex and large
     * objects, it is your fault in app design if you are doing so
     */
    public Object payload;

    /**
     * @param type is used for specifying type of event
     */
    public Event(int type) {
        this.type = type;
    }

    /**
     * @param type is used for specifying type of event
     */
    public Event(int type, Object payload) {
        this.type = type;
        this.payload = payload;
    }

    /**
     * @return string representation of current event in human readable format
     */
    @Override
    public String toString() {
        switch (type) {
            case ON_UNDO:
                return getClass().getCanonicalName() + " ON_UNDO";
            case ON_REDO:
                return getClass().getCanonicalName() + " ON_REDO";
            case ON_TILT_FACTOR_X_CHANGED:
                return getClass().getCanonicalName() + " ON_TILT_FACTOR_X_CHANGED";
            case ON_TILT_FACTOR_Y_CHANGED:
                return getClass().getCanonicalName() + " ON_TILT_FACTOR_Y_CHANGED";
            case ON_ROTATE:
                return getClass().getCanonicalName() + " ON_ROTATE";
            case ON_APPLY_CROPPING:
                return getClass().getCanonicalName() + " ON_APPLY_CROPPING";
            case ON_ADJUSTER_VALUE_CHANGED:
                return getClass().getCanonicalName() + " ON_ADJUSTER_VALUE_CHANGED";
            case ON_CLEAR_CANVAS:
                return getClass().getCanonicalName() + " ON_CLEAR_CANVAS";
            case ON_RESTORE_IMAGE_BEFORE_CROPPING:
                return getClass().getCanonicalName() + " ON_RESTORE_IMAGE_BEFORE_CROPPING";
            case ON_APPLY_EFFECT:
                return getClass().getCanonicalName() + " ON_APPLY_EFFECT";
            case ON_ADJUST_FILTER_LEVEL:
                return getClass().getCanonicalName() + " ON_ADJUST_FILTER_LEVEL";
            case ON_ABS_MENU_CLICKED:
                return getClass().getCanonicalName() + " ON_ABS_MENU_CLICKED";
            case ON_ABS_MENU_APPLY:
                return getClass().getCanonicalName() + " ON_ABS_MENU_APPLY";
            case ON_ABS_MENU_RESTORE:
                return getClass().getCanonicalName() + " ON_ABS_MENU_RESTORE";
            case ON_ABS_MENU_CANCEL:
                return getClass().getCanonicalName() + " ON_ABS_MENU_CANCEL";
            case ON_ROTATE_TRANSFORMATION:
                return getClass().getCanonicalName() + " ON_ROTATE_TRANSFORMATION";
            case ON_SKEW_TRANSFORMATION:
                return getClass().getCanonicalName() + " ON_SKEW_TRANSFORMATION";
            case ON_CHANGE_CROPPING_SHAPE:
                return getClass().getCanonicalName() + " ON_CHANGE_CROPPING_SHAPE";
            case ON_FINISHED_ROTATION:
                return getClass().getCanonicalName() + " ON_FINISHED_ROTATION";
            case ON_FINAL_SAVE_IMAGE:
                return getClass().getCanonicalName() + " ON_FINAL_SAVE_IMAGE";
            case SHARE_FACEBOOK:
                return getClass().getCanonicalName() + " SHARE_FACEBOOK";
            case SHARE_VK:
                return getClass().getCanonicalName() + " SHARE_VK";
            case SHARE_TWITTER:
                return getClass().getCanonicalName() + " SHARE_TWITTER";
            case SHARE_ODNOKLASSNIKI:
                return getClass().getCanonicalName() + " SHARE_ODNOKLASSNIKI";
            case ON_APPLY_DRAWING_ON_CANVAS:
                return getClass().getCanonicalName() + " ON_APPLY_DRAWING_ON_CANVAS";
            case ON_RESTORE_IMAGE_BEFORE_DRAWING:
                return getClass().getCanonicalName() + " ON_RESTORE_IMAGE_BEFORE_DRAWING";
            case FLIP:
                return getClass().getCanonicalName() + " FLIP";
            case MIRROR:
                return getClass().getCanonicalName() + " MIRROR";
//            case FLIP_HORIZONTAL:
//                return getClass().getCanonicalName() + " FLIP_HORIZONTAL";
//            case FLIP_HORIZONTAL:
//                return getClass().getCanonicalName() + " FLIP_HORIZONTAL";
//            case FLIP_HORIZONTAL:
//                return getClass().getCanonicalName() + " FLIP_HORIZONTAL";
//            case FLIP_HORIZONTAL:
//                return getClass().getCanonicalName() + " FLIP_HORIZONTAL";
            default:
                break;
        }
        return "Unknown EVENT";
    }


}

