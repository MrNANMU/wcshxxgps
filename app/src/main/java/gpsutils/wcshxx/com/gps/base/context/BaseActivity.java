package gpsutils.wcshxx.com.gps.base.context;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import gpsutils.wcshxx.com.gps.utils.DialogManager;
import gpsutils.wcshxx.com.gps.utils.PermissionUtils;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public abstract class BaseActivity extends AppCompatActivity implements PermissionUtils.PermissionListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        initView(savedInstanceState);
        initData(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DialogManager.free(this);
    }

    /**
     * 动态申请权限的时候，如果需要监听申请结果，覆写次此方法即可
     * @param allow
     * @param deny
     */
    @Override
    public void onPermissionResult(List<String> allow, List<String> deny) {

    }

    @Override
    public void afterPermissionRequested(boolean isRequested) {

    }

    abstract protected int getContentViewId();

    abstract protected void initView(Bundle savedInstanceState);

    abstract protected void initData(Bundle savedInstanceState);

    public <V extends View> V $(int id){
        return (V)findViewById(id);
    }

    public Drawable findDrawableById(int id){
        return ContextCompat.getDrawable(this,id);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PermissionUtils.REQUEST_PERMISSION){
            List<String> allow = new ArrayList<>();
            List<String> deny = new ArrayList<>();
            for(int i=0; i < permissions.length; i++){
                if(grantResults[i] == PERMISSION_GRANTED){
                    allow.add(permissions[i]);
                }else{
                    deny.add(permissions[i]);
                }
            }
            onPermissionResult(allow,deny);
            afterPermissionRequested(true);
        }
    }


}
