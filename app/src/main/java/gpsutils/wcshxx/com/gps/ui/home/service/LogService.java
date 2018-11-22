package gpsutils.wcshxx.com.gps.ui.home.service;

import android.app.Service;
import android.content.Intent;
import android.location.GpsStatus;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gpsutils.wcshxx.com.gps.base.callback.GPSInfoChangeListener;
import gpsutils.wcshxx.com.gps.base.callback.NewGPSFileCreateListener;
import gpsutils.wcshxx.com.gps.base.context.App;
import gpsutils.wcshxx.com.gps.config.Config;
import gpsutils.wcshxx.com.gps.ui.home.fragment.InfoFragment;
import gpsutils.wcshxx.com.gps.utils.FileUtils;
import gpsutils.wcshxx.com.gps.utils.GPSUtils;
import gpsutils.wcshxx.com.gps.utils.LogUtils;
import gpsutils.wcshxx.com.gps.utils.ToastUtils;

public class LogService extends Service {

    private boolean doing = true;
    private InfoFragment.InfoHandler infoHandler;
    private LogBinder binder;
    private GpsStatus.NmeaListener gpsListener;
    private String usePath;
    private GPSUtils gpsUtils;

    public class LogBinder extends Binder{

        public void bindHandler(InfoFragment.InfoHandler handler){
            infoHandler = handler;
        }

        public void unbindHandler(){
            infoHandler = null;
        }

        public void setNmeaListener(GpsStatus.NmeaListener listener){
            gpsListener = listener;
        }

        public void distoryNmeaListener(){
            gpsListener = null;
        }

        public void setUsedFilePath(String path){
            usePath = path;
        }

        public String getUsedFilePath(){
            return usePath;
        }

        public void start(){
            LogService.this.start();
        }

        public void stop(InfoFragment.MyCountDownTimer countDownTimer, FileWriter fileWriter, File file,NewGPSFileCreateListener newFileListener){
            countDownTimer.cancel();
            LogService.this.stop();
            unbindHandler();
            distoryNmeaListener();
            if(fileWriter != null){
                try {
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(FileUtils.isEmpty(file,true)){
                file.delete();
            }else{
                if(newFileListener != null){
                    newFileListener.newFileCreate(file.getAbsolutePath());
                }
            }
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        binder = new LogBinder();
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        gpsUtils = GPSUtils.init(App.getApplication());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        usePath = null;
    }

    private void start(){
        doing = true;
        getLocationAsync();
    }

    private void stop(){
        doing = false;
        gpsUtils.stop();
    }

    private void getLocationAsync(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                gpsUtils.getLocation(new GpsStatus.NmeaListener() {
                    @Override
                    public void onNmeaReceived(long timestamp, String nmea) {
                        if(!doing){
                            return;
                        }
                        List<String> types = Config.getShowType();
                        Set<String> typeCount = new HashSet<>();
                        String[] nemas = new String[types.size()];
                        Message ms = Message.obtain();
                        for(int i=0; i<types.size(); i++){
                            if(nmea.startsWith(types.get(i))){
                                typeCount.add(types.get(i));
                                if(typeCount.size() == types.size()){
                                    ms.obj = nmea;
                                    infoHandler.sendMessage(ms);
                                    LogUtils.e(nmea);
                                }
                                try {
                                    Thread.sleep(Config.getInterval());
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        }
                    }
                });
                Looper.loop();
            }
        }).start();
    }

}
