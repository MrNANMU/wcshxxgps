package gpsutils.wcshxx.com.gps.ui.home.fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.GpsStatus;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import gpsutils.wcshxx.com.gps.R;
import gpsutils.wcshxx.com.gps.base.callback.NewGPSFileCreateListener;
import gpsutils.wcshxx.com.gps.base.context.App;
import gpsutils.wcshxx.com.gps.base.view.BaseFragment;
import gpsutils.wcshxx.com.gps.base.callback.GPSInfoChangeListener;
import gpsutils.wcshxx.com.gps.config.Config;
import gpsutils.wcshxx.com.gps.ui.home.adapter.InfoAdapter;
import gpsutils.wcshxx.com.gps.ui.home.service.LogService;
import gpsutils.wcshxx.com.gps.ui.home.service.LogService2;
import gpsutils.wcshxx.com.gps.utils.DateUtils;
import gpsutils.wcshxx.com.gps.utils.FileUtils;
import gpsutils.wcshxx.com.gps.utils.GPSUtils;
import gpsutils.wcshxx.com.gps.utils.LogUtils;
import gpsutils.wcshxx.com.gps.utils.ToastUtils;

public class InfoFragment extends BaseFragment implements View.OnClickListener,GpsStatus.NmeaListener{

    private RecyclerView rv_info;
    private InfoAdapter adapter;
    private LinearLayoutManager manager;
    private List<String> list = new ArrayList<>();
    private Map<String,String> gpsDataMap = new HashMap<>();
    private FloatingActionButton fab_get;
    private LogService.LogBinder logService;
    private LogService2.GpsBinder gpsBinder;
    private File file;
    private FileWriter fileWriter;
    private NewGPSFileCreateListener newFileListener;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtils.e("onServiceConnected");
            gpsBinder = (LogService2.GpsBinder)service;
            /*logService = (LogService.LogBinder)service;
            logService.setNmeaListener(InfoFragment.this);
            logService.bindHandler(new InfoHandler());
            if (fileWriter == null) {
                try {
                    if (TextUtils.isEmpty(logService.getUsedFilePath())) {
                        file = FileUtils.create(String.valueOf(System.currentTimeMillis()) + ".txt", true);
                        logService.setUsedFilePath(file.getAbsolutePath());
                    } else {
                        BufferedReader reader = new BufferedReader(new FileReader(logService.getUsedFilePath()));
                        if (reader.ready()) {
                            String str;
                            while (!TextUtils.isEmpty(str = reader.readLine())) {
                                list.add(str);
                            }
                            adapter.notifyDataSetChanged();
                        }
                        reader.close();
                        file = new File(logService.getUsedFilePath());
                    }
                    fileWriter = new FileWriter(file, true);
                } catch (IOException e) {
                    e.printStackTrace();
                    ToastUtils.show("检测到您可能没有开启读写权限，无法保存历史记录");
                }
            }
            logService.start();*/
            gpsBinder.start();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            ToastUtils.show("未知异常");
        }
    };

    public static InfoFragment create(){
        return new InfoFragment();
    }

    public static InfoFragment create(Bundle bundle){
        InfoFragment fragment = new InfoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getResource() {
        return R.layout.fragment_info;
    }

    @Override
    public void getParameter(Bundle bundle) {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        rv_info = $(R.id.rv_info);
        fab_get = $(R.id.fab_get);
        setLinteners();

        adapter = new InfoAdapter(context,list);
        manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_info.setAdapter(adapter);
        rv_info.setLayoutManager(manager);

    }

    private void setLinteners(){
        fab_get.setOnClickListener(this);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        List<String> list = Config.getShowType();
        for(String type:list){
            gpsDataMap.put(type,"");
        }
        setButtonImg(App.isServiceRunning(LogService.class.getName()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_get:
                /*boolean isServiceRunning = (Boolean) v.getTag();
                if(isServiceRunning){
                    logService.stop(countDownTimer,fileWriter,file,newFileListener);
                    context.unbindService(connection);
                    App.stopService(LogService.class);
                    setButtonImg(false);
                    resetCountStatus();
                }else{
                    App.startService(LogService.class);
                    Intent startService = new Intent(context,LogService.class);
                    startService.putExtra("from","MainActvity.InfoFragment");
                    context.bindService(startService,connection, Context.BIND_AUTO_CREATE);
                    setButtonImg(true);
                }*/
                context.bindService(new Intent(context,LogService2.class),connection,Context.BIND_AUTO_CREATE);
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Thread.sleep(20000);
                            ((Activity)context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    context.unbindService(connection);
                                    context.stopService(new Intent(context,LogService2.class));
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                break;
        }
    }

    private void setButtonImg(boolean isServiceRunning){
        if(isServiceRunning){
            fab_get.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.save));
        }else{
            fab_get.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.log));
        }
        fab_get.setTag(isServiceRunning);
    }

    public void updateGpsInfo(String GPGGA) {
        adapter.add(GPGGA);
        rv_info.smoothScrollToPosition(list.size());
        if(fileWriter != null){
            try {
                fileWriter.write(GPGGA+"\n");
            } catch (IOException e) {
                e.printStackTrace();
                ToastUtils.show("保存数据失败");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(logService != null){
            logService.distoryNmeaListener();
        }
        if(fileWriter != null){
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setNewGPSFileCreateListener(NewGPSFileCreateListener newFileListener){
        this.newFileListener = newFileListener;
    }

    private boolean isFirstGet = true;
    private MyCountDownTimer countDownTimer = new MyCountDownTimer(Config.getInterval(),1000);

    @Override
    public void onNmeaReceived(long timestamp, String nmea) {
        LogUtils.e("nmea -> "+nmea);
        String typeHead = nmea.substring(0,nmea.indexOf(","));
        List<String> type = Config.getShowType();
        if(type.contains(typeHead)){
            if(isFirstGet){
                updateGpsInfo(nmea);
                isFirstGet = false;
                countDownTimer.start();
            }else{
                gpsDataMap.put(typeHead,nmea);
            }
        }
    }

    private void resetCountStatus(){
        isFirstGet = true;
        countDownTimer = new MyCountDownTimer(Config.getInterval(),1000);
        list.removeAll(list);
        adapter.notifyDataSetChanged();
        fileWriter = null;
    }

    public class MyCountDownTimer extends CountDownTimer{

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            for(String type:Config.getShowType()){
               updateGpsInfo(gpsDataMap.get(type));
            }
            start();
        }
    }

    public class InfoHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String nmea = (String)msg.obj;
            updateGpsInfo(nmea);
        }

    }

}
