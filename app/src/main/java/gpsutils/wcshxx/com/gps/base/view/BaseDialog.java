package gpsutils.wcshxx.com.gps.base.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import gpsutils.wcshxx.com.gps.utils.DialogManager;


abstract public class BaseDialog extends Dialog {

    protected View root;
    protected Context context;

    public BaseDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        init();
    }

    protected BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
        init();
    }

    public void init(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        root = LayoutInflater.from(context).inflate(getResourceId(),null);
        setContentView(root);
        initView();
        initData();
    }

    abstract protected int getResourceId();

    abstract protected void initView();

    abstract protected void initData();

    abstract public void reset();

    public void addToManager(){
        DialogManager.add(this);
    }

    public Context getDialogContext(){
        return context;
    }

    public void removeFromManager(){
        DialogManager.remove(this);
    }

    public void setDialogLocation(int x,int y,int w,int h,int gravity){
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.x = x;
        lp.y = y;
        lp.width = w;
        lp.height = h;
        window.setGravity(gravity);
        window.setAttributes(lp);
    }

    public BaseDialog setDialogOffset(int x,int y){
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.x = x;
        lp.y = y;
        window.setAttributes(lp);
        return this;
    }

    public BaseDialog setDialogSize(int w,int h){
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = w;
        lp.height = h;
        window.setAttributes(lp);
        return this;
    }

    public BaseDialog setDialogGravity(int gravity){
        Window window = getWindow();
        window.setGravity(gravity);
        return this;
    }

    public void setAnim(int aniXml){
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.windowAnimations = aniXml;
        getWindow().setAttributes(lp);
    }

    public void setAnim(int animXml, ViewTreeObserver.OnGlobalLayoutListener listener){
        setAnim(animXml);
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(listener);
    }

    public <V extends View> V $(int id){
        return (V)root.findViewById(id);
    }
}
