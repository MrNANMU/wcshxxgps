package gpsutils.wcshxx.com.gps.utils;

import android.widget.EditText;

public class ViewHelper {

    /**
     * 设置EditText是否获取焦点
     * @param v
     * @param focusable
     */
    public static void setEditTextFocusable(EditText v, boolean focusable){
        v.setFocusable(focusable);
        v.setFocusableInTouchMode(focusable);
        v.requestFocus();
        v.requestFocusFromTouch();
    }
}
