package com.drawingmagic.eventbus;

import android.os.Bundle;

/**
 * Class represents event which is sent to EventBus Currently in app this is only one type of event,
 * that can be sent to EventBus. Differentiation is possible by type class field
 */
public final class Event {
    public static final int ON_HOTSPOT_STATE_CHANGED = 1;

    /**
     * Type of created event. Used for specifying type of event without creating class hierarchy of
     * different types of events. Possible types of events are class constants.
     */
    public final int type;
    /**
     * Native Android Bunlde object used for sending data between different components inside app.
     * In case of this class is used for sending primitive types or if it is really needed - Object
     * (Parceble), but for the purpose of sending Objects {@link Event#payload}. It is recommended
     * to send only primitive in the bundle
     */
    public Bundle arguments;
    /**
     * Payload object is used for passing any non-primitive data. It is possible to path any type,
     * cause all classes are derived from Object in Java. Type of passed payload is determined by
     * event type at the receiver side It is highly recommended not to pass very complex and large
     * objects, it is your fault in app design if you are doing so
     */
    public Object payload;

    /**
     * @param type      is used for specifying type of event
     * @param arguments bundle containing arguments (primitive key-value pairs)
     * @param payload   object (complex object, lists, arrays...)
     */
    public Event(int type, Bundle arguments, Object payload) {
        this.arguments = arguments;
        this.payload = payload;
        this.type = type;
    }

    /**
     * @param type      is used for specifying type of event
     * @param arguments bundle containing arguments (primitive key-value pairs)
     */
    public Event(int type, Bundle arguments) {
        this.arguments = arguments;
        this.type = type;
    }

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
            case ON_HOTSPOT_STATE_CHANGED:
                return getClass().getCanonicalName() + " ON_HOTSPOT_STATE_CHANGED";
//            case ON_DELETE_HOTSPOT:
//                return getClass().getCanonicalName() + " ON_DELETE_HOTSPOT";
//            case ON_CALENDAR_PLANNING_CLICKED:
//                return getClass().getCanonicalName() + " ON_CALENDAR_PLANNING_CLICKED";
//            case ON_GO_TO_DAILY_PLANNING_CLICKED:
//                return getClass().getCanonicalName() + " ON_GO_TO_DAILY_PLANNING_CLICKED";
//            case ON_GO_TO_TV_CLICKED:
//                return getClass().getCanonicalName() + " ON_GO_TO_TV_CLICKED";
//            case ON_UPDATE_APP:
//                return getClass().getCanonicalName() + " ON_UPDATE_APP";
//            case ON_CLOSE_DIALOG_REQUEST:
//                return getClass().getCanonicalName() + " ON_CLOSE_DIALOG_REQUEST";
//            case ON_CHANGE_COLOR_REQUEST:
//                return getClass().getCanonicalName() + " ON_CHANGE_COLOR_REQUEST";
//            case ON_CHANGE_TEXT_SIZE_REQUEST:
//                return getClass().getCanonicalName() + " ON_CHANGE_TEXT_SIZE_REQUEST";
//            case ON_CLEAR_CANVAS_REQUEST:
//                return getClass().getCanonicalName() + " ON_CLEAR_CANVAS_REQUEST";
//            case ON_LOGIN_USER_REQUEST:
//                return getClass().getCanonicalName() + " ON_LOGIN_USER_REQUEST";
//            case ON_TRANSPORT_PLAN_CLICKED:
//                return getClass().getCanonicalName() + " ON_TRANSPORT_PLAN_CLICKED";
//            case ON_CREATE_HOTSPOT_REQUEST:
//                return getClass().getCanonicalName() + " ON_CREATE_HOTSPOT_REQUEST";
//            case ON_DELETE_ADVANCED_PLANNING:
//                return getClass().getCanonicalName() + " ON_DELETE_ADVANCED_PLANNING";
//            case ON_EDIT_ADVANCED_PLAN_REQUEST:
//                return getClass().getCanonicalName() + " ON_EDIT_ADVANCED_PLAN_REQUEST";
//            case ON_FIRE_PLAN_REQUEST:
//                return getClass().getCanonicalName() + " ON_EDIT_ADVANCED_PLAN_REQUEST";
//            case ON_SAVE_WHITE_BOARD_REQUEST:
//                return getClass().getCanonicalName() + " ON_SAVE_WHITE_BOARD_REQUEST";
//            default:
//                break;
        }
        return "Unknown EVENT";
    }

}

