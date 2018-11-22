package gpsutils.wcshxx.com.gps.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GnssStatus;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.OnNmeaMessageListener;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import gpsutils.wcshxx.com.gps.base.context.App;
import gpsutils.wcshxx.com.gps.config.Config;

public class GPSUtils{

    private static GPSUtils instance;
    private Context context;
    private LocationManager mLocationManager;
    private MyLocationListener myLocationListener;
    private GpsStatus.NmeaListener nmeaListener;

    private GPSUtils(){}

    public static GPSUtils init(Context context){
        if(instance == null){
            instance = new GPSUtils();
        }
        instance.context = context;
        instance.mLocationManager = ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE));
        instance.myLocationListener = new MyLocationListener();
        return instance;
    }

    public static GPSUtils getInstance(){
        return instance;
    }

    public void getLocation(GpsStatus.NmeaListener listener) {
        if (ActivityCompat.checkSelfPermission(instance.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            ToastUtils.show("GPS可用");
            nmeaListener = listener;
            mLocationManager.addNmeaListener(nmeaListener);
            //开始定位
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Config.getInterval(), 0, new MyLocationListener());
        }else{
            ToastUtils.show("请打开gps或者选择gps模式为准确度高");
            //前往设置GPS页面
            App.getApplication().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

    }

    public void stop(){
        mLocationManager.removeUpdates(myLocationListener);
        mLocationManager.removeNmeaListener(nmeaListener);
        instance = null;
    }

    private static class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

}
