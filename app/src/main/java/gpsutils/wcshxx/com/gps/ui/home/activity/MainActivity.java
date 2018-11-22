package gpsutils.wcshxx.com.gps.ui.home.activity;

import android.Manifest;
import android.content.Intent;
import android.location.GpsStatus;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gpsutils.wcshxx.com.gps.R;
import gpsutils.wcshxx.com.gps.base.context.BaseActivity;
import gpsutils.wcshxx.com.gps.base.view.BaseFragment;
import gpsutils.wcshxx.com.gps.ui.home.adapter.MyFragmentAdapter;
import gpsutils.wcshxx.com.gps.ui.home.fragment.FileFragment;
import gpsutils.wcshxx.com.gps.ui.home.fragment.InfoFragment;
import gpsutils.wcshxx.com.gps.ui.setting.activity.SettingActivity;
import gpsutils.wcshxx.com.gps.utils.FileUtils;
import gpsutils.wcshxx.com.gps.utils.GPSUtils;
import gpsutils.wcshxx.com.gps.utils.LogUtils;
import gpsutils.wcshxx.com.gps.utils.PermissionUtils;
import gpsutils.wcshxx.com.gps.utils.ToastUtils;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private TabLayout tl_tabs;
    private ViewPager vp_home;
    private ImageView iv_set;
    private List<BaseFragment> fragments;
    private MyFragmentAdapter fragmentAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tl_tabs = $(R.id.tl_tabs);
        vp_home = $(R.id.vp_home);
        iv_set = $(R.id.iv_set);
        iv_set.setOnClickListener(this);
        InfoFragment infoFragment = InfoFragment.create();
        FileFragment fileFragment = FileFragment.create();
        infoFragment.setNewGPSFileCreateListener(fileFragment);
        fragments = new ArrayList<>();
        fragments.add(infoFragment);
        fragments.add(fileFragment);
        fragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(),fragments);
        vp_home.setAdapter(fragmentAdapter);
        tl_tabs.setupWithViewPager(vp_home);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Map<String,String> map = new HashMap<>();
        map.put("定位","获取您的大致位置");
        map.put("读写存储","保存您记录的信息和加载历史信息");
        PermissionUtils.request(this,map,new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE});

    }

    @Override
    public void onPermissionResult(List<String> allow, List<String> deny) {

    }

    @Override
    public void afterPermissionRequested(boolean isRequested) {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }
}
