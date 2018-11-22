package gpsutils.wcshxx.com.gps.ui.home.dialog;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import gpsutils.wcshxx.com.gps.R;
import gpsutils.wcshxx.com.gps.base.context.BaseActivity;
import gpsutils.wcshxx.com.gps.base.view.BaseDialog;
import gpsutils.wcshxx.com.gps.config.Config;
import gpsutils.wcshxx.com.gps.ui.history.activity.HistoryActivity;

public class OpenSetDialog extends BaseDialog implements View.OnClickListener{

    private TextView tv_open_by_gps;
    private TextView tv_open_by_system;
    private LinearLayout ll_checkbox_wrap;
    private CheckBox cb_save_open_set;
    private String filePath;

    public OpenSetDialog(@NonNull Context context) {
        super(context,R.style.MyDialog_NoBackGround);
    }

    public OpenSetDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected OpenSetDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected int getResourceId() {
        return R.layout.dialog_open_set;
    }

    @Override
    protected void initView() {
        tv_open_by_gps = $(R.id.tv_open_by_gps);
        tv_open_by_system = $(R.id.tv_open_by_system);
        ll_checkbox_wrap = $(R.id.ll_checkbox_wrap);
        cb_save_open_set = $(R.id.cb_save_open_set);
        setListeners();
    }

    private void setListeners(){
        tv_open_by_gps.setOnClickListener(this);
        tv_open_by_system.setOnClickListener(this);
        ll_checkbox_wrap.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void reset() {

    }

    public void setOpenPath(String filePath){
        this.filePath = filePath;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_open_by_gps:
                dismiss();
                Config.setReader(cb_save_open_set.isChecked()?Config.Reader.APP:Config.Reader.NOT_SELECT);
                Intent intent = new Intent(context, HistoryActivity.class);
                intent.putExtra("file_path",filePath);
                context.startActivity(intent);
                break;
            case R.id.tv_open_by_system:
                dismiss();
                Config.setReader(cb_save_open_set.isChecked()?Config.Reader.SYSTEM:Config.Reader.NOT_SELECT);
                Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
                //设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent2.setType("*/*");
                intent2.addCategory(Intent.CATEGORY_OPENABLE);
                ((BaseActivity)context).startActivityForResult(intent2,1);
                break;
            case R.id.ll_checkbox_wrap:
                cb_save_open_set.setChecked(!cb_save_open_set.isChecked());
                break;
        }
    }
}
