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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gpsutils.wcshxx.com.gps.R;
import gpsutils.wcshxx.com.gps.base.callback.NewGPSFileCreateListener;
import gpsutils.wcshxx.com.gps.base.context.App;
import gpsutils.wcshxx.com.gps.base.view.BaseFragment;
import gpsutils.wcshxx.com.gps.config.Config;
import gpsutils.wcshxx.com.gps.ui.home.adapter.InfoAdapter;
import gpsutils.wcshxx.com.gps.ui.home.service.LogService;
import gpsutils.wcshxx.com.gps.ui.home.service.LogService2;
import gpsutils.wcshxx.com.gps.utils.LogUtils;
import gpsutils.wcshxx.com.gps.utils.ToastUtils;

public class InfoFragment2 extends BaseFragment implements View.OnClickListener{

    private RecyclerView rv_info;
    private InfoAdapter adapter;
    private LinearLayoutManager manager;
    private List<String> list;
    private Map<String,String> gpsDataMap = new HashMap<>();
    private FloatingActionButton fab_get;
    private LogService2.GpsBinder gpsBinder;
    private NewGPSFileCreateListener newFileListener;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtils.e("onServiceConnected");
            gpsBinder = (LogService2.GpsBinder)service;
            gpsBinder.init();
            list = gpsBinder.getInfoList();
            adapter = new InfoAdapter(context,list);
            manager = new LinearLayoutManager(context);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            rv_info.setAdapter(adapter);
            rv_info.setLayoutManager(manager);
            gpsBinder.bindAdapter(adapter);
            gpsBinder.bindRecyclerView(rv_info);
            gpsBinder.start();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            ToastUtils.show("未知异常");
        }
    };

    public static InfoFragment2 create(){
        return new InfoFragment2();
    }

    public static InfoFragment2 create(Bundle bundle){
        InfoFragment2 fragment = new InfoFragment2();
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
                boolean isServiceRunning = (Boolean) v.getTag();
                if(isServiceRunning){
                    context.unbindService(connection);
                    setButtonImg(false);
                    resetCountStatus();
                    newFileListener.newFileCreate(gpsBinder.getcurrentFilePath());
                    gpsBinder.stop();
                }else{
                    context.bindService(new Intent(context,LogService2.class),connection,Context.BIND_AUTO_CREATE);
                    setButtonImg(true);
                }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(gpsBinder != null){
            gpsBinder.unbind();
        }
    }

    public void setNewGPSFileCreateListener(NewGPSFileCreateListener newFileListener){
        this.newFileListener = newFileListener;
    }

    private void resetCountStatus(){
        list.removeAll(list);
        adapter.notifyDataSetChanged();
    }

}
