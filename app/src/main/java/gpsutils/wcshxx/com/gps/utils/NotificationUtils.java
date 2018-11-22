package gpsutils.wcshxx.com.gps.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import gpsutils.wcshxx.com.gps.R;
import gpsutils.wcshxx.com.gps.base.context.App;

public class NotificationUtils {

    //创建通知至少包含 小图标、标题、内容 才能显示

    public static final String NOTIFICATION_CHANNEL_ID = "WCSHXX";
    private static final NotificationManager MANAGER = (NotificationManager)App.getApplication().getSystemService(Context.NOTIFICATION_SERVICE);

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Notification create(int id){
        MANAGER.createNotificationChannel(createChannel());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(App.getApplication(),NOTIFICATION_CHANNEL_ID);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("GPS记录仪正在运行")
                .setContentText("正在为您记录数据");
        return builder.build();
    }

    private static NotificationChannel createChannel(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return null;
        }
        LogUtils.e("create channel");
        NotificationChannel nc = new NotificationChannel(NOTIFICATION_CHANNEL_ID,"记录仪后台服务", NotificationManager.IMPORTANCE_DEFAULT);
        nc.enableLights(false); //是否在桌面icon右上角展示小红点
        nc.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
        return nc;
    }

    public static NotificationManager getNotificationManager(){
        return MANAGER;
    }

}
