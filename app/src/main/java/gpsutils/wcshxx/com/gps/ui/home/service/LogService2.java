package gpsutils.wcshxx.com.gps.ui.home.service;

import android.app.Service;
import android.content.Intent;
import android.location.GpsStatus;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import gpsutils.wcshxx.com.gps.config.Config;
import gpsutils.wcshxx.com.gps.utils.FileUtils;
import gpsutils.wcshxx.com.gps.utils.GPSUtils;
import gpsutils.wcshxx.com.gps.utils.LogUtils;
import gpsutils.wcshxx.com.gps.utils.NotificationUtils;
import rx.AsyncEmitter;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class LogService2 extends Service {

    private static final int NOTIFICATION_ID = 9210;
    private GPSUtils gpsUtils;
    private Subscription uiSubscription;
    private File file;
    private String currentFileName;

    public class GpsBinder extends Binder{

        public void start(){
            Observable<String> observable = observable();
            uiSubscription = observable.throttleWithTimeout(Config.getInterval(), TimeUnit.MILLISECONDS).observeOn(Schedulers.io()).map(new Func1<String, String>() {
                @Override
                public String call(String s) {
                    LogUtils.e("保存文件 -> 进程:"+Thread.currentThread().getName());
                    return "变换后:::"+s;
                }
            }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
                @Override
                public void call(String s) {
                    LogUtils.e("更新UI -> 进程:"+Thread.currentThread().getName()+";内容:"+s);
                }
            });
        }

        public void stop(){
            stopForeground(true);
            if(uiSubscription != null && !uiSubscription.isUnsubscribed()){
                uiSubscription.unsubscribe();
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new GpsBinder();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(NOTIFICATION_ID, NotificationUtils.create(NOTIFICATION_ID));
        gpsUtils = GPSUtils.init(this);
        currentFileName = String.valueOf(System.currentTimeMillis());
        file = FileUtils.open(currentFileName + ".txt");
        if(file == null){
            file = FileUtils.create(currentFileName + ".txt", true);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        if(uiSubscription != null && !uiSubscription.isUnsubscribed()){
            uiSubscription.unsubscribe();
        }
    }

    private Observable<String> observable(){
        return Observable.fromEmitter(new Action1<AsyncEmitter<String>>() {
            @Override
            public void call(final AsyncEmitter<String> asyncEmitter) {
                GpsStatus.NmeaListener listener = new GpsStatus.NmeaListener(){

                    @Override
                    public void onNmeaReceived(long timestamp, String nmea) {
                        /*List<String> showTypes = Config.getShowType();
                        final int i = showTypes.size();*/
                        if(nmea.startsWith("$GPGGA")){
                            LogUtils.e("所在进程："+Thread.currentThread().getName());
                            asyncEmitter.onNext(nmea);
                        }
                    }
                };
                asyncEmitter.setCancellation(new AsyncEmitter.Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        LogUtils.e("取消定位");
                        gpsUtils.stop();
                    }
                });
                gpsUtils.getLocation(listener);
            }
        }, AsyncEmitter.BackpressureMode.LATEST);
    }
}
