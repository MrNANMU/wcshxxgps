package gpsutils.wcshxx.com.gps.utils;

import android.view.Gravity;
import android.view.WindowManager;

import gpsutils.wcshxx.com.gps.R;
import gpsutils.wcshxx.com.gps.base.view.BaseDialog;


/**
 * 此工具类是为了把常用的Dialog重复性工作，如动画效果、位置等封装起来，方便设置
 */
public class DialogHelper {

    /**
     * 无边框底部弹出
     */
    public static void fromBottom(BaseDialog dialog){
        dialog.setAnim(R.style.MyAnim_FromBottom);
        dialog.setDialogLocation(0,0,PixUtils.getScreenWidth(), WindowManager.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);
    }

    /**
     * 批量将Dialog添加到DialogManager中
     * @param dialogs
     */
    public static void addToManager(BaseDialog...dialogs){
        for(BaseDialog dialog:dialogs){
            dialog.addToManager();
        }
    }

    public static void showUpLoadDialog(){

    }



}
