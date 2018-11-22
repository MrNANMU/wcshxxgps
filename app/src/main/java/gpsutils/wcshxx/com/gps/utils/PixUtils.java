package gpsutils.wcshxx.com.gps.utils;

import android.util.DisplayMetrics;
import android.util.TypedValue;

import gpsutils.wcshxx.com.gps.base.context.App;

public class PixUtils {
    /**
     * 将PX转换为DP
     * @param px
     * @return
     */
    public static int px2dp(int px){
        final float scale = App.getApplication().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 将DP值转换为PX
     * @param dp
     * @return
     */
    public static int dp2px(int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                App.getApplication().getResources().getDisplayMetrics());
    }

    /**
     * 获取屏幕高度
     * @return
     */
    public static int getScreenHeight(){
        DisplayMetrics dm = App.getApplication().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 获取屏幕宽度
     * @return
     */
    public static int getScreenWidth(){
        DisplayMetrics dm = App.getApplication().getResources().getDisplayMetrics();
        return dm.widthPixels;
    }
}
