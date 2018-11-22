package gpsutils.wcshxx.com.gps.utils;

import android.graphics.drawable.Drawable;
import android.service.autofill.Sanitizer;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import gpsutils.wcshxx.com.gps.R;
import gpsutils.wcshxx.com.gps.base.context.App;

public class SnackUtils {

    public static class SnackOptions {

        public static final int LEFT = 0;
        public static final int RIGHT = 1;

        public View v;
        public String msg;
        public int duration;
        public Snackbar.Callback callback;
        public int backgroundColor = -1;
        public int msgColor = -1;
        public int actionTextColor = -1;
        public int msgIcon = -1;
        public int iconLocation = 0;

        public SnackOptions setView(View v) {
            this.v = v;
            return this;
        }

        public SnackOptions setMsg(String msg) {
            this.msg = msg;
            return this;
        }

        public SnackOptions setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        public SnackOptions setCallback(Snackbar.Callback callback) {
            this.callback = callback;
            return this;
        }

        public SnackOptions setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public SnackOptions setMsgColor(int msgColor) {
            this.msgColor = msgColor;
            return this;
        }

        public SnackOptions setActionTextColor(int actionTextColor) {
            this.actionTextColor = actionTextColor;
            return this;
        }

        public SnackOptions setMsgIcon(int msgIcon) {
            this.msgIcon = msgIcon;
            return this;
        }

        public SnackOptions setIconLocation(int iconLocation) {
            this.iconLocation = iconLocation;
            return this;
        }

    }

    public static SnackOptions getOptions(){
        return new SnackOptions();
    }

    public static Snackbar make(SnackOptions options){
        if(options == null){
            throw new NullPointerException("Options不能为Null");
        }
        if(options.v == null){
            throw new NullPointerException("Options的View不能为Null");
        }
        Snackbar snackbar = Snackbar.make(options.v,options.msg,options.duration);
        if(options.callback != null){
            snackbar.addCallback(options.callback);
        }
        if(options.actionTextColor != -1){
            snackbar.setActionTextColor(options.actionTextColor);
        }
        if(options.backgroundColor != -1){
            snackbar.getView().setBackgroundColor(options.backgroundColor);
        }
        TextView textView = null;
        if(options.msgColor != -1){
            //Design包下表明SnackBar的文字控件ID为 R.id.snackbar_text，为防止以后有修改，获取后再判断一次是否为null
            textView = snackbar.getView().findViewById(R.id.snackbar_text);
            if(textView != null){
                textView.setTextColor(options.msgColor);
            }
        }
        if(options.msgIcon != -1){
            if(textView == null){
                textView = snackbar.getView().findViewById(R.id.snackbar_text);
            }
            if(textView != null){
                Drawable d = ContextCompat.getDrawable(App.getApplication(), options.msgIcon);
                d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());
                if(options.iconLocation == SnackOptions.LEFT){
                    // 给TextView左边添加图标
                    textView.setCompoundDrawables(d, null, null, null);
                }else{
                    // 给TextView右边添加图标
                    textView.setCompoundDrawables(null, null, d, null);
                }
                // 让文字居中
                textView.setGravity(Gravity.CENTER);
            }
        }
        //SnackBar的action控件ID为 R.id.snackbar_action，类型为Button
        //虽然也可以进行一些操作，但为了整体美观度这些设置足够
        return snackbar;
    }

}
