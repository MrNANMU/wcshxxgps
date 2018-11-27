package gpsutils.wcshxx.com.gps.ui.home.service;

import android.app.Service;
import android.content.Intent;
import android.location.GpsStatus;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import gpsutils.wcshxx.com.gps.config.Config;
import gpsutils.wcshxx.com.gps.ui.home.adapter.InfoAdapter;
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
    private static String currentFileName;
    private List<String> list;

    public class GpsBinder extends Binder{

        private InfoAdapter adapter;
        private RecyclerView recyclerView;

        public void init(){
            list = new ArrayList<>();
        }

        public void start(){
            Observable<String> observable = observable();
            uiSubscription = observable.throttleFirst(Config.getInterval(), TimeUnit.MILLISECONDS).observeOn(Schedulers.io()).map(new Func1<String, String>() {
                @Override
                public String call(String s) {
                    FileUtils.WriteHelper.with(file).append(s).close();
                    return s;
                }
            }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
                @Override
                public void call(String s) {
                    updateUi(s);
                }
            });
        }

        public void stop(){
            stopForeground(true);
            if(uiSubscription != null && !uiSubscription.isUnsubscribed()){
                uiSubscription.unsubscribe();
            }
            currentFileName = null;
        }

        public List<String> getInfoList(){
            return list;
        }

        public void bindAdapter(InfoAdapter adapter){
            this.adapter = adapter;
        }

        public void bindRecyclerView(RecyclerView recyclerView){
            this.recyclerView = recyclerView;
        }

        public void updateUi(String data){
            if(adapter != null){
                adapter.add(data);
            }
            if(recyclerView != null){
                recyclerView.smoothScrollToPosition(list.size());
            }
        }

        public void unbind(){
            recyclerView = null;
            adapter = null;
        }

        public String getcurrentFilePath(){
            if(file != null){
                return file.getAbsolutePath();
            }
            return null;
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
        if(currentFileName == null){
            currentFileName = String.valueOf(System.currentTimeMillis());
        }
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
                        LogUtils.e("数据更新："+nmea);
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
