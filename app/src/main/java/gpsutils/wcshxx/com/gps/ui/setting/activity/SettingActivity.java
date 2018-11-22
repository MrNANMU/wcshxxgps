package gpsutils.wcshxx.com.gps.ui.setting.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import gpsutils.wcshxx.com.gps.R;
import gpsutils.wcshxx.com.gps.base.context.BaseActivity;
import gpsutils.wcshxx.com.gps.config.Config;
import gpsutils.wcshxx.com.gps.ui.setting.dialog.SetReaderDialog;
import gpsutils.wcshxx.com.gps.utils.LogUtils;
import gpsutils.wcshxx.com.gps.utils.ToastUtils;
import gpsutils.wcshxx.com.gps.utils.ViewHelper;

public class SettingActivity extends BaseActivity implements View.OnClickListener,View.OnFocusChangeListener {

    private RelativeLayout rl_interval_wrap;
    private EditText et_interval;

    private RelativeLayout rl_maxitem_wrap;
    private EditText et_maxitem;

    private RelativeLayout rl_reader_wrap;
    private TextView tv_reader;

    private RelativeLayout rl_reset_wrap;

    private boolean intEditting = false;
    private boolean maxEditting = false;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        rl_interval_wrap = $(R.id.rl_interval_wrap);
        et_interval = $(R.id.et_interval);

        rl_maxitem_wrap = $(R.id.rl_maxitem_wrap);
        et_maxitem = $(R.id.et_maxitem);

        rl_reader_wrap = $(R.id.rl_reader_wrap);
        tv_reader = $(R.id.tv_reader);

        rl_reset_wrap = $(R.id.rl_reset_wrap);

        ViewHelper.setEditTextFocusable(et_interval,false);
        ViewHelper.setEditTextFocusable(et_maxitem,false);

        setListeners();
    }

    private void setListeners(){
        rl_interval_wrap.setOnClickListener(this);
        rl_maxitem_wrap.setOnClickListener(this);
        rl_reader_wrap.setOnClickListener(this);
        rl_reset_wrap.setOnClickListener(this);

        et_interval.setOnFocusChangeListener(this);
        et_maxitem.setOnFocusChangeListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        et_interval.setText(Config.getInterval()/1000 + "");
        et_maxitem.setText(Config.getMaxItemsNumber()+"");
        switch (Config.getReader()){
            case Config.Reader.APP:
                tv_reader.setText("GPS记录仪");
                break;
            case Config.Reader.NOT_SELECT:
                tv_reader.setText("阅读时选择");
                break;
            case Config.Reader.SYSTEM:
                tv_reader.setText("系统文件浏览器");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(intEditting){
            String strInterval = et_interval.getText().toString();
            if(TextUtils.isEmpty(strInterval)){
                et_interval.setText(Config.getInterval()/1000+"");
            }else{
                Config.setInterval(Integer.valueOf(strInterval)*1000);
            }
        }
        if(maxEditting){
            String strMaxitem = et_maxitem.getText().toString();
            if(TextUtils.isEmpty(strMaxitem)){
                et_maxitem.setText(Config.getMaxItemsNumber()+"");
            }else{
                Config.setMaxItemsNumber(Integer.valueOf(strMaxitem));
            }
        }
        if(intEditting || maxEditting){
            ToastUtils.show("设置成功，下次记录时生效");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_interval_wrap:
                ViewHelper.setEditTextFocusable(et_interval,true);
                break;
            case R.id.rl_maxitem_wrap:
                ViewHelper.setEditTextFocusable(et_maxitem,true);
                break;
            case R.id.rl_reader_wrap:
                SetReaderDialog setReaderDialog = new SetReaderDialog(this);
                setReaderDialog.bindTextView(tv_reader);
                setReaderDialog.show();
                break;
            case R.id.rl_reset_wrap:
                Config.reset();
                et_interval.setText(Config.getInterval()/1000+"");
                et_maxitem.setText(Config.getMaxItemsNumber()+"");
                tv_reader.setText("阅读时选择");
                ToastUtils.show("回复默认设置成功，下次记录时生效");
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case R.id.et_interval:
                if(!hasFocus){
                    intEditting = false;
                    String strInterval = ((EditText)v).getText().toString();
                    if(TextUtils.isEmpty(strInterval)){
                        ((EditText) v).setText(Config.getInterval()/1000+"");
                    }else{
                        Config.setInterval(Integer.valueOf(strInterval)*1000);
                    }
                }else{
                    intEditting = true;
                }
                break;
            case R.id.et_maxitem:
                if(!hasFocus){
                    maxEditting = false;
                    String strMaxitem = ((EditText)v).getText().toString();
                    if(TextUtils.isEmpty(strMaxitem)){
                        ((EditText) v).setText(Config.getMaxItemsNumber()+"");
                    }else{
                        Config.setMaxItemsNumber(Integer.valueOf(strMaxitem));
                    }
                }else{
                    maxEditting = true;
                }
                break;
        }
    }
}
