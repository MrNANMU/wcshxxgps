package gpsutils.wcshxx.com.gps.utils;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import gpsutils.wcshxx.com.gps.base.context.App;

public class ToastUtils {

    public static void show(final String toast){
        show(toast,false);
    }

    public static void show(final String toast,final boolean isLong){
        if("main".equals(Thread.currentThread().getName())){
            Toast.makeText(App.getApplication(),toast,isLong?Toast.LENGTH_LONG:Toast.LENGTH_SHORT).show();
        }else{
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(App.getApplication(),toast,isLong?Toast.LENGTH_LONG:Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * TextView及其子类的字符串为空时弹出Toast
     * @param v 要检验的View
     * @param toast 为空时要显示的Toast内容
     */
    public static void emptyStringToast(TextView v,String toast){
        if(v == null || TextUtils.isEmpty(v.getText())){
            show(toast);
        }
    }

}
