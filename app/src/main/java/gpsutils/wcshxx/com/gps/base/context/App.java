package gpsutils.wcshxx.com.gps.base.context;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import gpsutils.wcshxx.com.gps.config.Config;
import gpsutils.wcshxx.com.gps.utils.LogUtils;
import gpsutils.wcshxx.com.gps.utils.SharePreUtils;

public class App extends Application {

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        LogUtils.allowLog(false);
        if(isFirstRunning()){
            Config.reset();
        }
    }

    public static App getApplication() {
        return instance;
    }

    public static boolean isServiceRunning(String ServiceName) {
        if (("").equals(ServiceName) || ServiceName == null){
            return false;
        }
        ActivityManager myManager = (ActivityManager) instance.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString().equals(ServiceName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isServiceRunning(Service service){
        return isServiceRunning(service.getClass().getName());
    }

    public static boolean isFirstRunning(){
        boolean first = !SharePreUtils.getBoolean("IS_FIRST_RUNNING");
        if(first){
            SharePreUtils.saveBoolean("IS_FIRST_RUNNING",first);
        }
        return first;
    }

    public static void startService(Class service){
        Intent intent = new Intent(instance,service);
        intent.putExtra("from","application");
        instance.startService(intent);
    }

    public static void stopService(Class service){
        instance.stopService(new Intent(instance,service));
    }
}
